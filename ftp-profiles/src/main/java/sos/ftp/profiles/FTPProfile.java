package sos.ftp.profiles;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.widgets.Text;

import com.sos.keepass.SOSKeePassDatabase;
import com.sos.keepass.SOSKeePassPath;
import com.sos.keepass.exceptions.SOSKeePassDatabaseException;

import sos.util.SOSString;

public class FTPProfile {

    protected static Text logtext = null;
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

    private String queryFile;
    private String queryKeyFile;
    private String queryPassword;

    private String auth_file = null;
    private SOSString sosString = new SOSString();
    private boolean hasError = false;
    public static final String conFileNameExtension4JSObject = ".xml";

    public FTPProfile(Properties prop) throws Exception {
        try {
            profilename = sosString.parseToString(prop, "profilename");

            password = sosString.parseToString(prop, "password");
            this.getDecryptetPassword();

            host = sosString.parseToString(prop, "host");
            port = sosString.parseToString(prop, "port");
            user = sosString.parseToString(prop, "user");
            savePassword = sosString.parseToBoolean(sosString.parseToString(prop, "save_password").length() == 0 ? "true" : sosString.parseToString(
                    prop, "save_password"));

            root = sosString.parseToString(prop, "root");
            localdirectory = getValueFromCS(sosString.parseToString(prop, "localdirectory"));
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

    public String getValueFromCS(String uri) {
        String localUri;
        localUri = uri;
        if (!SOSString.isEmpty(uri)) {
            if (localUri.startsWith("cs://")) {
                try {
                    String concatSign = "?";
                    if (queryPassword != null && !localUri.contains(SOSKeePassPath.QUERY_PARAMETER_PASSWORD + "=")) {
                        localUri = localUri + concatSign + SOSKeePassPath.QUERY_PARAMETER_PASSWORD + "=" + queryPassword;
                        concatSign = "&";
                    }
                    if (queryFile != null && !localUri.contains(SOSKeePassPath.QUERY_PARAMETER_FILE + "=")) {
                        localUri = localUri + concatSign + SOSKeePassPath.QUERY_PARAMETER_FILE + "=" + queryFile;
                        concatSign = "&";
                    }
                    if (queryKeyFile != null && !localUri.contains(SOSKeePassPath.QUERY_PARAMETER_KEY_FILE + "=")) {
                        localUri = localUri + concatSign + SOSKeePassPath.QUERY_PARAMETER_KEY_FILE + "=" + queryKeyFile;
                        concatSign = "&";
                    }
                    return SOSKeePassDatabase.getProperty(localUri);
                } catch (Exception e) {
                    return uri;
                }
            } else {
                return uri;
            }
        } else {
            return uri;
        }
    }

    public String getDecryptetPassword() throws Exception {
        String passwordOrUri = getPassword();
        if (passwordOrUri.isEmpty()) {
            return passwordOrUri;
        }

        if (passwordOrUri.endsWith("=")) {
            passwordOrUri = SOSProfileCrypt.decrypt(getProfilename(), passwordOrUri);
        } else if (passwordOrUri.endsWith("=enc")) {
            passwordOrUri = passwordOrUri.substring(0, passwordOrUri.length() - 4);
            passwordOrUri = SOSProfileCrypt.decryptBasic(getProfilename(), passwordOrUri);
        }

        String password;
        if (passwordOrUri.startsWith("cs://")) {
            SOSKeePassPath path = new SOSKeePassPath(passwordOrUri);
            if (!path.isValid()) {
                throw new SOSKeePassDatabaseException(String.format("[%s][not valid uri]%s", passwordOrUri, path.getError()));
            }

            queryFile = path.getQueryParameters().get(SOSKeePassPath.QUERY_PARAMETER_FILE);
            queryKeyFile = path.getQueryParameters().get(SOSKeePassPath.QUERY_PARAMETER_KEY_FILE);
            queryPassword = path.getQueryParameters().get(SOSKeePassPath.QUERY_PARAMETER_PASSWORD);
            password = SOSKeePassDatabase.getProperty(passwordOrUri);
        } else {
            password = passwordOrUri;
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

    public String getCsPort() {
        return this.getValueFromCS(this.getPort());
    }

    public String getCsHost() {
        return this.getValueFromCS(this.getHost());
    }

    public String getCsUser() {
        return this.getValueFromCS(this.getUser());
    }

    public String getCsAuthFile() {
        return this.getValueFromCS(this.getAuthFile());
    }

    public String getCsProxyServer() {
        return this.getValueFromCS(this.getProxyServer());
    }

    public String getCsProxyPassword() {
        return this.getValueFromCS(this.getProxyPassword());
    }

    public String getCsProxyUser() {
        return this.getValueFromCS(this.getProxyUser());
    }

    public String getCsProxyPort() {
        return this.getValueFromCS(this.getProxyPort());
    }

    public String getCsProxyProtocol() {
        return this.getValueFromCS(this.getProxyProtocol());
    }

}
