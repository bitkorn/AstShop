/*
 * 
 */
package de.callshop4u;

import de.callshop4u.ami.AMIconn;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author allapow
 */
public class ShowAMIgui extends JFrame {

    private static ShowAMIgui showAMIgui;
    private AstGUI astgui = AstGUI.getInstance();
    private Preferences prefs = astgui.getPrefs();
    private ResourceBundle langBundle = astgui.getLangBundle();

    private ShowAMIgui() {
        // singleton
        createGUI();
    }

    private void createGUI() {
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setBounds(200, 200, 600, 400);
        setTitle("Asterisk Manager Administration");
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        // AMI User Abfrage
        JLabel amiUserL = new JLabel(langBundle.getString("amiUser"));
        add(amiUserL);
        amiUserL.setBounds(20, 20, 160, 20);
        final JTextField amiUserT = new JTextField(prefs.get("amiuser", "username"));
        add(amiUserT);
        amiUserT.setBounds(180, 20, 200, 20);

        // AMI Passwort Abfrage
        JLabel amiPassL = new JLabel(langBundle.getString("amiPass"));
        add(amiPassL);
        amiPassL.setBounds(20, 50, 160, 20);
        final JPasswordField amiPassT = new JPasswordField(prefs.get("amipass", "xxx"));
        add(amiPassT);
        amiPassT.setBounds(180, 50, 200, 20);

        // AMI Host Abfrage
        JLabel amiHostL = new JLabel(langBundle.getString("amihost"));
        add(amiHostL);
        amiHostL.setBounds(20, 80, 160, 20);
        final JTextField amiHostT = new JTextField(prefs.get("amihost", "localhost"));
        add(amiHostT);
        amiHostT.setBounds(180, 80, 200, 20);

        // Button um die Verbindungsdaten zu speichern
        JButton storeB = new JButton(langBundle.getString("speichern"));
        add(storeB);
        storeB.setBounds(420, 20, 120, 30);

        // Button zum Verbinden
        JButton verbindenB = new JButton(langBundle.getString("verbinden"));
        add(verbindenB);
        verbindenB.setBounds(40, 120, 120, 30);

        // Button zum ausblenden
        JButton hideB = new JButton(langBundle.getString("exit"));
        add(hideB);
        hideB.setBounds(180, 120, 120, 30);

        // TextArea fuer Meldungen
        final JTextArea meldungTa = new JTextArea(4, 30);
        meldungTa.setEditable(false);
        meldungTa.setBounds(20, 170, 520, 100);
        JScrollPane scroll = new JScrollPane(meldungTa);
        scroll.setBounds(20, 170, 520, 100);
        add(scroll);

        // RadioButtons automatisches Anmelden
        JRadioButton checkYes = new JRadioButton(langBundle.getString("autoAnmelden"));
        checkYes.setBounds(40, 285, 250, 20);
        checkYes.setActionCommand("yes");
        JRadioButton checkNo = new JRadioButton(langBundle.getString("nichtAutoAnmelden"));
        checkNo.setBounds(40, 305, 250, 20);
        checkNo.setActionCommand("no");
        ButtonGroup checkGroup = new ButtonGroup();
        checkGroup.add(checkYes);
        checkGroup.add(checkNo);
        add(checkYes);
        add(checkNo);


        // Action fuer den verbinden-Button
        class VerbindenActionL implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                String host = amiHostT.getText().trim();
                String user = amiUserT.getText().trim();
                char[] amiPass = amiPassT.getPassword();
                String pass = String.valueOf(amiPass);
                AMIconn.createFactory(host, user, pass);
                meldungTa.append("\n" + AMIconn.testManagerConn());
            }
        }
        VerbindenActionL verbAct = new VerbindenActionL();
        verbindenB.addActionListener(verbAct);

        // Action fuer den schießen-Button
        class HideAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        }
        HideAction ha = new HideAction();
        hideB.addActionListener(ha);

        /**
         * Action um die Verbindungsdaten in die Properties zu schreiben
         */
        class StoreProperties implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                String user = amiUserT.getText().trim();
                char[] amiPass = amiPassT.getPassword();
                String pass = String.valueOf(amiPass);
                String host = amiHostT.getText().trim();
                prefs.put("amiuser", user);
                prefs.put("amipass", pass);
                prefs.put("amihost", host);
            }
        }
        StoreProperties storeAction = new StoreProperties();
        storeB.addActionListener(storeAction);

        /**
         * Action für die Radiobuttons
         */
        class CheckboxListener implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                astgui.setProp("amiauto", e.getActionCommand());
            }
        }
        CheckboxListener listcheck = new CheckboxListener();
        checkYes.addActionListener(listcheck);
        checkNo.addActionListener(listcheck);
    }

    /**
     * factory method
     */
    public static ShowAMIgui getInstance() {
        if (showAMIgui != null) {
            return showAMIgui;
        } else {
            showAMIgui = new ShowAMIgui();
            return showAMIgui;
        }
    }
}
