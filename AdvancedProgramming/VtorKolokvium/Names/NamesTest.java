package VtorKolokvium.Names;

import java.util.*;
import java.util.stream.Collectors;

class Name {
    private final String name;
    private int nameOccurrence;

    public Name(String name) {
        this.name = name;
        this.nameOccurrence = 1;
    }

    //TODO:implement method correctly
    public int countDifferentLettersInWord() {
        String lowercase = name.toLowerCase();
        int differentLetter = 0;
        for (int i = 0; i < lowercase.length(); i++) {
            boolean isUnique = true;
            for (int j = 0; j < i; j++) {
                if (lowercase.charAt(i) == lowercase.charAt(j)) {
                    isUnique = false;
                    break;
                }
            }
            if (isUnique) {
                differentLetter++;
            }
        }
        return differentLetter;
    }

    public void incrementNameOccurrence() {
        nameOccurrence++;
    }

    public int getNameOccurrence() {
        return nameOccurrence;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d", name, nameOccurrence, countDifferentLettersInWord());
    }
}

class Names {
    private List<Name> names;

    public Names() {
        names = new ArrayList<>();
    }

    public void addName(String name) {
        for (Name n : names) {
            if (n.getName().equals(name)) {
                n.incrementNameOccurrence();
                return;
            }
        }
        names.add(new Name(name));
    }

    public void printN(int n) {
        names.stream()
                .filter(name -> name.getNameOccurrence() >= n)
                .sorted(Comparator.comparing(name -> name.getName().toLowerCase()))
                .forEach(System.out::println);

    }

    public String findName(int len, int index) {
        List<Name> filteredNames = names.stream()
                .filter(name -> name.getName().length() < len)
                .sorted(Comparator.comparing(name -> name.getName().toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (filteredNames.isEmpty()) {
            return "No such name found";
        }

        int adjustedIndex = index % filteredNames.size();
        return filteredNames.get(adjustedIndex).getName();
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}