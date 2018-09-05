package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.repository.SynologyRepository;

import java.util.Set;

public class SynologyService {

    private SynologyRepository synologyRepository;

    public SynologyService(SynologyRepository synologyRepository) {
        this.synologyRepository = synologyRepository;
    }

    public void persistTorrentDataSet(Set<Data> data) throws TorrentDownloaderException {
        try {
            this.synologyRepository.persistTorrentDataSet(data);

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error persisting torrent data", e);
        }
    }

    public void persistDownloaderDataSet(Set<Data> data) throws TorrentDownloaderException {
        try {
            this.synologyRepository.persistDownloaderDataSet(data);

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error persisting synology data", e);
        }
    }

    public void checkDataSet(Set<Data> data) throws TorrentDownloaderException {
        try {
            this.synologyRepository.checkTorrentDataSet(data);

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error checking data", e);
        }
    }

    public String getCategory(String title) throws TorrentDownloaderException {
        try {
            return this.synologyRepository.getCategory(title);

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error getting category data", e);
        }
    }

    public Boolean isConnected() {
        return this.synologyRepository.isConnected();
    }

}
