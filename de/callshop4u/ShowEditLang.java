/*
 * 
 */
package de.callshop4u;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author allapow
 */
public class ShowEditLang extends JFrame {

    private static ShowEditLang showEditLang;
    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    private Color panBackground = astgui.getPanBackground();
    private JPanel panel = new JPanel();
    JComboBox combo;
    private LinkedHashMap<String, String> langMap = new LinkedHashMap<String, String>();

    {
        langMap.put("german", "de_DE");
        langMap.put("englisch", "en");
    }

    private ShowEditLang() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setTitle(langBundle.getString("editLangTitle"));
        setIconImage(astgui.getFavImage());
        setMinimumSize(new Dimension(400, 200));
        setPreferredSize(new Dimension(400, 200));
        setBounds(200, 200, 400, 200);
        panel.setLayout(null);
        panel.setBackground(panBackground);

        setInfo();
        addComboBox();
        addLangButton();
        add(panel);
    }

    /**
     * Info Label
     */
    private void setInfo() {
        JLabel infoLabel = new JLabel();
        infoLabel.setText("<html>To add a new language to this software,<br>please contact the developer info@callshop4u.de</html>");
        infoLabel.setBounds(10, 5, 400, 50);
        JLabel editInfoLabel = new JLabel();
        editInfoLabel.setText(astgui.getLangBundle().getString("editInfoLabel"));
        editInfoLabel.setBounds(10, 110, 400, 50);
        panel.add(infoLabel);
        panel.add(editInfoLabel);
    }

    /**
     * hart gekodeter Inhalt für die ComboBox in einer HashMap
     */
    private void addComboBox() {
        combo = new JComboBox();
        combo.setBounds(20, 70, 150, 30);
        Set<String> langKeys = langMap.keySet();
        TreeSet<String> sortedVals = new TreeSet<String>();
        sortedVals.addAll(langKeys);
        for (String key : sortedVals) {
            combo.addItem(key);
        }

        panel.add(combo);
    }

    /**
     * save language Button
     */
    private void addLangButton() {
        JButton langButton = new JButton(langBundle.getString("save"));
        langButton.setBounds(190, 70, 170, 30);
        class LangAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                String cs = (String) combo.getSelectedItem();
                String l = langMap.get(cs);
                String[] ls = l.split("_");
                if (ls.length > 1) {
                    astgui.switchLanguage(ls[0], ls[1]);
                } else {
                    astgui.switchLanguage(ls[0], "");
                }
            }
        }
        LangAction langAct = new LangAction();
        langButton.addActionListener(langAct);

        panel.add(langButton);
    }

    /**
     * factory method
     */
    public static ShowEditLang getInstance() {
        if (showEditLang != null) {
            return showEditLang;
        } else {
            showEditLang = new ShowEditLang();
            return showEditLang;
        }
    }
}
