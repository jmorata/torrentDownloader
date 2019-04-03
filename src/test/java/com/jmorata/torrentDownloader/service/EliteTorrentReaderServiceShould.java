package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Set;

import static org.junit.Assert.assertFalse;

public class EliteTorrentReaderServiceShould {

    private static final String PROP_FILE = "torrentDownloader.properties";

    private static PropertiesService propertiesService;

    private EliteTorrentReaderService eliteTorrentReaderService;

    @Before
    public void startUp() throws Exception {
        propertiesService = new PropertiesService(PROP_FILE);
        String downUrl = propertiesService.getProperty("torrent.url");
        String categoriesStr = propertiesService.getProperty("torrent.categories");
        eliteTorrentReaderService = new EliteTorrentReaderService(new URL(downUrl), categoriesStr);
    }

    @Test
    public void buildDataSetFromStreamTest() throws TorrentDownloaderException {
        Set<Data> dataSet = eliteTorrentReaderService.buildDataSet();
        assertFalse(dataSet.isEmpty());
    }

}
