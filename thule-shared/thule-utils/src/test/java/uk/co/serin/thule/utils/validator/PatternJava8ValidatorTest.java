package uk.co.serin.thule.utils.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
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
    public void given_invalid_pattern_syntax_when_initialize_then_an_illegal_argument_exception_is_thrown() {
        // Given
        given(parameters.regexp()).willThrow(new PatternSyntaxException(null, null, 0));

        // When
        var throwable = catchThrowable(() -> patternJava8Validator.initialize(parameters));

        // Then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void given_null_value_when_checking_is_valid_then_validation_passes() {
        // When
        var valid = patternJava8Validator.isValid(null, constraintValidatorContext);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void given_valid_value_when_checking_is_valid_then_validation_passes() {
        // Given
        given(pattern.matcher(value)).willReturn(matcher);
        given(matcher.matches()).willReturn(true);

        // When
        var valid = patternJava8Validator.isValid(value, constraintValidatorContext);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void when_initialize_then_regular_expression_pattern_is_set() {
        // Given
        given(parameters.regexp()).willReturn("Regex");

        // When
        patternJava8Validator.initialize(parameters);

        // Then
        var pattern = ReflectionTestUtils.getField(patternJava8Validator, "pattern");
        assertThat(pattern).isNotNull();
    }
}