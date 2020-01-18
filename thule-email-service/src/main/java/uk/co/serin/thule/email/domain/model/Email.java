package uk.co.serin.thule.email.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("Attachments to include in the email")
    @Builder.Default
    private Set<Attachment> attachments = new HashSet<>();

    @ApiModelProperty("Blind courtesy copy recipient email addresses")
    @Builder.Default
    @Valid
    private Set<@javax.validation.constraints.Email(message = INVALID_EMAIL_ADDRESS_MESSAGE) String> bccs = new HashSet<>();

    @ApiModelProperty("Body or content of the email")
    @NotEmpty
    private String body;

    @ApiModelProperty("Courtesy copy recipient email addresses")
    @Builder.Default
    @Valid
    private Set<@javax.validation.constraints.Email(message = INVALID_EMAIL_ADDRESS_MESSAGE) String> ccs = new HashSet<>();

    @ApiModelProperty("Senders email address")
    @EqualsAndHashCode.Include
    private String from;

    @ApiModelProperty("Email subject line")
    @EqualsAndHashCode.Include
    @NotEmpty
    private String subject;

    @ApiModelProperty("Recipient email addresses")
    @Builder.Default
    @Valid
    private Set<@javax.validation.constraints.Email(message = INVALID_EMAIL_ADDRESS_MESSAGE) String> tos = new HashSet<>();
}
