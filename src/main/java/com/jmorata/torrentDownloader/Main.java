package com.jmorata.torrentDownloader;

import com.jmorata.torrentDownloader.job.TorrentDownloaderJob;
import com.jmorata.torrentDownloader.job.TorrentSortDownloaderJob;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.factory.DataWebReaderFactory;
import com.jmorata.torrentDownloader.quartz.QuartzUtils;
import com.jmorata.torrentDownloader.repository.SynologyRepository;
import com.jmorata.torrentDownloader.service.*;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final String PROP_FILE = "torrentDownloader.properties";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static PropertiesService propertiesService;

    private static SynologyService synologyService;

    public static void main(String[] args) throws TorrentDownloaderException {
        logger.info("TorrentDownloader v2.0 (c) jmorata");

        try {
            propertiesService = new PropertiesService(PROP_FILE);
            synologyService = getSynologyService();

            createTorrentDownloaderJob();
            createTorrentSortDownloaderJob();

        } catch (Exception e) {
            throw new TorrentDownloaderException("Generic error", e);
        }
    }

    private static SynologyService getSynologyService() throws TorrentDownloaderException {
        SynologyRepository synologyRepository = new SynologyRepository(propertiesService);
        return new SynologyService(synologyRepository);
    }

    private static void createTorrentDownloaderJob() throws SchedulerException, TorrentDownloaderException {
        JobDetail job = QuartzUtils.getJobDetail(TorrentDownloaderJob.class, "torrentDownloaderJob");
        final String cron = propertiesService.getProperty("quartz.check");
        Trigger trigger = QuartzUtils.getTrigger("torrentDownloaderTrigger", cron);

        TorrentDownloaderService torrentDownloaderService = getTorrentDownloaderService(synologyService);
        QuartzUtils.startJob(job, trigger, "TorrentDownloaderService", torrentDownloaderService);
    }

    private static void createTorrentSortDownloaderJob() throws SchedulerException, TorrentDownloaderException {
        JobDetail job = QuartzUtils.getJobDetail(TorrentSortDownloaderJob.class, "torrentSortDownloaderJob");
        final String cron = propertiesService.getProperty("quartz.sort");
        Trigger trigger = QuartzUtils.getTrigger("torrentSortDownloaderTrigger", cron);

        TorrentSortDownloaderService torrentSortDownloaderService = new TorrentSortDownloaderService(synologyService, propertiesService);
        QuartzUtils.startJob(job, trigger, "TorrentSortDownloaderService", torrentSortDownloaderService);
    }

    private static TorrentDownloaderService getTorrentDownloaderService(SynologyService synologyService) throws TorrentDownloaderException {
        DataWebReaderService dataWebReaderService = DataWebReaderFactory.getInstance(propertiesService);
        return new TorrentDownloaderService(dataWebReaderService, synologyService);
    }

}
