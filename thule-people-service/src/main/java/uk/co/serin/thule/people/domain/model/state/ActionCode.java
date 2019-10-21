package uk.co.serin.thule.people.domain.model.state;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ActionCode {
    ADDRESS_DISABLE,
    ADDRESS_DISCARD,
    ADDRESS_ENABLE,
    ADDRESS_RECOVER,
    ADDRESS_UPDATE,
    ADDRESS_VIEW,
    PERSON_DISABLE,
    PERSON_DISCARD,
    PERSON_ENABLE,
    PERSON_RECOVER,
    PERSON_UPDATE,
    PERSON_VIEW,
}
