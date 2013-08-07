package sos.scheduler.editor.app;

import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

public class TreeData extends JSObjectElement {
    private String  _helpKey;
    private String  _child;

    private TreeItem objTreeItem = null;

	private static final String	conItemDataKeyCOPY_ELEMENT			= "copy_element";

    public TreeData() {
    }


    public TreeData(final int type, final Element element, final String helpKey, final String child) {
    	super(type, element);
        _helpKey = helpKey;
        _child = child;
    }

    public TreeData(final int type, final Element element, final String helpKey) {
        this(type, element, helpKey, null);
    }

    public TreeData(final TreeItem pobjTreeItem, final int type, final Element element, final String helpKey) {
        this(type, element, helpKey, null);

        objTreeItem = pobjTreeItem;
        pobjTreeItem.setData(this);
        pobjTreeItem.setText(getNameAndTitle());
        pobjTreeItem.setData(conItemDataKeyCOPY_ELEMENT, element);
    }


    public String getHelpKey() {
        return _helpKey;
    }


    public void setHelpKey(final String helpKey) {
        _helpKey = helpKey;
    }


    public String getChild() {
        return _child;
    }


    public void setChild(final String child) {
        _child = child;
    }
}
