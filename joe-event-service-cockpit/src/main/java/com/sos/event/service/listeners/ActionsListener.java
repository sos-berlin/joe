package com.sos.event.service.listeners;

import java.util.Iterator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;
import sos.scheduler.editor.actions.IUpdateTree;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.forms.ActionsForm;
import sos.scheduler.editor.actions.forms.EventsForm;
import sos.scheduler.editor.actions.forms.EventForm;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.actions.forms.JobCommandsForm;
import sos.scheduler.editor.actions.forms.JobCommandForm;



import java.util.List;

public class ActionsListener implements IUpdateTree {
	
	
    private ActionsDom _dom;

    private ActionsForm _gui;
    
    public static String ACTION_PREFIX =  "Action: ";
    
    public static String GROUP_PREFIX  = "Group: ";
    
    public static String COMMAND_PREFIX= "Command: ";
    
    

    public ActionsListener(ActionsForm gui, ActionsDom dom) {
    	_gui = gui;
        _dom = dom;
    }


    public void fillTree(Tree tree) {
        tree.removeAll();

        Element desc = _dom.getRoot();
        sos.scheduler.editor.app.Utils.setResetElement(_dom.getRoot());
        TreeItem item = new TreeItem(tree, SWT.NONE);
        item.setText("Actions");
        item.setData(new TreeData(Editor.ACTIONS, desc, Options.getDocHelpURL("actions")));

        treeFillAction(item);
      

    }



    public boolean treeSelection(Tree tree, Composite c) {
        try {
        	//System.out.println("--> " + tree.getSelectionCount());
            if (tree.getSelectionCount() > 0) {

                // dispose the old form
                Control[] children = c.getChildren();
                for (int i = 0; i < children.length; i++) {
                    if (!Utils.applyFormChanges(children[i]))
                        return false;
                    children[i].dispose();
                }

                TreeItem item = tree.getSelection()[0];
                TreeData data = (TreeData) item.getData();
                
                if(data == null)
                	return false;
                
                //System.out.println("test: "  + item.getText());
                
                
                _dom.setInit(true);

                switch (data.getType()) {
                    case Editor.ACTIONS:
                        new sos.scheduler.editor.actions.forms.ActionsListForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                        break;
                    case Editor.ACTION:                    	 
                        new sos.scheduler.editor.actions.forms.ActionForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                        break;
                   
                    case Editor.EVENTS:
                        new EventsForm(c, SWT.NONE, _dom, data.getElement(), _gui);
                        break;
                    case Editor.EVENT_GROUP:
                    	new EventForm(c, SWT.NONE, _dom, data.getElement(), Editor.EVENT_GROUP);
                    	
                        break;
                    case Editor.ADD_EVENT_GROUP:
                       new EventForm(c, SWT.NONE, _dom, data.getElement(), Editor.ADD_EVENT_GROUP);
                    	
                        break;   
                    case Editor.REMOVE_EVENT_GROUP:
                        new EventForm(c, SWT.NONE, _dom, data.getElement(), Editor.REMOVE_EVENT_GROUP);
                    	
                        break;   
                    case Editor.ACTION_COMMANDS:
						//new JobCommandsForm(c, SWT.NONE, _dom, data.getElement(), _gui, this);
                    	new JobCommandsForm(c, SWT.NONE, _dom, data.getElement(), _gui);						  
                        break;
                    case Editor.JOB_COMMAND_EXIT_CODES:
						new sos.scheduler.editor.actions.forms.JobCommandNamesForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
                    case Editor.JOB_COMMAND:
						new JobCommandForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
                    case Editor.PARAMETER:					
						//int type = getType(data.getElement());
						new sos.scheduler.editor.actions.forms.ParameterForm(c, SWT.NONE, _dom, data.getElement(), _gui, Editor.JOB_COMMANDS);						
						break;
						
                    default:
                        System.out.println("no form found for " + item.getText());
                }

                c.layout();

            }
        } catch (Exception e) {
            e.printStackTrace();
            MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
        }
        _dom.setInit(false);
        return true;
    }


    
    public void treeFillAction(TreeItem parent) {
    	Element actions = _dom.getRoot();
    	parent.removeAll();
    	List list = actions.getChildren("action", actions.getNamespace());


    	for(int i = 0; i < list.size(); i++) {
    		Element action = (Element)list.get(i);
    		TreeItem item = new TreeItem(parent, SWT.NONE);
    		item.setText(ACTION_PREFIX + Utils.getAttributeValue("name", action));
    		item.setData(new TreeData(Editor.ACTION, action, Options.getDocHelpURL("action")));

    		fillEvents(item, action);
    		fillCommands(item, action);
    		
    		item.setExpanded(true);
    	}
    	
		parent.setExpanded(true);
    }
    
    public void fillCommands(TreeItem parent, Element action) {
    	TreeItem item = new TreeItem(parent, SWT.NONE);
        item.setText("Commands");
        item.setData(new TreeData(Editor.ACTION_COMMANDS, action, Options.getDocHelpURL("commands")));
        treeFillCommands(item, action, true);
    }
    
    public void fillEvents(TreeItem parent,Element action) {
    	parent.removeAll();
    	
    	TreeItem item = new TreeItem(parent, SWT.NONE);
    	item.setText("Events");
    	item.setData(new TreeData(Editor.EVENTS, action, Options.getDocHelpURL("events")));
    	fillEventGroup(item, action);
    	
    }
    
    public void fillEventGroup(TreeItem parent,Element action) {
    	parent.removeAll();
    	
    	Element events = action.getChild("events");
    	if(events != null) {
    		List l = events.getChildren("event_group");
    		for(int i = 0; i < l.size(); i++) {
    			Element eventGroup = (Element)l.get(i);
    			TreeItem item2 = new TreeItem(parent, SWT.NONE);
    			item2.setText(GROUP_PREFIX + Utils.getAttributeValue("group", eventGroup));
    			item2.setData(new TreeData(Editor.EVENT_GROUP, eventGroup, Options.getDocHelpURL("event_group")));
    			//item.setExpanded(true);	
    		}
    	}
    }
   
    
    
    public void treeFillCommands(TreeItem parent, Element action, boolean expand) {
    
    	Element eCommands = action.getChild("commands");
    	TreeItem item = null;
    	parent.removeAll();
    	if(eCommands != null) {
    		List commands = eCommands.getChildren("command");    		
    		if (commands != null) {
    			Iterator it = commands.iterator();
    			

    			while (it.hasNext()) {
    				Element e = (Element) it.next();
    				if (e.getAttributeValue("name") != null) {
    					item = new TreeItem(parent, SWT.NONE);
    					item.setText(COMMAND_PREFIX + e.getAttributeValue("name"));
    					item.setData(new TreeData(Editor.JOB_COMMAND_EXIT_CODES, e, Options.getHelpURL("job.commands")));
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
    	item1.setData(new TreeData(Editor.ADD_EVENT_GROUP, eCommands, Options.getHelpURL("job.commands")));
    	item1.setData("key", "commands_@_add_event");	
    	item1.setData("copy_element", eCommands);
    	


    	TreeItem item2 = new TreeItem(parent, SWT.NONE);
    	item2.setText("remove event");
    	item2.setData(new TreeData(Editor.REMOVE_EVENT_GROUP, eCommands, Options.getHelpURL("job.commands")));
    	item2.setData("key", "commands_@_remove_event");	
    	item2.setData("copy_element", eCommands);
    }
    
    public void treeFillCommand(TreeItem parent, Element elem, boolean expand) {
    	
		parent.removeAll();
		treeFillCommand(parent, elem.getChildren("order"));
		treeFillCommand(parent, elem.getChildren("add_order"));
		treeFillCommand(parent, elem.getChildren("start_job"));

	}

	private void treeFillCommand(TreeItem parent,List cmdList) {		
		for(int i =0; i < cmdList.size(); i++) {
			Element cmdElem = (Element)cmdList.get(i);   
			TreeItem item = new TreeItem(parent, SWT.NONE);
			String name = Utils.getAttributeValue("job_chain", cmdElem) != null && Utils.getAttributeValue("job_chain",cmdElem).length() > 0? Utils.getAttributeValue("job_chain", cmdElem) : Utils.getAttributeValue("job", cmdElem);
			item.setText(cmdElem.getName()+ ": " + name);
			item.setData(new TreeData(Editor.JOB_COMMAND, cmdElem, Options.getHelpURL("job.commands")));
			item.setExpanded(false);
			
			//PARAMETER
			item = new TreeItem(item, SWT.NONE);
			item.setData(new TreeData(Editor.PARAMETER, cmdElem, Options.getHelpURL("parameter")));
			item.setData("key", "params_@_param");
			item.setData("copy_element", cmdElem);
			item.setText("Parameter");
		}
	}
}
