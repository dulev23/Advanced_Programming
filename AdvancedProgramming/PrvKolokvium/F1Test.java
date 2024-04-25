package PrvKolokvium;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        try {
            f1Race.readResults(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        f1Race.printSorted(System.out);
    }
}

class Lap {
    double mm; //minutes
    double ss; //seconds
    double nnn; //milliseconds

    public Lap(double mm, double ss, double nnn) {
        this.mm = mm;
        this.ss = ss;
        this.nnn = nnn;
    }

    @Override
    public String toString() {
        return String.format("%.0f:%02.0f:%03.0f", mm, ss, nnn);
    }
}

class F1Racer {
    String name;
    List<Lap> lap;

    public F1Racer(String name, List<Lap> lap) {
        this.name = name;
        this.lap = lap;
    }

    public String getName() {
        return name;
    }

    Lap fastestLap() {
        if (lap == null || lap.isEmpty()) {
            return null;
        }
        Lap max = lap.get(0);
        for (int i = 1; i < lap.size(); i++) {
            Lap currentLap = lap.get(i);
            if (currentLap.mm < max.mm || (currentLap.mm == max.mm && currentLap.ss < max.ss) || (currentLap.mm == max.mm && currentLap.ss == max.ss && currentLap.nnn < max.nnn)) {
                max = currentLap;
            }
        }
        return max;
    }

    @Override
    public String toString() {
        return "F1Racer{" +
                "name='" + name + '\'' +
                ", lap=" + lap +
                '}';
    }
}

class F1Race {
    private List<F1Racer> racers;

    public F1Race() {
        racers = new ArrayList<>();
    }

    void readResults(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            String driverName = parts[0];
            List<Lap> laps = new ArrayList<>();

            for (int i = 1; i < parts.length; i++) {
                String lapTimeStr = parts[i];
                String[] lapTimeParts = lapTimeStr.split(":");
                double mm = Double.parseDouble(lapTimeParts[0]);
                double ss = Double.parseDouble(lapTimeParts[1]);
                double nnn = Double.parseDouble(lapTimeParts[2]);

                laps.add(new Lap(mm, ss, nnn));
            }

            racers.add(new F1Racer(driverName, laps));
        }
    }

    void printSorted(OutputStream outputStream) {
        Collections.sort(racers, Comparator.comparingDouble(racer -> racer.fastestLap().mm * 60 + racer.fastestLap().ss + racer.fastestLap().nnn / 1000));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));

        int position = 1;
        for (F1Racer racer : racers) {
            String formattedOutput = String.format("%d. %-11s %-11s",position, racer.getName(), racer.fastestLap());
            position++;
            writer.println(formattedOutput);
        }

        writer.flush();
    }
}