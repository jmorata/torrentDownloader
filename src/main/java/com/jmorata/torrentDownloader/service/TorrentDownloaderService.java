package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class TorrentDownloaderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataWebReaderService dataWebReaderService;

    private final SynologyService synologyService;

    public TorrentDownloaderService(DataWebReaderService dataWebReaderService, SynologyService synologyService) {
        this.dataWebReaderService = dataWebReaderService;
        this.synologyService = synologyService;
    }

    public void execute() throws TorrentDownloaderException {
        if (synologyService.isConnected()) {
            Set<Data> data = dataWebReaderService.buildDataSet();
            data = synologyService.checkDataSet(data);
            synologyService.persistDownloaderDataSet(data);
            synologyService.persistTorrentDataSet(data);

            if (data.isEmpty()) {
                logger.info("There isn't updates");
            } else {
                for (Data rssData : data) {
                    logger.info(rssData.getCategory() + " found: " + rssData.getTitle());
                }
            }
        }
    }

}
