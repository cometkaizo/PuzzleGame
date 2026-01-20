package com.cometkaizo.registry;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: Registry for something that depends on the game app (e.g., input bindings)
 */
public class Registry<T> {

    private GameApp app;
    private final Map<String, Function<GameApp, ? extends T>> entryFunctions = new LinkedHashMap<>(5);
    private final Map<String, T> entries = new LinkedHashMap<>(5);
    private Collection<T> entryView = List.of();

    /// Registers the given object with the given key
    @SuppressWarnings("unchecked")
    public <V extends T> Supplier<V> register(String key, Function<GameApp, V> objectFunc) {
        Objects.requireNonNull(objectFunc, "Object function cannot be null");
        Objects.requireNonNull(objectFunc, "Key cannot be null");
        throwIfDuplicateKey(key);

        addEntryFunction(key, objectFunc);
        if (app != null) {
            addEntry(key, objectFunc.apply(app));
            updateEntryView();
        }
        return () -> {
            V value = (V) entries.get(key);
            if (value == null) throw new IllegalStateException("Unknown key '" + key + "'; " + Registry.class.getSimpleName() + "#register(GameApp) has not been called");
            return value;
        };
    }

    /// Adds the given object with the given key
    private void addEntryFunction(String key, Function<GameApp, ? extends T> objectFunc) {
        entryFunctions.put(key, objectFunc);
    }

    /// Adds a given entry with the given key
    private void addEntry(String key, T value) {
        Objects.requireNonNull(value, "Contract violation: cannot add null entry");
        entries.put(key, value);
    }

    /// Throws an exception if the key is already registered
    private void throwIfDuplicateKey(String key) {
        Object duplicate = entryFunctions.get(key);
        if (duplicate != null) throw new IllegalArgumentException("Key '" + key + "' already exists for '" + duplicate + "'");
    }

    /// Registers this registry to the given app
    public void register(GameApp app) {
        this.app = app;
        for (String key : entryFunctions.keySet()) {
            Function<GameApp, ? extends T> valueFunc = entryFunctions.get(key);
            addEntry(key, valueFunc.apply(app));
        }
        updateEntryView();
    }

    /// Updates the unmodifiable view to the entries
    private void updateEntryView() {
        entryView = Collections.unmodifiableCollection(entries.values());
    }

    /// Returns the value associated with the key
    public T getValue(String key) {
        if (app == null) throw new IllegalStateException("Unknown key '" + key + "'; " + Registry.class.getSimpleName() + "#register(GameApp) has not been called");
        T result = entries.get(key);
        if (result == null) throw new NoSuchElementException("Unknown key '" + key + "'; available entries are: \n" + entries);
        return result;
    }

    /// Returns the key associated with the value
    public String getKey(T value) {
        if (app == null) throw new IllegalStateException("Unknown key '" + value + "'; " + Registry.class.getSimpleName() + "#register(GameApp) has not been called");
        var entry = CollectionUtils.getFirst(entries.entrySet(), e -> e.getValue() == value);
        if (entry.isEmpty()) throw new NoSuchElementException("Unknown value '" + value + "'; available entries are: \n" + entries);
        return entry.get().getKey();
    }

    /// Returns an unmodifiable view of the entries
    public Collection<T> values() {
        return entryView;
    }

}
