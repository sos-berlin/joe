package sos.scheduler.editor.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Options {

	public static final String OPTIONS_FILE = "../conf/options.properties";

	public static final String DEFAULT_OPTIONS = "/sos/scheduler/editor/options.properties";

	private static Properties _defaults;
	
	private static Properties _properties;

	private static boolean _changed = false;

	private Options() {

	}

	public static String loadOptions(Class cl) {
		try {
			_defaults = new Properties();
			_defaults.load(cl.getResourceAsStream(DEFAULT_OPTIONS));

			_properties = new Properties(_defaults);
		} catch (Exception e) {
			return "Error reading default options from " + DEFAULT_OPTIONS
					+ ": " + e.getMessage();
		}

		try {
			File file = new File(OPTIONS_FILE);
			if (file.exists()) {
				FileInputStream fi = new FileInputStream(OPTIONS_FILE);
				_properties.load(fi);
				fi.close();
			}
		} catch (Exception e) {
			return "Error reading custom options from " + OPTIONS_FILE + ": "
					+ e.getMessage();
		}

		return null;
	}

	public static String saveProperties() {
		if (_properties != null && _changed) {
			try {
				FileOutputStream fo = new FileOutputStream(OPTIONS_FILE);
				_properties.store(fo, "--Job Scheduler Editor Options--");
				fo.close();
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
		}
		return null;
	}

	private static void setProperty(String key, String value) {
		_properties.setProperty(key, value);
		_changed = true;
	}

	public static String getDefault(String key) {
		return _defaults.getProperty(key);
	}
	
	public static String getLanguage() {
		return _properties.getProperty("editor.language");
	}

	public static void setLanguage(String language) {
		setProperty("editor.language", language);
	}

	public static String getHelpURL(String key) {
		try {
			String url = _properties.getProperty("editor.help.url." + key);
			return url != null && !url.equals("") ? url : null;
		} catch(Exception e) {
			return null;
		}
	}
	
	public static String[] getBrowserExec(String url, String lang) {
		String os = System.getProperty("os.name").toLowerCase();
		String value = "";
		
		if(os.indexOf("windows") > -1)
			value = _properties.getProperty("editor.browser.windows");
		else
			value = _properties.getProperty("editor.browser.unix");
		
		value = value.replaceAll("\\{file\\}", url);
		value = value.replaceAll("\\{lang\\}", lang);
		return value.split("\\|");
	}
	
	public static String getVersion() {
		return _properties.getProperty("editor.version");
	}
	
	public static String getSchema() {
		return _properties.getProperty("editor.xml.xsd");
	}
	
	public static boolean isValidate() {
		return _properties.getProperty("editor.xml.validate", "true").equalsIgnoreCase("true");
	}
	
	public static String getXSLT() {
		return _properties.getProperty("editor.xml.xslt");
	}
	
	public static String getXSLTFilePrefix() {
		return _properties.getProperty("editor.xml.xslt.file.prefix", "scheduler_editor-");
	}
	
	public static String getXSLTFileSuffix() {
		return _properties.getProperty("editor.xml.xslt.file.suffix", "html");
	}
}
