package com.sos.event.service.listeners;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import com.sos.event.service.forms.ActionsForm;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.Events.ActionsDom;

public class JobCommandNamesListener {
	private ActionsForm	_main		= null;
	private ActionsDom	_dom		= null;
	private Element		_command	= null;

	//private        Element          _job          = null;
	public JobCommandNamesListener(ActionsDom dom, Element command, ActionsForm update) {
		_dom = dom;
		_command = command;
		_main = update;
		//if (_command != null)
		//	_job = _command.getParentElement();
	}

	public void fillCommands(Element job, TreeItem parent, boolean expand) {
		List commands = job.getChildren("commands");
		//java.util.ArrayList listOfReadOnly = _dom.getListOfReadOnlyFiles();
		if (commands != null) {
			Iterator it = commands.iterator();
			parent.removeAll();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				if (e.getAttributeValue("on_exit_code") != null) {
					TreeItem item = new TreeItem(parent, SWT.NONE);
					item.setText(e.getAttributeValue("name"));
					item.setData(new TreeData(JOEConstants.JOB_COMMAND, e, Options.getHelpURL("job.commands")));
					/*if (listOfReadOnly != null && listOfReadOnly.contains(Utils.getAttributeValue("name", job))) {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					} else {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					}*/
				}
			}
		}
		parent.setExpanded(expand);
	}

	/*public boolean isDisabled() {
		return _dom.isJobDisabled(Utils.getAttributeValue("name", _job));
	}*/
	public void addCommand(Element e) {
		_dom.setChanged(true);
		//_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		_command.addContent(e);
		_main.updateCommand();
	}

	private int getActCommand(Table table) {
		int index = table.getSelectionIndex();
		int j = index;
		int ignore = 0;
		List c = _command.getChildren();
		Iterator it2 = c.iterator();
		while (it2.hasNext() && j >= 0) {
			Element e2 = (Element) it2.next();
			if (!e2.getName().equals("start_job") && !e2.getName().equals("add_order") && !e2.getName().equals("order")) {
				ignore++;
			}
			else {
				j--;
			}
		}
		return index + ignore;
	}

	public void deleteCommand(Table table) {
		int j = 0;
		int index = table.getSelectionIndex();
		j = getActCommand(table);
		table.remove(index);
		List c = _command.getChildren();
		if (_command != null) {
			c.remove(j);
		}
		_main.updateCommand();
		_dom.setChanged(true);
		//_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.DELETE);
	}

	public String getCommandAttribute(Table table, String attribute) {
		int i = getActCommand(table);
		List l = _command.getChildren();
		Element e = (Element) l.get(i);
		return Utils.getAttributeValue(attribute, e);
	}

	public void setName(String value) {
		Utils.setAttribute("name", value, _command, _dom);
		_main.updateTreeItem(value);
	}

	public String getName() {
		return Utils.getAttributeValue("name", _command);
	}

	public void setHost(String value) {
		Utils.setAttribute("scheduler_host", value, _command, _dom);
	}

	public String getHost() {
		return Utils.getAttributeValue("scheduler_host", _command);
	}

	public void setPort(String value) {
		Utils.setAttribute("scheduler_port", value, _command, _dom);
	}

	public String getPort() {
		return Utils.getAttributeValue("scheduler_port", _command);
	}

	public Element getCommand() {
		return _command;
	}

	public void fillCommands(Table table) {
		boolean created;
		TableItem item = null;
		table.removeAll();
		List c = _command.getChildren();
		Iterator it2 = c.iterator();
		while (it2.hasNext()) {
			Element e2 = (Element) it2.next();
			created = false;
			if (e2.getName().equals("start_job") || e2.getName().equals("add_order") || e2.getName().equals("order")) {
				if (!created) { // Nur die commands add_order und start_job
					// anzeigen
					item = new TableItem(table, SWT.NONE);
					item.setText(1, "");
					created = true;
				}
				item.setText(0, e2.getName());
				item.setText(3, Utils.getAttributeValue("at", e2));
				if (e2.getName().equals("start_job"))
					item.setText(1, Utils.getAttributeValue("job", e2));
				if (e2.getName().equals("add_order") || e2.getName().equals("order")) {
					item.setText(1, Utils.getAttributeValue("id", e2));
					item.setText(2, Utils.getAttributeValue("job_chain", e2));
				}
			}
		}
	}
}
