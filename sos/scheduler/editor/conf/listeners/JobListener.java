package sos.scheduler.editor.conf.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.CDATA;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.app.Options;

public class JobListener {
    private ISchedulerUpdate _main;

    private SchedulerDom     _dom;

    private Element          _job;

    private List             _params;
        


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
        Utils.setAttribute("__comment__", comment, _job, _dom);
    }


    public boolean isDisabled() {
        return _dom.isJobDisabled(Utils.getAttributeValue("name", _job));
    }


    public String getName() {
        return Utils.getAttributeValue("name", _job);
    }


    public void setName(String name, boolean updateTree) {
        Utils.setAttribute("name", name, _job, _dom);
        if (updateTree)
            _main.updateJob(name);
    }


    public String getTitle() {
        return Utils.getAttributeValue("title", _job);
    }


    public void setTitle(String title) {
        Utils.setAttribute("title", title, _job, _dom);
    }


    public String getSpoolerID() {
        return Utils.getAttributeValue("spooler_id", _job);
    }


    public void setSpoolerID(String spoolerID) {
        Utils.setAttribute("spooler_id", spoolerID, _job, _dom);
    }


    public String getProcessClass() {
        return Utils.getAttributeValue("process_class", _job);
    }


    public void setProcessClass(String processClass) {
        Utils.setAttribute("process_class", processClass, _job, _dom);
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
    }


    public String getPriority() {

        return Utils.getAttributeValue("priority", _job);
    }


    public void setPriority(String priority) {
        Utils.setAttribute("priority", priority, _job, _dom);
    }


    public String getTasks() {
        return Utils.getAttributeValue("tasks", _job);
    }


    public void setTasks(String tasks) {
        Utils.setAttribute("tasks", Utils.getIntegerAsString(Utils.str2int(tasks)), _job, _dom);
    }


    public String getTimeout() {
        return Utils.getAttributeValue("timeout", _job);
    }


    public void setTimeout(String timeout) {
        Utils.setAttribute("timeout", Utils.getIntegerAsString(Utils.str2int(timeout)), _job, _dom);
    }


    public String getIdleTimeout() {
        return Utils.getAttributeValue("idle_timeout", _job);
    }


    public void setIdleTimeout(String idleTimeout) {
        Utils.setAttribute("idle_timeout", Utils.getIntegerAsString(Utils.str2int(idleTimeout)), _job, _dom);
    }


    public void setForceIdletimeout(boolean forceIdleTimeout) {
        if (forceIdleTimeout) {
            Utils.setAttribute("force_idle_timeout", "yes", _job, _dom);
        } else {
            Utils.setAttribute("force_idle_timeout", "no", _job, _dom);
        }
    }

    public void setStopOnError(boolean stopOnError) {
      if (stopOnError) {
          Utils.setAttribute("stop_on_error", "yes", _job, _dom);
      } else {
          Utils.setAttribute("stop_on_error", "no", _job, _dom);
      }
  }

    public void setMintasks(String mintasks) {
        Utils.setAttribute("min_tasks", Utils.getIntegerAsString(Utils.str2int(mintasks)), _job, _dom);
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
                    item.setText(1, ((Element) o).getAttributeValue("value"));
                }
            }
        }
    }

    public void fillParams(ArrayList listOfParams, Table table) {
    	boolean existParam = false;
    	for (int i =0; i < listOfParams.size(); i++) {
    		HashMap h = (HashMap)listOfParams.get(i);                
    		TableItem item = new TableItem(table, SWT.NONE);
    		if (h.get("name") != null) {
    			if(h.get("required") != null && Boolean.getBoolean(h.get("required").toString())) {
    				item.setBackground(Options.getRequiredColor());
    			}
    			if(existsParams(h.get("name").toString(), table)) {
    				item.setBackground(Options.getRedColor());
    				existParam = true;
    			}
    			item.setText(0, h.get("name").toString());
    			item.setText(1, (h.get("default_value") != null ? h.get("default_value").toString() : ""));                    	
    		}           
    	}
    	if(existParam) {
    		//System.out.println("Die Liste enthält doppelte Parameter. Diese sind in roten hintergrund hinterlegt.");
    		int cont = MainWindow.message(MainWindow.getSShell(), sos.scheduler.editor.app.Messages.getString("double_params"), SWT.ICON_WARNING | SWT.OK );
    	}
    }
    
    private boolean existsParams(String name, Table table) {
    	
    	try {
    		for (int i =0; i < table.getItemCount(); i++) {
    			if(table.getItem(i).getText(0).equals(name)) {
    				return true;
    			}
    		}
    	} catch (Exception e) {
    		System.out.println("error in JobListener.existsParams " + e.getMessage());
    	}
    	return false;
    }

    public void deleteParameter(Table table, int index) {
    	String currParam = table.getItem(index).getText(0);
    	
        if (_params != null) {
            _params.remove(index);
            _dom.setChanged(true);
        }
        table.remove(index);
        //durch das importieren kann ein Parameter zweimal vorkommen. Der Zweite Parameter ist in ro hinterlegt.
        //Wenn einer der Parameter gelöscht wird, dann soll der rot hinterlegte Parameter wieder einen weißen Hintergrund haben  
       updateBackGroundColor(table, currParam);
    }


    public void saveParameter(Table table, String name, String value) {
        boolean found = false;
        String value2 = value.replaceAll("\"", "&quot;");
        if (_params != null) {
            int index = 0;
            Iterator it = _params.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Element) {
                    Element e = (Element) o;
                    if (name.equals(e.getAttributeValue("name"))) {
                        found = true;
                        e.setAttribute("value", value2);
                        _dom.setChanged(true);
                        table.getItem(index).setText(1, value);
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
            if (_params == null)
                initParams();
            _params.add(e);

            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { name, value });
        }
        //updateBackGroundColor(table, name);
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
                return;
            }

            /*
             * boolean found = false; List mixed = desc.getContent(); Iterator
             * it = mixed.iterator(); while (it.hasNext()) { Object o =
             * it.next(); if (o instanceof CDATA || o instanceof Text) { found =
             * true; if (description.equals("")) { ((Text) o).detach(); break; }
             * else ((Text) o).setText(description); } } if (!found &&
             * !description.equals("")) desc.addContent(new CDATA(description));
             */
            desc.removeContent();
            if (!(f == null || f.equals(""))) {
                setInclude(f);
            }
            desc.addContent(new CDATA(description));
            _dom.setChanged(true);
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
        }
    }

    public String getIgnoreSignal() {
      return Utils.getAttributeValue("ignore_signals", _job);
    }


    public void setIgnoreSignal(String signals) {
      Utils.setAttribute("ignore_signals", signals, _job, _dom);
    }


	public SchedulerDom get_dom() {
		return _dom;
	}


	public ISchedulerUpdate get_main() {
		return _main;
	}    
    
}
