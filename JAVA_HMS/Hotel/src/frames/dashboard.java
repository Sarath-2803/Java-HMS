package frames;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
public class dashboard {
    public static void main(String args[]){
        JFrame frame = new JFrame("Dashboard");
        frame.setSize(1000, 740);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        ImageIcon icon = new ImageIcon("assets/HMSICON.png");
        frame.setIconImage(icon.getImage());

        JPanel navbar = new JPanel();
        navbar.setBounds(0, 0, 1000, 100);
        navbar.setBackground(new Color(0, 0, 51));
        frame.add(navbar);
        navbar.setLayout(null);

        JLabel title = new JLabel("Hotel Management System");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(24.0f));
        title.setBounds(20, 20, 400, 50);
        navbar.add(title);
        // Welcome label on the navbar
        JLabel welcomeLabel = new JLabel("Welcome to the Dashboard");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setBounds(740, 20, 400, 50);
        welcomeLabel.setForeground(new Color(255, 255, 255));
        navbar.add(welcomeLabel);

        JPanel sidebar = new JPanel();
        sidebar.setBounds(0, 100, 200, 900);
        sidebar.setBackground(new Color(1, 29, 95));
        frame.add(sidebar);
        sidebar.setLayout(null);
    // Sidebar buttons
    JButton btn1 = new JButton("Booking");
    btn1.setBounds(10, 50, 180, 100);
    btn1.setFont(btn1.getFont().deriveFont(16.0f));
    sidebar.add(btn1);
    btn1.setBackground(new Color(31, 173, 216));
    btn1.setFocusPainted(false);

    JButton btn2 = new JButton("<html>Restaurant <br> Reservation<html>");
    btn2.setBounds(10, 200, 180, 100);
    btn2.setFont(btn2.getFont().deriveFont(15.0f));
    sidebar.add(btn2);
    btn2.setBackground(new Color(0, 255, 0));
    btn2.setFocusPainted(false);

    JButton btn3 = new JButton("<html>Rooms <br> Status<html>");
    btn3.setBounds(10, 350, 180, 100);
    btn3.setFont(btn3.getFont().deriveFont(15.0f));
    sidebar.add(btn3);
    btn3.setBackground(new Color(255, 165, 0));
    btn3.setFocusPainted(false);

    JButton btn4 = new JButton("Logout");
    btn4.setBounds(10, 500, 180, 100);
    btn4.setFont(btn4.getFont().deriveFont(16.0f));
    sidebar.add(btn4);
    btn4.setBackground(new Color(255, 0, 0));
    btn4.setFocusPainted(false);

    // JPanel topbar = new JPanel();
    // topbar.setBounds(200, 100, 800, 150);
    // topbar.setBackground(Color.LIGHT_GRAY);
    // frame.add(topbar);
    // topbar.setLayout(null);

    // JPanel RoomsPanel = new JPanel();
    // RoomsPanel.setBounds(20, 20, 200, 100);
    // RoomsPanel.setBackground(new Color(153, 255, 255));
    // topbar.add(RoomsPanel);
   
    // RoomsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    // JLabel totalRoomsLabel = new JLabel("Total Rooms: 50");
    // totalRoomsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    // totalRoomsLabel.setBounds(30, 30, 200, 30);
   
    // RoomsPanel.add(totalRoomsLabel);
    // RoomsPanel.setLayout(null);

    // JPanel TotalGuestsPanel = new JPanel();
    // TotalGuestsPanel.setBounds(285, 20, 200, 100);
    // topbar.add(TotalGuestsPanel);
    // TotalGuestsPanel.setBackground(new Color(142, 0, 213));
    // TotalGuestsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    // JLabel totalGuestsLabel = new JLabel("Current Guests: 39");
    // totalGuestsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    // TotalGuestsPanel.add(totalGuestsLabel);
    // totalGuestsLabel.setBounds(30, 30, 200, 30);
    // TotalGuestsPanel.setLayout(null);

    // JPanel TotalRevenuePanel = new JPanel();
    // TotalRevenuePanel.setBounds(560, 20, 200, 100);
    // topbar.add(TotalRevenuePanel);
    // TotalRevenuePanel.setBackground(new Color(0, 213, 164));
    // TotalRevenuePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    // TotalRevenuePanel.setLayout(null);

    // JLabel totalRevenueLabel = new JLabel("Total Revenue: $5000");
    // totalRevenueLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    // TotalRevenuePanel.add(totalRevenueLabel);
    // totalRevenueLabel.setBounds(10, 30, 200, 30);

    JLabel welcomeLabel1 = new JLabel("Welcome to Hotel Reservation");
    welcomeLabel1.setFont(new Font("Arial", Font.PLAIN, 24));
    welcomeLabel1.setBounds(440, 400, 400, 30);
    frame.add(welcomeLabel1);

    JLabel chooseOptionLabel = new JLabel("Choose an option from the sidebar");
    chooseOptionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    chooseOptionLabel.setBounds(450, 435, 400, 30);
    frame.add(chooseOptionLabel);

    JLabel dateTimeLabel = new JLabel("Date and Time:");
    dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    dateTimeLabel.setBounds(460, 465, 400, 30);
    frame.add(dateTimeLabel);
  
        JLabel footer= new JLabel("@2025 All rights reserved | Hotel Management System");
        footer.setBounds(450, 650, 400, 30);
        footer.setForeground(Color.DARK_GRAY);
        frame.add(footer);

    // Timer to update date and time every second
    Timer timer= new Timer(1000,e->{
        java.time.LocalDateTime now =java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter=java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime=now.format(formatter);
        dateTimeLabel.setText("Date and Time: "+dateTime);
    });
    timer.start();


    frame.setVisible(true);

    btn4.addActionListener(e -> {
        
        int result = JOptionPane.showConfirmDialog(
            frame,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
       
       
        if (result == JOptionPane.YES_OPTION) {
            
            frame.dispose();
               login.main(new String[]{});
            
        }
    });
   
    btn2.addActionListener(e -> {
        restaurant_reservation.main(new String[]{});
    });
    btn1.addActionListener(e->{
        BookingFrame.main(new String[]{});
    });
    btn3.addActionListener(e->{
        RoomStatus.main(new String[]{});
    });
}

}