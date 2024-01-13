package uni.bachelors.travelagency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.bachelors.travelagency.model.*;
import uni.bachelors.travelagency.repository.HolidayRepository;
import uni.bachelors.travelagency.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    @GetMapping
    public ResponseEntity<List<ResponseReservationDTO>> getAllReservations() {
        try {
            List<ResponseReservationDTO> reservations = new ArrayList<>();
            reservationRepository.findAll().forEach(reservations::add);

            if (reservations.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @RequestMapping("/{reservationId}")
    public ResponseEntity<ResponseReservationDTO> getReservationById(@PathVariable Long reservationId) {
        Optional<ResponseReservationDTO> reservation = reservationRepository.findById(reservationId);

        if (reservation.isPresent())
            return new ResponseEntity<>(reservation.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ResponseReservationDTO> postReservation(@RequestBody CreateReservationDTO createReservationDTO) {
        Optional<ResponseHolidayDTO> holiday = holidayRepository.findById(createReservationDTO.getHoliday());

        if (holiday.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ResponseHolidayDTO holidayObj = holiday.get();
        if (holidayObj.getFreeSlots() == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        holidayObj.setFreeSlots(holidayObj.getFreeSlots() - 1);
        holidayObj = holidayRepository.save(holidayObj);

        ResponseReservationDTO reservation = new ResponseReservationDTO();
        reservation.setContactName(createReservationDTO.getContactName());
        reservation.setPhoneNumber(createReservationDTO.getPhoneNumber());
        reservation.setHoliday(holidayObj);

        reservation = reservationRepository.save(reservation);

        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseReservationDTO> putReservation(@RequestBody UpdateReservationDTO updateReservationDTO) {
        Optional<ResponseReservationDTO> existingReservation = reservationRepository.findById(updateReservationDTO.getId());

        if (existingReservation.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ResponseReservationDTO updatedReservation = existingReservation.get();

        updatedReservation.setContactName(updateReservationDTO.getContactName());
        updatedReservation.setPhoneNumber(updateReservationDTO.getPhoneNumber());

        Long existingHolidayId = existingReservation.get().getHoliday().getId();
        if (!existingHolidayId.equals(updateReservationDTO.getHoliday())) {
            Optional<ResponseHolidayDTO> newHoliday = holidayRepository.findById(updateReservationDTO.getHoliday());

            if (newHoliday.isEmpty())
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            updatedReservation.setHoliday(newHoliday.get());
        }

        ResponseReservationDTO responseReservationDTO = reservationRepository.save(updatedReservation);

        return new ResponseEntity<>(responseReservationDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Boolean> deleteReservation(@PathVariable Long reservationId) {
        Optional<ResponseReservationDTO> reservation = reservationRepository.findById(reservationId);

        if (reservation.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<ResponseHolidayDTO> holiday = holidayRepository.findById(reservation.get().getHoliday().getId());

        if (holiday.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ResponseHolidayDTO holidayDTO = holiday.get();
        holidayDTO.setFreeSlots(holidayDTO.getFreeSlots() + 1);

        holidayRepository.save(holidayDTO);

        try {
            reservationRepository.deleteById(reservationId);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
