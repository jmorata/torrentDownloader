package com.jmorata.torrentDownloader.torrentDownloader.service;

import com.jmorata.torrentDownloader.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.torrentDownloader.exception.TorrentDownloaderException;

import java.io.InputStream;
import java.util.Set;

public abstract class DataWebReaderService {

    public abstract Set<Data> buildDataSetFromURL(String url) throws TorrentDownloaderException;

}
