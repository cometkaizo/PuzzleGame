package com.cometkaizo.event;

import java.util.function.Consumer;

public interface EventBus {
    void post(Event event);
    /// Registers a listener to this event bus with the specified event type and the specified key.
    /// The specified listener cannot be unregistered unless the exact instance is retained.
    default <T extends Event> void register(Class<? extends T> type, Consumer<? super T> listener) {
        register(listener, type, listener);
    }
    /// Registers a listener to this event bus with the specified event type and the specified key.
    /// The key is used to unregister the listener. If the listener is never unregistered, it is
    /// preferable to use {@link #register(Class, Consumer)}
    <T extends Event> void register(Object key, Class<? extends T> type, Consumer<? super T> listener);
    /// Unregisters all listeners that were registered with the specified key
    void unregister(Object key);
}
