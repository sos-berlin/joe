package com.sos.joe.jobdoc.editor.forms;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
// import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import com.sos.dialog.Globals;
import com.sos.dialog.classes.SOSComposite;
import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSSashForm;
import com.sos.dialog.classes.SOSTree;
import com.sos.joe.globals.interfaces.IEditorAdapter;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSMsgJOE;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.jobdoc.editor.IDocumentationUpdate;
import com.sos.joe.jobdoc.editor.NoteDialog;
import com.sos.joe.jobdoc.editor.listeners.DocumentationListener;
import com.sos.joe.xml.IOUtils;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class DocumentationForm extends JobDocBaseForm<DocumentationListener> implements IDocumentationUpdate /*
                                                                                                              * extends
                                                                                                              * SOSJOEMessageCodes
                                                                                                              * implements
                                                                                                              * IEditor
                                                                                                              * ,
                                                                                                              * IDocumentationUpdate
                                                                                                              */{

    private SOSSashForm sashForm = null;
    private SOSGroup group = null;
    private Composite docMainForm = null;
    private SOSTree docTree = null;
    private TreeItem selection = null;
    private IEditorAdapter container = null;

    public DocumentationForm(IEditorAdapter container_, Composite parent, int style) {
        super(parent, style);
        container = container_;
        dom = new DocumentationDom();
        dom.setDataChangedListener(this);
        listener = new DocumentationListener(this, dom);
    }

    private void initialize() {
        createSashForm();
        // setSize(new Point(724, 479));
        // setLayout(new GridLayout());
    }

    /** This method initializes sashForm */
    private void createSashForm() {
        sashForm = new SOSSashForm(this, SWT.NONE, "JobDocDocumentationForm");
        createGroup();
        createDocMainForm();
        sashForm.restoreSize();
    }

    /** This method initializes group */
    private void createGroup() {
        group = new SOSMsgJOE("JOE_G_DocumentationForm_DocElements").Control(new SOSGroup(sashForm, SWT.V_SCROLL | SWT.H_SCROLL));
        docTree = new SOSTree(group, SWT.NONE);
        docTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        docTree.setBackground(Globals.getCompositeBackground());

        docTree.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event e) {
                if (docTree.getSelectionCount() > 0) {
                    if (selection == null) {
                        selection = docTree.getItem(0);
                    }
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

    private void createDocMainForm() {
        docMainForm = new SOSComposite(sashForm, SWT.NONE);
        // docMainForm.setLayout(new FillLayout());
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

    @Override
    public void openBlank() {
        initialize();
        listener.fillTree(docTree);
        docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
        listener.treeSelection(docTree, docMainForm);
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
        container.setSaveStatus();
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
        // dialog.setUpdateText(txt); //Textfeld soll beim verlassen des Dialogs
        // aktualisert werden
        dialog.setTooltip(tooltip);
        dialog.setParams(dom, parentElement, name, optional, changeStatus);
        dialog.open();
    }

    @Override
    public void updateReleases() {
        if (docTree.getSelectionCount() > 0) {
            TreeItem item = docTree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            org.jdom.Element elem = data.getElement();
            listener.treeFillReleases(item, elem);
            // tree.getSelection()[0].getItems()[tree.getSelection()[0].getItemCount()-1].setExpanded(true);
        }
    }

    public void updateDatabaseResource() {
        if (docTree.getSelectionCount() > 0) {
            TreeItem item = docTree.getSelection()[0];
            TreeData data = (TreeData) item.getData();
            org.jdom.Element elem = data.getElement();
            listener.treeFillDatabaseResources(item, elem.getChild("resources", elem.getNamespace()));
            // tree.getSelection()[0].getItems()[tree.getSelection()[0].getItemCount()-1].setExpanded(true);
        }
    }

    @Override
    public void updateTree(String which) {
        if (which.equals("main")) {
            // neu zeichnen und das erste Element markieren
            listener.fillTree(docTree);
            docTree.setSelection(new TreeItem[] { docTree.getItem(0) });
        }
        listener.treeSelection(docTree, docMainForm);
    }

    @Override
    protected void applySetting() {
    }

    @Override
    public boolean applyChanges() {
        Control[] c = docMainForm.getChildren();
        return c.length == 0 || Utils.applyFormChanges(c[0]);
    }

    @Override
    public boolean open(Collection files) {
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
