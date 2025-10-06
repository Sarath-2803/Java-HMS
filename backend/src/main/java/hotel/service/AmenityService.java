package service;

import dao.AmenityDAO;
import java.sql.Connection;
import java.util.List;
import model.Amenity;

public class AmenityService {
    private AmenityDAO amenityDAO;

    public AmenityService(AmenityDAO amenityDAO) {
        this.amenityDAO = amenityDAO;
    }

    // Create new amenity
    public Amenity createAmenity(Integer capacity, Connection conn) {
        Amenity newAmenity = new Amenity(capacity);
        return amenityDAO.save(newAmenity, conn);
    }

    // Find all amenities
    public List<Amenity> getAllAmenities(Connection conn) {
        return amenityDAO.findAll(conn);
    }

    // Find by id
    public Amenity getById(Long id, Connection conn) {
        Amenity amenity = amenityDAO.findById(id, conn);
        if (amenity == null) {
            throw new RuntimeException("Amenity not found!");
        }
        return amenity;
    }

    // Update amenity
    public Amenity updateAmenity(Amenity amenity, Connection conn) {
        getById(amenity.getId(), conn);
        return amenityDAO.update(amenity, conn);
    }

    // Delete amenity
    public void deleteAmenity(Long id, Connection conn) {
        getById(id, conn);
        amenityDAO.delete(id, conn);
    }
}