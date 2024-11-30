package com.Pune.Bhatkanti.Service;


import com.Pune.Bhatkanti.model.Place;
import com.Pune.Bhatkanti.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {
    @Autowired
    private PlaceRepository placeRepository;

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Place savePlace(Place place) {
        return placeRepository.save(place);
    }

    public Place getPlaceById(Long id) {
        return placeRepository.findById(id).orElse(null);
    }

    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }
}
