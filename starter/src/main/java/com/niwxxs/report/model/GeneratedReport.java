package com.niwxxs.report.model;

import java.io.File;
import java.util.Date;

public class GeneratedReport {

    private final File file;
    private final Date generatedAt;

    public GeneratedReport(File file, Date generatedAt) {
        this.file = file;
        this.generatedAt = generatedAt;
    }

    public File getFile() {
        return file;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }
}
