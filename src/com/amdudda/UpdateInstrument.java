package com.amdudda;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public UpdateInstrument(String pkToUse) {
        setContentPane(updateInstrumentRootPanel);
        setTitle("Update Database Record");
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // fetch the data for the instrument
        // instantiate an Instrument object and read its attributes.
        selectedInstrument = new Instrument(pkToUse);

        // then populate our fields with the data, IF we have data to use.
        if (pkToUse != null) populateFormFields();

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
                    return;
                }
            }
        });

// TODO: why isn't this working?
        heightTextField.addFocusListener(new FocusAdapter() {
            private String fieldValue = heightTextField.getText().toString();
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!DataValidator.isDouble(fieldValue)) {
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


        // TODO: comboboxes - for now just add the current value of the field for the instrument.
        classificationComboBox1.addItem(selectedInstrument.getInstType());
        countryComboBox.addItem(selectedInstrument.getCountry());
        locationComboBox.addItem(selectedInstrument.getLocation());
        tuningTypeComboBox.addItem(selectedInstrument.getTuning());
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
        } catch (SQLException sqle) {
            System.out.println("Unable to update database record.\n" + sqle);
        }
    }

    // TODO: method to insert a new instrument.
}
