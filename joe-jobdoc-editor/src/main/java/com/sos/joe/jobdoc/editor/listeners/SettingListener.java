package com.sos.joe.jobdoc.editor.listeners;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class SettingListener extends JobDocBaseListener<DocumentationDom> {

    private final static Logger LOGGER = Logger.getLogger(SettingListener.class);
    private Element _setting;
    private boolean _newSetting;
    private static final String[] _types = { "", "integer", "double", "float", "string", "boolean", "clob", "blob" };

    public SettingListener(DocumentationDom dom, Element parent) {
        _dom = dom;
        _parent = parent;
    }

    public void fillSettings(Table table) {
        table.removeAll();
        int index = 0;
        for (Iterator it = _parent.getChildren("setting", _dom.getNamespace()).iterator(); it.hasNext();) {
            Element setting = (Element) it.next();
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, getAttributeValue("name", setting));
            item.setText(1, getAttributeValue("default_value", setting));
            item.setText(2, getAttributeValue("type", setting));
            item.setText(3, getBooleanValue("required", setting) ? "yes" : "no");
            item.setText(4, getAttributeValue("reference", setting));
            item.setText(5, getAttributeValue("id", setting));
            if (setting.equals(_setting))
                table.select(index);
            index++;
        }
    }

    public void setNewSetting() {
        _setting = new Element("setting", _dom.getNamespace());
        _newSetting = true;
    }

    public boolean selectSetting(int index) {
        try {
            _setting = (Element) _parent.getChildren("setting", _dom.getNamespace()).get(index);
            _newSetting = false;
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public String getName() {
        return getAttributeValue("name", _setting);
    }

    public String getDefaultValue() {
        return getAttributeValue("default_value", _setting);
    }

    public String getID() {
        return getAttributeValue("id", _setting);
    }

    public boolean isRequired() {
        return getBooleanValue("required", _setting);
    }

    public String getReference() {
        return getAttributeValue("reference", _setting);
    }

    public String getType() {
        return getAttributeValue("type", _setting);
    }

    public String[] getTypes() {
        return _types;
    }

    public Element getSettingElement() {
        return _setting;
    }

    public boolean isNewSetting() {
        return _newSetting;
    }

    public String[] getReferences(String ownID) {
        return DocumentationListener.getIDs(_dom, ownID);
    }

    public void applySetting(String name, String defaultValue, String id, boolean required, String reference, String type) {
        setAttribute("name", name, _setting);
        setAttribute("default_value", defaultValue, _setting);
        setAttribute("id", id, _setting);
        setBoolean("required", required, _setting);
        setAttribute("reference", DocumentationListener.getID(reference), _setting);
        setAttribute("type", type, _setting);
        if (_newSetting) {
            _parent.addContent(_setting);
        }
        _newSetting = false;
        _dom.setChanged(true);
    }

    public boolean removeSetting(int index) {
        if (selectSetting(index)) {
            _setting.detach();
            _setting = null;
            _newSetting = false;
            _dom.setChanged(true);
            return true;
        } else {
            return false;
        }
    }
}
