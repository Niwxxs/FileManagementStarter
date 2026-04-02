package com.niwxxs.report.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReportRequest {

    private String projectCode;
    private String reportCode;
    private String template;
    private Map<String, Object> extraParams = new LinkedHashMap<String, Object>();

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Object> getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(Map<String, Object> extraParams) {
        if (extraParams == null) {
            this.extraParams = new LinkedHashMap<String, Object>();
            return;
        }
        this.extraParams = extraParams;
    }
}
