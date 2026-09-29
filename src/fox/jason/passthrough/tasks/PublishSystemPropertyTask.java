package fox.jason.passthrough.tasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

// Bridges an Ant property to a JVM system property.
public class PublishSystemPropertyTask extends Task {

  private String name;
  private String value;

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public void execute() {
    if (name == null) {
      throw new BuildException("You must supply a name");
    }
    System.setProperty(name, value == null ? "" : value);
  }
}
