package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;

import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;


public class SchedulesListener { 

	private    SchedulerDom         _dom          = null;

	private    ISchedulerUpdate     _main         = null;

	private    Element              _config       = null;

	private    Element              _schedules    = null;

	private    List                 _list         = null;


	public SchedulesListener(SchedulerDom dom, ISchedulerUpdate update) {

		_dom = dom;
		_main = update;
		if(_dom.isLifeElement()) {

		} else {
			_config = _dom.getRoot().getChild("config");
			_schedules = _config.getChild("schedules");

			if (_schedules != null)
				_list = _schedules.getChildren("schedule");
		}

	}


	private void initSchedules() {
		if (_config.getChild("schedules") == null) {
			Element _schedules = new Element("schedules");
			_config.addContent(_schedules);
			_list = _schedules.getChildren("schedule");
		} else {
			_schedules = _config.getChild("schedules");
			_list = _schedules.getChildren("schedule");
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

					item.setText(0, name);

				}
			}
		}
	}


	public void newScheduler(Table table) {
		Element schedule = new Element("schedule");
		String name = "schedule" + (table.getItemCount() + 1);
		schedule.setAttribute("name", name);				
		if (_list == null)
			initSchedules();
		_list.add(schedule);
				
		fillTable(table);
		table.setSelection(table.getItemCount() - 1);
		_main.updateSchedules();
		_main.expandItem(name);
		_dom.setChanged(true);
		_dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", schedule), SchedulerDom.NEW);

	}



	public boolean deleteSchedule(Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element e = (Element) item.getData();			
			e.detach();
			_dom.setChanged(true);
			_dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", e) ,SchedulerDom.DELETE);
			table.remove(index);			
			_main.updateSchedules();
			if(_list==null)
				initSchedules();
			if (_list.size() == 0) {
				_config.removeChild("schedules");
				_schedules = null;
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


}
