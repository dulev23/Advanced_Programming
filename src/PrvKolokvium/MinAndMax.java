package PrvKolokvium;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {
    T min;
    T max;
    int counter;
    int maxCounter;
    int minCounter;

    public MinMax() {
        this.min = null;
        this.max = null;
        this.counter = 0;
        this.maxCounter = 0;
        this.minCounter = 0;
    }

    void update(T element) {
        if(element==null){
            return;
        }
        if (min == null || element.compareTo(min) < 0) {
            min = element;
            minCounter = 1;
        } else if (element.equals(min)) {
            minCounter++;
        }
        if (max == null || element.compareTo(max) > 0) {
            max = element;
            maxCounter = 1;
        } else if (element.equals(max)) {
            maxCounter++;
        }
        counter++;
    }

    T max() {
        return max;
    }

    T min() {
        return min;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n", min(), max(), counter - maxCounter - minCounter);
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}