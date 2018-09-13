package com.jmorata.torrentDownloader.Job;

import com.jmorata.torrentDownloader.service.TorrentDownloaderService;
import org.quartz.*;

@DisallowConcurrentExecution
public class TorrentDownloaderJob implements Job {

    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();
            TorrentDownloaderService torrentDownloaderService = (TorrentDownloaderService) schedulerContext.get("TorrentDownloaderService");
            torrentDownloaderService.execute();

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

}
