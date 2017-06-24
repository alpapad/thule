package uk.co.serin.thule.people.repository.support;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import uk.co.serin.thule.core.aspects.TracePublicMethods;
import uk.co.serin.thule.people.domain.DomainModel;

import java.io.Serializable;

import javax.persistence.EntityManager;

@TracePublicMethods
class ThuleJpaRepository<T, I extends Serializable> extends SimpleJpaRepository<T, I> implements ThuleRepository<T, I> {
    private final String deleteByUpdatedByEjbQl;
    private final EntityManager entityManager;

    public ThuleJpaRepository(JpaEntityInformation<T, I> metadata, EntityManager entityManager) {
        super(metadata, entityManager);
        this.entityManager = entityManager;

        deleteByUpdatedByEjbQl = "DELETE FROM " + metadata.getEntityName() + " WHERE " + DomainModel.ENTITY_ATTRIBUTE_NAME_AUDIT + "." + DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY + " = :" + DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY;
    }

    @Override
    @Modifying
    public void deleteByUpdatedBy(String updatedBy) {
        entityManager.createQuery(deleteByUpdatedByEjbQl).setParameter(DomainModel.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, updatedBy).executeUpdate();
    }
}