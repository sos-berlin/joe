package sos.scheduler.editor.conf.forms;

 
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.conf.composites.JobMainComposite;
import sos.scheduler.editor.conf.container.JobDelayAfterError;
import sos.scheduler.editor.conf.container.JobDocumentation;
import sos.scheduler.editor.conf.container.JobEmailSettings;
import sos.scheduler.editor.conf.container.JobOptions;
import sos.scheduler.editor.conf.container.JobSetback;
import sos.scheduler.editor.conf.container.JobSourceViewer;
import sos.scheduler.editor.conf.container.JobStartWhenDirectoryChanged;
import sos.scheduler.editor.conf.listeners.JobOptionsListener;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class ScriptJobMainForm extends ScriptForm {

    private JobOptionsListener objDataOptionsProvider;
    private Composite tabItemOptionsComposite = null;
    private Composite tabItemDirChangedComposite = null;
    private Composite objTabControlComposite = null;
    private Composite tabItemEMailComposite = null;
    private Composite tabItemDocumentationComposite = null;
    private Composite tabItemOrderSetBackComposite = null;
    private Composite tabItemDelayAfterErrorComposite = null;
    private Composite tabItemSourceViewerComposite = null;
    private JobMainComposite jobMainComposite = null;
    private CTabItem tabItemSourceViewer = null;
    private CTabItem tabItemEMail = null;
    private CTabItem tabItemDocumentation = null;
    private CTabItem tabItemOrderSetBack = null;
    private CTabItem tabItemDelayAfterError = null;
    private CTabItem tabItemOptions = null;
    private CTabItem tabItemDirChanged = null;
    private JobOptions objJobOptions = null;
    private Group objMainOptionsGroup = null;

    public ScriptJobMainForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
        super(parent, style, dom, job, main);
        showWaitCursor();
        objDataOptionsProvider = new JobOptionsListener(dom, job);
        initialize();
        restoreCursor();
    }

    public void initForm() {
        jobMainComposite.init();
        if (objDataOptionsProvider.hasProcessFile()) {
            MainWindow.message("<process file=> detected. This is no longer supported", SWT.ICON_ERROR);
            objDataOptionsProvider.deleteProcessFile();
        }
    }

    protected String[] getScriptLanguages() {
        return objDataProvider._languagesJob;
    }

    protected String getPredefinedFunctionNames() {
        return "spooler_init;spooler_open;spooler_process;spooler_close;spooler_exit;spooler_on_error;spooler_on_success";
    }

    protected void createGroup() {
        GridLayout gridLayoutMainOptionsGroup = new GridLayout();
        gridLayoutMainOptionsGroup.numColumns = 1;
        objMainOptionsGroup = new Group(this, SWT.NONE);
        objMainOptionsGroup.setText(objDataProvider.getJobNameAndTitle());
        objMainOptionsGroup.setLayout(gridLayoutMainOptionsGroup);
        objMainOptionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 1;
        gridLayout.numColumns = 1;
        jobMainComposite = new JobMainComposite(objMainOptionsGroup, SWT.NONE, objDataProvider);
        jobMainComposite.setLayout(gridLayout);
        jobMainComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        createLanguageSelector(jobMainComposite.getgMain());
        createScriptTabForm(objMainOptionsGroup);
        createJobMainTabPages();
        languageSelector.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent arg0) {
                disposeJobMainTabPages();
                createJobMainTabPages();
            }
        });
    }

    private void disposeJobMainTabPages() {
        if (tabItemSourceViewer != null) {
            tabItemSourceViewer.dispose();
        }
        if (tabItemEMail != null) {
            tabItemEMail.dispose();
        }
        if (tabItemDocumentation != null) {
            tabItemDocumentation.dispose();
        }
        if (tabItemOrderSetBack != null) {
            tabItemOrderSetBack.dispose();
        }
        if (tabItemDelayAfterError != null) {
            tabItemDelayAfterError.dispose();
        }
        if (tabItemOptions != null) {
            tabItemOptions.dispose();
        }
        if (tabItemDirChanged != null) {
            tabItemDirChanged.dispose();
        }
        if (tabItemDirChanged != null) {
            tabItemDirChanged.dispose();
        }
    }

    private void createJobMainTabPages() {
        tabItemSourceViewerComposite = null;
        tabItemOptionsComposite = null;
        tabItemDirChangedComposite = null;
        tabItemEMailComposite = null;
        tabItemDocumentationComposite = null;
        tabItemOrderSetBackComposite = null;
        tabItemDelayAfterErrorComposite = null;
        objTabControlComposite = new Composite(tabFolder, SWT.NONE);
        objTabControlComposite.setLayout(new GridLayout());
        setResizableV(objTabControlComposite);
        tabItemOptions = JOE_TI_ScriptJobMainForm_Options.control(new CTabItem(tabFolder, SWT.NONE));
        ScrolledComposite scroller = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

        tabItemOptionsComposite = new Composite(scroller, SWT.NONE);
        scroller.setContent(tabItemOptionsComposite);

        scroller.setExpandVertical(true);
        scroller.setExpandHorizontal(true);
        scroller.setMinSize(tabItemOptionsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        scroller.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                scroller.setMinSize(new Point(500,580));
            }
        });
        
        tabItemOptions.setControl(scroller);
        tabItemOptionsComposite.setLayout(new GridLayout());
        setResizableV(tabItemOptionsComposite);
        tabItemEMail = JOE_TI_ScriptJobMainForm_EMail.control(new CTabItem(tabFolder, SWT.NONE));
        tabItemEMailComposite = new Composite(tabFolder, SWT.NONE);
        tabItemEMailComposite.setLayout(new GridLayout());
        setResizableV(tabItemEMailComposite);
        tabItemEMail.setControl(tabItemEMailComposite);
        if (objDataProvider.isOrderJob()) {
            tabItemOrderSetBack = JOE_TI_ScriptJobMainForm_SetBack.control(new CTabItem(tabFolder, SWT.NONE));
            tabItemOrderSetBackComposite = new Composite(tabFolder, SWT.NONE);
            tabItemOrderSetBackComposite.setLayout(new GridLayout());
            setResizableV(tabItemOrderSetBackComposite);
            tabItemOrderSetBack.setControl(tabItemOrderSetBackComposite);
        } else {
            tabItemDelayAfterError = JOE_TI_ScriptJobMainForm_OnError.control(new CTabItem(tabFolder, SWT.NONE));
            tabItemDelayAfterErrorComposite = new Composite(tabFolder, SWT.NONE);
            tabItemDelayAfterErrorComposite.setLayout(new GridLayout());
            setResizableV(tabItemDelayAfterErrorComposite);
            tabItemDelayAfterError.setControl(tabItemDelayAfterErrorComposite);
            tabItemDirChanged = JOE_TI_ScriptJobMainForm_FileWatcher.control(new CTabItem(tabFolder, SWT.NONE));
            tabItemDirChangedComposite = new Composite(tabFolder, SWT.NONE);
            tabItemDirChangedComposite.setLayout(new GridLayout());
            setResizableV(tabItemDirChangedComposite);
            tabItemDirChanged.setControl(tabItemDirChangedComposite);
        }
        tabItemDocumentation = JOE_TI_ScriptJobMainForm_Doc.control(new CTabItem(tabFolder, SWT.NONE));
        tabItemDocumentationComposite = new Composite(tabFolder, SWT.NONE);
        tabItemDocumentationComposite.setLayout(new GridLayout());
        setResizableV(tabItemDocumentationComposite);
        tabItemDocumentation.setControl(tabItemDocumentationComposite);
        tabItemSourceViewer = JOE_TI_ScriptJobMainForm_XML.control(new CTabItem(tabFolder, SWT.NONE));
        tabItemSourceViewerComposite = new Composite(tabFolder, SWT.NONE);
        tabItemSourceViewerComposite.setLayout(new GridLayout());
        setResizableV(tabItemSourceViewerComposite);
        tabItemSourceViewer.setControl(tabItemSourceViewerComposite);
        createSetbackTab(tabItemOrderSetBackComposite);
        createDelayAfterErrorTab(tabItemDelayAfterErrorComposite);
        createDirChangedTab(tabItemDirChangedComposite);
        createEmailSettingsTab(tabItemEMailComposite);
        createDocumentationTab(tabItemDocumentationComposite);
        createOptionsTab(tabItemOptionsComposite);
        createSourceViewerTab(tabItemSourceViewerComposite);
    }

    private void createSourceViewerTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        new JobSourceViewer(pParentComposite, objDataProvider);
        pParentComposite.layout();
    }

    private void createDocumentationTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        new JobDocumentation(pParentComposite, objDataProvider);
        pParentComposite.layout();
    }

    private void createOptionsTab(final Composite pParentComposite) {
        objJobOptions = new JobOptions(pParentComposite, objDataProvider);
        objJobOptions.getbOrderYes().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (!objJobOptions.getbOrderYes().getSelection()) {
                    return;
                }
                disposeJobMainTabPages();
                createJobMainTabPages();
                tabFolder.setSelection(tabItemOptions);
            }
        });
        objJobOptions.getbOrderNo().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (!objJobOptions.getbOrderNo().getSelection()) {
                    return;
                }
                disposeJobMainTabPages();
                createJobMainTabPages();
                tabFolder.setSelection(tabItemOptions);
            }
        });
        pParentComposite.layout();
    }

    private void createSetbackTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        new JobSetback(pParentComposite, objDataOptionsProvider);
        pParentComposite.layout();
    }

    private void createDelayAfterErrorTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        new JobDelayAfterError(pParentComposite, objDataOptionsProvider);
        pParentComposite.layout();
    }

    private void createEmailSettingsTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        new JobEmailSettings(pParentComposite, objDataProvider);
        pParentComposite.layout();
    }

    private void createDirChangedTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        new JobStartWhenDirectoryChanged(pParentComposite, objDataProvider);
        pParentComposite.layout();
    }

}