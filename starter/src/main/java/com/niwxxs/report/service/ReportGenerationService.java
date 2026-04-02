package com.niwxxs.report.service;

import com.niwxxs.report.model.GeneratedReport;
import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.properties.ReportProperties;
import com.niwxxs.report.spi.ReportDataProvider;
import com.niwxxs.report.spi.ReportOutputWriter;
import com.niwxxs.report.spi.ReportRenderer;
import com.niwxxs.report.spi.ReportTemplateResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportGenerationService {

    private final ReportProperties properties;
    private final ReportDataProviderRegistry dataProviderRegistry;
    private final ReportTemplateResolver templateResolver;
    private final ReportRenderer renderer;
    private final ReportOutputWriter outputWriter;

    public ReportGenerationService(ReportProperties properties,
                                   ReportDataProviderRegistry dataProviderRegistry,
                                   ReportTemplateResolver templateResolver,
                                   ReportRenderer renderer,
                                   ReportOutputWriter outputWriter) {
        this.properties = properties;
        this.dataProviderRegistry = dataProviderRegistry;
        this.templateResolver = templateResolver;
        this.renderer = renderer;
        this.outputWriter = outputWriter;
    }

    public GeneratedReport generate(ReportRequest request) throws IOException {
        validateRequest(request);

        ReportDataProvider provider = dataProviderRegistry.resolveProvider(request.getProjectCode(), request.getReportCode());
        Map<String, Object> queried = provider.queryData(request);
        Map<String, Object> renderData = new LinkedHashMap<String, Object>();
        if (queried != null) {
            renderData.putAll(queried);
        }
        if (request.getExtraParams() != null) {
            renderData.putAll(request.getExtraParams());
        }

        InputStream templateInputStream = templateResolver.resolveTemplate(request, properties);
        try {
            byte[] bytes = renderer.render(templateInputStream, renderData);
            return outputWriter.write(request, bytes, properties);
        } finally {
            templateInputStream.close();
        }
    }

    private void validateRequest(ReportRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Report request must not be null");
        }
        if (!StringUtils.hasText(request.getProjectCode())) {
            throw new IllegalArgumentException("projectCode is required");
        }
        if (!StringUtils.hasText(request.getReportCode())) {
            throw new IllegalArgumentException("reportCode is required");
        }
    }
}
