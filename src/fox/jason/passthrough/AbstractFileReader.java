package fox.jason.passthrough;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

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

  public AbstractFileReader() {}

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
      String dita = runTarget(srcFile, title);

      //Delegate to content handler.
      SAXResult result = new SAXResult(handler);
      try {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Source xslStream = new StreamSource(
          new StringReader(dita),
          url.toString()
        );
        Transformer transformer = factory.newTransformer();
        transformer.transform(xslStream, result);
      } catch (Exception e) {
        throw new IOException(e);
      }
    } catch (URISyntaxException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void parse(String systemId) throws IOException, SAXException {
    parse(new InputSource(systemId));
  }

  protected abstract String runTarget(File inputFile, String title)
    throws IOException;

  protected void writeSourceToFile(URI uri, File copy) throws IOException {
    File original = new File(uri);
    Path copied = copy.toPath();
    Path originalPath = original.toPath();
    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
  }

  protected String readResultFile(File output) throws IOException {
    return new String(
      Files.readAllBytes(Paths.get(output.getPath())),
      java.nio.charset.StandardCharsets.UTF_8
    );
  }

  protected void writeToFile (String contents, File file) throws IOException {
    Files.write(Paths.get(file.getPath()), contents.getBytes());
  }
}
