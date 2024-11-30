package com.Pune.Bhatkanti.Controller;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/maps")
public class MapController {

    @Value("${google.api.key}")
    private String googleApiKey;

    @GetMapping("/route")
    public String getRoute(@RequestParam String origin, @RequestParam String destination) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin + "&destination=" + destination + "&key=" + googleApiKey;

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }
}
