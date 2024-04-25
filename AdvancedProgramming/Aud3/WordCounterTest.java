package Aud3;

import javax.sound.sampled.Line;
import java.io.*;
import java.nio.Buffer;
import java.util.Scanner;
import java.util.function.Consumer;

class LineConsumer implements Consumer<String> {

    int lines = 0, words = 0, chars = 0;
    @Override
    public void accept(String s) {
        ++lines;
        words+=s.split("\\s+").length;
        chars+=s.length();
    }

    @Override
    public String toString() {
        return String.format("Lines: %d Words: %d Characters: %d", lines, words, chars);
    }

}

class WordCounter{
    public static void count (InputStream is){
        Scanner sc = new Scanner(is);
        int l = 0, w = 0, c = 0;
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            ++l;
            String[] words = line.split("\\s+");
            w+=words.length;
            c+=line.length();
        }

        System.out.println(String.format("Lines: %d Words: %d Characters: %d", l, w, c));
    }

    public static void countWithStream(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        LineConsumer consumer = new LineConsumer();
        br.lines().forEach(consumer);
        System.out.println(consumer);
    }
}
public class WordCounterTest {
    public static void main(String[] args) {
        try {
            InputStream isFromFile = new FileInputStream("C:\\Users\\Administrator\\IdeaProjects\\Napredno Programiranje\\src\\Aud3\\files\\source.txt");
//            WordCounter.count(isFromFile);
            WordCounter.countWithStream(isFromFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
