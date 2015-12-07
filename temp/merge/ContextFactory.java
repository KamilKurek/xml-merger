package pl.tecna.aurea.engine.forms.merge;

import org.w3c.dom.Element;

public interface ContextFactory {

  Context create(Element element);
  
}
