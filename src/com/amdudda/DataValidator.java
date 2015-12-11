package com.amdudda;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;

/**
 * Created by amdudda on 12/5/15.
 */
public class DataValidator {
    // this is a helper object to validate data input
    /*
     some constants for database constraints used in comboboxes, to make them easy to find when they are changed
     because mySQL doesn't actually store CHECK constraints - it runs them and disposes them:
     https://bugs.mysql.com/bug.php?id=3464 and lots of duplicate bug reports.
    */
    public static final String[] TUNING_TYPES = {"Fixed", "Specified", "Variable", "Untuned"};
    public static final String[] STORAGE_LOCATIONS = {"Exhibit", "Library", "Storage", "On Loan"};
    public static final String[] INSTRUMENT_TYPES = {"Idiophone", "Membranophone", "Chordophone", "Aerophone", "Electrophone", "Hybrid"};
    public static final String[] CONTACT_TYPES = {"Donor", "Museum", "Seller", "Musician", "School", "Private Individual"};
    public static final String[] REGIONS = {"Europe", "Asia", "Africa", "Australia", "The Americas", "Oceania"};
    public static final String[] LOCATIONS_IN_ROOM = {"North", "South", "East", "West", "Floor",
            "NW Corner","SW Corner","SE Corner","NE Corner"};

    // some data validators
    public static boolean isDate(String date) {
        // This checks for a year between 1995 and next year, and that month and date are at least vaguely plausible values.
        try {
            String[] dateparts = date.split("-");
            if (dateparts.length != 3) return false;
            int year = Integer.parseInt(dateparts[0]);
            int month = Integer.parseInt(dateparts[1]);
            int day = Integer.parseInt(dateparts[2]);
            return (year >= 1995 && year <= Integer.parseInt(Year.now().toString()) + 1 ) &&
                    (month > 0 && month < 13) &&
                    (day > 0 && day <= 31);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCurrency(String weight) {
        // how to split string at a dot: http://javadevnotes.com/java-string-split-dot-examples
        // this validates our Decimal(12,2) data types.
        boolean verdict;
        try {
            System.out.println(weight);
            String[] wtsplit = weight.split("\\.");
            if (wtsplit.length == 2) {
                verdict = (wtsplit[1].length() < 10 && Integer.parseInt(wtsplit[0]) < 100);
            } else {
                verdict = (Integer.parseInt(wtsplit[0]) < 1000);
            }
        } catch (Exception e) {
            //System.out.println(e);
            verdict = false;
        }
        return verdict;
    }

    public static boolean isNote(String note) {
        // verifies a note is either a letter plus a number or a letter plus a number with either
        // 'f' or 's' (flat or sharp) between them
        char firstchar,midchar,lastchar;
        boolean verdict = false;
        if (note.length() == 2) {
            firstchar = note.toUpperCase().charAt(0);
            lastchar = note.charAt(1);
            verdict = ( (firstchar >= 65 && firstchar <= 90) && (lastchar >= 48 && lastchar <= 57) );
        } else if (note.length() == 3) {
            firstchar = note.toUpperCase().charAt(0);
            midchar = note.toLowerCase().charAt(1);
            lastchar = note.charAt(2);
            // 'f' is ascii char 102, 's' is ascii char 115
            verdict = ( (firstchar >= 65 && firstchar <= 90) && (lastchar >= 48 && lastchar <= 57)
                        && (midchar == 102 || midchar == 115));
        }
        return verdict;
    }

    public static boolean isDouble(String zifr) {
        try {
            Double.parseDouble(zifr);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    // also some generators for comboboxes

    // a generic one for things database constraints don't use
    public static void generateComboBox(JComboBox cBox, String[] listToUse) {
        for (int i = 0; i < listToUse.length; i++) {
            cBox.addItem(listToUse[i]);
        }
    }

    public static ArrayList<String> getCountries() {
        ArrayList<String> countryList = new ArrayList<String>();
        try {
            String sqlToRun = "SELECT * FROM Countries";
            Statement s = Database.conn.createStatement();
            ResultSet r = s.executeQuery(sqlToRun);
            while (r.next()) {
                countryList.add(r.getObject("CountryName").toString());
            }

            r.close();
            s.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch list of countries from database.\n" + sqle);
        }
        return countryList;
    }

    public static void generateCountryComboBox(JComboBox cBox) {
        ArrayList<String> countryList = getCountries();
        // in reverse order to put USA at top of list.
        countryList.add(0,"Mexico");
        countryList.add(0,"Canada");
        countryList.add(0,"United States");
        for (int i = 0; i < countryList.size(); i++) {
            cBox.addItem(countryList.get(i));
        }
    }

    public static void generateStateComboBox(JComboBox cBox) {
        // ideally, we'd show the full name next to the abbreviation, but for now, just do abbrevs.
        // not bothering with declaring constants, as this is the only place this table even gets used.
        String sqlToRun = "SELECT Abbreviation FROM States";
        Statement s = null;
        ResultSet r = null;
        try {
            s = Database.conn.createStatement();
            r = s.executeQuery(sqlToRun);
            while (r.next()) {
                cBox.addItem(r.getObject("Abbreviation").toString());
            }
            r.close();
            s.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to extract list of states.\n" + sqle);
        }
    }

    public static void generateRoomComboBox(JComboBox cBox) {
        String sqlToRun = "SELECT RoomNum FROM Rooms";
        Statement s = null;
        ResultSet r = null;
        try {
            s = Database.conn.createStatement();
            r = s.executeQuery(sqlToRun);
            while (r.next()) {
                cBox.addItem(r.getObject("RoomNum").toString());
            }
            r.close();
            s.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to extract list of room numbers.\n" + sqle);
        }
    }

    public static void generateSearchFields(JComboBox cBox, AbstractTableModel atm) {
        for (int i=0; i<atm.getColumnCount(); i++) {
            cBox.addItem(atm.getColumnName(i));
        }
    }
}
