package sos.scheduler.editor.conf.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.forms.JobsForm;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobsListener extends JOEListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobsListener.class);
	private SchedulerDom _dom = null;
	private ISchedulerUpdate _main = null;
	private Element _config = null;
	private Element _jobs = null;
	private List<Element> _list = null;

	public JobsListener(SchedulerDom dom, ISchedulerUpdate update) {
		_dom = dom;
		_main = update;
		if (_dom.isLifeElement()) {
			// do nothing
		} else {
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
			} else {
				_jobs = _config.getChild("jobs");
				_list = _jobs.getChildren("job");
			}
		}
	}

	public void fillTable(Table table) {
		table.removeAll();
		if (_list != null) {
			for (Iterator it = _list.iterator(); it.hasNext();) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					TableItem item = new TableItem(table, SWT.NONE);
					item.setData(e);
					String name = Utils.getAttributeValue("name", e);
					if (!Utils.isElementEnabled("job", _dom, e)) {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					} else {
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

	private String getNewJobName() {
		int i = 1;
		String jobname = "job" + i;
		while (existJobname(jobname)) {
			i++;
			jobname = "job" + i;
		}
		return jobname;
	}

	public void newJob(Table table, boolean isOrder) {

		File template = null;
		Element templateElement = null;
		String liveFolder = Options.getSchedulerHotFolder();
		String path = liveFolder + "/.sos-defaults";
		LOGGER.debug("Looking for templates in:" + path);

		if (isOrder) {
			template = new File(path, "/order_job.template.job.xml");
		} else {
			template = new File(path, "/standalone_job.template.job.xml");
		}
		if (template.exists()) {
			SchedulerDom dom = new SchedulerDom();
			boolean res = IOUtils.openFile(template.getAbsolutePath(), null, dom);

			if (res) {
				templateElement = dom.getRoot();
				templateElement.detach();
			}
		}

		String jobName = getNewJobName();
		Element job = null;
		if (templateElement != null) {
			job = templateElement;
		} else {
			job = new Element("job");
			Utils.setAttribute("stop_on_error", "no", job);
		}
		Utils.setAttribute("name", jobName, job);

		if (isOrder) {
			Utils.setAttribute("order", "yes", job);
		}

		if (job.getChildren("run_time").isEmpty()) {
			Element runtime = new Element("run_time");
			job.addContent(runtime);
		}

		if (_list == null) {
			initJobs();
		}

		_list.add(job);

		fillTable(table);
		table.setSelection(table.getItemCount() - 1);
		_main.updateJobs();
		_main.expandItem("Job: " + jobName);
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", job), SchedulerDom.NEW);
	}

	public Element createJobElement(java.util.HashMap attr) {
		Element job = new Element("job");
		if (attr.get("name") != null && !attr.get("name").toString().isEmpty()) {
			Utils.setAttribute("name", attr.get("name").toString(), job);
		}
		if (attr.get("title") != null && !attr.get("title").toString().isEmpty()) {
			Utils.setAttribute("title", attr.get("title").toString(), job);
		}
		if (attr.get("order") != null && !attr.get("order").toString().isEmpty()) {
			Utils.setAttribute("order", attr.get("order").toString(), job);
			if ("yes".equalsIgnoreCase(attr.get("order").toString())) {
				Utils.setAttribute("stop_on_error", "no", job);
			}
		}
		if (attr.get("tasks") != null && !attr.get("tasks").toString().isEmpty()
				&& !"unbounded".equals(attr.get("tasks"))) {
			Utils.setAttribute("tasks", attr.get("tasks").toString(), job);
		}
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
		if (attr.get("filename") != null && !attr.get("filename").toString().isEmpty()
				&& !"..".equals(attr.get("filename"))) {
			Element desc = new Element("description");
			Element include = new Element("include");
			Utils.setAttribute("file", attr.get("filename").toString(), include);
			desc.addContent(include);
			job.addContent(desc);
		}
		if (attr.get("script") != null && "script".equals(attr.get("script").toString())) {
			Element script = new Element("script");
			if (attr.get("script_language") != null && !attr.get("script_language").toString().isEmpty()) {
				Utils.setAttribute("language", attr.get("script_language").toString(), script);
			}
			if (attr.get("script_java_class") != null && !attr.get("script_java_class").toString().isEmpty()) {
				Utils.setAttribute("java_class", attr.get("script_java_class").toString(), script);
			}
			if (attr.get("script_com_class") != null && !attr.get("script_com_class").toString().isEmpty()) {
				Utils.setAttribute("com_class", attr.get("script_com_class").toString(), script);
			}
			if (attr.get("script_filename") != null && !attr.get("script_filename").toString().isEmpty()) {
				Utils.setAttribute("filename", attr.get("script_filename").toString(), script);
			}
			if (attr.get("script_use_engine") != null && !attr.get("script_use_engine").toString().isEmpty()) {
				Utils.setAttribute("use_engine", attr.get("script_use_engine").toString(), script);
			}
			if (attr.get("script_include_file") != null) {
				ArrayList listOfIncludes = (ArrayList) attr.get("script_include_file");
				for (int i = 0; i < listOfIncludes.size(); i++) {
					if (listOfIncludes.get(i) != null) {
						Element include = new Element("include");
						include.setNamespace(this._dom.getNamespace());
						Utils.setAttribute("file", listOfIncludes.get(i).toString(), include);
						script.addContent(include);
					}
				}
			}
			if (!script.getAttributes().isEmpty()) {
				job.addContent(script);
			}
		}
		if (attr.get("monitor") != null && "monitor".equals(attr.get("monitor").toString())) {
			Element monitor = new Element("monitor");
			Element mon_script = new Element("script");
			if (attr.get("monitor_script_language") != null
					&& !attr.get("monitor_script_language").toString().isEmpty()) {
				Utils.setAttribute("language", attr.get("monitor_script_language").toString(), mon_script);
			}
			if (attr.get("monitor_script_java_class") != null
					&& !attr.get("monitor_script_java_class").toString().isEmpty()) {
				Utils.setAttribute("java_class", attr.get("monitor_script_java_class").toString(), mon_script);
			}
			if (attr.get("monitor_script_com_class") != null
					&& !attr.get("monitor_script_com_class").toString().isEmpty()) {
				Utils.setAttribute("com_class", attr.get("monitor_script_com_class").toString(), mon_script);
			}
			if (attr.get("monitor_script_filename") != null
					&& !attr.get("monitor_script_filename").toString().isEmpty()) {
				Utils.setAttribute("filename", attr.get("monitor_script_filename").toString(), mon_script);
			}
			if (attr.get("monitor_script_use_engine") != null
					&& !attr.get("monitor_script_use_engine").toString().isEmpty()) {
				Utils.setAttribute("use_engine", attr.get("monitor_script_use_engine").toString(), mon_script);
			}
			if (attr.get("monitor_script_include_file") != null) {
				ArrayList listOfMonIncludes = (ArrayList) attr.get("monitor_script_include_file");
				for (int i = 0; i < listOfMonIncludes.size(); i++) {
					Element mon_include = new Element("include");
					mon_include.setNamespace(this._dom.getNamespace());
					Utils.setAttribute("file", listOfMonIncludes.get(i).toString(), mon_include);
					mon_script.addContent(mon_include);
				}
			}
			monitor.addContent(mon_script);
			if (!mon_script.getAttributes().isEmpty()) {
				job.addContent(monitor);
			}
		}
		if (attr.get("process") != null && "process".equals(attr.get("process").toString())) {
			Element process = new Element("process");
			if (attr.get("process_file") != null && !attr.get("process_file").toString().isEmpty()) {
				Utils.setAttribute("file", attr.get("process_file").toString(), process);
			}
			if (attr.get("process_log") != null && !attr.get("process_log").toString().isEmpty()) {
				Utils.setAttribute("log_file", attr.get("process_log").toString(), process);
			}
			if (attr.get("process_param") != null && !attr.get("process_param").toString().isEmpty()) {
				Utils.setAttribute("param", attr.get("process_param").toString(), process);
			}
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
		Element runtime = new Element("run_time");
		job.addContent(runtime);
		return job;
	}

	public Element createParams(java.util.HashMap attr, Element parent) {
		Element params = null;
		if (parent.getChildren("params").isEmpty()) {
			params = new Element("params");
			parent.addContent(0, params);
		} else {
			params = parent.getChild("params");
		}
		ArrayList listOfParams = new ArrayList();
		if (attr.get("params") != null) {
			listOfParams = (ArrayList) attr.get("params");
		}
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

	public Element createJobElement(java.util.HashMap attr, Element job) {
		if (Utils.getAttributeValue("title", job).isEmpty() && attr.get("title") != null
				&& !attr.get("title").toString().isEmpty()) {
			Utils.setAttribute("title", attr.get("title").toString(), job);
		}
		if (Utils.getAttributeValue("order", job).isEmpty() && attr.get("order") != null
				&& !attr.get("order").toString().isEmpty()) {
			Utils.setAttribute("order", attr.get("order").toString(), job);
		}
		if (Utils.getAttributeValue("tasks", job).isEmpty() && attr.get("tasks") != null
				&& !attr.get("tasks").toString().isEmpty() && !"unbounded".equals(attr.get("tasks"))) {
			Utils.setAttribute("tasks", attr.get("tasks").toString(), job);
		}
		createParams(attr, job);
		if (job.getChild("description") == null && attr.get("filename") != null
				&& !attr.get("filename").toString().isEmpty() && !"..".equals(attr.get("filename"))) {
			Element desc = new Element("description");
			Element include = new Element("include");
			Utils.setAttribute("file", attr.get("filename").toString(), include);
			desc.addContent(include);
			job.addContent(desc);
		}
		if (job.getChild("script") == null && attr.get("script") != null
				&& "script".equals(attr.get("script").toString())) {
			Element script = new Element("script");
			if (attr.get("script_language") != null && !attr.get("script_language").toString().isEmpty()) {
				Utils.setAttribute("language", attr.get("script_language").toString(), script);
			}
			if (attr.get("script_java_class") != null && !attr.get("script_java_class").toString().isEmpty()) {
				Utils.setAttribute("java_class", attr.get("script_java_class").toString(), script);
			}
			if (attr.get("script_com_class") != null && !attr.get("script_com_class").toString().isEmpty()) {
				Utils.setAttribute("com_class", attr.get("script_com_class").toString(), script);
			}
			if (attr.get("script_filename") != null && !attr.get("script_filename").toString().isEmpty()) {
				Utils.setAttribute("filename", attr.get("script_filename").toString(), script);
			}
			if (attr.get("script_use_engine") != null && !attr.get("script_use_engine").toString().isEmpty()) {
				Utils.setAttribute("use_engine", attr.get("script_use_engine").toString(), script);
			}
			if (attr.get("script_include_file") != null) {
				ArrayList listOfIncludes = (ArrayList) attr.get("script_include_file");
				for (int i = 0; i < listOfIncludes.size(); i++) {
					if (listOfIncludes.get(i) != null) {
						Element include = new Element("include");
						include.setNamespace(this._dom.getNamespace());
						Utils.setAttribute("file", listOfIncludes.get(i).toString(), include);
						script.addContent(include);
					}
				}
			}
			if (!script.getAttributes().isEmpty()) {
				job.addContent(script);
			}
		}
		if (job.getChild("monitor") == null && attr.get("monitor") != null
				&& "monitor".equals(attr.get("monitor").toString())) {
			Element monitor = new Element("monitor");
			Element mon_script = new Element("script");
			if (attr.get("monitor_script_language") != null
					&& !attr.get("monitor_script_language").toString().isEmpty()) {
				Utils.setAttribute("language", attr.get("monitor_script_language").toString(), mon_script);
			}
			if (attr.get("monitor_script_java_class") != null
					&& !attr.get("monitor_script_java_class").toString().isEmpty()) {
				Utils.setAttribute("java_class", attr.get("monitor_script_java_class").toString(), mon_script);
			}
			if (attr.get("monitor_script_com_class") != null
					&& !attr.get("monitor_script_com_class").toString().isEmpty()) {
				Utils.setAttribute("com_class", attr.get("monitor_script_com_class").toString(), mon_script);
			}
			if (attr.get("monitor_script_filename") != null
					&& !attr.get("monitor_script_filename").toString().isEmpty()) {
				Utils.setAttribute("filename", attr.get("monitor_script_filename").toString(), mon_script);
			}
			if (attr.get("monitor_script_use_engine") != null
					&& !attr.get("monitor_script_use_engine").toString().isEmpty()) {
				Utils.setAttribute("use_engine", attr.get("monitor_script_use_engine").toString(), mon_script);
			}
			if (attr.get("monitor_script_include_file") != null) {
				ArrayList listOfMonIncludes = (ArrayList) attr.get("monitor_script_include_file");
				for (int i = 0; i < listOfMonIncludes.size(); i++) {
					Element mon_include = new Element("include");
					mon_include.setNamespace(this._dom.getNamespace());
					Utils.setAttribute("file", listOfMonIncludes.get(i).toString(), mon_include);
					mon_script.addContent(mon_include);
				}
			}
			monitor.addContent(mon_script);
			if (!mon_script.getAttributes().isEmpty()) {
				job.addContent(monitor);
			}
		}
		if (job.getChild("process") == null && attr.get("process") != null
				&& "process".equals(attr.get("process").toString())) {
			Element process = new Element("process");
			if (attr.get("process_file") != null && !attr.get("process_file").toString().isEmpty()) {
				Utils.setAttribute("file", attr.get("process_file").toString(), process);
			}
			if (attr.get("process_log") != null && !attr.get("process_log").toString().isEmpty()) {
				Utils.setAttribute("log_file", attr.get("process_log").toString(), process);
			}
			if (attr.get("process_param") != null && !attr.get("process_param").toString().isEmpty()) {
				Utils.setAttribute("param", attr.get("process_param").toString(), process);
			}
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
		return job;
	}

	public void newImportJob(Element job, int assistentType) {
		try {
			if (!_dom.isLifeElement()) {
				if (_list == null) {
					initJobs();
				}
				_list.add(job);
			}
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", job), SchedulerDom.NEW);
			if (JOEConstants.JOB_CHAINS != assistentType && JobsForm.getTable() != null) {
				fillTable(JobsForm.getTable());
				JobsForm.getTable().setSelection(JobsForm.getTable().getItemCount() - 1);
			}
			if (_dom.isLifeElement()) {
				List l = job.getAttributes();
				_dom.getRoot().removeContent();
				_dom.getRoot().addContent(job.cloneContent());
				for (int i = 0; i < l.size(); i++) {
					org.jdom.Attribute a = (org.jdom.Attribute) l.get(i);
					_dom.getRoot().setAttribute(a.getName(), a.getValue());
				}
				_main.updateJob(job);
			} else {
				_main.updateOrders();
				_main.updateJobs();
			}
			_main.expandItem(Utils.getAttributeValue("name", job));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			try {
				new ErrorLog("error in " + getMethodName() + " ; could not start assistent.", e);
			} catch (Exception ee) {
				// do nothing
			}
		}
	}

	public void newImportJob(Element job) {
		if (_list == null) {
			initJobs();
		}
		_list.add(job);
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", job), SchedulerDom.NEW);
		fillTable(JobsForm.getTable());
		JobsForm.getTable().setSelection(JobsForm.getTable().getItemCount() - 1);
		_main.updateJobs();
		_main.updateOrders();
	}

	public boolean deleteJob(Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element e = (Element) item.getData();
			e.detach();
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", e), SchedulerDom.DELETE);
			table.remove(index);
			_main.updateJobs();
			if (_list == null) {
				initJobs();
			}
			if (_list.isEmpty()) {
				_config.removeChild("jobs");
				_jobs = null;
				_list = null;
			}
			if (index >= table.getItemCount()) {
				index--;
			}
			if (index >= 0) {
				table.setSelection(index);
				return true;
			}
		}
		return false;
	}

	public boolean editJob(Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element objElement = (Element) item.getData();
			return true;
		}
		return false;
	}

	public boolean isEnabled(Element e) {
		String enabledAtt = Utils.getAttributeValue("enabled", e);
		return "yes".equalsIgnoreCase(enabledAtt) || enabledAtt.isEmpty();
	}

	public void setJobEnabled(Element e, boolean enabled) {
		Utils.setAttribute("enabled", enabled, e);
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", e), SchedulerDom.MODIFY);
		_main.updateJobs();
	}

	private boolean hasJobComment(Element e) {
		if (e != null) {
			for (Iterator it = e.getContent().iterator(); it.hasNext();) {
				Object o = it.next();
				if (o instanceof Element) {
					Element ee = (Element) o;
					if (!"".equals(Utils.getAttributeValue("__comment__", ee))) {
						return true;
					}
					if (hasJobComment(ee)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean existJobname(String name) {
		if (name == null || name.isEmpty()) {
			return false;
		}
		for (int i = 0; _list != null && i < _list.size(); i++) {
			Element currJob = _list.get(i);
			String jobName = Utils.getAttributeValue("name", currJob);
			if (jobName.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	private boolean existParams(Element params, String name) {
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