package com.amdudda;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.NumberFormatter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;

/**
 * Created by amdudda on 12/14/15.
 */
public class AccountingTableModel extends AbstractTableModel {
    // No guarantees I'll get this done tonight, but let's see what I can pull off. :-)
    // Needs to be separate because I want this to mimic a spreadsheet.

    private int rowcount = 0;
    private int colcount = 0;
    private ResultSet acctgTable = null;

    // recycled from BROWSELISTDATAMODEL
    public AccountingTableModel(ResultSet bT) {
        this.acctgTable = bT;
        setTableDimensions();
    }

    protected void refresh() {
        setTableDimensions();
        this.fireTableStructureChanged();  // not sure I want this... keep output consistent; see to-do item below
        this.fireTableDataChanged();
    }

    private void setTableDimensions() {
        try {
            colcount = acctgTable.getMetaData().getColumnCount();
            rowcount = countRows();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch table dimensions.\n" + sqle);
        }

    }

    private int countRows() {
        int tally = 0;
        try {
            acctgTable.beforeFirst();
            while (acctgTable.next()) {
                tally++;
            }
            acctgTable.beforeFirst();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch row count.\n" + sqle);
        }
        // debugging: System.out.println(tally);
        return tally;
    }

    @Override
    public int getRowCount() {
        return rowcount;
    }

    @Override
    public int getColumnCount() {
        return colcount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object contents = "";
        try {
            acctgTable.absolute(rowIndex + 1);
            contents = acctgTable.getObject(columnIndex + 1);
            // return an empty string if the value is null, or the datamodel won't know what to display
            if (contents == null) contents = "";
        } catch (SQLException sqle) {
            System.out.println("Unable to get data for cell at row " + rowIndex + ", column " + columnIndex + ".\n" + sqle);
        }
        return contents;
    }

    @Override
    public String getColumnName(int colNum) {
        String columnText = "?";
        try {
            columnText = this.acctgTable.getMetaData().getColumnName(colNum + 1);
        } catch (SQLException sqle) {
            System.out.println("Unable to extract column name.\n" + sqle);
        }
        return columnText;
    }

    public int getColumnNumber(String colName) {
        int colNum = 0;
        for (int i = 0; i <= this.colcount; i++) {
            if (colName.equals(this.getColumnName(i))) {
                colNum = i;
                break;
            }
        }
        //System.out.println(colName + " is in column " + colNum);
        return colNum;
    }

    public void setAcctgTable(ResultSet tbl) {
        this.acctgTable = tbl;
    }

/* not needed because setValueAt won't work with this particular recordset.
    @Override
    public boolean isCellEditable(int row, int col) {
        if (getColumnName(col).equals(AccountingScreen.INST_ID) ||
                getColumnName(col).equals(AccountingScreen.AC_FROM) ||
                getColumnName(col).equals(AccountingScreen.INST_NAME)) {
            return false;
        }
        else { return true; }

    }*/

/*
    This doesn't work for the particular recordset I've got.
    @Override
    //This is called when user edits an editable cell
    public void setValueAt(Object newValue, int row, int col) {

        //Make sure newValue is an integer AND that it is in the range of valid ratings
        boolean isDoubleColumn = false;
        String selectedColumn = getColumnName(col);

        try {
            // TODO: Add error checking for the editable fields.
            if (selectedColumn.equals(AccountingScreen.PURCH_PRICE) || selectedColumn.equals(AccountingScreen.INS_VAL)) {
                isDoubleColumn = true;
                if (!DataValidator.isCurrency(newValue.toString())) {
                    throw new NumberFormatException();
                }
            }
            if (selectedColumn.equals(AccountingScreen.AC_DATE)) {
                isDoubleColumn = false;
                if (!DataValidator.isDate(newValue.toString())) {
                    throw new DateTimeException(null);
                }
            }
        } catch (NumberFormatException ne) {
            //Error dialog box. First argument is the parent GUI component, which is only used to center the
            // dialog box over that component. We don't have a reference to any GUI components here
            // but are allowed to use null - this means the dialog box will show in the center of your screen.
            JOptionPane.showMessageDialog(null, "Try entering a number less than 1 billion with no more than 2 decimal places.");
            //return prevents the following database update code happening...
            return;
        } catch (DateTimeException dte) {
            JOptionPane.showMessageDialog(null, "Try entering a date in YYYY-MM-DD format.");
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error affecting column _" + selectedColumn + "_.");
            System.out.println("Unable to update accounting data via datatable.\n" + e);
            return;
        }

        //This only happens if the new rating is valid
        try {
            acctgTable.absolute(row + 1);
            if (isDoubleColumn) {
                acctgTable.updateDouble(selectedColumn, Double.parseDouble(newValue.toString()));
            } else {
                acctgTable.updateDate(selectedColumn,Date.valueOf(newValue.toString()));
            }
            acctgTable.updateRow();  // updates the database
            fireTableDataChanged(); // updates the GUI display
        } catch (SQLException e) {
            System.out.println("error changing value " + e);
        }
    }
    */
}
