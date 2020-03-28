package uk.co.serin.thule.email.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import uk.co.serin.thule.email.controller.EmailController;
import uk.co.serin.thule.email.domain.model.Attachment;
import uk.co.serin.thule.email.domain.model.Email;
import uk.co.serin.thule.email.service.EmailService;

import java.util.Set;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("itest")
@AutoConfigureRestDocs(outputDir = "build/snippets")
@WebMvcTest(value = EmailController.class)
public class EmailControllerIntTest {
    private static final String EMAILS_PATH = "/emails";

    @MockBean
    private EmailService emailService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void given_no_authenticated_user_when_post_emails_then_http_status_401() throws Exception {
        // Given
        var emailRequest = Email.builder().build();
        var content = objectMapper.writeValueAsString(emailRequest);

        //When
        mvc.perform(RestDocumentationRequestBuilders.post(EMAILS_PATH).contentType(MediaType.APPLICATION_JSON).content(content).with(csrf()))

           //Then
           .andExpect(status().isUnauthorized())
           .andDo(document("post-emails/http-response-401"));
    }

    @Test
    @WithMockUser
    public void when_posting_an_email_then_email_is_returned_and_status_is_accepted() throws Exception {
        // Given
        var attachment = Attachment.builder().content(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...")
                                   .label("Attachment").build();
        var email = Email.builder().attachments(Set.of(attachment))
                         .body("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...")
                         .from("from@test.co.uk").subject("This is a test email").tos(Set.of("to@test.co.uk")).build();
        var content = objectMapper.writeValueAsString(email);

        // When
        mvc.perform(post(EMAILS_PATH).contentType(MediaType.APPLICATION_JSON).content(content).with(csrf()))

           // Then
           .andExpect(status().isAccepted())
           .andExpect(content().string(content))
           .andDo(print())
           .andDo(document("post-emails/http-response-202",
                   preprocessRequest(prettyPrint()),
                   preprocessResponse(prettyPrint()),
                   requestFields(
                           fieldWithPath("attachments.[]").type(JsonFieldType.ARRAY).description("Attachments to apply to the email")
                                                          .attributes(key("required").value("false")),
                           fieldWithPath("attachments.[].content").type(JsonFieldType.STRING).description("Content of the attachment"),
                           fieldWithPath("attachments.[].label").type(JsonFieldType.STRING).description("Label or name of the attachment"),
                           fieldWithPath("bccs.[]").type(JsonFieldType.ARRAY).description("Blind courtesy copy recipients email addresses")
                                                   .attributes(key("required").value("false")),
                           fieldWithPath("body").type(JsonFieldType.STRING).description("The main content of the email"),
                           fieldWithPath("ccs.[]").type(JsonFieldType.ARRAY).description("Courtesy copy recipients email addresses")
                                                  .attributes(key("required").value("false")),
                           fieldWithPath("from").type(JsonFieldType.STRING).description("Email address of the sender")
                                                .attributes(key("required").value("false")),
                           fieldWithPath("subject").type(JsonFieldType.STRING).description("Subject line in the email"),
                           fieldWithPath("tos.[]").type(JsonFieldType.ARRAY).description("Recipients email addresses")
                                                  .attributes(key("required").value("false"))
                   ),
                   responseFields(
                           fieldWithPath("attachments.[]").type(JsonFieldType.ARRAY).description("Attachments to apply to the email"),
                           fieldWithPath("attachments.[].content").type(JsonFieldType.STRING).description("Content of the attachment"),
                           fieldWithPath("attachments.[].label").type(JsonFieldType.STRING).description("Label or name of the attachment"),
                           fieldWithPath("bccs.[]").type(JsonFieldType.ARRAY).description("Blind courtesy copy recipients email addresses"),
                           fieldWithPath("body").type(JsonFieldType.STRING).description("The main content of the email"),
                           fieldWithPath("ccs.[]").type(JsonFieldType.ARRAY).description("Courtesy copy recipients email addresses"),
                           fieldWithPath("from").type(JsonFieldType.STRING).description("Email address of the sender"),
                           fieldWithPath("subject").type(JsonFieldType.STRING).description("Subject line in the email"),
                           fieldWithPath("tos.[]").type(JsonFieldType.ARRAY).description("Recipients email addresses")
                   )
           ));
    }
}
