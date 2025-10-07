package frames;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.sql.Connection;
import util.*;
import model.Room;
import dao.RoomDAO;
import java.util.List;
import java.util.ArrayList;

public class RoomStatus extends JFrame {

    private JTable roomTable; 

    public RoomStatus() {
        setTitle("Room Status - Today");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        ImageIcon icon = new ImageIcon("assets/HMSICON.png");
        setIconImage(icon.getImage());

        // Connect to DB, get room id, room no, type, status from bookings db
        String[] coloumns = {"Room ID", "Room No", "Type"};
        List<Object[]> data = new ArrayList<>();
        try(Connection conn = util.Utils.getConnection()){
            RoomDAO rd = new RoomDAO();
            List<Room> rooms = rd.findAll(conn);
            for (Room room : rooms) {
                Object[] row = new Object[4];
                row[0] = room.getId();
                row[1] = room.getRoomNumber();
                row[2] = room.getType();
                data.add(row);
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        Object[][] tableData = data.toArray(new Object[0][]);
        DefaultTableModel model = new DefaultTableModel(tableData, coloumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(roomTable);

        JButton backButton = new JButton("← Back to Dashboard");
        backButton.addActionListener(e -> {
            dispose();
        });

        JButton newBookingButton = new JButton("New Booking");
        newBookingButton.addActionListener(e -> {
            dispose();
            RoomBookingFrame.main(new String[]{});
        });

        // --- Add Room Button (bottom-right) --- 
        JButton btnAddRoom = new JButton("Add Room"); btnAddRoom.addActionListener(e -> { 
            // Step 1: Ask for access key 
            String accessKey = JOptionPane.showInputDialog(this, "Enter Access Key:", "Admin Verification", JOptionPane.PLAIN_MESSAGE); if (accessKey == null || !accessKey.equals("sudo123")) { 
                // change key as needed 
                JOptionPane.showMessageDialog(this, "Invalid Access Key!", "Access Denied", JOptionPane.ERROR_MESSAGE); return; }

            // Step 2: Room input fields
            String[] types = {"Single", "Double", "Suite", "Deluxe"};
            JComboBox<String> typeBox = new JComboBox<>(types);
            JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
            panel.add(new JLabel("Room Type:"));
            panel.add(typeBox);

            int result = JOptionPane.showConfirmDialog(this, panel, "Add Room", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) return;

            String type = (String) typeBox.getSelectedItem();

            try (Connection conn = Utils.getConnection()) {
                RoomDAO dao = new RoomDAO();
                    Room newRoom = new Room(type);
                    dao.create(newRoom, conn);
                    JOptionPane.showMessageDialog(this, "Room added successfully!");

                // Step 4: Refresh UI
                dispose();
                new RoomStatus().setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            });

            // Add button to SOUTH region (aligned right) 
            JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
            southPanel.add(btnAddRoom); 
        add(southPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(newBookingButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RoomStatus().setVisible(true);
        });
    }
}
