# regurgitator-extensions-mq

regurgitator is a lightweight, modular, extendable java framework that you configure to 'regurgitate' canned or clever responses to incoming requests; useful for quickly mocking or prototyping services without writing any code. simply configure, deploy and run.

start your reading here: [regurgitator-all](http://github.com/talmeym/regurgitator-all#regurgitator)

## regurgitator over MQ

***mq msg*** => ***RegurgitatorMessageListener*** => ***message*** => ***regurgitator*** => ***MqResponseCallback*** => ***mq msg***

#### MqMessagingSystem

regurgitator supports operation over mq by first abstracting the mq system to be used out to an interface for you to implement with a mq system of your choice.

```java
package com.emarte.regurgitator.extensions.mq;

import javax.jms.*;

public interface MqMessagingSystem {
	public Connection getConnection() throws JMSException;
	public TextMessage createTextMessage();
	public Destination createDestination(String destination);
}
```

### mq message bridge

it then provides a ``mq message bridge`` to allow the capture of mq message from one queue or topic, the processing of that message through regurgitator and the subsequent placing of response messages onto another mq destination.

the ``mq message bridge`` is made up of the following classes:

#### MqResponseCallback(MqMessagingSystem mqSys, String defaultOutputDestination)

the mq response callback take a response from regurgitator and converts it into an outgoing mq message

#### RegurgitatorMessageListener(Regurgitator regurg, ResponseCallBack callback)

the regurgitator message listener accepts incoming mq messages and passes them on to regurgitator as ``message`` objects

#### MqMessageBridge(MqMessagingSystem mqSys, String inputDestination, String outputDestination, Regurgitator regurg)

the mq message bridge uses a ``MqMessagingSystem`` to create a consumer on an input destination, adds a ``RegurgitatorMessageListener`` to it for accepting messages and passing them to regurgitator, and gives that regurgitator a ``MqResponseCallback`` to handle putting responses onto a output destination.

### request mappings

Regurgitator maps the following incoming jms attributes to request message parameters:

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
- ``create-jms-response`` ([xml](https://github.com/talmeym/regurgitator-extensions-mq-xml#create-jms-response), [json](https://github.com/talmeym/regurgitator-extensions-mq-json#create-jms-response), [yml](https://github.com/talmeym/regurgitator-extensions-mq-yml#create-jms-response)) create a response, pre-populating parameters for jms destination, correlation id and message type


