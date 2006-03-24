package sos.scheduler.editor.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Comment;
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.app.Utils;

public class JobsListener {

	private DomParser _dom;

	private IUpdate _main;

	private Element _config;
	
	private Element _jobs;

	private List _list;

	public JobsListener(DomParser dom, IUpdate update) {
		_dom = dom;
		_main = update;
		_config = _dom.getRoot().getChild("config");
		_jobs = _config.getChild("jobs");
		if(_jobs != null)
			_list = _jobs.getChildren("job");
	}

	private void initJobs() {
		if(_config.getChild("jobs") == null) {
		Element _jobs = new Element("jobs");
		_config.addContent(_jobs);
		_list = _jobs.getChildren("job");
		} else {
			_jobs = _config.getChild("jobs");
			_list = _jobs.getChildren("job");
		}
	}
	
	public void fillTable(Table table) {
		table.removeAll();
		if (_list != null) {
			for(Iterator it = _list.iterator(); it.hasNext(); ) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					TableItem item = new TableItem(table, SWT.NONE);
					item.setData(e);
					String name = Utils.getAttributeValue("name", e);

					item.setChecked(_dom.isJobDisabled(name));
					item.setText(1, name);
					item.setText(2, Utils.getAttributeValue("title", e));
					item.setText(3, Utils.getAttributeValue("spooler_id", e));
					item
							.setText(4, Utils.getAttributeValue(
									"process_class", e));
					item.setText(5, Utils.isAttributeValue("order", e) ? "Yes"
							: "No");
				}
			}
		}
	}

	public void newJob(Table table) {
		Element job = new Element("job");
		job.setAttribute("name", "job" + (table.getItemCount() + 1));
		Element runtime = new Element("run_time");
		runtime.setAttribute("let_run", "no");
		if(_list == null)
			initJobs();
		_list.add(job.addContent(runtime));
		_dom.setChanged(true);
		fillTable(table);
		table.setSelection(table.getItemCount() - 1);
		_main.updateJobs();
	}

	public boolean deleteJob(Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element e = (Element) item.getData();
			_dom.setJobDisabled(Utils.getAttributeValue("name", e), false);
			e.detach();
			_dom.setChanged(true);
			table.remove(index);
			_main.updateJobs();
			
			if(_list.size() == 0) {
				_config.removeChild("jobs");
				_jobs = null;
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
	
	public void setJobDisabled(String name, boolean disabled) {
		_dom.setJobDisabled(name, disabled);
		_main.updateJobs();
	}
	
	public boolean hasJobComment(Element e) {
		if(e != null) {
			for(Iterator it = e.getContent().iterator(); it.hasNext(); ) {
				Object o = it.next();
				if(o instanceof Comment)
					return true;
				else if (o instanceof Element) {
					Element ee = (Element)o;
					if(!Utils.getAttributeValue("__comment__", ee).equals(""))
						return true;
					if(hasJobComment(ee))
						return true;
				}
					
			}
		}
		return false;
	}
}
