package uni.bachelors.travelagency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.bachelors.travelagency.model.CreateHolidayDTO;
import uni.bachelors.travelagency.model.ResponseHolidayDTO;
import uni.bachelors.travelagency.model.ResponseLocationDTO;
import uni.bachelors.travelagency.model.UpdateHolidayDTO;
import uni.bachelors.travelagency.repository.HolidayRepository;
import uni.bachelors.travelagency.repository.LocationRepository;

import java.util.*;

@RestController
@RequestMapping("/holidays")
public class HolidayController {

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<List<ResponseHolidayDTO>> getAllHolidays() {
        try {
            List<ResponseHolidayDTO> holidays = new ArrayList<>();
            holidayRepository.findAll().forEach(holidays::add);

            if (holidays.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(holidays, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @RequestMapping("/{holidayId}")
    public ResponseEntity<ResponseHolidayDTO> getHolidayById(@PathVariable Long holidayId) {
        Optional<ResponseHolidayDTO> holiday = holidayRepository.findById(holidayId);

        if (holiday.isPresent())
            return new ResponseEntity<>(holiday.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ResponseHolidayDTO> postHoliday(@RequestBody CreateHolidayDTO createHolidayDTO) {
        Optional<ResponseLocationDTO> location = locationRepository.findById(createHolidayDTO.getLocation());

        if (location.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ResponseHolidayDTO holiday = new ResponseHolidayDTO();
        holiday.setLocation(location.get());
        holiday.setTitle(createHolidayDTO.getTitle());
        holiday.setStartDate(createHolidayDTO.getStartDate());
        holiday.setDuration(createHolidayDTO.getDuration());

        Double price = 0d;
        try {
            price = Double.parseDouble(createHolidayDTO.getPrice());
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        holiday.setPrice(price);
        holiday.setFreeSlots(createHolidayDTO.getFreeSlots());

        holiday = holidayRepository.save(holiday);

        return new ResponseEntity<>(holiday, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseHolidayDTO> putHoliday(@RequestBody UpdateHolidayDTO updateHolidayDTO) {
        Optional<ResponseHolidayDTO> existingHoliday = holidayRepository.findById(updateHolidayDTO.getId());

        if (existingHoliday.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ResponseHolidayDTO updatedHoliday = existingHoliday.get();

        //Edit existing data ##
        Long existingLocationId = existingHoliday.get().getLocation().getId();
        if (!existingLocationId.equals(updateHolidayDTO.getLocation())) {
            Optional<ResponseLocationDTO> newLocation = locationRepository.findById(updateHolidayDTO.getLocation());

            if (newLocation.isEmpty())
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            updatedHoliday.setLocation(newLocation.get());
        }

        updatedHoliday.setTitle(updateHolidayDTO.getTitle());
        updatedHoliday.setStartDate(updateHolidayDTO.getStartDate());
        updatedHoliday.setDuration(updateHolidayDTO.getDuration());

        try {
            Double price = Double.parseDouble(updateHolidayDTO.getPrice());
            updatedHoliday.setPrice(price);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        updatedHoliday.setFreeSlots(updateHolidayDTO.getFreeSlots());
        //##

        ResponseHolidayDTO responseHolidayDTO = holidayRepository.save(updatedHoliday);

        return new ResponseEntity<>(responseHolidayDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{holidayId}")
    public ResponseEntity<Boolean> deleteHoliday(@PathVariable Long holidayId) {
        try {
            holidayRepository.deleteById(holidayId);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
