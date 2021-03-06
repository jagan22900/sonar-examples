package com.mycompany.sonarqube.scanner;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

public class IssueSensor implements Sensor {

  private final FileSystem fs;
  private final ResourcePerspectives perspectives;

  /**
   * Use of IoC to get FileSystem
   */
  public IssueSensor(FileSystem fs, ResourcePerspectives perspectives) {
    this.fs = fs;
    this.perspectives = perspectives;
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    // This sensor is executed only when there are Java files
    return fs.hasFiles(fs.predicates().hasLanguage("java"));
  }

  @Override
  public void analyse(Project project, SensorContext sensorContext) {
    // This sensor create an issue on each java file
    for (InputFile inputFile : fs.inputFiles(fs.predicates().hasLanguage("java"))) {
      Issuable issuable = perspectives.as(Issuable.class, inputFile);
      issuable.addIssue(issuable.newIssueBuilder()
        .ruleKey(RuleKey.of("java-example", "ExampleRule1"))
        .message("Message explaining why this issue has been raised.")
        .line(1)
        .build());
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
