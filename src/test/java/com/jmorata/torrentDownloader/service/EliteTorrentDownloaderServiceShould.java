package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.factory.DataWebReaderFactory;
import com.jmorata.torrentDownloader.repository.SynologyRepository;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class EliteTorrentDownloaderServiceShould {

    private static final String PROP_FILE = "torrentDownloader.properties";

    private static PropertiesService propertiesService;

    private TorrentDownloaderService torrentDownloaderService;

    private EliteTorrentReaderService eliteTorrentReaderService;

    @Mock
    private SynologyService synologyService;

    @Before
    public void startUp() throws Exception {
        propertiesService = new PropertiesService(PROP_FILE);
        String downUrl = propertiesService.getProperty("torrent.url");
        String categoriesStr = propertiesService.getProperty("torrent.categories");

        eliteTorrentReaderService = new EliteTorrentReaderService(new URL(downUrl), categoriesStr);

        DataWebReaderService dataWebReaderService = DataWebReaderFactory.getInstance(propertiesService);
        torrentDownloaderService = new EliteTorrentDownloaderService(dataWebReaderService, synologyService, propertiesService);
    }

    @Test
    public void downloadTorrentFile() throws TorrentDownloaderException, IOException {
        File directory = deleteDirectoryContents();

        Set<Data> dataSet = eliteTorrentReaderService.buildDataSet().stream().limit(1).collect(Collectors.toSet());
        torrentDownloaderService.downloadTorrentFile(dataSet);

        int sizeDirectory = Objects.requireNonNull(directory.listFiles()).length;
        assertEquals(sizeDirectory, 1);
    }

    private File deleteDirectoryContents() throws TorrentDownloaderException, IOException {
        String dirIn = propertiesService.getProperty("dir.in");
        File directory = new File(dirIn + File.separatorChar + TorrentDownloaderService.torrentDir);
        FileUtils.deleteDirectory(directory);
        directory.mkdir();

        return directory;
    }

}