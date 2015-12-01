package com.amdudda;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by amdudda on 11/30/15.
 */
public class BrowseListDataModel extends AbstractTableModel {
    int rowcount = 0;
    int colcount = 0;
    ResultSet browseTable = null;

    public BrowseListDataModel(ResultSet bT) {
        this.browseTable = bT;
        setTableDimensions();
    }

    private void setTableDimensions() {
        try {
            colcount = browseTable.getMetaData().getColumnCount();
            rowcount = countRows();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch table dimensions.\n" + sqle);
        }

    }

    private int countRows() {
        int tally = 0;
        try {
            browseTable.beforeFirst();
            while (browseTable.next()) {
                tally++;
            }
            browseTable.beforeFirst();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch row count.\n" + sqle);
        }
        System.out.println(tally);
        return tally;
    }

    @Override
    public int getRowCount() {
        return this.rowcount;
    }

    @Override
    public int getColumnCount() {
        return this.colcount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object contents = "";
        try {
            browseTable.absolute(rowIndex + 1);
            contents = browseTable.getObject(columnIndex + 1);
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
            columnText = this.browseTable.getMetaData().getColumnName(colNum+1);
        } catch (SQLException sqle) {
            System.out.println("Unable to extract column name.\n" + sqle);
        }
        return  columnText;
    }
}
