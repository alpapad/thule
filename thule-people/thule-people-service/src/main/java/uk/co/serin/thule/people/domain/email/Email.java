package uk.co.serin.thule.people.domain.email;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public final class Email {
    public static final String ENTITY_ATTRIBUTE_NAME_BODY = "body";
    public static final String ENTITY_ATTRIBUTE_NAME_SUBJECT = "subject";
    public static final String ENTITY_ATTRIBUTE_NAME_TOS = "tos";

    private final Set<String> tos = new HashSet<>();
    private String body;
    private String subject;

    /**
     * Default constructor required when instantiating as java bean, e.g. by hibernate or jackson
     */
    protected Email() {
    }

    public void addTos(Set<String> tos) {
        this.tos.addAll(tos);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Set<String> getTos() {
        return Collections.unmodifiableSet(tos);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "Email{", "}")
                .add(String.format("body=%s", body))
                .add(String.format("subject=%s", subject))
                .add(String.format("tos=%s", tos))
                .toString();
    }

    public static final class EmailBuilder {
        private String body;
        private String subject;
        private Set<String> tos = new HashSet<>();

        private EmailBuilder() {
        }

        public static EmailBuilder anEmail() {
            return new EmailBuilder();
        }

        public Email build() {
            Email email = new Email();
            email.setBody(body);
            email.setSubject(subject);
            email.addTos(this.tos);
            return email;
        }

        public EmailBuilder withBody(String body) {
            this.body = body;
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
