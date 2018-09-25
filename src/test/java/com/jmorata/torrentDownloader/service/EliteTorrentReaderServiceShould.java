package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.service.EliteTorrentReaderService;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Set;

import static org.junit.Assert.assertFalse;

public class EliteTorrentReaderServiceShould {

    private EliteTorrentReaderService eliteTorrentReaderService;

    @Before
    public void startUp() throws Exception {
        String downUrl = "https://www.elitetorrent.biz/peliculas-8";
        String categoriesStr = "1080p-castellano";
        URL url = new URL(downUrl);

        eliteTorrentReaderService = new EliteTorrentReaderService(url, categoriesStr);
    }

    @Test
    public void buildDataSetFromStreamTest() throws TorrentDownloaderException {
        Set<Data> dataSet = eliteTorrentReaderService.buildDataSet();
        assertFalse(dataSet.isEmpty());
    }

}
