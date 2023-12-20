package Aud6;


import java.util.Collection;

public class CountOccurrencesTest {
    public static int count1(Collection<Collection<String>> c, String str) {
        int counter = 0;
        for (Collection<String> collection : c) {
            for (String element : collection) {
                if (element.equalsIgnoreCase(str)) {
                    ++counter;
                }
            }
        }
        return counter;
    }

    public static int count2(Collection<Collection<String>> c, String str) {
        return (int) c.stream()
                .flatMap(col -> col.stream())
                .filter(string -> string.equalsIgnoreCase(str))
                .count();
    }

    public static void main(String[] args) {

    }
}