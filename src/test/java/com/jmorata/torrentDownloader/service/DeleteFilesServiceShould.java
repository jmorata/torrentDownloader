package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Objects;

public class DeleteFilesServiceShould {

    private static final String PROP_FILE = "torrentDownloader.properties";

    private DeleteFilesService deleteFilesService;

    private static String dirIn;
    private static String dirOut;
    private static String outDummyDir;
    private static String inDummyDir;

    @Before
    public void startUp() throws TorrentDownloaderException {
        PropertiesService propertiesService = new PropertiesService(PROP_FILE);
        deleteFilesService = new DeleteFilesService(propertiesService);

        dirIn = propertiesService.getProperty("dir.in");
        dirOut = propertiesService.getProperty("dir.out");

        String defaultCategory = propertiesService.getProperty("torrent.categories").split(",")[0];
        outDummyDir = dirOut + File.separatorChar + defaultCategory;
        inDummyDir = dirIn + File.separatorChar + TorrentDownloaderService.torrentDir;
    }

    @Test
    public void empty_directories_test() throws TorrentDownloaderException, IOException {
        cleanDirectories();
        deleteFilesService.execute();
    }

    @Test
    public void dirOut_directory_with_new_file_test() throws TorrentDownloaderException, IOException {
        cleanDirectories();

        (new File(outDummyDir)).mkdir();
        genDummyMKVFile();
        deleteFilesService.execute();

        Assert.assertEquals(1, (int) countFilesIn(outDummyDir));
    }

    @Test
    public void dirOut_directory_with_old_file_test() throws TorrentDownloaderException, IOException {
        cleanDirectories();

        (new File(outDummyDir)).mkdir();
        File dummyFile=genDummyMKVFile();
        changeCreationTimeToPast(dummyFile);
        genDummyMKVFile();

        deleteFilesService.execute();

        Assert.assertEquals(1, (int) countFilesIn(outDummyDir));
    }

    @Test
    public void torrent_directory_with_new_file_test() throws TorrentDownloaderException, IOException {
        cleanDirectories();

        (new File(inDummyDir)).mkdir();
        genDummyTorrentFile();
        deleteFilesService.execute();

        Assert.assertEquals(1, (int) countFilesIn(inDummyDir));
    }

    @Test
    public void torrent_directory_with_old_file_test() throws TorrentDownloaderException, IOException {
        cleanDirectories();

        (new File(inDummyDir)).mkdir();
        File dummyFile=genDummyTorrentFile();
        changeCreationTimeToPast(dummyFile);
        genDummyTorrentFile();

        deleteFilesService.execute();

        Assert.assertEquals(1, (int) countFilesIn(inDummyDir));
    }

    private void changeCreationTimeToPast(File dummyFile) throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);

        Path path = Paths.get(dummyFile.getAbsolutePath());
        Files.setAttribute(path, "creationTime", FileTime.fromMillis(calendar.getTimeInMillis()));
        try {
            Thread.sleep(1000); // windows file refresh
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private File genDummyTorrentFile() throws IOException {
        File dummyFile = new File(inDummyDir + File.separatorChar + "dummy_" + System.currentTimeMillis() + ".torrent");
        dummyFile.createNewFile();
        return dummyFile;
    }

    private File genDummyMKVFile() throws IOException {
        File dummyFile = new File(outDummyDir + File.separatorChar + "dummy_" + System.currentTimeMillis() + ".mkv");
        dummyFile.createNewFile();
        return dummyFile;
    }

    private void cleanDirectories() throws IOException {
        FileUtils.deleteDirectory(new File(dirIn));
        FileUtils.deleteDirectory(new File(dirOut));
        (new File(dirIn)).mkdir();
        (new File(dirOut)).mkdir();
    }

    private Integer countFilesIn(String dummyDir) {
        File dummyDirFile = new File(dummyDir);
        return Objects.requireNonNull(dummyDirFile.listFiles()).length;
    }

}