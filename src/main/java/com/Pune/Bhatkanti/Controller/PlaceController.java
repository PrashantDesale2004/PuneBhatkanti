package com.Pune.Bhatkanti.Controller;


import com.Pune.Bhatkanti.model.Place;
import com.Pune.Bhatkanti.Service.PlaceService;
import com.Pune.Bhatkanti.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/places")
public class PlaceController {
    @Value("${google.api.key}")
    private String googleApiKey;

    private final PlaceRepository placeRepository;

    public PlaceController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPlace(@RequestParam String placeName) {
        try {
            // Construct the Google Maps Places API URL
            String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="
                    + placeName + "&inputtype=textquery&fields=formatted_address,geometry&key=" + googleApiKey;

            // Fetch details using RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response);
            if (!jsonResponse.getString("status").equals("OK")) {
                throw new RuntimeException("Failed to fetch place details: " + jsonResponse.getString("status"));
            }

            JSONObject candidate = jsonResponse.getJSONArray("candidates").getJSONObject(0);
            String formattedAddress = candidate.getString("formatted_address");
            JSONObject location = candidate.getJSONObject("geometry").getJSONObject("location");
            double latitude = location.getDouble("lat");
            double longitude = location.getDouble("lng");

            // Create and save the Place entity
            Place place = new Place();
            place.setName(placeName);
            place.setAddress(formattedAddress);
            place.setLatitude(latitude);
            place.setLongitude(longitude);

            placeRepository.save(place);

            return ResponseEntity.ok(place);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding place: " + e.getMessage());
        }
    }
    @Autowired
    private PlaceService placeService;

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @PostMapping
    public Place addPlace(@RequestBody Place place) {
        return placeService.savePlace(place);
    }

    @GetMapping("/{id}")
    public Place getPlace(@PathVariable Long id) {
        return placeService.getPlaceById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
    }
}
