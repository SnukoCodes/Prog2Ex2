package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private static HomeController homeController;

    @BeforeAll
    static void init() {
        homeController = new HomeController();
    }

    @BeforeEach
    void beforeEach() {
        try {
            homeController.initializeState();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void at_initialization_allMovies_and_observableMovies_should_be_filled_and_equal() {
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void if_not_yet_sorted_sort_is_applied_in_ascending_order() {
        // given
        homeController.sortedState = SortedState.NONE;

        // Manually sort movies
        List<Movie> sortedMovies = homeController.observableMovies.stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .toList();

        // when
        homeController.sortMovies();

        assertEquals(sortedMovies, homeController.observableMovies);
    }

    @Test
    void if_last_sort_ascending_next_sort_should_be_descending() {
        // given
        homeController.sortedState = SortedState.ASCENDING;

        // Manually sort movies
        List<Movie> sortedMovies = homeController.observableMovies.stream()
                .sorted(Comparator.comparing(Movie::getTitle).reversed())
                .toList();

        // when
        homeController.sortMovies();

        assertEquals(sortedMovies, homeController.observableMovies);
    }

    @Test
    void if_last_sort_descending_next_sort_should_be_ascending() {
        // given
        homeController.sortedState = SortedState.DESCENDING;

        // Manually sort movies
        List<Movie> sortedMovies = homeController.observableMovies.stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .toList();

        // when
        homeController.sortMovies();

        assertEquals(sortedMovies, homeController.observableMovies);
    }

    @Test
    void query_filter_matches_with_lower_and_uppercase_letters() {
        // given
        String query = "IfE";

        // when
        try {
            // get movies with filter
            List<Movie> actual = homeController.filterByQuery(query);
            Set<String> movieNames = actual.stream()
                    // Cast the movie objects to their corresponding title
                    .map(Movie::getTitle)
                    // Check if any of the expected elements is within the list
                    .filter(element -> element.equals("Life Is Beautiful") || element.equals("The Wolf of Wall Street"))
                    // Convert the list to a set
                    .collect(Collectors.toSet());
            assertTrue(movieNames.contains("Life Is Beautiful") || movieNames.contains("The Wolf of Wall Street"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void query_filter_with_null_value_returns_unfiltered_list() {
        try {
            // Get movies with null - query and cast as String set by movie title...
            Set<String> filterMovies = getSetOfMovieTitles(homeController.filterByQuery(null));
            // ... same for the movies to be expected
            Set<String> expectedMovies = getSetOfMovieTitles(homeController.observableMovies);
            assertEquals(expectedMovies, filterMovies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void genre_filter_with_null_value_returns_unfiltered_list() {
        try {
            // Get movies with null - genre and cast as String set by movie title...
            Set<String> filterMovies = getSetOfMovieTitles(homeController.filterByGenre(null));
        } catch (IOException e) {
            assertTrue(e.getClass().toString().contains("java.io.IOException"));
        }
    }

    @Test
    void genre_filter_returns_all_movies_containing_given_genre() {
        try {
            // Get movies with drama genre and cast as String set by movie title...
            Set<String> filterMovies = getSetOfMovieTitles(homeController.filterByGenre(Genre.DRAMA));
            // ... same for the movies to be expected
            Set<String> expectedMovies = homeController.observableMovies.stream()
                            .filter(element -> List.of(element.getGenres()).contains(Genre.DRAMA))
                            .map(Movie::getTitle)
                            .collect(Collectors.toSet());
            assertEquals(expectedMovies, filterMovies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get_longest_movie_title(){
        try {
            List<Movie> movies = homeController.filterByQuery(null);
            assertEquals(homeController.getLongestMovieTitle(movies),46);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get_movies_from_director(){
        try {
            List<Movie> movies = homeController.filterByQuery(null);
            assertEquals(homeController.countMoviesFrom(movies,"Christopher Nolan"),2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<String> getSetOfMovieTitles(List<Movie> filterMovies) {
        return filterMovies.stream()
                // Get movie titles
                .map(Movie::getTitle)
                // cast as set
                .collect(Collectors.toSet());
    }
}