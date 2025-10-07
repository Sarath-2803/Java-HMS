package service;

import java.sql.Connection;
import dao.RoomBookingDAO;
import model.RoomBooking;

import java.util.List;

public class RoomBookingService {
    private final RoomBookingDAO bookingDao;

    public RoomBookingService(RoomBookingDAO bookingDao) {
        this.bookingDao = bookingDao;
    }

    public RoomBooking createBooking(RoomBooking booking,Connection conn) {
        return bookingDao.bookRoom(booking,conn);
    }

    public List<RoomBooking> getAllBookings(Connection conn) {
        return bookingDao.findAll(conn);
    }

    public RoomBooking getBooking(Long id,Connection conn) {
        RoomBooking booking = bookingDao.findById(id,conn);
        if (booking == null) throw new RuntimeException("Booking not found");
        return booking;
    }

    public RoomBooking updateBooking(RoomBooking booking,Connection conn) {
        return bookingDao.update(booking,conn);
    }

    public void deleteBooking(Long id,Connection conn) {
        bookingDao.delete(id,conn);
    }
}
