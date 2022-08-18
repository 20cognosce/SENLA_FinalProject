package com.senla.utils.geolocation;

import com.senla.utils.constants.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;


//using HERE location platform (Google Maps analog)
public class Geocoder {

    private static final String GEOCODING_RESOURCE = Constants.GEOCODING_RESOURCE;
    public static final String REVERSE_GEOCODING_RESOURCE = Constants.REVERSE_GEOCODING_RESOURCE;
    private static final String API_KEY = Constants.API_KEY;

    public static String getGeocodeResponseFromQuery(String query) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        String requestUri = GEOCODING_RESOURCE
                + "?apiKey=" + API_KEY
                + "&q=" + encodedQuery
                + "&lang=ru-RUS";

        HttpRequest geocodingRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000))
                .build();

        HttpResponse<String> geocodingResponse = httpClient.send(geocodingRequest, HttpResponse.BodyHandlers.ofString());
        return geocodingResponse.body();
    }

    public static String getGeocodeResponseFromCoordinates(Double latitude, Double longitude) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        String requestUri = REVERSE_GEOCODING_RESOURCE
                + "?apiKey=" + API_KEY
                + "&at=" + latitude + "," + longitude
                + "&lang=ru-RUS";

        HttpRequest geocodingRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000))
                .build();

        HttpResponse<String> geocodingResponse = httpClient.send(geocodingRequest, HttpResponse.BodyHandlers.ofString());
        return geocodingResponse.body();
    }
}
