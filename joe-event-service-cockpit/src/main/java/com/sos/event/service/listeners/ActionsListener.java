package com.sos.event.service.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.event.service.actions.IUpdateTree;
import com.sos.event.service.forms.ActionForm;
import com.sos.event.service.forms.ActionsForm;
import com.sos.event.service.forms.ActionsListForm;
import com.sos.event.service.forms.EventForm;
import com.sos.event.service.forms.EventsForm;
import com.sos.event.service.forms.JobCommandForm;
import com.sos.event.service.forms.JobCommandNamesForm;
import com.sos.event.service.forms.JobCommandsForm;
import com.sos.event.service.forms.ParameterForm;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.Events.ActionsDom;

public class ActionsListener implements IUpdateTree {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionsListener.class);
    private ActionsDom _dom;
    private ActionsForm _gui;
    public static String ACTION_PREFIX = "Action: ";
    public static String GROUP_PREFIX = "Group: ";
    public static String COMMAND_PREFIX = "Command: ";

    public ActionsListener(ActionsForm gui, ActionsDom dom) {
        _gui = gui;
        _dom = dom;
    }

    public void fillTree(Tree tree) {
        tree.removeAll();
        Element desc = _dom.getRoot();
        Utils.setResetElement(_dom.getRoot());
        TreeItem item = new TreeItem(tree, SWT.NONE);
        item.setText("Actions");
        item.setData(new TreeData(JOEConstants.ACTIONS, desc, Options.getDocHelpURL("actions")));
        treeFillAction(item);
    }

    public boolean treeSelection(Tree tree, Composite c) {
        try {
            if (tree.getSelectionCount() > 0) {
                Control[] children = c.getChildren();
                for (int i = 0; i < children.length; i++) {
                    if (!Utils.applyFormChanges(children[i])) {
                        return false;
                    }
                    children[i].dispose();
                }
                TreeItem item = tree.getSelection()[0];
                TreeData data = (TreeData) item.getData();
                if (data == null) {
                    return false;
                }
                _dom.setInit(true);
                switch (data.getType()) {
                case JOEConstants.ACTIONS:
                    new ActionsListForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                    break;
                case JOEConstants.ACTION:
                    new ActionForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                    break;
                case JOEConstants.EVENTS:
                    new EventsForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                    break;
                case JOEConstants.EVENT_GROUP:
                    new EventForm(c, SWT.NONE, _dom, data.getElement(), JOEConstants.EVENT_GROUP);
                    break;
                case JOEConstants.ADD_EVENT_GROUP:
                    new EventForm(c, SWT.NONE, _dom, data.getElement(), JOEConstants.ADD_EVENT_GROUP);
                    break;
                case JOEConstants.REMOVE_EVENT_GROUP:
                    new EventForm(c, SWT.NONE, _dom, data.getElement(), JOEConstants.REMOVE_EVENT_GROUP);
                    break;
                case JOEConstants.ACTION_COMMANDS:
                    new JobCommandsForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                    break;
                case JOEConstants.JOB_COMMAND_EXIT_CODES:
                    new JobCommandNamesForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                    break;
                case JOEConstants.JOB_COMMAND:
                    new JobCommandForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                    break;
                case JOEConstants.PARAMETER:
                    new ParameterForm(c, SWT.NONE, _dom, data.getElement(), _gui, JOEConstants.JOB_COMMANDS);
                    break;
                default:
                    LOGGER.info("no form found for " + item.getText());
                }
                c.layout();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ErrorLog.message(e.getMessage(), SWT.ICON_ERROR);
        }
        _dom.setInit(false);
        return true;
    }

    public void treeFillAction(TreeItem parent) {
        Element actions = _dom.getRoot();
        parent.removeAll();
        List list = actions.getChildren("action", actions.getNamespace());
        for (int i = 0; i < list.size(); i++) {
            Element action = (Element) list.get(i);
            TreeItem item = new TreeItem(parent, SWT.NONE);
            item.setText(ACTION_PREFIX + Utils.getAttributeValue("name", action));
            item.setData(new TreeData(JOEConstants.ACTION, action, Options.getDocHelpURL("action")));
            fillEvents(item, action);
            fillCommands(item, action);
            item.setExpanded(true);
        }
        parent.setExpanded(true);
    }

    public void fillCommands(TreeItem parent, Element action) {
        TreeItem item = new TreeItem(parent, SWT.NONE);
        item.setText("Commands");
        item.setData(new TreeData(JOEConstants.ACTION_COMMANDS, action, Options.getDocHelpURL("commands")));
        treeFillCommands(item, action, true);
    }

    public void fillEvents(TreeItem parent, Element action) {
        parent.removeAll();
        TreeItem item = new TreeItem(parent, SWT.NONE);
        item.setText("Events");
        item.setData(new TreeData(JOEConstants.EVENTS, action, Options.getDocHelpURL("events")));
        fillEventGroup(item, action);
    }

    public void fillEventGroup(TreeItem parent, Element action) {
        parent.removeAll();
        Element events = action.getChild("events");
        if (events != null) {
            List l = events.getChildren("event_group");
            for (int i = 0; i < l.size(); i++) {
                Element eventGroup = (Element) l.get(i);
                TreeItem item2 = new TreeItem(parent, SWT.NONE);
                item2.setText(GROUP_PREFIX + Utils.getAttributeValue("group", eventGroup));
                item2.setData(new TreeData(JOEConstants.EVENT_GROUP, eventGroup, Options.getDocHelpURL("event_group")));
            }
        }
    }

    public void treeFillCommands(TreeItem parent, Element action, boolean expand) {
        Element eCommands = action.getChild("commands");
        TreeItem item = null;
        parent.removeAll();
        if (eCommands != null) {
            List commands = eCommands.getChildren("command");
            if (commands != null) {
                Iterator it = commands.iterator();
                while (it.hasNext()) {
                    Element e = (Element) it.next();
                    if (e.getAttributeValue("name") != null) {
                        item = new TreeItem(parent, SWT.NONE);
                        item.setText(COMMAND_PREFIX + e.getAttributeValue("name"));
                        item.setData(new TreeData(JOEConstants.JOB_COMMAND_EXIT_CODES, e, Options.getHelpURL("job.commands")));
                        item.setData("key", "commands_@_order");
                        item.setData("copy_element", e);
                        treeFillCommand(item, e, false);
                    }
                }
            }
            treeFillAddRemoveEvent(parent, eCommands);
        }
    }

    public void treeFillAddRemoveEvent(TreeItem parent, Element eCommands) {
        TreeItem item1 = new TreeItem(parent, SWT.NONE);
        item1.setText("add event");
        item1.setData(new TreeData(JOEConstants.ADD_EVENT_GROUP, eCommands, Options.getHelpURL("job.commands")));
        item1.setData("key", "commands_@_add_event");
        item1.setData("copy_element", eCommands);
        TreeItem item2 = new TreeItem(parent, SWT.NONE);
        item2.setText("remove event");
        item2.setData(new TreeData(JOEConstants.REMOVE_EVENT_GROUP, eCommands, Options.getHelpURL("job.commands")));
        item2.setData("key", "commands_@_remove_event");
        item2.setData("copy_element", eCommands);
    }

    public void treeFillCommand(TreeItem parent, Element elem, boolean expand) {
        parent.removeAll();
        treeFillCommand(parent, elem.getChildren("order"));
        treeFillCommand(parent, elem.getChildren("add_order"));
        treeFillCommand(parent, elem.getChildren("start_job"));
    }

    private void treeFillCommand(TreeItem parent, List cmdList) {
        for (int i = 0; i < cmdList.size(); i++) {
            Element cmdElem = (Element) cmdList.get(i);
            TreeItem item = new TreeItem(parent, SWT.NONE);
            String name =
                    Utils.getAttributeValue("job_chain", cmdElem) != null && Utils.getAttributeValue("job_chain", cmdElem).length() > 0
                            ? Utils.getAttributeValue("job_chain", cmdElem) : Utils.getAttributeValue("job", cmdElem);
            item.setText(cmdElem.getName() + ": " + name);
            item.setData(new TreeData(JOEConstants.JOB_COMMAND, cmdElem, Options.getHelpURL("job.commands")));
            item.setExpanded(false);
            // PARAMETER
            item = new TreeItem(item, SWT.NONE);
            item.setData(new TreeData(JOEConstants.PARAMETER, cmdElem, Options.getHelpURL("parameter")));
            item.setData("key", "params_@_param");
            item.setData("copy_element", cmdElem);
            item.setText("Parameter");
        }
    }

}