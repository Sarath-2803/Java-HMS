package frames;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import service.AmenityService;
import service.AmenityBookingService;
import model.Amenity;
import model.AmenityBooking;
import util.Utils;

public class restaurant_chart {
    public static void main(String args[]){
  
            JFrame frame = new JFrame("Restaurant Chart");
            frame.setSize(600, 600);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(null);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            ImageIcon icon = new ImageIcon("assets/HMSICON.png");
        frame.setIconImage(icon.getImage());

            JLabel label = new JLabel("Restaurant Chart ");
            label.setBounds(200, 30, 300, 30);
            frame.add(label);
            label.setFont(new Font("Arial", Font.BOLD, 24));

            JPanel panel = new JPanel();
            panel.setBounds(37, 90, 500, 430);
            panel.setLayout(null);
            panel.setBackground(new Color(220, 220, 220));
            frame.add(panel);
            JButton backButton = new JButton("Back");
            backButton.setBounds(10, 10, 80, 30);
            frame.add(backButton);
            backButton.addActionListener(e -> frame.dispose());
            
            JButton refreshButton = new JButton("Refresh");
            refreshButton.setBounds(100, 10, 80, 30);
            frame.add(refreshButton);

            String[] columnNames = {"Table ID", "Capacity", "Status", "Reserved Until"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(20, 20, 460, 390);
            panel.add(scrollPane);

            // Load data from services directly
            Runnable loadData = () -> {
                Connection connection = null;
                try {
                    tableModel.setRowCount(0);
                    
                    // Get database connection
                    connection = Utils.getConnection();
                    
                    // Create service instances
                    dao.AmenityDAO amenityDAO = new dao.AmenityDAO();
                    service.AmenityService amenityService = new service.AmenityService(amenityDAO);
                    dao.AmenityBookingDAO amenityBookingDAO = new dao.AmenityBookingDAO();
                    service.AmenityBookingService amenityBookingService = new service.AmenityBookingService(amenityBookingDAO);
                    
                    // Call service methods directly
                    List<Amenity> amenities = amenityService.getAllAmenities(connection);
                    List<AmenityBooking> bookings = amenityBookingService.getAllAmenityBookings(connection);
                    
                    // Map current reservations
                    Map<Long, String> reservedTables = new HashMap<>();
                    LocalDateTime now = LocalDateTime.now();
                    
                    for (AmenityBooking booking : bookings) {
                        long amenityId = booking.getAmenityId();
                        LocalDateTime endTime = booking.getEndTime().toLocalDateTime();
                        
                        if (endTime.isAfter(now)) {
                            String formattedEndTime = endTime.format(
                                java.time.format.DateTimeFormatter.ofPattern("MM/dd HH:mm"));
                            reservedTables.put(amenityId, formattedEndTime);
                        }
                    }
                    
                    // Populate table
                    for (Amenity amenity : amenities) {
                        long id = amenity.getId();
                        int capacity = amenity.getCapacity();
                        
                        String status = reservedTables.containsKey(id) ? "Reserved" : "Available";
                        String reservedUntil = reservedTables.getOrDefault(id, "-");
                        
                        tableModel.addRow(new Object[]{id, capacity, status, reservedUntil});
                    }
                    
                    if (amenities.isEmpty()) {
                        JOptionPane.showMessageDialog(frame,
                            "No tables found. Please add tables via backend.",
                            "No Tables", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                        "Failed to load data.\n" +
                        "Error: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Close connection
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (Exception closeEx) {
                            closeEx.printStackTrace();
                        }
                    }
                }
            };
            
            loadData.run();
            refreshButton.addActionListener(e -> loadData.run());

            frame.setVisible(true);
    }
}