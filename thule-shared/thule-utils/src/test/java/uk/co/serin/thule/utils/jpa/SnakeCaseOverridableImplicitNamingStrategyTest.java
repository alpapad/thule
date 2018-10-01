package uk.co.serin.thule.utils.jpa;

import org.hibernate.boot.model.naming.EntityNaming;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SnakeCaseOverridableImplicitNamingStrategyTest {

    @InjectMocks
    private SnakeCaseOverridableImplicitNamingStrategy sut;

    @Mock
    private CamelCaseToSnakeCaseConverter camelCaseToSnakeCaseConverter;

    @Test
    public void when_creating_strategy_with_default_constructor_then_strategy_is_instantiated_without_exception() {

        //When
        sut = new SnakeCaseOverridableImplicitNamingStrategy();

    }

    @Test
    public void when_transforming_entity_name_then_result_is_snake_cased() {

        //Given
        var entityNaming = mock(EntityNaming.class);
        var jpaEntityName = "someEntityName";
        var convertedEntityName = "convertedEntity_name";

        given(entityNaming.getJpaEntityName()).willReturn(jpaEntityName);
        given(camelCaseToSnakeCaseConverter.convert(jpaEntityName)).willReturn(convertedEntityName);

        //When
        var transformedEntityName = sut.transformEntityName(entityNaming);

        //Then
        assertThat(transformedEntityName).isEqualTo(convertedEntityName);

    }

    @Test
    public void when_transforming_attribute_path_then_result_is_snake_cased() {

        //Given
        var attributePath = mock(AttributePath.class);
        String attributePathProperty = "vheuve";
        String snakeCasedProperty = "ehkhejgdfgfgejv";

        given(attributePath.getProperty()).willReturn(attributePathProperty);
        given(camelCaseToSnakeCaseConverter.convert(attributePathProperty)).willReturn(snakeCasedProperty);

        //When
        var transformedAttributePath = sut.transformAttributePath(attributePath);

        //Then
        assertThat(transformedAttributePath).isEqualTo(snakeCasedProperty);

    }

    @Test
    public void when_determining_identifier_column_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitIdentifierColumnNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineIdentifierColumnName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_collection_table_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitCollectionTableNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineCollectionTableName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_discriminator_column_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitDiscriminatorColumnNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineDiscriminatorColumnName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_tenant_id_column_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitTenantIdColumnNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineTenantIdColumnName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_primary_key_join_column_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitPrimaryKeyJoinColumnNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determinePrimaryKeyJoinColumnName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_any_discriminator_column_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitAnyDiscriminatorColumnNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineAnyDiscriminatorColumnName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_any_key_column_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitAnyKeyColumnNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineAnyKeyColumnName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_map_key_column_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitMapKeyColumnNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineMapKeyColumnName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_list_index_column_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitIndexColumnNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineListIndexColumnName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_unique_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitUniqueKeyNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineUniqueKeyName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void when_determining_index_name_then_operation_is_not_supported() {

        //Given
        var source = mock(ImplicitIndexNameSource.class);

        //When
        Throwable exception = catchThrowable(() -> sut.determineIndexName(source));

        //Then
        assertThat(exception).isInstanceOf(UnsupportedOperationException.class);

    }

}