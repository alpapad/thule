package uk.co.serin.thule.email.domain;

import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public final class Attachment {
    private final byte[] content;
    private final String label;

    /**
     * Copy object constructor
     *
     * @param attachment Object to be copied
     */
    public Attachment(Attachment attachment) {
        // Copy business key
        this.content = attachment.content;
        this.label = attachment.label;
        // Copy mutable properties
        BeanUtils.copyProperties(attachment, this);
    }

    /**
     * Business key constructor
     *
     * @param content Business key attribute
     * @param label   Business key attribute
     */
    public Attachment(byte[] content, String label) {
        this.content = Arrays.copyOf(content, content.length);
        this.label = label;
    }

    public byte[] getContent() {
        return Arrays.copyOf(content, content.length);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(content), label);
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
        return Arrays.equals(content, that.content) && Objects.equals(label, that.label);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Attachment{", "}")
                .add(String.format("content=%s", content))
                .add(String.format("label=%s", label))
                .toString();
    }
}
