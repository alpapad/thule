package uk.co.serin.thule.people.repository.repositories;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;

import uk.co.serin.thule.people.MySqlDockerContainer;

@ActiveProfiles("mysql")
public class PersonRepositoryMysqlIntTest extends AbstractPersonRepositoryIntTest {
    @BeforeClass
    public static void setUpClass() {
        MySqlDockerContainer.startMySqlContainerIfDown();
    }
}

