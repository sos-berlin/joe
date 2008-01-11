package sos.scheduler.editor.conf.listeners;


import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class ConfigListener {
	
    private SchedulerDom _dom;

    private Element      _config;
    
    private List         _params;


    public ConfigListener(SchedulerDom dom) {
        _dom = dom;
        _config = _dom.getRoot().getChild("config");
    }


    public SchedulerDom getDom() {
        return _dom;
    }


    public String getComment() {
        return Utils.getAttributeValue("__comment__", _config);
    }


    public void setComment(String comment) {
        Utils.setAttribute("__comment__", comment, _config, _dom);
    }


    public String getIncludePath() {
        return Utils.getAttributeValue("include_path", _config);
    }


    public void setIncludePath(String path) {
      Utils.setAttribute("include_path", path, _config, _dom);
  }

    public void setIpAddress(String ip) {
      Utils.setAttribute("ip_address", ip, _config, _dom);
      _dom.setChanged(true);
  }


    public String getJavaClasspath() {
        return Utils.getAttributeValue("java_class_path", _config);
    }


    public void setJavaClasspath(String classpath) {
        Utils.setAttribute("java_class_path", classpath, _config, _dom);
    }


    public String getJavaOptions() {
        return Utils.getAttributeValue("java_options", _config);
    }


    public void setJavaOptions(String options) {
        Utils.setAttribute("java_options", options, _config, _dom);
    }


    public String getLogDir() {
        return Utils.getAttributeValue("log_dir", _config);
    }


    public void setLogDir(String dir) {
        Utils.setAttribute("log_dir", dir, _config, _dom);
    }


    public String getMailXSLTStylesheet() {
        return Utils.getAttributeValue("mail_xslt_stylesheet", _config);
    }


    public void setMailXSLTStylesheet(String stylesheet) {
        Utils.setAttribute("mail_xslt_stylesheet", stylesheet, _config, _dom);
    }


    public String getMainSchedulerHost() {
        return Utils.getAttributeValue("main_scheduler", _config).split(":")[0];
    }


    public int getMainSchedulerPort() {
        String[] str = Utils.getAttributeValue("main_scheduler", _config).split(":");
        if (str.length > 1) {
            try {
                return new Integer(str[1]).intValue();
            } catch (Exception e) {
                Utils.setAttribute("main_scheduler", str[0] + ":0", _config, _dom);
            }
        }
        return 0;
    }


    public void setMainScheduler(String scheduler) {
        if (scheduler.startsWith(":"))
            scheduler = "";
        Utils.setAttribute("main_scheduler", scheduler, _config, _dom);
    }


    public boolean isMainScheduler() {
        return _config.getAttribute("main_scheduler") != null;
    }


    public String getParam() {
        return Utils.getAttributeValue("param", _config);
    }


    public void setParam(String param) {
        Utils.setAttribute("param", param, _config, _dom);
    }


    public int getPriorityMax() {
        if (_config.getAttributeValue("priority_max") == null)
            return 1000;
        else
            return Utils.getIntValue("priority_max", _config);
    }


    public void setPriorityMax(int max) {
        Utils.setAttribute("priority_max", new Integer(max).toString(), _config, _dom);
    }


    public String getSpoolerID() {
        return Utils.getAttributeValue("spooler_id", _config);
    }


    public void setSpoolerID(String spoolerid) {
        Utils.setAttribute("spooler_id", spoolerid, _config, _dom);
    }


    public String getTcpPort() {
      return Utils.getAttributeValue("tcp_port", _config);
  }

   public String getIpAddress() {
      return Utils.getAttributeValue("ip_address ", _config);
   }


    public void setTcpPort(String port) {
        Utils.setAttribute("tcp_port", Utils.getIntegerAsString(Utils.str2int(port)), _config, _dom);
        _config.removeAttribute("port");
        _dom.setChanged(true);
    }


    public String getUdpPort() {
        return Utils.getAttributeValue("udp_port", _config);
    }


    public void setUdpPort(String port) {
        Utils.setAttribute("udp_port", Utils.getIntegerAsString(Utils.str2int(port)), _config, _dom);
        _config.removeAttribute("port");
        _dom.setChanged(true);
    }


    public String getPort() {
        return Utils.getAttributeValue("port", _config);
    }


    public void setPort(String port) {
        Utils.setAttribute("port", Utils.getIntegerAsString(Utils.str2int(port)), _config, _dom);
        _config.removeAttribute("tcp_port");
        _config.removeAttribute("udp_port");
        _dom.setChanged(true);
    }


    public boolean isPort() {
        return _config.getAttribute("port") != null;
    }
    
    public String[] getJobs() {
    	if(_config == null)
    		return new String[0];
    	Element jobs = _config.getChild("jobs");
    	
    	if (jobs != null) {
    		List jobList = jobs.getChildren("job");            
    		String[] names = new String[jobList.size()];
    		
    		for (int i = 0; i < jobList.size(); i++) {
    			Element job = (Element)jobList.get(i); 
    			names[i] = job.getAttributeValue("name");
    		}
    		
    		return names;
    	} else
    		return new String[0];
    }
    
    
    public String getConfigurationAddEvent() {
        return Utils.getAttributeValue("configuration_add_event", _config);
    }


    public void setConfigurationAddEvent(String configurationAddEvent) {
        Utils.setAttribute("configuration_add_event", configurationAddEvent, _config, _dom);
    }
    
    public String getConfigurationModifyEvent() {
        return Utils.getAttributeValue("configuration_modify_event", _config);
    }


    public void setConfigurationModifyEvent(String configurationModifyEvent) {
        Utils.setAttribute("configuration_modify_event", configurationModifyEvent, _config, _dom);
    }
    
    public String getConfigurationDeleteEvent() {
        return Utils.getAttributeValue("configuration_delete_event", _config);
    }


    public void setConfigurationDeleteEvent(String configurationDeleteEvent) {
        Utils.setAttribute("configuration_delete_event", configurationDeleteEvent, _config, _dom);
    }
    
    private void initParams() {

        if (_config.getChild("params") != null) {
            _params = _config.getChild("params").getChildren();
        }

        if (_params == null) {
        	_config.addContent(0, new Element("params"));
            _params = _config.getChild("params").getChildren();
        }
    }

    
    public void fillParams(Table tableParameters) {
    	_params = null;
        tableParameters.removeAll();
        //initParams();

        if (_config.getChild("params") != null) {
            _params = _config.getChild("params").getChildren();
        }

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


    public void saveParameter(Table table, String name, String value) {
        boolean found = false;
        

        if (_params != null) {

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

        if (!found) {
            Element e = new Element("param");

            e.setAttribute("name", name);
            e.setAttribute("value", value);
            _dom.setChanged(true);
            

            if (_params == null)
                initParams();
            if (_params != null)
                _params.add(e);

            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { name, value });
        }
    }

}
