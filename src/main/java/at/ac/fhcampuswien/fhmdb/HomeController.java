package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingComboBox;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeState();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initializeLayout();
    }

    public void initializeState() throws IOException {
        allMovies = Movie.initializeMovies();
        observableMovies.clear();
        observableMovies.addAll(allMovies); // add all movies to the observable list
        sortedState = SortedState.NONE;
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell()); // apply custom cells to the listview

        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No Filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");

        releaseYearComboBox.setPromptText("Filter by Release Year");
        releaseYearComboBox.getItems().add("No Filter");
        releaseYearComboBox.getItems().addAll(observableMovies.stream()
                .map(Movie::getReleaseYear)
                .distinct()
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new)));
        ratingComboBox.setPromptText("Minimum Rating");
        ratingComboBox.getItems().add("No Filter");
        // Create an ObservableList with ratings from 1 to 10
        ObservableList<Integer> possibleRatings = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // Add all items to the ComboBox
        ratingComboBox.getItems().addAll(possibleRatings);
    }

    public void sortMovies() {
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }
    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }
    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    public void searchBtnClicked(ActionEvent actionEvent) throws IOException {
        String searchQuery = searchField.getText().trim().toLowerCase();

        // Get selected values safely
        String genre = getSelectedValue(genreComboBox);
        String minRating = getSelectedValue(ratingComboBox);
        String releaseYear = getSelectedValue(releaseYearComboBox);

        // Convert values while handling "No filter" case
        String genreResult = genre.equalsIgnoreCase("no filter") ? null : genre;
        Integer minRatingResult = minRating.equalsIgnoreCase("no filter") ? null : Integer.parseInt(minRating);
        Integer releaseYearResult = releaseYear.equalsIgnoreCase("no filter") ? null : Integer.parseInt(releaseYear);

        // Fetch movies and sort
        List<Movie> filteredMovies = MovieAPI.getMovies(searchQuery, genreResult, releaseYearResult, minRatingResult);
        observableMovies.clear();
        observableMovies.addAll(filteredMovies); // add all movies to the observable list
        sortedState = SortedState.NONE;
        sortMovies(sortedState);
    }

    /**
     * Helper method to get selected value from ComboBox safely
     */
    private String getSelectedValue(ComboBox<?> comboBox) {
        Object selected = comboBox.getSelectionModel().getSelectedItem();
        return (selected != null) ? selected.toString() : "No filter";
    }

    
    public String getMostPopularActor(List<Movie> movies) { 
        if (movies == null || movies.isEmpty()) {
            return "";
        }
        
        return movies.stream()
                // 1. collect actors from all movies
                .flatMap(movie -> Arrays.stream(movie.getMainCast()))
                // 2. group actors and count how often each appears
                .collect(Collectors.groupingBy(
                        actor -> actor,
                        Collectors.counting()
                ))
                // 3. find actor with the highest count
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                // 4. return actors name - or empty string if none found
                .map(Map.Entry::getKey)
                .orElse("");
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return 0;
        }
        
        return movies.stream()
                // 1: extract all movie titles
                .map(Movie::getTitle)
                // 2: map each title to its length
                .mapToInt(String::length)
                // 3: find the max length
                .max()
                .orElse(0);
    }

}