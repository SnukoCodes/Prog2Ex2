package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MovieAPI {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private static final OkHttpClient client = new OkHttpClient();

    // Methode: Alle Filme abrufen
    public static List<Movie> getMovies(String query, String genre, Integer releaseYear, Integer ratingFrom) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        // Add query parameters only if they are provided (not null)
        if (query != null && !query.isEmpty()) {
            urlBuilder.addQueryParameter("query", query);
        }
        if (genre != null && !genre.isEmpty()) {
            urlBuilder.addQueryParameter("genre", genre);
        }
        if (releaseYear != null) {
            urlBuilder.addQueryParameter("releaseYear", String.valueOf(releaseYear));
        }
        if (ratingFrom != null) {
            urlBuilder.addQueryParameter("ratingFrom", String.valueOf(ratingFrom));
        }

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            return parseMovies(responseBody);
        }
    }



    // JSON in Movie-Objekte umwandeln
    private static List<Movie> parseMovies(String json) {
        Gson gson = new Gson();
        Type movieListType = new TypeToken<List<Movie>>() {}.getType();
        return gson.fromJson(json, movieListType);
    }
}
