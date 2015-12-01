package com.amdudda;

import javax.swing.*;
import java.io.*;
import java.sql.*;

/**
 * Created by amdudda on 11/28/15.
 */
public class Database {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "MusicologyInstitute";
    private static final String USER = "amdudda";
    private static final String PASS = "tanuki";

    protected static Connection conn;
    protected static Statement statement;
    protected static ResultSet rs;

    private static boolean dbFlag = true;
    // The list of setup files to be read in so the tables can be created.
    // The queries need to be run in this order to prevent them from tripping up on foreign keys referring
    // to nonexistent tables.
    protected static final String[] setupFileNames = {"00-Countries.sql", "00a-populateCountries.sql",
            "01-Contact.sql", "02-Instrument.sql", "03-Loan.sql", "04-StorageLibrary.sql", "05-Rooms.sql",
            "06-Exhibit.sql", "07-InstrumentExhibit.sql"};

    // no constructor - this is just setup stuff.


    // a method to set up the database
    protected static void setupDatabase() {
        if (!getDBflag()) {
            createSchema();
            openConnStatement();
            createTables();
        /*
        Create a file in data directory that flags a successful setup.
        If the database has already been successfully established, we can skip this stuff and proceed to the user interface.
        */
            flagSuccessfulSetup(dbFlag);
        }
        closeConnStatement();
    }

    private static void createSchema() {
        try {
            conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASS);
            statement = conn.createStatement();

            statement.executeUpdate("CREATE SCHEMA " + DB_NAME);

            statement.close();
            conn.close();
        } catch (SQLException sqle) {
            if (sqle.getErrorCode() == 1007) {
                // in this case, proceed to trying to set up the data tables.
                System.out.printf("The %s database already exists. " +
                        " Please have your administrator verify that the database has set up properly." +
                        " It may have added tables to another, pre-existing, database.\n", DB_NAME);
            } else {
                System.out.printf("Unable to set up the %s Database.\n", DB_NAME);
                System.out.println("Error Code Number: " + sqle.getErrorCode());
                System.out.println(sqle);
                System.exit(-1);
            }
            // In either case, exit the program because the database won't be set up properly &
            // I want to flag to user that something broke.
            dbFlag = false;
        }
    }

    private static void createTables() {
        String sqlToRun;
        // the try... catch is inside the for loop so that the code can skip a table if there is a problem creating it.
        for (int i = 0; i < setupFileNames.length; i++) {
            try {

                sqlToRun = (fetchSQLdata(setupFileNames[i]));
                statement.executeUpdate(sqlToRun);
            } catch (SQLException sqle) {
                if (sqle.getErrorCode() == 1050) {
                    System.out.println(sqle.getMessage() + ".  Please have your administrator verify that the database has set up properly.");
                } else {
                    System.out.println("Unable to create table even though it does not appear to exist already.\n" + sqle);
                    System.out.println(sqle.getErrorCode());
                }
                dbFlag = false;
            }
        }
    }

    // some methods to work with the database connections
    protected static void openConnStatement() {
        // establishes a connection to the database.
        try {
            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASS);
            statement = conn.createStatement();
            rs = null;
        } catch (SQLException sqle) {
            System.out.println("Unable to open connection or create statement.");
            System.out.println(sqle);
        }
    }

    protected static void closeConnStatement() {
        try {
            if (statement != null) statement.close();
            if (conn != null) conn.close();
            if (rs != null) rs.close();
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
    }

    protected static void checkJdbcDriver() {
        //check that the JDBC driver actually works
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "Unable to instantiate driver class - check the string value you set up for it.");
            System.out.println("Unable to instantiate driver class - check the string value you set up for it.");
            cnfe.printStackTrace();
            System.exit(-1); // No driver = can't do squat till it's fixed.
        }
    }

    // and a function to read in a sql file and return a string that can be run as a query
    private static String fetchSQLdata(String filename) {
        String queryToRun = "";
        // we're reading some data, so need to catch any exceptions that might be thrown.
        try {
            // setting up the path and reader of the datafile
            String fpath = "./SetupFiles/" + filename;
            File f = new File(fpath);
            FileReader fr = new FileReader(f);
            BufferedReader bufRead = new BufferedReader(fr);

            String line = bufRead.readLine();
            while (line != null) {
                queryToRun += line;
                line = bufRead.readLine();
            }

            // and close the data stream.
            bufRead.close();
            fr.close();

        } catch (IOException ioe) {
            System.out.println("Something went wrong!\n" + ioe);
        }

        return queryToRun;
    }

    private static void flagSuccessfulSetup(boolean status) {
        // this creates a file the program can check to see if a successful setup has already been completed.
        // TODO: Note in Readme how to flip this manually if the database is created in the server instead of through the code.
        // Need to convert boolean to string value
        String successful;
        if (status) { successful = "TRUE"; } else { successful = "FALSE"; }

        try {
            // setting up the path and reader of the datafile
            String fpath = "./data/dbFlag.txt";
            File f = new File(fpath);
            FileWriter fW = new FileWriter(f);
            BufferedWriter bufWrite = new BufferedWriter(fW);

            bufWrite.write(successful);

            // and close the data stream.
            bufWrite.close();
            fW.close();

        } catch (IOException ioe) {
            System.out.println("Unable to update database creation flag.\n" + ioe);
        }
    }

    private static boolean getDBflag() {
        // reads dbFlag.txt and returns true/false depending on what's recorded there.
        boolean alreadySetup = false;  // defensively presuming the database isn't already there.
        try {
            // setting up the path and reader of the datafile
            String fpath = "./data/dbFlag.txt";
            File f = new File(fpath);
            FileReader fR = new FileReader(f);
            BufferedReader bufRead = new BufferedReader(fR);

            String line = bufRead.readLine();
            if (line.equalsIgnoreCase("true")) {
                alreadySetup = true;
            }

            // and close the data stream.
            bufRead.close();
            fR.close();

        } catch (IOException ioe) {
            System.out.println("Unable to verify current database setup status.\n" + ioe);
        }

        return alreadySetup;
    }

}
