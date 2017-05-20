package uk.co.serin.thule.core.validator;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PatternJava8Validator implements ConstraintValidator<PatternJava8, CharSequence> {
    private Pattern pattern;

    @Override
    public void initialize(PatternJava8 parameters) {
        int intFlag = 0;

        try {
            pattern = Pattern.compile(parameters.regexp(), intFlag);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
