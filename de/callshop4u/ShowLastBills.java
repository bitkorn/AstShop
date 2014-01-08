/*
 * Panel zum anzeigen der letzten Abrechnungen
 */
package de.callshop4u;

import de.callshop4u.zeug.TabellenStringRenderer;
import de.callshop4u.zeug.UnixTimestamp;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;

public class ShowLastBills extends JFrame implements TableModelListener {

    private static ShowLastBills slb;
    private AstGUI astgui = AstGUI.getInstance();
    private GridBagConstraints gc = new GridBagConstraints();
    private Color panBackg = astgui.getPanBackground();
    private Color tabBackg = new Color(143, 152, 255);
    private JPanel panel = new JPanel();
    private LBTableModel tModel;

    private ShowLastBills() {
        // Singleton

        setTitle("last bills");
        setIconImage(astgui.getFavImage());
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(700, 200));
        setPreferredSize(new Dimension(700, 200));
        setBounds(200, 200, 700, 200);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(panBackg);

        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.insets = new Insets(5, 10, 5, 10);
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.fill = GridBagConstraints.BOTH;

        tModel = new LBTableModel();
        makeTable();
        add(panel);
    }

    private void makeTable() {
        tModel.updatetable();
        JTable table = new JTable(tModel);
        table.setDefaultRenderer(String.class, new TabellenStringRenderer());
        table.getModel().addTableModelListener(this);

        table.getColumnModel().getColumn(1).setMinWidth(170);

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setBackground(tabBackg);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, gc);
    }

    public void tableChanged(TableModelEvent e) {
        // nothing to do
    }

    public static ShowLastBills getInstance() {
        if (slb != null) {
            return slb;
        } else {
            slb = new ShowLastBills();
            slb.pack();
            return slb;
        }
    }
}

/**
 * TabellenModel
 */
class LBTableModel extends AbstractTableModel {

    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();

    private Class[] columnClasses = new Class[]{String.class, String.class, Float.class, Integer.class};
    private String[] colls = {langBundle.getString("telefon"), langBundle.getString("ersterAnruf"), "total", "quantity"};
    private Object[][] data;
    private String[] phones = Phone.getPhoneNames();
    private static Logger logger = Logger.getRootLogger();

    public void updatetable() {
        data = new Object[phones.length][4];
        try {
            for (int i = 0; i < phones.length; i++) {
                Statement stmt1 = DBconn.con.createStatement();
                ResultSet result1 = stmt1.executeQuery("SELECT max(unixtime) AS \"unixtime\" FROM cleared WHERE src='" + phones[i] + "'");
                result1.first();
                String unixtime = result1.getString("unixtime");
                Statement stmt2 = DBconn.con.createStatement();
                ResultSet result2 = stmt2.executeQuery("SELECT * FROM cleared WHERE src='" + phones[i] + "' AND unixtime='" + unixtime + "'");
                result2.last();
                int anzFrage = result2.getRow();
                result2.first();
                if (anzFrage != 0) {
                    data[i][0] = result2.getString("src");
                    data[i][1] = UnixTimestamp.getTimestamp(result2.getString("unixtime"));
                    data[i][2] = result2.getFloat("total");
                    data[i][3] = result2.getInt("anzahl");
                }
            }
        } catch (SQLException ex) {
            AstGUI.getInstance().setSqlStatus('r');
            logger.error(ex.getMessage());
        } finally {
            fireTableDataChanged();
        }
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        return colls.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return colls[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }
}
