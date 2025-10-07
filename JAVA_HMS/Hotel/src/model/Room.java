package model;

public class Room {
    private Long id;
    private Long roomNumber;
    private String type;
    private Double price;
    private Integer capacity;

    public Room() {}

    public Room(Long id, String roomNumber, String type, Double price, Integer capacity) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
    }

    public Room(Long roomNumber, String type, Double price, Integer capacity) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
    }
    public Room(String type){
        this.type = type;
        if(type.equals("Single")){
            this.price = 1000.0;
            this.capacity = 1;
        }
        else if(type.equals("Double")){
            this.price = 2000.0;
            this.capacity = 2;
        }
        else if(type.equals("Suite")){
            this.price = 3500.0;
            this.capacity = 4;
        }
        else if(type.equals("Deluxe")){
            this.price = 5000.0;
            this.capacity = 4;
        }
    }

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRoomNumber() { return roomNumber; }
    public void setRoomNumber(Long roomNumber) { this.roomNumber = roomNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}
