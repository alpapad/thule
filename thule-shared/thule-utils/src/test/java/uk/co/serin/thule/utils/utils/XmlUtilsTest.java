package uk.co.serin.thule.utils.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class XmlUtilsTest {

    @Test
    public void given_invalid_xml_when_pretty_print_then_an_illegal_state_exception_is_thrown() {
        // Given
        var xml = "<invalid-xml>";

        // When
        var throwable = catchThrowable(() -> XmlUtils.prettyPrint(xml));

        // Then
        assertThat(throwable).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void given_valid_xml_when_pretty_print_then_it_is_indented_and_appears_on_multiple_lines() {
        // Given
        var xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><messages><message1>Hello there</message1><message2>Hello there once more</message2></messages>";
        var expectedXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><messages>\n    <message1>Hello there</message1>\n    <message2>Hello there once more</message2>\n</messages>\n";

        // When
        var actualXml = XmlUtils.prettyPrint(xml);

        // Then
        assertThat(actualXml).isEqualTo(expectedXml);
    }
}