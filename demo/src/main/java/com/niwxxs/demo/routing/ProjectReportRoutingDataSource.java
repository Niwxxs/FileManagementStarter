package com.niwxxs.demo.routing;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ProjectReportRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingKeyContext.getKey();
    }
}
