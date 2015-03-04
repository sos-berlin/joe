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
    public ISOSVfsFileTransfer getFtpClient() {
        return ftpClient;
    }

    private static final String REGEX_FOR_JOBSCHEDULER_OBJECTS = "^.*\\.(job|job_chain|order|process_class|schedule|lock|config)\\.xml$";
    protected ISOSVFSHandler        objVFS                  = null;
    protected ISOSVfsFileTransfer   ftpClient               = null;
    protected enuTransferTypes      enuSourceTransferType   = enuTransferTypes.local;
    protected enuTransferTypes      enuTargetTransferType   = enuTransferTypes.local;
    private   JADEOptions           jadeOptions;
    SOSConnection2OptionsAlternate  virtuelFileSystemOptions;
    private   FTPProfile            ftpProfile              = null;

    public FTPProfileJadeClient(FTPProfile ftpProfile_) {
        super();
        this.ftpProfile = ftpProfile_;

    }
    
    public void disconnect() throws Exception{
        if (objVFS != null){
            objVFS.CloseConnection();
        }
        if (ftpClient != null){
           ftpClient.disconnect();
        }
    }
    
    private void connect() throws RuntimeException, Exception {
        if (objVFS == null){
            jadeOptions = new JADEOptions();
            enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol().toLowerCase());
    
           
            virtuelFileSystemOptions = jadeOptions.getConnectionOptions().Source();
           
            virtuelFileSystemOptions.host.Value(ftpProfile.getHost());
            virtuelFileSystemOptions.port.Value(ftpProfile.getPort());
            virtuelFileSystemOptions.user.Value(ftpProfile.getUser());
            virtuelFileSystemOptions.password.Value(ftpProfile.getDecryptetPassword());
            virtuelFileSystemOptions.protocol.Value(enuSourceTransferType);
            virtuelFileSystemOptions.auth_method.Value(ftpProfile.getAuthMethod());
    
            objVFS = VFSFactory.getHandler(enuSourceTransferType);
            ftpClient = (ISOSVfsFileTransfer) objVFS;
            objVFS.Connect(virtuelFileSystemOptions);
            objVFS.Authenticate(virtuelFileSystemOptions);
        }
     }
    
    
    private String getJobChainNodeParameterFilename(String filename){
        String result = "";
        if (filename.matches("^.*\\.job_chain\\.xml$")){
            result = filename.replaceAll("\\.job_chain\\.", ".config.");
        }
        return result;
    }
    
    
    public void mkdir(String directory, String newFolder) throws RuntimeException, Exception {
        connect();
        ftpClient.mkdir(directory + "/" + newFolder);
    }
    
    public Vector<String> getList(String remoteDir) throws RuntimeException, Exception{
        connect();
        return ftpClient.nList(remoteDir);   
    }
  
    public HashMap<String, SOSFileEntry> getDirectoryContent(String remoteDir) throws Exception{
        connect();
        
        if (remoteDir.equals("")){
            remoteDir = "/";
        }
        
        ftpClient.nList(remoteDir);
        SOSFileEntries sosFileList = ftpClient.getSOSFileEntries();

        HashMap <String,SOSFileEntry> h = new HashMap<String, SOSFileEntry>();
        
        for (SOSFileEntry sosFileListEntry : sosFileList) {
            String filename = sosFileListEntry.getFilename();
            if (!filename.equals(".")){
               h.put(filename,sosFileListEntry);
            }
        }
        return  h;
    }
   
  
    public void removeFile(SOSFileEntry sosFileEntry) throws Exception{
        connect();
        if (sosFileEntry.isDirectory()){
            ftpClient.changeWorkingDirectory(sosFileEntry.getParentPath());
            ftpClient.nList(sosFileEntry.getFilename());
            SOSFileEntries sosFileList = ftpClient.getSOSFileEntries();
            if (sosFileList.size() == 0){
                  ftpClient.rmdir(sosFileEntry.getFilename());
            }
            
        }else{
            ftpClient.delete(sosFileEntry.getFullPath());
        }
    }
    
    
    
    public void renameFile(String remoteDir, String oldFilename, String newFilename) throws Exception{
        connect();
        ftpClient.changeWorkingDirectory(remoteDir);
        ftpClient.rename(oldFilename, newFilename);
        
        //if this a job chain configuration file, also copy the configuration parameters file
        String jobChainNodeParameterOldFilename = getJobChainNodeParameterFilename(oldFilename);
        if (jobChainNodeParameterOldFilename.length() > 0){
            String jobChainNodeParameterNewFilename = getJobChainNodeParameterFilename(newFilename);
            try{
                ftpClient.rename(jobChainNodeParameterOldFilename, jobChainNodeParameterNewFilename);
            }catch(Exception e){}
        }
        
        
    }
    
    public void removeFile(String dir, String filename) throws Exception{
        SOSFileEntry sosFileEntry = new SOSFileEntry();
        sosFileEntry.setFilename(filename);
        sosFileEntry.setParentPath(dir);
        removeFile(sosFileEntry);
    }
    
    public void removeDir(String dir) throws Exception{
        connect();

        String folder = new File(dir).getName();
        String path = new File(dir).getParent();
        ftpClient.changeWorkingDirectory(path);
        
        ftpClient.rmdir(folder);    
    }
       
    public void copyLocalFileToRemote(String localDir, String targetDir, String filename) throws Exception{
        jadeOptions = new JADEOptions();

        
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());

        jadeOptions.TargetDir.Value(targetDir);
        jadeOptions.Target().host.Value(ftpProfile.getHost());
        jadeOptions.file_path.Value(filename);
        jadeOptions.Target().port.Value(ftpProfile.getPort());
        jadeOptions.Target().protocol.Value(enuSourceTransferType);
        jadeOptions.Target().user.Value(ftpProfile.getUser());
        jadeOptions.Target().password.Value(ftpProfile.getDecryptetPassword());
        if(!ftpProfile.getAuthMethod().equals("")){
            jadeOptions.Target().auth_method.Value(ftpProfile.getAuthMethod());
        }

        jadeOptions.SourceDir.Value(localDir);
        jadeOptions.Source().protocol.Value("local");
        
        jadeOptions.operation.Value(enuJadeOperations.copy);
      
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.Execute();
        
        //if this a job chain configuration file, also copy the configuration parameters file
        String jobChainNodeParameterFilename = getJobChainNodeParameterFilename(filename);
        if (jobChainNodeParameterFilename.length() > 0){
            jadeOptions.force_files.value(false);
            jadeOptions.file_path.Value(jobChainNodeParameterFilename);
            try{
            jadeEngine.Execute();
            }catch(Exception e){}
        }
        
        jadeEngine.Logout();
    }
    
    private  void removeRemoteHotFolderFiles(String remoteDir) throws Exception{
        JADEOptions jadeOptions = new JADEOptions();
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());
        jadeOptions.protocol.Value(enuSourceTransferType);
        jadeOptions.getConnectionOptions().Target().protocol.Value(enuSourceTransferType);
        jadeOptions.getConnectionOptions().Target().host.Value(ftpProfile.getHost());
        jadeOptions.getConnectionOptions().Target().port.Value(ftpProfile.getPort());
        jadeOptions.getConnectionOptions().Target().user.Value(ftpProfile.getUser());
        jadeOptions.getConnectionOptions().Target().password.Value(ftpProfile.getDecryptetPassword());
       
        if(!ftpProfile.getAuthMethod().equals("")){
            jadeOptions.getConnectionOptions().Target().auth_method.Value(ftpProfile.getAuthMethod());
        }
        
        
        jadeOptions.getConnectionOptions().Source().protocol.Value(enuSourceTransferType);
        jadeOptions.getConnectionOptions().Source().host.Value(ftpProfile.getHost());
        jadeOptions.getConnectionOptions().Source().port.Value(ftpProfile.getPort());
        jadeOptions.getConnectionOptions().Source().user.Value(ftpProfile.getUser());
        jadeOptions.getConnectionOptions().Source().password.Value(ftpProfile.getDecryptetPassword());
        if(!ftpProfile.getAuthMethod().equals("")){
            jadeOptions.getConnectionOptions().Source().auth_method.Value(ftpProfile.getAuthMethod());
        }
        
        
        jadeOptions.Source().protocol.Value(enuSourceTransferType);
        jadeOptions.Source().host.Value(ftpProfile.getHost());
        jadeOptions.Source().port.Value(ftpProfile.getPort());
        jadeOptions.Source().user.Value(ftpProfile.getUser());
        jadeOptions.Source().password.Value(ftpProfile.getDecryptetPassword());
        if(!ftpProfile.getAuthMethod().equals("")){
            jadeOptions.Source().auth_method.Value(ftpProfile.getAuthMethod());
        }
        
        
        jadeOptions.Target().protocol.Value(enuSourceTransferType);
        jadeOptions.Target().host.Value(ftpProfile.getHost());
        jadeOptions.Target().port.Value(ftpProfile.getPort());
        jadeOptions.Target().user.Value(ftpProfile.getUser());
        jadeOptions.Target().password.Value(ftpProfile.getDecryptetPassword());
        if(!ftpProfile.getAuthMethod().equals("")){
            jadeOptions.Target().auth_method.Value(ftpProfile.getAuthMethod());
        }
        
        jadeOptions.file_path.Value("");

        jadeOptions.file_path.Value("");
        jadeOptions.file_spec.Value(REGEX_FOR_JOBSCHEDULER_OBJECTS);
        jadeOptions.local_dir.Value(remoteDir);
        jadeOptions.remote_dir.Value(remoteDir);
        jadeOptions.operation.Value("delete");
        jadeOptions.ErrorOnNoDataFound.value(false);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.Execute();

         
        jadeEngine.Logout();
    }     
    
    
    
    public void copyLocalFilesToRemote(String sourceDir, String targetDir, String sourceHotFolder) throws Exception{
        jadeOptions = new JADEOptions();
        String remoteDir = targetDir + "/" + sourceHotFolder;
        String localDir = sourceDir ;
        
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());

        jadeOptions.SourceDir.Value(localDir);
        jadeOptions.Source().protocol.Value("local");
        
        jadeOptions.TargetDir.Value(remoteDir);
        jadeOptions.createFoldersOnTarget.value(true);
        jadeOptions.Target().host.Value(ftpProfile.getHost());
        jadeOptions.file_spec.Value(REGEX_FOR_JOBSCHEDULER_OBJECTS);
        jadeOptions.Target().port.Value(ftpProfile.getPort());
        jadeOptions.Target().protocol.Value(enuSourceTransferType);
        jadeOptions.Target().user.Value(ftpProfile.getUser());
        jadeOptions.Target().password.Value(ftpProfile.getDecryptetPassword());
        if(!ftpProfile.getAuthMethod().equals("")){
            jadeOptions.Target().auth_method.Value(ftpProfile.getAuthMethod());
        }

        try{
        removeRemoteHotFolderFiles(remoteDir);
        }catch(Exception e){}
        
        jadeOptions.operation.Value(enuJadeOperations.copy);
        jadeOptions.ErrorOnNoDataFound.value(false);
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);
        jadeEngine.Execute();


        jadeEngine.Logout();
    }    
    
    
     public File copyRemoteFileToLocal(SOSFileEntry sosFileEntry) throws Exception{
      
        jadeOptions = new JADEOptions();

        
        enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());

        jadeOptions.SourceDir.Value(sosFileEntry.getParentPath());
        jadeOptions.Source().host.Value(ftpProfile.getHost());
        jadeOptions.file_path.Value(sosFileEntry.getFilename());
        jadeOptions.Source().port.Value(ftpProfile.getPort());
        jadeOptions.Source().protocol.Value(enuSourceTransferType);
        jadeOptions.Source().user.Value(ftpProfile.getUser());
        jadeOptions.Source().password.Value(ftpProfile.getDecryptetPassword());
        if(!ftpProfile.getAuthMethod().equals("")){
            jadeOptions.Source().auth_method.Value(ftpProfile.getAuthMethod());
        }

        jadeOptions.TargetDir.Value(ftpProfile.getLocaldirectory());
        jadeOptions.Target().protocol.Value("local");
        
        jadeOptions.operation.Value(enuJadeOperations.copy);
      
        JadeEngine jadeEngine = new JadeEngine(jadeOptions);

        jadeEngine.Execute();
        //if this a job chain configuration file, also copy the configuration parameters file
        String jobChainNodeParameterFilename = getJobChainNodeParameterFilename(sosFileEntry.getFilename());
        if (jobChainNodeParameterFilename.length() > 0){
           jadeOptions.force_files.value(false); //Hat leider keine Funktion. Siehe JADE-268
           jadeOptions.ErrorOnNoDataFound.value(false);
           jadeOptions.file_path.Value(jobChainNodeParameterFilename);
           try{
               jadeEngine.Execute();
           }catch (Exception e){}
        }
        
        jadeEngine.Logout();
        return new File(ftpProfile.getLocaldirectory(),sosFileEntry.getFilename());
                
    }

  
    private  void removeLocalHotFolderFiles(String sourceDir) throws Exception{
         
         jadeOptions = new JADEOptions();
       
         enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());
         jadeOptions.protocol.Value("local");
         jadeOptions.getConnectionOptions().Source().protocol.Value("local");
         jadeOptions.getConnectionOptions().Target().protocol.Value("local");
         jadeOptions.file_path.Value("");
         jadeOptions.file_spec.Value(REGEX_FOR_JOBSCHEDULER_OBJECTS);
         jadeOptions.local_dir.Value(sourceDir);
         jadeOptions.remote_dir.Value(sourceDir);
         jadeOptions.operation.Value("delete");
         jadeOptions.ErrorOnNoDataFound.value(false);
         JadeEngine jadeEngine = new JadeEngine(jadeOptions);
         jadeEngine.Execute();

          
         jadeEngine.Logout();
     }     
     
     public ArrayList<String> copyRemoteFilesToLocal(String sourceDir,String soureHotFolder) throws Exception{
         
         jadeOptions = new JADEOptions();
         
         if (!sourceDir.startsWith("/") && !sourceDir.startsWith(".")){
         //    sourceDir = "./" + sourceDir;
         }
         
         String remoteDir = "";
         remoteDir = sourceDir + "/" + soureHotFolder;
         String localDir = ftpProfile.getLocaldirectory() + "/" + soureHotFolder;
         
         try {
             removeLocalHotFolderFiles(localDir);
         }catch(Exception e){}
         
         enuSourceTransferType = enuTransferTypes.valueOf(ftpProfile.getProtocol());

         jadeOptions.SourceDir.Value(remoteDir);
         jadeOptions.Source().host.Value(ftpProfile.getHost());
         jadeOptions.file_spec.Value(REGEX_FOR_JOBSCHEDULER_OBJECTS);
         jadeOptions.Source().port.Value(ftpProfile.getPort());
         jadeOptions.Source().protocol.Value(enuSourceTransferType);
         jadeOptions.Source().user.Value(ftpProfile.getUser());
         jadeOptions.Source().password.Value(ftpProfile.getDecryptetPassword());
         if(!ftpProfile.getAuthMethod().equals("")){
             jadeOptions.Source().auth_method.Value(ftpProfile.getAuthMethod());
         }

         jadeOptions.TargetDir.Value(localDir);
         jadeOptions.createFoldersOnTarget.value(true);
         jadeOptions.Target().protocol.Value("local");
         
         jadeOptions.operation.Value(enuJadeOperations.copy);
         jadeOptions.ErrorOnNoDataFound.value(false);
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

