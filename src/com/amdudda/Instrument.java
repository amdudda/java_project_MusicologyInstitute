package com.amdudda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by amdudda on 12/1/15.
 */
public class Instrument {
    // Global variables related to Instrument table in database
    public static final String INSTRUMENT_TABLE_NAME = "Instrument";
    // note that the constants below return a fully qualified field name.  I do this so the output mirrors the syntax
    // used to call the constants, because SQL doesn't care if you do unnecessary table specification, and because it
    // reduces confusion when reviewing SQL syntax during debugging.
    public static final String INSTID = INSTRUMENT_TABLE_NAME + ".InstID";
    public static final String INSTNAME = INSTRUMENT_TABLE_NAME + ".InstName";
    public static final String INSTTYPE = INSTRUMENT_TABLE_NAME + ".InstType";
    public static final String SUBTYPE = INSTRUMENT_TABLE_NAME + ".Subtype";
    public static final String ACQUIREDDATE = INSTRUMENT_TABLE_NAME + ".AcquiredDate";
    public static final String ACQUIREDFROM = INSTRUMENT_TABLE_NAME + ".AcquiredFrom";
    public static final String PURCHASEPRICE = INSTRUMENT_TABLE_NAME + ".PurchasePrice";
    public static final String INSURANCEVALUE = INSTRUMENT_TABLE_NAME + ".InsuranceValue";
    public static final String LOCATION = INSTRUMENT_TABLE_NAME + ".Location";
    public static final String HEIGHT = INSTRUMENT_TABLE_NAME + ".Height";
    public static final String WIDTH = INSTRUMENT_TABLE_NAME + ".Width";
    public static final String DEPTH = INSTRUMENT_TABLE_NAME + ".Depth";
    public static final String REGION = INSTRUMENT_TABLE_NAME + ".Region";
    public static final String COUNTRY = INSTRUMENT_TABLE_NAME + ".Country";
    public static final String CULTURE = INSTRUMENT_TABLE_NAME + ".Culture";
    public static final String TUNING = INSTRUMENT_TABLE_NAME + ".Tuning";
    public static final String LOWNOTE = INSTRUMENT_TABLE_NAME + ".LowNote";
    public static final String HIGHNOTE = INSTRUMENT_TABLE_NAME + ".HighNote";
    public static final String DESCRIPTION = INSTRUMENT_TABLE_NAME + ".Description";
    public static final String ISALOAN = INSTRUMENT_TABLE_NAME + ".isALoan";

    // variables to store object attributes
    private String ContactID;
    private String ContactName;
    private String BusinessName;
    private String Address;
    private String City;
    private String State;
    private String PostalCode;
    private String Country;
    private String BusinessPhone;
    private String ContactPhone;
    private String ContactType;
    private String Notes;

    // and a constructor so we can pass instrument attributes to the editing screen and pass values back to the databae
    public Instrument(String recID) {
        if (recID != null) {
            // only bother populating the values if a recordID is actually passed to the constructor.
            try {
                PreparedStatement psFetch = Database.conn.prepareStatement("SELECT * FROM ? WHERE ? = ?");
                psFetch.setString(1,INSTRUMENT_TABLE_NAME);
                psFetch.setString(2,INSTID);
                psFetch.setInt(3,Integer.parseInt(recID));
                ResultSet my_instrument = psFetch.executeQuery();
            } catch (SQLException sqle) {
                System.out.println("Unable to fetch instrument data)");
            }
        }
    }


    // also need a method to return a resultset for basic browsing screen
    protected static ResultSet getBrowsingData() {
        ResultSet dataToBrowse = null;
        Statement bts = BrowseDatabaseScreen.browseTableStatement;
        try {
            bts = Database.conn.createStatement();
            String sqlToRun = "SELECT " +
                    INSTID + "," +
                    INSTNAME  + "," +
                    INSTTYPE  + "," +
                    SUBTYPE + "," +
                    LOCATION +
                    " FROM " + INSTRUMENT_TABLE_NAME;
            dataToBrowse = bts.executeQuery(sqlToRun);
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch data for table.\n" + sqle);
        }
        return dataToBrowse;
    }
}
