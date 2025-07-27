package andrew.learning.array;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MyArrayList<T> implements Iterable<T> {
    int size = 0;

    int capacity = 2;

    T[] elements;

    public MyArrayList() {
        elements = (T[]) new Object[capacity];
    }

    public void add(T element) {
        // 扩容
        if (size == capacity) {
            capacity += capacity >> 1;
            elements = Arrays.copyOf(elements, capacity);
        }
        elements[size++] = element;
    }

    public T get(int index) {
        return elements[index];
    }

    public void forEach2(Consumer<T> consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(elements[i]);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public T next() {
                return elements[cursor++];
            }
        };
    }

    public Stream<T> stream() {
        T[] newElements = Arrays.copyOf(elements, size);
        return Stream.of(newElements);
    }
}
