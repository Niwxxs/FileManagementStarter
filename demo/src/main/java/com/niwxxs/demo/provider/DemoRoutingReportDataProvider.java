package com.niwxxs.demo.provider;

import com.niwxxs.demo.routing.ProjectReportDataSourceRouter;
import com.niwxxs.demo.routing.RoutingKeyContext;
import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.spi.ReportDataProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DemoRoutingReportDataProvider implements ReportDataProvider {

    private final JdbcTemplate jdbcTemplate;
    private final ProjectReportDataSourceRouter dataSourceRouter;

    public DemoRoutingReportDataProvider(JdbcTemplate jdbcTemplate, ProjectReportDataSourceRouter dataSourceRouter) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSourceRouter = dataSourceRouter;
    }

    @Override
    public boolean supports(String projectCode, String reportCode) {
        return "weekly".equals(reportCode) && ("project-a".equals(projectCode) || "project-b".equals(projectCode));
    }

    @Override
    public Map<String, Object> queryData(ReportRequest request) {
        String dsKey = dataSourceRouter.resolveDataSourceKey(request.getProjectCode(), request.getReportCode());
        RoutingKeyContext.setKey(dsKey);
        try {
            Map<String, Object> dbRow = jdbcTemplate.queryForMap("SELECT project_name, owner, score FROM report_metric LIMIT 1");
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("projectName", dbRow.get("PROJECT_NAME"));
            data.put("owner", dbRow.get("OWNER"));
            data.put("score", dbRow.get("SCORE"));
            data.put("projectCode", request.getProjectCode());
            return data;
        } finally {
            RoutingKeyContext.clear();
        }
    }
}
