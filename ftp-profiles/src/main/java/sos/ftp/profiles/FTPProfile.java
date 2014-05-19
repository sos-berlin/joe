package sos.ftp.profiles;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import sos.net.SOSFTP;
import sos.net.SOSFTPS;
import sos.net.SOSFileTransfer;
import sos.net.SOSSFTP;
import sos.util.SOSLogger;
import sos.util.SOSString;

/**
 * Beinhaltet alle FTP Zugangsdaten zu einer Profile.
 * 
 * Anhand der Zugangsdaten kann hier eine FTP Verbindung stattfinden. Hier
 * können Dateien per FTP transferiert werden.
 * 
 * @author Mueruevet Oeksuez
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

    /** Hilfsvariable. Überprüft, ob die FTP Verbindung erfolgt wurde */
    private boolean isLoggedIn = false;

    /** Hilfsvariable. aktuelle Verzeichnis */
    private String workingDirectory = "";

    /** Hilfsvariable. Flag der beim Fehler auf true gesetzt wird */
    private boolean hasError = false;

    /**
     * The FTP server will always reply the ftp error codes, see
     * http://www.the-eggman.com/seminars/ftp_error_codes.html
     */
    private final int ERROR_CODE = 300;

    /** @see sos.net.SOSFileTransfer */
    private SOSFileTransfer ftpClient = null;

    /**
     * Ein org.eclipse.swt.widgets.Text Objekt, indem alle Log Ausgaben
     * geschrieben werden, wenn der logText != null ist
     */
    protected static Text logtext = null;

    /**
     * sFTP mit publickey und Passphares hat nicht geklappt, Rückfall auf nur
     * Password*
     */
    private boolean tryAgain = false;

    /** */
    private static String PROFILE_PREFIX = "XXXSOSXX";

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
     */
    public String getLocaldirectory() {
        return localdirectory;
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
        return protocol;
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
     * Stellt eine FTP Verbindung her
     * 
     * @return sos.net.SOSFileTransfer
     * @throws Exception
     */
    public SOSFileTransfer connect() throws Exception {

        log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);

        if (sosString.parseToString(profilename).length() == 0) {
            FTPProfileDialog.message("Please first define a Profile", SWT.ICON_WARNING);
            return null;
        }
        isLoggedIn = false;
        hasError = false;
        workingDirectory = "";

        if (isLoggedIn && ftpClient != null) {

            if (logtext != null)
                if (logtext != null)
                    logtext.append("..ftp is connected");
            return null;
        }

        SOSFileTransfer sosftp = null;

        String protocol = "ftp";
        String host = "";
        int port = 22;
        isLoggedIn = false;
        String user = "";
        String password = "";

        String account = "";

        String authenticationFilename = "";
        String authenticationMethod = "publickey";
        String proxyHost = "";
        String proxyPassword = "";
        String proxyUser = "";

        String transferMode = "binary";

        int proxyPort = 21;
        try {

            protocol = sosString.parseToString(getProtocol()).length() > 0 ? sosString.parseToString(getProtocol()) : "ftp";
            host = sosString.parseToString(getHost());
            user = sosString.parseToString(getUser());
            authenticationMethod = sosString.parseToString(getAuthMethod()).length() > 0 ? sosString.parseToString(getAuthMethod()) : "publickey";
            String sPort = sosString.parseToString(getPort());

            if (sPort.length() > 0)
                port = Integer.parseInt(sosString.parseToString(sPort));

            if (authenticationMethod.length() > 0 && authenticationMethod.equals("both"))
                authenticationMethod = "publickey";

            authenticationFilename = sosString.parseToString(getAuthFile()).length() > 0 ? sosString.parseToString(getAuthFile()) : "";

            if (host == null || host.length() == 0)
                throw new Exception("no host was specified");
            if (user == null || user.length() == 0)
                throw new Exception("no user was specified");
            password = sosString.parseToString(getPassword());

            // entschlüsseln der Password
            try {

                String key = PROFILE_PREFIX;
                try {
                    if (Class.forName("sos.scheduler.editor.app.Options") != null) {
                        Class options = Class.forName("sos.scheduler.editor.app.Options");
                        Method method = options.getMethod("getProperty", new Class[] { String.class });
                        Object okey = method.invoke(ftpClient, new Object[] { "profile.timestamp." + profilename });
                        if (okey != null) {
                            key = okey.toString();
                            if (key.length() > 8)
                                key = key.substring(key.length() - 8);
                        }

                    }
                } catch (java.lang.ClassNotFoundException c) {
                    // ignore error
                }

                if (password.length() > 0 && sosString.parseToString(key).length() > 0 && password.endsWith("=")) {
                    password = SOSProfileCrypt.decrypt(key, password);
                }

            } catch (Exception e) {
                throw new Exception("could not decrypt password, cause: " + e.toString());
            }

            if (tryAgain) {

                authenticationMethod = "password";
                authenticationFilename = "";
            }

            // Verbindung konnte nicht hergestellt werden. Es wird nochmals
            // versucht. Wenn
            // Password nicht angegeben wurde, dann kommt ein Dialog, indem der
            // Password geschrieben werden kann
            if (password.length() == 0 && !protocol.equalsIgnoreCase("sftp") || (tryAgain && sosString.parseToString(password).length() == 0)) {
                Shell shell = new Shell();

                shell.pack();
                FTPPopUpDialog fTPPopUpDialog = new FTPPopUpDialog(shell);
                fTPPopUpDialog.setText("Password");
                fTPPopUpDialog.open(this);
                while (!shell.isDisposed()) {
                    if (!shell.getDisplay().readAndDispatch())
                        shell.getDisplay().sleep();
                }

                password = getPassword();
            }

            if (getTransfermode() != null)
                transferMode = sosString.parseToString(getTransfermode());
            if (protocol.equalsIgnoreCase("ftp")) {
                sosftp = new SOSFTP(host);
                ftpClient = sosftp;
                if (logtext != null)
                    logtext.append("..ftp server reply [init] [host=" + host + "], [port=" + port + "]: " + ftpClient.getReplyString());

                if (account != null && account.length() > 0) {
                    isLoggedIn = ((SOSFTP) sosftp).login(user, password, account);
                    if (logtext != null)
                        logtext.append("..ftp server reply [login] [user=" + user + "], [account=" + account + "]: " + ftpClient.getReplyString());
                } else {
                    isLoggedIn = ((SOSFTP) sosftp).login(user, password);
                    if (logtext != null)
                        logtext.append("..ftp server reply [login] [user=" + user + "]: " + ftpClient.getReplyString());
                }
                if (!isLoggedIn || ((SOSFTP) sosftp).getReplyCode() > ERROR_CODE) {
                    throw new Exception("..ftp server reply [login failed] [user=" + user + "], [account=" + account + "]: " + ftpClient.getReplyString());
                }
            } else if (protocol.equalsIgnoreCase("sftp")) {
                try {
                    Class sftpClass;
                    try {
                        sftpClass = Class.forName("sos.net.SOSSFTP");
                        Constructor con = sftpClass.getConstructor(new Class[] { String.class, int.class });
                        ftpClient = (SOSFileTransfer) con.newInstance(new Object[] { host, new Integer(port) });
                    } catch (Exception e) {
                        if (logtext != null)
                            logtext.append("Failed to initialize SOSSFTP class, need recent sos.net.jar and trilead jar. " + e);
                        throw new Exception("Failed to initialize SOSSFTP class, need recent sos.net.jar and trilead jar. " + e, e);

                    }
                    Class[] stringParam = new Class[] { String.class };
                    Method method = sftpClass.getMethod("setAuthenticationFilename", stringParam);
                    method.invoke(ftpClient, new Object[] { authenticationFilename });
                    method = sftpClass.getMethod("setAuthenticationMethod", stringParam);
                    method.invoke(ftpClient, new Object[] { authenticationMethod });
                    method = sftpClass.getMethod("setPassword", stringParam);
                    method.invoke(ftpClient, new Object[] { password });
                    method = sftpClass.getMethod("setProxyHost", stringParam);
                    method.invoke(ftpClient, new Object[] { proxyHost });
                    method = sftpClass.getMethod("setProxyPassword", stringParam);
                    method.invoke(ftpClient, new Object[] { proxyPassword });
                    method = sftpClass.getMethod("setProxyPort", new Class[] { int.class });
                    method.invoke(ftpClient, new Object[] { new Integer(proxyPort) });
                    method = sftpClass.getMethod("setProxyUser", stringParam);
                    method.invoke(ftpClient, new Object[] { proxyUser });
                    method = sftpClass.getMethod("setUser", stringParam);
                    method.invoke(ftpClient, new Object[] { user });

                    method = sftpClass.getMethod("connect", new Class[] {});
                    method.invoke(ftpClient, new Object[] {});
                    isLoggedIn = true;
                    if (logtext != null)
                        logtext.append("..sftp server logged in [user=" + user + "], [host=" + host + "]");
                    sosftp = (sos.net.SOSSFTP) ftpClient;

                } catch (Exception e1) {
                    if (sosString.parseToString(getAuthMethod()).equalsIgnoreCase("both") && tryAgain == false)
                        tryAgain = true;
                    else
                        tryAgain = false;
                    throw new Exception("..sftp server login failed [user=" + user + "], [host=" + host + "]: " + e1, e1);

                }
            } else if (protocol.equalsIgnoreCase("ftps")) {

                try {
                    if (proxyHost != null && proxyPort != 0) {
                        System.getProperties().setProperty("proxyHost", proxyHost);
                        System.getProperties().setProperty("proxyPort", String.valueOf(proxyPort));
                        System.getProperties().setProperty("proxySet", "true");
                    }

                    SOSFTPS sosftps = new SOSFTPS(host, port);
                    ftpClient = sosftps;
                    if (logtext != null)
                        logtext.append("..ftp server reply [init] [host=" + host + "], [port=" + port + "]: " + ftpClient.getReplyString());
                    isLoggedIn = sosftps.login(user, password);
                    if (logtext != null)
                        logtext.append("..ftp server reply [login] [user=" + user + "]: " + ftpClient.getReplyString());
                    if (!isLoggedIn || sosftps.getReplyCode() > ERROR_CODE) {
                        throw new Exception("..ftp server reply [login failed] [user=" + user + "], [account=" + account + "]: " + ftpClient.getReplyString());
                    }
                    isLoggedIn = true;
                    sosftp = (sos.net.SOSFTPS) ftpClient;
                } catch (Exception e) {
                    throw new Exception("..ftps server login failed [user=" + user + "], [host=" + host + "]: " + e);
                }

            } else {
                throw new Exception("Unknown protocol: " + protocol);
            }
            if (ftpClient instanceof SOSFTP) {
                sosftp = (SOSFTP) ftpClient;
                if (transferMode.equalsIgnoreCase("ascii")) {
                    if (((SOSFTP) sosftp).ascii()) {
                        if (logtext != null)
                            logtext.append("..using ASCII mode for file transfer");
                        if (logtext != null)
                            logtext.append("..ftp server reply [ascii]: " + ftpClient.getReplyString());
                    } else {
                        throw new Exception(".. could not switch to ASCII mode for file transfer ..ftp server reply [ascii]: " + ftpClient.getReplyString());
                    }
                } else {
                    if (((SOSFTP) sosftp).binary()) {
                        if (logtext != null)
                            logtext.append("using binary mode for file transfers.");
                        if (logtext != null)
                            logtext.append("..ftp server reply [binary]: " + ftpClient.getReplyString());
                    } else {
                        throw new Exception(".. could not switch to binary mode for file transfer ..ftp server reply [ascii]: " + ftpClient.getReplyString());
                    }
                }
            }
            saveLastProfile(profilename);

            if (getRoot() != null && getRoot().length() > 0) {
                changeDirectory(getRoot());
            }

        } catch (Exception ex) {
            isLoggedIn = false;
            hasError = true;
            log("..error in ftp server init with [host=" + host + "], [port=" + port + "], cause: " + FTPDialogListener.getErrorMessage(ex), SOSLogger.WARN);
            if (logtext != null)
                logtext.append("..error in ftp server init with [host=" + host + "], [port=" + port + "], cause: " + FTPDialogListener.getErrorMessage(ex) + "\n");
            if (tryAgain) {
                if (logtext != null)
                    logtext.append("..try connect with Authentication Method=password." + "\n");
                connect();
                tryAgain = false;
            }
            FTPProfileDialog.message("..error in ftp server init with [host=" + host + "], [port=" + port + "], cause: " + FTPDialogListener.getErrorMessage(ex), SWT.ICON_ERROR);
            try {
                if (sosftp != null)
                    sosftp.disconnect();
            } catch (Exception e) {
            }
        }

        return sosftp;
    }

    private void saveLastProfile(String profile) {
        try {
            Class options = getOptionClass();
            if (options != null) {
                Method method = options.getMethod("setProperty", new Class[] { String.class, String.class });
                method.invoke(ftpClient, new Object[] { "last_profile", profile });
                method = options.getMethod("saveProperties", new Class[] {});

            }

        } catch (java.lang.Exception c) {
            // ignore error
            // System.out.print("test: " + c.toString());
        }

    }

    protected String getPass(String profilename) {
        String pass = PROFILE_PREFIX;
        try {
            if (pass.length() > 8) {
                pass = pass.substring(pass.length() - 8);
            }
            Class options = getOptionClass();
            if (options != null) {
                pass = String.valueOf(sos.util.SOSUniqueID.get());
                Method method = options.getMethod("setProperty", new Class[] { String.class, String.class });
                method.invoke(ftpClient, new Object[] { "profile.timestamp." + profilename, pass });

                method = options.getMethod("saveProperties", new Class[] {});
                method.invoke(ftpClient, new Object[] {});

            }

        } catch (java.lang.Exception c) {
            // ignore error
            // System.out.print("test: " + c.toString());
        }
        return pass;

    }

    private Class getOptionClass() {
        Class options = null;
        try {
            if (Class.forName("sos.scheduler.editor.app.Options") != null) {
                options = Class.forName("sos.scheduler.editor.app.Options");
            }
        } catch (java.lang.ClassNotFoundException c) {
            // tu nix
        } catch (java.lang.Exception c) {
            // tu nix
        }
        return options;
    }

    /**
     * Liefert alle Dateinamen aus der Verzeichnis directory
     * 
     * @param directory
     * @return Vector
     */
    public Vector getList(String directory) {
        // liste alle Dateinamen.
        Vector vec = null;
        try {
            changeDirectory(directory);
            vec = ftpClient.nList("");
        } catch (Exception e) {

            FTPProfileDialog.message("..ftp server reply [cd] [nlist]: " + ftpClient.getReplyString() + ", cause: " + e.getMessage(), SWT.ICON_WARNING);

            hasError = true;
            if (logtext != null)
                logtext.append("..ftp server reply [cd] [nlist]: " + ftpClient.getReplyString() + ", cause: " + e.getMessage());
        }
        return vec;
    }

    /**
     * Liefert alle Dateinamen aus der aktuelle Verzeichnis
     * 
     * @param directory
     * @return Vector
     */

    public Vector getList() {
        // liste alle Dateinamen.
        Vector vec = null;
        try {
            vec = ftpClient.nList("");
        } catch (Exception e) {

            FTPProfileDialog.message("..ftp server reply [cd] [nlist]: " + ftpClient.getReplyString() + ", cause: " + e.getMessage(), SWT.ICON_WARNING);

            hasError = true;
            if (logtext != null)
                logtext.append("..ftp server reply [cd] [nlist]: " + ftpClient.getReplyString() + ", cause: " + e.getMessage());
        }
        return vec;
    }

    /**
     * 
     * Wechselt in das Verzeichnis directory und liefert einen HahsMap zurück.
     * Folgende Bedeutung haben die Werte der HashMap.
     * 
     * [key, value] 1. [a.txt, file] bedeutet, in dem Verzeichnis (directory)
     * existiert eine Datei a.txt 2. [a.txt_size, 3] bedeutet, die Datei a.txt
     * ist 3 byte gross 3. [tmp, dir] bedeutet, das tmp ein Verzeichnis ist.
     * 
     * @param directory
     * @return HashMap
     * @throws Exception
     */
    public HashMap changeDirectory(String directory) throws Exception {
        FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
        HashMap listnames = new HashMap();

        try {
            directory = directory.replaceAll("\\\\", "/");

            if (directory == null || directory.length() == 0)
                directory = (sosString.parseToString(getRoot()).length() > 0 ? sosString.parseToString(getRoot()) : ".");

            if (!(directory.startsWith(".") || directory.startsWith("/")))
                directory = "./" + directory;

            if (ftpClient instanceof SOSFTP) {

                if (directory.startsWith("./")) {

                    if (workingDirectory.length() >= 0 && workingDirectory.equalsIgnoreCase(directory)) {
                        directory = ".";
                    } else if (workingDirectory.length() >= directory.length() && workingDirectory.startsWith(directory)) {

                        String curWorkingDirectory = workingDirectory.substring(directory.length());
                        curWorkingDirectory = curWorkingDirectory.endsWith("/") ? curWorkingDirectory.substring(0, curWorkingDirectory.length()) : curWorkingDirectory;
                        int countOfcdUP = curWorkingDirectory.split("/").length;
                        for (int i = 0; i < countOfcdUP; i++)
                            cdUP();

                    } else if (directory.startsWith(workingDirectory) || workingDirectory.length() == 0) {

                        String curWorkingDirectory = directory;
                        directory = "./" + directory.substring(workingDirectory.length());
                        workingDirectory = curWorkingDirectory;

                    } else {

                        String curWorkingDirectory = workingDirectory;
                        int countOfcdUP = curWorkingDirectory.split("/").length;
                        if (curWorkingDirectory.endsWith("/") || curWorkingDirectory.startsWith("./") || curWorkingDirectory.startsWith("/"))
                            ;
                        countOfcdUP--;
                        for (int i = 0; i < countOfcdUP; i++) {
                            cdUP();
                        }

                        workingDirectory = directory;

                    }
                } else if (directory.equals("..")) {
                    int pos = workingDirectory.endsWith("/") ? workingDirectory.lastIndexOf("/", 1) : workingDirectory.lastIndexOf("/");
                    if (pos == -1)
                        pos = directory.length();
                    if (!workingDirectory.equals("") && workingDirectory.length() >= pos)
                        workingDirectory = workingDirectory.substring(0, pos);
                    else
                        workingDirectory = "";

                } else {
                    workingDirectory = "";

                }
                hasError = false;
            }

            if (ftpClient.changeWorkingDirectory(directory)) {
                if (logtext != null)
                    logtext.append("..ftp server reply [cd] [directory ftp_file_path=" + directory + "]: " + ftpClient.getReplyString());

                java.util.Vector onlyfiles = ftpClient.nList(".");
                java.util.Vector onlyfileNames = new  java.util.Vector<String>();
                for(int i=0; i < onlyfiles.size(); i++){
                	File f = new File((String) onlyfiles.get(i));
                	onlyfileNames.add(f.getName());
                 
                }
                 String[] filesAndDirs = ftpClient.listNames(".");

                // String[] filesAndDirs = ftpClient.listNames("../..");

                for (int i = 0; i < filesAndDirs.length; i++) {
                    String fdFilenames = filesAndDirs[i];
                   
                    if (onlyfileNames.contains(fdFilenames)) {
                        listnames.put(fdFilenames, "file");
                        listnames.put(fdFilenames + "_size", String.valueOf(ftpClient.size(fdFilenames)));

                    } else {
                        if (!fdFilenames.equals(".") && !fdFilenames.equals(".."))
                            listnames.put(fdFilenames, "dir");
                    }
                }
                hasError = false;
            } else {
                throw new Exception("..ftp server reply [cd] [directory ftp_file_path=" + directory + "]: " + ftpClient.getReplyString());
            }
        } catch (Exception e) {

            FTPProfileDialog.message("could not change Directory [" + directory + "] cause:" + e.getMessage(), SWT.ICON_WARNING);

            hasError = true;
            if (logtext != null)
                logtext.append("could not change Directory [" + directory + "] cause:" + e.getMessage());
        }
        return listnames;
    }

    /**
     * Wechslet in das Parent Verzeichnis
     * 
     * @return java.util.HashMap
     * @throws Exception
     */
    public HashMap cdUP() throws Exception {
        return changeDirectory("..");
    }

    /**
     * Stellt eine Verbindung mit der profile und wechselt in das Verzeichnis
     * directory
     * 
     * @param profile
     * @param directory
     * @return java.uti.HashMap
     * @throws Exception
     */
    public HashMap changeDirectory(String profile, String directory) throws Exception {
        log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
        try {
            if (isLoggedIn)
                disconnect();

            if (!isLoggedIn)
                connect();

            if (!isLoggedIn)// connect war nicht erfolgreich
                return new HashMap();
            // TODO 2
            // init(profile);

            // ftpClient.changeWorkingDirectory(directory));
        } catch (Exception e) {

            hasError = true;
            FTPProfileDialog.message("could not change Directory [" + directory + "] cause:" + e.getMessage(), SWT.ICON_WARNING);
        }

        return changeDirectory(directory);

    }

    public void disconnect() throws Exception {
        log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
        if (isLoggedIn && ftpClient != null) {
            try {
                workingDirectory = "";
                if (ftpClient.isConnected())
                    ftpClient.disconnect();
            } catch (Exception e) {

                if (logtext != null) {
                    logtext.append("could not disconnect FTP Server cause: " + e.getMessage());
                }
            }
        }
        isLoggedIn = false;
    }

    public boolean isConnect() {

        if (isLoggedIn && ftpClient != null)
            return true;
        else
            return false;
    }

    /**
     * Holt eine Datei per ftp
     * 
     * @param filename
     *            -> Datei, die auf der FTP Server liegt und abgeholt werden
     *            soll
     * @param targetfile
     *            -> pfad und Dateiname, die auf lokalem Rechner abgelegt werden
     *            soll
     * @return
     * @throws Exception
     */
    public long getFile(String filename, String targetfile) throws Exception {
        long l = 0;
        try {
            if (ftpClient == null) {
                FTPProfileDialog.message("ftpClient is null. Please call first FTPDialog.connect", SWT.ICON_ERROR);
                throw new Exception("ftpClient is null. Please call first FTPDialog.connect");
            }
            l = ftpClient.getFile(filename, targetfile, false);
        } catch (Exception e) {
            log("error in FTPDialogListener.getFile(), could not get File [" + filename + "] : cause: " + e.toString(), SOSLogger.WARN);
            if (logtext != null)
                logtext.append("could not get File [" + filename + "] :" + e.getMessage());
            hasError = true;
        }
        return l;
    }

    /**
     * Holt eine Datei per ftp
     * 
     * @param filename
     *            -> Datei, die auf der FTP Server liegt und abgeholt werden
     *            soll
     * @param targetfile
     *            -> pfad und Dateiname, die auf lokalem Rechner abgelegt werden
     *            soll
     * @return
     * @throws Exception
     */
    public long getFile(String filename, File targetfile) throws Exception {
        long l = 0;
        try {
            if (ftpClient == null) {
                FTPProfileDialog.message("ftpClient is null. Please call first FTPDialog.connect", SWT.ICON_ERROR);
                throw new Exception("ftpClient is null. Please call first FTPDialog.connect");
            }
            l = ftpClient.getFile(filename, targetfile.getCanonicalPath(), false);
        } catch (Exception e) {
            log("error in FTPDialogListener.getFile(), could not get File [" + filename + "] : cause: " + e.toString(), SOSLogger.WARN);
            if (logtext != null)
                logtext.append("could not get File [" + filename + "] :" + e.getMessage());
            hasError = true;
        }
        return l;
    }

    /**
     * Holt eine Datei per ftp und hängt diese unterhalb vom targetFile
     * 
     * @param filename
     *            -> Datei, die auf der FTP Server liegt und abgeholt werden
     *            soll
     * @param targetfile
     *            -> pfad und Dateiname, die auf lokalem Rechner abgelegt werden
     *            soll
     * @return long --> Dateigrösse
     * @throws Exception
     */
    public long appendFile(String filename, String targetfile) throws Exception {
        long l = 0;
        try {
            if (ftpClient == null) {
                FTPProfileDialog.message("ftpClient is null. Please call first FTPDialog.connect", SWT.ICON_ERROR);
                throw new Exception("ftpClient is null. Please call first FTPDialog.connect");
            }
            l = ftpClient.getFile(filename, targetfile, true);
        } catch (Exception e) {
            log("error in FTPDialogListener.getFile(), could not get File [" + filename + "] : cause: " + e.toString(), SOSLogger.WARN);
            if (logtext != null)
                logtext.append("could not get File [" + filename + "] :" + e.getMessage());
            hasError = true;
        }
        return l;
    }

    /**
     * Holt eine Datei per ftp und hängt diese unterhalb vom targetFile
     * 
     * @param filename
     *            -> Datei, die auf der FTP Server liegt und abgeholt werden
     *            soll
     * @param targetfile
     *            -> pfad und Dateiname, die auf lokalem Rechner abgelegt werden
     *            soll
     * @return long --> Dateigrösse
     * @throws Exception
     */
    public long appendFile(String filename, File targetfile) throws Exception {
        long l = 0;
        try {
            if (ftpClient == null) {
                FTPProfileDialog.message("ftpClient is null. Please call first FTPDialog.connect", SWT.ICON_ERROR);
                throw new Exception("ftpClient is null. Please call first FTPDialog.connect");
            }
            l = ftpClient.getFile(filename, targetfile.getCanonicalPath(), true);
        } catch (Exception e) {
            log("error in FTPDialogListener.getFile(), could not get File [" + filename + "] : cause: " + e.toString(), SOSLogger.WARN);
            if (logtext != null)
                logtext.append("could not get File [" + filename + "] :" + e.getMessage());
            hasError = true;
        }
        return l;
    }

    /**
     * Holt eine Datei per FTP und speichert diese auf das lokalverzeichnis ab,
     * soweit diese Konfiguriert wurde. Sonst wird eine temporäres Verzeichnis
     * abgelegt. Wird ein Parameter subFolder übergeben, dann wird die Datei
     * unter locallDirectory + subFolder abgelegt.
     * 
     * @param filename
     * @param subFolder
     * @return
     * @throws Exception
     */
    public String openFile(String filename, String subFolder) throws Exception {
        String targetfile = null;
        boolean deleteTmpFile = false;// wenn locahdirectory nicht angegeben
                                      // ist, dann temp Verzeichnis bilden und
                                      // diese anschliessend löschen
        try {

            if (ftpClient == null) {
                FTPProfileDialog.message("ftpClient is null. Please call first FTPDialog.connect", SWT.ICON_ERROR);
                throw new Exception("ftpClient is null. Please call first FTPDialog.connect");
            }

            // targetfile =
            // sosString.parseToString(currProfile.get("localdirectory" ));
            targetfile = sosString.parseToString(getLocaldirectory());
            if (targetfile.length() == 0) {
                targetfile = System.getProperty("java.io.tmpdir");
                deleteTmpFile = true;
            }

            targetfile = targetfile.replaceAll("\\\\", "/");
            if (subFolder != null)
                targetfile = (targetfile.endsWith("/") || targetfile.endsWith("\\") ? targetfile + subFolder : targetfile + "/" + subFolder);

            File file = new File(targetfile);
            if (!file.exists()) {
                file.mkdirs();
            }

            if (deleteTmpFile)
                file.deleteOnExit();

            // targetfile = (targetfile.endsWith("/") ||
            // targetfile.endsWith("\\") ? targetfile : targetfile + "/")+ new
            // java.io.File(filename).getName();
            targetfile = new File(targetfile, new java.io.File(filename).getName()).getCanonicalPath();

            if (ftpClient instanceof SOSFTP) {
                if (filename.startsWith("./")) {
                    filename = "./" + filename.substring(workingDirectory.length());
                }
            }
            // targetfile = "c:/temp/1/1.txt";
            long l = ftpClient.getFile(filename, targetfile, false);

            if (l == -1)
                throw new Exception(" could not get file [filename=" + filename + "], [target=" + targetfile + "], cause " + ftpClient.getReplyString());

            if (logtext != null)
                logtext.append("..ftp server reply [getfile] [size= " + l + "] :" + ftpClient.getReplyString());

        } catch (Exception e) {
            log("error in FTPDialogListener.getFile(), could not get File [" + filename + "] : cause: " + e.toString(), SOSLogger.WARN);
            if (logtext != null)
                logtext.append("could not get File [" + filename + "] :" + e.getMessage());
            hasError = true;
        }
        return targetfile;
    }

    /**
     * Löscht die Datei file auf der FTP Server.
     * 
     * @param file
     */
    public void removeFile(String file) {
        try {
            if (ftpClient != null && ftpClient.delete(file)) {
                if (logtext != null)
                    logtext.append("..ftp server reply [delete] [file=" + file + "]: " + ftpClient.getReplyString());
            } else {
                if (logtext != null)
                    logtext.append("..ftp server reply [could not delete] [file=" + file + "]: " + ftpClient.getReplyString());
                hasError = true;
            }
        } catch (Exception e) {

            if (logtext != null)
                logtext.append("could not delete file [" + file + "] cause:" + e.getMessage());

            log("could not delete file [" + file + "] cause:" + e.toString(), SOSLogger.WARN);
            hasError = true;
        }
    }

    /**
     * Speziell für den Scheduler Editor.
     * 
     * @param String
     *            source
     * @param String
     *            source
     * @param String
     *            target
     * @param String
     *            java.util.ArrayList
     * @param java
     *            .util.HashMap
     */
    public void saveHotFolderAs(String source, String target, ArrayList listOfHotFolderElements, HashMap changes) {
        try {
            Vector listOfExistFiles = new Vector(); // überprüft ob das
                                                    // HotFolderElement
                                                    // existiert. das kann
                                                    // passieren, wenn ein
                                                    // Element neu angelegt wird
                                                    // und ohne zwischespeichern
                                                    // der Name verändert wird.
            if (ftpClient != null) {
                HashMap h = changeDirectory(target);
                java.util.Iterator i = h.keySet().iterator();
                while (i.hasNext()) {
                    Object key = i.next();
                    // String value = h.get(key) != null ? h.get(key).toString()
                    // : "";
                    if (key != null && !key.toString().endsWith("_size"))
                        listOfExistFiles.add(key);

                }
                // listOfExistFiles = ftpClient.nList(target);

            }

            ArrayList listOfRemovedFiles = new ArrayList(); // hilfsvariable:
                                                            // verhindert das
                                                            // zweimal löschen
                                                            // der Hot Folder
                                                            // Element

            source = source.endsWith("/") ? source : source + "/";
            target = target.endsWith("/") ? target : target + "/";

            if (listOfHotFolderElements != null) {
                for (int i = 0; i < listOfHotFolderElements.size(); i++) {
                    String filename = sosString.parseToString(listOfHotFolderElements.get(i));
                    filename = new File(filename).getName();
                    System.out.println(filename);
                    String hotElementname = "";
                    String attrname = "";
                    hotElementname = filename.substring(0, filename.lastIndexOf(conFileNameExtension4JSObject));
                    hotElementname = hotElementname.substring(hotElementname.lastIndexOf(".") + 1);
                    attrname = filename.substring(0, filename.indexOf("." + hotElementname + conFileNameExtension4JSObject));

                    if (changes.containsKey(hotElementname + "_" + attrname) && changes.get(hotElementname + "_" + attrname).equals("delete")) {
                        if (listOfExistFiles.contains(filename)) {
                            removeFile(filename);
                            listOfRemovedFiles.add(filename);
                        } else if (listOfExistFiles.contains(target + filename)) {
                            removeFile(target + filename);
                            listOfRemovedFiles.add(target + filename);
                        }

                    }
                }
            }

            if (changes != null && changes.keySet() != null) {
                java.util.Iterator c = changes.keySet().iterator();
                while (c.hasNext()) {
                    String remFile = c.next().toString();
                    if (changes.get(remFile) != null && changes.get(remFile).equals("delete")) {
                        String prefix = "";
                        if (remFile.startsWith("job_chain"))
                            prefix = "job_chain";
                        else if (remFile.startsWith("job"))
                            prefix = "job";
                        else if (remFile.startsWith("order"))
                            prefix = "order";
                        else if (remFile.startsWith("add_order"))
                            prefix = "add_order";
                        else if (remFile.startsWith("process_class"))
                            prefix = "process_class";
                        else if (remFile.startsWith("lock"))
                            prefix = "lock";
                        else if (remFile.startsWith("schedule"))
                            prefix = "schedule";

                        remFile = remFile.substring(prefix.length() + 1) + "." + remFile.substring(0, prefix.length()) + conFileNameExtension4JSObject;
                        if (!listOfRemovedFiles.contains(target + remFile) && !listOfRemovedFiles.contains(remFile)) {

                            if (listOfExistFiles.contains(remFile)) {
                                removeFile(remFile);
                            } else if (listOfExistFiles.contains(target + remFile)) {
                                removeFile(target + remFile);
                            }
                        }
                    }
                }
            }
            
            File fSource = new File(source);
            if (!fSource.exists())
                throw new Exception(source + " not exist.");

            if (!fSource.isDirectory())
                throw new Exception(source + " is not a directory.");

            String[] files = fSource.list();
            for (int i = 0; i < files.length; i++) {
                String sourcefile = source + files[i];
                String targetFile = target + new File(sourcefile).getName();

                String hotElementname = "";
                String attrname = "";
                hotElementname = new File(sourcefile).getName().substring(0, new File(sourcefile).getName().lastIndexOf(conFileNameExtension4JSObject));
                hotElementname = hotElementname.substring(hotElementname.lastIndexOf(".") + 1);
                attrname = new File(sourcefile).getName().substring(0, new File(sourcefile).getName().indexOf("." + hotElementname + conFileNameExtension4JSObject));

                if (changes.containsKey(hotElementname + "_" + attrname) && (changes.get(hotElementname + "_" + attrname).equals("modify") || changes.get(hotElementname + "_" + attrname).equals("new")))
                    saveAs(sourcefile, targetFile);
            }

        } catch (Exception e) {
            log("..error in FTPDialogListener.saveHotFolderAs() 2, cause: " + FTPDialogListener.getErrorMessage(e), SOSLogger.WARN);
            hasError = true;
            if (logtext != null)
                logtext.append("..ftp server reply: " + ftpClient.getReplyString());
        }
    }

    /**
     * Speichert die lokale Datei source auf der FTP Server.
     * 
     * @param source
     * @param target
     * @return
     */
    public long saveAs(String source, String target) {
        long bytesSend = 0;
        try {

        	 if (ftpClient instanceof SOSFTP)// change to Parent directory
                 changeDirectory("./");
        	 
            target = target.replaceAll("\\\\", "/");

            bytesSend = ftpClient.putFile(source, target);

            if (logtext != null)
                logtext.append(source + " transfer to " + target + "[bytes=" + bytesSend + "]");

        } catch (Exception e) {
            log("..error in FTPDialogListener.saveAs(): could not save " + source + " as " + target + " cause:" + e.toString(), SOSLogger.WARN);
            if (logtext != null)
                logtext.append("..ftp server reply: " + ftpClient.getReplyString());
            hasError = true;
        }
        return bytesSend;
    }

    /**
     * alles speichern
     * 
     * @param source
     * @param target
     * @param listOfHotFolderElements
     */

    public ArrayList saveHotFolderAs(String source, String target) {
        ArrayList list = new ArrayList();
        try {

            source = source.endsWith("/") ? source : source + "/";
            target = target.endsWith("/") ? target : target + "/";

            File fSource = new File(source);
            if (!fSource.exists())
                throw new Exception(source + " not exist.");

            if (!fSource.isDirectory())
                throw new Exception(source + " is not a directory.");

            if (ftpClient instanceof SOSFTP)
                changeDirectory("./");// change to Parent Directory

            String[] files = fSource.list();
            for (int i = 0; i < files.length; i++) {
                String sourcefile = source + files[i];
                if (files[i].endsWith(conFileNameExtension4JSObject)) {
                    // String targetFile = new File(sourcefile).getName();
                    String targetFile = target + new File(sourcefile).getName();
                    // System.out.println("source: " + sourcefile + ", target: "
                    // + targetFile);
                    saveAs(sourcefile, targetFile);
                    // list.add(target +targetFile);
                    list.add(targetFile);
                }
            }

        } catch (Exception e) {
            log("..error in FTPDialogListener.saveHotFolderAs(), cause: " + FTPDialogListener.getErrorMessage(e), SOSLogger.WARN);
            hasError = true;
            if (logtext != null)
                logtext.append("..ftp server reply: " + ftpClient.getReplyString());
        }
        return list;
    }

    /**
     * Besteht bereits eine FTP verbindung
     * 
     * @return
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Erzeugt ein Verzeichnis auf der FTP Server.
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public boolean mkDirs(String path) throws Exception {
        try {

            if (ftpClient.mkdir(path)) {
                if (logtext != null)
                    logtext.append("..ftp server reply [mkdir] [path=" + path + "]: " + ftpClient.getReplyString());
            } else {
                throw new Exception("..ftp server reply [mkdir failed] [path=" + path + "]: " + ftpClient.getReplyString());
            }

        } catch (Exception e) {
            hasError = true;
            log("error in " + sos.util.SOSClassUtil.getMethodName() + ", cause: " + e.toString(), SOSLogger.WARN);
            if (logtext != null)
                logtext.append("..could not create Directory [" + path + "] cause:" + e.getMessage());
        }
        return true;
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

    /**
     * @return the ftpClient
     */
    public SOSFileTransfer getFtpClient() {
        return ftpClient;
    }
}
