package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public UpdateExhibit(String exID) {
        my_exhibit = new Exhibit(exID);
        // TODO: need to decide what gets passed to this screen
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

        exitDiscardChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(updateExhibitRootPanel,"This button doesn't do anything yet.");
            }
        });
    }
}
