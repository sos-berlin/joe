package sos.scheduler.editor.conf.forms;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.IEditor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.TreeMenu;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

public class SchedulerForm extends Composite implements ISchedulerUpdate, IEditor {
	@SuppressWarnings("unused")
	private final String		conClsName		= "SchedulerForm";
	@SuppressWarnings("unused")
	private final String		conSVNVersion	= "$Id$";
	@SuppressWarnings("unused")
	private static final Logger	logger			= Logger.getLogger(SchedulerForm.class);
	private SchedulerDom		dom				= null;
	private SchedulerListener	listener		= null;
	private IContainer			container		= null;
	private TreeItem			selection		= null;
	private SashForm			sashForm		= null;
	private Group				gTree			= null;
	private Tree				tree			= null;
	private Composite			cMainForm		= null;

	// private static boolean fontChange = false;
	public SchedulerForm(IContainer container1, Composite parent, int style) {
		super(parent, style);
		this.container = container1;
		this.dom = new SchedulerDom();
		this.dom.setDataChangedListener(this);
		listener = new SchedulerListener(this, this.dom);
	}

	public SchedulerForm(IContainer container1, Composite parent, int style, int type) {
		super(parent, style);
		this.container = container1;
		this.dom = new SchedulerDom(type);
		this.dom.setDataChangedListener(this);
		listener = new SchedulerListener(this, this.dom);
	}

	private void initialize() {
		FillLayout fillLayout = new FillLayout();
		fillLayout.spacing = 0;
		fillLayout.marginWidth = 5;
		fillLayout.marginHeight = 5;
		setSize(new Point(783, 450));
		setLayout(fillLayout);
		createSashForm();
	}

	/**
	 * This method initializes sashForm
	 */
	private void createSashForm() {
		sashForm = new SashForm(this, SWT.NONE);
		// sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		createGTree();
		createCMainForm();
		sashForm.setWeights(new int[] { 176, 698 });
		Options.loadSash("main", sashForm);
	}

	/**
	 * This method initializes gTree
	 */
	private void createGTree() {
		gTree = new Group(sashForm, SWT.NONE);
		gTree.setLayout(new FillLayout());
		gTree.setText("Scheduler Elements");
		tree = new Tree(gTree, SWT.BORDER);
		tree.setMenu(new TreeMenu(tree, dom, this).getMenu());
		tree.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event e) {
				e.doit = tree.getSelectionCount() > 0;
			}
		});
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (tree.getSelectionCount() > 0) {
					if (selection == null)
						selection = tree.getItem(0);
					e.doit = listener.treeSelection(tree, cMainForm);
					if (!e.doit) {
						tree.setSelection(new TreeItem[] { selection });
					}
					else {
						selection = tree.getSelection()[0];
					}
				}
			}
		});
	}

	/**
	 * This method initializes cMainForm
	 */
	private void createCMainForm() {
		cMainForm = new Composite(sashForm, SWT.NONE);
		cMainForm.setLayout(new FillLayout());
	}

	public Shell getSShell() {
		return this.getShell();
	}

	public void updateLanguage() {
		if (cMainForm.getChildren().length > 0) {
			if (cMainForm.getChildren()[0] instanceof IUpdateLanguage) {
				((IUpdateLanguage) cMainForm.getChildren()[0]).setToolTipText();
			}
		}
	}

	public void dataChanged() {
		container.setStatusInTitle();
	}

	public void dataChanged(CTabItem tab) {
		((sos.scheduler.editor.app.TabbedContainer) container).setStatusInTitle(tab);
	}

	public void updateExitCodesCommand() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillExitCodesCommands(item, data.getElement(), true);
			item.setExpanded(true);
			if (item.getItemCount() > 0)
				item.getItems()[item.getItemCount() - 1].setExpanded(true);
			// listener.treeFillCommands(tree.getSelection()[0], data.getElement(), true);
			// hier einen neuen TreeItem aufbauen
		}
	}

	public void updateCommands() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillCommands(tree.getSelection()[0], data.getElement(), true);
		}
	}

	public void updateDays(int type) {
		updateDays(type, null);
		/*if (tree.getSelectionCount() > 0) {
		 TreeItem item = tree.getSelection()[0];
		 TreeData data = (TreeData) item.getData();
		 listener.treeFillDays(item, data.getElement(), type, true);
		 }*/
	}

	public void updateDays(int type, String name) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillDays(item, data.getElement(), type, true, name);
			item.setExpanded(true);
		}
	}

	public void updateJob() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillJob(item, data.getElement(), true);
		}
	}

	public void updateJob(Element elem) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			String job = "Job: " + Utils.getAttributeValue("name", elem);
			item.setText(job);
			TreeData data = (TreeData) item.getData();
			data.setElement(elem);
			// listener.treeFillMain(tree, this, SchedulerDom.LIFE_JOB);
			listener.treeFillJob(item, data.getElement(), true);
			listener.treeSelection(tree, cMainForm);
			// listener.treeFillJobs(tree.getParentItem());
		}
	}

	public void updateJob(String s) {
		TreeItem item = tree.getSelection()[0];
		String job = "Job: " + s;
		item.setText(job);
	}

	public void updateJobs() {
		// if(tree.getSelection()[0].getText().startsWith("Job Chain")) {
		if (!tree.getSelection()[0].getText().startsWith("Jobs")) {
			// Assistent: Der Aufruf erfolgte über den Assistenten. Hier ist nicht das Element "Jobs" im Tree selektiert
			// sondern das Element "Job Chains".
			updateJobs_();
		}
		else
			if (tree.getSelectionCount() > 0)
				listener.treeFillJobs(tree.getSelection()[0]);
	}

	public void expandItem(String name) {
		listener.treeExpandJob(tree.getSelection()[0], name);
	}

	private void updateJobs_() {
		if (tree.getSelectionCount() > 0) {
			for (int i = 0; i < tree.getItemCount(); i++) {
				TreeItem ti = tree.getItem(i);
				if (ti.getText().equalsIgnoreCase("Jobs")) {
					// System.out.println("itemText "+ ti.getText());
					listener.treeFillJobs(ti);
				}
			}
		}
	}

	public void updateOrder(String s) {
		TreeItem item = tree.getSelection()[0];
		String order = "Order: " + s;
		item.setText(order);
	}

	public void updateOrders() {
		if (tree.getSelectionCount() > 0) {
			listener.treeFillOrders(tree.getSelection()[0], true);
		}
	}

	public boolean applyChanges() {
		Control[] c = cMainForm.getChildren();
		return c.length == 0 || Utils.applyFormChanges(c[0]);
	}

	public void openBlank() {
		initialize();
		// dom.initScheduler();
		listener.treeFillMain(tree, cMainForm);
	}

	public void openBlank(int type) {
		initialize();
		// dom.initScheduler();
		listener.treeFillMain(tree, cMainForm, type);
	}

	public boolean openDirectory(String filename, Collection files) {
		// boolean res = IOUtils.openFile("#xml#", files, dom);
		// System.out.println("test: " + filename);
		boolean res = IOUtils.openFile(filename, files, dom);
		if (res) {
			if (dom.getListOfChangeElementNames() != null && dom.getListOfChangeElementNames().size() > 0) {
				dom.setChanged(true);
			}
			initialize();
			listener.treeFillMain(tree, cMainForm, SchedulerDom.DIRECTORY);
		}
		return res;
	}

	public boolean open(Collection files) {
		boolean res = IOUtils.openFile(files, dom);
		if (res) {
			initialize();
			listener.treeFillMain(tree, cMainForm);
		}
		return res;
	}

	public boolean open(String filename, Collection files) {
		boolean res = IOUtils.openFile(filename, files, dom);
		if (res) {
			initialize();
			listener.treeFillMain(tree, cMainForm);
		}
		return res;
	}

	public boolean open(String filename, Collection files, int type) {
		boolean res = IOUtils.openFile(filename, files, dom);
		if (res) {
			initialize();
			listener.treeFillMain(tree, cMainForm, type);
		}
		return res;
	}

	public boolean save() {
		boolean res = true;
		// if(dom.getFilename() != null && new java.io.File(dom.getFilename()).getName().startsWith("#xml#.config.") &&
		// dom.getFilename().endsWith(".xml~")) {
		if (dom.isDirectory()) {
			res = IOUtils.saveDirectory(dom, false, SchedulerDom.DIRECTORY, null, container);
		}
		else
			if (dom.isLifeElement()) {
				int type = -1;
				if (dom.getRoot().getName().equals("job"))
					type = SchedulerDom.LIFE_JOB;
				if (dom.getRoot().getName().equals("job_chain"))
					type = SchedulerDom.LIFE_JOB_CHAIN;
				if (dom.getRoot().getName().equals("process_class"))
					type = SchedulerDom.LIFE_PROCESS_CLASS;
				if (dom.getRoot().getName().equals("lock"))
					type = SchedulerDom.LIFE_LOCK;
				if (dom.getRoot().getName().equals("order"))
					type = SchedulerDom.LIFE_ORDER;
				if (dom.getRoot().getName().equals("add_order"))
					type = SchedulerDom.LIFE_ADD_ORDER;
				res = IOUtils.saveDirectory(dom, false, type, dom.getRoot().getName(), container);
			}
			else {
				res = IOUtils.saveFile(dom, false);
			}
		if (res)
			container.setNewFilename(null);
		if (res)
			setReChangedTreeItemText();
		if (dom.getRoot().getName().equals("spooler"))
			Utils.setResetElement(dom.getRoot().getChild("config"));
		else
			Utils.setResetElement(dom.getRoot());
		return res;
	}

	public boolean saveAs() {
		String old = dom.getFilename();
		boolean res = IOUtils.saveFile(dom, true);
		if (dom.isLifeElement()) {
			updateLifeElement();
		}
		if (res)
			container.setNewFilename(old);
		if (res)
			setReChangedTreeItemText();
		return res;
	}

	public boolean close() {
		return applyChanges() && IOUtils.continueAnyway(dom);
	}

	public boolean hasChanges() {
		Options.saveSash("main", sashForm.getWeights());
		// System.out.println(dom.isChanged() + " isLife: " + (dom.isDirectory() || dom.isLifeElement()));
		// if(dom.isDirectory()) {
		/*if(dom.isChanged()) {
			fontChange = true;
			setChangedInItalicFont();
		} else {
			//zurücksetzen
			if (fontChange){
				FontData fontDatas[] = tree.getFont().getFontData();
				FontData fdata = fontDatas[0];
				Font font = new Font(Display.getCurrent(), fdata.getName(), fdata.getHeight(), SWT.NORMAL);
				tree.setFont(font);
				for(int i = 0; i < tree.getItemCount(); i++) {
					TreeItem item = tree.getItem(i);
					setChangedFont(item);
				}
			}


		}
		*/
		return dom.isChanged();
	}

	/*//Nur für Hot Foldern. Setz den Font vom kursiv auf normal
	private void setChangedFont(TreeItem item ) {
		FontData fontDatas[] = tree.getFont().getFontData();
		FontData fdata = fontDatas[0];
		Font font = new Font(Display.getCurrent(), fdata.getName(), fdata.getHeight(), SWT.NORMAL);

		item.setFont(font);
		for(int j = 0; j < item.getItemCount(); j++) {
			TreeItem cItem = item.getItem(j);
			setChangedFont(cItem);					
		}			
	}

	


	//Nur für Hot Foldern
	private void setChangedInItalicFont() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];    
			TreeData data = (TreeData) item.getData();
			FontData fontDatas[] = item.getFont().getFontData();
			FontData fdata = fontDatas[0];
			Font font = new Font(Display.getCurrent(), fdata.getName(), fdata.getHeight(), SWT.ITALIC);
			item.setFont(font);
			while(item.getParentItem() != null) {
				item = item.getParentItem();
				item.setFont(font);				
			}
			
		}
	}
	*/
	public String getHelpKey() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			if (item.getData("key") != null) {
				return Options.getHelpURL(item.getData("key").toString());
			}
			else {
				TreeData data = (TreeData) item.getData();
				if (data != null && data.getHelpKey() != null)
					return data.getHelpKey();
			}
		}
		return null;
	}

	public String getFilename() {
		return dom.getFilename();
	}

	public void updateTree(String which) {
		// String mar = getTreeSelection();
		if (which.equalsIgnoreCase("main")) {
			if (dom.isLifeElement()) {
				listener.treeFillMainForLifeElement(tree, cMainForm);
			}
			else {
				listener.treeFillMain(tree, cMainForm);
			}
			// } else if(which.equalsIgnoreCase("jobs"))
		}
		else
			listener.treeSelection(tree, cMainForm);
	}

	public void selectTreeItem(String parent, String child) {
		for (int i = 0; i < tree.getItemCount(); i++) {
			TreeItem pItem = tree.getItem(i);
			if (pItem.getText().equals(parent)) {
				for (int j = 0; j < pItem.getItemCount(); j++) {
					TreeItem cItem = pItem.getItem(j);
					if (cItem.getText().equals(child)) {
						tree.setSelection(new TreeItem[] { cItem });
						break;
					}
				}
			}
		}
	}

	public String getTreeSelection() {
		if (tree.getSelectionCount() > 0)
			return tree.getSelection()[0].getText();
		else
			return "Config";
	}

	public void updateJobChains() {
		listener.treeFillJobChains(tree.getSelection()[0]);
		if (tree.getSelection()[0].getItemCount() > 0)
			tree.getSelection()[0].getItems()[tree.getSelection()[0].getItemCount() - 1].setExpanded(true);
	}

	public void updateJobChain(String newName, String oldName) {
		// listener.treeFillJobChains(tree.getSelection()[0]);
		if (newName.equals(oldName))
			return;
		if (dom.isLifeElement()) {
			TreeItem item = tree.getSelection()[0];
			item.setText("Job Chain: " + newName);
		}
		TreeItem item = tree.getSelection()[0];
		if (item.getText().equals("Job Chains")) {
			TreeItem[] items = item.getItems();
			for (int i = 0; i < items.length; i++) {
				TreeItem it = items[i];
				if (it.getText().equals("Job Chain: " + oldName))
					it.setText("Job Chain: " + newName);
			}
		}
		else {
			TreeItem[] parent = tree.getItems();
			for (int i = 0; i < parent.length; i++) {
				if (parent[i].getText().equals("Job Chains")) {
					TreeItem[] items = parent[i].getItems();
					for (int j = 0; j < items.length; j++) {
						TreeItem it = items[j];
						if (it.getText().equals("Job Chain: " + oldName))
							it.setText("Job Chain: " + newName);
					}
				}
			}
			// String jobChain = "Job Chain: " + newName;
			// item.setText(jobChain);
		}
	}

	public void updateLifeElement() {
		// TreeItem item = tree.getSelection()[0];
		TreeItem item = tree.getItem(0);
		tree.setSelection(new TreeItem[] { item });
		TreeData data = (TreeData) item.getData();
		org.jdom.Element elem = data.getElement();
		if (elem.getName().equals("job")) {
			updateJob(Utils.getAttributeValue("name", elem));
			updateJob();
		}
		else
			if (elem.getName().equals("job_chain")) {
				updateJobChain(item.getText(0).substring("Job Chain: ".length()), Utils.getAttributeValue("name", elem));
			}
			else
				if (elem.getName().equals("add_order") || elem.getName().equals("order")) {
					updateOrder(Utils.getAttributeValue("id", elem));
				}
				else
					if (elem.getName().equals("config")) {
						if (elem.getChild("process_classes") != null) {
							Element process_classes = elem.getChild("process_classes");
							Element pc = process_classes.getChild("process_class");
							pc.setAttribute("name", dom.getRoot().getAttributeValue("name"));
						}
						if (elem.getChild("locks") != null) {
							Element locks = elem.getChild("locks");
							Element pc = locks.getChild("lock");
							pc.setAttribute("name", dom.getRoot().getAttributeValue("name"));
						}
					}
		listener.treeSelection(tree, cMainForm);
	}

	public void updateSpecificWeekdays() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillSpecificWeekdays(item, data.getElement(), true);
		}
	}

	public Tree getTree() {
		return tree;
	}

	public SchedulerDom getDom() {
		return dom;
	}

	public void updateSchedules() {
		if (tree.getSelectionCount() > 0)
			listener.treeFillSchedules(tree.getSelection()[0]);
	}

	public void updateWebServices() {
		if (tree.getSelectionCount() > 0)
			listener.treeFillWebServices(tree.getSelection()[0]);
	}

	public void updateScripts() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			org.jdom.Element elem = data.getElement();
			listener.treeFillScripts(item, elem, false);
		}
	}

	public void updateTreeItem(String s) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			if (item.getParentItem() != null && item.getParentItem().getText().equals("Monitor") && s.equals(""))
				item.setText("<empty>");
			else
				item.setText(s);
		}
	}

	public void updateFont() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			updateFont(item);
		}
	}

	public void updateFont(TreeItem item) {
		FontData fontDatas[] = item.getFont().getFontData();
		FontData data = fontDatas[0];
		boolean isBold = false;
		TreeData data_ = (TreeData) item.getData();
		if (data_ == null || data_.getElement() == null)
			return;
		int type = data_.getType();
		Element elem = data_.getElement();
		if (type == Editor.EVERYDAY) {
			if (!elem.getChildren("period").isEmpty() || !elem.getChildren("at").isEmpty())
				isBold = true;
		}
		else
			if (type == Editor.DAYS) {
				if (!elem.getChildren("date").isEmpty())
					isBold = true;
			}
			else
				if (type == Editor.WEEKDAYS) {
					if (item.getData("key") != null && item.getData("key").equals("holidays_@_weekdays")) {
						if (elem.getChild("holidays") != null && !elem.getChild("holidays").getChildren("weekdays").isEmpty()) {
							isBold = true;
						}
					}
					else {
						if (!elem.getChildren("weekdays").isEmpty())
							isBold = true;
					}
				}
				else
					if (type == Editor.MONTHDAYS) {
						if (!elem.getChildren("monthdays").isEmpty() && !elem.getChild("monthdays").getChildren("day").isEmpty())
							isBold = true;
					}
					else
						if (type == Editor.ULTIMOS) {
							if (!elem.getChildren("ultimos").isEmpty())
								isBold = true;
						}
						else
							if (type == Editor.SPECIFIC_WEEKDAYS) {
								if (!elem.getChildren("monthdays").isEmpty() && !elem.getChild("monthdays").getChildren("weekday").isEmpty())
									isBold = true;
							}
							else
								if (type == Editor.SPECIFIC_MONTHS) {
									if (!elem.getChildren("month").isEmpty())
										isBold = true;
								}
								else
									if (type == Editor.RUNTIME) {
										elem = elem.getChild("run_time");
										if (elem != null) {
											/*int hasAttribute = Utils.getAttributeValue("begin", elem).length() + Utils.getAttributeValue("end", elem).length() +
											(Utils.getAttributeValue("let_run", elem).equals("yes") ? 1 : 0) +
											(Utils.getAttributeValue("once", elem).equals("yes") ? 1 : 0);
											
											if(hasAttribute > 0)
											*/
											if (elem.getAttributes().size() > 0)
												isBold = true;
										}
									}
		Font font = null;
		if (isBold) {
			font = new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.BOLD);
		}
		else {
			font = new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.NONE);
		}
		item.setFont(font);
	}

	public SchedulerListener getListener() {
		return listener;
	}

	// hasRuntimeChild = false, wenn der Runtime ELement Attribute wie schedule oder runtime_function hat
	public void updateRunTime() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			item.removeAll();
			TreeData data = (TreeData) item.getData();
			listener.treeFillRunTimes(item, data.getElement(), !Utils.isElementEnabled("job", dom, data.getElement()), "run_time");
		}
	}

	public void updateCMainForm() {
		listener.treeSelection(tree, cMainForm);
	}

	public void setChangedTreeItemText(String key1) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			/*TreeData data = (TreeData) item.getData();
			FontData fontDatas[] = item.getFont().getFontData();
			FontData fdata = fontDatas[0];
			Font font = new Font(Display.getCurrent(), fdata.getName(), fdata.getHeight(), SWT.ITALIC);
			item.setFont(font);
			*/
			if (!dom.isDirectory())
				return;
			// TreeData data = (TreeData) item.getData();
			/*Element elem = Utils.getHotFolderParentElement(data.getElement());
			String key1 = "";
			
			if(elem.getName().equals("order") || elem.getName().equals("add_order"))
				key1 = elem.getName()+ "_" + Utils.getAttributeValue("job_chain",elem)+ "," +Utils.getAttributeValue("id",elem);
			else
				key1 = elem.getName() + "_" + Utils.getAttributeValue("name", elem);
			*/
			if (!dom.getChangedJob().containsKey(key1))
				return;
			if (item.getText().endsWith(SchedulerListener.LOCKS) || item.getText().endsWith(SchedulerListener.PROCESS_CLASSES)) {
				if (!item.getText().startsWith("*")) {
					item.setText("*" + item.getText());
				}
				return;
			}
			if (dom.getChangedJob().get(key1).equals(SchedulerDom.NEW) && !key1.startsWith("process_class")) {
				int i = item.getItemCount() - 1;
				if (i < 0)
					i = 0;
				item = item.getItem(i);
				if (!item.getText().startsWith("*")) {
					// item.getItem(item.getItemCount()-1);
					item.setText("*" + item.getText());
				}
				return;
			}
			while (item != null && item.getParentItem() != null) {
				String sParent = item.getParentItem().getText();
				if (sParent.equals(SchedulerListener.JOBS) || sParent.equals(SchedulerListener.ORDERS) || sParent.equals(SchedulerListener.JOB_CHAINS)
						|| sParent.equals(SchedulerListener.SCHEDULES) || item.getText().equals(SchedulerListener.LOCKS)
						|| item.getText().equals(SchedulerListener.PROCESS_CLASSES)) {
					if (!item.getText().startsWith("*")) {
						item.setText("*" + item.getText());
					}
					item = null;
				}
				else {
					item = item.getParentItem();
					// item.setFont(font);
				}
			}
		}
	}

	public void setReChangedTreeItemText() {
		for (int i = 0; i < tree.getItemCount(); i++) {
			TreeItem item = tree.getItem(i);
			setChangedItemText(item);
		}
	}

	private void setChangedItemText(TreeItem item) {
		if (item.getText().startsWith("*")) {
			item.setText(item.getText().substring(1));
		}
		for (int j = 0; j < item.getItemCount(); j++) {
			TreeItem cItem = item.getItem(j);
			setChangedItemText(cItem);
		}
	}
}