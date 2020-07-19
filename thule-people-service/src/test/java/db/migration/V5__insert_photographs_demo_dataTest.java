package db.migration;

import org.flywaydb.core.api.migration.Context;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Connection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class V5__insert_photographs_demo_dataTest {
    @Mock
    private Connection connection;
    @Mock
    private Context context;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private V5__insert_photographs_demo_data sut;

    @Test
    void when_inserting_photographs_then_6_photographs_are_inserted() throws Exception {
        // Given
        ReflectionTestUtils.setField(sut, "jdbcTemplate", jdbcTemplate);
        given(context.getConnection()).willReturn(connection);

        // When
        sut.migrate(context);

        // Then
        verify(jdbcTemplate, times(6)).update(anyString(), any(), anyString());
    }
}