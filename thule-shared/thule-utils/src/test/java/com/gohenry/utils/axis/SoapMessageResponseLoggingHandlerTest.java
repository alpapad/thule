package com.gohenry.utils.axis;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;

import javax.activation.DataHandler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SoapMessageResponseLoggingHandlerTest {
    @Mock
    private AttachmentPart attachmentPart;
    @Mock
    private Iterator attachments;
    @Mock
    private DataHandler dataHandler;
    @Mock
    private Logger logger;
    @Mock
    private Message message;
    @Mock
    private MessageContext messageContext;
    @InjectMocks
    private SoapMessageResponseLoggingHandler sut;

    @Test(expected = AxisFault.class)
    public void axisfault_is_thrown_when_retrieving_soap_message() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Given
        String soapMessage = "<soapMessage/>";
        replaceLoggerWithMock();

        given(logger.isDebugEnabled()).willReturn(true);
        given(messageContext.getResponseMessage()).willReturn(message);
        given(message.getSOAPPartAsString()).willReturn(soapMessage);
        given(message.getAttachments()).willReturn(attachments);
        given(attachments.hasNext()).willReturn(true, false);
        given(attachments.next()).willReturn(this.attachmentPart);
        given(attachmentPart.getActivationDataHandler()).willReturn(dataHandler);
        given(dataHandler.getContent()).willThrow(IOException.class);

        // When
        sut.invoke(messageContext);

        // Then (see expected in @Test annotation)
    }

    private void replaceLoggerWithMock() throws NoSuchFieldException, IllegalAccessException {
        // Set the LOGGER which is a bit tricky because it is a static final variable
        Field field = ReflectionUtils.findField(SoapMessageResponseLoggingHandler.class, "LOGGER");
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true); // Remove private access modifier
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL); // Remove final modifier so we can change the fields value
        ReflectionTestUtils.setField(SoapMessageResponseLoggingHandler.class, "LOGGER", logger); // Now we can change it!
    }

    @Test
    public void logging_is_not_attempted_when_there_is_no_request_message() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Given
        replaceLoggerWithMock();

        given(logger.isDebugEnabled()).willReturn(true);

        // When
        sut.invoke(messageContext);

        // Then
        verify(logger, never()).debug(anyString(), anyString());
    }

    @Test
    public void soap_message_is_logged_when_in_debug_mode() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Given
        String soapMessage = "<soapMessage/>";
        replaceLoggerWithMock();

        given(logger.isDebugEnabled()).willReturn(true);
        given(messageContext.getResponseMessage()).willReturn(message);
        given(message.getSOAPPartAsString()).willReturn(soapMessage);
        given(message.getAttachments()).willReturn(attachments);
        given(attachments.hasNext()).willReturn(true, false);
        given(attachments.next()).willReturn(this.attachmentPart);
        given(attachmentPart.getActivationDataHandler()).willReturn(dataHandler);
        given(dataHandler.getContent()).willReturn(soapMessage);

        // When
        sut.invoke(messageContext);

        // Then
        verify(logger, times(2)).debug(anyString(), anyString());
    }

    @Test
    public void soap_message_is_not_logged_when_not_in_debug_mode() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Given
        replaceLoggerWithMock();
        given(logger.isDebugEnabled()).willReturn(false);

        // When
        sut.invoke(messageContext);

        // Then
        verify(logger, never()).debug(anyString(), anyString());
    }
}