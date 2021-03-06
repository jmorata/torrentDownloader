package com.jmorata.torrentDownloader.service;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class EliteTorrentReaderService extends DataWebReaderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String NOT_FOUND_LINK = "not found";

    public EliteTorrentReaderService(URL url, String categoriesStr) {
        super(url, categoriesStr);
    }

    @Override
    public Set<Data> buildDataSet() throws TorrentDownloaderException {
        Set<Data> dataSet = new HashSet<>();
        Elements links;

        try {
            Document doc = Jsoup.connect(url.toString()).get();
            links = doc.select("a[class]");

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error processing url " + url, e);
        }

        for (Element link : links) {
            try {
                addDataByCategories(dataSet, link);

            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
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
        String torrentLink = getTorrentLink(linkHref, category);

        if (!NOT_FOUND_LINK.equals(torrentLink)) {
            Data data = newData(linkHref, linkTitle, category, torrentLink);
            dataSet.add(data);
        }
    }

    private Data newData(String linkHref, String linkTitle, String category, String torrentLink) {
        return Data.builder()
                .category(category)
                .title(linkTitle)
                .link(linkHref)
                .torrentLink(getHost(linkHref) + torrentLink)
                .torrent(getTorrent(linkHref))
                .build();
    }

    private String getTorrentLink(String linkHref, String category) throws TorrentDownloaderException {
        try {
            URL url = new URL(linkHref);
            Document doc = Jsoup.connect(url.toString()).get();

            if (checkCategory(doc, category)) {
                return "#";
            }

            return NOT_FOUND_LINK;

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error processing torrent link: " + linkHref, e);
        }
    }

    private boolean checkCategory(Document doc, String category) {
        Elements spans = doc.select("span");
        for (Element span : spans) {
            if (span.text().toUpperCase().contains(category.toUpperCase())) {
                return true;
            }
        }

        return false;
    }

}
