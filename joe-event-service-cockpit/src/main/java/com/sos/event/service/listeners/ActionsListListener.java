package com.sos.event.service.listeners;


import org.jdom.Element;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.app.Utils;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;

public class ActionsListListener {
   
	private ActionsDom           _dom                 = null;

    private Element              _actions             = null;

   
    public ActionsListListener(ActionsDom dom, Element actions) {
        _dom = dom;
        _actions = actions;
    }
   
    public String getActions() {
        return Utils.getAttributeValue("name", _actions);
    }
    
    public void newAction(String name) {
    	if(_actions == null)
    		_actions = _dom.getRoot();
    	
    	Element action = new Element("action");
    	Utils.setAttribute("name", name, action);
    	
    	_actions.addContent(action);
    	_dom.setChanged(true);
    }


    public void fillActions(Table table) {  
    	
    	if(_actions == null)
    		_actions = _dom.getRoot();
    	
        if(table != null) {
        	table.removeAll();
        	
        	java.util.List l = _actions.getChildren("action");
        	for(int i = 0; i < l.size(); i++) {
        		Element action = (Element)l.get(i);
        		TableItem item = new TableItem(table, SWT.NONE);        		
        		item.setText(Utils.getAttributeValue("name", action));
        		item.setData(action);
        		
        	}
        }
    }

    public void removeAction(Table table) {
    	if(table.getSelectionCount() > 0) {
    		TableItem item = table.getSelection()[0];
    		Element e = (Element) item.getData();
    		e.detach();
    		fillActions(table);
    		_dom.setChanged(true);
    	}
    }
   

}
