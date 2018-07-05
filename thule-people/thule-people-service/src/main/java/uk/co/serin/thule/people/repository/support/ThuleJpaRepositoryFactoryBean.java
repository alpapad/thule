package uk.co.serin.thule.people.repository.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

import java.io.Serializable;

import javax.persistence.EntityManager;

public class ThuleJpaRepositoryFactoryBean<T extends JpaRepository<S, I>, S, I extends Serializable> extends JpaRepositoryFactoryBean<T, S, I> {
    /**
     * Creates a new {@link ThuleJpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public ThuleJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        return new ThuleJpaRepositoryFactory(entityManager);
    }
}
