package uk.co.serin.thule.utils.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public interface XmlUtils {
    static String prettyPrint(String xml) {
        try {
            var unformattedXml = new StreamSource(new StringReader(xml));
            var formattedXml = new StreamResult(new StringWriter());

            var transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            var transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(unformattedXml, formattedXml);

            return formattedXml.getWriter().toString();
        } catch (TransformerException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}