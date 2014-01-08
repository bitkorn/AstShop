/*
 * konvertiert Tariflisten
 */
package de.callshop4u;

import de.callshop4u.zeug.TariffListConverter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class ShowTariffListConverter extends JFrame {

    public static ShowTariffListConverter showTariffListConverter;
    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    private JFileChooser dateiWahl;
    private File file;
    private JTextArea messageField;

    /*
     * oeffnet DateiwahlFenster und
     * initialisiert File file
     */
    private void openFileAction() {
        dateiWahl = new JFileChooser(langBundle.getString("openfile"));
        dateiWahl.setMultiSelectionEnabled(false);
        int returnVal = dateiWahl.showOpenDialog(dateiWahl);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = dateiWahl.getSelectedFile();
        }
        String message = TariffListConverter.convertPbxnetwork(file);
        messageField.append(message);
    }
    
    public ShowTariffListConverter() {
        setTitle(langBundle.getString("tariffconverter"));
        setIconImage(astgui.getFavImage());
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(null);
        setBounds(200, 200, 500, 250);
        setGui();
    }

    private void setGui() {
        JLabel description01 = new JLabel(langBundle.getString("konverterdescription"));
        JLabel description02 = new JLabel("31655, 3168, 3169;\"Niederl-Mobil\";;;31,18;31,18;60;60");
        JButton filechooserB = new JButton(langBundle.getString("openfile"));
        class FileChooserAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                openFileAction();
            }
        }
        filechooserB.addActionListener(new FileChooserAction());
        messageField = new JTextArea();
        description01.setBounds(20, 20, 400, 20);
        description02.setBounds(20, 45, 500, 20);
        filechooserB.setBounds(20, 90, 170, 25);
        messageField.setBounds(20, 120, 400, 100);
        add(description01);
        add(description02);
        add(filechooserB);
        add(messageField);
    }

    /*
     * singleton
     */
    public static ShowTariffListConverter getInstance() {
        if (showTariffListConverter == null) {
            showTariffListConverter = new ShowTariffListConverter();
        }
        return showTariffListConverter;
    }
}
