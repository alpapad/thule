package com.gohenry.utils.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PatternJava8ValidatorTest {
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    @Mock
    private Matcher matcher;
    @Mock
    private PatternJava8 parameters;
    @Mock
    private Pattern pattern;
    @InjectMocks
    private PatternJava8Validator patternJava8Validator;
    @Mock
    private CharSequence value;

    @Test
    public void initialize_sets_pattern() {
        // Given
        given(parameters.regexp()).willReturn("Regex");

        // When
        patternJava8Validator.initialize(parameters);

        // Then
        Object pattern = ReflectionTestUtils.getField(patternJava8Validator, "pattern");
        assertThat(pattern).isNotNull();
    }

    @Test
    public void null_passes_validation() {
        // Given

        // When
        boolean valid = patternJava8Validator.isValid(null, constraintValidatorContext);

        // Then
        assertThat(valid).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void patternsyntaxException_results_in_illegalargumentexception() {
        // Given
        given(parameters.regexp()).willThrow(new PatternSyntaxException(null, null, 0));

        // When
        patternJava8Validator.initialize(parameters);

        // Then (see expected in @Test annotation)
    }

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(patternJava8Validator, "pattern", pattern);
    }

    @Test
    public void valid_pattern_passes_validation() {
        // Given
        given(pattern.matcher(value)).willReturn(matcher);
        given(matcher.matches()).willReturn(true);

        // When
        boolean valid = patternJava8Validator.isValid(value, constraintValidatorContext);

        // Then
        assertThat(valid).isTrue();
    }
}