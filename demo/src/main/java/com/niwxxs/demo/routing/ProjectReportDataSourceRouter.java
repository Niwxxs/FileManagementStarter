package com.niwxxs.demo.routing;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProjectReportDataSourceRouter {

    private final Map<String, String> routeMap = new HashMap<String, String>();

    public ProjectReportDataSourceRouter() {
        routeMap.put(routeKey("project-a", "weekly"), "projectA");
        routeMap.put(routeKey("project-b", "weekly"), "projectB");
    }

    public String resolveDataSourceKey(String projectCode, String reportCode) {
        String key = routeMap.get(routeKey(projectCode, reportCode));
        if (key == null) {
            throw new IllegalArgumentException("No datasource route found for project=" + projectCode + ", report=" + reportCode);
        }
        return key;
    }

    private String routeKey(String projectCode, String reportCode) {
        return String.valueOf(projectCode) + "::" + String.valueOf(reportCode);
    }
}
