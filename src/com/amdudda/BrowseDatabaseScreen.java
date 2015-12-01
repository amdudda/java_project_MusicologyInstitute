package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by amdudda on 12/1/15.
 */
public class BrowseDatabaseScreen extends JFrame {
    private JTable browseDataTable;
    private JButton editSelectedRecordButton;
    private JButton addNewRecordButton;
    private JButton returnToMainMenuButton;
    private JPanel browseDbScreenRootPanel;
    private BrowseListDataModel bldm;
    private ResultSet dataToBrowse = null;
    private Statement browseTableStatement = null;

    public BrowseDatabaseScreen() { // ResultSet dTB) {
        //this.dataToBrowse = dTB;
        setContentPane(browseDbScreenRootPanel);
        pack();
        setTitle("Browse MI Database");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // this screen takes different result sets based on whether instruments or contacts are being browsed.
        try {
            browseTableStatement = Database.conn.createStatement();
            dataToBrowse = browseTableStatement.executeQuery("SELECT InstName, InstType, Subtype, Location FROM Instrument");
            bldm = new BrowseListDataModel(dataToBrowse);
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch data for table.\n" + sqle);
        }

        browseDataTable.setModel(bldm);


        returnToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dataToBrowse.close();
                    browseTableStatement.close();
                } catch (SQLException sqle) {
                    System.out.println("Unable to close connections.\n" + sqle);
                }
                dispose();
            }
        });
    }
}
