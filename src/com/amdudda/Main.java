package com.amdudda;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
	// write your code here

        // check for the JDBC driver - if it's not there, nothing in this code will work.
        Database.checkJdbcDriver();

        // have user supply username and password for the database connection
        //Login login = new Login();

        //Database.setupDatabase();
        Database.username = "amdudda";
        Database.pwd = "tanuki";
        Database.openConnStatement();  // have to reopen connection because
        // (a) dbsetup closes it, and (b) if dbFlag.txt set to true, it never actually fires up.

        MainMenuGUI mMG = new MainMenuGUI();

        // and close everything when the program exits - now taken care of in Main Menu screen.
        //Database.closeConnStatement();

    }
}
