package com.jmorata.torrentDownloader.service;

public class TorrentDownloaderService {

    private final DataWebReaderService dataWebReaderService;
    private final SynologyService synologyService;

    public TorrentDownloaderService(DataWebReaderService dataWebReaderService, SynologyService synologyService) {
        this.dataWebReaderService = dataWebReaderService;
        this.synologyService = synologyService;
    }

    public void run() {

    }

}
