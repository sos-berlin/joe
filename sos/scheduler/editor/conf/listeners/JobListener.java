package sos.scheduler.editor.conf.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.app.Options;

public class JobListener {
	private ISchedulerUpdate _main;
	
	private SchedulerDom     _dom;
	
	private Element          _job;
	
	private List             _params;
	
	//Hifsvariable
	private static String library      = "";  
	
	private static HashMap parameterDescription = new HashMap();
	
	private static HashMap parameterRequired = new HashMap();
	
	
	
	public JobListener(SchedulerDom dom, Element job, ISchedulerUpdate update) {
		_dom = dom;
		_job = job;
		_main = update;
		Element params = _job.getChild("params");
		if (params != null)
			_params = params.getChildren("param");
	}
	
	
	private void initParams() {
		_job.addContent(0, new Element("params"));
		_params = _job.getChild("params").getChildren();
	}
	
	
	public String getComment() {
		return Utils.getAttributeValue("__comment__", _job);
	}
	
	
	public void setComment(String comment) {
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		Utils.setAttribute("__comment__", comment, _job, _dom);
	}
	
	
	public boolean isDisabled() {
		return _dom.isJobDisabled(Utils.getAttributeValue("name", _job));
	}
	
	
	public String getName() {
		return Utils.getAttributeValue("name", _job);
	}
	
	
	public void setName(String name, boolean updateTree) {	
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.DELETE);
		Utils.setAttribute("name", name, _job, _dom);
		if (updateTree)
			_main.updateJob(name);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public String getTitle() {
		return Utils.getAttributeValue("title", _job);
	}
	
	
	public void setTitle(String title) {
		Utils.setAttribute("title", title, _job, _dom);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public String getSpoolerID() {
		return Utils.getAttributeValue("spooler_id", _job);
	}
	
	
	public void setSpoolerID(String spoolerID) {
		Utils.setAttribute("spooler_id", spoolerID, _job, _dom);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public String getProcessClass() {
		return Utils.getAttributeValue("process_class", _job);
	}
	
	
	public void setProcessClass(String processClass) {
		Utils.setAttribute("process_class", processClass, _job, _dom);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public boolean getOrder() {
		String order = _job.getAttributeValue("order");
		return order == null ? false : order.equalsIgnoreCase("yes");
	}
	
	public boolean getStopOnError() {
		String stopOnError = _job.getAttributeValue("stop_on_error");
		return stopOnError == null ? true : stopOnError.equalsIgnoreCase("yes");
	}
	
	public boolean getForceIdletimeout() {
		String forceIdleTimeout = _job.getAttributeValue("force_idle_timeout");
		return forceIdleTimeout == null ? false : forceIdleTimeout.equalsIgnoreCase("yes");
	}
	
	
	public void setOrder(boolean order) {
		if (order) {
			_job.setAttribute("order", "yes");
			_job.removeAttribute("priority");
			if (_job.getChild("run_time") == null)
				_job.addContent(new Element("run_time").setAttribute("let_run", "no"));
		} else {
			_job.removeAttribute("order");
		}
		// _main.updateJob();
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public String getPriority() {
		
		return Utils.getAttributeValue("priority", _job);
	}
	
	
	public void setPriority(String priority) {
		Utils.setAttribute("priority", priority, _job, _dom);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public String getTasks() {
		return Utils.getAttributeValue("tasks", _job);
	}
	
	
	public void setTasks(String tasks) {
		Utils.setAttribute("tasks", Utils.getIntegerAsString(Utils.str2int(tasks)), _job, _dom);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public String getTimeout() {
		return Utils.getAttributeValue("timeout", _job);
	}
	
	
	public void setTimeout(String timeout) {
		Utils.setAttribute("timeout", Utils.getIntegerAsString(Utils.str2int(timeout)), _job, _dom);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public String getIdleTimeout() {
		return Utils.getAttributeValue("idle_timeout", _job);
	}
	
	
	public void setIdleTimeout(String idleTimeout) {
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		Utils.setAttribute("idle_timeout", Utils.getIntegerAsString(Utils.str2int(idleTimeout)), _job, _dom);
	}
	
	
	public void setForceIdletimeout(boolean forceIdleTimeout) {
		if (forceIdleTimeout) {
			Utils.setAttribute("force_idle_timeout", "yes", _job, _dom);
		} else {
			Utils.setAttribute("force_idle_timeout", "no", _job, _dom);
		}
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	public void setStopOnError(boolean stopOnError) {
		if (stopOnError) {
			Utils.setAttribute("stop_on_error", "yes", _job, _dom);
		} else {
			Utils.setAttribute("stop_on_error", "no", _job, _dom);
		}
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	public void setMintasks(String mintasks) {
		Utils.setAttribute("min_tasks", Utils.getIntegerAsString(Utils.str2int(mintasks)), _job, _dom);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	public String getMintasks() {
		return Utils.getAttributeValue("min_tasks", _job);
	}
	
	
	public String[] getProcessClasses() {
		String[] names = null;
		Element classes = _dom.getRoot().getChild("config").getChild("process_classes");
		if (classes != null) {
			List list = classes.getChildren("process_class");
			names = new String[list.size()];
			int i = 0;
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					String name = ((Element) o).getAttributeValue("name");
					if (name == null)
						name = "";
					names[i++] = name;
				}
			}
		}
		return names;
	}
	
	
	public void fillParams(Table table) {
		if (_params != null) {
			Iterator it = _params.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {					
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, ((Element) o).getAttributeValue("name"));
					item.setText(1, (((Element) o).getAttributeValue("value") != null ? ((Element) o).getAttributeValue("value") : ""));
					if(parameterRequired != null && isParameterRequired(((Element) o).getAttributeValue("name")))
						item.setBackground(Options.getRequiredColor());
				}
			}						
		}		
		_main.updateJob();
	} 
	
	public void fillParams(ArrayList listOfParams, Table table, boolean refreshTable) {
		boolean existParam = false;
		
		if(refreshTable) {
			if(_params!=null)
				_params.clear();		
			table.removeAll();
		}
		
		for (int i =0; i < listOfParams.size(); i++) {
			HashMap h = (HashMap)listOfParams.get(i);                
			if (h.get("name") != null) {
				TableItem item = existsParams(h.get("name").toString(), table, (h.get("default_value") != null? h.get("default_value").toString(): ""));
				if(!refreshTable && item != null) {					
					if(h.get("required") != null && h.get("required").equals("true"))
						item.setBackground(Options.getRequiredColor());
					
					existParam = true;
				} else {
					String pname = h.get("name").toString();
					String pvalue = (h.get("default_value") != null ? h.get("default_value").toString() : "");
					String desc = (h.get("description") != null ? h.get("description").toString() : ""); 
					saveParameter(table, pname, pvalue, desc, (h.get("required")!=null ? h.get("required").equals("true"):false));					
				}
			}           
		}
						
	}
	
	public TableItem existsParams(String name, Table table, String replaceValue) {
		
		try {
			for (int i =0; i < table.getItemCount(); i++) {
				if(table.getItem(i).getText(0).equals(name)) {
					/*if(table.getItem(i).getText(1) == null || table.getItem(i).getText(1).length() == 0) {
						if(replaceValue != null && replaceValue.length() > 0) {
							table.getItem(i).setText(1, replaceValue);
							saveParameter(table, table.getItem(i).getText(0), replaceValue);
						}
					}*/
					return table.getItem(i);
				}
			}
		} catch (Exception e) {
			System.out.println("error in JobListener.existsParams " + e.getMessage());
		}
		return null;
	}
	
	public void deleteParameter(Table table, int index) {
		
		
		if (_params != null) {
			_params.remove(index);
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);			
		}
		table.remove(index);
		
	}					
	
	public void saveParameter(Table table, String name, String value, String parameterDescription, boolean required) {
		Element e = new Element("param");
		e.setAttribute("name", name);
		e.setAttribute("value", value);
		
		
		if (_params == null)
			initParams();
		_params.add(e);
		
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { name, value });
		if(parameterDescription!=null && parameterDescription.trim().length()>0) {
			item.setData(parameterDescription);
		}
		if(required) {
			item.setBackground(Options.getRequiredColor());
		}
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public void saveParameter(Table table, String name, String value) {
		
		boolean found = false;
		String value2 = value.replaceAll("\"", "&quot;");
		
		//test
		//_params = _job.getChild("params").getChildren("param");
		//
		if (_params != null) {
			int index = 0;
			Iterator it = _params.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					if (name.equals(e.getAttributeValue("name"))) {
						found = true;
						Utils.setAttribute("value", value2, e);
						//e.setAttribute("value", value2);						
						_dom.setChanged(true);
						_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
						table.getItem(index).setText(1, value);
						break;
					}
					index++;
				}
			}
		}
		if (!found) {
			Element e = new Element("param");
			e.setAttribute("name", name);
			e.setAttribute("value", value2);
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
			if (_params == null)
				initParams();
			_params.add(e);
			
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { name, value });
			if(parameterRequired != null && isParameterRequired(name))
				item.setBackground(Options.getRequiredColor());
		}		
		
	}
	
	private void updateBackGroundColor(Table table, String currParamname) {
		for (int i = 0; i < table.getItemCount(); i++) {
			if(table.getItem(i).getText(0).equals(currParamname)) {
				table.getItem(i).setBackground(Options.getWhiteColor());    			
			}
		}
	}
	
	public String getDescription() {
		Element desc = _job.getChild("description");
		if (desc != null) {
			return desc.getTextTrim();
		} else
			return "";
	}
	
	
	public void setDescription(String description) {
		Element desc = _job.getChild("description");
		String f = getInclude();
		
		if (desc == null && !description.equals("")) {
			desc = new Element("description");
			_job.addContent(0, desc);
		}
		
		if (desc != null) {
			if (description.equals("") && (f == null || f.equals(""))) {
				_job.removeChild("description");
				_dom.setChanged(true);
				_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
				return;
			}
			
			desc.removeContent();
			if (!(f == null || f.equals(""))) {
				setInclude(f);
			}
			desc.addContent(new CDATA(description));
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		}
	}
	
	
	public String getInclude() {
		
		Element desc = _job.getChild("description");
		if (desc != null) {
			Element inc = desc.getChild("include");
			if (inc != null)
				return inc.getAttributeValue("file");
		}
		return "";
	}
	
	
	public void setInclude(String file) {
		Element desc = _job.getChild("description");
		if (desc == null && !file.equals("")) {
			desc = new Element("description");
			_job.addContent(desc);
		}
		
		if (desc != null) {
			if (!file.equals("")) {
				Element incl = desc.getChild("include");
				if (incl == null)
					desc.addContent(0, new Element("include").setAttribute("file", file));
				else
					incl.setAttribute("file", file);
				
			} else {
				desc.removeChild("include");
				if (getDescription().equals(""))
					_job.removeChild("description");
			}
			
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		}
	}
	
	public String getIgnoreSignal() {
		return Utils.getAttributeValue("ignore_signals", _job);
	}
	
	
	public void setIgnoreSignal(String signals) {
		Utils.setAttribute("ignore_signals", signals, _job, _dom);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}
	
	
	public SchedulerDom get_dom() {
		return _dom;
	}
	
	
	public ISchedulerUpdate get_main() {
		return _main;
	}
	
	
	public static String getLibrary() {
		return library;
	}
	
	
	public static void setLibrary(String library) {
		JobListener.library = library;
	}
	
	
	public Element getJob() {
		return _job;
	}    
	
	public void getAllParameterDescription() {
		String xmlPaths = sos.scheduler.editor.app.Options.getSchedulerHome() ;
		xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? xmlPaths.concat(getInclude()) : xmlPaths.concat("/").concat(getInclude()));		
		
		try {
			
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build( new File( xmlPaths ) );
			Element root = doc.getRootElement();				
			Element config = root.getChild("configuration", root.getNamespace());
			Element params = config.getChild("params", config.getNamespace());
			if(params == null)
				return;
			List listMainElements = params.getChildren("param", params.getNamespace());
			for( int i=0; i<listMainElements.size(); i++ ){					
				Element elMain  = (Element)(listMainElements.get( i ));					
				if(elMain.getName().equalsIgnoreCase("param")) { 
					Element note = elMain.getChild("note", elMain.getNamespace());
					if(note != null) {
						List notelist = note.getChildren();
						for (int j = 0; j < notelist.size(); j++) {
							Element elNote  = (Element)(notelist.get( j ));							
							parameterDescription.put( elMain.getAttributeValue("name"), elNote.getText());
							if(elMain.getAttributeValue("required") != null)
								parameterRequired.put( elMain.getAttributeValue("name"), elMain.getAttributeValue("required"));
						}
					}																
				}				
			}
			
		} catch( Exception ex ) {			
			ex.printStackTrace();
		}		
	}
	/**
	 * Note/Beschreibung der Parameter
	 * @param name
	 * @return
	 */
	public String getParameterDescription(String name) {
		return (parameterDescription.get(name) != null ? parameterDescription.get(name).toString() : "");
	}
	
	private boolean isParameterRequired(String name) {
		String _isIt = (parameterRequired.get(name) != null ? parameterRequired.get(name).toString() : "");
		if(_isIt.equals("true")) { 
			return ( true);
		} else {
			return false;
		}
	}
	
}
