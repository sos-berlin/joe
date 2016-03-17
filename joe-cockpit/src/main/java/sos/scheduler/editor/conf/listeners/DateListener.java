package sos.scheduler.editor.conf.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class DateListener implements Comparator {

    public static final int HOLIDAY = 0;
    public static final int DATE = 1;
    private SchedulerDom _dom = null;
    /** 0 = holiday 1 = date */
    private int _type = -1;
    private static String[] _elementName = { "holiday", "date" };
    private Element _element = null;
    private Element _parent = null;
    private List _list = null;
    private List _listOfAt = null;

    public DateListener(SchedulerDom dom, Element element, int type) {
        _dom = dom;
        _element = element;
        _type = type;
        if (type == 0 && _element != null) {
            _parent = _element.getChild("holidays");
        } else {
            _parent = _element;
        }
        if (_parent != null) {
            _list = _parent.getChildren(_elementName[_type]);
            if (type == 1) {
                _listOfAt = _parent.getChildren("at");
            }
            sort();
        }
    }

    public void fillList(org.eclipse.swt.widgets.List list) {
        list.removeAll();
        if (_list != null) {
            Iterator it = _list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("date") != null) {
                    list.add(e.getAttributeValue("date"));
                }
            }
        }
        if (_listOfAt != null) {
            Iterator it = _listOfAt.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("at") != null) {
                    String date = Utils.getAttributeValue("at", e);
                    date = date.substring(0, 10);
                    String[] split = date.split("-");
                    if (!exists(Utils.str2int(split[0]), Utils.str2int(split[1]), Utils.str2int(split[2])) && !dateExistInList(list, date)) {
                        list.add(date);
                    }
                }
            }
        }
    }

    private boolean dateExistInList(TreeItem parent, String date) {
        for (int i = 0; i < parent.getItemCount(); i++) {
            if (parent.getItem(i).getText().equalsIgnoreCase(date)) {
                return true;
            }
        }
        return false;
    }

    private boolean dateExistInList(org.eclipse.swt.widgets.List list, String date) {
        for (int i = 0; i < list.getItemCount(); i++) {
            if (list.getItem(i).equalsIgnoreCase(date)) {
                return true;
            }
        }
        return false;
    }

    public Element addDate(int year, int month, int day) {
        if (_parent == null && _type == 0) {
            _parent = new Element("holidays");
            _element.addContent(_parent);
            _list = _parent.getChildren("holiday");
        }
        if (_list == null && _type == 0) {
            _list = _parent.getChildren("holiday");
        }
        if (_list == null && _type == 1) {
            _list = _parent.getChildren(_elementName[_type]);
        }
        Element date = new Element(_elementName[_type]);
        date.setAttribute("date", asStr(year) + "-" + asStr(month) + "-" + asStr(day));
        _list.add(date);
        sort();
        _dom.setChanged(true);
        if (_dom.isDirectory() && _element.getParentElement() != null) {
            _dom.setChangedForDirectory(_element, SchedulerDom.MODIFY);
        }
        return date;
    }

    public String asStr(int value) {
        if (value < 10) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }

    public void removeDate(int index) {
        String delDate = "";
        if (index >= 0 && index < _list.size()) {
            delDate = Utils.getAttributeValue("date", (Element) (_list.get(index)));
        } else {
            int i = _list.size() - (index);
            if (i < 0) {
                i = i * (-1);
            }
            Element atE = (Element) (_listOfAt.get(i));
            delDate = Utils.getAttributeValue("at", atE).substring(0, 10);
        }
        if (index >= 0 && index < _list.size()) {
            _list.remove(index);
            if (_list.isEmpty() && _type == 0 && (_parent.getChildren("include") == null || _parent.getChildren("include").isEmpty())) {
                _element.removeChild("holidays");
                _parent = null;
            }
            _dom.setChanged(true);
            if (_dom.isDirectory() && _element.getParentElement() != null) {
                _dom.setChangedForDirectory(_element, SchedulerDom.MODIFY);
            }
        } else {
            if (_listOfAt != null && !_listOfAt.isEmpty()) {
                index = _list.size() - index;
                if (index > -1) {
                    _listOfAt.remove(index);
                }
            } else {
                System.out.println("Bad index " + index + " for " + _elementName[_type]);
            }
        }
        if (_listOfAt != null) {
            ArrayList remList = new ArrayList();
            for (int i = 0; i < _listOfAt.size(); i++) {
                Element e = (Element) _listOfAt.get(i);
                String at = Utils.getAttributeValue("at", e);
                String date = at.substring(0, at.indexOf(" "));
                if (date.equalsIgnoreCase(delDate)) {
                    remList.add(e);
                }
            }
            for (int i = 0; i < remList.size(); i++) {
                _listOfAt.remove(remList.get(i));
            }
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
                    item.setData(new TreeData(JOEConstants.PERIODS, e, Options.getHelpURL("periods")));
                    if (!Utils.isElementEnabled("job", _dom, e)) {
                        item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                    }
                }
            }
        }
        ArrayList l = new ArrayList();
        if (_listOfAt != null) {
            Iterator it = _listOfAt.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("at") != null) {
                    String sDate = e.getAttributeValue("at").split(" ")[0];
                    String[] splitDate = sDate.split("-");
                    if (!exists(Utils.str2int(splitDate[0]), Utils.str2int(splitDate[1]), Utils.str2int(splitDate[2]))
                            && !dateExistInList(parent, sDate)) {
                        Element a = new Element("date");
                        Utils.setAttribute("date", Utils.getAttributeValue("at", e).substring(0, 10), a);
                        l.add(a);
                    }
                }
            }
        }
        for (int i = 0; i < l.size(); i++) {
            Element e = (Element) l.get(i);
            if (!dateExistInList(parent, Utils.getAttributeValue("date", e))) {
                _list.add(l.get(i));
                TreeItem item = new TreeItem(parent, SWT.NONE);
                item.setText(e.getAttributeValue("date"));
                item.setData(new TreeData(JOEConstants.PERIODS, (Element) l.get(i), Options.getHelpURL("periods")));
                if (!Utils.isElementEnabled("job", _dom, e)) {
                    item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                }
            }
        }
        parent.setExpanded(expand);
    }

    public boolean exists(int year, int month, int day) {
        if (_list != null) {
            for (int i = 0; i < _list.size(); i++) {
                int[] date = getDate(i);
                if (year == date[0] && month == date[1] && day == date[2]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void sort() {
        if (_list != null && _parent != null) {
            List copy = new ArrayList(_list);
            Collections.sort(copy, this);
            _parent.removeChildren(_elementName[_type]);
            _parent.addContent(copy);
            _list = _parent.getChildren(_elementName[_type]);
        }
    }

    public int compare(Object o1, Object o2) {
        if (o1 instanceof Element && o2 instanceof Element) {
            String date1 = ((Element) o1).getAttributeValue("date");
            String date2 = ((Element) o2).getAttributeValue("date");
            if (date1 == null) {
                date1 = "";
            }
            if (date2 == null) {
                date2 = "";
            }
            return date1.compareTo(date2);
        }
        return 0;
    }

    public String[] getIncludes() {
        if (_parent != null) {
            List includeList = _parent.getChildren("include");
            String[] includes = new String[includeList.size()];
            Iterator it = includeList.iterator();
            int i = 0;
            while (it.hasNext()) {
                Element include = (Element) it.next();
                String file = include.getAttributeValue("file");
                includes[i++] = file == null ? "" : file;
            }
            return includes;
        } else {
            return new String[0];
        }
    }

    public void fillTable(Table table) {
        table.removeAll();
        if (_parent != null) {
            List includeList = _parent.getChildren("include");
            for (int i = 0; i < includeList.size(); i++) {
                Element include = (Element) includeList.get(i);
                if (include.getAttributeValue("file") != null) {
                    String filename = Utils.getAttributeValue("file", include);
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setText(0, filename);
                    item.setText(1, "file");
                    String fname = new File(filename).getName();
                } else {
                    TableItem item = new TableItem(table, SWT.NONE);
                    String filename = Utils.getAttributeValue("live_file", include);
                    item.setText(0, filename);
                    item.setText(1, "live_file");
                }
            }
        }
    }

    public void addInclude(Table table, String filename, boolean isLive) {
        if (_parent == null && _type == 0) {
            _parent = new Element("holidays");
            _element.addContent(_parent);
        }
        if (_parent != null) {
            if (Options.getHolidaysDescription().get(filename) != null
                    && Options.getHolidaysDescription().get("file_" + Options.getHolidaysDescription().get(filename)) != null
                    && !Options.getHolidaysDescription().get("file_" + Options.getHolidaysDescription().get(filename)).toString().isEmpty()) {
                String home = Options.getSchedulerNormalizedHotFolder();
                filename = Options.getHolidaysDescription().get("file_" + Options.getHolidaysDescription().get(filename)).toString();
                if (filename.indexOf(home) > -1) {
                    filename = new java.io.File(home).getName() + "/" + filename.substring(home.length());
                }
                isLive = false;
            }
            _parent.addContent(new Element("include").setAttribute(isLive ? "live_file" : "file", filename));
            _dom.setChanged(true);
            if (_dom.isDirectory() && _element.getParentElement() != null) {
                _dom.setChangedForDirectory(_element, SchedulerDom.MODIFY);
            }
        } else {
            System.out.println("no script element defined!");
        }
    }

    public void removeInclude(int index) {
        if (_parent != null) {
            List includeList = _parent.getChildren("include");
            if (index >= 0 && index < includeList.size()) {
                includeList.remove(index);
                if (includeList.isEmpty() && _type == 0 && (_parent.getChildren() == null || _parent.getChildren().isEmpty())) {
                    _element.removeChild("holidays");
                    _parent = null;
                }
                _dom.setChanged(true);
                if (_dom.isDirectory() && _element.getParentElement() != null) {
                    _dom.setChangedForDirectory(_element, SchedulerDom.MODIFY);
                }
            } else {
                System.out.println("index " + index + " is out of range for include!");
            }
        } else {
            System.out.println("no script element defined!");
        }
    }

    public List get_list() {
        return _list;
    }

    public String[] getHolidayDescription() {
        java.util.Iterator h = Options.getHolidaysDescription().keySet().iterator();
        ArrayList list = new ArrayList();
        while (h.hasNext()) {
            String desc = h.next().toString();
            if (desc != null && !desc.isEmpty() && !desc.startsWith("file_") && !desc.startsWith("holiday_id_")) {
                list.add(desc);
            }
        }
        String[] ret = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ret[i] = list.get(i).toString();
        }
        return ret;
    }

}