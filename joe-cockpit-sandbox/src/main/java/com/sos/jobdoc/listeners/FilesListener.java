package com.sos.jobdoc.listeners;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Element;

import com.sos.jobdoc.DocumentationDom;

import sos.scheduler.editor.app.Utils;

public class FilesListener {
    private DocumentationDom _dom;

    private Element          _parent;

    private Element          _resources;

    private Element          _file;

    private String[]         _platforms = { "all", "win32", "win64", "linux32", "linux64", "solaris32", "solaris64",
            "hpux64"                   };

    private String[]         _types     = { "java", "binary", "other" };

    private boolean          _isNewFile = false;


    public FilesListener(DocumentationDom dom, Element parent) {
        _dom = dom;
        _parent = parent;
        _resources = _parent.getChild("resources", _dom.getNamespace());
    }


    public String[] getPlatforms() {
        return _platforms;
    }


    public String[] getTypes() {
        return _types;
    }


    public void fillFiles(Table table) {
        table.removeAll();
        int index = 0;
        if (_resources != null) {
            for (Iterator it = _resources.getChildren("file", _dom.getNamespace()).iterator(); it.hasNext();) {
                Element file = (Element) it.next();
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, Utils.getAttributeValue("file", file));
                item.setText(1, Utils.getAttributeValue("os", file));
                item.setText(2, Utils.getAttributeValue("type", file));
                item.setText(3, Utils.getAttributeValue("id", file));
                if (file.equals(_file))
                    table.select(index);
                index++;
            }
        }
    }


    public boolean selectFile(int index) {
        if (_resources != null) {
            try {
                _file = (Element) _resources.getChildren("file", _dom.getNamespace()).get(index);
                _isNewFile = false;
                return true;
            } catch (Exception e) {
                return false;
            }
        } else
            return false;
    }


    public void setNewFile() {
        _file = new Element("file", _dom.getNamespace());
        Utils.setAttribute("os", _platforms[0], _file);
        Utils.setAttribute("type", _types[0], _file);
        _isNewFile = true;
    }


    public String getFile() {
        return Utils.getAttributeValue("file", _file);
    }


    public String getID() {
        return Utils.getAttributeValue("id", _file);
    }


    public String getOS() {
        return Utils.getAttributeValue("os", _file);
    }


    public String getType() {
        return Utils.getAttributeValue("type", _file);
    }


    public void applyFile(String file, String id, String os, String type) {
        if (_resources == null)
            _resources = ResourcesListener.getResourcesElement(_dom, _parent);

        Utils.setAttribute("file", file, _file);
        Utils.setAttribute("id", id, _file);
        Utils.setAttribute("os", os, _file);
        Utils.setAttribute("type", type, _file);

        if (_isNewFile)
            _resources.addContent(_file);

        _isNewFile = false;
        _dom.setChanged(true);
    }


    public void removeFile(int index) {
        if (_resources != null && selectFile(index)) {
            _file.detach();
            _file = null;
            _dom.setChanged(true);
        }

        ResourcesListener.checkResources(_dom, _parent);
    }


    public Element getFileElement() {
        return _file;
    }


    public boolean isNewFile() {
        return _isNewFile;
    }
}
