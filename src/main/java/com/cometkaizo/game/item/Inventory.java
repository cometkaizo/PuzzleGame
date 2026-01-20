package com.cometkaizo.game.item;

import com.cometkaizo.game.GameState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: This class represents the player's inventory of items
 */
public class Inventory implements Iterable<Item> {
    private List<Item> items = new ArrayList<>();

    /// Adds an item to this inventory
    public void add(Item item) {
        items.add(item);
    }

    /// Returns whether the given item is in this inventory
    public boolean contains(Item item) {
        return items.contains(item);
    }
    /// Returns whether the given item type is in this inventory
    public boolean contains(Class<? extends Item> type) {
        return items.stream().anyMatch(type::isInstance);
    }

    /// Removes the given item
    public boolean remove(Item item) {
        return items.remove(item);
    }
    /// Removes the given item type
    public boolean remove(Class<? extends Item> type) {
        return items.removeIf(type::isInstance);
    }

    /// Gets the item at the given index
    public Item get(int index) {
        return items.get(index);
    }

    /// Returns an iterator for this inventory so that it can be used in a for-each loop
    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }
    /// Returns the list of items in this inventory
    public List<Item> asList() {
        return items;
    }

    /// Returns the number of items in this inventory
    public int size() {
        return items.size();
    }

    /// Writes this inventory to the given game state
    public void write(GameState state) {
        state.inventory = new ArrayList<>(items);
    }

    /// Sets the list of items to this inventory
    public void set(List<Item> inventory) {
        items = new ArrayList<>(inventory);
    }
}
