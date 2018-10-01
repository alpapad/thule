package uk.co.serin.thule.utils.jpa;

public class CamelCaseToSnakeCaseConverter {

    public String convert(String name) {

        if (name == null) {
            return null;
        }

        var builder = new StringBuilder(name);

        for (int i = 1; i < builder.length() - 1; i++) {
            if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i))) {
                builder.insert(i++, '_');
            }
        }

        return builder.toString().toLowerCase();

    }

    private boolean isUnderscoreRequired(char before, char current) {
        return Character.isLowerCase(before) && Character.isUpperCase(current);
    }

}