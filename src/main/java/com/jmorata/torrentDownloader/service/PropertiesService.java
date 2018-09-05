package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;

import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertiesService {

    private Properties properties;

    public String getProperty(String property) throws TorrentDownloaderException {
        String value=properties.getProperty(property);
        if (value==null) {
            throw new TorrentDownloaderException("Error getting property "+property);
        }

        return value;
    }

    public PropertiesService(String configPath) throws TorrentDownloaderException {
        try {
            String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
            properties = new Properties();
            properties.load(new FileInputStream(rootPath + "/"+ configPath));

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error reading properties file "+configPath, e);
        }
    }

}
