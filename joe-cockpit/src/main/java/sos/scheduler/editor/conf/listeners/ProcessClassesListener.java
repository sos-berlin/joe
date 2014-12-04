package sos.scheduler.editor.conf.listeners;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.command.RemoteScheduler;
import sos.scheduler.editor.app.Utils;

import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.trilead.ssh2.channel.RemoteX11AcceptThread;

public class ProcessClassesListener {
	private final static String	CATCHALL		    = "<empty>";
	private SchedulerDom		_dom			    = null;
	private Element				_config			    = null;
	private Element				_processClasses	    = null;
    private Element             _remoteSchedulers    = null;
    private List<Element>       _listProcessClasses = null;
    private List<Element>       _listRemoteScheduler= null;
    private Element             _class              = null;
    private Element             _remoteScheduler   = null;

	public ProcessClassesListener(SchedulerDom dom, Element config) throws JDOMException {
		_dom = dom;
		_config = config;

        _processClasses = _config.getChild("process_classes");
        if (_processClasses != null) {
            _listProcessClasses = _processClasses.getChildren("process_class");
         }
		
	}

    private void initClasses() {
        if (_config.getChild("process_classes") == null) {
            _processClasses = new Element("process_classes");
            _config.addContent(_processClasses);
        }
        else {
            _processClasses = _config.getChild("process_classes");
        }
        _listProcessClasses = _processClasses.getChildren("process_class");
    }

    private void initRemoteScheduler() {
        if (_class.getChild("remote_schedulers") == null) {
            _remoteSchedulers = new Element("remote_schedulers");
            _class.addContent(_remoteSchedulers);
        }
        else {
            _remoteSchedulers = _class.getChild("remote_schedulers");
        }
        if (_remoteSchedulers != null) {
            _listRemoteScheduler = _remoteSchedulers.getChildren("remote_scheduler");
        }
    }

    public void fillProcessClassesTable(Table tableProcessClasses) {
        tableProcessClasses.removeAll();
        if (_listProcessClasses != null) {
            for (Iterator it = _listProcessClasses.iterator(); it.hasNext();) {
                Element e = (Element) it.next();
                TableItem item = new TableItem(tableProcessClasses, SWT.NONE);
                String name = Utils.getAttributeValue("name", e);
                if (name.equals(""))
                    name = CATCHALL;
                item.setText(0, name);
                item.setText(1, "" + Utils.getIntValue("max_processes", e));
                item.setText(2, Utils.getAttributeValue("remote_scheduler", e));
                if (!Utils.isElementEnabled("process_class", _dom, e)) {
                    item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                }
            }
        }
    }
    
    public void fillRemoteSchedulerTable(Table tableRemoteScheduler) {
        
            
            initRemoteScheduler();
            
            tableRemoteScheduler.removeAll();
            if (_listRemoteScheduler != null) {
                for (Iterator it = _listRemoteScheduler.iterator(); it.hasNext();) {
                    Element e = (Element) it.next();
                    TableItem item = new TableItem(tableRemoteScheduler, SWT.NONE);
                    String host = getRemoteHost(e);
                    String port = getRemotePort(e);

                    item.setText(0, host);
                    item.setText(1, port);
                }
            }
        
    }

    public Element selectProcessClass(int index) {
        if (_listProcessClasses == null) {
            initClasses();
        }
        if (_listProcessClasses != null && index >= 0 && index < _listProcessClasses.size()) {
            _class = (Element) _listProcessClasses.get(index);
        }
        else {
            _class = null;
        }
        return _class;
    }

   
	public String getProcessClass() {
		String name = Utils.getAttributeValue("name", _class);
		if (name.equals(CATCHALL))
			name = "";
		return name;
	}

	public String getRemoteHost(Element ee) {
        String host = Utils.getAttributeValue("remote_scheduler", ee);
        try {
            host = host.substring(0, host.lastIndexOf(":"));
        }
        catch (Exception e) {
            host = "";
        }
        return host.trim();
    }

	public String getRemoteHost() {
		return getRemoteHost(_class);
	}

    public String getRemotePort(Element ee) {
        String port = Utils.getAttributeValue("remote_scheduler", ee);
        try {
            port = port.substring(port.lastIndexOf(":") + 1);
        }
        catch (Exception e) {
            port = "";
        }
        return port.trim();
    }

    public String getRemotePort() {
        return getRemotePort(_class);
    }

    
	public String getMaxProcesses() {
		return Utils.getAttributeValue("max_processes", _class);
	}

	public String getSpoolerID() {
		return Utils.getAttributeValue("spooler_id", _class);
	}

	public void setIgnoreProcessClasses(boolean ignore) {
		if (_processClasses == null) {
			Element config = _dom.getRoot().getChild("config");
			_processClasses = config.getChild("process_classes");
			if (_processClasses == null) {
				_processClasses = new Element("process_classes");
				config.addContent(_processClasses);
			}
		}
		Utils.setAttribute("ignore", ignore, false, _processClasses, _dom);
	}

	public boolean isIgnoreProcessClasses() {
		if (_processClasses != null)
			return Utils.getAttributeValue("ignore", _processClasses).equals("yes") ? true : false;
		else
			return false;
	}

	public boolean isReplace() {
		//default ist true daher auch gleich leerstring
		return Utils.getAttributeValue("replace", _class).equals("") || Utils.getBooleanValue("replace", _class);
	}

	public void newProcessClass() {
		_class = new Element("process_class");
       // initRemoteScheduler();

	}

	public void applyProcessClass(String processClass, String host, String port, int maxProcesses) {
		_dom.setChanged(true);
		_dom.setChangedForDirectory("process_class", Utils.getAttributeValue("name", _class), SchedulerDom.DELETE);
		Utils.setAttribute("name", processClass, _class, _dom);
		Utils.setAttribute("max_processes", maxProcesses, _class, _dom);
		if (host.trim().concat(port.trim()).length() > 0) {
			Utils.setAttribute("remote_scheduler", host.trim() + ":" + port.trim(), _class, _dom);
		}
		if (_listProcessClasses == null)
			initClasses();
		if (!_listProcessClasses.contains(_class)) {
			_listProcessClasses.add(_class);
			_dom.setChangedForDirectory("process_class", processClass, SchedulerDom.NEW);
		}
		else
			if (_dom.isLifeElement()) {
				_dom.setChangedForDirectory("process_class", processClass, SchedulerDom.NEW);
				_dom.getRoot().setAttribute("name", _class.getAttributeValue("name"));
			}
			else {
				_dom.setChangedForDirectory("process_class", processClass, SchedulerDom.MODIFY);
			}
	}
 

    public void applyRemoteSchedulerTable(Table tableRemoteScheduler) {
        if (tableRemoteScheduler.getItemCount() > 0) {
            this.initRemoteScheduler();
            _listRemoteScheduler.clear();
        }
        for (int i = 0; i < tableRemoteScheduler.getItemCount(); i++) {
            TableItem item = tableRemoteScheduler.getItems()[i];
            _remoteScheduler = new Element("remote_scheduler");
            String host = item.getText(0);
            String port = item.getText(1);
            
            if (host.trim().concat(port.trim()).length() > 0) {
                Utils.setAttribute("remote_scheduler", host.trim() + ":" + port.trim(), _remoteScheduler, _dom);
            }
            
           _listRemoteScheduler.add(_remoteScheduler);
        }
           
    }
   
	public void removeProcessClass(int index) {
		if (index >= 0 && index < _listProcessClasses.size()) {
			String processClass = Utils.getAttributeValue("name", (Element) _listProcessClasses.get(index));
			_listProcessClasses.remove(index);
			if (_listProcessClasses.size() == 0 && !isIgnoreProcessClasses()) {
				_config.removeChild("process_classes");
				_processClasses = null;
				_listProcessClasses = null;
			}
			_class = null;
			_dom.setChanged(true);
			_dom.setChangedForDirectory("process_class", processClass, SchedulerDom.DELETE);
		}
	}

 
	
	public boolean isValidClass(String name) {
		if (_listProcessClasses != null) {
			for (Iterator it = _listProcessClasses.iterator(); it.hasNext();) {
				Element e = (Element) it.next();
				if (Utils.getAttributeValue("name", e).equals(name))
					return false;
			}
		}
		return true;
	}
}
