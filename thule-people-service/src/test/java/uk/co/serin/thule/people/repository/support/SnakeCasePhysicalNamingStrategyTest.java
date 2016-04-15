package uk.co.serin.thule.people.repository.support;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SnakeCasePhysicalNamingStrategyTest {
    private static final String CAMEL_CASE_IDENTIFIER = "camelCaseIdentifier";
    private static final String UNDERSCORE_SEPARATING_IDENTIFIER1 = "camel_case_identifier";
    @Mock
    private JdbcEnvironment jdbcEnvironment;
    private SnakeCasePhysicalNamingStrategy snakeCasePhysicalNamingStrategy = new SnakeCasePhysicalNamingStrategy();

    @Test
    public void nullIdentifier() {
        // Given

        // When
        Identifier identifier = snakeCasePhysicalNamingStrategy.toPhysicalCatalogName(null, jdbcEnvironment);

        // Then
        assertThat(identifier).isNull();
    }

    @Test
    public void toPhysicalCatalogName() {
        // Given
        Identifier camelCaseIdentifier = Identifier.toIdentifier(CAMEL_CASE_IDENTIFIER);

        // When
        Identifier identifier = snakeCasePhysicalNamingStrategy.toPhysicalCatalogName(camelCaseIdentifier, jdbcEnvironment);

        // Then
        assertThat(identifier.getText()).isEqualTo(UNDERSCORE_SEPARATING_IDENTIFIER1);
    }

    @Test
    public void toPhysicalColumnName() {
        // Given
        Identifier camelCaseIdentifier = Identifier.toIdentifier(CAMEL_CASE_IDENTIFIER);

        // When
        Identifier identifier = snakeCasePhysicalNamingStrategy.toPhysicalColumnName(camelCaseIdentifier, jdbcEnvironment);

        // Then
        assertThat(identifier.getText()).isEqualTo(UNDERSCORE_SEPARATING_IDENTIFIER1);
    }

    @Test
    public void toPhysicalSchemaName() {
        // Given
        Identifier camelCaseIdentifier = Identifier.toIdentifier(CAMEL_CASE_IDENTIFIER);

        // When
        Identifier identifier = snakeCasePhysicalNamingStrategy.toPhysicalSchemaName(camelCaseIdentifier, jdbcEnvironment);

        // Then
        assertThat(identifier.getText()).isEqualTo(UNDERSCORE_SEPARATING_IDENTIFIER1);
    }

    @Test
    public void toPhysicalSequenceName() {
        // Given
        Identifier camelCaseIdentifier = Identifier.toIdentifier(CAMEL_CASE_IDENTIFIER);

        // When
        Identifier identifier = snakeCasePhysicalNamingStrategy.toPhysicalSequenceName(camelCaseIdentifier, jdbcEnvironment);

        // Then
        assertThat(identifier.getText()).isEqualTo(UNDERSCORE_SEPARATING_IDENTIFIER1);
    }

    @Test
    public void toPhysicalTableName() {
        // Given
        Identifier camelCaseIdentifier = Identifier.toIdentifier(CAMEL_CASE_IDENTIFIER);

        // When
        Identifier identifier = snakeCasePhysicalNamingStrategy.toPhysicalTableName(camelCaseIdentifier, jdbcEnvironment);

        // Then
        assertThat(identifier.getText()).isEqualTo(UNDERSCORE_SEPARATING_IDENTIFIER1);
    }
}