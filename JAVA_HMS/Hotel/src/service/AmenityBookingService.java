package service;

import dao.AmenityBookingDAO;
import java.sql.Timestamp;
import java.sql.Connection;
import java.util.List;
import model.AmenityBooking;

public class AmenityBookingService {
    private AmenityBookingDAO amenityBookingDAO;

    public AmenityBookingService(AmenityBookingDAO amenityBookingDAO) {
        this.amenityBookingDAO = amenityBookingDAO;
    }

    // Create new amenity booking
    public AmenityBooking bookAmenity(int requiredCapacity,Timestamp startTime,Timestamp endTime, Connection conn) {       
        return amenityBookingDAO.bookAmenity(requiredCapacity, startTime, endTime, conn);
        }

    // Find all amenity bookings
    public List<AmenityBooking> getAllAmenityBookings(Connection conn) {
        return amenityBookingDAO.findAll(conn);
    }

    // Find by id
    public AmenityBooking getById(Long id, Connection conn) {
        AmenityBooking booking = amenityBookingDAO.findById(id, conn);
        if (booking == null) {
            throw new RuntimeException("Amenity booking not found!");
        }
        return booking;
    }

    // Update amenity booking
    public AmenityBooking updateAmenityBooking(AmenityBooking booking, Connection conn) {
        getById(booking.getId(), conn);
        return amenityBookingDAO.update(booking, conn);
    }

    // Delete amenity booking
    public void deleteAmenityBooking(Long id, Connection conn) {
        getById(id, conn);
        amenityBookingDAO.delete(id, conn);
    }
}