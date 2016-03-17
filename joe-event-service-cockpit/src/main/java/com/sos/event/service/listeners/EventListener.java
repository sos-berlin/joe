package com.sos.event.service.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.Events.ActionsDom;

public class EventListener {

    private ActionsDom _dom = null;
    private Element _eventGroup = null;
    private int type = -1;

    public EventListener(ActionsDom dom, Element eventGroup, int type_) {
        _dom = dom;
        _eventGroup = eventGroup;
        type = type_;
    }

    public void fillEvent(Table table) {
        table.removeAll();
        List l = null;
        try {
            if (type == JOEConstants.ADD_EVENT_GROUP) {
                XPath x3 = XPath.newInstance("add_event/event");
                l = x3.selectNodes(_eventGroup);
            } else if (type == JOEConstants.REMOVE_EVENT_GROUP) {
                XPath x3 = XPath.newInstance("remove_event/event");
                l = x3.selectNodes(_eventGroup);
            } else {
                l = _eventGroup.getChildren("event");
            }
        } catch (Exception e) {
            ErrorLog.message(e.getMessage(), SWT.ICON_WARNING);
        }
        for (int i = 0; i < l.size(); i++) {
            Element event = (Element) l.get(i);
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, Utils.getAttributeValue("event_name", event));
            item.setText(1, Utils.getAttributeValue("event_id", event));
            item.setText(2, Utils.getAttributeValue("event_title", event));
            item.setText(3, Utils.getAttributeValue("event_class", event));
            item.setText(4, Utils.getAttributeValue("job_name", event));
            item.setText(5, Utils.getAttributeValue("job_chain", event));
            item.setText(6, Utils.getAttributeValue("order_id", event));
            item.setText(7, Utils.getAttributeValue("comment", event));
            item.setText(8, Utils.getAttributeValue("exit_code", event));
            item.setText(9, Utils.getAttributeValue("expiration_period", event));
            item.setText(10, Utils.getAttributeValue("expiration_cycle", event));
            item.setData(event);
        }
    }

    public void apply(String eventName, String eventId, String eventClass, String eventTitle, String jobname, String jobChain, String orderId,
            String comment, String exitCode, String expirationPeriod, String expirationCycle, Table table) {
        Element event = null;
        if (table.getSelectionCount() > 0) {
            event = (Element) table.getSelection()[0].getData();
        } else if (table.getItemCount() > 0 && ((Element) table.getItem(0).getData() != null 
                && ((Element) table.getItem(0).getData()).getAttributes().isEmpty())) {
            event = (Element) table.getItem(0).getData();
        } else {
            event = new Element("event");
            if (type == JOEConstants.ADD_EVENT_GROUP) {
                Element addEvent = new Element("add_event");
                addEvent.addContent(event);
                _eventGroup.addContent(addEvent);
            } else if (type == JOEConstants.REMOVE_EVENT_GROUP) {
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
        Utils.setAttribute("exit_code", exitCode, event);
        Utils.setAttribute("expiration_period", expirationPeriod, event);
        Utils.setAttribute("expiration_cycle", expirationCycle, event);
        fillEvent(table);
        _dom.setChanged(true);
    }

    public void removeEvent(Table table) {
        if (table.getSelectionCount() > 0) {
            TableItem item = table.getSelection()[0];
            Element elem = (Element) item.getData();
            if (type == JOEConstants.REMOVE_EVENT_GROUP || type == JOEConstants.ADD_EVENT_GROUP) {
                if (elem.getParentElement() != null) {
                    elem.getParentElement().detach();
                }
            } else {
                elem.detach();
            }
            table.remove(table.getSelectionIndex());
            fillEvent(table);
            if (type == JOEConstants.REMOVE_EVENT_GROUP || type == JOEConstants.ADD_EVENT_GROUP) {
                if (elem.getParentElement() != null) {
                    elem.getParentElement().detach();
                }
            }
            _dom.setChanged(true);
        }
    }

    public String getEventGroupName() {
        if (_eventGroup != null) {
            return Utils.getAttributeValue("group", _eventGroup);
        } else {
            return "";
        }
    }

    public String getActionName() {
        Element action = _eventGroup;
        boolean loop = true;
        while (loop) {
            if (action == null) {
                loop = false;
            }
            if ("action".equals(action.getName())) {
                loop = false;
            } else {
                action = action.getParentElement();
            }
        }
        if (action != null) {
            return Utils.getAttributeValue("name", action);
        } else {
            return "";
        }
    }

    public String[] getEventClasses() {
        String[] retVal = new String[] {};
        try {
            if (_eventGroup != null) {
                List l = _eventGroup.getChildren("event");
                ArrayList list = new ArrayList();
                for (int i = 0; i < l.size(); i++) {
                    Element event = (Element) l.get(i);
                    String eventClass = Utils.getAttributeValue("event_class", event);
                    if (!eventClass.isEmpty() && !list.contains(eventClass)) {
                        list.add(Utils.getAttributeValue("event_class", event));
                    }
                }
                if (!list.isEmpty()) {
                    retVal = new String[list.size()];
                }
                for (int i = 0; i < list.size(); i++) {
                    retVal[i] = list.get(i).toString();
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return retVal;
    }

}