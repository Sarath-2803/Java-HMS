package dao;

import model.RoomBooking;
import util.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomBookingDAO {

    // Create new booking
    public RoomBooking bookRoom(RoomBooking booking, Connection conn) {
        String sql = "INSERT INTO roombookings (userId, roomId, checkIn, checkOut, totalPrice) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, booking.getUserId());
            ps.setLong(2, booking.getRoomId());
            ps.setTimestamp(3, Timestamp.valueOf(booking.getCheckIn().atStartOfDay()));
            ps.setTimestamp(4, Timestamp.valueOf(booking.getCheckOut().atStartOfDay()));
            ps.setDouble(5, booking.getTotalPrice());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) booking.setId(rs.getLong("id"));
            return booking;
        } catch (SQLException e) {
            throw new RuntimeException("Error booking room: " + e.getMessage(), e);
        }
    }

    // Find all bookings
    public List<RoomBooking> findAll(Connection conn) {
        String sql = "SELECT * FROM roombookings";
        List<RoomBooking> bookings = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) bookings.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching bookings: " + e.getMessage(), e);
        }
        return bookings;
    }

    // Find by ID
    public RoomBooking findById(Long id,Connection conn) {
        String sql = "SELECT * FROM roombookings WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching booking: " + e.getMessage(), e);
        }
    }

    // Update booking
    public RoomBooking update(RoomBooking booking,Connection conn) {
        String sql = "UPDATE roombookings SET userId=?, roomId=?, checkIn=?, checkOut=?, totalPrice=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, booking.getUserId());
            ps.setLong(2, booking.getRoomId());
            ps.setTimestamp(3, Timestamp.valueOf(booking.getCheckIn().atStartOfDay()));
            ps.setTimestamp(4, Timestamp.valueOf(booking.getCheckOut().atStartOfDay()));
            ps.setDouble(5, booking.getTotalPrice());
            ps.setLong(6, booking.getId());

            ps.executeUpdate();
            return booking;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking: " + e.getMessage(), e);
        }
    }

    // Delete booking
    public void delete(Long id,Connection conn) {
        String sql = "DELETE FROM roombookings WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting booking: " + e.getMessage(), e);
        }
    }

    // Map DB row to RoomBooking - SIMPLIFIED!
    private RoomBooking mapRow(ResultSet rs) throws SQLException {
        RoomBooking booking = new RoomBooking();
        booking.setId(rs.getLong("id"));
        booking.setUserId(rs.getLong("userId"));      // Directly set userId
        booking.setRoomId(rs.getLong("roomId"));      // Directly set roomId
        booking.setCheckIn(rs.getTimestamp("checkIn").toLocalDateTime().toLocalDate());
        booking.setCheckOut(rs.getTimestamp("checkOut").toLocalDateTime().toLocalDate());
        booking.setTotalPrice(rs.getDouble("totalPrice"));

        return booking;
    }
}
