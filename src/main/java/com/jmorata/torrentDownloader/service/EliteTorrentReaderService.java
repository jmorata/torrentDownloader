package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class EliteTorrentReaderService extends DataWebReaderService {

    private static final String DOWNLOAD_TORRENT = "Descargar el .torrent";
    private static final String NOT_FOUND_LINK = "not found" ;

    public EliteTorrentReaderService(URL url, String categoriesStr) {
        super(url, categoriesStr);
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
        if (linkHref.toUpperCase().contains(category.toUpperCase())) {
            String torrentLink = getTorrentLink(linkHref);

            if (!NOT_FOUND_LINK.equals(torrentLink)) {
                Data data = newData(linkHref, linkTitle, category, torrentLink);
                dataSet.add(data);
            }
        }
    }

    private Data newData(String linkHref, String linkTitle, String category, String torrentLink) {
        return Data.builder()
                            .category(category)
                            .title(linkTitle)
                            .link(linkHref)
                            .torrentLink(getHost(torrentLink))
                            .torrent(getTorrent(torrentLink))
                            .build();
    }

    private String getTorrentLink(String linkHref) throws TorrentDownloaderException {
        try {
            URL url = new URL(linkHref);
            Document doc = Jsoup.connect(url.toString()).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String text = link.text();
                if (DOWNLOAD_TORRENT.equalsIgnoreCase(text)) {
                    return link.attr("href");
                }
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error processing torrent link" + linkHref, e);
        }

        return NOT_FOUND_LINK;
    }

}
