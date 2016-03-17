package com.sos.joe.jobdoc.editor.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.jdom.Element;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class IncludeFilesListener extends JobDocBaseListener<DocumentationDom> {

    boolean _changes = false;

    public IncludeFilesListener(DocumentationDom dom, Element parent) {
        _dom = dom;
        _parent = parent;
    }

    public String[] getIncludes() {
        if (_parent != null) {
            List includeList = _parent.getChildren("include", _dom.getNamespace());
            ArrayList files = new ArrayList();
            for (Iterator it = includeList.iterator(); it.hasNext();) {
                Element include = (Element) it.next();
                String file = include.getAttributeValue("file");
                files.add(file == null ? "" : file);
            }
            return (String[]) files.toArray(new String[] {});
        } else {
            return new String[0];
        }
    }

    public void saveIncludes(String[] includes) {
        if (_changes) {
            _parent.removeChildren("include", _dom.getNamespace());
            for (int i = 0; i < includes.length; i++) {
                _parent.addContent(new Element("include", _dom.getNamespace()).setAttribute("file", includes[i]));
            }
        }
    }

    public boolean exists(String file, String[] items) {
        for (int i = 0; i < items.length; i++) {
            if (file.equals(items[i])) {
                ErrorLog.message(Messages.getString("include.fileExists"), SWT.ICON_INFORMATION);
                return true;
            }
        }
        return false;
    }

    public void setChanges(boolean changes) {
        _changes = changes;
        _dom.setChanged(true);
    }
    
}