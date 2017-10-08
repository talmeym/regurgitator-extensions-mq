/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;
import com.emarte.regurgitator.core.Message;

import javax.jms.*;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.extensions.mq.MessageRequestUtil.applyRequestData;

public class RegurgitatorMessageListener implements MessageListener {
    private static final Log log = getLog(RegurgitatorMessageListener.class);

    private final Regurgitator regurgitator;
    private final ResponseCallBack responseCallBack;

    public RegurgitatorMessageListener(Regurgitator regurgitator, ResponseCallBack responseCallBack) {
        this.regurgitator = regurgitator;
        this.responseCallBack = responseCallBack;
    }

    @Override
    public void onMessage(javax.jms.Message message) {
        log.debug("Accepting new jms message");
        TextMessage textMessage = (TextMessage) message;

        try {
            log.debug("Creating new message");
            Message newMessage = new Message(responseCallBack);

            log.debug("Applying jms message details to message");
            applyRequestData(newMessage, textMessage);

            log.debug("Sending message to regurgitator");
            regurgitator.processMessage(newMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
