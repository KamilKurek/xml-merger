package pl.tecna.aurea.engine.forms.merge;

import org.w3c.dom.Element;

public interface AttributeMerger {

  void mergeAttributes(Element source, Element actual, Element merged);
  
}
