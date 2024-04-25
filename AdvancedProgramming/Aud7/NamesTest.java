package Aud7;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

class Name implements Comparable<Name> {
    String name;
    int frequency;

    public Name(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }


    @Override
    public int compareTo(Name o) {
        return Integer.compare(this.frequency, o.frequency);
    }
}

public class NamesTest {
    private static Map<String, Integer> readNames(InputStream is) {
        Map<String, Integer> frequencyForName = new HashMap<>();
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String name = parts[0];
            int frequency = Integer.parseInt(parts[1]);
            frequencyForName.put(name, frequency);
        }
        return frequencyForName;
    }

    public static void main(String[] args) {
        try {
            Map<String, Integer> boyNames = readNames(new FileInputStream("src/Aud7/data/boynames.txt"));
            Map<String, Integer> girlNames = readNames(new FileInputStream("src/Aud7/data/girlsnames.txt"));
//            System.out.println(girlNames);

            Set<String> allNames = new HashSet<>();

            allNames.addAll(boyNames.keySet());
            allNames.addAll(girlNames.keySet());

            Map<String, Integer> unisexNames = new HashMap<>();

            allNames.stream()
                    .filter(name -> boyNames.containsKey(name) && girlNames.containsKey(name))
                    .forEach(name -> unisexNames.put(name,boyNames.get(name) + girlNames.get(name)));

//            System.out.println(unisexNames);

            //sort the map by value in descending order
            unisexNames.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> System.out.println(String.format("%s -> %d", entry.getKey(), entry.getValue())));

            //create custom objects of type name and then sort them
//            allNames.stream()
//                    .filter(name -> boyNames.containsKey(name) && girlNames.containsKey(name))
//                    .map(name -> new Name(name,boyNames.get(name)+girlNames.get(name)))
//                    .sorted(Comparator.reverseOrder())
//                    .forEach(System.out::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
