package pl.tecna.xml.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pl.tecna.xml.Context;
import pl.tecna.xml.ContextFactory;
import pl.tecna.xml.Key;
import pl.tecna.xml.KeyFactory;

import com.google.inject.assistedinject.Assisted;

public class ContextImpl implements Context {

  private static final Logger LOG = LoggerFactory.getLogger(ContextImpl.class);
  
  @Inject
  private KeyFactory keyFactory;
  
  @Inject
  private ContextFactory contextFactory;
  
  protected Element element;
  protected String path;
  protected Key key;
  protected Map<Key, Context> childs;

  @Inject
  public ContextImpl(@Assisted Element element) {
    this.element = element;
  }
  
  @Override
  public Element getElement() {
    return element;
  }

  public Map<Key, Context> getChilds() {
    if (childs == null) {
      initChildsMap();
    }
    return childs;
  }
  
  private void initChildsMap() {
    childs = new HashMap<Key, Context>();
    NodeList childNodes = element.getChildNodes();
    for (int i = 0 ; i < childNodes.getLength() ; i++) {
      Node childNode = childNodes.item(i);
      if (childNode instanceof Element) { //TODO other types of node
        Element childElement = (Element) childNode;
        Context ctx = contextFactory.create(childElement);
        Key key = ctx.getKey();
        childs.put(key, ctx);
      }
    }
  }
  
  @Override
  public Key getKey() {
    if (key == null) {
      key = keyFactory.create(this);
    }
    return key;
  }

  @Override
  public Map<Key, Context> getChildMap() {
    if (childs == null) {
      initChildsMap();
    }
    return childs;
  }

  @Override
  public Context findChild(Key key) {
    Map<Key, Context> map = getChildMap();
    return map.get(key);
  }

  @Override
  public String getPath() {
    if (path == null) {
      initPath();
    }
    return path;
  }
  
  private void initPath() {
    path = getXPath(getElement(), "");
  }
  
  private String getXPath(Node node, String xpath) {
    if (node == null) {
      return "";
    }
    String elementName = "";
    if (node instanceof Element) {
      elementName = ((Element) node).getNodeName();
    }
    Node parent = node.getParentNode();
    String predicates = "";
    if (parent == null) {
      return xpath;
    } else if (parent instanceof Element) {
      predicates = getPredicatesString((Element) parent);
    } else {
      LOG.warn("Unknown element parent (class:" + parent.getClass() + ")");
    }
    StringBuilder currentXpathBuilder = new StringBuilder()
        .append("/")
        .append(predicates)
        .append(elementName)
        .append(xpath);
    return getXPath(parent, currentXpathBuilder.toString());
  }

  private String getPredicatesString(Element parent) {
    Integer elementIndex = getElementIndex(parent);
    if (elementIndex == null) {
      return "";
    } else {
      return new StringBuilder()
          .append("[")
          .append(elementIndex.toString())
          .append("]")
          .toString();
    }
  }
  
  private Integer getElementIndex(Element parent) {
    NodeList childNodes = parent.getChildNodes();
    if (childNodes.getLength() == 0) {
      return null;
    }
    int count = 0;
    for (int i = 0 ; i < childNodes.getLength() ; i++) {
      Node child = childNodes.item(i);
      if (child == getElement()) {
        return count;
      } else if (child instanceof Element &&
          child.getNodeName().contentEquals(getElement().getNodeName())) {
        count++;
      }
    }
    return count;
  }

  @Override
  public void addChild(Context context) {
    childs.put(context.getKey(), context);
    element.appendChild(context.getElement());
  }

  @Override
  public void removeChilds(Iterable<Context> childsToRemove) {
    for (Context childToRemove : childsToRemove) {
      childs.remove(childToRemove.getKey());
      element.removeChild(childToRemove.getElement());
    }
  }

  @Override
  public boolean containsChild(Key child) {
    Map<Key, Context> map = getChildMap();
    return map.containsKey(key);
  }

  @Override
  public String toString() {
    if (element == null) {
      return "Context[WARN: element is null]";
    }
    StringBuilder builder = new StringBuilder()
    .append("Context[element name:")
    .append(element.getNodeName())
    .append(" Key:")
    .append(getKey().toString())
    .append("]");
    
    return builder.toString();
  }
}
