package de.callshop4u;
/*
 * ShowDBgui.java
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.*;

public class ShowDBgui {

    private static ShowDBgui dbgui;
    private AstGUI astgui = AstGUI.getInstance();
    private Preferences prefs = astgui.getPrefs();
    private ResourceBundle langBundle = astgui.getLangBundle();
    private final JDialog frame = new JDialog();

    private ShowDBgui() {
        // singleton
    }

    void DBadminF() {
        frame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        frame.setBounds(200, 200, 600, 400);
        frame.setTitle("SQL Admin");
        frame.setIconImage(astgui.getFavImage());
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setLayout(null);

        // DB User Abfrage
        JLabel dbUserL = new JLabel(langBundle.getString("sqlUser"));
        frame.add(dbUserL);
        dbUserL.setBounds(20, 20, 160, 20);
        final JTextField dbUserT = new JTextField(prefs.get("dbuser", "username"));
        frame.add(dbUserT);
        dbUserT.setBounds(180, 20, 200, 20);

        // DB Passwort Abfrage
        JLabel dbPassL = new JLabel(langBundle.getString("sqlPass"));
        frame.add(dbPassL);
        dbPassL.setBounds(20, 50, 160, 20);
        final JPasswordField dbPassT = new JPasswordField(prefs.get("dbpass", "xxx"));
        frame.add(dbPassT);
        dbPassT.setBounds(180, 50, 200, 20);

        // DB Host Abfrage
        JLabel dbHostL = new JLabel(langBundle.getString("sqlHost"));
        frame.add(dbHostL);
        dbHostL.setBounds(20, 80, 160, 20);
        final JTextField dbHostT = new JTextField(prefs.get("dbhost", "localhost"), 20);
        frame.add(dbHostT);
        dbHostT.setBounds(180, 80, 200, 20);

        // Button für Speicherung der Verbindungsdaten
        JButton storeB = new JButton(langBundle.getString("save"));
        frame.add(storeB);
        storeB.setBounds(420, 20, 120, 30);

        // Button fuer Datenbank Verbindung
        JButton verbindenB = new JButton(langBundle.getString("verbinden"));
        frame.add(verbindenB);
        verbindenB.setBounds(40, 120, 120, 30);

        // Button fuer Datenbank Test
        JButton testB = new JButton(langBundle.getString("test"));
        frame.add(testB);
        testB.setBounds(180, 120, 120, 30);

        // Button fur Telefone initialisieren
        JButton teleB = new JButton(langBundle.getString("teleInit"));
        frame.add(teleB);
        teleB.setBounds(320, 120, 220, 30);

        // TextArea fuer Meldungen
        final JTextArea meldungTa = new JTextArea(4, 30);
        meldungTa.setEditable(false);
        meldungTa.setBounds(20, 170, 520, 100);
        JScrollPane scroll = new JScrollPane(meldungTa);
        scroll.setBounds(20, 170, 520, 100);
        frame.add(scroll);

        // Button zum Schlissen der Verbindung
        JButton trennenB = new JButton(langBundle.getString("trennen"));
        frame.add(trennenB);
        trennenB.setBounds(40, 290, 120, 30);

        // RadioButtons automatisches Anmelden
        JRadioButton checkYes = new JRadioButton(langBundle.getString("autoAnmelden"));
        checkYes.setBounds(210, 285, 250, 20);
        checkYes.setActionCommand("yes");
        JRadioButton checkNo = new JRadioButton(langBundle.getString("nichtAutoAnmelden"));
        checkNo.setBounds(210, 305, 250, 20);
        checkNo.setActionCommand("no");
        ButtonGroup checkGroup = new ButtonGroup();
        checkGroup.add(checkYes);
        checkGroup.add(checkNo);
        frame.add(checkYes);
        frame.add(checkNo);

        // Action fuer den verbinden-Button
        class VerbindenActionL implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                char[] dbPass = dbPassT.getPassword();

                String sUser = dbUserT.getText().trim();
                String sPwd = String.valueOf(dbPass);
                ;
                String sUrl = dbHostT.getText().trim() + "/asterisk";
                DBconn.openDB(sUrl, sUser, sPwd);
                meldungTa.append("\n" + DBconn.erfMeld);
            }
        }
        VerbindenActionL verbAct = new VerbindenActionL();
        verbindenB.addActionListener(verbAct);

        // Action fuer den close-Button
        class TrennenActionL implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (DBconn.isConnected()) {
                    DBconn.closeDB();
                    meldungTa.append("\n" + DBconn.erfMeld2);
                }
            }
        }
        TrennenActionL trennAct = new TrennenActionL();
        trennenB.addActionListener(trennAct);

        /**
         * Action fuer den test-Button
         */
        class TestenActionL implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                DBconn.test();
                meldungTa.append("\n" + DBconn.erfMeld3);
            }
        }
        TestenActionL testAct = new TestenActionL();
        testB.addActionListener(testAct);

        /**
         * Action fuer den Telefone initialisieren Button
         * Erstellt gleichzeitig die PhonePanels
         */
        class InitPhonesActionL implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (Phone.getAnzPh() == 0) {
                    if (DBconn.isConnected()) {
                        int zahl = Phone.initGetAnzPh();
                        AstGUI.getInstance().addPhonePanels();
                        meldungTa.append("\n" + zahl + langBundle.getString("telesInitialisiert"));
                    } else {
                        meldungTa.append("\n" + langBundle.getString("zuerstMitDbVerbinden"));
                    }
                }
            }
        }
        InitPhonesActionL teleZAct = new InitPhonesActionL();
        teleB.addActionListener(teleZAct);

        /**
         * Action um die Verbindungsdaten in die Properties zu schreiben
         */
        class StoreProperties implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                char[] dbPass = dbPassT.getPassword();
                prefs.put("dbuser", dbUserT.getText());
                prefs.put("dbpass", String.valueOf(dbPass));
                prefs.put("dbhost", dbHostT.getText());
            }
        }
        StoreProperties storeAction = new StoreProperties();
        storeB.addActionListener(storeAction);

        /**
         * Action für die Radiobuttons
         */
        class CheckboxListener implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                prefs.put("dbauto", e.getActionCommand());
            }
        }
        CheckboxListener listcheck = new CheckboxListener();
        checkYes.addActionListener(listcheck);
        checkNo.addActionListener(listcheck);

        frame.setVisible(true);
    }

    public void setVisible(Boolean visible) {
        frame.setVisible(visible);
    }

    /**
     * Factory Methode
     * @return
     */
    public static ShowDBgui getInstance() {
        if (dbgui != null) {
            return dbgui;
        } else {
            dbgui = new ShowDBgui();
            return dbgui;
        }
    }
}
