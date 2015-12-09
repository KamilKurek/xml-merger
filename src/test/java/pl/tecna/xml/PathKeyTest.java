package pl.tecna.xml;

import java.io.IOException;

import javax.inject.Inject;

import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

@RunWith(JukitoRunner.class)
@UseModules({XmlMergerModule.class, DOMDocumentBuilderModule.class})
public class PathKeyTest extends DocumentReader {
  
  private static final String TEST_XML_NAME = "pathKeyTest.xml";
  
  private static final Logger LOG = LoggerFactory.getLogger(PathKeyTest.class);
  
  @Inject
  private ContextFactory contextFactory;
  
  @Test
  public void testPathRead() throws IOException {
    Element testEl = readDocumentElement(TEST_XML_NAME);
    Context testCtx = contextFactory.create(testEl);
    LOG.info("@@@@ CONTEXT ELEMENT: " + testCtx.getElement().getClass());
    logCtxPath(0, testCtx);
  }
  
  private void logCtxPath(int level, Context ctx) {
    LOG.info(getPrefix(level) + ":" + ctx.getPath() + " =======" + ctx.getElement());
    level += 1;
    for (Context childCtx : ctx.getChildMap().values()) {
      logCtxPath(level, childCtx);
    }
  }

  private String getPrefix(int level) {
    String result = "";
    for (int i = 0 ; i < level ; i++) {
      result += "-";
    }
    return result;
  }
  
}
