package pl.tecna.xml.impl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.tecna.xml.AttributeMerger;
import pl.tecna.xml.Context;
import pl.tecna.xml.ContextMerger;
import pl.tecna.xml.Key;

/**
 * laczenie danych kilku formularzy, algorytm:
 * 1 pobranie node-a wszystkich kontekstow
 * 2 porownanie atrybutow i merge atrybutow
 * 3 sprawdzenie childow i wybranie czy maja byc laczone, czy dodawane do aktualnego, czy usuwane
 *  a) jezeli dodawane to element jest dodawany wraz z wszystkimi child-ami
 *  b) jezeli sa laczone to przeskok do pierwszego kroku
 * 
 * SOURCE: element zrodlowy, jesli laczonych jest kilka sciezek rownoleglych, to 
 * jest to element przed rozdzieleniem sterowania
 * ACTUAL: aktualny laczony element
 * MERGED: aktualny wynik laczenia
 * 
 * 
 * @author Kamil Kurek
 *
 */
public class ContextMergerImpl implements ContextMerger {

  private Logger log = LoggerFactory.getLogger(ContextMergerImpl.class);
  
  @Inject
  private AttributeMerger attributeMerger;
  
  @Inject
  private ContextMerger contextMerger;
  
  @Override
  public void merge(Context source, Context actual, Context merged) {
    logCurrent(source, actual, merged);
    doMerge(source, actual, merged);
  }
  
  private void doMerge(Context source, Context actual, Context merged) {
    attributeMerger.mergeAttributes(source.getElement(), actual.getElement(), merged.getElement());
    removeMissingChilds(source, actual, merged);
    updateChilds(source, actual, merged);
    addNewChilds(source, actual, merged);
  }
  
  private void removeMissingChilds(Context source, Context actual, Context merged) {
    List<Context> toRemove = new LinkedList<Context>();
    for (Context mergedChild : merged.getChildMap().values()) {
      Key mergedChildKey = mergedChild.getKey();
      if (!actual.containsChild(mergedChildKey) && source.containsChild(mergedChildKey)) {
        toRemove.add(mergedChild);
      }
    }
    merged.removeChilds(toRemove);
  }
  
  private void updateChilds(Context source, Context actual, Context merged) {
    for (Context actualChild : actual.getChildMap().values()) {
      Key actualChildKey = actualChild.getKey();
      if (merged.containsChild(actualChildKey)) {
        Context sourceChild = source.findChild(actualChildKey);
        Context mergedChild = merged.findChild(actualChildKey);
        contextMerger.merge(sourceChild, actualChild, mergedChild);
      } else if (!source.containsChild(actualChildKey)) {
        merged.addChild(actualChild);
      } else if (source.containsChild(actualChildKey)) {
        //TODO check if both xml are the same, if true then element is not added
      }
    }
  }
  
  private void addNewChilds(Context source, Context actual, Context merged) {
    for (Context mergedChild : merged.getChildMap().values()) {
      Key mergedChildKey = mergedChild.getKey();
      if (actual.findChild(mergedChildKey) == null && source.findChild(mergedChildKey) == null) {
        actual.addChild(mergedChild);
      }
    }
  }
  
  private void logCurrent(Context source, Context actual, Context merged) {
    log.info("MERGE elements (SOURCE:{}, ACTUAL:{}, MERGED:{}, should merge:{})", source, actual, merged);
  }

}
