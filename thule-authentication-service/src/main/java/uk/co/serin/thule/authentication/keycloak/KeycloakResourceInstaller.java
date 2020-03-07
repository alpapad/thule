package uk.co.serin.thule.authentication.keycloak;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import uk.co.serin.thule.utils.trace.TracePublicMethods;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@TracePublicMethods
public class KeycloakResourceInstaller implements CommandLineRunner {
    private static final String THULE_EMAIL_SERVICE_CLIENT_ID = "thule-email-service";
    private static final String THULE_PEOPLE_SERVICE_CLIENT_ID = "thule-people-service";
    private static final String THULE_ROLE_NAME = "USER";
    private static final String THULE_WEBAPP_CLIENT_ID = "thule-webapp";
    private KeycloakRepository keycloakRepository;
    private SpringTemplateEngine springTemplateEngine;

    @Override
    public void run(String... args) {
        createKeycloakResources();
        createK8sSecretsFile();
    }

    private void createKeycloakResources() {
        keycloakRepository.createRealm();

        // Thule Email Service
        keycloakRepository.createServiceClient(THULE_EMAIL_SERVICE_CLIENT_ID);
        keycloakRepository.createRoleForClient(THULE_ROLE_NAME, THULE_EMAIL_SERVICE_CLIENT_ID);

        // Thule People Service
        keycloakRepository.createServiceClient(THULE_PEOPLE_SERVICE_CLIENT_ID);
        keycloakRepository.createRoleForClient(THULE_ROLE_NAME, THULE_PEOPLE_SERVICE_CLIENT_ID);

        // Thule Webapp
        keycloakRepository.createPublicClient(THULE_WEBAPP_CLIENT_ID);
        keycloakRepository.createRoleForClient(THULE_ROLE_NAME, THULE_WEBAPP_CLIENT_ID);

        var userId = keycloakRepository.createUser("thule@serin-consultancy.co.uk", "thule", "Project", "Thule");
        keycloakRepository.createUserRoleMapping(userId, THULE_EMAIL_SERVICE_CLIENT_ID, THULE_ROLE_NAME);
        keycloakRepository.createUserRoleMapping(userId, THULE_PEOPLE_SERVICE_CLIENT_ID, THULE_ROLE_NAME);
        keycloakRepository.createUserRoleMapping(userId, THULE_WEBAPP_CLIENT_ID, THULE_ROLE_NAME);
    }

    private void createK8sSecretsFile() {
        var thuleEmailServiceClientSecret = Base64Utils.encodeToString(keycloakRepository.getClientSecret(THULE_EMAIL_SERVICE_CLIENT_ID).getBytes());
        var thulePeopleServiceClientSecret = Base64Utils.encodeToString(keycloakRepository.getClientSecret(THULE_PEOPLE_SERVICE_CLIENT_ID).getBytes());
        var templateVariables = Map.<String, Object>of(
                "thuleEmailServiceClientSecret", thuleEmailServiceClientSecret,
                "thulePeopleServiceClientSecret", thulePeopleServiceClientSecret);

        // Create thymeleaf context for variable substitution
        var context = new Context();
        context.setVariables(templateVariables);

        // Create the k8s secrets file
        var thuleKeycloakSecrets = springTemplateEngine.process("thule-keycloak-secrets-template", context);
        try {
            FileCopyUtils.copy(thuleKeycloakSecrets, new FileWriter(new File("build/thule-keycloak-secrets.yml")));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
