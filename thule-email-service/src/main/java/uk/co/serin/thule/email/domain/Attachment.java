package uk.co.serin.thule.email.domain;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public final class Attachment {
    public static final String ENTITY_ATTRIBUTE_NAME_CONTENT = "content";
    public static final String ENTITY_ATTRIBUTE_NAME_LABEL = "label";
    public static final String ENTITY_NAME_ATTACHMENTS = "attachments";
    private final byte[] content;
    private final String label;

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

    public static final class AttachmentBuilder {
        private byte[] content;
        private String label;

        private AttachmentBuilder() {
        }

        public static AttachmentBuilder anAttachment() {
            return new AttachmentBuilder();
        }

        public Attachment build() {
            return new Attachment(content, label);
        }

        public AttachmentBuilder withContent(byte[] content) {
            this.content = content;
            return this;
        }

        public AttachmentBuilder withLabel(String label) {
            this.label = label;
            return this;
        }
    }
}
