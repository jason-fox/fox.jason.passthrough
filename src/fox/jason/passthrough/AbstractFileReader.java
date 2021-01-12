package fox.jason.passthrough;

import java.io.StringReader;
import java.io.Reader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.XMLConstants;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.URISyntaxException;

import fox.jason.passthrough.AntExecutor;

public abstract class AbstractFileReader implements XMLReader {
  /**
   * Entity resolver
   */
  private EntityResolver resolver;
  /**
   * Content Handler
   */
  private ContentHandler handler;
  /**
   * Error Handler.
   */
  private ErrorHandler errorHandler;

  /**
   * Field file.
   */
  private File jarFile;

  public AbstractFileReader(Class clazz) {
    super();

    String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
    try {
      this.jarFile =new File(URLDecoder.decode(path, "UTF-8"));
    } catch (UnsupportedEncodingException e ){
      this.jarFile = null;
    }
  }

   protected File getJarFile(){
     return jarFile;
   }

  @Override
  public boolean getFeature(String name)
    throws SAXNotRecognizedException, SAXNotSupportedException {
    return false;
  }

  @Override
  public void setFeature(String name, boolean value)
    throws SAXNotRecognizedException, SAXNotSupportedException {
    // Not required for passthrough
  }

  @Override
  public Object getProperty(String name)
    throws SAXNotRecognizedException, SAXNotSupportedException {
    return null;
  }

  @Override
  public void setProperty(String name, Object value)
    throws SAXNotRecognizedException, SAXNotSupportedException {
    // Not required for passthrough
  }

  @Override
  public void setEntityResolver(EntityResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public EntityResolver getEntityResolver() {
    return resolver;
  }

  @Override
  public void setDTDHandler(DTDHandler handler) {
    //
  }

  @Override
  public DTDHandler getDTDHandler() {
    return null;
  }

  @Override
  public void setContentHandler(ContentHandler handler) {
    this.handler = handler;
  }

  @Override
  public ContentHandler getContentHandler() {
    return handler;
  }

  @Override
  public void setErrorHandler(ErrorHandler handler) {
    errorHandler = handler;
  }

  @Override
  public ErrorHandler getErrorHandler() {
    return errorHandler;
  }

  @Override
  public void parse(InputSource input) throws IOException, SAXException {
    URL url = new URL(input.getSystemId());
    String suffix;
    String title = url.getPath().replace('_', ' ');
    if (title.contains("/")) {
      title = title.substring(title.lastIndexOf('/') + 1, title.length());
    }
    if (title.contains(".")) {
      suffix = title.substring(title.lastIndexOf('.'));
      title = title.substring(0, title.lastIndexOf('.'));
    } else {
      suffix = title;
    }
    
    try {
      File srcFile = File.createTempFile("src", suffix);
      srcFile.deleteOnExit();
      writeSourceToFile(url.toURI(), srcFile);
      String dita = executeAntTask(srcFile, title);

      //Delegate to content handler.
      SAXResult result = new SAXResult(handler);
      try {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Source xslStream = new StreamSource(new StringReader(dita), url.toString());
        Transformer transformer = factory.newTransformer();
        transformer.transform(xslStream,result);

      } catch (Exception e) {
        throw new IOException(e);
      }

    } catch (URISyntaxException e){
      throw new IOException(e);
    }  
  }

  protected abstract String getAntFile();

  @Override
  public void parse(String systemId) throws IOException, SAXException {
    parse(new InputSource(systemId));
  }
  protected String fileAsString( File output) throws IOException {
    return new String(Files.readAllBytes(Paths.get(output.getPath())), java.nio.charset.StandardCharsets.UTF_8);
  }

  protected String executeAntTask(File passthroughInputFile, String title) throws IOException {
    File resultFile = File.createTempFile("dest", null);
    resultFile.deleteOnExit();
    AntExecutor.executeAntTask(getAntFile(), passthroughInputFile, resultFile, title);
    return fileAsString(resultFile);
  }

  protected void writeSourceToFile(URI uri, File copy) throws IOException {
    File original = new File(uri);
    Path copied = copy.toPath();
    Path originalPath = original.toPath();
    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
  }

}
