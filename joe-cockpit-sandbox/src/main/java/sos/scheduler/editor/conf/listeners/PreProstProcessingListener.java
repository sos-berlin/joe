package sos.scheduler.editor.conf.listeners;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;

import sos.scheduler.editor.app.Utils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class PreProstProcessingListener extends JOEListener {

	@SuppressWarnings("unused")
	private final String		conClassName	= "PreProstProcessingListener";
	@SuppressWarnings("unused")
	private static final String	conSVNVersion	= "$Id$";
	@SuppressWarnings("unused")
	private static final Logger	logger			= Logger.getLogger(PreProstProcessingListener.class);

//	private SchedulerDom		_dom				= null;
//	private ISchedulerUpdate	_main				= null;
	private List<Element>		lstPrePostProcessingProcedures				= null;
//	private Element				_parent				= null;

	private final static String	EMPTY_MONITOR_NAME	= "<empty>";

	public PreProstProcessingListener(final SchedulerDom dom, final ISchedulerUpdate update, final Element parent) {
		_dom = dom;
		_main = update;
		_parent = parent;
		initScripts();
	}

	@SuppressWarnings("unchecked")
	private void initScripts() {
		lstPrePostProcessingProcedures = _parent.getChildren("monitor");
		strUpdateElementName = Utils.getAttributeValue("name", _parent);
		strUpdateObjectType = "job";
	}

	public void fillTable(final Table table) {
		table.removeAll();
		if (lstPrePostProcessingProcedures != null) {
			for (int i = 0; i < lstPrePostProcessingProcedures.size(); i++) {
				Element monitor = lstPrePostProcessingProcedures.get(i);
				TableItem item = new TableItem(table, SWT.NONE);
				item.setData(monitor);
				String name = Utils.getAttributeValue("name", monitor);
				if (!Utils.isElementEnabled("job", _dom, _parent)) {
					item.setForeground(getColor4DisabledElements());
				}
				else {
					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				}
				item.setText(0, name.equals("") ? EMPTY_MONITOR_NAME : name);
				item.setText(1, Utils.getAttributeValue("ordering", monitor));
			}

		}
	}

	public void newPrePostProcessingProcedure(final Table table, final String name) {
		// TODO Template erlauben. Einstiegsformular verwenden
		Element monitor = new Element("monitor");
		if (name != null && name.length() > 0) {
			monitor.setAttribute("name", "monitor" + (table.getItemCount() + 1));
		}

		if (lstPrePostProcessingProcedures == null) {
			initScripts();
		}

		_dom.setChanged(true);
		if (_dom.isLifeElement() || _dom.isDirectory())
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _parent), SchedulerDom.MODIFY);

		fillTable(table);
		table.setSelection(table.getItemCount() - 1);
		_main.updateScripts();
		_main.expandItem("Monitor: " + "monitor" + table.getItemCount());
	}

	public void save(final Table table, String name, final String ordering, final String newName) {
		boolean found = false;
		Element e = null;
		TableItem item = null;
		if (lstPrePostProcessingProcedures != null) {
			int index = 0;

			for (int i = 0; i < lstPrePostProcessingProcedures.size(); i++) {
				e = lstPrePostProcessingProcedures.get(i);
				if (name.equals(Utils.getAttributeValue("name", e)) || name.equals("<empty>") && Utils.getAttributeValue("name", e).equals("")) {
					if (newName != null) {
						//name ändern
						Utils.setAttribute("name", newName, e);
						_main.updateScripts();
						name = newName;
					}
					found = true;
					Utils.setAttribute("ordering", ordering, e);
					setDirty();
					table.getItem(index).setText(new String[] { name.equals("") ? EMPTY_MONITOR_NAME : name, ordering });
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

			if (lstPrePostProcessingProcedures == null)
				initScripts();
			if (lstPrePostProcessingProcedures != null)
				lstPrePostProcessingProcedures.add(e);

			item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { name.equals("") ? EMPTY_MONITOR_NAME : name, ordering });
			item.setData(e);

			_main.updateScripts();
		}
	}

	public boolean delete(final Table table) {
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
			if (lstPrePostProcessingProcedures == null)
				initScripts();
			if (lstPrePostProcessingProcedures.size() == 0) {
				lstPrePostProcessingProcedures = null;
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

	public boolean existScriptname(final String name) {
		if (name == null || name.length() == 0)
			return false;

		for (int i = 0; lstPrePostProcessingProcedures != null && i < lstPrePostProcessingProcedures.size(); i++) {
			Element currJob = lstPrePostProcessingProcedures.get(i);
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
