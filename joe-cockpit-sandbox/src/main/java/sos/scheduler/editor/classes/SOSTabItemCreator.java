/**
 *
 */
package sos.scheduler.editor.classes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import sos.scheduler.editor.app.SOSMsgJOE;

/**
 * @author KB
 *
 */
public class SOSTabItemCreator {
	private CTabItem	objTabItem				= null;
	private CTabFolder	objParentTab			= null;
	private Composite	tabItemNodesComposite	= null;

	public SOSTabItemCreator(final CTabItem ptabItem, final int pintStyle) {
		objTabItem = ptabItem;
	}

	public SOSTabItemCreator(final CTabItem ptabItem) {
		this(ptabItem, SWT.NONE);
	}

	public SOSTabItemCreator(final CTabFolder pobjParent) {
		objParentTab = pobjParent;
		objParentTab.setSimple(false);
	}

	public CTabItem getItem(final SOSMsgJOE pobjMsg, final int pintIndex) {
		objTabItem = pobjMsg.Control(new CTabItem(objParentTab, SWT.NONE));
		objTabItem.setData("TabKeyIndex", pintIndex);
		setResizableV(objTabItem.getControl());
		tabItemNodesComposite = new Composite(objParentTab, SWT.NONE);
		tabItemNodesComposite.setLayout(new GridLayout(1, true));
		setResizableV(tabItemNodesComposite);
		objTabItem.setControl(tabItemNodesComposite);
		return objTabItem;
	}

	public Composite getComposite() {
		setResizableV(tabItemNodesComposite);
		return tabItemNodesComposite;
	}

	protected void setResizableV(final Control objControl) {
		if (objControl != null) {
			boolean flgGrapVerticalspace = true;
			objControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, flgGrapVerticalspace));
		}
	}

}
