package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class LocksListener {
 

    private SchedulerDom        _dom;

    private Element             _config;

    private Element             _locks;

    private List                _list;

    private Element             _lock;


    public LocksListener(SchedulerDom dom, Element config) throws JDOMException {
        _dom = dom;
        _config = config;
        _locks = _config.getChild("locks");

        if (_locks != null)
            _list = _locks.getChildren("lock");
    }


    private void initLocks() {
        if (_config.getChild("locks") == null) {
        	_locks = new Element("locks");
            _config.addContent(_locks);
        } else {
        	_locks = _config.getChild("locks");
        }
        _list = _locks.getChildren("lock");
    }


    public void fillTable(Table table) {
        table.removeAll();
        if (_list != null) {
            for (Iterator it = _list.iterator(); it.hasNext();) {
                Element e = (Element) it.next();
                TableItem item = new TableItem(table, SWT.NONE);
                String name = Utils.getAttributeValue("name", e);
               
                item.setText(0, name);
                item.setText(1, "" + Utils.getIntValue("max_non_exclusive", e));

            }
        }
    }
    
    public void selectLock(int index) {
        if (_list != null && index >= 0 && index < _list.size())
            _lock = (Element) _list.get(index);
        else
        	_lock = null;
    }


    public String getLock() {
      String name = Utils.getAttributeValue("name", _lock);
      return name;
  }

    public int getMax_non_exclusive() {
      int m = Utils.getIntValue("max_non_exclusive", _lock);
      return m;
   }



    public void newProcessClass() {
    	_lock = new Element("lock");
    }


    public void applyLock(String name,  int maxNonExclusive) {
        Utils.setAttribute("name", name, _lock, _dom);
        Utils.setAttribute("max_non_exclusive", maxNonExclusive, _lock, _dom);
        if (_list == null)
            initLocks();
        if (!_list.contains(_lock))
            _list.add(_lock);
        _dom.setChanged(true);
    }


    public void removeLock(int index) {
        if (index >= 0 && index < _list.size()) {
            _list.remove(index);
            if (_list.size() == 0) {
                _config.removeChild("process_classes");
                _locks = null;
                _list = null;
            }
            _lock = null;
            _dom.setChanged(true);
        }
    }


    public boolean isValidLock(String name) {
        if (_list != null) {
            for (Iterator it = _list.iterator(); it.hasNext();) {
                Element e = (Element) it.next();
                if (Utils.getAttributeValue("name", e).equals(name))
                    return false;
            }
        }
        return true;
    }
}
