package fox.jason.passthrough;

public class PassthroughFileReader extends AbstractFileReader {

  private static final String ANT_FILE = "/../process_passthrough.xml";

  @Override
  protected String getAntFile(){
    String path =  this.getJarFile().getParent();
    return path + ANT_FILE;
  }
  
  public PassthroughFileReader() {
    super(PassthroughFileReader.class);
  }
}
