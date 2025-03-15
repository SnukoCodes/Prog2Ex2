package at.ac.fhcampuswien.fhmdb.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    public String id;
    public String title;
    public Genre[] genres;
    public int releaseYear;
    public String description;
    public String imgUrl;
    public int lengthInMinutes;
    public String [] directors;
    public String [] writers;
    public String [] mainCast;
    public double rating;



    public Movie(String id, String title,Genre[] genres,int releaseYear, String description, String imgUrl, int lengthInMinutes, String[] directors, String[] writers, String [] mainCast, double rating) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.description = description;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating =  rating;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Movie other)) {
            return false;
        }
        return this.title.equals(other.title) && this.description.equals(other.description) && this.genres.equals(other.genres);
    }

    public String getId() {return id;}

    public String getTitle() {
        return title;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public Integer getReleaseYear() {return releaseYear;}

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {return imgUrl;}

    public Integer getLengthInMinutes() {return lengthInMinutes;}

    public String[] getDirectors() {return directors;}

    public String[] getWriters() {return writers;}

    public String[] getMainCast() {return mainCast;}

    public Number getRating() {return rating;}

    public static List<Movie> initializeMovies() throws IOException {
        List<Movie> movies = new ArrayList<>();
//        movies = MovieAPI.getMovies();

        return movies;
    }
}
