package sos.ftp.profiles;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestFTPProfileJadeClientFTP extends TestFTPProfileJadeClient {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        ftpProperties = new Properties();
        ftpProperties.put("profilename", "test");
        ftpProperties.put("host", "homer.sos");
        ftpProperties.put("port", "21");
        ftpProperties.put("user", "test");
        ftpProperties.put("password", "12345");
        ftpProperties.put("root", "/home/test");
        ftpProperties.put("transfertype", "ASCII");
        ftpProperties.put("localdirectory", System.getProperty("java.io.tmpdir") + "/jobschedulertest");
        ftpProperties.put("profilename", "test");
        ftpProperties.put("protocol", "ftp");
        ftpProperties.put("auth_method", "password");
        // ftpProperties.put("auth_file","");
        ftpProfile = new FTPProfile(ftpProperties);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFTPProfileJadeClient() throws Exception {
        FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(new FTPProfile(new Properties()));
    }

    @Test
    public void testMkdir() throws RuntimeException, Exception {
        super.testMkdir();
    }

    @Test
    public void testGetList() throws Exception {
        super.testGetList();
    }

    @Test
    public void testGetDirectoryContent() throws Exception {
        super.testGetDirectoryContent();
    }

    @Test
    public void testRemoveFileSOSFileEntry() throws Exception {
        super.testRemoveFileSOSFileEntry();
    }

    @Test
    public void testRenameFile() throws RuntimeException, Exception {
        super.testRenameFile();
    }

    @Test
    public void testRemoveFileStringString() throws Exception {
        super.testRemoveFileStringString();
    }

    @Test
    public void testCopyLocalFileToRemote() throws Exception {
        super.testCopyLocalFileToRemote();
    }

    @Test
    public void testCopyLocalFilesToRemote() throws RuntimeException, Exception {
        super.testCopyLocalFilesToRemote();
    }

    @Test
    public void testCopyRemoteFileToLocal() throws Exception {
        ftpProfile = new FTPProfile(ftpProperties);

        super.testCopyRemoteFileToLocal();
    }

    @Test
    public void testCopyRemoteFilesToLocal() {
        // fail("Not yet implemented");
    }

}
