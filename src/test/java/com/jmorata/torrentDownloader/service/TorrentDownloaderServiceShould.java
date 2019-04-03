package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.factory.DataWebReaderFactory;
import com.jmorata.torrentDownloader.repository.SynologyRepository;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TorrentDownloaderServiceShould {

    private static final String PROP_FILE = "torrentDownloader.properties";

    private static PropertiesService propertiesService;

    private TorrentDownloaderService torrentDownloaderService;

    private EliteTorrentReaderService eliteTorrentReaderService;

    @Before
    public void startUp() throws Exception {
        propertiesService = new PropertiesService(PROP_FILE);
        String downUrl = propertiesService.getProperty("torrent.url");
        String categoriesStr = propertiesService.getProperty("torrent.categories");
        eliteTorrentReaderService = new EliteTorrentReaderService(new URL(downUrl), categoriesStr);

        SynologyService synologyService = getSynologyService();
        torrentDownloaderService = getTorrentDownloaderService(synologyService);
    }

    private static SynologyService getSynologyService() throws TorrentDownloaderException {
        SynologyRepository synologyRepository = new SynologyRepository(propertiesService);
        return new SynologyService(synologyRepository);
    }

    private static TorrentDownloaderService getTorrentDownloaderService(SynologyService synologyService) throws TorrentDownloaderException {
        DataWebReaderService dataWebReaderService = DataWebReaderFactory.getInstance(propertiesService);
        return new TorrentDownloaderService(dataWebReaderService, synologyService, propertiesService);
    }

    @Test
    public void downloadTorrentFile() throws TorrentDownloaderException, IOException {
        File directory = deleteDirectoyContents();

        Set<Data> dataSet = eliteTorrentReaderService.buildDataSet();
        torrentDownloaderService.downloadTorrentFile(dataSet);

        int sizeDirectory = Objects.requireNonNull(directory.listFiles()).length;
        assertEquals(sizeDirectory, dataSet.size());
    }

    private File deleteDirectoyContents() throws TorrentDownloaderException, IOException {
        String dirIn = propertiesService.getProperty("dir.in");
        File directory = new File(dirIn + File.separatorChar + TorrentDownloaderService.torrentDir);
        FileUtils.deleteDirectory(directory);
        directory.mkdir();

        return directory;
    }

}