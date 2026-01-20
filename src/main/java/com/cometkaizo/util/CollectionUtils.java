package com.cometkaizo.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: List of useful collections-related methods
 */
public class CollectionUtils {

    /// Returns the index of the first element in the given list that matches the condition, if any
    public static <T> int indexOf(List<T> list, Predicate<T> condition) {
        for (int index = 0; index < list.size(); index++) {
            T element = list.get(index);
            if (condition.test(element)) return index;
        }
        return -1;
    }
    /// Returns the first element in the given collection that matches the condition, if any
    public static <T> Optional<T> getFirst(Collection<T> collection, Predicate<T> condition) {
        for (T element : collection) {
            if (condition.test(element)) return Optional.ofNullable(element);
        }
        return Optional.empty();
    }

    /// Finds the "largest" element in the collection as specified by the value function
    public static <T, C extends Comparable<C>> T findMax(Collection<T> collection, Function<T, C> valueFunction) {
        T largestElement = null;
        C largestValue = null;

        for (T element : collection) {
            C value = valueFunction.apply(element);
            if (largestValue == null || largestValue.compareTo(value) < 0) {
                largestValue = value;
                largestElement = element;
            }
        }

        return largestElement;
    }

    /// Finds the "smallest" element in the collection as specified by the value function
    public static <T, C extends Comparable<C>> T findMin(Collection<T> collection, Function<T, C> valueFunction) {
        T smallestElement = null;
        C smallestValue = null;

        for (T element : collection) {
            C value = valueFunction.apply(element);
            if (smallestValue == null || smallestValue.compareTo(value) > 0) {
                smallestValue = value;
                smallestElement = element;
            }
        }

        return smallestElement;
    }

    /// Performs an operation on all elements in the array
    public static <T> void forEach(T[] array, Consumer<T> operation) {
        for (T element : array) {
            operation.accept(element);
        }
    }

    /// Maps each element in given array to an object and returns the new array
    public static <T, R> Object[] map(T[] array, Function<T, Object> function) {
        Object[] resultArray = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            T element = array[i];
            resultArray[i] = function.apply(element);
        }
        return resultArray;
    }

    /// Maps each element in given array to something else and returns the new array
    public static <T, R> R[] map(T[] array, Function<T, R> function, IntFunction<R[]> arrayGenerator) {
        R[] resultArray = arrayGenerator.apply(array.length);
        for (int i = 0; i < array.length; i++) {
            T element = array[i];
            resultArray[i] = function.apply(element);
        }
        return resultArray;
    }

    /// Maps each element in the given collection to something else
    public static <T, R> ArrayList<R> map(Collection<T> collection, Function<T, R> function) {
        ArrayList<R> resultList = new ArrayList<>(collection.size());
        for (var element : collection) {
            resultList.add(function.apply(element));
        }
        return resultList;
    }

    /// Maps each element in the given collection to something else
    public static <T, R, C extends Collection<R>> C map(Collection<T> collection, Function<T, R> function, IntFunction<C> collectionGenerator) {
        C resultCollection = collectionGenerator.apply(collection.size());
        for (var element : collection) {
            resultCollection.add(function.apply(element));
        }
        return resultCollection;
    }

}
