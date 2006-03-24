package sos.scheduler.editor.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Utils;

public class SecurityListener {
	private DomParser _dom;

	private Element _config;

	private Element _security;

	private Element _host;

	private List _list;

	private static String[] _levels = { "none", "signal", "info", "no_add",
			"all" };

	public SecurityListener(DomParser dom, Element config) throws JDOMException {
		_dom = dom;
		_config = config;

		_security = _config.getChild("security");
		if (_security != null)
			_list = _security.getChildren("allowed_host");
	}

	private void initSecurity() {
		if (_config.getChild("security") == null) {
			_security = new Element("security");
			_config.addContent(_security);
		} else {
			_security = _config.getChild("security");
		}
		_list = _security.getChildren("allowed_host");
	}

	public void fillTable(Table table) {
		table.removeAll();
		if (_list != null) {
			for (Iterator it = _list.iterator(); it.hasNext();) {
				Object o = it.next();
				if (o instanceof Element) {
					Element e = (Element) o;
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, Utils.getAttributeValue("host", e));
					item.setText(1, Utils.getAttributeValue("level", e));
				}
			}
		}
	}

	public void selectHost(int index) {
		if (_list != null && index >= 0 && index < _list.size())
			_host = (Element) _list.get(index);
		else
			_host = null;
	}

	public String getHost() {
		return Utils.getAttributeValue("host", _host);
	}

	public String getLevel() {
		return Utils.getAttributeValue("level", _host);
	}

	public boolean getIgnoreUnknownHosts() {
		return Utils.isAttributeValue("ignore_unknown_hosts", _security);
	}

	public void setIgnoreUnknownHosts(boolean ignore) {
		if (_list == null)
  		initSecurity();
		
		Utils.setAttribute("ignore_unknown_hosts", ignore, _security, _dom);
	}

	public String[] getLevels() {
		return _levels;
	}

	public void newHost() {
		_host = new Element("allowed_host");
	}

	public void applyHost(String host, String level) {
		_host.setAttribute("host", host);
		_host.setAttribute("level", level);
		if (_list == null)
			initSecurity();
		if (!_list.contains(_host))
			_list.add(_host);
		_dom.setChanged(true);
	}

	public void removeHost(int index) {
		if (index >= 0 && index < _list.size()) {
			_list.remove(index);
			if (_list.size() == 0) {
				_config.removeChild("security");
				_security = null;
				_list = null;
			}
			_host = null;
			_dom.setChanged(true);
		}
	}

	public int getLevelIndex(String level) {
		for (int i = 0; i < _levels.length; i++) {
			if (level.equalsIgnoreCase(_levels[i]))
				return i;
		}
		return -1;
	}

}
