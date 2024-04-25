package Aud3;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Student implements Comparable<Student> {
    String lastName;
    String firstName;
    int exam1;
    int exam2;
    int exam3;

    public Student(String line) {
        String[] parts = line.split(":");
        this.lastName = parts[0];
        this.firstName = parts[1];
        this.exam1 = Integer.parseInt(parts[2]);
        this.exam2 = Integer.parseInt(parts[3]);
        this.exam3 = Integer.parseInt(parts[4]);
    }

    public double totalPoints() {
        return exam1 * 0.25 + exam2 * 0.3 + exam3 * 0.45;
    }

    public String grade() {
        double pts = totalPoints();
        if (pts >= 90) {
            return "A";
        } else if (pts >= 80) {
            return "B";
        } else if (pts >= 70) {
            return "C";
        } else if (pts >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(this.totalPoints(), other.totalPoints());
    }

    @Override
    public String toString() {
        return String.format("%s %s %d %d %d $.2f %s", lastName, firstName, exam1, exam2, exam3, totalPoints(), grade());
    }
}

class GradeSystem {

    List<Student> students = new ArrayList<>();

    public void loadData(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        students = br.lines()
                .map(line -> new Student(line))
                .collect(Collectors.toList());
    }

    public void printFinalResults(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        students.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(student -> pw.println(student));

        pw.flush();
        pw.close();
    }
}

public class GradesTester {
    public static void main(String[] args) {
        try {
            InputStream isFromFile = new FileInputStream(new File("C:\\Users\\Administrator\\IdeaProjects\\Napredno Programiranje\\src\\Aud3\\files\\students.txt"));
            GradeSystem gradeSystem = new GradeSystem();

            gradeSystem.loadData(isFromFile);

            gradeSystem.printFinalResults(System.out);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
