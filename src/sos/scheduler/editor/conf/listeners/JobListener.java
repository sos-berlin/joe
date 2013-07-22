package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.CDATA;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;

public class JobListener extends JOEListener {

	@SuppressWarnings("unused")
	private final String			conSVNVersion		= "$Id$";

	private static Logger			logger				= Logger.getLogger(JobListener.class);
	@SuppressWarnings("unused")
	private final String			conClassName		= "JobListener";

	private static String			library				= "";

	public final  String[]	_languagesJob		= { "shell", "java", "javascript", "VBScript", "perlScript", "javax.script:rhino", "" };
	public final  String[]	_languagesMonitor	= { "java", "javascript", "VBScript", "perlScript", "javax.script:rhino", "" };
	public String[]					_languages			= null;

	private Element					_script				= null;
	private Element					_settings			= null;
	private Element					_process			= null;
	private Element					_environment		= null;

	private int						_type				= -1;

	private List<Element>			_directories		= null;
	private Element					_directory			= null;
	private List					_setbacks			= null;
	private Element					_setback			= null;
	private List					_errorDelays		= null;
	private Element					_errorDelay			= null;

	public JobListener(SchedulerDom dom, Element job, ISchedulerUpdate update) {

		_dom = dom;
		_job = job;
		_parent = _job;
		objElement = _job;
 		_main = update;

		_directories = _job.getChildren("start_when_directory_changed");
		_setbacks = _job.getChildren("delay_order_after_setback");
		_errorDelays = _job.getChildren("delay_after_error");

		_settings = _job.getChild("settings");

		setScript();

	}

	public String getFile() {
		return Utils.getAttributeValue("file", _process);
	}

	public void setFile(String file) {
		if (_job.getChild("script") != null) {
			int c = sos.scheduler.editor.app.MainWindow.message("Do you want really remove script and put new Run Executable File?", SWT.YES | SWT.NO
					| SWT.ICON_WARNING);
			if (c != SWT.YES)
				return;

		}

		initProcess();
		Utils.setAttribute("file", file, _process, _dom);
		Utils.setChangedForDirectory(_job, _dom);
	}

	public String getParam() {
		return Utils.getAttributeValue("param", _process);
	}

	public void setParam(String param) {
		initProcess();
		Utils.setAttribute("param", param, _process, _dom);
		Utils.setChangedForDirectory(_job, _dom);
	}

	public String getLogFile() {
		return Utils.getAttributeValue("log_file", _process);
	}

	public void setLogFile(String file) {
		initProcess();
		Utils.setAttribute("log_file", file, _process, _dom);
		Utils.setChangedForDirectory(_job, _dom);
	}

	public boolean isIgnoreSignal() {
		return Utils.isAttributeValue("ignore_signal", _process);
	}

	public void setIgnoreSignal(boolean ignore) {
		Utils.setAttribute("ignore_signal", ignore, _process, _dom);
		Utils.setChangedForDirectory(_job, _dom);
	}

	public boolean isIgnoreError() {
		return Utils.isAttributeValue("ignore_error", _process);
	}

	public void setIgnoreError(boolean ignore) {
		Utils.setAttribute("ignore_error", ignore, _process, _dom);
		Utils.setChangedForDirectory(_job, _dom);
	}

	private void setProcess() {
		_process = _job.getChild("process");

		_environment = _process != null ? _process.getChild("environment") : null;

	}

	private void initProcess() {
		if (_process == null) {
			_job.addContent(new Element("process").addContent(new Element("environment")));
			_job.removeChild("script");
			setProcess();
			_dom.setChanged(true);
		}
	}

	public boolean isShell() {
		// String strLang = getLanguageAsString(getLanguage());
		// return strLang.equalsIgnoreCase("shell");
		return (getLanguage() == 0);
	}

	private void setScript() {
		if (_type == Editor.MONITOR) {
			Element monitor = _job;
			if (monitor != null) {
				_script = monitor.getChild("script");
			}
		}
		else
			_script = _job.getChild("script");
	}

	public String getJobNameAndTitle() {
		String strT =  this.getJobName();
		if (this.isDisabled()== true) {
			strT += " (" + sos.scheduler.editor.app.Messages.getLabel("disabled") + ")";
		}
		strT += " - " + this.getTitle();
		return strT; 
	}

	public String[] getLanguages() {
		return _languages;
	}

	public String getComment() {
		return Utils.getAttributeValue("__comment__", _job);
	}

	public void setComment(String comment) {
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		Utils.setAttribute("__comment__", comment, _job, _dom);
	}

	public boolean isDisabled() {
		boolean disabled = (!Utils.getAttributeValue("enabled", _job).equalsIgnoreCase("yes"))
				&& (!Utils.getAttributeValue("enabled", _job).equalsIgnoreCase(""));
		return disabled;
	}

	public String getJobName() {
		return Utils.getAttributeValue("name", _job);
	}

	public void setJobName(String name, boolean updateTree) {

		String removename = Utils.getAttributeValue("name", _job);
		Utils.setAttribute("name", name, _job, _dom);
		if (_dom.isChanged() && ((_dom.isDirectory() && !Utils.existName(removename, _job, "job")) || _dom.isLifeElement()))
			_dom.setChangedForDirectory("job", removename, SchedulerDom.DELETE);

		if (updateTree)
			_main.updateJob(name);

		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);

	}

	public String getTitle() {
		return Utils.getAttributeValue("title", _job);
	}

	public void setTitle(String title) {
		Utils.setAttribute("title", title, _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getSpoolerID() {
		return Utils.getAttributeValue("spooler_id", _job);
	}

	public void setSpoolerID(String spoolerID) {
		Utils.setAttribute("spooler_id", spoolerID, _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getProcessClass() {
		String strT = Utils.getAttributeValue("process_class", _job);
		if (strT == null) {
			strT = "";
		}
		return strT;
	}

	public void setProcessClass(String processClass) {
		Utils.setAttribute("process_class", processClass, _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public boolean getOrder() {
		String order = _job.getAttributeValue("order");
		return order == null ? false : order.equalsIgnoreCase("yes");
	}

	public boolean isOrderJob() {
		return getOrder();
	}

	public boolean getStopOnError() {
		String stopOnError = _job.getAttributeValue("stop_on_error");
		return stopOnError == null ? true : stopOnError.equalsIgnoreCase("yes");
	}

	public boolean getForceIdletimeout() {
		String forceIdleTimeout = _job.getAttributeValue("force_idle_timeout");
		return forceIdleTimeout == null ? false : forceIdleTimeout.equalsIgnoreCase("yes");
	}
	
	

	public void setOrder(boolean order) {
		if (order) {
			_job.setAttribute("order", "yes");
			_job.removeAttribute("priority");
			if (_job.getChild("run_time") == null)
				_job.addContent(new Element("run_time").setAttribute("let_run", "no"));
		}
		else {
			_job.removeAttribute("order");
		}
		_dom.setChanged(true);
		if (_main != null)
            _main.updateJob(this.getJobName());

	 
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getPriority() {
		return Utils.getAttributeValue("priority", _job);
	}

	public void setPriority(String priority) {
		Utils.setAttribute("priority", priority, _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getJavaOptions() {
		return Utils.getAttributeValue("java_options", _job);
	}

	public void setJavaOptions(String javaOptions) {
		Utils.setAttribute("java_options", javaOptions, _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getTasks() {
		return Utils.getAttributeValue("tasks", _job);
	}

	public void setTasks(String tasks) {
		Utils.setAttribute("tasks", Utils.getIntegerAsString(Utils.str2int(tasks)), _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getTimeout() {
		return Utils.getAttributeValue("timeout", _job);
	}

	public void setTimeout(String timeout) {
		Utils.setAttribute("timeout", timeout, _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getIdleTimeout() {
		return Utils.getAttributeValue("idle_timeout", _job);
	}

	public void setIdleTimeout(String idleTimeout) {
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		Utils.setAttribute("idle_timeout", idleTimeout, _job, _dom);
	}

	public void setForceIdletimeout(boolean forceIdleTimeout) {
		if (forceIdleTimeout) {
			Utils.setAttribute("force_idle_timeout", "yes", _job, _dom);
		}
		else {
			Utils.setAttribute("force_idle_timeout", "no", "no", _job, _dom);
		}
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public void setStopOnError(boolean stopOnError) {
		if (stopOnError) {
			Utils.setAttribute("stop_on_error", "yes", "yes", _job, _dom);
		}
		else {
			Utils.setAttribute("stop_on_error", "no", _job, _dom);
		}
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public void setReplace(boolean replace) {
		if (replace) {
			Utils.setAttribute("replace", "yes", "yes", _job, _dom);
		}
		else {
			Utils.setAttribute("replace", "no", "yes", _job, _dom);
		}
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public boolean getReplace() {
		String replace = _job.getAttributeValue("replace");
		return replace == null ? true : replace.equalsIgnoreCase("yes");
	}

	public void setTemporary(boolean temporary) {
		if (temporary) {
			Utils.setAttribute("temporary", "yes", "no", _job, _dom);
		}
		else {
			Utils.setAttribute("temporary", "no", "no", _job, _dom);
		}
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public boolean getTemporary() {
		String temporary = _job.getAttributeValue("temporary");
		return temporary == null ? false : temporary.equalsIgnoreCase("yes");

	}

	public void setMintasks(String mintasks) {
		Utils.setAttribute("min_tasks", Utils.getIntegerAsString(Utils.str2int(mintasks)), _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getMintasks() {
		return Utils.getAttributeValue("min_tasks", _job);
	}

	public void setVisible(String visible) {
		Utils.setAttribute("visible", visible, _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getVisible() {
		return Utils.getAttributeValue("visible", _job);
	}

	public String[] getProcessClasses() {
		String[] names = null;
		if (_dom.getRoot().getName().equalsIgnoreCase("spooler")) {
			Element classes = _dom.getRoot().getChild("config").getChild("process_classes");
			if (classes != null) {
				List list = classes.getChildren("process_class");
				names = new String[list.size()];
				int i = 0;
				Iterator it = list.iterator();
				while (it.hasNext()) {
					Object o = it.next();
					if (o instanceof Element) {
						String name = ((Element) o).getAttributeValue("name");
						if (name == null)
							name = "";
						names[i++] = name;
					}
				}
			}
		}
		if (names == null) {
			names = new String[] { "" };
		}
		return names;
	}

	public String getDescription() {
		Element desc = _job.getChild("description");
		if (desc != null) {
			return desc.getTextTrim();
		}
		else
			return "";
	}

	public void setDescription(String description) {
		Element desc = _job.getChild("description");
		String f = getInclude();

		if (desc == null && !description.equals("")) {
			desc = new Element("description");
			_job.addContent(0, desc);
		}

		if (desc != null) {
			if (description.equals("") && (f == null || f.equals(""))) {
				_job.removeChild("description");
				_dom.setChanged(true);
				if (_dom.isDirectory() || _dom.isLifeElement())
					_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
				return;
			}

			desc.removeContent();
			if (!(f == null || f.equals(""))) {
				setInclude(f);
			}
			desc.addContent(new CDATA(description));
			_dom.setChanged(true);
			if (_dom.isDirectory() || _dom.isLifeElement())
				_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		}
	}

	public String getInclude() {

		Element desc = _job.getChild("description");
		if (desc != null) {
			Element inc = desc.getChild("include");
			if (inc != null)
				return Utils.getAttributeValue("file", inc) + Utils.getAttributeValue("live_file", inc);
		}
		return "";
	}

	public boolean isLiveFile() {
		Element desc = _job.getChild("description");
		if (desc != null) {
			Element inc = desc.getChild("include");
			if (inc != null)
				return Utils.getAttributeValue("live_file", inc).length() > 0;
		}
		return false;
	}

	public void setInclude(String file) {
		Element desc = _job.getChild("description");
		if (desc == null && !file.equals("")) {
			desc = new Element("description");
			_job.addContent(desc);
		}

		if (desc != null) {
			if (!file.equals("")) {
				Element incl = desc.getChild("include");
				if (incl == null)
					desc.addContent(0, new Element("include").setAttribute("file", file));
				else
					incl.setAttribute("file", file);

			}
			else {
				desc.removeChild("include");
				if (getDescription().equals(""))
					_job.removeChild("description");
			}

			_dom.setChanged(true);
			if (_dom.isDirectory() || _dom.isLifeElement())
				_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		}
	}

	public void setInclude(String file, boolean isLifeFileFile) {
		Element desc = _job.getChild("description");
		if (desc == null && !file.equals("")) {
			desc = new Element("description");
			_job.addContent(desc);
		}

		if (desc != null) {
			if (!file.equals("")) {
				Element incl = desc.getChild("include");
				if (incl == null)
					desc.addContent(0, new Element("include").setAttribute((isLifeFileFile ? "live_file" : "file"), file));
				else {
					incl.removeAttribute("file");
					incl.removeAttribute("live_file");
					incl.setAttribute((isLifeFileFile ? "live_file" : "file"), file);
				}

			}
			else {
				desc.removeChild("include");
				if (getDescription().equals(""))
					_job.removeChild("description");
			}

			_dom.setChanged(true);
			if (_dom.isDirectory() || _dom.isLifeElement())
				_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		}
	}

	public String getIgnoreSignal() {
		return Utils.getAttributeValue("ignore_signals", _job);
	}

	public void setIgnoreSignal(String signals) {
		Utils.setAttribute("ignore_signals", signals, _job, _dom);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public ISchedulerUpdate get_main() {
		return _main;
	}

	public static String getLibrary() {
		return library;
	}

	public static void setLibrary(String library) {
		JobListener.library = library;
	}

	public Element getJob() {
		return _job;
	}

	public void setWarnIfLongerThan(String warnIfLongerThan) {
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		Utils.setAttribute("warn_if_longer_than", warnIfLongerThan, _job, _dom);
	}

	public String getWarnIfLongerThan() {
		return Utils.getAttributeValue("warn_if_longer_than", _job);
	}

	public void setWarnIfShorterThan(String warnIfShorterThan) {
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		Utils.setAttribute("warn_if_shorter_than", warnIfShorterThan, _job, _dom);
	}

	public String getWarnIfShorterThan() {
		return Utils.getAttributeValue("warn_if_shorter_than", _job);
	}

	

	public int languageAsInt(String language) {
		if (language != null) {
			String strT = language.toLowerCase();
			if (_languages == null) {
				_languages = _languagesJob;
			}
			for (int i = 0; i < _languages.length; i++) {
				if (_languages[i].equalsIgnoreCase(strT))
					return i;
			}

			if (_script != null) {
				_script.setAttribute("language", "shell");
			}
		}
		return 0;
	}

	private String languageAsString(int language) {
		String strR = "";
		if (language >= 0) {
			strR = _languages[language];
		}
		return strR;
	}

	public String getLanguage(int language) {
		return _languages[language];
	}

	public int getLanguage() {
		if (_script != null) {
			int intT = languageAsInt(_script.getAttributeValue("language"));
			if (intT < 0) {
				intT = 0;
			}
			return intT;
		}
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
			if (_type == Editor.MONITOR) {
				// Element monitor = _job.getChild("monitor");
				Element monitor = _job;
				if (monitor == null) {
					monitor = new Element("monitor");
					_job.addContent(monitor);
				}
				monitor.addContent(_script);
			}
			else
				_job.addContent(_script);
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
			MainWindow.ErrMsg("No script element defined!");
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

	public String getPrePostProcessingScriptSource () {
		String strT = "";
		return strT;
		
	}
	
	
	public String getSource() {
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

	public void setSource(String source) {

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
				// System.out.println("no script element defined!");
			}

		}
		catch (org.jdom.IllegalDataException jdom) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), jdom);
			}
			catch (Exception ee) {
				// tu nichts
			}

			MainWindow.message(jdom.getMessage(), SWT.ICON_ERROR);
		}
		catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}

			MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
			System.out.println(e);
		}
	}

	public Element getParent() {
		return _job;
	}

	// public String getName() {
	// return Utils.getAttributeValue("name", _job);
	// }
	//
	public void setName(String name) {
		Utils.setAttribute("name", name, _job);
		if (_main != null)
			_main.updateTreeItem(name);

		_dom.setChanged(true);

		setChangedForDirectory();
	}

	public String getOrdering() {
		return Utils.getAttributeValue("ordering", _job);
	}

	public void setOrdering(String ordering) {
		Utils.setAttribute("ordering", ordering, "0", _job);
		_dom.setChanged(true);
		setChangedForDirectory();
	}

	private void setChangedForDirectory() {
		if (_dom.isDirectory() || _dom.isLifeElement()) {
			if (_job != null) {
				Element job = _job;
				if (!job.getName().equals(_job))
					job = Utils.getJobElement(_job);
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

	public void newDirectory() {
		_directory = new Element("start_when_directory_changed");
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public void selectDirectory(int index) {
		if (index >= 0 && index < _directories.size())
			_directory = (Element) _directories.get(index);
	}

	public void applyDirectory(String directory, String regex) {
	    if (_directory == null){
	        newDirectory();
	    }
		Utils.setAttribute("directory", directory, _directory, _dom);
		Utils.setAttribute("regex", regex, _directory, _dom);
		if (!_directories.contains(_directory))
			_directories.add(_directory);
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public void deleteDirectory(int index) {
		if (index >= 0 && index < _directories.size()) {
			_directories.remove(index);
			_directory = null;
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		}
	}

	public String getDirectory() {
		return Utils.getAttributeValue("directory", _directory);
	}

	public boolean isDirectoryTrigger() {
		return _directories.size() > 0;
	}

	public void fillDirectories(Table table) {
		table.removeAll();
		Iterator it = _directories.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Utils.getAttributeValue("directory", e));
			item.setText(1, Utils.getAttributeValue("regex", e));
		}
	}

	public String getRegex() {
		return Utils.getAttributeValue("regex", _directory);
	}

	public void setValue(String name, String value) {
		setValue(name, value, "");
		/*setMail();
		Utils.setAttribute(name, value, _settings, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
		*/
	}

	public void setValue(String name, String value, String default_) {
		if (value == null || value.length() == 0) {
			if (_settings != null) {
				// return;
				_settings.removeChild(name);
				if (_settings.getContentSize() == 0 && _job != null) {
					_job.removeContent(_settings);
				}
				_dom.setChanged(true);
				if (_dom.isDirectory() || _dom.isLifeElement())
					_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
				return;
			}
			else {
				return;
			}
		}

		setMail();

		// Utils.setAttribute(name, value, default_, _settings, _dom);

		Element elem = null;
		if (_settings.getChild(name) == null) {
			elem = new Element(name);
			_settings.addContent(elem);
		}
		else
			elem = _settings.getChild(name);

		elem.setText(value);

		_dom.setChanged(true);
		if (_dom.isDirectory() || _dom.isLifeElement())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);

	}

	private void setMail() {

		_settings = _parent.getChild("settings");
		if (_settings == null) {
			_settings = new Element("settings");
			_parent.addContent(_settings);
		}
	}

	public boolean isExecutable() {
		setProcess();
		return _process != null;
	}

	public String getHistoryWithLog() {
		return getValue("history_with_log");
	}

	public void setHistoryWithLog(final String pstrValue) {
		setValue("history_with_log", pstrValue);
	}

	public String getHistory() {
		return getValue("history");
	}

	public void setHistory(final String pstrValue) {
		setValue("history", pstrValue);
	}

	public String getHistoryOnProcess() {
		return getValue("history_on_process");
	}

	public void setHistoryOnProcess(final String pstrValue) {
		setValue("history_on_process", pstrValue);
	}

	public String getValue(String name) {
		if (_settings == null)
			return "";
		Element elem = _settings.getChild(name);
		if (elem == null)
			return "";

		return elem.getTextNormalize();
	}

	public String getMonitorName() {
		return Utils.getAttributeValue("name", _parent);
	}

	public void setMonitorName(String name) {
		Utils.setAttribute("name", name, _parent);
		if (_main != null)
			_main.updateTreeItem(name);

		_dom.setChanged(true);

		setChangedForDirectory();
	}

}
