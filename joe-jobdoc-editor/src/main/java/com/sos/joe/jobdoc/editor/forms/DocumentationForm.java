package com.sos.joe.jobdoc.editor.forms;
import static com.sos.dialog.Globals.MsgHandler;

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

import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.IDocumentationUpdate;
import com.sos.joe.jobdoc.editor.NoteDialog;
import com.sos.joe.jobdoc.editor.listeners.DocumentationListener;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class DocumentationForm extends JobDocBaseForm<DocumentationListener> implements IDocumentationUpdate /* extends SOSJOEMessageCodes implements IEditor, IDocumentationUpdate */ {
//	private DocumentationListener	listener	= null;
//	private DocumentationDom		dom			= null;
	private SashForm				sashForm	= null;
	private Group					group		= null;
	private Composite				docMainForm	= null;
	private Tree					docTree		= null;
	private TreeItem				selection	= null;

	public DocumentationForm(Composite parent, int style) {
		super(parent, style);
//		this.container = container;
		// initialize();
		dom = new DocumentationDom();
		dom.setDataChangedListener(this);
		listener = new DocumentationListener(this, dom);
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
		Options.loadSash("documentation", sashForm);
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		group = MsgHandler.newMsg("JOE_G_DocumentationForm_DocElements").Control(new Group(sashForm, SWT.V_SCROLL | SWT.H_SCROLL));
		group.setLayout(new FillLayout()); // Generated
		docTree = new Tree(group, SWT.NONE);
		docTree.addListener(SWT.Selection, new Listener() {
			@Override public void handleEvent(Event e) {
				if (docTree.getSelectionCount() > 0) {
					if (selection == null)
						selection = docTree.getItem(0);
					e.doit = listener.treeSelection(docTree, docMainForm);
					if (!e.doit) {
						docTree.setSelection(new TreeItem[] { selection });
					}
					else {
						selection = docTree.getSelection()[0];
					}
				}
			}
		});
	}

	private void createDocMainForm() {
		docMainForm = new Composite(sashForm, SWT.NONE);
		docMainForm.setLayout(new FillLayout());
	}

	public boolean open(String filename, Collection files) {
		boolean res = IOUtils.openFile(filename, files, dom);
		if (res) {
			initialize();
			listener.fillTree(docTree);
			docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
			listener.treeSelection(docTree, docMainForm);
		}
		return res;
	}

	@Override public void openBlank() {
		initialize();
		listener.fillTree(docTree);
		docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
		listener.treeSelection(docTree, docMainForm);
	}

	@Override public void updateLanguage() {
		if (docMainForm.getChildren().length > 0) {
			if (docMainForm.getChildren()[0] instanceof IUpdateLanguage) {
				((IUpdateLanguage) docMainForm.getChildren()[0]).setToolTipText();
			}
		}
	}

	@Override public String getHelpKey() {
		if (docTree.getSelectionCount() > 0) {
			TreeItem item = docTree.getSelection()[0];
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
//		container.setStatusInTitle();
	}

	public static void openNoteDialog(DocumentationDom dom, Element parentElement, String name, boolean optional, String title) {
		openNoteDialog(dom, parentElement, name, null, optional, true, title);
	}

	public static void openNoteDialog(DocumentationDom dom, Element parentElement, String name, boolean optional, boolean changeStatus, String title) {
		openNoteDialog(dom, parentElement, name, null, optional, changeStatus, title);
	}

	public static void openNoteDialog(DocumentationDom dom, Element parentElement, String name, String tooltip, boolean optional, String title) {
		openNoteDialog(dom, parentElement, name, tooltip, optional, true, title);
	}

	public static void openNoteDialog(DocumentationDom dom, Element parentElement, String name, String tooltip, boolean optional, boolean changeStatus,
			String title) {
		NoteDialog dialog = new NoteDialog(ErrorLog.getSShell(), title);
		dialog.setText("Note Editor");
		dialog.setTooltip(tooltip);
		dialog.setParams(dom, parentElement, name, optional, changeStatus);
		dialog.open();
	}

	public static void openNoteDialog(DocumentationDom dom, Element parentElement, String name, String tooltip, boolean optional, boolean changeStatus,
			String title, org.eclipse.swt.widgets.Text txt) {
		NoteDialog dialog = new NoteDialog(ErrorLog.getSShell(), title);
		dialog.setText("Note Editor");
		//dialog.setUpdateText(txt); //Textfeld soll beim verlassen des Dialogs aktualisert werden
		dialog.setTooltip(tooltip);
		dialog.setParams(dom, parentElement, name, optional, changeStatus);
		dialog.open();
	}

	@Override public void updateReleases() {
		if (docTree.getSelectionCount() > 0) {
			TreeItem item = docTree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			org.jdom.Element elem = data.getElement();
			listener.treeFillReleases(item, elem);
			//tree.getSelection()[0].getItems()[tree.getSelection()[0].getItemCount()-1].setExpanded(true);
		}
	}

	public void updateDatabaseResource() {
		if (docTree.getSelectionCount() > 0) {
			TreeItem item = docTree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			org.jdom.Element elem = data.getElement();
			listener.treeFillDatabaseResources(item, elem.getChild("resources", elem.getNamespace()));
			//tree.getSelection()[0].getItems()[tree.getSelection()[0].getItemCount()-1].setExpanded(true);
		}
	}

	@Override public void updateTree(String which) {
		if (which.equals("main")) {
			//neu zeichnen und das erste Element markieren
			listener.fillTree(docTree);
			docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
		}
		listener.treeSelection(docTree, docMainForm);
	}

	@Override protected void applySetting() {
	}
	
	@Override public boolean applyChanges() {
		Control[] c = docMainForm.getChildren();
		return c.length == 0 || Utils.applyFormChanges(c[0]);
	}
	@Override public boolean open(Collection files) {
		boolean res = IOUtils.openFile(files, dom);
		if (res) {
			initialize();
			listener.fillTree(docTree);
			docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
			listener.treeSelection(docTree, docMainForm);
		}
		return res;
	}


} // @jve:decl-index=0:visual-constraint="10,10"
