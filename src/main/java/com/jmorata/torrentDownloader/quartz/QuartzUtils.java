package com.jmorata.torrentDownloader.quartz;

import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzUtils {

    private static final String JOB_GROUP = "torrentGroup";

    public static void startJob(JobDetail job, Trigger trigger, String paramName, Object object) throws SchedulerException {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.getContext().put(paramName, object);
        scheduler.scheduleJob(job, trigger);
    }

    public static Trigger getTrigger(String triggerName, String cron) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, JOB_GROUP)
                .withSchedule(
                        CronScheduleBuilder.cronSchedule(cron))
                .build();
    }

    public static JobDetail getJobDetail(Class jobClass, String jobName) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName, JOB_GROUP).build();
    }

}
