package sos.scheduler.editor.app;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import sos.scheduler.editor.conf.forms.SchedulerForm;
import sos.scheduler.editor.conf.listeners.DaysListener;
import sos.scheduler.editor.conf.listeners.JobsListener;

import com.sos.VirtualFileSystem.Interfaces.ISOSVirtualFile;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSMsgJOE;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.interfaces.IContainer;
import com.sos.joe.xml.DomParser;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.scheduler.model.objects.JSObjBase;

public class TreeMenu {
	private final DomParser		_dom					= null;
	private Tree				objLiveFolderTreeViewer	= null;
	private Menu				objTreeItemMenu			= null;
	private static TreeData		_copy					= null;
	private static int			_type					= -1;
	private final SchedulerForm	objSchedulerForm		= null;
	// TODO I18N
	private static final String	EDIT_XML				= "EditXML";
	private static final String	SHOW_XML				= "ShowXML";
	private static final String	SHOW_PROPERTIES			= "Properties";
	//private static final String SHOW_INFO = "Show Info";
	private static final String	COPY					= "Copy";
	private static final String	COPY_TO_CLIPBOARD		= "XMLtoClipboard";
	private static final String	PASTE					= "Paste";
	//	private static final String	DELETE_HOT_HOLDER_FILE	= "Delete Hot Folder File";
	private static final String	NEW						= "New";
	private static final String	DELETE					= "Delete";
	private static final String	SAVE_OBJECT				= "Save";
	private static final String	RENAME					= "Rename";
	private static final String	SAVE_OBJECT_AS			= "SaveAs";
	//
	private abstract class MenueItemListenerBase implements Listener {
		protected String								name		= "???";
		@SuppressWarnings("unused") protected JSObjBase	objO		= null;
		protected String								strI18NKey	= "";

		protected MenueItemListenerBase(final String pstrI18NKey) {
			this.strI18NKey = pstrI18NKey;
		}

		@Override public void handleEvent(final Event e) {
			TreeData elem = getItemElement();
			try {
				if (elem != null) {
					objO = elem.getLiveObject();
					name = elem.getName();
					if (doIt(elem) == true) {
						String strT = String.format("Operation '%1$s' on Object %2$s successfull completed.", elem.getTreeItem().getText(), name);
						JOEMainWindow.setStatusLine(strT);
					}
				}
				else {
					// Message: failure
				}
			}
			catch (Exception e1) {
				new ErrorLog(String.format("Operation '%1$s' failed on Object %2$s ", elem.getTreeItem().getText(), name), e1);
			}
		}

		protected abstract boolean doIt(final TreeData pobjElem) throws Exception;

		protected boolean askQuestion(final String pstrQuestion) {
			int c = JOEMainWindow.message(pstrQuestion, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			if (c != SWT.YES) {
				return false;
			}
			return true;
		}

		@SuppressWarnings("unused") public String getI18NKey() {
			return strI18NKey;
		}

		public MenuItem createMenuItem(final Menu pobjMenu) {
			MenuItem item = new MenuItem(pobjMenu, SWT.PUSH);
			item.addListener(SWT.Selection, this);
			SOSMsgJOE objMsg = new SOSMsgJOE(strI18NKey);
			String strT = objMsg.label();
			if (strT != null) {
				//				TreeData objTD = getTreeData();
				item.setText(String.format(strT, ""));
			}
			else {
				item.setText(strI18NKey);
			}
			String strIcon = objMsg.icon();
			item.setImage(ResourceManager.getImageFromResource(strIcon));
			item.setData("key", strI18NKey);
			item.setEnabled(false);
			return item;
		}
	}
	private class ShowXMLListener extends MenueItemListenerBase {
		protected ShowXMLListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			JSObjBase objO = pobjElem.getLiveObject();
			Utils.showClipboard(objO.marshal(), objLiveFolderTreeViewer.getShell(), true, objO.getObjectName());
			return true;
		}
	}
	private class EditXMLListener extends MenueItemListenerBase {
		protected EditXMLListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			JSObjBase objO = pobjElem.getLiveObject();
			String newXML = Utils.showClipboard(objO.marshal(), objLiveFolderTreeViewer.getShell(), true, objO.getObjectName());
			if (newXML != null) {
				objO.unMarshal(newXML);
				objO.save();
				return true;
			}
			else {
				return false;
			}
		}
	}
	private class SaveListener extends MenueItemListenerBase {
		protected SaveListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			JSObjBase objO = pobjElem.getLiveObject();
			if (objO.isDirty()) {
				objO.save();
				return true;
			}
			else {
				return false;
			}
		}
	}
	private class PropertiesListener extends MenueItemListenerBase {
		protected PropertiesListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			String strProperties = String.format("Properties of object '%1$s'\n", name);
			JSObjBase objO = pobjElem.getLiveObject();
			ISOSVirtualFile objF = objO.getHotFolderSrc();
			strProperties += String.format("Path-name: %1$s \n", objF.getName());
			strProperties += String.format("Size: %1$s \n", objF.getFileSize() + " ");
			JOEMainWindow.message(strProperties, SWT.ICON_INFORMATION | SWT.OK);
			return true;
		}
	}
	private class RenameListener extends MenueItemListenerBase {
		protected RenameListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			String strNewFileName = "";
			// TODO ask for new name and check for duplicates
			pobjElem.getLiveObject().Rename(strNewFileName);
			return true;
		}
	}
	private class createNewObjectListener extends MenueItemListenerBase {
		protected createNewObjectListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			@SuppressWarnings("unused") TreeItem objTi = pobjElem.getTreeItem();
			//				String name = getParentItemName();
			int iType = pobjElem.getType();
			switch (iType) {
				case JOEConstants.JOBS:
					//						break;
				case JOEConstants.JOB:
					JobsListener objJobsListener = new JobsListener((SchedulerDom) _dom, objSchedulerForm);
					objJobsListener.newJob(false);
					break;
				case JOEConstants.JOB_CHAIN:
					break;
				case JOEConstants.JOB_CHAINS:
					break;
				case JOEConstants.ORDER:
					break;
				case JOEConstants.ORDERS:
					break;
				case JOEConstants.SCHEDULES:
					break;
				case JOEConstants.SCHEDULE:
					break;
				case JOEConstants.LOCKS:
					break;
				case JOEConstants.PROCESS_CLASSES:
					break;
				case JOEConstants.WEBSERVICES:
					break;
				case JOEConstants.MONITOR:
					break;
				case JOEConstants.MONITORS:
					break;
				default:
					break;
			}
			return true;
		}
	}
	private class DeleteListener extends MenueItemListenerBase {
		protected DeleteListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) throws Exception {
			if (askQuestion(String.format("Do you want to delete the object '%1$s' ?", name)) == true) {
				pobjElem.getLiveObject().getHotFolderSrc().deleteFile();
				pobjElem.deleteTreeItem();
				return true;
			}
			else {
				return false;
			}
		}
	}
	private class SaveAsListener extends MenueItemListenerBase {
		protected SaveAsListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			String strNewFileName = "";
			pobjElem.getLiveObject().saveAs(strNewFileName);
			return true;
		}
	}
	private class CopyListener extends MenueItemListenerBase {
		protected CopyListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			TreeData objTD = getTreeData();
			if (objTD != null) {
				_copy = objTD;
				_type = objTD.getType();
			}
			else {
				return false;
			}
			return true;
		}
	}
	private class PasteListener extends MenueItemListenerBase {
		protected PasteListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			//			String strNewFileName = "";
			//			pobjElem.getLiveObject().saveAs(strNewFileName);
			//			String name = pobjElem.getName();
			return true;
		}
	}
	private class CopyToClipboardListener extends MenueItemListenerBase {
		protected CopyToClipboardListener(String pstrI18NKey) {
			super(pstrI18NKey);
		}

		@Override protected boolean doIt(TreeData pobjElem) {
			TreeData elem = getTreeData();
			if (elem != null) {
				String xml = elem.getLiveObject().toXMLString();
				if (xml != null) {
					Utils.copyClipboard(xml, objLiveFolderTreeViewer.getDisplay());
				}
				else {
					new ErrorLog("can't get xml to clipboard");
					return false;
				}
			}
			return true;
		}
	}

	public TreeMenu(final Tree tree) {
		objLiveFolderTreeViewer = tree;
		createMenu();
	}

	public int message(final String message, final int style) {
		MessageBox mb = new MessageBox(objLiveFolderTreeViewer.getShell(), style);
		mb.setMessage(message);
		return mb.open();
	}

	private TreeData getTreeData() {
		TreeData objTreeData = null;
		if (objLiveFolderTreeViewer.getSelectionCount() > 0) {
			objTreeData = (TreeData) objLiveFolderTreeViewer.getSelection()[0].getData();
		}
		else {
		}
		return objTreeData;
	}

	private void createMenu() {
		//		TreeData objTD = getTreeData();
		objTreeItemMenu = new Menu(objLiveFolderTreeViewer.getShell(), SWT.POP_UP);
		objLiveFolderTreeViewer.setMenu(objTreeItemMenu);
		//		MenuItem item;
		{
			new createNewObjectListener(TreeMenu.NEW).createMenuItem(objTreeItemMenu);
			new MenuItem(objTreeItemMenu, SWT.SEPARATOR);
			new SaveListener(TreeMenu.SAVE_OBJECT).createMenuItem(objTreeItemMenu);
			new SaveAsListener(TreeMenu.SAVE_OBJECT_AS).createMenuItem(objTreeItemMenu);
			new RenameListener(TreeMenu.RENAME).createMenuItem(objTreeItemMenu);
			new MenuItem(objTreeItemMenu, SWT.SEPARATOR);
			new CopyListener(TreeMenu.COPY).createMenuItem(objTreeItemMenu);
			new PasteListener(TreeMenu.PASTE).createMenuItem(objTreeItemMenu);
			new DeleteListener(TreeMenu.DELETE).createMenuItem(objTreeItemMenu);
			new MenuItem(objTreeItemMenu, SWT.SEPARATOR);
			new CopyToClipboardListener(TreeMenu.COPY_TO_CLIPBOARD).createMenuItem(objTreeItemMenu);
			new EditXMLListener(TreeMenu.EDIT_XML).createMenuItem(objTreeItemMenu);
			new ShowXMLListener(TreeMenu.SHOW_XML).createMenuItem(objTreeItemMenu);
			new MenuItem(objTreeItemMenu, SWT.SEPARATOR);
			new PropertiesListener(TreeMenu.SHOW_PROPERTIES).createMenuItem(objTreeItemMenu);
		}
		objTreeItemMenu.addListener(SWT.Show, new Listener() {
			@Override public void handleEvent(final Event e) {
				try {
					TreeData objTreeData = getTreeData();
					for (MenuItem objMenuItem : objTreeItemMenu.getItems()) {
						objMenuItem.setEnabled(false);
					}
					if (objTreeData != null) {
						JSObjBase objJSO = objTreeData.getLiveObject();
						if (objJSO != null && objTreeData.isFolder() == false) {
							
							if (objJSO.isDirty() == true) {
								getMenuItem(TreeMenu.SAVE_OBJECT).setEnabled(true);
							}
							getMenuItem(TreeMenu.SAVE_OBJECT_AS).setEnabled(true);
							getMenuItem(TreeMenu.EDIT_XML).setEnabled(true);
							getMenuItem(TreeMenu.SHOW_XML).setEnabled(true);
							getMenuItem(TreeMenu.COPY_TO_CLIPBOARD).setEnabled(true);
							getMenuItem(TreeMenu.COPY).setEnabled(true);
							//						if (getParentItemName().length() > 0) {
							getMenuItem(TreeMenu.NEW).setEnabled(true);
							//						}
							if (getItemElement() != null) {
								getMenuItem(TreeMenu.DELETE).setEnabled(true);
							}
						}
						else {
							getMenuItem(TreeMenu.NEW).setEnabled(true);				
							getMenuItem(TreeMenu.COPY).setEnabled(true);
							}
						if (_copy != null) {
							MenuItem _paste = getMenuItem(TreeMenu.PASTE);
							_paste.setEnabled(true);
						}
						getMenuItem(TreeMenu.SHOW_PROPERTIES).setEnabled(true);
						getMenuItem(TreeMenu.RENAME).setEnabled(true);					}
				}
				catch (Exception e1) {
					new ErrorLog("Problems creating TreeView-Menue", e1);
				}
			}
		});
	}

	public Menu getMenu() {
		return objTreeItemMenu;
	}

	private Listener getInfoListener() {
		return new Listener() {
			@Override public void handleEvent(final Event e) {
				//				Element element = getElement();
				//				if (element != null) {
				//					try {
				//						String filename = _dom.transform(element);
				//						Program prog = Program.findProgram("html");
				//						if (prog != null)
				//							prog.execute(filename);
				//						else {
				//							filename = new File(filename).toURL().toString();
				//							Runtime.getRuntime().exec(Options.getBrowserExec(filename, null));
				//						}
				//					}
				//					catch (Exception ex) {
				//						new ErrorLog("error in " + SOSClassUtil.getMethodName(), ex);
				//					}
				//				}
			}
		};
	}

	//	private boolean existJobname(final Element jobs, final String jobname) {
	//		boolean retVal = false;
	//		java.util.List list = jobs.getChildren();
	//		for (int i = 0; i < list.size(); i++) {
	//			Element job = (Element) list.get(i);
	//			if (Utils.getAttributeValue("name", job).equalsIgnoreCase(jobname)) {
	//				retVal = true;
	//				break;
	//			}
	//		}
	//		return retVal;
	//	}
	//
	private void refreshTree(final String whichItem) {
		IContainer con = JOEMainWindow.getContainer();
		SchedulerForm sf = (SchedulerForm) con.getCurrentEditor();
		sf.updateTree(whichItem);
	}

	private MenuItem getMenuItem(final String name) {
		MenuItem[] items = objTreeItemMenu.getItems();
		for (MenuItem item : items) {
			if (item.getStyle() != SWT.SEPARATOR) {
				String strK = (String) item.getData("key");
				if (strK != null && strK.equalsIgnoreCase(name)) {
					return item;
				}
			}
		}
		return null;
	}

	private boolean isParent(String key, final String elemname) {
		// String[] split = key.split("\\.");
		String[] split = key.split("_@_");
		if (split.length > 1)
			key = split[0];
		String[] s = null;
		if (_dom.getDomOrders().get(key) != null)
			s = _dom.getDomOrders().get(key);
		else
			if (DaysListener.getDays().get(key) != null)
				// s =
				// (String[])DaysListener.getDays().get(key);
				return true; // group sind nicht definiert
			else
				s = getPossibleParent(key);
		for (int i = 0; s != null && i < s.length; i++) {
			if (s[i].equalsIgnoreCase(elemname)) {
				return true;
			}
		}
		return false;
	}

	// liefert mögliche Vaterknoten der key, falls diese nicht in dom.orders
	// steht
	private String[] getPossibleParent(final String key) {
		if (key.equals("jobs") || key.equals("monitor"))
			return new String[] { "job" };
		if (key.equals("schedules"))
			return new String[] { "schedule" };
		if (key.equals("job_chains"))
			return new String[] { "job_chain" };
		// weekdays monthdays ultimos
		else
			return new String[] {};
	}

	private void updateTreeView(final TreeData data) {
		if (_type == JOEConstants.SPECIFIC_WEEKDAYS) {
			objSchedulerForm.updateSpecificWeekdays();
		}
		objSchedulerForm.update();
		int iType = data.getType();
		switch (iType) {
			case JOEConstants.JOBS:
				objSchedulerForm.updateJobs();
				break;
			case JOEConstants.JOB:
				objSchedulerForm.updateJob();
				break;
			case JOEConstants.SCHEDULES:
				objSchedulerForm.updateSchedules();
				break;
			case JOEConstants.ORDERS:
				objSchedulerForm.updateOrders();
				break;
			case JOEConstants.JOB_CHAINS:
			case JOEConstants.JOB_CHAIN:
				objSchedulerForm.updateJobChains();
				break;
			default:
				break;
		}
		objSchedulerForm.expandItem(objLiveFolderTreeViewer.getSelection()[0].getText());
		objSchedulerForm.updateTreeItem(objLiveFolderTreeViewer.getSelection()[0].getText());
		objSchedulerForm.updateTree("");
		refreshTree("main");
		//		if (_dom instanceof SchedulerDom && ((SchedulerDom) _dom).isDirectory()) {
		//			((SchedulerDom) _dom).setChangedForDirectory(data.getElement(), SchedulerDom.NEW);
		//		}
		//		_dom.setChanged(true);
	}

	private TreeData getItemElement() {
		TreeData objTD = getTreeData();
		if (objTD != null) {
			switch (objTD.getType()) {
				case JOEConstants.JOB:
				case JOEConstants.JOB_CHAIN:
				case JOEConstants.ORDER:
				case JOEConstants.WEBSERVICE:
				case JOEConstants.MONITOR:
				case JOEConstants.SCHEDULE:
					return objTD;
				default:
					break;
			}
		}
		return null;
	}

	public String getHotFolderName_() {
		JSObjBase objTD = getTreeData().getLiveObject();
		String newName = objTD.getHotFolderSrc().getName();
		return newName;
	}
}