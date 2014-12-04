package sos.scheduler.editor.conf.listeners;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class RunTimeListener {
	private SchedulerDom	_dom		= null;
	private Element			_job		= null;
	private Element			_runtime	= null;

	//private       ISchedulerUpdate _gui        = null;
	public RunTimeListener(SchedulerDom dom, Element job, ISchedulerUpdate gui) {
		_dom = dom;
//		_gui = gui;
		_job = job;
		_runtime = _job.getChild("run_time");
		checkRuntime();
	}

	public boolean isOnOrder() {
		return Utils.isAttributeValue("order", _job);
	}

	public String getComment() {
		return Utils.getAttributeValue("__comment__", _runtime);
	}

	public void setComment(String comment) {
		Utils.setAttribute("__comment__", comment, _runtime, _dom);
	}

	private void setRuntime() {
		if (_job.getChild("run_time") == null) {
			_runtime = new Element("run_time");
			_job.addContent(_runtime);
		}
	}

	public Element getRunTime() {
		if (_runtime == null)
			setRuntime();
		return _runtime;
	}

	public void setFunction(String function_name) {
		if (_runtime != null) {
			Utils.setAttribute("start_time_function", function_name, _runtime, _dom);
			//_gui.updateRunTime();
			//_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
			if (_dom.isDirectory() || _dom.isLifeElement())
				_dom.setChangedForDirectory(_job, SchedulerDom.MODIFY);
		}
	}

	public String getFunction() {
		String s = _runtime.getAttributeValue("start_time_function");
		if (s == null)
			s = "";
		return s;
	}

	public String[] getSchedules() {
		String[] retval = new String[0];
		if (!_dom.isLifeElement() && _dom.getRoot() != null) {
			if (_dom.getRoot().getChild("config") != null && _dom.getRoot().getChild("config").getChild("schedules") != null) {
				java.util.List l = _dom.getRoot().getChild("config").getChild("schedules").getChildren("schedule");
				retval = new String[l.size()];
				for (int i = 0; i < l.size(); i++) {
					Element e = (Element) l.get(i);
					retval[i] = Utils.getAttributeValue("name", e);
				}
			}
		}
		return retval;
	}

	public String getSchedule() {
		return Utils.getAttributeValue("schedule", _runtime);
	}

	public void setSchedule(String schedule) {
		if (_runtime != null) {
			Utils.setAttribute("schedule", schedule, _runtime, _dom);
			//_gui.updateRunTime();
			if (_dom.isDirectory() || _dom.isLifeElement())
				_dom.setChangedForDirectory(_job, SchedulerDom.MODIFY);
		}
	}
	


	/*
	Runtime darf Starttime nicht im Attribut haben.
	Der Starttime darf nur in Perioden definiert werden.
	   Wenn ein run_time Element bereits definitionen aus der Starttime hat (repeat_time und single_time und absolute_repeat),
	dann werden diese Attribute in every-Day gemoved.
		 */
	private void checkRuntime() {
		//repeat ins every_day moven
		String repeat = Utils.getAttributeValue("repeat", _runtime);
		if (repeat.length() > 0) {
			Element p = new Element("period");
			p.setAttribute("repeat", repeat);
			_runtime.addContent(p);
			_runtime.removeAttribute("repeat");
		}
		//single_start ins every_day moven
		String single_start = Utils.getAttributeValue("single_start", _runtime);
		if (single_start.length() > 0) {
			Element p = new Element("period");
			p.setAttribute("single_start", single_start);
			_runtime.addContent(p);
			_runtime.removeAttribute("single_start");
		}
	}

	public Element getParent() {
		return _job;
	}
}
