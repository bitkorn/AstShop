/*
 * stellt die Verbindung zum Asterisk Manager her
 */
package de.callshop4u.ami;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;

public class AMIconn {

    /* die Factory wird für mehrere Connections genommen*/
    private static ManagerConnectionFactory factory;
    private static AsteriskAmiLauscher horcher;
    private static Logger logger = Logger.getRootLogger();

    public static void createFactory(String host, String user, String passwd) {
        if (factory == null) {
            factory = new ManagerConnectionFactory(host, user, passwd);
            horcher = new AsteriskAmiLauscher(factory);
            Thread t = new Thread(horcher);
            t.start();
        }
    }
    
    /**
     * AMIgui ruft es beim Connecten auf, um die Fähigkeit der Verbindung zu testen
     * @return
     */
    public static String testManagerConn() {
        ManagerConnection con = factory.createManagerConnection();
        String test = "";
        try {
            con.login();
            test = con.getState().toString();
        } catch (IllegalStateException ex) {
            logger.error(ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } catch (AuthenticationFailedException ex) {
            logger.error(ex.getMessage());
        } catch (TimeoutException ex) {
            logger.error(ex.getMessage());
        } 
        return test;
    }

    public static boolean isConnected() {
        if(horcher.getState()) {
            return true;
        } else {
            return false;
        }
    }
}
