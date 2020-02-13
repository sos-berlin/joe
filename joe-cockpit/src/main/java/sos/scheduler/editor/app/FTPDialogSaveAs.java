package sos.scheduler.editor.app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.sos.VirtualFileSystem.common.SOSFileEntry;
import com.sos.VirtualFileSystem.common.SOSFileEntry.EntryType;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.xml.DomParser;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

import sos.ftp.profiles.FTPProfileJadeClient;

public class FTPDialogSaveAs extends FTPDialog {

    private static final String FILENAME = "Filename";
    private static final String SAVE_AS = "Save as";
    private boolean isSingleFile;
    private SOSFileEntry sosRemoteFileEntry;

    public FTPDialogSaveAs() {
        super();
        isSingleFile = isSingleFile();
    }

    @Override
    String getTitle() {
        return SAVE_AS;
    }

    @Override
    protected void setTxtFilenameText(Text txtFilename, TableItem tableItem) {
        txtFilename.setText("");
    }

    @Override
    protected String getFilenameLabel() {
        return FILENAME;
    }

    @Override
    protected void fillTable(Table directoryTable, HashMap<String, SOSFileEntry> h) {
        try {
            Iterator<String> it = h.keySet().iterator();
            directoryTable.removeAll();
            while (it.hasNext()) {
                String key = it.next();
                SOSFileEntry sosFileEntry = h.get(key);
                TableItem item = new TableItem(directoryTable, SWT.NONE);
                item.setText(0, key);
                item.setText(1, sosFileEntry.getFilesizeAsString());
                item.setText(2, sosFileEntry.getCategory());
                item.setData(sosFileEntry);
                if (sosFileEntry.isDirectory()) {
                    item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory.gif"));
                } else {
                    item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_file.gif"));
                }
            }
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    @Override
    protected void setTooltip() {
        butExecute.setToolTipText(Messages.getTooltip("ftpdialog.btn_save_as"));
        txtFilename.setToolTipText(Messages.getTooltip("ftpdialog.txt_save_as"));
    }

    private boolean isSingleFile() {
        DomParser currdom = MainWindow.getSpecifiedDom();
        return currdom != null && currdom instanceof SchedulerDom && ((SchedulerDom) currdom).isLifeElement();
    }

    private void setFtpProperties() {
        MainWindow.getContainer().getCurrentTab().setData("ftp_profile_name", listener.getCurrProfileName());
        MainWindow.getContainer().getCurrentTab().setData("ftp_profile", listener.getCurrProfile());
        MainWindow.getContainer().getCurrentTab().setData("ftp_title", "[FTP::" + listener.getCurrProfileName() + "]");
    }

    private void copySingleFile2Remote() throws Exception {
        String remoteDir = txtDir.getText() + "";
        String localFullPath = new File(MainWindow.getContainer().getCurrentEditor().getFilename()).getCanonicalPath();
        String localParentPath = new File(localFullPath).getParent();
        String localFilename = new File(MainWindow.getContainer().getCurrentEditor().getFilename()).getName();
        String remoteFilename = getNewFileName(localFilename, txtFilename.getText());
        sosRemoteFileEntry = new SOSFileEntry(EntryType.FILESYSTEM);
        sosRemoteFileEntry.setDirectory(false);
        sosRemoteFileEntry.setFilename(remoteFilename);
        sosRemoteFileEntry.setParentPath(remoteDir);
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(listener.getCurrProfile(),MainWindow.joeUserInfo);
        ftpProfileJadeClient.copyLocalFileToRemote(localParentPath, remoteDir, localFilename);
        ftpProfileJadeClient.renameFile(remoteDir, localFilename, remoteFilename);
    }

    private void copyHotFolder2Remote() throws Exception {
        String remoteDir = txtDir.getText() + "";
        String localFullPath = new File(MainWindow.getContainer().getCurrentEditor().getFilename()).getCanonicalPath();
        String remoteFilename = txtFilename.getText();
        sosRemoteFileEntry = new SOSFileEntry(EntryType.FILESYSTEM);
        sosRemoteFileEntry.setDirectory(true);
        sosRemoteFileEntry.setFilename(remoteFilename);
        sosRemoteFileEntry.setParentPath(remoteDir);
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(listener.getCurrProfile(),MainWindow.joeUserInfo);
        ftpProfileJadeClient.copyLocalFilesToRemote(localFullPath, remoteDir, remoteFilename);
    }

    private void copyLocal2Remote() throws Exception {
        if (isSingleFile) {
            copySingleFile2Remote();
        } else {
            copyHotFolder2Remote();
        }
    }

    private String getJobSchedulerObject(String filename) {
        return filename.replaceFirst("^.*(\\.(job|order|job_chain|schedule|lock)\\.xml$)", "$1");
    }

    private String getNewFileName(String filename, String newObjectname) {
        return newObjectname + getJobSchedulerObject(filename);
    }

    private void openRemoteSingleFile() {
        try {
            File file = ftpProfileJadeClient.copyRemoteFileToLocal(sosRemoteFileEntry);
            if (MainWindow.getContainer().openQuick(file.getAbsolutePath()) != null) {
                setFtpProperties();
                MainWindow.getContainer().getCurrentTab().setData("ftp_remote_directory", sosRemoteFileEntry.getFullPath());
                MainWindow.getContainer().getCurrentTab().setData("sosFileEntry", sosRemoteFileEntry);
                MainWindow.setSaveStatus();
                schedulerConfigurationShell.dispose();
            }
        } catch (Exception r) {
            if (sosRemoteFileEntry != null) {
                MainWindow.message("could not open File: " + sosRemoteFileEntry.getFullPath() + ", cause: " + r.toString(), SWT.ICON_WARNING);
            } else {
                MainWindow.message("could not open File: , cause: " + r.toString(), SWT.ICON_WARNING);
            }
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
        }

    }

    private void openRemoteHotFolder() {
        try {
            String dir = sosRemoteFileEntry.getParentPath();
            String hotfolder = sosRemoteFileEntry.getFilename();
            ArrayList<String> hotFolderElements = ftpProfileJadeClient.copyRemoteFilesToLocal(dir, hotfolder);
            if (MainWindow.getContainer().openDirectory(listener.getCurrProfile().getLocaldirectory() + "/" + hotfolder) != null) {
                setFtpProperties();
                MainWindow.getContainer().getCurrentTab().setData("ftp_remote_directory", sosRemoteFileEntry.getFullPath());
                MainWindow.getContainer().getCurrentTab().setData("ftp_hot_folder_elements", hotFolderElements);
                MainWindow.getContainer().getCurrentTab().setData("sosFileEntry", sosRemoteFileEntry);
                MainWindow.setSaveStatus();
            }
            ftpProfileJadeClient.disconnect();
        } catch (Exception r) {
            if (sosRemoteFileEntry != null) {
                MainWindow.message("could not open File: " + sosRemoteFileEntry.getFullPath() + ", cause: " + r.toString(), SWT.ICON_WARNING);
            } else {
                MainWindow.message("could not open File: , cause: " + r.toString(), SWT.ICON_WARNING);
            }
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
        }
    }

    private void openRemote() {
        if (isSingleFile) {
            openRemoteSingleFile();
        } else {
            openRemoteHotFolder();
        }
    }

    private void saveas() {
        try {
            copyLocal2Remote();
            openRemote();
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not save File", e);
            MainWindow.message("could not save File: cause: " + e.getMessage(), SWT.ICON_WARNING);
        } finally {
            schedulerConfigurationShell.dispose();
        }
    }

    @Override
    protected void execute() {
        Utils.startCursor(schedulerConfigurationShell);
        saveas();
        Utils.stopCursor(schedulerConfigurationShell);
    }

}