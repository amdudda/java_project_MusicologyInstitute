package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    private JTextArea notesTextArea;
    private JButton exitButton;
    private JPanel updateContactRootPanel;
    private Contact selected_contact;

    public UpdateContact(String selContact) {
        selected_contact = new Contact(selContact);

        setContentPane(updateContactRootPanel);
        setTitle("Update Contact");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        populateFormData();
        pack();

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void populateFormData() {
        // populates our form with data
        // text fields first
        contactNameTextField.setText(selected_contact.getContactName());
        businessNameTextField.setText(selected_contact.getBusinessName());
        addressTextArea.setText(selected_contact.getAddress());
        cityTextField.setText(selected_contact.getCity());
        postalCodeTextField.setText(selected_contact.getPostalCode());
        contactPhoneTextField.setText(selected_contact.getContactPhone());
        businessPhoneTextField.setText(selected_contact.getBusinessPhone());
        notesTextArea.setText(selected_contact.getNotes());

        // TODO: drop-down lists - for now just add current values.
        DataValidator.generateCountryComboBox(countryComboBox);
        countryComboBox.setSelectedItem(selected_contact.getCountry());

        stateComboBox.addItem(selected_contact.getState());
        contactTypeComboBox.addItem(selected_contact.getContactType());
    }

    /*private void generateCountryComboBox() {
        ArrayList<String> countryList = DataValidator.getCountries();
        // in reverse order to put USA at top of list.
        countryList.add(0,"Mexico");
        countryList.add(0,"Canada");
        countryList.add(0,"United States");
        for (int i = 0; i < countryList.size(); i++) {
            countryComboBox.addItem(countryList.get(i));
        }
    }*/
}
