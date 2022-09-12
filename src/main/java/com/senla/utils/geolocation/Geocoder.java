package com.senla.utils.geolocation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.model.entity.Geolocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

//using HERE location platform (Google Maps analog)
@Slf4j
@Component
public class Geocoder {

    @Value("${geocoding.resource}")
    private String GEOCODING_RESOURCE;
    @Value("${reverse.geocoding.resource}")
    public String REVERSE_GEOCODING_RESOURCE;
    @Value("${api.key}")
    private String API_KEY;

    public Geolocation getGeolocationFromCoordinates(Double latitude, Double longitude) {
        try {
            String geocode = getGeocodeResponseFromCoordinates(latitude, longitude);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseJsonNode = mapper.readTree(geocode);
            JsonNode items = responseJsonNode.get("items");

            JsonNode address = items.get(0).get("address");
            JsonNode position = items.get(0).get("position");

            String label = Objects.isNull(address.get("label")) ? "" : address.get("label").asText();
            String countryCode = Objects.isNull(address.get("countryCode")) ? "" : address.get("countryCode").asText();
            String countryName = Objects.isNull(address.get("countryName")) ? "" : address.get("countryName").asText();
            String county = Objects.isNull(address.get("county")) ? "" : address.get("county").asText();
            String city = Objects.isNull(address.get("city")) ? "" : address.get("city").asText();
            String district = Objects.isNull(address.get("district")) ? "" : address.get("district").asText();
            String street = Objects.isNull(address.get("street")) ? "" : address.get("street").asText();
            String houseNumber = Objects.isNull(address.get("houseNumber")) ? "" : address.get("houseNumber").asText();

            return Geolocation.builder()
                    .latitude(position.get("lat").asDouble())
                    .longitude(position.get("lng").asDouble())
                    .countryCode(countryCode)
                    .countryName(countryName)
                    .county(county)
                    .city(city)
                    .district(district)
                    .street(street)
                    .houseNumber(houseNumber)
                    .description(label)
                    .build();
        } catch (IOException | InterruptedException e) {
            log.error("Не удалось получить геолокацию по запросу", e);
            throw new RuntimeException(e);
        }
    }

    private String getGeocodeResponseFromQuery(String query) throws IOException, InterruptedException {
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

    private String getGeocodeResponseFromCoordinates(Double latitude, Double longitude) throws IOException, InterruptedException {
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
