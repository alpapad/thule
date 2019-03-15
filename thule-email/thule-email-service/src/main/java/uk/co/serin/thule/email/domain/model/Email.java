package uk.co.serin.thule.email.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
public class Email {
    private static final String INVALID_EMAIL_ADDRESS_MESSAGE = "Email address should be of username@domain.extension format";

    @Builder.Default
    private Set<Attachment> attachments = new HashSet<>();

    @Builder.Default
    @Valid
    private Set<@javax.validation.constraints.Email(message = INVALID_EMAIL_ADDRESS_MESSAGE) String> bccs = new HashSet<>();

    @NotEmpty
    private String body;

    @Builder.Default
    @Valid
    private Set<@javax.validation.constraints.Email(message = INVALID_EMAIL_ADDRESS_MESSAGE) String> ccs = new HashSet<>();

    @EqualsAndHashCode.Include
    private String from;

    @EqualsAndHashCode.Include
    @NotEmpty
    private String subject;

    @Builder.Default
    @Valid
    private Set<@javax.validation.constraints.Email(message = INVALID_EMAIL_ADDRESS_MESSAGE) String> tos = new HashSet<>();
}
