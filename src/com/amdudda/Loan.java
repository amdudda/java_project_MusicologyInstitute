package com.amdudda;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by amdudda on 12/8/15.
 */
public class Loan extends LocationInfo {
    //private int InstID lives in LocationInfo.
    private int ContactID;
    private java.sql.Date StartDate;
    private java.sql.Date EndDate;
    private String ContactName;

    private static final String LOAN_TABLE_NAME = "Loan";
    private static final String INST_ID = LOAN_TABLE_NAME + ".InstID";
    private static final String CONTACT_ID = LOAN_TABLE_NAME + ".ContactID";
    private static final String START_DATE = LOAN_TABLE_NAME + ".StartDate";
    private static final String END_DATE = LOAN_TABLE_NAME + ".EndDate";

    Loan (int instID,int ContactID) {
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
}
