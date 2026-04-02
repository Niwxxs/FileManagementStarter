package com.niwxxs.report.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "report")
public class ReportProperties {

    private boolean enabled = true;
    private String templateRoot = "classpath:/templates/reports";
    private String outputDir = System.getProperty("user.dir") + File.separator + "reports";
    private String fileNamePattern = "${projectCode}-${reportCode}-${timestamp}.docx";
    private boolean scheduleEnabled;
    private List<Task> tasks = new ArrayList<Task>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTemplateRoot() {
        return templateRoot;
    }

    public void setTemplateRoot(String templateRoot) {
        this.templateRoot = templateRoot;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public boolean isScheduleEnabled() {
        return scheduleEnabled;
    }

    public void setScheduleEnabled(boolean scheduleEnabled) {
        this.scheduleEnabled = scheduleEnabled;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        if (tasks == null) {
            this.tasks = new ArrayList<Task>();
            return;
        }
        this.tasks = tasks;
    }

    public static class Task {

        private String name;
        private boolean enabled = true;
        private String projectCode;
        private String reportCode;
        private String template;
        private String cron;
        private String zone = "Asia/Shanghai";
        private Map<String, Object> extraParams = new LinkedHashMap<String, Object>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

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

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
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
}
