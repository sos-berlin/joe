package sos.scheduler.editor.conf.listeners;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ScheduleListener {

    private SchedulerDom _dom = null;
    private ISchedulerUpdate _main = null;
    private Element _schedule = null;

    public ScheduleListener(SchedulerDom dom, ISchedulerUpdate update, Element schedule) {
        _dom = dom;
        _main = update;
        _schedule = schedule;
    }

    public String getName() {
        return Utils.getAttributeValue("name", _schedule);
    }

    public void setName(String _name) {
        _dom.setChanged(true);
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
        _dom.setChanged(true);
        _dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);
    }

    public String getTitle() {
        return Utils.getAttributeValue("title", _schedule);
    }

    public void setValidFrom(String validFrom) {
        Utils.setAttribute("valid_from", validFrom, _schedule);
        _dom.setChanged(true);
        _dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);
    }

    public String getValidFrom() throws ParseException {
        return Utils.getAttributeValue("valid_from", _schedule);
    }

    private void clearValid() {
        Utils.setAttribute("valid_from", null, _schedule);
        Utils.setAttribute("valid_to", null, _schedule);
        _dom.setChanged(true);
        _dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);
    }

    public void setValidTo(String validTo) throws ParseException {
        Utils.setAttribute("valid_to", validTo, _schedule);
        _dom.setChanged(true);
        _dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);
    }

    public String getValidTo() throws ParseException {
        return Utils.getAttributeValue("valid_to", _schedule);
    }

    public void setSubstitut(String substitute) {
        if ("".equals(substitute.trim())) {
            clearValid();
        }
        Utils.setAttribute("substitute", substitute, _schedule);
        _dom.setChanged(true);
        _dom.setChangedForDirectory("schedule", Utils.getAttributeValue("name", _schedule), SchedulerDom.MODIFY);
    }

    public String getSubstitute() {
        return Utils.getAttributeValue("substitute", _schedule);
    }

    public String[] getAllSchedules() {
        List s = null;
        if (_dom.isLifeElement() || _schedule == null) {
            return null;
        }
        String currSchedulename = Utils.getAttributeValue("name", _schedule);
        Element schedules = null;
        if (_schedule.getParentElement() != null) {
            schedules = (Element) _schedule.getParentElement().clone();
        } else {
            return null;
        }
        s = schedules.getChildren("schedule");
        ArrayList list = new java.util.ArrayList();
        list.add("");
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i) instanceof Element) {
                Element e = (Element) s.get(i);
                if (!Utils.getAttributeValue("name", e).equals(currSchedulename)) {
                    list.add(Utils.getAttributeValue("name", e));
                }
            }
        }
        String[] str = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            str[i] = (String) list.get(i);
        }
        return str;
    }

}