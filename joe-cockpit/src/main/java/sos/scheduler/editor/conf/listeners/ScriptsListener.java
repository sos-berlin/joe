package sos.scheduler.editor.conf.listeners;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ScriptsListener {
	private SchedulerDom		_dom				= null;
	private ISchedulerUpdate	_main				= null;
	private List				_list				= null;
	private Element				_parent				= null;
	private boolean             isJob               = true;
	private String              objectName          = "";
    private Element             _config     = null;
	private Element             _monitors  = null;

	private final static String	EMPTY_MONITOR_NAME	= "<empty>";

	public ScriptsListener(SchedulerDom dom, ISchedulerUpdate update, Element parent) {
		_dom = dom;
		_parent=parent;
		_main = update;		
		isJob = true;
        objectName = "job";

		//Parent can be <jobs> or <monitors> 
		if (!_dom.isLifeElement()) { 
            _config = _dom.getRoot().getChild("config");
            if (!parent.getName().equals("job")){    //named monitor.
                if (_config.getChild("monitors") == null) {
                   _monitors = new Element("monitors");
                   _config.addContent(_monitors);
                }else{
                  _monitors = _config.getChild("monitors");
                  
                }
                _parent = _monitors;
                isJob = false;
                objectName = "monitor";
            }
          
         _list = _parent.getChildren("monitor");
        }
         
	}

	private void initScripts() {
        _list = _parent.getChildren("monitor");
	}
	
	 
	public void fillTable(Table table) {
		table.removeAll();
		if (_list != null) {
			for (int i = 0; i < _list.size(); i++) {
				Element monitor = (Element) _list.get(i);
				TableItem item = new TableItem(table, SWT.NONE);
				item.setData(monitor);
				String name = Utils.getAttributeValue("name", monitor);
				if (!Utils.isElementEnabled("job", _dom, _parent)) {
					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				}
				else {
					item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				}
				item.setText(0, name.equals("") ? EMPTY_MONITOR_NAME : name);
				item.setText(1, Utils.getAttributeValue("ordering", monitor));
			}
		}
		_main.updateScripts();
	}
 
	
	public void save(Table table, String name, String ordering, String newName){
	        boolean found = false;
	        Element e = null;
	        TableItem item = null;
	        if (_list != null) {
	            int index = 0;
	            for (int i = 0; i < _list.size(); i++) {
	                e = (Element) _list.get(i);
	                if (name.equals(Utils.getAttributeValue("name", e)) || (name.equals("<empty>") && Utils.getAttributeValue("name", e).equals(""))) {
	                    if (newName != null) {
	                        Utils.setAttribute("name", newName, e);
	                        _main.updateScripts();
	                        name = newName;
	                    }
	                    found = true;
	                    Utils.setAttribute("ordering", ordering, e);
	                    _dom.setChanged(true);
	                    if (_dom.isLifeElement() || _dom.isDirectory())
	                        _dom.setChangedForDirectory(objectName, name, SchedulerDom.MODIFY);
	                    table.getItem(index).setText(new String[] { (name.equals("") ? EMPTY_MONITOR_NAME : name), ordering });
	                }
	                index++;
	            }
	        }     

	        if (!found) {
	            e = new Element("monitor");
	            if (name.equals(EMPTY_MONITOR_NAME)) {
	                e.removeAttribute("name");
	                Utils.setAttribute("ordering", ordering, e);
	            }
	            else {
	                Utils.setAttribute("name", name, e);
	                Utils.setAttribute("ordering", ordering, e);
	            }
	            _dom.setChanged(true);
	            if (_dom.isLifeElement() || _dom.isDirectory())
	                _dom.setChangedForDirectory(objectName, name, SchedulerDom.MODIFY);
	            if (_list == null)
	                initScripts();
	            if (_list != null)
	                _list.add(e);
	            item = new TableItem(table, SWT.NONE);
	            item.setText(new String[] { (name.equals("") ? EMPTY_MONITOR_NAME : name), ordering });
	            item.setData(e);
	            _main.updateScripts();
	        }   
	}
 
	 

	public boolean delete(Table table) {
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem item = table.getItem(index);
			Element e = (Element) item.getData();
			e.detach();
			_dom.setChanged(true);
			if (_dom.isLifeElement() || _dom.isDirectory()){
			    if (isJob){
	                _dom.setChangedForDirectory(objectName, Utils.getAttributeValue("name", _parent), SchedulerDom.MODIFY);
			    }else{
                    _dom.setChangedForDirectory(objectName, item.getText(), SchedulerDom.DELETE);
			    }
			}
			table.remove(index);
			_main.updateScripts();
			if (_list == null)
				initScripts();
			if (_list.size() == 0) {
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

	public boolean existScriptname(String name) {
		if (name == null || name.length() == 0)
			return false;
		for (int i = 0; _list != null && i < _list.size(); i++) {
			Element currJob = (Element) _list.get(i);
			String jobName = Utils.getAttributeValue("name", currJob);
			if (jobName.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public Element getParent() {
		return _parent;
	}
}
