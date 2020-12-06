package com.jmorata.torrentDownloader;

import com.jmorata.torrentDownloader.service.PropertiesService;
import com.jmorata.torrentDownloader.service.SynologyService;
import com.jmorata.torrentDownloader.config.QuartzConfig;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.repository.SynologyRepository;
import fi.iki.elonen.SimpleWebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final String PROP_FILE = "torrentDownloader.properties";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static PropertiesService propertiesService;

    public static void main(String[] args) {

        Package mainPackage = Main.class.getPackage();
        logger.info("TorrentDownloader v"+mainPackage.getImplementationVersion()+" (c) jmorata");

        try {
            propertiesService = new PropertiesService(PROP_FILE);

            startQuartzConfig();
            launchNanoWebServer();

        } catch (TorrentDownloaderException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private static void startQuartzConfig() throws TorrentDownloaderException {
        SynologyService synologyService = getSynologyService();
        QuartzConfig quartzConfig = new QuartzConfig(synologyService, propertiesService);
        quartzConfig.printJobNames();
    }


    private static SynologyService getSynologyService() throws TorrentDownloaderException {
        SynologyRepository synologyRepository = new SynologyRepository(propertiesService);
        return new SynologyService(synologyRepository);
    }

    private static void launchNanoWebServer() throws TorrentDownloaderException {
        final String host = propertiesService.getProperty("nano.host");
        final String port = propertiesService.getProperty("nano.port");
        final String dirIn = propertiesService.getProperty("dir.in");

        String[] args = {"--host", host, "--port", port, "--dir", dirIn};
        SimpleWebServer.main(args);
    }

}
