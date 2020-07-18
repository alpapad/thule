package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@SuppressWarnings("java:S101") // Suppress Class names should comply with a naming convention
public class V5__insert_photographs_demo_data extends BaseJavaMigration {
    private static final String SQL = "INSERT INTO photographs(hash, photo, person_id, positin, version, created_at, created_by, updated_at, updated_by) " +
            "SELECT ?, ?, people.id, 0, 1, CURRENT_TIMESTAMP, 'superuser', CURRENT_TIMESTAMP, 'superuser' " +
            "FROM people WHERE people.user_id = ?";
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Override
    public void migrate(Context context) throws Exception {
        jdbcTemplate.setDataSource(new SingleConnectionDataSource(context.getConnection(), true));

        insertPhotograph("green", "photographs/reverendGreen.jpg");
        insertPhotograph("mustard", "photographs/colonelMustard.jpg");
        insertPhotograph("peacock", "photographs/mrsPeacock.jpg");
        insertPhotograph("plum", "photographs/professorPlum.jpg");
        insertPhotograph("scarlett", "photographs/missScarlet.jpg");
        insertPhotograph("white", "photographs/mrsWhite.jpg");
    }

    public void insertPhotograph(String userId, String photographLocation) throws IOException {
        var resource = new DefaultResourceLoader().getResource(photographLocation);
        var photo = FileCopyUtils.copyToByteArray(resource.getInputStream());
        var hash = new String(DigestUtils.md5Digest(photo), Charset.defaultCharset());

        jdbcTemplate.update(SQL, hash, photo, userId);
    }
}
