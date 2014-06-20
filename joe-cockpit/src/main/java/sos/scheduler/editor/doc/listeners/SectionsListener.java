package sos.scheduler.editor.doc.listeners;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class SectionsListener extends JobDocBaseListener<DocumentationDom> {

	private Element				_section;
	private boolean				_newSection	= false;

	public SectionsListener(DocumentationDom dom, Element parent) {
		_dom = dom;
		_parent = parent;
	}

	public void fillSections(Table table) {
		table.removeAll();
		int index = 0;
		for (Iterator it = _parent.getChildren("section", _dom.getNamespace()).iterator(); it.hasNext();) {
			Element section = (Element) it.next();
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Utils.getAttributeValue("name", section));
			item.setText(1, Utils.getAttributeValue("reference", section));
			item.setText(2, Utils.getAttributeValue("id", section));
			if (section.equals(_section))
				table.select(index);
			index++;
		}
	}

	public void setNewSection() {
		_section = new Element("section", _dom.getNamespace());
		_newSection = true;
	}

	public boolean selectSection(int index) {
		try {
			_section = (Element) _parent.getChildren("section", _dom.getNamespace()).get(index);
			_newSection = false;
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getName() {
		return Utils.getAttributeValue("name", _section);
	}

	public String getID() {
		return Utils.getAttributeValue("id", _section);
	}

	public String getReference() {
		return Utils.getAttributeValue("reference", _section);
	}

	public String[] getReferences(String ownID) {
		return DocumentationListener.getIDs(_dom, ownID);
	}

	public void applySection(String name, String id, String reference) {
		setAttribute("name", name, _section);
		setAttribute("id", id, _section);
		setAttribute("reference", DocumentationListener.getID(reference), _section);
		if (_newSection)
			_parent.addContent(_section);
		_newSection = false;
		_dom.setChanged(true);
	}

	public void removeSection(int index) {
		if (selectSection(index)) {
			_section.detach();
			_section = null;
			_newSection = false;
			_dom.setChanged(true);
		}
	}
}
