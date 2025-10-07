package dao;

import model.RoomBooking;
import util.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import model.Room;

public class RoomBookingDAO {

    // Book a room with availability checking - returns booking with roomId or null if not available
    public RoomBooking bookRoom(Long userId, String roomType, LocalDate checkIn, LocalDate checkOut, Connection conn) {
        // First find an available room of the requested type
        String findAvailableRoomSql = 
            "SELECT id FROM rooms r " +
            "WHERE r.type = ? AND NOT EXISTS (" +
            "  SELECT 1 FROM roombookings b " +
            "  WHERE b.roomId = r.id AND (? < b.checkOut AND ? > b.checkIn)" +
            ") LIMIT 1";
        
        try (PreparedStatement findStmt = conn.prepareStatement(findAvailableRoomSql)) {
            findStmt.setString(1, roomType);
            findStmt.setTimestamp(2, Timestamp.valueOf(checkIn.atStartOfDay()));
            findStmt.setTimestamp(3, Timestamp.valueOf(checkOut.atStartOfDay()));
            
            Long roomId = null;
            try (ResultSet rs = findStmt.executeQuery()) {
                if (rs.next()) {
                    roomId = rs.getLong("id");
                }
            }
            
            // If no room available, return booking with roomId = null
            if (roomId == null) {
                RoomBooking unavailableBooking = new RoomBooking();
                unavailableBooking.setUserId(userId);
                unavailableBooking.setRoomId(null); // Indicates no room available
                unavailableBooking.setCheckIn(checkIn);
                unavailableBooking.setCheckOut(checkOut);
                unavailableBooking.setTotalPrice(0.0); // No price since no room
                return unavailableBooking;
            }
            
            // Calculate total price
            double totalPrice = calculateTotalPrice(roomId, checkIn, checkOut, conn);
            
            // If room available, proceed with booking
            String insertSql = "INSERT INTO roombookings (userId, roomId, checkIn, checkOut, totalPrice) VALUES (?, ?, ?, ?, ?) RETURNING id";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setLong(1, userId);
                ps.setLong(2, roomId);
                ps.setTimestamp(3, Timestamp.valueOf(checkIn.atStartOfDay()));
                ps.setTimestamp(4, Timestamp.valueOf(checkOut.atStartOfDay()));
                ps.setDouble(5, totalPrice);

                ResultSet rs = ps.executeQuery();
                RoomBooking booking = new RoomBooking();
                if (rs.next()) booking.setId(rs.getLong("id"));
                booking.setUserId(userId);
                booking.setRoomId(roomId);
                booking.setCheckIn(checkIn);
                booking.setCheckOut(checkOut);
                booking.setTotalPrice(totalPrice);
                return booking;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error booking room: " + e.getMessage(), e);
        }
    }

    // Calculate total price based on room price and duration
    private double calculateTotalPrice(Long roomId, LocalDate checkIn, LocalDate checkOut, Connection conn) {
        String sql = "SELECT price FROM rooms WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double roomPrice = rs.getDouble("price");
                long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
                return roomPrice * days;
            }
            throw new RuntimeException("Room not found with ID: " + roomId);
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating price: " + e.getMessage(), e);
        }
    }

    // Find available rooms by type and date range
    public List<Room> findAvailableRooms(String roomType, LocalDate checkIn, LocalDate checkOut, Connection conn) {
        String sql = 
            "SELECT r.* FROM rooms r " +
            "WHERE r.type = ? AND NOT EXISTS (" +
            "  SELECT 1 FROM roombookings b " +
            "  WHERE b.roomId = r.id AND (? < b.checkOut AND ? > b.checkIn)" +
            ")";
        
        List<Room> availableRooms = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomType);
            ps.setTimestamp(2, Timestamp.valueOf(checkIn.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(checkOut.atStartOfDay()));
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getLong("id"));
                room.setRoomNumber(rs.getLong("roomNumber"));
                room.setType(rs.getString("type"));
                room.setPrice(rs.getDouble("price"));
                room.setCapacity(rs.getInt("capacity"));
                availableRooms.add(room);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding available rooms: " + e.getMessage(), e);
        }
        return availableRooms;
    }

    // Check room availability (returns true/false)
    public boolean isRoomAvailable(String roomType, LocalDate checkIn, LocalDate checkOut, Connection conn) {
        String sql = 
            "SELECT COUNT(*) as available_count FROM rooms r " +
            "WHERE r.type = ? AND NOT EXISTS (" +
            "  SELECT 1 FROM roombookings b " +
            "  WHERE b.roomId = r.id AND (? < b.checkOut AND ? > b.checkIn)" +
            ")";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomType);
            ps.setTimestamp(2, Timestamp.valueOf(checkIn.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(checkOut.atStartOfDay()));
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("available_count") > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking room availability: " + e.getMessage(), e);
        }
    }
    public RoomBooking extendBooking(Long bookingId, LocalDate newCheckOut, Connection conn) {
        // First get the current booking details
        RoomBooking currentBooking = findById(bookingId, conn);
        if (currentBooking == null) {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }
        
        // Check if the room is available for the extended period
        boolean isAvailable = isRoomAvailableForExtension(
            currentBooking.getRoomId(), 
            currentBooking.getCheckIn(), 
            newCheckOut, 
            bookingId, // exclude current booking from conflict check
            conn
        );
        
        if (!isAvailable) {
            throw new RuntimeException("Room not available for the extended dates. Please choose different dates.");
        }
        
        // Calculate new total price for extended period
        double newTotalPrice = calculateTotalPrice(currentBooking.getRoomId(), currentBooking.getCheckIn(), newCheckOut, conn);
        
        // Update the booking
        String updateSql = "UPDATE roombookings SET checkOut = ?, totalPrice = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setTimestamp(1, Timestamp.valueOf(newCheckOut.atStartOfDay()));
            ps.setDouble(2, newTotalPrice);
            ps.setLong(3, bookingId);
            
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("Failed to extend booking");
            }
            
            // Return updated booking
            currentBooking.setCheckOut(newCheckOut);
            currentBooking.setTotalPrice(newTotalPrice);
            return currentBooking;
        } catch (SQLException e) {
            throw new RuntimeException("Error extending booking: " + e.getMessage(), e);
        }
    }

    // Check if room is available for extension (excludes current booking from conflict check)
    private boolean isRoomAvailableForExtension(Long roomId, LocalDate checkIn, LocalDate newCheckOut, Long excludeBookingId, Connection conn) {
        String sql = 
            "SELECT COUNT(*) as conflict_count FROM roombookings b " +
            "WHERE b.roomId = ? AND b.id != ? AND (? < b.checkOut AND ? > b.checkIn)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            ps.setLong(2, excludeBookingId);
            ps.setTimestamp(3, Timestamp.valueOf(checkIn.atStartOfDay()));
            ps.setTimestamp(4, Timestamp.valueOf(newCheckOut.atStartOfDay()));
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("conflict_count") == 0;
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking extension availability: " + e.getMessage(), e);
        }
    }

    // Existing methods (keep as is, but update to accept Connection parameter)
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

    public RoomBooking findById(Long id, Connection conn) {
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

    public RoomBooking update(RoomBooking booking, Connection conn) {
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

    public boolean delete(Long id, Connection conn) {
        String sql = "DELETE FROM roombookings WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting booking: " + e.getMessage(), e);
        }
    }

    private RoomBooking mapRow(ResultSet rs) throws SQLException {
        RoomBooking booking = new RoomBooking();
        booking.setId(rs.getLong("id"));
        booking.setUserId(rs.getLong("userId"));
        booking.setRoomId(rs.getLong("roomId"));
        booking.setCheckIn(rs.getTimestamp("checkIn").toLocalDateTime().toLocalDate());
        booking.setCheckOut(rs.getTimestamp("checkOut").toLocalDateTime().toLocalDate());
        booking.setTotalPrice(rs.getDouble("totalPrice"));
        return booking;
    }
}