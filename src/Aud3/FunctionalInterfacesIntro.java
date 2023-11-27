package Aud3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FunctionalInterfacesIntro {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();

        strings.add("test");
        strings.add("blabla");
        strings.add("Makedonija");
        strings.add("NApredno programiranje test");

        //Function
        //mostly used in MAP function to convert one type into another

        Function<String, Integer> lengthFunction = s -> s.toLowerCase().indexOf("a");

        //Predicate (condition)
        //mostly used FILTER stream operator

        Predicate<Integer> positiveNumber = x -> x > 0;

        System.out.println(strings.stream()
                .map(lengthFunction)
                .filter(positiveNumber)
                .collect(Collectors.toList()));

        Supplier<Integer> supplier = () -> new Random().nextInt(10);

        for (int i = 0; i < 100; i++) {
            System.out.println(supplier.get());
        }

        //Consumer
        Consumer<String> consumer = str -> System.out.println(str);

        strings.stream().forEach(consumer);
    }
}
