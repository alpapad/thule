package uk.co.serin.thule.email;

import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmtpServerConfigurer {
    @Value("${spring.mail.port}")
    private int smtpServerPort;

    @Bean
    public SmtpServer smtpServer() {
        ServerOptions serverOptions = new ServerOptions();
        serverOptions.port = smtpServerPort;
        return SmtpServerFactory.startServer(serverOptions);
    }
}
