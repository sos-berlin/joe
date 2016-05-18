package sos.ftp.profiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.sos.DataExchange.JadeEngine;
import com.sos.DataExchange.Options.JADEOptions;
import com.sos.JSHelper.Options.SOSOptionJadeOperation.enuJadeOperations;
import com.sos.JSHelper.Options.SOSOptionTransferType.enuTransferTypes;
import com.sos.VirtualFileSystem.DataElements.SOSFileList;
import com.sos.VirtualFileSystem.DataElements.SOSFileListEntry;
import com.sos.VirtualFileSystem.Factory.VFSFactory;
import com.sos.VirtualFileSystem.Interfaces.ISOSVFSHandler;
import com.sos.VirtualFileSystem.Interfaces.ISOSVfsFileTransfer;
import com.sos.VirtualFileSystem.Options.SOSConnection2OptionsAlternate;
import com.sos.VirtualFileSystem.common.SOSFileEntries;
import com.sos.VirtualFileSystem.common.SOSFileEntry;

public class FTPProfileJadeClient {

    protected ISOSVFSHandler objVFS = null;
    protected ISOSVfsFileTransfer ftpClient = null;
    protected enuTransferTypes enuSourceTransferType = enuTransferTypes.local;
    protected enuTransferTypes enuTargetTransferType = enuTransferTypes.local;
    private static final String REGEX_FOR_JOBSCHEDULER_OBJECTS =
            "^.*\\.(monitor|job|job_chain|order|process_class|schedule|lock|config)\\.(xml|png|dot)$";
    private JADEOptions jadeOptions;
    private FTPProfile ftpProfile = null;
    SOSConnection2OptionsAlternate virtuelFileSystemOptions;

    public ISOSVfsFileTransfer getFtpClient() {
        return ftpClient;
    }

    public FTPProfileJadeClient(FTPProfile ftpProfile_) {
        super();
        this.ftpProfile = ftpProfile_;
    }

    public void disconnect() throws Exception {
        if (objVFS != null) {
            objVFS.CloseConnection();
            objVFS.CloseSession();
            objVFS = null;
        }
        if (ftpClient != null) {
            ftpClient.disconnect();
            ftpClient.close();
            ftpClient = null;
        }
    }

    private void connect() throws RuntimeException, Exception {
        if (objVFS == null) {
            jadeOptions = new JADEOptions();
            enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol().toLowerCase());
            virtuelFileSystemOptions = jadeOptions.getConnectionOptions().Source();
            virtuelFileSystemOptions.host.Value(ftpProfile.getHost());
            virtuelFileSystemOptions.port.Value(ftpProfile.getPort());
            virtuelFileSystemOptions.user.Value(ftpProfile.getUser());
            virtuelFileSystemOptions.password.Value(ftpProfile.getDecryptetPassword());
            virtuelFileSystemOptions.protocol.Value(enuSourceTransferType);
            virtuelFileSystemOptions.passiveMode.value(ftpProfile.isPassiveMode());
            if (!"".equals(ftpProfile.getAuthMethod())) {
                virtuelFileSystemOptions.authMethod.Value(ftpProfile.getAuthMethod());
                virtuelFileSystemOptions.authFile.Value(ftpProfile.getAuthFile());
            }
            if (ftpProfile.getUseProxy()) {
                virtuelFileSystemOptions.proxyHost.Value(ftpProfile.getProxyServer());
                virtuelFileSystemOptions.proxyPassword.Value(ftpProfile.getProxyPassword());
                virtuelFileSystemOptions.proxyUser.Value(ftpProfile.getProxyUser());
                virtuelFileSystemOptions.proxyProtocol.Value(ftpProfile.getProxyProtocol());
                virtuelFileSystemOptions.proxyPort.Value(ftpProfile.getProxyPort());
            }
            objVFS = VFSFactory.getHandler(enuSourceTransferType);
            ftpClient = (ISOSVfsFileTransfer) objVFS;
            objVFS.Connect(virtuelFileSystemOptions);
            objVFS.Authenticate(virtuelFileSystemOptions);
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
        return ftpClient.nList(remoteDir);
    }

    public HashMap<String, SOSFileEntry> getDirectoryContent(String remoteDir) throws Exception {
        connect();
        if ("".equals(remoteDir)) {
            remoteDir = "/";
        }
        ftpClient.nList(remoteDir);
        SOSFileEntries sosFileList = ftpClient.getSOSFileEntries();
        HashMap<String, SOSFileEntry> h = new HashMap<String, SOSFileEntry>();
        for (SOSFileEntry sosFileListEntry : sosFileList) {
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
            ftpClient.changeWorkingDirectory(sosFileEntry.getParentPath());
            ftpClient.nList(sosFileEntry.getFilename());
            SOSFileEntries sosFileList = ftpClient.getSOSFileEntries();
            if (sosFileList.size() == 0) {
                ftpClient.rmdir(sosFileEntry.getFilename());
            }
        } else {
            ftpClient.delete(sosFileEntry.getFullPath());
        }
    }

    public void renameFile(String remoteDir, String oldFilename, String newFilename) throws Exception {
        connect();
        ftpClient.changeWorkingDirectory(remoteDir);
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
        SOSFileEntry sosFileEntry = new SOSFileEntry();
        sosFileEntry.setFilename(filename);
        sosFileEntry.setParentPath(dir);
        removeFile(sosFileEntry);
    }

    public void removeDir(String dir) throws Exception {
        connect();
        String folder = new File(dir).getName();
        String path = new File(dir).getParent();
        ftpClient.changeWorkingDirectory(path);
        ftpClient.rmdir(folder);
    }

    public void copyLocalFileToRemote(String localDir, String targetDir, String filename) throws Exception {
        jadeOptions = new JADEOptions();
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.targetDir.Value(targetDir);
        jadeOptions.Target().host.Value(ftpProfile.getHost());
        jadeOptions.filePath.Value(filename);
        jadeOptions.Target().port.Value(ftpProfile.getPort());
        jadeOptions.Target().protocol.Value(enuSourceTransferType);
        jadeOptions.Target().user.Value(ftpProfile.getUser());
        jadeOptions.Target().password.Value(ftpProfile.getDecryptetPassword());
        if (ftpProfile.getUseProxy()) {
            jadeOptions.Target().proxyHost.Value(ftpProfile.getProxyServer());
            jadeOptions.Target().proxyUser.Value(ftpProfile.getProxyUser());
            jadeOptions.Target().proxyPassword.Value(ftpProfile.getProxyPassword());
            jadeOptions.Target().proxyProtocol.Value(ftpProfile.getProxyProtocol());
            jadeOptions.Target().proxyPort.Value(ftpProfile.getProxyPort());
        }
        if (!"".equals(ftpProfile.getAuthMethod())) {
            jadeOptions.Target().authMethod.Value(ftpProfile.getAuthMethod());
            jadeOptions.Target().authFile.Value(ftpProfile.getAuthFile());
        }
        jadeOptions.sourceDir.Value(localDir);
        jadeOptions.Source().protocol.Value("local");
        jadeOptions.operation.Value(enuJadeOperations.copy);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.Execute();
        // if this a job chain configuration file, also copy the configuration
        // parameters file
        String jobChainNodeParameterFilename = getJobChainNodeParameterFilename(filename);
        if (!jobChainNodeParameterFilename.isEmpty()) {
            jadeOptions.forceFiles.value(false);
            jadeOptions.filePath.Value(jobChainNodeParameterFilename);
            try {
                jadeEngine.Execute();
            } catch (Exception e) {
            }
        }
        jadeEngine.Logout();
    }

    private void removeRemoteHotFolderFiles(String remoteDir) throws Exception {
        JADEOptions jadeOptions = new JADEOptions();
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.protocol.Value(enuSourceTransferType);
        jadeOptions.getConnectionOptions().Source().protocol.Value(enuSourceTransferType);
        jadeOptions.getConnectionOptions().Source().host.Value(ftpProfile.getHost());
        jadeOptions.getConnectionOptions().Source().port.Value(ftpProfile.getPort());
        jadeOptions.getConnectionOptions().Source().user.Value(ftpProfile.getUser());
        jadeOptions.getConnectionOptions().Source().password.Value(ftpProfile.getDecryptetPassword());
        jadeOptions.getConnectionOptions().Source().directory.Value(remoteDir);
        if (!"".equals(ftpProfile.getAuthMethod())) {
            jadeOptions.getConnectionOptions().Source().authMethod.Value(ftpProfile.getAuthMethod());
            jadeOptions.getConnectionOptions().Source().authFile.Value(ftpProfile.getAuthFile());
        }
        jadeOptions.Source().protocol.Value(enuSourceTransferType);
        jadeOptions.Source().host.Value(ftpProfile.getHost());
        jadeOptions.Source().port.Value(ftpProfile.getPort());
        jadeOptions.Source().user.Value(ftpProfile.getUser());
        jadeOptions.Source().password.Value(ftpProfile.getDecryptetPassword());
        jadeOptions.Source().directory.Value(remoteDir);
        if (ftpProfile.getUseProxy()) {
            jadeOptions.Source().proxyHost.Value(ftpProfile.getProxyServer());
            jadeOptions.Source().proxyUser.Value(ftpProfile.getProxyUser());
            jadeOptions.Source().proxyPassword.Value(ftpProfile.getProxyPassword());
            jadeOptions.Source().proxyProtocol.Value(ftpProfile.getProxyProtocol());
            jadeOptions.Source().proxyPort.Value(ftpProfile.getProxyPort());
        }
        if (!"".equals(ftpProfile.getAuthMethod())) {
            jadeOptions.Source().authMethod.Value(ftpProfile.getAuthMethod());
            jadeOptions.Source().authFile.Value(ftpProfile.getAuthFile());

        }
        jadeOptions.filePath.Value("");
        jadeOptions.fileSpec.Value(REGEX_FOR_JOBSCHEDULER_OBJECTS);
        jadeOptions.sourceDir.Value(remoteDir);
        jadeOptions.operation.Value("delete");
        jadeOptions.errorOnNoDataFound.value(false);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.Execute();
        jadeEngine.Logout();
    }

    public void copyLocalFilesToRemote(String sourceDir, String targetDir, String sourceHotFolder) throws Exception {
        jadeOptions = new JADEOptions();
        String remoteDir = targetDir + "/" + sourceHotFolder;
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.sourceDir.Value(sourceDir);
        jadeOptions.Source().protocol.Value("local");
        jadeOptions.targetDir.Value(remoteDir);
        jadeOptions.createFoldersOnTarget.value(true);
        jadeOptions.Target().host.Value(ftpProfile.getHost());
        jadeOptions.fileSpec.Value(REGEX_FOR_JOBSCHEDULER_OBJECTS);
        jadeOptions.Target().port.Value(ftpProfile.getPort());
        jadeOptions.Target().protocol.Value(enuSourceTransferType);
        jadeOptions.Target().user.Value(ftpProfile.getUser());
        jadeOptions.Target().password.Value(ftpProfile.getDecryptetPassword());
        if (ftpProfile.getUseProxy()) {
            jadeOptions.Target().proxyHost.Value(ftpProfile.getProxyServer());
            jadeOptions.Target().proxyUser.Value(ftpProfile.getProxyUser());
            jadeOptions.Target().proxyPassword.Value(ftpProfile.getProxyPassword());
            jadeOptions.Target().proxyProtocol.Value(ftpProfile.getProxyProtocol());
            jadeOptions.Target().proxyPort.Value(ftpProfile.getProxyPort());
        }
        if (!"".equals(ftpProfile.getAuthMethod())) {
            jadeOptions.Target().authMethod.Value(ftpProfile.getAuthMethod());
            jadeOptions.Target().authFile.Value(ftpProfile.getAuthFile());
        }
        try {
            removeRemoteHotFolderFiles(remoteDir);
        } catch (Exception e) {
        }
        jadeOptions.operation.Value(enuJadeOperations.copy);
        jadeOptions.errorOnNoDataFound.value(false);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.Execute();
        jadeEngine.Logout();
    }

    public File copyRemoteFileToLocal(SOSFileEntry sosFileEntry) throws Exception {
        jadeOptions = new JADEOptions();
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.sourceDir.Value(sosFileEntry.getParentPath());
        jadeOptions.Source().host.Value(ftpProfile.getHost());
        jadeOptions.filePath.Value(sosFileEntry.getFilename());
        jadeOptions.Source().port.Value(ftpProfile.getPort());
        jadeOptions.Source().protocol.Value(enuSourceTransferType);
        jadeOptions.Source().user.Value(ftpProfile.getUser());
        jadeOptions.Source().password.Value(ftpProfile.getDecryptetPassword());
        if (ftpProfile.getUseProxy()) {
            jadeOptions.Source().proxyHost.Value(ftpProfile.getProxyServer());
            jadeOptions.Source().proxyUser.Value(ftpProfile.getProxyUser());
            jadeOptions.Source().proxyPassword.Value(ftpProfile.getProxyPassword());
            jadeOptions.Source().proxyProtocol.Value(ftpProfile.getProxyProtocol());
            jadeOptions.Source().proxyPort.Value(ftpProfile.getProxyPort());
        }
        if (!"".equals(ftpProfile.getAuthMethod())) {
            jadeOptions.Source().authMethod.Value(ftpProfile.getAuthMethod());
            jadeOptions.Source().authFile.Value(ftpProfile.getAuthFile());
        }
        jadeOptions.targetDir.Value(ftpProfile.getLocaldirectory());
        jadeOptions.Target().protocol.Value("local");
        jadeOptions.operation.Value(enuJadeOperations.copy);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.Execute();
        // if this a job chain configuration file, also copy the configuration
        // parameters file
        String jobChainNodeParameterFilename = getJobChainNodeParameterFilename(sosFileEntry.getFilename());
        if (!jobChainNodeParameterFilename.isEmpty()) {
            jadeOptions.forceFiles.value(false);
            jadeOptions.errorOnNoDataFound.value(false);
            jadeOptions.filePath.Value(jobChainNodeParameterFilename);
            try {
                jadeEngine.Execute();
            } catch (Exception e) {
            }
        }
        jadeEngine.Logout();
        return new File(ftpProfile.getLocaldirectory(), sosFileEntry.getFilename());
    }

    private void removeLocalHotFolderFiles(String sourceDir) throws Exception {
        jadeOptions = new JADEOptions();
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.protocol.Value("local");
        jadeOptions.getConnectionOptions().Source().protocol.Value("local");
        jadeOptions.filePath.Value("");
        jadeOptions.fileSpec.Value(REGEX_FOR_JOBSCHEDULER_OBJECTS);
        jadeOptions.sourceDir.Value(sourceDir);
        jadeOptions.operation.Value("delete");
        jadeOptions.errorOnNoDataFound.value(false);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.Execute();
        jadeEngine.Logout();
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
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.sourceDir.Value(remoteDir);
        jadeOptions.Source().host.Value(ftpProfile.getHost());
        jadeOptions.fileSpec.Value(REGEX_FOR_JOBSCHEDULER_OBJECTS);
        jadeOptions.Source().port.Value(ftpProfile.getPort());
        jadeOptions.Source().protocol.Value(enuSourceTransferType);
        jadeOptions.Source().user.Value(ftpProfile.getUser());
        jadeOptions.Source().password.Value(ftpProfile.getDecryptetPassword());
        if (ftpProfile.getUseProxy()) {
            jadeOptions.Source().proxyHost.Value(ftpProfile.getProxyServer());
            jadeOptions.Source().proxyUser.Value(ftpProfile.getProxyUser());
            jadeOptions.Source().proxyPassword.Value(ftpProfile.getProxyPassword());
            jadeOptions.Source().proxyProtocol.Value(ftpProfile.getProxyProtocol());
            jadeOptions.Source().proxyPort.Value(ftpProfile.getProxyPort());
        }
        if (!"".equals(ftpProfile.getAuthMethod())) {
            jadeOptions.Source().authMethod.Value(ftpProfile.getAuthMethod());
            jadeOptions.Source().authFile.Value(ftpProfile.getAuthFile());
        }
        jadeOptions.targetDir.Value(localDir);
        jadeOptions.createFoldersOnTarget.value(true);
        jadeOptions.Target().protocol.Value("local");
        jadeOptions.operation.Value(enuJadeOperations.copy);
        jadeOptions.errorOnNoDataFound.value(false);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.Execute();
        ArrayList<String> resultList = new ArrayList<String>();
        SOSFileList sosFileList = jadeEngine.getFileList();
        for (SOSFileListEntry sosFileListEntry : sosFileList.List()) {
            resultList.add(sosFileListEntry.TargetFileName());
        }
        jadeEngine.Logout();
        return resultList;
    }

}