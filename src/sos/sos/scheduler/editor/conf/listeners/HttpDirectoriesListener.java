package sos.scheduler.editor.conf.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;

public class HttpDirectoriesListener {


	private      SchedulerDom    _dom            = null;

	private      Element         _config         = null;

	private      Element         _http_server    = null;

	private      List            _httpDirectory  = new ArrayList();


	public HttpDirectoriesListener(SchedulerDom dom, Element config) {

		_dom = dom;
		_config = config;
		initDirectories();

	}

	private void initDirectories() {

		_http_server = _config.getChild("http_server");
		if (_http_server != null) {
			_httpDirectory = _http_server.getChildren("http_directory");
		}

	}

	public void fillHttpDirectoryTable(Table table) {

		table.removeAll();   
		for (Iterator it = _httpDirectory.iterator(); it.hasNext();) {
			Element http_directory = (Element) it.next();
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Utils.getAttributeValue("url_path", http_directory));
			item.setText(1, Utils.getAttributeValue("path", http_directory));
		}
	}

	public void applyHttpDirectory(TableItem[] httpDirectories) {

		if (_http_server != null) _http_server.removeChildren("http_directory");

		if (httpDirectories.length > 0) {
			if (_http_server == null && _config.getAttribute("http_server") == null) {
				_http_server = new Element("http_server");
				_config.addContent(_http_server);
			}

			for (int i = 0; i < httpDirectories.length; i++) {
				Element directory = new Element("http_directory");
				Utils.setAttribute("url_path", httpDirectories[i].getText(0), directory);
				Utils.setAttribute("path", httpDirectories[i].getText(1), directory);
				if (httpDirectories[i].getText(0) != "") {
					_http_server.addContent(directory);
				}
			}
			_dom.setChanged(true);
		}
	}
	public void fillHttpDirectory(Table table) {
		table.removeAll();
		if (_http_server != null) {
			for (Iterator it = _http_server.getChildren("http_directory").iterator(); it.hasNext();) {
				Element e = (Element) it.next();
				TableItem directory = new TableItem(table, SWT.NONE);
				directory.setText(0, Utils.getAttributeValue("url_path", e));
				directory.setText(1, Utils.getAttributeValue("path", e));
			}
		}
	}


}
