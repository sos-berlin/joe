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

public class BaseListener {
	private DomParser _dom;

	private Element _config;

	private Element _baseFile;

	private List _list;

	public BaseListener(DomParser dom) throws JDOMException {
		_dom = dom;
		_config = _dom.getRoot().getChild("config");
		_list = _config.getChildren("base");
	}

	public String getComment() {
		return Utils.getAttributeValue("__comment__", _baseFile);
	}
	
	public String[] getFiles() {
		Iterator it = _list.iterator();
		String[] files = new String[_list.size()];
		int index = 0;
		while (it.hasNext()) {
			String file = ((Element) it.next()).getAttributeValue("file");
			if (file == null)
				file = "UNKNOWN FILE";
			files[index++] = file;
		}
		return files;
	}
	
	public void fillTable(Table table) {
		table.removeAll();
		for(Iterator it = _list.iterator(); it.hasNext(); ) {
			Element e = (Element)it.next();
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Utils.getAttributeValue("file", e));
			String comment = Utils.getAttributeValue("__comment__", e);
			if(comment.indexOf("\n") > -1)
				comment = comment.substring(0, comment.indexOf("\n") - 1) + "..."; 
			item.setText(1, comment);
		}
	}

	public void selectBaseFile(int index) {
		if (index >= 0 && index < _list.size())
			_baseFile = (Element) _list.get(index);
		else
			_baseFile = null;
	}

	public void newBaseFile() {
		_baseFile = new Element("base");
	}

	public void applyBaseFile(String file, String comment) {
		_baseFile.setAttribute("file", file);
		_baseFile.setAttribute("__comment__", comment);
		if (!_list.contains(_baseFile))
			_list.add(_baseFile);
		_dom.setChanged(true);
	}

	public void removeBaseFile(int index) {
		if (index >= 0 && index < _list.size())
			_list.remove(index);
		_dom.setChanged(true);
	}

	public String getFile() {
		return Utils.getAttributeValue("file", _baseFile);
	}

}
