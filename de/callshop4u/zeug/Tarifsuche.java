/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.callshop4u.zeug;

import de.callshop4u.DBconn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author allapow
 */
public class Tarifsuche {
    
    private int anzNull = 0;            // Anzahl der Nullen
    private String suchStr = "";	// String der gesucht wird
    private String preis = "";		// Preis der gesucht wird
    private String dest = "";		// Destination die gesucht wird
    private String clearString = "";	// String ohne Nullen
    private Statement stmt = null;	// Statement
    private ResultSet result = null;
    private static Logger logger = Logger.getRootLogger();

    public String[] startSuch(String s) {
        /*
         * Nullen cutten und zaehlen
         * clearString initialisieren
         */
        cutNull(s);
        boolean gefunden = false;
        //
        if(anzNull == 0) {
            try {
                gefunden = suchTarifWorld("49");
                // weil suchTarifWorld("49") suchStr auf "0049" setzt
                // suchStr = die Nummer ohne Vorwahlen
                suchStr = s;
            } catch (SQLException ex) {
//                AstGUI.getInstance().setSqlStatus('r');
                logger.error(ex.getMessage());
            }
        }
        // wenn 1
        // dann in tarif mit vorangestelltem 49 suchen
        // wenn da Ergebnis in vorw_de ohne 49 suchen
        if(anzNull == 1) { 
            try {
                gefunden = suchTarifWorld("49" + clearString);
                if(gefunden) {
                    // suchStr und dest neu setzen
                    gefunden = searchDeDestination(clearString);
                }
            } catch (SQLException ex) {
//                AstGUI.getInstance().setSqlStatus('r');
                logger.error(ex.getMessage());
            }
        }
        
        if(anzNull == 2) { // || clearString.equals("111") || clearString.equals("112")) {
            try {
                    suchTarifWorld(clearString);
                } catch (SQLException e) {
//                    AstGUI.getInstance().setSqlStatus('r');
                    logger.error(e.getMessage());
                }
        }

        String numberS = suchStr;
        String preisS = preis;
        String ziel = dest;
        String[] number = new String[3];
        number[0] = numberS;
        number[1] = ziel;
        number[2] = preisS;
        anzNull = 0;
        return number;
    }
    
    /**
     * Nullen abschneiden und zaehlen
     */
    private void cutNull(String z) {
        String gabe = z;

        if (gabe.equals("")) {
//            JOptionPane.showMessageDialog(null, "nix uebrgeben", "SuchTarif-Meldung", JOptionPane.INFORMATION_MESSAGE);
        } else {
            while (gabe.startsWith("0")) {
                anzNull += 1;
                gabe = gabe.substring(1);
            }
        }
        clearString = gabe;
    }
    

    /**
     * Tarif in der Welt-Datenbank suchen und in Variablen schreiben
     * @throws SQLException
     */
    private Boolean suchTarifWorld(String s) throws SQLException {
        String key = null;
        String cutme = s;
        int l = cutme.length();
        int row = 0;

        for (int i = 0; i < l; i++) {
            key = cutme.substring(0, l - i);
            stmt = DBconn.con.createStatement();
            result = stmt.executeQuery("SELECT * FROM tarif WHERE vorwahl = " + key);
            result.next();
            row = result.getRow();
            if (row != 0) {
                suchStr = "00" + result.getString(1);
                dest = result.getString(2);
                preis = result.getString(3);
                result.close();
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Tarif in der Welt-Datenbank suchen und in Variablen schreiben
     * @throws SQLException
     */
    private Boolean searchDeDestination(String s) throws SQLException {
        String key = null;
        String cutme = s;
        int l = cutme.length();
        int row = 0;

        for (int i = 0; i < l; i++) {
            key = cutme.substring(0, l - i);
            stmt = DBconn.con.createStatement();
            result = stmt.executeQuery("SELECT * FROM vorw_de WHERE vorwahl = " + key);
            result.next();
            row = result.getRow();
            if (row != 0) {
                suchStr = "0" + result.getString(1);
                dest = "DE-" + result.getString(2);
                result.close();
                return true;
            }
        }
        return false;
    }
    
    
}
