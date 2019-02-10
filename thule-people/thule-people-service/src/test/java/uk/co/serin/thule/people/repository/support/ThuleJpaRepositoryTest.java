package uk.co.serin.thule.people.repository.support;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import uk.co.serin.thule.people.domain.entity.AuditEntity;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ThuleJpaRepositoryTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    private JpaEntityInformation<Object, Serializable> jpaEntityInformation;
    private ThuleJpaRepository<Object, Serializable> thuleJpaRepository;
    @Mock
    private TypedQuery<Serializable> typedQuery;

    @Before
    public void setUp() {
        given(entityManager.getDelegate()).willReturn(new Object());
        thuleJpaRepository = new ThuleJpaRepository<>(jpaEntityInformation, entityManager);
    }

    @Test
    public void when_delete_by_updated_by_then_delete_is_executed() {
        // Given
        String userId = "userId";

        given(entityManager.createQuery(anyString())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);

        // When
        thuleJpaRepository.deleteByUpdatedBy(userId);

        // Then
        verify(entityManager).createQuery(anyString());
        verify(typedQuery).setParameter(AuditEntity.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, userId);
        verify(typedQuery).executeUpdate();
    }
}