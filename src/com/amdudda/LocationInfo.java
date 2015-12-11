package com.amdudda;

/**
 * Created by amdudda on 12/10/15.
 */
public abstract class LocationInfo {
    // done so we can pass toString info to Instrument object.
    protected int InstID;

    // need away to update database for these objects.
    public abstract void updateRecord();
    public abstract void insertRecord();

    public int getInstID() {
        return InstID;
    }

    public void setInstID(int instID) {
        InstID = instID;
    }
}
