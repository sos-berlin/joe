package sos.scheduler.editor.conf.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.LanguageDescriptorList;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.util.SOSClassUtil;
import sos.util.SOSDate;

public class JobsListener extends JOEListener {

//	private SchedulerDom		_dom	= null;
//	private ISchedulerUpdate	_main	= null;
	private Element				_config	= null;
	private Element				_jobs	= null;
	private List<Element>		_list	= null;

	public JobsListener(final SchedulerDom dom, final ISchedulerUpdate update) {

		_dom = dom;
		_main = update;
		if (_dom.isLifeElement()) {

		}
		else {
			_config = _dom.getRoot().getChild("config");
			_jobs = _config.getChild("jobs");

			if (_jobs != null) {
				_list = _jobs.getChildren("job");
			}
		}

	}

	private void initJobs() {
		if (!_dom.isLifeElement()) {
			if (_config.getChild("jobs") == null) {
				Element _jobs = new Element("jobs");
				_config.addContent(_jobs);
				_list = _jobs.getChildren("job");
			}
			else {
				_jobs = _config.getChild("jobs");
				_list = _jobs.getChildren("job");
			}
		}

	}

	public void populateJobsTable(final Table table) {

		table.removeAll();
		if (_list != null) {
			for (Object o : _list) {
				if (o instanceof Element) {
					Element e = (Element) o;
					TableItem item = new TableItem(table, SWT.NONE);
					item.setData(e);
					String name = Utils.getAttributeValue("name", e);
					if (!Utils.isElementEnabled("job", _dom, e)) {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					}
					else {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					}
					item.setChecked(!isEnabled(e));
					item.setText(1, name);
					item.setText(2, Utils.getAttributeValue("title", e));
					item.setText(3, Utils.getAttributeValue("spooler_id", e));
					item.setText(4, Utils.getAttributeValue("process_class", e));
					item.setText(5, Utils.isAttributeValue("order", e) ? "Yes" : "No");
				}
			}
		}

	}

	public void newJob(final boolean isOrder) {

		// TODO: enable usage of Template for job-definition

		Element job = new Element("job");
//		Utils.setAttribute("name", "job" + (table.getItemCount() + 1), job);
		String strNewJobName = "job_" + _list.size() + 1;
		Utils.setAttribute("name", strNewJobName, job);

		if (isOrder) {
			Utils.setAttribute("order", "yes", job);
			Utils.setAttribute("stop_on_error", "no", job);
		}
		Element script = new Element("script");
		job.addContent(script);
		try {
			script.addContent("echo created by JOE, at " + SOSDate.getCurrentTimeAsString() + "\n");
		}
		catch (Exception e) {
		}
		Utils.setAttribute("language", LanguageDescriptorList.getDefaultLanguage().getLanguageName(), script);
		Element runtime = new Element("run_time");
		job.addContent(runtime);

		if (_list == null) {
			initJobs();
		}
		_list.add(job);

//		populateJobsTable(table);
//		table.setSelection(table.getItemCount() - 1);
		_main.updateJobs();
		_main.expandItem(strNewJobName);
		_dom.setChanged(true);

		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", job), SchedulerDom.NEW);

		// TODO: switch to job-editor form
	}

	public Element createJobElement(final java.util.HashMap attr) {
		Element job = new Element("job");

		if (attr.get("name") != null && attr.get("name").toString().length() > 0)
			Utils.setAttribute("name", attr.get("name").toString(), job);

		if (attr.get("title") != null && attr.get("title").toString().length() > 0)
			Utils.setAttribute("title", attr.get("title").toString(), job);

		if (attr.get("order") != null && attr.get("order").toString().length() > 0) {
			Utils.setAttribute("order", attr.get("order").toString(), job);
			if (attr.get("order").toString().equalsIgnoreCase("yes")) {
				Utils.setAttribute("stop_on_error", "no", job);
			}
		}

		if (attr.get("tasks") != null && attr.get("tasks").toString().length() > 0 && !attr.get("tasks").equals("unbounded"))
			Utils.setAttribute("tasks", attr.get("tasks").toString(), job);

		Element params = new Element("params");
		job.addContent(0, params);
		ArrayList listOfParams = new ArrayList();
		if (attr.get("params") != null) {
			listOfParams = (ArrayList) attr.get("params");
		}
		Element param = null;
		for (int i = 0; i < listOfParams.size(); i++) {
			HashMap p = (HashMap) listOfParams.get(i);
			param = new Element("param");

			param.setAttribute("name", p.get("name") != null ? p.get("name").toString() : "");
			param.setAttribute("value", p.get("default_value") != null ? p.get("default_value").toString() : "");
			params.addContent(param);
		}

		//config Description Element bilden
		if (attr.get("filename") != null && attr.get("filename").toString().length() > 0 && !attr.get("filename").equals("..")) {
			Element desc = new Element("description");
			Element include = new Element("include");
			Utils.setAttribute("file", attr.get("filename").toString(), include);
			//include.setAttribute("file", attr.get("filename").toString());
			desc.addContent(include);
			job.addContent(desc);
		}

		//script Element bilden
		if (attr.get("script") != null && attr.get("script").toString().equals("script")) {
			Element script = new Element("script");
			if (attr.get("script_language") != null && attr.get("script_language").toString().length() > 0)
				Utils.setAttribute("language", attr.get("script_language").toString(), script);

			if (attr.get("script_java_class") != null && attr.get("script_java_class").toString().length() > 0)
				Utils.setAttribute("java_class", attr.get("script_java_class").toString(), script);

			if (attr.get("script_com_class") != null && attr.get("script_com_class").toString().length() > 0)
				Utils.setAttribute("com_class", attr.get("script_com_class").toString(), script);

			if (attr.get("script_filename") != null && attr.get("script_filename").toString().length() > 0)
				Utils.setAttribute("filename", attr.get("script_filename").toString(), script);

			if (attr.get("script_use_engine") != null && attr.get("script_use_engine").toString().length() > 0)
				Utils.setAttribute("use_engine", attr.get("script_use_engine").toString(), script);

			if (attr.get("script_include_file") != null) {
				ArrayList listOfIncludes = (ArrayList) attr.get("script_include_file");
				for (int i = 0; i < listOfIncludes.size(); i++) {
					if (listOfIncludes.get(i) != null) {
						Element include = new Element("include");
						include.setNamespace(_dom.getNamespace());
						Utils.setAttribute("file", listOfIncludes.get(i).toString(), include);
						script.addContent(include);
					}
				}

			}
			if (script.getAttributes().size() > 0)
				job.addContent(script);
		}

		if (attr.get("monitor") != null && attr.get("monitor").toString().equals("monitor")) {
			//monitor element bilden
			Element monitor = new Element("monitor");

			Element mon_script = new Element("script");

			if (attr.get("monitor_script_language") != null && attr.get("monitor_script_language").toString().length() > 0)
				Utils.setAttribute("language", attr.get("monitor_script_language").toString(), mon_script);

			if (attr.get("monitor_script_java_class") != null && attr.get("monitor_script_java_class").toString().length() > 0)
				Utils.setAttribute("java_class", attr.get("monitor_script_java_class").toString(), mon_script);

			if (attr.get("monitor_script_com_class") != null && attr.get("monitor_script_com_class").toString().length() > 0)
				Utils.setAttribute("com_class", attr.get("monitor_script_com_class").toString(), mon_script);

			if (attr.get("monitor_script_filename") != null && attr.get("monitor_script_filename").toString().length() > 0)
				Utils.setAttribute("filename", attr.get("monitor_script_filename").toString(), mon_script);

			if (attr.get("monitor_script_use_engine") != null && attr.get("monitor_script_use_engine").toString().length() > 0)
				Utils.setAttribute("use_engine", attr.get("monitor_script_use_engine").toString(), mon_script);

			if (attr.get("monitor_script_include_file") != null) {
				ArrayList listOfMonIncludes = (ArrayList) attr.get("monitor_script_include_file");
				for (int i = 0; i < listOfMonIncludes.size(); i++) {
					Element mon_include = new Element("include");
					mon_include.setNamespace(_dom.getNamespace());
					Utils.setAttribute("file", listOfMonIncludes.get(i).toString(), mon_include);
					mon_script.addContent(mon_include);
				}
			}

			monitor.addContent(mon_script);

			if (mon_script.getAttributes().size() > 0)
				job.addContent(monitor);
		}

		if (attr.get("process") != null && attr.get("process").toString().equals("process")) {
			Element process = new Element("process");

			if (attr.get("process_file") != null && attr.get("process_file").toString().length() > 0)
				Utils.setAttribute("file", attr.get("process_file").toString(), process);

			if (attr.get("process_log") != null && attr.get("process_log").toString().length() > 0)
				Utils.setAttribute("log_file", attr.get("process_log").toString(), process);

			if (attr.get("process_param") != null && attr.get("process_param").toString().length() > 0)
				Utils.setAttribute("param", attr.get("process_param").toString(), process);

			if (attr.get("process_environment") != null) {
				Element environment = new Element("environment");
				ArrayList listOfEnv = (ArrayList) attr.get("process_environment");
				for (int i = 0; i < listOfEnv.size(); i++) {
					HashMap h = (HashMap) listOfEnv.get(i);
					if (h.get("name") != null) {
						Element variable = new Element("variable");
						variable.setAttribute("name", h.get("name").toString());
						variable.setAttribute("value", h.get("value") != null ? h.get("value").toString() : "");
						environment.addContent(variable);
					}
				}

				process.addContent(environment);
			}
			job.addContent(process);
		}

		//runtime
		Element runtime = new Element("run_time");
		//runtime.setAttribute("let_run", "no");
		job.addContent(runtime);
		return job;
	}

	public Element createParams(final java.util.HashMap attr, final Element parent) {

		Element params = null;
		if (parent.getChildren("params").size() == 0) {
			params = new Element("params");
			parent.addContent(0, params);
		}
		else {
			params = parent.getChild("params");
		}

		ArrayList listOfParams = new ArrayList();
		if (attr.get("params") != null)
			listOfParams = (ArrayList) attr.get("params");
		//Parameters bilden
		Element param = null;
		for (int i = 0; i < listOfParams.size(); i++) {
			HashMap p = (HashMap) listOfParams.get(i);
			if (p.get("name") != null && !existParams(params, p.get("name").toString())) {
				param = new Element("param");
				param.setAttribute("name", p.get("name") != null ? p.get("name").toString() : "");
				param.setAttribute("value", p.get("default_value") != null ? p.get("default_value").toString() : "");
				params.addContent(param);
			}
		}
		return parent;
	}

	/**
	 * Starten des Wizard für bestehenden Job.
	 *
	 * Einstellungen aus einer Jobbeschreibung werden dem jobelement hinzugefügt,
	 * soweit das Jobelement diese Einstellungen nicht hat.
	 *
	 * @param attr -> Einstellungen aus einer Jobbeschreibung
	 * @param job -> Job ist der bestehende Job
	 * @return
	 */
	public Element createJobElement(final java.util.HashMap attr, final Element job) {

		//		config Job Attribute bilden
		if (Utils.getAttributeValue("title", job).length() == 0) {
			if (attr.get("title") != null && attr.get("title").toString().length() > 0)
				Utils.setAttribute("title", attr.get("title").toString(), job);
		}

		if (Utils.getAttributeValue("order", job).length() == 0) {
			if (attr.get("order") != null && attr.get("order").toString().length() > 0)
				Utils.setAttribute("order", attr.get("order").toString(), job);
		}

		if (Utils.getAttributeValue("tasks", job).length() == 0) {
			if (attr.get("tasks") != null && attr.get("tasks").toString().length() > 0 && !attr.get("tasks").equals("unbounded"))
				Utils.setAttribute("tasks", attr.get("tasks").toString(), job);
		}

		createParams(attr, job);

		//config Description Element bilden
		if (job.getChild("description") == null) {
			if (attr.get("filename") != null && attr.get("filename").toString().length() > 0 && !attr.get("filename").equals("..")) {
				Element desc = new Element("description");
				Element include = new Element("include");
				Utils.setAttribute("file", attr.get("filename").toString(), include);
				desc.addContent(include);
				job.addContent(desc);
			}
		}

		//script Element bilden
		if (job.getChild("script") == null) {
			if (attr.get("script") != null && attr.get("script").toString().equals("script")) {
				Element script = new Element("script");
				if (attr.get("script_language") != null && attr.get("script_language").toString().length() > 0)
					Utils.setAttribute("language", attr.get("script_language").toString(), script);

				if (attr.get("script_java_class") != null && attr.get("script_java_class").toString().length() > 0)
					Utils.setAttribute("java_class", attr.get("script_java_class").toString(), script);

				if (attr.get("script_com_class") != null && attr.get("script_com_class").toString().length() > 0)
					Utils.setAttribute("com_class", attr.get("script_com_class").toString(), script);

				if (attr.get("script_filename") != null && attr.get("script_filename").toString().length() > 0)
					Utils.setAttribute("filename", attr.get("script_filename").toString(), script);

				if (attr.get("script_use_engine") != null && attr.get("script_use_engine").toString().length() > 0)
					Utils.setAttribute("use_engine", attr.get("script_use_engine").toString(), script);

				if (attr.get("script_include_file") != null) {
					ArrayList listOfIncludes = (ArrayList) attr.get("script_include_file");
					for (int i = 0; i < listOfIncludes.size(); i++) {
						if (listOfIncludes.get(i) != null) {
							Element include = new Element("include");
							include.setNamespace(_dom.getNamespace());
							Utils.setAttribute("file", listOfIncludes.get(i).toString(), include);
							script.addContent(include);
						}
					}

				}
				if (script.getAttributes().size() > 0)
					job.addContent(script);
			}
		}

		if (job.getChild("monitor") == null) {
			if (attr.get("monitor") != null && attr.get("monitor").toString().equals("monitor")) {
				//monitor element bilden
				Element monitor = new Element("monitor");

				Element mon_script = new Element("script");

				if (attr.get("monitor_script_language") != null && attr.get("monitor_script_language").toString().length() > 0)
					Utils.setAttribute("language", attr.get("monitor_script_language").toString(), mon_script);

				if (attr.get("monitor_script_java_class") != null && attr.get("monitor_script_java_class").toString().length() > 0)
					Utils.setAttribute("java_class", attr.get("monitor_script_java_class").toString(), mon_script);

				if (attr.get("monitor_script_com_class") != null && attr.get("monitor_script_com_class").toString().length() > 0)
					Utils.setAttribute("com_class", attr.get("monitor_script_com_class").toString(), mon_script);

				if (attr.get("monitor_script_filename") != null && attr.get("monitor_script_filename").toString().length() > 0)
					Utils.setAttribute("filename", attr.get("monitor_script_filename").toString(), mon_script);

				if (attr.get("monitor_script_use_engine") != null && attr.get("monitor_script_use_engine").toString().length() > 0)
					Utils.setAttribute("use_engine", attr.get("monitor_script_use_engine").toString(), mon_script);

				if (attr.get("monitor_script_include_file") != null) {
					ArrayList listOfMonIncludes = (ArrayList) attr.get("monitor_script_include_file");
					for (int i = 0; i < listOfMonIncludes.size(); i++) {
						Element mon_include = new Element("include");
						mon_include.setNamespace(_dom.getNamespace());
						Utils.setAttribute("file", listOfMonIncludes.get(i).toString(), mon_include);
						mon_script.addContent(mon_include);
					}
				}

				monitor.addContent(mon_script);

				if (mon_script.getAttributes().size() > 0)
					job.addContent(monitor);
			}
		}

		if (job.getChild("process") == null) {
			if (attr.get("process") != null && attr.get("process").toString().equals("process")) {
				Element process = new Element("process");

				if (attr.get("process_file") != null && attr.get("process_file").toString().length() > 0)
					Utils.setAttribute("file", attr.get("process_file").toString(), process);

				if (attr.get("process_log") != null && attr.get("process_log").toString().length() > 0)
					Utils.setAttribute("log_file", attr.get("process_log").toString(), process);

				if (attr.get("process_param") != null && attr.get("process_param").toString().length() > 0)
					Utils.setAttribute("param", attr.get("process_param").toString(), process);

				if (attr.get("process_environment") != null) {
					Element environment = new Element("environment");
					ArrayList listOfEnv = (ArrayList) attr.get("process_environment");
					for (int i = 0; i < listOfEnv.size(); i++) {
						HashMap h = (HashMap) listOfEnv.get(i);
						if (h.get("name") != null) {
							Element variable = new Element("variable");
							variable.setAttribute("name", h.get("name").toString());
							variable.setAttribute("value", h.get("value") != null ? h.get("value").toString() : "");
							environment.addContent(variable);
						}
					}

					process.addContent(environment);
				}

				job.addContent(process);

			}
		}

		return job;
	}

	public void newImportJob(final Element job, final int assistentType) {
		try {
			if (!_dom.isLifeElement()) {
				if (_list == null)
					initJobs();
				_list.add(job);
			}
			_dom.setChanged(true);

//			if (Editor.JOB_CHAINS != assistentType && JobsForm.getTable() != null) {
//				populateJobsTable(JobsForm.getTable());
//				JobsForm.getTable().setSelection(JobsForm.getTable().getItemCount() - 1);
//			}
//
			if (_dom.isLifeElement()) {
				List l = job.getAttributes();

				_dom.getRoot().removeContent();
				_dom.getRoot().addContent(job.cloneContent());
				for (int i = 0; i < l.size(); i++) {
					org.jdom.Attribute a = (org.jdom.Attribute) l.get(i);
					_dom.getRoot().setAttribute(a.getName(), a.getValue());
				}
				_main.updateJob(job);
			}
			else {
				_main.updateOrders();
				_main.updateJobs();
			}

			_main.expandItem(Utils.getAttributeValue("name", job));
		}
		catch (Exception e) {
			System.err.print(e);
				new ErrorLog("error in " + SOSClassUtil.getMethodName() + " ; could not start assistent.", e);
		}
	}

	public void newImportJob(final Element job) {

		if (_list == null)
			initJobs();

		_list.add(job);
		_dom.setChanged(true);

//		populateJobsTable(JobsForm.getTable());
//		JobsForm.getTable().setSelection(JobsForm.getTable().getItemCount() - 1);
//
		_main.updateJobs();
		_main.updateOrders();
	}

	public boolean deleteJob(final Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element e = (Element) item.getData();
			e.detach();
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", e), SchedulerDom.DELETE);
			table.remove(index);
			_main.updateJobs();
			if (_list == null)
				initJobs();
			if (_list.size() == 0) {
				_config.removeChild("jobs");
				_jobs = null;
				_list = null;
			}

			if (index >= table.getItemCount())
				index--;
			if (index >= 0) {
				table.setSelection(index);
				return true;
			}
		}
		return false;
	}

	public boolean editJob(final Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element objElement = (Element) item.getData();

			//			new JobMainForm(c, SWT.NONE, _dom, objElement, objSchedulerForm);

			return true;
		}

		return false;
	}

	public boolean isEnabled(final Element e) {
		String enabledAtt = Utils.getAttributeValue("enabled", e);
		boolean enabled = enabledAtt.equalsIgnoreCase("yes") || enabledAtt.length() == 0;
		return enabled;
	}

	public void setJobEnabled(final Element e, final boolean enabled) {
		Utils.setAttribute("enabled", enabled, e);

		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", e), SchedulerDom.MODIFY);
		_main.updateJobs();
	}

	private boolean hasJobComment(final Element e) {
		if (e != null) {
			for (Iterator it = e.getContent().iterator(); it.hasNext();) {
				Object o = it.next();
				/*if (o instanceof Comment && false)
					return true;
				else */
				if (o instanceof Element) {
					Element ee = (Element) o;
					if (!Utils.getAttributeValue("__comment__", ee).equals(""))
						return true;
					if (hasJobComment(ee))
						return true;
				}

			}
		}
		return false;
	}

	public boolean existJobname(final String name) {
		if (name == null || name.length() == 0)
			return false;

		for (int i = 0; _list != null && i < _list.size(); i++) {
			Element currJob = _list.get(i);
			String jobName = Utils.getAttributeValue("name", currJob);
			if (jobName.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Existiert ein param Element mit der gleichen
	 * @param params
	 * @param name
	 * @return
	 */
	private boolean existParams(final Element params, final String name) {
		List param = params.getChildren("param");
		for (int i = 0; i < param.size(); i++) {
			Element e = (Element) param.get(i);
			if (Utils.getAttributeValue("name", e).equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

}
