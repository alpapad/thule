package uk.co.serin.thule.people.repository.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import uk.co.serin.thule.people.domain.person.Person;
import uk.co.serin.thule.people.repository.repositories.PersonRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ThuleJpaRepositoryFactoryBeanTest {
    @Mock
    private EntityManager entityManager;
    private ThuleJpaRepositoryFactoryBean<PersonRepository, Person, Long> thuleJpaRepositoryFactoryBean = new ThuleJpaRepositoryFactoryBean<>(PersonRepository.class);

    @Test
    public void create_repository_factory() {
        // Given
        given(entityManager.getDelegate()).willReturn(new Object());

        // When
        RepositoryFactorySupport repositoryFactory = thuleJpaRepositoryFactoryBean.createRepositoryFactory(entityManager);

        // Then
        assertThat(repositoryFactory).isInstanceOf(ThuleJpaRepositoryFactory.class);
    }
}