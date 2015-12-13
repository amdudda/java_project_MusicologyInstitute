package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by amdudda on 12/1/15.
 */
public class SelectExhibitScreen extends JFrame {
    // recycled from SelectContactScreen
    private JTable browseDataTable;
    private JButton useSelectedRecordButton;
    private JButton returnToMainMenuButton;
    private JPanel selectContactRootPanel;
    private JComboBox selectSearchColumnComboBox;
    private JTextField searchStringTextField;
    private JButton searchButton;
    private JButton clearResultsButton;
    private BrowseListDataModel bldm;
    private static ResultSet dataToBrowse;
    protected static Statement browseTableStatement;

    // This reuses the BrowseDatabaseScreen to create a "select Contact" window.
    // Need to pass window object so we can update the value on that screen.
    public SelectExhibitScreen(ResultSet dTB, JTextField textField) {
        this.dataToBrowse = dTB;
        setContentPane(selectContactRootPanel);
        pack();
        setTitle("Select Exhibit");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // this screen takes different result sets based on whether instruments or contacts are being browsed.

        bldm = new BrowseListDataModel(dataToBrowse);
        browseDataTable.setModel(bldm);

        DataValidator.generateSearchFields(selectSearchColumnComboBox,bldm);

        returnToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (dataToBrowse != null) dataToBrowse.close();
                    if (browseTableStatement != null) browseTableStatement.close();
                } catch (SQLException sqle) {
                    System.out.println("Unable to close connections.\n" + sqle);
                }
                dispose();
            }
        });

        useSelectedRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selPK = bldm.getValueAt(browseDataTable.getSelectedRow(), 0).toString();
                // Send selectedPK to the Add Instrument screen and close the window.
                textField.setText(selPK);
                dispose();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JOptionPane.showMessageDialog(browseDbScreenRootPanel,"This button doesn't do anything yet.");
                String selField = selectSearchColumnComboBox.getSelectedItem().toString();
                String searchString = searchStringTextField.getText();
                bldm.search(selField, searchString);
            }
        });

        clearResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bldm.clearSearch();
            }
        });

    }

}
