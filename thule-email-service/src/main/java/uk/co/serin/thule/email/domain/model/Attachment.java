package uk.co.serin.thule.email.domain.model;

import javax.validation.constraints.NotEmpty;

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
    @EqualsAndHashCode.Include
    @NotEmpty
    private String content;

    @EqualsAndHashCode.Include
    @NotEmpty
    private String label;
}
