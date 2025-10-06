import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class NewUserFrame extends JFrame {

private JTextField guestNameField, emailField, phoneNoField;

    public NewUserFrame() {
        setTitle("New User");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        ImageIcon icon = new ImageIcon("HMSICON.png");
        setIconImage(icon.getImage());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "User Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        formPanel.add(new JLabel("Guest Name:"));
        guestNameField = new JTextField(30);
        guestNameField.setPreferredSize(new Dimension(120, 25));
        formPanel.add(guestNameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(30);
        emailField.setPreferredSize(new Dimension(120, 25));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone Number:"));
        phoneNoField = new JTextField(30);
        phoneNoField.setPreferredSize(new Dimension(120, 25));
        formPanel.add(phoneNoField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton registerButton = new JButton("Register and Proceed to Book");
        registerButton.addActionListener(e -> {
            dispose();
            //Check if all are filled
            //Check if mail ID is already registered, if yes, prompt already registered and go to room booking
            //else create new user data and go to room booking
            RoomBooking.main(new String[]{});
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            dispose();
            new BookingFrame().setVisible(true);
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NewUserFrame().setVisible(true));
    }
}
