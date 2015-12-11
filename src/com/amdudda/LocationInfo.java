package com.amdudda;

/**
 * Created by amdudda on 12/10/15.
 */
public abstract class LocationInfo {
    // done so we can pass toString info to Instrument object.
    protected int InstID;

    public int getInstID() {
        return InstID;
    }

    public void setInstID(int instID) {
        InstID = instID;
    }
}
