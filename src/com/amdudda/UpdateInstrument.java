package com.amdudda;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by amdudda on 12/2/15.
 */
public class UpdateInstrument extends JFrame {
    private JPanel updateInstrumentRootPanel;
    private JTextField instrIDtextField;
    private JTextField instrNameTextField;
    private JTextField regionTextField;
    private JTextField acquiredDateTextField;
    private JTextField acquiredFromTextField;
    private JComboBox locationComboBox;
    private JCheckBox isALoanCheckBox;
    private JTextField heightTextField;
    private JTextField widthTextField;
    private JTextField depthTextField;
    private JTextArea descriptionTextArea;
    private JComboBox classificationComboBox1;
    private JTextField subtypeTextField;
    private JTextField cultureTextField;
    private JComboBox tuningTypeComboBox;
    private JTextField lowNoteTextField;
    private JTextField highNoteTextField;
    private JButton exitButton;
    private JComboBox countryComboBox;
    private JButton updateDatabaseButton;
    private Instrument selectedInstrument;

    public UpdateInstrument(String pkToUse) {// fetch the data for the instrument
        // instantiate an Instrument object and read its attributes.
        selectedInstrument = new Instrument(pkToUse);

        setContentPane(updateInstrumentRootPanel);
        setTitle("Update Database Record");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // then populate our fields with the data, IF we have data to use.
        populateFormFields();
        // then pack it into a window that fits all the data.
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
                if (pkToUse != null) {
                    // update the record
                    updateInstrument();
                } else {
                    // insert the record.
                    addInstrument();
                }
            }
        });

        heightTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String fieldValue = heightTextField.getText().toString();
                if (!fieldValue.equals("") && !DataValidator.isDouble(fieldValue)) {
                    JOptionPane.showMessageDialog(updateInstrumentRootPanel,"Please enter a number.");
                }
            }
        });

        widthTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String fieldValue = widthTextField.getText().toString();
                if (!fieldValue.equals("") && !DataValidator.isDouble(fieldValue)) {
                    JOptionPane.showMessageDialog(updateInstrumentRootPanel,"Please enter a number.");
                }
            }
        });

        depthTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String fieldValue = depthTextField.getText().toString();
                if (!fieldValue.equals("") && !DataValidator.isDouble(fieldValue)) {
                    JOptionPane.showMessageDialog(updateInstrumentRootPanel,"Please enter a number.");
                }
            }
        });
    }

    private void populateFormFields() {
        // first, let's setup the text fields
        instrIDtextField.setText("" + selectedInstrument.getInstID());
        instrNameTextField.setText(selectedInstrument.getInstName());
        acquiredDateTextField.setText(selectedInstrument.getAcquiredDate().toString());
        // TODO: acquiredFrom info should be a human-readable name, not an ID number
        acquiredFromTextField.setText(selectedInstrument.getAcquiredFrom());
        regionTextField.setText(selectedInstrument.getRegion());
        heightTextField.setText("" + selectedInstrument.getHeight());
        widthTextField.setText("" + selectedInstrument.getWidth());
        depthTextField.setText("" + selectedInstrument.getDepth());
        // TODO: should subtype be some sort of combobox? if so, needs listener to listen for changes to classification.
        subtypeTextField.setText(selectedInstrument.getSubtype());
        cultureTextField.setText(selectedInstrument.getCulture());
        lowNoteTextField.setText(selectedInstrument.getLowNote());
        highNoteTextField.setText(selectedInstrument.getHighNote());
        descriptionTextArea.setText(selectedInstrument.getDescription());

        // ooh!  tickybox!
        isALoanCheckBox.setSelected(selectedInstrument.isALoan());


        // populate comboboxes
        DataValidator.generateClassificationComboBox(classificationComboBox1);
        classificationComboBox1.setSelectedItem(selectedInstrument.getInstType());

        DataValidator.generateCountryComboBox(countryComboBox);
        countryComboBox.setSelectedItem(selectedInstrument.getCountry());

        DataValidator.generateLocationComboBox(locationComboBox);
        locationComboBox.setSelectedItem(selectedInstrument.getLocation());

        DataValidator.generateTuningComboBox(tuningTypeComboBox);
        tuningTypeComboBox.setSelectedItem(selectedInstrument.getTuning());
    }


    public void updateInstrument() {
        // a method that will be called by the update button
        String preppedStatemt = "UPDATE " + Instrument.INSTRUMENT_TABLE_NAME + " SET " +
                Instrument.INSTNAME + "= ?, " + // 1
                Instrument.INSTTYPE + "= ?, " +
                Instrument.SUBTYPE + "= ?, " +
                Instrument.ACQUIREDDATE + "= ?, " +
                Instrument.ACQUIREDFROM + "= ?, " + // 5
                Instrument.LOCATION + "= ?, " +
                Instrument.HEIGHT + "= ?, " +
                Instrument.WIDTH + "= ?, " +
                Instrument.DEPTH + "= ?, " +
                Instrument.REGION + "= ?, " + // 10
                Instrument.CULTURE + "= ?, " +
                Instrument.TUNING + "= ?, " +
                Instrument.LOWNOTE + "= ?, " +
                Instrument.HIGHNOTE + "= ?, " +
                Instrument.DESCRIPTION + "= ?, " + // 15
                Instrument.ISALOAN + "= ? " +
                "WHERE " + Instrument.INSTID + " = ? ";
        try {
            PreparedStatement updtInst = Database.conn.prepareStatement(preppedStatemt);
            updtInst.setString(1,instrNameTextField.getText());
            updtInst.setString(2,classificationComboBox1.getSelectedItem().toString());
            updtInst.setString(3,subtypeTextField.getText());
            updtInst.setDate(4,java.sql.Date.valueOf(acquiredDateTextField.getText()));
            updtInst.setInt(5,Integer.parseInt(acquiredFromTextField.getText()));
            updtInst.setString(6,locationComboBox.getSelectedItem().toString());
            updtInst.setDouble(7,Double.parseDouble(heightTextField.getText()));
            updtInst.setDouble(8,Double.parseDouble(widthTextField.getText()));
            updtInst.setDouble(9,Double.parseDouble(depthTextField.getText()));
            updtInst.setString(10,regionTextField.getText());
            updtInst.setString(11,cultureTextField.getText());
            updtInst.setString(12,tuningTypeComboBox.getSelectedItem().toString());
            updtInst.setString(13,lowNoteTextField.getText());
            updtInst.setString(14,highNoteTextField.getText());
            updtInst.setString(15,descriptionTextArea.getText());
            updtInst.setBoolean(16,isALoanCheckBox.isSelected());
            // don't forget to update only the selected instrument
            updtInst.setInt(17,selectedInstrument.getInstID());
            updtInst.executeUpdate();
            updtInst.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to update database record.\n" + sqle);
        }
    }

    // Method to insert a new instrument.
    public void addInstrument() {
        // a method that will be called by the add button
        // I considered making this a method in Instrument, but didn't want to create
        // listeners for every single field to update Instrument object with data.
        String prSt = "INSERT INTO " + Instrument.INSTRUMENT_TABLE_NAME + " (" +
                Instrument.INSTNAME + ", " +
                Instrument.INSTTYPE + ", " +
                Instrument.SUBTYPE + ", " +
                Instrument.ACQUIREDDATE + ", " +
                Instrument.ACQUIREDFROM + ", " +
                Instrument.LOCATION + ", " +
                Instrument.HEIGHT + ", ";
        // apparently IntelliJ sql parser doesn't like super-long strings, so I've broken it up here.
        // TODO: insert URL from Clara that references this issue.
        prSt+=  Instrument.WIDTH + ", " +
                Instrument.DEPTH + ", " +
                Instrument.REGION + ", " +
                Instrument.CULTURE + ", " +
                Instrument.TUNING + ", " +
                Instrument.LOWNOTE + ", " +
                Instrument.HIGHNOTE + ", " +
                Instrument.DESCRIPTION + ", " +
                Instrument.ISALOAN + ") " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        System.out.println(prSt);
        // TODO: Why does the below not work?
        try {
            PreparedStatement addInst = Database.conn.prepareStatement(prSt);
            addInst.setString(1,instrNameTextField.getText());
            addInst.setString(2,classificationComboBox1.getSelectedItem().toString());
            addInst.setString(3,subtypeTextField.getText());
            addInst.setDate(4,java.sql.Date.valueOf(acquiredDateTextField.getText()));
            addInst.setInt(5,Integer.parseInt(acquiredFromTextField.getText()));
            addInst.setString(6,locationComboBox.getSelectedItem().toString());
            addInst.setDouble(7,Double.parseDouble(heightTextField.getText()));
            addInst.setDouble(8,Double.parseDouble(widthTextField.getText()));
            addInst.setDouble(9,Double.parseDouble(depthTextField.getText()));
            addInst.setString(10,regionTextField.getText());
            addInst.setString(11,cultureTextField.getText());
            addInst.setString(12,tuningTypeComboBox.getSelectedItem().toString());
            addInst.setString(13,lowNoteTextField.getText());
            addInst.setString(14,highNoteTextField.getText());
            addInst.setString(15,descriptionTextArea.getText());
            addInst.setBoolean(16,isALoanCheckBox.isSelected());
            addInst.executeUpdate();
            // don't forget to close my statement just in case it's still lingering...
            if (addInst != null) addInst.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to update database record.\n" + sqle);
        }
    }

}
