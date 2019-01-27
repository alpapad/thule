package uk.co.serin.thule.test.assertj;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.assertj.core.api.AbstractAssert;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpStatusCodeExceptionAssert extends AbstractAssert<HttpStatusCodeExceptionAssert, HttpStatusCodeException> {

    public HttpStatusCodeExceptionAssert(HttpStatusCodeException actual) {
        super(actual, HttpStatusCodeExceptionAssert.class);
    }

    public HttpStatusCodeExceptionAssert hasMessageErrorAttribute(String expected) {

        var actualAttribute = getErrorAttributes().get("message");
        Assert.notNull(actualAttribute, "'message' error attribute cannot be null");
        if (!actualAttribute.equals(expected)) {
            throw new AssertionError(String.format("Expected 'message' error attribute to be <%s> but was <%s>", actualAttribute, expected));
        }

        return this;
    }

    private Map<String, String> getErrorAttributes() {
        try {
            return new ObjectMapper().readValue(actual.getResponseBodyAsString(), HashMap.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}