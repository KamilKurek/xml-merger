package pl.tecna.xml;

import javax.inject.Singleton;

import pl.tecna.xml.impl.AttributeMergerImpl;
import pl.tecna.xml.impl.ContextImpl;
import pl.tecna.xml.impl.ContextMergerImpl;
import pl.tecna.xml.impl.PathKey;
import pl.tecna.xml.impl.XmlMergerImpl;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class XmlMergerModule extends AbstractModule {
	
  @Override
  protected void configure() {
    bind(AttributeMerger.class).to(AttributeMergerImpl.class).in(Singleton.class);
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
