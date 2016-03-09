package com.sos.joe.jobdoc.editor;

import static sos.util.SOSClassUtil.getMethodName;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.jdom.Element;
import org.jdom.JDOMException;

import com.sos.dialog.BrowserViewForm;
import com.sos.dialog.Globals;
import com.sos.dialog.classes.SOSCTabFolder;
import com.sos.dialog.classes.SOSCTabItem;
import com.sos.dialog.classes.SOSSashForm;
import com.sos.dialog.classes.WindowsSaver;
import com.sos.dialog.components.TextArea;
import com.sos.dialog.components.WaitCursor;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSMsgJOE;
import com.sos.joe.jobdoc.editor.TreeViewEntry.enuTreeItemType;
import com.sos.joe.jobdoc.editor.forms.DocumentationForm;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class Application extends ApplicationWindow {

    private static final String conI18NKey_JOE_L_JOB_DOC_XML = "JOE_L_JobDoc.XML";
    private static final String conI18NKey_JOE_L_JOB_DOC_FORMAT = "JOE_L_JobDoc.Format";
    private static final String conI18NKey_JOE_L_JOB_DOC_DESIGN = "JOE_L_JobDoc.Design";
    @SuppressWarnings("unused")
    private final String conClassName = this.getClass().getSimpleName();
    @SuppressWarnings("unused")
    private static final String conSVNVersion = "$Id$";
    @SuppressWarnings("unused")
    private final Logger logger = Logger.getLogger(this.getClass());
    private WindowsSaver objPersistenceStore;

    /** Create the application window. */
    public Application() {
        super(null);
        createActions();
        addToolBar(SWT.FLAT | SWT.WRAP);
        addMenuBar();
        addStatusLine();
    }

    private void initGlobals(Display display) {
        Globals.MsgHandler = new SOSMsgJOE("init");
        Globals.flgIgnoreColors = false;
        Globals.stFontRegistry.put("button-text", new FontData[] { new FontData("Arial", 9, SWT.BOLD) });
        Globals.stFontRegistry.put("code", new FontData[] { new FontData("Courier New", 12, SWT.NORMAL) });
        Globals.stFontRegistry.put("text", new FontData[] { new FontData("Arial", 10, SWT.NORMAL) });
        Globals.stFontRegistry.put("tabitem-text", new FontData[] { new FontData("", 9, SWT.NORMAL) });
        Globals.stColorRegistry.put("IncludedOption", display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW).getRGB());
        Globals.stColorRegistry.put("MandatoryFieldColor", display.getSystemColor(SWT.COLOR_BLUE).getRGB());
        Globals.stColorRegistry.put("Color4FieldHasFocus", display.getSystemColor(SWT.COLOR_GREEN).getRGB());
        // Colorschema
        // Globals.stColorRegistry.put("FieldBackGround", new RGB(220, 249, 0));
        // // var.
        Globals.stColorRegistry.put("FieldBackGround", new RGB(242, 247, 247)); // var.
                                                                                // 1
                                                                                // =
                                                                                // #DCF900
                                                                                // =
                                                                                // rgb(220,249,0)
        Globals.stColorRegistry.put("Color4FieldHasFocus", new RGB(124, 231, 0)); // var.
                                                                                  // 1
                                                                                  // =
                                                                                  // #7CE700
                                                                                  // =
                                                                                  // rgb(124,231,0)
        Globals.stColorRegistry.put("Color4FieldInError", new RGB(255, 225, 0)); // var.
                                                                                 // 1
                                                                                 // =
                                                                                 // #FFE100
                                                                                 // =
                                                                                 // rgb(255,225,0)
        // Globals.stColorRegistry.put("CompositeBackGround", new
        // RGB(236,252,113)); // var. 5 = #ECFC71 = rgb(236,252,113)
        // var. 5 = #FFFFB0 = rgb(255,255,176)
        Globals.stColorRegistry.put("CompositeBackGround", new RGB(242, 247, 247)); //
        Globals.setApplicationWindow(this);
        // Globals.MsgHandler = new JADEMsg("init");
    }

    /** Create contents of the application window.
     * 
     * @param parent */
    @Override
    protected Control createContents(final Composite parent) {
        Display display = parent.getDisplay();
        assert display != null;
        initGlobals(display);
        final Shell shell = this.getShell();
        ErrorLog.setSShell(shell);
        objPersistenceStore = new WindowsSaver(this.getClass(), shell, 940, 600);
        objPersistenceStore.restoreWindow();

        Composite container = new Composite(parent, SWT.NONE);
        GridLayout gl_container = new GridLayout(1, true);
        container.setLayout(gl_container);
        final SOSSashForm sashForm = new SOSSashForm(container, SWT.BORDER | SWT.SMOOTH | SWT.HORIZONTAL, "JobDocApplication");

        parent.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                logger.debug("control resized");
            }
        });

        //
        TreeViewer treeViewer = new TreeViewer(sashForm, SWT.BORDER);
        Tree objTree = treeViewer.getTree();
        objTree.setBackground(Globals.getCompositeBackground());

        treeViewer.addTreeListener(new ITreeViewerListener() {

            @Override
            public void treeCollapsed(final TreeExpansionEvent event) {
            }

            @Override
            public void treeExpanded(final TreeExpansionEvent event) {
            }
        });
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(final DoubleClickEvent objEvent) {
                try (WaitCursor objWC = new WaitCursor()) {
                    IStructuredSelection objSelection = (IStructuredSelection) objEvent.getSelection();
                    TreeViewer objTv = (TreeViewer) objEvent.getSource();
                    Tree tree1 = objTv.getTree();
                    int i = 0;
                    for (Object objSel : objSelection.toList()) {
                        if (objSel instanceof TreeViewEntry) {
                            TreeViewEntry objTreeViewEntry = (TreeViewEntry) objSel;
                            if (objTreeViewEntry.isFile()) {
                                String strM = "Section ausgewählt: " + objTreeViewEntry.getName();
                                setStatus(strM);
                                objTreeViewEntry.setTreeItem(tree1.getSelection()[i++]);
                                createTabFolder(objTreeViewEntry);
                            }
                        }
                    }

                } catch (Exception e) {
                }
            }
        });
        // Tree tree = treeViewer.getTree();
        FillTree(treeViewer);
        tabFolder = new SOSCTabFolder(sashForm, SWT.BORDER);
        tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        objPersistenceStore.restoreWindow();
        sashForm.restoreSize();
        return container;
    }

    // private final String strBaseDir =
    // "C:\\Program Files\\sos-berlin.com\\jobscheduler\\scheduler#4444\\scheduler_data\\jobs";
    private String strBaseDir = "R:/java.sources/trunk/products/jobscheduler/sos-scheduler/src/main/java/sos/scheduler/jobdoc";
    private SOSCTabFolder tabFolder = null;
    private static MainWindow window = null;
    private static Display display = null;

    private void createTabFolder(final TreeViewEntry objTVE) {
        String strFileName = objTVE.getFile().getAbsolutePath();
        openDocumentation(tabFolder, strFileName);
    }

    private void openDocumentation(Composite objComposite, final String pstrFileName2Open) {
        try {
            final SOSCTabFolder objTabFolder = new SOSCTabFolder(objComposite, SWT.BOTTOM /* TabItemPos */);
            objTabFolder.ItemsHasClose = false;
            final SOSCTabItem objDesignTab = objTabFolder.getTabItem(conI18NKey_JOE_L_JOB_DOC_DESIGN);
            final SOSCTabItem objFormattedTab = objTabFolder.getTabItem(conI18NKey_JOE_L_JOB_DOC_FORMAT);
            final SOSCTabItem objXMLTab = objTabFolder.getTabItem(conI18NKey_JOE_L_JOB_DOC_XML);
            newItem(objTabFolder, pstrFileName2Open);

            final DocumentationForm objJobDocForm = new DocumentationForm(null, objTabFolder, SWT.NONE);
            if (objJobDocForm.open(pstrFileName2Open, filelist)) {
                objDesignTab.setControl(objJobDocForm);
            } else
                return;
            //
            objTabFolder.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent event) {
                    try (WaitCursor objWC = new WaitCursor()) {
                        logger.debug("CTabFolder Item selected");
                        CTabItem objSelectedItem = objTabFolder.getSelection();
                        // TreeViewEntry objTVE = (TreeViewEntry)
                        // objSelectedItem.getData();
                        String strI18NKey = (String) objSelectedItem.getData("key");
                        DocumentationDom objDom = objJobDocForm.getDom();
                        Element element = objDom.getRoot();

                        switch (strI18NKey) {
                        case conI18NKey_JOE_L_JOB_DOC_DESIGN:
                            break;

                        case conI18NKey_JOE_L_JOB_DOC_FORMAT:
                            if (element != null) {
                                try {
                                    String filename = objDom.transform(element, pstrFileName2Open);
                                    if (filename.length() > 0) {
                                        URL objUrl = new File(filename).toURI().toURL();
                                        BrowserViewForm objBrowser = new BrowserViewForm(objTabFolder, SWT.NONE, objUrl.toString());
                                        objFormattedTab.setControl(objBrowser.getBrowser());
                                    }
                                } catch (Exception ex) {
                                    new ErrorLog("error in preview.", ex);
                                }
                            }
                            break;

                        case conI18NKey_JOE_L_JOB_DOC_XML:
                            TextArea objTA = new TextArea(objTabFolder, "JobDoc.XMLSource");
                            // objTA.setEditable(false);
                            try {
                                String strXML = objDom.getXML(element);
                                objTA.setXMLText(strXML);
                                objXMLTab.setControl(objTA);
                                objTA.refreshContent();
                            } catch (JDOMException e) {
                                new ErrorLog("error in getxml.", e);
                            }
                            break;

                        default:
                            break;
                        }
                    } catch (Exception e) {
                        new ErrorLog("Problem", e);
                    }
                }
            });
        } catch (Exception e) {
            new ErrorLog("error in " + getMethodName(), e);
        }
    }

    private final ArrayList<String> filelist = new ArrayList<String>();
    private final String strTitleText = "";

    private SOSCTabItem newItem(Control control, String filename) {
        SOSCTabItem objTabITem = new SOSCTabItem(tabFolder, SWT.CLOSE);
        objTabITem.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(final DisposeEvent e) {
            }
        });
        objTabITem.setControl(control);
        objTabITem.setShowClose(true);
        tabFolder.setSelection(tabFolder.indexOf(objTabITem));
        // tab.setData(new TabData(filename, strTitleText));
        // TreeViewItem t = (TreeViewItem) objTabITem.getData();
        // t.caption = filename;
        objTabITem.setText(filename);
        // filelist.add(filename);
        return objTabITem;
    }

    protected void FillTree(final TreeViewer pobjTreeView) {
        pobjTreeView.setContentProvider(new FileTreeContentProvider());
        pobjTreeView.setLabelProvider(new FileTreeLabelProvider());
        String strT = System.getProperty("Joe.JobDoc.Basedir");
        if (strT != null) {
            strBaseDir = strT;
        }
        pobjTreeView.setInput(strBaseDir); // pass a non-null that will be
                                           // ignored
    }

    /** Return the initial size of the window. */
    @Override
    protected Point getInitialSize() {
        objPersistenceStore = new WindowsSaver(this.getClass(), this.getShell(), 940, 600);
        return objPersistenceStore.getWindowSize();
    }

    /** This class provides the labels for the file tree */
    class FileTreeLabelProvider implements ILabelProvider {

        // The listeners
        private final List listeners;
        // Label provider state: preserve case of file names/directories
        boolean preserveCase = true;

        /** Constructs a FileTreeLabelProvider */
        public FileTreeLabelProvider() {
            // Create the list to hold the listeners
            listeners = new ArrayList();
        }

        /** Sets the preserve case attribute
         * 
         * @param preserveCase the preserve case attribute */
        public void setPreserveCase(final boolean preserveCase) {
            this.preserveCase = preserveCase;
            // Since this attribute affects how the labels are computed,
            // notify all the listeners of the change.
            LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
            for (int i = 0, n = listeners.size(); i < n; i++) {
                ILabelProviderListener ilpl = (ILabelProviderListener) listeners.get(i);
                ilpl.labelProviderChanged(event);
            }
        }

        /** Gets the image to display for a node in the tree
         * 
         * @param arg0 the node
         * @return Image */
        @Override
        public Image getImage(final Object arg0) {
            TreeViewEntry objTV = (TreeViewEntry) arg0;
            return objTV.getImage();
        }

        /** Gets the text to display for a node in the tree
         * 
         * @param arg0 the node
         * @return String */
        @Override
        public String getText(final Object arg0) {
            // Get the name of the file
            TreeViewEntry objTV = (TreeViewEntry) arg0;
            String text = objTV.getName();
            // If name is blank, get the path
            // Check the case settings before returning the text
            return preserveCase ? text : text.toUpperCase();
        }

        /** Adds a listener to this label provider
         * 
         * @param arg0 the listener */
        @Override
        public void addListener(final ILabelProviderListener arg0) {
            listeners.add(arg0);
        }

        /** Called when this LabelProvider is being disposed */
        @Override
        public void dispose() {
            // Dispose the images
        }

        /** Returns whether changes to the specified property on the specified
         * element would affect the label for the element
         * 
         * @param arg0 the element
         * @param arg1 the property
         * @return boolean */
        @Override
        public boolean isLabelProperty(final Object arg0, final String arg1) {
            return false;
        }

        /** Removes the listener
         * 
         * @param arg0 the listener to remove */
        @Override
        public void removeListener(final ILabelProviderListener arg0) {
            listeners.remove(arg0);
        }
    }

    /** This class provides the content for the tree in FileTree */
    class FileTreeContentProvider implements ITreeContentProvider {

        /** Gets the children of the specified object
         * 
         * @param arg0 the parent object
         * @return Object[] */
        @Override
        public Object[] getChildren(final Object parentElement) {
            TreeViewEntry[] objO = new TreeViewEntry[] {};
            Vector<TreeViewEntry> objV = new Vector<TreeViewEntry>();
            if (parentElement instanceof TreeViewEntry) {
                TreeViewEntry objTV = (TreeViewEntry) parentElement;
                switch (objTV.getType()) {
                case IsRoot:
                    break;
                case isDirectory:
                    // Return the files and subdirectories in this directory
                    Object[] objFiles = objTV.getFile().listFiles();
                    if (objFiles != null) {
                        for (Object objFle : objFiles) {
                            File objFile = (File) objFle;
                            String strName = objFile.getName();
                            boolean flgDoAdd = false;
                            enuTreeItemType intType = TreeViewEntry.enuTreeItemType.isDirectory;
                            if (objFile.isDirectory()) {
                                if (strName.startsWith(".")) {

                                } else {
                                    flgDoAdd = true;
                                }
                            } else {
                                if (strName.endsWith(".xml") || strName.endsWith(".sosdoc") || strName.endsWith(".jobdoc")) {
                                    intType = TreeViewEntry.enuTreeItemType.isFile;
                                    flgDoAdd = true;
                                }
                            }
                            if (flgDoAdd) {
                                TreeViewEntry objE = new TreeViewEntry(intType);
                                objE.setFile(objFile);
                                objV.add(objE);
                            }
                        }
                    }
                    break;
                // case profiles_root:
                // objO = model.getProfiles();
                // break;
                default:
                    break;
                }
            }
            return objV.toArray();
        }

        /** Gets the parent of the specified object
         * 
         * @param arg0 the object
         * @return Object */
        @Override
        public Object getParent(final Object arg0) {
            // Return this file's parent file
            return ((File) arg0).getParentFile();
        }

        /** Returns whether the passed object has children
         * 
         * @param arg0 the parent object
         * @return boolean */
        @Override
        public boolean hasChildren(final Object arg0) {
            // Get the children
            Object[] obj = getChildren(arg0);
            // Return whether the parent has children
            return obj == null ? false : obj.length > 0;
        }

        /** Gets the root element(s) of the tree
         * 
         * @param arg0 the input data
         * @return Object[] */
        @Override
        public Object[] getElements(final Object arg0) {
            File objF;
            Vector<TreeViewEntry> objV = new Vector<>();
            if (arg0 instanceof String) {
                objF = new File((String) arg0);
            } else {
                if (arg0 instanceof TreeViewEntry) {
                    objF = ((TreeViewEntry) arg0).getFile();
                } else {
                    objF = (File) arg0;
                }
            }
            File[] objFiles = objF.listFiles();
            if (objFiles != null) {
                for (Object objFle : objFiles) {
                    File objFile = (File) objFle;
                    boolean flgDoAdd = false;
                    enuTreeItemType intType = TreeViewEntry.enuTreeItemType.isDirectory;
                    if (objFile.isDirectory()) {
                        flgDoAdd = true;
                    } else {
                        if (objFile.getName().endsWith(".xml") || objFile.getName().endsWith(".sosdoc") || objFile.getName().endsWith(".jobdoc")) {
                            intType = TreeViewEntry.enuTreeItemType.isFile;
                            flgDoAdd = true;
                        }
                    }
                    if (flgDoAdd) {
                        TreeViewEntry objE = new TreeViewEntry(intType);
                        objE.setFile(objFile);
                        objV.add(objE);
                    }
                }
            }
            return objV.toArray();
        }

        /** Disposes any created resources */
        @Override
        public void dispose() {
            // Nothing to dispose
        }

        /** Called when the input changes
         * 
         * @param arg0 the viewer
         * @param arg1 the old input
         * @param arg2 the new input */
        @Override
        public void inputChanged(final Viewer arg0, final Object arg1, final Object arg2) {
            // Nothing to change
        }
    }

    /** Helper method for our maximize behavior. If the passed control is already
     * maximized, restore it. Otherwise, maximize it
     * 
     * @param control the control to maximize or restore
     * @param sashForm the parent SashForm */
    private void maximizeHelper(final Control control, final SashForm sashForm) {
        // See if the control is already maximized
        if (control == sashForm.getMaximizedControl()) {
            // Already maximized; restore it
            sashForm.setMaximizedControl(null);
        } else {
            // Not yet maximized, so maximize it
            sashForm.setMaximizedControl(control);
        }
    }

    /** Create the actions. */
    private void createActions() {
        // Create the actions
    }

    /** Create the menu manager.
     * 
     * @return the menu manager */
    @Override
    protected MenuManager createMenuManager() {
        MenuManager menuManager = new MenuManager("menu");
        return menuManager;
    }

    /** Create the toolbar manager.
     * 
     * @return the toolbar manager */
    @Override
    protected ToolBarManager createToolBarManager(final int style) {
        ToolBarManager toolBarManager = new ToolBarManager(style);
        return toolBarManager;
    }

    /** Create the status line manager.
     * 
     * @return the status line manager */
    @Override
    protected StatusLineManager createStatusLineManager() {
        StatusLineManager statusLineManager = new StatusLineManager();
        return statusLineManager;
    }

    /** Configure the shell.
     * 
     * @param newShell */
    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("SOS Documentation Editor");
    }
}
