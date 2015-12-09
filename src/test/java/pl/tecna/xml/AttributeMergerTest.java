package pl.tecna.xml;

import java.io.IOException;

import javax.inject.Inject;

import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;

@RunWith(JukitoRunner.class)
@UseModules({XmlMergerModule.class, DOMDocumentBuilderModule.class})
public class AttributeMergerTest extends DocumentReader {

  private static final String TEST_FILE_1_NAME = "attributesTestXml1.xml";
  private static final String TEST_FILE_2_NAME = "attributesTestXml2.xml";

  @Inject
  private AttributeMerger attributeMerger;

  @Test
  public void mergeAttributesTest() throws IOException {
    Element test1Element = readDocumentElement(TEST_FILE_1_NAME);
    Element test2Element = readDocumentElement(TEST_FILE_2_NAME);
    attributeMerger.mergeAttributes(test1Element, test2Element, test2Element);
    testAttribute(test2Element, "attribute1", "TEST2");
    Assert.assertFalse(test2Element.hasAttribute("attribute2"));
    testAttribute(test2Element, "attribute3", "TEST3");
    testAttribute(test2Element, "attribute4", "TEST4");
    Assert.assertFalse(test2Element.hasAttribute("attribute5"));
  }

  private void testAttribute(Element element, String name, String value) {
    Assert.assertTrue(element.hasAttribute(name));
    Assert.assertEquals(value, element.getAttribute(name));
  }
  
}
