package Aud7;

import java.util.*;

public class SetIntro {
    public static void main(String[] args) {
        //pristap O(logn), iteriranje O(nlogn), dodavanje O(logn), brisenje O(nlogn)
        //zadaci vo koi se bara da se cuvaat unikatni elementi i istite da se sortirani
        Set<Integer> treeIntSet = new TreeSet<>(Comparator.reverseOrder());
        for (int i = 1; i <= 10; i++) {
            treeIntSet.add(i);
        }
        System.out.println(treeIntSet);

        //najednostavna vremenska kompleksnost
        //Ako se bara vnesuvanje na elementi so O(n) togas ova e potrebno
        //nema duplikati
        //redosledot se izmestuva
        Set<Integer> hashIntSet = new HashSet<>();
        for (int i = 1; i <= 10; i++) {
            hashIntSet.add(i);
        }
        System.out.println(hashIntSet);


        Set<String> hashStringSet = new HashSet<>();
        hashStringSet.add("FINKI");
        hashStringSet.add("finki");
        hashStringSet.add("NP");
        hashStringSet.add("Napredno");

        System.out.println(hashStringSet);

        //LinkedHashSet
        //za da se zacuva redosledot na vnesuvanje
        //O(1) (kako obicen HashSet)
        Set<String> linkedHashStringSet = new HashSet<>();
        linkedHashStringSet.add("FINKI");
        linkedHashStringSet.add("finki");
        linkedHashStringSet.add("NP");
        linkedHashStringSet.add("Napredno");

        System.out.println(linkedHashStringSet);
    }
}
