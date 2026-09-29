package fox.jason.passthrough;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public abstract class AntTaskFileReader extends AbstractFileReader {

  public AntTaskFileReader() {}

  protected String calculateJarPath(Class clazz) {
    try {
      File jarFile = new File(
        URLDecoder.decode(
          clazz.getProtectionDomain().getCodeSource().getLocation().getPath(),
          "UTF-8"
        )
      );
      return jarFile.getParent();
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }

  protected String executeAntTask(String antFile, File inputFile, String title)
    throws IOException {
    File resultFile = File.createTempFile("dest", null);
    resultFile.deleteOnExit();
    AntExecutor.executeAntTask(antFile, inputFile, resultFile, title);
    return readResultFile(resultFile);
  }
}
