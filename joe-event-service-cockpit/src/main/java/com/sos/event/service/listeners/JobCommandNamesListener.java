package com.sos.event.service.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import com.sos.event.service.forms.ActionsForm;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.Events.ActionsDom;

public class JobCommandNamesListener {

    private ActionsForm _main = null;
    private ActionsDom _dom = null;
    private Element _command = null;

    public JobCommandNamesListener(ActionsDom dom, Element command, ActionsForm update) {
        _dom = dom;
        _command = command;
        _main = update;
    }

    public void fillCommands(Element job, TreeItem parent, boolean expand) {
        List commands = job.getChildren("commands");
        if (commands != null) {
            Iterator it = commands.iterator();
            parent.removeAll();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("on_exit_code") != null) {
                    TreeItem item = new TreeItem(parent, SWT.NONE);
                    item.setText(e.getAttributeValue("name"));
                    item.setData(new TreeData(JOEConstants.JOB_COMMAND, e, Options.getHelpURL("job.commands")));
                }
            }
        }
        parent.setExpanded(expand);
    }

    public void addCommand(Element e) {
        _dom.setChanged(true);
        _command.addContent(e);
        _main.updateCommand();
    }

    private int getActCommand(Table table) {
        int index = table.getSelectionIndex();
        int j = index;
        int ignore = 0;
        List c = _command.getChildren();
        Iterator it2 = c.iterator();
        while (it2.hasNext() && j >= 0) {
            Element e2 = (Element) it2.next();
            if (!"start_job".equals(e2.getName()) && !"add_order".equals(e2.getName()) && !"order".equals(e2.getName())) {
                ignore++;
            } else {
                j--;
            }
        }
        return index + ignore;
    }

    public void deleteCommand(Table table) {
        int j = 0;
        int index = table.getSelectionIndex();
        j = getActCommand(table);
        table.remove(index);
        List c = _command.getChildren();
        if (_command != null) {
            c.remove(j);
        }
        _main.updateCommand();
        _dom.setChanged(true);
    }

    public String getCommandAttribute(Table table, String attribute) {
        int i = getActCommand(table);
        List l = _command.getChildren();
        Element e = (Element) l.get(i);
        return Utils.getAttributeValue(attribute, e);
    }

    public void setName(String value) {
        Utils.setAttribute("name", value, _command, _dom);
        _main.updateTreeItem(value);
    }

    public String getName() {
        return Utils.getAttributeValue("name", _command);
    }

    public void setHost(String value) {
        Utils.setAttribute("scheduler_host", value, _command, _dom);
    }

    public String getHost() {
        return Utils.getAttributeValue("scheduler_host", _command);
    }

    public void setPort(String value) {
        Utils.setAttribute("scheduler_port", value, _command, _dom);
    }

    public String getPort() {
        return Utils.getAttributeValue("scheduler_port", _command);
    }

    public Element getCommand() {
        return _command;
    }

    public void fillCommands(Table table) {
        boolean created;
        TableItem item = null;
        table.removeAll();
        List c = _command.getChildren();
        Iterator it2 = c.iterator();
        while (it2.hasNext()) {
            Element e2 = (Element) it2.next();
            created = false;
            if ("start_job".equals(e2.getName()) || "add_order".equals(e2.getName()) || "order".equals(e2.getName())) {
                if (!created) {
                    item = new TableItem(table, SWT.NONE);
                    item.setText(1, "");
                    created = true;
                }
                item.setText(0, e2.getName());
                item.setText(3, Utils.getAttributeValue("at", e2));
                if ("start_job".equals(e2.getName())) {
                    item.setText(1, Utils.getAttributeValue("job", e2));
                } else if ("add_order".equals(e2.getName()) || "order".equals(e2.getName())) {
                    item.setText(1, Utils.getAttributeValue("id", e2));
                    item.setText(2, Utils.getAttributeValue("job_chain", e2));
                }
            }
        }
    }

}