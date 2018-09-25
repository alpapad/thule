package com.gohenry.utils.jpa;

import org.hibernate.boot.model.naming.EntityNaming;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitAnyDiscriminatorColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitAnyKeyColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitCollectionTableNameSource;
import org.hibernate.boot.model.naming.ImplicitDiscriminatorColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitIdentifierColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitIndexColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitIndexNameSource;
import org.hibernate.boot.model.naming.ImplicitMapKeyColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitPrimaryKeyJoinColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitTenantIdColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitUniqueKeyNameSource;
import org.hibernate.boot.model.source.spi.AttributePath;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;

/**
 * The implicit naming strategy for JPA to use the snake case with the ability to override naming with annotations
 * The purpose of throwing UnsupportedOperationException is to make sure a spring boot application explicitly
 * errors rather than use default parent implementation. We don't anticipate those methods being used in our projects,
 * if they are, they should be explicitly implemented.
 */
public class SnakeCaseOverridableImplicitNamingStrategy extends SpringImplicitNamingStrategy {

    private CamelCaseToSnakeCaseConverter camelCaseToSnakeCaseConverter;

    //Parameterless constructor is required for the hibernate to instantiate the strategy outside of the Spring context
    public SnakeCaseOverridableImplicitNamingStrategy() {
        camelCaseToSnakeCaseConverter = new CamelCaseToSnakeCaseConverter();
    }


    public SnakeCaseOverridableImplicitNamingStrategy(CamelCaseToSnakeCaseConverter camelCaseToSnakeCaseConverter) {
        this.camelCaseToSnakeCaseConverter = camelCaseToSnakeCaseConverter;
    }

    @Override
    protected String transformEntityName(EntityNaming entityNaming) {
        var transformedEntityName = super.transformEntityName(entityNaming);
        return camelCaseToSnakeCaseConverter.convert(transformedEntityName);
    }

    protected String transformAttributePath(AttributePath attributePath) {
        var attributeProperty = attributePath.getProperty();
        return camelCaseToSnakeCaseConverter.convert(attributeProperty);
    }

    @Override
    public Identifier determineIdentifierColumnName(ImplicitIdentifierColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determineCollectionTableName(ImplicitCollectionTableNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determineDiscriminatorColumnName(ImplicitDiscriminatorColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determineTenantIdColumnName(ImplicitTenantIdColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determinePrimaryKeyJoinColumnName(ImplicitPrimaryKeyJoinColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determineAnyDiscriminatorColumnName(ImplicitAnyDiscriminatorColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determineAnyKeyColumnName(ImplicitAnyKeyColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determineMapKeyColumnName(ImplicitMapKeyColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determineListIndexColumnName(ImplicitIndexColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Identifier determineIndexName(ImplicitIndexNameSource source) {
        throw new UnsupportedOperationException();
    }

}
