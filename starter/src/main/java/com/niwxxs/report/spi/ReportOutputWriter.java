package com.niwxxs.report.spi;

import com.niwxxs.report.model.GeneratedReport;
import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.properties.ReportProperties;

import java.io.IOException;

public interface ReportOutputWriter {

    GeneratedReport write(ReportRequest request, byte[] renderedBytes, ReportProperties properties) throws IOException;
}
