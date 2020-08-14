/*
 *  This file is part of the DITA-OT Passthrough Plug-in project.
 *  See the accompanying LICENSE file for applicable licenses.
 */

package fox.jason.passthrough.tasks;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.MacroInstance;

//
//	Run a given macro against an input and output
//
public class PassthroughTask extends Task {
  /**
   * Field macro.
   */
  private String macro;
  /**
   * Field src.
   */
  private String src;

  /**
   * Field dest.
   */
  private String dest;

  /**
   * Field title.
   */
  private String title;

  /**
   * Field metadata.
   */
  private String metadata;

  /**
   * Field regex.
   */
  private Pattern regex;

  /**
   * Creates a new <code>PassthroughTask</code> instance.
   */
  public PassthroughTask() {
    this.macro = null;
    this.src = null;
    this.dest = null;
    this.title = null;
    this.metadata = null;
    this.regex = Pattern.compile("^\\/[a-z]:", Pattern.CASE_INSENSITIVE);
  }

  /**
   * Method setMacro.
   *
   * @param macro String
   */
  public void setMacro(String macro) {
    this.macro = macro;
  }

  /**
   * Method setSrc.
   *
   * @param src String
   */
  public void setSrc(String src) {
    if (this.regex.matcher(src).matches()) {
      this.src = src.substring(1);
    }
    this.src = src;

    String rawTitle = src.replace('_', ' ');

    if (rawTitle.contains("/")) {
      rawTitle =
        rawTitle.substring(rawTitle.lastIndexOf('/') + 1, rawTitle.length());
      if (rawTitle.contains(".")) {
        rawTitle = rawTitle.substring(0, rawTitle.lastIndexOf('.'));
      }
    }

    this.title = rawTitle;
  }

  /**
   * Method setDest.
   *
   * @param dest String
   */
  public void setDest(String dest) {
    if (this.regex.matcher(dest).matches()) {
      dest = dest.substring(1);
    }
    this.dest = dest;
  }

  /**
   * Method setMetadata.
   *
   * @param metadata String
   */
  public void setMetadata(String metadata) {
    this.metadata = metadata;
  }

  /**
   * Method execute.
   *
   * @throws BuildException if something goes wrong
   */
  @Override
  public void execute() {
    //	@param macro - A macro to run.
    //	@param src - The source topic filename
    //	@param dest - The destination topic filename
    //	@param metadata - Topic metadata
    if (this.macro == null) {
      throw new BuildException("You must supply a macro to run");
    }
    if (this.src == null) {
      throw new BuildException("You must supply a source topic filename");
    }
    if (this.dest == null) {
      throw new BuildException("You must supply destination topic filename");
    }
    if (this.metadata == null) {
      throw new BuildException("You must supply topic metadata");
    }

    MacroInstance task = (MacroInstance) getProject().createTask(macro);

    try {
      task.setDynamicAttribute(
        "src",
        URLDecoder.decode(this.src, StandardCharsets.UTF_8.name())
      );
      task.setDynamicAttribute(
        "dest",
        URLDecoder.decode(this.dest, StandardCharsets.UTF_8.name())
      );
      task.setDynamicAttribute("title", this.title);
      task.setDynamicAttribute("metadata", this.metadata);
      task.execute();
    } catch (UnsupportedEncodingException err){
      throw new BuildException(err);
    }
  }
}
