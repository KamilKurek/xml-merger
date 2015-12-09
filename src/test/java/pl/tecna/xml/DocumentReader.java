package pl.tecna.xml;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DocumentReader {
  
  @Inject
  private Provider<DocumentBuilder> documentBuilderProvider;

  protected Element readDocumentElement(String fileName) throws IOException {
    try {
      return documentBuilderProvider.get().parse(
          new InputSource(new InputStreamReader(AttributeMergerTest.class.getResourceAsStream(fileName))))
          .getDocumentElement();
    } catch (SAXException e) {
      throw new IllegalArgumentException("Could not parse data");
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not read data");
    }
  }
  
}
