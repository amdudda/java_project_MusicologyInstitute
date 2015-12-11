package com.amdudda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by amdudda on 12/10/15.
 */
public class LocationInfoForm extends JFrame {
    private JRadioButton storageRadioButton;
    private JRadioButton onExhibitRadioButton;
    private JRadioButton onLoanRadioButton;
    private ButtonGroup currentLocationButtonGroup = new ButtonGroup();
    private JTextField startDateTextField;
    private JTextField endDateTextField;
    private JComboBox LocInRmComboBox;
    private JTextField contactIDTextField;
    private JTextField exhibitRoomTextField;
    private JTextField exhibitIDTextField;
    private JTextField cabinetTextField;
    private JTextField shelfTextField;
    private JButton updateDatabaseButton;
    private JButton exitDiscardChangesButton;
    private JTextField storageRoomTextField;
    private JRadioButton libraryRadioButton;
    private JPanel locationInfoRootPanel;

    private int inst_id;
    private String cur_location;
    private Loan loan;
    private OnExhibit onExhibit;
    private StorageLibrary storageLibrary;

    public LocationInfoForm(Instrument instrument) {
        // updates current location info for selected instrument.
        this.inst_id = instrument.getInstID();
        this.cur_location = instrument.getLocation();

        // set up window
        setContentPane(locationInfoRootPanel);
        setTitle("Select or Update Instrument Location");
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // fetch existing data
        this.loan = new Loan(this.inst_id,instrument.getAcquiredFrom());
        this.onExhibit = new OnExhibit(this.inst_id);
        this.storageLibrary = new StorageLibrary(this.inst_id);

        populateFields();

        // listeners for the form
        exitDiscardChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        updateDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(locationInfoRootPanel,"This button doesn't do anything yet.");
            }
        });

        // TODO: Data validation
    }

    private void populateFields() {
        // populate our fields
        // TODO: need to set up radiobuttons
        currentLocationButtonGroup.add(onExhibitRadioButton);
        currentLocationButtonGroup.add(libraryRadioButton);
        currentLocationButtonGroup.add(storageRadioButton);
        currentLocationButtonGroup.add(onLoanRadioButton);
        // got this idea from http://www.java2s.com/Tutorial/Java/0240__Swing/Settingselectedbuttoninabuttongroup.htm
        Enumeration<AbstractButton> elements = currentLocationButtonGroup.getElements();
        while (elements.hasMoreElements()) {
            if (elements.nextElement().getActionCommand().equals(cur_location)) {
                elements.nextElement().setSelected(true);
                break;
            }
        }


        storageRoomTextField.setText(storageLibrary.getRoom());
        cabinetTextField.setText(storageLibrary.getCabinet());
        shelfTextField.setText(""+storageLibrary.getShelf());

        exhibitIDTextField.setText(""+onExhibit.getInstID());
        exhibitRoomTextField.setText(onExhibit.getRoom());
        DataValidator.generateComboBox(LocInRmComboBox,DataValidator.LOCATIONS_IN_ROOM);

        contactIDTextField.setText("" + loan.getContactID());
        startDateTextField.setText(loan.getStartDate().toString());
        endDateTextField.setText(loan.getEndDate().toString());
    }
}
