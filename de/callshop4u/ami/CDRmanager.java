/**
 * AsteriskAmiLauscher füllt über updateStartDB() und updateEndDB() die Tabelle cdr_bill
 * updateAbgerechnetDB() setzt das Flag
 *
 * UniqueID (Unixtime) ist die Startzeit des Calls, NICHT die von Answer()
 *
 */
package de.callshop4u.ami;

import de.callshop4u.DBconn;
import de.callshop4u.panels.RootPanel;
import de.callshop4u.zeug.Tarifsuche;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import org.apache.log4j.Logger;

public class CDRmanager {

    private static CDRmanager cdr;
    private static RootPanel rootPanel = RootPanel.getInstance();
    private static Hashtable<String, String> clearedUnixtime = new Hashtable<String, String>();
    private static Hashtable<String, Integer> clearedPrice = new Hashtable<String, Integer>();
    private static Hashtable<String, Integer> clearedAnzahl = new Hashtable<String, Integer>();
    private static Logger logger = Logger.getRootLogger();

    private CDRmanager() {
        // Singleton
    }

    /**
     * Start des Anrufs
     * 
     * @param src
     * @param unixtime
     */
    public void updateStartDB(String src, String unixtime) {
        if(unixtime == null) {
            unixtime = "0";
        }
//        System.out.println("cID in CDRmanager.updateStartDB: " + src);
//        System.out.println("cID in CDRmanager.clearedUnixtime.get(src): " + clearedUnixtime.get(src));
        // Map Methode aufrufen
        if (clearedUnixtime.get(src) == null) {
            clearedUnixtime.put(src, unixtime);
        }

        try {
            Statement stmt = DBconn.con.createStatement();
            stmt.executeUpdate("INSERT INTO cdr_bill(src,unixtime,flag) VALUES('" + src + "','" + unixtime + "','false')");
        } catch (SQLException ex) {
//            AstGUI.getInstance().setSqlStatus('r');
            logger.error(ex.getMessage());
        }
        // Bescheid geben
        synchronized (cdr) {
            rootPanel.changePhoneTableStart(src);
        }
    }

    /**
     * Ende des Anrufs
     *
     * @param src
     * @param dst
     * @param unixtime
     * @param start
     * @param end
     * @param billsec
     * @param disposition
     */
    public void updateEndDB(String src, String dst, String unixtime, Date start, Date end, int billsec, String disposition) {
        System.out.println("---------------------------");
        // 
        String startString = "";
        String endString = "";
        int billmin = 0;
        Tarifsuche such = new Tarifsuche();
        String[] temp = such.startSuch(dst);
        String preis = temp[2];
        float totalPrice = Float.valueOf(preis);
        int totalCent = 0;
        // Anruf erfolgreich?
        if(start != null) {
            billmin = (billsec +60) / 60; // Sekunden zu Minuten
        } 
        // also if call canceled
        totalPrice = billmin * totalPrice;
        
        DateFormat dfStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // Anruf erfolgreich?
        if(start != null) {
            // Nachkommastellen weg
            totalPrice = Math.round(totalPrice * 100);
            totalPrice /= 100; // Cent
            totalCent = Math.round(totalPrice);
            // call has a beginning
            startString = dfStart.format(start);
//            startString = StringToDate.getDate(start);
        }
        endString = dfStart.format(end);
//        endString = StringToDate.getDate(end);
        
        // Map Methoden aufrufen
        updateClearedPrice(src, totalCent);
        updateClearedAnzahl(src);
        //
        try {
            Statement stmt = DBconn.con.createStatement();
            stmt.executeUpdate("UPDATE cdr_bill SET dest='" + dst + "', preis='" + preis + "', start_bill='" + startString + "', end_bill='" + endString + "', billsec='" + billmin + "', disposition='" + disposition + "', total='" + totalCent + "', flag='false' WHERE src='" + src + "' AND unixtime='" + unixtime + "'");
        } catch (SQLException ex) {
//            AstGUI.getInstance().setSqlStatus('r');
            logger.error(ex.getMessage());
        }
        // RootPanel Bescheid geben
        synchronized (cdr) {
            rootPanel.changePhoneTableEnd(src);
            rootPanel.changePhoneTableTotal(src, totalCent);
        }
    }

    // benutzt vom PhonePanel
    public void updateAbgerechnetDB(String src) {
        try {
            Statement sUpdate = DBconn.con.createStatement();
            sUpdate.executeUpdate("UPDATE cdr_bill SET flag='true' WHERE src='" + src + "' AND flag='false'");
            updateClearedDB(src);
        } catch (SQLException ex) {
            System.out.println("hier soll er nicht hin ------------------");
//            AstGUI.getInstance().setSqlStatus('r');
            logger.error(ex.getMessage());
        }
        synchronized (cdr) {
            rootPanel.changePhoneTableAbgerechnet(src);
        }
    }

    /**
     * die, die die cleared updatet
     * von this.updateAbgerechnetDB() aufgerufen
     */
    private void updateClearedDB(String src) {
        if (clearedUnixtime.get(src) != null && !clearedUnixtime.get(src).isEmpty()) {
            try {
                Statement cUpdate = DBconn.con.createStatement();
                cUpdate.executeUpdate("INSERT INTO cleared VALUES ('" + src + "','" + clearedUnixtime.get(src) + "','" + clearedPrice.get(src) + "','" + clearedAnzahl.get(src) + "')");

                clearedUnixtime.remove(src);
                clearedPrice.remove(src);
                clearedAnzahl.remove(src);
            } catch (SQLException ex) {
//                AstGUI.getInstance().setSqlStatus('r');
                logger.error(ex.getMessage());
            }
        }
    }

    /**
     * Methoden die die Map'src aktualisieren
     * 
     */
    private void updateClearedPrice(String src, Integer val) {
        if (clearedPrice.get(src) == null || clearedPrice.get(src).intValue() < 1) {
            clearedPrice.put(src, 0);
        }
        Integer e = clearedPrice.get(src) + val;
        clearedPrice.put(src, e);
    }

    private void updateClearedAnzahl(String src) {
        if (clearedAnzahl.get(src) == null) {
            clearedAnzahl.put(src, 0);
        }
        Integer e = clearedAnzahl.get(src) + 1;
        clearedAnzahl.put(src, e);
    }

    /**
     * producer Methode
     */
    public static synchronized CDRmanager getInstance() {
        if (cdr != null) {
            return cdr;
        } else {
            cdr = new CDRmanager();
            return cdr;
        }
    }
}
