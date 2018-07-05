package uk.co.serin.thule.email.domain;

import org.springframework.util.StringUtils;

import uk.co.serin.thule.utils.validator.PatternJava8;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

public final class Email {
    public static final String ENTITY_ATTRIBUTE_NAME_BCCS = "bccs";
    public static final String ENTITY_ATTRIBUTE_NAME_BODY = "body";
    public static final String ENTITY_ATTRIBUTE_NAME_CCS = "ccs";
    public static final String ENTITY_ATTRIBUTE_NAME_FROM = "from";
    public static final String ENTITY_ATTRIBUTE_NAME_SUBJECT = "subject";
    public static final String ENTITY_ATTRIBUTE_NAME_TOS = "tos";
    public static final String ENTITY_NAME_EMAILS = "emails";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Set<Attachment> attachments = new HashSet<>();
    @Valid
    private final Set<@PatternJava8(regexp = EMAIL_PATTERN, message = "Email address should be of username@domain.extension format") String> bccs = new HashSet<>();
    @Valid
    private final Set<@PatternJava8(regexp = EMAIL_PATTERN, message = "Email address should be of username@domain.extension format") String> ccs = new HashSet<>();
    @Valid
    private final Set<@PatternJava8(regexp = EMAIL_PATTERN, message = "Email address should be of username@domain.extension format") String> tos = new HashSet<>();
    @NotNull
    private String body;
    private String from;
    @NotNull
    private String subject;

    /**
     * Default constructor required when instantiating as java bean, e.g. by hibernate or jackson
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    protected Email() {
    }

    /**
     * Business key constructor
     *
     * @param from    Business key attribute
     * @param subject Business key attribute
     */
    @SuppressWarnings("squid:S2637")
    // Suppress SonarQube bug "@NonNull" values should not be set to null
    public Email(String from, String subject) {
        if (StringUtils.hasText(from)) {
            this.from = from;
        } else {
            throw new ValidationException("The 'from' email address is mandatory");
        }
        if (StringUtils.hasText(subject)) {
            this.subject = subject;
        } else {
            throw new ValidationException("The 'subject' is mandatory");
        }
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public boolean hasARecipient() {
        return !this.getTos().isEmpty() || !this.getCcs().isEmpty() || !this.getBccs().isEmpty();
    }

    public Set<String> getTos() {
        return Collections.unmodifiableSet(tos);
    }

    public Set<String> getCcs() {
        return Collections.unmodifiableSet(ccs);
    }

    public Set<String> getBccs() {
        return Collections.unmodifiableSet(bccs);
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
