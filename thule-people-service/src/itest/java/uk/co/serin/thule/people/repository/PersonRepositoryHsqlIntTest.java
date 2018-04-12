package uk.co.serin.thule.people.repository;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"itest", "itest-hsql", "${spring.profiles.include:default}"})
public class PersonRepositoryHsqlIntTest extends PersonRepositoryBaseIntTest {
}
