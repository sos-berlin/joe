package sos.scheduler.editor.conf.listeners;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class OrdersListener {
	private SchedulerDom		_dom;
	private ISchedulerUpdate	_main;
	private Element				_commands;
	private Element				_config;
	private List				_orders;
	private List				_orders2;

	public OrdersListener(SchedulerDom dom, ISchedulerUpdate update) {
		_dom = dom;
		_main = update;
		if (_dom.isLifeElement())
			return;
		_config = _dom.getRoot().getChild("config");
		_commands = _config.getChild("commands");
		if (_commands != null) {
			_orders = _commands.getChildren("add_order");
			_orders2 = _commands.getChildren("order");
		}
	}

	private void initCommands() {
		if (_config.getChild("commands") == null) {
			_commands = new Element("commands");
			_config.addContent(_commands);
		}
		else {
			_commands = _config.getChild("commands");
		}
		_orders = _commands.getChildren("add_order");
		_orders2 = _commands.getChildren("order");
	}

	public void fillTable(Table table) {
		table.removeAll();
		if (_orders != null) {
			for (Iterator it = _orders.iterator(); it.hasNext();) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					TableItem item = new TableItem(table, SWT.NONE);
					item.setData(e);
					String id = Utils.getAttributeValue("id", e);
					item.setText(0, id);
					if (!Utils.isElementEnabled("commands", _dom, e)) {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					}
				}
			}
			for (Iterator it = _orders2.iterator(); it.hasNext();) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					TableItem item = new TableItem(table, SWT.NONE);
					item.setData(e);
					String id = Utils.getAttributeValue("id", e);
					item.setText(0, id);
					if (!Utils.isElementEnabled("commands", _dom, e)) {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					}
				}
			}
		}
	}

	private boolean haveId(int id, Table table) {
		int count = table.getItemCount();
		for (int i = 0; i < count; i++) {
			TableItem item = table.getItem(i);
			String actId = item.getText();
			if (actId.trim().equals(String.valueOf(id)))
				return true;
		}
		return false;
	}

	public void newCommands(Table table) {
		boolean found = false;
		String id = "";
		int c = 1;
		while (!found) {
			if (!haveId(c, table)) {
				found = true;
				id = String.valueOf(c);
			}
			c++;
		}
		Element add_order = new Element("order");
		Element runtime = new Element("run_time");
		runtime.setAttribute("let_run", "no");
		add_order.setAttribute("id", id);
		if (_commands == null)
			initCommands();
		add_order.addContent(runtime);
		_orders2.add(add_order);
		fillTable(table);
		table.setSelection(table.getItemCount() - 1);
		_main.updateOrders();
		_dom.setChanged(true);
		_dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain", add_order) + "," + id, SchedulerDom.NEW);
	}

	public boolean deleteCommands(Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element e = (Element) item.getData();
			e.detach();
			_dom.setChanged(true);
			_dom.setChangedForDirectory("order", Utils.getAttributeValue("job_chain", e) + "," + Utils.getAttributeValue("id", e), SchedulerDom.DELETE);
			table.remove(index);
			_main.updateOrders();
			if (index >= table.getItemCount())
				index--;
			if (index >= 0) {
				table.setSelection(index);
				return true;
			}
		}
		return false;
	}

	/** Lifert alle Order Id's */
	public String[] getOrderIds() {
		String[] listOfIds = null;
		if (_orders != null) {
			listOfIds = new String[_orders.size()];
			//for (Iterator it = _orders.iterator(); it.hasNext();) {
			for (int i = 0; i < _orders.size(); i++) {
				Object o = _orders.get(i);
				if (o instanceof Element) {
					Element e = (Element) o;
					String id = Utils.getAttributeValue("id", e);
					listOfIds[i] = id;
				}
			}
		}
		if (_orders2 != null) {
			listOfIds = new String[_orders2.size()];
			//for (Iterator it = _orders.iterator(); it.hasNext();) {
			for (int i = 0; i < _orders2.size(); i++) {
				Object o = _orders2.get(i);
				if (o instanceof Element) {
					Element e = (Element) o;
					String id = Utils.getAttributeValue("id", e);
					listOfIds[i] = id;
				}
			}
		}
		return listOfIds;
	}
}
