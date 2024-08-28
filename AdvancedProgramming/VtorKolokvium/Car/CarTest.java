package VtorKolokvium.Car;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Car implements Comparable<Car> {
    private final String manufacturer;
    private final String model;
    private final int price;
    private final float horsePower;

    public Car(String manufacturer, String model, int price, float horsePower) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.horsePower = horsePower;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public float getHorsePower() {
        return horsePower;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%.0fKW) %d", manufacturer, model, horsePower, price);
    }

    @Override
    public int compareTo(Car other) {
        return Integer.compare(this.getPrice(), other.getPrice());
    }
}

class CarCollection {
    private List<Car> cars;

    public CarCollection() {
        cars = new ArrayList<>();
    }


    public void addCar(Car car) {

        cars.add(car);
    }

    public void sortByPrice(boolean b) {
        if (b) {
            cars = cars.stream()
                    .sorted(Comparator.comparing(Car::getPrice)
                            .thenComparing(Car::getHorsePower))
                    .collect(Collectors.toList());
        } else {
            cars = cars.stream()
                    .sorted(Comparator.comparing(Car::getPrice).thenComparing(Car::getHorsePower).reversed())
                    .collect(Collectors.toList());
        }
    }

    public List<Car> getList() {
        return cars;
    }

    public List<Car> filterByManufacturer(String manufacturer) {
        return cars.stream()
                .filter(car -> car.getManufacturer().equalsIgnoreCase(manufacturer.toLowerCase()))
                .sorted(Comparator.comparing(Car::getModel))
                .collect(Collectors.toList());
    }
}

public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if (parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}