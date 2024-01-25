package uni.bachelors.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uni.bachelors.travelagency.model.ResponseHolidayDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<ResponseHolidayDTO, Long> {
    @Query("SELECT h FROM ResponseHolidayDTO h " +
           "WHERE (:locationIdList IS NULL OR h.location.id IN :locationIdList) " +
           "AND (:startDate IS NULL OR h.startDate = :startDate) " +
           "AND (:duration IS NULL OR h.duration = :duration)")
    List<ResponseHolidayDTO> findByFilter(@Param("locationIdList") List<Long> locationIdList,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("duration") Integer duration);
}
