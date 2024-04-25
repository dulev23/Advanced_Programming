package PrvKolokvium;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = 0;
        try {
            n = subtitles.loadSubtitles(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

class TimeStamp {
    int hours;
    int minutes;
    int seconds;
    int milliseconds;

    public TimeStamp(String time) {
        String[] parts = time.split(":|,");
        this.hours = Integer.parseInt(parts[0]);
        this.minutes = Integer.parseInt(parts[1]);
        this.seconds = Integer.parseInt(parts[2]);
        this.milliseconds = Integer.parseInt(parts[3]);
    }

    public void addMilliseconds(int ms) {
        milliseconds += ms;
        if (milliseconds < 0) {
            int secondsToBorrow = Math.abs(milliseconds) / 1000 + 1;
            milliseconds = 1000 - Math.abs(milliseconds) % 1000;
            addSeconds(-secondsToBorrow);
        } else if (milliseconds >= 1000) {
            int secondsToAdd = milliseconds / 1000;
            milliseconds %= 1000;
            addSeconds(secondsToAdd);
        }
    }

    private void addSeconds(int seconds) {
        this.seconds += seconds;
        if (this.seconds < 0) {
            int minutesToBorrow = Math.abs(this.seconds) / 60 + 1;
            this.seconds = 60 - Math.abs(this.seconds) % 60;
            addMinutes(-minutesToBorrow);
        } else if (this.seconds >= 60) {
            int minutesToAdd = this.seconds / 60;
            this.seconds %= 60;
            addMinutes(minutesToAdd);
        }
    }

    private void addMinutes(int minutes) {
        this.minutes += minutes;
        if (this.minutes < 0) {
            int hoursToBorrow = Math.abs(this.minutes) / 60 + 1;
            this.minutes = 60 - Math.abs(this.minutes) % 60;
            addHours(-hoursToBorrow);
        } else if (this.minutes >= 60) {
            int hoursToAdd = this.minutes / 60;
            this.minutes %= 60;
            addHours(hoursToAdd);
        }
    }

    private void addHours(int hours) {
        this.hours += hours;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d,%03d",hours,minutes,seconds,milliseconds);
    }
}

class Subs {
    int index;
    String startTime;
    String endTime;
    String text;

    public Subs(int index, String startTime, String endTime, String text) {
        this.index = index;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    public void shift(int ms) {
        TimeStamp startTimeUtil = new TimeStamp(startTime);
        TimeStamp endTimeUtil = new TimeStamp(endTime);

        startTimeUtil.addMilliseconds(ms);
        endTimeUtil.addMilliseconds(ms);

        this.startTime = startTimeUtil.toString();
        this.endTime = endTimeUtil.toString();
    }

    @Override
    public String toString() {
        return String.format("%d\n%s --> %s\n%s", index, startTime, endTime, text);
    }
}
class Subtitles {
    List<Subs> subtitles;

    public Subtitles() {
        subtitles = new ArrayList<>();
    }

    public int loadSubtitles(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<Subs> elements = new ArrayList<>();
        int elementsRead = 0;
        String line;

        while ((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                int index = Integer.parseInt(line);
                String[] times = br.readLine().split(" --> ");
                StringBuilder textBuilder = new StringBuilder();
                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    textBuilder.append(line).append("\n");
                }
                String text = textBuilder.toString();
                elements.add(new Subs(index, times[0], times[1],text));
                elementsRead++;
            }
        }
        subtitles = elements;
        return elementsRead;
    }

    public void print() {
        for (Subs element : subtitles) {
            System.out.println(element.toString());
        }
    }

    void shift(int ms){
        for (Subs subs : subtitles){
            subs.shift(ms);
        }
    }
}