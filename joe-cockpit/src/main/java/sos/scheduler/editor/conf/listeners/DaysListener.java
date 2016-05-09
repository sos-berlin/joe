package sos.scheduler.editor.conf.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.forms.SchedulerForm;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.forms.DocumentationForm;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class DaysListener {

    private static final String[] ELEMENT_NAME = { "weekdays", "monthdays", "ultimos", "month" };
    private static final String[] WEEKDAYS_EN_SHORT = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
    private static final String[] WEEKDAYS_GER_SHORT = { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" };
    private static final String[] WEEKDAYS_GER = { "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag" };
    private static final String[] WEEKDAYS_ARRAY = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
    private static final String[] MONTHDAYS_ARRAY = { "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th",
            "14th", "15th", "16th", "17th", "18th", "19th", "20th", "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th", "30th",
            "31st" };
    private static final String[] ULTIMOS_ARRAY = { "last day", "1 day", "2 days", "3 days", "4 days", "5 days", "6 days", "7 days", "8 days",
            "9 days", "10 days", "11 days", "12 days", "13 days", "14 days", "15 days", "16 days", "17 days", "18 days", "19 days", "20 days",
            "21 days", "22 days", "23 days", "24 days", "25 days", "26 days", "27 days", "28 days", "29 days", "30 days" };
    private static final String[] MONTH = { "january", "february", "march", "april", "may", "june", "july", "august", "september", "october",
            "november", "december", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
    private static final String[][] DAYS = { WEEKDAYS_ARRAY, MONTHDAYS_ARRAY, ULTIMOS_ARRAY, MONTH };
    private static final int[] OFFSET = { 1, 1, 0, 1 };
    private String[] usedDays = null;
    private Element[] dayElements = null;
    private boolean isWeekdaysHoliday = false;
    private final SchedulerDom dom;
    private Element runtime;
    private int type = 0;
    public static final int WEEKDAYS = 0;
    public static final int MONTHDAYS = 1;
    public static final int ULTIMOS = 2;
    public static final int SPECIFIC_MONTHS = 3;
    public static final int SPECIFIC_DAY = 6;

    public DaysListener(SchedulerDom dom, Element runtime, int type, boolean isWeekdaysHoliday) {
        if (type != WEEKDAYS && type != MONTHDAYS && type != ULTIMOS && type != SPECIFIC_MONTHS) {
            throw new IllegalArgumentException("type must be 0, 1 or 6!");
        }
        this.isWeekdaysHoliday = isWeekdaysHoliday;
        this.dom = dom;
        this.type = type;
        this.runtime = runtime;
        setUsedDays();
    }

    public static String[] getWeekdays() {
        return WEEKDAYS_ARRAY;
    }

    public static String[] getMonthdays() {
        return MONTHDAYS_ARRAY;
    }

    public static String[] getMonth() {
        return MONTH;
    }

    public static String[] getUltimos() {
        return ULTIMOS_ARRAY;
    }

    public String[] getUnusedDays() {
        if (usedDays == null) {
            setUsedDays();
        }
        ArrayList<String> unused = new ArrayList<String>();
        for (int i = 0; i < DAYS[type].length; i++) {
            boolean found = false;
            for (int j = 0; j < usedDays.length; j++) {
                if (DAYS[type][i].equalsIgnoreCase(usedDays[j])) {
                    found = true;
                }
            }
            if (!found) {
                unused.add(DAYS[type][i]);
            }
        }
        return unused.toArray(new String[0]);
    }

    public String[] getUsedDays() {
        if (usedDays == null) {
            setUsedDays();
        }
        return usedDays;
    }

    public String[] getUsedDaysInString() {
        String[] used = getUsedDays();
        String a = "";
        for (int i = 0; i < used.length; i++) {
            a = "";
            String[] groupUsedDay = used[i].split(" ");
            if (groupUsedDay.length == 1) {
                a = groupUsedDay[0];
            } else {
                for (int j = 0; j < groupUsedDay.length; j++) {
                    try {
                        a = (a.length() == 0 ? a : a + " ") + getAllDays()[Integer.parseInt(groupUsedDay[j]) - OFFSET[type]];
                    } catch (Exception e) {
                        a = (a.length() == 0 ? a : a + " ") + groupUsedDay[j];
                    }
                }
            }
            used[i] = a;
        }
        return used;
    }

    public String[] getUsedUltimosDaysInString() {
        String[] used = getUsedDays();
        if (used.length > 0) {
            for (int i = 0; dayElements != null && i < dayElements.length; i++) {
                Element e = dayElements[i];
                String str = "";
                String[] group = Utils.getAttributeValue("day", e).split(" ");
                if (group.length == 1) {
                    str = DAYS[type][Integer.parseInt(group[0]) - OFFSET[type]];
                } else {
                    for (int j = 0; j < group.length; j++) {
                        str = (str.isEmpty() ? str : str + " ") + DAYS[type][Integer.parseInt(group[j]) - OFFSET[type]];
                    }
                }
                used[i] = str;
            }
        }
        return used;
    }

    public Element[] getDayElements() {
        return dayElements;
    }

    public String[] getUsedMonth() {
        String[] retVal = new String[0];
        if (type == SPECIFIC_MONTHS) {
            List l = runtime.getChildren("month");
            retVal = new String[l.size()];
            for (int i = 0; i < l.size(); i++) {
                Element e = (Element) l.get(i);
                retVal[i] = Utils.getAttributeValue("month", e);
            }
        }
        return retVal;
    }

    public String[] getUnUsedMonth() {
        String[] usedMonth = getUsedMonth();
        ArrayList unused = new ArrayList();
        if (type == SPECIFIC_MONTHS) {
            for (int i = 0; i < DAYS[type].length; i++) {
                boolean found = false;
                for (int j = 0; j < usedMonth.length; j++) {
                    if (DAYS[type][i].equalsIgnoreCase(usedMonth[j])) {
                        found = true;
                    }
                }
                if (!found) {
                    unused.add(DAYS[type][i]);
                }
            }
        }
        return (String[]) unused.toArray(new String[0]);
    }

    private void setUsedDays() {
        if (runtime.getChild("holidays") != null || isWeekdaysHoliday) {
            isHolidayWeeksdayParent();
        }
        if (runtime != null && runtime.getChild(ELEMENT_NAME[type]) != null) {
            Element daylist = runtime.getChild(ELEMENT_NAME[type]);
            List list = daylist.getChildren("day");
            int size = list.size();
            String[] days = new String[size];
            Element[] elements = new Element[size];
            Iterator it = list.iterator();
            int i = 0;
            while (it.hasNext()) {
                Element e = (Element) it.next();
                try {
                    if (Utils.getAttributeValue("day", e).indexOf(" ") == -1) {
                        int day = 0;
                        if (!Utils.isNumeric(e.getAttributeValue("day"))) {
                            day = getDayNumber(e.getAttributeValue("day"));
                            e.setAttribute("day", String.valueOf(day));
                        } else {
                            day = new Integer(e.getAttributeValue("day")).intValue();
                        }
                        if (type == WEEKDAYS && day == 0) {
                            day = 7;
                            e.setAttribute("day", String.valueOf(day));
                        }
                        days[i] = DAYS[type][day - OFFSET[type]];
                        elements[i++] = e;
                    } else {
                        String[] split = Utils.getAttributeValue("day", e).split(" ");
                        String attr = "";
                        for (int j = 0; j < split.length; j++) {
                            int day = 0;
                            if (!Utils.isNumeric(split[j])) {
                                day = getDayNumber(split[j]);
                            } else {
                                day = new Integer(split[j]).intValue();
                            }
                            if (type == WEEKDAYS && day == 0) {
                                day = 7;
                            }
                            attr = (attr == null || attr.isEmpty() ? "" : attr + " ") + day;
                        }
                        e.setAttribute("day", attr);
                        elements[i++] = e;
                    }
                } catch (Exception ex) {
                    new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ;Invalid day element in " + ELEMENT_NAME[type], ex);
                }
            }
            usedDays = sort(days, elements);
        } else {
            usedDays = new String[0];
        }
    }

    private String[] sort(String[] daylist, Element[] elements) {
        int size = elements.length;
        String[] sorted = new String[size];
        try {
            dayElements = new Element[size];
            int index = 0;
            for (int i = 0; i < DAYS[type].length; i++) {
                for (int j = 0; j < daylist.length; j++) {
                    if (DAYS[type][i].equalsIgnoreCase(daylist[j])) {
                        sorted[index] = DAYS[type][i];
                        dayElements[index++] = elements[j];
                    }
                }
            }
            for (int j = 0; j < elements.length; j++) {
                Element e = elements[j];
                if (Utils.getAttributeValue("day", e).indexOf(" ") > -1) {
                    sorted[index] = e.getAttributeValue("day");
                    dayElements[index++] = elements[j];
                }
            }
            return sorted;
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
            return sorted;
        }
    }

    private int getDayNumber(String day) {
        for (int i = 0; i < DAYS[type].length; i++) {
            if (DAYS[type][i].equalsIgnoreCase(day.toLowerCase())) {
                return i + OFFSET[type];
            }
            if (type == 0) {
                if (WEEKDAYS_GER[i].equalsIgnoreCase(day.toLowerCase())) {
                    return i + OFFSET[type];
                }
                if (WEEKDAYS_GER_SHORT[i].equalsIgnoreCase(day.toLowerCase())) {
                    return i + OFFSET[type];
                }
                if (WEEKDAYS_EN_SHORT[i].equalsIgnoreCase(day.toLowerCase())) {
                    return i + OFFSET[type];
                }
            }

        }
        return -1;
    }

    private String getDayGroupNumbers(String day) {
        String retVal = "";
        String[] group = null;
        if (type == ULTIMOS) {
            group = getNormalizedUltimos(day);
        } else {
            group = day.split(" ");
        }
        for (int j = 0; j < group.length; j++) {
            for (int i = 0; i < DAYS[type].length; i++) {
                if (DAYS[type][i].equalsIgnoreCase(group[j])) {
                    retVal = (retVal.length() == 0 ? retVal : retVal + " ") + (i + OFFSET[type]);
                }
            }
        }
        return retVal;
    }

    public void addDay(String day) {
        isHolidayWeeksdayParent();
        Element daylist = runtime.getChild(ELEMENT_NAME[type]);
        if (daylist == null && type != SPECIFIC_MONTHS) {
            daylist = new Element(ELEMENT_NAME[type]);
            runtime.addContent(daylist);
        }
        if (type == SPECIFIC_MONTHS) {
            List l = runtime.getChildren("month");
            boolean found = false;
            for (int i = 0; i < l.size(); i++) {
                Element e = (Element) l.get(i);
                if (Utils.getAttributeValue("month", e).equals(day)) {
                    found = true;
                }
            }
            if (!found) {
                daylist = new Element(ELEMENT_NAME[type]);
                runtime.addContent(daylist);
                Utils.setAttribute("month", day, daylist);
            }
        } else {
            daylist.addContent(new Element("day").setAttribute("day", "" + getDayNumber(day)));
        }
        dom.setChanged(true);
        if (runtime != null && runtime.getParentElement() != null) {
            dom.setChangedForDirectory(runtime, SchedulerDom.MODIFY);
        }
        setUsedDays();
    }

    public void addGroup(String group) {
        String[] split = null;
        isHolidayWeeksdayParent();
        Element daylist = runtime.getChild(ELEMENT_NAME[type]);
        if (daylist == null && type != SPECIFIC_MONTHS) {
            daylist = new Element(ELEMENT_NAME[type]);
            runtime.addContent(daylist);
        }
        if (type == SPECIFIC_MONTHS) {
            List l = runtime.getChildren("month");
            boolean found = false;
            for (int i = 0; i < l.size(); i++) {
                Element e = (Element) l.get(i);
                if (Utils.getAttributeValue("month", e).equals(group)) {
                    found = true;
                }
            }
            if (!found) {
                daylist = new Element(ELEMENT_NAME[type]);
                runtime.addContent(daylist);
                Utils.setAttribute("month", group, daylist);
            }
        } else {
            if (type == ULTIMOS) {
                split = getNormalizedUltimos(group);
            } else {
                split = group.split(" ");
            }
            String attr = "";
            for (int i = 0; i < split.length; i++) {
                attr = (attr.length() == 0 ? "" : attr + " ") + getDayNumber(split[i]);
            }
            daylist.addContent(new Element("day").setAttribute("day", attr));
        }
        dom.setChangedForDirectory(runtime, SchedulerDom.MODIFY);
        setUsedDays();
    }

    public void updateGroup(String newGroup, String oldGroup) {
        String[] split = null;
        Element daylist = runtime.getChild(ELEMENT_NAME[type]);
        if (daylist == null && type != SPECIFIC_MONTHS) {
            daylist = new Element(ELEMENT_NAME[type]);
            runtime.addContent(daylist);
        }
        if (type == SPECIFIC_MONTHS) {
            List l = runtime.getChildren("month");
            boolean found = false;
            for (int i = 0; i < l.size(); i++) {
                Element e = (Element) l.get(i);
                if (Utils.getAttributeValue("month", e).equals(oldGroup)) {
                    found = true;
                    e.setAttribute("month", newGroup);
                }
            }
            if (!found) {
                daylist = new Element(ELEMENT_NAME[type]);
                runtime.addContent(daylist);
                Utils.setAttribute("month", newGroup, daylist);
            }
        } else {
            boolean found = false;
            String[] used = getUsedDays();
            if (used.length > 0) {
                for (int i = 0; dayElements != null && i < dayElements.length; i++) {
                    Element e = dayElements[i];
                    String str = "";
                    String[] group = Utils.getAttributeValue("day", e).split(" ");
                    if (group.length == 1) {
                        str = DAYS[type][Integer.parseInt(group[0]) - OFFSET[type]];
                    } else {
                        for (int j = 0; j < group.length; j++) {
                            str = (str.length() == 0 ? str : str + " ") + DAYS[type][Integer.parseInt(group[j]) - OFFSET[type]];
                        }
                    }
                    if (str.equals(oldGroup)) {
                        e.setAttribute("day", getDayGroupNumbers(newGroup));
                        found = true;
                    }
                }
                if (!found) {
                    split = getNormalizedUltimos(newGroup);
                    String attr = "";
                    for (int i = 0; i < split.length; i++) {
                        attr = (attr.length() == 0 ? "" : attr + " ") + getDayNumber(split[i]);
                    }
                    daylist.addContent(new Element("day").setAttribute("day", attr));
                }
            }
        }
        dom.setChangedForDirectory(runtime, SchedulerDom.MODIFY);
        setUsedDays();
    }

    public void deleteMonth(String month) {
        Element daylist = runtime.getChild(ELEMENT_NAME[type]);
        if (daylist != null) {
            List list = runtime.getChildren(ELEMENT_NAME[type]);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("month") != null && e.getAttributeValue("month").equals(month)) {
                    e.detach();
                    if (list.isEmpty()) {
                        runtime.removeChild(ELEMENT_NAME[type]);
                    }
                    dom.setChanged(true);
                    if (runtime != null && runtime.getParentElement() != null) {
                        dom.setChangedForDirectory(runtime, SchedulerDom.MODIFY);
                    }
                    setUsedDays();
                    break;
                }
            }
        }
    }

    public void deleteDay(String day) {
        if (type == SPECIFIC_MONTHS) {
            deleteMonth(day);
            return;
        }
        Element daylist = runtime.getChild(ELEMENT_NAME[type]);
        if (daylist != null) {
            List list = daylist.getChildren("day");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if ("day".equals(e.getName())
                        && (e.getAttributeValue("day") != null && (e.getAttributeValue("day").equals("" + getDayNumber(day))
                                || e.getAttributeValue("day").equals(day) || e.getAttributeValue("day").equals(getDayGroupNumbers(day))))) {
                    e.detach();
                    boolean isEmpty = true;
                    List _list = runtime.getChildren(ELEMENT_NAME[type]);
                    for (int i = 0; i < _list.size(); i++) {
                        Element s = (Element) _list.get(i);
                        if (!s.getChildren().isEmpty()) {
                            isEmpty = false;
                            break;
                        }
                    }
                    if (list.isEmpty() && isEmpty) {
                        runtime.removeChild(ELEMENT_NAME[type]);
                    }
                    dom.setChanged(true);
                    if (runtime != null && runtime.getParentElement() != null) {
                        dom.setChangedForDirectory(runtime, SchedulerDom.MODIFY);
                    }
                    setUsedDays();
                    break;
                }
            }
        }
    }

    public void updateDay(String newDay, String oldDay) {
        Element daylist = runtime.getChild(ELEMENT_NAME[type]);
        if (daylist != null) {
            List list = daylist.getChildren("day");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("day") != null
                        && (e.getAttributeValue("day").equals("" + getDayNumber(oldDay)) || e.getAttributeValue("day").equals(oldDay) || e.getAttributeValue(
                                "day").equals(getDayGroupNumbers(oldDay)))) {
                    e.setAttribute("day", getDayGroupNumbers(newDay));
                    dom.setChanged(true);
                    if (runtime != null && runtime.getParentElement() != null) {
                        dom.setChangedForDirectory(runtime, SchedulerDom.MODIFY);
                    }
                    setUsedDays();
                    break;
                }
            }
        }
    }

    public void fillTreeDays(TreeItem parent, boolean expand) {
        try {
            String[] used = null;
            parent.removeAll();
            if (usedDays == null) {
                setUsedDays();
            }
            used = getUsedDays();
            for (int i = 0; used != null && i < used.length; i++) {
                TreeItem item = new TreeItem(parent, SWT.NONE);
                if ("1 2 3 4 5 6 7".equals(used[i])) {
                    item.setText("Every Day");
                } else {
                    item.setText(used[i]);
                }
                item.setData("max_occur", "1");
                item.setData("key", used[i] + "_@_" + dayElements[i].getName());
                item.setData("copy_element", dayElements[i]);
                item.setData(new TreeData(JOEConstants.PERIODS, dayElements[i], Options.getHelpURL("periods")));
                if (runtime != null && !Utils.isElementEnabled("job", dom, runtime)) {
                    item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                }
            }
            parent.setExpanded(expand);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open file on Webdav Server", e);
        }
    }

    public String[] getAllDays() {
        return DAYS[type];
    }

    public static java.util.HashMap getDays() {
        java.util.HashMap l = new java.util.HashMap();
        l.put("weekdays", WEEKDAYS_ARRAY);
        l.put("monthdays", MONTHDAYS_ARRAY);
        l.put("ultimos", ULTIMOS_ARRAY);
        l.put("month", MONTH);
        return l;
    }

    public String[] getNormalizedUltimos(String group) {
        String[] allUltimos = getUltimos();
        ArrayList l = new ArrayList();
        for (int i = 0; i < allUltimos.length; i++) {
            if (group.startsWith(allUltimos[i]) || group.indexOf(" " + allUltimos[i].concat(" ")) > -1 || group.endsWith(" ".concat(allUltimos[i]))) {
                l.add(allUltimos[i]);
            }
        }
        String[] split = new String[l.size()];
        for (int i = 0; i < l.size(); i++) {
            split[i] = l.get(i).toString();
        }
        return split;
    }

    private void isHolidayWeeksdayParent() {
        if ("holidays".equals(runtime.getName()) || MainWindow.getContainer().getCurrentEditor() instanceof DocumentationForm) {
            return;
        }
        SchedulerForm f = (SchedulerForm) (sos.scheduler.editor.app.MainWindow.getContainer().getCurrentEditor());
        if (f == null) {
            return;
        }
        Tree tree = f.getTree();
        if (tree != null && tree.getSelectionCount() > 0) {
            TreeItem item = f.getTree().getSelection()[0];
            if (item.getParentItem() != null
                    && ("Holidays".equalsIgnoreCase(item.getParentItem().getText()) || (item.getParentItem().getData("key") != null && "holidays".equals(item.getParentItem().getData(
                            "key"))))) {
                if (runtime.getChild("holidays") != null) {
                    runtime = runtime.getChild("holidays");
                } else {
                    runtime.addContent(new Element("holidays"));
                    runtime = runtime.getChild("holidays");
                }
            }
        }
    }

}