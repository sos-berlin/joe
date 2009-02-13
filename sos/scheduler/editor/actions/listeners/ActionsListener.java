package sos.scheduler.editor.actions.listeners;

import java.util.Iterator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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

        TreeItem item = new TreeItem(tree, SWT.NONE);
        item.setText("Actions");
        item.setData(new TreeData(Editor.ACTIONS, desc, Options.getDocHelpURL("actions")));

        treeFillAction(item);
        /*TreeItem item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Action");
        item2.setData(new TreeData(Editor.ACTION, desc, Options.getDocHelpURL("action")));
        
        TreeItem item3 = new TreeItem(item2, SWT.NONE);
        item3.setText("Events");
        item3.setData(new TreeData(Editor.EVENTS, desc.getChild("events", _dom.getNamespace()), Options.getDocHelpURL("events")));
        
        item2 = new TreeItem(item3, SWT.NONE);
        item2.setText("Event Group");
        item2.setData(new TreeData(Editor.EVENT_GROUP, desc.getChild("event_group", _dom.getNamespace()), Options.getDocHelpURL("event_group")));
        item.setExpanded(true);
        
        TreeItem item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Commands");
        item2.setData(new TreeData(Editor.ACTION_COMMANDS, desc.getChild("commands", _dom.getNamespace()), Options.getDocHelpURL("commands")));
        */
        /*
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Monitor");
        item2.setData(new TreeData(Editor.DOC_MONITOR, desc.getChild("job", _dom.getNamespace()), Options.getDocHelpURL("monitor")));
        item.setExpanded(true);

        item = new TreeItem(tree, SWT.NONE);
        item.setText("Releases");
        item.setData(new TreeData(Editor.DOC_RELEASES, desc.getChild("releases", _dom.getNamespace()), Options
                .getDocHelpURL("releases")));
        
        //treeFillReleases(item, desc.getChild("releases", _dom.getNamespace()));
        item.setExpanded(true);
        
        item = new TreeItem(tree, SWT.NONE);
        item.setText("Resources");
        item.setData(new TreeData(Editor.DOC_RESOURCES, desc, Options.getDocHelpURL("resources")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Files");
        item2.setData(new TreeData(Editor.DOC_FILES, desc, Options.getDocHelpURL("files")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Databases");
        item2.setData(new TreeData(Editor.DOC_DATABASES, desc, Options.getDocHelpURL("databases")));
        item.setExpanded(true);
        //treeFillDatabaseResources(item2, desc.getChild("resources", _dom.getNamespace()));

        item = new TreeItem(tree, SWT.NONE);
        item.setText("Configuration");
        item.setData(new TreeData(Editor.DOC_CONFIGURATION, desc.getChild("configuration", _dom.getNamespace()),
                Options.getDocHelpURL("configuration")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Parameters");
        item2.setData(new TreeData(Editor.DOC_PARAMS, desc.getChild("configuration", _dom.getNamespace()), Options
                .getDocHelpURL("parameters")));
        item.setExpanded(true);
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Payload");
        item2.setData(new TreeData(Editor.DOC_PAYLOAD, desc.getChild("configuration", _dom.getNamespace()), Options
                .getDocHelpURL("payload")));
        item2 = new TreeItem(item, SWT.NONE);
        item2.setText("Settings");
        item2.setData(new TreeData(Editor.DOC_SETTINGS, desc.getChild("configuration", _dom.getNamespace()), Options
                .getDocHelpURL("settings")));

        _profiles = new TreeItem(item2, SWT.NONE);
        item2.setExpanded(true);
        _profiles.setText("Profiles");
        _profiles.setData(new TreeData(Editor.DOC_PROFILES, desc.getChild("configuration", _dom.getNamespace()),
                Options.getDocHelpURL("profiles")));
        //fillProfiles();

        _connections = new TreeItem(item2, SWT.NONE);
        _connections.setText("Connections");
        _connections.setData(new TreeData(Editor.DOC_CONNECTIONS, desc.getChild("configuration", _dom.getNamespace()),
                Options.getDocHelpURL("connections")));
        //fillConnections();
*/
        /*item = new TreeItem(tree, SWT.NONE);
        item.setText("Test");
        if(desc.getChild("Test", desc.getNamespace()) == null)
        	desc.addContent(new Element("Test", desc.getNamespace())).addContent(new Element("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml")));
        else if(desc.getChild("Test", desc.getNamespace()).getChild("div",org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml"))==null)
        	desc.getChild("Test", desc.getNamespace()).addContent(new Element("div", org.jdom.Namespace.getNamespace("http://www.w3.org/1999/xhtml")));
        */
        //item.setData(new TreeData(Editor.ACTIONS, desc, Options.getDocHelpURL("Actions")));
        

    }


   /* private Element getSettings() {
        Element configuration = _dom.getRoot().getChild("configuration", _dom.getNamespace());
        return configuration.getChild("settings", _dom.getNamespace());
    }


    public void fillProfiles() {
        Element settings = getSettings();
        _profiles.removeAll();
        if (settings != null) {
            for (Iterator it = settings.getChildren("profile", _dom.getNamespace()).iterator(); it.hasNext();) {
                Element element = (Element) it.next();
                TreeItem item = new TreeItem(_profiles, SWT.NONE);
                String name = Utils.getAttributeValue("name", element);
                item.setText("Sections [Profile: " + (name != null ? name : ProfilesListener.defaultName) + "]");
                item.setData(new TreeData(Editor.DOC_SECTIONS, element, Options.getDocHelpURL("sections")));
                _profiles.setExpanded(true);
                fillSections(item, element, false);
            }
        }
  
    }
*/

   /* public void fillConnections() {
        Element settings = getSettings();
        _connections.removeAll();
        if (settings != null) {
            for (Iterator it = settings.getChildren("connection", _dom.getNamespace()).iterator(); it.hasNext();) {
                Element element = (Element) it.next();
                TreeItem item = new TreeItem(_connections, SWT.NONE);
                String name = Utils.getAttributeValue("name", element);
                item.setText("Applications [Connection: " + (name != null ? name : ConnectionsListener.defaultName) + "]");
                item.setData(new TreeData(Editor.DOC_APPLICATIONS, element, Options.getDocHelpURL("applications")));
                _connections.setExpanded(true);
                fillApplications(item, element, false);
            }
        }
     
    }
    */


    /*public void fillApplications(TreeItem parent, Element element, boolean expand) {
        parent.removeAll();
        for (Iterator it = element.getChildren("application", _dom.getNamespace()).iterator(); it.hasNext();) {
            Element section = (Element) it.next();
            TreeItem item = new TreeItem(parent, SWT.NONE);
            item.setText("Sections [Appl.: " + Utils.getAttributeValue("name", section) + "]");
            item.setData(new TreeData(Editor.DOC_SECTIONS, section, Options.getDocHelpURL("sections")));
            parent.setExpanded(expand);
            fillSections(item, section, false);
        }
    }


    public void fillSections(TreeItem parent, Element element, boolean expand) {
        parent.removeAll();
        for (Iterator it = element.getChildren("section", _dom.getNamespace()).iterator(); it.hasNext();) {
            Element section = (Element) it.next();
            TreeItem item = new TreeItem(parent, SWT.NONE);
            item.setText("Settings [Section: " + Utils.getAttributeValue("name", section) + "]");
            item.setData(new TreeData(Editor.DOC_SECTION_SETTINGS, section, Options.getDocHelpURL("setting")));
            parent.setExpanded(expand);
        }
    }
*/

    public boolean treeSelection(Tree tree, Composite c) {
        try {
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
                        new EventForm(c, SWT.NONE, _dom, data.getElement(), _gui, Editor.EVENT_GROUP);
                        break;
                    case Editor.ADD_EVENT_GROUP:
                        new EventForm(c, SWT.NONE, _dom, data.getElement(), _gui, Editor.ADD_EVENT_GROUP);
                        break;   
                    case Editor.REMOVE_EVENT_GROUP:
                        new EventForm(c, SWT.NONE, _dom, data.getElement(), _gui, Editor.REMOVE_EVENT_GROUP);
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
		//new JobCommandListener(_dom, null, null).fillCommands(job, parent, expand);
		//fillCommands(job, parent, expand);
    	Element eCommands = action.getChild("commands");
    	if(eCommands != null) {
    		List commands = eCommands.getChildren("command");    		
    		if (commands != null) {
    			Iterator it = commands.iterator();
    			parent.removeAll();

    			while (it.hasNext()) {
    				Element e = (Element) it.next();
    				if (e.getAttributeValue("name") != null) {
    					TreeItem item = new TreeItem(parent, SWT.NONE);
    					item.setText(COMMAND_PREFIX + e.getAttributeValue("name"));
    					item.setData(new TreeData(Editor.JOB_COMMAND_EXIT_CODES, e, Options.getHelpURL("job.commands")));
    					item.setData("key", "commands_@_order");	
    					item.setData("copy_element", e);

    					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

    					treeFillCommand(item, e, false);
    				}
    			}
    		}
    		

    		//List addEvents = eCommands.getChildren("add_event");    		
    		//if (addEvents != null) {
    		//	Iterator it = addEvents.iterator();
    			
    		//	if (it.hasNext()) {
    				
    				TreeItem item = new TreeItem(parent, SWT.NONE);
					item.setText("add event");
					item.setData(new TreeData(Editor.ADD_EVENT_GROUP, eCommands, Options.getHelpURL("job.commands")));
					item.setData("key", "commands_@_add_event");	
					item.setData("copy_element", eCommands);
    				
					item = new TreeItem(parent, SWT.NONE);
					item.setText("remove event");
					item.setData(new TreeData(Editor.REMOVE_EVENT_GROUP, eCommands, Options.getHelpURL("job.commands")));
					item.setData("key", "commands_@_remove_event");	
					item.setData("copy_element", eCommands);
    				
    				/*if (e.getAttributeValue("name") != null) {
    					TreeItem item = new TreeItem(parent, SWT.NONE);
    					item.setText(e.getAttributeValue("name"));
    					item.setData(new TreeData(Editor.JOB_COMMAND_EXIT_CODES, e, Options.getHelpURL("job.commands")));
    					item.setData("key", "commands_@_order");	
    					item.setData("copy_element", e);

    					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

    					treeFillCommand(item, e, false);
    				}
    				*/
    			//}
    		//}

    		
    		
    		parent.setExpanded(expand);
    	}
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
