package com.jmorata.torrentDownloader.config;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.factory.DataWebReaderFactory;
import com.jmorata.torrentDownloader.factory.TorrentDownloaderFactory;
import com.jmorata.torrentDownloader.job.DeleteFilesJob;
import com.jmorata.torrentDownloader.job.TorrentDownloaderJob;
import com.jmorata.torrentDownloader.job.TorrentSortDownloaderJob;
import com.jmorata.torrentDownloader.quartz.QuartzUtils;
import com.jmorata.torrentDownloader.service.*;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static SynologyService synologyService;
    private static PropertiesService propertiesService;

    public QuartzConfig(SynologyService synologyService, PropertiesService propertiesService) throws TorrentDownloaderException {
        QuartzConfig.synologyService = synologyService;
        QuartzConfig.propertiesService = propertiesService;

        try {
            createTorrentDownloaderJob();
            createTorrentSortDownloaderJob();
            createDeleteFilesJob();

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error configuring quartz", e);
        }
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
        return TorrentDownloaderFactory.getInstance(dataWebReaderService, synologyService, propertiesService);
    }

    private static void createDeleteFilesJob() throws SchedulerException, TorrentDownloaderException {
        JobDetail job = QuartzUtils.getJobDetail(DeleteFilesJob.class, "deleteFilesJob");
        final String cron = propertiesService.getProperty("quartz.delete");
        Trigger trigger = QuartzUtils.getTrigger("deleteFilesTrigger", cron);

        DeleteFilesService deleteFilesService = new DeleteFilesService(propertiesService);
        QuartzUtils.startJob(job, trigger, "DeleteFilesService", deleteFilesService);
    }

    public void printJobNames() throws TorrentDownloaderException {
        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    logger.info("Successfully configured job: "+jobName);
                }
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("Error getting job names", e);
        }
    }

}
