package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
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
    private JComboBox locInRmComboBox;
    private JTextField contactIDTextField;
    private JTextField exhibitIDTextField;
    private JTextField cabinetTextField;
    private JTextField shelfTextField;
    private JButton updateDatabaseButton;
    private JButton exitDiscardChangesButton;
    private JRadioButton libraryRadioButton;
    private JPanel locationInfoRootPanel;
    private JComboBox slRoomComboBox;
    private JComboBox exhibitRoomComboBox;

    private int inst_id;
    private String cur_location;
    private Loan loan;
    private OnExhibit onExhibit;
    private StorageLibrary storageLibrary;
    private Instrument my_instrument;

    public LocationInfoForm(Instrument instrument) {
        // updates current location info for selected instrument.
        this.my_instrument = instrument;
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
                // JOptionPane.showMessageDialog(locationInfoRootPanel,"This button doesn't do anything yet.");
                updateInstrumentLocation();
                dispose();
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
            AbstractButton abstractButton = elements.nextElement();
            if (abstractButton.getActionCommand().equals(cur_location)) {
                abstractButton.setSelected(true);
                break;
            }
        }

        DataValidator.generateRoomComboBox(slRoomComboBox);
        slRoomComboBox.setSelectedItem(storageLibrary.getRoom());
        cabinetTextField.setText(storageLibrary.getCabinet());
        if (storageLibrary.getShelf() == 0) {
            shelfTextField.setText("");
        } else {
            shelfTextField.setText("" + storageLibrary.getShelf());
        }

        if (onExhibit.getExhibitID() == 0 ) {
            exhibitIDTextField.setText("");
        } else {
            exhibitIDTextField.setText("" + onExhibit.getExhibitID());
        }
        DataValidator.generateRoomComboBox(exhibitRoomComboBox);
        exhibitRoomComboBox.setSelectedItem(onExhibit.getRoom());
        DataValidator.generateComboBox(locInRmComboBox,DataValidator.LOCATIONS_IN_ROOM);
        locInRmComboBox.setSelectedItem(onExhibit.getLocationInRoom());

        contactIDTextField.setText("" + loan.getContactID());
        startDateTextField.setText(loan.getStartDate().toString());
        endDateTextField.setText(loan.getEndDate().toString());
    }

    private void updateInstrumentLocation() {
        // updates instrument's location with new data
        // what we do varies based on which button is selected
        String selButton = currentLocationButtonGroup.getSelection().getActionCommand();
        // TODO: need to update UpdtInst screen with new location indicator
        if (selButton.equals("On Loan")) {
            loan.setContactID(Integer.parseInt(contactIDTextField.getText()));
            loan.setStartDate(Date.valueOf(startDateTextField.getText()));
            loan.setEndDate(Date.valueOf(endDateTextField.getText()));
            my_instrument.setLocation(DataValidator.LOC_LOAN);
        } else if (selButton.equals("Exhibit")) {
            onExhibit.setExhibitID(Integer.parseInt(exhibitIDTextField.getText()));
            onExhibit.setRoom(exhibitRoomComboBox.getSelectedItem().toString());
            onExhibit.setLocationInRoom(locInRmComboBox.getSelectedItem().toString());
            my_instrument.setLocation(DataValidator.LOC_EXHIBIT);
        } else {
            // we update the Storage table
            storageLibrary.setRoom(slRoomComboBox.getSelectedItem().toString());
            storageLibrary.setCabinet(cabinetTextField.getText());
            storageLibrary.setShelf(Integer.parseInt(shelfTextField.getText()));
            if (selButton.equals("Library")) {
                storageLibrary.setStorageType(DataValidator.LOC_LIBRARY);
                my_instrument.setLocation(DataValidator.LOC_LIBRARY);
            } else {
                // presumably in Storage
                storageLibrary.setStorageType(DataValidator.LOC_STORAGE);
                my_instrument.setLocation(DataValidator.LOC_STORAGE);
            }
        }
    }
}
