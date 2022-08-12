package com.senla;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.utils.geolocation.Geocoder;

import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();

        //String response = Geocoder.getGeocodeResponseFromCoordinates(-179.0,-90.0);
        String response = Geocoder.getGeocodeResponseFromQuery("Москва, метро Студенческая");
        JsonNode responseJsonNode = mapper.readTree(response);

        JsonNode items = responseJsonNode.get("items");

        System.out.println(items);
    }
}
