package com.jmorata.torrentDownloader.job;

import com.jmorata.torrentDownloader.service.TorrentSortDownloaderService;
import org.quartz.*;

@DisallowConcurrentExecution
public class TorrentSortDownloaderJob implements Job {

    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();
            TorrentSortDownloaderService torrentSortDownloaderService = (TorrentSortDownloaderService) schedulerContext.get("TorrentSortDownloaderService");
            torrentSortDownloaderService.execute();

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

}
