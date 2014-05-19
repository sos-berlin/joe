package com.sos.jobdoc.forms;

import java.util.Collection;

import org.apache.log4j.Logger;
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

import com.sos.jobdoc.DocumentationDom;
import com.sos.jobdoc.IDocumentationUpdate;
import com.sos.jobdoc.NoteDialog;
import com.sos.jobdoc.listeners.DocumentationListener;
import com.sos.joe.interfaces.IContainer;
import com.sos.joe.interfaces.IEditor;
import com.sos.joe.interfaces.IUpdateLanguage;

import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.TreeData;
import sos.scheduler.editor.app.Utils;

// TODO doppelte einträge verhindern

public class DocumentationForm extends SOSJOEMessageCodes implements IEditor, IDocumentationUpdate {

	@SuppressWarnings("unused")
	private final String conClassName = this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String conSVNVersion = "$Id$";
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(this.getClass());

    private DocumentationListener listener    = null;

    private DocumentationDom      dom         = null;

    private IContainer            container   = null;

    private SashForm              sashForm    = null;

    private Group                 group       = null;

    private Composite             docMainForm = null;

    private Tree                  docTree     = null;

    private TreeItem              selection   = null;


    public DocumentationForm(final IContainer container, final Composite parent, final int style) {
        super(parent, style);
        this.container = container;

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
        group = JOE_G_DocumentationForm_DocElements.Control(new Group(sashForm, SWT.V_SCROLL | SWT.H_SCROLL));
        group.setLayout(new FillLayout()); // Generated

        docTree = new Tree(group, SWT.NONE);
        docTree.addListener(SWT.Selection, new Listener() {
            @Override
			public void handleEvent(final Event e) {
                if (docTree.getSelectionCount() > 0) {
                    if (selection == null)
                        selection = docTree.getItem(0);

                    e.doit = listener.treeSelection(docTree, docMainForm);

                    if (!e.doit) {
                        docTree.setSelection(new TreeItem[] { selection });
                    } else {
                        selection = docTree.getSelection()[0];
                    }
                }
            }
        });
    }


    /**
     * This method initializes docMainForm
     */
    private void createDocMainForm() {
        docMainForm = new Composite(sashForm, SWT.NONE);
        docMainForm.setLayout(new FillLayout());
    }


    @Override
	public boolean applyChanges() {
        Control[] c = docMainForm.getChildren();
        return c.length == 0 || Utils.applyFormChanges(c[0]);
    }


    @Override
	public boolean close() {
        return applyChanges() && IOUtils.continueAnyway(dom);
    }


    @Override
	public boolean hasChanges() {
        Options.saveSash("documentation", sashForm.getWeights());

        return dom.isChanged();
    }


    @Override
	public boolean open(final Collection files) {
    	assert false;
        boolean res = false ; // IOUtils.openFile(files, dom);
        if (res) {
            initialize();
            listener.fillTree(docTree);
            docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
            listener.treeSelection(docTree, docMainForm);
        }

        return res;
    }

    public boolean open(final String filename, final Collection files) {
    	assert false;
        boolean res = false ; // IOUtils.openFile(filename ,files, dom);
        if (res) {
            initialize();
            listener.fillTree(docTree);
            docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
            listener.treeSelection(docTree, docMainForm);
        }

        return res;
    }

    @Override
	public void openBlank() {
        initialize();
        listener.fillTree(docTree);
        docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
        listener.treeSelection(docTree, docMainForm);
    }


    @Override
	public boolean save() {
        boolean res = IOUtils.saveFile(dom, false);
        if (res)
            container.setNewFilename(null);

        Utils.setResetElement(dom.getRoot());

        return res;
    }


    @Override
	public boolean saveAs() {
        String old = dom.getFilename();
        boolean res = IOUtils.saveFile(dom, true);
        if (res)
            container.setNewFilename(old);
        return res;
    }


    @Override
	public void updateLanguage() {
        if (docMainForm.getChildren().length > 0) {
            if (docMainForm.getChildren()[0] instanceof IUpdateLanguage) {
                ((IUpdateLanguage) docMainForm.getChildren()[0]).setToolTipText();
            }
        }
    }


    @Override
	public String getHelpKey() {
        if (docTree.getSelectionCount() > 0) {
            TreeItem item = docTree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            if (data != null && data.getHelpKey() != null)
                return data.getHelpKey();
        }
        return null;
    }


    @Override
	public String getFilename() {
        return dom.getFilename();
    }


    @Override
	public void dataChanged() {
        container.setStatusInTitle();
    }


    public static void openNoteDialog(final DocumentationDom dom, final Element parentElement, final String name, final boolean optional,final String title) {
        openNoteDialog(dom, parentElement, name, null, optional, true,title);
    }


    public static void openNoteDialog(final DocumentationDom dom, final Element parentElement, final String name, final boolean optional,
            final boolean changeStatus,final String title) {
        openNoteDialog(dom, parentElement, name, null, optional, changeStatus,title);
    }


    public static void openNoteDialog(final DocumentationDom dom, final Element parentElement, final String name, final String tooltip,
            final boolean optional,final String title) {
        openNoteDialog(dom, parentElement, name, tooltip, optional, true,title);
    }


    public static void openNoteDialog(final DocumentationDom dom, final Element parentElement, final String name, final String tooltip,
            final boolean optional, final boolean changeStatus, final String title) {
        NoteDialog dialog = new NoteDialog(MainWindow.getSShell(),title);
        dialog.setText("Note Editor");
        dialog.setTooltip(tooltip);

        dialog.setParams(dom, parentElement, name, optional, changeStatus);
        dialog.open();
    }


    public static void openNoteDialog(final DocumentationDom dom, final Element parentElement, final String name, final String tooltip,
            final boolean optional, final boolean changeStatus, final String title, final org.eclipse.swt.widgets.Text txt) {
        NoteDialog dialog = new NoteDialog(MainWindow.getSShell(),title);
        dialog.setText("Note Editor");
        //dialog.setUpdateText(txt); //Textfeld soll beim verlassen des Dialogs aktualisert werden
        dialog.setTooltip(tooltip);

        dialog.setParams(dom, parentElement, name, optional, changeStatus);
        dialog.open();
    }


	public DocumentationDom getDom() {
		return dom;
	}

	@Override
	public void updateReleases() {
		if(docTree.getSelectionCount() > 0) {
			TreeItem item = docTree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			org.jdom.Element elem = data.getElement();
			listener.treeFillReleases(item, elem);
			//tree.getSelection()[0].getItems()[tree.getSelection()[0].getItemCount()-1].setExpanded(true);
		}
	}

	public void updateDatabaseResource() {
		if(docTree.getSelectionCount() > 0) {
			TreeItem item = docTree.getSelection()[0];
			TreeData data = (TreeData) item.getData();
			org.jdom.Element elem = data.getElement();
			listener.treeFillDatabaseResources(item, elem.getChild("resources", elem.getNamespace()));
			//tree.getSelection()[0].getItems()[tree.getSelection()[0].getItemCount()-1].setExpanded(true);
		}
	}

	public void updateTree(final String which) {
		if(which.equals("main")) {
			//neu zeichnen und das erste Element markieren
			listener.fillTree(docTree);
			docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
		}
		listener.treeSelection(docTree, docMainForm);

	}
} // @jve:decl-index=0:visual-constraint="10,10"
