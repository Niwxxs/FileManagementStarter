package com.niwxxs.report.spi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface ReportRenderer {

    byte[] render(InputStream templateInputStream, Map<String, Object> data) throws IOException;
}
