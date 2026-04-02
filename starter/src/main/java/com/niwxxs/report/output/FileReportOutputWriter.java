package com.niwxxs.report.output;

import com.niwxxs.report.model.GeneratedReport;
import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.properties.ReportProperties;
import com.niwxxs.report.spi.ReportOutputWriter;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileReportOutputWriter implements ReportOutputWriter {

    private static final String DEFAULT_PATTERN = "${projectCode}-${reportCode}-${timestamp}.docx";

    @Override
    public GeneratedReport write(ReportRequest request, byte[] renderedBytes, ReportProperties properties) throws IOException {
        String outputDir = StringUtils.hasText(properties.getOutputDir())
                ? properties.getOutputDir()
                : System.getProperty("user.dir") + File.separator + "reports";

        File folder = new File(outputDir, request.getProjectCode() + File.separator + request.getReportCode());
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Cannot create output directory: " + folder.getAbsolutePath());
        }

        Date now = new Date();
        String fileName = resolveFileName(properties.getFileNamePattern(), request, now);
        File outputFile = new File(folder, fileName);

        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        try {
            fileOutputStream.write(renderedBytes);
        } finally {
            fileOutputStream.close();
        }

        return new GeneratedReport(outputFile, now);
    }

    private String resolveFileName(String pattern, ReportRequest request, Date now) {
        String value = StringUtils.hasText(pattern) ? pattern : DEFAULT_PATTERN;
        value = value.replace("${projectCode}", request.getProjectCode());
        value = value.replace("${reportCode}", request.getReportCode());
        value = value.replace("${timestamp}", String.valueOf(now.getTime()));
        value = value.replace("${date}", new SimpleDateFormat("yyyyMMdd").format(now));

        if (!value.endsWith(".docx")) {
            value = value + ".docx";
        }
        return value;
    }
}
