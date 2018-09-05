package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.domain.Data;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class DataWebReaderService {

    protected URL url;

    protected Set<String> categories;

    public DataWebReaderService(URL url, String categoriesStr) {
        this.url = url;
        this.categories = new HashSet<String> (Arrays.asList(categoriesStr.split(",")));
    }

    public abstract Set<Data> buildDataSet() throws TorrentDownloaderException;

    protected String getHost(String torrentLink) {
        if (!torrentLink.startsWith("http")) {
            torrentLink = url.getProtocol() + "://" + url.getHost() + torrentLink;
        }

        return torrentLink;
    }

    protected String getTorrent(String torrentLink) {
        return torrentLink.substring(torrentLink.lastIndexOf("/") + 1);
    }

}
