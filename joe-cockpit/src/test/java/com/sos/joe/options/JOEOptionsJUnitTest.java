package com.sos.joe.options;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import org.apache.log4j.Logger;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/** \class JOEOptionsOptionsJUnitTest - JOEOptions
 *
 * \brief
 *
 *
 * 
 *
 * see \see
 * C:\Users\KB\AppData\Local\Temp\scheduler_editor-3587302424923049269.html for
 * (more) details.
 *
 * \verbatim ; mechanicaly created by
 * C:\ProgramData\sos-berlin.com\jobscheduler\
 * latestscheduler_4446\config\JOETemplates
 * \java\xsl\JSJobDoc2JSJUnitOptionSuperClass.xsl from http://www.sos-berlin.com
 * at 20130206180040 \endverbatim
 *
 * \section TestData Eine Hilfe zum Erzeugen einer HashMap mit Testdaten
 *
 * Die folgenden Methode kann verwendet werden, um für einen Test eine HashMap
 * mit sinnvollen Werten für die einzelnen Optionen zu erzeugen.
 *
 * \verbatim private HashMap <String, String> SetJobSchedulerSSHJobOptions
 * (HashMap <String, String> pobjHM) { pobjHM.put
 * ("		JOEOptionsOptionsJUnitTest.auth_file", "test"); // This parameter
 * specifies the path and name of a user's pr return pobjHM; } // private void
 * SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM) \endverbatim */
public class JOEOptionsJUnitTest extends JSToolBox {

    private final String conClassName = "JOEOptionsOptionsJUnitTest";				//$NON-NLS-1$
    private static Logger logger = Logger.getLogger(JOEOptionsJUnitTest.class);

    protected JOEOptions objOptions = null;

    @Before
    public void setUp() throws Exception {
        objOptions = new JOEOptions();
        JSListenerClass.bolLogDebugInformation = true;
        JSListenerClass.intMaxDebugLevel = 9;
    }

    /** \brief testJOEHomeDir :
     *
     * \details */
    @Test
    public void testJOEHomeDir() { // SOSOptionString
        assertNotEquals("JOE Home Dir wrong", "C:\\ProgramData\\sos-berlin.com\\JOE/", objOptions.JOEHomeDir.Value());
        objOptions.JOEHomeDir.Value("++env:SOS_JOE_HOME++");
        // Value(String) Method adds "/" to the path, therefore it has to be
        // added to the expected value too
        assertEquals("", "++env:SOS_JOE_HOME++" + "/", objOptions.JOEHomeDir.Value());

    }

    /** \brief testJOEJobDocDir :
     *
     * \details */
    @Test
    public void testJOEJobDocDir() { // SOSOptionString
        objOptions.JOEJobDocDir.Value("++env:SOS_JOBDOC_DIR++");
        // Value(String) Method adds "/" to the path, therefore it has to be
        // added to the expected value too
        assertEquals("", "++env:SOS_JOBDOC_DIR++" + "/", objOptions.JOEJobDocDir.Value());

    }

} // public class JOEOptionsOptionsJUnitTest