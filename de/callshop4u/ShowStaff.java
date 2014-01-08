/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.callshop4u;

import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author allapow
 */
public class ShowStaff extends JFrame{

    private static ShowStaff showStaff;
    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    
    private ShowStaff() {
        
        setTitle(langBundle.getString("tarife"));
        setIconImage(astgui.getFavImage());
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(null);
        setBounds(200, 200, 500, 800);
    }
    
    /**
     * producer
     */
    public static ShowStaff getInstance() {
        if(showStaff == null) {
            showStaff = new ShowStaff();
        }
        return showStaff;
    }
}
