package uk.co.serin.thule.people.domain.entity.state;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StateCode {
    ADDRESS_DISABLED,
    ADDRESS_DISCARDED,
    ADDRESS_ENABLED,
    PERSON_DISABLED,
    PERSON_DISCARDED,
    PERSON_ENABLED,
}
