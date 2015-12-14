package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;

/**
 * Created by amdudda on 12/13/15.
 */
public class Login extends JFrame {
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton cancelButton;
    private JButton loginButton;
    private JPanel loginRootPanel;
    private JTextArea pleaseEnterYourUsernameTextArea;

    public Login() {
        // TODO: Make this more secure.
        // This is security theater only; all this stuff dances around in plaintext!
        setContentPane(loginRootPanel);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login Screen");
        setVisible(true);
        setSize(350,200);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    Database.username = usernameTextField.getText();
                    for (char c : passwordField.getPassword()) {
                        Database.pwd += Character.toString(c);
                    }
                    //System.out.println(Database.pwd);
                    if (!Database.checkCredentials()) {
                        pleaseEnterYourUsernameTextArea.setText("Invalid username or password.  Please try again.");
                        Database.pwd = "";
                    } else {
                        startProgram();
                        dispose();
                    }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }

    private static void startProgram() {

        Database.setupDatabase();

        Database.openConnStatement();  // have to reopen connection because
        // (a) dbsetup closes it, and (b) if dbFlag.txt set to true, it never actually fires up.
        MainMenuGUI mMG = new MainMenuGUI();
    }
}
