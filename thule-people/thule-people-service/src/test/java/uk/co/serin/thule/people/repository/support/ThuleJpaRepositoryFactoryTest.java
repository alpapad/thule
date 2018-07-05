package uk.co.serin.thule.people.repository.support;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import uk.co.serin.thule.people.domain.DomainModel;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.Metamodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ThuleJpaRepositoryFactoryTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    private JpaEntityInformation<Object, Serializable> jpaEntityInformation;
    @Mock
    private IdentifiableType<DomainModel> managedType;
    @Mock
    private Metamodel metamodel;
    @Mock
    private RepositoryInformation repositoryInformation;
    @Mock
    private RepositoryMetadata repositoryMetadata;
    private ThuleJpaRepositoryFactory thuleJpaRepositoryFactory;

    @Test
    public void get_repository_base_class() {
        // Given

        // When
        Class<?> repositoryBaseClass = thuleJpaRepositoryFactory.getRepositoryBaseClass(repositoryMetadata);

        // Then
        assertThat(repositoryBaseClass).isEqualTo(ThuleRepository.class);
    }

    @Test
    public void get_target_repository() {
        // Given
        BDDMockito.<Class<?>>given(repositoryInformation.getDomainType()).willReturn(DomainModel.class);
        given(entityManager.getMetamodel()).willReturn(metamodel);
        given(metamodel.managedType(DomainModel.class)).willReturn(managedType);

        // When
        Object targetRepository = thuleJpaRepositoryFactory.getTargetRepository(repositoryInformation);

        // Then
        assertThat(targetRepository).isInstanceOf(ThuleJpaRepository.class);
    }

    @Before
    public void setUp() {
        given(entityManager.getDelegate()).willReturn(new Object());
        thuleJpaRepositoryFactory = new ThuleJpaRepositoryFactory(entityManager);
    }
}