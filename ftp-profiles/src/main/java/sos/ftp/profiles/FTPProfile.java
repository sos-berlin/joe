package sos.ftp.profiles;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.widgets.Text;

import sos.util.SOSLogger;
import sos.util.SOSString;

/**
 * Beinhaltet alle FTP Zugangsdaten zu einer Profile.
 * 
 * Anhand der Zugangsdaten kann hier eine FTP Verbindung stattfinden. Hier
 * können Dateien per FTP transferiert werden.
 * 
 * @author Mueruevet Oeksuez
 * @author Uwe Risse
 * 
 */
public class FTPProfile {
    @SuppressWarnings("unused")
    private final String conSVNVersion = "$Id$";

    public static final String conFileNameExtension4JSObject = ".xml";

    /** Name der Profile */
    private String profilename = null;

    /** Host oder IP-Adresse, an die Dateien transferiert werden */
    private String host = null;

    /** Port, über den Dateien transferiert werden */
    private String port = null;

    /** Benutzername zur Anmeldung am FTP-Server */
    private String user = null;

    /** Soll der Password gespeichert werden. */
    private boolean savePassword = false;

    /** Kennwort zur Anmeldung am FTP-Server */
    private String password = null;

    /** Das Root Verzeichnis auf der FTP Server */
    private String root = null;

    /** Kopien der Datein werden im lokalen Verzeichnis abgelegt */
    private String localdirectory = null;

    /** Der Transfer-Modus kann die Werte ascii oder binary beinhalten */
    private String transfermode = null;

    /** Protokoll: FTP oder SFTP */
    private String protocol = null;

    /** Wird proxy verwendet? */
    private boolean use_proxy = false;

    /**
     * Der Wert des Parameters ist der Hostname oder die IP-Adresse eines
     * Proxies, über den die Verbindung zum SSH Server hergestellt wird. Die
     * Verwendung von Proxies ist optional.
     */
    private String proxy_server = null;

    /**
     * Der Wert des Parameters ist der Port des Proxies, über den eine
     * Verbindung zum SSH Server hergestellt wird.
     */
    private String proxy_port = null;

    /**
     * Der Parmeter spezifiziert die Authentifizierungsmethode am SSH Server,
     * unterstützt werden publickey und password.
     */
    private String auth_method = null;

    /**
     * Der Parameter bestimmt den Pfad und Namen einer Datei mit dem Private Key
     * des Benutzers, für den die Anmeldung am SSH Server erfolgt. Der Parameter
     * muss angegeben werden, wenn mit dem Parameterauth_methoddie
     * Authentifizierungsmethodepublickeybestimmt wurde.1 Falls die Datei mit
     * einem Kennwort geschützt ist, wird es mit dem Parameterpasswordangegeben.
     * */
    private String auth_file = null;

    /** @see sos.util.SOSString Object */
    private SOSString sosString = new SOSString();

  
    /** Hilfsvariable. Flag der beim Fehler auf true gesetzt wird */
    private boolean hasError = false;
    
    /**
     * Ein org.eclipse.swt.widgets.Text Objekt, indem alle Log Ausgaben
     * geschrieben werden, wenn der logText != null ist
     */
    protected static Text logtext = null;


    /** sos.util.SOSLogger Object */
    protected static SOSLogger logger = null;

    /**
     * Konstruktor
     * 
     * @param Properties
     *            . Beinhaltet alle Zugangsdaten.
     * @throws Exception
     */
    public FTPProfile(Properties prop) throws Exception {
        try {
            profilename = sosString.parseToString(prop, "profilename");
            host = sosString.parseToString(prop, "host");
            port = sosString.parseToString(prop, "port");
            user = sosString.parseToString(prop, "user");
            savePassword = sosString.parseToBoolean(sosString.parseToString(prop, "save_password").length() == 0 ? "true" : sosString.parseToString(prop, "save_password"));
            password = sosString.parseToString(prop, "password");
            root = sosString.parseToString(prop, "root");
            localdirectory = sosString.parseToString(prop, "localdirectory");
            transfermode = sosString.parseToString(prop, "transfermode");
            protocol = sosString.parseToString(prop, "protocol");
            use_proxy = sosString.parseToBoolean(sosString.parseToString(prop, "use_proxy"));
            proxy_server = sosString.parseToString(prop, "proxy_server");
            proxy_port = sosString.parseToString(prop, "proxy_port");
            auth_method = sosString.parseToString(prop, "auth_method");
            auth_file = sosString.parseToString(prop, "auth_file");
        } catch (Exception e) {
            throw new Exception("error in FTPProfile.init(), cause: " + e.toString(), e);
        }
    }

    /**
     * Liefert den Profilename
     * 
     * @return the profilename
     */
    public String getProfilename() {
        return profilename;
    }

    /**
     * Liefetr die Host oder IP-Adresse, an die Dateien transferiert werden
     * 
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Port, über den Dateien transferiert werden
     * 
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * Benutzername zur Anmeldung am FTP-Server
     * 
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Soll der Password gespeichert werden.
     * 
     * @return the savePassword
     */
    public boolean isSavePassword() {
        return savePassword;
    }

    /**
     * Kennwort zur Anmeldung am FTP-Server
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    public String getDecryptetPassword() throws Exception{
        String password = getPassword();
        if (password.length() > 0 && password.endsWith("=")) {
            password = SOSProfileCrypt.decrypt(getProfilename(), password);
        }
        return password;
    }
    /**
     * Das Root Verzeichnis auf der FTP Server
     * 
     * @return the root
     */
    public String getRoot() {
        return root;
    }

    /**
     * Kopien der Datein werden im lokalen Verzeichnis abgelegt
     * 
     * @return the localdirectory
     * @throws IOException 
     */
    public String getLocaldirectory() throws IOException {
        return new File(localdirectory).getCanonicalPath();
    }

    /**
     * Der Transfer-Modus kann die Werte ascii oder binary beinhalten
     * 
     * @return the transfermode
     */
    public String getTransfermode() {
        return transfermode;
    }

    /**
     * Protokoll: FTP oder SFTP
     * 
     * @return the protocol
     */
    public String getProtocol() {
        return protocol.toLowerCase();
    }

    /**
     * Wird proxy verwendet?
     * 
     * @return the use_proxy
     */
    public boolean getUseProxy() {
        return use_proxy;
    }

    /**
     * Der Wert des Parameters ist der Hostname oder die IP-Adresse eines
     * Proxies, über den die Verbindung zum SSH Server hergestellt wird. Die
     * Verwendung von Proxies ist optional.
     * 
     * @return the proxy_server
     */
    public String getProxyServer() {
        return proxy_server;
    }

    /**
     * Der Wert des Parameters ist der Port des Proxies, über den eine
     * Verbindung zum SSH Server hergestellt wird.
     * 
     * @return the proxy_port
     */
    public String getProxyPort() {
        return proxy_port;
    }

    /**
     * Der Parmeter spezifiziert die Authentifizierungsmethode am SSH Server,
     * unterstützt werden publickey und password.
     * 
     * @return the auth_method
     */
    public String getAuthMethod() {
        return auth_method;
    }

    /**
     * Der Parameter bestimmt den Pfad und Namen einer Datei mit dem Private Key
     * des Benutzers, für den die Anmeldung am SSH Server erfolgt. Der Parameter
     * muss angegeben werden, wenn mit dem Parameterauth_methoddie
     * Authentifizierungsmethodepublickeybestimmt wurde.1 Falls die Datei mit
     * einem Kennwort geschützt ist, wird es mit dem Parameterpasswordangegeben.
     * 
     * @return the auth_file
     */
    public String getAuthFile() {
        return auth_file;
    }

    /**
     * Kennwort zur Anmeldung am FTP-Server
     * 
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    // ******************************************************************************************************//

    /**
     * Ein org.eclipse.swt.widgets.Text Objekt, indem alle Log Ausgaben
     * geschrieben werden.
     */
    public void setLogText(org.eclipse.swt.widgets.Text text) {
        logtext = text;
    }


    /**
     * Ein Flag, der angibt, ob ein Fehler bereits zustande gekommen ist.
     * 
     * @return boolean
     */
    public boolean hasError() {
        return hasError;
    }

    /**
     * Loggen. Wenn ein SOSLogger Objekt übergeben wurde, dann werden die
     * Logausgaben im SOSLogger augegeben. Sonst in Standardausgaben
     * 
     * @param txt
     * @param level
     */
    public static void log(String txt, int level) {

        if (logger == null) {
            if (level > -1 || level < 10)
                System.out.println(txt);
            else
                System.err.println(txt);
            return;
        }
        try {
            switch (level) {

            case 1:
                logger.debug1(txt);
                break;

            case 2:
                logger.debug2(txt);
                break;

            case 3:
                logger.debug3(txt);
                break;

            case 4:
                logger.debug4(txt);
                break;

            case 5:
                logger.debug5(txt);
                break;

            case 6:
                logger.debug6(txt);
                break;

            case 7:
                logger.debug7(txt);
                break;

            case 8:
                logger.debug8(txt);
                break;

            case 9:
                logger.debug9(txt);
                break;

            case 10:
                logger.info(txt);
                break;

            case SOSLogger.WARN:
                logger.warn(txt);
                break;

            case SOSLogger.ERROR:
                logger.error(txt);
                break;

            default:
                logger.debug(txt);

                break;
            }
        } catch (Exception e) {
            System.out.print(txt);
        }
    }

    /**
     * @param logger
     *            the logger to set
     */
    public void setLogger(SOSLogger logger) {
        FTPProfile.logger = logger;
    }

   
}
