package uk.co.serin.thule.email.domain;

import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.StringJoiner;

import javax.validation.ValidationException;

public final class Attachment {
    public static final String ENTITY_ATTRIBUTE_NAME_CONTENT = "content";
    public static final String ENTITY_ATTRIBUTE_NAME_LABEL = "label";
    public static final String ENTITY_NAME_ATTACHMENTS = "attachments";
    private String content;
    private String label;

    /**
     * Default constructor required when instantiating as java bean, e.g. by hibernate or jackson
     */
    protected Attachment() {
    }

    /**
     * Business key constructor
     *
     * @param content Business key attribute
     * @param label   Business key attribute
     */
    public Attachment(String content, String label) {
        if (StringUtils.hasText(content)) {
            this.content = content;
        } else {
            throw new ValidationException("The 'content' is mandatory");
        }
        if (StringUtils.hasText(label)) {
            this.label = label;
        } else {
            throw new ValidationException("The 'label' is mandatory");
        }
    }

    public String getContent() {
        return content;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attachment that = (Attachment) o;
        return Objects.equals(content, that.content) && Objects.equals(label, that.label);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Attachment{", "}")
                .add(String.format("content=%s", content))
                .add(String.format("label=%s", label))
                .toString();
    }
}
