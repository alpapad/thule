package uk.co.serin.thule.utils.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlUtilsTest {

    @Test(expected = IllegalStateException.class)
    public void invalid_xml_fails_to_be_reformatted() {
        // Given
        String xml = "<invalid-xml>";

        // When
        XmlUtils.prettyPrint(xml);

        // Then (see expected in @Test annotation)
    }

    @Test
    public void xml_is_indented_and_appears_on_multiple_lines() {
        // Given
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><messages><message1>Hello there</message1><message2>Hello there once more</message2></messages>";
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><messages>\n    <message1>Hello there</message1>\n    <message2>Hello there once more</message2>\n</messages>\n";

        // When
        String actualXml = XmlUtils.prettyPrint(xml);

        // Then
        assertThat(actualXml).isEqualTo(expectedXml);
    }
}