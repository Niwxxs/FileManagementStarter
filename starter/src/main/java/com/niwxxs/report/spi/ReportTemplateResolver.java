package com.niwxxs.report.spi;

import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.properties.ReportProperties;

import java.io.IOException;
import java.io.InputStream;

public interface ReportTemplateResolver {

    InputStream resolveTemplate(ReportRequest request, ReportProperties properties) throws IOException;
}
