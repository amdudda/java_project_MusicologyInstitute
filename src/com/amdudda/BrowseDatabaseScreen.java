package com.amdudda;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
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
    private static ResultSet dataToBrowse;
    protected static Statement browseTableStatement;

    public BrowseDatabaseScreen(ResultSet dTB) {
        this.dataToBrowse = dTB;
        setContentPane(browseDbScreenRootPanel);
        pack();
        setTitle("Browse MI Database");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // this screen takes different result sets based on whether instruments or contacts are being browsed.

        bldm = new BrowseListDataModel(dataToBrowse);
        browseDataTable.setModel(bldm);


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

        editSelectedRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selPK = bldm.getValueAt(browseDataTable.getSelectedRow(), 0).toString();
                if (browseDataTable.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(browseDbScreenRootPanel,"Please select a record to edit.");
                } else if (bldm.getTableName().contains("Instrument")) {
                    UpdateInstrument updtInst = new UpdateInstrument(selPK);
                } else {
                    // TODO: need to handle fetching contacts data
                    UpdateContact updtCont = new UpdateContact(selPK);
                    return;
                }
            }
        });
        addNewRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bldm.getTableName().contains("Instrument")) {
                    UpdateInstrument updtInst = new UpdateInstrument(null);
                } else {
                    // TODO: need to handle fetching contacts data
                    UpdateContact updtCont = new UpdateContact(null);
                    return;
                }
            }
        });
    }

}
