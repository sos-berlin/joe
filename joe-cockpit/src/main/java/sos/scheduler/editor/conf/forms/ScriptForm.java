package sos.scheduler.editor.conf.forms;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;

import sos.scheduler.editor.classes.LanguageSelector;
import sos.scheduler.editor.conf.container.JobDotNetAPI;
import sos.scheduler.editor.conf.container.JobIncludeFile;
import sos.scheduler.editor.conf.container.JobJavaAPI;
import sos.scheduler.editor.conf.container.JobScript;
import sos.scheduler.editor.conf.listeners.JobListener;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public abstract class ScriptForm extends SOSJOEMessageCodes {

    protected Group objMainOptionsGroup = null;
    protected JobListener objDataProvider = null;
    protected ISchedulerUpdate update;
    protected Element job;
    protected LanguageSelector languageSelector = null;
    protected CTabFolder tabFolder = null;
    protected JobScript objJobScript = null;
    private static final Logger LOGGER = Logger.getLogger(ScriptJobMainForm.class);
    private final int intNoOfLabelColumns = 2;
    private boolean init = true;
    private Composite tabItemJavaAPIComposite = null;
    private Composite tabItemDotNetAPIComposite = null;
    private Composite tabItemIncludedFilesComposite = null;
    private Composite objTabControlComposite = null;
    private Composite tabItemScriptComposite = null;
    private CTabItem tabItemScript = null;
    private CTabItem tabItemJavaAPI = null;
    private CTabItem tabItemDotNetAPI = null;
    private CTabItem tabItemIncludedFiles = null;
    private JobJavaAPI objJobJavaAPI = null;
    private JobDotNetAPI objJobDotNetAPI = null;
    private JobIncludeFile objJobIncludeFile = null;
    private final SchedulerDom dom;

    protected abstract void initForm();

    protected abstract void createGroup();

    protected abstract String getPredefinedFunctionNames();

    protected abstract String[] getScriptLanguages();

    public ScriptForm(Composite parent, int style, SchedulerDom dom_, Element job_, ISchedulerUpdate main) {
        super(parent, style);
        job = job_;
        dom = dom_;
        update = main;
        dom.setInit(true);
        objDataProvider = new JobListener(dom, job, main);
        objDataProvider._languages = objDataProvider._languagesJob;
        dom.setInit(false);
    }

    public void apply() {
    }

    public boolean isUnsaved() {
        return false;
    }

    protected void initialize() {
        dom.setInit(true);
        init = true;
        this.setLayout(new GridLayout());
        createGroup();
        fillForm();
        setSize(new org.eclipse.swt.graphics.Point(723, 566));
        initForm();
        init = false;
        dom.setInit(true);
    }

    @Override
    protected void setResizableV(Control objControl) {
        boolean flgGrapVerticalspace = true;
        objControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, flgGrapVerticalspace));
    }

    protected void disposeTabPages() {
        if (tabItemScript != null) {
            tabItemScript.dispose();
        }
        if (tabItemJavaAPI != null) {
            tabItemJavaAPI.dispose();
        }
        if (tabItemDotNetAPI != null) {
            tabItemDotNetAPI.dispose();
        }
        if (tabItemIncludedFiles != null) {
            tabItemIncludedFiles.dispose();
        }
    }

    protected void createScriptTabForm(Group pobjMainOptionsGroup) {
        tabFolder = new CTabFolder(pobjMainOptionsGroup, SWT.None);
        tabFolder.setLayout(new GridLayout());
        setResizableV(tabFolder);
        tabFolder.setSimple(true);
        tabFolder.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                int intIndex = tabFolder.getSelectionIndex();
                Options.setLastTabItemIndex(intIndex);
                LOGGER.debug(JOE_M_ScriptForm_ItemIndex.params(tabFolder.getSelectionIndex()));
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        createTabPages();
        tabFolder.setSelection(0);
    }

    private void createTabPages() {
        tabItemJavaAPIComposite = null;
        tabItemDotNetAPIComposite = null;
        tabItemIncludedFilesComposite = null;
        tabItemScriptComposite = null;
        objTabControlComposite = new Composite(tabFolder, SWT.NONE);
        objTabControlComposite.setLayout(new GridLayout());
        setResizableV(objTabControlComposite);
        tabItemScript = JOE_ScriptForm_TabItemScript.control(new CTabItem(tabFolder, SWT.NONE));
        tabItemScriptComposite = new Composite(tabFolder, SWT.NONE);
        tabItemScriptComposite.setLayout(new GridLayout());
        setResizableV(tabItemScriptComposite);
        tabItemScript.setControl(tabItemScriptComposite);
        if (objDataProvider.isJvm()) {
            tabItemJavaAPI = JOE_ScriptForm_TabItemJavaAPI.control(new CTabItem(tabFolder, SWT.NONE));
            tabItemJavaAPIComposite = new Composite(tabFolder, SWT.NONE);
            tabItemJavaAPIComposite.setLayout(new GridLayout());
            setResizableV(tabItemJavaAPIComposite);
            tabItemJavaAPI.setControl(tabItemJavaAPIComposite);
        }
        if (objDataProvider.isDotNet()) {
            tabItemDotNetAPI = JOE_ScriptForm_TabItemDotNetAPI.control(new CTabItem(tabFolder, SWT.NONE));
            tabItemDotNetAPIComposite = new Composite(tabFolder, SWT.NONE);
            tabItemDotNetAPIComposite.setLayout(new GridLayout());
            setResizableV(tabItemDotNetAPIComposite);
            tabItemDotNetAPI.setControl(tabItemDotNetAPIComposite);
        }
        tabItemIncludedFilesComposite = new Composite(tabFolder, SWT.NONE);
        tabItemIncludedFilesComposite.setLayout(new GridLayout());
        setResizableV(tabItemIncludedFilesComposite);
        tabItemIncludedFiles = JOE_ScriptForm_TabItemIncludes.control(new CTabItem(tabFolder, SWT.NONE));
        tabItemIncludedFiles.setControl(tabItemIncludedFilesComposite);
        createJavaAPITab(tabItemJavaAPIComposite);
        createDotNetAPITab(tabItemDotNetAPIComposite);

        createTabItemIncludeFile(tabItemIncludedFilesComposite);
        createScriptTab(tabItemScriptComposite);
    }

    private void createScriptTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobScript = new JobScript(pParentComposite, objDataProvider);
        pParentComposite.layout();
    }

    private void createJavaAPITab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobJavaAPI = new JobJavaAPI(pParentComposite, objDataProvider, objJobJavaAPI);
        pParentComposite.layout();
    }

    private void createDotNetAPITab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobDotNetAPI = new JobDotNetAPI(pParentComposite, objDataProvider, objJobDotNetAPI);
        pParentComposite.layout();
    }

    private void createTabItemIncludeFile(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobIncludeFile = new JobIncludeFile(pParentComposite, objDataProvider, objJobIncludeFile);
        pParentComposite.layout();
    }

    protected void createLanguageSelector(Composite pobjComposite) {
        Label labelLanguageSelector = JOE_L_ScriptForm_Language.control(new Label(pobjComposite, SWT.NONE));
        labelLanguageSelector.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, intNoOfLabelColumns, 1));
        languageSelector = new LanguageSelector(pobjComposite, SWT.NONE);
        languageSelector.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent arg0) {
                if (objDataProvider != null && !init) {
                    String strT = languageSelector.getText();
                    objDataProvider.setLanguage(strT);
                    disposeTabPages();
                    createTabPages();
                    objJobScript.getCboPrefunction().setEnabled(false);
                    if (languageSelector.isJava() && objJobJavaAPI.getTbxClassName() != null) {
                        objJobJavaAPI.getTbxClassName().setFocus();
                        objJobJavaAPI.getTClasspath().setText(objDataProvider.getClasspath());
                        if (!"".equals(objJobJavaAPI.getTbxClassName().getText()) && "".equals(objDataProvider.getJavaClass())) {
                            objDataProvider.setJavaClass(objJobJavaAPI.getTbxClassName().getText());
                        }
                        objJobJavaAPI.getTbxClassName().setText(objDataProvider.getJavaClass());
                        tabFolder.setSelection(tabItemJavaAPI);
                    } else {
                        if (languageSelector.isDotNet() && objJobDotNetAPI.getDotNetDll() != null) {
                            objJobDotNetAPI.getDotNetDll().setFocus();
                            objJobDotNetAPI.getDotNetClass().setText(objDataProvider.getDotNetClass());
                            if (!"".equals(objJobDotNetAPI.getDotNetDll().getText()) && "".equals(objDataProvider.getDotNetDll())) {
                                objDataProvider.setDotNetDll(objJobDotNetAPI.getDotNetDll().getText());
                            }
                            objJobDotNetAPI.getDotNetDll().setText(objDataProvider.getDotNetDll());
                            tabFolder.setSelection(tabItemDotNetAPI);
                        } else {
                            String lan = "";
                            tabFolder.setSelection(tabItemScript);
                            objJobScript.gettSource().setFocus();
                            if (languageSelector.isScriptLanguage()) {
                                lan = getPredefinedFunctionNames();
                                objJobScript.getCboPrefunction().setItems(lan.split(";"));
                                objJobScript.getCboPrefunction().setEnabled(true);
                            }
                        }
                    }
                }
            }
        });
        GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_combo.minimumWidth = 100;
        languageSelector.setLayoutData(gd_combo);
        languageSelector.select(0);
        languageSelector.setItems(getScriptLanguages());
    }

    protected void fillForm() {
        init = true;
        int language = objDataProvider.getLanguage();
        if (objJobScript.getCboPrefunction() != null) {
            objJobScript.getCboPrefunction().removeAll();
        }
        languageSelector.selectLanguageItem(language);
        if (language != JobListener.NONE) {
            objDataProvider.fillTable(objJobIncludeFile.getTableIncludes());
        } else {
            languageSelector.selectLanguageItem(0);
            objDataProvider.setLanguage(0);
        }
        String lan = "";
        if (!languageSelector.isShell() && !languageSelector.isJava()) {
            lan = this.getPredefinedFunctionNames();
            objJobScript.getCboPrefunction().setItems(lan.split(";"));
        }
        if (languageSelector.isJava()) {
            tabFolder.setSelection(tabItemJavaAPI);
        } else {
            if (languageSelector.isDotNet()) {
                tabFolder.setSelection(tabItemDotNetAPI);
            } else {
                tabFolder.setSelection(tabItemScript);
                objJobScript.gettSource().setFocus();
            }
        }
        init = false;
    }

    public JobJavaAPI getObjJobJAPI() {
        return objJobJavaAPI;
    }

    public JobIncludeFile getObjJobIncludeFile() {
        return objJobIncludeFile;
    }

    public CTabItem getTabItemIncludedFiles() {
        return tabItemIncludedFiles;
    }

}