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

public class MejorTorrentReaderService extends DataWebReaderService {

    public MejorTorrentReaderService(URL url, Set<String> categories) {
        super(url, categories);
    }

    @Override
    public Set<Data> buildDataSet() throws TorrentDownloaderException {
        Set<Data> dataSet = new HashSet<>();

        try {
            Document doc = Jsoup.connect(url.toString()).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String linkHref = link.attr("href");
                if (linkHref.contains("descargar-torrent")) {
                    addDataByCategories(dataSet, link);
                }
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error processing " + url);
        }

        return dataSet;
    }

    private void addDataByCategories(Set<Data> dataSet, Element link) {
        Element torrentTypeElement = link.nextElementSibling();
        String torrentType = torrentTypeElement.text();
        String linkHref = link.attr("href");
        String linkText = link.text();

        for (String category: categories) {
            if (torrentType.contains(category)) {

                // TODO: complete data object
                Data data = Data.builder()
                        .category(category)
                        .title(linkText)
                        .torrentLink(linkHref)
                        .build();
                dataSet.add(data);
            }
        }
    }

}
