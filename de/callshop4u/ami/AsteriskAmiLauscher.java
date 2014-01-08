/*
 * Asterisk Manager Event Listener
 * handelt die Channels
 * füllt cdr_bill
 * 
 */
package de.callshop4u.ami;

import de.callshop4u.AstGUI;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.ShutdownEvent;

/**
 *
 * @author allapow
 */
public class AsteriskAmiLauscher extends AbstractManagerEventListener implements Runnable {

    private static ManagerConnection managerConnection;
    private static CDRmanager cdrManager;
    private static boolean state = false;
    private AstGUI astgui = AstGUI.getInstance();
    private ResourceBundle langBundle = astgui.getLangBundle();
    private static Logger logger = Logger.getRootLogger();

    AsteriskAmiLauscher(ManagerConnectionFactory factory) {
        cdrManager = CDRmanager.getInstance();
        managerConnection = factory.createManagerConnection();
        try {
            managerConnection.login();
            AstGUI.getInstance().setAmiStatus('g');
        } catch (IllegalStateException ex) {
            AstGUI.getInstance().setAmiStatus('r');
            JOptionPane.showMessageDialog(null, langBundle.getString("fehlerAmiVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage());
        } catch (IOException ex) {
            AstGUI.getInstance().setAmiStatus('r');
            JOptionPane.showMessageDialog(null, langBundle.getString("fehlerAmiVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage());
        } catch (AuthenticationFailedException ex) {
            AstGUI.getInstance().setAmiStatus('r');
            JOptionPane.showMessageDialog(null, langBundle.getString("fehlerAmiVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage());
        } catch (TimeoutException ex) {
            AstGUI.getInstance().setAmiStatus('r');
            JOptionPane.showMessageDialog(null, langBundle.getString("fehlerAmiVerbindung"), "Message", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage());
        }
        state = true;
        managerConnection.addEventListener(this);
    }

//    @Override
//    public void onManagerEvent(ManagerEvent event) {
//        DialEvent dialEvent = null;
//        if (event != null) {
//            if (event instanceof DialEvent) {
//                dialEvent = (DialEvent) event;
//                String cID = dialEvent.getCallerIdNum();
//                String unixtime = dialEvent.getUniqueId();
//                System.out.println("Caller ID null: " + cID);
//                System.out.println("UniqueID: " + unixtime);
//                synchronized (this) {
//                    cdrManager.updateStartDB(cID, unixtime);
//                }
//            }
//            
//        }
//    }

    @Override
    protected void handleEvent(DialEvent event) {
        System.out.println("DialEvent Art: " + event.toString());
        if(event instanceof DialEvent) {
            DialEvent dialEvent = (DialEvent)event;
            if(dialEvent.getSubEvent().equalsIgnoreCase("begin")) {
                String cID = dialEvent.getCallerIdNum();
                String unixtime = dialEvent.getUniqueId();
                String dialstatus = dialEvent.getDialStatus();
                System.out.println("=======DialStatus: " + dialstatus);
    //            System.out.println("Caller ID null: " + cID);
    //            System.out.println("UniqueID: " + unixtime);
                synchronized (this) {
                    cdrManager.updateStartDB(cID, unixtime);
                }
            }
        }
    }

    @Override
    protected void handleEvent(CdrEvent event) {
        String cID = event.getCallerId();
        String src = event.getSrc();
        String dest = event.getDestination();
        String unixtime = event.getUniqueId();
        Date startTime = event.getAnswerTimeAsDate();
        Date endTime = event.getEndTimeAsDate();
        int billsec = event.getBillableSeconds();
        String dispos = event.getDisposition();
        System.out.println("Caller ID: " + cID);
        System.out.println("src: " + src);
        System.out.println("Destination: " + dest);
        System.out.println("UniqueId: " + unixtime);
        System.out.println("AnswerTimeAsDate: " + startTime);
        System.out.println("EndTimeAsDate: " + endTime);
        System.out.println("BillableSeconds: " + billsec);
        System.out.println("Disposition: " + dispos);

        synchronized (this) {
            cdrManager.updateEndDB(src, dest, unixtime, startTime, endTime, billsec, dispos);
        }
    }

    // listen Shutdown
    @Override
    protected void handleEvent(ShutdownEvent event) {
        // Returns true if the server has been restarted; false  if it has been halted.
        if (event.getRestart()) {
            AstGUI.getInstance().setAmiStatus('g');
        } else if (!event.getRestart()) {
            AstGUI.getInstance().setAmiStatus('r');
        } else {
            AstGUI.getInstance().setAmiStatus('r');
        }

    }

    // A ConnectEvent is triggered after successful login to the Asterisk server.
    @Override
    protected void handleEvent(ConnectEvent event) {
        AstGUI.getInstance().setAmiStatus('g');
    }

    public void run() {
    }

    public boolean getState() {
        return state;
    }
}
