package sos.scheduler.editor.app;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.sos.JSHelper.Options.SOSOptionLocale;
import com.sos.i18n.I18NBase;
import com.sos.i18n.annotation.I18NResourceBundle;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public class Options extends I18NBase {
	private static final String	conLanguageEN								= "en";
	private static final String	conEnvironmentVariableSOS_LOCALE			= "SOS_LOCALE";
	public static final String	conPropertyEDITOR_OPTIONS_FILE				= "editor.options.file";
	public static final String	conPropertyEDITOR_LANGUAGE					= "editor.language";
	public static final String	conPropertyEDITOR_ShowSplashScreen			= "editor.ShowSplashScreen";
	public static final String	conPropertyEDITOR_OpenLastFolder			= "editor.OpenLastFolder";
	public static final String	conPropertyTEMPLATE_LANGUAGE				= "template.language";
	public static final String	conPropertyTEMPLATE_LANGUAGE_LIST			= "template.language.list";
	public static final String	conPropertyEDITOR_ShowSplashScreenPicture	= "editor.ShowSplashScreenPicture"; // "./SplashScreenJOE.bmp";
	

	private final static String	conClassName								= "Options";
	@SuppressWarnings("unused")
	private final String		conSVNVersion								= "$Id$";
	private static final Logger	logger										= Logger.getLogger(Options.class);
	public static final String	DEFAULT_OPTIONS								= "/sos/scheduler/editor/options.properties";
	private static Properties	_defaults									= null;
	private static Properties	_properties									= null;
	private static boolean		_changed									= false;
	private static boolean		_showWizardInfo								= true;
	private static String[]		jobTitleList								= null;
	private static HashMap		holidaysDescription							= null;
	public static String		conJOEGreeting								= "";
	private static String		strLastFolderName							= "";

	private Options() {
	}

	public static int getLastTabItemIndex() {
		String strR = _properties.getProperty("LastTabItemIndex");
		if (strR == null || strR == "") {
			return -1;
		}
		return (int) new Integer(strR);
	}

	public static void setLastTabItemIndex(final int pintLastTabItemIndex) {
		setProperty("LastTabItemIndex", String.valueOf(pintLastTabItemIndex));
	}

	public static String getLastFolderName() {
		return _properties.getProperty("LastFolderName");
	}

	public static void setLastFolderName(final String pstrLastFolderName) {
		setProperty("LastFolderName", pstrLastFolderName);
	}

	public static String getLastIncludeFolderName() {
		return _properties.getProperty("LastIncludeFolderName");
	}

	public static String getLastPropertyValue(final String pstrPropertyName) {
		return _properties.getProperty("LastIncludeFolderName");
	}

	public static void setLastPropertyValue(final String pstrPropertyName, final String pstrPropertyValue) {
		setProperty(pstrPropertyName, pstrPropertyValue);
	}

	public static void setLastIncludeFolderName(final String pstrLastFolderName) {
		setProperty("LastIncludeFolderName", pstrLastFolderName);
	}

	public static String getDefaultOptionFilename() {
		getProperties();
		// return getDefault("editor.options.file").replaceAll("\\{scheduler_home\\}", getSchedulerHome());
		String strSD = getSchedulerData().replaceAll("\\\\", "/");
		String strKey = conPropertyEDITOR_OPTIONS_FILE;
		String strF = getDefault(strKey);
		String strRet = strF.replaceAll("\\{scheduler_data\\}", strSD);
		return strRet;
	}

	private static void getProperties() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getProperties";
		if (_properties == null) {
			_properties = new Properties(_defaults);
		}
	} // private void getProperties

	public static String loadOptions(Class cl) {
		String fName = "";
		try {
			_defaults = new Properties();
			logger.debug(String.format("load Options from file: %1$s", DEFAULT_OPTIONS));
			_defaults.load(cl.getResourceAsStream(DEFAULT_OPTIONS));
			_properties = new Properties(_defaults);
			// _properties = new Properties();
			// _properties.putAll(_defaults);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + "; Error reading default options from " + DEFAULT_OPTIONS, e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			return "Error reading default options from " + DEFAULT_OPTIONS + ": " + e.getMessage();
		}
		try {
			fName = getDefaultOptionFilename();
			File file = new File(fName);
			if (file.exists()) {
				FileInputStream fi = new FileInputStream(fName);
				_properties.load(fi);
				fi.close();
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + ". Error reading custom options from " + fName, e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			return "Error reading custom options from " + fName + ": " + e.getMessage();
		}
		return null;
	}

	public static String saveProperties() {
		if (_properties != null && _changed) {
			try {
				FileOutputStream fo = new FileOutputStream(getDefaultOptionFilename());
				_properties.store(fo, conJOEGreeting + " - Options --");
				fo.close();
			}
			catch (Exception e) {
				try {
					new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
				}
				catch (Exception ee) {
					// tu nichts
				}
				e.printStackTrace();
				return e.getMessage();
			}
		}
		return null;
	}

	public static void setProperty(String key, String value) {
		if (_properties != null) {
			_properties.setProperty(key, value);
			_changed = true;
		}
	}

	public static boolean showSplashScreen() {
		return getBoolOption(conPropertyEDITOR_ShowSplashScreen);
	}

	public static boolean openLastFolder() {
		return getBoolOption(conPropertyEDITOR_OpenLastFolder);
	}

	public static boolean checkBool(final String pstrText) {
		boolean flgR = false;
		if (pstrText != null && pstrText.equalsIgnoreCase("true")) {
			flgR = true;
		}
		return flgR;
	}

	public static boolean getBoolOption (final String pstrPropertyName) {
		getProperties();
		String strT = _properties.getProperty(pstrPropertyName);
		boolean flgR = checkBool(strT);
		return flgR;		
	}
	public static String showSplashScreenPicture() {
		getProperties();
		String strT = _properties.getProperty(conPropertyEDITOR_ShowSplashScreenPicture, "./SplashScreenJOE.bmp");
		return strT;
	}

	public static String getLanguage() {
		getProperties();
		String strT = Locale.getDefault().getLanguage();
		String strSOSLocale = System.getenv(conEnvironmentVariableSOS_LOCALE);
		if (strSOSLocale != null) {
			strT = strSOSLocale;
		}
		else {
			strT = _properties.getProperty(conPropertyEDITOR_LANGUAGE);
			if (strT == null || strT.trim().length() <= 0) {
				strT = conLanguageEN;
			}
		}
		SOSOptionLocale.i18nLocale = new Locale(strT);
		return strT;
	}

	public static String getTemplateLanguage() {
		getProperties();
		String strT = Locale.getDefault().getLanguage();
		String strSOSLocale = System.getenv(conEnvironmentVariableSOS_LOCALE);
		if (strSOSLocale != null) {
			strT = strSOSLocale;
		}
		else {
			strSOSLocale = strT;
		}

		strT = _properties.getProperty(conPropertyTEMPLATE_LANGUAGE);
		if (strT == null || strT.trim().length() <= 0) {
			strT = strSOSLocale;
			setTemplateLanguage(strT);
		}

		return strT;
	}

	public static String getTemplateLanguageList() {
		getProperties();
		String strLanguages = "de;en;fr;it;es";

		String strT = _properties.getProperty(conPropertyTEMPLATE_LANGUAGE_LIST);
		if (strT == null || strT.trim().length() <= 0) {
			strT = strLanguages;
			setProperty(conPropertyTEMPLATE_LANGUAGE_LIST, strT);
		}

		return strT;
	}

	public static String getDefault(String key) {
		if (_defaults == null) {
			_defaults = new Properties();
		}
		return _defaults.getProperty(key);
	}

	public static void setLanguage(String language) {
		SOSOptionLocale.i18nLocale = new Locale(language);
		setProperty(conPropertyEDITOR_LANGUAGE, language);
	}

	public static void setTemplateLanguage(String language) {
		setProperty(conPropertyTEMPLATE_LANGUAGE, language);
	}

	private static String getHelp(String key, String prefix) {
		try {
			String url = _properties.getProperty(prefix + ".help.url." + key);
			return url != null && !url.equals("") ? url : null;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static String getHelpURL(String key, String prefix) {
		try {
			String helpKey = getHelp(key, prefix);
			String url = null;
			if (helpKey == null)
				url = Options.getHelp("index", prefix).replaceAll("\\{lang\\}", getLanguage());
			else
				url = helpKey.replaceAll("\\{lang\\}}", getLanguage());
			return (Options.getHelp("maindir", prefix) + url).replaceAll("\\{scheduler_home\\}", Options.getSchedulerHome()).replaceAll("\\{lang\\}",
					getLanguage());
		}
		catch (Exception e) {
			return null;
		}
	}

	public static String getDocHelpURL(String key) {
		return getHelpURL(key, "doc");
	}

	public static String getHelpURL(String key) {
		return getHelpURL(key, "editor");
	}

	public static String[] getBrowserExec(String url, String lang) {
		String os = System.getProperty("os.name").toLowerCase();
		String value = "";
		if (os.indexOf("windows") > -1)
			value = _properties.getProperty("editor.browser.windows");
		else
			value = _properties.getProperty("editor.browser.unix");
		url = url.replaceAll("file:/", "file://");
		value = value.replaceAll("\\{file\\}", url);
		value = value.replaceAll("\\{lang\\}", lang);
		return value.split("\\|");
	}

	public static String getSchemaVersion() {
		readSchemaVersion();// zum testen
		return _properties.getProperty("editor.schemaversion");
	}

	public static void readSchemaVersion() {
		try {
			// String schema = _properties.getProperty("editor.xml.xsd");
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(System.class.getResource(Options.getSchema()).toString());
			XPath x = XPath.newInstance("//xsd:documentation");
			List<Element> listOfElement = x.selectNodes(doc);
			if (!listOfElement.isEmpty()) {
				Element e = listOfElement.get(0);
				String version = e.getText();
				int pos1 = version.indexOf("$") + "$Id: ".length();
				int pos2 = version.indexOf("jz $");
				version = version.substring(pos1, pos2);
				_properties.put("editor.schemaversion", version);
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + ". could not read schema version from ", e);
			}
			catch (Exception ee) {
				// tu nichts
			}
		}
	}

	public static String getVersion() {
		return _properties.getProperty("editor.version");
	}

	public static String getSchema() {
		return _properties.getProperty("editor.xml.xsd");
	}

	public static String getActionSchema() {
		return _properties.getProperty("actions.xml.xsd");
	}

	public static boolean isValidate() {
		return _properties.getProperty("editor.xml.validate", "true").equalsIgnoreCase("true");
	}

	public static String getXSLT() {
		// return _properties.getProperty("editor.xml.xslt").replaceAll("\\{scheduler_home\\}", getSchedulerHome());
		return _properties.getProperty("editor.xml.xslt").replaceAll("\\{scheduler_data\\}", getSchedulerData().replaceAll("\\\\", "/"));
	}

	public static String getDocSchema() {
		return _properties.getProperty("documentation.xml.xsd");
	}

	public static String getActionsSchema() {
		return _properties.getProperty("actions.xml.xsd");
	}

	public static boolean isDocValidate() {
		return _properties.getProperty("documentation.xml.validate", "true").equalsIgnoreCase("true");
	}

	public static String getDocXSLT() {
		return _properties.getProperty("documentation.xml.xslt").replaceAll("\\{scheduler_data\\}", getSchedulerData().replaceAll("\\\\", "/"));
	}

	public static String getXHTMLSchema() {
		return _properties.getProperty("documentation.xhtml.xsd");
	}

	public static String getXSLTFilePrefix() {
		return _properties.getProperty("editor.xml.xslt.file.prefix", "scheduler_editor-");
	}

	public static String getXSLTFileSuffix() {
		return _properties.getProperty("editor.xml.xslt.file.suffix", "html");
	}

	public static String getBackupDir() {
		String s = _properties.getProperty("editor.backup.path", "");
		if (!s.endsWith("")) {
			s = s + "/";
		}
		return s;
	}

	public static boolean getBackupEnabled() {
		return (_properties.getProperty("editor.backup.enabled", "false").equalsIgnoreCase("true"));
	}

	public static String getLastDirectory() {
		return getSchedulerHotFolder();
		// return (_properties.getProperty("editor.file.lastopendir", ""));
	}

	public static void setLastDirectory(File f, sos.scheduler.editor.app.DomParser dom) {
		if (f != null && f.getParent() != null) {
			if (dom instanceof sos.scheduler.editor.conf.SchedulerDom && ((sos.scheduler.editor.conf.SchedulerDom) dom).isDirectory()) {
				setProperty("editor.file.lastopendir", f.getPath());
			}
			else {
				setProperty("editor.file.lastopendir", f.getParent());
			}
		}
	}

	public static void saveWindow(Shell shell, String name) {
		setProperty(name + ".window.left", String.valueOf(shell.getLocation().x));
		setProperty(name + ".window.top", String.valueOf(shell.getLocation().y));
		setProperty(name + ".window.width", String.valueOf(shell.getSize().x));
		setProperty(name + ".window.height", String.valueOf(shell.getSize().y));
		setProperty(name + ".window.status", String.valueOf(shell.getMaximized()));
	}

	public static void loadWindow(Shell shell, String name) {
		Point location = new Point(0, 0);
		Point size = new Point(0, 0);
		try {
			String left = _properties.getProperty(name + ".window.left");
			String top = _properties.getProperty(name + ".window.top");
			if (left != null && Utils.isNumeric(left) && top != null && Utils.isNumeric(top)) {
				location.x = new Integer(left).intValue();
				location.y = new Integer(top).intValue();
				shell.setLocation(location);
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
		}
		try {
			Boolean b = new Boolean(_properties.getProperty(name + ".window.status"));
			shell.setMaximized(b.booleanValue());
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
		}
		try {
			String width = _properties.getProperty(name + ".window.width");
			String height = _properties.getProperty(name + ".window.height");
			if (width != null && Utils.isNumeric(width) && height != null && Utils.isNumeric(height)) {
				size.x = new Integer(_properties.getProperty(name + ".window.width")).intValue();
				size.y = new Integer(_properties.getProperty(name + ".window.height")).intValue();
				shell.setSize(size);
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
		}
	}

	public static void saveSash(String name, int[] sash) {
		setProperty(name + ".sash.layout", sash[0] + "," + sash[1]);
	}

	public static void loadSash(String name, SashForm sash) {
		try {
			String value = _properties.getProperty(name + ".sash.layout");
			if (value != null) {
				String[] values = value.split(",");
				int[] weights = { new Integer(values[0].trim()).intValue(), new Integer(values[1].trim()).intValue() };
				sash.setWeights(weights);
			}
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			// e.printStackTrace();
			System.err.println("No properties found for sash '" + name + "'!");
		}
	}

	private static String getProp(final String pstrPropertyName, final String pstrDefaultValue) {
		String strT = System.getProperty(pstrPropertyName);
		if (strT != null && strT.length() > 0) {
			if (strT.startsWith("%") && strT.endsWith("%")) {
				String strV = strT.substring(1, strT.length() - 1);
				String strW = System.getenv(strV);
				if (strW != null) {
					strT = strW;
				}
				else {
					strT = pstrDefaultValue;
				}
			}
		}
		else {
			strT = pstrDefaultValue;
		}

		return strT;
	}

	public static String getSchedulerHome() {
		return getProp("SCHEDULER_HOME", "");
	}

	public static String getSchedulerData() {
		return getProp("SCHEDULER_DATA", getSchedulerHome());
	}

	public static String getSchedulerNormalizedHome() {
		String home = Options.getSchedulerHome();
		home = new File(home).getAbsolutePath() + "/";
		home = home.replaceAll("\\\\", "/");
		return home;
	}

	public static String getSchedulerNormalizedData() {
		String data = Options.getSchedulerData();
		data = new File(data).getAbsolutePath() + "/";
		data = data.replaceAll("\\\\", "/");
		return data;
	}

	public static String getSchedulerHotFolder() {
		String sdata = getSchedulerData();
		sdata = sdata.endsWith("/") || sdata.endsWith("\\") ? sdata : sdata + "/";
		String strData = sdata + "config/live/";
		return getProp("SCHEDULER_HOT_FOLDER", strData);

	}

	public static String getSchedulerNormalizedHotFolder() {
		String sdata = Options.getSchedulerHotFolder();
		sdata = new File(sdata).getAbsolutePath() + "/";
		sdata = sdata.replaceAll("\\\\", "/");
		return sdata;
	}

	public static Color getRequiredColor() {
		try {
			int r = new Integer(_properties.getProperty("required.color.r")).intValue();
			int g = new Integer(_properties.getProperty("required.color.g")).intValue();
			int b = new Integer(_properties.getProperty("required.color.b")).intValue();
			return ResourceManager.getColor(r, g, b);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			return ResourceManager.getColor(255, 255, 210);
		}
	}

	public static Color getLightBlueColor() {
		try {
			return ResourceManager.getColor(224, 255, 255);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			return ResourceManager.getColor(255, 255, 187);
		}
	}

	public static Color getLightYellow() {
		try {
			int r = 255;
			int g = 255;
			int b = 187;
			return ResourceManager.getColor(r, g, b);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			return ResourceManager.getColor(255, 255, 187);
		}
	}

	public static Color getBlueColor() {
		try {
			int r = 0;
			int g = 0;
			int b = new Integer(_properties.getProperty("required.color.b")).intValue();
			return ResourceManager.getColor(r, g, b);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			return ResourceManager.getColor(255, 255, 210);
		}
	}

	public static Color getRedColor() {
		try {
			int r = new Integer(_properties.getProperty("required.color.r")).intValue();
			int g = 0;
			int b = 0;
			return ResourceManager.getColor(r, g, b);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			return ResourceManager.getColor(255, 255, 219);
		}
	}

	public static Color getWhiteColor() {
		try {
			int r = 255;
			int g = 255;
			int b = 255;
			return ResourceManager.getColor(r, g, b);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			e.printStackTrace();
			return ResourceManager.getColor(255, 255, 219);
		}
	}

	public static boolean isShowWizardInfo() {
		String s = _properties.getProperty("editor.job.wizard.info.show");
		if (s != null && s.trim().length() > 0) {
			_showWizardInfo = s.equals("true");
		}
		return _showWizardInfo;
	}

	public static void setShowWizardInfo(boolean wizardInfo) {
		_showWizardInfo = wizardInfo;
		_properties.setProperty("editor.job.wizard.info.show", wizardInfo ? "true" : "false");
	}

	public static boolean getPropertyBoolean(String name) {
		String s = _properties.getProperty(name);
		if (s == null)
			return true;
		return s.equalsIgnoreCase("true");
	}

	public static void setPropertyBoolean(String name, boolean value) {
		_properties.setProperty(name, value ? "true" : "false");
	}

	public static String getDetailXSLT() {
		return _properties.getProperty("detail.editor.xslt");
	}

	public static String getProperty(String key) {
		return _properties.getProperty(key);
	}

	public static String[] getJobTitleList() {
		if (jobTitleList != null)
			return jobTitleList;
		else
			return new String[] {};
	}

	public static void setJobTitleList(String[] jobTitleList) {
		Options.jobTitleList = jobTitleList;
	}

	public static HashMap getHolidaysDescription() {
		if (holidaysDescription != null)
			return holidaysDescription;
		else
			return new HashMap();
	}

	public static void setHolidaysDescription(HashMap holidaysDescription) {
		Options.holidaysDescription = holidaysDescription;
	}

	public static String[] getPropertiesWithPrefix(String prefix) {
		String[] retVal = null;
		String s = "";
		Properties p = new Properties();
		p.putAll(_defaults);
		p.putAll(_properties);
		// 9. Folgende Monitore sollen in der Auslieferung im Lieferumfang sein
		// configuration_monitor -->sos.scheduler.managed.configuration.ConfigurationOrderMonitor
		// create_event_monitor --> sos.scheduler.jobs.JobSchedulerSubmitEventMonitor
		if (prefix.equalsIgnoreCase("monitor_favorite_")) {
			if (!p.containsKey("monitor_favorite_java_configuration_monitor")) {
				p.put("monitor_favorite_java_configuration_monitor", "sos.scheduler.managed.configuration.ConfigurationOrderMonitor");
				setProperty("monitor_favorite_java_configuration_monitor", "sos.scheduler.managed.configuration.ConfigurationOrderMonitor");
			}
			if (!p.containsKey("monitor_favorite_java_create_event_monitor")) {
				p.put("monitor_favorite_java_create_event_monitor", "sos.scheduler.job.JobSchedulerSubmitEventMonitor");
				setProperty("monitor_favorite_java_create_event_monitor", "sos.scheduler.job.JobSchedulerSubmitEventMonitor");
			}
		}
		java.util.Iterator keys = p.keySet().iterator();
		while (keys.hasNext()) {
			Object key = keys.next();
			if (key != null && key.toString().length() > 0 && key.toString().startsWith(prefix))
				s = key.toString().substring(prefix.length()) + ";" + s;
		}
		retVal = s.split(";");
		return retVal;
	}

	public static void removeProperty(String name) {
		if (name != null && name.length() > 0) {
			_properties.remove(name);
			saveProperties();
		}
	}
}
