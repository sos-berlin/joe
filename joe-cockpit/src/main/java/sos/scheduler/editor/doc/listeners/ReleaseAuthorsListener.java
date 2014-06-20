package sos.scheduler.editor.doc.listeners;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ReleaseAuthorsListener extends JobDocBaseListener<DocumentationDom> {
	private Element				_release;

	public ReleaseAuthorsListener(DocumentationDom dom, Element release) {
		_dom = dom;
		_release = release;
	}

	public void removeAuthor(Element elem) {
		if (_release != null) {
			_release.getContent().remove(elem);
			_dom.setChanged(true);
		}
	}

	public void fillAuthors(Table table) {
		table.removeAll();
		if (_release != null) {
			for (Iterator it = _release.getChildren("author", _dom.getNamespace()).iterator(); it.hasNext();) {
				Element author = (Element) it.next();
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, Utils.getAttributeValue("name", author));
				item.setText(1, Utils.getAttributeValue("email", author));
				item.setData(author);
			}
		}
		Utils.setBackground(table, true);
	}

	public void applyRelease(Table table) {
		TableItem[] authors = table.getItems();
		_release.removeChildren("author", _dom.getNamespace());
		for (int i = 0; i < authors.length; i++) {
			Element author = new Element("author", _dom.getNamespace());
			Utils.setAttribute("name", authors[i].getText(0), author);
			Utils.setAttribute("email", authors[i].getText(1), author);
			_release.addContent(author);
		}
		fillAuthors(table);
		_dom.setChanged(true);
	}
}
