package com.niwxxs.demo.config;

import com.niwxxs.demo.routing.ProjectReportRoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DemoDataSourceConfiguration {

    @Bean(name = "projectADataSource")
    public DataSource projectADataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:projectA;MODE=MySQL;DB_CLOSE_DELAY=-1")
                .username("sa")
                .password("")
                .build();
    }

    @Bean(name = "projectBDataSource")
    public DataSource projectBDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:projectB;MODE=MySQL;DB_CLOSE_DELAY=-1")
                .username("sa")
                .password("")
                .build();
    }

    @Bean
    @Primary
    public DataSource dataSource(@Qualifier("projectADataSource") DataSource projectADataSource,
                                 @Qualifier("projectBDataSource") DataSource projectBDataSource) {
        initOne(new JdbcTemplate(projectADataSource), "Project A", "Team Alpha", 87);
        initOne(new JdbcTemplate(projectBDataSource), "Project B", "Team Beta", 93);

        ProjectReportRoutingDataSource routingDataSource = new ProjectReportRoutingDataSource();
        Map<Object, Object> targets = new HashMap<Object, Object>();
        targets.put("projectA", projectADataSource);
        targets.put("projectB", projectBDataSource);
        routingDataSource.setTargetDataSources(targets);
        routingDataSource.setDefaultTargetDataSource(projectADataSource);
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    private void initOne(JdbcTemplate jdbcTemplate, String projectName, String owner, int score) {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS report_metric (project_name VARCHAR(64), owner VARCHAR(64), score INT)");
        jdbcTemplate.update("DELETE FROM report_metric");
        jdbcTemplate.update("INSERT INTO report_metric(project_name, owner, score) VALUES (?,?,?)", projectName, owner, score);
    }
}