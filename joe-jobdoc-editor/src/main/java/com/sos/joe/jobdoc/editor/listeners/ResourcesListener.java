package com.sos.joe.jobdoc.editor.listeners;

import org.jdom.Element;

import com.sos.joe.xml.Utils;

import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ResourcesListener extends JobDocBaseListener<DocumentationDom> {

    private Element _resources;
    private Element _memory;
    private Element _space;
    private String[] _units = { "KB", "MB", "GB", "TB" };

    public ResourcesListener(DocumentationDom dom, Element parent) {
        _dom = dom;
        _parent = parent;
        _resources = _parent.getChild("resources", _dom.getNamespace());
        if (_resources != null) {
            _memory = _resources.getChild("memory", _dom.getNamespace());
            _space = _resources.getChild("space", _dom.getNamespace());
        }
    }

    public static Element getResourcesElement(DocumentationDom dom, Element parent) {
        Element res = parent.getChild("resources", dom.getNamespace());
        if (res == null) {
            res = new Element("resources", dom.getNamespace());
            parent.addContent(res);
        }
        return res;
    }

    public static void checkResources(DocumentationDom dom, Element parent) {
        Element res = parent.getChild("resources", dom.getNamespace());
        if (res != null && res.getChildren().isEmpty()) {
            res.detach();
        }
    }

    public void setMemory(boolean enabled) {
        if (_resources == null) {
            _resources = getResourcesElement(_dom, _parent);
        }
        _memory = _resources.getChild("memory", _dom.getNamespace());
        if (enabled && _memory == null) {
            _memory = new Element("memory", _dom.getNamespace());
            _resources.addContent(_memory);
            _dom.setChanged(true);
        } else if (!enabled && _memory != null) {
            _memory.detach();
            _dom.setChanged(true);
            checkResources(_dom, _parent);
        }
    }

    public String getMemory() {
        return Utils.getAttributeValue("min", _memory);
    }

    public void setMemory(String memory) {
        Utils.setAttribute("min", memory, _memory, _dom);
    }

    public String[] getUnits() {
        return _units;
    }

    public String getMemoryUnit() {
        String unit = Utils.getAttributeValue("unit", _memory);
        if ("".equals(unit)) {
            unit = _units[1];
            Utils.setAttribute("unit", unit, _memory);
        }
        return unit;
    }

    public void setMemoryUnit(String unit) {
        Utils.setAttribute("unit", unit, _memory, _dom);
    }

    public Element getMemoryElement() {
        return _memory;
    }

    public boolean isMemory() {
        return _memory != null;
    }

    public void setSpace(boolean enabled) {
        if (_resources == null) {
            _resources = getResourcesElement(_dom, _parent);
        }
        _space = _resources.getChild("space", _dom.getNamespace());
        if (enabled && _space == null) {
            _space = new Element("space", _dom.getNamespace());
            _resources.addContent(_space);
            _dom.setChanged(true);
        } else if (!enabled && _space != null) {
            _space.detach();
            _dom.setChanged(true);
            checkResources(_dom, _parent);
        }
    }

    public String getSpace() {
        return Utils.getAttributeValue("min", _space);
    }

    public void setSpace(String space) {
        Utils.setAttribute("min", space, _space, _dom);
    }

    public String getSpaceUnit() {
        String unit = Utils.getAttributeValue("unit", _space);
        if ("".equals(unit)) {
            unit = _units[1];
            Utils.setAttribute("unit", unit, _space);
        }
        return unit;
    }

    public void setSpaceUnit(String unit) {
        Utils.setAttribute("unit", unit, _space, _dom);
    }

    public Element getSpaceElement() {
        return _space;
    }

    public boolean isSpace() {
        return _space != null;
    }

}