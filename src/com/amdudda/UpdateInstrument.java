package com.amdudda;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            // TODO: finish rest of data fields.
        } catch (SQLException sqle) {
            System.out.println("Unable to update database record.\n" + sqle);
        }
    }
}
