/*
 * increment or decrement all rows in column sell-price
 */
package de.callshop4u.zeug;

import de.callshop4u.DBconn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class TariffEdit {
    
    private static Logger logger = Logger.getRootLogger();

    public TariffEdit() {
    }
    
    public static String editSellprice(int sumCount) {
        if(sumCount > 10) {
            return "max 10 Cent";
        }
        if(sumCount < 1 && sumCount > 0) {
            return "not allowed number";
        }
        String message = "";
        String sqlQuery = "SELECT * FROM tarif";
        String sqlEdit = "UPDATE tarif SET preis=? WHERE vorwahl=?";
        try {
            PreparedStatement pstmtUpdate = DBconn.con.prepareStatement(sqlEdit);
            Statement stmtQuery = DBconn.con.createStatement();
            ResultSet result = stmtQuery.executeQuery(sqlQuery);
            result.last();
            int resultLength = result.getRow();
            result.first();
            for(int i = 0; i < resultLength; i++) {
                String tmpVorwahl = result.getString("vorwahl");
                String tmpStringPreis = result.getString("preis");
                Float tmpFloat = Float.parseFloat(tmpStringPreis);
                Float newPrice = tmpFloat + sumCount;
                tmpStringPreis = newPrice.toString();
                int commaplace = tmpStringPreis.indexOf(".");
                if(tmpStringPreis.length() > commaplace + 3) {
                    tmpStringPreis = tmpStringPreis.substring(0, commaplace +3);
                }
                pstmtUpdate.setString(1, tmpStringPreis);
                pstmtUpdate.setString(2, tmpVorwahl);
                int updateCount = pstmtUpdate.executeUpdate();
                if(updateCount != 1) {
                    message = "error while update: " + tmpVorwahl + " " + tmpStringPreis;
                    return message;
                }
                result.next();
            }
            
            message = "success";
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            message = ex.getMessage();
        }
        
        return message;
    }
    
}
