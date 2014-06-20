package sos.scheduler.editor.conf.listeners;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.CDATA;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ScriptListener extends JOEListener {
	public final static int			NONE				= -1;
	public final static String[]	_languagesJob		= { "shell", "java", "javascript", "VBScript", "perlScript", "" };
	public final static String[]	_languagesMonitor	= { "java", "javascript", "VBScript", "perlScript", "" };
	public String[]					_languages			= null;
	private SchedulerDom			_dom				= null;
	private Element					_parent				= null;
	private Element					_script				= null;
	private int						_type				= -1;
	private ISchedulerUpdate		_update				= null;

	public ScriptListener(SchedulerDom dom, Element parent, int type, ISchedulerUpdate update) {
		_dom = dom;
		_parent = parent;
		_type = type; 
		if (type == JOEConstants.MONITOR) {
			_languages = _languagesMonitor;
		}
		else {
			_languages = _languagesJob;
		}
		_update = update;
		setScript();
	}

	private void setScript() {
		if (_type == JOEConstants.MONITOR) {
			// Element monitor = _parent.getChild("monitor");
			Element monitor = _parent;
			if (monitor != null) {
				_script = monitor.getChild("script");
				// if (_script == null) monitor.detach();
			}
		}
		else
			_script = _parent.getChild("script");
	}

	public int languageAsInt(String language) {
		for (int i = 0; i < _languages.length; i++) {
			if (_languages[i].equalsIgnoreCase(language))
				return i;
		}
		if (_script != null)
			_script.setAttribute("language", "java");
		return 0;
	}

	private String languageAsString(int language) {
		return _languages[language];
	}

	public String getLanguage(int language) {
		return _languages[language];
	}

	public int getLanguage() {
		if (_script != null)
			return languageAsInt(_script.getAttributeValue("language"));
		else
			return NONE;
	}

	public String getLanguageAsString(int language) {
		if (_script != null)
			return languageAsString(language);
		else {
			return "";
		}
	}

	public void setLanguage(final String pstrLanguage) {
		this.setLanguage(languageAsInt(pstrLanguage));
	}

	public void setLanguage(int language) {
		setScript();
		if (_script == null && language != NONE) {
			// init script element
			_script = new Element("script");
			if (_type == JOEConstants.MONITOR) {
				// Element monitor = _parent.getChild("monitor");
				Element monitor = _parent;
				if (monitor == null) {
					monitor = new Element("monitor");
					_parent.addContent(monitor);
				}
				monitor.addContent(_script);
			}
			else
				_parent.addContent(_script);
		}
		if (_script != null) {
			if (!isJava()) {
				_script.removeAttribute("java_class");
				_script.removeAttribute("java_class_path");
			}
			if (language != NONE)
				Utils.setAttribute("language", languageAsString(language), _script, _dom);
			_dom.setChanged(true);
			setChangedForDirectory();
		}
	}

	private void setAttributeValue(String element, String value, int language) {
		if (getLanguage() == language) {
			_script.setAttribute(element, value);
			_dom.setChanged(true);
			setChangedForDirectory();
		}
	}

	public String getJavaClass() {
		return Utils.getAttributeValue("java_class", _script);
	}

	public void setJavaClass(String javaClass) {
		setAttributeValue("java_class", javaClass.trim(), languageAsInt("java"));
		setChangedForDirectory();
	}

	public String getComClass() {
		return Utils.getAttributeValue("com_class", _script);
	}

	public String getFilename() {
		return Utils.getAttributeValue("filename", _script);
	}

	public void setClasspath(String classpath) {
		setAttributeValue("java_class_path", classpath, languageAsInt("java"));
	}

	public String getClasspath() {
		String s = Utils.getAttributeValue("java_class_path", _script);
		return s;
	}

	public void fillTable(Table table) {
		if (_script != null && table != null) {
			table.removeAll();
			List includeList = _script.getChildren("include");
			for (int i = 0; i < includeList.size(); i++) {
				Element include = (Element) includeList.get(i);
				if (include.getAttributeValue("file") != null) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, Utils.getAttributeValue("file", include));
					item.setText(1, "file");
				}
				else {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, Utils.getAttributeValue("live_file", include));
					item.setText(1, "live_file");
				}
			}
		}
	}

	public String getIncludesAsString() {
		String retVal = "";
		String[] inc = getIncludes();
		for (int i = 0; i < inc.length; i++) {
			if (inc[i] != null)
				retVal = inc[i] + ";" + retVal;
		}
		return retVal;
	}

	public String[] getIncludes() {
		if (_script != null) {
			List includeList = _script.getChildren("include");
			String[] includes = new String[includeList.size()];
			Iterator it = includeList.iterator();
			int i = 0;
			while (it.hasNext()) {
				Element include = (Element) it.next();
				String file = "";
				if (include.getAttribute("live_file") != null)
					file = include.getAttributeValue("live_file");
				else
					file = include.getAttributeValue("file");
				includes[i++] = file == null ? "" : file;
			}
			return includes;
		}
		else
			return new String[0];
	}

	// Aus der Tabelle werden die includes für die Scripte generiert-
	public void addIncludesFromTable(Table table, java.util.HashMap inc) {
		if (_script != null) {
			Iterator it = inc.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				String val = inc.get(key) != null && inc.get(key).equals("live_file") ? "live_file" : "file";
				Element include = new Element("include");
				include.setAttribute(val, key);
				_script.addContent(include);
			}
		}
	}

	public void addInclude(Table table, String filename, boolean isLife) {
		if (_script != null) {
			List includes = _script.getChildren("include");
			if (table.getSelectionCount() > 0) {
				Element in = (Element) _script.getChildren("include").get(table.getSelectionIndex());
				in.setAttribute((isLife ? "live_file" : "file"), filename);
			}
			else {
				_script.addContent(includes.size(), new Element("include").setAttribute((isLife ? "live_file" : "file"), filename));
			}
			_dom.setChanged(true);
			fillTable(table);
			setChangedForDirectory();
		}
		else {
			MainWindow.message("No script element defined!", SWT.ICON_ERROR);
			System.out.println("no script element defined!");
		}
	}

	public void addInclude(String filename) {
		if (_script != null) {
			List includes = _script.getChildren("include");
			_script.addContent(includes.size(), new Element("include").setAttribute("file", filename));
			_dom.setChanged(true);
			setChangedForDirectory();
		}
		else {
			MainWindow.message("No script element defined!", SWT.ICON_ERROR);
			System.out.println("no script element defined!");
		}
	}

	/*  private void removeScriptSource() {
	    String includes[] = getIncludes();

	    _script.removeContent();

	    for (int i = 0; i < includes.length; i++) {
	      addInclude(includes[i]);
	     }
	  }
	*/
	public void removeInclude(int index) {
		if (_script != null) {
			List includeList = _script.getChildren("include");
			if (index >= 0 && index < includeList.size()) {
				includeList.remove(index);
				_dom.setChanged(true);
				setChangedForDirectory();
			}
			else
				System.out.println("index " + index + " is out of range for include!");
		}
		else {
			MainWindow.message("No script element defined!", SWT.ICON_ERROR);
			System.out.println("no script element defined!");
		}
	}

	public void removeIncludes() {
		if (_script != null) {
			_script.removeChildren("include");
		}
	}

	@Override public String getSource() {
		if (_script != null) {
			return _script.getTextTrim();
		}
		else
			return "";
	}

	/*public void deleteScript() {
	  //    if (_script != null) 	_script.removeContent();
	  if (_script != null) 	removeScriptSource();
	}*/
	@Override public void setSource(String source) {
		try {
			if (_script != null) {
				List l = _script.getContent();
				for (int i = 0; i < l.size(); i++) {
					if (l.get(i) instanceof CDATA)
						l.remove(i);
				}
				if (!source.equals("")) {
					_script.addContent(new CDATA(source));
				}
				_dom.setChanged(true);
				setChangedForDirectory();
			}
			else {
				MainWindow.message("No script element defined!", SWT.ICON_ERROR);
				System.out.println("no script element defined!");
			}
		}
		catch (org.jdom.IllegalDataException jdom) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), jdom);
			}
			catch (Exception ee) {
				// tu nichts
			}
			MainWindow.message(jdom.getMessage(), SWT.ICON_ERROR);
		}
		catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
			System.out.println(e);
		}
	}

	public Element getParent() {
		return _parent;
	}

	public String getName() {
		return Utils.getAttributeValue("name", _parent);
	}

	public void setMonitorName(final String name) {
		this.setName(name);
	}

	@Override public String getPrePostProcessingScriptSource() {
		String strT = "";
		return strT;
	}

	public void setName(String name) {
		Utils.setAttribute("name", name, _parent);
		if (_update != null)
			_update.updateTreeItem(name);
		_dom.setChanged(true);
		setChangedForDirectory();
	}

	public String getOrdering() {
		return Utils.getAttributeValue("ordering", _parent);
	}

	public void setOrdering(String ordering) {
		Utils.setAttribute("ordering", ordering, "0", _parent);
		setChangedForDirectory();
	}

	private void setChangedForDirectory() {
		if (_dom.isDirectory() || _dom.isLifeElement()) {
			if (_parent != null) {
				Element job = _parent;
				if (!job.getName().equals(_parent))
					job = Utils.getJobElement(_parent);
				_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", job), SchedulerDom.MODIFY);
			}
		}
	}

	/**
	 * @return the _dom
	 */
	public SchedulerDom getDom() {
		return _dom;
	}

	public boolean isJava() {
		return languageAsString(getLanguage()).equalsIgnoreCase("java");
	}
}
