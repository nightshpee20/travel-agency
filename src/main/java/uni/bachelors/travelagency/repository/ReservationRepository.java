package uni.bachelors.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.bachelors.travelagency.model.ResponseReservationDTO;

public interface ReservationRepository extends JpaRepository<ResponseReservationDTO, Long> {
}
