package com.niwxxs.demo.template;

import com.niwxxs.report.model.ReportRequest;
import com.niwxxs.report.properties.ReportProperties;
import com.niwxxs.report.spi.ReportTemplateResolver;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class DemoTemplateResolverConfiguration {

    @Bean
    @Primary
    public ReportTemplateResolver demoTemplateResolver() {
        return new ReportTemplateResolver() {
            @Override
            public InputStream resolveTemplate(ReportRequest request, ReportProperties properties) throws IOException {
                byte[] content = buildTemplate();
                return new ByteArrayInputStream(content);
            }
        };
    }

    private byte[] buildTemplate() throws IOException {
        XWPFDocument doc = new XWPFDocument();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            XWPFParagraph title = doc.createParagraph();
            title.createRun().setText("Weekly Report - {{projectName}}");
            XWPFParagraph line1 = doc.createParagraph();
            line1.createRun().setText("Owner: {{owner}}");
            XWPFParagraph line2 = doc.createParagraph();
            line2.createRun().setText("Score: {{score}}");
            XWPFParagraph line3 = doc.createParagraph();
            line3.createRun().setText("Project Code: {{projectCode}}");
            doc.write(outputStream);
            return outputStream.toByteArray();
        } finally {
            outputStream.close();
            doc.close();
        }
    }
}
