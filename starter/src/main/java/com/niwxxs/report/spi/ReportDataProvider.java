package com.niwxxs.report.spi;

import com.niwxxs.report.model.ReportRequest;

import java.util.Map;

public interface ReportDataProvider {

    boolean supports(String projectCode, String reportCode);

    Map<String, Object> queryData(ReportRequest request);
}
