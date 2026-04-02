# report-spring-boot-starter

A reusable Spring Boot Starter for project-level scheduled report generation based on POI-TL templates.

## Project Structure

- `starter/`: reusable Spring Boot Starter module
- `demo/`: runnable consumer demo with datasource routing example
- root `pom.xml`: Maven aggregator parent

## Features

- Generate `.docx` reports from POI-TL templates.
- Let each project provide its own query logic via SPI.
- Support one-time generation and multi-task cron scheduling.
- Auto configuration with `spring.factories`.
- Demonstrate datasource routing by `projectCode/reportCode`.

## Dependency

```xml
<dependency>
    <groupId>com.niwxxs</groupId>
    <artifactId>report-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Publish Notes

The publish configuration now lives in `starter/pom.xml`:

- `distributionManagement` is configured to `ossrh`
- source/javadoc jars are attached by default
- GPG signing is enabled only in `release` profile

## Quick Start

### 1) Implement project data provider

```java
@Component
public class ProjectAWeeklyProvider implements ReportDataProvider {

    @Override
    public boolean supports(String projectCode, String reportCode) {
        return "project-a".equals(projectCode) && "weekly".equals(reportCode);
    }

    @Override
    public Map<String, Object> queryData(ReportRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("title", "Project A Weekly Report");
        data.put("owner", "Team A");
        return data;
    }
}
```

### 2) Configure report properties

```yaml
report:
  enabled: true
  template-root: classpath:/templates/reports
  output-dir: ./build/reports
  file-name-pattern: ${projectCode}-${reportCode}-${date}.docx
  schedule-enabled: true
  tasks:
    - name: project-a-weekly
      enabled: true
      project-code: project-a
      report-code: weekly
      cron: "0 0 8 ? * MON"
      zone: Asia/Shanghai
```

### 3) Generate manually when needed

```java
@Autowired
private ReportGenerationService reportGenerationService;

public void generateNow() throws IOException {
    ReportRequest request = new ReportRequest();
    request.setProjectCode("project-a");
    request.setReportCode("weekly");
    GeneratedReport report = reportGenerationService.generate(request);
    System.out.println(report.getFile().getAbsolutePath());
}
```

## Demo Module

The runnable demo is under `demo/` and shows:

- datasource routing by `projectCode/reportCode`
- scheduled report generation for multiple projects
- startup-time one-shot report generation

See `demo/README.md` for details.

## Template Conventions

- Default template path: `${report.template-root}/{projectCode}/{reportCode}.docx`
- POI-TL placeholder sample: `{{title}}`, `{{owner}}`

## Verify

```powershell
mvn clean test
```

## Run Demo

```powershell
mvn -pl demo -am spring-boot:run
```