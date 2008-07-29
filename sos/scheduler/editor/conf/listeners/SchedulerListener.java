package sos.scheduler.editor.conf.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.*;

/*
 * jedes Treeitem muss folgende daten speichern, um die kopie und paste Funktion vollständig zu gewährleisten:
 * 
 * item.setData("copy_element", element); -> Element zum kopieren
 * item.setData("key", "run_time"); -> name des Elements bzw. Pfad vom copy_element aus. Beispiel Element ist job, dann muss Parameter den key = params.param haben
 * item.setData("override_attributes", "true"); -> optional: default ist false. Wenn true angegeben wird, dann werden die Attribute überschrieben. z.B. runtime  
 * item.setData("max_occur", "1"); -> Das Element darf max. einmal vorkommen: z.B. process_classes.
 * 
 */

public class SchedulerListener {

	private SchedulerDom  _dom;

	private SchedulerForm _gui;

	/** Aufruf erfolgt durch open Directory oder open Configurations*/
	private int type = -1;


	public SchedulerListener(SchedulerForm gui, SchedulerDom dom) {
		_gui = gui;
		_dom = dom;
	}

	public void treeFillMain(Tree tree, Composite c, int type_) {
		type = type_;		
		if(_dom.isLifeElement())
			treeFillMainForLifeElement(tree, c);
		else 
			treeFillMain(tree, c);
	}

	public void treeFillMainForLifeElement(Tree tree, Composite c) {

		tree.removeAll();

		Element element = _dom.getRoot();        

		TreeItem item = new TreeItem(tree, SWT.NONE);

		if(type == SchedulerDom.LIFE_JOB) {
			String name = "";
			if(_dom.getFilename() != null && new java.io.File(_dom.getFilename()).exists()) {
				name = new java.io.File(_dom.getFilename()).getName();
				name = name.substring(0, name.indexOf(".job.xml"));

				checkLifeAttributes(element, name);

				Utils.setAttribute("name", name, element);
			} else {
				name = Utils.getAttributeValue("name", element);
			}

			String job = "Job: " + name;
			job += _dom.isJobDisabled(name) ? " (Disabled)" : "";        	
			item.setText(job);
			item.setData(new TreeData(Editor.JOB, element, Options.getHelpURL("job")));
			item.setData("key", "job"); 
			item.setData("copy_element", element);
			if(!Utils.isElementEnabled("job", _dom, element)) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			} else {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			}
			treeFillJob(item, element, false);        	
			item.setExpanded(true);

		} else if(type == SchedulerDom.LIFE_JOB_CHAIN) {
			
			String name = "";
			if(_dom.getFilename() != null && new java.io.File(_dom.getFilename()).exists()) {
				name = new java.io.File(_dom.getFilename()).getName();
				name = name.substring(0, name.indexOf(".job_chain.xml"));				
				checkLifeAttributes(element, name);				
				Utils.setAttribute("name", name, element);										
			} else {
				name = Utils.getAttributeValue("name", element);				
			}			
			String jobChainName = "Job Chain: " + name;			
			item.setText(jobChainName);
			item.setData(new TreeData(Editor.JOB_CHAIN, element, Options.getHelpURL("job_chain")));
			item.setData("key", "job_chain");	
			item.setData("copy_element", element);
			Utils.setAttribute("orders_recoverable", true, element);
			Utils.setAttribute("visible", true, element);			
			if(!Utils.isElementEnabled("job_chain", _dom, element)) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			} else {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			}
			
			//Job Chain Nodes
			TreeItem in = new TreeItem(item, SWT.NONE);
			in.setText("Nodes");
			in.setData(new TreeData(Editor.JOB_CHAIN_NODES, element, Options.getHelpURL("job_chain")));
			in.setData("key", "job_chain_node");
			in.setData("copy_element", element);
			

			//Job Chain Nested Nodes
			TreeItem iNestedNodes = new TreeItem(item, SWT.NONE);
			iNestedNodes.setText("Nested Job Chains");
			iNestedNodes.setData(new TreeData(Editor.JOB_CHAIN_NESTED_NODES, element, Options.getHelpURL("job_chain")));
			iNestedNodes.setData("key", "job_chain_node.job_chain");
			iNestedNodes.setData("copy_element", element);
			iNestedNodes.setExpanded(true);
			
			

		} else if(type == SchedulerDom.LIFE_PROCESS_CLASS) {

			String name = "";
			if(_dom.getFilename() != null && new java.io.File(_dom.getFilename()).exists()) {
				name = new java.io.File(_dom.getFilename()).getName();
				name = name.substring(0, name.indexOf(".process_class.xml"));				
				checkLifeAttributes(element, name);				
				Utils.setAttribute("name", name, element);										
			} 			
			Element spooler = new Element("spooler");
			Element config = new Element("config");
			spooler.addContent(config);
			Element process_classes = new Element("process_classes");
			config.addContent(process_classes);
			process_classes.addContent((Element)element.clone());    		
			item.setData(new TreeData(Editor.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"), "process_classes"));
			item.setData("key", "process_classes");
			item.setData("copy_element", element);
			item.setData("max_occur", "1");
			item.setText("Process Classes");

		} else if(type == SchedulerDom.LIFE_LOCK) {

			String name = "";
			if(_dom.getFilename() != null && new java.io.File(_dom.getFilename()).exists()) {
				name = new java.io.File(_dom.getFilename()).getName();
				name = name.substring(0, name.indexOf(".lock.xml"));

				checkLifeAttributes(element, name);

				Utils.setAttribute("name", name, element);				

			} 
			Element spooler = new Element("spooler");
			Element config = new Element("config");
			spooler.addContent(config);
			Element locks = new Element("locks");
			config.addContent(locks);
			locks.addContent((Element)element.clone());        	                        
			item.setData(new TreeData(Editor.LOCKS, config, Options.getHelpURL("locks"), "locks"));
			item.setData("key", "locks");
			item.setData("copy_element", element);
			item.setText("Locks");

		} else if(type == SchedulerDom.LIFE_ORDER || type == SchedulerDom.LIFE_ADD_ORDER) {

			String name = "";
			if(_dom.getFilename() != null && new java.io.File(_dom.getFilename()).exists()) {
				name = new java.io.File(_dom.getFilename()).getName();
				name = name.substring(0, name.indexOf(".order.xml"));        	        	        	

				checkLifeAttributes(element, name);

				Utils.setAttribute("job_chain", name.substring(0, name.indexOf(",")), element);
				Utils.setAttribute("id", name.substring(name.indexOf(",")+1), element);
			}

			item.setText("Order: " + element.getAttributeValue("id"));
			item.setData(new TreeData(Editor.ORDER, element, Options.getHelpURL("orders")));
			item.setData("key", "commands_@_order");
			item.setData("copy_element", element);
			if(!Utils.isElementEnabled("commands", _dom, element)) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			} else {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			}
			treeFillOrder(item, element, false);

		} else if(type == SchedulerDom.LIFE_SCHEDULE) {

			String name = "";
			if(_dom.getFilename() != null && new java.io.File(_dom.getFilename()).exists()) {
				name = new java.io.File(_dom.getFilename()).getName();
				name = name.substring(0, name.indexOf(".schedule.xml"));				
				checkLifeAttributes(element, name);				
				Utils.setAttribute("name", name, element);										
			} else {
				name = Utils.getAttributeValue("name", element);				
			}			

			treeFillRunTimes(item, element, false, Utils.getAttributeValue("name", element));

			List l = element.getChildren("month");     	
			for(int i =0; i < l.size(); i++) {
				Element e = (Element)l.get(i);
				treeFillRunTimes(item.getItem(item.getItemCount()-1).getItem(item.getItem(item.getItemCount()-1).getItemCount()-1), e, false, Utils.getAttributeValue("month", e));
			}
			item.setExpanded(true);

		}

		tree.setSelection(new TreeItem[] { tree.getItem(0) });
		treeSelection(tree, c);

	}

	public void treeFillMain(Tree tree, Composite c) {
		tree.removeAll();

		Element config = _dom.getRoot().getChild("config");

		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.CONFIG, config, Options.getHelpURL("config")));
		item.setData("key", "config");
		item.setData("copy_element", config);
		item.setText("Config");		
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();

		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.BASE, config, Options.getHelpURL("base")));
		item.setData("key", "base");
		item.setData("copy_element", config);
		item.setText("Base Files");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.PARAMETER, config, Options.getHelpURL("parameter")));
		//item.setData("key", "params.param");
		item.setData("key", "params_@_param");
		item.setData("copy_element", config);
		item.setText("Parameter");
		
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();		

		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.SECURITY, config, Options.getHelpURL("security"), "security"));
		item.setData("key", "security");
		item.setData("max_occur", "1");		
		item.setData("copy_element", config);
		item.setText("Security");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();

		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.CLUSTER, config, Options.getHelpURL("cluster"), "cluster"));
		item.setData("key", "cluster");
		item.setData("max_occur", "1");
		item.setData("copy_element", config);
		item.setText("Cluster");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();

		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"), "process_classes"));
		item.setData("key", "process_classes");
		item.setData("max_occur", "1");
		item.setData("copy_element", config);
		item.setText("Process Classes");

		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.SCHEDULES, config, Options.getHelpURL("schedules"), "schedules"));
		item.setData("key", "schedules_@_schedule");
		item.setData("copy_element", config);
		item.setText("Schedules");
		treeFillSchedules(item);

		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.LOCKS, config, Options.getHelpURL("locks"), "locks"));
		item.setData("key", "locks");
		item.setData("copy_element", config);
		item.setText("Locks");

		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.SCRIPT, config, Options.getHelpURL("start_script"), "script"));
		item.setData("key", "script");
		item.setData("copy_element", config);
		item.setText("Start Script");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();

		if(type != SchedulerDom.DIRECTORY) {

			TreeItem http_server = new TreeItem(tree, SWT.NONE);                
			//http_server.setData(new TreeData(Editor.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
			http_server.setData(new TreeData(Editor.HTTP_SERVER, config, Options.getHelpURL("http_server"), "http_server"));
			//http_server.setData("key", "http_server");
			ArrayList  l = new ArrayList();
			l.add("http_server_@_web_service");
			l.add("http_server_@_http.authentication");
			l.add("http_server_@_http_directory");			
			http_server.setData("key", l);			
			http_server.setData("copy_element", config);
			http_server.setText("Http Server");

			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(Editor.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));			
			item.setData("key", "http_server_@_web_service");
			item.setData("copy_element", config);
			item.setText("Web Services");
			treeFillWebServices(item);


			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(Editor.HTTP_AUTHENTICATION, config, Options.getHelpURL("http_authentication"), "http_server"));
			item.setData("key", "http_server_@_http.authentication");
			item.setData("copy_element", config);
			item.setText("Http Authentication");


			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(Editor.HTTPDIRECTORIES, config, Options.getHelpURL("http_directories"), "http_server"));
			item.setData("key", "http_server_@_http_directory");
			item.setData("copy_element", config);
			item.setText("Http Directories");


		}

		if(type != SchedulerDom.DIRECTORY) {
			item = new TreeItem(tree, SWT.NONE);
			item.setData(new TreeData(Editor.HOLIDAYS, config, Options.getHelpURL("holidays"), "holidays"));
			item.setData("key", "holidays");
			item.setData("copy_element", config);
			item.setText("Holidays");


			treeFillHolidays(item, config);
		}
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.JOBS, config, Options.getHelpURL("jobs"), "jobs"));
		item.setData("key", "jobs_@_job");
		item.setData("copy_element", config);
		item.setText("Jobs");
		treeFillJobs(item);
		item.setExpanded(true);

		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.JOB_CHAINS, config, Options.getHelpURL("job_chains"), "job_chains"));
		item.setData("key", "job_chains_@_job_chain");
		item.setData("copy_element", config);
		item.setText("Job Chains");
		treeFillJobChains(item);


		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.ORDERS, config, Options.getHelpURL("orders"), "orders"));
		item.setData("key", "commands_@_order");
		item.setData("copy_element", config);
		item.setText("Orders");
		treeFillOrders(item, true);

		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.COMMANDS, config, Options.getHelpURL("commands"), "commands"));
		item.setData("key", "commands");
		item.setData("copy_element", config);
		item.setText("Commands");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();


		tree.setSelection(new TreeItem[] { tree.getItem(0) });
		treeSelection(tree, c);
	}



	public void treeFillOrders(TreeItem parent, boolean expand) {
		TreeItem orders = parent;

		if (!parent.getText().equals("Orders")) {
			Tree t = parent.getParent();
			for (int i = 0; i < t.getItemCount(); i++)
				if (t.getItem(i).getText().equals("Orders")) {
					orders = t.getItem(i);
				}			
		}

		if (orders != null) {

			orders.removeAll();
			Element commands = _dom.getRoot().getChild("config").getChild("commands");
			if (commands != null) {
				List l = commands.getChildren("add_order");
				if (l != null) {
					Iterator it = l.iterator();
					while (it.hasNext()) {
						Element e = (Element) it.next();
						if (e.getName().equals("add_order") && e.getAttributeValue("id") != null) {
							TreeItem item = new TreeItem(orders, SWT.NONE);
							item.setText("Order: " + e.getAttributeValue("id"));
							item.setData(new TreeData(Editor.ORDER, e, Options.getHelpURL("orders")));
							item.setData("key", "commands_@_order");
							item.setData("copy_element", commands);
							if(!Utils.isElementEnabled("commands", _dom, e)) {
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
							} else {
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
							}

							treeFillOrder(item, e, false);
						}
					}
				}

				List lOrder = commands.getChildren("order");
				if (lOrder != null) {
					Iterator it = lOrder.iterator();
					while (it.hasNext()) {
						Element e = (Element) it.next();
						if (e.getName().equals("order") && e.getAttributeValue("id") != null) {
							TreeItem item = new TreeItem(orders, SWT.NONE);
							item.setText("Order: " + e.getAttributeValue("id"));
							item.setData(new TreeData(Editor.ORDER, e, Options.getHelpURL("orders")));
							item.setData("key", "commands_@_order");
							item.setData("copy_element", e);
							if(!Utils.isElementEnabled("commands", _dom, e)) {
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
							} else {
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
							}
							treeFillOrder(item, e, false);
						}
					}
				}
			}
		}
		orders.setExpanded(expand);
	}



	public void treeFillJobs(TreeItem parent) {
		parent.removeAll();

		Element jobs = _dom.getRoot().getChild("config").getChild("jobs");        
		if (jobs != null) {
			Iterator it = jobs.getChildren().iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element element = (Element) o;
					
					if(type == SchedulerDom.DIRECTORY) {
						checkLifeAttributes(element, Utils.getAttributeValue("name", element));
					}

					TreeItem i = new TreeItem(parent, SWT.NONE);
					String name = Utils.getAttributeValue("name", element);
					String job = "Job: " + name;
					job += _dom.isJobDisabled(name) ? " (Disabled)" : "";

					i.setText(job);
					i.setData(new TreeData(Editor.JOB, element, Options.getHelpURL("job")));
					
					i.setData("copy_element", element);
					i.setData("key", "jobs_@_job");

					if(!Utils.isElementEnabled("job", _dom, element)) {
						i.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					} else {
						i.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					}
					treeFillJob(i, element, false);
				}
			}
		}
		parent.setExpanded(true);

	}

	public void treeExpandJob(TreeItem parent, String job) {

		//if (parent.getText().equals("Jobs")) {

		for (int i = 0; i < parent.getItemCount(); i++)
			//if (parent.getItem(i).getText().equals("Job: "+job)) {
			if (parent.getItem(i).getText().equals(job)) {
				parent.getItem(i).setExpanded(true);
			}

		//}

	}

	public void treeExpandItem(TreeItem parent, String elem) {
		for (int i = 0; i < parent.getItemCount(); i++)
			if (parent.getItem(i).getText().equals(elem)) {
				parent.getItem(i).setExpanded(true);
			}

	}

	public void treeFillJob(TreeItem parent, Element job, boolean expand) {

		boolean disable = !Utils.isElementEnabled("job", _dom, job);

		parent.removeAll();


		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setText("Execute");
		item.setData("max_occur", "1");
		item.setData("override_attributes", "true");
		item.setData(new TreeData(Editor.EXECUTE, job, Options.getHelpURL("job.execute")));
		//item.setData("key", "job.execute");
		//process script
		ArrayList  l = new ArrayList();
		l.add("process");
		l.add("script");
		//l.add("script.include");
		item.setData("key", l);
		item.setData("copy_element", job);
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}

		item = new TreeItem(parent, SWT.NONE);
		item.setData(new TreeData(Editor.PARAMETER, job, Options.getHelpURL("parameter")));
		item.setData("key", "params_@_param");
		
		ArrayList  ll = new ArrayList();
		ll.add("params_@_param");
		ll.add("params_@_include");
		//l.add("environment");			
		item.setData("key", ll);			
		
		
		item.setData("copy_element", job);
		item.setText("Parameter");
		//if(type == SchedulerDom.DIRECTORY)
		//	item.dispose();

		item = new TreeItem(parent, SWT.NONE);
		item.setText("Monitor");
		item.setData(new TreeData(Editor.MONITORS, job, Options.getHelpURL("job.monitor"), "monitor"));  
		item.setData("key", "monitor");
		item.setData("copy_element", job);
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		treeFillScripts(item, job, disable);

		item = new TreeItem(parent, SWT.NONE);
		item.setText("Run Options");
		item.setData(new TreeData(Editor.OPTIONS, job, Options.getHelpURL("job.options")));
		
		
		l = new ArrayList();
		l.add("start_when_directory_changed");			    
		l.add("delay_after_error");	                    
		l.add("delay_order_after_setback"); 
		//item.setData("key", "job.options");
		item.setData("key", l);
		
		item.setData("copy_element", job);
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}

		item = new TreeItem(parent, SWT.NONE);
		item.setText("Locks");
		item.setData(new TreeData(Editor.LOCKUSE, job, Options.getHelpURL("job.locks")));
		//item.setData("key", "job.locks");
		item.setData("key", "lock.use");
		item.setData("copy_element", job);
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}

		treeFillRunTimes(parent, job, disable, "run_time");

		/*List l = job.getChild("run_time").getChildren("month");     	
		for(int i =0; i < l.size(); i++) {
			Element e = (Element)l.get(i);
			treeFillRunTimes(parent.getItem(parent.getItemCount()-1).getItem(parent.getItem(parent.getItemCount()-1).getItemCount()-1), e, !Utils.isElementEnabled("job", _dom, job), Utils.getAttributeValue("month", e), false);
		}*/


		List commands = job.getChildren("commands");		
		item = new TreeItem(parent, SWT.NONE);
		item.setText("Commands");		
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}

		if (commands != null)
			treeFillCommands(item, job, false);

		item.setData(new TreeData(Editor.JOB_COMMANDS, job, Options.getHelpURL("job.commands")));
		//item.setData("key", "job_@_commands");
		item.setData("key", "commands");
		item.setData("copy_element", job);
		parent.setExpanded(expand);

	}


	public void treeFillOrder(TreeItem parent, Element order, boolean expand) {
		parent.removeAll();

		//Element runtime = order.getChild("run_time");

		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setData(new TreeData(Editor.PARAMETER, order, Options.getHelpURL("parameter")));
		item.setData("key", "params_@_param");
		item.setData("copy_element", order);
		item.setText("Parameter");


		treeFillRunTimes(parent, order, false, "run_time");

		List l = order.getChild("run_time").getChildren("month"); 
		for(int i =0; i < l.size(); i++) {
			Element e = (Element)l.get(i);
			treeFillRunTimes(parent.getItem(parent.getItemCount()-1).getItem(parent.getItem(parent.getItemCount()-1).getItemCount()-1), e, !Utils.isElementEnabled("job", _dom, order), Utils.getAttributeValue("month", e));    		
		}

		parent.setExpanded(expand);
	}


	public void treeFillHolidays(TreeItem item, Element elem) {
		
		
		
		item = new TreeItem(item, SWT.NONE);
		item.setText("Weekdays");
		item.setData(new TreeData(Editor.WEEKDAYS, elem, Options.getHelpURL("job.run_time.weekdays"),"weekdays"));
		item.setData("key", "holidays_@_weekdays");
		item.setData("copy_element", elem);		
					
		if( elem.getChild("holidays") != null) {
			treeFillDays(item,  elem, 0, false);
		}
			//treeFillDays(item,  elem.getChild("holidays"), 0, false);
		_gui.updateFont(item);

		
		//wurde zurückgestellt
		/*Element holidays = elem.getChild("holidays");
		if(holidays == null) {
			holidays = new Element("holidays");
			elem.addContent(holidays);											
		}
		System.out.println(Utils.getElementAsString(_dom.getRoot()));
		if(holidays.getChild("run_time") == null) {			
			holidays.addContent(new Element("run_time"));
		}

		treeFillRunTimes(item, holidays, false, "run_time", false);				
		
		*/
		
	}


	public void treeFillExitCodesCommands(TreeItem parent, Element elem, boolean expand) {
		parent.removeAll();
		treeFillExitCodesCommands(parent, elem.getChildren("order"));
		treeFillExitCodesCommands(parent, elem.getChildren("add_order"));
		treeFillExitCodesCommands(parent, elem.getChildren("start_job"));

	}

	private void treeFillExitCodesCommands(TreeItem parent,List cmdList) {		
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


	public void treeFillCommands(TreeItem parent, Element job, boolean expand) {
		//new JobCommandListener(_dom, null, null).fillCommands(job, parent, expand);
		//fillCommands(job, parent, expand);
		List commands = job.getChildren("commands");
		java.util.ArrayList listOfReadOnly = _dom.getListOfReadOnlyFiles();
		if (commands != null) {
			Iterator it = commands.iterator();
			parent.removeAll();

			while (it.hasNext()) {
				Element e = (Element) it.next();
				if (e.getAttributeValue("on_exit_code") != null) {
					TreeItem item = new TreeItem(parent, SWT.NONE);
					item.setText(e.getAttributeValue("on_exit_code"));
					item.setData(new TreeData(Editor.JOB_COMMAND_EXIT_CODES, e, Options.getHelpURL("job.commands")));
					item.setData("key", "commands_@_order");	
					item.setData("copy_element", e);

					if (listOfReadOnly != null && listOfReadOnly.contains(Utils.getAttributeValue("name", job))) {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					} else {
						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					}
					treeFillExitCodesCommands(item, e, false);
				}
			}
		}
		parent.setExpanded(expand);
	}


	public void treeFillDays(TreeItem parent, Element element, int type, boolean expand){
		treeFillDays(parent, element, type, expand, null);		
	}

	public void treeFillDays(TreeItem parent, Element element, int type, boolean expand, String name) {
		parent.removeAll();
		if (element != null) { 
			if (    type == DaysListener.WEEKDAYS || 
					type == DaysListener.MONTHDAYS || 
					type == DaysListener.ULTIMOS || 
					type == DaysListener.SPECIFIC_MONTHS) {
				
				//if(element.getName().equals("config") && element.getChild("holidays") != null)
				//	element = element.getChild("holidays");
				
				if(parent.getParentItem().getText().equals("Holidays")) {
					if(element.getChild("holidays") == null)
						element.addContent(new Element("holidays"));
					
					element = element.getChild("holidays");
					return;
						
				}
					
				
				//element = isHolidayWeeksdayParent(element);
				
				new DaysListener(_dom, element, type, ( parent.getData("key") != null && parent.getData("key").equals("holidays_@_weekdays"))).fillTreeDays(parent, expand);
				
				if(type == DaysListener.SPECIFIC_MONTHS) {
					List l = element.getChildren("month"); 
					//TreeItem item = _gui.getTree().getSelection()[0];
					for(int i =0; i < l.size(); i++) {
						Element e = (Element)l.get(i);
						//treeFillRunTimes(_gui.getTree().getSelection()[0], e, !Utils.isElementEnabled("job", _dom, element), Utils.getAttributeValue("month", e), false);                	
						treeFillRunTimes(parent, e, !Utils.isElementEnabled("job", _dom, element), Utils.getAttributeValue("month", e));
					}

				}


			} else if ( type==4) {
				//new DateListener(_dom, element, 1).fillTreeDays(parent, expand);
			}  else if (type == 6) {
				new DateListener(_dom, element, DateListener.DATE).fillTreeDays(parent, expand);
			} else {
				System.out.println("Invalid type = " + type + " for filling the days in the tree!");
			}
		}
	}


	public void treeFillSpecificWeekdays(TreeItem parent, Element element, boolean expand) {
		if (element != null) {

			new SpecificWeekdaysListener(_dom, element).fillTreeDays(parent, expand);

		}
	}

	public void treeFillJobChains(TreeItem parent) {
		parent.removeAll();
		//java.util.ArrayList listOfReadOnly = _dom.getListOfReadOnlyFiles();
		Element jobChains = _dom.getRoot().getChild("config").getChild("job_chains");        
		if (jobChains != null) {
			Iterator it = jobChains.getChildren().iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element element = (Element) o;
					TreeItem i = new TreeItem(parent, SWT.NONE);

					String name = Utils.getAttributeValue("name", element);
					String jobChainName = "Job Chain: " + name;

					i.setText(jobChainName);
					i.setData(new TreeData(Editor.JOB_CHAIN, element, Options.getHelpURL("job_chain")));
					i.setData("key", "job_chains_@_job_chain");					
					i.setData("copy_element", element);
										
					//Job Chain Nodes
					TreeItem iNodes = new TreeItem(i, SWT.NONE);
					iNodes.setText("Nodes");
					iNodes.setData(new TreeData(Editor.JOB_CHAIN_NODES, element, Options.getHelpURL("job_chain")));
					//iNodes.setData("key", "job_chain_node");
					//iNodes.setData("key", "job_chain_node");
					iNodes.setData("key", "job_chain_node");
					iNodes.setData("copy_element", element);
					iNodes.setExpanded(true);
					
					//Job Chain Nested Nodes
					TreeItem iNestedNodes = new TreeItem(i, SWT.NONE);
					iNestedNodes.setText("Nested Job Chains");
					iNestedNodes.setData(new TreeData(Editor.JOB_CHAIN_NESTED_NODES, element, Options.getHelpURL("job_chain")));
					//iNestedNodes.setData("key", "job_chain_node.job_chain");
					iNestedNodes.setData("key", "job_chain_node.job_chain");
					//iNestedNodes.setData("key", "job_chain_node.job_chain");
					iNestedNodes.setData("copy_element", element);
					iNestedNodes.setExpanded(true);
					
					
					if(!Utils.isElementEnabled("job_chain", _dom, element)) {
						i.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
						iNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
						iNestedNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					} else {
						i.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
						iNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
						iNestedNodes.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					}

				}
			}
		}
		parent.setExpanded(true);

	}
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
				if (data != null) {

					_dom.setInit(true);
					switch (data.getType()) {
					case Editor.CONFIG:
						new ConfigForm(c, SWT.NONE, _dom, _gui);						
						break;
					case Editor.PARAMETER:					
						int type = getType(data.getElement());
						new sos.scheduler.editor.conf.forms.ParameterForm(c, SWT.NONE, _dom, data.getElement(), _gui, type);						
						break;
					case Editor.SECURITY:
						new SecurityForm(c, SWT.NONE, _dom, data.getElement());
						break;
					case Editor.CLUSTER:
						new ClusterForm(c, SWT.NONE, _dom, data.getElement());
						break;
					case Editor.BASE:
						new BaseForm(c, SWT.NONE, _dom);
						break;
					case Editor.PROCESS_CLASSES:
						new ProcessClassesForm(c, SWT.NONE, _dom, data.getElement());
						break;
					case Editor.LOCKS:
						new LocksForm(c, SWT.NONE, _dom, data.getElement());
						break;
					case Editor.MONITORS:
						new ScriptsForm(c, SWT.NONE, _dom, _gui, data.getElement());
						break;
					case Editor.MONITOR:
						new ScriptForm(c, SWT.NONE, "Monitor", _dom, data.getElement(), data.getType(), _gui);
						break;
					case Editor.SCRIPT:
						new ScriptForm(c, SWT.NONE, "Start Script", _dom, data.getElement(), data.getType(), _gui);
						break;
					case Editor.JOB:
						new JobForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.EXECUTE:
						new ExecuteForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.ORDERS:
						//new OrdersForm(c, SWT.NONE, _dom, _gui, this);
						new OrdersForm(c, SWT.NONE, _dom, _gui);
						break;
					case Editor.ORDER:
						new OrderForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.JOB_COMMAND_EXIT_CODES:
						new sos.scheduler.editor.conf.forms.JobCommandExitCodesForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.JOB_COMMAND:
						new JobCommandForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.JOB_COMMANDS:
						new JobCommandsForm(c, SWT.NONE, _dom, data.getElement(), _gui, this);
						break;
					case Editor.RUNTIME:
						new RunTimeForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.WEEKDAYS:
						new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.WEEKDAYS, ( item.getData("key") != null && item.getData("key").equals("holidays_@_weekdays")));
						break;
					case Editor.MONTHDAYS:
						new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.MONTHDAYS, false);
						break;
					case Editor.SPECIFIC_MONTHS:
						new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.SPECIFIC_MONTHS, false);
						break;
					case Editor.ULTIMOS:
						new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.ULTIMOS, false);
						break;
					case Editor.EVERYDAY:
					case Editor.PERIODS:
						new PeriodsForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.JOBS:
						new JobsForm(c, SWT.NONE, _dom, _gui);
						break;
					case Editor.HOLIDAYS:
						new DateForm(c, SWT.NONE, DateListener.HOLIDAY, _dom, data.getElement(), _gui);
						break;
					case Editor.DAYS:
						new DateForm(c, SWT.NONE, DateListener.DATE, _dom, data.getElement(), _gui);
						break;
					case Editor.SPECIFIC_WEEKDAYS:
						new SpecificWeekdaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.MONTHDAYS);
						break;
					case Editor.WEBSERVICES:
						new WebservicesForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.WEBSERVICE:
						new WebserviceForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.HTTPDIRECTORIES:
						new HttpDirectoriesForm(c, SWT.NONE, _dom, data.getElement());
						break;
					case Editor.HTTP_AUTHENTICATION:
						new HttpAuthenticationForm(c, SWT.NONE, _dom, data.getElement());
						break;
					case Editor.OPTIONS:
						new JobOptionsForm(c, SWT.NONE, _dom, data.getElement());
						break;
					case Editor.LOCKUSE:
						new JobLockUseForm(c, SWT.NONE, _dom, data.getElement());
						break;
					case Editor.JOB_CHAINS:
						new JobChainsForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						//JobChainsForm jc= new JobChainsForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						//jc.setISchedulerUpdate(_gui);
						break;
					case Editor.JOB_CHAIN:
						JobChainForm jc_= new JobChainForm(c, SWT.NONE, _dom, data.getElement());
						jc_.setISchedulerUpdate(_gui);
						break;
					case Editor.JOB_CHAIN_NODES:
						JobChainNodesForm jcn_= new JobChainNodesForm(c, SWT.NONE, _dom, data.getElement());
						jcn_.setISchedulerUpdate(_gui);
						break;
					case Editor.JOB_CHAIN_NESTED_NODES:
						JobChainNestedNodesForm j= new JobChainNestedNodesForm(c, SWT.NONE, _dom, data.getElement());
						j.setISchedulerUpdate(_gui);
						break;
						
					case Editor.COMMANDS:
						new CommandsForm(c, SWT.NONE, _dom, _gui);
						break;
					case Editor.SCHEDULES:
						new sos.scheduler.editor.conf.forms.SchedulesForm(c, SWT.NONE, _dom, _gui);
						break;
					case Editor.SCHEDULE:
						new sos.scheduler.editor.conf.forms.ScheduleForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.HTTP_SERVER:						
						break;

					default:
						System.out.println("no form found for " + item.getText());
					}
				}

				c.layout();
			}
		} catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}

			e.printStackTrace();
			MainWindow.message(e.getMessage(), SWT.ICON_ERROR);
		}
		_dom.setInit(false);
		return true;
	}

	private void checkLifeAttributes(Element element, String name) {
		if(element.getName().equals("add_order") || element.getName().equals("order")) {
			
			if((Utils.getAttributeValue("job_chain", element) + "," + Utils.getAttributeValue("id", element)).equalsIgnoreCase(name)) {
				//Attribut name ist ungleich der Dateiname
				ArrayList l = new ArrayList();
				if(_dom.getListOfChangeElementNames() != null)
					l = _dom.getListOfChangeElementNames();
				l.add(element.getName() + "_" + name);
				_dom.setListOfChangeElementNames(l);

			}

		} else {
			
			if(Utils.getAttributeValue("name", element).length() > 0 && 
					!Utils.getAttributeValue("name", element).equalsIgnoreCase(name)) {
				//Attribut name ist ungleich der Dateiname
				ArrayList l = new ArrayList();
				if(_dom.getListOfChangeElementNames() != null)
					l = _dom.getListOfChangeElementNames();
				l.add(element.getName() + "_" + name);
				_dom.setListOfChangeElementNames(l);
				_dom.setChanged(true);
			}
			if(element.getName().equals("job") && Utils.getAttributeValue("spooler_id", element).length() > 0) {
				element.removeAttribute("spooler_id");
				ArrayList l = new ArrayList();
				if(_dom.getListOfChangeElementNames() != null)
					l = _dom.getListOfChangeElementNames();
				l.add(element.getName() + "_" + name);
				_dom.setListOfChangeElementNames(l);
				_dom.setChanged(true);
			}
		}
	}

	public void treeFillRunTimes(TreeItem item,Element job, boolean disable, String run_time) {
		
		
		
		
		Element runtime = null; 
		Element _runtime = job.getChild("run_time");
	
		
		
		// create runtime tag
		if (_runtime == null && run_time.equals("run_time")) { 			
			_runtime = new Element(run_time);
			job.addContent(_runtime);	
			runtime=_runtime;		
		}  else if(!run_time.equals("run_time") && job.getName().equals("month")){
			//_runtime = Utils.getRunTimeParentElement(job);
			_runtime = job.getParentElement();
		} else if(job.getName().equals("schedule")) {
			runtime = job;
		} else {
			runtime=_runtime;
		}

		
		
		//Specific months: create month child tags (type=3)
		if(!run_time.equals("run_time") && !job.getName().equals("schedule")) {

			List _monthList = _runtime.getChildren("month");
			//List _monthList = job.getParentElement().getChildren("month");
			boolean monthFound = false;
			for(int i = 0; i < _monthList.size(); i++) {
				Element _month = (Element)_monthList.get(i);
				if(Utils.getAttributeValue("month", _month).equalsIgnoreCase(run_time)) {
					monthFound = true;
					job = _month;
					runtime=job;

				} 
			}
			if(!monthFound) {
				Element newMonth = new Element("month"); 
				Utils.setAttribute("month", run_time, newMonth );
				//_runtime.addContent(newMonth);
				job.addContent(newMonth);
				job = newMonth;
				runtime=job;

			}

		}
		
		//test
		TreeItem run = null;
		boolean hasSchedulesAttribut = Utils.getAttributeValue("schedule", _runtime).trim().length() > 0;
		if(hasSchedulesAttribut) {

			for(int i = 0; i < item.getItemCount(); i++) {
				if(item.getItem(i).equals("Run Time")){
					run = item.getItem(i);
				}
			}

		}
		if(item.getText().equals("Run Time")){
			run = item;
		}
		//ende test

		//if(run == null) {
		if (runtime != null) {
			if(type == SchedulerDom.LIFE_SCHEDULE) {
				run = item;
			} else if(run == null) {
				run = new TreeItem(item, SWT.NONE);
			}
			run.setData("max_occur", "1");
			
			run.setText(run_time);
			if(run_time.equals("run_time")) {
				run.setText("Run Time");								
			} else {
				run.setText(run_time);

			}
			
			

			if(run_time.equals("run_time")) {
				run.setData(new TreeData(Editor.RUNTIME, job, Options.getHelpURL("job.run_time"), "run_time"));
				run.setData("key", "run_time");
				run.setData("override_attributes", "true");
				run.setData("max_occur", "1");
				run.setData("copy_element", job);
				if(disable) {
					run.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
				_gui.updateFont(run);
			} else if(job.getName().equals("schedule")) {
				run.setData(new TreeData(Editor.SCHEDULE, job, Options.getHelpURL("job.schedule"), "schedule"));
				run.setData("key", "schedules_@_schedule");
				run.setData("copy_element", job);
			} 

			item = new TreeItem(run, SWT.NONE);
			item.setText("Everyday");
			item.setData(new TreeData(Editor.EVERYDAY, runtime, Options.getHelpURL("job.run_time.everyday")));
			//item.setData("key", "run_time_@_everyday");	
			item.setData("key", "period");
			item.setData("copy_element", job.getChild("run_time"));			
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			_gui.updateFont(item);
			
			item = new TreeItem(run, SWT.NONE);
			item.setText("Weekdays");
			item.setData(new TreeData(Editor.WEEKDAYS, runtime, Options.getHelpURL("job.run_time.weekdays"),"weekdays"));
			item.setData("key", "weekdays");
			item.setData("override_attributes", "true");
			item.setData("max_occur", "1");
			item.setData("copy_element", job.getChild("run_time"));
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}			
			treeFillDays(item, runtime, 0, false);
			_gui.updateFont(item);

			item = new TreeItem(run, SWT.NONE);
			item.setText("Monthdays");
			item.setData(new TreeData(Editor.MONTHDAYS, runtime, Options.getHelpURL("job.run_time.monthdays"),"monthdays"));
			item.setData("key", "monthdays");
			item.setData("override_attributes", "true");
			item.setData("max_occur", "1");
			item.setData("copy_element", job.getChild("run_time"));
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillDays(item, runtime, 1, false);
			_gui.updateFont(item);

			item = new TreeItem(run, SWT.NONE);
			item.setText("Ultimos");
			item.setData(new TreeData(Editor.ULTIMOS, runtime, Options.getHelpURL("job.run_time.ultimos"), "ultimos"));
			item.setData("key", "ultimos");
			item.setData("override_attributes", "true");
			item.setData("max_occur", "1");
			item.setData("copy_element", job.getChild("run_time"));
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillDays(item, runtime, 2, false);
			_gui.updateFont(item);


			item = new TreeItem(run, SWT.NONE);
			item.setText("Specific Weekdays");
			item.setData(new TreeData(Editor.SPECIFIC_WEEKDAYS, runtime, Options.getHelpURL("job.run_time.monthdays"), "monthdays"));
			item.setData("key", "monthdays");
			item.setData("override_attributes", "true");
			item.setData("max_occur", "1");
			item.setData("copy_element", job.getChild("run_time"));
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillSpecificWeekdays(item, runtime, false);
			_gui.updateFont(item);

			//Specific Days
			if(run_time.equals("run_time") || job.getName().equals("schedule")) {
				item = new TreeItem(run, SWT.NONE);
				item.setText("Specific Days");
				item.setData(new TreeData(Editor.DAYS, runtime, Options.getHelpURL("job.run_time.specific_days")));
				item.setData("key", "specific_days");
				item.setData("override_attributes", "true");
				item.setData("max_occur", "1");
				item.setData("copy_element", job.getChild("run_time"));
				treeFillDays(item, runtime, 6, false);
				//item.getParent().setSelection(new TreeItem[] {item});
				//setFontForRuntimeChild(item);
				_gui.updateFont(item);
			}
			
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}

			//Specific Monthdays
			if(run_time.equals("run_time") || job.getName().equals("schedule")) {
				item = new TreeItem(run, SWT.NONE);
				item.setText("Specific Month");
				//item.setData(new TreeData(Editor.SPECIFIC_MONTHS, runtime, Options.getHelpURL("job.run_time.monthdays"),"specific_monthdays"));
				item.setData(new TreeData(Editor.SPECIFIC_MONTHS, runtime, Options.getHelpURL("job.run_time.monthdays"),"month"));
				item.setData("key", "specific_monthdays");	
				item.setData("override_attributes", "true");
				item.setData("max_occur", "1");
				item.setData("copy_element", job.getChild("run_time"));
				if(disable) {
					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}			
				treeFillDays(item, runtime, DaysListener.SPECIFIC_MONTHS, false);
				_gui.updateFont(item);
			}
			
			//holidays
			if(run_time.equals("run_time") || job.getName().equals("schedule")) {
				item = new TreeItem(run, SWT.NONE);
				item.setData(new TreeData(Editor.HOLIDAYS, runtime, Options.getHelpURL("holidays"), "holidays"));
				item.setData("key", "holidays");
				item.setData("override_attributes", "true");
				item.setData("max_occur", "1");
				item.setData("copy_element", job.getChild("run_time"));
				item.setText("Holidays");
				treeFillHolidays(item, runtime);
			}
			

			/*if (runtime != null)
				treeFillDays(item, runtime, 6, false);
*/


		}

	}

	/*public void fillCommands(Element job, TreeItem parent, boolean expand) {
        List commands = job.getChildren("commands");
        java.util.ArrayList listOfReadOnly = _dom.getListOfReadOnlyFiles();
        if (commands != null) {
            Iterator it = commands.iterator();
            parent.removeAll();

            while (it.hasNext()) {
                Element e = (Element) it.next();
                if (e.getAttributeValue("on_exit_code") != null) {
                    TreeItem item = new TreeItem(parent, SWT.NONE);
                    item.setText(e.getAttributeValue("on_exit_code"));
                    item.setData(new TreeData(Editor.JOB_COMMAND, e, Options.getHelpURL("job.commands")));

                    if (listOfReadOnly != null && listOfReadOnly.contains(Utils.getAttributeValue("name", job))) {
                    	item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                    } else {
                    	item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                    }
                }
            }
        }
        parent.setExpanded(expand);

    }
	 */

	private int getType(Element elem){
		if(elem.getName().equals("job") )
			return Editor.JOB; 
		else if(elem.getName().equals("order") && (_dom.isLifeElement() || (elem.getParentElement() != null && elem.getParentElement().getName().equals("commands"))))
			return Editor.COMMANDS;
		else if( (elem.getName().equals("order") || elem.getName().equals("add_order") || elem.getName().equals("start_job")) )
			return Editor.JOB_COMMANDS;
		else if(elem.getName().equals("web_service") )
			return Editor.WEBSERVICE;
		else
			return Editor.CONFIG ;
	}

	
	
	public void treeFillWebServices(TreeItem parent) {
		parent.removeAll();

		Element httpServer = null;		

		Element config = _dom.getRoot().getChild("config");
		if(config != null)
			httpServer = _dom.getRoot().getChild("config").getChild("http_server");
		if (httpServer != null) {
			Iterator it = httpServer.getChildren("web_service").iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element element = (Element) o;
					TreeItem item = new TreeItem(parent, SWT.NONE);
					item.setData(new TreeData(Editor.WEBSERVICE, element, Options.getHelpURL("http_server"), "http_server"));
					item.setData("key", "http_server_@_web_service");
					item.setData("copy_element", element);
					item.setText("Web Service: " + element.getAttributeValue("name"));
					
					TreeItem itemParam = new TreeItem(item, SWT.NONE);
					itemParam.setData(new TreeData(Editor.PARAMETER, element, Options.getHelpURL("parameter")));
					itemParam.setData("key", "params_@_param");
					item.setData("copy_element", element);
					itemParam.setText("Parameter");
					
				}
			}
		}
		parent.setExpanded(true);

	}

	
	public void treeFillSchedules(TreeItem parent) {
		parent.removeAll();

		Element schedules = _dom.getRoot().getChild("config").getChild("schedules");        
		if (schedules != null) {
			Iterator it = schedules.getChildren().iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element element = (Element) o;
					if(type == SchedulerDom.DIRECTORY) {
						checkLifeAttributes(element, Utils.getAttributeValue("name", element));
					}
					treeFillRunTimes(parent, element, false, Utils.getAttributeValue("name", element));
				}
			}
		}
		parent.setExpanded(true);

	}
	
	public void treeFillScripts(TreeItem parent, Element elem, boolean disable) {
		parent.removeAll();

		List l = elem.getChildren("monitor");
		
		for(int i = 0; i < l.size(); i++) {
			Element monitor = (Element)l.get(i);
			TreeItem item = new TreeItem(parent, SWT.NONE);
			//item.setText("Monitor: " + Utils.getAttributeValue("name", monitor));
			if(Utils.getAttributeValue("name", monitor).equals(""))
				item.setText("<empty>");
			else
				item.setText(Utils.getAttributeValue("name", monitor));
			item.setData(new TreeData(Editor.MONITOR, monitor, Options.getHelpURL("job.monitor"), "monitor"));  
			item.setData("key", "monitor");
			item.setData("copy_element", elem);
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
		}
				
		parent.setExpanded(true);

	}

	
}
