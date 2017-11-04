/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

import java.util.ServiceLoader;

class MqMessagingLookup {
    private static ServiceLoader<MqMessagingSystem> MQ_MESSAGING_SYSTEMS = ServiceLoader.load(MqMessagingSystem.class);

    static MqMessagingSystem mqMessagingSystem() {
        for(MqMessagingSystem mqMessagingSystem: MQ_MESSAGING_SYSTEMS) {
            return mqMessagingSystem;
        }

        throw new IllegalStateException("Cannot load mq messaging system");
    }
}