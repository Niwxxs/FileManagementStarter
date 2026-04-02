package com.niwxxs.report.schedule;

import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.properties.ReportProperties;
import com.niwxxs.report.service.ReportGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

public class ReportTaskScheduler implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportTaskScheduler.class);

    private final ReportProperties properties;
    private final TaskScheduler taskScheduler;
    private final ReportGenerationService reportGenerationService;
    private final List<ScheduledFuture<?>> scheduledFutures = new ArrayList<ScheduledFuture<?>>();

    public ReportTaskScheduler(ReportProperties properties,
                               TaskScheduler taskScheduler,
                               ReportGenerationService reportGenerationService) {
        this.properties = properties;
        this.taskScheduler = taskScheduler;
        this.reportGenerationService = reportGenerationService;
    }

    @Override
    public void afterPropertiesSet() {
        for (ReportProperties.Task task : properties.getTasks()) {
            if (!task.isEnabled()) {
                continue;
            }
            if (!StringUtils.hasText(task.getCron())
                    || !StringUtils.hasText(task.getProjectCode())
                    || !StringUtils.hasText(task.getReportCode())) {
                LOGGER.warn("Skip task [{}] because cron/projectCode/reportCode is incomplete", task.getName());
                continue;
            }

            ScheduledFuture<?> future = taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    runTask(task);
                }
            }, new CronTrigger(task.getCron(), TimeZone.getTimeZone(task.getZone())));
            scheduledFutures.add(future);
            LOGGER.info("Scheduled report task [{}] with cron [{}]", task.getName(), task.getCron());
        }
    }

    private void runTask(ReportProperties.Task task) {
        ReportRequest request = new ReportRequest();
        request.setProjectCode(task.getProjectCode());
        request.setReportCode(task.getReportCode());
        request.setTemplate(task.getTemplate());
        request.setExtraParams(task.getExtraParams());
        try {
            reportGenerationService.generate(request);
        } catch (IOException ex) {
            LOGGER.error("Generate report failed for task [{}]", task.getName(), ex);
        }
    }

    @Override
    public void destroy() {
        for (ScheduledFuture<?> future : scheduledFutures) {
            future.cancel(true);
        }
        scheduledFutures.clear();
    }
}
