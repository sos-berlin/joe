package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class ExecuteListener {

	private SchedulerDom	_dom			= null;

	private Element			_job			= null;

	private Element			_process		= null;

	private Element			_environment	= null;

	public SchedulerDom getDom() {
		return _dom;
	}

	public ExecuteListener(SchedulerDom dom, Element job) {

		_dom = dom;
		_job = job;
		setProcess();

	}

	public boolean isExecutable() {
		return _process != null;
	}

	public boolean isScript() {
		return _job.getChild("script") != null;
	}

	public void setNothing() {
		setExecutable(false);
		_job.removeChild("script");
		Utils.setChangedForDirectory(_job, _dom);
		_dom.setChanged(true);

	}

	public void setExecutable(boolean executable) {
		if (executable) {
			initProcess();
		}
		else {
			_job.removeChild("process");
			setProcess();
		}
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

	public void applyVariable(String name, String value) {
		List variables = _environment.getChildren("variable");
		Iterator it = variables.iterator();
		boolean found = false;
		while (it.hasNext() && !found) {
			Element variable = (Element) it.next();
			if (name.equals(variable.getAttributeValue("name"))) {
				variable.setAttribute("value", value);
				found = true;
			}
		}
		if (!found) {
			Element variable = new Element("variable");
			variable.setAttribute("name", name);
			variable.setAttribute("value", value);
			variables.add(variable);
		}
		Utils.setChangedForDirectory(_job, _dom);
		_dom.setChanged(true);
	}

	public void removeVariable(String name) {
		List variables = _environment.getChildren("variable");
		Iterator it = variables.iterator();
		boolean found = false;
		while (!found && it.hasNext()) {
			Element variable = (Element) it.next();
			if (name.equals(variable.getAttributeValue("name"))) {
				variable.detach();
				found = true;
			}
		}
		_dom.setChanged(true);
	}

	public Element getJob() {
		return _job;
	}
}
