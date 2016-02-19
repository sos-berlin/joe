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
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;

public class FTPDialogHotFolder extends FTPDialog{
 
    
	private static final String FOLDER = "Folder";
    private static final String OPEN_HOT_FOLDER = "Open Hot Folder";

    public FTPDialogHotFolder() {
	    super();
	}

    @Override
    String getTitle() {
        return OPEN_HOT_FOLDER;
    }

    @Override
    protected void setTxtFilenameText(Text txtFilename, TableItem tableItem) {
       txtFilename.setText(tableItem.getText(0));
        
    }

    @Override
    protected String getFilenameLabel() {
      return FOLDER;
    }
    
    @Override
    protected void fillTable(Table directoryTable, HashMap<String, SOSFileEntry> h) {
        
        try{
            directoryTable.removeAll();
            Iterator<String> it = h.keySet().iterator();
            
            // directories
            while (it.hasNext()) {
                String key = it.next();
                SOSFileEntry sosFileEntry = h.get(key);

                if (sosFileEntry.isDirectory()) {
                   TableItem item = new TableItem(directoryTable, SWT.NONE);
                   item.setText(0, key);
                   item.setText(1, sosFileEntry.getFilesizeAsString());
                   item.setText(2, sosFileEntry.getCategory());
                   item.setData(sosFileEntry);
                   item.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_directory.gif"));
                } 
            }
          
        }
        catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    @Override
    protected void setTooltip() {
        butExecute.setToolTipText(Messages.getTooltip("ftpdialog.btn_open_hot_folder"));
        txtFilename.setToolTipText(Messages.getTooltip("ftpdialog.txt_open_hot_folder"));
    }

    @Override
    void execute() {
        Utils.startCursor(schedulerConfigurationShell);
        openHotFolder();
        Utils.stopCursor(schedulerConfigurationShell);
        
    }
    
    private void openHotFolder()   {
         
         
        try {
            String dir = txtDir.getText();
       
            String hotfolder = txtFilename.getText();
            
            SOSFileEntry sosFileEntry = null;
            sosFileEntry = getSosFileEntryFromTable();
            if (sosFileEntry == null){
                sosFileEntry = new SOSFileEntry();
                sosFileEntry.setDirectory(true);
                sosFileEntry.setFilename(new File(txtDir.getText()).getName());
                sosFileEntry.setParentPath(new File(txtDir.getText()).getParent());
                dir = sosFileEntry.getParentPath();
                hotfolder = sosFileEntry.getFilename();
            }

            
                ArrayList<String> hotFolderElements = ftpProfileJadeClient.copyRemoteFilesToLocal(dir,hotfolder);
           
                String s = Options.getLastFolderName();
                if (MainWindow.getContainer().openDirectory(listener.getCurrProfile().getLocaldirectory() + "/" + hotfolder) != null) {
                    Options.setLastFolderName(s);

                    MainWindow.getContainer().getCurrentTab().setData("ftp_profile_name", listener.getCurrProfileName());
                    MainWindow.getContainer().getCurrentTab().setData("ftp_profile", listener.getCurrProfile());
                    MainWindow.getContainer().getCurrentTab().setData("ftp_title", "[FTP::" + listener.getCurrProfileName() + "]");
                    MainWindow.getContainer().getCurrentTab().setData("ftp_remote_directory", sosFileEntry.getFullPath());
                    MainWindow.getContainer().getCurrentTab().setData("ftp_hot_folder_elements", hotFolderElements);
                    MainWindow.getContainer().getCurrentTab().setData("sosFileEntry", sosFileEntry);

                    MainWindow.setSaveStatus();
                }
                ftpProfileJadeClient.disconnect();
            schedulerConfigurationShell.dispose();
            
        }
        catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not Open Hot Folder.", e);
            MainWindow.message("could not Open Hot Folder: cause: " + e.getMessage(), SWT.ICON_WARNING);
        }
    }
 
}
