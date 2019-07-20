package fox.jason.passthrough.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class FileReader implements XMLReader {

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

	@Override
	public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
		return false;
	}

	@Override
	public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
	}

	@Override
	public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
		return null;
	}

	@Override
	public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
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
		StringBuilder sb = new StringBuilder();
		sb.append("<topic id=\"passthrough\" class=\"- topic/topic \" domains=\"(topic abbrev-d) a(props deliveryTarget) (topic equation-d) (topic hazard-d) (topic hi-d) (topic indexing-d) (topic markup-d) (topic mathml-d) (topic pr-d) (topic relmgmt-d) (topic sw-d) (topic svg-d) (topic ui-d) (topic ut-d) (topic markup-d xml-d)\" >");
		sb.append("<title class=\"- topic/title \">passthrough</title>");
		sb.append("<body class=\"- topic/body \"/>");
		sb.append("</topic>");
		//Delegate to content handler.
		SAXResult result = new SAXResult(handler);
		try {
			TransformerFactory.newInstance().newTransformer().transform(new StreamSource(new StringReader(sb.toString()), url.toString()),
					result);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public void parse(String systemId) throws IOException, SAXException {
		parse(new InputSource(systemId));
	}

}
