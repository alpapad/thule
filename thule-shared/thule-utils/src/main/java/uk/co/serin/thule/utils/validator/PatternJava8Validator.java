package uk.co.serin.thule.utils.validator;


import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatternJava8Validator implements ConstraintValidator<PatternJava8, CharSequence> {
    private Pattern pattern;

    @Override
    public void initialize(PatternJava8 parameters) {
        try {
            pattern = Pattern.compile(parameters.regexp());
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        var matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
