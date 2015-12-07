package com.amdudda;

import java.sql.PreparedStatement;
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

    // variables to store object attributes
    private int ContactID;
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

    ResultSet my_contact;

    // constructor for contact info
    public Contact(String pkToUse) {
        // first, fetch the data
        PreparedStatement stmt = null;
        try {
            String sqlToRun = "SELECT * FROM " + CONTACT_TABLE_NAME + " WHERE " + CONTACTID + " = ? ";
            stmt = Database.conn.prepareStatement(sqlToRun);
            stmt.setInt(1,Integer.parseInt(pkToUse));
            my_contact = stmt.executeQuery();
            my_contact.next();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch contact info.\n" + sqle);
        }
        // then try to parse it.
        try {
            ContactID = Integer.parseInt(my_contact.getObject(CONTACTID).toString());
            ContactName = fetchValueOfString(CONTACTNAME);
            BusinessName = fetchValueOfString(BUSINESSNAME);
            Address = fetchValueOfString(ADDRESS);
            City = fetchValueOfString(CITY);
            State = fetchValueOfString(STATE);
            PostalCode = fetchValueOfString(POSTALCODE);
            Country = fetchValueOfString(COUNTRY);
            BusinessPhone = fetchValueOfString(BUSINESSPHONE);
            ContactPhone = fetchValueOfString(CONTACTPHONE);
            ContactType = fetchValueOfString(CONTACTTYPE);
            Notes = fetchValueOfString(NOTES);
            // don't forget to close the resultset & statement in case they're still hanging out there for some reason.
            my_contact.close();
            if (stmt != null) stmt.close();
        } catch (SQLException sqle) {
            System.out.println("Unable to assign object attributes.\n" + sqle);
        }
    }

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

    private String fetchValueOfString(String fieldname) {
        // This method reads in a value and translates nulls into an empty string so the constructor doesn't choke.
        try {
            return (my_contact.getObject(fieldname) == null) ? "" : my_contact.getObject(fieldname).toString();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch value of " + fieldname + ".\n" + sqle);
            return "?????";
        }
    }

    // getters and setters for attributes

    public int getContactID() {
        return ContactID;
    }

    public void setContactID(int contactID) {
        ContactID = contactID;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getBusinessPhone() {
        return BusinessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        BusinessPhone = businessPhone;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String contactPhone) {
        ContactPhone = contactPhone;
    }

    public String getContactType() {
        return ContactType;
    }

    public void setContactType(String contactType) {
        ContactType = contactType;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}
