package sos.scheduler.editor.classes;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJoeLockFolder {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLockFolder() throws IOException {
        JoeLockFolder joeLockFolder = new JoeLockFolder("c:/temp");
        joeLockFolder.unLockFolder();
        joeLockFolder.lockFolder();
        assertEquals("testLockFolder",true,joeLockFolder.getLockFile().exists());
        assertEquals("testLockFolder",true,joeLockFolder.isFolderLocked());        
    }
    
 

    @Test
    public void testIsFolderLocked() throws IOException {
        JoeLockFolder joeLockFolder = new JoeLockFolder("c:/temp");
        joeLockFolder.unLockFolder();
        joeLockFolder.lockFolder();
        assertEquals("testLockFolder",true,joeLockFolder.isFolderLocked());        
    }
    
    @Test
    public void testUnlockFolder() throws IOException {
        JoeLockFolder joeLockFolder = new JoeLockFolder("c:/temp");
        joeLockFolder.lockFolder();
        assertEquals("testLockFolder",true,joeLockFolder.getLockFile().exists());
        joeLockFolder.unLockFolder();
        assertEquals("testLockFolder",false,joeLockFolder.isFolderLocked());      
    }
    
    @Test
    public void testGetData() throws IOException {
        JoeLockFolder joeLockFolder = new JoeLockFolder("c:/temp");
        joeLockFolder.lockFolder();
        joeLockFolder.getDataFromFile();
        assertEquals("testLockFolder",System.getProperty("user.name"),joeLockFolder.getUserFromFile());      
    }
        
           
}
