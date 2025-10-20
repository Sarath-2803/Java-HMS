package frames;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import model.RoomBooking;
import model.User;
import model.Room;
import dao.RoomDAO;
import dao.UserDAO;
import dao.RoomBookingDAO;
import util.*;
import java.util.List;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        ImageIcon icon = new ImageIcon("assets/HMSICON.png");
        setIconImage(icon.getImage());

        // Table Coloumns
        String[] columns = {"Booking ID", "Guest", "Room", "Check In", "Check Out"};
        List<Object[]> data = new ArrayList<>();
        try(Connection conn = util.Utils.getConnection()){
            RoomBookingDAO bd = new RoomBookingDAO();
            UserDAO ud = new UserDAO();
            RoomDAO rd = new RoomDAO();
            List<RoomBooking> bkngs = bd.findAll(conn);
            for (RoomBooking bkng : bkngs) {
                Object[] row = new Object[6];
                row[0] = "XYZHBN" + String.format("%05d", bkng.getId());
                row[1] = ud.findById(bkng.getUserId(), conn).getUsername();
                row[2] = rd.findById(bkng.getRoomId(), conn).getRoomNumber();
                row[3] = convertDateFormat(bkng.getCheckIn().toString());
                row[4] = convertDateFormat(bkng.getCheckOut().toString());
                //row[5] = bkng.getStatus() ? "Success" : "Failed";
                data.add(row);
            }
        }
        catch(Exception e)
        {
            centeringDialog(e.getMessage(), "Error");
        }

        Object[][] tableData = data.toArray(new Object[0][]);
        DefaultTableModel model = new DefaultTableModel(tableData, columns) {
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
        RoomBookingFrame.main(new String[]{});
    }
    private void handleExtendBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bookingIdmod = (String) bookingTable.getValueAt(selectedRow, 0);
            String digits = bookingIdmod.replaceAll("\\D+", "");
            long bookingId = Long.parseLong(digits);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(bookingTable);
            extendBooking(frame, bookingId);
        } else {
            centeringDialog("Select a booking to extend", "Alert");
        }
    }
    private void extendBooking(JFrame parent, long bookingId) {
        JTextField newDateField = new JTextField(10);
        JOptionPane optionPane = new JOptionPane(
            new Object[] {
                "Extend booking ID: " + bookingId,
                "Enter new checkout date (DD/MM/YYYY):", newDateField
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
                StringBuilder errorMsg = new StringBuilder();
                String checkIn = bookingTable.getValueAt(bookingTable.getSelectedRow(), 3).toString();
                String checkOut = bookingTable.getValueAt(bookingTable.getSelectedRow(), 4).toString();
                String newDate = newDateField.getText().trim();
                if (newDate.isEmpty()) errorMsg.append("- New date is required.\n");
                java.time.LocalDate checkInDate = null, checkOutDate = null, newCheckOutDate = null;
                try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        checkInDate = java.time.LocalDate.parse(checkIn, formatter);
                        checkOutDate = java.time.LocalDate.parse(checkOut, formatter);
                        newCheckOutDate = java.time.LocalDate.parse(newDate, formatter);
                        java.time.LocalDate now = java.time.LocalDate.now();
                        if (!newCheckOutDate.isAfter(checkOutDate)) {
                            errorMsg.append("- New date cannot be before current check-out date.\n");
                        }
                    } catch (Exception ex) {
                        errorMsg.append("- Invalid date format. Use DD/MM/YYYY for date.\n");
                        System.out.println(ex.getMessage());
                    }
                if (errorMsg.length() > 0) {
                    centeringDialog(errorMsg.toString(), "Alert");
                    return;
                }
                else {

                    try(Connection conn = Utils.getConnection()) {
                        RoomBookingDAO bd = new RoomBookingDAO();
                        double current = bd.findById(bookingId, conn).getTotalPrice();
                        RoomBooking newbkng = bd.extendBooking(bookingId, newCheckOutDate, conn);
                        if(newbkng != null) {
                            dispose();
                            new PaymentFrame(newbkng.getUserId(), bookingId, newbkng.getTotalPrice() - current).setVisible(true);
                        }
                        else {
                            centeringDialog("Failed to extend booking", "Alert");
                        }
                    }
                    catch(Exception ex) {
                        centeringDialog(ex.getMessage(), "Alert");
                    }
                }
            }
    }

    private void handleCancelBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bookingIdmod = (String) bookingTable.getValueAt(selectedRow, 0);
            String digits = bookingIdmod.replaceAll("\\D+", "");
            long bookingId = Long.parseLong(digits);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(bookingTable);
            cancelBooking(frame, bookingId);
        } else {
            centeringDialog("Select a booking to cancel", "Alert");
        }
    }
    private void cancelBooking(JFrame parent, long bookingId) {
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
            try(Connection conn = Utils.getConnection()) {
                        RoomBookingDAO bd = new RoomBookingDAO();
                        if(bd.delete(bookingId, conn)) {
                            dispose();
                            new BookingFrame().setVisible(true);
                            JOptionPane infoPane = new JOptionPane(
                                    "Booking ID " + bookingId + " has been cancelled.",
                                    JOptionPane.INFORMATION_MESSAGE);

                            JDialog infoDialog = infoPane.createDialog(parent, "Cancelled");
                            infoDialog.setSize(400, 200);
                            infoDialog.setLocationRelativeTo(parent);
                            infoDialog.setVisible(true);
                        }
                        else {
                            centeringDialog("Failed to cancel booking", "Alert");
                        }
                    }
                    catch(Exception ex) {
                        centeringDialog(ex.getMessage(), "Alert");
                    }
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
    private String convertDateFormat(String yyyyMmDd) {
    try {
        // Parse YYYY-MM-DD format
        String[] parts = yyyyMmDd.split("-");
        if (parts.length == 3) {
            String year = parts[0];
            String month = parts[1];
            String day = parts[2];
            
            // Convert to DD/MM/YYYY format
            return day + "/" + month + "/" + year;
        }
    } catch (Exception e) {
        centeringDialog(e.getMessage(), "Error");
    }
    return yyyyMmDd; // Return original if conversion fails
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingFrame().setVisible(true));
    }
}
