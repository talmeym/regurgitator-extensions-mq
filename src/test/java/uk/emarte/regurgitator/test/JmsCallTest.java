package uk.emarte.regurgitator.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.emarte.regurgitator.core.Message;
import uk.emarte.regurgitator.core.RegurgitatorException;
import uk.emarte.regurgitator.extensions.mq.JmsCall;
import uk.emarte.regurgitator.extensions.mq.MqMessagingSystem;

import javax.jms.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class JmsCallTest {
    @Mock
    private Connection connection;

    @Mock
    private TextMessage textMessage;

    @Mock
    private Destination destination;

    @Mock
    private Session session;

    @Mock
    private MessageProducer producer;

    @Mock
    private MqMessagingSystem mqMessagingSystem;

    @Test
    public void testStep() throws RegurgitatorException, JMSException {
        String destinationStr = "queue://somewhere";
        String propertyKey = "myProperty";
        String propertyValue = "propertyValue";
        String payloadText = "{\"something\":\"something\"}";

        when(mqMessagingSystem.createTextMessage()).thenReturn(textMessage);
        when(mqMessagingSystem.getConnection()).thenReturn(connection);
        when(connection.createSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
        when(mqMessagingSystem.createDestination(destinationStr)).thenReturn(destination);
        when(session.createProducer(destination)).thenReturn(producer);

        Message message = new Message(null);

        message.getContext(REQUEST_PROPERTIES_CONTEXT).setValue(propertyKey, propertyValue);
        message.getContext(REQUEST_PAYLOAD_CONTEXT).setValue(TEXT, payloadText);

        new JmsCall("jms-call-1", mqMessagingSystem, destinationStr).execute(message);

        verify(textMessage).setObjectProperty(propertyKey, propertyValue);
        verify(textMessage).setText(payloadText);
        verify(producer).send(textMessage);
    }
}
