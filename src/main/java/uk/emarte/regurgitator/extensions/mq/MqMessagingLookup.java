/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.extensions.mq;

import java.util.ServiceLoader;

public class MqMessagingLookup {
    private static final ServiceLoader<MqMessagingSystem> MQ_MESSAGING_SYSTEMS = ServiceLoader.load(MqMessagingSystem.class);

    public static MqMessagingSystem mqMessagingSystem() {
        for(MqMessagingSystem mqMessagingSystem: MQ_MESSAGING_SYSTEMS) {
            return mqMessagingSystem;
        }

        throw new IllegalStateException("Cannot load mq messaging system"); // TODO error handling ??
    }
}