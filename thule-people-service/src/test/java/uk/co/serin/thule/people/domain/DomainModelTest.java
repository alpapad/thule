package uk.co.serin.thule.people.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DomainModelTest {

    @Test
    public void getters_and_setters_operate_on_the_same_field() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        String updatedBy = "updatedBy";

        DomainModel domainModel = new DomainModel() {
        };
        ReflectionTestUtils.setField(domainModel, DomainModel.ENTITY_ATTRIBUTE_NAME_CREATED_AT, now);
        ReflectionTestUtils.setField(domainModel, DomainModel.ENTITY_ATTRIBUTE_NAME_ID, 1L);
        ReflectionTestUtils.setField(domainModel, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_AT, now);
        ReflectionTestUtils.setField(domainModel, DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, updatedBy);
        ReflectionTestUtils.setField(domainModel, DomainModel.ENTITY_ATTRIBUTE_NAME_VERSION, 1L);

        // When/then
        assertThat(domainModel.getCreatedAt()).isEqualTo(now);
        assertThat(domainModel.getId()).isEqualTo(1L);
        assertThat(domainModel.getUpdatedAt()).isEqualTo(now);
        assertThat(domainModel.getUpdatedBy()).isEqualTo(updatedBy);
        assertThat(domainModel.getVersion()).isEqualTo(1L);
    }

    @Test
    public void toString_is_overridden() {
        assertThat(new DomainModel() {
        }.toString()).contains(DomainModel.ENTITY_ATTRIBUTE_NAME_ID);
    }
}
