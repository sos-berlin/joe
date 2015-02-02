package sos.scheduler.editor.conf.forms;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import sos.scheduler.editor.app.IContainer;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.TabbedContainer;
import sos.scheduler.editor.app.TreeMenu;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.SchedulerListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IEditor;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.dialog.swtdesigner.SWTResourceManager;

public class SchedulerForm extends SOSJOEMessageCodes implements ISchedulerUpdate, IEditor {
	@SuppressWarnings("unused") private final String		conClsName		= "SchedulerForm";
	@SuppressWarnings("unused") private final String		conSVNVersion	= "$Id$";
	@SuppressWarnings("unused") private static final Logger	logger			= Logger.getLogger(SchedulerForm.class);
	private SchedulerDom									dom				= null;
	private SchedulerListener								listener		= null;
	private IContainer										container		= null;
	private TreeItem										selection		= null;
	private SashForm										sashForm		= null;
	private Group											gTree			= null;
	private Tree											tree			= null;
	private Composite										cMainForm		= null;

	// private static boolean fontChange = false;
	public SchedulerForm(final IContainer container1, final Composite parent, final int style) {
		super(parent, style);
		container = container1;
		dom = new SchedulerDom();
		dom.setDataChangedListener(this);
		listener = new SchedulerListener(this, dom);
	}

	public SchedulerForm(final IContainer container1, final Composite parent, final int style, final int type) {
		super(parent, style);
		container = container1;
		dom = new SchedulerDom(type);
		dom.setDataChangedListener(this);
		listener = new SchedulerListener(this, dom);
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
 		createGTree();
		createCMainForm();
		sashForm.setWeights(new int[] { 176, 698 });
		Options.loadSash("main", sashForm);
	}

	private void createGTree() {
		gTree = JOE_G_SchedulerForm_SchedulerElements.Control(new Group(sashForm, SWT.NONE));
		gTree.setLayout(new FillLayout());
		tree = new Tree(gTree, SWT.BORDER);
		tree.setMenu(new TreeMenu(tree, dom, this).getMenu());
		tree.addListener(SWT.MenuDetect, new Listener() {
			@Override public void handleEvent(final Event e) {
				e.doit = tree.getSelectionCount() > 0;
			}
		});
		tree.addListener(SWT.MouseDown, new Listener() {
			@Override public void handleEvent(final Event event) {
				Point point = new Point(event.x, event.y);
				TreeItem item = tree.getItem(point);
				if (item != null) {
					//		          System.out.println("Mouse down: " + item);
				}
			}
		});
		// see http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/DragandDropexamplesnippetdragleafitemsinatree.htm
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		final DragSource source = new DragSource(tree, operations);
		source.setTransfer(types);
		final TreeItem[] dragSourceItem = new TreeItem[1];
		source.addDragListener(new DragSourceListener() {
			@Override public void dragStart(final DragSourceEvent event) {
				TreeItem[] selection = tree.getSelection();
				if (selection.length > 0 && selection[0].getItemCount() == 0) {
					event.doit = true;
					dragSourceItem[0] = selection[0];
				}
				else {
					event.doit = false;
				}
			};

			@Override public void dragSetData(final DragSourceEvent event) {
				event.data = dragSourceItem[0].getText();
			}

			@Override public void dragFinished(final DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE)
					dragSourceItem[0].dispose();
				dragSourceItem[0] = null;
			}
		});
	/*tree.addListener(SWT.MouseUp, new Listener() {
			@Override public void handleEvent(final Event event) {
				if (event.button == 1) { // left mouse button
					Point point = new Point(event.x, event.y);
					TreeItem item = tree.getItem(point);
					if (item != null) {
						selection = item;
						event.doit = listener.treeSelection(tree, cMainForm);
						if (!event.doit) {
							tree.setSelection(new TreeItem[] { selection });
						}
						else {
							selection = tree.getSelection()[0];
						}
					}
				}
			}
		});
		 */
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

	private void createCMainForm() {
 		cMainForm = new Composite(sashForm, SWT.NONE);
		cMainForm.setLayout(new FillLayout());
		//		cMainForm.setLayout(new GridLayout());
	}

	public Shell getSShell() {
		return this.getShell();
	}

	@Override public void updateLanguage() {
		if (cMainForm.getChildren().length > 0) {
			if (cMainForm.getChildren()[0] instanceof IUpdateLanguage) {
				((IUpdateLanguage) cMainForm.getChildren()[0]).setToolTipText();
			}
		}
	}

	@Override public void dataChanged() {
		container.setStatusInTitle();
	}

	public void dataChanged(final CTabItem tab) {
		((TabbedContainer) container).setStatusInTitle(tab);
	}

	@Override public void updateExitCodesCommand() {
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

	@Override public void updateCommands() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillCommands(tree.getSelection()[0], data.getElement(), true);
		}
	}

	@Override public void updateDays(final int type) {
		updateDays(type, null);
		/*if (tree.getSelectionCount() > 0) {
		 TreeItem item = tree.getSelection()[0];
		 TreeData data = (TreeData) item.getData();
		 listener.treeFillDays(item, data.getElement(), type, true);
		 }*/
	}

	@Override public void updateDays(final int type, final String name) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillDays(item, data.getElement(), type, true, name);
			item.setExpanded(true);
		}
	}

	@Override public void updateJob() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillJob(item, data.getElement(), true);
		}
	}

	@Override public void updateJob(final Element elem) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			String job = Utils.getAttributeValue("name", elem);
			item.setText(job);
			TreeData data = (TreeData) item.getData();
			data.setElement(elem);
			// listener.treeFillMain(tree, this, SchedulerDom.LIVE_JOB);
			listener.treeFillJob(item, data.getElement(), true);
			listener.treeSelection(tree, cMainForm);
			// listener.treeFillJobs(tree.getParentItem());
		}
	}

	@Override public void updateJob(final String job) {
		TreeItem item = tree.getSelection()[0];
		TreeData data = (TreeData) item.getData();
		if (data != null){
		   org.jdom.Element element = data.getElement();
		   listener.setColorOfJobTreeItem(element, item);
		item.setText(job);
		}
	}

	@Override public void updateJobs() {
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

	@Override public void expandItem(final String name) {
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

	@Override public void updateOrder(final String s) {
		TreeItem item = tree.getSelection()[0];
		item.setText(s);
	}

	@Override public void updateOrders() {
		if (tree.getSelectionCount() > 0) {
			listener.treeFillOrders(tree.getSelection()[0], true);
		}
	}

	@Override public boolean applyChanges() {
		Control[] c = cMainForm.getChildren();
		return c.length == 0 || Utils.applyFormChanges(c[0]);
	}

	@Override public void openBlank() {
		initialize();
		// dom.initScheduler();
		listener.treeFillMain(tree, cMainForm);
	}

	public void openBlank(final int type) {
		initialize();
		// dom.initScheduler();
		listener.treeFillMain(tree, cMainForm, type);
	}

	public boolean openDirectory(final String filename, final Collection files) {
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

	@Override public boolean open(final Collection files) {
		boolean res = IOUtils.openFile(files, dom);
		if (res) {
			initialize();
			listener.treeFillMain(tree, cMainForm);
		}
		return res;
	}

	public boolean open(final String filename, final Collection files) {
		boolean res = IOUtils.openFile(filename, files, dom);
		if (res) {
			initialize();
			listener.treeFillMain(tree, cMainForm);
		}
		return res;
	}

	public boolean open(final String filename, final Collection files, final int type) {
		boolean res = IOUtils.openFile(filename, files, dom);
		if (res) {
			initialize();
			listener.treeFillMain(tree, cMainForm, type);
		}
		return res;
	}

	@Override public boolean save() {
		boolean res = true;
		// if(dom.getFilename() != null && new java.io.File(dom.getFilename()).getName().startsWith("#xml#.config.") &&
		// dom.getFilename().endsWith(".xml~")) {
		if (dom.isDirectory()) {
			res = MainWindow.saveDirectory(dom, false, SchedulerDom.DIRECTORY, null, container);
		}
		else
			if (dom.isLifeElement()) {
				int type = -1;
				if (dom.getRoot().getName().equals("job"))
					type = SchedulerDom.LIVE_JOB;
				if (dom.getRoot().getName().equals("job_chain"))
					type = SchedulerDom.LIVE_JOB_CHAIN;
				if (dom.getRoot().getName().equals("process_class"))
					type = SchedulerDom.LIFE_PROCESS_CLASS;
				if (dom.getRoot().getName().equals("lock"))
					type = SchedulerDom.LIFE_LOCK;
				if (dom.getRoot().getName().equals("order"))
					type = SchedulerDom.LIFE_ORDER;
				if (dom.getRoot().getName().equals("add_order"))
					type = SchedulerDom.LIFE_ADD_ORDER;
				res = MainWindow.saveDirectory(dom, false, type, dom.getRoot().getName(), container);
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

	@Override public boolean saveAs() {
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

	@Override public boolean close() {
		return applyChanges() && IOUtils.continueAnyway(dom);
	}

	@Override public boolean hasChanges() {
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
	@Override public String getHelpKey() {
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

	@Override public String getFilename() {
		return dom.getFilename();
	}

	@Override public void updateTree(final String which) {
		if (which.equalsIgnoreCase("convert")) {
			if (dom.isLifeElement()) {
				listener.treeFillMainForLifeElement(tree, cMainForm,false);
			}
			else {
				listener.treeFillMain(tree, cMainForm);
			}
		}else{

			if (which.equalsIgnoreCase("main")) {
				if (dom.isLifeElement()) {
					listener.treeFillMainForLifeElement(tree, cMainForm,true);
				}
				else {
					listener.treeFillMain(tree, cMainForm);
				}
			}
			else
				listener.treeSelection(tree, cMainForm);
		}
	}

	public void selectTreeItem(final String parent, final String child) {
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

	@Override public void updateJobChains() {
		listener.treeFillJobChains(tree.getSelection()[0]);
		if (tree.getSelection()[0].getItemCount() > 0)
			tree.getSelection()[0].getItems()[tree.getSelection()[0].getItemCount() - 1].setExpanded(true);
	}

	@Override public void updateJobChain(final String newName, final String oldName) {
		// listener.treeFillJobChains(tree.getSelection()[0]);
		if (newName.equals(oldName))
			return;
		if (dom.isLifeElement()) {
			TreeItem item = tree.getSelection()[0];
			item.setText(newName);
		}
		TreeItem item = tree.getSelection()[0];
		if (item.getText().equals("Job Chains")) {
			TreeItem[] items = item.getItems();
			for (TreeItem it : items) {
				if (it.getText().equals(oldName))
					it.setText(newName);
			}
		}
		else {
			TreeItem[] parent = tree.getItems();
			for (TreeItem element : parent) {
				if (element.getText().equals("Job Chains")) {
					TreeItem[] items = element.getItems();
					for (TreeItem it : items) {
						if (it.getText().equals(oldName))
							it.setText(newName);
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
				updateJobChain(item.getText(0), Utils.getAttributeValue("name", elem));
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

	@Override public void updateSpecificWeekdays() {
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

	@Override public void updateSchedules() {
		if (tree.getSelectionCount() > 0)
			listener.treeFillSchedules(tree.getSelection()[0]);
	}

	@Override public void updateWebServices() {
		if (tree.getSelectionCount() > 0)
			listener.treeFillWebServices(tree.getSelection()[0]);
	}

	@Override public void updateScripts() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			org.jdom.Element elem = data.getElement();
			listener.treeFillMonitorScripts(item, elem, false);
		}
	}

	@Override public void updateTreeItem(final String s) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			if (item.getParentItem() != null && item.getParentItem().getText().equals("Monitor") && s.equals(""))
				item.setText("<empty>");
			else
				item.setText(s);
		}
	}

	@Override public void updateFont() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			updateFont(item);
		}
	}
	private static Font	fontBold	= null;
	private static Font	fontRegular	= null;

	@Override public void updateFont(final TreeItem item) {
		FontData fontDatas[] = item.getFont().getFontData();
		FontData data = fontDatas[0];
		boolean isBold = false;
		TreeData data_ = (TreeData) item.getData();
		if (data_ == null || data_.getElement() == null)
			return;
		int type = data_.getType();
		Element elem = data_.getElement();
		if (type == JOEConstants.EVERYDAY) {
			if (!elem.getChildren("period").isEmpty() || !elem.getChildren("at").isEmpty())
				isBold = true;
		}
		else
			if (type == JOEConstants.DAYS) {
				if (!elem.getChildren("date").isEmpty())
					isBold = true;
			}
			else
				if (type == JOEConstants.WEEKDAYS) {
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
					if (type == JOEConstants.MONTHDAYS) {
						if (!elem.getChildren("monthdays").isEmpty() && !elem.getChild("monthdays").getChildren("day").isEmpty())
							isBold = true;
					}
					else
						if (type == JOEConstants.ULTIMOS) {
							if (!elem.getChildren("ultimos").isEmpty())
								isBold = true;
						}
						else
							if (type == JOEConstants.SPECIFIC_WEEKDAYS) {
								if (!elem.getChildren("monthdays").isEmpty() && !elem.getChild("monthdays").getChildren("weekday").isEmpty())
									isBold = true;
							}
							else
								if (type == JOEConstants.SPECIFIC_MONTHS) {
									if (!elem.getChildren("month").isEmpty())
										isBold = true;
								}
								else
									if (type == JOEConstants.RUNTIME) {
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
		int intStyle = SWT.NONE;
		if (isBold) {
			intStyle = SWT.BOLD;
			Font f = SWTResourceManager.getFont(data.getName(), data.getHeight(), intStyle);
			item.setFont(f);
		}
	}

	public SchedulerListener getListener() {
		return listener;
	}

	// hasRuntimeChild = false, wenn der Runtime ELement Attribute wie schedule oder runtime_function hat
	@Override public void updateRunTime() {
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

	public void setChangedTreeItemText(final String key1) {
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

	private void setChangedItemText(final TreeItem item) {
		if (item.getText().startsWith("*")) {
			item.setText(item.getText().substring(1));
		}
		for (int j = 0; j < item.getItemCount(); j++) {
			TreeItem cItem = item.getItem(j);
			setChangedItemText(cItem);
		}
	}
}