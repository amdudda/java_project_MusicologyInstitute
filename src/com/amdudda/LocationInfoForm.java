package com.amdudda;

import javafx.scene.control.RadioButton;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by amdudda on 12/10/15.
 */
public class LocationInfoForm extends ContactManager { // extends JFrame {
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

    public LocationInfoForm(Instrument instrument, UpdateInstrument updateInstrument) {
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
        this.loan = new Loan(this.inst_id);
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
                // Update UpdtInst screen with new location indicator.
                // I can't tweak the values here, because they get thrown away when this window closes.
                updateInstrument.setLocationInfo(getLocAttribs());
                dispose();
            }
        });

        // Data validation
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
        cabinetTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (cabinetTextField.getText().length() > 1) {
                    JOptionPane.showMessageDialog(locationInfoRootPanel,"Cabinet must only one character long.");
                    cabinetTextField.grabFocus();
                }
            }
        });
        contactIDTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                SelectContactScreen scs = new SelectContactScreen(Contact.getBrowsingData(),LocationInfoForm.this);
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
        ArrayList<JRadioButton> cLBGbuttons = new ArrayList<>();
        cLBGbuttons.add(onExhibitRadioButton);
        cLBGbuttons.add(libraryRadioButton);
        cLBGbuttons.add(storageRadioButton);
        cLBGbuttons.add(onLoanRadioButton);
        for (int b = 0; b < cLBGbuttons.size(); b++) {
            currentLocationButtonGroup.add(cLBGbuttons.get(b));
            JRadioButton cur_button = cLBGbuttons.get(b);
            if (cur_button.getActionCommand().equals(cur_location)) {
                currentLocationButtonGroup.setSelected(cur_button.getModel(),true);
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

    private HashMap<String,String> getLocAttribs() {
        String selButton = currentLocationButtonGroup.getSelection().getActionCommand();
        HashMap<String,String> locAttribs = new HashMap<String,String>();

        if (selButton.equals("On Loan")) {
            locAttribs.put("Type",DataValidator.LOC_LOAN);
            locAttribs.put(Loan.CONTACT_ID,contactIDTextField.getText());
            locAttribs.put(Loan.START_DATE,startDateTextField.getText());
            locAttribs.put(Loan.END_DATE,endDateTextField.getText());
        } else if (selButton.equals("Exhibit")) {
            locAttribs.put("Type",DataValidator.LOC_EXHIBIT);
            locAttribs.put(OnExhibit.EXHIBIT_ID,exhibitIDTextField.getText());
            locAttribs.put(onExhibit.ROOM,exhibitRoomComboBox.getSelectedItem().toString());
            locAttribs.put(onExhibit.LOCATION_IN_ROOM,locInRmComboBox.getSelectedItem().toString());
        } else {
            locAttribs.put("Type",selButton);
            locAttribs.put(StorageLibrary.ROOM,slRoomComboBox.getSelectedItem().toString());
            locAttribs.put(StorageLibrary.CABINET,cabinetTextField.getText());
            locAttribs.put(StorageLibrary.SHELF,shelfTextField.getText());
            }
        return locAttribs;
    }

    public void setAcquiredFromTextField(String id) {
        this.contactIDTextField.setText(id);
    }
}
