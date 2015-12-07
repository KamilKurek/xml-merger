package pl.tecna.aurea.engine.forms.merge.impl;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import pl.tecna.aurea.engine.forms.merge.AttributeMerger;

public class AttributeMergerImpl implements AttributeMerger {

  @Override
  public void mergeAttributes(Element source, Element actual, Element merged) {
    removeMissing(source, actual, merged);
    combine(source, actual, merged);
  }

  private void removeMissing(Element source, Element actual, Element merged) {
    NamedNodeMap actualAttributes = actual.getAttributes();
    for (int i = 0 ; i < actualAttributes.getLength() ; i++) {
      Attr actualAttribute = (Attr) actualAttributes.item(i);
      Attr mergedAttribute = merged.getAttributeNodeNS(actualAttribute.getNamespaceURI(), actualAttribute.getName());
      Attr sourceAttribute = source.getAttributeNodeNS(actualAttribute.getNamespaceURI(), actualAttribute.getName());
      removeIfMissing(actual, sourceAttribute, actualAttribute, mergedAttribute);
    } 
  }
  
  private void removeIfMissing(Element actual, Attr sourceAttribute, Attr actualAttribute, Attr mergedAttribute) {
    if (mergedAttribute == null) {
      if (actualAttribute != null && sourceAttribute != null) {
        actual.removeAttributeNode(actualAttribute);
      }
    }
  }
  
  private void combine(Element source, Element actual, Element merged) {
    NamedNodeMap mergedAttributes = merged.getAttributes();
    for (int i = 0 ; i < mergedAttributes.getLength() ; i++) {
      Attr mergedAttribute = (Attr) mergedAttributes.item(i);
      Attr actualAttribute = actual.getAttributeNodeNS(mergedAttribute.getNamespaceURI(), mergedAttribute.getName());
      Attr sourceAttribute = source.getAttributeNodeNS(mergedAttribute.getNamespaceURI(), mergedAttribute.getName());
      combineIfChanged(actual, sourceAttribute, actualAttribute, mergedAttribute);
    }
  }
  
  private void combineIfChanged(Element actual, Attr sourceAttribute, Attr actualAttribute, Attr mergedAttribute) {
    if (actualAttribute == null) {
      if (sourceAttribute == null || !sourceAttribute.getValue().contentEquals(mergedAttribute.getValue())) {
        Node imported = actual.getOwnerDocument().importNode(mergedAttribute, true);
        actual.setAttributeNodeNS((Attr) imported);
      }
    } else {
      if (sourceAttribute == null) {
        actualAttribute.setValue(mergedAttribute.getValue());
      } else if (sourceAttribute.getValue() == null || !sourceAttribute.getValue().contentEquals(mergedAttribute.getValue())){
        actualAttribute.setValue(mergedAttribute.getValue());
      }
    }
  }
  
}
