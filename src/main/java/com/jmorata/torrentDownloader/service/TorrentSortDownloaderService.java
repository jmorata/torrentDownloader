package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TorrentSortDownloaderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String defaultCategory;

    private SynologyService synologyService;

    private final String dirIn;

    private final String dirOut;

    private final static String regex = "(.+(\\.(?i)(mp4|avi|mkv))$)";

    public TorrentSortDownloaderService(SynologyService synologyService, PropertiesService propertiesService) throws TorrentDownloaderException {
        this.synologyService = synologyService;

        defaultCategory = propertiesService.getProperty("torrent.categories").split(",")[0];
        dirIn = propertiesService.getProperty("dir.in");
        dirOut = propertiesService.getProperty("dir.out");
    }

    public void execute() throws TorrentDownloaderException {

        try {
            File incoming = new File(dirIn);
            for (File inFile : incoming.listFiles()) {

                if (inFile.isFile() && inFile.getName().matches(regex)) {
                    String category = defaultCategory;
                    String directory = genCatDir(category);
                    String fileName = inFile.getName();

                    createFile(fileName, category, directory, inFile);
                }

                else if (isTorrentDir(inFile)) {

                    String dirFileName= inFile.getName();
                    if (dirFileName.contains("[")) {
                        dirFileName = dirFileName.substring(0, dirFileName.indexOf("[") - 1);
                    }

                    String category = synologyService.getCategory(dirFileName);
                    if (category == null) {
                        category = defaultCategory;
                    }

                    String directory = genCatDir(category);
                    for (File file : inFile.listFiles()) {
                        if (file.getName().matches(regex)) {
                            createFile(dirFileName, category, directory, file);

                        } else {
                            file.delete();
                        }
                    }

                    inFile.delete();
                }
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("There is an error sorting finished downloads", e);
        }
    }

    private boolean isTorrentDir(File inFile) {
        if (inFile.isDirectory()) {
            for (File file : inFile.listFiles()) {
                if (file.getName().matches(regex)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void createFile(String dirFileName, String category, String directory, File file) {
        String extension = file.getName().substring(file.getName().lastIndexOf("."));
        Integer it = 0;
        while (true) {
            String fileName = dirFileName + (it > 0 ? "(" + it + ")" : "") + extension;
            File newFile = new File(directory + File.separatorChar + fileName);
            if (!newFile.exists()) {
                try {
                    logger.info("Moving file " + fileName + " to category: " + category);
                    FileUtils.moveFile(file, newFile);

                } catch (Exception e) {
                    logger.warn("You need filesystem grants to perform operation: " + newFile.getAbsolutePath(), e);
                }
                break;
            }

            it++;
        }
    }

    private String genCatDir(String category) {
        String directory = dirOut + File.separatorChar + category;
        File destDir = new File(directory);
        if (!destDir.mkdirs() && !destDir.exists()) {
            logger.warn("You need filesystem grants to perform operation: " + destDir.getAbsolutePath());
        }

        return directory;
    }

}
