package sos.ftp.profiles;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.JSHelper.io.Files.JSFile;
import com.sos.VirtualFileSystem.common.SOSFileEntry;
 
public class TestFTPProfileJadeClient {
  protected Properties ftpProperties;
  protected FTPProfile ftpProfile ;
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
    
    private void cleanupFolder(String dir) {
        SOSFileEntry sosFileEntry = new SOSFileEntry();
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        
        try{
            sosFileEntry.setDirectory(false);
            sosFileEntry.setFilename("1.job.xml");
            sosFileEntry.setParentPath(dir);
            ftpProfileJadeClient.removeFile(sosFileEntry);
        }catch (Exception e){}
        try{
            sosFileEntry.setFilename("1.txt");
            ftpProfileJadeClient.removeFile(sosFileEntry);
            ftpProfileJadeClient.disconnect();
        }catch (Exception e){}
        
    }

    

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFTPProfileJadeClient() throws Exception {
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(new FTPProfile(new Properties()));
    }
 
    
    public void testMkdir() throws RuntimeException, Exception {
        
        String dir=ftpProfile.getRoot();
        String folder="newfolder";
        String path = dir + "/" + folder;
        
        cleanupFolder(path);
        
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        SOSFileEntry sosFileEntry = new SOSFileEntry();
        sosFileEntry.setDirectory(true);
        sosFileEntry.setFilename(folder);
        sosFileEntry.setParentPath(dir);
        try{
        ftpProfileJadeClient.removeFile(sosFileEntry);
        ftpProfileJadeClient.removeDir(path);
        }catch (Exception e){}
        ftpProfileJadeClient.mkdir(dir,folder);
        assertTrue ("Directory must exist", ftpProfileJadeClient.getFtpClient().isDirectory(path));
        ftpProfileJadeClient.removeDir(path);
        assertFalse ("Directory should have been deleted ", ftpProfileJadeClient.getFtpClient().isDirectory(path));
        ftpProfileJadeClient.disconnect();
        
    }

 
    
    public void testGetList() throws Exception {
 
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        Vector<String> v = ftpProfileJadeClient.getList(ftpProfile.getRoot());

        Iterator<String> iter = v.iterator();
        String s=(String) iter.next();
        assertNotEquals("Directory should exist", "",s);
        ftpProfileJadeClient.disconnect();
    }

    
    public void testGetDirectoryContent() throws Exception {
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        HashMap<String, SOSFileEntry> h =  ftpProfileJadeClient.getDirectoryContent(ftpProfile.getRoot());
        Iterator<String> it = h.keySet().iterator();
        
        String key = it.next();
        SOSFileEntry sosFileEntry = h.get(key);
        assertNotEquals("Directory should exist", "",key);
     }

    public void testRemoveFileSOSFileEntry() throws Exception {
        String dir=ftpProfile.getRoot();
        String folder="newfolderRemoveFile";
        String path = dir + "/" + folder;
        
        cleanupFolder(path);
        
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        ftpProfileJadeClient.mkdir(dir,folder);
        assertTrue ("Directory must exist", ftpProfileJadeClient.getFtpClient().isDirectory(path));
        SOSFileEntry sosFileEntry = new SOSFileEntry();
        sosFileEntry.setDirectory(true);
        sosFileEntry.setFilename(folder);
        sosFileEntry.setParentPath(dir);
        ftpProfileJadeClient.removeFile(sosFileEntry);
        assertFalse ("Directory should have been deleted ", ftpProfileJadeClient.getFtpClient().isDirectory(path));
        ftpProfileJadeClient.disconnect();


    }

    public void testRenameFile() throws RuntimeException, Exception {
        String dir=ftpProfile.getRoot();
        String folder="newfolder";
        String path = dir + "/" + folder;
        
        cleanupFolder(path);
        
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        ftpProfileJadeClient.mkdir(dir,folder);
        assertTrue ("Directory must exist", ftpProfileJadeClient.getFtpClient().isDirectory(path));
        SOSFileEntry sosFileEntry = new SOSFileEntry();
        sosFileEntry.setDirectory(true);
        sosFileEntry.setFilename(folder);
        sosFileEntry.setParentPath(dir);
        ftpProfileJadeClient.renameFile(dir, folder, "renamed");
        assertTrue ("Directory must exist", ftpProfileJadeClient.getFtpClient().isDirectory(dir + "/renamed"));
        assertFalse ("Directory should have been deleted ", ftpProfileJadeClient.getFtpClient().isDirectory(path));
        try {
            ftpProfileJadeClient.removeDir(dir + "/renamed");
        }catch(Exception e){}

        ftpProfileJadeClient.disconnect();
    }
    
    private void CreateTestFile(String dir, String filename) {
        JSFile temporaryFile = new JSFile(dir + "/" + filename);
        temporaryFile.deleteOnExit();
        try {
            temporaryFile.WriteLine("This is a test file nothing else");
            temporaryFile.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testRemoveFileStringString() throws Exception {
        String localDir=ftpProfile.getLocaldirectory();
        String filename = "1.txt";
        String targetDir=ftpProfile.getRoot();
        String folder="newfolder";
        String path = targetDir + "/" + folder;
        
        cleanupFolder(path);
        
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        ftpProfileJadeClient.mkdir(targetDir,folder);
        assertTrue ("Directory must exist", ftpProfileJadeClient.getFtpClient().isDirectory(path));
     
        CreateTestFile(localDir,filename);
        ftpProfileJadeClient.copyLocalFileToRemote(localDir, path, filename);
        
        ftpProfileJadeClient.removeFile(path,filename);
        HashMap<String, SOSFileEntry> h =  ftpProfileJadeClient.getDirectoryContent(path);
        assertEquals("File should have been deleted ", 0,h.size());
        ftpProfileJadeClient.disconnect();    }

    public void testCopyLocalFileToRemote() throws Exception {
        String localDir=ftpProfile.getLocaldirectory();
        String filename = "1.txt";
        String targetDir=ftpProfile.getRoot();
        String folder="newfolder";
        String path = targetDir + "/" + folder;
        
        cleanupFolder(path);
        
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        ftpProfileJadeClient.mkdir(targetDir,folder);
        assertTrue ("Directory must exist", ftpProfileJadeClient.getFtpClient().isDirectory(path));
     
        CreateTestFile(localDir,filename);
        ftpProfileJadeClient.copyLocalFileToRemote(localDir, path, filename);
        
        HashMap<String, SOSFileEntry> h =  ftpProfileJadeClient.getDirectoryContent(path);
        assertEquals("File should have been deleted ", 1,h.size());
        ftpProfileJadeClient.disconnect();        
    }

    public void testCopyLocalFilesToRemote() throws RuntimeException, Exception {
        String localDir=ftpProfile.getLocaldirectory();
        String filename = "1.job.xml";
        String targetDir=ftpProfile.getRoot();
        String folder="newfolder";
        String path = targetDir + "/" + folder;
        
        cleanupFolder(path);
        
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        ftpProfileJadeClient.mkdir(targetDir,folder);
        assertTrue ("Directory must exist", ftpProfileJadeClient.getFtpClient().isDirectory(path));
     
        CreateTestFile(localDir,filename);
        ftpProfileJadeClient.copyLocalFilesToRemote(localDir,  targetDir, folder);
        
        HashMap<String, SOSFileEntry> h =  ftpProfileJadeClient.getDirectoryContent(path);
        assertEquals("File should have been transfered ", 1,h.size());
        ftpProfileJadeClient.disconnect();        
    }

    public void testCopyRemoteFileToLocal() throws Exception {
        
        testCopyLocalFileToRemote();
        
        String filename = "1.txt";
        String sourceDir=ftpProfile.getRoot();
        String folder="newfolder";
        String path = sourceDir + "/" + folder;
        
        File targetFile = new File (ftpProfile.getLocaldirectory(),filename);
        targetFile.delete();

        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        ftpProfileJadeClient.mkdir(sourceDir,folder);

        SOSFileEntry sosFileEntry = new SOSFileEntry();
        sosFileEntry.setDirectory(true);
        sosFileEntry.setFilename(filename);
        sosFileEntry.setParentPath(path);
        ftpProfileJadeClient.copyRemoteFileToLocal(sosFileEntry);
        assertTrue ("File must exist",targetFile.exists());

        ftpProfileJadeClient.disconnect();            
        
    }

   public void testCopyRemoteFilesToLocal() throws Exception {
        
        String filename = "1.txt";
        String sourceDir=ftpProfile.getRoot();
        String folder="newfolder";
        String path = sourceDir + "/" + folder;
        
        File targetFile = new File (ftpProfile.getLocaldirectory(),filename);
        targetFile.delete();

        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(ftpProfile);
        ftpProfileJadeClient.mkdir(sourceDir,folder);

        SOSFileEntry sosFileEntry = new SOSFileEntry();
        sosFileEntry.setDirectory(true);
        sosFileEntry.setFilename(filename);
        sosFileEntry.setParentPath(path);
        ftpProfileJadeClient.copyRemoteFileToLocal(sosFileEntry);
        assertTrue ("File must exist",targetFile.exists());

        ftpProfileJadeClient.disconnect();            
        
    }    
   

}
