package de.callshop4u;
/*
 * AstGUI.java
 *
 */

import de.callshop4u.ami.AMIconn;
import de.callshop4u.panels.RootPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.apache.log4j.*;

public class AstGUI {

    private static AstGUI astgui;
    private final JFrame astF = new JFrame("AstShop");
    private URL iconUrl = AstGUI.class.getResource("/de/callshop4u/pics/favicon.png");
    private Image favImage;
    private JPanel statusPanel;
    private Color panBackground = new Color(144, 175, 200);
    private Color[] flagArr = {new Color(11, 207, 0), new Color(223, 0, 9)};
    private Color sqlFlag = flagArr[1];
    private Color amiFlag = flagArr[1];
    private Font f = new Font("Arial", Font.BOLD, 10);
    // properties and language
    private static Preferences prefs;
    private static String langBase = "de.callshop4u.lang.astshop";
    private static ResourceBundle langBundle;
    private static String[] langs = Locale.getISOLanguages();
    private static String[] countries = Locale.getISOCountries();
    // logging
    private static Logger logger = Logger.getRootLogger();

    private AstGUI() {
        // singleton
    }

    private void astDesktop() {
        astF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        astF.setMinimumSize(new Dimension(800, 600));
        astF.setBounds(200, 200, 800, 600);
        astF.setLayout(new BorderLayout());
        try {
            BufferedImage bFavImage = ImageIO.read(iconUrl);
            favImage = bFavImage;
        } catch (IOException ex) {
            logger.error("unable to load icon image: " + ex.getMessage());
        }
        astF.setIconImage(favImage);

        // MenuBar
        JMenuBar menuBar = new JMenuBar();

        // Datei Menu
        JMenu fileMenu = new JMenu(langBundle.getString("file"));
        menuBar.add(fileMenu);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        // Shop Menu
        JMenu shopMenu = new JMenu("Shop");
        menuBar.add(shopMenu);

        // Admin Menu
        final JMenu adminMenu = new JMenu("Admin");
        menuBar.add(adminMenu);

        // Help Menu
        JMenu helpMenu = new JMenu(langBundle.getString("help"));
        menuBar.add(helpMenu);

        /**
         * Edit language Button
         */
        JMenuItem editLangB = new JMenuItem(langBundle.getString("editLangButton"));
        class EditLangAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                ShowEditLang.getInstance().setVisible(true);
            }
        }
        EditLangAction editLangAct = new EditLangAction();
        editLangB.addActionListener(editLangAct);
        editMenu.add(editLangB);

        /**
         * DB GUI Button
         */
        JMenuItem sqlAdminB = new JMenuItem(langBundle.getString("sqlAdminButton"));
        class SqlAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                ShowDBgui.getInstance().DBadminF();
            }
        }
        SqlAction sqlAct = new SqlAction();
        sqlAdminB.addActionListener(sqlAct);
        adminMenu.add(sqlAdminB);

        /**
         * AMI GUI Button
         */
        JMenuItem amiB = new JMenuItem(langBundle.getString("amiAdminButton"));
        class AmiAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                ShowAMIgui.getInstance().setVisible(true);
            }
        }
        AmiAction amiAct = new AmiAction();
        amiB.addActionListener(amiAct);
        adminMenu.add(amiB);

        /**
         * TariffConverter Button
         */
        JMenuItem tariffConvB = new JMenuItem(langBundle.getString("tariffconverter"));
        class TariffConvAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                ShowTariffListConverter.getInstance().setVisible(true);
            }
        }
        TariffConvAction tariffConvAction = new TariffConvAction();
        tariffConvB.addActionListener(tariffConvAction);
        adminMenu.add(tariffConvB);
        
        /**
         * TariffEdit Button
         */
        JMenuItem tariffEditB = new JMenuItem(langBundle.getString("tariffedit"));
        class TariffEditAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                ShowTariffEdit.getInstance().setVisible(true);
            }
        }
        tariffEditB.addActionListener(new TariffEditAction());
        adminMenu.add(tariffEditB);
        
        /**
         * show Tarif Button
         */
        JMenuItem tarifB = new JMenuItem(langBundle.getString("tarife"));
        class TarifAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (DBconn.isConnected()) {
                    ShowTarif.getInstance().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, langBundle.getString("keineDbVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        TarifAction TarifAct = new TarifAction();
        tarifB.addActionListener(TarifAct);
        shopMenu.add(tarifB);

        /**
         * show one Tarif Button
         */
        JMenuItem tarifE = new JMenuItem(langBundle.getString("einTarif"));
        class TarifEAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (DBconn.isConnected()) {
                    ShowTarifEinen.getInstance().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, langBundle.getString("keineDbVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        TarifEAction TarifEAct = new TarifEAction();
        tarifE.addActionListener(TarifEAct);
        shopMenu.add(tarifE);

        /**
         * show last Calls Button
         */
        JMenuItem lastCallsB = new JMenuItem(langBundle.getString("letzteAnrufe"));
        class LastCallsAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (DBconn.isConnected()) {
                    ShowLastCalls slc = ShowLastCalls.getInstance();
                    slc.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, langBundle.getString("keineDbVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        LastCallsAction lcAct = new LastCallsAction();
        lastCallsB.addActionListener(lcAct);
        shopMenu.add(lastCallsB);


        /**
         * show last Bills Button
         */
        JMenuItem lastBillsB = new JMenuItem(langBundle.getString("letzteAbrechnungen"));
        class LastBillsAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (DBconn.isConnected()) {
                    ShowLastBills slb = ShowLastBills.getInstance();
                    slb.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, langBundle.getString("keineDbVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        LastBillsAction lbAct = new LastBillsAction();
        lastBillsB.addActionListener(lbAct);
        shopMenu.add(lastBillsB);

        /**
         * ShowAbout Dialog
         */
        Action aboutAction = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            {
                putValue(Action.NAME, langBundle.getString("about"));
                putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
            }

            public void actionPerformed(ActionEvent e) {
                ShowAbout.getInstance().setVisible(true);
            }
        };
        helpMenu.add(aboutAction);

        /**
         * Close Button
         */
        Action exitAction = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            {
                putValue(Action.NAME, langBundle.getString("exit"));
                putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
            }

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        fileMenu.add(exitAction);

        /**
         * Panel für Verbindungsstatus
         */
        URL url = AstGUI.class.getResource("/de/callshop4u/pics/logo.png");
        ImageIcon image = new ImageIcon(url);
        JLabel imageLabel = new JLabel(image);
        imageLabel.setBounds(600, 1, 120, 24);

        statusPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(sqlFlag);
                g2.setFont(f);
                g2.drawString("SQL-Status", 20, 20);
                g2.setColor(amiFlag);
                g2.drawString("AMI-Status", 140, 20);
            }
        };
        statusPanel.setPreferredSize(new Dimension(600, 26));
        statusPanel.setLayout(null);
        statusPanel.add(imageLabel);
        astF.add(statusPanel, BorderLayout.NORTH);

        astF.setJMenuBar(menuBar);
        astF.setVisible(true);
    }

    /**************************************************************************/
    /**
     * Getter four default Panel background
     */
    public Color getPanBackground() {
        return panBackground;
    }

    /**
     * Getter for favImage
     */
    public Image getFavImage() {
        return favImage;
    }

    /**
     * Farben für Statusanzeige ändern
     */
    public void setSqlStatus(char s) {
        switch (s) {
            case 'r':
                this.sqlFlag = flagArr[1];
                break;
            case 'g':
                this.sqlFlag = flagArr[0];
                break;
        }
        statusPanel.repaint();
    }

    public void setAmiStatus(char s) {
        if (s == 'g' && amiFlag == flagArr[0]) {
            return;
        }
        switch (s) {
            case 'r':
                this.amiFlag = flagArr[1];
                break;
            case 'g':
                this.amiFlag = flagArr[0];
                break;
        }
        statusPanel.repaint();
    }

    /**
     * automatic connect to database and AMI
     * add phonepanels
     */
    private void autoStart() {
        if (prefs.get("dbauto", "xxx").equals("yes")) {
            DBconn.openDB(prefs.get("dbhost", "localhost") + "/asterisk", prefs.get("dbuser", "username"), prefs.get("dbpass", "xxx"));
            if (DBconn.isConnected()) {
                Phone.initPhones();
                addPhonePanels();
            } else {
                JOptionPane.showMessageDialog(null, langBundle.getString("fehlerDbVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (prefs.get("amiauto", "xxx").equals("yes")) {
            AMIconn.createFactory(prefs.get("amihost", "localhost"), prefs.get("amiuser", "username"), prefs.get("amipass", "xxx"));
            if (!AMIconn.isConnected()) {
                JOptionPane.showMessageDialog(null, langBundle.getString("fehlerAmiVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * called from ShowDBgui
     */
    public void addPhonePanels() {
        JComponent comp = new RootPanel(Phone.getPhoneNames());
        JScrollPane scroll = new JScrollPane(comp);
        astF.getRootPane().add(scroll, BorderLayout.CENTER);
        astF.getContentPane().setComponentZOrder(scroll, 0);
        astF.setVisible(true);
        ShowDBgui.getInstance().setVisible(false);
    }

    /**
     * Factory method
     * @param args
     */
    public static AstGUI getInstance() {
        if (astgui != null) {
            return astgui;
        } else {
            astgui = new AstGUI();
            return astgui;
        }
    }

    /**
     * java.util.prefs.Preferences
     */
    private static void makeProps() {
        prefs = Preferences.userRoot().node("/de/astshop4u/pref");
    }

    public void setProp(String key, String value) {
        prefs.put(key, value);
    }

    public Preferences getPrefs() {
        return prefs;
    }

    /**
     * Resourcen für Internationalisierung laden
     * @param args
     */
    private static void loadBundle() {
        langBundle = ResourceBundle.getBundle(langBase, new Locale(prefs.get("language", "de_DE")));
    }

    /**
     * language Bundle Getter
     */
    public ResourceBundle getLangBundle() {
        return langBundle;
    }

    /**
     * ResourceBundle language switch
     * @param args
     */
    public void switchLanguage(String lang, String country) {
        HashSet langSet = new HashSet<String>();
        langSet.addAll(Arrays.asList(langs));
        HashSet countrySet = new HashSet<String>();
        countrySet.addAll(Arrays.asList(countries));
        if (lang != null && country != null) {
            if (langSet.contains(lang) && countrySet.contains(country)) {
                langBundle = ResourceBundle.getBundle(langBase, new Locale(lang + "_" + country));
                prefs.put("language", lang + "_" + country);
            } else if (langSet.contains(lang)) {
                langBundle = ResourceBundle.getBundle(langBase, new Locale(lang));
                prefs.put("language", lang);
            }
        }
    }

    private void logging() {
        try {
            SimpleLayout layout = new SimpleLayout();
            FileAppender fileAppender = new FileAppender(layout, "astshop.log", false);
            logger.addAppender(fileAppender);
            // ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
            logger.setLevel(Level.ALL);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        makeProps();
        loadBundle();
        getInstance().astDesktop();
        astgui.logging();
        logger.info(new Date());
        astgui.autoStart();
    }
}
