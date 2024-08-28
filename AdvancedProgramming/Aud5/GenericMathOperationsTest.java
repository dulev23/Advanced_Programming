package Aud5;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GenericMathOperationsTest {
    public static String statistics(List<? extends Number> numbers) {
        DoubleSummaryStatistics dss = new DoubleSummaryStatistics();
        numbers.forEach(i -> dss.accept(i.doubleValue()));

        double standardDeviation = 0;

        for (Number n : numbers) {
            standardDeviation += (n.doubleValue() - dss.getAverage())
                    * (n.doubleValue() - dss.getAverage());
        }

        double finalStandardDeviation = Math.sqrt(standardDeviation / numbers.size());

        return String.format("""
                        Min: %.2f
                        Max: %.2f
                        Average: %.2f
                        Standard deviation: %.2f
                        Count: %d
                        Sum: %.2f""",
                dss.getMin(),
                dss.getMax(),
                dss.getAverage(),
                finalStandardDeviation,
                dss.getCount(),
                dss.getSum());
    }

    public static void main(String[] args) {
        Random random = new Random();

        List<Integer> integers = new ArrayList<>();
        IntStream.range(0, 100000)
                .forEach(i -> integers.add(random.nextInt(100) + 1));

        List<Double> doubles = new ArrayList<>();
        IntStream.range(0, 100000)
                .forEach(i -> doubles.add(random.nextDouble() * 100.0));

        System.out.println(statistics(integers));
        System.out.println(statistics(doubles));
    }
}