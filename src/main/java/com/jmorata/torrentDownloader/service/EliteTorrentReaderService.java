package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EliteTorrentReaderService extends DataWebReaderService {

    private static final String DESCARGAR_EL_TORRENT = "Descargar el .torrent";

    public EliteTorrentReaderService(URL url, Set<String> categories) {
        super(url, categories);
    }

    @Override
    public Set<Data> buildDataSet() throws TorrentDownloaderException {
        Set<Data> dataSet = new HashSet<>();

        try {
            Document doc = Jsoup.connect(url.toString()).get();
            Elements links = doc.select("a[class]");

            for (Element link : links) {
                addDataByCategories(dataSet, link);
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error processing url " + url, e);
        }

        return dataSet;
    }

    private void addDataByCategories(Set<Data> dataSet, Element link) throws TorrentDownloaderException {
        String linkHref = link.attr("href");
        String linkTitle = link.attr("title");

        for (String category : categories) {
            addDataObject(dataSet, linkHref, linkTitle, category);
        }
    }

    private void addDataObject(Set<Data> dataSet, String linkHref, String linkTitle, String category) throws TorrentDownloaderException {
        if (linkHref.contains("-" + category + "-")) {
            String torrentLink = getTorrentLink(linkHref);
            if (!torrentLink.isEmpty()) {

                // TODO: complete data object
                Data data = Data.builder()
                        .category(category)
                        .title(linkTitle)
                        .link(linkHref)
                        .torrentLink(getHost(torrentLink))
                        .torrent(getTorrent(torrentLink))
                        .build();
                dataSet.add(data);
            }
        }
    }

    private String getTorrent(String torrentLink) {
        return torrentLink.substring(torrentLink.lastIndexOf("/") + 1);
    }

    private String getTorrentLink(String linkHref) throws TorrentDownloaderException {
        try {
            URL url = new URL(linkHref);
            Document doc = Jsoup.connect(url.toString()).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String text = link.text();
                if (DESCARGAR_EL_TORRENT.equalsIgnoreCase(text)) {
                    return link.attr("href");
                }
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error processing torrent link" + linkHref, e);
        }

        return "";
    }

    private String getHost(String torrentLink) {
        if (!torrentLink.startsWith("http")) {
            torrentLink = url.getProtocol() + "://" + url.getHost() + torrentLink;
        }

        return torrentLink;
    }

}
