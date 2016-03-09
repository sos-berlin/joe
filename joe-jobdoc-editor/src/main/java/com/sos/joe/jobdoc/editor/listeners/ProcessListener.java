package com.sos.joe.jobdoc.editor.listeners;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jdom.Content;
import org.jdom.Element;

import com.sos.joe.xml.Utils;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ProcessListener extends JobDocBaseListener<DocumentationDom> {

    Element _job;
    Element _process;

    public ProcessListener(DocumentationDom dom, Element job) {
        _dom = dom;
        _job = job;
    }

    public boolean isProcess() {
        boolean script = _job.getChild("script", _dom.getNamespace()) != null;
        _process = _job.getChild("process", _dom.getNamespace());
        if (!script && _process == null)
            setProcess();
        return _process != null;
    }

    public void setProcess() {
        _job.removeChild("script", _dom.getNamespace());
        _process = new Element("process", _dom.getNamespace());
        _job.addContent(_process);
        _dom.setChanged(true);
    }

    public String getFile() {
        return Utils.getAttributeValue("file", _process);
    }

    public void setFile(String file) {
        Utils.setAttribute("file", file, _process, _dom);
    }

    public String getParam() {
        return Utils.getAttributeValue("param", _process);
    }

    public void setParam(String param) {
        Utils.setAttribute("param", param, _process, _dom);
    }

    public String getLog() {
        return Utils.getAttributeValue("log", _process);
    }

    public void setLog(String log) {
        Utils.setAttribute("log", log, _process, _dom);
    }

    public void applyParam(String name, String value) {
        Element env = _process.getChild("environment", _dom.getNamespace());
        if (env == null) {
            env = new Element("environment", _dom.getNamespace());
            _process.addContent(env);
        }
        Element var = getVariable(name, env);
        if (var == null) {
            var = new Element("variable", _dom.getNamespace());
            env.addContent(var);
        }
        var.setAttribute("name", name);
        var.setAttribute("value", value);
        _dom.setChanged(true);
    }

    private Element getVariable(String name, Element env) {
        for (Iterator it = env.getContent().iterator(); it.hasNext();) {
            Content o = (Content) it.next();
            if (o instanceof Element && Utils.getAttributeValue("name", (Element) o).equals(name))
                return (Element) o;
        }
        return null;
    }

    public void removeVariable(String name) {
        Element env = _process.getChild("environment", _dom.getNamespace());
        if (env == null)
            return;
        Element var = getVariable(name, env);
        if (var != null)
            var.detach();
        if (env.getChildren().size() == 0)
            env.detach();
        _dom.setChanged(true);
    }

    public void fillTable(Table table) {
        table.removeAll();
        Element env = _process.getChild("environment", _dom.getNamespace());
        if (env != null) {
            for (Iterator it = env.getContent().iterator(); it.hasNext();) {
                Content o = (Content) it.next();
                if (o instanceof Element) {
                    Element var = (Element) o;
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setText(0, Utils.getAttributeValue("name", var));
                    item.setText(1, Utils.getAttributeValue("value", var));
                }
            }
        }
    }
}
