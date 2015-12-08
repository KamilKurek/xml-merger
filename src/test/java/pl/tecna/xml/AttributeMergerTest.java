package pl.tecna.xml;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.xml.parsers.DocumentBuilder;

import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@RunWith(JukitoRunner.class)
@UseModules({XmlMergerModule.class, DOMDocumentBuilderModule.class})
public class AttributeMergerTest {

	private static final String TEST_FILE_1_NAME = "attributesTestXml1.xml";
	private static final String TEST_FILE_2_NAME = "attributesTestXml2.xml";

	@Inject
	private Provider<DocumentBuilder> documentBuilderProvider;

	@Inject
	private AttributeMerger attributeMerger;

	@Test
	public void mergeAttributesTest() throws IOException {
		Element test1Element = readDocumentElement(TEST_FILE_1_NAME);
		Element test2Element = readDocumentElement(TEST_FILE_2_NAME);
		attributeMerger.mergeAttributes(test1Element, test2Element, test2Element);
	}

	private Element readDocumentElement(String fileName) throws IOException {
		try {
			return documentBuilderProvider.get().parse(new InputSource(
					new InputStreamReader(AttributeMergerTest.class.getResourceAsStream(fileName))))
					.getDocumentElement();
		} catch (SAXException e) {
			throw new IllegalArgumentException("Could not parse data");
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not read data");
		}
	}

}
