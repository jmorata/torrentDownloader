package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TorrentSortDownloaderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String DEFAULT_CATEGORY = "Video Files";

    private SynologyService synologyService;

    private final String dirIn;

    private final String dirOut;

    public TorrentSortDownloaderService(SynologyService synologyService, PropertiesService propertiesService) throws TorrentDownloaderException {
        this.synologyService = synologyService;

        dirIn = propertiesService.getProperty("dir.in");
        dirOut = propertiesService.getProperty("dir.out");
    }

    public void execute() throws TorrentDownloaderException {

        String regex = "(.+(\\.(?i)(mp4|avi|mkv))$)";
        String zipRegex = "(.+(\\.(?i)(rar|zip))$)";

        try {
            File incoming = new File(dirIn);
            for (File inFile : incoming.listFiles()) {

                // video files
                if (inFile.isFile() && inFile.getName().matches(regex)) {
                    String category = DEFAULT_CATEGORY;
                    String directory = genCatDir(category);
                    String fileName = inFile.getName();

                    createFile(fileName, category, directory, inFile);
                }

                // inside directories
                else if (inFile.isDirectory()) {
                    String dirFileName;
                    if (inFile.getName().contains("[")) {
                        dirFileName = inFile.getName().substring(0, inFile.getName().indexOf("[") - 1);
                    } else {
                        dirFileName = inFile.getName();
                    }

                    // check category
                    String category = synologyService.getCategory(dirFileName);
                    if (category != null) {

                        // create dest dir
                        String directory = genCatDir(category);

                        // zip/rar files
                        String zipFileName = null;
                        for (File file : inFile.listFiles()) {
                            if (file.getName().matches(zipRegex)) {
                                zipFileName = file.getName();
                                break;
                            }
                        }

                        if (zipFileName != null) {
                            logger.warn("Detected zipped file: " + zipFileName);
                            continue;
                        }

                        // move file
                        for (File file : inFile.listFiles()) {
                            if (file.getName().matches(regex)) {
                                createFile(dirFileName, category, directory, file);

                            } else {
                                file.delete();
                            }
                        }

                        // delete dir
                        inFile.delete();
                    }
                }
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("There is an error sorting finished downloads", e);
        }
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
        if (!destDir.exists() && !destDir.mkdirs()) {
            logger.warn("You need filesystem grants to perform operation: " + destDir.getAbsolutePath());
        }

        return directory;
    }

}
