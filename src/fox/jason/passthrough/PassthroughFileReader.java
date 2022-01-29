package fox.jason.passthrough;

import java.io.File;
import java.io.IOException;

public class PassthroughFileReader extends AbstractFileReader {

  public PassthroughFileReader() {}

  @Override
  protected String runTarget(File inputFile, String title)
    throws IOException {
    return readResultFile(inputFile);
  }
}
