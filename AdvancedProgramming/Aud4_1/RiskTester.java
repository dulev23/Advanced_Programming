package Aud4_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Round {
    List<Integer> attacker;
    List<Integer> defender;

    public Round(List<Integer> attacker, List<Integer> defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public Round(String input) {
        String[] parts = input.split(";");

        this.attacker = parseDice(parts[0]);
        this.defender = parseDice(parts[1]);
    }

    private List<Integer> parseDice(String input) {
        return Arrays.stream(input.split("\\s+"))
                .map(dice -> Integer.parseInt(dice))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Round{" +
                "attacker=" + attacker +
                ", defender=" + defender +
                '}';
    }

    public boolean hasAttackedWin() {

        return IntStream.range(0, attacker.size())
                .allMatch(i -> attacker.get(i) > defender.get(i));

//        for (int i = 0; i < attacker.size(); i++) {
//            if (attacker.get(i) <= defender.get(i)) {
//                return false;
//            }
//        }
//        return true;
    }
}

class Risk {
    public int processAttacksData(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        int result = (int) br.lines()
                .map(line -> new Round(line))
                .filter(round -> round.hasAttackedWin())
                .count();
        br.close();
        return result;
    }

}

public class RiskTester {

    public static void main(String[] args) throws IOException {
        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));
    }
}