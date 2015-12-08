package pl.tecna.xml;

import org.w3c.dom.Element;

public interface ContextFactory {

  Context create(Element element);
  
}
