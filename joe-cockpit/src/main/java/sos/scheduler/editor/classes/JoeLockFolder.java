package sos.scheduler.editor.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;

import org.eclipse.swt.SWT;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.scheduler.editor.app.MainWindow;

public class JoeLockFolder {

    private static final Logger  LOGGER    = LoggerFactory.getLogger(JoeLockFolder.class);
    private static final String DEFAUL_TIME_SINCE = "2000-01-01T00:00:00.000Z";
    private static final String JOE_XML_LOCK = "joe.xml.lock";
    String folderName;
    File lockFile;
    String userFromFile;
    String sinceFromFile;
    
    public JoeLockFolder(String folderName_) {
        super();
        if (folderName_ == null){
            folderName_ = "/";            
        }
        File f = new File(folderName_);
        if (f.isDirectory()){
            this.folderName = folderName_;
        }else{
            this.folderName = f.getParent();
        }
        lockFile = new File(folderName,JOE_XML_LOCK);
    }

   
    private void createLockFile(String user, String date) throws IOException{
        lockFile.delete();
        lockFile.createNewFile();
        FileOutputStream fos = null;
        String sUser = "User: " + user +  "\n";
        String sDate = "Since: " + date.getBytes();
        fos = new FileOutputStream(lockFile);
        fos.write(sUser.getBytes() );
        fos.write(date.getBytes());
        fos.flush();
        fos.close(); 
    }
    
    private void createLockFile() throws IOException{
        createLockFile(System.getProperty("user.name"),Instant.now().toString());
    }
    
    public void getDataFromFile(File lockFilePath){
        BufferedReader br = null;
        if (lockFilePath != null) {
            try {
                File f = new File(lockFilePath,JOE_XML_LOCK);
                if (f.exists()){
                 
                    FileReader fr = new FileReader(new File(lockFilePath,JOE_XML_LOCK));
                    br = new BufferedReader(fr);
                    String zeile = null;
                 
                    if ((zeile = br.readLine()) != null) {
                        userFromFile = zeile.replaceAll("^.*User: (.*)$", "$1");
                    }
                    if ((zeile = br.readLine()) != null) {
                        sinceFromFile = zeile.replaceAll("^.*Since: (.*)$", "$1");
                    }
                    br.close();
                    fr.close();
                }
             } catch (IOException e) {
                  LOGGER.error(e.getMessage(),e);
                  userFromFile = "Error occured reading lock file: " + lockFilePath.getAbsolutePath();
                  sinceFromFile = e.getMessage();
             }
        }else{
            userFromFile=System.getProperty("user.name"); 
            sinceFromFile=DEFAUL_TIME_SINCE;
        }
    }

    public String getUserFromFile() {
        return userFromFile;
    }

    public boolean userChanged(){
        getDataFromFile();
        return (!userFromFile.equals(System.getProperty("user.name")));
    }


    public String getSinceFromFile() {
        final DateTimeZone fromTimeZone = DateTimeZone.UTC;
        final DateTimeZone toTimeZone = DateTimeZone.getDefault();
        final DateTime dateTime = new DateTime(sinceFromFile, fromTimeZone);

        final DateTimeFormatter outputFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(toTimeZone);
        return outputFormatter.print(dateTime);
    }


    public void getDataFromFile(){
        getDataFromFile(lockFile.getParentFile());
    }
    
    public boolean isFolderLocked(){
        return lockFile.exists();
    }
    
    public void lockFolder() {
        try {
            createLockFile();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
     
    public void unLockFolder(){
        if (!isFolderLocked()){
            lockFolder();
        }
        if (!userChanged()){
            lockFile.delete();
        }else{
            String message = String.format("Could not unlock the folder \n\n%s\n\nas the User %s holds the folder since %s",folderName,userFromFile,getSinceFromFile());
            MainWindow.message(message, SWT.ICON_INFORMATION);

        }
    }


    public File getLockFile() {
        return lockFile;
    }    
}
