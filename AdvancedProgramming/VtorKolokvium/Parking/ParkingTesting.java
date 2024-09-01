package VtorKolokvium.Parking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

class Vehicle implements Comparable<Vehicle> {
    private final String registration;
    private final LocalDateTime enter;
    private final LocalDateTime exit;
    private final DateUtil parkingDuration;
    private final int numParking;
    private final boolean isParked;
    private final String spot;

    public Vehicle(String registration, LocalDateTime enter, LocalDateTime exit, DateUtil parkingDuration, boolean isParked, String spot) {
        this.registration = registration;
        this.enter = enter;
        this.exit = exit;
        this.parkingDuration = parkingDuration;
        numParking = 1;
        this.isParked = isParked;
        this.spot = spot;
    }

    public String getRegistration() {
        return registration;
    }

    public LocalDateTime getEnter() {
        return enter;
    }

    public String getSpot() {
        return spot;
    }

    public LocalDateTime getExit() {
        return exit;
    }

    public int getNumParking() {
        return numParking;
    }

    @Override
    public int compareTo(Vehicle o) {
        return this.enter.compareTo(o.enter);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "registration='" + registration + '\'' +
                ", enter=" + enter +
                ", exit=" + exit +
                ", parkingDuration=" + parkingDuration +
                ", numParking=" + numParking +
                ", isParked=" + isParked +
                ", spot='" + spot + '\'' +
                '}';
    }
}

class Parking {
    private final List<Vehicle> vehicleList;
    private final Map<String, Vehicle> parkingSpots;
    private final int capacity;

    public Parking(int capacity) {
        this.capacity = capacity;
        vehicleList = new ArrayList<>();
        this.parkingSpots = new HashMap<>();
    }

    public double getParkingCapacity() {
        return (double) parkingSpots.size() / capacity * 100;
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entrance) {
        if (entrance) {
            if (!parkingSpots.containsKey(spot)) {
                Vehicle vehicle = new Vehicle(registration, timestamp, null, null, true, spot);
                parkingSpots.put(spot, vehicle);
            }
        } else {
            if (parkingSpots.containsKey(spot)) {
                Vehicle vehicle = parkingSpots.get(spot);
                vehicle = new Vehicle(vehicle.getRegistration(), vehicle.getEnter(), timestamp, null, false, spot);
                parkingSpots.remove(spot);
                vehicleList.add(vehicle);
            }
        }
    }

    public void currentState() {
        System.out.printf("Capacity filled: %2.2f%%\n", getParkingCapacity());
        parkingSpots.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    Vehicle v1 = entry1.getValue();
                    Vehicle v2 = entry2.getValue();
                    String spot1 = entry1.getKey();
                    String spot2 = entry2.getKey();

                    int enterComparison = v2.getEnter().compareTo(v1.getEnter());
                    if (enterComparison != 0) {
                        return enterComparison;
                    } else {
                        return spot2.compareTo(spot1);
                    }
                })
                .forEach(vehicleEntry -> {
                    Vehicle vehicle = vehicleEntry.getValue();
                    String spot = vehicleEntry.getKey();
                    System.out.printf("Registration number: %s Spot: %s Start timestamp: %s\n", vehicle.getRegistration(), spot, vehicle.getEnter());
                });
    }

    public void history() {
        vehicleList.stream()
                .filter(vehicle -> vehicle.getEnter() != null && vehicle.getExit() != null)
                .sorted((v1, v2) -> {
                    long duration1 = DateUtil.durationBetween(v1.getEnter(), v1.getExit());
                    long duration2 = DateUtil.durationBetween(v2.getEnter(), v2.getExit());
                    return Long.compare(duration2, duration1);
                })
                .forEach(vehicle -> {
                    LocalDateTime enter = vehicle.getEnter();
                    LocalDateTime exit = vehicle.getExit();
                    long duration = DateUtil.durationBetween(enter, exit);
                    System.out.printf("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes: %d\n",
                            vehicle.getRegistration(),
                            vehicle.getSpot(),
                            enter,
                            exit,
                            duration);
                });
    }

    public Map<String, Integer> carStatistics() {
        Map<String, Integer> stats = new TreeMap<>();
        for (Vehicle vehicle : parkingSpots.values()) {
            String registration = vehicle.getRegistration();
            if (stats.containsKey(registration)) {
                stats.put(registration, stats.get(registration) + 1);
            } else {
                stats.put(registration, 1);
            }
        }

        for (Vehicle vehicle : vehicleList) {
            String registration = vehicle.getRegistration();
            int count = vehicle.getNumParking();
            if (stats.containsKey(registration)) {
                stats.put(registration, stats.get(registration) + 1);
            } else {
                stats.put(registration, count);
            }
        }

        return stats;
    }

    public Map<String, Double> spotOccupancy(LocalDateTime start, LocalDateTime end) {
        Map<String, Double> occupancyMap = new HashMap<>();

        long totalRangeMinutes = DateUtil.durationBetween(start, end);

        for (Map.Entry<String, Vehicle> entry : parkingSpots.entrySet()) {
            String spot = entry.getKey();
            Vehicle vehicle = entry.getValue();
            LocalDateTime vehicleEnter = vehicle.getEnter();
            LocalDateTime vehicleExit = vehicle.getExit();

            LocalDateTime overlapStart = vehicleEnter.isAfter(start) ? vehicleEnter : start;
            LocalDateTime overlapEnd = vehicleExit.isBefore(end) ? vehicleExit : end;

            if(overlapStart.isBefore(overlapEnd)){
                long occupiedTime = DateUtil.durationBetween(overlapStart,overlapEnd);
                double occupancyFraction = (double) occupiedTime / totalRangeMinutes;
                occupancyMap.put(spot,occupancyFraction);
            }else{
                occupancyMap.put(spot,0.0);
            }
        }

        return occupancyMap;
    }

    @Override
    public String toString() {
        return "Parking{" +
                "vehicleList=" + vehicleList +
                ", parkingSpots=" + parkingSpots +
                ", capacity=" + capacity +
                '}';
    }
}

public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.printf(String.format("%s -> %s\n", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}