package com.amdudda;

public class Main {

    public static void main(String[] args) {
	// write your code here

        // check for the JDBC driver - if it's not there, nothing in this code will work.
        Database.checkJdbcDriver();

        Database.setupDatabase();

        // and close everything when the program exits
        Database.closeConnStatement();

    }
}
