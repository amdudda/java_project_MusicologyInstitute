package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    private JButton updateDatabaseButton;
    private JLabel formDescriptionLabel;
    private Contact selected_contact;

    public UpdateContact(String selContact) {
        selected_contact = new Contact(selContact);

        setContentPane(updateContactRootPanel);
        if (selContact == null) {
            setTitle("Add Contact");
            formDescriptionLabel.setText("Add Contact");
        }
        else { setTitle("Update Contact"); }
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

        updateDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selContact == null) { insertContact(); } else { updateContact(); }
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

        // drop-down lists
        DataValidator.generateCountryComboBox(countryComboBox);
        DataValidator.generateStateComboBox(stateComboBox);
        DataValidator.generateComboBox(contactTypeComboBox, DataValidator.CONTACT_TYPES);

        if (selected_contact.getContactID() == 0) {
            countryComboBox.setSelectedItem(Contact.DEFAULT_COUNTRY);
            stateComboBox.setSelectedItem(Contact.DEFAULT_STATE);
            contactTypeComboBox.setSelectedItem(Contact.DEFAULT_CONTACT_TYPE);
        } else {
            countryComboBox.setSelectedItem(selected_contact.getCountry());
            stateComboBox.setSelectedItem(selected_contact.getState());
            contactTypeComboBox.setSelectedItem(selected_contact.getContactType());
        }
    }

    private void updateContact() {
        String sqlToUse = "UPDATE " + Contact.CONTACT_TABLE_NAME + " SET " +
                Contact.CONTACTNAME + "= ?, " +
                Contact.BUSINESSNAME + "= ?, " +
                Contact.ADDRESS + "= ?, " +
                Contact.CITY + "= ?, " +
                Contact.POSTALCODE + "= ?, " +
                Contact.CONTACTPHONE + "= ?, " +
                Contact.BUSINESSPHONE + "= ?, " +
                Contact.NOTES + "= ?, " +
                Contact.COUNTRY + "= ?, " +
                Contact.STATE + "= ?, " +
                Contact.CONTACTTYPE + "= ? " +
                "WHERE " + Contact.CONTACTID + " = ?";
        PreparedStatement ps = null;
        try {
            ps = Database.conn.prepareStatement(sqlToUse);

            int i = 1;
            ps.setString(i,contactNameTextField.getText());
            ps.setString(++i,businessNameTextField.getText());
            ps.setString(++i,addressTextArea.getText());
            ps.setString(++i,cityTextField.getText());
            ps.setString(++i,postalCodeTextField.getText());
            ps.setString(++i,contactPhoneTextField.getText());
            ps.setString(++i,businessPhoneTextField.getText());
            ps.setString(++i,notesTextArea.getText());
            ps.setString(++i,countryComboBox.getSelectedItem().toString());
            ps.setString(++i,stateComboBox.getSelectedItem().toString());
            ps.setString(++i,contactTypeComboBox.getSelectedItem().toString());
            ps.setInt(++i,selected_contact.getContactID());
            ps.executeUpdate();

            ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to update database.\n" + sqle);
        }
    }

    private void insertContact() {
        // add a new contact if that's what we're actually doing.
        String sqlToUse = "INSERT INTO " + Contact.CONTACT_TABLE_NAME + " (" +
                Contact.CONTACTNAME + ", " +
                Contact.BUSINESSNAME + ", " +
                Contact.ADDRESS + ", " +
                Contact.CITY + ", " +
                Contact.POSTALCODE +  ", " + // 5
                Contact.CONTACTPHONE + ", " +
                Contact.BUSINESSPHONE + ", " +
                Contact.NOTES + ", " +
                Contact.COUNTRY + ", " +
                Contact.STATE +  ", " + // 10
                Contact.CONTACTTYPE +
                ") VALUES (" +
                "?,?,?,?," +
                "?,?,?,?," +
                "?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = Database.conn.prepareStatement(sqlToUse);
            int i = 1;
            ps.setString(i,contactNameTextField.getText());
            ps.setString(++i,businessNameTextField.getText());
            ps.setString(++i,addressTextArea.getText());
            ps.setString(++i,cityTextField.getText());
            ps.setString(++i,postalCodeTextField.getText());
            ps.setString(++i,contactPhoneTextField.getText());
            ps.setString(++i,businessPhoneTextField.getText());
            ps.setString(++i,notesTextArea.getText());
            ps.setString(++i,countryComboBox.getSelectedItem().toString());
            ps.setString(++i,stateComboBox.getSelectedItem().toString());
            ps.setString(++i,contactTypeComboBox.getSelectedItem().toString());
            ps.executeUpdate();
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to insert new contact.\n" + sqle);
        }
    }
}
