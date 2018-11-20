package uk.co.serin.thule.utils.jpa;

import java.io.Serializable;

public class CamelCaseToSnakeCaseConverter implements Serializable {

    private static final long serialVersionUID = 2854806819239451467L;

    public String convert(String name) {

        if (name == null) {
            return null;
        }

        var builder = new StringBuilder(name);

        int index = 1;
        while (index < builder.length() - 1) {
            if (isUnderscoreRequired(builder.charAt(index - 1), builder.charAt(index))) {
                builder.insert(index++, '_');
            }
            index++;
        }

        return builder.toString().toLowerCase();

    }

    private boolean isUnderscoreRequired(char before, char current) {
        return Character.isLowerCase(before) && Character.isUpperCase(current);
    }

}