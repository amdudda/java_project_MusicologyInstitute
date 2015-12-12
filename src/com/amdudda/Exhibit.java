package com.amdudda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by amdudda on 12/12/15.
 */
public class Exhibit {
    public static final String EXHIBIT_TABLE_NAME = "Exhibit";
    public static final String EXHIBIT_ID = EXHIBIT_TABLE_NAME + ".ExhibitID";
    public static final String EXHIBIT_NAME = EXHIBIT_TABLE_NAME + ".ExhibitName";
    public static final String START_DATE = EXHIBIT_TABLE_NAME + ".StartDate";
    public static final String END_DATE = EXHIBIT_TABLE_NAME + ".EndDate";
    public static final String ROOM = EXHIBIT_TABLE_NAME + ".Room";

    private int ExhibitID;
    private String ExhibitName;
    private java.sql.Date StartDate;
    private java.sql.Date EndDate;
    private String Room;

    public Exhibit(String id) {
        // instantiates an Exhibit object
        String sqlToUse = "SELECT * FROM " + EXHIBIT_TABLE_NAME +
                " WHERE " + EXHIBIT_ID + " = ?";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = Database.conn.prepareStatement(sqlToUse);
            ps.setInt(1, Integer.parseInt(id));
            rs = ps.executeQuery();
        } catch (SQLException sqle) {
            System.out.println("Unable to get Exhibit data.\n" + sqle);
        }

        try {
            if (rs.next()) {
                this.ExhibitID = rs.getInt(EXHIBIT_ID);
                this.ExhibitName = rs.getString(EXHIBIT_NAME);
                this.StartDate = rs.getDate(START_DATE);
                this.EndDate = rs.getDate(END_DATE);
                this.Room = rs.getString(ROOM);
            }
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to parse Exhibit data.\n" + sqle);
            }
        }

    @Override
    public String toString() {
        return this.ExhibitName + "(#" + this.ExhibitID + ") is housed in Room " + this.Room +
                " and runs from " + this.StartDate + " to " + this.EndDate + ".";
    }

    protected static ResultSet getExhibitDetailList(){
        // lists all exhibits and associated instruments
        ResultSet rs = null;
        // TODO: create helper object for InstEx data.
        String sqltoUse = "SELECT " +
                EXHIBIT_ID + ", " + EXHIBIT_NAME + ", " + START_DATE + ", " + END_DATE + ", " + ROOM + ", " +
                "InstrumentExhibit.InstID, InstrumentExhibit.Room, InstrumentExhibit.LocationInRoom, " +
                Instrument.INSTNAME + ", " + Instrument.INSTTYPE + ", " + Instrument.SUBTYPE +
                " FROM Exhibit, InstrumentExhibit, Instrument" +
                " WHERE Exhibit.ExhibitID = InstrumentExhibit.ExhibitID AND" +
                " InstrumentExhibit.InstID = Instrument.InstID;";
        try {
            Statement s = Database.conn.createStatement();
            rs = s.executeQuery(sqltoUse);
        } catch (SQLException sqle) {
            System.out.println("Unable to get Exhibit data.\n" + sqle);
        }
        return rs;
    }
}
