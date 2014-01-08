/**
 * Verwaltung und Erstellung der einzelnen PhonePanel
 *
 * hält Referenz auf alle PhonePanel
 */
package de.callshop4u.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;
import javax.swing.JPanel;

public class RootPanel extends JPanel{

    private static RootPanel rootPanel;
    private GridBagLayout gridbag = new GridBagLayout();
    private GridBagConstraints c = new GridBagConstraints();
    private static LinkedHashMap<String, PhonePanel> phones = new LinkedHashMap<String, PhonePanel>();
    private String[] phoneNames;

    private RootPanel() {
        // Singleton
    }

    public RootPanel(String[] phoneNames) {
        setLayout(gridbag);
        this.phoneNames = phoneNames;
        adPanels();
    }

    private void adPanels() {
        setBackground(new Color(150, 150, 0));
//        setBorder(new BevelBorder(BevelBorder.RAISED));
        // jedes Panel in eine neue Zeile
        c.gridwidth = GridBagConstraints.REMAINDER;
        // Seitenabstände festlegen: Insets(int top, int left, int bottom, int right)
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.2;
        c.weighty = 0;


        // Seitenabstände für die PhonePanels neu festlegen
        c.insets = new Insets(5, 10, 5, 10);
        for (int i = 0; i < phoneNames.length; i++) {
            String pname = phoneNames[i];
            PhonePanel pan = new PhonePanel(pname);
            add(pan, c);
            phones.put(pname, pan);
        }
    }

    /**
     * Methoden die über das PhonePanel ihre TabellenModel's aufrufen
     * 
     * @param src
     */
    public void changePhoneTableStart(String src) {
        PhonePanel pp = phones.get(src);
        pp.setStatusRed();
        pp.updateTable();
    }

    public void changePhoneTableEnd(String src) {
        PhonePanel pp = phones.get(src);
        pp.setStatusGreen();
        pp.updateTable();
    }

    public void changePhoneTableAbgerechnet(String src) {
        phones.get(src).updateTable();
    }

    /**
     * total updaten
     * @param src
     */
    public void changePhoneTableTotal(String src, Integer total) {
        phones.get(src).updateTotal(total);
    }

    /**
     * producer Methode
     */
    public static synchronized RootPanel getInstance() {
        if(rootPanel != null) {
            return rootPanel;
        } else {
            rootPanel = new RootPanel();
            return rootPanel;
        }
    }
}
