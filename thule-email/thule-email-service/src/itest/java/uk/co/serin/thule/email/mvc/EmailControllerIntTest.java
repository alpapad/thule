package uk.co.serin.thule.email.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import uk.co.serin.thule.email.controller.EmailController;
import uk.co.serin.thule.email.datafactories.TestDataFactory;
import uk.co.serin.thule.email.domain.Email;
import uk.co.serin.thule.email.service.EmailService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("itest")
@WebMvcTest(EmailController.class)
public class EmailControllerIntTest {
    private static final String EMAIL_PATH = "/" + Email.ENTITY_NAME_EMAILS;
    @MockBean
    private EmailService emailService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void create_bank_transfer_request() throws Exception {
        // Given
        Email expectedEmail = TestDataFactory.buildEmail();

        // When
        mvc.perform(post(EMAIL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedEmail)))
                .andDo(print())

                // Then
                .andExpect(status().isAccepted())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedEmail)));
    }
}
