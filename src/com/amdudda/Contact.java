package com.amdudda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by amdudda on 12/1/15.
 */
public class Contact {
    // TODO: add global variables related to instruments table in database

    // also need a method to return a resultset for basic browsing screen
    protected static ResultSet getBrowsingData() {
        ResultSet dataToBrowse = null;
        Statement bts = BrowseDatabaseScreen.browseTableStatement;
        try {
            bts = Database.conn.createStatement();
            // TODO: Replace field & table names with constants
            dataToBrowse = bts.executeQuery("SELECT ContactID,ContactName,BusinessName,Address,City,State,Country,ContactType FROM Contact");
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch data for table.\n" + sqle);
        }
        return dataToBrowse;
    }
}
