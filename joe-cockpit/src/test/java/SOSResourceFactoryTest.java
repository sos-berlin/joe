import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.sos.resources.SOSProductionResource;
import com.sos.resources.SOSResourceFactory;

public class SOSResourceFactoryTest {

    @Test
    public void testAsFile() throws IOException {
        Path f = SOSResourceFactory.asFile("com/sos/resources/test.txt");
        assertEquals("this file contains only dummy text.", new String(Files.readAllBytes(f), Charset.defaultCharset()));
    }

    @Test
    public void testNamedResource() {
        Path tempFile = SOSResourceFactory.asFile(SOSProductionResource.SCHEDULER_XSD);
        //String s = this.getClass().getClassLoader().getResource(SOSProductionResource.SCHEDULER_XSD.getFullName()).toString();
        assertTrue(Files.exists(tempFile));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsFileInvalid() throws IOException {
        SOSResourceFactory.asFile("com/sos/resources/invalid.txt");
    }

}