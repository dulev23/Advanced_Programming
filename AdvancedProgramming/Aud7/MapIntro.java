package Aud7;

import java.util.*;

public class MapIntro {
    public static void main(String[] args) {
        //Mapite se koristat za broenje pojavuvanja na elementi, za grupiranje
        //TreeMap
        //mora klucot da bide comparable
        //izbegnuva duplikat klucevi
        //mapata e sortirana spored klucot
        //O(logn) za dodavanje, O(logn) za contains, O(logn) iteriranje
        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("FINKI","FINKI");
        treeMap.put("FinKI","Finki");
        treeMap.put("NP","Napredno programiranje");
        treeMap.put("F","Fakultet");
        treeMap.put("I","Informaticki");
        treeMap.put("F","Fakultetttt");

        System.out.println(treeMap);

        //HashMap
        //O(1) dodavanje, O(1) contains, O(N) iteriranje
        //go izmestuva redosledot
        //elementite sto se vo tip kluc na mapata mora da ima overriden hashCode method
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("FINKI","FINKI");
        hashMap.put("FinKI","Finki");
        hashMap.put("NP","Napredno programiranje");
        hashMap.put("F","Fakultet");
        hashMap.put("I","Informaticki");
        hashMap.put("F","Fakultetttt");

        System.out.println(hashMap);

        //LinkedHashMap
        //ista kompleksnost so HashMap
        //go zadrzuva redosledot
        Map<String,String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("FINKI","FINKI");
        linkedHashMap.put("FinKI","Finki");
        linkedHashMap.put("NP","Napredno programiranje");
        linkedHashMap.put("F","Fakultet");
        linkedHashMap.put("I","Informaticki");
        linkedHashMap.put("F","Fakultetttt");

        System.out.println(linkedHashMap);
    }
}
