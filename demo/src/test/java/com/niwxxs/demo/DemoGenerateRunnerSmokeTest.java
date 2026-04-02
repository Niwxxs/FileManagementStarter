package com.niwxxs.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest(properties = {
        "demo.generate-on-startup=true",
        "report.schedule-enabled=false",
        "report.output-dir=target/demo-test-output"
})
class DemoGenerateRunnerSmokeTest {

    @Test
    void shouldGenerateReportsOnStartup() {
        File projectA = new File("target/demo-test-output/project-a/weekly");
        File projectB = new File("target/demo-test-output/project-b/weekly");

        Assertions.assertTrue(projectA.exists());
        Assertions.assertTrue(projectB.exists());
        Assertions.assertTrue(projectA.isDirectory());
        Assertions.assertTrue(projectB.isDirectory());
        Assertions.assertTrue(projectA.listFiles() != null && projectA.listFiles().length > 0);
        Assertions.assertTrue(projectB.listFiles() != null && projectB.listFiles().length > 0);
    }
}
