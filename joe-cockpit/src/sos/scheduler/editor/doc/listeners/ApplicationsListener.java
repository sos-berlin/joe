package sos.scheduler.editor.doc.listeners;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;

public class ApplicationsListener {
    private DocumentationDom _dom;

    private Element          _parent;

    private Element          _application;

    private boolean          _newApp;


    public ApplicationsListener(DocumentationDom dom, Element parent) {
        _dom = dom;
        _parent = parent;
    }


    public void fillApps(Table table) {
        table.removeAll();
        int index = 0;
        for (Iterator it = _parent.getChildren("application", _dom.getNamespace()).iterator(); it.hasNext();) {
            Element app = (Element) it.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, Utils.getAttributeValue("name", app));
            item.setText(1, Utils.getAttributeValue("id", app));
            item.setText(2, Utils.getAttributeValue("reference", app));
            if (app.equals(_application))
                table.select(index);
            index++;
        }
    }


    public void setNewApp() {
        _application = new Element("application", _dom.getNamespace());
        _newApp = true;
    }


    public boolean selectApp(int index) {
        try {
            _application = (Element) _parent.getChildren("application", _dom.getNamespace()).get(index);
            _newApp = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getName() {
        return Utils.getAttributeValue("name", _application);
    }


    public String getID() {
        return Utils.getAttributeValue("id", _application);
    }


    public String getReference() {
        return Utils.getAttributeValue("reference", _application);
    }


    public String[] getReferences(String ownID) {
        return DocumentationListener.getIDs(_dom, ownID);
    }


    public void applyApp(String name, String id, String reference) {
        Utils.setAttribute("name", name, _application);
        Utils.setAttribute("id", id, _application);
        Utils.setAttribute("reference", DocumentationListener.getID(reference), _application);
        if (_newApp)
            _parent.addContent(_application);
        _newApp = false;
        _dom.setChanged(true);
    }


    public boolean removeApp(int index) {
        if (selectApp(index)) {
            _application.detach();
            _application = null;
            _newApp = false;
            _dom.setChanged(true);
            return true;
        } else
            return false;
    }
}
