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
    private PreparedStatement instrumentDataStatement;
    private ResultSet instrumentData;
    private Instrument selectedInstrument;

    public UpdateInstrument(String pkToUse) {
        /*try {
            instrumentDataStatement = Database.conn.createStatement();
        } catch (SQLException sqle) {
            System.out.println("Unable to configure instrumentDataStatement");
        }*/

        setContentPane(updateInstrumentRootPanel);
        setTitle("Update Database Record");
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // fetch the data for the instrument
        // instantiate an Instrument object and read its attributes.
        selectedInstrument = new Instrument(pkToUse);

        // then populate our fields with the data
        // TODO: create a method call to keep the main body human-readable.
        /*instrIDtextField.setText("" + selectedInstrument.getInstID());
        instrNameTextField.setText(selectedInstrument.getInstName());*/
        populateFormFields();

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (instrumentData != null) instrumentData.close();
                    if (instrumentDataStatement != null) instrumentDataStatement.close();
                } catch (SQLException sqle) {
                    System.out.println("Unable to close resultset or statement.");
                }
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
}
