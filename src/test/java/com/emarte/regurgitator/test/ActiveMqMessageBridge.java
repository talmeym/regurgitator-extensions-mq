package com.emarte.regurgitator.test;

import com.emarte.regurgitator.core.*;
import com.emarte.regurgitator.extensions.mq.*;

import javax.jms.JMSException;

import static com.emarte.regurgitator.core.ConfigurationFile.loadFile;

public class ActiveMqMessageBridge {
	public static void main(String[] args) throws RegurgitatorException, JMSException {
		new MqMessageBridge(new ActiveMqMessagingSystem(args[0]), args[1], args[2], new Regurgitator(args[3], loadFile(args[4])));
	}
}
