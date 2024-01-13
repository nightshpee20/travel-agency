package uni.bachelors.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.bachelors.travelagency.model.ResponseHolidayDTO;

@Repository
public interface HolidayRepository extends JpaRepository<ResponseHolidayDTO, Long> {
}
