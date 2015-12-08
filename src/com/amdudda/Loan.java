package com.amdudda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by amdudda on 12/8/15.
 */
public class Loan {
    private int InstID;
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
        // TODO: change sql to use constants.
        String sqlToRun = "SELECT " + INST_ID + ", " + CONTACT_ID + ", " + START_DATE +
                ", " + END_DATE + ", IF (isempty(" + Contact.BUSINESSNAME + ")," +
                Contact.CONTACTNAME + "," + Contact.BUSINESSNAME + ") AS CName " +
                " FROM " + LOAN_TABLE_NAME + ", " + Contact.CONTACT_TABLE_NAME +
                " WHERE " + CONTACT_ID + " = " + Contact.CONTACTID;
        System.out.println(sqlToRun);
        Statement s = null;
        ResultSet rs;
        try {
            s = Database.conn.createStatement();
            rs = s.executeQuery(sqlToRun);
            rs.next();  // in theory, should only return one record, since MI doesn't care about previous loans made
            this.InstID = rs.getInt(INST_ID);
            this.ContactID = rs.getInt(CONTACT_ID);
            this.StartDate = rs.getDate(START_DATE);
            this.EndDate = rs.getDate(END_DATE);
            this.ContactName = rs.getString("CName");
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch Loan data.\n" + sqle);
        }
    }

    @Override
    public String toString() {
        // generates a string with loan information
        return "Instrument ID# " + this.InstID + " was loaned to " + this.ContactName +
                "(ID # " + this.ContactID + ") from " + this.StartDate + " to " + this.EndDate + ".";
    }

}
