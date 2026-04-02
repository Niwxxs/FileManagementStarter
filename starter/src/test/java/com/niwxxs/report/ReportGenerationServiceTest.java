package com.niwxxs.report;

import com.niwxxs.report.model.GeneratedReport;
import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.output.FileReportOutputWriter;
import com.niwxxs.report.properties.ReportProperties;
import com.niwxxs.report.render.PoiTlReportRenderer;
import com.niwxxs.report.service.ReportDataProviderRegistry;
import com.niwxxs.report.service.ReportGenerationService;
import com.niwxxs.report.spi.ReportDataProvider;
import com.niwxxs.report.template.ResourceReportTemplateResolver;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ReportGenerationServiceTest {

    @Test
    void shouldGenerateDocxByPoiTlTemplate() throws IOException {
        File tempDir = Files.createTempDirectory("report-starter-test").toFile();
        File templateFile = new File(tempDir, "demo-template.docx");
        createTemplate(templateFile);

        ReportProperties properties = new ReportProperties();
        properties.setOutputDir(tempDir.getAbsolutePath());

        ReportDataProvider provider = new ReportDataProvider() {
            @Override
            public boolean supports(String projectCode, String reportCode) {
                return "p1".equals(projectCode) && "weekly".equals(reportCode);
            }

            @Override
            public Map<String, Object> queryData(ReportRequest request) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", "Alice");
                return map;
            }
        };

        ReportGenerationService service = new ReportGenerationService(
                properties,
                new ReportDataProviderRegistry(Collections.singletonList(provider)),
                new ResourceReportTemplateResolver(new DefaultResourceLoader()),
                new PoiTlReportRenderer(),
                new FileReportOutputWriter()
        );

        ReportRequest request = new ReportRequest();
        request.setProjectCode("p1");
        request.setReportCode("weekly");
        request.setTemplate(templateFile.toURI().toString());

        GeneratedReport generated = service.generate(request);
        Assertions.assertTrue(generated.getFile().exists());
        Assertions.assertTrue(readText(generated.getFile()).contains("Alice"));
    }

    private void createTemplate(File file) throws IOException {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.createRun().setText("Hello {{name}}");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            doc.write(fileOutputStream);
        } finally {
            fileOutputStream.close();
            doc.close();
        }
    }

    private String readText(File file) throws IOException {
        XWPFDocument doc = new XWPFDocument(new FileInputStream(file));
        try {
            StringBuilder sb = new StringBuilder();
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                sb.append(paragraph.getText());
            }
            return sb.toString();
        } finally {
            doc.close();
        }
    }
}
