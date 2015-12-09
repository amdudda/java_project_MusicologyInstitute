package com.amdudda;

import javax.swing.table.AbstractTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by amdudda on 11/30/15.
 */
public class BrowseListDataModel extends AbstractTableModel {
    private int rowcount = 0;
    private int colcount = 0;
    private ResultSet browseTable = null;
    private ResultSet originalData = null;

    public BrowseListDataModel(ResultSet bT) {
        this.browseTable = bT;
        this.originalData = bT;
        setTableDimensions();
    }

    private void refresh() {
        setTableDimensions();
        this.fireTableStructureChanged();  // not sure I want this... keep output consistent; see to-do item below
        this.fireTableDataChanged();
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
        // debugging: System.out.println(tally);
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

    public String getTableName() {
        try {
            return this.browseTable.getMetaData().getTableName(1);
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch table name.\n" + sqle);
            return null;
        }
    }

    public void search(String selField, String searchString) {
        // this updates the display with search results
        // TODO: edit query so it only returns available fields.
        String sqlToRun = "SELECT * FROM " + getTableName() + " WHERE " +
                selField + " LIKE ?";
        PreparedStatement ps = null;
        try {
            ps = Database.conn.prepareStatement(sqlToRun);
            ps.setString(1,"%" + searchString + "%");
            this.browseTable = ps.executeQuery();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch search results.");
        }

        this.refresh();
    }

    public void clearSearch() {
        this.browseTable = this.originalData;
        this.refresh();
    }
}
