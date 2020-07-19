package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

@SuppressWarnings("java:S101") // Suppress Class names should comply with a naming convention
public class V5__insert_photographs_demo_data extends BaseJavaMigration {
    private static final String SQL = "UPDATE people " +
            "SET photograph = ? " +
            "WHERE people.user_id = ?";
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Override
    public void migrate(Context context) throws Exception {
        jdbcTemplate.setDataSource(new SingleConnectionDataSource(context.getConnection(), true));

        insertAccount("green", "photographs/reverendGreen.jpg");
        insertAccount("mustard", "photographs/colonelMustard.jpg");
        insertAccount("peacock", "photographs/mrsPeacock.jpg");
        insertAccount("plum", "photographs/professorPlum.jpg");
        insertAccount("scarlett", "photographs/missScarlet.jpg");
        insertAccount("white", "photographs/mrsWhite.jpg");
    }

    public void insertAccount(String userId, String photographLocation) throws IOException {
        var resource = new DefaultResourceLoader().getResource(photographLocation);
        var photograph = FileCopyUtils.copyToByteArray(resource.getInputStream());

        jdbcTemplate.update(SQL, photograph, userId);
    }
}
