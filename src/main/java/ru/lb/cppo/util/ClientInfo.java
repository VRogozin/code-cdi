package ru.lb.cppo.util;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

public class ClientInfo {
    private static final Logger log4jLogger = Logger.getLogger(ClientInfo.class);

    public ClientInfo() {
        // Empty Constructor
    }

    public String getClientInfo() {
    String sessionId = VaadinSession.getCurrent().getSession().getId();

    WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
    // Environment stuff
    String ipAddress = webBrowser.getAddress(); // May be null, especially if running in a Portlet.
    String userAgentInfo = webBrowser.getBrowserApplication();
    String touchDevice = String.valueOf( webBrowser.isTouchDevice() );
    String screenSize = webBrowser.getScreenWidth() + "x" + webBrowser.getScreenHeight();
    String locale = webBrowser.getLocale().toString();
    String isHttps = String.valueOf( webBrowser.isSecureConnection() );
    // Date-time stuff
    DateTime serverNow = DateTime.now( DateTimeZone.UTC );
    java.util.Date browserCurrentDate = webBrowser.getCurrentDate();
    DateTime browserCurrentDateTime = new DateTime( browserCurrentDate , DateTimeZone.UTC );
    String serverClientDifference = new Period( serverNow , browserCurrentDateTime ).toString();
    int offset = webBrowser.getTimezoneOffset();
    int rawOffset = webBrowser.getRawTimezoneOffset();
    Boolean isInDst = webBrowser.isDSTInEffect();
    int dst = webBrowser.getDSTSavings();
    String timeDescription = "ClientNow→" + browserCurrentDateTime + "/ServerNow→" + serverNow + "/ServerClientDiff→" + serverClientDifference + "/OffsetFromUTC→" + offset + "/RawOffsetFromUTC→" + rawOffset + "/InDST→" + isInDst + "/DST→" + dst;

    String description = new StringBuilder()
        .append( "{ SessionId=" ).append( sessionId )
        .append( " \n| IP_Address=" ).append( ipAddress )
        .append( " \n| HTTPS=" ).append( isHttps )
        .append( " \n| Locale=" ).append( locale )
        .append( " \n| TouchDevice=" ).append( touchDevice )
        .append( " \n| ScreenSize=" ).append( screenSize )
        .append( " \n| UserAgent=" ).append( userAgentInfo )
        .append( " \n| Time= " ).append( timeDescription )
        .append( " \n}" ).toString();

        log4jLogger.log(Level.INFO, "Client Info : "+ description);

        return description;
    }

    public String getClientIP() {

        WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        // Environment stuff
        String ipAddress = webBrowser.getAddress(); // May be null, especially if running in a Portlet.

        if (ipAddress.isEmpty())
        {
            ipAddress="not available";
        }
        return ipAddress;
    }

}