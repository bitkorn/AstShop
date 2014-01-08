package de.callshop4u;
/*
 * Phone.java
 *
 * zaehlen vorhandener Phones anhand der src(Source, Caller-ID-Nummer)
 */

import java.sql.*;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.apache.log4j.Logger;

public class Phone {

    private static LinkedHashSet<String> phoneSet = new LinkedHashSet<String>();
    private static Statement phonSt = null;
    private static int anzPh = 0;
    private static String sqlZaehl = "SELECT src FROM cdr ORDER BY src";
    private static Logger logger = Logger.getRootLogger();

    /**
     * count phones, count write to anzPh
     */
    public static void initPhones() {
        try {
            phonSt = DBconn.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rsP = phonSt.executeQuery(sqlZaehl);

            rsP.last();
            int cCount = rsP.getRow();
            rsP.first();
            phoneSet.add(rsP.getString(1));

            for (int i = 1; i < cCount; i++) {
                rsP.next();
                phoneSet.add(rsP.getString(1));
                anzPh = phoneSet.size();
            }
            rsP.close();
            phonSt.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Namen der Telefone zurueck geben
     * initGetAnzPh() muß vorher aufgerufen worden sein
     */
    public static String[] getPhoneNames() {
        Iterator<String> iter = phoneSet.iterator();
        String [] phones = new String[phoneSet.size()];
        for (int i = 0; iter.hasNext(); i++) {
            phones[i] = iter.next();
        }
        return phones;
    }
    /**
     * initPhones() aufrufen und
     * Anzahl der Phones zurueck geben
     */
    public static int initGetAnzPh() {
        initPhones();
        return anzPh;
    }
    /**
     * Anzahl der Phones zurueck geben
     * initGetAnzPh() muß vorher aufgerufen worden sein
     */

    public static int getAnzPh() {
        return anzPh;
    }
}
