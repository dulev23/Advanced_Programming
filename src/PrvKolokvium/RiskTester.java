package PrvKolokvium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public void countWinner() {
        int countAttacker = 0, countDefender = 0;
        for (int i = 0; i < attacker.size(); i++) {
            if (attacker.get(i) > defender.get(i)) {
                countAttacker++;
            } else {
                countDefender++;
            }
        }
        System.out.println(countAttacker + " " + countDefender);
    }
}

class Risk {
    public void processAttacksData(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        br.lines().map(lines -> new Round(lines)).forEach(lines -> lines.countWinner());
        br.close();
    }

}

public class RiskTester {
    public static void main(String[] args) throws IOException {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}