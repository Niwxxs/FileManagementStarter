# report-starter-demo

This module is a consumer app for `report-spring-boot-starter`.

## What it demonstrates

- Auto-generate reports at startup for `project-a` and `project-b`
- Schedule recurring report tasks
- Route datasource by `projectCode/reportCode`

## Routing rule example

- `project-a + weekly` -> datasource `projectA`
- `project-b + weekly` -> datasource `projectB`

Implemented in `com.niwxxs.demo.routing.ProjectReportDataSourceRouter`.

## Quick try

Run from the repository root so Maven reactor can build `starter` and `demo` together.

```powershell
cd E:\git\FileManagementStarter
mvn -pl demo -am test
mvn -pl demo -am spring-boot:run
```

Generated files are written to `demo-output/`.