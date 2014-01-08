package de.callshop4u;
/*
 * DBconn.java
 *
 * Zugangsdaten speichern und DB-Verbindung testen
 */

import java.sql.*;
import org.apache.log4j.Logger;

public class DBconn {

    static String erfMeld;
    static String erfMeld2;
    static String erfMeld3;
    public static Connection con = null;
    private static DatabaseMetaData dbtest = null;
    private static Logger logger = Logger.getRootLogger();

    static void openDB(String sDbUrl, String sUsr, String sPwd) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + sDbUrl, sUsr, sPwd);
            AstGUI.getInstance().setSqlStatus('g');
            erfMeld = "Verbindung hergestellt";
        } catch (ClassNotFoundException e) {
            AstGUI.getInstance().setSqlStatus('r');
            erfMeld = ("DB Treiber nicht gefunden: " + e.getMessage());
            logger.error(e.getMessage());
        } catch (SQLInvalidAuthorizationSpecException e) {
            AstGUI.getInstance().setSqlStatus('r');
            erfMeld = ("falsche Zugangsdaten: " + e.getErrorCode());
            logger.error(e.getMessage());
        } catch (SQLException e) {
            AstGUI.getInstance().setSqlStatus('r');
            erfMeld = ("Datenbankfehler, Code: " + e.getErrorCode());
            System.err.println("user-false: " + sUsr + "\nPass-false: " + sPwd + "\nHost-false: " + sDbUrl);
            logger.error(e.getMessage());
        }
    }

// --- Datenbank schliessen -------
    static void closeDB() {
        try {
            con.close();
            erfMeld2 = ("DB geschlossen");
            AstGUI.getInstance().setSqlStatus('r');
        } catch (SQLException e) {
            erfMeld2 = ("Datenbankfehler beim Schließen der Verbindung, Code: " + e.getMessage());
            logger.error(e.getMessage());
        }
    }

//--- Datenbank testen -------
    static void test() {
        try {
            dbtest = con.getMetaData();
            erfMeld3 = ("verbunden mit: " + dbtest.getURL().toString());
        } catch (Exception e) {
            erfMeld3 = ("keine Verbindung");
            logger.error(e.getMessage());
        }
    }

    static boolean isConnected() {
        try {
            dbtest = con.getMetaData();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
