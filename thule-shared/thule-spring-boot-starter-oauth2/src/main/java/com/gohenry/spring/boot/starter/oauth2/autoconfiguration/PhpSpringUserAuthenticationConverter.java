package com.gohenry.spring.boot.starter.oauth2.autoconfiguration;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Collections;
import java.util.Map;

public class PhpSpringUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    protected String userId = "userId";

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            return super.extractAuthentication(map);
        } else {
            Map<String, Object> data = createDataStructure(map);
            Map<String, Object> mapWithPhpUserId = Collections.singletonMap(USERNAME, data.get(userId));

            return super.extractAuthentication(mapWithPhpUserId);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> createDataStructure(Map<String, ?> map) {
        Map<String, Object> data = (Map) map.get("data");
        data.put(userId, data.get(userId));
        data.put("refreshAfter", data.get("refreshAfter"));
        return data;
    }
}
