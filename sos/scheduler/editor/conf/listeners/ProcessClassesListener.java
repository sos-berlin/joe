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

public class ProcessClassesListener {
    private final static String CATCHALL = "<empty>";

    private SchedulerDom        _dom;

    private Element             _config;

    private Element             _processClasses;

    private List                _list;

    private Element             _class;


    public ProcessClassesListener(SchedulerDom dom, Element config) throws JDOMException {
        _dom = dom;
        _config = config;
        _processClasses = _config.getChild("process_classes");

        if (_processClasses != null)
            _list = _processClasses.getChildren("process_class");
    }


    private void initClasses() {
        if (_config.getChild("process_classes") == null) {
            _processClasses = new Element("process_classes");
            _config.addContent(_processClasses);
        } else {
            _processClasses = _config.getChild("process_classes");
        }
        _list = _processClasses.getChildren("process_class");
    }


    public void fillTable(Table table) {
        table.removeAll();
        if (_list != null) {
            for (Iterator it = _list.iterator(); it.hasNext();) {
                Element e = (Element) it.next();
                TableItem item = new TableItem(table, SWT.NONE);
                String name = Utils.getAttributeValue("name", e);
                if (name.equals(""))
                    name = CATCHALL;
                item.setText(0, name);
                item.setText(1, "" + Utils.getIntValue("max_processes", e));
                item.setText(2, Utils.getAttributeValue("spooler_id", e));
            }
        }
    }


    public void selectProcessClass(int index) {
        if (_list != null && index >= 0 && index < _list.size())
            _class = (Element) _list.get(index);
        else
            _class = null;
    }


    public String getProcessClass() {
        String name = Utils.getAttributeValue("name", _class);
        if (name.equals(CATCHALL))
            name = "";
        return name;
    }


    public int getMaxProcesses() {
        return Utils.getIntValue("max_processes", _class);
    }


    public String getSpoolerID() {
        return Utils.getAttributeValue("spooler_id", _class);
    }


    public void newProcessClass() {
        _class = new Element("process_class");
    }


    public void applyProcessClass(String processClass, int maxProcesses, String spoolerID) {
        Utils.setAttribute("name", processClass, _class, _dom);
        Utils.setAttribute("max_processes", maxProcesses, _class, _dom);
        Utils.setAttribute("spooler_id", spoolerID, _class, _dom);
        if (_list == null)
            initClasses();
        if (!_list.contains(_class))
            _list.add(_class);
        _dom.setChanged(true);
    }


    public void removeProcessClass(int index) {
        if (index >= 0 && index < _list.size()) {
            _list.remove(index);
            if (_list.size() == 0) {
                _config.removeChild("process_classes");
                _processClasses = null;
                _list = null;
            }
            _class = null;
            _dom.setChanged(true);
        }
    }


    public boolean isValidClass(String name) {
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
