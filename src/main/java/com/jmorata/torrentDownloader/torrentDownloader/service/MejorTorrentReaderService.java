package com.jmorata.torrentDownloader.torrentDownloader.service;

import ch.qos.logback.classic.Logger;
import com.jmorata.torrentDownloader.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.torrentDownloader.exception.TorrentDownloaderException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class MejorTorrentReaderService extends DataWebReaderService {

//    private Logger logger = LoggerFactory.getLogger("MejorTorrentReaderService");

    @Override
    public Set<Data> buildDataSetFromURL(String url) throws TorrentDownloaderException {
        Set<Data> dataSet=new HashSet<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");

//            for (Element link : links) {
//                (" * a: <%s>  (%s)", link.attr("abs:href"), link.text());
//            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error processing "+url);
        }

        return dataSet;
    }

}
