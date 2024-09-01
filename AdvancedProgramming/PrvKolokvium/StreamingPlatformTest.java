package PrvKolokvium;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

interface Rateable {
    double finalRating();

    List<String> getGenres();
}

class Movie implements Rateable, Comparable<Movie> {
    private final String name;
    private final List<String> genres;
    private final List<Double> ratings;

    public Movie(String name, List<String> genres, List<Double> ratings) {
        this.name = name;
        this.genres = genres;
        this.ratings = ratings;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<String> getGenres() {
        return genres;
    }

    @Override
    public double finalRating() {
        double averageRating = ratings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        return averageRating * Math.min(ratings.size() / 20.0, 1.0);
    }

    @Override
    public String toString() {
        return String.format("Movie %s %.4f", getName(), finalRating());
    }

    @Override
    public int compareTo(Movie o) {
        return Double.compare(o.finalRating(), this.finalRating());
    }
}

class Episode implements Comparable<Episode> {
    private final List<Double> episodeRatings;

    public Episode(List<Double> episodeRatings) {
        this.episodeRatings = new ArrayList<>(episodeRatings);
    }

    public double finalRating() {
        double averageRating = episodeRatings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        return averageRating * Math.min(episodeRatings.size() / 20.0, 1.0);
    }

    @Override
    public int compareTo(Episode o) {
        return Double.compare(this.finalRating(), o.finalRating());
    }
}

class Series implements Rateable, Comparable<Series> {
    private final String name;
    private final List<String> genres;
    private final List<Episode> episodes;

    public Series(String name, List<String> genres, List<Episode> episodeRatings) {
        this.name = name;
        this.genres = genres;
        this.episodes = episodeRatings;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<String> getGenres() {
        return genres;
    }

    @Override
    public double finalRating() {
        List<Double> allRatings = episodes.stream()
                .map(Episode::finalRating)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        List<Double> top3Ratings = allRatings.stream()
                .limit(3)
                .collect(Collectors.toList());

        return top3Ratings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("TV Show %s %.4f (%d episodes)", getName(), finalRating(), episodes.size());
    }

    @Override
    public int compareTo(Series o) {
        return Double.compare(o.finalRating(), this.finalRating());
    }
}

class StreamingPlatform {
    List<Movie> moviesList;
    List<Series> seriesList;

    public StreamingPlatform() {
        moviesList = new ArrayList<>();
        seriesList = new ArrayList<>();
    }

    public void addItem(String data) {
        String[] parts = data.split(";");
        String name = parts[0];
        List<String> genres = Arrays.asList(parts[1].split(","));
        if (parts.length == 3) {
            List<Double> ratings = Arrays.stream(parts[2].split("\\s+"))
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
            Movie movie = new Movie(name, genres, ratings);
            moviesList.add(movie);
        } else {
            List<Episode> episodes = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                String episodeData = parts[i];
                String[] episodeParts = episodeData.split("\\s+", 2);
                if (episodeParts.length == 2) {
                    List<Double> episodeRatings = Arrays.stream(episodeParts[1].split("\\s+"))
                            .map(Double::parseDouble)
                            .collect(Collectors.toList());
                    episodes.add(new Episode(episodeRatings));
                }
            }
            Series series = new Series(name, genres, episodes);
            seriesList.add(series);
        }
    }

    public void listAllItems(PrintStream out) {
        List<Rateable> allItems = new ArrayList<>();
        allItems.addAll(moviesList);
        allItems.addAll(seriesList);

        allItems.stream()
                .sorted((r1, r2) -> Double.compare(r2.finalRating(), r1.finalRating()))
                .forEach(out::println);
    }

    public void listFromGenre(String data, PrintStream out) {
        List<Rateable> allItems = new ArrayList<>();
        allItems.addAll(moviesList.stream()
                .filter(movie -> movie.getGenres().contains(data))
                .collect(Collectors.toList()));
        allItems.addAll(seriesList.stream()
                .filter(series -> series.getGenres().contains(data))
                .collect(Collectors.toList()));

        allItems.stream()
                .sorted((r1, r2) -> Double.compare(r2.finalRating(), r1.finalRating()))
                .forEach(out::println);
    }
}

public class StreamingPlatformTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StreamingPlatform sp = new StreamingPlatform();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            String method = parts[0];
            String data = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
            if (method.equals("addItem")) {
                sp.addItem(data);
            } else if (method.equals("listAllItems")) {
                sp.listAllItems(System.out);
            } else if (method.equals("listFromGenre")) {
                System.out.println(data);
                sp.listFromGenre(data, System.out);
            }
        }

    }
}