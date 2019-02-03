package uk.co.serin.thule.email.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@ToString
public final class Attachment {
    public static final String ENTITY_ATTRIBUTE_NAME_CONTENT = "content";
    public static final String ENTITY_ATTRIBUTE_NAME_LABEL = "label";
    public static final String ENTITY_NAME_ATTACHMENTS = "attachments";
    @EqualsAndHashCode.Include
    private String content;
    @EqualsAndHashCode.Include
    private String label;

}
