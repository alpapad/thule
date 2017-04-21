package uk.co.serin.thule.email.domain;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Email {
    private final String from;
    private final String subject;
    private final List<Attachment> attachments = new ArrayList<>();
    private final List<String> bccs = new ArrayList<>();
    private String body;
    private final List<String> ccs = new ArrayList<>();
    private final List<String> tos = new ArrayList<>();

    /**
     * Copy object constructor
     *
     * @param email Object to be copied
     */
    public Email(Email email) {
        // Copy business key
        this.from = email.from;
        this.subject = email.subject;
        // Copy mutable properties
        BeanUtils.copyProperties(email, this);
    }

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

    public List<Attachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    public void addAttachments(Stream<Attachment> attachments) {
        this.attachments.addAll(attachments.collect(Collectors.toList()));
    }

    public List<String> getBccs() {
        return Collections.unmodifiableList(bccs);
    }

    public void addBccs(Stream<String> bccs) {
        this.bccs.addAll(bccs.collect(Collectors.toList()));
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getCcs() {
        return Collections.unmodifiableList(ccs);
    }

    public void addCcs(Stream<String> ccs) {
        this.ccs.addAll(ccs.collect(Collectors.toList()));
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public List<String> getTos() {
        return Collections.unmodifiableList(tos);
    }

    public void addTos(Stream<String> tos) {
        this.tos.addAll(tos.collect(Collectors.toList()));
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
}
