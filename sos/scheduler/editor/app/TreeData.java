package sos.scheduler.editor.app;

import org.jdom.Element;

public class TreeData {
    private int     _type;

    private Element _element;

    private String  _helpKey;

    private String  _child;


    public TreeData() {

    }


    public TreeData(int type, Element element, String helpKey, String child) {
        _type = type;
        _element = element;
        _helpKey = helpKey;
        _child = child;
    }


    public TreeData(int type, Element element, String helpKey) {
        this(type, element, helpKey, null);
    }


    // public TreeData(int type, Element element) {
    // this(type, element, null);
    // }

    public int getType() {
        return _type;
    }


    public void setType(int type) {
        _type = type;
    }


    public Element getElement() {
        return _element;
    }


    public void setElement(Element element) {
        _element = element;
    }


    public String getHelpKey() {
        return _helpKey;
    }


    public void setHelpKey(String helpKey) {
        _helpKey = helpKey;
    }


    public String getChild() {
        return _child;
    }


    public void setChild(String child) {
        _child = child;
    }


    public boolean equals(int type) {
        return _type == type;
    }
}
