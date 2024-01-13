package uni.bachelors.travelagency.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "holiday")
public class ResponseHolidayDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private ResponseLocationDTO location;
    private String title;
    private Date startDate;
    private Integer duration;
    private Double price;
    private Integer freeSlots;

    public ResponseHolidayDTO() {}

    public ResponseHolidayDTO(Long id, ResponseLocationDTO location, String title, Date startDate, Integer duration, Double price, Integer freeSlots) {
        this.id = id;
        this.location = location;
        this.title = title;
        this.startDate = startDate;
        this.duration = duration;
        this.price = price;
        this.freeSlots = freeSlots;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResponseLocationDTO getLocation() {
        return location;
    }

    public void setLocation(ResponseLocationDTO location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getFreeSlots() {
        return freeSlots;
    }

    public void setFreeSlots(Integer freeSlots) {
        this.freeSlots = freeSlots;
    }
}
