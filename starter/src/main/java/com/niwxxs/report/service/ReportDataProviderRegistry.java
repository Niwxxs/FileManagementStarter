package com.niwxxs.report.service;

import com.niwxxs.report.spi.ReportDataProvider;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ReportDataProviderRegistry {

    private final List<ReportDataProvider> providers;

    public ReportDataProviderRegistry(List<ReportDataProvider> providers) {
        this.providers = providers;
    }

    public ReportDataProvider resolveProvider(String projectCode, String reportCode) {
        if (CollectionUtils.isEmpty(providers)) {
            throw new IllegalStateException("No ReportDataProvider bean found. Please implement one in your project.");
        }
        for (ReportDataProvider provider : providers) {
            if (provider.supports(projectCode, reportCode)) {
                return provider;
            }
        }
        throw new IllegalStateException("No ReportDataProvider matched project=" + projectCode + ", report=" + reportCode);
    }
}
