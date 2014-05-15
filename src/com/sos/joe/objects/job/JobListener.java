package com.sos.joe.objects.job;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JOEListener;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.joe.interfaces.ISchedulerUpdate;
import com.sos.scheduler.model.LanguageDescriptor;
import com.sos.scheduler.model.LanguageDescriptorList;
import com.sos.scheduler.model.objects.Include;
import com.sos.scheduler.model.objects.JSObjJob;
import com.sos.scheduler.model.objects.Job.DelayOrderAfterSetback;
import com.sos.scheduler.model.objects.Job.Description;
import com.sos.scheduler.model.objects.Job.Monitor;
import com.sos.scheduler.model.objects.Job.StartWhenDirectoryChanged;
import com.sos.scheduler.model.objects.JobSettings;
import com.sos.scheduler.model.objects.RunTime;
import com.sos.scheduler.model.objects.Script;

public class JobListener extends JOEListener {
	public static final String				conTagSCRIPT						= "script";
	private final String					conClassName						= this.getClass().getSimpleName();
	private final Logger					logger								= Logger.getLogger(this.getClass());
	@SuppressWarnings("unused")
	private final String					conSVNVersion						= "$Id: JobListener.java 21531 2013-12-08 14:59:54Z kb $";
	private List<StartWhenDirectoryChanged>	objStartWhenDirectoryChangedList	= null;
	private StartWhenDirectoryChanged		objStartWhenDirectoryChangedEntry	= null;
	private static String					library								= "";
	public String[]							_languages							= null;
	private final Element					_settings							= null;
	private final Element					_process							= null;
	private final Element					_environment						= null;
	private int								intJSObjectType						= -1;
	private final List<Element>				_directories						= null;
	private final Element					_directory							= null;
	private final List						_setbacks							= null;
	private final Element					_setback							= null;
	private final List						_errorDelays						= null;
	private final Element					_errorDelay							= null;
	private JSObjJob						objJSJob							= null;

	@Deprecated
	public JobListener(final SchedulerDom dom_, final Element objElement, final ISchedulerUpdate update) {
	}


	public JobListener(final TreeData pobjTreeData, final ISchedulerUpdate update) {
		objJSJob = pobjTreeData.getJob();
		objTreeData = pobjTreeData;
		intJSObjectType = objTreeData.getType();
	}

	public JobListener(final TreeData pobjTreeData) {
		objJSJob = pobjTreeData.getJob();
		objTreeData = pobjTreeData;
		intJSObjectType = objTreeData.getType();
	}

	public JobListener(final JSObjJob pobjJob) {
		objJSJob = pobjJob;
	}

	public String getFile() {
		String strT = objJSJob.getProcess().getFile();
		return avoidNull(strT);
	}

	@Deprecated
	public Element getJob() {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::getJob";
		return new Element("job");
	} // private String getJob

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

	@Override public void setDirty() {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::setDirty";
		objJSJob.setDirty(true);
	} // private void setDirty

	public String getLogFile() {
		return avoidNull(objJSJob.getProcess().getLogFile());
	}

	public String getLogLevel() {
		return avoidNull(objJSJob.getLogLevel());
	}

	public void setLogLevel(final String text) {
		// TODO		objJSJob.getSettings().setLogLevel(text);
		setDirty();
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

	@Deprecated public String[] getLanguages() {
		return _languages;
	}

	@Override public String getComment() {
		// return Utils.getAttributeValue("__comment__", _job);
		return "";
	}

	// TODO in Documentation unterbringen
	@Override public void setComment(final String comment) {
		// if (_dom.isDirectory() || _dom.isLifeElement())
		// _dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		// Utils.setAttribute("__comment__", comment, _job, _dom);
	}

	@Override public boolean isDisabled() {
		return objJSJob.isDisabled();
	}

	@Override public String getJobName() {
		return avoidNull(objJSJob.getJobName());
	}

	public void setJobName(final String name, final boolean updateTree) {
		// TODO how to handle rename?
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
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::getJobNameAndTitle";
		return avoidNull(objJSJob.getJobNameAndTitle());
	} // private String getJobNameAndTitle

	public void setOrder(final boolean order) {
		objJSJob.setOrder(setYesOrNo(order));
		if (order) {
			// TODO an attribute on the run_time: active=yes|no
			// TODO ignore run_Time on orderjobs by the engine
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
	//	private void getScript() {
	//		if (_type == Editor.MONITOR) {
	//			Element monitor = _job;
	//			if (monitor != null) {
	//				_script = monitor.getChild(conTagSCRIPT);
	//			}
	//		}
	//		else {
	//			if (_process == null) {
	//				_script = _job.getChild(conTagSCRIPT);
	//				_process = null;
	//			}
	//			else {
	//				_script = null;
	//			}
	//		}
	//	}
	@Override public String getDescription() {
		return objJSJob.getDescription().getContent().toString();
	}

	@Override public void setDescription(final String description) {
		Description objDescription = objJSJob.getDescription();
		List<Object> objL = objDescription.getContent();
		for (Object object : objL) {
			if (object instanceof String) {
				object = description;
				setDirty();
				break;
			}
		}
	}

	public String getInclude4JobDescription() {
		String strR = "";
		Include objI = getJobDescriptionInclude();
		if (objI != null) {
			strR = objI.getFile();
		}
		return strR;
	}

	public Include getJobDescriptionInclude() {
		Description objDescription = objJSJob.getDescription();
		List<Object> objD = objDescription.getContent();
		for (Object object : objD) {
			if (object instanceof Include) {
				return (Include) object;
			}
		}
		return null;
	}

	public boolean isDescriptionALiveFile() {
		// TODO implement
		List<Object> objL = objJSJob.getDescription().getContent();
		for (Object object : objL) {
			if (object instanceof Include) {
				Include objI = (Include) object;
				return isNotEmpty(objI.getLiveFile());
			}
		}
		return false;
	}

	public void setInclude(final String file) {
		setInclude(file, false);
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
		//		String strR = objJSJob.getIgnoreSignals()
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

	//	@Deprecated public ISchedulerUpdate get_main() {
	//		return _main;
	//	}
	//
	public JSObjJob getJSJob() {
		return objJSJob;
	}

	public String[] getProcessClasses() {
		// TODO Implement global list in objFactory / Hotfolder
		String[] names = null;
		//		if (_dom == null) {
		//			return new String[] {};
		//		}
		//		if (_dom.getRoot().getName().equalsIgnoreCase("spooler")) {
		//			Element classes = _dom.getRoot().getChild("config").getChild("process_classes");
		//			if (classes != null) {
		//				List list = classes.getChildren("process_class");
		//				names = new String[list.size()];
		//				int i = 0;
		//				Iterator it = list.iterator();
		//				while (it.hasNext()) {
		//					Object o = it.next();
		//					if (o instanceof Element) {
		//						String name = ((Element) o).getAttributeValue("name");
		//						if (name == null)
		//							name = "";
		//						names[i++] = name;
		//					}
		//				}
		//			}
		//		}
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

	@Override public int getLanguage() {
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
	LanguageDescriptor	objL	= null;

	private Script getScript() {
		Script objS = null;
		if (intJSObjectType == Editor.MONITOR) {
			List<Monitor> lstM = objJSJob.getMonitor();
			if (lstM != null) {
				// TODO select right monitor, 0 is not always the correct choice
				objS = lstM.get(0).getScript();
			}
		}
		else {
			if (_process == null) {
				objS = objJSJob.getScript();
			}
			else {
			}
		}
		return objS;
	}

	public LanguageDescriptor getLanguageDescriptor() {
		String strJavaClass = getJavaClass().toLowerCase();
		objL = LanguageDescriptorList.getLanguageDescriptor4Class(strJavaClass);
		if (objL == null) {
			Script objS = getScript();
			if (objS != null) {
				objL = LanguageDescriptorList.getLanguageDescriptor(objS.getLanguage());
			}
		}
		if (objL == null) {
			objL = LanguageDescriptorList.getDefaultLanguage();
		}
		setLanguage(objL.getLanguageNumber());
		return objL;
	}

	@Override public String getLanguageAsString() {
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

	@Override public void setLanguage(final String pstrLanguage) {
		this.setLanguage(languageAsInt(pstrLanguage));
	}

	public void fillIncludesTable(final Table table) {
		Script objS = getScript();
		if (objS != null && table != null) {
			table.removeAll();
			List<Object> includeList = objS.getContent();
			for (Object object : includeList) {
				if (object instanceof Include) {
					Include objIncl = (Include) object;
					TableItem item = new TableItem(table, SWT.NONE);
					if (objIncl.getLiveFile() != null) {
						item.setText(0, objIncl.getLiveFile());
						item.setText(1, "live_file");
					}
					else {
						item.setText(0, objIncl.getFile());
						item.setText(1, "file");
					}
				}
			}
		}
	}

	public String getIncludesAsString() {
		String retVal = "";
		List<Include> inc = getIncludes();
		for (Include element : inc) {
			if (element != null)
				retVal = element.getFile() + element.getLiveFile() + ";" + retVal;
		}
		return retVal;
	}

	// TODO move to JSobjJob -> JSObjScript
	public List<Include> getIncludes() {
		Script objS = getScript();
		List<Include> lstIncludeList = new ArrayList<Include>();
		if (objS != null) {
			List<Object> includeList = objS.getContent();
			for (Object object : includeList) {
				if (object instanceof Include) {
					Include objIncl = (Include) object;
					lstIncludeList.add(objIncl);
				}
			}
		}
		return lstIncludeList;
	}

	// TODO move to JSObjJob -> JSObjScript
	public boolean addInclude(final String filename, final boolean isLife) {
		Script objS = getScript();
		if (objS != null) {
			List<Include> includes = getIncludes();
			for (Include objInclude : includes) {
				if (objInclude.getFile().equalsIgnoreCase(filename)) {
					return false;
				}
				if (objInclude.getLiveFile().equalsIgnoreCase(filename)) {
					return false;
				}
			}
			List<Object> includeList = objS.getContent();
			Include objI = new Include();
			if (isLife == false) {
				objI.setFile(filename);
				objI.setLiveFile(EMPTY_STRING);
			}
			else {
				objI.setLiveFile(filename);
				objI.setFile(EMPTY_STRING);
			}
			includeList.add(objI);
			setDirty();
		}
		else {
			MainWindow.message("No script element defined!", SWT.ICON_ERROR);
			logger.debug("no script element defined!");
		}
		return true;
	}

	// TODO Move to JSObjScript
	public void removeInclude(final int index) {
		Script objS = getScript();
		if (objS != null) {
			List<Include> includes = getIncludes();
			if (index >= 0 && index <= includes.size()) {
				includes.remove(index);
				setDirty();
			}
		}
		else {
			MainWindow.message("No script element defined!", SWT.ICON_ERROR);
			logger.debug("no script element defined!");
		}
	}

	// TODO move to jsobjjob. Delete not on the getIncludes but on the getScriptList
	public void removeIncludes() {
		Script objS = getScript();
		if (objS != null) {
			List<Include> includes = getIncludes();
			if (includes != null) {
				objS.getContent().removeAll(includes);
				setDirty();
			}
			// TODO 
			objJSJob.setDirty();
		}
		else {
			MainWindow.message("No script element defined!", SWT.ICON_ERROR);
			logger.debug("no script element defined!");
		}
	}

	@Override public String getPrePostProcessingScriptSource() {
		// TODO implement
		String strT = "";
		return strT;
	}

	@Override public String getSource() {
		Script objS = objJSJob.getScript();
		List<Object> lstS = objS.getContent();
		if (lstS.size() > 0) {
			String strS = (String) objS.getContent().get(0);
			return strS.trim();
		}
		else {
			return "";
		}
	}

	@Override public void setSource(final String source) {
		objJSJob.getScript().getContent().add(source);
		setDirty();
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

	public void fillDirectories$StartWhenDirectoryChanged(final Table table) {
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

	public String getRegex$StartWhenDirectoryChanged() {
		return avoidNull(objStartWhenDirectoryChangedEntry.getRegex());
	}

	// public boolean isExecutable() {
	// setProcess();
	// return _process != null;
	// }
	public String getHistoryWithLog() {
		if (objJSJob.getSettings() != null) {
			return avoidNull(objJSJob.getHistoryWithLog());
		}
		return "";
	}

	public void setHistoryWithLog(final String pstrValue) {
		objJSJob.setHistoryWithLog(pstrValue);
	}

	public String getHistory() {
		if (objJSJob.getSettings() != null) {
			return avoidNull(avoidNull(objJSJob.getHistory()));
		}
		return "";
	}

	public void setHistory(final String pstrValue) {
		objJSJob.setHistory(pstrValue);
	}

	public String getHistoryOnProcess() {
		if (objJSJob.getSettings() != null) {
			return avoidNull(objJSJob.getHistoryOnProcess());
		}
		return "";
	}

	public void setHistoryOnProcess(final String pstrValue) {
		objJSJob.setHistoryOnProcess(pstrValue);
	}

	public String getMonitorName() {
		//	todo	return Utils.getAttributeValue("name", _parent);
		return "";
	}

	public void setMonitorName(final String name) {
		//	TODO	Utils.setAttribute("name", name, _parent);
	}

	public void setMailOnError(final String pstrValue, final String pstrDefaultValue) {
		objJSJob.setMailOnError(OneOfUs(pstrValue, pstrDefaultValue));
	}
	JobSettings	objSettings	= null;

	public JobSettings getSettings() {
		JobSettings objS = objJSJob.getSettings();
		if (objS == null) {
			throw new JobSchedulerException("is null");
		}
		return objS;
	}

	public String getMailOnError() {
		return objJSJob.getMailOnError();
	}

	public void setMailOnWarning(final String pstrValue, final String pstrDefaultValue) {
		objJSJob.setMailOnWarning(OneOfUs(pstrValue, pstrDefaultValue));
	}

	public String getMailOnWarning() {
		return objJSJob.getMailOnWarning();
	}

	public void setMailOnProcess(final String pstrValue, final String pstrDefaultValue) {
		objJSJob.setMailOnProcess(OneOfUs(pstrValue, pstrDefaultValue));
		setDirty();
	}

	public String getMailOnProcess() {
		return objJSJob.getMailOnProcess();
	}

	public void setMailOnSuccess(final String pstrValue, final String pstrDefaultValue) {
		objJSJob.setMailOnSuccess(OneOfUs(pstrValue, pstrDefaultValue));
	}

	public String getMailOnSuccess() {
		return objJSJob.getMailOnSuccess();
	}

	public void setMailOnDelayAfterError(final String pstrValue, final String pstrDefaultValue) {
		objJSJob.setMailOnDelayAfterError(OneOfUs(pstrValue, pstrDefaultValue));
	}

	public String getMailOnDelayAfterError() {
		return objJSJob.getMailOnDelayAfterError();
	}

	public void setLogMailTo(final String pstrValue, final String pstrDefaultValue) {
		objJSJob.setLogMailTo(OneOfUs(pstrValue, pstrDefaultValue));
	}

	public String getLogMailTo() {
		return objJSJob.getLogMailTo();
	}

	public void setLogMailCC(final String pstrValue, final String pstrDefaultValue) {
		objJSJob.setLogMailCc(OneOfUs(pstrValue, pstrDefaultValue));
	}

	public String getLogMailCC() {
		return objJSJob.getLogMailCC();
	}

	public void setLogMailBcc(final String pstrValue, final String pstrDefaultValue) {
		objJSJob.setLogMailBcc(OneOfUs(pstrValue, pstrDefaultValue));
	}

	public String getLogMailBCC() {
		return objJSJob.getLogMailBCC();
	}

	public String OneOfUs(final String pstrValue, final String pstrDefaultValue) {
		if (isNotEmpty(pstrValue)) {
			return pstrValue;
		}
		return pstrDefaultValue;
	}

	public boolean isSetbackDelay() {
		return objJSJob.getDelayOrderAfterSetback().size() > 0;
	}

	public void fillSetbacks(final Table table) {
		table.removeAll();
		for (DelayOrderAfterSetback objItem : objJSJob.getDelayOrderAfterSetback()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "" + objItem.getSetbackCount());
			item.setText(1, objItem.getIsMaximum());
			String s = objItem.getDelay();
			if (s.equals("00")) {
				s = "0";
			}
			item.setText(2, s);
		}
	}

	public void newErrorDelays(final Table errorDelays) {
		//	   TODO     TableItem[] items = errorDelays.getItems();
		//	        for (int i = items.length; i >= 0; i--) {
		//	            deleteErrorDelay(i);
		//	        }
		//
		//	        for (int i = 0; i < items.length; i++) {
		//	            newErrorDelay();
		//	            applyErrorDelay(items[i].getText(0), items[i].getText(1));
		//	        }
	}

	public void newSetbacks(final Table setback) {
		//	 TODO       TableItem[] items = setback.getItems();
		//	        for (int i = items.length; i >= 0; i--) {
		//	            deleteSetbackDelay(i);
		//	        }
		//
		//	        for (int i = 0; i < items.length; i++) {
		//	            newSetbackDelay();
		//	            applySetbackDelay(items[i].getText(0), items[i].getText(1).equalsIgnoreCase("yes"), items[i].getText(2));
		//	        }
		//	        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void newSetbackDelay() {
		//	 TODO       _setback = new Element("delay_order_after_setback");
		//	        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void selectSetbackDelay(final int index) {
		//	 TODO       if (index >= 0 && index < _setbacks.size())
		//	            _setback = (Element) _setbacks.get(index);
	}

	public void applySetbackDelay(final String setbackCount, final boolean maximum, final String delay) {
		//	 TODO       Utils.setAttribute("setback_count", setbackCount, _setback, _dom);
		//	        Utils.setAttribute("is_maximum", maximum, _setback, _dom);
		//	        Utils.setAttribute("delay", delay, _setback, _dom);
		//	        if (!_setbacks.contains(_setback))
		//	            _setbacks.add(_setback);
		//	        _dom.setChanged(true);
		//	        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void deleteSetbackDelay(final int index) {
		//	 TODO       if (index >= 0 && index < _setbacks.size()) {
		//	            _setbacks.remove(index);
		//	            _setback = null;
		//	            _dom.setChanged(true);
		//	            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		//	        }
	}

	public String getSetbackCount() {
		return "0";
		//	 TODO       return Utils.getIntegerAsString(Utils.getIntValue("setback_count", -999, _setback));
	}

	public boolean isMaximum() {
		//	        return Utils.isAttributeValue("is_maximum", _setback);
		return false;
	}

	public String getSetbackCountHours() {
		return "";
		//	        return Utils.getIntegerAsString(Utils.getHours(_setback.getAttributeValue("delay"), -999));
	}

	public String getSetbackCountMinutes() {
		return "";
		//	        return Utils.getIntegerAsString(Utils.getMinutes(_setback.getAttributeValue("delay"), -999));
	}

	public String getSetbackCountSeconds() {
		return "";
		//	        return Utils.getIntegerAsString(Utils.getSeconds(_setback.getAttributeValue("delay"), -999));
	}

	// error count
	public boolean isErrorDelay() {
		// TODO complete implemenatation
		//		if (_errorDelays != null) {
		//			return _errorDelays.size() > 0;
		//		}
		return false;
	}

	public String getDelay(final Element e) {
		String delay = e.getAttributeValue("delay");
		if (delay == null || delay.equals(""))
			delay = "00:00";
		if (delay.equalsIgnoreCase("stop"))
			return "stop";
		else {
			int hours = Utils.getHours(delay, 0);
			int minutes = Utils.getMinutes(delay, 0);
			int seconds = Utils.getSeconds(delay, 0);
			return Utils.getTime(hours, minutes, seconds, true);
		}
	}

	public void newErrorDelay() {
		//	 TODO       _errorDelay = new Element("delay_after_error");
		//	        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void selectErrorDelay(final int index) {
		//	        if (index >= 0 && index < _errorDelays.size())
		//	            _errorDelay = (Element) _errorDelays.get(index);
	}

	public void applyErrorDelay(final String errorCount, final String delay) {
		//	        Utils.setAttribute("error_count", errorCount, _errorDelay, _dom);
		//	        Utils.setAttribute("delay", delay, _errorDelay, _dom);
		//	        if (!_errorDelays.contains(_errorDelay))
		//	            _errorDelays.add(_errorDelay);
		//	        _dom.setChanged(true);
		//	        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void deleteErrorDelay(final int index) {
		//	        if (index >= 0 && index < _errorDelays.size()) {
		//	            _errorDelays.remove(index);
		//	            _errorDelay = null;
		//	            _dom.setChanged(true);
		//	            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		//	        }
	}

	public String getErrorCount() {
		//	        return Utils.getIntegerAsString(Utils.getIntValue("error_count", -999, _errorDelay));
		return "";
	}

	public boolean isStop() {
		//	        if (_errorDelay != null && _errorDelay.getAttributeValue("delay") != null)
		//	            return _errorDelay.getAttributeValue("delay").equalsIgnoreCase("stop");
		//	        else
		//	            return false;
		return false;
	}

	public String getErrorCountHours() {
		return "00"; // Utils.getIntegerAsString(Utils.getHours(_errorDelay.getAttributeValue("delay"), -999));
	}

	public String getErrorCountMinutes() {
		return "00"; //  Utils.getIntegerAsString(Utils.getMinutes(_errorDelay.getAttributeValue("delay"), -999));
	}

	public String getErrorCountSeconds() {
		return "00"; //  Utils.getIntegerAsString(Utils.getSeconds(_errorDelay.getAttributeValue("delay"), -999));
	}

	public void fillErrorDelayTable(final Table table) {
		//		table.removeAll();
		//		if (_errorDelays != null) {
		//			Iterator it = _errorDelays.iterator();
		//			while (it.hasNext()) {
		//				Element e = (Element) it.next();
		//				TableItem item = new TableItem(table, SWT.NONE);
		//				item.setText(0, "" + Utils.getIntValue("error_count", e));
		//				item.setText(1, getDelay(e));
		//			}
		//		}
	}
}
