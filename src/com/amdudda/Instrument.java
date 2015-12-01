package com.amdudda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by amdudda on 12/1/15.
 */
public class Instrument {
    // TODO: add global variables related to instruments table in database

    // also need a method to return a resultset for basic browsing screen
    protected static ResultSet getBrowsingData() {
        ResultSet dataToBrowse = null;
        Statement bts = BrowseDatabaseScreen.browseTableStatement;
        try {
            bts = Database.conn.createStatement();
            dataToBrowse = bts.executeQuery("SELECT InstName, InstType, Subtype, Location FROM Instrument");
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch data for table.\n" + sqle);
        }
        return dataToBrowse;
    }
}
