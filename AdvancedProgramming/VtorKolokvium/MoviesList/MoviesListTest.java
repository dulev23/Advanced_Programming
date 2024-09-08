//package VtorKolokvium.MoviesList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Movie implements Comparable<Movie> {
    private String title;
    private List<Integer> ratingsList;

    public Movie(String title, int[] ratings) {
        this.title = title;
        ratingsList = new ArrayList<>();
        for (int rating : ratings) {
            this.ratingsList.add(rating);
        }
    }

    public String getTitle() {
        return title;
    }

    public int getTotalRatings(){
        return ratingsList.size();
    }

    public double getAverageRating() {
        return ratingsList.stream()
                .mapToDouble(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", getTitle(),getAverageRating(),getTotalRatings());
    }

    @Override
    public int compareTo(Movie o) {
        return Double.compare(this.getAverageRating(), o.getAverageRating());
    }
}

class MoviesList {
    private final List<Movie> moviesList;
    private final List<Integer> ratingsList;

    public MoviesList() {
        this.moviesList = new ArrayList<>();
        this.ratingsList = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        moviesList.add(new Movie(title, ratings));
    }

    public int getMaxRating(){
        return moviesList.stream()
                .mapToInt(Movie::getTotalRatings)
                .max()
                .orElse(1);
    }
    public List<Movie> top10ByAvgRating() {
        return moviesList.stream()
                .sorted(Comparator.comparing(Movie::getAverageRating).reversed().thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }

    public int totalMoviesRating(){
        return moviesList.stream()
                .mapToInt(Movie::getTotalRatings)
                .sum();
    }

    public List<Movie> top10ByRatingCoef(){
        int totalRating = getMaxRating();
        return moviesList.stream()
                .sorted((m1,m2) -> {
                    double coef1 = m1.getAverageRating() * m1.getTotalRatings() / (double) totalRating;
                    double coef2 = m2.getAverageRating() * m2.getTotalRatings() / (double) totalRating;
                    return Double.compare(coef2,coef1);
                })
                .limit(10)
                .collect(Collectors.toList());
    }
}

public class MoviesListTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}