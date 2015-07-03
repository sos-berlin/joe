package sos.scheduler.editor.conf.listeners;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class RunTimeListener {
	private SchedulerDom	_dom		= null;
	private Element			_job		= null;
	private Element			_runtime	= null;

	public RunTimeListener(SchedulerDom dom, Element job, ISchedulerUpdate gui) {
		_dom = dom;
		_job = job;
		_runtime = _job.getChild("run_time");
		checkRuntime();
	}

   public String getTimeZone() {
        return Utils.getAttributeValue("time_zone", _runtime);
    }

   public void setTimeZone(String timeZone) {
	   Utils.setAttribute("time_zone", timeZone, _runtime,_dom);
       notifyChange(_runtime);

    }
	    
	public boolean isOnOrder() {
		return Utils.isAttributeValue("order", _job);
	}

	public String getComment() {
		return Utils.getAttributeValue("__comment__", _runtime);
	}

	public void setComment(String comment) {
		Utils.setAttribute("__comment__", comment, _runtime, _dom);
		notifyChange(_runtime);
	}

	private void setRuntime() {
		if (_job.getChild("run_time") == null) {
			_runtime = new Element("run_time");
			_job.addContent(_runtime);
		}
	}
	
	private void notifyChange(Element el){
	       _dom.setChanged(true);
	        
	        Element parent = Utils.getRunTimeParentElement(el);

	        String name = "";
	        if (parent.getName().equals("order")){
	            name = Utils.getAttributeValue("job_chain", parent) + "," + Utils.getAttributeValue("id", parent);
	        }else{
	            name = Utils.getAttributeValue("name", parent);
	        }

	        _dom.setChangedForDirectory(parent.getName(), name, SchedulerDom.MODIFY);

	    }
	    

	public Element getRunTime() {
		if (_runtime == null)
			setRuntime();
		return _runtime;
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
			if (_dom.isDirectory() || _dom.isLifeElement())
				_dom.setChangedForDirectory(_job, SchedulerDom.MODIFY);
		}
	}
	


	 
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
