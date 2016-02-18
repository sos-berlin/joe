package com.sos.joe.globals.misc;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import com.sos.joe.globals.JOEConstants;
import com.sos.scheduler.model.SchedulerHotFolder;
import com.sos.scheduler.model.objects.JSObjBase;
import com.sos.scheduler.model.objects.JSObjJob;

public class TreeData {
	@SuppressWarnings("unused") private final String		conClassName		= this.getClass().getSimpleName();
	@SuppressWarnings("unused") private static final String	conSVNVersion		= "$Id: TreeData.java 24050 2014-05-01 08:29:09Z kb $";
	@SuppressWarnings("unused") private final Logger		logger				= Logger.getLogger(this.getClass());
	private int												_type;
	private Element											_element;
	private String											_helpKey;
	private String											_child;
	private TreeItem										objTreeItem			= null;
	Object													objHotFolderObject	= null;

	public TreeData() {
	}

	public TreeItem getTreeItem() {
		return objTreeItem;
	}

	public TreeItem setTreeItem(final TreeItem pobjTreeItem) {
		objTreeItem = pobjTreeItem;
		return objTreeItem;
	}

	public JSObjJob getJob() {
		return (JSObjJob) objHotFolderObject;
	}

	public Object getObject() {
		return objHotFolderObject;
	}

	@Deprecated public TreeData(final int type, final Element element, final String helpKey, final String child) {
		_type = type;
		_element = element;
		_helpKey = helpKey;
		_child = child;
	}

	public void UpdateFont(final boolean isBold) {
		if (objTreeItem == null) {
			return;
		}
		FontData fontDatas[] = objTreeItem.getFont().getFontData();
		FontData data = fontDatas[0];
		Font font = null;
		if (isBold) {
			font = new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.BOLD);
		}
		else {
			font = new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.NONE);
		}
		objTreeItem.setFont(font);
	}

	@Deprecated public TreeData(final int type, final Element element, final String helpKey) {
		this(type, element, helpKey, null);
	}

	public TreeData(final int type, final Object pobjHotFolderObject, final String helpKey) {
		_helpKey = helpKey;
		_type = type;
		objHotFolderObject = pobjHotFolderObject;
	}

	public TreeData(final int type, final Object pobjHotFolderObject, final String helpKey, final String child) {
		_helpKey = helpKey;
		_type = type;
		objHotFolderObject = pobjHotFolderObject;
		_child = child;
	}

	public boolean isFolder() {
		boolean flgR = false;
		flgR = (objHotFolderObject instanceof SchedulerHotFolder) || _type == JOEConstants.ROOT_FOLDER || _type == JOEConstants.SUB_FOLDER;
		return flgR;
	}

	public boolean isInternalNode () {
		boolean flgR = false;
		flgR = (isFolder() == false && objHotFolderObject == null);
		return flgR;
	}
	public int getType() {
		return _type;
	}

	public void setType(final int type) {
		_type = type;
	}

	@Deprecated public Element getElement() {
		return _element;
	}

	@Deprecated public void setElement(final Element element) {
		_element = element;
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

	public boolean equals(final int type) {
		return _type == type;
	}
	
	public int hashCode(){
	    return _type;
	}
	 

	public void UpdateTreeItem() {
		JSObjBase objO = (JSObjBase) objHotFolderObject;
		objTreeItem.setText(objO.getObjectNameAndTitle());
	}

	public boolean TypeEqualTo(final int pintType) {
		return pintType == _type;
	}

	public boolean NameEqualTo(final String pstrName) {
		return pstrName.equalsIgnoreCase(getName());
		//		return false;
	}

	public void deleteTreeItem() {
		Tree objTr = objTreeItem.getParent();
		objTreeItem.dispose();
	}

	public JSObjBase getLiveObject() {
		JSObjBase objO = (JSObjBase) objHotFolderObject;
		return objO;
	}

	public String getName() {
		JSObjBase objO = (JSObjBase) objHotFolderObject;
		if (objO != null){
		   return objO.getObjectName();
		}else{
		   return "";	
		}
	}
}
