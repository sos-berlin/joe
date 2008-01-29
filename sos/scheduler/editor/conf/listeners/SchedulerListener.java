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
import sos.scheduler.editor.conf.forms.BaseForm;
import sos.scheduler.editor.conf.forms.ClusterForm;
import sos.scheduler.editor.conf.forms.CommandsForm;
import sos.scheduler.editor.conf.forms.ConfigForm;
import sos.scheduler.editor.conf.forms.DateForm;
import sos.scheduler.editor.conf.forms.DaysForm;
import sos.scheduler.editor.conf.forms.ExecuteForm;
import sos.scheduler.editor.conf.forms.HttpAuthenticationForm;
import sos.scheduler.editor.conf.forms.HttpDirectoriesForm;
import sos.scheduler.editor.conf.forms.JobChainForm;
import sos.scheduler.editor.conf.forms.JobChainsForm;
import sos.scheduler.editor.conf.forms.JobCommandForm;
import sos.scheduler.editor.conf.forms.JobCommandsForm;
import sos.scheduler.editor.conf.forms.JobForm;
import sos.scheduler.editor.conf.forms.JobLockUseForm;
import sos.scheduler.editor.conf.forms.JobOptionsForm;
import sos.scheduler.editor.conf.forms.JobsForm;
import sos.scheduler.editor.conf.forms.LocksForm;
import sos.scheduler.editor.conf.forms.OrderForm;
import sos.scheduler.editor.conf.forms.OrdersForm;
import sos.scheduler.editor.conf.forms.PeriodsForm;
import sos.scheduler.editor.conf.forms.ProcessClassesForm;
import sos.scheduler.editor.conf.forms.RunTimeForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.forms.ScriptForm;
import sos.scheduler.editor.conf.forms.SecurityForm;
import sos.scheduler.editor.conf.forms.SpecificWeekdaysForm;
import sos.scheduler.editor.conf.forms.WebservicesForm;

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
			
			Utils.setAttribute("orders_recoverable", true, element);
			Utils.setAttribute("visible", true, element);
			
			if(!Utils.isElementEnabled("job_chain", _dom, element)) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			} else {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			}
			
		} else if(type == SchedulerDom.LIFE_PROCESS_CLASS) {
						
			Element spooler = new Element("spooler");
			Element config = new Element("config");
			spooler.addContent(config);
			Element process_classes = new Element("process_classes");
			config.addContent(process_classes);
			process_classes.addContent((Element)element.clone());        				
			item.setData(new TreeData(Editor.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"), "process_classes"));
			item.setData("key", "process_classes");
			item.setText("Process Classes");
			
		} else if(type == SchedulerDom.LIFE_LOCK) {
			
			Element spooler = new Element("spooler");
			Element config = new Element("config");
			spooler.addContent(config);
			Element locks = new Element("locks");
			config.addContent(locks);
			locks.addContent((Element)element.clone());        	                        
			item.setData(new TreeData(Editor.LOCKS, config, Options.getHelpURL("locks"), "locks"));
			item.setData("key", "locks");
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
			
			item.setText("Order:" + element.getAttributeValue("id"));
			item.setData(new TreeData(Editor.ORDER, element, Options.getHelpURL("orders")));
			item.setData("key", "orders");
			if(!Utils.isElementEnabled("commands", _dom, element)) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			} else {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			}
			treeFillOrder(item, element, false);
			
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
		item.setText("Config");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.PARAMETER, config, Options.getHelpURL("parameter")));
		item.setData("key", "parameter");
		item.setText("Parameter");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.BASE, config, Options.getHelpURL("base")));
		item.setData("key", "base");
		item.setText("Base Files");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.SECURITY, config, Options.getHelpURL("security"), "security"));
		item.setData("key", "security");
		item.setText("Security");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.CLUSTER, config, Options.getHelpURL("cluster"), "cluster"));
		item.setData("key", "cluster");
		item.setText("Cluster");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.PROCESS_CLASSES, config, Options.getHelpURL("process_classes"), "process_classes"));
		item.setData("key", "process_classes");
		item.setText("Process Classes");
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.LOCKS, config, Options.getHelpURL("locks"), "locks"));
		item.setData("key", "locks");
		item.setText("Locks");
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.SCRIPT, config, Options.getHelpURL("start_script"), "script"));
		item.setData("key", "script");
		item.setText("Start Script");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		if(type != SchedulerDom.DIRECTORY) {
			
			TreeItem http_server = new TreeItem(tree, SWT.NONE);                
			http_server.setData(new TreeData(Editor.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
			http_server.setData("key", "http_server");
			http_server.setText("Http Server");
			
			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(Editor.WEBSERVICES, config, Options.getHelpURL("http_server"), "http_server"));
			item.setData("key", "http_server");
			item.setText("Web Services");
			
			
			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(Editor.HTTP_AUTHENTICATION, config, Options.getHelpURL("http_authentication"), "http_server"));
			item.setData("key", "http_server");
			item.setText("Http Authentication");
			
			
			item = new TreeItem(http_server, SWT.NONE);
			item.setData(new TreeData(Editor.HTTPDIRECTORIES, config, Options.getHelpURL("http_directories"), "http_server"));
			item.setData("key", "http_server");
			item.setText("Http Directories");
			
			
		}
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.HOLIDAYS, config, Options.getHelpURL("holidays"), "holidays"));
		item.setData("key", "holidays");
		item.setText("Holidays");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.JOBS, config, Options.getHelpURL("jobs"), "jobs"));
		item.setData("key", "jobs");
		item.setText("Jobs");
		treeFillJobs(item);
		item.setExpanded(true);
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.JOB_CHAINS, config, Options.getHelpURL("job_chains"), "job_chains"));
		item.setData("key", "job_chains");
		item.setText("Job Chains");
		treeFillJobChains(item);
		
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.ORDERS, config, Options.getHelpURL("orders"), "orders"));
		item.setData("key", "orders");
		item.setText("Orders");
		treeFillOrders(item, true);
		
		item = new TreeItem(tree, SWT.NONE);
		item.setData(new TreeData(Editor.COMMANDS, config, Options.getHelpURL("commands"), "commands"));
		item.setData("key", "commands");
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
							item.setText("Order:" + e.getAttributeValue("id"));
							item.setData(new TreeData(Editor.ORDER, e, Options.getHelpURL("orders")));
							item.setData("key", "orders");
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
							item.setText("Order:" + e.getAttributeValue("id"));
							item.setData(new TreeData(Editor.ORDER, e, Options.getHelpURL("orders")));
							item.setData("key", "orders");
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
					i.setData("key", "job");
					
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
		
		if (parent.getText().equals("Jobs")) {
			
			for (int i = 0; i < parent.getItemCount(); i++)
				if (parent.getItem(i).getText().equals("Job: "+job)) {
					parent.getItem(i).setExpanded(true);
				}
			
		}
		
	}
	
	
	public void treeFillJob(TreeItem parent, Element job, boolean expand) {
		
		boolean disable = !Utils.isElementEnabled("job", _dom, job);
		
		parent.removeAll();
		
		
		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setText("Execute");
		item.setData(new TreeData(Editor.EXECUTE, job, Options.getHelpURL("job.execute")));
		item.setData("key", "job.execute");
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		
		item = new TreeItem(parent, SWT.NONE);
		item.setData(new TreeData(Editor.PARAMETER, job, Options.getHelpURL("parameter")));
		item.setData("key", "parameter");
		item.setText("Parameter");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		item = new TreeItem(parent, SWT.NONE);
		item.setText("Monitor");
		item.setData(new TreeData(Editor.MONITOR, job, Options.getHelpURL("job.monitor"), "monitor"));  
		item.setData("key", "monitor");
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		
		item = new TreeItem(parent, SWT.NONE);
		item.setText("Run Options");
		item.setData(new TreeData(Editor.OPTIONS, job, Options.getHelpURL("job.options")));
		item.setData("key", "job.options");
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		
		item = new TreeItem(parent, SWT.NONE);
		item.setText("Locks");
		item.setData(new TreeData(Editor.LOCKUSE, job, Options.getHelpURL("job.locks")));
		item.setData("key", "job.locks");
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		
		treeFillRunTimes(parent, job, disable, "run_time");
		
		List l = job.getChild("run_time").getChildren("month");     	
    	for(int i =0; i < l.size(); i++) {
    		Element e = (Element)l.get(i);
    		treeFillRunTimes(parent.getItem(parent.getItemCount()-1).getItem(parent.getItem(parent.getItemCount()-1).getItemCount()-1), e, !Utils.isElementEnabled("job", _dom, job), Utils.getAttributeValue("month", e));
    	}
		
		
		List commands = job.getChildren("commands");		
		item = new TreeItem(parent, SWT.NONE);
		item.setText("Commands");
		if(disable) {
			item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		
		if (commands != null)
			treeFillCommands(item, job, false);
		
		item.setData(new TreeData(Editor.JOB_COMMANDS, job, Options.getHelpURL("job.commands")));
		item.setData("key", "job.commands");
		parent.setExpanded(expand);
		
	}
	
	
	public void treeFillOrder(TreeItem parent, Element order, boolean expand) {
		parent.removeAll();

		//Element runtime = order.getChild("run_time");
		
		TreeItem item = new TreeItem(parent, SWT.NONE);
		item.setData(new TreeData(Editor.PARAMETER, order, Options.getHelpURL("parameter")));
		item.setData("key", "parameter");
		item.setText("Parameter");
		if(type == SchedulerDom.DIRECTORY)
			item.dispose();
		
		
		treeFillRunTimes(parent, order, false, "run_time");
		
		List l = order.getChild("run_time").getChildren("month"); 
    	for(int i =0; i < l.size(); i++) {
    		Element e = (Element)l.get(i);
    		treeFillRunTimes(parent.getItem(parent.getItemCount()-1).getItem(parent.getItem(parent.getItemCount()-1).getItemCount()-1), e, !Utils.isElementEnabled("job", _dom, order), Utils.getAttributeValue("month", e));    		
    	}
		
		parent.setExpanded(expand);
	}
	
	
	public void treeFillCommands(TreeItem parent, Element job, boolean expand) {
		new JobCommandListener(_dom, null, null).fillCommands(job, parent, expand);
	}
	
	
	public void treeFillDays(TreeItem parent, Element element, int type, boolean expand) {
		treeFillDays(parent, element, type, expand, null);		
	}
	
	public void treeFillDays(TreeItem parent, Element element, int type, boolean expand, String name) {
		if (element != null) { 
			if (type == DaysListener.WEEKDAYS || type == DaysListener.MONTHDAYS || type == DaysListener.ULTIMOS || type == DaysListener.SPECIFIC_MONTHS) {			
				new DaysListener(_dom, element, type).fillTreeDays(parent, expand);
                if(type == DaysListener.SPECIFIC_MONTHS) {
                	List l = element.getChildren("month"); 
                	//TreeItem item = _gui.getTree().getSelection()[0];
                	for(int i =0; i < l.size(); i++) {
                		Element e = (Element)l.get(i);
                		treeFillRunTimes(_gui.getTree().getSelection()[0], e, !Utils.isElementEnabled("job", _dom, element), Utils.getAttributeValue("month", e));                	
                	}
                	
                }
                
				
			} else if (type == 6 || type==4) {
				new DateListener(_dom, element, 1).fillTreeDays(parent, expand);
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
					i.setData("key", "job_chain");
					if(!Utils.isElementEnabled("job_chain", _dom, element)) {
						i.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					} else {
						i.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
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
						int type = data.getElement().getName().equals("job") ?  Editor.JOB : Editor.CONFIG ;
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
					case Editor.MONITOR:
						new ScriptForm(c, SWT.NONE, "Monitor", _dom, data.getElement(), data.getType());
						break;
					case Editor.SCRIPT:
						new ScriptForm(c, SWT.NONE, "Start Script", _dom, data.getElement(), data.getType());
						break;
					case Editor.JOB:
						new JobForm(c, SWT.NONE, _dom, data.getElement(), _gui);
						break;
					case Editor.EXECUTE:
						new ExecuteForm(c, SWT.NONE, _dom, data.getElement());
						break;
					case Editor.ORDERS:
						//new OrdersForm(c, SWT.NONE, _dom, _gui, this);
						new OrdersForm(c, SWT.NONE, _dom, _gui);
						break;
					case Editor.ORDER:
						new OrderForm(c, SWT.NONE, _dom, data.getElement(), _gui);
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
						new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.WEEKDAYS);
						break;
					case Editor.MONTHDAYS:
						new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.MONTHDAYS);
						break;
					case Editor.SPECIFIC_MONTHS:
						new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.SPECIFIC_MONTHS);
						break;
					case Editor.ULTIMOS:
						new DaysForm(c, SWT.NONE, _dom, data.getElement(), _gui, DaysListener.ULTIMOS);
						break;
					case Editor.EVERYDAY:
					case Editor.PERIODS:
						new PeriodsForm(c, SWT.NONE, _dom, data.getElement());
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
						new WebservicesForm(c, SWT.NONE, _dom, data.getElement());
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
					case Editor.COMMANDS:
						new CommandsForm(c, SWT.NONE, _dom, _gui);
						break;
					default:
						System.out.println("no form found for " + item.getText());
					}
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
	
	private void checkLifeAttributes(Element element, String name) {
		if(element.getName().equals("add_order") || element.getName().equals("order")) {
			/*if(Utils.getAttributeValue("id", element).length() == 0 
			 || Utils.getAttributeValue("job_chain", element).length() == 0) {
			 ArrayList l = new ArrayList();
			 l.add(name);
			 _dom.setListOfEmptyElementNames(l);
			 } else */ 
			if((Utils.getAttributeValue("job_chain", element) + "," + Utils.getAttributeValue("id", element)).equalsIgnoreCase(name)) {
				//Attribut name ist ungleich der Dateiname
				ArrayList l = new ArrayList();
				l.add(element.getName() + "_" + name);
				_dom.setListOfChangeElementNames(l);
				
			}
			
			
			
		} else {
			/*if(Utils.getAttributeValue("name", element).length() == 0) {
			 //Attribut name ist leer	
			  ArrayList l = new ArrayList();
			  l.add(name);
			  _dom.setListOfEmptyElementNames(l);
			  
			  } else */
			if(Utils.getAttributeValue("name", element).length() > 0 && 
					!Utils.getAttributeValue("name", element).equalsIgnoreCase(name)) {
				//Attribut name ist ungleich der Dateiname
				ArrayList l = new ArrayList();
				l.add(element.getName() + "_" + name);
				_dom.setListOfChangeElementNames(l);
				_dom.setChanged(true);
			}
			if(element.getName().equals("job")) {
				element.removeAttribute("spooler_id");
				_dom.setChanged(true);
			}
		}
	}
	
	public void treeFillRunTimes(TreeItem item,Element job, boolean disable, String run_time) {
		Element runtime = null; 
		Element _runtime = job.getChild("run_time");
		
		//Element runtime = job.getChild(run_time);				
		//Utils.getElementAsString(job.getParentElement().getParentElement())
		// create runtime tag
		if (_runtime == null && run_time.equals("run_time")) { 			
			_runtime = new Element(run_time);
			job.addContent(_runtime);	
			runtime=_runtime;
		}  else if(!run_time.equals("run_time")){
			_runtime = job.getParentElement().getParentElement().getChild("run_time");			
		} else {
			runtime=_runtime;
		}
		
		//Specific months: create month child tags (type=3)
		if(!run_time.equals("run_time")) {
			
			List _monthList = _runtime.getChildren("month");
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
				_runtime.addContent(newMonth);
				job = newMonth;
				runtime=job;
				
			}
			
		}
		
		//runtime=_runtime;
		
		if (runtime != null) {
			TreeItem run = new TreeItem(item, SWT.NONE);
			run.setText(run_time);
			if(run_time.equals("run_time")) {
				run.setText("Run Time");								
			} else {
				run.setText(run_time);
			}
			
			if(run_time.equals("run_time")) {
				run.setData(new TreeData(Editor.RUNTIME, job, Options.getHelpURL("job.run_time"), "run_time"));
				run.setData("key", "run_time");
				if(disable) {
					run.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
			}
			
			item = new TreeItem(run, SWT.NONE);
			item.setText("Everyday");
			item.setData(new TreeData(Editor.EVERYDAY, runtime, Options.getHelpURL("job.run_time.everyday")));
			item.setData("key", "job.run_time.everyday");
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			item = new TreeItem(run, SWT.NONE);
			item.setText("Weekdays");
			item.setData(new TreeData(Editor.WEEKDAYS, runtime, Options.getHelpURL("job.run_time.weekdays"),"weekdays"));
			item.setData("key", "job.run_time.weekdays");
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			
			treeFillDays(item, runtime, 0, false);
			
			item = new TreeItem(run, SWT.NONE);
			item.setText("Monthdays");
			item.setData(new TreeData(Editor.MONTHDAYS, runtime, Options.getHelpURL("job.run_time.monthdays"),"monthdays"));
			item.setData("key", "job.run_time.monthdays");
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillDays(item, runtime, 1, false);
			
			item = new TreeItem(run, SWT.NONE);
			item.setText("Ultimos");
			item.setData(new TreeData(Editor.ULTIMOS, runtime, Options.getHelpURL("job.run_time.ultimos"), "ultimos"));
			item.setData("key", "job.run_time.ultimos");
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillDays(item, runtime, 2, false);
			
			
			item = new TreeItem(run, SWT.NONE);
			item.setText("Specific Weekdays");
			item.setData(new TreeData(Editor.SPECIFIC_WEEKDAYS, runtime, Options.getHelpURL("job.run_time.monthdays"), "monthdays"));
			item.setData("key", "job.run_time.monthdays");
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			treeFillSpecificWeekdays(item, runtime, false);
			
			item = new TreeItem(run, SWT.NONE);
			item.setText("Specific Days");
			item.setData(new TreeData(Editor.DAYS, runtime, Options.getHelpURL("job.run_time.specific_days")));
			item.setData("key", "job.run_time.specific_days");
			if(disable) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			}
			
			//Specific Monthdays
			if(run_time.equals("run_time")) {
				item = new TreeItem(run, SWT.NONE);
				item.setText("Specific Month");
				//item.setData(new TreeData(Editor.SPECIFIC_MONTHS, runtime, Options.getHelpURL("job.run_time.monthdays"),"specific_monthdays"));
				item.setData(new TreeData(Editor.SPECIFIC_MONTHS, runtime, Options.getHelpURL("job.run_time.monthdays"),"month"));
				item.setData("key", "job.run_time.specific_monthdays");				
				if(disable) {
					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
				treeFillDays(item, runtime, 1, false);
				
				
			}
			
			if (runtime != null)
				treeFillDays(item, runtime, 6, false);
		}
		
	}
}
