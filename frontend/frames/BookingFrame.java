import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//import http.HttpClient;
//import org.json.*;
//import model.User;
//import dao.UserDAO;
//import util.*;

public class BookingFrame extends JFrame {

    private JTable bookingTable;
    private JButton btnBack, btnNewBooking, btnExtend, btnCancel;

    public BookingFrame() {
        setTitle("Bookings");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        ImageIcon icon = new ImageIcon("HMSICON.png");
        setIconImage(icon.getImage());

        // Table Coloumns
        String[] columns = {"Booking ID", "Guest", "Room", "Check In", "Check Out", "Status"};
        Object data[][] = {{"XYZH001", "Sarath", 101, "06/10/2025", "10/10/2025", "Active"}, {"XYZH002", "Jonas", 202, "07/10/2025", "09/10/2025", "Cancelled"}}; // Dummy data for testing purposes -- Need to connect DB 
        //Bookinf ID = XYZH + table id (3 digits)
        //Guest name from user id and users table
        //Room number from room table
        //for the time being printing the users

        DefaultTableModel model = new DefaultTableModel(data, columns) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
            }
        };

        bookingTable = new JTable(model);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(bookingTable), BorderLayout.CENTER);


        // Buttons for further actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnNewBooking = new JButton("New Booking");
        btnExtend = new JButton("Extend Booking");
        btnCancel = new JButton("Cancel Booking");

        buttonPanel.add(btnNewBooking);
        buttonPanel.add(btnExtend);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        // Back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnBack = new JButton("← Back to Dashboard");
        topPanel.add(btnBack);
        add(topPanel, BorderLayout.NORTH);


        // Button actions
        btnNewBooking.addActionListener(e -> newBooking());
        btnExtend.addActionListener(e -> handleExtendBooking());
        btnCancel.addActionListener(e -> handleCancelBooking());
        btnBack.addActionListener(e -> goBack());

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void newBooking() {
        dispose();
        RoomBooking.main(new String[]{});
    }
    private void handleExtendBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bookingIdmod = bookingTable.getValueAt(selectedRow, 0).toString().substring(4);
            int bookingId = (int) Integer.parseInt(bookingIdmod);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(bookingTable);
            extendBooking(frame, bookingId);
        } else {
            centeringDialog("Select a booking to extend", "Alert");
        }
    }
    private void extendBooking(JFrame parent, int bookingId) {
        JTextField newDateField = new JTextField(10);
        JOptionPane optionPane = new JOptionPane(
            new Object[] {
                "Extend booking ID: " + bookingId,
                "Enter new checkout date (YYYY-MM-DD):", newDateField
            },
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(parent, "Extend Booking");
        dialog.setSize(400, 200);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        Object selectedValue = optionPane.getValue();
            if (selectedValue != null && (int) selectedValue == JOptionPane.OK_OPTION) {
                String newDate = newDateField.getText().trim();

                JOptionPane infoPane = new JOptionPane(
                        "Booking ID " + bookingId + " has been extended to " + newDate,
                        JOptionPane.INFORMATION_MESSAGE);

                JDialog infoDialog = infoPane.createDialog(parent, "Extended");
                infoDialog.setSize(450, 200);
                infoDialog.setLocationRelativeTo(parent);
                infoDialog.setVisible(true);
            }
    }

    private void handleCancelBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bookingIdmod = bookingTable.getValueAt(selectedRow, 0).toString().substring(4);
            int bookingId = (int) Integer.parseInt(bookingIdmod);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(bookingTable);
            cancelBooking(frame, bookingId);
        } else {
            centeringDialog("Select a booking to cancel", "Alert");
        }
    }
    private void cancelBooking(JFrame parent, int bookingId) {
        JOptionPane optionPane = new JOptionPane(
            "Cancel booking ID: " + bookingId + "?",
            JOptionPane.WARNING_MESSAGE,
            JOptionPane.YES_NO_OPTION);
        JDialog dialog = optionPane.createDialog(parent, "Cancel Booking");
        dialog.setSize(400, 200);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        Object selectedValue = optionPane.getValue();
        if (selectedValue != null && (int) selectedValue == JOptionPane.YES_OPTION) {
            JOptionPane infoPane = new JOptionPane(
                    "Booking ID " + bookingId + " has been cancelled.",
                    JOptionPane.INFORMATION_MESSAGE);

            JDialog infoDialog = infoPane.createDialog(parent, "Cancelled");
            infoDialog.setSize(400, 200);
            infoDialog.setLocationRelativeTo(parent);
            infoDialog.setVisible(true);
        }
    }
    private void goBack() {
        dispose();
        
    }

    private void centeringDialog(String message, String title) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingFrame().setVisible(true));
    }
}
