package de.callshop4u;
/*
 * ShowTarifEinen.java
 *
 * zeigt einen passenden Tarif zu einer Vorwahl
 *
 * showOne() gibt ein JFrame mit passendem Inhalt zurück
 */

import de.callshop4u.zeug.Tarifsuche;
import java.awt.event.*;
import java.util.ResourceBundle;
import javax.swing.*;

public class ShowTarifEinen extends JFrame{

    private static ShowTarifEinen showTarifEinen;
    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    static String feldText = "";
    static String labelText = "";
    static String[] ergeb = new String[3];

    private ShowTarifEinen() {
        // singleton
        setIconImage(astgui.getFavImage());
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(null);
        setBounds(200, 200, 500, 170);
        showOne();
    }

    private void showOne() {
        JLabel oneTlabel1 = new JLabel(langBundle.getString("showOneTariflabel"));		// Information zur Tarifsuche
        oneTlabel1.setBounds(20, 20, 220, 25);
        final JTextField oneTtext = new JTextField("");	// Eingabetextfeld
        oneTtext.setBounds(250, 20, 100, 25);
        JButton oneTbutton = new JButton(langBundle.getString("anzeigen"));		// Button
        oneTbutton.setBounds(370, 20, 100, 25);
        final JLabel oneTL1 = new JLabel(labelText);		// Anzeige des gefundenen Tarif 1
        oneTL1.setBounds(20, 55, 200, 20);
        final JLabel oneTL2 = new JLabel(labelText);		// Anzeige des gefundenen Tarif 2
        oneTL2.setBounds(20, 80, 300, 20);
        final JLabel oneTL3 = new JLabel(labelText);		// Anzeige des gefundenen Tarif 3
        oneTL3.setBounds(20, 105, 200, 20);

        class OneTAction implements ActionListener {

            public void actionPerformed(ActionEvent be) {
                feldText = oneTtext.getText().trim();
                if (feldText.equals("")) {
                    JOptionPane.showMessageDialog(null, langBundle.getString("keineNumAngegeben"), "Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
//                    SuchTarif such = new SuchTarif();
//                    ergeb = such.startSuch(feldText);			// Suche Aufrufen
                    Tarifsuche such = new Tarifsuche();
                    ergeb = such.startSuch(feldText);
                    String num = ergeb[0];
                    String dest = ergeb[1];
                    String preis = ergeb[2];
                    oneTL1.setText(langBundle.getString("nummerP") + num);
                    oneTL2.setText(langBundle.getString("landP") + dest);
                    oneTL3.setText(langBundle.getString("preisP") + preis);
                }
            }
        }
        OneTAction oneAct = new OneTAction();
        oneTbutton.addActionListener(oneAct);

        add(oneTlabel1);
        add(oneTtext);
        add(oneTbutton);
        add(oneTL1);
        add(oneTL2);
        add(oneTL3);
    }

    /**
     * producer method
     */
    public static ShowTarifEinen getInstance() {
        if(showTarifEinen != null) {
            return showTarifEinen;
        } else {
            showTarifEinen = new ShowTarifEinen();
            return showTarifEinen;
        }
    }
}
