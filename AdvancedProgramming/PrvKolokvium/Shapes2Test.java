package PrvKolokvium;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

enum Type {
    C,
    S
}

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String id, double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", id, maxArea));
    }
}

class Shape {
    Type type;
    double size;
    double area;

    public Shape() {
    }

    double shapeArea() {
        if (type.equals(Type.C)) {
            return area = Math.PI * size * size;
        } else if (type.equals(Type.S)) {
            return area = size * size;
        }
        return 0;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setSize(double size) {
        this.size = size;
    }
}

class Canvas implements Comparable<Canvas> {
    String id;
    List<Shape> shapes;

    public Canvas(String id, List<Shape> shapes) {
        this.id = id;
        this.shapes = shapes;
    }

    public static Canvas create(String line, double maxArea) throws IrregularCanvasException {
        String[] parts = line.split("\\s+");
        String ID = parts[0];
        List<Shape> shapes = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 2) {
            Shape shape = new Shape();
            shape.setType(Type.valueOf(parts[i]));
            shape.setSize(Double.parseDouble(parts[i + 1]));
            shapes.add(shape);
        }

        if (shapes.stream().anyMatch(shape -> shape.shapeArea() > maxArea)) {
            throw new IrregularCanvasException(ID, maxArea);
        }

        return new Canvas(ID, shapes);
    }

    int countCircles() {
        return (int) shapes.stream().filter(shape -> shape.type.equals(Type.C)).count();
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics dss = shapes.stream().mapToDouble(Shape::shapeArea).summaryStatistics();
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                id,
                shapes.size(),
                countCircles(),
                shapes.size() - countCircles(),
                dss.getMin(),
                dss.getMax(),
                dss.getAverage());
    }

    @Override
    public int compareTo(Canvas o) {
        return Double.compare(this.shapes.stream().mapToDouble(Shape::shapeArea).sum(),
                o.shapes.stream().mapToDouble(Shape::shapeArea).sum());
    }
}

class ShapesApplication {
    double maxArea;
    List<Canvas> canvasList;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        this.canvasList = new ArrayList<>();
    }

    void readCanvases(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        canvasList = br.lines()
                .map(line -> {
                    try {
                        return Canvas.create(line, maxArea);
                    } catch (IrregularCanvasException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    void printCanvases(OutputStream os) {
        try (PrintWriter printWriter = new PrintWriter(os)) {
            canvasList.stream()
                    .sorted(Comparator.reverseOrder())
                    .forEach(printWriter::println);
            printWriter.flush();
        }
    }
}

public class Shapes2Test {
    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);
    }
}
