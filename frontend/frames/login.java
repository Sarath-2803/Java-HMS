import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

 
// The hotel system uses a passkey which identifies each hotel and access its services
public class login{
    public static void main(String args [])
    {
        //The main login frame
        JFrame frame =new JFrame("Login");
        ImageIcon icon = new ImageIcon("HMSICON.png");
        frame.setIconImage(icon.getImage());

        JPanel Topbar=new JPanel();
        Topbar.setBounds(0,0,900,50);
        Topbar.setBackground(new Color(25, 25, 112));
        frame.add(Topbar);
        

        JLabel title=new JLabel("Hotel Management System");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(22.0f));
        Topbar.add(title);

        JPanel panel=new JPanel();
        panel.setLayout(null);
        //The login label
        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(loginLabel.getFont().deriveFont(22.0f)); // To change the font size of the login label
        loginLabel.setBounds(230, 20, 100, 50); // Set position and size of login label
       

        JLabel passwordLabel = new JLabel("Passkey:");
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(18.0f)); // To change the font size of the password label
        passwordLabel.setBounds(40, 120, 200, 30); // Adjusted position for better centering
        passwordLabel.setForeground(Color.BLUE); // Set text color to blue
       
        panel.add(passwordLabel);
        
        panel.add(loginLabel);
        
        // Center the panel in the frame
      
        
        panel.setBounds(180,200, 500, 200);
        panel.setBackground(Color.LIGHT_GRAY);
       
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPasswordField passwordInput = new JPasswordField();
        passwordInput.setBounds(130, 120, 200, 30); // Adjusted to align with label
        panel.add(passwordInput);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(340, 120, 100, 30); // Adjusted to align with input field
        loginButton.setForeground(Color.red);
        loginButton.setFocusable(false);
        panel.add(loginButton);
        
        JLabel footer= new JLabel("@2025 All rights reserved | Hotel Management System");
        footer.setBounds(250, 600, 400, 30);
        footer.setForeground(Color.WHITE);
        frame.add(footer);

        frame.setLayout(null);
        loginButton.addActionListener(e->{
            String passkey="admin123";
            char[] userInput=passwordInput.getPassword(); 
            
            String userInputStr = new String(userInput); 
            if(userInputStr.equals(passkey)){
                frame.dispose();
                dashboard.main(new String[]{});
            }
            else{
                passwordInput.setText("");
               int result = JOptionPane.showConfirmDialog(
            frame,
            "Incorrect Passkey. Do you want to try again?",
            "Login Failed",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
       
       
        if (result == JOptionPane.YES_OPTION) {
            
            frame.dispose();            
               login.main(new String[]{});
            
        }
        else{
            frame.dispose();
        }
            }
        

       });
    
        
        frame.setSize(900, 700);
        frame.setResizable(false);
         frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);        
        frame.add(panel);              
        frame.getContentPane().setBackground(new Color(66,66,183)); 
    }

    
}