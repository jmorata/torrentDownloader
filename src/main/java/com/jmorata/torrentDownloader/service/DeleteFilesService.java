package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DeleteFilesService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Integer days;
    private final String dirIn;
    private final int deleteTime;
    private final Set<String> categories;

    public DeleteFilesService(PropertiesService propertiesService) throws TorrentDownloaderException {
        String categoriesStr = propertiesService.getProperty("torrent.categories");
        this.categories = new HashSet<>(Arrays.asList(categoriesStr.split(",")));
        this.days = new Integer(propertiesService.getProperty("delete.days"));
        this.dirIn = propertiesService.getProperty("dir.in");

        this.deleteTime = 1000 * 60 * 60 * 24 * days;
    }

    public void execute() throws TorrentDownloaderException {
        if (days > 0) {
            try {
                deleteOldCategories();
                deleteOldTorrents();

            } catch (Exception e) {
                throw new TorrentDownloaderException("Error deleting old files", e);
            }
        }
    }

    private void deleteOldCategories() throws IOException {
        for (String category : categories) {
            File directory = new File(dirIn + File.separatorChar + category);
            deleteOldDirectory(directory);
        }
    }

    private void deleteOldTorrents() throws IOException {
        File directory = new File(dirIn + File.separatorChar + TorrentDownloaderService.torrentDir);
        deleteOldDirectory(directory);
    }

    private void deleteOldDirectory(File directory) throws IOException {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                deleteOldFile(file);
            }
        }
    }

    private void deleteOldFile(File file) throws IOException {
        long creationTime = getCreationTime(file);
        long nowTime = System.currentTimeMillis();

        if (creationTime + deleteTime >= nowTime) {
            file.delete();
            logger.info("Deleted " + days + " days file: " + file.getName());
        }
    }

    private long getCreationTime(File file) throws IOException {
        Path filePath = file.toPath();
        BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
        return attributes.creationTime().to(TimeUnit.MILLISECONDS);
    }

}
