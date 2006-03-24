package sos.scheduler.editor.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;


public class DaysListener {
	public static final int WEEKDAYS = 0;
	public static final int MONTHDAYS = 1;
	public static final int ULTIMOS = 2;
	
	private DomParser _dom;

	private Element _runtime;

	/** 0 = weekdays 1 = monthdays 2 = ultimos */
	private int _type = 0;

	private static String[] _elementName = { "weekdays", "monthdays", "ultimos" };

	private static String[] _weekdays = { "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday", "Sunday" };

	private static String[] _monthdays = { "1st", "2nd", "3rd", "4th", "5th",
			"6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th", "14th",
			"15th", "16th", "17th", "18th", "19th", "20th", "21st", "22nd",
			"23rd", "24th", "25th", "26th", "27th", "28th", "29th", "30th",
			"31st" };

	private static String[] _ultimos = { "last day", "1 day", "2 days",
			"3 days", "4 days", "5 days", "6 days", "7 days", "8 days",
			"9 days", "10 days", "11 days", "12 days", "13 days", "14 days",
			"15 days", "16 days", "17 days", "18 days", "19 days", "20 days",
			"21 days", "22 days", "23 days", "24 days", "25 days", "26 days",
			"27 days", "28 days", "29 days", "30 days" };

	private static String[][] _days = { _weekdays, _monthdays, _ultimos };

	private static int[] _offset = { 1, 1, 0 };

	private String[] _usedDays = null;
	
	private Element[] _dayElements = null;
	

	public DaysListener(DomParser dom, Element runtime, int type) {
		if (type != WEEKDAYS && type != MONTHDAYS && type != ULTIMOS)
			throw new IllegalArgumentException("type must be 0, 1 or 2!");

		_dom = dom;
		_type = type;
		_runtime = runtime;
	}
	
	public static String[] getWeekdays() {
		return _weekdays;
	}
	
	public static String[] getMonthdays() {
		return _monthdays;
	}
	
	public static String[] getUltimos() {
		return _ultimos;
	}
	
	public String[] getUnusedDays() {
		if (_usedDays == null)
			setUsedDays();
		ArrayList unused = new ArrayList();
		for (int i = 0; i < _days[_type].length; i++) {
			boolean found = false;
			for (int j = 0; j < _usedDays.length; j++) {
				if (_days[_type][i].equalsIgnoreCase(_usedDays[j])) {
					found = true;
				}
			}
			if (!found)
				unused.add(_days[_type][i]);
		}
		return (String[]) unused.toArray(new String[0]);
	}

	public String[] getUsedDays() {
		if (_usedDays == null)
			setUsedDays();
		return _usedDays;
	}
	
	public Element[] getDayElements() {
		return _dayElements;
	}

	private void setUsedDays() {
		if (_runtime != null && _runtime.getChild(_elementName[_type]) != null) {
			Element daylist = _runtime.getChild(_elementName[_type]);
			List list = daylist.getChildren("day");
			int size = list.size();
			String[] days = new String[size];
			Element[] elements = new Element[size];
			Iterator it = list.iterator();
			int i = 0;
			while (it.hasNext()) {
				Element e = (Element) it.next();
				try {
					int day = new Integer(e.getAttributeValue("day"))
							.intValue();
					days[i] = _days[_type][day - _offset[_type]];
					elements[i++] = e;
				} catch (Exception ex) {
					System.out.println("Invalid day element in "
							+ _elementName[_type]);
				}
			}
			_usedDays = sort(days, elements);
		} else
			_usedDays = new String[0];
	}

	private String[] sort(String[] daylist, Element[] elements) {
		int size = daylist.length;
		String[] sorted = new String[size];
		_dayElements = new Element[size];
		int index = 0;
		for (int i = 0; i < _days[_type].length; i++) {
			for (int j = 0; j < daylist.length; j++)
				if (_days[_type][i].equalsIgnoreCase(daylist[j])) {
					sorted[index] = _days[_type][i];
					_dayElements[index++] = elements[j];
				}
		}
		return sorted;
	}

	private int getDayNumber(String day) {
		for (int i = 0; i < _days[_type].length; i++) {
			if (_days[_type][i].equalsIgnoreCase(day))
				return i + _offset[_type];
		}
		return -1;
	}

	public void addDay(String day) {
		Element daylist = _runtime.getChild(_elementName[_type]);
		if (daylist == null) {
			daylist = new Element(_elementName[_type]);
			_runtime.addContent(daylist);
		}

		daylist.addContent(new Element("day").setAttribute("day", ""
				+ getDayNumber(day)));
		_dom.setChanged(true);

		setUsedDays();
	}

	public void deleteDay(String day) {
		Element daylist = _runtime.getChild(_elementName[_type]);
		if (daylist != null) {
			List list = daylist.getChildren("day");
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				if (e.getAttributeValue("day") != null
						&& e.getAttributeValue("day").equals(
								"" + getDayNumber(day))) {
					e.detach();
					
					// remove empty tag
					if(list.size() == 0)
						_runtime.removeChild(_elementName[_type]);
					
					_dom.setChanged(true);
					setUsedDays();
					break;
				}
			}
		}
	}
	
	public void fillTreeDays(TreeItem parent, boolean expand) {
		parent.removeAll();
		if(_usedDays == null)
			setUsedDays();
		
		for(int i = 0; i < _usedDays.length; i++) {
			TreeItem item = new TreeItem(parent, SWT.NONE);
			item.setText(_usedDays[i]);
			item.setData(new TreeData(Editor.PERIODS, _dayElements[i], Options.getHelpURL("periods")));
		}
		parent.setExpanded(expand);
	}
}
