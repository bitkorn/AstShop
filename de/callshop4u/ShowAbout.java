package de.callshop4u;
/*
 * DBadmin.java
 *
 */

import java.awt.*;
import javax.swing.*;

public class ShowAbout extends JDialog {

    private static ShowAbout showAbout;
    private AstGUI astgui = AstGUI.getInstance();

    private ShowAbout() {
        // singleton
        setBounds(200, 200, 400, 200);
        setTitle("AstShop");
        setIconImage(astgui.getFavImage());
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        // Developer
        JLabel developerL = new JLabel("Developer: Torsten Brieskorn");
        add(developerL);
        developerL.setBounds(5, 5, 280, 30);

        // Developer Webseite
        JLabel developerW = new JLabel("http://www.callshop4u.de");
        add(developerW);
        developerW.setBounds(5, 35, 280, 30);

    }

    /**
     * factory method
     */
    public static ShowAbout getInstance() {
        if (showAbout != null) {
            return showAbout;
        } else {
            showAbout = new ShowAbout();
            return showAbout;
        }
    }
}
