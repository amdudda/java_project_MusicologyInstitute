package com.amdudda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by amdudda on 12/1/15.
 */
public class Contact {
    // Global variables related to Contact table in database
    public static final String CONTACT_TABLE_NAME = "Contact";
    // note that the constants below return a fully qualified field name.  I do this so the output mirrors the syntax
    // used to call the constants, because SQL doesn't care if you do unnecessary table specification, and because it
    // reduces confusion when reviewing SQL syntax during debugging.
    public static final String CONTACTID = CONTACT_TABLE_NAME + ".ContactID";
    public static final String CONTACTNAME = CONTACT_TABLE_NAME + ".ContactName";
    public static final String BUSINESSNAME = CONTACT_TABLE_NAME + ".BusinessName";
    public static final String ADDRESS = CONTACT_TABLE_NAME + ".Address";
    public static final String CITY = CONTACT_TABLE_NAME + ".City";
    public static final String STATE = CONTACT_TABLE_NAME + ".State";
    public static final String POSTALCODE = CONTACT_TABLE_NAME + ".PostalCode";
    public static final String COUNTRY = CONTACT_TABLE_NAME + ".Country";
    public static final String BUSINESSPHONE = CONTACT_TABLE_NAME + ".BusinessPhone";
    public static final String CONTACTPHONE = CONTACT_TABLE_NAME + ".ContactPhone";
    public static final String CONTACTTYPE = CONTACT_TABLE_NAME + ".ContactType";
    public static final String NOTES = CONTACT_TABLE_NAME + ".Notes";


    // also need a method to return a resultset for basic browsing screen
    protected static ResultSet getBrowsingData() {
        ResultSet dataToBrowse = null;
        Statement bts = BrowseDatabaseScreen.browseTableStatement;
        try {
            bts = Database.conn.createStatement();
            String sqlToRun = "SELECT " +
                    CONTACTID + ", " +
                    CONTACTNAME + ", " +
                    BUSINESSNAME + ", " +
                    ADDRESS + ", " +
                    CITY + ", " +
                    STATE + ", " +
                    COUNTRY + ", " +
                    CONTACTTYPE + " FROM " +
                    CONTACT_TABLE_NAME;
            dataToBrowse = bts.executeQuery(sqlToRun);
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch data for table.\n" + sqle);
        }
        return dataToBrowse;
    }
}
