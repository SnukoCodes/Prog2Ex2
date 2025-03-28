package at.ac.fhcampuswien.fhmdb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import java.util.List;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 890, 620);
        scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm());
        stage.setTitle("FHMDb!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // to prevent the app of crashing in case of ioerror
        try {

            // get movies from the api
            List<Movie> movies = MovieAPI.getMovies(null, null, null, null);
            HomeController controller = new HomeController();

            // [Placeholder] show functionality of getMostPopularActor
            System.out.println("Most popular actor: " + controller.getMostPopularActor(movies));

            // [Placeholder] show functionality of getLongestMovieTitle
            System.out.println("Longest title length: " + controller.getLongestMovieTitle(movies));

            // [Placeholder] show functionality of get countMoviesFrom
            System.out.println("Nolan movies: " + controller.countMoviesFrom(movies, "Christopher Nolan"));

            // [Placeholder] show functionality of getMoviesBetweenYears
            System.out.println("2000-2010 movies: " + controller.getMoviesBetweenYears(movies, 2000, 2010).size());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // launch the app
        launch();


    }
}