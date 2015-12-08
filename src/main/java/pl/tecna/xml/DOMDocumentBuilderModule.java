package pl.tecna.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

public class DOMDocumentBuilderModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(DOMDocumentBuilderModule.class);

  @Override
  protected void configure() {
    try {
      bind(DocumentBuilder.class).toInstance(DocumentBuilderFactory.newInstance().newDocumentBuilder());
    } catch (ParserConfigurationException e) {
      LOG.error("Error while creating DocumentBuilder instance", e);
    }		
  }

}
