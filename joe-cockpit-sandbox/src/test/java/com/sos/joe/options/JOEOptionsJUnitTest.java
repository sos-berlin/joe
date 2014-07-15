package com.sos.joe.options;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;

/**
 * \class 		JOEOptionsOptionsJUnitTest - JOEOptions
 *
 * \brief
 *
 *

 *
 * see \see C:\Users\KB\AppData\Local\Temp\scheduler_editor-3587302424923049269.html for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by C:\ProgramData\sos-berlin.com\jobscheduler\latestscheduler_4446\config\JOETemplates\java\xsl\JSJobDoc2JSJUnitOptionSuperClass.xsl from http://www.sos-berlin.com at 20130206180040
 * \endverbatim
 *
 * \section TestData Eine Hilfe zum Erzeugen einer HashMap mit Testdaten
 *
 * Die folgenden Methode kann verwendet werden, um für einen Test eine HashMap
 * mit sinnvollen Werten für die einzelnen Optionen zu erzeugen.
 *
 * \verbatim
 private HashMap <String, String> SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM) {
	pobjHM.put ("		JOEOptionsOptionsJUnitTest.auth_file", "test");  // This parameter specifies the path and name of a user's pr
		return pobjHM;
  }  //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)
 * \endverbatim
 */
public class JOEOptionsJUnitTest extends JSToolBox {
	private final String		conClassName	= "JOEOptionsOptionsJUnitTest";				//$NON-NLS-1$
	@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(JOEOptionsJUnitTest.class);
	private final JOEOptions	objE			= null;

	protected JOEOptions		objOptions		= null;

	public JOEOptionsJUnitTest() {
		//
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		objOptions = new JOEOptions();
		JSListenerClass.bolLogDebugInformation = true;
		JSListenerClass.intMaxDebugLevel = 9;
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * \brief testJOEHomeDir :
	 *
	 * \details
	 *
	 *
	 */
	@Test
	public void testJOEHomeDir() { // SOSOptionString
		assertEquals("JOE Home Dir wrong", "C:\\ProgramData\\sos-berlin.com\\JOE/", objOptions.JOEHomeDir.Value());
		objOptions.JOEHomeDir.Value("++env:SOS_JOE_HOME++");
		assertEquals("", objOptions.JOEHomeDir.Value(), "++env:SOS_JOE_HOME++");

	}

	/**
	 * \brief testJOEJobDocDir :
	 *
	 * \details
	 *
	 *
	 */
	@Test
	public void testJOEJobDocDir() { // SOSOptionString
		objOptions.JOEJobDocDir.Value("++env:SOS_JOBDOC_DIR++");
		assertEquals("", objOptions.JOEJobDocDir.Value(), "++env:SOS_JOBDOC_DIR++");

	}

} // public class JOEOptionsOptionsJUnitTest