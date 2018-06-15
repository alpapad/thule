package uk.co.serin.thule.people.repository;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"itest", "itest-h2", "${spring.profiles.include:default}"})
public class PersonRepositoryH2IntTest extends PersonRepositoryBaseIntTest {
}
