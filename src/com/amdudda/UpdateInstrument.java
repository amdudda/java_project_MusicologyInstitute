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
    private JLabel instrumentIDTextField;
    private JTextField instrNameTextField;
    private JTextField regionTextField;
    private JTextField acquiredDateTextField;
    private JTextField acquiredFromTextField;
    private JComboBox locationComboBox;
    private JTextField countryTextField;
    private JCheckBox checkIfYesCheckBox;
    private JTextField heightTextField;
    private JTextField widthTextField;
    private JTextField depthTextField;
    private JTextArea descriptionTextArea;
    private JComboBox classificationComboBox1;
    private JTextField subtypeTextField;
    private JTextField cultureTextField;
    private JComboBox tuningTypeComboBox;
    private JTextField lowNoteTextField;
    private JTextField HighNoteTextField;
    private JButton exitButton;
    private PreparedStatement instrumentDataStatement;
    private ResultSet instrumentData;

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
        Instrument selectedInstrument = new Instrument(pkToUse);

        // then populate our fields with the data
        // TODO: create a method call to keep the main body human-readable.
        instrIDtextField.setText("" + selectedInstrument.getInstID());
        instrNameTextField.setText(selectedInstrument.getInstName());

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


}
