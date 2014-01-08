/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.callshop4u.panels;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author allapow
 */
public class TabellenHeadRenderer extends JLabel implements TableCellRenderer {

    Color borderColor = new Color(76, 74, 239);
    Color borderColorHigh = new Color(65, 63, 255);
    Color borderColorShadow = new Color(15, 12, 207);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
//        setBorder(new LineBorder(borderColor , 2));
        setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED, borderColorHigh, borderColorShadow));
        setFont(new java.awt.Font("Arial", 1, 16));
        return this;
    }
}
