package sos.scheduler.editor.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class Options {

	public static final String DEFAULT_OPTIONS = "/sos/scheduler/editor/options.properties";

	private static Properties _defaults;
	
	private static Properties _properties;

	private static boolean _changed = false;

	private Options() {

	}

	public static String loadOptions(Class cl) {
		String fName="";
		try {
			_defaults = new Properties();
			_defaults.load(cl.getResourceAsStream(DEFAULT_OPTIONS));

			_properties = new Properties(_defaults);
		} catch (Exception e) {
			return "Error reading default options from " + DEFAULT_OPTIONS
					+ ": " + e.getMessage();
		}


		try {
			fName = getDefault("editor.options.file").replaceAll("\\{scheduler_home\\}" ,getSchedulerHome()); 
			File file = new File(fName);
			if (file.exists()) {
				FileInputStream fi = new FileInputStream(fName);
				_properties.load(fi);
				fi.close();
			}
		} catch (Exception e) {
			return "Error reading custom options from " + fName + ": "
					+ e.getMessage();
		}

		return null;
	}

	public static String saveProperties() {
		if (_properties != null && _changed) {
			try {
				FileOutputStream fo = new FileOutputStream(getDefault("editor.options.file").replaceAll("\\{scheduler_home\\}" ,getSchedulerHome()));
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
		return _properties.getProperty("editor.xml.xslt").replaceAll("\\{scheduler_home\\}",getSchedulerHome());
	}
	
	public static String getXSLTFilePrefix() {
		return _properties.getProperty("editor.xml.xslt.file.prefix", "scheduler_editor-");
	}
	
	public static String getXSLTFileSuffix() {
		return _properties.getProperty("editor.xml.xslt.file.suffix", "html");
	}
	
	public static void saveWindow(Shell shell){
		setProperty("editor.window.left", String.valueOf(shell.getLocation().x));
		setProperty("editor.window.top", String.valueOf(shell.getLocation().y));
		setProperty("editor.window.width", String.valueOf(shell.getSize().x));
		setProperty("editor.window.height", String.valueOf(shell.getSize().y));
		setProperty("editor.window.status", String.valueOf(shell.getMaximized()));
	}

	public static void loadWindow(Shell shell){
		Point location = new Point(0,0);
		Point size = new Point(0,0);
	 
		try{
		   location.x = new Integer(_properties.getProperty("editor.window.left")).intValue();
		   location.y = new Integer(_properties.getProperty("editor.window.top")).intValue();
       shell.setLocation(location);
		}catch (Exception e){e.printStackTrace();}
		
		try{
   		size.x = new Integer(_properties.getProperty("editor.window.width")).intValue();
	  	size.y = new Integer(_properties.getProperty("editor.window.height")).intValue();
      shell.setSize(size);
		}catch (Exception e){e.printStackTrace();}
		
		try{
   		Boolean b = new Boolean(_properties.getProperty("editor.window.status"));
	  	shell.setMaximized(b.booleanValue());
		}catch (Exception e){e.printStackTrace();}
	}

  public static void saveSash(String name, int[] sash){
  	
  	setProperty(name + ".sash.layout",sash[0] + "," + sash[1]);
  }
  
  public static void loadSash(String name, SashForm sash){
  	try{
  		String value = _properties.getProperty(name + ".sash.layout");
  		String[] values = value.split(",");
  		int[] weights = {new Integer(values[0].trim()).intValue(), new Integer(values[1].trim()).intValue()};
  		
  		sash.setWeights(weights);
		}catch (Exception e){e.printStackTrace();}
  	}
  
  public static String getSchedulerHome() {
  	if (System.getProperty("SCHEDULER_HOME") != null){
  		return System.getProperty("SCHEDULER_HOME");
  	}else {
  		return "";
  	}
 	 }
}
