package com.amdudda;

public class Main {

    public static void main(String[] args) {
	// write your code here

        // check for the JDBC driver - if it's not there, nothing in this code will work.
        Database.checkJdbcDriver();

        Database.setupDatabase();

        Database.openConnStatement();
        //MainMenuGUI mMG = new MainMenuGUI();

        Loan loan = new Loan(7,1);
        System.out.println((loan.toString()));

        // and close everything when the program exits - now taken care of in Main Menu screen.
        //Database.closeConnStatement();

    }
}
