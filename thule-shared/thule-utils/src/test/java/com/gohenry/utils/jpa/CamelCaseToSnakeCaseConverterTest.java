package com.gohenry.utils.jpa;

import com.gohenry.utils.jpa.CamelCaseToSnakeCaseConverter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CamelCaseToSnakeCaseConverterTest {

    private CamelCaseToSnakeCaseConverter sut;

    @Before
    public void init() {
        sut = new CamelCaseToSnakeCaseConverter();
    }

    @Test
    public void when_converting_null_string_then_null_is_returned() {

        //Given
        String text = null;

        //When
        var snakeCasedText = sut.convert(text);

        //Then
        assertThat(snakeCasedText).isNull();

    }

    @Test
    public void when_converting_camel_cased_text_then_capitals_are_prefixed_with_underscore_and_lowercased() {

        //Given
        var text = "bigBrownFox";

        //When
        var snakeCasedText = sut.convert(text);

        //Then
        assertThat(snakeCasedText).isEqualTo("big_brown_fox");

    }

    @Test
    public void when_converting_text_starting_with_capital_letter_then_result_doesnt_start_with_underscore() {

        //Given
        var text = "BigBrownFox";

        //When
        var snakeCasedText = sut.convert(text);

        //Then
        assertThat(snakeCasedText).isEqualTo("big_brown_fox");

    }

}