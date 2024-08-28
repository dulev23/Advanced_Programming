package Aud5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

interface Drawable<T> {
    T draw();
}

class Circle implements Drawable {
    @Override
    public Object draw() {
        return this;
    }
}

/**
 * V - value
 * T - type
 * E - element
 */

public class Box<E extends Drawable> {
    private List<E> elements;
    public static Random random = new Random();

    public Box() {
        elements = new ArrayList<>();
    }

    public void add(E element) {
        elements.add(element);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public E draw() {
        if (isEmpty()) return null;

        return elements.remove(random.nextInt(elements.size()));
    }


    public static void main(String[] args) {
        Box<Circle> box = new Box<Circle>();

        IntStream.range(0, 100)
                .forEach(i -> new Circle());

        IntStream.range(0, 103)
                .forEach(element -> System.out.println(box.draw()));

    }
}
