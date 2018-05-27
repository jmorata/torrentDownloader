package com.jmorata.torrentDownloader;

import com.jmorata.torrentDownloader.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.torrentDownloader.service.MejorTorrentReaderService;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

public class MejorTorrentReaderServiceShould {

    private MejorTorrentReaderService mejorTorrentReaderService;

    @Before
    public void startUp() {
        mejorTorrentReaderService=new MejorTorrentReaderService();
    }
    
    @Test(expected = TorrentDownloaderException.class)
    public void buildDataSetFromStreamTest() {
        String url="http://www.mejortorrent.com/secciones.php?sec=ultimos_torrents";
        Set<Data> dataSet=mejorTorrentReaderService.buildDataSetFromURL(url);
    }

}
