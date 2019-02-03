package uk.co.serin.thule.email.domain;

import uk.co.serin.thule.utils.validator.PatternJava8;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString(callSuper = true)
public class Email {
    public static final String ENTITY_ATTRIBUTE_NAME_BCCS = "bccs";
    public static final String ENTITY_ATTRIBUTE_NAME_BODY = "body";
    public static final String ENTITY_ATTRIBUTE_NAME_CCS = "ccs";
    public static final String ENTITY_ATTRIBUTE_NAME_FROM = "from";
    public static final String ENTITY_ATTRIBUTE_NAME_SUBJECT = "subject";
    public static final String ENTITY_ATTRIBUTE_NAME_TOS = "tos";
    public static final String ENTITY_NAME_EMAILS = "emails";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Set<Attachment> attachments = new HashSet<>();
    @Valid
    private Set<@PatternJava8(regexp = EMAIL_PATTERN, message = "Email address should be of username@domain.extension format") String> bccs =
            new HashSet<>();
    @Valid
    private Set<@PatternJava8(regexp = EMAIL_PATTERN, message = "Email address should be of username@domain.extension format") String> ccs =
            new HashSet<>();
    @Valid
    private Set<@PatternJava8(regexp = EMAIL_PATTERN, message = "Email address should be of username@domain.extension format") String> tos =
            new HashSet<>();
    @NotNull
    private String body;
    @EqualsAndHashCode.Include
    private String from;
    @EqualsAndHashCode.Include
    @NotNull
    private String subject;
}
