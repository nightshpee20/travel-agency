package uni.bachelors.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.bachelors.travelagency.model.ResponseReservationDTO;

@Repository
public interface ReservationRepository extends JpaRepository<ResponseReservationDTO, Long> {
}
