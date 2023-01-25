package org.gym.email;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Queue<T> {

    private List<T> storage = Collections.synchronizedList(new LinkedList<>());

    public void add(T object) {
        storage.add(object);
    }

    public T get() {
        if (!storage.isEmpty()) {
            return storage.remove(storage.size() - 1);
        }
        return null;
    }

}
