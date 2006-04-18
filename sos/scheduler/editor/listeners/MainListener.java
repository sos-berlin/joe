package sos.scheduler.editor.listeners;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.TextDialog;
import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.Languages;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.forms.BaseForm;
import sos.scheduler.editor.forms.ConfigForm;
import sos.scheduler.editor.forms.DateForm;
import sos.scheduler.editor.forms.DaysForm;
import sos.scheduler.editor.forms.ExecuteForm;
import sos.scheduler.editor.forms.JobChainsForm;
import sos.scheduler.editor.forms.JobForm;
import sos.scheduler.editor.forms.JobOptionsForm;
import sos.scheduler.editor.forms.JobsForm;
import sos.scheduler.editor.forms.MainWindow;
import sos.scheduler.editor.forms.PeriodsForm;
import sos.scheduler.editor.forms.ProcessClassesForm;
import sos.scheduler.editor.forms.RunTimeForm;
import sos.scheduler.editor.forms.ScriptForm;
import sos.scheduler.editor.forms.SecurityForm;
import sos.scheduler.editor.forms.WebservicesForm;

public class MainListener {

	private DomParser _dom;

	private MainWindow _gui;

	private String _filename;

	public MainListener(MainWindow gui, DomParser dom) {
		_gui = gui;
		_dom = dom;
	}

	public int message(String message, int style) {
		MessageBox mb = new MessageBox(_gui.getSShell(), style);
		mb.setMessage(message);
		return mb.open();
	}

	public void treeFillMain(Tree tree, Composite c) {
		tree.removeAll();

		Element config = _dom.getRoot().getChild("config");

		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.CONFIG, config, Options
				.getHelpURL("config")));
		item.setText("Config");
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.BASE, config, Options
				.getHelpURL("base")));
		item.setText("Base Files");
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.SECURITY, config, Options
				.getHelpURL("security"), "security"));
		item.setText("Security");
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.PROCESS_CLASSES, config, Options
				.getHelpURL("process_classes"), "process_classes"));
		item.setText("Process Classes");
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.SCRIPT, config, Options.getHelpURL("start_script"), "script"));
		item.setText("Start Script");
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.WEBSERVICES, config, Options
				.getHelpURL("web_services"), "web_services"));
		item.setText("Web Services");
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.HOLIDAYS, config, Options
				.getHelpURL("holidays"), "holidays"));
		item.setText("Holidays");
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.JOBS, config, Options
				.getHelpURL("jobs"), "jobs"));
		item.setText("Jobs");
		treeFillJobs(item);
		item.setExpanded(true);
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.JOB_CHAINS, config, Options
				.getHelpURL("job_chains"), "job_chains"));
		item.setText("Job Chains");

		tree.setSelection(new TreeItem[] { tree.getItem(0) });
		treeSelection(tree, c);
	}

	public void treeFillJobs(TreeItem parent) {
		parent.removeAll();
		Element jobs = _dom.getRoot().getChild("config").getChild("jobs");
		if (jobs != null) {
			Iterator it = jobs.getChildren().iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element element = (Element) o;
					TreeItem i = new TreeItem(parent, SWT.NONE);
					String name = Utils.getAttributeValue("name", element);
					String job = "Job: " + name;
					job += _dom.isJobDisabled(name) ? " (Disabled)" : "";

					i.setText(job);
					i.setData(new TreeData(Editor.JOB, element, Options
							.getHelpURL("job")));
					treeFillJob(i, element, false);
				}
			}
		}
		parent.setExpanded(true);
	}

	public void treeFillJob(TreeItem parent, Element job, boolean expand) {
		parent.removeAll();
		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setText("Execute");
		item.setData(new TreeData(Editor.EXECUTE, job, Options
				.getHelpURL("job.execute")));

		item = new TreeItem(parent, SWT.NONE);
		item.setText("Monitor");
		item.setData(new TreeData(Editor.MONITOR, job, Options
				.getHelpURL("job.monitor"), "monitor"));

		item = new TreeItem(parent, SWT.NONE);
		item.setText("Run Options");
		item.setData(new TreeData(Editor.OPTIONS, job, Options
				.getHelpURL("job.options")));

		Element runtime = job.getChild("run_time");
		// create runtime tag
		if (runtime == null) {
			runtime = new Element("run_time");
			job.addContent(runtime);
		}
		if (runtime != null) {
			TreeItem run = new TreeItem(parent, SWT.NONE);
			run.setText("Run Time");
			run.setData(new TreeData(Editor.RUNTIME, job, Options
					.getHelpURL("job.run_time"), "run_time"));

			item = new TreeItem(run, SWT.NONE);
			item.setText("Everyday");
			item.setData(new TreeData(Editor.EVERYDAY, runtime, Options
					.getHelpURL("job.run_time.everyday")));

			item = new TreeItem(run, SWT.NONE);
			item.setText("Weekdays");
			item.setData(new TreeData(Editor.WEEKDAYS, runtime, Options
					.getHelpURL("job.run_time.weekdays"), "weekdays"));
			treeFillDays(item, runtime, 0, false);

			item = new TreeItem(run, SWT.NONE);
			item.setText("Monthdays");
			item.setData(new TreeData(Editor.MONTHDAYS, runtime, Options
					.getHelpURL("job.run_time.monthdays"), "monthdays"));
			treeFillDays(item, runtime, 1, false);

			item = new TreeItem(run, SWT.NONE);
			item.setText("Ultimos");
			item.setData(new TreeData(Editor.ULTIMOS, runtime, Options
					.getHelpURL("job.run_time.ultimos"), "ultimos"));
			treeFillDays(item, runtime, 2, false);

			item = new TreeItem(run, SWT.NONE);
			item.setText("Specific Days");
			item.setData(new TreeData(Editor.DAYS, runtime, Options
					.getHelpURL("job.run_time.specific_days")));
			if (runtime != null)
				treeFillDays(item, runtime, 3, false);
		}

		parent.setExpanded(expand);
	}

	public void treeFillDays(TreeItem parent, Element element, int type,
			boolean expand) {
		if (element != null) {
			if (type == DaysListener.WEEKDAYS || type == DaysListener.MONTHDAYS
					|| type == DaysListener.ULTIMOS)
				new DaysListener(_dom, element, type).fillTreeDays(parent,
						expand);
			else if (type == 3)
				new DateListener(_dom, element, 1).fillTreeDays(parent, expand);
			else
				System.out.println("Invalid type = " + type
						+ " for filling the days in the tree!");
		}
	}

	public boolean treeSelection(Tree tree, Composite c) {
		try {
			if (tree.getSelectionCount() > 0) {

				// dispose the old form
				Control[] children = c.getChildren();
				for (int i = 0; i < children.length; i++) {
					if (!applyChanges(children[i]))
						return false;
					children[i].dispose();
				}

				TreeItem item = tree.getSelection()[0];
				TreeData data = (TreeData) item.getData();

				switch (data.getType()) {
				case Editor.CONFIG:
					new ConfigForm(c, SWT.NONE, _dom);
					break;
				case Editor.SECURITY:
					new SecurityForm(c, SWT.NONE, _dom, data.getElement());
					break;
				case Editor.BASE:
					new BaseForm(c, SWT.NONE, _dom);
					break;
				case Editor.PROCESS_CLASSES:
					new ProcessClassesForm(c, SWT.NONE, _dom, data.getElement());
					break;
				case Editor.MONITOR:
					new ScriptForm(c, SWT.NONE, "Monitor", _dom, data
							.getElement(), data.getType());
					break;
				case Editor.SCRIPT:
					new ScriptForm(c, SWT.NONE, "Start Script", _dom, data
							.getElement(), data.getType());
					break;
				case Editor.JOB:
					new JobForm(c, SWT.NONE, _dom, data.getElement(), _gui);
					break;
				case Editor.EXECUTE:
					new ExecuteForm(c, SWT.NONE, _dom, data.getElement());
					break;
				case Editor.RUNTIME:
					new RunTimeForm(c, SWT.NONE, _dom, data.getElement(), _gui);
					break;
				case Editor.WEEKDAYS:
					new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui,
							DaysListener.WEEKDAYS);
					break;
				case Editor.MONTHDAYS:
					new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui,
							DaysListener.MONTHDAYS);
					break;
				case Editor.ULTIMOS:
					new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui,
							DaysListener.ULTIMOS);
					break;
				case Editor.EVERYDAY:
				case Editor.PERIODS:
					new PeriodsForm(c, SWT.NONE, _dom, data.getElement());
					break;
				case Editor.JOBS:
					new JobsForm(c, SWT.NONE, _dom, _gui, this);
					break;
				case Editor.HOLIDAYS:
					new DateForm(c, SWT.NONE, DateListener.HOLIDAY, _dom, data
							.getElement(), _gui);
					break;
				case Editor.DAYS:
					new DateForm(c, SWT.NONE, DateListener.DATE, _dom, data
							.getElement(), _gui);
					break;
				case Editor.WEBSERVICES:
					new WebservicesForm(c, SWT.NONE, _dom, data.getElement());
					break;
				case Editor.OPTIONS:
					new JobOptionsForm(c, SWT.NONE, _dom, data.getElement());
					break;
				case Editor.JOB_CHAINS:
					new JobChainsForm(c, SWT.NONE, _dom, data.getElement());
					break;
				default:
					System.out.println("no form found for " + item.getText());
				}

				c.layout();
			}
		} catch (Exception e) {
			e.printStackTrace();
			message(e.getMessage(), SWT.ICON_ERROR);
		}

		return true;
	}

	public boolean applyChanges(Control c) {
		if (c instanceof IUnsaved) {
			// looking for unsaved changes...
			IUnsaved unsaved = (IUnsaved) c;
			if (unsaved.isUnsaved()) {
				int ok = message(Messages
						.getString("MainListener.apply_changes"), //$NON-NLS-1$
						SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
				if (ok == SWT.CANCEL)
					return false;
				else if (ok == SWT.YES)
					unsaved.apply();
			}
		}
		return true;
	}

	public boolean openFile(Tree tree, Composite c) {
		return openFile(tree, c, null);
	}

	public boolean openFile(Tree tree, Composite c, String filename) {
		try {
			// ask to save changes
			if (!continueAnyway())
				return false;

			// open file dialog
			if (filename == null || filename.equals("")) {
				FileDialog fdialog = new FileDialog(_gui.getSShell(), SWT.OPEN);
				// fdialog.setFilterExtensions(new String[] {"*.xml"});
				filename = fdialog.open();
			}

			if (filename != null && !filename.equals("")) { //$NON-NLS-1$
				File file = new File(filename);
				// check the file
				if (!file.exists())
					message(Messages.getString("MainListener.fileNotFound"), //$NON-NLS-1$
							SWT.ICON_WARNING | SWT.OK);
				else if (!file.canRead())
					message(Messages
							.getString("MainListener.fileReadProtected"), //$NON-NLS-1$
							SWT.ICON_WARNING | SWT.OK);
				else { // open it...
					int cont = SWT.NO;
					try {
						_dom.read(file);
					} catch (JDOMException e) {
						cont = message(Messages.getString(
								"MainListener.validationError",
								new String[] { file.getAbsolutePath(),
										e.getMessage() }), SWT.ICON_WARNING
								| SWT.YES | SWT.NO);
						if (cont == SWT.NO)
							return false;
					} catch (IOException e) {
						message(Messages.getString(
								"MainListener.errorReadingFile",
								new String[] { file.getAbsolutePath(),
										e.getMessage() }), SWT.ICON_ERROR
								| SWT.OK);
						return false;
					}

					if (cont == SWT.YES) { // validation error, continue
											// anyway...
						if (!_dom.read(file, false)) { // unknown file
							message(Messages
									.getString("MainListener.unknownXML"),
									SWT.ICON_WARNING | SWT.OK);
							return false;
						}
					}

					_filename = filename;
					treeFillMain(tree, c);
				}
			}
			_gui.getSShell().setText("Job Scheduler Editor [" + filename + "]");

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
		}
		return false;
	}

	private void createBackup()  {
		String backupPath;
		String f;
		if (_filename == null) {
			f = "";
		}else {
			f = _filename;
		}
		try {
		if (Options.getBackupEnabled() && !f.equals("")) {
			 if (Options.getBackupDir().equals("")) {
				 backupPath = new File(f).getPath();
			 }else {
				 backupPath = Options.getBackupDir();
			 }
   		 File outFile = new File(backupPath  + "/" +  new File(_filename).getName() + ".bak");
		   File inFile =  new File(_filename);
	     if (inFile.exists()){
          if (outFile.exists()  ){ // Wenn destination schon existiert, dann vorher löschen.
    	      outFile.delete(); 
          }
          boolean ok = inFile.renameTo( outFile );
          if( !ok )  throw new Exception( "Import-Datei " + inFile + " kann nicht nach " + outFile + " kopiert werden." );
   		}
		}
	} catch (Exception e) {
		e.printStackTrace();
		message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
	}
	}
	
	public boolean saveFile(boolean saveas) {
	
		try {
			String filename = _filename;
			if (saveas || filename == null || filename.equals("")) { //$NON-NLS-1$
				FileDialog fdialog = new FileDialog(_gui.getSShell(), SWT.SAVE);
				filename = fdialog.open();
				if (filename == null)
					return false;
			}
			File file = new File(filename);

			if ( !filename.equals(_filename) && file.exists()) {
				int ok = message(Messages
						.getString("MainListener.doFileOverwrite"), //$NON-NLS-1$
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				if (ok == SWT.NO)
					return false;
			} 
			

			if (!file.canWrite()) {
				message(Messages.getString("MainListener.fileWriteProtected"), //$NON-NLS-1$
						SWT.ICON_WARNING | SWT.OK);
				return false;
			} else {
				createBackup();
				
				_dom.write(filename, this);
				_filename = filename;
		    _gui.getSShell().setText("Job Scheduler Editor [" + filename + "]");

			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			message(e.getMessage(), SWT.ICON_ERROR | SWT.OK);
			return false;
		}
	}

	public boolean newScheduler(Tree tree, Composite c) {
		if (continueAnyway()) {
			_dom.init();
			treeFillMain(tree, c);
			_filename = null;
			return true;
		}
		return false;
	}

	private boolean continueAnyway() {
		if (_dom.isChanged()) {
			int ok = message(Messages.getString("MainListener.contentChanged"), //$NON-NLS-1$
					SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);

			if (ok == SWT.CANCEL)
				return false;

			else if (ok == SWT.YES && !saveFile(false))
				return false;
		}
		return true;
	}

	public boolean close() {
		return continueAnyway();
	}

	public void loadOptions() {
		String msg = Options.loadOptions(getClass());
		if (msg != null)
			message("No options file " + Options.getDefaultOptionFilename()  
					+ " found - using defaults!\n" + msg, SWT.ICON_ERROR
					| SWT.OK);
	}

	public void loadMessages() {
		if (!Messages.setResource(new Locale(Options.getLanguage()))) {
			message("The resource bundle " + Messages.getBundle()
					+ " for the language " + Options.getLanguage()
					+ " was not found!", SWT.ICON_ERROR | SWT.OK);
		}
		if (_gui.getForm() != null){
		_gui.getForm().setToolTipText();
		}
	}

	public void saveOptions() {
		String msg = Options.saveProperties();
		if (msg != null)
			message("Options cannot be saved!\n" + msg, SWT.ICON_ERROR | SWT.OK);
	}

	public void setLanguages(Menu menu) {
		boolean found = false;
		MenuItem defaultItem = null;
		HashMap langs = Languages.getLanguages();
		Iterator it = langs.keySet().iterator();

		while (it.hasNext()) {
			String key = (String) it.next();
			String val = (String) langs.get(key);

			MenuItem item = new MenuItem(menu, SWT.RADIO);
			item.setText(key);
			item.setData(val);
			if (Options.getLanguage().equals(val)) {
				found = true;
				item.setSelection(true);
			}

			if (Options.getDefault("editor.language").equals(val))
				defaultItem = item;

			item.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					MenuItem item = (MenuItem) e.widget;
					if (item.getSelection()) {
						String lang = (String) item.getData();
						Options.setLanguage(lang);
						loadMessages();
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
		}

		if (!found) {
			String def = Options.getDefault("editor.language");
			message("The language " + Options.getLanguage()
					+ " was not found - setting to " + def, SWT.ICON_WARNING
					| SWT.OK);
			Options.setLanguage(def);
			if (defaultItem != null)
				defaultItem.setSelection(true);
		}
	}

	public String getHelpURL(Tree tree, String lang) {
		String url = null;
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			if (data != null && data.getHelpKey() != null)
				url = data.getHelpKey().replaceAll("\\{lang\\}}", lang);
		}

		if (url == null)
			url = Options.getHelpURL("index").replaceAll("\\{lang\\}", lang);

		return (Options.getHelpURL("maindir") + url).replaceAll("\\{scheduler_home\\}", Options.getSchedulerHome()).replaceAll("\\{lang\\}", lang);
	}

	public void openHelp(Tree tree) {
		String lang = Options.getLanguage();
		String url = getHelpURL(tree, lang);

		try {
		 
			url = new File(url).toURL().toString();

			Program prog = Program.findProgram("html");
			if (prog != null)
				prog.execute(url);
			else {
				Runtime.getRuntime().exec(Options.getBrowserExec(url, lang));
			}
		} catch (Exception e) {
			e.printStackTrace();
			message(Messages.getString("MainListener.cannot_open_help",
					new String[] { url, lang, e.getMessage() }), SWT.ICON_ERROR
					| SWT.OK);
		}
	}

	public void showAbout() {
		TextDialog dialog = new TextDialog(_gui.getSShell());
		dialog.setText("About Job Scheduler Editor");
		String message = Messages.getString("MainListener.aboutText", Options.getVersion() + "\nSchema-Version:" + Options.getSchemaVersion() + "\n");
		dialog.setContent(message, SWT.CENTER);
		dialog.getStyledText().setEnabled(false);
		StyleRange bold = new StyleRange();
		bold.start = 0;
		bold.length = message.indexOf("\n");
		bold.fontStyle = SWT.BOLD;
		dialog.getStyledText().setStyleRange(bold);
		dialog.open();
	}
	
}
