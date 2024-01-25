package uni.bachelors.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uni.bachelors.travelagency.model.ResponseLocationDTO;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<ResponseLocationDTO, Long> {
    @Query("SELECT l.id FROM ResponseLocationDTO l WHERE l.city LIKE %:location% OR l.country LIKE %:location%")
    List<Long> findIdByCityOrCountry(@Param("location") String location);
}
