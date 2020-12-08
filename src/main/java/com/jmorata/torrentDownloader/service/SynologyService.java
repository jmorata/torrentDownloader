package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.adapter.DataAdapter;
import com.jmorata.torrentDownloader.entity.DataEntity;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.repository.SynologyRepository;

import java.util.Set;

public class SynologyService {

    private SynologyRepository synologyRepository;

    public SynologyService(SynologyRepository synologyRepository) {
        this.synologyRepository = synologyRepository;
    }

    public void persistTorrentDataSet(Set<Data> data) throws TorrentDownloaderException {
        try {
            Set<DataEntity> dataEntitySet=DataAdapter.getDataEntitySet(data);
            this.synologyRepository.persistTorrentDataSet(dataEntitySet);

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error persisting torrent data", e);
        }
    }

    public void persistDownloaderDataSet(Set<Data> data) throws TorrentDownloaderException {
        try {
            Set<DataEntity> dataEntitySet=DataAdapter.getDataEntitySet(data);
            this.synologyRepository.persistDownloaderDataSet(dataEntitySet);

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error persisting synology data", e);
        }
    }

    public Set<Data> checkDataSet(Set<Data> data) throws TorrentDownloaderException {
        try {
            Set<DataEntity> dataEntitySet=DataAdapter.getDataEntitySet(data);
            this.synologyRepository.checkTorrentDataSet(dataEntitySet);

            return DataAdapter.getDataSet(dataEntitySet);

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

    public String getTitle(String name) throws TorrentDownloaderException {
        try {
            return this.synologyRepository.getTitle(name);

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error getting title data", e);
        }
    }

    public Boolean isConnected() {
        return this.synologyRepository.isConnected();
    }

}
