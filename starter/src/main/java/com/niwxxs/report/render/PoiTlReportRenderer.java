package com.niwxxs.report.render;

import com.deepoove.poi.XWPFTemplate;
import com.niwxxs.report.spi.ReportRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public class PoiTlReportRenderer implements ReportRenderer {

    @Override
    public byte[] render(InputStream templateInputStream, Map<String, Object> data) throws IOException {
        Map<String, Object> renderData = data == null ? Collections.<String, Object>emptyMap() : data;
        XWPFTemplate template = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            template = XWPFTemplate.compile(templateInputStream).render(renderData);
            template.write(outputStream);
            return outputStream.toByteArray();
        } finally {
            if (template != null) {
                template.close();
            }
            outputStream.close();
        }
    }
}
