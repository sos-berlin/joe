package sos.scheduler.editor.actions.listeners;


import org.jdom.Element;
import org.jdom.xpath.XPath;

import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.forms.ActionsForm;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;

import java.util.ArrayList;
import java.util.List;

public class EventListener {
   
	private ActionsDom           _dom                 = null;

    private Element              _eventGroup              = null;
    
   // private ActionsForm          gui                  = null;

    private int                  type                 = -1;
   
    public EventListener(ActionsDom dom, Element eventGroup, ActionsForm _gui, int type_) {
        _dom = dom;
        _eventGroup = eventGroup;
        type = type_;
        //gui = _gui;
    }


	
 
	public void fillEvent(Table table){
		table.removeAll();
		//if(_events != null) {
		List l = null;
		//if(_eventGroup.getName().equals("commands")) {
		try {
			if(type == Editor.ADD_EVENT_GROUP) {		
				/*if(_eventGroup.getChild("add_event") != null) {
					_eventGroup = _eventGroup.getChild("add_event");
				}*/

				XPath x3 = XPath.newInstance("//add_event/event");				 
				l = x3.selectNodes(_dom.getDoc());


			} else if(type == Editor.REMOVE_EVENT_GROUP) {
				/*if(_eventGroup.getChild("remove_event") != null) {
					_eventGroup = _eventGroup.getChild("remove_event");
				}
				l = _eventGroup.getChildren("remove_event");
				*/
				XPath x3 = XPath.newInstance("//remove_event/event");				 
				l = x3.selectNodes(_dom.getDoc());

			} else {
				l = _eventGroup.getChildren("event");		
			}
		} catch(Exception e) {
			MainWindow.message(e.getMessage(), SWT.ICON_WARNING);	
		}
		//}
		
			for(int i = 0; i < l.size(); i++){
				Element event = (Element)l.get(i);
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, Utils.getAttributeValue("event_name", event));
				item.setText(1, Utils.getAttributeValue("event_id", event));
				item.setText(2, Utils.getAttributeValue("event_title", event));
				item.setText(3, Utils.getAttributeValue("event_class", event));
				
				item.setText(4, Utils.getAttributeValue("job_name", event));
				item.setText(5, Utils.getAttributeValue("job_chain", event));
				item.setText(6, Utils.getAttributeValue("order_id", event));
				item.setText(7, Utils.getAttributeValue("comment", event));
				
				item.setData(event);
			}
		}
	
   
	public void apply(String eventName, String eventId, String eventClass, String eventTitle,
			String jobname, String jobChain, String orderId, String comment,
			Table table) {
		
		Element event = null;
		
		if(table.getSelectionCount() > 0) {
			event = (Element)table.getSelection()[0].getData();	
		} else {					
			event = new Element("event");
			
			if(type == Editor.ADD_EVENT_GROUP) {
			
				Element addEvent = new Element("add_event");
				addEvent.addContent(event);
				_eventGroup.addContent(addEvent);   
			
			}else if(type == Editor.REMOVE_EVENT_GROUP){
			
					Element removeEvent = new Element("remove_event");
					removeEvent.addContent(event);
					_eventGroup.addContent(removeEvent);            					   
								
			} else {
				_eventGroup.addContent(event);
			}
		}

		Utils.setAttribute("event_name", eventName, event);
		Utils.setAttribute("event_id", eventId, event);
		Utils.setAttribute("event_class", eventClass, event);
		Utils.setAttribute("event_title", eventTitle, event);
		Utils.setAttribute("job_name", jobname, event);
		Utils.setAttribute("job_chain", jobChain, event);
		Utils.setAttribute("order_id", orderId, event);
		Utils.setAttribute("comment", comment, event);

		fillEvent(table);

		_dom.setChanged(true);
	}
	
	public void removeEvent(Table table) {
	        if(table.getSelectionCount() > 0) {
	        	TableItem item = table.getSelection()[0];
	        	Element elem = (Element)item.getData();
	        	elem.detach();
	        	table.remove(table.getSelectionIndex());
	        	fillEvent(table);
	        	_dom.setChanged(true);
	        }
	}
	
	public String getEventGroupName() {
		if(_eventGroup != null)			
			return Utils.getAttributeValue("group", _eventGroup);
		else 
			return "";
	}
	
	public String getActionName() {
		if(_eventGroup != null && _eventGroup.getParentElement() != null && _eventGroup.getParentElement().getParentElement() != null)			
			return Utils.getAttributeValue("name", _eventGroup.getParentElement().getParentElement());
		else 
			return "";
	}
	
	public String[] getEventClasses() {
		String[] retVal = new String[]{};
		try {
			if(_eventGroup != null) {
				List l = _eventGroup.getChildren("event");
				ArrayList list = new ArrayList();
				for(int i=0; i < l.size(); i++) {					
					Element event = (Element)l.get(i);
					String eventClass = Utils.getAttributeValue("event_class", event); 
					if(eventClass.length() > 0 && !list.contains(eventClass)) {
						list.add(Utils.getAttributeValue("event_class", event));	
					}
				}
				if(list.size() > 0)
					retVal = new String[list.size()];
				for(int i = 0; i < list.size(); i++) {
					retVal[i] = list.get(i).toString(); 
				}
			}
		} catch (Exception e) {
           //tu nichts
			System.out.println(e.toString());
		}
		return retVal;
	}
	
}
