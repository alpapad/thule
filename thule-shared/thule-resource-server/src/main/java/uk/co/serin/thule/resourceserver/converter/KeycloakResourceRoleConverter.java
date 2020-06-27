package uk.co.serin.thule.resourceserver.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KeycloakResourceRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";
    private Environment environment;

    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if (jwt.getClaims() == null) {
            return Set.of();
        }

        // Derive the resource id
        var applicationName = environment.getProperty("spring.application.name", "default");
        var resourceId = environment.getProperty("thule.shared.oauth2.resource.id", applicationName);

        // Extract roles at realm level
        var realmRoles = List.<String>of();
        if (jwt.getClaims().containsKey(REALM_ACCESS)) {
            var realmAccess = (Map<String, Object>) jwt.getClaims().get(REALM_ACCESS);
            realmRoles = (List<String>) realmAccess.get(ROLES);
        }

        // Extract roles at resource (micro-service) level
        var resourceRoles = List.<String>of();
        if (jwt.getClaims().containsKey(RESOURCE_ACCESS) && ((Map<String, Object>) jwt.getClaims().get(RESOURCE_ACCESS)).containsKey(resourceId)) {
            var resourceAccess = (Map<String, Object>) jwt.getClaims().get(RESOURCE_ACCESS);
            var resource = (Map<String, Object>) resourceAccess.get(resourceId);
            resourceRoles = (List<String>) resource.get(ROLES);
        }

        // Return a collection of Granted Authorities
        return Stream.concat(realmRoles.stream(), resourceRoles.stream()) // Merge realm and resource roles together
                     .map(roleName -> "ROLE_" + roleName) // Prefix roles with ROLE_ to conform to Spring Security granted authority naming standard
                     .map(SimpleGrantedAuthority::new) // Convert role name string to a GrantedAuthority
                     .collect(Collectors.toUnmodifiableSet()); // Convert to a set
    }
}
