package com.niwxxs.report.template;

import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.properties.ReportProperties;
import com.niwxxs.report.spi.ReportTemplateResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ResourceReportTemplateResolver implements ReportTemplateResolver {

    private final ResourceLoader resourceLoader;

    public ResourceReportTemplateResolver(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public InputStream resolveTemplate(ReportRequest request, ReportProperties properties) throws IOException {
        String location = request.getTemplate();
        if (!StringUtils.hasText(location)) {
            location = buildDefaultLocation(properties.getTemplateRoot(), request.getProjectCode(), request.getReportCode());
        }
        Resource resource = resourceLoader.getResource(location);
        if (!resource.exists()) {
            throw new FileNotFoundException("Template not found: " + location);
        }
        return resource.getInputStream();
    }

    private String buildDefaultLocation(String templateRoot, String projectCode, String reportCode) {
        String root = StringUtils.hasText(templateRoot) ? templateRoot.trim() : "classpath:/templates/reports";
        if (root.endsWith("/")) {
            return root + projectCode + "/" + reportCode + ".docx";
        }
        return root + "/" + projectCode + "/" + reportCode + ".docx";
    }
}
