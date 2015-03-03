package sos.scheduler.editor.app;

// Wird zurzeit nicht verwendet. Menüpunkt saveAs ist deaktiviert.

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.sos.VirtualFileSystem.common.SOSFileEntry;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.misc.ResourceManager;

public class FTPDialogOpenFile extends FTPDialog{
 
    
	private static final String FILENAME = "Filename";
    private static final String OPEN_FILE = "Open";

    public FTPDialogOpenFile() {
	    super();
	}

    @Override
    String getTitle() {
        return OPEN_FILE;
    }

    @Override
    protected void setTxtFilenameText(Text txtFilename, TableItem tableItem) {
         txtFilename.setText(tableItem.getText(0));
    }

    @Override
    protected String getFilenameLabel() {
      return FILENAME;
    }
    
    @Override
    protected void fillTable(Table directoryTable, HashMap<String, SOSFileEntry> h) {
        
        try{
            Iterator<String> it = h.keySet().iterator();
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
                     }else {
                         item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_file.gif"));
                     }
                 }
             
             
        }
        catch (Exception e) {
            try {
                new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            }
            catch (Exception ee) {
            }
            System.out.println("..error in FTPDialog " + e.getMessage());
        }
    }

    @Override
    protected void setTooltip() {
        butExecute.setToolTipText(Messages.getTooltip("ftpdialog.btn_open_file"));
        txtFilename.setToolTipText(Messages.getTooltip("ftpdialog.txt_open_file"));
    }
         

    @Override
    protected void execute() {
        Utils.startCursor(schedulerConfigurationShell);
        openFile();
        Utils.stopCursor(schedulerConfigurationShell);
    }
 
    /**
     * Öffnet die ausgewählte Datei.
     * 
     */
    private void openFile() {
        SOSFileEntry sosFileEntry = null;
        try {
            
            sosFileEntry = getSosFileEntryFromTable();
            
            if (sosFileEntry != null){
                if (sosFileEntry.isDirectory()) {//Open the directory in the dir file list
                    txtDir.setText(sosFileEntry.getFullPath());
                    fillTable(ftpProfileJadeClient.getDirectoryContent(txtDir.getText()));
                    txtFilename.setText("");
                }else{//Open the file in JOE
            
                    File file = ftpProfileJadeClient.copyRemoteFileToLocal(sosFileEntry);
            
                    if (MainWindow.getContainer().openQuick(file.getAbsolutePath()) != null) {
                        MainWindow.getContainer().getCurrentTab().setData("ftp_profile_name", listener.getCurrProfileName());
                        MainWindow.getContainer().getCurrentTab().setData("ftp_profile", listener.getCurrProfile());
                        MainWindow.getContainer().getCurrentTab().setData("ftp_title", "[FTP::" + listener.getCurrProfileName() + "]");
                        MainWindow.getContainer().getCurrentTab().setData("ftp_remote_directory", sosFileEntry.getFullPath());
                        MainWindow.getContainer().getCurrentTab().setData("sosFileEntry", sosFileEntry);
                        MainWindow.setSaveStatus();
                        schedulerConfigurationShell.dispose();
                }
              }

             }
        }
        catch (Exception r) {
                if(sosFileEntry != null){
                    MainWindow.message("could not open File: " + sosFileEntry.getFullPath() + ", cause: " + r.toString(), SWT.ICON_WARNING);
                }else{
                    MainWindow.message("could not open File: , cause: " + r.toString(), SWT.ICON_WARNING);
                }
                new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), r);
        }
    }
}
