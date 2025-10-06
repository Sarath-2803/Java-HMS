package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.AmenityBooking;

public class AmenityBookingDAO {

    // Book an amenity for a time slot and required capacity
    public AmenityBooking bookAmenity(int requiredCapacity, Timestamp startTime, Timestamp endTime, Connection conn) {
        String findAmenitySql =
            "SELECT id FROM amenities a " +
            "WHERE a.capacity >= ? AND NOT EXISTS (" +
            "  SELECT 1 FROM amenity_bookings b " +
            "  WHERE b.amenity_id = a.id AND (? < b.end_time AND ? > b.start_time)" +
            ") LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(findAmenitySql)) {
            stmt.setInt(1, requiredCapacity);
            stmt.setTimestamp(2, startTime);
            stmt.setTimestamp(3, endTime);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long amenityId = rs.getLong("id");
                    String insertSql = "INSERT INTO amenity_bookings (amenity_id, start_time, end_time) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                        insertStmt.setLong(1, amenityId);
                        insertStmt.setTimestamp(2, startTime);
                        insertStmt.setTimestamp(3, endTime);
                        insertStmt.executeUpdate();
                        try (ResultSet genKeys = insertStmt.getGeneratedKeys()) {
                            if (genKeys.next()) {
                                AmenityBooking booking = new AmenityBooking();
                                booking.setId(genKeys.getLong(1));
                                booking.setAmenityId(amenityId);
                                booking.setStartTime(startTime);
                                booking.setEndTime(endTime);
                                return booking;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // No available amenity found
    }


    public List<AmenityBooking> findAll(Connection conn) {
        String sql = "SELECT id, amenity_id, start_time, end_time FROM amenity_bookings";
        List<AmenityBooking> bookings = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AmenityBooking booking = new AmenityBooking();
                booking.setId(rs.getLong("id"));
                booking.setAmenityId(rs.getLong("amenity_id"));
                booking.setStartTime(rs.getTimestamp("start_time"));
                booking.setEndTime(rs.getTimestamp("end_time"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bookings;
    }

    public AmenityBooking findById(Long id, Connection conn) {
        String sql = "SELECT id, amenity_id, start_time, end_time FROM amenity_bookings WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AmenityBooking booking = new AmenityBooking();
                    booking.setId(rs.getLong("id"));
                    booking.setAmenityId(rs.getLong("amenity_id"));
                    booking.setStartTime(rs.getTimestamp("start_time"));
                    booking.setEndTime(rs.getTimestamp("end_time"));
                    return booking;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public AmenityBooking update(AmenityBooking booking, Connection conn) {
        String sql = "UPDATE amenity_bookings SET amenity_id = ?, start_time = ?, end_time = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, booking.getAmenityId());
            stmt.setTimestamp(2, booking.getStartTime());
            stmt.setTimestamp(3, booking.getEndTime());
            stmt.setLong(4, booking.getId());
            stmt.executeUpdate();
            return booking;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id, Connection conn) {
        String sql = "DELETE FROM amenity_bookings WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}