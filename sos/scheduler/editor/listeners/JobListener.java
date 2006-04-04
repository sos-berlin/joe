package sos.scheduler.editor.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.Text;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.app.Utils;

public class JobListener {
	private IUpdate _main;

	private DomParser _dom;

	private Element _job;

	private List _params;

	public JobListener(DomParser dom, Element job, IUpdate update) {
		_dom = dom;
		_job = job;
		_main = update;
		Element params = _job.getChild("params");
		if (params != null)
			_params = params.getChildren("param");
	}

	private void initParams() {
		_job.addContent(0, new Element("params"));
		_params = _job.getChild("params").getChildren();
	}

	public String getComment() {
		return Utils.getAttributeValue("__comment__", _job);
	}
	
	public void setComment(String comment) {
		Utils.setAttribute("__comment__", comment, _job, _dom);
	}
	
	public boolean isDisabled() {
		return _dom.isJobDisabled(Utils.getAttributeValue("name", _job));
	}
	
	public String getName() {
		return Utils.getAttributeValue("name", _job);
	}

	public void setName(String name) {
		Utils.setAttribute("name", name, _job, _dom);
	}

	public String getTitle() {
		return Utils.getAttributeValue("title", _job);
	}

	public void setTitle(String title) {
		Utils.setAttribute("title", title, _job, _dom);
	}

	public String getSpoolerID() {
		return Utils.getAttributeValue("spooler_id", _job);
	}

	public void setSpoolerID(String spoolerID) {
		Utils.setAttribute("spooler_id", spoolerID, _job, _dom);
	}

	public String getProcessClass() {
		return Utils.getAttributeValue("process_class", _job);
	}

	public void setProcessClass(String processClass) {
		Utils.setAttribute("process_class", processClass, _job, _dom);
	}

	public boolean getOrder() {
		String order = _job.getAttributeValue("order");
		return order == null ? false : order.equalsIgnoreCase("yes");
	}

	public void setOrder(boolean order) {
		if (order) {
			_job.setAttribute("order", "yes");
			_job.removeAttribute("priority");
			if (_job.getChild("run_time") == null)
				_job.addContent(new Element("run_time").setAttribute("let_run",
						"no"));
		} else {
			_job.removeAttribute("order");
		}
		//_main.updateJob();
		_dom.setChanged(true);
	}

	public String getPriority() {

		return Utils.getAttributeValue("priority", _job);
	}

	public void setPriority(String priority) {
		Utils.setAttribute("priority", priority, _job, _dom);
	}

	public int getTasks() {
		return Utils.getIntValue("tasks", 1, _job);
	}

	public void setTasks(String tasks) {
		Utils.setAttribute("tasks", tasks, _job, _dom);
	}

	public int getTimeout() {
		return Utils.getIntValue("timeout", _job);
	}

	public void setTimeout(String timeout) {
		Utils.setAttribute("timeout", timeout, _job, _dom);
	}

	public int getIdleTimeout() {
		return Utils.getIntValue("idle_timeout", _job);
	}

	public void setIdleTimeout(String timeout) {
		Utils.setAttribute("idle_timeout", timeout, _job, _dom);
	}

	public String[] getProcessClasses() {
		String[] names = null;
		Element classes = _dom.getRoot().getChild("config").getChild(
				"process_classes");
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
		return names;
	}

	public void fillParams(Table table) {
		if (_params != null) {
			Iterator it = _params.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, ((Element) o).getAttributeValue("name"));
					item.setText(1, ((Element) o).getAttributeValue("value"));
				}
			}
		}
	}

	public void deleteParameter(Table table, int index) {
		if (_params != null) {
			_params.remove(index);
			_dom.setChanged(true);
		}
		table.remove(index);
	}

	public void saveParameter(Table table, String name, String value) {
		boolean found = false;
		if (_params != null) {
			int index = 0;
			Iterator it = _params.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					if (name.equals(e.getAttributeValue("name"))) {
						found = true;
						e.setAttribute("name", value);
						_dom.setChanged(true);
						table.getItem(index).setText(1, value);
					}
					index++;
				}
			}
		}
		if (!found) {
			Element e = new Element("param");
			e.setAttribute("name", name);
			e.setAttribute("value", value);
			_dom.setChanged(true);
			if (_params == null)
				initParams();
			_params.add(e);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { name, value });
		}
	}

	public String getDescription() {
		Element desc = _job.getChild("description");
		if (desc != null) {
			return desc.getTextTrim();
		} else
			return "";
	}

	public void setDescription(String description) {
		Element desc = _job.getChild("description");

		if (desc == null && !description.equals("")) {
			desc = new Element("description");
			_job.addContent(0, desc);
		}

		if (desc != null) {
			if (description.equals("")
					&& (getInclude() == null || getInclude().equals(""))) {
				_job.removeChild("description");
				_dom.setChanged(true);
				return;
			}

			boolean found = false;
			List mixed = desc.getContent();
			Iterator it = mixed.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof CDATA || o instanceof Text) {
					found = true;
					if (description.equals("")) {
						((Text) o).detach();
						break;
					} else
						((Text) o).setText(description);
				}
			}
			if (!found && !description.equals(""))
				desc.addContent(new CDATA(description));

			_dom.setChanged(true);
		}
	}

	public String getInclude() {
		Element desc = _job.getChild("description");
		if (desc != null) {
			Element inc = desc.getChild("include");
			if (inc != null)
				return inc.getAttributeValue("file");
		}
		return "";
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
					desc.addContent(0, new Element("include").setAttribute(
							"file", file));
				else
					incl.setAttribute("file", file);

			} else {
				desc.removeChild("include");
				if (getDescription().equals(""))
					_job.removeChild("description");
			}

			_dom.setChanged(true);
		}
	}
}
