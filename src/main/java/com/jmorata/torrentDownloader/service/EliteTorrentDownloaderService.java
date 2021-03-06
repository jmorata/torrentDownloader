package com.jmorata.torrentDownloader.service;

import be.christophedetroyer.torrent.Torrent;
import be.christophedetroyer.torrent.TorrentParser;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

public class EliteTorrentDownloaderService extends TorrentDownloaderService {
    public EliteTorrentDownloaderService(DataWebReaderService dataWebReaderService, SynologyService synologyService, PropertiesService propertiesService) throws TorrentDownloaderException {
        super(dataWebReaderService, synologyService, propertiesService);
    }

    private final String ELITETORRENT = "elitetorrent_com_torrent";

    @Override
    protected void getBinaryFile(Data data) throws IOException {
        String urlStr = data.getTorrentLink();

        final WebClient webClient = new WebClient();
        final HtmlPage page = webClient.getPage(urlStr);
        final DomElement button = page.getElementById(ELITETORRENT);

        File fileTorrent = createTorrent(data, button);
        data.setName(getTorrentName(fileTorrent));
        webClient.close();
    }

    private String getTorrentName(File fileTorrent) throws IOException {
        Torrent torrent = TorrentParser.parseTorrent(fileTorrent.getPath());
        return torrent.getName();
    }

    private File createTorrent(Data data, DomElement button) throws IOException {
        try {
            InputStream contentAsStream = button.click().getWebResponse().getContentAsStream();
            File fileTorrent = new File(dirIn + File.separatorChar + torrentDir + File.separatorChar + data.getTorrent());
            java.nio.file.Files.copy(
                    contentAsStream,
                    fileTorrent.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            return fileTorrent;

        } catch (IOException e) {
            throw new IOException("Error when retrieve torrent content: " + data.getTorrentLink(), e.getCause());
        }
    }

}
