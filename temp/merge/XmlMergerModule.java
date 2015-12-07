package pl.tecna.aurea.engine.forms.merge;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import pl.tecna.aurea.engine.forms.merge.impl.AttributeMergerImpl;
import pl.tecna.aurea.engine.forms.merge.impl.ContextImpl;
import pl.tecna.aurea.engine.forms.merge.impl.ContextMergerImpl;
import pl.tecna.aurea.engine.forms.merge.impl.PathKey;
import pl.tecna.aurea.engine.forms.merge.impl.XmlMergerImpl;

public class XmlMergerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AttributeMerger.class).to(AttributeMergerImpl.class).in(Singleton.class);
//    bind(Context.class).to(ContextImpl.class);
    bind(ContextMerger.class).to(ContextMergerImpl.class).in(Singleton.class);
    bind(XmlMerger.class).to(XmlMergerImpl.class).in(Singleton.class);
    
    install(new FactoryModuleBuilder()
        .implement(Context.class, ContextImpl.class)
        .build(ContextFactory.class));
    
    install(new FactoryModuleBuilder()
        .implement(Key.class, PathKey.class)
        .build(KeyFactory.class));
  }

}
