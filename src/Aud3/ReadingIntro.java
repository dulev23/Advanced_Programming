package Aud3;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReadingIntro {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<String> input = new ArrayList<>();

//        while (sc.hasNextLine()) {
//            String line = sc.nextLine();
//            input.add(line);
//        }
//
//        System.out.println(input);
//
//        //Formatirano vcituvanje
//        int n;
//        n = sc.nextInt();
//        List<Integer> numbers = new ArrayList<>();
//        for (int i = 0; i < n; i++){
//            numbers.add(sc.nextInt());
//        }
//
//        System.out.println(numbers);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        input = br.lines().collect(Collectors.toList());

        System.out.println(input);

    }
}
