package model;

public class Amenity {
    private Long id;
    private Integer capacity;

    //constructors
    public Amenity() {}

    public Amenity(Integer capacity) {
        this.capacity = capacity;
    }

    //getter
    public Long getId() {
        return id;
    }

    public Integer getCapacity(){
        return capacity;
    }

    //setter
    public void setId(Long id) {
        this.id = id;
    }
    public void setCapacity(Integer type) {
        this.capacity = capacity;
    }
}