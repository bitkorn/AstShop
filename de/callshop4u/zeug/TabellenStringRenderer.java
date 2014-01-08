/*
 * 
 */

package de.callshop4u.zeug;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class TabellenStringRenderer extends DefaultTableCellRenderer{

    @Override
    protected void setValue(Object value) {
        if(value != null) {
        setText(value.toString());
        } else {
            setText("");
        }
        setHorizontalAlignment(SwingConstants.RIGHT);
    }


}
