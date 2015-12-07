package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by amdudda on 12/6/15.
 */
public class UpdateContact extends JFrame {
    private JTextField contactNameTextField;
    private JTextField businessNameTextField;
    private JTextArea addressTextArea;
    private JTextField cityTextField;
    private JTextField postalCodeTextField;
    private JComboBox stateComboBox;
    private JComboBox countryComboBox;
    private JTextField contactPhoneTextField;
    private JTextField businessPhoneTextField;
    private JComboBox contactTypeComboBox;
    private JTextArea NotesTextArea;
    private JButton exitButton;
    private JPanel updateContactRootPanel;
    private Contact selected_contact;

    public UpdateContact(String selContact) {
        setContentPane(updateContactRootPanel);
        setTitle("Update Contact");
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        selected_contact = new Contact(selContact);

        // todo: Populate our fields.


        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
