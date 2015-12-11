package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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

    public LocationInfoForm(Instrument instrument, UpdateInstrument updateInstrument) {
        // updates current location info for selected instrument.
        my_instrument = instrument;
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
                updateInstrumentLocation(instrument);
                // TODO: need to update UpdtInst screen with new location indicator
                /*
                Instrument instToUpdate = updateInstrument.getSelectedInstrument();
                instToUpdate.setLocationInfo(my_instrument.getLocationInfo());
*/
                String summary = instrument.getLocationInfo().toString();
                System.out.println(summary);
                updateInstrument.setLocationComboBox(instrument.getLocation());
                updateInstrument.setLocationSummaryTextArea(summary);
                // TODO: why oh why does this not update the instrument object on UpdateInstrument?????
                dispose();
            }
        });

        // TODO: Data validation
        startDateTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                validateDate(startDateTextField);
            }
        });


        endDateTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                validateDate(endDateTextField);
            }
        });
        shelfTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!(shelfTextField.getText().equals("") || DataValidator.isInteger(shelfTextField.getText()))) {
                    JOptionPane.showMessageDialog(locationInfoRootPanel,"Please enter a whole number with no decimals.");
                    shelfTextField.grabFocus();
                }
            }
        });
    }

    private void validateDate(JTextField fieldToCheck) {
        if (!DataValidator.isDate(fieldToCheck.getText())) {
            JOptionPane.showMessageDialog(locationInfoRootPanel,"Please enter a date in YYYY-MM-DD format.");
            fieldToCheck.grabFocus();
        }
    }

    private void populateFields() {
        // populate our fields
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
        startDateTextField.setText((loan.getStartDate() == null) ? "" : loan.getStartDate().toString());
        endDateTextField.setText((loan.getEndDate() == null) ? "" : loan.getEndDate().toString());
    }

    private void updateInstrumentLocation(Instrument instToUpdate) {
        // updates instrument's location with new data
        // what we do varies based on which button is selected
        String selButton = currentLocationButtonGroup.getSelection().getActionCommand();

        if (selButton.equals("On Loan")) {
            //Loan locToUpdate = new Loan(instToUpdate.getInstID(),instToUpdate.getAcquiredFrom());
            instToUpdate.setLocationInfo(new Loan(instToUpdate.getInstID(),instToUpdate.getAcquiredFrom()));
            Loan locToUpdate = (Loan) instToUpdate.getLocationInfo();
            locToUpdate.setContactID(Integer.parseInt(contactIDTextField.getText()));
            locToUpdate.setStartDate(Date.valueOf(startDateTextField.getText()));
            locToUpdate.setEndDate(Date.valueOf(endDateTextField.getText()));
            instToUpdate.setLocation(DataValidator.LOC_LOAN);
            instToUpdate.setLocationInfo(loan);
        } else if (selButton.equals("Exhibit")) {
            //OnExhibit locToUpdate = new OnExhibit(instToUpdate.getInstID());
//            instToUpdate.setLocationInfo(new OnExhibit(instToUpdate.getInstID()));
//            OnExhibit locToUpdate = (OnExhibit) instToUpdate.getLocationInfo();
            // System.out.println("Setting room to " + exhibitRoomComboBox.getSelectedItem().toString());
            OnExhibit locToUpdate = (OnExhibit) instToUpdate.getLocationInfo();
            locToUpdate.setRoom(exhibitRoomComboBox.getSelectedItem().toString());
            locToUpdate.setLocationInRoom(locInRmComboBox.getSelectedItem().toString());
            instToUpdate.setLocation(DataValidator.LOC_EXHIBIT);
            instToUpdate.setLocationInfo(locToUpdate);
        } else {
            // we update the Storage table
            //StorageLibrary locToUpdate = new StorageLibrary(instToUpdate.getInstID());
            instToUpdate.setLocationInfo(new StorageLibrary(instToUpdate.getInstID()));
            StorageLibrary locToUpdate = (StorageLibrary) instToUpdate.getLocationInfo();
            locToUpdate.setRoom(slRoomComboBox.getSelectedItem().toString());
            locToUpdate.setCabinet(cabinetTextField.getText());
            locToUpdate.setShelf(Integer.parseInt(shelfTextField.getText()));
            if (selButton.equals("Library")) {
                locToUpdate.setStorageType(DataValidator.LOC_LIBRARY);
                instToUpdate.setLocation(DataValidator.LOC_LIBRARY);
            } else {
                // presumably in Storage
                locToUpdate.setStorageType(DataValidator.LOC_STORAGE);
                instToUpdate.setLocation(DataValidator.LOC_STORAGE);
            }
            instToUpdate.setLocationInfo(storageLibrary);
        }
    }

}
