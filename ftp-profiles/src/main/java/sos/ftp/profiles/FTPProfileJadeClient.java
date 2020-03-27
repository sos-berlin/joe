package sos.ftp.profiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.widgets.Shell;

import com.sos.DataExchange.JadeEngine;
import com.sos.DataExchange.Options.JADEOptions;
import com.sos.JSHelper.Options.SOSOptionJadeOperation.enuJadeOperations;
import com.sos.JSHelper.Options.SOSOptionTransferType.TransferTypes;
import com.sos.vfs.common.SOSFileList;
import com.sos.vfs.common.SOSFileListEntry;
import com.sos.vfs.common.SOSVFSFactory;
import com.sos.vfs.common.interfaces.ISOSTransferHandler;
import com.sos.vfs.common.options.SOSDestinationOptions;
import com.sos.vfs.common.SOSFileEntry;
import com.sos.vfs.common.SOSFileEntry.EntryType;

public class FTPProfileJadeClient {

    protected ISOSTransferHandler ftpClient = null;
    protected TransferTypes enuSourceTransferType = TransferTypes.local;
    protected TransferTypes enuTargetTransferType = TransferTypes.local;
    private static final String REGEX_FOR_JOBSCHEDULER_OBJECTS =
            "^.*\\.(monitor|job|job_chain|order|process_class|schedule|lock|config)\\.(xml|png|dot)$";
    private JADEOptions jadeOptions;
    private FTPProfile ftpProfile = null;
    SOSDestinationOptions virtuelFileSystemOptions;
    JOEUserInfo joeUserInfo;

    public ISOSTransferHandler getFtpClient() {
        return ftpClient;
    }

    public FTPProfileJadeClient(FTPProfile ftpProfile_, JOEUserInfo joeUserInfo) {
        super();

        this.joeUserInfo = joeUserInfo;
        this.ftpProfile = ftpProfile_;
    }

    public void disconnect() {
        try {
            if (ftpClient != null) {
                ftpClient.disconnect();
                ftpClient = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getPasswordFromDialog() {
        final Shell shell = new Shell();
        shell.pack();
        FTPPopUpDialog fTPPopUpDialog = new FTPPopUpDialog(shell);
        fTPPopUpDialog.open(ftpProfile);
    }

    private String getPassword() throws Exception {
        if (ftpProfile.isPromptForPassword()) {
            getPasswordFromDialog();
        }
        return ftpProfile.getDecryptetPassword();
    }

    private String getSftpPassPhrase() throws Exception {
        if (ftpProfile.isPromptForPassphrase()) {
            getPasswordFromDialog();
        }
        return ftpProfile.getDecryptetPassword();
    }

    private JOEUserInfo getUserInfo() {
        if (joeUserInfo == null) {
            joeUserInfo = new JOEUserInfo();
        }
        return joeUserInfo;
    }

    private void connect() throws RuntimeException, Exception {
        if (ftpClient == null) {
            jadeOptions = new JADEOptions();
            enuSourceTransferType = TransferTypes.valueOf(ftpProfile.getProtocol().toLowerCase());
            virtuelFileSystemOptions = jadeOptions.getTransferOptions().getSource();
            virtuelFileSystemOptions.host.setValue(ftpProfile.getHost());
            virtuelFileSystemOptions.port.setValue(ftpProfile.getPort());
            virtuelFileSystemOptions.user.setValue(ftpProfile.getUser());
            virtuelFileSystemOptions.password.setValue(getPassword());

            virtuelFileSystemOptions.protocol.setValue(enuSourceTransferType);
            virtuelFileSystemOptions.passiveMode.value(ftpProfile.isPassiveMode());
            virtuelFileSystemOptions.user_info.value(getUserInfo());

            if (ftpProfile.isPublicKeyAuthentication()) {
                virtuelFileSystemOptions.authMethod.setValue("publickey");
                virtuelFileSystemOptions.authFile.setValue(ftpProfile.getAuthFile());
                virtuelFileSystemOptions.passphrase.setValue(getSftpPassPhrase());
                virtuelFileSystemOptions.useKeyAgent.value(ftpProfile.isUseKeyAgent());
            }

            if (ftpProfile.isPasswordAuthentication()) {
                if (ftpProfile.isKeyboardInteractive()) {
                    virtuelFileSystemOptions.authMethod.setValue("keyboard-interactive");
                } else {
                    virtuelFileSystemOptions.authMethod.setValue("password");
                }
            }

            if (ftpProfile.isTwoFactorAuthentication() && ftpProfile.isPasswordAuthentication() && ftpProfile.isPublicKeyAuthentication()) {
                if (ftpProfile.isKeyboardInteractive()) {
                    virtuelFileSystemOptions.required_authentications.setValue("publickey,keyboard-interactive");
                } else {
                    virtuelFileSystemOptions.required_authentications.setValue("publickey,password");
                }
            } else {

                if (ftpProfile.isPasswordAuthentication() && ftpProfile.isPublicKeyAuthentication()) {
                    if (ftpProfile.isKeyboardInteractive()) {
                        virtuelFileSystemOptions.preferred_authentications.setValue("publickey,keyboard-interactive");
                    } else {
                        virtuelFileSystemOptions.preferred_authentications.setValue("publickey,password");
                    }
                }
            }

            if (ftpProfile.getUseProxy()) {
                virtuelFileSystemOptions.proxyHost.setValue(ftpProfile.getProxyServer());
                virtuelFileSystemOptions.proxyPassword.setValue(ftpProfile.getProxyPassword());
                virtuelFileSystemOptions.proxyUser.setValue(ftpProfile.getProxyUser());
                virtuelFileSystemOptions.proxyProtocol.setValue(ftpProfile.getProxyProtocol());
                virtuelFileSystemOptions.proxyPort.setValue(ftpProfile.getProxyPort());
            }
            ftpClient = SOSVFSFactory.getHandler(enuSourceTransferType);
            ftpClient.connect(virtuelFileSystemOptions);
     
        }
    }

    private String getJobChainNodeParameterFilename(String filename) {
        String result = "";
        if (filename.matches("^.*\\.job_chain\\.xml$")) {
            result = filename.replaceAll("\\.job_chain\\.", ".config.");
        }
        return result;
    }

    public void mkdir(String directory, String newFolder) throws RuntimeException, Exception {
        connect();
        ftpClient.mkdir(directory + "/" + newFolder);
    }

    public Vector<String> getList(String remoteDir) throws RuntimeException, Exception {
        connect();
        Vector<String> result = new Vector<String>();
        List<SOSFileEntry> entries = ftpClient.nList(remoteDir, false, true);
        for (SOSFileEntry entry : entries) {
            result.add(entry.getFullPath());
        }
        return result;
    }

    public HashMap<String, SOSFileEntry> getDirectoryContent(String remoteDir) throws Exception {
        connect();
        if ("".equals(remoteDir)) {
            remoteDir = "/";
        }

        if (!ftpClient.isDirectory(remoteDir)) {
            return null;
        }
        List<SOSFileEntry> entries = ftpClient.nList(remoteDir, false, true);
        HashMap<String, SOSFileEntry> h = new HashMap<String, SOSFileEntry>();
        for (SOSFileEntry sosFileListEntry : entries) {
            String filename = sosFileListEntry.getFilename();
            if (!".".equals(filename)) {
                h.put(filename, sosFileListEntry);
            }
        }
        return h;
    }

    public void removeFile(SOSFileEntry sosFileEntry) throws Exception {
        connect();
        if (sosFileEntry.isDirectory()) {
            List<SOSFileEntry> entries = ftpClient.nList(sosFileEntry.getFilename(), false, true);
            if (entries.size() == 0) {
                ftpClient.rmdir(sosFileEntry.getFilename());
            }
        } else {
            ftpClient.delete(sosFileEntry.getFullPath(), true);
        }
    }

    public void renameFile(String remoteDir, String oldFilename, String newFilename) throws Exception {
        connect();
        ftpClient.rename(oldFilename, newFilename);
        // if this a job chain configuration file, also copy the configuration
        // parameters file
        String jobChainNodeParameterOldFilename = getJobChainNodeParameterFilename(oldFilename);
        if (!jobChainNodeParameterOldFilename.isEmpty()) {
            String jobChainNodeParameterNewFilename = getJobChainNodeParameterFilename(newFilename);
            try {
                ftpClient.rename(jobChainNodeParameterOldFilename, jobChainNodeParameterNewFilename);
            } catch (Exception e) {
            }
        }
    }

    public void removeFile(String dir, String filename) throws Exception {
        SOSFileEntry sosFileEntry = new SOSFileEntry(EntryType.FILESYSTEM);
        sosFileEntry.setFilename(filename);
        sosFileEntry.setParentPath(dir);
        removeFile(sosFileEntry);
    }

    public void removeDir(String dir) throws Exception {
        connect();
        String folder = new File(dir).getName();
        ftpClient.rmdir(folder);
    }

    public void copyLocalFileToRemote(String localDir, String targetDir, String filename) throws Exception {
        jadeOptions = new JADEOptions();
        enuSourceTransferType = TransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.getTarget().directory.setValue(targetDir);
        jadeOptions.getTarget().host.setValue(ftpProfile.getHost());
        jadeOptions.filePath.setValue(filename);
        jadeOptions.getTarget().port.setValue(ftpProfile.getPort());
        jadeOptions.getTarget().protocol.setValue(enuSourceTransferType);
        jadeOptions.getTarget().user.setValue(ftpProfile.getUser());
        jadeOptions.getTarget().password.setValue(getPassword());
        if (ftpProfile.getUseProxy()) {
            jadeOptions.getTarget().proxyHost.setValue(ftpProfile.getProxyServer());
            jadeOptions.getTarget().proxyUser.setValue(ftpProfile.getProxyUser());
            jadeOptions.getTarget().proxyPassword.setValue(ftpProfile.getProxyPassword());
            jadeOptions.getTarget().proxyProtocol.setValue(ftpProfile.getProxyProtocol());
            jadeOptions.getTarget().proxyPort.setValue(ftpProfile.getProxyPort());
        }

        if (ftpProfile.isPasswordAuthentication()) {
            jadeOptions.getTarget().authMethod.setValue("password");
        }

        if (ftpProfile.isPublicKeyAuthentication()) {
            jadeOptions.getTarget().authMethod.setValue("publickey");
            jadeOptions.getTarget().authFile.setValue(ftpProfile.getAuthFile());
            jadeOptions.getTarget().useKeyAgent.value(ftpProfile.isUseKeyAgent());
        }

        jadeOptions.getTarget().user_info.value(getUserInfo());
        jadeOptions.getSource().directory.setValue(localDir);
        jadeOptions.getSource().protocol.setValue("local");
        jadeOptions.operation.setValue(enuJadeOperations.copy);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.execute();
        // if this a job chain configuration file, also copy the configuration
        // parameters file
        String jobChainNodeParameterFilename = getJobChainNodeParameterFilename(filename);
        if (!jobChainNodeParameterFilename.isEmpty()) {
            jadeOptions.forceFiles.value(false);
            jadeOptions.filePath.setValue(jobChainNodeParameterFilename);
            try {
                jadeEngine.execute();
            } catch (Exception e) {
            }
        }
        jadeEngine.logout();
    }

    public void copyLocalFilesToRemote(String sourceDir, String targetDir, String sourceHotFolder) throws Exception {
        jadeOptions = new JADEOptions();
        String remoteDir = targetDir + "/" + sourceHotFolder;
        enuSourceTransferType = TransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.getSource().directory.setValue(sourceDir);
        jadeOptions.getSource().protocol.setValue("local");
        jadeOptions.getTarget().directory.setValue(remoteDir);
        jadeOptions.createFoldersOnTarget.value(true);
        jadeOptions.getTarget().host.setValue(ftpProfile.getHost());
        jadeOptions.fileSpec.setValue(REGEX_FOR_JOBSCHEDULER_OBJECTS);
        jadeOptions.getTarget().port.setValue(ftpProfile.getPort());
        jadeOptions.getTarget().protocol.setValue(enuSourceTransferType);
        jadeOptions.getTarget().user.setValue(ftpProfile.getUser());
        jadeOptions.getTarget().password.setValue(getPassword());
        if (ftpProfile.getUseProxy()) {
            jadeOptions.getTarget().proxyHost.setValue(ftpProfile.getProxyServer());
            jadeOptions.getTarget().proxyUser.setValue(ftpProfile.getProxyUser());
            jadeOptions.getTarget().proxyPassword.setValue(ftpProfile.getProxyPassword());
            jadeOptions.getTarget().proxyProtocol.setValue(ftpProfile.getProxyProtocol());
            jadeOptions.getTarget().proxyPort.setValue(ftpProfile.getProxyPort());
        }

        if (ftpProfile.isPasswordAuthentication()) {
            jadeOptions.getTarget().authMethod.setValue("password");
        }

        if (ftpProfile.isPublicKeyAuthentication()) {
            jadeOptions.getTarget().authMethod.setValue("publickey");
            jadeOptions.getTarget().authFile.setValue(ftpProfile.getAuthFile());
            jadeOptions.getTarget().useKeyAgent.value(ftpProfile.isUseKeyAgent());
        }

        jadeOptions.getTarget().user_info.value(getUserInfo());

        jadeOptions.operation.setValue(enuJadeOperations.copy);
        jadeOptions.errorOnNoDataFound.value(false);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.execute();
        jadeEngine.logout();
    }

    public File copyRemoteFileToLocal(SOSFileEntry sosFileEntry) throws Exception {
        jadeOptions = new JADEOptions();
        enuSourceTransferType = TransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.getSource().directory.setValue(sosFileEntry.getParentPath());
        jadeOptions.getSource().host.setValue(ftpProfile.getHost());
        jadeOptions.filePath.setValue(sosFileEntry.getFilename());
        jadeOptions.getSource().port.setValue(ftpProfile.getPort());
        jadeOptions.getSource().protocol.setValue(enuSourceTransferType);
        jadeOptions.getSource().user.setValue(ftpProfile.getUser());
        jadeOptions.getSource().password.setValue(getPassword());
        if (ftpProfile.getUseProxy()) {
            jadeOptions.getSource().proxyHost.setValue(ftpProfile.getProxyServer());
            jadeOptions.getSource().proxyUser.setValue(ftpProfile.getProxyUser());
            jadeOptions.getSource().proxyPassword.setValue(ftpProfile.getProxyPassword());
            jadeOptions.getSource().proxyProtocol.setValue(ftpProfile.getProxyProtocol());
            jadeOptions.getSource().proxyPort.setValue(ftpProfile.getProxyPort());
        }

        if (ftpProfile.isPasswordAuthentication()) {
            jadeOptions.getSource().authMethod.setValue("password");
        }

        if (ftpProfile.isPublicKeyAuthentication()) {
            jadeOptions.getSource().authMethod.setValue("publickey");
            jadeOptions.getSource().authFile.setValue(ftpProfile.getAuthFile());
            jadeOptions.getSource().useKeyAgent.value(ftpProfile.isUseKeyAgent());
        }

        jadeOptions.getSource().user_info.value(getUserInfo());
        jadeOptions.getTarget().directory.setValue(ftpProfile.getLocaldirectory());
        jadeOptions.getTarget().protocol.setValue("local");
        jadeOptions.operation.setValue(enuJadeOperations.copy);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.execute();
        // if this a job chain configuration file, also copy the configuration
        // parameters file
        String jobChainNodeParameterFilename = getJobChainNodeParameterFilename(sosFileEntry.getFilename());
        if (!jobChainNodeParameterFilename.isEmpty()) {
            jadeOptions.forceFiles.value(false);
            jadeOptions.errorOnNoDataFound.value(false);
            jadeOptions.filePath.setValue(jobChainNodeParameterFilename);
            try {
                jadeEngine.execute();
            } catch (Exception e) {
            }
        }
        jadeEngine.logout();
        return new File(ftpProfile.getLocaldirectory(), sosFileEntry.getFilename());
    }

    private void removeLocalHotFolderFiles(String sourceDir) throws Exception {
        jadeOptions = new JADEOptions();
        enuSourceTransferType = TransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.protocol.setValue("local");
        jadeOptions.getTransferOptions().getSource().protocol.setValue("local");
        jadeOptions.filePath.setValue("");
        jadeOptions.fileSpec.setValue(REGEX_FOR_JOBSCHEDULER_OBJECTS);
        jadeOptions.getSource().directory.setValue(sourceDir);
        jadeOptions.operation.setValue("delete");
        jadeOptions.errorOnNoDataFound.value(false);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.execute();
        jadeEngine.logout();
    }

    public ArrayList<String> copyRemoteFilesToLocal(String sourceDir, String soureHotFolder) throws Exception {
        jadeOptions = new JADEOptions();
        String remoteDir = "";
        remoteDir = sourceDir + "/" + soureHotFolder;
        String localDir = ftpProfile.getLocaldirectory() + "/" + soureHotFolder;
        try {
            removeLocalHotFolderFiles(localDir);
        } catch (Exception e) {
        }
        enuSourceTransferType = TransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.getSource().directory.setValue(remoteDir);
        jadeOptions.getSource().host.setValue(ftpProfile.getHost());
        jadeOptions.fileSpec.setValue(REGEX_FOR_JOBSCHEDULER_OBJECTS);
        jadeOptions.getSource().port.setValue(ftpProfile.getPort());
        jadeOptions.getSource().protocol.setValue(enuSourceTransferType);
        jadeOptions.getSource().user.setValue(ftpProfile.getUser());
        jadeOptions.getSource().password.setValue(getPassword());
        if (ftpProfile.getUseProxy()) {
            jadeOptions.getSource().proxyHost.setValue(ftpProfile.getProxyServer());
            jadeOptions.getSource().proxyUser.setValue(ftpProfile.getProxyUser());
            jadeOptions.getSource().proxyPassword.setValue(ftpProfile.getProxyPassword());
            jadeOptions.getSource().proxyProtocol.setValue(ftpProfile.getProxyProtocol());
            jadeOptions.getSource().proxyPort.setValue(ftpProfile.getProxyPort());
        }

        if (ftpProfile.isPasswordAuthentication()) {
            jadeOptions.getSource().authMethod.setValue("password");
        }

        if (ftpProfile.isPublicKeyAuthentication()) {
            jadeOptions.getSource().authMethod.setValue("publickey");
            jadeOptions.getSource().authFile.setValue(ftpProfile.getAuthFile());
            jadeOptions.getSource().useKeyAgent.value(ftpProfile.isUseKeyAgent());
        }

        jadeOptions.getSource().user_info.value(getUserInfo());

        jadeOptions.getTarget().directory.setValue(localDir);
        jadeOptions.createFoldersOnTarget.value(true);
        jadeOptions.getTarget().protocol.setValue("local");
        jadeOptions.operation.setValue(enuJadeOperations.copy);
        jadeOptions.errorOnNoDataFound.value(false);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.execute();
        ArrayList<String> resultList = new ArrayList<String>();
        SOSFileList sosFileList = jadeEngine.getFileList();
        for (SOSFileListEntry sosFileListEntry : sosFileList.getList()) {
            resultList.add(sosFileListEntry.getTargetFileName());
        }
        jadeEngine.logout();
        return resultList;
    }

}