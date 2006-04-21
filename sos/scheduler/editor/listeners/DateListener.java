package sos.scheduler.editor.listeners;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;

public class DateListener implements Comparator {
	
	
	public static final int HOLIDAY = 0;
	public static final int DATE = 1;
	
	private DomParser _dom;
	/** 0 = holiday 1 = date */
	private int _type;
	
	private static String[] _elementName = {"holiday", "date"};

	private Element _element;

	private Element _parent;

	private List _list;

	public DateListener(DomParser dom, Element element, int type) {
		_dom = dom;
		_element = element;
		_type = type;
		
		if(type == 0 && _element != null)
			_parent = _element.getChild("holidays");
		else
			_parent = _element;
		if (_parent != null) {
			_list = _parent.getChildren(_elementName[_type]);
			sort();
		}
	}

	public void fillList(org.eclipse.swt.widgets.List list) {
		list.removeAll();
		if (_list != null) {
			Iterator it = _list.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				if (e.getAttributeValue("date") != null)
					list.add(e.getAttributeValue("date"));
			}
		}
	}

	public void addDate(int year, int month, int day) {
		if (_parent == null && _type == 0) {
			_parent = new Element("holidays");
			_element.addContent(_parent);
			_list = _parent.getChildren("holiday");
		}

		Element date = new Element(_elementName[_type]);
		date.setAttribute("date", asStr(year) + "-" + asStr(month) + "-"
				+ asStr(day));
		_list.add(date);
		sort();
		_dom.setChanged(true);
	}

	private String asStr(int value) {
		if (value < 10)
			return "0" + value;
		else
			return "" + value;
	}

	public void removeDate(int index) {
		if (index >= 0 && index < _list.size()) {
			_list.remove(index);
			if (_list.size() == 0 && _type == 0) {
				_element.removeChild("holidays");
				_parent = null;
			}
			_dom.setChanged(true);
		} else {
			System.out.println("Bad index " + index + " for " + _elementName[_type]);
		}
	}

	public int[] getDate(int index) {
		Element element = (Element) _list.get(index);
		String date = element.getAttributeValue("date");
		String[] str = date.split("-");
		try {
			int[] values = new int[3];
			values[0] = new Integer(str[0]).intValue();
			values[1] = new Integer(str[1]).intValue();
			values[2] = new Integer(str[2]).intValue();
			return values;
		} catch (Exception ex) {
			System.out.println("Invalid " + _elementName[_type] + " date: " + date);
			element.setAttribute("date", "1970-01-01");
			return new int[] { 1970, 1, 1 };
		}
	}
	
	public int[] getNow() {
		Calendar cal = Calendar.getInstance();
		int[] date = new int[3];
		date[0] = cal.get(Calendar.YEAR);
		date[1] = cal.get(Calendar.MONTH) + 1;
		date[2] = cal.get(Calendar.DAY_OF_MONTH);
		return date;
	}

	public void fillTreeDays(TreeItem parent, boolean expand) {
		if (_list != null) {
			Iterator it = _list.iterator();
			parent.removeAll();

			while (it.hasNext()) {
				Element e = (Element) it.next();
				if (e.getAttributeValue("date") != null) {
					TreeItem item = new TreeItem(parent, SWT.NONE);
					item.setText(e.getAttributeValue("date"));
					item.setData(new TreeData(Editor.PERIODS, e, Options.getHelpURL("periods")));
				}
			}
		}
		parent.setExpanded(expand);
	}
	
	public boolean exists(int year, int month, int day) {
		if(_list != null) {
			for(int i = 0; i < _list.size(); i++) {
				int[] date = getDate(i);
				if(year == date[0] && month == date[1] && day == date[2])
					return true;
			}
		}
		return false;
	}

	private void sort() {
		if(_list != null && _parent != null) {
			List copy = new ArrayList(_list);
			Collections.sort(copy, this);
			_parent.removeChildren(_elementName[_type]);
			_parent.addContent(copy);
			_list = _parent.getChildren(_elementName[_type]);
		}
	}
	
	public int compare(Object o1, Object o2) {
		if(o1 instanceof Element && o2 instanceof Element) {
			String date1 = ((Element)o1).getAttributeValue("date");
			String date2 = ((Element)o2).getAttributeValue("date");
			if(date1 == null)
				date1 = "";
			if(date2 == null)
				date2 = "";
			return date1.compareTo(date2); 
		}
		return 0;
	}
}
