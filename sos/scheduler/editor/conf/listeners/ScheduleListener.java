package sos.scheduler.editor.conf.listeners;

import org.jdom.Element;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;


public class ScheduleListener { 
	
	
	private    SchedulerDom     _dom                   = null;
	
	private    ISchedulerUpdate _main                  = null;
	
	private    Element          _schedule              = null;
	
	
	public ScheduleListener(SchedulerDom dom, ISchedulerUpdate update, Element schedule) {
		
		_dom = dom;
		_main = update;
		_schedule = schedule;
		
	}

	public String getName() {
		return Utils.getAttributeValue("name", _schedule);
	}

	public void setName(String _name) {
		_dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.DELETE);
		Utils.setAttribute("name", _name, _schedule);
		_main.updateTreeItem(_name);
		_dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);	
		
		
	}

	public Element getSchedule() {
		return _schedule;
	}
	
	public void setTitle(String title) {		
		Utils.setAttribute("title", title, _schedule);		
		_dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);	
		
	}
	
	public String getTitle() {
		return Utils.getAttributeValue("title", _schedule);
	}

	
	public void setValidFrom(String validFrom) {
		
		Utils.setAttribute("valid_from", validFrom, _schedule);		
		_dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);	
		
	}
	
	public String getValidFrom() {
		return Utils.getAttributeValue("valid_from", _schedule);
	}

	
	public void setValidTo(String validTo) {
		
				/*if(!sos.util.SOSDate.isValidDate(validTo, DateFormat.MEDIUM, Locale.getDefault()))
						System.out.println(validTo + " not a valid day");*/
		/*try {
			DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
			formatter.parse(validTo.substring(0));
		} catch(Exception e) {
			System.out.println(validTo + " not a valid day" + e.getMessage());
		}*/
		Utils.setAttribute("valid_to", validTo, _schedule);		
		_dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);	
		
	}
	
	public String getValidTo() {
		return Utils.getAttributeValue("valid_to", _schedule);
	}

	
	public void setSubstitut(String substitute) {		
		Utils.setAttribute("substitute", substitute, _schedule);		
		_dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);	
		
	}
	
	public String getSubstitute() {
		return Utils.getAttributeValue("substitute", _schedule);
	}

	public String[] getAllSchedules() {
		
		java.util.List s = null;
		 
		if(_dom.isLifeElement() || _schedule == null) {
			return null;
		}
				
		/*Element schedules = _schedule.getParentElement();
		if(schedules == null)
			return null;
		*/
		
		String currSchedulename = Utils.getAttributeValue("name",_schedule);
		Element schedules = null;
		if(_schedule.getParentElement() != null)
			schedules = (Element)_schedule.getParentElement().clone();
		else 
			return null;
		
		s = schedules.getChildren("schedule");		
		
		/*		java.util.ArrayList _schedules = new java.util.ArrayList();
		for(int i = 0; i < s.size(); i++) {
			Element e = (Element)s.get(i);
			if(!e.equals(_schedule)) {
				_schedules.add(Utils.getAttributeValue("name", e));
			}
		}
		*/
		//convert in String[]
		String[] str = new String[s.size()-1];
		int index = 0;
		for(int i = 0; i < s.size(); i++) {
			if(s.get(i) instanceof Element) {
				Element e = (Element)s.get(i);
				if(!Utils.getAttributeValue("name", e).equals(currSchedulename)) {
					str[index] = Utils.getAttributeValue("name", e);
					index++;
				}
			}
		}
				
		return str;
		
			
		
	}
}
