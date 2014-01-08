package de.callshop4u;
/*
 * ShowTarif.java
 *
 * Fenster das die Tarifliste in einer Tabelle anzeigt
 * erfordert Tabelle tarif in DB asterisk (areacode, land, price)
 */

import java.sql.*;
import java.util.ResourceBundle;
import javax.swing.*;
import org.apache.log4j.Logger;

public class ShowTarif extends JFrame {

    private static ShowTarif showTarif;
    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    private String[] colNames = {langBundle.getString("vorwahl"), langBundle.getString("ort"), langBundle.getString("preis")};
    private String[][] daten;
    private static Logger logger = Logger.getRootLogger();

    private ShowTarif() {
        // Singleton
        setTitle(langBundle.getString("tarife"));
        setIconImage(astgui.getFavImage());
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(null);
        setBounds(200, 200, 500, 800);
        setGui();
    }

    /**
     * SQL Methode um daten zu fuellen
     */
    private void rufTarif() {
        int rowCnt = 0;
        try {
            Statement rufSt = DBconn.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rsT = rufSt.executeQuery("SELECT * FROM tarif ORDER BY 1");
            rsT.first();
            rsT.last();
            rowCnt = rsT.getRow();
            daten = new String[rowCnt][3];
            rsT.first();

            for (int i = 1; i <= rowCnt; i++) {
                for (int e = 1; e <= 3; e++) {
                    daten[i - 1][e - 1] = rsT.getString(e);
                }
                rsT.next();
            }
        } catch (SQLException e) {
            AstGUI.getInstance().setSqlStatus('r');
            JOptionPane.showMessageDialog(null, langBundle.getString("keineDbVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
            logger.error(e.getMessage());
        }
    }

    private void setGui() {
        rufTarif();
        JTable tarifTable = new JTable(daten, colNames);
        JScrollPane tarifScroll = new JScrollPane(tarifTable);
        tarifScroll.setBounds(5, 10, 460, 700);
        add(tarifScroll);
    }

    /**
     * producer Methode
     */
    public static ShowTarif getInstance() {
        if (showTarif != null) {
            return showTarif;
        } else {
            showTarif = new ShowTarif();
            return showTarif;
        }
    }
}
