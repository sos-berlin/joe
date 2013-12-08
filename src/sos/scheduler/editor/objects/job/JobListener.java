package com.sos.joe.objects.job;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JOEListener;

import com.sos.scheduler.model.LanguageDescriptor;
import com.sos.scheduler.model.LanguageDescriptorList;
import com.sos.scheduler.model.objects.JSObjJob;
import com.sos.scheduler.model.objects.Job.StartWhenDirectoryChanged;
import com.sos.scheduler.model.objects.RunTime;
import com.sos.scheduler.model.objects.Script;

public class JobListener extends JOEListener {

	public static final String	conTagSCRIPT	= "script";

	private final String conClassName = this.getClass().getSimpleName();
	private final Logger logger = Logger.getLogger(this.getClass());
	@SuppressWarnings("unused")
	private final String		conSVNVersion	= "$Id: JobListener.java 21531 2013-12-08 14:59:54Z kb $";

	private List<StartWhenDirectoryChanged>	objStartWhenDirectoryChangedList	= null;
	private StartWhenDirectoryChanged		objStartWhenDirectoryChangedEntry	= null;

	private static String		library			= "";

	public String[]				_languages		= null;

	private Element				_script			= null;
	private Element				_settings		= null;
	private Element				_process		= null;
	private final Element				_environment	= null;

	private final int			_type			= -1;

	private final List<Element>		_directories	= null;
	private final Element				_directory		= null;
	private final List				_setbacks		= null;
	private final Element		_setback		= null;
	private final List				_errorDelays	= null;
	private final Element		_errorDelay		= null;

	private JSObjJob						objJSJob							= null;

	public JobListener(final SchedulerDom dom, final Element job, final ISchedulerUpdate update) {

		_dom = dom;
		// _job = job;
		// _parent = _job;
		// objElement = _job;
		_main = update;

		// _directories = _job.getChildren("start_when_directory_changed");
		// _setbacks = _job.getChildren("delay_order_after_setback");
		// _errorDelays = _job.getChildren("delay_after_error");
		//
		// _settings = _job.getChild("settings");

		setScript();
		_main = update;

	}


	public JobListener(final JSObjJob pobjJob, final ISchedulerUpdate update) {
		objJSJob = pobjJob;
	}

	private void setScript() {
		// if (_type == Editor.MONITOR) {
		// Element monitor = _job;
		// if (monitor != null) {
		// _script = monitor.getChild("script");
		// }
		// }
		// else
		// _script = _job.getChild("script");
	}


	public String getFile() {
		String strT = objJSJob.getProcess().getFile();
		return avoidNull(strT);
	}

	public void setFile(final String file) {
		objJSJob.setFile(file);
	}

	public String getParam() {
		String strT = objJSJob.getProcess().getParam();
		return avoidNull(strT);
	}

	public void setParam(final String param) {
		objJSJob.getProcess().setParam(param);
		setDirty();
	}

	@Override
	public void setDirty() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::setDirty";

		objJSJob.setDirty(true);

	} // private void setDirty

	public String getLogFile() {
		return avoidNull(objJSJob.getProcess().getLogFile());
	}

	private String avoidNull(final String pstrV) {
		if (pstrV == null) {
			return "";
		}
		return pstrV;
	}

	public void setLogFile(final String file) {
		objJSJob.getProcess().setLogFile(file);
		setDirty();
	}

	public boolean isIgnoreSignal() {
		return getYesOrNo(objJSJob.getProcess().getIgnoreSignal());
	}

	public void setIgnoreSignal(final boolean ignore) {
		objJSJob.getProcess().setIgnoreSignal(setYesOrNo(ignore));
		setDirty();
	}

	public boolean isIgnoreError() {
		return getYesOrNo(objJSJob.getProcess().getIgnoreError());
	}

	public void setIgnoreError(final boolean ignore) {
		objJSJob.getProcess().setIgnoreError(setYesOrNo(ignore));
		setDirty();
	}

	public boolean isShell() {
		return getLanguage() == 0;
	}

	@Deprecated
	public String[] getLanguages() {
		return _languages;
	}

	@Override
	public String getComment() {
		// return Utils.getAttributeValue("__comment__", _job);
		return "";
	}

	@Override
	public void setComment(final String comment) {
		// if (_dom.isDirectory() || _dom.isLifeElement())
		// _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		// Utils.setAttribute("__comment__", comment, _job, _dom);
	}

	@Override
	public boolean isDisabled() {
		return objJSJob.isDisabled();
	}

	@Override
	public String getJobName() {
		return avoidNull(objJSJob.getJobName());
	}

	public void setJobName(final String name, final boolean updateTree) {

		objJSJob.setName(name);
		setDirty();
	}

	public String getTitle() {
		return avoidNull(objJSJob.getTitle());
	}

	public void setTitle(final String title) {
		objJSJob.setTitle(title);
		setDirty();
	}

	public String getSpoolerID() {
		return avoidNull(objJSJob.getSpoolerId());
	}

	public void setSpoolerID(final String spoolerID) {
		objJSJob.setSpoolerId(spoolerID);
		setDirty();
	}

	public String getProcessClass() {
		return avoidNull(objJSJob.getProcessClass());
	}

	public void setProcessClass(final String processClass) {
		objJSJob.setProcessClass(processClass);
		setDirty();
	}

	public boolean getOrder() {
		return getYesOrNo(objJSJob.getOrder());
	}

	public boolean isOrderJob() {
		return getOrder();
	}

	public boolean getStopOnError() {
		return getYesOrNo(objJSJob.getStopOnError());
	}

	public boolean getForceIdletimeout() {
		return getYesOrNo(objJSJob.getForceIdleTimeout());
	}

	public String getJobNameAndTitle() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getJobNameAndTitle";

		return avoidNull(objJSJob.getJobNameAndTitle());

	} // private String getJobNameAndTitle

	public void setOrder(final boolean order) {
		objJSJob.setOrder(setYesOrNo(order));
		if (order) {
			// _job.removeAttribute("priority");
			RunTime objR = objJSJob.getRunTime();
			// if (_job.getChild("run_time") == null)
			// _job.addContent(new Element("run_time").setAttribute("let_run", "no"));
		}
	}

	public String getPriority() {
		// TODO getPriority
		//		if (objJSJob.getPriority() != null) {
		//			return avoidNull(objJSJob.getPriority().get(0));
		//		}
		return "";
	}

	public void setPriority(final String priority) {
		// Utils.setAttribute("priority", priority, _job, _dom);
		// if (_dom.isDirectory() || _dom.isLifeElement())
		// _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public String getJavaOptions() {
		return avoidNull(objJSJob.getJavaOptions());
	}

	public void setJavaOptions(final String javaOptions) {
		objJSJob.setJavaOptions(javaOptions);
		setDirty();
	}

	public String getTasks() {
		return avoidNull(objJSJob.getTasksAsString());
	}

	public void setTasks(final String tasks) {
		objJSJob.setTasks(tasks);
		setDirty();
	}

	public String getTimeout() {
		return avoidNull(objJSJob.getTimeout());
	}

	public void setTimeout(final String timeout) {
		objJSJob.setTimeout(timeout);
		setDirty();
	}

	public String getIdleTimeout() {
		return avoidNull(objJSJob.getIdleTimeout());
	}

	public void setIdleTimeout(final String idleTimeout) {
		objJSJob.setIdleTimeout(idleTimeout);
		setDirty();
	}

	public void setForceIdletimeout(final boolean forceIdleTimeout) {
		objJSJob.setTimeout(setYesOrNo(forceIdleTimeout));
		setDirty();
	}

	public void setStopOnError(final boolean stopOnError) {
		objJSJob.setStopOnError(setYesOrNo(stopOnError));
		setDirty();
	}

	public void setReplace(final boolean replace) {
		objJSJob.setReplace(setYesOrNo(replace));
		setDirty();
	}

	public boolean getReplace() {
		return getYesOrNo(objJSJob.getReplace());
	}

	public void setTemporary(final boolean temporary) {
		objJSJob.setTemporary(setYesOrNo(temporary));
		setDirty();
	}

	public boolean getTemporary() {
		return objJSJob.isTemporary();

	}

	public void setMintasks(final String mintasks) {
		objJSJob.setMintasks(mintasks);
	}

	public String getMintasks() {
		return avoidNull(objJSJob.getMintasks());
	}

	public void setVisible(final String visible) {
		objJSJob.setVisible(visible);
	}

	public String getVisible() {
		return avoidNull(objJSJob.getVisible());
	}


//	private void setProcess() {
//		_process = _job.getChild("process");
//		if (_process != null) {
//			_script = null;
//			_environment =  _process.getChild("environment");
//		}
//	}

//	private void initProcess() {
//		_process = _job.getChild("process");
//		if (_process == null) {
//			_process = new Element("process");
//			_environment = new Element("environment");
//			_job.addContent(_process);
//			_job.removeChild(conTagSCRIPT);
//			setProcess();
//			_dom.setChanged(true);
//		}
//	}

	private void getScript() {
		if (_type == Editor.MONITOR) {
			Element monitor = _job;
			if (monitor != null) {
				_script = monitor.getChild(conTagSCRIPT);
			}
		}
		else {
			if (_process == null) {
				_script = _job.getChild(conTagSCRIPT);
				_process = null;
			}
			else {
				_script = null;
			}
		}
	}


//	@Override
//	public void setDirty() {
//		if (_dom.isDirectory() || _dom.isLifeElement()) {
//			_dom.setChangedForDirectory("job", getJobName(), SchedulerDom.MODIFY);
//		}
//	}



	@Override
	public String getDescription() {
		return objJSJob.getDescription().getContent().toString();
	}

	@Override
	public void setDescription(final String description) {
		// TODO set Description
		// Element desc = _job.getChild("description");
		// String f = getInclude();
		//
		// if (desc == null && !description.equals("")) {
		// desc = new Element("description");
		// _job.addContent(0, desc);
		// }
		//
		// if (desc != null) {
		// if (description.equals("") && (f == null || f.equals(""))) {
		// _job.removeChild("description");
		// _dom.setChanged(true);
		// if (_dom.isDirectory() || _dom.isLifeElement())
		// _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		// return;
		// }
		//
		// desc.removeContent();
		// if (!(f == null || f.equals(""))) {
		// setInclude(f);
		// }
		// desc.addContent(new CDATA(description));
		// _dom.setChanged(true);
		// if (_dom.isDirectory() || _dom.isLifeElement())
		// _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		// }
	}

	public String getInclude() {
		// TODO implement getInclude
		// Element desc = _job.getChild("description");
		// if (desc != null) {
		// Element inc = desc.getChild("include");
		// if (inc != null)
		// return Utils.getAttributeValue("file", inc) + Utils.getAttributeValue("live_file", inc);
		// }
		return "";
	}

	public boolean isLiveFile() {
		// TODO implement
		List<Object> objL = objJSJob.getDescription().getContent();
		// Element desc = _job.getChild("description");
		// if (desc != null) {
		// Element inc = desc.getChild("include");
		// if (inc != null)
		// return Utils.getAttributeValue("live_file", inc).length() > 0;
		// }
		return false;
	}

	public void setInclude(final String file) {
		setInclude(file, false);
		// Element desc = _job.getChild("description");
		// if (desc == null && !file.equals("")) {
		// desc = new Element("description");
		// _job.addContent(desc);
		// }
		//
		// if (desc != null) {
		// if (!file.equals("")) {
		// Element incl = desc.getChild("include");
		// if (incl == null)
		// desc.addContent(0, new Element("include").setAttribute("file", file));
		// else
		// incl.setAttribute("file", file);
		//
		// }
		// else {
		// desc.removeChild("include");
		// if (getDescription().equals(""))
		// _job.removeChild("description");
		// }
		//
		// _dom.setChanged(true);
		// if (_dom.isDirectory() || _dom.isLifeElement())
		// _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		// }
	}

	public void setInclude(final String file, final boolean isLifeFileFile) {
		// TODO implement
		// Element desc = _job.getChild("description");
		// if (desc == null && !file.equals("")) {
		// desc = new Element("description");
		// _job.addContent(desc);
		// }
		//
		// if (desc != null) {
		// if (!file.equals("")) {
		// Element incl = desc.getChild("include");
		// if (incl == null)
		// desc.addContent(0, new Element("include").setAttribute((isLifeFileFile ? "live_file" : "file"), file));
		// else {
		// incl.removeAttribute("file");
		// incl.removeAttribute("live_file");
		// incl.setAttribute((isLifeFileFile ? "live_file" : "file"), file);
		// }
		//
		// }
		// else {
		// desc.removeChild("include");
		// if (getDescription().equals(""))
		// _job.removeChild("description");
		// }
		//
		// _dom.setChanged(true);
		// if (_dom.isDirectory() || _dom.isLifeElement())
		// _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		// }
	}

	public String getIgnoreSignal() {
		// TODO result is a list. what is expected here?
		// objJSJob.getIgnoreSignals().
		// return Utils.getAttributeValue("ignore_signals", _job);
		return "";
	}

	public void setIgnoreSignal(final String signals) {
		// TODO implement
		// Utils.setAttribute("ignore_signals", signals, _job, _dom);
		// if (_dom.isDirectory() || _dom.isLifeElement())
		// _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	@Deprecated
	public ISchedulerUpdate get_main() {
		return _main;
	}

	public JSObjJob getJSJob() {
		return objJSJob;
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

	public static String getLibrary() {
		return library;
	}

	public static void setLibrary(final String library) {
		JobListener.library = library;
	}

	public void setWarnIfLongerThan(final String warnIfLongerThan) {
		objJSJob.setWarnIfLongerThan(warnIfLongerThan);
	}

	public String getWarnIfLongerThan() {
		return avoidNull(objJSJob.getWarnIfLongerThan());
	}

	public void setWarnIfShorterThan(final String pstrWarnIfShorterThan) {
		objJSJob.setWarnIfShorterThan(pstrWarnIfShorterThan);
	}

	public String getWarnIfShorterThan() {
		return avoidNull(objJSJob.getWarnIfShorterThan());
	}

	public int languageAsInt(final String language) {
		return objJSJob.languageAsInt(language);
	}

	@Override
	public int getLanguage() {
		return objJSJob.getLanguage();
	}

	public String getLanguageAsString(final int language) {
		return objJSJob.getLanguageAsString(language);
	}

	public void setLanguage(final int language) {
		objJSJob.setLanguage(language);
	}

	public String getJavaClass() {
		String s = objJSJob.getScript().getJavaClass();
		return avoidNull(s);
	}

	public void setJavaClass(final String javaClass) {
		objJSJob.setJavaClass(javaClass);
	}

	public String getComClass() {
		return avoidNull(objJSJob.getScript().getComClass());
	}

	public String getFilename() {
		return avoidNull(objJSJob.getScript().getFilename());
	}

	public void setClasspath(final String classpath) {
		objJSJob.setClasspath(classpath);
	}

	public String getClasspath() {
		String s = objJSJob.getScript().getJavaClassPath();
		return avoidNull(s);
	}


	//	private String languageAsString(int language) {
	//		String strR = "";
	//		if (language >= 0) {
	//			strR = _languages[language];
	//		}
	//		return strR;
	//	}

//	private String languageAsString(final int language) {
//		// TODO in die Klasse LanguageDescriptorList
//		String strR = "";
//		if (language >= 0) {
//			strR = _languages[language];
//		}
//		return strR;
//	}

	//	public String getLanguage(final int language) {
	//		// TODO in die Klasse LanguageDescriptorList
	//		return _languages[language];
	//	}
	//
	LanguageDescriptor	objL	= null;

	public LanguageDescriptor getLanguageDescriptor() {
		String strJavaClass = getJavaClass().toLowerCase();
		objL = LanguageDescriptorList.getLanguageDescriptor4Class(strJavaClass);
		if (objL == null) {
			if (_script != null) {
				objL = LanguageDescriptorList.getLanguageDescriptor(_script.getAttributeValue("language"));
			}
		}
		if (objL == null) {
			objL = LanguageDescriptorList.getDefaultLanguage();
		}
		setLanguage(objL.getLanguageNumber());
		return objL;
	}

	@Override
	public String getLanguageAsString() {
		String strT = "";
		if (objL != null) {
			strT = objL.getLanguageName();
		}
		return strT;
	}

	public void setLanguage(final LanguageDescriptor pobjLD) {
		this.setLanguage(pobjLD.getLanguageNumber());
		objL = pobjLD;
	}

	@Override
	public void setLanguage(final String pstrLanguage) {
		this.setLanguage(languageAsInt(pstrLanguage));
	}

	@Deprecated
	public void fillIncludesTable(final Table table) {
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
		for (String element : inc) {
			if (element != null)
				retVal = element + ";" + retVal;
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
	public void addIncludesFromTable(final Table table, final HashMap inc) {
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

	public void addInclude(final Table table, final String filename, final boolean isLife) {
		if (_script != null) {
			List includes = _script.getChildren("include");
			if (table.getSelectionCount() > 0) {
				Element in = (Element) _script.getChildren("include").get(table.getSelectionIndex());

				in.setAttribute(isLife ? "live_file" : "file", filename);
			}
			else {
				_script.addContent(includes.size(), new Element("include").setAttribute(isLife ? "live_file" : "file", filename));
			}
			_dom.setChanged(true);
			fillIncludesTable(table);
			setChangedForDirectory();

		}
		else {
			MainWindow.ErrMsg("No script element defined!");
		}
	}

	public void addInclude(final String filename) {
		if (_script != null) {
			List includes = _script.getChildren("include");
			_script.addContent(includes.size(), new Element("include").setAttribute("file", filename));
			_dom.setChanged(true);
			setChangedForDirectory();
		}
		else {
			MainWindow.message("No script element defined!", SWT.ICON_ERROR);
			logger.debug("no script element defined!");
		}
	}

	public void removeInclude(final int index) {
		objJSJob.getScript().getContent();
	}


//	public void removeInclude(final int index) {
//		if (_script != null) {
//			List includeList = _script.getChildren("include");
//			if (index >= 0 && index < includeList.size()) {
//				includeList.remove(index);
//				_dom.setChanged(true);
//				setChangedForDirectory();
//			}
//			else
//				System.out.println("index " + index + " is out of range for include!");
//		}
//		else {
//			MainWindow.message("No script element defined!", SWT.ICON_ERROR);
//			logger.info("no script element defined!");
//		}
//	}

	public void removeIncludes() {
		if (_script != null) {
			_script.removeChildren("include");
		}
	}

	@Override
	public String getPrePostProcessingScriptSource() {
		String strT = "";
		return strT;
	}

	@Override
	public String getSource() {
		Script objS = objJSJob.getScript();
		if (objS.getContent().size() > 0) {
			return (String) objS.getContent().get(0);
		}
		else {
			return "";
		}
	}

	@Override
	public void setSource(final String source) {
		objJSJob.getScript().getContent().add(source);
		//		objJSJob.getScript().setContent(source);
		setDirty();
		//		try {
		//
		//			if (_script != null) {
		//				List l = _script.getContent();
		//				for (int i = 0; i < l.size(); i++) {
		//					if (l.get(i) instanceof CDATA)
		//						l.remove(i);
		//				}
		//				if (!source.equals("")) {
		//
		//					_script.addContent(new CDATA(source));
		//				}
		//
		//				_dom.setChanged(true);
		//				setChangedForDirectory();
		//			}
		//			else {
		//				MainWindow.message("No script element defined!", SWT.ICON_ERROR);
		//				// System.out.println("no script element defined!");
		//			}
		//
		//		}
		//		catch (org.jdom.IllegalDataException jdom) {
		//			try {
		//				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), jdom);
		//			}
		//			catch (Exception ee) {
		//				// tu nichts
		//			}
		//
		//			MainWindow.message(jdom.getMessage(), SWT.ICON_ERROR);
		//		}
		//		catch (Exception e) {
		//			try {
		//				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
		//			}
		//			catch (Exception ee) {
		//				// tu nichts
		//			}
		//
		//			MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
		//			System.out.println(e);
		//		}
	}

	public JSObjJob getParent() {
		return objJSJob;
	}

	public void setName(final String name) {
		objJSJob.setName(name);
	}

	public String getOrdering() {
		return String.valueOf(objJSJob.getMonitor().get(0).getOrdering());
	}

	public void setOrdering(final String ordering) {
		objJSJob.setOrdering(ordering);
	}



	// public String getName() {
	// return Utils.getAttributeValue("name", _job);
	// }
	//

	private void setChangedForDirectory() {
		setDirty();
		{
			if (_job != null) {
				Element job = _job;
				if (!job.getName().equals(_job))
					job = Utils.getJobElement(_job);
				_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", job), SchedulerDom.MODIFY);
			}
		}
	}

	public boolean isJava() {
		return objJSJob.isJava();
	}

	public void newDirectory() {
		StartWhenDirectoryChanged objDir = new StartWhenDirectoryChanged();
		objStartWhenDirectoryChangedEntry = objDir;
		objJSJob.getStartWhenDirectoryChanged().add(objDir);
		setDirty();
	}

	public void selectDirectory(final int index) {
		objStartWhenDirectoryChangedList = objJSJob.getStartWhenDirectoryChanged();
		if (index >= 0 && index < objStartWhenDirectoryChangedList.size())
			objStartWhenDirectoryChangedEntry = objStartWhenDirectoryChangedList.get(index);
	}

	public void applyDirectory(final String directory, final String regex) {
		if (objStartWhenDirectoryChangedEntry == null) {
			newDirectory();
		}
		objStartWhenDirectoryChangedEntry.setDirectory(directory);
		objStartWhenDirectoryChangedEntry.setRegex(regex);
		setDirty();
	}

	public void deleteDirectory(final int index) {
		objStartWhenDirectoryChangedList = objJSJob.getStartWhenDirectoryChanged();
		if (index >= 0 && index < objStartWhenDirectoryChangedList.size()) {
			objStartWhenDirectoryChangedList.remove(index);
			objStartWhenDirectoryChangedEntry = null;
			setDirty();
		}
	}

	public String getDirectory() {
		return avoidNull(objStartWhenDirectoryChangedEntry.getDirectory());
	}

	public boolean isDirectoryTrigger() {
		return objStartWhenDirectoryChangedList.size() > 0;
	}

	public void fillDirectories(final Table table) {
		table.removeAll();
		objStartWhenDirectoryChangedList = objJSJob.getStartWhenDirectoryChanged();
		if (objStartWhenDirectoryChangedList != null) {
			for (StartWhenDirectoryChanged objItem : objStartWhenDirectoryChangedList) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, objItem.getDirectory());
				item.setText(1, objItem.getRegex());
			}
		}
	}

	public String getRegex() {
		return avoidNull(objStartWhenDirectoryChangedEntry.getRegex());
	}

	// public boolean isExecutable() {
	// setProcess();
	// return _process != null;
	// }

	public String getHistoryWithLog() {
		if (objJSJob.getSettings() != null) {
			return avoidNull(objJSJob.getSettings().getHistoryWithLog());
		}
		return "";
	}

	public void setHistoryWithLog(final String pstrValue) {
		objJSJob.setHistoryWithLog(pstrValue);
	}

	public String getHistory() {
		if (objJSJob.getSettings() != null) {
			return avoidNull(avoidNull(objJSJob.getSettings().getHistory()));
		}
		return "";
	}

	public void setHistory(final String pstrValue) {
		objJSJob.setHistory(pstrValue);
	}

	public String getHistoryOnProcess() {
		if (objJSJob.getSettings() != null) {
			return avoidNull(objJSJob.getSettings().getHistoryOnProcess());
		}
		return "";
	}

	public void setHistoryOnProcess(final String pstrValue) {
		objJSJob.setHistoryOnProcess(pstrValue);
	}

	public String getMonitorName() {
		//		return Utils.getAttributeValue("name", _parent);
		return "";
	}

	public void setMonitorName(final String name) {
		//		Utils.setAttribute("name", name, _parent);
	}

	/**
	 * @return the _dom
	 */
	@Deprecated
	public SchedulerDom getDom() {
		return _dom;
	}

	@Deprecated
	public void setValue(final String name, final String value) {
		setValue(name, value, "");
	}

	@Deprecated
	public void setValue(final String name, final String value, final String default_) {
		if (value == null || value.length() == 0) {
			if (_settings != null) {
				// return;
				_settings.removeChild(name);
				if (_settings.getContentSize() == 0 && _job != null) {
					_job.removeContent(_settings);
				}
				_dom.setChanged(true);
				setDirty();
			}
			return;
		}

		setMail();
		Element elem = null;
		if (_settings.getChild(name) == null) {
			elem = new Element(name);
			_settings.addContent(elem);
		}
		else
			elem = _settings.getChild(name);

		elem.setText(value);

		_dom.setChanged(true);
		setDirty();
	}

	private void setMail() {
		_settings = _parent.getChild("settings");
		if (_settings == null) {
			_settings = new Element("settings");
			_parent.addContent(_settings);
		}
	}

	@Deprecated
	public String getValue(final String name) {
		if (_settings == null)
			return "";
		Element elem = _settings.getChild(name);
		if (elem == null)
			return "";

		return elem.getTextNormalize();
	}
}
