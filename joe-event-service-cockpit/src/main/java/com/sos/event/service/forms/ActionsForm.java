package com.sos.event.service.forms;
import static sos.util.SOSClassUtil.getMethodName;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import com.sos.event.service.actions.IActionsUpdate;
import com.sos.event.service.listeners.ActionsListener;
import com.sos.joe.globals.interfaces.IEditor;
import com.sos.joe.globals.interfaces.IEditorAdapter;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.DomParser;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.Events.ActionsDom;

public class ActionsForm extends SOSJOEMessageCodes implements IEditor, IActionsUpdate {
	private ActionsListener	listener	= null;
	private ActionsDom		dom			= null;
	//	private IContainer		container	= null;
	private SashForm		sashForm	= null;
	private Group			group		= null;
	private Composite		docMainForm	= null;
	private Tree			tree		= null;
	private TreeItem		selection	= null;
	private IEditorAdapter container    = null;

	public ActionsForm(IEditorAdapter container_, Composite parent, int style) {
		super(parent, style);
		container = container_;
		// initialize();
		dom = new ActionsDom();
		dom.setDataChangedListener(this);
		listener = new ActionsListener(this, dom);
	}

	private void initialize() {
		createSashForm();
		setSize(new Point(724, 479));
		setLayout(new FillLayout());
	}

	/**
	 * This method initializes sashForm
	 */
	private void createSashForm() {
		sashForm = new SashForm(this, SWT.NONE);
		createGroup();
		createDocMainForm();
		sashForm.setWeights(new int[] { 66, 264 });
		Options.loadSash("test", sashForm);
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		group = JOE_G_ActionsForm_ActionsElements.Control(new Group(sashForm, SWT.V_SCROLL | SWT.H_SCROLL));
		group.setLayout(new FillLayout()); // Generated
		tree = new Tree(group, SWT.NONE);
		//tree.addListener(SWT.Selection, new Listener() {
		tree.addListener(SWT.Selection, new Listener() {
			@Override public void handleEvent(Event e) {
				if (tree.getSelectionCount() > 0) {
					if (selection == null)
						selection = tree.getItem(0);
					e.doit = listener.treeSelection(tree, docMainForm);
					if (!e.doit) {
						tree.setSelection(new TreeItem[] { selection });
					}
					else {
						selection = tree.getSelection()[0];
					}
				}
				return;
			}
		});
	}

	//Der SelectionListener gibt	lediglich den auslösenden	Event aus
	/**
	 * This method initializes docMainForm
	 */
	private void createDocMainForm() {
		docMainForm = new Composite(sashForm, SWT.NONE);
		docMainForm.setLayout(new FillLayout());
	}

	@Override public boolean applyChanges() {
		Control[] c = docMainForm.getChildren();
		return c.length == 0 || Utils.applyFormChanges(c[0]);
	}

	@Override public boolean close() {
		return applyChanges() && IOUtils.continueAnyway(dom);
	}

	@Override public boolean hasChanges() {
		Options.saveSash("test", sashForm.getWeights());
		return dom.isChanged();
	}

	@Override public boolean open(Collection files) {
		boolean res = IOUtils.openFile(files, dom);
		if (res) {
			initialize();
			listener.fillTree(tree);
			tree.setSelection(new TreeItem[] { tree.getItem(0) });
			listener.treeSelection(tree, docMainForm);
		}
		return res;
	}

	public boolean open(String filename, Collection files) {
		boolean res = IOUtils.openFile(filename, files, dom);
		if (res) {
			initialize();
			listener.fillTree(tree);
			tree.setSelection(new TreeItem[] { tree.getItem(0) });
			listener.treeSelection(tree, docMainForm);
		}
		return res;
	}

	@Override public void openBlank() {
		initialize();
		listener.fillTree(tree);
		tree.setSelection(new TreeItem[] { tree.getItem(0) });
		listener.treeSelection(tree, docMainForm);
	}
	
	public static boolean save_Action(final DomParser dom, final boolean saveas) {
		try {
			if (dom.getFilename() == null || saveas) {
				 SaveEventsDialogForm d= new SaveEventsDialogForm(dom);
				if (dom.getFilename() == null)// Cancel
					return false;
			}
			IOUtils.saveFile(dom, false);
		}
		catch (Exception e) {
			new ErrorLog("error in " + getMethodName() + " could not save directory.", e);
		}
		return true;
	}


	@Override public boolean save() {
		boolean res = save_Action(dom, false);
		// TODO setNEwFilename
		//		if (res)
		//			container.setNewFilename(null);
		Utils.setResetElement(dom.getRoot());
		return res;
	}

	@Override public boolean saveAs() {
		String old = dom.getFilename();
		boolean res = save_Action(dom, true);
		//		if (res)
		//			container.setNewFilename(old);
		return res;
	}

	@Override public void updateLanguage() {
		if (docMainForm.getChildren().length > 0) {
			if (docMainForm.getChildren()[0] instanceof IUpdateLanguage) {
				((IUpdateLanguage) docMainForm.getChildren()[0]).setToolTipText();
			}
		}
	}

	@Override public String getHelpKey() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			if (data != null && data.getHelpKey() != null)
				return data.getHelpKey();
		}
		return null;
	}

	@Override public String getFilename() {
		return dom.getFilename();
	}

	@Override public void dataChanged() {
    	container.setSaveStatus();
	}

	public ActionsDom getDom() {
		return dom;
	}

	@Override public void updateAction(String name) {
		if (tree.getSelectionCount() > 0)
			tree.getSelection()[0].setText(ActionsListener.ACTION_PREFIX + name);
	}

	@Override public void updateActions() {
		listener.treeFillAction(tree.getTopItem());
	}

	public void updateEvents(Element action) {
		TreeItem item = tree.getTopItem();
		for (int i = 0; i < item.getItemCount(); i++) {
			TreeItem it = item.getItem(i);
			if (it.getText().equals(ActionsListener.ACTION_PREFIX + Utils.getAttributeValue("name", action))) {
				it = it.getItem(0);//events Knoten
				listener.fillEventGroup(it, action);
				it.setExpanded(true);
				break;
			}
		}
	}

	@Override public void updateCommands() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillCommands(tree.getSelection()[0], data.getElement(), true);
			item.setExpanded(true);
		}
	}

	@Override public void updateTreeItem(String s) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			item.setText(s);
		}
	}

	@Override public void updateCommand() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			listener.treeFillCommand(item, data.getElement(), true);
			item.setExpanded(true);
			if (item.getItemCount() > 0)
				item.getItems()[item.getItemCount() - 1].setExpanded(true);
			//listener.treeFillCommands(tree.getSelection()[0], data.getElement(), true);
			//hier einen neuen TreeItem aufbauen
		}
	}

	public Tree getTree() {
		return tree;
	}

	@Override public void updateTree(String which) {
		if (which.equals("main")) {
			//neu zeichnen und das erste Element markieren
			listener.fillTree(tree);
			tree.setSelection(new TreeItem[] { tree.getItem(0) });
		}
		listener.treeSelection(tree, docMainForm);
	}
} // @jve:decl-index=0:visual-constraint="10,10"
