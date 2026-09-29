package fox.jason.passthrough;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class AntExecutor {

  /**
   * To execute a target specified in the Ant build.xml file
   *
   */
  public static boolean executeAntTask(
    String antFilePath,
    File inputFile,
    File outputFile,
    String title
  ) {
    boolean success = false;
    DefaultLogger consoleLogger = getConsoleLogger();

    // Prepare Ant project
    Project project = new Project();
    File buildFile = new File(antFilePath);
    project.setUserProperty("ant.file", buildFile.getAbsolutePath());
    project.addBuildListener(consoleLogger);

    // Capture event for Ant script build start / stop / failure
    try {
      project.fireBuildStarted();
      project.init();
      ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
      project.addReference("ant.projectHelper", projectHelper);
      projectHelper.parse(project, buildFile);

      project.setProperty("passthrough.input", inputFile.getAbsolutePath());
      project.setProperty("passthrough.output", outputFile.getAbsolutePath());
      project.setProperty("passthrough.title", title);
      project.executeTarget(project.getDefaultTarget());
      project.fireBuildFinished(null);
      success = true;
    } catch (BuildException buildException) {
      project.fireBuildFinished(buildException);
      System.err.println(buildException);
      throw new RuntimeException("plugin error:", buildException);
    }
    return success;
  }

  /**
   * Logger to log output generated while executing ant script in console
   *
   * @return
   */
  private static DefaultLogger getConsoleLogger() {
    DefaultLogger consoleLogger = new DefaultLogger();
    consoleLogger.setErrorPrintStream(System.err);
    consoleLogger.setOutputPrintStream(System.out);
    consoleLogger.setMessageOutputLevel(Project.MSG_INFO);

    return consoleLogger;
  }
}
