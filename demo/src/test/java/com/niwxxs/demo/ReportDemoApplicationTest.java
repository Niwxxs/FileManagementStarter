package com.niwxxs.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "demo.generate-on-startup=false",
        "report.schedule-enabled=false"
})
class ReportDemoApplicationTest {

    @Test
    void contextLoads() {
    }
}