package de.callshop4u.panels;

import de.callshop4u.AstGUI;
import de.callshop4u.ami.CDRmanager;
import de.callshop4u.zeug.TabellenStringRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Logger;

public class PhonePanel extends JPanel implements TableModelListener {

    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    private static Logger logger = Logger.getRootLogger();
    private String src;
    private GridBagLayout grid = new GridBagLayout();
    private GridBagConstraints gc = new GridBagConstraints();
    private TabellenModel tModel;
    private Color[] flagArr = {new Color(11, 207, 0), new Color(255, 0, 0)};
    private Color flag = flagArr[0];
    private Color panBackg = astgui.getPanBackground();
    private Color tabBackg = new Color(143, 152, 255);
    private Color lableColor = new Color(255, 255, 0);
    private Font lableFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private Integer total = 0;
    private JLabel totalLabel;
    private JLabel centLabel;
    private Color borderColorHigh = new Color(65, 63, 255);
    private Color borderColorShadow = new Color(15, 12, 207);

    public PhonePanel(String id) {
        this.src = id;
        logger.info("PhonePanel: "+src);
        tModel = new TabellenModel(src);
        setLayout(grid);
        setBackground(panBackg);
        setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED, borderColorHigh, borderColorShadow));
        makeTableView();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(flag);
        g2.fillOval(650, 3, 16, 16);
    }

    /**
     * erstellt die Tabelle im PhonePanel
     */
    private void makeTableView() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(panBackg);
        panel.setMinimumSize(new Dimension(320, 22));

        // GridbagConstraints für das Panel
        gc = new GridBagConstraints();
        // Seitenabstände festlegen: Insets(int top, int left, int bottom, int right)
        gc.insets = new Insets(0, 0, 0, 10);
        gc.anchor = GridBagConstraints.LINE_START;
        gc.weighty = 0;

        // Label Titel
        JLabel titel = new JLabel(src);
        titel.setPreferredSize(new Dimension(60, 20));
        titel.setFont(lableFont);
        titel.setForeground(lableColor);
        gc.ipadx = 60;
        panel.add(titel, gc);

        // Button zum Abrechnen
        JButton button = new JButton(langBundle.getString("cash"));
        button.setPreferredSize(new Dimension(80, 20));
        button.addActionListener(new AbrechnenAction());
        gc.anchor = GridBagConstraints.CENTER;
        gc.ipadx = 70;
        panel.add(button, gc);

        // Label Total
        totalLabel = new JLabel(total.toString());
        panel.add(totalLabel, gc);
        // Label Cent
        centLabel = new JLabel(langBundle.getString("cent"));
        panel.add(centLabel, gc);

        // Panel adden
        gc.anchor = GridBagConstraints.WEST;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.fill = GridBagConstraints.NONE;
        add(panel, gc);

        /**
         * Tabelle erstellen
         */
        JTable table = new JTable(tModel);
        table.setDefaultRenderer(String.class, new TabellenStringRenderer());
        table.getModel().addTableModelListener(this);

        table.getColumnModel().getColumn(0).setMinWidth(110);
        table.getColumnModel().getColumn(2).setMinWidth(150);
        table.getColumnModel().getColumn(3).setMinWidth(150);
        table.getColumnModel().getColumn(5).setMinWidth(90);

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setBackground(tabBackg);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new TabellenHeadRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(750, 100));
        gc = new GridBagConstraints();
        gc.gridwidth = GridBagConstraints.REMAINDER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.weighty = 0;
        add(scroll, gc);

    }

    /**
     * erneuert den Inhalt in der Tabelle beim CallStart und CallEnd
     * über die Datenbank.cdr_bill
     */
    public void updateTable() {
        tModel.updateTable();
    }

    /**
     *  Invoked when this table's TableModel generates a TableModelEvent.
     * @param e
     */
    public void tableChanged(TableModelEvent e) {
        // nothing to do
    }

    void setStatusRed() {
        flag = flagArr[1];
        repaint();
    }

    void setStatusGreen() {
        flag = flagArr[0];
        repaint();
    }

    void updateTotal(Integer wert) {
//        Float f = ((Math.round(wert*10000))/10000F);
        total += wert;
        totalLabel.setText(total.toString());
    }
    void setTotalZero() {
        totalLabel.setText("0");
        total = 0;
    }

    class AbrechnenAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setTotalZero();
            CDRmanager.getInstance().updateAbgerechnetDB(src);
        }
    }
}
