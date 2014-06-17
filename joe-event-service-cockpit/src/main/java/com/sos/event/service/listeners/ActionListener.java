package com.sos.event.service.listeners;


import org.jdom.Element;

import com.sos.event.service.actions.ActionsDom;
import com.sos.event.service.forms.ActionsForm;

public class ActionListener {
   
	private ActionsDom           _dom                 = null;

    private Element              _action             = null;
    
    private ActionsForm gui                 = null;

   
    public ActionListener(ActionsDom dom, Element action, ActionsForm _gui) {
        _dom = dom;
        _action = action;
        gui = _gui;
    }


	public String getName() {
		return Utils.getAttributeValue("name", _action);
	}


	public void setName(String name) {
		Utils.setAttribute("name", name, _action);
		gui.updateAction(name);
		_dom.setChanged(true);
	}
   
   

}
