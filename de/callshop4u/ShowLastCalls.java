/*
 * Panel zum anzeigen der zuletzt abgerechneten Calls
 * ... Liste der Calls
 */
package de.callshop4u;

import de.callshop4u.zeug.TabellenStringRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;

/**
 * @author allapow
 */
public class ShowLastCalls extends JFrame implements TableModelListener {

    private static ShowLastCalls slc;
    private AstGUI astgui = AstGUI.getInstance();
    private GridBagConstraints gc = new GridBagConstraints();
    private Color panBackground = new Color(144, 175, 200);
    private Color tabBackg = new Color(143, 152, 255);
    private JPanel panel = new JPanel();
    private LCTableModel tModel;
    private String[] phones;
    private static Logger logger = Logger.getRootLogger();

    private ShowLastCalls() {
        // Singleton
        phones = Phone.getPhoneNames();

        setTitle("last Calls");
        setIconImage(astgui.getFavImage());
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(800, 200));
        setPreferredSize(new Dimension(800, 200));
        setBounds(200, 200, 800, 200);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(panBackground);

        JComboBox combo = new JComboBox();
        for(int i = 0; i < phones.length; i++) {
            combo.addItem(phones[i]);
        }
        gc.weightx = 0.5;
        gc.insets = new Insets(5, 10, 5, 10);
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(combo,gc);

        tModel = new LCTableModel();
        makeTable();

        combo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tModel.updateTable(((JComboBox)e.getSource()).getSelectedItem().toString());
            }
        });
        add(panel);
    }

    private void makeTable() {
        tModel.updateTable(phones[0]);
        JTable table = new JTable(tModel);
        table.setDefaultRenderer(String.class, new TabellenStringRenderer());
        table.getModel().addTableModelListener(this);

        table.getColumnModel().getColumn(3).setMinWidth(150);
        table.getColumnModel().getColumn(4).setMinWidth(150);
        table.getColumnModel().getColumn(6).setMinWidth(90);

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setBackground(tabBackg);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        gc.fill = GridBagConstraints.BOTH;
        gc.weighty = 0.5;
        panel.add(scroll,gc);
    }

    public void tableChanged(TableModelEvent e) {
    }

    public static ShowLastCalls getInstance() {
        if(slc != null) {
            return slc;
        } else {
            slc = new ShowLastCalls();
            slc.pack();
            return slc;
        }
    }
}

/**
 * TabellenModel
 */
class LCTableModel extends AbstractTableModel {

    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    private static Logger logger = Logger.getRootLogger();

    private Class[] columnClasses = new Class[]{String.class, String.class, Float.class, String.class, String.class, Float.class, String.class, Float.class};
    private String[] colls = {langBundle.getString("telefon"), langBundle.getString("ziel"), langBundle.getString("preis"), langBundle.getString("start"), langBundle.getString("ende"), langBundle.getString("zeit"), langBundle.getString("ergebnis"), langBundle.getString("total")};
    private Object[][] data;

    void updateTable(String src) {
        int rowCount = 0;
        try {
            Statement stmt1 = DBconn.con.createStatement();
            ResultSet resTime = stmt1.executeQuery("SELECT max(unixtime) AS \"unixtime\" FROM cleared WHERE src='" + src + "'");
            resTime.first();
            String unixtime = resTime.getString("unixtime");
            Statement stmt2 = DBconn.con.createStatement();
            ResultSet result = stmt2.executeQuery("SELECT * FROM cdr_bill WHERE src='" + src + "' AND unixtime >= '" + unixtime + "' ORDER BY unixtime");
            result.last();
            rowCount = result.getRow();
            data = new Object[rowCount][8];
            result.beforeFirst();
            for (int i = 0; result.next(); i++) {
                data[i][0] = result.getString("src");
                data[i][1] = result.getString("dest");
                data[i][2] = result.getFloat("preis");
                data[i][3] = result.getString("start_bill");
                data[i][4] = result.getString("end_bill");
                Integer sec = result.getInt("billsec");
                data[i][5] = (sec % 60f) / 100 + sec / 60;
                data[i][6] = result.getString("disposition");
                data[i][7] = result.getFloat("total");
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
