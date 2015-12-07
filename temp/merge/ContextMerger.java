package pl.tecna.aurea.engine.forms.merge;

public interface ContextMerger {

  void merge(Context source, Context actual, Context merged);
  
}
