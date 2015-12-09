package pl.tecna.xml;

import java.util.Map;

import org.w3c.dom.Element;

public interface Context {

  Element getElement();
  
  Key getKey();
  
  Map<Key, Context> getChildMap();
  
  Context findChild(Key key);
  
  boolean containsChild(Key child);
  
  void addChild(Context context);
  
  String getPath();
  
  void removeChilds(Iterable<Context> childs);
  
}
