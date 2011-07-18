package sos.scheduler.editor.app;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
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

public class Options {
	public static final String	conPropertyEDITOR_OPTIONS_FILE	= "editor.options.file";
	public static final String	conPropertyEDITOR_LANGUAGE		= "editor.language";
	@SuppressWarnings("unused")
	private final static String	conClassName					= "Options";
	@SuppressWarnings("unused")
	private final String		conSVNVersion					= "$Id$";
	private static final Logger	logger							= Logger.getLogger(Options.class);
	public static final String	DEFAULT_OPTIONS					= "/sos/scheduler/editor/options.properties";
	private static Properties	_defaults						= null;
	private static Properties	_properties						= null;
	private static boolean		_changed						= false;
	private static boolean		_showWizardInfo					= true;
	private static String[]		jobTitleList					= null;
	private static HashMap		holidaysDescription				= null;

	private Options() {
	}

	public static String getDefaultOptionFilename() {
		// return getDefault("editor.options.file").replaceAll("\\{scheduler_home\\}", getSchedulerHome());
		return getDefault(conPropertyEDITOR_OPTIONS_FILE).replaceAll("\\{scheduler_data\\}", getSchedulerData().replaceAll("\\\\", "/"));
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
				_properties.store(fo, "--JOE - SOSJobScheduler Object Editor - Options--");
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

	public static String getLanguage() {
		getProperties();
		return _properties.getProperty(conPropertyEDITOR_LANGUAGE);
	}

	public static String getDefault(String key) {
		return _defaults.getProperty(key);
	}

	public static void setLanguage(String language) {
		setProperty(conPropertyEDITOR_LANGUAGE, language);
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

	public static String getSchedulerHome() {
		if (System.getProperty("SCHEDULER_HOME") != null) {
			return System.getProperty("SCHEDULER_HOME");
		}
		else {
			return "";
		}
	}

	public static String getSchedulerData() {
		if (System.getProperty("SCHEDULER_DATA") != null) {
			return System.getProperty("SCHEDULER_DATA");
		}
		else {
			return getSchedulerHome();
		}
	}

	public static String getSchedulerNormalizedHome() {
		String home = Options.getSchedulerHome();
		home = home.endsWith("/") || home.endsWith("\\") ? home : home + "/";
		home = home.replaceAll("\\\\", "/");
		return home;
	}

	public static String getSchedulerNormalizedData() {
		String data = Options.getSchedulerData();
		data = data.endsWith("/") || data.endsWith("\\") ? data : data + "/";
		data = data.replaceAll("\\\\", "/");
		return data;
	}

	public static String getSchedulerHotFolder() {
		if (System.getProperty("SCHEDULER_HOT_FOLDER") != null && System.getProperty("SCHEDULER_HOT_FOLDER").length() > 0) {
			return System.getProperty("SCHEDULER_HOT_FOLDER");
		}
		else {
			String sdata = getSchedulerData();
			sdata = sdata.endsWith("/") || sdata.endsWith("\\") ? sdata : sdata + "/";
			return sdata + "config/live/";
		}
	}

	public static String getSchedulerNormalizedHotFolder() {
		String sdata = Options.getSchedulerHotFolder();
		sdata = sdata.endsWith("/") || sdata.endsWith("\\") ? sdata : sdata + "/";
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
