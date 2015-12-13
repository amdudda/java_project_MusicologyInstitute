package com.amdudda;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Date;

/**
 * Created by amdudda on 12/12/15.
 */
public class UpdateExhibit extends JFrame {
    private JTextField exhibitIDTextField;
    private JTextField exhibitNameTextField;
    private JTextField startDateTextField;
    private JTextField endDateTextField;
    private JComboBox roomComboBox;
    private JButton saveChangesButton;
    private JButton exitDiscardChangesButton;
    private JPanel updateExhibitRootPanel;
    private Exhibit my_exhibit;

    public UpdateExhibit(String exID, BrowseListDataModel tableModel) {
        my_exhibit = new Exhibit(exID);
        setContentPane(updateExhibitRootPanel);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update or Add Exhibit");
        setVisible(true);

        // generate our combobox
        DataValidator.generateRoomComboBox(this.roomComboBox);

        // populate our form fields
        if (exID != null) {
            this.exhibitIDTextField.setText("" + this.my_exhibit.getExhibitID());
            this.exhibitNameTextField.setText(this.my_exhibit.getExhibitName());
            this.startDateTextField.setText(this.my_exhibit.getStartDate().toString());
            this.endDateTextField.setText(this.my_exhibit.getEndDate().toString());
            this.roomComboBox.setSelectedItem(this.my_exhibit.getRoom());
        }

        // now pack our window
        pack();

        // data validation
        startDateTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!(startDateTextField.equals("") || DataValidator.isDate(startDateTextField.getText()))) {
                    JOptionPane.showMessageDialog(updateExhibitRootPanel,"Please enter a date in YYYY-MM-DD format.");
                }
            }
        });

        endDateTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!(endDateTextField.equals("") || DataValidator.isDate(endDateTextField.getText()))) {
                    JOptionPane.showMessageDialog(updateExhibitRootPanel,"Please enter a date in YYYY-MM-DD format.");
                }
            }
        });

        // action listeners
        exitDiscardChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JOptionPane.showMessageDialog(updateExhibitRootPanel,"This button doesn't do anything yet.");
                // update object values
                my_exhibit.setExhibitName(exhibitNameTextField.getText());
                my_exhibit.setStartDate(Date.valueOf(startDateTextField.getText()));
                my_exhibit.setEndDate(Date.valueOf(endDateTextField.getText()));
                my_exhibit.setRoom(roomComboBox.getSelectedItem().toString());
                // then update the database
                my_exhibit.updateRecord();
                tableModel.clearSearch();
                dispose();
            }
        });


    }

}
