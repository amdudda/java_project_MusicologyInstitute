package com.amdudda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by amdudda on 12/10/15.
 */
public class OnExhibit extends LocationInfo {
    // this maps to the Exhibit table but is called "on exhibit" for clarity.
    public static final String EXHIBIT_TABLE_NAME = "InstrumentExhibit";
    public static final String INSTR_ID = EXHIBIT_TABLE_NAME + ".InstID";
    public static final String EXHIBIT_ID = EXHIBIT_TABLE_NAME + ".ExhibitID";
    public static final String ROOM = EXHIBIT_TABLE_NAME + ".Room";
    public static final String LOCATION_IN_ROOM = EXHIBIT_TABLE_NAME + ".LocationInRoom";

    // InstrID is declared in LocationInfo
    private int ExhibitID;
    private String Room;
    private String LocationInRoom;

    public OnExhibit(int id) {
        String sqlToUse = "SELECT * FROM " + EXHIBIT_TABLE_NAME +
                " WHERE " + INSTR_ID + " = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = Database.conn.prepareStatement(sqlToUse);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();
            this.InstID = Integer.parseInt(rs.getObject(INSTR_ID).toString());
            this.ExhibitID = Integer.parseInt(rs.getObject(EXHIBIT_ID).toString());
            this.Room = (rs.getObject(ROOM) == null) ? "" : rs.getObject(ROOM).toString();
            this.LocationInRoom = (rs.getObject(LOCATION_IN_ROOM) == null) ? "" : rs.getObject(LOCATION_IN_ROOM).toString();
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch exhibit location information.\n" + sqle);
        }
    }

    @Override
    public String toString() {
        if (this.InstID == 0) return "No exhibit information found.";
        return "On exhibit in the " + this.LocationInRoom + " area of room " + this.Room;
    }

    // getters and setters
    public int getExhibitID() {
        return ExhibitID;
    }

    public void setExhibitID(int exhibitID) {
        ExhibitID = exhibitID;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getLocationInRoom() {
        return LocationInRoom;
    }

    public void setLocationInRoom(String locationInRoom) {
        LocationInRoom = locationInRoom;
    }

    // required methods

    @Override
    public void updateRecord() {
        String sqlToUse = "UPDATE " + EXHIBIT_TABLE_NAME + " SET " +
                EXHIBIT_ID + " = ?," +
                ROOM + " = ?," +
                LOCATION_IN_ROOM + " = ? " +
                " WHERE " + INSTR_ID + " = ?";
        try {
            PreparedStatement ps = Database.conn.prepareStatement(sqlToUse);
            int i = 1;
            ps.setInt(i, this.ExhibitID);
            ps.setString(++i, this.Room);
            ps.setString(++i, this.LocationInRoom);
            ps.setInt(++i, this.InstID);
            ps.executeUpdate();
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to update On-Exhibit data.\n" + sqle);
        }
    }

    @Override
    public void insertRecord() {
        String sqlToUse = "INSERT INTO " + EXHIBIT_TABLE_NAME + "(" +
                INSTR_ID + "," + EXHIBIT_ID + "," + ROOM + "," + LOCATION_IN_ROOM +
                ") VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = Database.conn.prepareStatement(sqlToUse);
            int i = 1;
            ps.setInt(i, this.InstID);
            ps.setInt(++i, this.ExhibitID);
            ps.setString(++i, this.Room);
            ps.setString(++i, this.LocationInRoom);
            ps.executeUpdate();
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to insert On-Exhibit data.\n" + sqle);
        }
    }
}
