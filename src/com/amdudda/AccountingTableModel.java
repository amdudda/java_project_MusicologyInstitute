package com.amdudda;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
