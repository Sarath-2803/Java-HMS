package model;
import java.sql.Timestamp;
public class AmenityBooking {
    private Long id;
    private Long amenity_id;
    private Timestamp start_time;
    private Timestamp end_time;

    // Constructors
    public AmenityBooking() {}

    public AmenityBooking(Long amenity_id, Timestamp start_time, Timestamp end_time) {
        this.amenity_id=amenity_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public Long getAmenityId() {
        return amenity_id;
    }

    public Timestamp getStartTime() {
        return start_time;
    }

    public Timestamp getEndTime() {
        return end_time;
    }

    //setter

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmenityId(Long amenity_id) {
        this.amenity_id = amenity_id;
    }

    public void setStartTime(Timestamp start_time) {
        this.start_time = start_time;
    }

    public void setEndTime(Timestamp end_time) {
        this.end_time = end_time;
    }
}