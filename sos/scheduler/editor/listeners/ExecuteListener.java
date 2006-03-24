package sos.scheduler.editor.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Utils;

public class ExecuteListener {
	private DomParser _dom;

	private Element _job;

	private Element _process;

	private Element _environment;

	public ExecuteListener(DomParser dom, Element job) {
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
		_dom.setChanged(true);
	}
	
	public void setExecutable(boolean executable) {
		if (executable) {
			initProcess();
		} else {
			_job.removeChild("process");
			setProcess();
		}
	}

	private void setProcess() {
		_process = _job.getChild("process");

		_environment = _process != null ? _process.getChild("environment")
				: null;
	}

	private void initProcess() {
		if (_process == null) {
			_job.addContent(new Element("process").addContent(new Element(
					"environment")));
			_job.removeChild("script");
			setProcess();
			_dom.setChanged(true);
		}
	}

	public String getFile() {
		return Utils.getAttributeValue("file", _process);
	}

	public void setFile(String file) {
		initProcess();
		Utils.setAttribute("file", file, _process, _dom);
	}

	public String getParam() {
		return Utils.getAttributeValue("param", _process);
	}

	public void setParam(String param) {
		initProcess();
		Utils.setAttribute("param", param, _process);
	}

	public String getLogFile() {
		return Utils.getAttributeValue("log_file", _process);
	}

	public void setLogFile(String file) {
		initProcess();
		Utils.setAttribute("log_file", file, _process);
	}

	public boolean isIgnoreSignal() {
		return Utils.isAttributeValue("ignore_signal", _process);
	}

	public void setIgnoreSignal(boolean ignore) {
		Utils.setAttribute("ignore_signal", ignore, _process, _dom);
	}

	public boolean isIgnoreError() {
		return Utils.isAttributeValue("ignore_error", _process);
	}

	public void setIgnoreError(boolean ignore) {
		Utils.setAttribute("ignore_error", ignore, _process, _dom);
	}

	public void fillEnvironmentTable(Table table) {
		table.removeAll();
		if (_environment != null) {
			Iterator it = _environment.getChildren("variable").iterator();
			while (it.hasNext()) {
				Element variable = (Element) it.next();
				String name = variable.getAttributeValue("name");
				String value = variable.getAttributeValue("value");
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, name != null ? name : "");
				item.setText(1, value != null ? value : "");
			}
		}
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
	}
}
