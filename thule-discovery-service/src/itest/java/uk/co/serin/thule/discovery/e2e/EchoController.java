package uk.co.serin.thule.discovery.e2e;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class EchoController {
    @RequestMapping(value = "/echo/{message}")
    public Map<String, String> echo(@PathVariable("message") String message) {
        return Collections.singletonMap("echo", message);
    }
}
