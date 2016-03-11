package com.emarte.regurgitator.test;

import com.emarte.regurgitator.core.*;
import com.emarte.regurgitator.extensions.mq.*;

import javax.jms.JMSException;

import static com.emarte.regurgitator.core.ConfigurationFile.loadFile;

public class ActiveMqMessageBridge {
	public static void main(String[] args) throws RegurgitatorException, JMSException {

		String brokerUri = args[0];
		String inputQueue = args[1];
		String outputQueue = args[2];
		String regurgitatorId = args[3];
		String regurgitatorConfigLocation = args[4];

		new MqMessageBridge(new ActiveMqMessagingSystem(brokerUri), inputQueue, outputQueue, new Regurgitator(regurgitatorId, loadFile(regurgitatorConfigLocation)));
	}
}
