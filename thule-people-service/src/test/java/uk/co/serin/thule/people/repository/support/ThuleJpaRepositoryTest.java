package uk.co.serin.thule.people.repository.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ThuleJpaRepositoryTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    private JpaEntityInformation<Object, Serializable> jpaEntityInformation;
    private ThuleJpaRepository<Object, Serializable> thuleJpaRepository;
    @Mock
    private TypedQuery<Serializable> typedQuery;

    @BeforeEach
    public void beforeEach() {
        given(entityManager.getDelegate()).willReturn(new Object());
        thuleJpaRepository = new ThuleJpaRepository<>(jpaEntityInformation, entityManager);
    }

    @Test
    public void when_delete_by_updated_by_then_delete_is_executed() {
        // Given
        var userId = "userId";

        given(entityManager.createQuery(anyString())).willReturn(typedQuery);
        given(typedQuery.setParameter(anyString(), any())).willReturn(typedQuery);

        // When
        thuleJpaRepository.deleteByUpdatedBy(userId);

        // Then
        verify(entityManager).createQuery(anyString());
        verify(typedQuery).setParameter("updatedBy", userId);
        verify(typedQuery).executeUpdate();
    }
}