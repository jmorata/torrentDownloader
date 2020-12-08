package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TorrentSortDownloaderServiceShould {

    private static final String PROP_FILE = "torrentDownloader.properties";

    private TorrentSortDownloaderService torrentSortDownloaderService;

    @Mock
    private SynologyService synologyService;

    private static String dirIn;
    private static String dirOut;
    private static String outDummyDir;

    private static final String dummyName = "dummy";

    @Before
    public void startUp() throws TorrentDownloaderException {
        PropertiesService propertiesService = new PropertiesService(PROP_FILE);
        dirIn = propertiesService.getProperty("dir.in");
        dirOut = propertiesService.getProperty("dir.out");

        String defaultCategory = propertiesService.getProperty("torrent.categories").split(",")[0];
        outDummyDir = dirOut + File.separatorChar + defaultCategory;

        torrentSortDownloaderService = new TorrentSortDownloaderService(synologyService, propertiesService);
        when(synologyService.getTitle(any())).thenReturn(dummyName);
    }

    @Test
    public void one_file_to_sort() throws TorrentDownloaderException, IOException {
        cleanDirectories();
        File dummyMKVFile = genDummyMKVFile();
        torrentSortDownloaderService.execute();

        Assert.assertTrue(existsOutDummyDir(dummyMKVFile));
    }

    private boolean existsOutDummyDir(File dummyMKVFile) {
        File dummyFile = new File(outDummyDir + File.separatorChar + dummyName+ ".mkv");
        return dummyFile.exists();
    }

    private void cleanDirectories() throws IOException {
        FileUtils.deleteDirectory(new File(dirIn));
        FileUtils.deleteDirectory(new File(dirOut));
        (new File(dirIn)).mkdir();
        (new File(dirOut)).mkdir();
    }

    private File genDummyMKVFile() throws IOException {
        File dummyFile = new File(dirIn + File.separatorChar + "dummy_" + System.currentTimeMillis() + ".mkv");
        dummyFile.createNewFile();
        return dummyFile;
    }

}