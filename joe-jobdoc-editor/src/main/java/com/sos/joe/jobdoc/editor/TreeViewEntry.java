package com.sos.joe.jobdoc.editor;

/**
 * 
 */
import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.misc.ResourceManager;

public class TreeViewEntry {

    // @SuppressWarnings("unused")
    // private final Logger logger = Logger.getLogger(TreeViewEntry.class);
    public final String conSVNVersion = "$Id: JadeTreeViewEntry.java 22824 2014-03-10 14:40:40Z kb $";
    private enuTreeItemType enuType = enuTreeItemType.IsRoot;

    public enum enuTreeItemType {
        IsRoot, isDirectory, isFile;
    }

    private File objFile = null;

    public void setFile(final File objF) {
        objFile = objF;
    }

    public File getFile() {
        return objFile;
    }

    public enuTreeItemType getType() {
        return enuType;
    }

    // private final SectionsHandler group;
    public TreeViewEntry(final enuTreeItemType iType) {
        enuType = iType;
    }

    public boolean isFile() {
        return enuType == enuTreeItemType.isFile;
    }

    public String getName() {
        String strR = "";
        switch (enuType) {
        case IsRoot:
            strR = objFile.getName();
            break;
        case isDirectory:
            strR = objFile.getName();
            break;
        case isFile:
        default:
            strR = objFile.getName();
            break;
        }
        return strR;
    }

    public String getTitle() {
        String strT = this.getName();
        // if (strT2.length() > 0) {
        // strT = strT + " - " + strT2;
        // }
        return strT;
    }

    public Image getImage() {
        Image imgR = null;
        String strB = JOEConstants.JOE_IMAGES;
        switch (enuType) {
        case IsRoot:
            imgR = ResourceManager.getImageFromResource(strB + "icon_directory.gif");
            break;
        case isDirectory:
            imgR = ResourceManager.getImageFromResource(strB + "icon_directory.gif");
            break;
        case isFile:
            imgR = ResourceManager.getImageFromResource(strB + "icon_file.gif");
            break;
        default:
            imgR = null;
            break;
        }
        return imgR;
    }

    public void selectChild() {
        if (objTreeItem != null) {
            objTreeItem.getParent().setSelection(objTreeItem);
        }
    }

    TreeItem objTreeItem = null;

    public void setTreeItem(final TreeItem pobjTreeItem) {
        objTreeItem = pobjTreeItem;
    }
}
