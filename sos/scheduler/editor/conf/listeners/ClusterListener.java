package sos.scheduler.editor.conf.listeners;


import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class ClusterListener {
	
    public final static int            NONE              = 0;

    private             SchedulerDom   _dom              = null;

    private             Element        _parent           = null;
    
    private             Element        _cluster          = null;
    

    public ClusterListener(SchedulerDom dom, Element parent) {
    	
        _dom = dom;
        _parent = parent;
        setCluster();
        
    }

    private void setCluster() {
    	
       _cluster = _parent.getChild("cluster");
       if (_cluster == null) {
      	 _cluster =   new Element("cluster");
         _parent.addContent(_cluster);
          }
       
    }

    private void setAttributeValue(String element, String value) {
    	
        _cluster.setAttribute(element, value);
        _dom.setChanged(true);
        
    }


    public String getHeartbeatTimeout() {
      return Utils.getAttributeValue("heart_beat_timeout", _cluster);
    }
   
    public String getHeartbeatOwnTimeout() {
      return Utils.getAttributeValue("heart_beat_own_timeout", _cluster);
    }

    public String  getHeartbeatWarnTimeout() {
      return Utils.getAttributeValue("heart_beat_warn_timeout", _cluster);
    }


    public void setHeartbeatWarnTimeout(String heart_beat_warn_timeout) {
    	_cluster.removeAttribute("heart_beat_warn_timeout");
      if (!heart_beat_warn_timeout.equals(""))setAttributeValue("heart_beat_warn_timeout", heart_beat_warn_timeout);
    }

    public void setHeartbeatOwnTimeout(String heart_beat_own_timeout) {
    	_cluster.removeAttribute("heart_beat_own_timeout");
    	if (!heart_beat_own_timeout.equals(""))setAttributeValue("heart_beat_own_timeout", heart_beat_own_timeout);
    }
    
    public void setHeartbeatTimeout(String heart_beat_timeout) {
    	_cluster.removeAttribute("heart_beat_timeout");
    	if (!heart_beat_timeout.equals(""))setAttributeValue("heart_beat_timeout", heart_beat_timeout);
    }
    
    

 
}
