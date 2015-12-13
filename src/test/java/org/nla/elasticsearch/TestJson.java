package org.nla.elasticsearch;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TestJson {

    private static int PRETTY_PRINT_INDENT_FACTOR = 4;

    private DocumentBuilderFactory documentBuilderfactory;

    private DocumentBuilder documentBuilder;

    private TransformerFactory transformerFactory = TransformerFactory.newInstance();

    @Before
    public void setUp() {
        try {
            this.documentBuilderfactory = DocumentBuilderFactory.newInstance();
            this.documentBuilder = this.documentBuilderfactory.newDocumentBuilder();
        } catch (Exception e) {
            throw new RuntimeException("Unable to construct ProfileLoader: " + e.getMessage(), e);
        }
    }

    @Test
    public void testTransformXmlToJson() {
        String output = null;
        try {
            Document xmlDocument = this.documentBuilder.parse(new File(getClass().getClassLoader().getResource("person1.xml").getFile()));
            String xmlString = this.convertDocumentToXmlString(xmlDocument);

            JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
            output = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        } catch (Exception e) {
            throw new RuntimeException("Unable to transform xml file as json string: " + e.getMessage(), e);
        }

        assertThat(output, notNullValue());
    }

    private String convertDocumentToXmlString(Document doc) {
        String output = null;
        try {
            Transformer transformer = this.transformerFactory.newTransformer();
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            output = writer.getBuffer().toString();
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to transform document into xml string: " + e.getMessage(), e);
        }
        return output;
    }

}
