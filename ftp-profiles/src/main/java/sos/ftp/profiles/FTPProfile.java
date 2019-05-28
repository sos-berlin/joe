package sos.ftp.profiles;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.widgets.Text;

import sos.util.SOSLogger;
import sos.util.SOSString;

public class FTPProfile {

    protected static Text logtext = null;
    protected static SOSLogger logger = null;
    private String profilename = null;
    private String host = null;
    private String port = null;
    private String user = null;
    private boolean savePassword = false;
    private boolean useKeyAgent = false;
    private String password = null;
    private String root = null;
    private String localdirectory = null;
    private String transfermode = null;
    private String protocol = null;
    private boolean useProxy = false;
    private boolean passiveMode = true;
    private String proxyServer = null;
    private String proxyPort = null;
    private String proxyUser = null;
    private String proxyPassword = null;
    private String proxyProtocol = null;
    private boolean publicKeyAuthentication;
    private boolean passwordAuthentication;
    private boolean keyboardInteractive;
    private boolean twoFactorAuthentication;
    private boolean promptForPassphrase;
    private boolean promptForPassword;
    private String sftpPassphrase = null;

    private String auth_file = null;
    private SOSString sosString = new SOSString();
    private boolean hasError = false;
    public static final String conFileNameExtension4JSObject = ".xml";

    public FTPProfile(Properties prop) throws Exception {
        try {
            profilename = sosString.parseToString(prop, "profilename");
            host = sosString.parseToString(prop, "host");
            port = sosString.parseToString(prop, "port");
            user = sosString.parseToString(prop, "user");
            savePassword = sosString.parseToBoolean(sosString.parseToString(prop, "save_password").length() == 0 ? "true" : sosString.parseToString(
                    prop, "save_password"));
            password = sosString.parseToString(prop, "password");
            root = sosString.parseToString(prop, "root");
            localdirectory = sosString.parseToString(prop, "localdirectory");
            transfermode = sosString.parseToString(prop, "transfermode");
            protocol = sosString.parseToString(prop, "protocol");
            useProxy = sosString.parseToBoolean(sosString.parseToString(prop, "use_proxy"));
            useKeyAgent = sosString.parseToBoolean(sosString.parseToString(prop, "use_key_agent"));

            passiveMode = sosString.parseToBoolean(sosString.parseToString(prop, "passivemode"));
            proxyServer = sosString.parseToString(prop, "proxy_server");
            proxyPort = sosString.parseToString(prop, "proxy_port");
            proxyUser = sosString.parseToString(prop, "proxy_user");
            proxyPassword = sosString.parseToString(prop, "proxy_password");
            proxyProtocol = sosString.parseToString(prop, "proxy_protocol");

            publicKeyAuthentication = sosString.parseToBoolean(sosString.parseToString(prop, "publickey_authentication"));
            passwordAuthentication = sosString.parseToBoolean(sosString.parseToString(prop, "password_authentication"));
            keyboardInteractive = sosString.parseToBoolean(sosString.parseToString(prop, "keyboard_interactive"));
            twoFactorAuthentication = sosString.parseToBoolean(sosString.parseToString(prop, "twofactor_authentication"));
            promptForPassphrase = sosString.parseToBoolean(sosString.parseToString(prop, "_for_passphrase"));
            promptForPassword = sosString.parseToBoolean(sosString.parseToString(prop, "prompt_for_password"));
            sftpPassphrase = sosString.parseToString(sosString.parseToString(prop, "sftp_passphrase"));

            auth_file = sosString.parseToString(prop, "auth_file");
        } catch (Exception e) {
            throw new Exception("error in FTPProfile.init(), cause: " + e.toString(), e);
        }
    }

    public String getProfilename() {
        return profilename;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getProxyProtocol() {
        return proxyProtocol;
    }

    public boolean isSavePassword() {
        return savePassword;
    }

    public String getPassword() {
        return password;
    }

    public String getDecryptetPassword() throws Exception {
        String password = getPassword();
        if (!password.isEmpty() && password.endsWith("=")) {
            password = SOSProfileCrypt.decrypt(getProfilename(), password);
        }
        return password;
    }

    public String getRoot() {
        return root;
    }

    public String getLocaldirectory() throws IOException {
        return new File(localdirectory).getCanonicalPath();
    }

    public String getLocaldirectory(String defaultFileName) throws IOException {
        if (localdirectory.isEmpty()) {
            return new File(defaultFileName).getCanonicalPath();
        } else {
            return new File(localdirectory).getCanonicalPath();
        }
    }

    public String getTransfermode() {
        return transfermode;
    }

    public String getPassiveMode() {
        if (isPassiveMode()) {
            return "true";
        } else {
            return "false";
        }
    }

    public String getProtocol() {
        return protocol.toLowerCase();
    }

    public boolean getUseProxy() {
        return useProxy;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public String getUseKeyAgent() {
        if (isUseKeyAgent()) {
            return "true";
        } else {
            return "false";
        }
    }

    public String getProxyServer() {
        return proxyServer;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public String getAuthFile() {
        return auth_file;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogText(org.eclipse.swt.widgets.Text text) {
        logtext = text;
    }

    public boolean hasError() {
        return hasError;
    }

    public static void log(String txt, int level) {
        if (logger == null) {
            if (level > -1 || level < 10) {
                System.out.println(txt);
            } else {
                System.err.println(txt);
            }
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

    public void setLogger(SOSLogger logger) {
        FTPProfile.logger = logger;
    }

    public boolean isUseKeyAgent() {
        return useKeyAgent;
    }

    public void setUseKeyAgent(boolean useKeyAgent) {
        this.useKeyAgent = useKeyAgent;
    }

    public boolean isPublicKeyAuthentication() {
        return publicKeyAuthentication;
    }

    public boolean isPasswordAuthentication() {
        return passwordAuthentication;
    }

    public boolean isKeyboardInteractive() {
        return keyboardInteractive;
    }

    public boolean isTwoFactorAuthentication() {
        return twoFactorAuthentication;
    }

    public boolean isPromptForPassphrase() {
        return promptForPassphrase;
    }

    public boolean isPromptForPassword() {
        return promptForPassword;
    }

    public String getSftpPassphrase() {
        return sftpPassphrase;
    }

    public String getAuth_file() {
        return auth_file;
    }

    public void setSftpPassphrase(String sftpPassphrase) {
        this.sftpPassphrase = sftpPassphrase;
    }

}
