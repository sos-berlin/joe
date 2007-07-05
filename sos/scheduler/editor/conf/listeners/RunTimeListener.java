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
          _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
      }
  }
    
    public String getFunction() {
    	String s = _runtime.getAttributeValue("start_time_function");
    	if (s==null)s="";
      return s;
    }
    

}
