package uk.co.serin.thule.people.repository;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"itest", "itest-hsql"})
public class PersonRepositoryHsqlIntTest extends PersonRepositoryBaseIntTest {
}
