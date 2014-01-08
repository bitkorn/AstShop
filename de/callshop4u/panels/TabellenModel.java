package de.callshop4u.panels;

import de.callshop4u.AstGUI;
import de.callshop4u.DBconn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;

/**
 *
 * @author allapow
 */
public class TabellenModel extends AbstractTableModel {

    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    private static Logger logger = Logger.getRootLogger();

    private String[] columnNames = {langBundle.getString("ziel"), langBundle.getString("preis"), langBundle.getString("start"), langBundle.getString("ende"), langBundle.getString("zeit"), langBundle.getString("ergebnis"), langBundle.getString("total")};
    /*
     * Klassendefinitionen für getColumnClass()
     */
    private Class[] columnClasses = new Class []{String.class, Float.class, String.class, String.class, Float.class, String.class, Float.class};
    private Object[][] data;
    private String query;

    TabellenModel(String src) {
        query = "SELECT dest,preis,unixtime,start_bill,end_bill,billsec,disposition,total FROM cdr_bill WHERE src='"+src+"' AND flag='false'";
        // Hier muß die Prüfung auf nicht abgerechnete Daten statt finden!
        updateTable(); // Tabellen beim Start füllen
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * überflüssig weil das editieren verboten wird
     *
     * @param aValue
     * @param rowIndex
     * @param columnIndex
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = aValue;
    }

    /**
     * erneuert den Inhalt in der Tabelle beim CallStart und CallEnd
     * über die Datenbank.cdr_bill
     */
    void updateTable() {
        Connection con = DBconn.con;
        int amount = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(query);
            result.last();
            amount = result.getRow();
            data = new Object[amount][7];
            result.beforeFirst();
                for (int i = 0; result.next(); i++) {
                    data[i][0] = result.getString("dest");
                    data[i][1] = result.getFloat("preis");
                    data[i][2] = result.getString("start_bill");
                    data[i][3] = result.getString("end_bill");
//                    Integer sec = result.getInt("billsec");
//                    data[i][4] = (sec % 60f) / 100 + sec / 60;
                    data[i][4] = result.getInt("billsec");
                    data[i][5] = result.getString("disposition");
                    data[i][6] = result.getFloat("total");
                }
        } catch (SQLException ex) {
            AstGUI.getInstance().setSqlStatus('r');
            JOptionPane.showMessageDialog(null, langBundle.getString("keineDbVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage());
        } finally {
            fireTableDataChanged();
        }
    }

    @Override
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    
}
