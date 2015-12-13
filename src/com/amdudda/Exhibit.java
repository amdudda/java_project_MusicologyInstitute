package com.amdudda;

import java.sql.*;

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
        if (id != null) {
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
    }

    @Override
    public String toString() {
        return this.ExhibitName + "(#" + this.ExhibitID + ") is housed in Room " + this.Room +
                " and runs from " + this.StartDate + " to " + this.EndDate + ".";
    }

    protected static ResultSet getBrowsingData(){
        // lists all exhibits and associated instruments
        ResultSet rs = null;
        // TODO: create helper object for InstEx data.
        /*String sqltoUse = "SELECT " +
                EXHIBIT_ID + ", " + EXHIBIT_NAME + ", " + START_DATE + ", " + END_DATE + ", " + ROOM + ", " +
                "InstrumentExhibit.InstID, InstrumentExhibit.Room AS InstrRoom, InstrumentExhibit.LocationInRoom, " +
                Instrument.INSTNAME + ", " + Instrument.INSTTYPE + ", " + Instrument.SUBTYPE +
                " FROM Exhibit, InstrumentExhibit, Instrument" +
                " WHERE Exhibit.ExhibitID = InstrumentExhibit.ExhibitID AND" +
                " InstrumentExhibit.InstID = Instrument.InstID";*/
            // second line of query below refers to named fields in the subquery.
        String sqltoUse = "SELECT " +
                EXHIBIT_ID + ", " + EXHIBIT_NAME + ", " + START_DATE + ", " + END_DATE + ", " + ROOM + " AS ExhibitHome, " +
                "InstID, InstrRoom, LocationInRoom, InstName, InstType, Subtype" +
                " FROM Exhibit " +
                " LEFT JOIN " +
                " (SELECT " +
                InstrumentExhibit.EXHIBIT_ID + " AS exID, " + InstrumentExhibit.INST_ID + ", " +
                InstrumentExhibit.ROOM  + " AS InstrRoom, " + InstrumentExhibit.LOCATION_IN_ROOM + ", " +
                Instrument.INSTNAME + ", " + Instrument.INSTTYPE + ", " + Instrument.SUBTYPE +
                " FROM " +
                "InstrumentExhibit, " + Instrument.INSTRUMENT_TABLE_NAME +
                " WHERE " +
                "InstrumentExhibit.InstID = " + Instrument.INSTID + ") AS IOE " +
                " ON " +
                EXHIBIT_ID + " = IOE.exID ";
        try {
            Statement s = Database.conn.createStatement();
            rs = s.executeQuery(sqltoUse);
        } catch (SQLException sqle) {
            System.out.println("Unable to get Exhibit data.\n" + sqle);
            System.out.println(sqle.getSQLState());
            System.out.println(sqle.getErrorCode());
        }
        return rs;
    }

    // getters and setters

    public int getExhibitID() {
        return ExhibitID;
    }

    public void setExhibitID(int exhibitID) {
        ExhibitID = exhibitID;
    }

    public String getExhibitName() {
        return ExhibitName;
    }

    public void setExhibitName(String exhibitName) {
        ExhibitName = exhibitName;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    // method to update an Exhibit object in database
    protected void updateRecord() {
        String sqlToRun = "UPDATE " + EXHIBIT_TABLE_NAME + " SET " +
                EXHIBIT_NAME + " = ?, " +
                START_DATE + " = ?," +
                END_DATE + " = ?, " +
                ROOM + " = ? " +
                " WHERE " + EXHIBIT_ID + " = ?";
        PreparedStatement ps = null;
        try {
            ps = Database.conn.prepareStatement(sqlToRun);
            int i = 1;
            ps.setString(i,this.ExhibitName);
            ps.setDate(++i,this.StartDate);
            ps.setDate(++i,this.EndDate);
            ps.setString(++i,this.Room);
            ps.setInt(++i,this.ExhibitID);

            if (ps.executeUpdate() == 0 ) {
                // if update fails, try inserting instead
                insertRecord();
            }
            if (ps != null) ps.close();

        } catch (SQLException e) {
            System.out.println("Unable to update Exhibit record.\n" + e);
            //e.printStackTrace();
        }
    }

    protected void insertRecord() {
        // inserts a record
        String sqlToRun = "INSERT INTO " + EXHIBIT_TABLE_NAME + "(" +
                EXHIBIT_NAME + ", " +
                START_DATE + "," +
                END_DATE + ", " +
                ROOM + ") " +
                " VALUES (?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = Database.conn.prepareStatement(sqlToRun);
            int i = 1;
            ps.setString(i,this.ExhibitName);
            ps.setDate(++i,this.StartDate);
            ps.setDate(++i,this.EndDate);
            ps.setString(++i,this.Room);

            ps.executeUpdate();
            if (ps != null) ps.close();

        } catch (SQLException e) {
            System.out.println("Unable to insert Exhibit record.\n" + e);
            //e.printStackTrace();
        }
    }
}
