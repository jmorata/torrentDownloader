package com.jmorata.torrentDownloader.job;

import com.jmorata.torrentDownloader.service.DeleteFilesService;
import org.quartz.*;

@DisallowConcurrentExecution
public class DeleteFilesJob implements Job {

    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();
            DeleteFilesService deleteFilesService = (DeleteFilesService) schedulerContext.get("DeleteFilesService");
            deleteFilesService.execute();

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

}
