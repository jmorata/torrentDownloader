package com.jmorata.torrentDownloader.factory;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.service.DataWebReaderService;
import com.jmorata.torrentDownloader.service.EliteTorrentReaderService;
import com.jmorata.torrentDownloader.service.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class DataWebReaderFactory {

    private static final Logger logger = LoggerFactory.getLogger(DataWebReaderFactory.class);

    private static final String ELITE_TORRENT = "EliteTorrent";

    private static String urlStr;

    private static String categories;

    public static DataWebReaderService getInstance(PropertiesService propertiesService) throws TorrentDownloaderException {
        String engine = propertiesService.getProperty("torrent.engine");
        logger.info("Successfully configured engine: " + engine);

        urlStr = propertiesService.getProperty("torrent.url");
        categories = propertiesService.getProperty("torrent.categories");

        try {
            switch (engine) {
                case ELITE_TORRENT: {
                    return createEliteTorrentService();
                }

                default: {
                    throw new TorrentDownloaderException("DataWebReader config factory doesn't exists");
                }
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("DataWebReader config factory error", e);
        }
    }

    private static DataWebReaderService createEliteTorrentService() throws Exception {
        URL url = new URL(urlStr);

        return new EliteTorrentReaderService(url, categories);
    }

}
