package Aud3;

import java.util.ArrayList;

public class ArrayListIntro {
    public static void main(String[] args) {
        ArrayList<String> strings; //string = null

        strings = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            strings.add(String.format("NP%d", i));
        }

        for (int i=0; i<15;i++){
            strings.add(String.format("2023-%d",i));
        }

        System.out.println(strings);

        strings.addFirst("Java");
        System.out.println(strings);

    }
}
