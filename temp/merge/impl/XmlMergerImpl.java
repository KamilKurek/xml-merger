package pl.tecna.aurea.engine.forms.merge.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.tecna.aurea.engine.forms.merge.Context;
import pl.tecna.aurea.engine.forms.merge.ContextMerger;
import pl.tecna.aurea.engine.forms.merge.XmlMerger;

public class XmlMergerImpl implements XmlMerger {

  @Inject
  private Provider<DocumentBuilder> documentBuilderProvider;
  
  @Inject
  private Provider<Transformer> transformerProvider;
  
  @Inject
  private ContextMerger merger;
  
  @Override
  public String merge(String source, String actual, String merged) {
    Document sourceDocument = parseXml(source);
    Document actualDocument = parseXml(actual);
    Document mergedDocument = parseXml(merged);

    Context sourceRootContext = createRootContext(sourceDocument);
    Context actualRootContext = createRootContext(actualDocument);
    Context mergedRootContext = createRootContext(mergedDocument);
    
    if (actualRootContext.getElement() != null) {
      actualDocument.removeChild(actualRootContext.getElement());
    }

    //TODO context powinien miec liste node-ow nie bedacych elementem
    actualDocument.appendChild(actualRootContext.getElement());
    
    return serializeDocument(actualDocument);
  }
  
  private Context createRootContext(Document document) {
    return new ContextImpl(document.getDocumentElement());
  }
  
  private String serializeDocument(Document document) {
    DOMSource domSource = new DOMSource(document);

    try {
      StringWriter sw = new StringWriter();
      StreamResult sr = new StreamResult(sw);
      transformerProvider.get().transform(domSource, sr);
      
      return sw.toString();
    } catch (TransformerException e) {
      throw new IllegalStateException("Could not flush data");
    }
  }
  
  private Document parseXml(String xml) {
    try {
      return documentBuilderProvider.get().parse(new InputSource(new StringReader(xml)));
    } catch (SAXException e) {
      throw new IllegalArgumentException("Could not parse data");
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not read data");
    }
  }

}
