package sos.scheduler.editor.doc.listeners;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;

public class ConnectionsListener {
    public static final String defaultName = "default";

    private DocumentationDom   _dom;

    private SettingsListener   _settings;

    private Element            _connection;

    private boolean            _newConn;


    public ConnectionsListener(DocumentationDom dom, Element parent) {
        _dom = dom;
        _settings = new SettingsListener(dom, parent);
    }


    public void checkSettings() {
        _settings.checkSettings();
    }


    public void fillConnections(Table table) {
        table.removeAll();
        int index = 0;
        for (Iterator it = _settings.getSettingsElement().getChildren("connection", _dom.getNamespace()).iterator(); it
                .hasNext();) {
            Element con = (Element) it.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(Utils.getAttributeValue("name", con));
            if (con.equals(_connection))
                table.select(index);
            index++;
        }
    }


    public void setNewConnection() {
        _connection = new Element("connection", _dom.getNamespace());
        _newConn = true;
    }


    public boolean selectConnection(int index) {
        try {
            _connection = (Element) _settings.getSettingsElement().getChildren("connection", _dom.getNamespace()).get(
                    index);
            _newConn = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getName() {
        String name = Utils.getAttributeValue("name", _connection);
        return name.length() > 0 ? name : defaultName;
    }


    public Element getConnectionElement() {
        return _connection;
    }


    public void applyConnection(String name) {
        Utils.setAttribute("name", name, _connection);
        if (_newConn)
            _settings.getSettingsElement().addContent(_connection);
        _newConn = false;
        _dom.setChanged(true);
    }


    public boolean removeConnection(int index) {
        if (selectConnection(index)) {
            _connection.detach();
            _connection = null;
            _newConn = false;
            _dom.setChanged(true);
            return true;
        } else
            return false;
    }


    public boolean isNewConnection() {
        return _newConn;
    }

}
