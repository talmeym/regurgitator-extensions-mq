# regurgitator-extensions-mq

regurgitator is a lightweight, modular, extendable java framework that you configure to 'regurgitate' canned or clever responses to incoming requests; useful for quickly mocking or prototyping services without writing any code. simply configure, deploy and run.

start your reading here: [regurgitator-all](http://github.com/talmeym/regurgitator-all#regurgitator)

## regurgitator over MQ

### request mappings

Regurgitator maps the following java jms attributes to request message parameters:

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


The same attribute list are also mapped to response parameters:

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


