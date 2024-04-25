package Aud4;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Square {
    int size;

    public Square(int size) {
        this.size = size;
    }

    int getPerimeter(){
        return 4 * size;
    }
}

class Canvas implements Comparable<Canvas> {
    String ID;
    List<Square> squareList;

    public Canvas(String ID, List<Square> squareList) {
        this.ID = ID;
        this.squareList = squareList;
    }

    public static Canvas create(String line){
        String[] parts = line.split("\\s+");
        String ID = parts[0];
        List<Square> squares = new ArrayList<>();

        squares = Arrays.stream(parts)
                .skip(1)
                .map(str -> new Square(Integer.parseInt(str)))
                .collect(Collectors.toList());
        return new Canvas(ID,squares);
    }

    int totalSquares(){
        return squareList.size();
    }

    int totalPerimeter(){
        return squareList.stream().mapToInt(Square::getPerimeter).sum();
    }


    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(this.totalPerimeter(),o.totalPerimeter());
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",ID,totalSquares(),totalPerimeter());
    }
}

class ShapesApplication {
    List<Canvas> canvases;
    public ShapesApplication() {
        canvases = new ArrayList<>();
    }

    public int readCanvases (InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        canvases = br.lines()
                .map(Canvas::create)
                .collect(Collectors.toList());

        br.close();
        return canvases.stream()
                .mapToInt(Canvas::totalSquares)
                .sum();
    }

    public void printLargestCanvasTo(PrintStream out) {
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
        try {
            System.out.println(shapesApplication.readCanvases(System.in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);
    }
}