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

        JButton newRoombtn = new JButton("Add new Room");
        newRoombtn.addActionListener(e -> {
            
        });

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
