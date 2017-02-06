package uk.co.serin.thule.people.repository.support;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.people.repository.repositories.PersonRepository;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.Metamodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ThuleJpaRepositoryFactoryBeanTest {
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
    private ThuleJpaRepositoryFactoryBean thuleJpaRepositoryFactoryBean = new ThuleJpaRepositoryFactoryBean(PersonRepository.class);

    @Test
    public void createRepositoryFactory() {
        // Given

        // When
        RepositoryFactorySupport repositoryFactory = thuleJpaRepositoryFactoryBean.createRepositoryFactory(entityManager);

        // Then
        assertThat(repositoryFactory).isInstanceOf(ThuleJpaRepositoryFactory.class);
    }

    @Before
    public void setUp() {
        given(entityManager.getDelegate()).willReturn(new Object());
    }
}