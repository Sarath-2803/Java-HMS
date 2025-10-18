package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Amenity;

public class AmenityDAO {

    public Amenity save(Amenity amenity, Connection conn) {
        String sql = "INSERT INTO amenities (capacity) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, amenity.getCapacity());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    amenity.setId(rs.getLong(1));
                }
            }
            return amenity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Amenity> findAll(Connection conn) {
        String sql = "SELECT id, capacity FROM amenities";
        List<Amenity> amenities = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Amenity amenity = new Amenity(rs.getInt("capacity"));
                amenity.setId(rs.getLong("id"));
                System.out.println("Type of amenity: " + amenity.getCapacity());
                amenities.add(amenity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Amenities fetched from DB: " + amenities);
        return amenities;
    }

    public Amenity findById(Long id, Connection conn) {
        String sql = "SELECT id, capacity FROM amenities WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Amenity amenity = new Amenity(rs.getInt("capacity"));
                    amenity.setId(rs.getLong("id"));
                    return amenity;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Amenity update(Amenity amenity, Connection conn) {
        String sql = "UPDATE amenities SET capacity = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, amenity.getCapacity());
            stmt.setLong(2, amenity.getId());
            stmt.executeUpdate();
            return amenity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id, Connection conn) {
        String sql = "DELETE FROM amenities WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}