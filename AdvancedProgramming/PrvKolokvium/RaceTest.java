package PrvKolokvium;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Racer implements Comparable<Racer> {
    private LocalTime startTime;
    private LocalTime endTime;
    private String ID;
    private Duration duration;

    public Racer(String ID, LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.ID = ID;
        duration = Duration.between(startTime, endTime);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getID() {
        return ID;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public int compareTo(Racer o) {
        return this.duration.compareTo(o.duration);
    }

    @Override
    public String toString() {
        return String.format("%s %02d:%02d:%02d",
                ID,
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }
}

class TeamRace {
    private String ID;

    public static void findBestTeam(InputStream in, PrintStream out) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        PrintWriter pw = new PrintWriter(out);

        List<Racer> racers = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            String[] parts = line.split(" ");
            String id = parts[0];
            LocalTime start = LocalTime.parse(parts[1]);
            LocalTime end = LocalTime.parse(parts[2]);
            racers.add(new Racer(id, start, end));
            line = br.readLine();
        }

        Collections.sort(racers);

        List<Racer> bestTeam = racers.subList(0, Math.min(4, racers.size()));
        Duration totalDuration = bestTeam.stream()
                .map(Racer::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        racers.stream()
                .limit(4)
                .forEach(System.out::println);

        System.out.printf(String.format("%02d:%02d:%02d",
                totalDuration.toHours(),
                totalDuration.toMinutesPart(),
                totalDuration.toSecondsPart()));
        pw.flush();
    }
}

public class RaceTest {
    public static void main(String[] args) throws IOException {
        try {
            TeamRace.findBestTeam(System.in, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
