package com.niwxxs.config;

import com.niwxxs.report.output.FileReportOutputWriter;
import com.niwxxs.report.properties.ReportProperties;
import com.niwxxs.report.render.PoiTlReportRenderer;
import com.niwxxs.report.schedule.ReportTaskScheduler;
import com.niwxxs.report.service.ReportDataProviderRegistry;
import com.niwxxs.report.service.ReportGenerationService;
import com.niwxxs.report.spi.ReportDataProvider;
import com.niwxxs.report.spi.ReportOutputWriter;
import com.niwxxs.report.spi.ReportRenderer;
import com.niwxxs.report.spi.ReportTemplateResolver;
import com.niwxxs.report.template.ResourceReportTemplateResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;

@Configuration
@ConditionalOnClass(name = "com.deepoove.poi.XWPFTemplate")
@ConditionalOnProperty(prefix = "report", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ReportProperties.class)
public class ReportAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReportTemplateResolver reportTemplateResolver(ResourceLoader resourceLoader) {
        return new ResourceReportTemplateResolver(resourceLoader);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReportRenderer reportRenderer() {
        return new PoiTlReportRenderer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReportOutputWriter reportOutputWriter() {
        return new FileReportOutputWriter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReportDataProviderRegistry reportDataProviderRegistry(List<ReportDataProvider> providers) {
        return new ReportDataProviderRegistry(providers);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReportGenerationService reportGenerationService(ReportProperties properties,
                                                           ReportDataProviderRegistry registry,
                                                           ReportTemplateResolver templateResolver,
                                                           ReportRenderer renderer,
                                                           ReportOutputWriter outputWriter) {
        return new ReportGenerationService(properties, registry, templateResolver, renderer, outputWriter);
    }

    @Bean
    @ConditionalOnMissingBean(name = "reportTaskScheduler")
    @ConditionalOnProperty(prefix = "report", name = "schedule-enabled", havingValue = "true")
    public ReportTaskScheduler reportTaskScheduler(ReportProperties properties,
                                                   TaskScheduler taskScheduler,
                                                   ReportGenerationService reportGenerationService) {
        return new ReportTaskScheduler(properties, taskScheduler, reportGenerationService);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "report", name = "schedule-enabled", havingValue = "true")
    public TaskScheduler reportSchedulerExecutor() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("report-task-");
        scheduler.initialize();
        return scheduler;
    }
}
