package com.jmorata.torrentDownloader;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.service.MejorTorrentReaderService;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;


public class MejorTorrentReaderServiceShould {

    private MejorTorrentReaderService mejorTorrentReaderService;

    @Before
    public void startUp() throws MalformedURLException {
        URL url=new URL("http://www.mejortorrent.com/secciones.php?sec=ultimos_torrents");
        Set<String> categories=new HashSet<>();
        categories.add("MicroHD");

        mejorTorrentReaderService=new MejorTorrentReaderService(url, categories);
    }
    
    @Test
    public void buildDataSetFromStreamTest() throws TorrentDownloaderException {
        Set<Data> dataSet=mejorTorrentReaderService.buildDataSet();
        assertFalse(dataSet.isEmpty());
    }

}
