package com.cometkaizo.event;

import com.cometkaizo.world.Tickable;

import java.util.function.Consumer;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents a channel on which events are posted and listened for
 */
public interface EventBus extends Tickable {
    /**
     * Author: Andy Wang
     * Date Modified: TODO
     * Description: Posts the given event and notifies all listeners
     */
    void post(Event event);
    /// Author: Andy Wang
    /// Date Modified: TODO
    /// Registers a listener to this event bus with the specified event type and the specified key.
    /// The specified listener cannot be unregistered unless the exact instance is retained.
    default <T extends Event> void register(Class<? extends T> type, Consumer<? super T> listener) {
        register(listener, type, listener);
    }
    /// Author: Andy Wang
    /// Date Modified: TODO
    /// Registers a listener to this event bus with the specified event type and the specified key.
    /// The key is used to unregister the listener. If the listener is never unregistered, it is
    /// preferable to use {@link #register(Class, Consumer)}
    <T extends Event> void register(Object key, Class<? extends T> type, Consumer<? super T> listener);
    /// Author: Andy Wang
    /// Date Modified: TODO
    /// Unregisters all listeners that were registered with the specified key
    void unregister(Object key);

    /// Author: Andy Wang
    /// Date Modified: TODO
    /// Ticks this event bus for internal operations such as adding and removing listeners
    @Override
    void tick();
}
