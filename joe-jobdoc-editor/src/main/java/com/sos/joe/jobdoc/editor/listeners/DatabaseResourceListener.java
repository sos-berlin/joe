package com.sos.joe.jobdoc.editor.listeners;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import com.sos.joe.xml.Utils;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class DatabaseResourceListener extends JobDocBaseListener<DocumentationDom> {

    private static final String DEFAULT_NAME = "[unknown]";
    private Element _resources;
    private Element _database;
    private Element _resource;
    private boolean _newDatabase;
    private boolean _newResource;
    private String[] _types = { "table", "view", "trigger", "procedure", "function", "java", "index", "other" };

    public DatabaseResourceListener(DocumentationDom dom, Element database) {
        _dom = dom;
        _database = database;
    }

    public void fillDatabases(Table table) {
        table.removeAll();
        int index = 0;
        if (_resources != null) {
            for (Iterator it = _resources.getChildren("database", _dom.getNamespace()).iterator(); it.hasNext();) {
                Element database = (Element) it.next();
                TableItem item = new TableItem(table, SWT.NONE);
                String name = Utils.getAttributeValue("name", database);
                item.setText(0, !"".equals(name) ? name : DEFAULT_NAME);
                item.setText(1, Utils.getBooleanValue("required", database) ? "yes" : "no");
                if (database.equals(_database)) {
                    table.select(index);
                }
                index++;
            }
        }
    }

    public void setNewDatabase() {
        _database = new Element("database", _dom.getNamespace());
        Utils.setAttribute("required", false, _database);
        _newDatabase = true;
    }

    public boolean setDatabase(int index) {
        if (_resources != null) {
            List dbs = _resources.getChildren("database", _dom.getNamespace());
            try {
                _database = (Element) dbs.get(index);
                _newDatabase = false;
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getDBName() {
        return Utils.getAttributeValue("name", _database);
    }

    public boolean isRequired() {
        return Utils.getBooleanValue("required", _database);
    }

    public void fillResources(Table table) {
        table.removeAll();
        int index = 0;
        if (_database != null) {
            for (Iterator it = _database.getChildren("resource", _dom.getNamespace()).iterator(); it.hasNext();) {
                Element resource = (Element) it.next();
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, Utils.getAttributeValue("name", resource));
                item.setText(1, Utils.getAttributeValue("type", resource));
                if (resource.equals(_resource)) {
                    table.select(index);
                }
                index++;
            }
        }
    }

    public void setNewResource() {
        _resource = new Element("resource", _dom.getNamespace());
        Utils.setAttribute("type", _types[0], _resource);
        _newResource = true;
    }

    public boolean setResource(int index) {
        if (_database != null) {
            List dbs = _database.getChildren("resource", _dom.getNamespace());
            try {
                _resource = (Element) dbs.get(index);
                _newResource = false;
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public Element getResource() {
        return _resource;
    }

    public String getName() {
        return Utils.getAttributeValue("name", _resource);
    }

    public String getType() {
        return Utils.getAttributeValue("type", _resource);
    }

    public void applyResource(String name, String type) {
        Utils.setAttribute("name", name, _resource);
        Utils.setAttribute("type", type, _resource);
        if (_newResource) {
            _database.addContent(_resource);
        } else {
            _dom.setChanged(true);
        }
        _newResource = false;
    }

    public void removeResource(int index) {
        if (setResource(index)) {
            _resource.detach();
            _resource = null;
            _dom.setChanged(true);
        }
    }

    public String[] getTypes() {
        return _types;
    }

    public boolean isNewResource() {
        return _newResource;
    }

    public boolean isNewDatabase() {
        return _newDatabase;
    }

}