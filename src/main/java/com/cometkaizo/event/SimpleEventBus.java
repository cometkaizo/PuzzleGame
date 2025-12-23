package com.cometkaizo.event;

import java.util.*;
import java.util.function.Consumer;

public class SimpleEventBus implements EventBus {
    private final Map<Object, Map<Class<? extends Event>, List<Consumer<?>>>> listeners = new HashMap<>(3);
    private final Set<Object> pendingRemoval = new HashSet<>(3);

    public SimpleEventBus() {

    }

    @Override
    public void post(Event event) {
        if (!pendingRemoval.isEmpty()) {
            pendingRemoval.forEach(listeners::remove);
            pendingRemoval.clear();
        }

        listeners.forEach((_, ls) ->
                ls.forEach((type, l) ->
                        tryListen(type, l, event)));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Event> void tryListen(Class<? extends T> listeningType, List<Consumer<?>> listeners, Event event) {
        if (listeningType.isAssignableFrom(event.getClass())) {
            listeners.forEach(listener -> ((Consumer<? super T>) listener).accept((T) event));
        }
    }

    @Override
    public <T extends Event> void register(Object key, Class<? extends T> type, Consumer<? super T> listener) {
        listeners.computeIfAbsent(key, _ -> new HashMap<>()).computeIfAbsent(type, _ -> new ArrayList<>(1)).add(listener);
    }

    @Override
    public void unregister(Object key) {
        pendingRemoval.add(key);
    }

}
