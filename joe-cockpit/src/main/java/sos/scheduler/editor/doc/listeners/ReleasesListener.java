package sos.scheduler.editor.doc.listeners;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ReleasesListener extends JobDocBaseListener<DocumentationDom>  {
	private Element				_release;
	private boolean				_newRelease	= false;

	public ReleasesListener(DocumentationDom dom, Element parent) {
		_dom = dom;
		_parent = parent;
	}

	public String getTitle() {
		return Utils.getElement("title", _release, _dom.getNamespace());
	}

	public String getID() {
		return Utils.getAttributeValue("id", _release);
	}

	public String getCreated() {
		return Utils.getAttributeValue("created", _release);
	}

	public String getModified() {
		return Utils.getAttributeValue("modified", _release);
	}

	public void fillAuthors(Table table) {
		table.removeAll();
		if (_release != null) {
			for (Iterator it = _release.getChildren("author", _dom.getNamespace()).iterator(); it.hasNext();) {
				Element author = (Element) it.next();
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, Utils.getAttributeValue("name", author));
				item.setText(1, Utils.getAttributeValue("email", author));
			}
		}
		Utils.setBackground(table, true);
	}

	public void newRelease() {
		_release = new Element("release", _dom.getNamespace());
		_parent.addContent(_release);
		_newRelease = true;
	}

	public Element getRelease() {
		return _release;
	}

	/*public void removeRelease() {
	    if (_release != null) {
	        _release.detach();
	        _release = null;
	        _dom.setChanged(true);
	    }
	}*/
	public void removeRelease(int index) {
		if (_parent != null) {
			_parent.getChildren("release", _dom.getNamespace()).remove(index);
			_dom.setChanged(true);
		}
	}

	public boolean selectRelease(int i) {
		try {
			_release = (Element) _parent.getChildren("release", _dom.getNamespace()).get(i);
			_newRelease = false;
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	public void applyRelease(String title, String id, String created, String modified, TableItem[] authors) {
		Utils.setAttribute("id", id, _release);
		Utils.setAttribute("created", created, _release);
		Utils.setAttribute("modified", modified, _release);
		Utils.setElement("title", title, false, _release, _dom.getNamespace(), _dom);
		/*
		_release.removeChildren("author", _dom.getNamespace());
		for (int i = 0; i < authors.length; i++) {
		    Element author = new Element("author", _dom.getNamespace());
		    Utils.setAttribute("name", authors[i].getText(0), author);
		    Utils.setAttribute("email", authors[i].getText(1), author);
		    _release.addContent(author);
		}

		if (_newRelease && !_parent.getContent().contains(_release)) {        	
			_parent.addContent(_release);        	
		}
		*/
		_newRelease = false;
		_dom.setChanged(true);
	}

	public boolean isNewRelease() {
		return _newRelease;
	}

	public void fillReleases(Table table) {
		table.removeAll();
		int index = 0;
		for (Iterator it = _parent.getChildren("release", _dom.getNamespace()).iterator(); it.hasNext();) {
			Element release = (Element) it.next();
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Utils.getAttributeValue("id", release));
			item.setText(1, Utils.getElement("title", release, _dom.getNamespace()));
			item.setText(2, Utils.getAttributeValue("created", release));
			item.setText(3, Utils.getAttributeValue("modified", release));
			if (release.equals(_release))
				table.select(index);
			index++;
		}
		Utils.setBackground(table, true);
	}
}
