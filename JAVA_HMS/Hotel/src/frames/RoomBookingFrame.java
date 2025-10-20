package frames;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.regex.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Connection;
import model.Room;
import model.RoomBooking;
import model.User;
import dao.RoomDAO;
import dao.UserDAO;
import dao.RoomBookingDAO;
import util.*;

public class RoomBookingFrame extends JFrame {

private JTextField guestNameField, checkInField, checkOutField;
private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public RoomBookingFrame() {
        setTitle("New Room Booking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        ImageIcon icon = new ImageIcon("assets/HMSICON.png");
        setIconImage(icon.getImage());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Booking Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));


        formPanel.add(new JLabel("Registered Email ID:"));
        guestNameField = new JTextField(30);
        guestNameField.setPreferredSize(new Dimension(120, 25));
        formPanel.add(guestNameField);

        formPanel.add(new JLabel("Room Type:"));
        String[] roomTypes = {"Single", "Double", "Suite", "Deluxe"};
        JComboBox<String> roomTypeDropdown = new JComboBox<>(roomTypes);
        roomTypeDropdown.setSelectedIndex(0);
        roomTypeDropdown.setPreferredSize(new Dimension(120, 25));
        formPanel.add(roomTypeDropdown);

        formPanel.add(new JLabel("Check-In Date:"));
        checkInField = new JTextField("DD/MM/YYYY");
        formPanel.add(checkInField);

        formPanel.add(new JLabel("Check-Out Date:"));
        checkOutField = new JTextField("DD/MM/YYYY");
        formPanel.add(checkOutField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton newRegisterButton = new JButton("Register New User");
        newRegisterButton.addActionListener(e -> {
            dispose();
            NewUserFrame.main(new String[]{});
        });

        JButton payButton = new JButton("Confirm & Pay");
        payButton.addActionListener(e -> {
                String email = guestNameField.getText().trim();
                String roomType = (String) roomTypeDropdown.getSelectedItem();
                String checkIn = checkInField.getText().trim();
                String checkOut = checkOutField.getText().trim();
                StringBuilder errorMsg = new StringBuilder();
                if (email.isEmpty()) errorMsg.append("- Email is required.\n");
                if (checkIn.isEmpty()) errorMsg.append("- Check-in date is required.\n");
                if (checkOut.isEmpty()) errorMsg.append("- Check-out date is required.\n");
                java.time.LocalDate checkInDate = null, checkOutDate = null;
                if(!EMAIL_PATTERN.matcher(email).matches())
                    errorMsg.append("- Invalid email format.\n");
                if (errorMsg.length() == 0) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        checkInDate = java.time.LocalDate.parse(checkIn, formatter);
                        checkOutDate = java.time.LocalDate.parse(checkOut, formatter);
                        java.time.LocalDate now = java.time.LocalDate.now();
                        if (checkInDate.isBefore(now)) {
                            errorMsg.append("- Reservation Date cannot be in the past.\n");
                        } else if (!checkOutDate.isAfter(checkInDate)) {
                            errorMsg.append("- Illegal reservation date. Check-in date must be before check-out date.\n");
                        }
                    } catch (Exception ex) {
                        errorMsg.append("- Invalid date format. Use DD/MM/YYYY for date.\n");
                        System.out.println(ex.getMessage());
                    }
                }
                if (errorMsg.length() > 0) {
                    JOptionPane.showMessageDialog(this, errorMsg.toString(), "Missing or Invalid Information", JOptionPane.ERROR_MESSAGE);
                } else {
                    try(Connection conn = Utils.getConnection()) {
                        RoomBookingDAO bd = new RoomBookingDAO();
                        UserDAO ud = new UserDAO();
                        RoomDAO rd = new RoomDAO();
                        User user = ud.findByEmail(email, conn);
                        if (user == null) {
                            centeringDialog("User not Registered\nPlease Register", "Alert");
                            dispose();
                            new NewUserFrame().setVisible(true);
                        }
                        RoomBooking bkng = bd.bookRoom(user.getId(), roomType, checkInDate, checkOutDate, conn);
                        
                        if(bkng.getRoomId() == null) {
                            centeringDialog("Failed to book room", "Alert");
                        }
                        else {
                            //centeringDialog("Room has been successfully booked", "Success");
                            dispose();
                            new PaymentFrame(user.getId(), bkng.getId(), bkng.getTotalPrice()).setVisible(true);
                        }
                    } catch (Exception ex) {
                        centeringDialog(ex.getMessage(), "Alert");
                        dispose();
                        new BookingFrame().setVisible(true);
                    }

                }
            });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            dispose();
            BookingFrame.main(new String[]{});
        });

        buttonPanel.add(newRegisterButton);
        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private void centeringDialog(String message, String title) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RoomBookingFrame().setVisible(true));
    }
}
