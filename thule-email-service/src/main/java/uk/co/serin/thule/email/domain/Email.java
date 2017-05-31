package uk.co.serin.thule.email.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public final class Email {
    public static final String ENTITY_ATTRIBUTE_NAME_BCCS = "bccs";
    public static final String ENTITY_ATTRIBUTE_NAME_BODY = "body";
    public static final String ENTITY_ATTRIBUTE_NAME_CCS = "ccs";
    public static final String ENTITY_ATTRIBUTE_NAME_FROM = "from";
    public static final String ENTITY_ATTRIBUTE_NAME_SUBJECT = "subject";
    public static final String ENTITY_ATTRIBUTE_NAME_TOS = "tos";
    public static final String ENTITY_NAME_EMAILS = "emails";
    private final Set<Attachment> attachments = new HashSet<>();
    private final Set<String> bccs = new HashSet<>();
    private final Set<String> ccs = new HashSet<>();
    private final String from;
    private final String subject;
    private final Set<String> tos = new HashSet<>();
    private String body;

    /**
     * Business key constructor
     *
     * @param from    Business key attribute
     * @param subject Business key attribute
     */
    public Email(String from, String subject) {
        this.from = from;
        this.subject = subject;
    }

    public void addAttachments(Set<Attachment> attachments) {
        this.attachments.addAll(attachments);
    }

    public void addBccs(Set<String> bccs) {
        this.bccs.addAll(bccs);
    }

    public void addCcs(Set<String> ccs) {
        this.ccs.addAll(ccs);
    }

    public void addTos(Set<String> tos) {
        this.tos.addAll(tos);
    }

    public Set<Attachment> getAttachments() {
        return Collections.unmodifiableSet(attachments);
    }

    public Set<String> getBccs() {
        return Collections.unmodifiableSet(bccs);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Set<String> getCcs() {
        return Collections.unmodifiableSet(ccs);
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public Set<String> getTos() {
        return Collections.unmodifiableSet(tos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email = (Email) o;
        return Objects.equals(from, email.from) && Objects.equals(subject, email.subject);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Email{", "}")
                .add(String.format("attachments=%s", attachments))
                .add(String.format("bccs=%s", bccs))
                .add(String.format("body=%s", body))
                .add(String.format("ccs=%s", ccs))
                .add(String.format("from=%s", from))
                .add(String.format("subject=%s", subject))
                .add(String.format("tos=%s", tos))
                .toString();
    }

    public static final class EmailBuilder {
        private Set<Attachment> attachments = new HashSet<>();
        private Set<String> bccs = new HashSet<>();
        private String body;
        private Set<String> ccs = new HashSet<>();
        private String from;
        private String subject;
        private Set<String> tos = new HashSet<>();

        private EmailBuilder() {
        }

        public static EmailBuilder anEmail() {
            return new EmailBuilder();
        }

        public Email build() {
            Email email = new Email(from, subject);
            email.setBody(body);
            email.addBccs(this.bccs);
            email.addAttachments(this.attachments);
            email.addTos(this.tos);
            email.addCcs(this.ccs);
            return email;
        }

        public EmailBuilder withAttachments(Set<Attachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public EmailBuilder withBccs(Set<String> bccs) {
            this.bccs = bccs;
            return this;
        }

        public EmailBuilder withBody(String body) {
            this.body = body;
            return this;
        }

        public EmailBuilder withCcs(Set<String> ccs) {
            this.ccs = ccs;
            return this;
        }

        public EmailBuilder withFrom(String from) {
            this.from = from;
            return this;
        }

        public EmailBuilder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailBuilder withTos(Set<String> tos) {
            this.tos = tos;
            return this;
        }
    }
}
