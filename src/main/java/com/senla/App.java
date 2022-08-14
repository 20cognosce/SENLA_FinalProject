package com.senla;


import com.senla.config.AppConfig;
import com.senla.dao.impl.RideDaoImpl;
import com.senla.dao.impl.UserDaoImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Arrays;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        /*ObjectMapper mapper = new ObjectMapper();

        String response = Geocoder.getGeocodeResponseFromCoordinates(55.72791,37.64483);
        //String response = Geocoder.getGeocodeResponseFromQuery("Москва, метро Студенческая");
        JsonNode responseJsonNode = mapper.readTree(response);

        JsonNode items = responseJsonNode.get("items");

        System.out.println(items);*/

        ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println(Arrays.toString(ctx.getBeanDefinitionNames()));
        System.out.println(ctx.getBean(UserDaoImpl.class).getEntityManager());
        System.out.println(ctx.getBean(RideDaoImpl.class).getEntityManager());
        ctx.close();
    }
}
