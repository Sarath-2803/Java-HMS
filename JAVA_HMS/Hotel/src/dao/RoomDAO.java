package dao;

import model.Room;
import util.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public Room create(Room room, Connection conn) {
        String sql = "INSERT INTO rooms (type, price, capacity) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room.getType());
            ps.setDouble(2, room.getPrice());
            ps.setInt(3, room.getCapacity());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                room.setId(rs.getLong("id"));
            }
            return room;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating room: " + e.getMessage(), e);
        }
    }

    public List<Room> findAll(Connection conn) {
        String sql = "SELECT * FROM rooms";
        List<Room> rooms = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching rooms: " + e.getMessage(), e);
        }
        return rooms;
    }

    public Room findById(Long id, Connection conn) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching room: " + e.getMessage(), e);
        }
    }

    public Room update(Room room, Connection conn) {
        String sql = "UPDATE rooms SET roomNumber=?, type=?, price=?, capacity=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, room.getRoomNumber());
            ps.setString(2, room.getType());
            ps.setDouble(3, room.getPrice());
            ps.setInt(4, room.getCapacity());
            ps.setLong(5, room.getId());
            ps.executeUpdate();
            return room;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating room: " + e.getMessage(), e);
        }
    }

    public void delete(Long id, Connection conn) {
        String sql = "DELETE FROM rooms WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting room: " + e.getMessage(), e);
        }
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getLong("id"));
        room.setRoomNumber(rs.getLong("roomNumber"));
        room.setType(rs.getString("type"));
        room.setPrice(rs.getDouble("price"));
        room.setCapacity(rs.getInt("capacity"));
        return room;
    }
}
