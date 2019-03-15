package uk.co.serin.thule.people.domain.model.email;

import java.util.HashSet;
import java.util.Set;

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
@ToString(onlyExplicitlyIncluded = true)
public class Email {
    public static final String ENTITY_ATTRIBUTE_NAME_BODY = "body";
    public static final String ENTITY_ATTRIBUTE_NAME_SUBJECT = "subject";
    public static final String ENTITY_ATTRIBUTE_NAME_TOS = "tos";
    private String body;
    private String subject;
    @Builder.Default
    private Set<String> tos = new HashSet<>();
}
