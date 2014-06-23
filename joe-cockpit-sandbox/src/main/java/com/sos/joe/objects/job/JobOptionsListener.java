package com.sos.joe.objects.job;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.JOEListener;

import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.scheduler.model.objects.JSObjJob;
import com.sos.scheduler.model.objects.Job.DelayOrderAfterSetback;

public class JobOptionsListener extends JOEListener {
	@SuppressWarnings("unused") private final String		conClassName	= this.getClass().getSimpleName();
	@SuppressWarnings("unused") private static final String	conSVNVersion	= "$Id$";
	@SuppressWarnings("unused") private final Logger		logger			= Logger.getLogger(this.getClass());
	//    private            SchedulerDom     _dom          = null;
	//    private            Element          _job          = null;
	private List											_directories	= null;
	private Element											_directory		= null;
	private List											_setbacks		= null;
	private Element											_setback		= null;
	private List											_errorDelays	= null;
	private Element											_errorDelay		= null;
	private JSObjJob										objJSJob		= null;

	public JobOptionsListener(final SchedulerDom dom, final Element job) {
		_dom = dom;
		_job = job;
		_setback = new Element("delay_order_after_setback");
		_directories = _job.getChildren("start_when_directory_changed");
		_setbacks = _job.getChildren("delay_order_after_setback");
		_errorDelays = _job.getChildren("delay_after_error");
	}

	public JobOptionsListener(final JSObjJob pobjJob) {
		objJSJob = pobjJob;
	}

	public boolean isDirectoryTrigger() {
		return objJSJob.getStartWhenDirectoryChanged().size() > 0;
		//        return _directories.size() > 0;
	}

	public void fillDirectories(final Table table) {
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
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public void selectDirectory(final int index) {
		if (index >= 0 && index < _directories.size())
			_directory = (Element) _directories.get(index);
	}

	public void applyDirectory(final String directory, final String regex) {
		Utils.setAttribute("directory", directory, _directory, _dom);
		Utils.setAttribute("regex", regex, _directory, _dom);
		if (!_directories.contains(_directory))
			_directories.add(_directory);
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public void deleteDirectory(final int index) {
		if (index >= 0 && index < _directories.size()) {
			_directories.remove(index);
			_directory = null;
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
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
		return objJSJob.getDelayOrderAfterSetback().size() > 0;
	}

	public void fillSetbacks(final Table table) {
		table.removeAll();
		for (DelayOrderAfterSetback objItem : objJSJob.getDelayOrderAfterSetback()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "" + objItem.getSetbackCount());
			item.setText(1, objItem.getIsMaximum());
			String s = objItem.getDelay();
			if (s.equals("00")) {
				s = "0";
			}
			item.setText(2, s);
		}
	}

	public void newErrorDelays(final Table errorDelays) {
		//        TableItem[] items = errorDelays.getItems();
		//        for (int i = items.length; i >= 0; i--) {
		//            deleteErrorDelay(i);
		//        }
		//
		//        for (int i = 0; i < items.length; i++) {
		//            newErrorDelay();
		//            applyErrorDelay(items[i].getText(0), items[i].getText(1));
		//        }
	}

	public void newSetbacks(final Table setback) {
		//        TableItem[] items = setback.getItems();
		//        for (int i = items.length; i >= 0; i--) {
		//            deleteSetbackDelay(i);
		//        }
		//
		//        for (int i = 0; i < items.length; i++) {
		//            newSetbackDelay();
		//            applySetbackDelay(items[i].getText(0), items[i].getText(1).equalsIgnoreCase("yes"), items[i].getText(2));
		//        }
		//        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void newSetbackDelay() {
		//        _setback = new Element("delay_order_after_setback");
		//        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void selectSetbackDelay(final int index) {
		//        if (index >= 0 && index < _setbacks.size())
		//            _setback = (Element) _setbacks.get(index);
	}

	public void applySetbackDelay(final String setbackCount, final boolean maximum, final String delay) {
		//        Utils.setAttribute("setback_count", setbackCount, _setback, _dom);
		//        Utils.setAttribute("is_maximum", maximum, _setback, _dom);
		//        Utils.setAttribute("delay", delay, _setback, _dom);
		//        if (!_setbacks.contains(_setback))
		//            _setbacks.add(_setback);
		//        _dom.setChanged(true);
		//        _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
	}

	public void deleteSetbackDelay(final int index) {
		//        if (index >= 0 && index < _setbacks.size()) {
		//            _setbacks.remove(index);
		//            _setback = null;
		//            _dom.setChanged(true);
		//            _dom.setChangedForDirectory("job", Utils.getAttributeValue("name",_job), SchedulerDom.MODIFY);
		//        }
	}

	public String getSetbackCount() {
		return "0";
		//        return Utils.getIntegerAsString(Utils.getIntValue("setback_count", -999, _setback));
	}

	public boolean isMaximum() {
		//        return Utils.isAttributeValue("is_maximum", _setback);
		return false;
	}

	public String getSetbackCountHours() {
		return "";
		//        return Utils.getIntegerAsString(Utils.getHours(_setback.getAttributeValue("delay"), -999));
	}

	public String getSetbackCountMinutes() {
		return "";
		//        return Utils.getIntegerAsString(Utils.getMinutes(_setback.getAttributeValue("delay"), -999));
	}

	public String getSetbackCountSeconds() {
		return "";
		//        return Utils.getIntegerAsString(Utils.getSeconds(_setback.getAttributeValue("delay"), -999));
	}

	// error count
	public boolean isErrorDelay() {
		// TODO complete implemenatation
		if (_errorDelays != null) {
			return _errorDelays.size() > 0;
		}
		return false;
	}

	public void fillTable(final Table table) {
		table.removeAll();
		if (_errorDelays != null) {
			Iterator it = _errorDelays.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, "" + Utils.getIntValue("error_count", e));
				item.setText(1, getDelay(e));
			}
		}
	}

	//private String getDelay(Element e) {
	public String getDelay(final Element e) {
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
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public void selectErrorDelay(final int index) {
		if (index >= 0 && index < _errorDelays.size())
			_errorDelay = (Element) _errorDelays.get(index);
	}

	public void applyErrorDelay(final String errorCount, final String delay) {
		Utils.setAttribute("error_count", errorCount, _errorDelay, _dom);
		Utils.setAttribute("delay", delay, _errorDelay, _dom);
		if (!_errorDelays.contains(_errorDelay))
			_errorDelays.add(_errorDelay);
		_dom.setChanged(true);
		_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
	}

	public void deleteErrorDelay(final int index) {
		if (index >= 0 && index < _errorDelays.size()) {
			_errorDelays.remove(index);
			_errorDelay = null;
			_dom.setChanged(true);
			_dom.setChangedForDirectory("job", Utils.getAttributeValue("name", _job), SchedulerDom.MODIFY);
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
