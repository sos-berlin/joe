package sos.scheduler.editor.conf.listeners;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobMonitorUseListener {
	private SchedulerDom	_dom			= null;
	private Element			_job			= null;
	private List<Element>	_monitorUseList	= null;
	private Element			_monitorUse		= null;

	public JobMonitorUseListener(SchedulerDom dom, Element job) {
		_dom = dom;
		_job = job;
		_monitorUseList = _job.getChildren("monitor.use");
	}

	public void fillMonitorUse(Table table) {
		table.removeAll();
		Iterator<Element> it = _monitorUseList.iterator();
		while (it.hasNext()) {
			Element e =  it.next();
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Utils.getAttributeValue("monitor", e));
			item.setText(1, Utils.getAttributeValue("ordering", e));
		}
	}

	public void newMonitorUse() {
		_monitorUse = new Element("monitor.use");
	}

	public void selectMonitorUse(int index) {
		if (index >= 0 && index < _monitorUseList.size())
			_monitorUse = _monitorUseList.get(index);
	}

	public void applyMonitorUse(String monitorUse, String ordering) {
		if (_monitorUse == null)
			newMonitorUse();
		Utils.setAttribute("monitor", monitorUse, _monitorUse, _dom);
		Utils.setAttribute("ordering", ordering, _monitorUse, _dom);
		if (!_monitorUseList.contains(_monitorUse))
			_monitorUseList.add(_monitorUse);
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public void deleteMonitorUse(int index) {
		if (index >= 0 && index < _monitorUseList.size()) {
			_monitorUseList.remove(index);
			_monitorUse = null;
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
		}
	}

	public String getMonitorUse() {
		return Utils.getAttributeValue("monitor", _monitorUse);
	}

	public String getOrdering() {
		if (Utils.getAttributeValue("ordering", _monitorUse) == null || Utils.getAttributeValue("ordering", _monitorUse).length() == 0) {
			return "";
		}
		else {
			return (Utils.getAttributeValue("ordering", _monitorUse));
		}
	}

	public boolean isValidMonitor(String monitor) {
		if (monitor.equals(""))
			return false;
		if (_monitorUseList != null) {
			for (Iterator<Element> it = _monitorUseList.iterator(); it.hasNext();) {
				Element e = it.next();
				if (Utils.getAttributeValue("monitor", e).equals(monitor))
					return false;
			}
		}
		return true;
	}

	public String[] getMonitors() {
		String[] names = null;
		if (_dom.isLifeElement()) {
			names = new String[0];
			return names;
		}
		Element monitors = _dom.getRoot().getChild("config").getChild("monitors");
		if (monitors != null) {
			List<Element> list = monitors.getChildren("monitor");
			names = new String[list.size()];
			int i = 0;
			Iterator<Element> it = list.iterator();
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

	public Element getJob() {
		return _job;
	}
}
