package com.amdudda;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        setContentPane(loginRootPanel);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login Screen");
        //setModal(true);
        setVisible(true);
        setSize(350,200);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Database.USER = usernameTextField.getText();
                for (char c: passwordField.getPassword()) {
                    Database.PASS += Character.toString(c);
                }
                //System.out.println(Database.PASS);
                startProgram();
                dispose();
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
