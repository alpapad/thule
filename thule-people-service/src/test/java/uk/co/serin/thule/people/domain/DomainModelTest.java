package uk.co.serin.thule.people.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DomainModelTest {

    @Test
    public void gettersAndSettersOperateOnTheSameField() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        String updatedBy = "updatedBy";

        DomainModel domainModel = new DomainModel() {
            private static final long serialVersionUID = -5732896444138835908L;
        };
        domainModel.setCreatedAt(now);
        domainModel.setId(1L);
        domainModel.setUpdatedAt(now);
        domainModel.setUpdatedBy(updatedBy);
        domainModel.setVersion(1L);

        // When/then
        assertThat(domainModel.getCreatedAt()).isEqualTo(now);
        assertThat(domainModel.getId()).isEqualTo(1L);
        assertThat(domainModel.getUpdatedAt()).isEqualTo(now);
        assertThat(domainModel.getUpdatedBy()).isEqualTo(updatedBy);
        assertThat(domainModel.getVersion()).isEqualTo(1L);
    }

    @Test
    public void toStringIsOverridden() {
        assertThat(new DomainModel() {
            private static final long serialVersionUID = -7917846140115672916L;
        }.toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_ID);
    }
}
