package sos.scheduler.editor.conf.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;

public class PeriodsListener {
	
	
	private SchedulerDom          _dom        = null;
	
	private Element               _parent     = null;
	
	private List                  _list       = null;
	
	private int                   _period     = -1;
	
	private List                  _listOfAt   = null;
		
	private ISchedulerUpdate      _main        = null;
	
	
	public PeriodsListener(SchedulerDom dom, Element parent) {
		this(dom, parent, null);		
	}
	
	public PeriodsListener(SchedulerDom dom, Element parent, ISchedulerUpdate main) {
		_dom    = dom;
		_parent = parent; 
		_main   = main;
		
		if(_parent.getName().equals("at")) {
			_list = getDateChildren();
		} else {
			_list = parent.getChildren("period");
		}
				
		_listOfAt = getAtChildren();
		
	}
	
	
	private List getDateChildren() {
		List retval = new ArrayList();
		String date = Utils.getAttributeValue("at",_parent).substring(0, 10);
		if(_parent.getParentElement() == null) {
			return retval;
		}
		List parentDates = _parent.getParentElement().getChildren("date");
		for(int i=0; i < parentDates.size(); i++) {
			Element d = (Element)parentDates.get(i);
			if(Utils.getAttributeValue("date", d).equals(date)){
				retval = d.getChildren("period"); 
				return retval;
			}
		}
		
		return retval;
	}
	
	
	private List getAtChildren() {
		//List ats = null;
		ArrayList retval = new ArrayList();
		
		if(_parent != null && _parent.getParentElement() != null) {
			List l = null;
			String date = "";
			l = _parent.getParentElement().getChildren("at");
			if(_parent.getName().equals("at")) {    				
				date = Utils.getAttributeValue("at",_parent).substring(0, 10);
			}else {    				
				date = Utils.getAttributeValue("date",_parent);
			}
			//ats = _parent.getChildren();
			
			for (int i=0; i <l.size(); i++) {
				Element at = (Element)l.get(i);
				if(Utils.getAttributeValue("at", at).startsWith(date)){
					retval.add(at);
				}
			}
		}
		
		return retval;
	}
	
	public boolean isOnOrder() {
		
		Element job = _parent;
				
		if(job.getParentElement()==null)
			return false;		
		
		//mögliche Vaterknoten herausfinden
		while (!job.getName().equals("job") 
				&& !job.getName().equals("add_order")
				&& !job.getName().equals("order")
				&& !job.getName().equals("schedule")
				&& !job.getName().equals("config"))
			job = job.getParentElement();
		
		return		
		Utils.isAttributeValue("order", job) && job.getName().equals("job") || 
		Utils.isAttributeValue("id", job)&& job.getName().equals("add_order");

		/*return		
		Utils.isAttributeValue("order", job) && job.getName().equals("job") || 
		Utils.isAttributeValue("id", job)&& job.getName().equals("add_order") || 
		job.getName().equals("order")|| 
		job.getName().equals("config")*/
	}
	
	
	public void fillTable(Table table) {
		table.removeAll();
		
		if (_list != null) {
			for (Iterator it = _list.iterator(); it.hasNext();) {
				Element e = (Element) it.next();
				TableItem item = new TableItem(table, SWT.NONE);
				if(Utils.getAttributeValue("single_start", e).length() > 0)
					item.setText(0, "");
				else
					item.setText(0, Utils.isAttributeValue("let_run", e) ? "Yes" : "No");
				item.setText(1, Utils.getAttributeValue("begin", e));
				item.setText(2, Utils.getAttributeValue("end", e));
				item.setText(3, Utils.getAttributeValue("repeat", e));
				item.setText(4, Utils.getAttributeValue("single_start", e));
				item.setText(5, Utils.getAttributeValue("absolute_repeat", e));
				
				
			}
		}        
		ArrayList remList = new ArrayList();
		for(int i =0; i < _listOfAt.size(); i++) {
			Element at = (Element)_listOfAt.get(i);
			String sAT = Utils.getAttributeValue("at", at);
			
			if(sAT != null && sAT.length()> 0) {
				String date = sAT.substring(0, 10);				
				String time = sAT.substring((sAT.indexOf(" ") > -1 ? sAT.indexOf(" ") + 1 : sAT.length()));
				if(time.length()==0) {
					//at ohne Zeitangabe werden gelöscht
					remList.add(at);
				} else {					
					if(date.equalsIgnoreCase(Utils.getAttributeValue("date", _parent)) ||
							Utils.getAttributeValue("at", _parent).startsWith(date)){
						TableItem item = new TableItem(table, SWT.NONE);    		
						item.setText(0, "No");
						item.setText(1, "");
						item.setText(2, "");
						item.setText(3, "");                         
						item.setText(4, time);
					}
				}
			}
		}
		_listOfAt.removeAll(remList);
		
	} 
	
	public void removePeriod(Element el) {
		if(_list.contains(el)){
			_list.remove(el);
			_period = -1;	
		} else if(_listOfAt.contains(el)){
			
			el.getParentElement().getChildren("at").remove(el);
			_listOfAt.remove(el);
			
		}
		_dom.setChanged(true);
		
		_dom.setChangedForDirectory(_parent, SchedulerDom.MODIFY);
		if(_main != null) {
			_main.updateFont();
		}
	}
	
	public void removePeriod(int index) {
		
		if (index >= 0 && index < _list.size()) {
			_list.remove(index);
			_period = -1;			
		} else {
			if(_parent.getName().equals("at") || _list.size()==0 && _listOfAt.size() > 0) {
				int i = _list.size() - (index); 
				if(i < 0) 
					i = i*(-1);
				Element el = (Element)(_listOfAt.get(i));	
				
				if(_list.size()==0 && _listOfAt.size()==1) {
					//das letzte at Element wird gelöscht. Das Datum steht auf der Treeview
					String[] date = Utils.getAttributeValue("at", el).substring(0, 10).split("-");
					
					DateListener dateListener = new DateListener(_dom, _parent.getParentElement(), 1); 
					if(!dateListener.exists(Utils.str2int(date[0]), Utils.str2int(date[1]), Utils.str2int(date[2]))) {    					
						_parent = dateListener.addDate(Utils.str2int(date[0]), Utils.str2int(date[1]), Utils.str2int(date[2]));
						_list = _parent.getChildren("period");
					}
				}
				
				if(i + 1 < _listOfAt.size())
					_parent =(Element)_listOfAt.get(i + 1);
				
				el.getParentElement().getChildren("at").remove(_listOfAt.get(i));
				
				
				_listOfAt.remove(i);				
				
			} else {
				System.out.println("Bad period index for removal!");
			}
		}    	
		
		_dom.setChanged(true);
		_dom.setChangedForDirectory(_parent, SchedulerDom.MODIFY);
		if(_main != null) {
			_main.updateFont();
		}
	
	}
	
	
	public Element getNewPeriod() {
		_period = -1;
		return new Element("period");
	}
	
	
	public Element getPeriod(int index) {
		if (index >= 0 && index < _list.size()) {
			_period = index;
			//return (Element) ((Element) _list.get(index)).clone();
			return (Element) _list.get(index);
		} else {			
			if(_parent.getName().equals("at") || _listOfAt.size() > 0) {
				int i = _list.size() - (index); 
				if(i < 0) 
					i = i*(-1);
				Element el = (Element)(_listOfAt.get(i));
				return el;
			} 
			System.out.println("Bad period index for selection!");
			return null;
			
		}
	}
	

	public void applyPeriod(Element period) {
		if(!period.getName().equals("at")) {			
			if (_period == -1)    			
				_list.add(period);
			else {				
				if(!_list.contains(period))
					_list.set(_period, period);
				
			}
		}
		_dom.setChanged(true);
		//Element job = getJobElement(_parent);
		_dom.setChangedForDirectory(_parent, SchedulerDom.MODIFY);
		if(_main != null) {
			_main.updateFont();
		}
	}
	
	public List get_list() {
		return _list;
	}
	
	public Element getAtElement(String at) {
		String date = "";
		if(_parent.getName().equalsIgnoreCase("at")){
			date = Utils.getAttributeValue("at", _parent);
			date = date.substring(0, date.indexOf(" "));
		} else {
			date = Utils.getAttributeValue("date", _parent);
		}
		
		for (int i =0; i < _listOfAt.size(); i++) {
			Element eat = (Element)(_listOfAt.get(i));
			String[] seat = Utils.getAttributeValue("at", eat).split(" ");
			String sDate = seat[0];
			String time = seat[1];
			if(sDate.equalsIgnoreCase(date) && time.equalsIgnoreCase(at)) {
				return eat;
			}
		}
		return null;
		
	}
		
	
}
