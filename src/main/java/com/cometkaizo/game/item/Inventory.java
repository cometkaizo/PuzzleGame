package com.cometkaizo.game.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: TODO
 * Description: This class represents the player's inventory of items
 */
public class Inventory implements Iterable<Item> {
    private List<Item> items = new ArrayList<>();

    public void add(Item item) {
        items.add(item);
    }

    public boolean contains(Item item) {
        return items.contains(item);
    }
    public boolean contains(Class<? extends Item> type) {
        return items.stream().anyMatch(type::isInstance);
    }

    public boolean remove(Item item) {
        return items.remove(item);
    }
    public boolean remove(Class<? extends Item> type) {
        return items.removeIf(type::isInstance);
    }

    public Item get(int index) {
        return items.get(index);
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }
    public List<Item> asList() {
        return items;
    }

    public int size() {
        return items.size();
    }
}
