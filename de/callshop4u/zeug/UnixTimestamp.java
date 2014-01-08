/*
 * uniqueID (Unix Time) in Date
 * UniqueID: 1276974195.0
 */

package de.callshop4u.zeug;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author allapow
 */
public class UnixTimestamp {

    public static String getTimestamp(String unixtime) {
        int i = unixtime.indexOf(".");
        String s = unixtime.substring(0, i);
        String s2 = s + "000";
        Date date = new Date(Long.parseLong(s2));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.GERMANY);
        String res = dateFormat.format(date);
        return res;
    }
}
