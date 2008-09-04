package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.jdom.CDATA;
import org.jdom.Element;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;

public class JobListener {

	
	private          ISchedulerUpdate   _main      = null;

	private          SchedulerDom       _dom       = null;

	private          Element            _job       = null;
	//Hifsvariable
	private static   String             library    = "";  


	public JobListener(SchedulerDom dom, Element job, ISchedulerUpdate update) {
		
		_dom = dom;
		_job = job;
		_main = update;

	}

	public String getComment() {
		return Utils.getAttributeValue("__comment__", _job);
	}


	public void setComment(String comment) {		
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		Utils.setAttribute("__comment__", comment, _job, _dom);
	}


	public boolean isDisabled() {
		return _dom.isJobDisabled(Utils.getAttributeValue("name", _job));
	}


	public String getName() {		
		return Utils.getAttributeValue("name", _job);
	}


	public void setName(String name, boolean updateTree) {
		boolean isDis = isDisabled();
		
		if(isDis) //ändern der Jobname der disabled ist: alte jobname aus der disable Liste löschen und neue hinzufügen
			_dom.setJobDisabled(Utils.getAttributeValue("name",_job), false);

		_dom.setJobDisabled(name, isDis);		


		String removename = Utils.getAttributeValue("name", _job);
		Utils.setAttribute("name", name, _job, _dom);		
		if(_dom.isChanged() && ((_dom.isDirectory() && !Utils.existName(removename, _job, "job"))  || _dom.isLifeElement())) 
			_dom.setChangedForDirectory("job", removename , SchedulerDom.DELETE);		

		if (updateTree)
			_main.updateJob(name);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		
	}


	public String getTitle() {
		return Utils.getAttributeValue("title", _job);
	}


	public void setTitle(String title) {
		Utils.setAttribute("title", title, _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}


	public String getSpoolerID() {
		return Utils.getAttributeValue("spooler_id", _job);
	}


	public void setSpoolerID(String spoolerID) {
		Utils.setAttribute("spooler_id", spoolerID, _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}


	public String getProcessClass() {
		return Utils.getAttributeValue("process_class", _job);
	}


	public void setProcessClass(String processClass) {
		Utils.setAttribute("process_class", processClass, _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
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
		_dom.setChanged(true);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}


	public String getPriority() {	
		return Utils.getAttributeValue("priority", _job);
	}


	public void setPriority(String priority) {
		Utils.setAttribute("priority", priority, _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public String getJavaOptions() {	
		return Utils.getAttributeValue("java_options", _job);
	}


	public void setJavaOptions(String javaOptions) {
		Utils.setAttribute("java_options", javaOptions, _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}



	public String getTasks() {
		return Utils.getAttributeValue("tasks", _job);
	}


	public void setTasks(String tasks) {
		Utils.setAttribute("tasks", Utils.getIntegerAsString(Utils.str2int(tasks)), _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}


	public String getTimeout() {
		return Utils.getAttributeValue("timeout", _job);
	}


	public void setTimeout(String timeout) {
		Utils.setAttribute("timeout", Utils.getIntegerAsString(Utils.str2int(timeout)), _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}


	public String getIdleTimeout() {
		return Utils.getAttributeValue("idle_timeout", _job);
	}


	public void setIdleTimeout(String idleTimeout) {
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		Utils.setAttribute("idle_timeout", Utils.getIntegerAsString(Utils.str2int(idleTimeout)), _job, _dom);
	}


	public void setForceIdletimeout(boolean forceIdleTimeout) {
		if (forceIdleTimeout) {
			Utils.setAttribute("force_idle_timeout", "yes", _job, _dom);
		} else {
			Utils.setAttribute("force_idle_timeout", "no", "no", _job, _dom);
		}
		if(_dom.isDirectory() || _dom.isLifeElement()) 
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void setStopOnError(boolean stopOnError) {
		if (stopOnError) {
			Utils.setAttribute("stop_on_error", "yes", "yes", _job, _dom);
		} else {
			Utils.setAttribute("stop_on_error", "no", _job, _dom);
		}
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void setReplace(boolean replace) {
		if (replace) {
			Utils.setAttribute("replace", "yes", "yes", _job, _dom);
		} else {
			Utils.setAttribute("replace", "no", "yes",_job, _dom);
		}
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public boolean getReplace() {
		String replace = _job.getAttributeValue("replace");
		return replace == null ? true : replace.equalsIgnoreCase("yes");
	}


	public void setTemporary(boolean temporary) {
		if (temporary) {
			Utils.setAttribute("temporary", "yes", "no", _job, _dom);
		} else {
			Utils.setAttribute("temporary", "no", "no",_job, _dom);
		}
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public boolean getTemporary() {
		String temporary = _job.getAttributeValue("temporary");
		return temporary == null ? false : temporary.equalsIgnoreCase("yes");

	}

	public void setMintasks(String mintasks) {
		Utils.setAttribute("min_tasks", Utils.getIntegerAsString(Utils.str2int(mintasks)), _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public String getMintasks() {
		return Utils.getAttributeValue("min_tasks", _job);
	}

	public void setVisivle(String visible) {
		Utils.setAttribute("visible", visible, _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public String getVisible() {
		return Utils.getAttributeValue("visible", _job);
	}


	public String[] getProcessClasses() {
		String[] names = null;
		if(_dom.getRoot().getName().equalsIgnoreCase("spooler")) {
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
		}
		return names;
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
				if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
				return;
			}

			desc.removeContent();
			if (!(f == null || f.equals(""))) {
				setInclude(f);
			}
			desc.addContent(new CDATA(description));
			_dom.setChanged(true);
			if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		}
	}


	public String getInclude() {

		Element desc = _job.getChild("description");
		if (desc != null) {
			Element inc = desc.getChild("include");
			if (inc != null)
				return Utils.getAttributeValue("file", inc) + Utils.getAttributeValue("live_file", inc);
		}
		return "";
	}

	public boolean isLiveFile() {
		Element desc = _job.getChild("description");
		if (desc != null) {
			Element inc = desc.getChild("include");
			if (inc != null)
				return Utils.getAttributeValue("live_file",inc).length() > 0;
		}
		return false;
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
			if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		}
	}

	public void setInclude(String file, boolean isLifeFileFile) {
		Element desc = _job.getChild("description");
		if (desc == null && !file.equals("")) {
			desc = new Element("description");
			_job.addContent(desc);
		}

		if (desc != null) {
			if (!file.equals("")) {
				Element incl = desc.getChild("include");
				if (incl == null)
					desc.addContent(0, new Element("include").setAttribute((isLifeFileFile?"live_file":"file"), file));
				else {
					incl.removeAttribute("file");
					incl.removeAttribute("live_file");
					incl.setAttribute((isLifeFileFile?"live_file":"file"), file);
				}

			} else {
				desc.removeChild("include");
				if (getDescription().equals(""))
					_job.removeChild("description");
			}

			_dom.setChanged(true);
			if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		}
	}

	public String getIgnoreSignal() {
		return Utils.getAttributeValue("ignore_signals", _job);
	}


	public void setIgnoreSignal(String signals) {
		Utils.setAttribute("ignore_signals", signals, _job, _dom);
		if(_dom.isDirectory() || _dom.isLifeElement()) _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
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

}
