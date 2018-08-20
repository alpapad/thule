package com.gohenry.utils.axis;

import com.gohenry.utils.utils.XmlUtils;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.handlers.BasicHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

public class SoapMessageRequestLoggingHandler extends BasicHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoapMessageRequestLoggingHandler.class);
    private static final long serialVersionUID = -7208304003681110138L;

    public void invoke(MessageContext msgContext) throws AxisFault {
        if (LOGGER.isDebugEnabled()) {
            try {
                if (msgContext.getRequestMessage() != null) {
                    LOGGER.debug("About to invoke AXIS SOAP service with request\n{}",
                            XmlUtils.prettyPrint(msgContext.getRequestMessage().getSOAPPartAsString()));

                    Iterator attachmentParts = msgContext.getRequestMessage().getAttachments();
                    while (attachmentParts.hasNext()) {
                        String attachmentPartXml = AttachmentPart.class.cast(attachmentParts.next()).
                                getActivationDataHandler().getContent().toString();
                        LOGGER.debug("\n{}", XmlUtils.prettyPrint(attachmentPartXml));
                    }
                }
            } catch (IOException e) {
                throw AxisFault.makeFault(e);
            }
        }
    }
}
