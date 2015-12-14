# MusicologyInstitute
Final project for Java class.  Musical Instrument Museum using mySQL database.

* MUSICOLOGY INSTITUTE DATABASE README *


** PROGRAM DESCRIPTION **

A Java program that manages a database for a fictional ethnomusicological museum.  There are instruments in storage, on exhibit, and other places, and contacts the museum has acquired the instruments from.


** DATABASE SETUP **

*** To set up the database, you will need to do one of the following ***

-- To create a new, empty database -- 

1. Change the hardcoded username and password in the Database object in my code.

2. Change the contents of \data\dbFlag.txt to say "false" (no quotes).

3. Run the program.

4. If you want to play with test data, you will need to run the queries in \SetupFiles\DataDump\ (do this in mySQL) after the database has been successfully set up.

-- To set up the database with test data before running the program: --

1. Create the MusicologyInstitute database in mySQL.

2. Run the queries in \SetupFiles\DataDump\ after the database has been successfully set up.

3. Change the hardcoded username and password in the Database object in my code.

** COOL THINGS **

My biggest challenge was getting data to persist across screens, because Java passes references, not values, and the references disappear after "dispose()".  I'm probaby proudest of the LocationInfoForm, which actually works with three different tables.  The goal was for that to be transparent to the end users; end users just care about supplying the location information, not about how it's stored.  I fudged by using a hashmap and a method to build a new location object using the hashmap's data.

I also had to figure out how to create records and link them to foreign keys in child tables.  UpdateInstrument uses code to enable mySQL to return the relevant primary key so that location information can be added along with the new instrument.  (The URL that showed me how is attributed in the addIntrument method.)


** KNOWN BUGS AND ISSUES **

Username and password are hard-coded and stored as plaintext.

There is no easy way to add instruments to an exhibit; it can be done on a per-instrument basis, but not directly from the screen to edit exhibit information.

There is no ability to add accounting information to the database - so no way to list a purchase price or an insurance value for an instrument.

