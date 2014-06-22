package sos.scheduler.editor.conf.listeners;


import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class MailListener {
	
    //public final static int            NONE              = 0;

    private             SchedulerDom   _dom              = null;

    private             Element        _parent           = null;
    
    private             Element        _settings          = null;
    

    public MailListener(SchedulerDom dom, Element parent) {
    	
        _dom = dom;
        _parent = parent;
        
        _settings = _parent.getChild("settings");
        
    }

    private void setMail() {

    	_settings = _parent.getChild("settings");
    	if (_settings == null) {
    		_settings =   new Element("settings");
    		_parent.addContent(_settings);
    	}

    }

    public void setValue(String name, String value) {
    	setValue(name, value, "");
    	/*setMail();
		Utils.setAttribute(name, value, _settings, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
		*/
	}

    public void setValue(String name, String value, String default_) {
    	if(value == null || value.length() == 0) {
    		if(_settings != null) {
    			//return;
    			_settings.removeChild(name);
    			if(_settings.getContentSize() == 0 && _parent != null) {
    				_parent.removeContent(_settings);
    			}
    			_dom.setChanged(true);
    			if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
    			return;
    		}else{
    			return;
    		}
    	}
    	setMail();
		//Utils.setAttribute(name, value, default_, _settings, _dom);
    	
    	Element elem = null;
    	if(_settings.getChild(name) == null) {
    		elem = new Element(name);
    		_settings.addContent(elem);
    	}else 
    		elem = _settings.getChild(name);
    	
    	elem.setText(value);
    	
    	_dom.setChanged(true);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_parent), SchedulerDom.MODIFY);
		
	}
    
    
    public String getValue(String name) {
    	if(_settings == null)
    		return "";
    	Element elem = _settings.getChild(name);
    	if(elem == null)
    		return "";
    	
      //return Utils.getAttributeValue(name, _settings);
    	return elem.getTextNormalize();
    }

}
