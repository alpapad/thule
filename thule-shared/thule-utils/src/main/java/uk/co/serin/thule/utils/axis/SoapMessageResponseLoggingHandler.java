package uk.co.serin.thule.utils.axis;

import uk.co.serin.thule.utils.utils.XmlUtils;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.handlers.BasicHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

public class SoapMessageResponseLoggingHandler extends BasicHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoapMessageResponseLoggingHandler.class);
    private static final long serialVersionUID = -7208304003681110138L;


    public void invoke(MessageContext msgContext) throws AxisFault {
        if (LOGGER.isDebugEnabled()) {
            try {
                if (msgContext.getResponseMessage() != null) {
                    LOGGER.debug("Have successfully invoked an AXIS SOAP service with response\n{}",
                            XmlUtils.prettyPrint(msgContext.getResponseMessage().getSOAPPartAsString()));

                    Iterator attachmentParts = msgContext.getResponseMessage().getAttachments();
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
