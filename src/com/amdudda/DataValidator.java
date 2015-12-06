package com.amdudda;

import java.time.Year;

/**
 * Created by amdudda on 12/5/15.
 */
public class DataValidator {
    // this is a helper object to validate data input


    public static boolean isDate(String date) {
        // This checks for a year between 1995 and next year, and that month and date are at least vaguely plausible values.
        try {
            String[] dateparts = date.split("-");
            if (dateparts.length != 3) return false;
            int year = Integer.parseInt(dateparts[0]);
            int month = Integer.parseInt(dateparts[1]);
            int day = Integer.parseInt(dateparts[2]);
            return (year >= 1995 && year <= Integer.parseInt(Year.now().toString()) + 1 ) &&
                    (month > 0 && month < 13) &&
                    (day > 0 && day <= 31);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCurrency(String weight) {
        // how to split string at a dot: http://javadevnotes.com/java-string-split-dot-examples
        // this validates our Decimal(12,2) data types.
        boolean verdict;
        try {
            System.out.println(weight);
            String[] wtsplit = weight.split("\\.");
            if (wtsplit.length == 2) {
                verdict = (wtsplit[1].length() < 10 && Integer.parseInt(wtsplit[0]) < 100);
            } else {
                verdict = (Integer.parseInt(wtsplit[0]) < 1000);
            }
        } catch (Exception e) {
            //System.out.println(e);
            verdict = false;
        }
        return verdict;
    }

    public static boolean isNote(String note) {
        // verifies a note is either a letter plus a number or a letter plus a number with either
        // 'f' or 's' (flat or sharp) between them
        char firstchar,midchar,lastchar;
        boolean verdict = false;
        if (note.length() == 2) {
            firstchar = note.toUpperCase().charAt(0);
            lastchar = note.charAt(1);
            verdict = ( (firstchar >= 65 && firstchar <= 90) && (lastchar >= 48 && lastchar <= 57) );
        } else if (note.length() == 3) {
            firstchar = note.toUpperCase().charAt(0);
            midchar = note.toLowerCase().charAt(1);
            lastchar = note.charAt(2);
            // 'f' is ascii char 102, 's' is ascii char 115
            verdict = ( (firstchar >= 65 && firstchar <= 90) && (lastchar >= 48 && lastchar <= 57)
                        && (midchar == 102 || midchar == 115));
        }
        return verdict;
    }

    public static boolean isDouble(String zifr) {
        try {
            Double.parseDouble(zifr);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
