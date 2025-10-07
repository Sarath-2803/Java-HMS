package frames;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;
import java.util.regex.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Connection;
import util.*;
import model.Payment;
import model.RoomBooking;
import dao.RoomBookingDAO;
import dao.PaymentDAO;

public class PaymentFrame extends JFrame {
    private JLabel billAmount;
    private JComboBox<String> methodBox;
    private JPanel methodPanel;
    private JTextField cardNumber, expiryDate;
    private JPasswordField cvv;
    private JLabel qrLabel;
    private static final Pattern CARD_PATTERN = Pattern.compile("^[0-9]{16}$");
    private static final Pattern CVV_PATTERN = Pattern.compile("^[0-9]{3}$");
    private static final DateTimeFormatter EXP_FORMAT = DateTimeFormatter.ofPattern("MM/yy");

    private long user;
    private long bid;
    private double amount;
    private Payment payment;

    public PaymentFrame(long user, long bid, double amount) {
        
        this.user = user;
        this.bid = bid;
        this.amount = amount;
        this.payment = new Payment(user, bid, amount, false);

        setTitle("Payment");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        ImageIcon icon = new ImageIcon("assets/HMSICON.png");
        setIconImage(icon.getImage());

        setLayout(new BorderLayout());

        billAmount = new JLabel("Amount to Pay: ₹" + amount, SwingConstants.CENTER);
        billAmount.setFont(new Font("Arial", Font.BOLD, 24));
        add(billAmount, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        methodBox = new JComboBox<>(new String[]{"Cash", "Card", "UPI"});
        methodBox.addActionListener(e -> updateMethodPanel());
        centerPanel.add(methodBox, BorderLayout.NORTH);
        methodPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        methodPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(methodPanel);
        centerPanel.add(wrapper, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JButton btnConfirm = new JButton("Confirm Payment");
        JButton btnCancel = new JButton("Cancel");
        btnConfirm.addActionListener(e -> confirmPayment());
        btnCancel.addActionListener(e -> cancelPayment());
        add(btnConfirm, BorderLayout.SOUTH);

        updateMethodPanel();
    }

    private void updateMethodPanel() {
        methodPanel.removeAll();
        qrLabel = null;

        String method = (String) methodBox.getSelectedItem();

        if ("Cash".equals(method)) {
            methodPanel.add(new JLabel("Collect Payment from Guest.", SwingConstants.CENTER));
        } 
        else if ("Card".equals(method)) {
            methodPanel.setLayout(new GridLayout(3, 2, 10, 10));
            methodPanel.add(new JLabel("Card Number:"));
            cardNumber = new JTextField(16);
            methodPanel.add(cardNumber);

            methodPanel.add(new JLabel("Expiry Date (MM/YY):"));
            expiryDate = new JTextField("MM/YY");
            methodPanel.add(expiryDate);

            methodPanel.add(new JLabel("CVV:"));
            cvv = new JPasswordField(3);
            methodPanel.add(cvv);
        } 
        else if ("UPI".equals(method)) {
            qrLabel = new JLabel();
            qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
            qrLabel.setIcon(new ImageIcon(generateFakeQR()));
            methodPanel.add(new JLabel("Scan this QR to pay:", SwingConstants.CENTER));
            methodPanel.add(qrLabel);
        }

        methodPanel.revalidate();
        methodPanel.repaint();
    }

    private void confirmPayment() {
        String method = (String) methodBox.getSelectedItem();
        if ("Card".equals(method)) {
            String cardNo = cardNumber.getText().trim();
            String expiry = expiryDate.getText().trim();
            char[] userInputcvv = cvv.getPassword(); 
            String inputcvv = new String(userInputcvv); 
            StringBuilder errorMsg = new StringBuilder();
            if (cardNo.isEmpty()) errorMsg.append("- Card Number is required.\n");
            if (expiry.isEmpty()) errorMsg.append("- Expiry Date is required.\n");
            if (inputcvv.isEmpty()) errorMsg.append("- CVV is required.\n");
            if(!CARD_PATTERN.matcher(cardNo).matches())
                errorMsg.append("- Invalid Card Number.\n");
            if(!CVV_PATTERN.matcher(inputcvv).matches())
                errorMsg.append("- Invalid CVV format.\n");
            try {
                    YearMonth expDate = YearMonth.parse(expiry, EXP_FORMAT);
                    YearMonth now = YearMonth.now();
                    if(expDate.isBefore(now))
                        errorMsg.append("- Card is expired.\n");
                } catch (DateTimeParseException e) {
                    errorMsg.append("- Invalid Expiry Date format.\n");
            }
            if(errorMsg.length() == 0) {
                int sum = 0;
                boolean alternate = false;
                for (int i = cardNo.length() - 1; i >= 0; i--) {
                    int n = Integer.parseInt(cardNo.substring(i, i + 1));
                    if (alternate) {
                        n *= 2;
                        if (n > 9) n -= 9;
                    }
                    sum += n;
                    alternate = !alternate;
                }
                if (sum % 10 != 0)
                    errorMsg.append("- Invalid Card Number.\n");
            }
            
            if (errorMsg.length() > 0) {
                centeringDialog(errorMsg.toString(), "Missing or Invalid Information");
                errorMsg = new StringBuilder();
            } else {
                PaymentDAO pd = new PaymentDAO();
                RoomBookingDAO bd = new RoomBookingDAO();
                try(Connection conn = Utils.getConnection()){
                    Payment newpayement = pd.save(payment, conn);
                    if (newpayement == null) {
                        RoomBooking bkng = bd.findById(bid, conn);
                        //bkng.setStatus(false);
                        bd.update(bkng, conn);
                        centeringDialog("Payment Failed", "Error");
                        throw new Exception("Error saving payment...");
                    }
                    this.payment.setStatus(true);
                    RoomBooking bkng = bd.findById(this.payment.getBookingId(), conn);
                    bkng.setStatus(true);
                    bd.update(bkng, conn);
                    centeringDialog("Payment Successful via " + method, "Success");
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
                dispose();
                new BookingFrame().setVisible(true);
            }
        }
        else {
               PaymentDAO pd = new PaymentDAO();
                RoomBookingDAO bd = new RoomBookingDAO();
                Payment payment = new Payment(user, bid, amount, true);
                try(Connection conn = Utils.getConnection()){
                    Payment newpayement = pd.save(payment, conn);
                    if (newpayement == null) {
                        RoomBooking bkng = bd.findById(bid, conn);
                        //bkng.setStatus(false);
                        //bd.update(bkng, conn);
                        centeringDialog("Payment Failed", "Error");
                        throw new Exception("Error saving payment...");
                    }
                    centeringDialog("Payment Successful via " + method, "Success");
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
                dispose();
                new BookingFrame().setVisible(true);
            }
    }
    private void cancelPayment() {
        dispose();
        new BookingFrame().setVisible(true);
    }

    private Image generateFakeQR() {
        int size = 200;
        BufferedImage qr = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = qr.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);

        g.setColor(Color.BLACK);
        Random rand = new Random();
        for (int i = 0; i < 500; i++) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            qr.setRGB(x, y, Color.BLACK.getRGB());
        }

        g.dispose();
        return qr;
    }
    private void centeringDialog(String message, String title) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaymentFrame frame = new PaymentFrame(1, 1, 1000);
            frame.setVisible(true);
        });
    }

}
