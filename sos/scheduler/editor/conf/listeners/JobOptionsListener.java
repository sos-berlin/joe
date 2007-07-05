package sos.scheduler.editor.conf.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class JobOptionsListener {
    private SchedulerDom _dom;

    private Element      _job;

    private List         _directories;

    private Element      _directory;

    private List         _setbacks;

    private Element      _setback;

    private List         _errorDelays;

    private Element      _errorDelay;


    public JobOptionsListener(SchedulerDom dom, Element job) {
        _dom = dom;
        _job = job;
        _directories = _job.getChildren("start_when_directory_changed");
        _setbacks = _job.getChildren("delay_order_after_setback");
        _errorDelays = _job.getChildren("delay_after_error");
    }


    // directory trigger

    public boolean isDirectoryTrigger() {
        return _directories.size() > 0;
    }


    public void fillDirectories(Table table) {
        table.removeAll();
        Iterator it = _directories.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, Utils.getAttributeValue("directory", e));
            item.setText(1, Utils.getAttributeValue("regex", e));
        }
    }


    public void newDirectory() {
        _directory = new Element("start_when_directory_changed");
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
    }


    public void selectDirectory(int index) {
        if (index >= 0 && index < _directories.size())
            _directory = (Element) _directories.get(index);
    }


    public void applyDirectory(String directory, String regex) {
        Utils.setAttribute("directory", directory, _directory, _dom);
        Utils.setAttribute("regex", regex, _directory, _dom);
        if (!_directories.contains(_directory))
            _directories.add(_directory);
        _dom.setChanged(true);                
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
    }


    public void deleteDirectory(int index) {
        if (index >= 0 && index < _directories.size()) {
            _directories.remove(index);
            _directory = null;
            _dom.setChanged(true);
            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
        }
    }


    public String getDirectory() {
        return Utils.getAttributeValue("directory", _directory);
    }


    public String getRegex() {
        return Utils.getAttributeValue("regex", _directory);
    }


    // setbacks

    public boolean isSetbackDelay() {
        return _setbacks.size() > 0;
    }


    public void fillSetbacks(Table table) {
        table.removeAll();
        Iterator it = _setbacks.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, "" + Utils.getIntValue("setback_count", e));
            item.setText(1, Utils.isAttributeValue("is_maximum", e) ? "Yes" : "No");
            String s = getDelay(e);
            if (s.equals("00")) {
                s = "0";
            }
            item.setText(2, s);
        }
    }


    public void newErrorDelays(Table errorDelays) {
        TableItem[] items = errorDelays.getItems();
        for (int i = items.length; i >= 0; i--) {
            deleteErrorDelay(i);
        }

        for (int i = 0; i < items.length; i++) {
            newErrorDelay();
            applyErrorDelay(items[i].getText(0), items[i].getText(1));
        }
    }


    public void newSetbacks(Table setback) {
        TableItem[] items = setback.getItems();
        for (int i = items.length; i >= 0; i--) {
            deleteSetbackDelay(i);
        }

        for (int i = 0; i < items.length; i++) {
            newSetbackDelay();
            applySetbackDelay(items[i].getText(0), items[i].getText(1).equalsIgnoreCase("yes"), items[i].getText(2));
        }
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
    }


    public void newSetbackDelay() {
        _setback = new Element("delay_order_after_setback");
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
    }


    public void selectSetbackDelay(int index) {
        if (index >= 0 && index < _setbacks.size())
            _setback = (Element) _setbacks.get(index);
    }


    public void applySetbackDelay(String setbackCount, boolean maximum, String delay) {
        Utils.setAttribute("setback_count", setbackCount, _setback, _dom);
        Utils.setAttribute("is_maximum", maximum, _setback, _dom);
        Utils.setAttribute("delay", delay, _setback, _dom);
        if (!_setbacks.contains(_setback))
            _setbacks.add(_setback);
        _dom.setChanged(true);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
    }


    public void deleteSetbackDelay(int index) {
        if (index >= 0 && index < _setbacks.size()) {
            _setbacks.remove(index);
            _setback = null;
            _dom.setChanged(true);
            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
        }
    }


    public String getSetbackCount() {
        return Utils.getIntegerAsString(Utils.getIntValue("setback_count", -999, _setback));
    }


    public boolean isMaximum() {
        return Utils.isAttributeValue("is_maximum", _setback);
    }


    public String getSetbackCountHours() {
        return Utils.getIntegerAsString(Utils.getHours(_setback.getAttributeValue("delay"), -999));
    }


    public String getSetbackCountMinutes() {
        return Utils.getIntegerAsString(Utils.getMinutes(_setback.getAttributeValue("delay"), -999));
    }


    public String getSetbackCountSeconds() {
        return Utils.getIntegerAsString(Utils.getSeconds(_setback.getAttributeValue("delay"), -999));
    }


    // error count

    public boolean isErrorDelay() {
        return _errorDelays.size() > 0;
    }


    public void fillTable(Table table) {
        table.removeAll();
        Iterator it = _errorDelays.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, "" + Utils.getIntValue("error_count", e));
            item.setText(1, getDelay(e));
        }
    }


    //private String getDelay(Element e) {
    public String getDelay(Element e) {
        String delay = e.getAttributeValue("delay");
        if (delay == null || delay.equals(""))
            delay = "00:00";

        if (delay.equalsIgnoreCase("stop"))
            return "stop";
        else {
            int hours = Utils.getHours(delay, 0);
            int minutes = Utils.getMinutes(delay, 0);
            int seconds = Utils.getSeconds(delay, 0);

            return Utils.getTime(hours, minutes, seconds, true);
        }
    }


    public void newErrorDelay() {
        _errorDelay = new Element("delay_after_error");
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
    }


    public void selectErrorDelay(int index) {
        if (index >= 0 && index < _errorDelays.size())
            _errorDelay = (Element) _errorDelays.get(index);
    }


    public void applyErrorDelay(String errorCount, String delay) {
        Utils.setAttribute("error_count", errorCount, _errorDelay, _dom);
        Utils.setAttribute("delay", delay, _errorDelay, _dom);
        if (!_errorDelays.contains(_errorDelay))
            _errorDelays.add(_errorDelay);
        _dom.setChanged(true);
        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
    }


    public void deleteErrorDelay(int index) {
        if (index >= 0 && index < _errorDelays.size()) {
            _errorDelays.remove(index);
            _errorDelay = null;
            _dom.setChanged(true);
            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
        }
    }


    public String getErrorCount() {
        return Utils.getIntegerAsString(Utils.getIntValue("error_count", -999, _errorDelay));
    }


    public boolean isStop() {
        if (_errorDelay != null && _errorDelay.getAttributeValue("delay") != null)
            return _errorDelay.getAttributeValue("delay").equalsIgnoreCase("stop");
        else
            return false;
    }


    public String getErrorCountHours() {
        return Utils.getIntegerAsString(Utils.getHours(_errorDelay.getAttributeValue("delay"), -999));
    }


    public String getErrorCountMinutes() {
        return Utils.getIntegerAsString(Utils.getMinutes(_errorDelay.getAttributeValue("delay"), -999));
    }


    public String getErrorCountSeconds() {
        return Utils.getIntegerAsString(Utils.getSeconds(_errorDelay.getAttributeValue("delay"), -999));
    }


	public Element getJob() {
		return _job;
	}

    
	public List getErrorDelays() {
		return _errorDelays;
	}


	public List getSetbacks() {
		return _setbacks;
	}


	
}
