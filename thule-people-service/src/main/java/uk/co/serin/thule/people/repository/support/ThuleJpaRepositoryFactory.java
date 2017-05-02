package uk.co.serin.thule.people.repository.support;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import java.io.Serializable;

import javax.persistence.EntityManager;

class ThuleJpaRepositoryFactory extends JpaRepositoryFactory {
    private final EntityManager entityManager;

    ThuleJpaRepositoryFactory(final EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation repositoryInformation) {
        JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(repositoryInformation.getDomainType());
        return new ThuleJpaRepository<>(entityInformation, entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return ThuleRepository.class;
    }
}