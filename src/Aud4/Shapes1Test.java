package Aud4;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Square {
    int side;

    public Square(int side) {
        this.side = side;
    }

    public int getPerimeter() {
        return 4 * side;
    }

}

class Canvas implements Comparable<Canvas> {
    String canvasID;
    List<Square> squares;

    public Canvas(String canvasID, List<Square> squares) {
        this.canvasID = canvasID;
        this.squares = squares;
    }

    public static Canvas createCanvas(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];

//        List<Square> squares = new ArrayList<>();
//        for (int i = 1; i < parts.length; i++){
//            squares.add(new Square(Integer.parseInt(parts[i])));
//        }

        List<Square> squares = Arrays.stream(parts).skip(1)
                .map(part -> Integer.parseInt(part))
                .map(side -> new Square(side))
                .collect(Collectors.toList());

        return new Canvas(id, squares);
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",
                canvasID,
                squares.size(),
                sumOfPerimeter());
    }

    private int sumOfPerimeter(){
        return squares.stream().mapToInt(Square::getPerimeter).sum();
    }
    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(this.sumOfPerimeter(),o.sumOfPerimeter());
    }
}

class ShapesApplication {
    List<Canvas> canvases;
    public int readLargestCanvasTo(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        canvases = br.lines()
                .map(line -> Canvas.createCanvas(line))
                .collect(Collectors.toList());
        return canvases.stream()
                .mapToInt(canvas -> canvas.squares.size())
                .sum();
    }
    public void printLargestCanvasTo(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        Canvas max = canvases.stream().max(Comparator.naturalOrder()).get();

        pw.println(max);
        pw.flush();
    }
}
public class Shapes1Test {
    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readLargestCanvasTo(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);
    }
}