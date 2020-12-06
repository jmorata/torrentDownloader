package com.jmorata.torrentDownloader.factory;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.service.*;

public class TorrentDownloaderFactory {

    private static final String ELITE_TORRENT = "EliteTorrent";

    public static TorrentDownloaderService getInstance(DataWebReaderService dataWebReaderService, SynologyService synologyService, PropertiesService propertiesService) throws TorrentDownloaderException {
        String engine = propertiesService.getProperty("torrent.engine");

        try {
            switch (engine) {
                case ELITE_TORRENT: {
                    return new EliteTorrentDownloaderService(dataWebReaderService, synologyService, propertiesService);
                }

                default: {
                    throw new TorrentDownloaderException("TorrentDownloader config factory doesn't exists");
                }
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("TorrentDownloader config factory error", e);
        }
    }

}
