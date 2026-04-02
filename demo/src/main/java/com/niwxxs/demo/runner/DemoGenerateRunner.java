package com.niwxxs.demo.runner;

import com.niwxxs.report.model.GeneratedReport;
import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.service.ReportGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
@ConditionalOnProperty(prefix = "demo", name = "generate-on-startup", havingValue = "true", matchIfMissing = true)
public class DemoGenerateRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoGenerateRunner.class);

    private final ReportGenerationService reportGenerationService;

    public DemoGenerateRunner(ReportGenerationService reportGenerationService) {
        this.reportGenerationService = reportGenerationService;
    }

    @Override
    public void run(String... args) throws Exception {
        generate("project-a", "weekly");
        generate("project-b", "weekly");
    }

    private void generate(String projectCode, String reportCode) throws Exception {
        ReportRequest request = new ReportRequest();
        request.setProjectCode(projectCode);
        request.setReportCode(reportCode);
        GeneratedReport generated = reportGenerationService.generate(request);
        LOGGER.info("Generated report: {}", generated.getFile().getAbsolutePath());
    }
}