package VtorKolokvium.StreamingPlatform;

import java.util.*;
import java.util.stream.Collectors;

class CosineSimilarityCalculator {

    public static double cosineSimilarity(Map<String, Integer> c1, Map<String, Integer> c2) {
        return cosineSimilarity(c1.values(), c2.values());
    }

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}

class Movie implements Comparable<Movie> {
    private final String id;
    private final String title;
    private final float rating;

    public Movie(String id, String title) {
        this.id = id;
        this.title = title;
        this.rating = 0;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return String.format("Movie ID: %s Title: %s Rating: %.2f", getId(), getTitle(), getRating());
    }

    @Override
    public int compareTo(Movie o) {
        return Float.compare(o.getRating(), this.getRating());
    }
}

class User {
    private final String id;
    private final String name;
    private final Map<String, Double> userRating;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        userRating = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("User ID: %s Name: %s", getId(), getName());
    }
}

class StreamingPlatform {
    private final List<Movie> moviesList;
    private final List<User> usersList;
    private final Map<String, Map<String, Integer>> ratings;

    public StreamingPlatform() {
        moviesList = new ArrayList<>();
        usersList = new ArrayList<>();
        ratings = new HashMap<>();
    }

    public double getAverageRating(String movieId) {
        if (ratings.isEmpty() || !ratings.containsKey(movieId)) {
            return 0.0;
        }

        return ratings.get(movieId).values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    public void addMovie(String id, String name) {
        moviesList.add(new Movie(id, name));
    }

    public void addUser(String id, String name) {
        usersList.add(new User(id, name));
    }

    public void addRating(String userId, String movieId, int rating) {
        ratings.putIfAbsent(movieId, new HashMap<>());
        ratings.get(movieId).put(userId, rating);
    }

    public void topNMovies(int n) {
        moviesList.stream()
                .sorted((m1, m2) -> Double.compare(getAverageRating(m2.getId()), getAverageRating(m1.getId())))
                .limit(n)
                .forEach(movie -> {
                    System.out.printf("Movie ID: %s Title: %s Rating: %.2f\n", movie.getId(), movie.getTitle(), getAverageRating(movie.getId()));
                });
    }

    public void favouriteMoviesForUsers(List<String> users) {
        for (String userId : users) {
            Map<String, Integer> userRatings = ratings.entrySet().stream()
                    .filter(stringMapEntry -> stringMapEntry.getValue().containsKey(userId))
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(userId)));
            if (userRatings.isEmpty()) {
                continue;
            }
            double maxRating = userRatings.values().stream().max(Integer::compareTo).orElse(0);

            User user = usersList.stream().filter(user1 -> user1.getId().equals(userId)).findFirst().orElse(null);
            if (user == null) {
                continue;
            }
            String userName = user.getName();
            List<String> favoriteMovies = userRatings.entrySet().stream()
                    .filter(stringIntegerEntry -> stringIntegerEntry.getValue() == maxRating)
                    .map(Map.Entry::getKey)
                    .sorted()
                    .collect(Collectors.toList());
            System.out.printf("User ID: %s Name: %s\n", userId, userName);
            favoriteMovies.stream().map(movieID -> {
                        String movieTitle = moviesList.stream()
                                .filter(movie -> movie.getId().equals(movieID))
                                .map(Movie::getTitle)
                                .findFirst()
                                .orElse(null);
                        return String.format("Movie ID: %s Title: %s Rating: %.2f", movieID, movieTitle, getAverageRating(movieID));
                    }).sorted((m1, m2) -> Double.compare(Double.parseDouble(m2.split(" Rating: ")[1]), Double.parseDouble(m1.split(" Rating: ")[1])))
                    .forEach(System.out::println);
            System.out.println();
        }
    }

    public void similarUsers(String userId) {
        
    }
}

public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id, name);
            } else if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id, name);
            } else if (parts[0].equals("addRating")) {
                //String userId, String movieId, int rating
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")) {
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + users.stream().collect(Collectors.joining(", ")));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}