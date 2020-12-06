package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class DataWebReaderService {

    protected URL url;

    protected Set<String> categories;

    public DataWebReaderService(URL url, String categoriesStr) {
        this.url = url;
        this.categories = new HashSet<>(Arrays.asList(categoriesStr.split(",")));
    }

    public abstract Set<Data> buildDataSet() throws TorrentDownloaderException;

    protected String getHost(String link) {
        if (!link.startsWith("http")) {
            link = url.getProtocol() + "://" + url.getHost() + link;
        }

        return link;
    }

    protected String getTorrent(String torrentLink) {
        torrentLink = torrentLink.substring(0, torrentLink.lastIndexOf("/"));
        return torrentLink.substring(torrentLink.lastIndexOf("/") + 1)+".torrent";
    }

}
