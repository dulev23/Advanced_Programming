package Aud3;

import java.io.*;
import java.util.Comparator;

class Person implements Comparable<Person>{
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String line){
        String [] parts = line.split("\\s+");
        this.name = parts[0];
        this.age = Integer.parseInt(parts[1]);
    }
    @Override
    public String toString() {
        return "Person( "
                + "Name = '"
                + name + '\''
                + ", Age = "
                + age + ')';
    }

    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age,other.age);
    }
}
public class OldestPersonTest {
    public static Person find (InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.lines()
                .map(line -> new Person(line))
                .max(Comparator.naturalOrder())
                .orElse(new Person("Stefan", 27));
    }
    public static void main(String[] args) {
        try {
            InputStream isFromFile = new FileInputStream("C:\\Users\\Administrator\\IdeaProjects\\Napredno Programiranje\\src\\Aud3\\files\\people.txt");
            System.out.println(OldestPersonTest.find(isFromFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
