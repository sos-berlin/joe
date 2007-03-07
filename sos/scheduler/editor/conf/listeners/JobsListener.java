package sos.scheduler.editor.conf.listeners;
 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Comment;
import org.jdom.Element;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.forms.JobAssistentForm;
import sos.scheduler.editor.conf.forms.JobsForm;
import sos.scheduler.editor.conf.forms.ShowAllImportJobsForm;
//import sos.util.SOSClassUtil;
//import sos.util.SOSString;
 
public class JobsListener { 

    private SchedulerDom     _dom;

    private ISchedulerUpdate _main;

    private Element          _config;

    private Element          _jobs;

    private List             _list;
    
//    private SOSString        sosString = new SOSString();


    public JobsListener(SchedulerDom dom, ISchedulerUpdate update) {
        _dom = dom;
        _main = update;
        _config = _dom.getRoot().getChild("config");
        _jobs = _config.getChild("jobs");
        if (_jobs != null)
            _list = _jobs.getChildren("job");
    }


    private void initJobs() {
        if (_config.getChild("jobs") == null) {
            Element _jobs = new Element("jobs");
            _config.addContent(_jobs);
            _list = _jobs.getChildren("job");
        } else {
            _jobs = _config.getChild("jobs");
            _list = _jobs.getChildren("job");
        }
    }


    public void fillTable(Table table) {
        table.removeAll();
        if (_list != null) {
            for (Iterator it = _list.iterator(); it.hasNext();) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setData(e);
                    String name = Utils.getAttributeValue("name", e);

                    item.setChecked(_dom.isJobDisabled(name));
                    item.setText(1, name);
                    item.setText(2, Utils.getAttributeValue("title", e));
                    item.setText(3, Utils.getAttributeValue("spooler_id", e));
                    item.setText(4, Utils.getAttributeValue("process_class", e));
                    item.setText(5, Utils.isAttributeValue("order", e) ? "Yes" : "No");
                }
            }
        }
    }


    public void newJob(Table table) {
        Element job = new Element("job");
        job.setAttribute("name", "job" + (table.getItemCount() + 1));
        Element runtime = new Element("run_time");
        runtime.setAttribute("let_run", "no");
        if (_list == null)
            initJobs();
        _list.add(job.addContent(runtime));
        _dom.setChanged(true);
        fillTable(table);
        table.setSelection(table.getItemCount() - 1);
        _main.updateJobs();
    }
 

    public Element createJobElement(java.util.HashMap attr) {
    	Element job = new Element("job");
    	 
//      config Job Attribute bilden
        if(attr.get("name") != null && attr.get("name").toString().length() > 0) 
        	Utils.setAttribute("name", attr.get("name").toString(), job);       
        
        if(attr.get("title") != null && attr.get("title").toString().length() > 0)
        	Utils.setAttribute("title", attr.get("title").toString(), job);
        
        if(attr.get("order") != null && attr.get("order").toString().length() > 0)
        	Utils.setAttribute("order", attr.get("order").toString(), job);
        
        if(attr.get("tasks") != null && attr.get("tasks").toString().length() > 0)
        	Utils.setAttribute("tasks", attr.get("tasks").toString(), job);
        
        /*job.setAttribute("title", (attr.get("title") != null ? attr.get("title").toString() : ""));                
        job.setAttribute("order", (attr.get("order") != null ? attr.get("order").toString() : ""));
        job.setAttribute("tasks", (attr.get("tasks") != null && !attr.get("tasks").equals("unbounded")? attr.get("tasks").toString() : ""));
        */
        //Element Parameters und entsprechende Kindknoten param bils
        Element params = new Element("params");
        job.addContent(0, params);
        ArrayList listOfParams = (ArrayList)attr.get("params");        
        //Parameter werden aus der Jobbeschreibung geholt 
        //config Parameters bilden
        Element param = null;
        for (int i = 0; i < listOfParams.size(); i++ ) {
        	HashMap p = (HashMap)listOfParams.get(i);
        	param = new Element("param");
        	/*if(p.get("name") != null && p.get("name").toString().length() > 0) {
                Utils.setAttribute("name", p.get("name").toString(), param);        	
        		Utils.setAttribute("value", (p.get("default_value") != null ? p.get("default_value").toString() :""), param);
        	}*/        	
        	param.setAttribute("name", (p.get("name") != null ? p.get("name").toString() :""));        
        	param.setAttribute("value", (p.get("default_value") != null ? p.get("default_value").toString() :""));
        	//Es gibt hier noch weiter Informationen wie die Parameter Beschreibung und ob required, sollen diese auch übergeben werden?
        	params.addContent(param);
        }
                
        //config Description Element bilden
        if( attr.get("filename") != null && attr.get("filename").toString().length() > 0) {
        	Element desc = new Element("description");
        	Element include = new Element ("include");
        	Utils.setAttribute("file", attr.get("filename").toString(), include);
        	//include.setAttribute("file", attr.get("filename").toString()); 
        	desc.addContent(include);
        	job.addContent(desc);
        }

        //script Element bilden  
        if(attr.get("script") != null && attr.get("script").toString().equals("script")) {
        	Element script = new Element("script");        	
        	if(attr.get("script_language") != null && attr.get("script_language").toString().length() > 0)
        		Utils.setAttribute("language", attr.get("script_language").toString(), script);
        		//script.setAttribute("language", attr.get("script_language").toString());
        	
        	if(attr.get("script_java_class") != null && attr.get("script_java_class").toString().length() > 0)
        		Utils.setAttribute("java_class", attr.get("script_java_class").toString(), script);
        		//script.setAttribute("java_class", attr.get("script_java_class").toString()); 
        	
        	if(attr.get("script_com_class") != null && attr.get("script_com_class").toString().length() > 0)
        		Utils.setAttribute("com_class", attr.get("script_com_class").toString(), script);
        		//script.setAttribute("com_class", attr.get("script_com_class").toString()); 
        	
        	if(attr.get("script_filename") != null && attr.get("script_filename").toString().length() > 0)
        		Utils.setAttribute("filename", attr.get("script_filename").toString(), script);
        		//script.setAttribute("filename", attr.get("script_filename").toString()); 
        	
        	if(attr.get("script_use_engine") != null && attr.get("script_use_engine").toString().length() > 0)
        		Utils.setAttribute("use_engine", attr.get("script_use_engine").toString(), script);
        		//script.setAttribute("use_engine", attr.get("script_use_engine").toString()); 
        	
        	if(attr.get("script_include_file") != null) {
        		ArrayList listOfIncludes = (ArrayList)attr.get("script_include_file");
        		for (int i = 0; i < listOfIncludes.size(); i++) {
        			if(listOfIncludes.get(i) != null) {
        				Element include = new Element("include");
        				include.setNamespace(this._dom.getNamespace());
        				Utils.setAttribute("file", listOfIncludes.get(i).toString(), include);
        				//include.setAttribute("file", listOfIncludes.get(i).toString());
        				script.addContent(include);
        			}
        		}
        		
        	}      
        	if(script.getAttributes().size()> 0)
        		job.addContent(script);
        }  
        
        if(attr.get("monitor") != null && attr.get("monitor").toString().equals("monitor")) {
        	//monitor element bilden        
        	Element monitor = new Element("monitor");
        	
        	Element mon_script = new Element("script");
        	
        	if(attr.get("monitor_script_language") != null && attr.get("monitor_script_language").toString().length() > 0) 
        		Utils.setAttribute("language", attr.get("monitor_script_language").toString(), mon_script);
        		//mon_script.setAttribute("language", attr.get("monitor_script_language").toString());
        	
        	if(attr.get("monitor_script_java_class") != null && attr.get("monitor_script_java_class").toString().length() > 0)
        		Utils.setAttribute("java_class", attr.get("monitor_script_java_class").toString(), mon_script);
        		//mon_script.setAttribute("java_class", attr.get("monitor_script_java_class").toString());
        	
        	if(attr.get("monitor_script_com_class") != null && attr.get("monitor_script_com_class").toString().length() > 0)
        		Utils.setAttribute("com_class", attr.get("monitor_script_com_class").toString(), mon_script);
        		//mon_script.setAttribute("com_class", attr.get("monitor_script_com_class").toString());
        	
        	if(attr.get("monitor_script_filename") != null && attr.get("monitor_script_filename").toString().length() > 0)
        		Utils.setAttribute("filename", attr.get("monitor_script_filename").toString(), mon_script);
        		//mon_script.setAttribute("filename", attr.get("monitor_script_filename").toString());
        	
        	if(attr.get("monitor_script_use_engine") != null && attr.get("monitor_script_use_engine").toString().length() > 0)
        		Utils.setAttribute("use_engine", attr.get("monitor_script_use_engine").toString(), mon_script);
        		//mon_script.setAttribute("use_engine", attr.get("monitor_script_use_engine").toString());
        	
        	if(attr.get("monitor_script_include_file") != null) {
        		ArrayList listOfMonIncludes = (ArrayList)attr.get("monitor_script_include_file");
        		for (int i = 0; i < listOfMonIncludes.size(); i++) {
        			Element mon_include = new Element("include");
        			mon_include.setNamespace(this._dom.getNamespace());
        			//mon_include.setAttribute("file", listOfMonIncludes.get(i).toString());
        			Utils.setAttribute("file", listOfMonIncludes.get(i).toString(), mon_include);
        			mon_script.addContent(mon_include);
        		}
        	}
        	
        	monitor.addContent(mon_script);
        	
        	if(mon_script.getAttributes().size()> 0)
        		job.addContent(monitor);
        }
                
        if(attr.get("process") != null && attr.get("process").toString().equals("process")) {
        	Element process = new Element("process");
        	
        	if(attr.get("process_file") != null && attr.get("process_file").toString().length() > 0)
        		Utils.setAttribute("file", attr.get("process_file").toString(), process);
        	
        	if(attr.get("process_log") != null && attr.get("process_log").toString().length() > 0)
        		Utils.setAttribute("log_file", attr.get("process_log").toString(), process);
        	
        	if(attr.get("process_param") != null && attr.get("process_param").toString().length() > 0)
        		Utils.setAttribute("param", attr.get("process_param").toString(), process);
        	
        	if(attr.get("process_environment") != null) { 
        		Element environment = new Element("environment");
        		ArrayList listOfEnv = (ArrayList)attr.get("process_environment");
        		for (int i =0; i < listOfEnv.size(); i++) {
        			HashMap h = (HashMap)listOfEnv.get(i);
        			if(h.get("name")!= null) {
        				Element variable = new Element("variable");
        				variable.setAttribute("name", h.get("name").toString());
        				variable.setAttribute("value", (h.get("value") != null ? h.get("value").toString() : ""));
        				environment.addContent(variable);
        			}
        		}
        	
        		process.addContent(environment);
        	}
        	job.addContent(process);
        	
        }
        
       
        //runtime
        Element runtime = new Element("run_time");
        runtime.setAttribute("let_run", "no");
        job.addContent(runtime);
        return job;
    }
    
    public void newImportJob(java.util.HashMap attr) {
        
        Element job = createJobElement(attr);
        if (_list == null)
            initJobs();
        
        _list.add(job);
        _dom.setChanged(true);
        fillTable(JobsForm.getTable());
        JobsForm.getTable().setSelection(JobsForm.getTable().getItemCount() - 1);
        _main.updateJobs();        
    }

public void newImportJob(Element job) {
                
        if (_list == null)
            initJobs();
        
        _list.add(job);
        _dom.setChanged(true);
        fillTable(JobsForm.getTable());
        JobsForm.getTable().setSelection(JobsForm.getTable().getItemCount() - 1);
        _main.updateJobs();        
    }

    
    public boolean deleteJob(Table table) {
        int index = table.getSelectionIndex();
        if (index >= 0) {
            TableItem item = table.getItem(index);
            Element e = (Element) item.getData();
            _dom.setJobDisabled(Utils.getAttributeValue("name", e), false);
            e.detach();
            _dom.setChanged(true);
            table.remove(index);
            _main.updateJobs();

            if (_list.size() == 0) {
                _config.removeChild("jobs");
                _jobs = null;
                _list = null;
            }

            if (index >= table.getItemCount())
                index--;
            if (index >= 0) {
                table.setSelection(index);
                return true;
            }
        }
        return false;
    }


    public void setJobDisabled(String name, boolean disabled) {
        _dom.setJobDisabled(name, disabled);
        _main.updateJobs();
    }


    public boolean hasJobComment(Element e) {
        if (e != null) {
            for (Iterator it = e.getContent().iterator(); it.hasNext();) {
                Object o = it.next();
                if (o instanceof Comment && false)
                    return true;
                else if (o instanceof Element) {
                    Element ee = (Element) o;
                    if (!Utils.getAttributeValue("__comment__", ee).equals(""))
                        return true;
                    if (hasJobComment(ee))
                        return true;
                }

            }
        }
        return false;
    }
       
    
    public void openImportJobs(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate update) throws Exception {    
    	ShowAllImportJobsForm iDialog = null;    	
    	
    	try {
    	  iDialog = new ShowAllImportJobsForm (dom, update);//(parent.getShell(), style);
    	  iDialog.showAllImportJobs();
    	  
    	   	  
    	} catch (Exception e) {
    		throw new Exception ("error in JobsListener.openImportJobs " + e.getMessage());
    	}
      
    }
    
    public void startJobAssistent(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate update) throws Exception {    
    	    	
    	
    	try {
    		JobAssistentForm assitent = new JobAssistentForm(dom, update);
    		assitent.startJobAssistant();
    	   	  
    	} catch (Exception e) {
    		throw new Exception ("error in JobsListener.startJobAssistent() " + e.getMessage());
    	}
      
    }
    
    
    
}
