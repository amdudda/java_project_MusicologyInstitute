package com.amdudda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by amdudda on 12/10/15.
 */
public class StorageLibrary extends LocationInfo {
    public static final String STORAGE_LIBRARY_TABLE_NAME = "StorageLibrary";
    public static final String INSTR_ID = STORAGE_LIBRARY_TABLE_NAME + ".InstrID";
    public static final String STORAGE_TYPE = STORAGE_LIBRARY_TABLE_NAME + ".StorageType";
    public static final String ROOM = STORAGE_LIBRARY_TABLE_NAME + ".Room";
    public static final String CABINET = STORAGE_LIBRARY_TABLE_NAME + ".Cabinet";
    public static final String SHELF = STORAGE_LIBRARY_TABLE_NAME + ".Shelf";

    // InstrID declared in parent object
    private String StorageType;
    private String Room;
    private String Cabinet;
    private int Shelf;

    public StorageLibrary(int id) {
        String sqlToUse = "SELECT * FROM " + STORAGE_LIBRARY_TABLE_NAME +
            " WHERE " + INSTR_ID + " = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = Database.conn.prepareStatement(sqlToUse);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();
            this.InstID = Integer.parseInt(rs.getObject(INSTR_ID).toString());
            this.StorageType = rs.getObject(STORAGE_TYPE).toString();
            this.Room = rs.getObject(ROOM).toString();
            this.Cabinet = rs.getObject(CABINET).toString();
            this.Shelf = Integer.parseInt(rs.getObject(SHELF).toString());
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch storage information.\n" + sqle);
        }
    }

    @Override
    public String toString() {
        if (this.InstID == 0) return "No storage or libary location information found.";
        return "In " + this.StorageType + " at room " + this.Room +
                " in cabinet " + this.Cabinet + " on shelf " + this.Shelf;
    }
}
