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
    public List<Movie> getAllMovies() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .header("User-Agent", "http.agent")
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseBody = response.body().string();
        return parseMovies(responseBody);
    }

    // Methode: Filme mit Filter abrufen
    public List<Movie> searchMovies(String query, String genre) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        if (query != null && !query.isEmpty()) {
            urlBuilder.addQueryParameter("query", query);
        }
        if (genre != null && !genre.isEmpty()) {
            urlBuilder.addQueryParameter("genre", genre);
        }

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "http.agent")
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseBody = response.body().string();
        return parseMovies(responseBody);
    }

    // JSON in Movie-Objekte umwandeln
    private List<Movie> parseMovies(String json) {
        Gson gson = new Gson();
        Type movieListType = new TypeToken<List<Movie>>() {}.getType();
        return gson.fromJson(json, movieListType);
    }
}
