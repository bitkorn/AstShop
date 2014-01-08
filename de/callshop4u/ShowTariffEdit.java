/*
 * konvertiert Tariflisten
 */
package de.callshop4u;

import de.callshop4u.zeug.TariffEdit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ShowTariffEdit extends JFrame {

    public static ShowTariffEdit showTariffEdit;
    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    private JTextArea messageField;
    private JTextField sumCountT;
    private int sumCount;

    public ShowTariffEdit() {
        setTitle(langBundle.getString("tariffedit"));
        setIconImage(astgui.getFavImage());
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(null);
        setBounds(200, 200, 500, 250);
        setGui();
    }

    private void setGui() {
        JLabel description01 = new JLabel(langBundle.getString("tariffeditdescription"));
        JLabel description02 = new JLabel(langBundle.getString("tariffeditdescription2"));
        sumCountT = new JTextField();
        JButton editB = new JButton(langBundle.getString("edit"));
        messageField = new JTextArea();
        messageField.setEditable(false);
        messageField.setLineWrap(true);
        JScrollPane scrollP = new JScrollPane(messageField);
        class EditTariffAction implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                int tmp = extractSumcount();
                System.out.println("TempInt: " + tmp);
                String message = TariffEdit.editSellprice(tmp);
                messageField.append(message + "\r\n");
            }
        }
        editB.addActionListener(new EditTariffAction());
        description01.setBounds(20, 20, 470, 20);
        description02.setBounds(20, 45, 470, 20);
        sumCountT.setBounds(20, 90, 25, 25);
        editB.setBounds(60, 90, 170, 25);
        scrollP.setBounds(20, 120, 400, 100);
        add(description01);
        add(description02);
        add(sumCountT);
        add(editB);
        add(scrollP);
    }

    /*
     * Integer aus Textfeld extrahieren
     */
    private int extractSumcount() {
        int tmpInt = 0;
        try {
            tmpInt = Integer.parseInt(sumCountT.getText());
//            if (tmpInt < 1) {
//                messageField.append(langBundle.getString("notanumber") + "\r\n");
//            }
        } catch (NumberFormatException e) {
            messageField.append(e.getMessage() + ":\r\n");
            tmpInt = 0;
        }
        return tmpInt;
    }

    /*
     * singleton
     */
    public static ShowTariffEdit getInstance() {
        if (showTariffEdit == null) {
            showTariffEdit = new ShowTariffEdit();
        }
        return showTariffEdit;
    }
}
