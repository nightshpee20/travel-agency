package uni.bachelors.travelagency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.bachelors.travelagency.model.CreateLocationDTO;
import uni.bachelors.travelagency.model.ResponseHolidayDTO;
import uni.bachelors.travelagency.model.ResponseLocationDTO;
import uni.bachelors.travelagency.model.UpdateLocationDTO;
import uni.bachelors.travelagency.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<List<ResponseLocationDTO>> getAllLocations() {
        try {
            List<ResponseLocationDTO> locations = new ArrayList<>();
            locationRepository.findAll().forEach(locations::add);

            if (locations.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(locations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @RequestMapping("/{locationId}")
    public ResponseEntity<ResponseLocationDTO> getLocationById(@PathVariable Long locationId) {
        Optional<ResponseLocationDTO> location = locationRepository.findById(locationId);

        if (location.isPresent())
            return new ResponseEntity<>(location.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ResponseLocationDTO> postLocation(@RequestBody CreateLocationDTO createLocationDTO) {
        ResponseLocationDTO location = new ResponseLocationDTO();

        location.setStreet(createLocationDTO.getStreet());
        location.setNumber(createLocationDTO.getNumber());
        location.setCity(createLocationDTO.getCity());
        location.setCountry(createLocationDTO.getCountry());
        location.setImageUrl(createLocationDTO.getImageUrl());

        location = locationRepository.save(location);

        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseLocationDTO> putLocation(@RequestBody UpdateLocationDTO updateLocationDTO) {
        Optional<ResponseLocationDTO> existingLocation = locationRepository.findById(updateLocationDTO.getId());

        if (existingLocation.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ResponseLocationDTO updatedLocation = existingLocation.get();

        updatedLocation.setStreet(updateLocationDTO.getStreet());
        updatedLocation.setNumber(updateLocationDTO.getNumber());
        updatedLocation.setCity(updateLocationDTO.getCity());
        updatedLocation.setCountry(updateLocationDTO.getCountry());
        updatedLocation.setImageUrl(updateLocationDTO.getImageUrl());

        ResponseLocationDTO responseLocationDTO = locationRepository.save(updatedLocation);

        return new ResponseEntity<>(responseLocationDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Boolean> deleteLocation(@PathVariable Long locationId) {
        try {
            locationRepository.deleteById(locationId);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
