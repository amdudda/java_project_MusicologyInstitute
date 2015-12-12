package com.amdudda;

import javax.swing.*;

/**
 * Created by amdudda on 12/12/15.
 */
public abstract class ContactManager extends JFrame {
    // helper object so that objects can share the SelectContactScreen

    public abstract void setAcquiredFromTextField(String id);
}
