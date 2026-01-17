package com.cometkaizo.event;

import java.util.*;
import java.util.function.Consumer;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: Simple implementation of an EventBus
 */
public class SimpleEventBus implements EventBus {
    private final Map<Object, Map<Class<? extends Event>, List<Consumer<?>>>> listeners = Collections.synchronizedMap(new LinkedHashMap<>(3));
    private final Set<Object> pendingRemoval = new HashSet<>(3);
    private final Set<PendingAddition<?>> pendingAddition = new LinkedHashSet<>(10);

    /**
     * Author: Andy Wang
     * Date Modified: TODO
     * Description: Posts the given event
     */
    @Override
    public void post(Event event) {
        listeners.forEach((_, ls) ->
                ls.forEach((type, l) ->
                        tryListen(type, l, event)));
    }

    /**
     * Author: Andy Wang
     * Date Modified: TODO
     * Description: Tries to notify each of the given listeners
     */
    @SuppressWarnings("unchecked")
    private static <T extends Event> void tryListen(Class<? extends T> listeningType, List<Consumer<?>> listeners, Event event) {
        if (listeningType.isAssignableFrom(event.getClass())) {
            listeners.forEach(listener -> ((Consumer<? super T>) listener).accept((T) event));
        }
    }

    /**
     * Author: Andy Wang
     * Date Modified: TODO
     * Description: Registers an event listener
     */
    @Override
    public <T extends Event> void register(Object key, Class<? extends T> type, Consumer<? super T> listener) {
        pendingAddition.add(new PendingAddition<>(key, type, listener));
    }

    /**
     * Author: Andy Wang
     * Date Modified: TODO
     * Description: Registers all event listeners registered with the given key
     */
    @Override
    public void unregister(Object key) {
        pendingRemoval.add(key);
    }

    /// Author: Andy Wang
    /// Date Modified: TODO
    /// Ticks this event bus for internal operations such as adding and removing listeners
    @Override
    public void tick() {
        try {
            if (!pendingAddition.isEmpty()) {
                pendingAddition.forEach(p -> p.add(listeners));
                pendingAddition.clear();
            }
            if (!pendingRemoval.isEmpty()) {
                pendingRemoval.forEach(listeners::remove);
                pendingRemoval.clear();
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Author: Andy Wang
     * Date Modified: TODO
     * Description: This class represents an event listener waiting to be added
     */
    private record PendingAddition<T extends Event>(Object key, Class<? extends T> type, Consumer<? super T> listener) {
        public void add(Map<Object, Map<Class<? extends Event>, List<Consumer<?>>>> listeners) {
            listeners.computeIfAbsent(key, _ -> new HashMap<>()).computeIfAbsent(type, _ -> new ArrayList<>(1)).add(listener);
        }
    }

}
