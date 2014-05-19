package sos.scheduler.editor.actions.listeners;


import org.jdom.Element;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.forms.ActionsForm;
import sos.scheduler.editor.app.Utils;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import java.util.List;
import java.util.ArrayList;


public class EventsListener {
   
	private ActionsDom           _dom                 = null;

    private Element              _action              = null;
    
    private Element              _events              = null;
    
    private ActionsForm          gui                  = null;

   
    
    public EventsListener(ActionsDom dom, Element action, ActionsForm _gui) {
        _dom = dom;
        _action = action;
        _events = _action.getChild("events");
        gui = _gui;
    }


	public String getLogic() {
		if(_events != null)
			return Utils.getAttributeValue("logic", _events);
		else
			return "";
	}

	public String[] getEventClasses() {
		String[] retVal = new String[]{};
		try {
			if(_events != null) {
				List l = _events.getChildren("event_group");
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
           
			System.out.println(e.toString());
		}
		return retVal;
	}

	public ArrayList getEventClassAndId(String group) {
		ArrayList list = new ArrayList();
		try {
			if(_events != null) {
				List l = _events.getChildren("event_group");
				
				for(int i=0; i < l.size(); i++) {					
					Element eventGroup = (Element)l.get(i);
					if(Utils.getAttributeValue("group", eventGroup).equalsIgnoreCase(group)){
						List l2 = eventGroup.getChildren("event");
						for(int j = 0; j < l2.size(); j++) {
							Element event = (Element)l2.get(j);
							String eventName = Utils.getAttributeValue("event_name", event);
							if(eventName.length()>0)
								list.add(eventName);
							/*String eventClass = Utils.getAttributeValue("event_class", event); 
							String eventId = Utils.getAttributeValue("event_id", event);
							if(eventClass.concat(eventId).length() > 0 && !list.contains(eventClass + "." + eventId)) {
								list.add(eventClass + "." + eventId);	
							}*/
						}
					}
				}
				
			}
		} catch (Exception e) {
           
			System.out.println(e.toString());
		}
		return list;
	}
	
	public ArrayList getGroups() {
		ArrayList list = new ArrayList();
		try {
			if(_events != null) {
				List l = _events.getChildren("event_group");				
				for(int i=0; i < l.size(); i++) {					
					Element event = (Element)l.get(i);
					String eventClass = Utils.getAttributeValue("group", event); 
					if(eventClass.length() > 0 && !list.contains(eventClass)) {
						list.add(Utils.getAttributeValue("group", event) + " ");	
					}
				}
				
			}
		} catch (Exception e) {
           
			System.out.println(e.toString());
		}
		return list;
	}
	
	public void setLogic(String logic) {
		/*if(_events == null)
			_action.addContent(new Element("event"));
			*/
		if(_events == null) {
			_events = new Element("events");
			_action.addContent(_events);
		}
		Utils.setAttribute("logic", logic, _events);
		_dom.setChanged(true);
		//gui.updateAction(logic);
	}
 
	public void fillEvents(Table table){
		table.removeAll();
		if(_events != null) {
			List l = _events.getChildren("event_group");
			for(int i = 0; i < l.size(); i++){
				Element event = (Element)l.get(i);
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, Utils.getAttributeValue("group", event));
				item.setText(1, Utils.getAttributeValue("logic", event));
				item.setText(2, Utils.getAttributeValue("event_class", event));
				item.setData(event);
			}
		}
	}
   
	public void apply(String group, String eventClass, String groupLogic, Table table) {
		Element eventGroup = null;
		
		if(_events == null) {
			_events = new Element("events");
			_action.addContent(_events);
		}
			
		
		if(table.getSelectionCount() > 0) {
		   eventGroup = (Element)table.getSelection()[0].getData();	
		} else {
	       eventGroup = new Element("event_group");
	       _events.addContent(eventGroup);
		}
	       Utils.setAttribute("group", group, eventGroup);
	       Utils.setAttribute("event_class", eventClass, eventGroup);
	       Utils.setAttribute("logic", groupLogic, eventGroup);
	       
	       fillEvents(table);
	       gui.updateEvents(_action);
	       _dom.setChanged(true);
	       table.deselectAll();
	}
	
	public void removeEvent(Table table) {
	        if(table.getSelectionCount() > 0) {
	        	TableItem item = table.getSelection()[0];
	        	Element elem = (Element)item.getData();
	        	elem.detach();
	        	table.remove(table.getSelectionIndex());
	        	fillEvents(table);
	        	_dom.setChanged(true);
	        	 gui.updateEvents(_action);
	        }
	        if(_events != null && _events.getChildren().isEmpty())
	        	_events.detach();
	}
	
	public String getActionname() {
		if(_action != null)
			return Utils.getAttributeValue("name", _action);
		else 
			return "";
	}
	
}
