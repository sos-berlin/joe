package com.sos.joe.options;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import com.sos.JSHelper.Options.JSOptionsClass;
import com.sos.JSHelper.Options.SOSOptionFolderName;
import com.sos.JSHelper.Options.SOSOptionInteger;

/**
 * \class 		JOEOptionsOptionsSuperClass - JOEOptions
 *
 * \brief
 * An Options-Super-Class with all Options. This Class will be extended by the "real" Options-class (\see JOEOptionsOptions.
 * The "real" Option class will hold all the things, which are normaly overwritten at a new generation
 * of the super-class.
 *
 *

 *
 * see \see C:\Users\KB\AppData\Local\Temp\scheduler_editor-3587302424923049269.html for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by C:\ProgramData\sos-berlin.com\jobscheduler\latestscheduler_4446\config\JOETemplates\java\xsl\JSJobDoc2JSOptionSuperClass.xsl from http://www.sos-berlin.com at 20130206180040
 * \endverbatim
 * \section OptionsTable Tabelle der vorhandenen Optionen
 *
 * Tabelle mit allen Optionen
 *
 * MethodName
 * Title
 * Setting
 * Description
 * IsMandatory
 * DataType
 * InitialValue
 * TestValue
 *
 *
 *
 * \section TestData Eine Hilfe zum Erzeugen einer HashMap mit Testdaten
 *
 * Die folgenden Methode kann verwendet werden, um für einen Test eine HashMap
 * mit sinnvollen Werten für die einzelnen Optionen zu erzeugen.
 *
 * \verbatim
 private HashMap <String, String> SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM) {
	pobjHM.put ("		JOEOptionsOptionsSuperClass.auth_file", "test");  // This parameter specifies the path and name of a user's pr
		return pobjHM;
  }  //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)
 * \endverbatim
 */
@JSOptionClass(name = "JOEOptionsOptionsSuperClass", description = "JOEOptionsOptionsSuperClass")
public class JOEOptionsSuperClass extends JSOptionsClass {
	/**
	 *
	 */
	private static final long	serialVersionUID	= -344851070737644820L;
	private final String		conClassName		= this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String	conSVNVersion		= "$Id$";
	@SuppressWarnings("unused")
	private final Logger		logger				= Logger.getLogger(this.getClass());

	/**
	 * \option ShowMessageDelay
	 * \type SOSOptionInteger
	 * \brief ShowMessageDelay - Define the number of seconds a message will be shown
	 *
	 * \details
	 * Defines the number of seconds a message will be shown
	 *
	 * \mandatory: f
	 *
	 * \created 01.11.2013 14:48:59 by KB
	 */
	@JSOptionDefinition(name = "ShowMessageDelay", description = "Defines the number of seconds a message will be shown", key = "ShowMessageDelay", type = "SOSOptionInteger", mandatory = true)
	public SOSOptionInteger		ShowMessageDelay	= new SOSOptionInteger( // ...
															this, // ....
															conClassName + ".ShowMessageDelay", // ...
															"Defines the number of seconds a message will be shown", // ...
															"3", // ...
															"3", // ...
															true);

	public String getShowMessageDelay() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getShowMessageDelay";

		return ShowMessageDelay.Value();
	} // public String getShowMessageDelay

	public JOEOptionsSuperClass setShowMessageDelay(final String pstrValue) {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::setShowMessageDelay";
		ShowMessageDelay.Value(pstrValue);
		return this;
	} // public JOEOptionsSuperClass setShowMessageDelay

	/**
	 * \var JOEHomeDir :
	 *
	 *
	 */
	@JSOptionDefinition(name = "JOEHomeDir", description = "", key = "JOEHomeDir", type = "SOSOptionString", mandatory = false)
	public SOSOptionFolderName	JOEHomeDir	= new SOSOptionFolderName(this, conClassName + ".JOEHomeDir", // HashMap-Key
													"", // Titel
													"env:SOS_JOE_HOME", // InitValue
													"env:SOS_JOE_HOME", // DefaultValue
													false // isMandatory
											);

	/**
	 * \brief getJOEHomeDir :
	 *
	 * \details
	 *
	 *
	 * \return
	 *
	 */
	public SOSOptionFolderName getJOEHomeDir() {
		return JOEHomeDir;
	}

	/**
	 * \brief setJOEHomeDir :
	 *
	 * \details
	 *
	 *
	 * @param JOEHomeDir :
	 */
	public void setJOEHomeDir(final SOSOptionFolderName p_JOEHomeDir) {
		JOEHomeDir = p_JOEHomeDir;
	}

	/**
	 * \var JOEJobDocDir :
	 *
	 *
	 */
	@JSOptionDefinition(name = "JOEJobDocDir", description = "", key = "JOEJobDocDir", type = "SOSOptionString", mandatory = false)
	public SOSOptionFolderName	JOEJobDocDir	= new SOSOptionFolderName(this, conClassName + ".JOEJobDocDir", // HashMap-Key
														"", // Titel
														"env:SOS_JOBDOC_DIR", // InitValue
														"env:SOS_JOBDOC_DIR", // DefaultValue
														false // isMandatory
												);

	/**
	 * \brief getJOEJobDocDir :
	 *
	 * \details
	 *
	 *
	 * \return
	 *
	 */
	public SOSOptionFolderName getJOEJobDocDir() {
		return JOEJobDocDir;
	}

	/**
	 * \brief setJOEJobDocDir :
	 *
	 * \details
	 *
	 *
	 * @param JOEJobDocDir :
	 */
	public void setJOEJobDocDir(final SOSOptionFolderName p_JOEJobDocDir) {
		JOEJobDocDir = p_JOEJobDocDir;
	}

	public JOEOptionsSuperClass() {
		objParentClass = this.getClass();
	} // public JOEOptionsOptionsSuperClass

	public JOEOptionsSuperClass(final JSListener pobjListener) {
		this();
		this.registerMessageListener(pobjListener);
	} // public JOEOptionsOptionsSuperClass

	//

	public JOEOptionsSuperClass(final HashMap<String, String> JSSettings) throws Exception {
		this();
		this.setAllOptions(JSSettings);
	} // public JOEOptionsOptionsSuperClass (HashMap JSSettings)

	/**
	 * \brief getAllOptionsAsString - liefert die Werte und Beschreibung aller
	 * Optionen als String
	 *
	 * \details
	 *
	 * \see toString
	 * \see toOut
	 */
	private String getAllOptionsAsString() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getAllOptionsAsString";
		String strT = conClassName + "\n";
		final StringBuffer strBuffer = new StringBuffer();
		// strT += IterateAllDataElementsByAnnotation(objParentClass, this,
		// JSOptionsClass.IterationTypes.toString, strBuffer);
		// strT += IterateAllDataElementsByAnnotation(objParentClass, this, 13,
		// strBuffer);
		strT += this.toString(); // fix
		//
		return strT;
	} // private String getAllOptionsAsString ()

	/**
	 * \brief setAllOptions - übernimmt die OptionenWerte aus der HashMap
	 *
	 * \details In der als Parameter anzugebenden HashMap sind Schlüssel (Name)
	 * und Wert der jeweiligen Option als Paar angegeben. Ein Beispiel für den
	 * Aufbau einer solchen HashMap findet sich in der Beschreibung dieser
	 * Klasse (\ref TestData "setJobSchedulerSSHJobOptions"). In dieser Routine
	 * werden die Schlüssel analysiert und, falls gefunden, werden die
	 * dazugehörigen Werte den Properties dieser Klasse zugewiesen.
	 *
	 * Nicht bekannte Schlüssel werden ignoriert.
	 *
	 * \see JSOptionsClass::getItem
	 *
	 * @param pobjJSSettings
	 * @throws Exception
	 */
	@Override
	public void setAllOptions(final HashMap<String, String> pobjJSSettings) throws Exception {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::setAllOptions";
		flgSetAllOptions = true;
		objSettings = pobjJSSettings;
		super.Settings(objSettings);
		super.setAllOptions(pobjJSSettings);
		flgSetAllOptions = false;
	} // public void setAllOptions (HashMap <String, String> JSSettings)

	/**
	 * \brief CheckMandatory - prüft alle Muss-Optionen auf Werte
	 *
	 * \details
	 * @throws Exception
	 *
	 * @throws Exception
	 * - wird ausgelöst, wenn eine mandatory-Option keinen Wert hat
	 */
	@Override
	public void CheckMandatory() throws JSExceptionMandatoryOptionMissing //
			, Exception {
		try {
			super.CheckMandatory();
		}
		catch (Exception e) {
			throw new JSExceptionMandatoryOptionMissing(e.toString());
		}
	} // public void CheckMandatory ()

	/**
	 *
	 * \brief CommandLineArgs - Übernehmen der Options/Settings aus der
	 * Kommandozeile
	 *
	 * \details Die in der Kommandozeile beim Starten der Applikation
	 * angegebenen Parameter werden hier in die HashMap übertragen und danach
	 * den Optionen als Wert zugewiesen.
	 *
	 * \return void
	 *
	 * @param pstrArgs
	 * @throws Exception
	 */
	@Override
	public void CommandLineArgs(final String[] pstrArgs) throws Exception {
		super.CommandLineArgs(pstrArgs);
		this.setAllOptions(super.objSettings);
	}
} // public class JOEOptionsOptionsSuperClass