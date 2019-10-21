package uk.co.serin.thule.email.contract;

import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("ctest")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ContractBaseTest {
    private SmtpServer smtpServer;
    @Value("${spring.mail.host}")
    private String springMailHost;
    @Value("${spring.mail.port}")
    private int springMailPort;

    public SmtpServer getSmtpServer() {
        return smtpServer;
    }

    @After
    public void stopEmbeddedServer() {
        if (smtpServer != null) {
            smtpServer.stop();
        }
        smtpServer = null;
    }

    void startEmbeddedSmtpServer() {
        var serverOptions = new ServerOptions();
        serverOptions.port = springMailPort;
        smtpServer = SmtpServerFactory.startServer(serverOptions);
    }
}
