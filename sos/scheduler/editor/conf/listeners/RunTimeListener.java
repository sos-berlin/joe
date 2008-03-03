package sos.scheduler.editor.conf.listeners;

import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class RunTimeListener {
    private SchedulerDom _dom;

    private Element      _job;

    private Element      _runtime;
    

    public RunTimeListener(SchedulerDom dom, Element job) {
        _dom = dom;
        _job = job;
        _runtime = _job.getChild("run_time");
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

          Utils.setAttribute("start_time_function",function_name, _runtime, _dom);
          //_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
          _dom.setChangedForDirectory(_job, SchedulerDom.MODIFY);
      }
  }
    
    public String getFunction() {
    	String s = _runtime.getAttributeValue("start_time_function");
    	if (s==null)s="";
      return s;
    }

    public String[] getSchedules() {
    	String[] retval = new String[0];
    	if (!_dom.isLifeElement() && _dom.getRoot() != null) {
    		if(_dom.getRoot().getChild("config") != null && _dom.getRoot().getChild("config").getChild("schedules")!= null) {
    			java.util.List l = _dom.getRoot().getChild("config").getChild("schedules").getChildren("schedule");
    			retval = new String[l.size()];
    			for(int i = 0; i < l.size(); i++){
    				Element e = (Element)l.get(i);
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
        Utils.setAttribute("schedule", schedule, _runtime);
    }

}
