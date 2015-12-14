package com.amdudda;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
	// write your code here

        // check for the JDBC driver - if it's not there, nothing in this code will work.
        Database.checkJdbcDriver();

        // have user supply username and password for the database connection
        Login login = new Login();

        // and close everything when the program exits - now taken care of in Main Menu screen.
        //Database.closeConnStatement();

    }
}
