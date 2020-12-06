package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public abstract class TorrentDownloaderService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public final static String torrentDir = "torrent";

    protected final DataWebReaderService dataWebReaderService;

    protected final SynologyService synologyService;

    protected final String dirIn;

    protected final String nanoHost;

    protected final String nanoPort;

    public TorrentDownloaderService(DataWebReaderService dataWebReaderService, SynologyService synologyService, PropertiesService propertiesService) throws TorrentDownloaderException {
        this.dataWebReaderService = dataWebReaderService;
        this.synologyService = synologyService;

        dirIn = propertiesService.getProperty("dir.in");
        nanoHost = propertiesService.getProperty("nano.host");
        nanoPort = propertiesService.getProperty("nano.port");

        createTorrentDirectory();
    }

    public void execute() throws TorrentDownloaderException {
        if (synologyService.isConnected()) {
            Set<Data> data = dataWebReaderService.buildDataSet();
            downloadTorrentFile(data);
            data = synologyService.checkDataSet(data);
            synologyService.persistDownloaderDataSet(data);
            synologyService.persistTorrentDataSet(data);

            if (data.isEmpty()) {
                logger.info("There isn't updates");
            } else {
                for (Data rssData : data) {
                    logger.info(rssData.getCategory() + " found: " + rssData.getTitle());
                }
            }
        }
    }

    protected void createTorrentDirectory() throws TorrentDownloaderException {
        File destDir = new File(dirIn + File.separatorChar + torrentDir);
        if (!destDir.mkdirs() && !destDir.exists()) {
            throw new TorrentDownloaderException("You need filesystem grants to perform operation: " + destDir.getAbsolutePath());
        }
    }

    public void downloadTorrentFile(Set<Data> dataSet) {
        Iterator<Data> it = dataSet.iterator();
        while (it.hasNext()) {
            Data data = it.next();
            try {
                getBinaryFile(data);
                setLocalTorrent(data);

            } catch (Exception e) {
                it.remove();
                logger.error(data.getTorrent() + " can't be downloaded !", e);
            }
        }
    }

    protected void setLocalTorrent(Data data) {
        String localHost = "http://" + nanoHost + ":" + nanoPort + "/" + torrentDir + "/";
        data.setTorrentLink(localHost + data.getTorrent());
    }

    protected void getBinaryFile(Data data) throws IOException {
        String urlStr = data.getTorrentLink();
        Connection.Response r = Jsoup.connect(urlStr)
                .ignoreContentType(true)
                .execute();

        File fileTorrent = new File(dirIn + File.separatorChar + torrentDir + File.separatorChar + data.getTorrent());
        FileOutputStream out = new FileOutputStream(fileTorrent);
        out.write(r.bodyAsBytes());
        out.close();
    }

}
