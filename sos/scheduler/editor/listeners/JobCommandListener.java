package sos.scheduler.editor.listeners;


import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import org.jdom.Element;
 
import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
 

public class JobCommandListener {	
	private IUpdate _main;

	private DomParser _dom;
	private String[] _chains = new String[0];

 

	private List _params;
 

	private Element _command;
	private Element _job;
	 

	public JobCommandListener(DomParser dom, Element command,  IUpdate update) {
		_dom = dom;
		_command = command;
		_main = update;
		if (_command != null) _job = _command.getParentElement();
	}
	

	public void fillCommands(Element job, TreeItem parent, boolean expand) {
		List commands  = job.getChildren("commands");	 
		if (commands != null) {
			Iterator it = commands.iterator();
			parent.removeAll();

			while (it.hasNext()) {
				Element e = (Element) it.next();
				if (e.getAttributeValue("on_exit_code") != null) {
					TreeItem item = new TreeItem(parent, SWT.NONE);
					item.setText(e.getAttributeValue("on_exit_code"));
					item.setData(new TreeData(Editor.COMMAND, e, Options.getHelpURL("job.commands")));		
				}
			}
		}
		parent.setExpanded(expand);
		
	}
	
	private void initParams(Table table) {
		int j = 0;
		 
		j = getActCommand(table);
		if (j>=0) {
		   List l = _command.getChildren();
		   if (l != null) {
   	      Element e = (Element)l.get(j);
   	      if (e.getChild("params") != null) {
   	        _params = e.getChild("params").getChildren();
   	      }
   	      if (_params == null){
   	      	e.addContent(0, new Element("params"));
     	      _params = e.getChild("params").getChildren();
   	        }
   	      }
		   }
 
 }
	
	public void clearParams() {
		_params = null;
	}

	public boolean isDisabled() {
		return _dom.isJobDisabled(Utils.getAttributeValue("name", _job));
	}
	
	public String getName() {
		return Utils.getAttributeValue("name", _job);
	}

	
	public String getTitle() {
		return Utils.getAttributeValue("title", _job);
	}


	public void fillParams(Table tableCommand,Table tableParameters) {
		clearParams();
		initParams(tableCommand);
		tableParameters.removeAll();
		
		 
		if (_params != null) {
			Iterator it = _params.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					if (e.getName().equals("param")) {
			   		TableItem item = new TableItem(tableParameters, SWT.NONE);
				   	item.setText(0, ((Element) o).getAttributeValue("name"));
					  item.setText(1, ((Element) o).getAttributeValue("value"));
					}
					if (e.getName().equals("copy_params")) {
			   		TableItem item = new TableItem(tableParameters, SWT.NONE);
				   	item.setText(0, "<from>");
					  item.setText(1, ((Element) o).getAttributeValue("from"));
					}
				}
			}
		}
	}

	public void deleteParameter(Table table, int index) {
		if (_params != null) {
			_params.remove(index);
			_dom.setChanged(true);
		}
		table.remove(index);
	}

	public void addCommand(Element e) {
		_dom.setChanged(true);
		_command.addContent(e);
	}
	
	private int getActCommand(Table table) {
		int index = table.getSelectionIndex();
		
		int j = index;
		int ignore=0;
		
		List c = _command.getChildren();
		Iterator it2 = c.iterator();
		while (it2.hasNext() && j >= 0) {
		
			Element e2 = (Element) it2.next();
 
			 
			if (!e2.getName().equals("start_job") && !e2.getName().equals("add_order")) {
         ignore++;
		  }else {
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
		_dom.setChanged(true);
  }
 
	public void saveParameter(Table table, String name, String value) {
		boolean found = false;
		if (_params != null) {
			
			
			if (name.equals("<from>")) {
				found = (table.getSelectionIndex() > -1);
			}else {
   			 int index = 0;
			   Iterator it = _params.iterator();
			   while (it.hasNext()) {
				   Object o = it.next();
				   if (o instanceof Element) {
					   Element e = (Element) o;
					   if (e.getName().equals("param")) {
   					   if (name.equals(e.getAttributeValue("name"))) {
		   				   found = true;
				   		   e.setAttribute("value", value);
						     _dom.setChanged(true);
						     table.getItem(index).setText(1, value);
   					   }
					   }
   				 index++;
				  }
			  }
		  } 
		
		
		if (name.equals("<from>") && found) {
			int index = table.getSelectionIndex();
			table.getItem(index).setText(0, name);
			table.getItem(index).setText(1, value);
			Element e = (Element) _params.get(index);
			e.setName("copy_params");
			e.setAttribute("from",value);
			e.removeAttribute("name");
			e.removeAttribute("value");
			_dom.setChanged(true);
		}
		}
		
		if (!found) {
		  Element e = new Element("param");
			if (!name.equals("<from>")) {
		   	 e.setAttribute("name", name);
			   e.setAttribute("value", value);
			}else {
  			 e.setName("copy_params");
		   	 e.setAttribute("from", value);
			}
			
			_dom.setChanged(true);
			if (_params == null)initParams(table);
			if (_params != null)_params.add(e);

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { name, value });
		}
		

	}
	 
	 
	
	public String getCommandAttribute(Table table,String attribute) {	 
		int i = getActCommand(table);
		List l = _command.getChildren();
		Element e = (Element)l.get(i);
		
		return Utils.getAttributeValue(attribute, e);
	}
	 

	public void setCommandAttribute(Button bApply, String name, String value, Table table) {
	if (_command != null) {
			
			int i = getActCommand(table);
			List l = _command.getChildren();
			if (i<l.size()  && i >= 0) {
			   Element e = (Element)l.get(i);

			   Utils.setAttribute(name, value, e , _dom);
			
			   if (bApply != null) {
	    	   bApply.setEnabled(true);
	       }
			}
		}
  }	
	
	public void setCommandName(Button bApply, String cmd, String value, Table table) {
		if (_command != null) {
   			_dom.setChanged(true);

				int i = getActCommand(table);
				List l = _command.getChildren();
				Element e = (Element)l.get(i);

				e.setName(cmd);
				if (cmd.equals("add_order")) {
					Utils.setAttribute("id", value, e , _dom);
					Utils.setAttribute("job", "", e , _dom);
				}else {
					Utils.setAttribute("id", "", e , _dom);
					Utils.setAttribute("job", value, e , _dom);
				}
				
				if (bApply != null) {
		    	bApply.setEnabled(true);
		    }
			}
	  }	
			 
	
		
	
		
		
	public boolean getCommandReplace(Table table) {	 
		int i = getActCommand(table);
		List l = _command.getChildren();
		Element e = (Element)l.get(i);
		
	 return Utils.getAttributeValue("replace", e).equalsIgnoreCase("yes");
		
	}
	
	
	
	public String getExitCode() {	 
 		 return Utils.getAttributeValue("on_exit_code", _command);
	}
		
				
	public void setExitCode(String value, boolean updateTree) {	 
		 Utils.setAttribute("on_exit_code",value, _command, _dom);
		 if (updateTree)	_main.updateCommand(value);
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
				if (e2.getName().equals("start_job") || e2.getName().equals("add_order")) {
          
					if (!created) { //Nur die commands add_order und start_job anzeigen
					   item = new TableItem(table, SWT.NONE);
		   			 item.setText(1,"");
		   			 created = true;
					}
					item.setText(0, e2.getName());
					item.setText(3,Utils.getAttributeValue("at", e2));
					if (e2.getName().equals("start_job")) item.setText(1,Utils.getAttributeValue("job", e2));
					if (e2.getName().equals("add_order")) {
						item.setText(1,Utils.getAttributeValue("id", e2));
						item.setText(2,Utils.getAttributeValue("job_chain", e2));
					}
				}
			}
		}			
 
	public String[] getJobChains() {
		Element element = _job.getParentElement().getParentElement().getChild("job_chains");
		if (element != null) {
			List chains = element.getChildren("job_chain");
			_chains = new String[chains.size()];
			int index = 0;
			Iterator it = chains.iterator();
			while (it.hasNext()) {
				String name = ((Element) it.next()).getAttributeValue("name");
				_chains[index++] = name != null ? name : "";
			}
		} else
			_chains = new String[0];

		return _chains;
	}

	public int getChainIndex(String jobChain) {
		for (int i = 0; i < _chains.length; i++) {
			if (_chains[i].equals(jobChain))
				return i;
		}
		return -1;
	}
}
