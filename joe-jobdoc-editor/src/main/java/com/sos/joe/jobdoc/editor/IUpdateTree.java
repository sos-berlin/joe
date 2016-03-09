package com.sos.joe.jobdoc.editor;

import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

public interface IUpdateTree {

    public void fillProfiles();

    public void fillConnections();

    public void fillApplications(TreeItem parent, Element element, boolean expand);

    public void fillSections(TreeItem parent, Element element, boolean expand);
}
