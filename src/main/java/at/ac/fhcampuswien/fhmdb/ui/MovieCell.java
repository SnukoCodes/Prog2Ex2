package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label year = new Label();
    private final Label rating = new Label();
    private final VBox layout = new VBox(title, detail, genre, year, rating);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            // Apply styling class
            this.getStyleClass().add("movie-cell");

            // Set title
            title.setText(movie.getTitle());
            title.getStyleClass().add("text-yellow");
            title.setFont(title.getFont().font(20));

            // Set description
            detail.setText(movie.getDescription() != null ? movie.getDescription() : "No description available");
            detail.getStyleClass().add("text-white");
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);

            // Set genres
            String genres = Arrays.stream(movie.getGenres())
                    .map(Enum::toString)
                    .collect(Collectors.joining(", "));
            genre.setText(genres);
            genre.getStyleClass().add("text-white");
            genre.setStyle("-fx-font-style: italic");

            // Set year and rating
            year.setText("Year: " + movie.getReleaseYear());
            rating.setText("Rating: " + movie.getRating());

            year.getStyleClass().add("text-white");
            rating.getStyleClass().add("text-white");

            // Layout settings
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
            layout.setPadding(new Insets(10));
            layout.setSpacing(10);
            layout.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            // Organizing elements in a VBox
            VBox infoBox = new VBox(title, detail, genre, year, rating);
            infoBox.setSpacing(5);

            layout.getChildren().setAll(infoBox); // Update layout with all elements
            setGraphic(layout);
        }
    }
}

