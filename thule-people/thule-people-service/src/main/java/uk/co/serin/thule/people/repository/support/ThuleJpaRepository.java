package uk.co.serin.thule.people.repository.support;


import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import uk.co.serin.thule.people.domain.entity.AuditEntity;
import uk.co.serin.thule.utils.service.trace.TracePublicMethods;

import java.io.Serializable;

import javax.persistence.EntityManager;

@TracePublicMethods
public class ThuleJpaRepository<T, I extends Serializable> extends SimpleJpaRepository<T, I> {
    private final String deleteByUpdatedByEjbQl;
    private final EntityManager entityManager;

    public ThuleJpaRepository(JpaEntityInformation<T, I> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;

        deleteByUpdatedByEjbQl = "DELETE FROM " + entityInformation.getEntityName() + " WHERE " + AuditEntity.ENTITY_ATTRIBUTE_NAME_AUDIT + "." +
                AuditEntity.ENTITY_ATTRIBUTE_NAME_UPDATED_BY + " = :" + AuditEntity.ENTITY_ATTRIBUTE_NAME_UPDATED_BY;
    }

    @Transactional
    public void deleteByUpdatedBy(String updatedBy) {
        entityManager.createQuery(deleteByUpdatedByEjbQl).setParameter(AuditEntity.ENTITY_ATTRIBUTE_NAME_UPDATED_BY, updatedBy).executeUpdate();
    }
}