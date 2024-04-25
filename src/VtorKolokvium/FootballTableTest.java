package VtorKolokvium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;


class Team {
    String name;
    int matchesPlayed;
    int matchesWon;
    int matchesDrawn;
    int matchesLost;
    int totalPoints;
    int goalsScored;
    int goalsConceded;

    public static int POSITION = 1;

    public Team(String name) {
        this.name = name;
        this.matchesPlayed = 0;
        this.matchesWon = 0;
        this.matchesDrawn = 0;
        this.matchesLost = 0;
        this.totalPoints = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
    }

    public void updateStats(int homeGoals, int awayGoals) {
        matchesPlayed++;
        goalsScored += homeGoals;
        goalsConceded += awayGoals;
        if (homeGoals > awayGoals) {
            matchesWon++;
            totalPoints += 3;
        } else if (homeGoals < awayGoals) {
            matchesLost++;
        } else {
            matchesDrawn++;
            totalPoints += 1;
        }
    }

    public int goalDifference() {
        return goalsScored - goalsConceded;
    }
    public String getName() {
        return name;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public int getMatchesDrawn() {
        return matchesDrawn;
    }

    public int getMatchesLost() {
        return matchesLost;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    @Override
    public String toString() {
        return String.format("%2d. %-15s%5s%5s%5s%5s%5s", POSITION++,name, matchesPlayed, matchesWon, matchesDrawn, matchesLost, totalPoints);
    }
}

class FootballTable{
    Map<String, Team> teams;

    public FootballTable() {
        teams = new TreeMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        teams.putIfAbsent(homeTeam, new Team(homeTeam));
        teams.putIfAbsent(awayTeam, new Team(awayTeam));

        Team home = teams.get(homeTeam);
        Team away = teams.get(awayTeam);

        home.updateStats(homeGoals, awayGoals);
        away.updateStats(awayGoals, homeGoals);
    }

    public void printTable() {
        List<Team> sortedTeams = teams.values().stream()
                .sorted(Comparator.comparing(Team::getTotalPoints)
                        .thenComparing(Team::goalDifference).reversed()
                        .thenComparing(Team::getName))
                .collect(Collectors.toList());

        for (Team team : sortedTeams) {
            System.out.println(team.toString());
        }
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}
