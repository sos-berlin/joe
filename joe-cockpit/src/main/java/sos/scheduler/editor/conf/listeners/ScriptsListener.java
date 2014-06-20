package sos.scheduler.editor.conf.listeners;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;

import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ScriptsListener {
	private SchedulerDom		_dom				= null;
	private ISchedulerUpdate	_main				= null;
	private List				_list				= null;
	private Element				_parent				= null;
	private final static String	EMPTY_MONITOR_NAME	= "<empty>";

	public ScriptsListener(SchedulerDom dom, ISchedulerUpdate update, Element parent) {
		_dom = dom;
		_main = update;
		_parent = parent;
		_list = parent.getChildren("monitor");
	}

	private void initScripts() {
		_list = _parent.getChildren("monitor");
	}

	public void fillTable(Table table) {
		table.removeAll();
		if (_list != null) {
			for (int i = 0; i < _list.size(); i++) {
				Element monitor = (Element) _list.get(i);
				TableItem item = new TableItem(table, SWT.NONE);
				item.setData(monitor);
				String name = Utils.getAttributeValue("name", monitor);
				if (!Utils.isElementEnabled("job", _dom, _parent)) {
					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
				else {
					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				}
				item.setText(0, name.equals("") ? EMPTY_MONITOR_NAME : name);
				item.setText(1, Utils.getAttributeValue("ordering", monitor));
			}
		}
	}

	public void newScript(Table table, String name) {
		Element monitor = new Element("monitor");
		if (name != null && name.length() > 0) {
			monitor.setAttribute("name", "monitor" + (table.getItemCount() + 1));
		}
		if (_list == null)
			initScripts();
		_dom.setChanged(true);
		if (_dom.isLifeElement() || _dom.isDirectory())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _parent), SchedulerDom.MODIFY);
		fillTable(table);
		table.setSelection(table.getItemCount() - 1);
		_main.updateScripts();
		_main.expandItem("Monitor: " + "monitor" + (table.getItemCount()));
	}

	public void save(Table table, String name, String ordering, String newName) {
		boolean found = false;
		Element e = null;
		TableItem item = null;
		if (_list != null) {
			int index = 0;
			for (int i = 0; i < _list.size(); i++) {
				e = (Element) _list.get(i);
				if (name.equals(Utils.getAttributeValue("name", e)) || (name.equals("<empty>") && Utils.getAttributeValue("name", e).equals(""))) {
					if (newName != null) {
						//name ändern
						Utils.setAttribute("name", newName, e);
						_main.updateScripts();
						name = newName;
					}
					found = true;
					Utils.setAttribute("ordering", ordering, e);
					_dom.setChanged(true);
					if (_dom.isLifeElement() || _dom.isDirectory())
						_dom.setChangedForDirectory(_parent.getName(), Utils.getAttributeValue("name", _parent), SchedulerDom.MODIFY);
					table.getItem(index).setText(new String[] { (name.equals("") ? EMPTY_MONITOR_NAME : name), ordering });
				}
				index++;
			}
		}
		if (!found) {
			e = new Element("monitor");
			if (name.equals(EMPTY_MONITOR_NAME)) {
				e.removeAttribute("name");
				Utils.setAttribute("ordering", ordering, e);
			}
			else {
				Utils.setAttribute("name", name, e);
				Utils.setAttribute("ordering", ordering, e);
			}
			_dom.setChanged(true);
			if (_dom.isLifeElement() || _dom.isDirectory())
				_dom.setChangedForDirectory(_parent.getName(), Utils.getAttributeValue("name", _parent), SchedulerDom.MODIFY);
			if (_list == null)
				initScripts();
			if (_list != null)
				_list.add(e);
			item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { (name.equals("") ? EMPTY_MONITOR_NAME : name), ordering });
			item.setData(e);
			_main.updateScripts();
		}
	}

	public boolean delete(Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element e = (Element) item.getData();
			e.detach();
			_dom.setChanged(true);
			if (_dom.isLifeElement() || _dom.isDirectory())
				_dom.setChangedForDirectory(_parent.getName(), Utils.getAttributeValue("name", _parent), SchedulerDom.MODIFY);
			table.remove(index);
			_main.updateScripts();
			if (_list == null)
				initScripts();
			if (_list.size() == 0) {
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

	public boolean existScriptname(String name) {
		if (name == null || name.length() == 0)
			return false;
		for (int i = 0; _list != null && i < _list.size(); i++) {
			Element currJob = (Element) _list.get(i);
			String jobName = Utils.getAttributeValue("name", currJob);
			if (jobName.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public Element getParent() {
		return _parent;
	}
}
