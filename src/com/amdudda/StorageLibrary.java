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

    // Setters and getters
    public String getStorageType() {
        return StorageType;
    }

    public void setStorageType(String storageType) {
        StorageType = storageType;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getCabinet() {
        return Cabinet;
    }

    public void setCabinet(String cabinet) {
        Cabinet = cabinet;
    }

    public int getShelf() {
        return Shelf;
    }

    public void setShelf(int shelf) {
        Shelf = shelf;
    }

    // required methods

    @Override
    public void updateRecord() {
        String sqlToUse = "UPDATE " + STORAGE_LIBRARY_TABLE_NAME + " SET " +
                STORAGE_TYPE + " = ?, " +
                ROOM + " = ?, " +
                CABINET +" = ?, " +
                SHELF +" = ? " +
                " WHERE " + INSTR_ID + " = ?";
        PreparedStatement ps;
        try {
            ps = Database.conn.prepareStatement(sqlToUse);
            int i = 1;
            ps.setString(i, this.StorageType);
            ps.setString(++i, this.Room);
            ps.setString(++i, this.Cabinet);
            ps.setInt(++i,this.Shelf);
            ps.setInt(++i, this.InstID);
            ps.executeUpdate();
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to update storage information.\n" + sqle);
        }
    }

    @Override
    public void insertRecord() {
        String sqlToUse = "INSERT INTO " + STORAGE_LIBRARY_TABLE_NAME + "(" +
                INSTR_ID + "," + STORAGE_TYPE + "," + ROOM + "," + CABINET + "," + SHELF +
                ") VALUES (?,?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = Database.conn.prepareStatement(sqlToUse);
            int i = 1;
            ps.setInt(i, this.InstID);
            ps.setString(++i, this.StorageType);
            ps.setString(++i, this.Room);
            ps.setString(++i, this.Cabinet);
            ps.setInt(++i,this.Shelf);
            ps.executeUpdate();
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to insert storage information.\n" + sqle);
        }
    }
}
