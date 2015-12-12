package com.amdudda;

import java.sql.*;

/**
 * Created by amdudda on 12/8/15.
 */
public class Loan extends LocationInfo {
    //private int InstID lives in LocationInfo.
    private int ContactID;
    private java.sql.Date StartDate;
    private java.sql.Date EndDate;
    private String ContactName;

    public static final String LOAN_TABLE_NAME = "Loan";
    public static final String INST_ID = LOAN_TABLE_NAME + ".InstID";
    public static final String CONTACT_ID = LOAN_TABLE_NAME + ".ContactID";
    public static final String START_DATE = LOAN_TABLE_NAME + ".StartDate";
    public static final String END_DATE = LOAN_TABLE_NAME + ".EndDate";

    Loan (int instID) {
        String sqlToRun = "SELECT " + INST_ID + ", " + CONTACT_ID + ", " + START_DATE +
                ", " + END_DATE + ", IF (isempty(" + Contact.BUSINESSNAME + ")," +
                Contact.CONTACTNAME + "," + Contact.BUSINESSNAME + ") AS CName " +
                " FROM " + LOAN_TABLE_NAME + ", " + Contact.CONTACT_TABLE_NAME +
                " WHERE " + INST_ID + " = " + instID;
                //" WHERE " + CONTACT_ID + " = " + Contact.CONTACTID;
        Statement s = null;
        ResultSet rs;
        try {
            s = Database.conn.createStatement();
            rs = s.executeQuery(sqlToRun);
            if (rs.next()) {  // in theory, should only return one record, since MI doesn't care about previous loans made
                this.InstID = rs.getInt(INST_ID);
                this.ContactID = rs.getInt(CONTACT_ID);
                this.StartDate = rs.getDate(START_DATE);
                this.EndDate = rs.getDate(END_DATE);
                this.ContactName = rs.getString("CName");
            }
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch Loan data.\n" + sqle);
        }
    }

    @Override
    public String toString() {
        // generates a string with loan information
        String toReturn = "";
        if (InstID == 0 ) return "No loan information found.";
        return "Instrument ID# " + this.InstID + " is on loan to " + this.ContactName +
                " (ID # " + this.ContactID + ") from " + this.StartDate + " to " + this.EndDate + ".";
    }

    // getters and setters
    public int getContactID() {
        return ContactID;
    }

    public void setContactID(int contactID) {
        ContactID = contactID;
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

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    // required methods

    @Override
    public void updateRecord() {
        String sqlToUse = "UPDATE " + LOAN_TABLE_NAME + " SET " +
                CONTACT_ID + " = ?, " +
                START_DATE + " = ?, " +
                END_DATE +" = ? " +
                " WHERE " + INST_ID + " = ?";
        PreparedStatement ps;
        // System.out.println("trying to update record!");
        try {
            ps = Database.conn.prepareStatement(sqlToUse);
            int i = 1;
            ps.setInt(i, this.ContactID);
            ps.setDate(++i, this.StartDate);
            ps.setDate(++i, this.EndDate);
            ps.setInt(++i, this.InstID);
            if (ps.executeUpdate() == 0 ) {
                // if update fails, try inserting instead
                 insertRecord();
            }
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to update loan information.\n" + sqle);
        }

    }

    @Override
    public void insertRecord() {
        String sqlToUse = "INSERT INTO " + LOAN_TABLE_NAME + "(" +
                INST_ID + "," + CONTACT_ID + "," + START_DATE + "," + END_DATE +
                ") VALUES (?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = Database.conn.prepareStatement(sqlToUse);
            int i = 1;
            ps.setInt(i, this.InstID);
            ps.setInt(++i, this.ContactID);
            ps.setDate(++i, this.StartDate);
            ps.setDate(++i, this.EndDate);
            ps.executeUpdate();
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to insert loan information.\n" + sqle);
        }
    }

    protected void updateContactName() {
        String sqlToUse = "SELECT " +
                " IF (isempty(" + Contact.BUSINESSNAME + ")," +
                Contact.CONTACTNAME + "," + Contact.BUSINESSNAME + ") AS CName " +
                " FROM " + Contact.CONTACT_TABLE_NAME + " WHERE " +
                Contact.CONTACTID + " = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = Database.conn.prepareStatement(sqlToUse);
            ps.setInt(1,this.ContactID);
            rs = ps.executeQuery();
            if (rs.next())
            {
                String nameToUse = rs.getObject("CName").toString();
                this.setContactName(nameToUse);
            } else {
                this.setContactName("UNKNOWN");
            }
        } catch (SQLException sqle) {
            System.out.println("Unable to update contact name in Loan object.\n" + sqle);
        }
    }
}
