# regurgitator-extensions-mq

regurgitator is a lightweight, modular, extendable java framework that you configure to 'regurgitate' canned or clever responses to incoming requests; useful for quickly mocking or prototyping services without writing any code. simply configure, deploy and run.

start your reading here: [regurgitator-all](https://talmeym.github.io/regurgitator-all#regurgitator)

[``apidocs``](https://regurgitator.emarte.uk/apidocs/regurgitator-extensions-mq/0.1.3/)

## regurgitator over mq

regurgitator allows the mocking of jms services by providing an ``mq message bridge`` to allow the capture of a jms 'request' message from one queue or topic, the processing of that request through regurgitator and the subsequent placing of a response message onto another jms destination.

### mq message bridge

***mq req*** => ***RegurgitatorMessageListener*** => ***message*** => ***regurgitator*** => ***MqResponseCallback*** => ***mq res***

the ``mq message bridge`` is made up of the following parts:

#### MqMessagingSystem

regurgitator abstracts the mq system to be used to an interface, for you to implement with a jms system of your choice. **regurgitator currently only supports the text jms message type**

```java
package uk.emarte.regurgitator.extensions.mq;

import javax.jms.*;

public interface MqMessagingSystem {
    Connection getConnection() throws JMSException;
    TextMessage createTextMessage();
    Destination createDestination(String destination);
}
```

#### MqResponseCallback(MqMessagingSystem mqSys, String outputDest)

the mq response callback takes a response from regurgitator and converts it into an outgoing jms message.

#### RegurgitatorMessageListener(Regurgitator regurg, ResponseCallBack callback)

the regurgitator message listener accepts incoming jms text messages and passes them on to regurgitator as ``message`` objects.

#### MqMessageBridge(MqMessagingSystem mqSys, String inputDest, String outputDest, Regurgitator regurg)

the mq message bridge uses a ``MqMessagingSystem`` to create a consumer on an input destination, adds a ``RegurgitatorMessageListener`` to it for accepting requests and passing them as messages to regurgitator, and gives regurgitator a ``MqResponseCallback`` to handle putting responses to an output destination.

### example

an example of using the mq message bridge with ``ActiveMQ`` can be found [here](https://github.com/talmeym/regurgitator-extensions-mq/tree/master/src/test/java/uk/emarte/regurgitator/test/ActiveMqMessagingSystem.java){:target="_blank"}. this example can be run with the following [configuration file](https://github.com/talmeym/regurgitator-extensions-mq/blob/master/src/test/resources/rock-paper-scissors-over-mq.xml){:target="_blank"} to play a famous game over mq.

### request mappings

Regurgitator maps the following incoming jms text message attributes to request message parameters:

|attribute|context|parameter|type|
|---|---|---|---|
|``Message.JMSMessageID``|``request-metadata``|``jms-message-id``|``STRING``|
|``Message.JMSType``|``request-metadata``|``jms-type``|``STRING``|
|``Message.JMSCorrelationID``|``request-metadata``|``jms-correlation-id``|``STRING``|
|``Message.JMSDestination``|``request-metadata``|``jms-destination``|``STRING``|
|``Message.JMSDeliveryMode``|``request-metadata``|``jms-delivery-mode``|``STRING``|
|``Message.JMSExpiration``|``request-metadata``|``jms-expiration``|``STRING``|
|``Message.JMSPriority``|``request-metadata``|``jms-priority``|``STRING``|
|``Message.JMSRedelivered``|``request-metadata``|``jms-redelivered``|``STRING``|
|``Message.JMSReplyTo``|``request-metadata``|``jms-reply-to``|``STRING``|
|``Message.JMSTimestamp``|``request-metadata``|``jms-timestamp``|``STRING``|

### response mappings

The same jms attribute set can also be explicitly set as response parameters:

|attribute|context|parameter|type|
|---|---|---|---|
|``Message.JMSMessageID``|``response-metadata``|``jms-message-id``|``STRING``|
|``Message.JMSType``|``response-metadata``|``jms-type``|``STRING``|
|``Message.JMSCorrelationID``|``response-metadata``|``jms-correlation-id``|``STRING``|
|``Message.JMSDestination``|``response-metadata``|``jms-destination``|``STRING``|
|``Message.JMSDeliveryMode``|``response-metadata``|``jms-delivery-mode``|``STRING``|
|``Message.JMSExpiration``|``response-metadata``|``jms-expiration``|``STRING``|
|``Message.JMSPriority``|``response-metadata``|``jms-priority``|``STRING``|
|``Message.JMSRedelivered``|``response-metadata``|``jms-redelivered``|``STRING``|
|``Message.JMSReplyTo``|``response-metadata``|``jms-reply-to``|``STRING``|
|``Message.JMSTimestamp``|``response-metadata``|``jms-timestamp``|``STRING``|

## steps

regurgitator-extensions-mq provides the following steps:
- ``jms-call`` ([xml](https://talmeym.github.io/regurgitator-extensions-mq-xml#jms-call), [json](https://talmeym.github.io/regurgitator-extensions-mq-json#jms-call), [yml](https://talmeym.github.io/regurgitator-extensions-mq-yml#jms-call)) make an outward jms 'call', placing a text message onto a jms destination, using existing parameter values for message payload, attributes, object properties etc.
- ``create-jms-response`` ([xml](https://talmeym.github.io/regurgitator-extensions-mq-xml#create-jms-response), [json](https://talmeym.github.io/regurgitator-extensions-mq-json#create-jms-response), [yml](https://talmeym.github.io/regurgitator-extensions-mq-yml#create-jms-response)) create a response message, but also allows pre-population of message attributes such as jms destination, correlation id and others.


