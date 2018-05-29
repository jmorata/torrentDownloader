package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.domain.Data;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.List;
import java.util.Set;

public abstract class DataWebReaderService {

    protected URL url;

    protected Set<String> categories;

    public DataWebReaderService(URL url, Set<String> categories) {
        this.url = url;
        this.categories = categories;
    }

    public abstract Set<Data> buildDataSet() throws TorrentDownloaderException;
}
