package com.amdudda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by amdudda on 12/10/15.
 */
public class OnExhibit extends LocationInfo {
    // this maps to the Exhibit table but is called "on exhibit" for clarity.
    public static final String EXHIBIT_TABLE_NAME = "Exhibit";
    public static final String INSTR_ID = EXHIBIT_TABLE_NAME + ".InstrID";
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
            this.Room = rs.getObject(ROOM).toString();
            this.LocationInRoom = rs.getObject(LOCATION_IN_ROOM).toString();
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
}
