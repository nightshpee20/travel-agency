package uni.bachelors.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.bachelors.travelagency.model.ResponseLocationDTO;

public interface LocationRepository extends JpaRepository<ResponseLocationDTO, Long> {
}
