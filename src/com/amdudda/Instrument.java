package com.amdudda;

import java.sql.*;

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
    private int InstID;
    private String InstName;
    private String InstType;
    private String Subtype;
    private java.sql.Date AcquiredDate;
    private int AcquiredFrom;
    private double PurchasePrice;
    private double InsuranceValue;
    private String Location;
    private double Height;
    private double Width;
    private double Depth;
    private String Region;
    private String Country;
    private String Culture;
    private String Tuning;
    private String LowNote;
    private String HighNote;
    private String Description;
    private boolean isALoan;
    private LocationInfo locationInfo;
    private Contact acquisitionInfo;

    // misc attributes for data retrieval
    ResultSet my_instrument;

    // and a constructor so we can pass instrument attributes to the editing screen and pass values back to the databae
    public Instrument(String recID) {
        if (recID != null) {
            PreparedStatement psFetch = null;
            // only bother populating the values if a recordID is actually passed to the constructor.
            try {
                psFetch = Database.conn.prepareStatement("SELECT * FROM " + INSTRUMENT_TABLE_NAME +
                        " WHERE " + INSTID + " = ?");
                psFetch.setInt(1, Integer.parseInt(recID));
                my_instrument = psFetch.executeQuery();
                my_instrument.next();
            } catch (SQLException sqle) {
                System.out.println("Unable to fetch instrument info.\n" + sqle);
            }
            // then try to parse it.
            try {
                // I map these without worrying about field order in the table.
                InstID = Integer.parseInt(my_instrument.getObject(INSTID).toString());
                //System.out.println("Instrument id is: " + InstID);
                InstName = fetchValueOfString(INSTNAME);
                InstType = fetchValueOfString(INSTTYPE);
                Subtype = fetchValueOfString(SUBTYPE);
                AcquiredDate = Date.valueOf(my_instrument.getObject(ACQUIREDDATE).toString());
                AcquiredFrom = Integer.parseInt(my_instrument.getObject(ACQUIREDFROM).toString());
                PurchasePrice = fetchValueOfDouble(PURCHASEPRICE);
                InsuranceValue = fetchValueOfDouble(INSURANCEVALUE);
                Location = fetchValueOfString(LOCATION);
                Height = fetchValueOfDouble(HEIGHT);
                Width = fetchValueOfDouble(WIDTH);
                Depth = fetchValueOfDouble(DEPTH);
                Region = fetchValueOfString(REGION);
                Country = fetchValueOfString(COUNTRY);
                Culture = fetchValueOfString(CULTURE);
                Tuning = fetchValueOfString(TUNING);
                LowNote = fetchValueOfString(LOWNOTE);
                HighNote = fetchValueOfString(HIGHNOTE);
                Description = fetchValueOfString(DESCRIPTION);
                isALoan = (my_instrument.getObject(ISALOAN) == null) ? false : Boolean.parseBoolean(my_instrument.getObject(ISALOAN).toString());

                // and close my query now that I've gathered my data...
                my_instrument.close();
                if(psFetch != null) psFetch.close();

            } catch (SQLException sqle) {
                System.out.println("Unable to assign instrument attributes.\n" + sqle);
            }

            // try fetching location info if it exists
            createLocationInfo();

            // try fetching contact info
            acquisitionInfo = new Contact(this.getAcquiredFromAsString());
        }
    }

    protected void createLocationInfo() {
        if (Location.equals(DataValidator.LOC_EXHIBIT)) {
            // on Exhibit:
            locationInfo = new OnExhibit(this.InstID);
        } else if (Location.equals(DataValidator.LOC_LOAN)) {
            // on Loan:
            locationInfo = new Loan(this.InstID,this.AcquiredFrom);
        } else {
            // in library or storage:
            locationInfo = new StorageLibrary(this.InstID);
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
                    INSTNAME + "," +
                    INSTTYPE + "," +
                    SUBTYPE + "," +
                    LOCATION +
                    " FROM " + INSTRUMENT_TABLE_NAME;
            dataToBrowse = bts.executeQuery(sqlToRun);
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch data for table.\n" + sqle);
        }
        return dataToBrowse;
    }

    protected static ResultSet getInstrumentData(int priKey, PreparedStatement s) {
        ResultSet instrumentData = null;
        String sqlToRun = "SELECT * FROM " + INSTRUMENT_TABLE_NAME +
                " WHERE " + INSTID + " = ?";
        try {
            s = Database.conn.prepareStatement(sqlToRun);
            s.setInt(1, priKey);
            instrumentData = s.executeQuery();

        } catch (SQLException sqle) {
            System.out.println("Unable to fetch instrument data for PK = " + priKey);
            System.out.println(sqle);
        }
        return instrumentData;
    }

    // getters and setters for Instrument attributes

    public int getInstID() {
        return InstID;
    }

    public void setInstID(int instID) {
        InstID = instID;
    }

    public String getInstName() {
        return InstName;
    }

    public void setInstName(String instName) {
        InstName = instName;
    }

    public String getInstType() {
        return InstType;
    }

    public void setInstType(String instType) {
        InstType = instType;
    }

    public String getSubtype() {
        return Subtype;
    }

    public void setSubtype(String subtype) {
        Subtype = subtype;
    }

    public Date getAcquiredDate() {
        return AcquiredDate;
    }

    public void setAcquiredDate(Date acquiredDate) {
        AcquiredDate = acquiredDate;
    }

    public int getAcquiredFrom() {
        return AcquiredFrom;
    }

    public String getAcquiredFromAsString() {
        return "" + AcquiredFrom;
    }

    public void setAcquiredFrom(int acquiredFrom) {
        AcquiredFrom = acquiredFrom;
    }

    public double getPurchasePrice() {
        return PurchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        PurchasePrice = purchasePrice;
    }

    public double getInsuranceValue() {
        return InsuranceValue;
    }

    public void setInsuranceValue(double insuranceValue) {
        InsuranceValue = insuranceValue;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public double getWidth() {
        return Width;
    }

    public void setWidth(double width) {
        Width = width;
    }

    public double getDepth() {
        return Depth;
    }

    public void setDepth(double depth) {
        Depth = depth;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCulture() {
        return Culture;
    }

    public void setCulture(String culture) {
        Culture = culture;
    }

    public String getTuning() {
        return Tuning;
    }

    public void setTuning(String tuning) {
        Tuning = tuning;
    }

    public String getLowNote() {
        return LowNote;
    }

    public void setLowNote(String lowNote) {
        LowNote = lowNote;
    }

    public String getHighNote() {
        return HighNote;
    }

    public void setHighNote(String highNote) {
        HighNote = highNote;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isALoan() {
        return isALoan;
    }

    public void setALoan(boolean ALoan) {
        isALoan = ALoan;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public Contact getAcquisitionInfo() {
        return acquisitionInfo;
    }



    // some data processing
    private String fetchValueOfString(String fieldname) {
        // This method reads in a value and translates nulls into an empty string so the constructor doesn't choke.
        try {
           return (my_instrument.getObject(fieldname) == null) ? "" : my_instrument.getObject(fieldname).toString();
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch value of " + fieldname + ".\n" + sqle);
            return "?????";
        }
    }

    private double fetchValueOfDouble(String fieldname) {
        // As with fetch...String, the point is to handle nulls gracefully.
        try {
            return (my_instrument.getObject(fieldname) == null) ? 0 : Double.parseDouble(my_instrument.getObject(HEIGHT).toString());
        } catch (SQLException sqle) {
            System.out.println("Unable to fetch value of " + fieldname + ".\n" + sqle);
            return -1;
        }
    }
}
