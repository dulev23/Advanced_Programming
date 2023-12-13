package PrvKolokvium;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

class Equation<T,E>{
    Supplier<T> supplier;
    Function<T,E> function;

    public Equation(Supplier<T> supplier, Function<T, E> function) {
        this.supplier = supplier;
        this.function = function;
    }

    Optional<E> calculate(){
        T input = supplier.get(); //supplier
        E result = function.apply(input); //function
        return Optional.of(result);
    }

}

class EquationProcessor{
    public static <T,E> void process(List<T> inputList,List<Equation<T,E>> equationList){
        for (T input : inputList){
            System.out.println("Input: " + input);
        }
        for (Equation<T, E> equation : equationList){
            Optional<E> result = equation.calculate();
            System.out.println("Result: " + result.get());
        }
    }
}

class Line {
    Double coefficient;
    Double x;
    Double intercept;

    public Line(Double coefficient, Double x, Double intercept) {
        this.coefficient = coefficient;
        this.x = x;
        this.intercept = intercept;
    }

    public static Line createLine(String line) {
        String[] parts = line.split("\\s+");
        return new Line(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2])
        );
    }

    public double calculateLine() {
        return coefficient * x + intercept;
    }

    @Override
    public String toString() {
        return String.format("%.2f * %.2f + %.2f", coefficient, x, intercept);
    }
}

public class EquationTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { // Testing with Integer, Integer
            List<Equation<Integer, Integer>> equations1 = new ArrayList<>();
            List<Integer> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(Integer.parseInt(sc.nextLine()));
            }

            // TODO: Add an equation where you get the 3rd integer from the inputs list, and the result is the sum of that number and the number 1000.
                Equation<Integer,Integer> eq1 = new Equation<>(() -> inputs.get(2),x -> x + 1000);
                equations1.add(eq1);

            // TODO: Add an equation where you get the 4th integer from the inputs list, and the result is the maximum of that number and the number 100.
                Equation<Integer,Integer> eq2 = new Equation<>(() -> inputs.get(3), x-> Math.max(x,100));
                equations1.add(eq2);

            EquationProcessor.process(inputs, equations1);

        } else { // Testing with Line, Integer
            List<Equation<Line, Double>> equations2 = new ArrayList<>();
            List<Line> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(Line.createLine(sc.nextLine()));
            }

            //TODO Add an equation where you get the 2nd line, and the result is the value of y in the line equation.
                Equation<Line,Double> eq3 = new Equation<>(() -> inputs.get(1),line -> line.calculateLine());
                equations2.add(eq3);

            //TODO Add an equation where you get the 1st line, and the result is the sum of all y values for all lines that have a greater y value than that equation.
                Equation<Line,Double> eq4 = new Equation<>(() -> inputs.get(0),
                        line -> inputs.stream().filter(line1 -> line1.calculateLine() > line.calculateLine()).mapToDouble(Line::calculateLine).sum());
                equations2.add(eq4);
            EquationProcessor.process(inputs, equations2);
        }
    }
}