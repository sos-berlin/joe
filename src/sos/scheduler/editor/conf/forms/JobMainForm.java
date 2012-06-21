package sos.scheduler.editor.conf.forms;

//import org.eclipse.draw2d.*;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.LanguageSelector;

import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.container.JobDelayAfterError;
import sos.scheduler.editor.conf.container.JobDocumentation;
import sos.scheduler.editor.conf.container.JobEmailSettings;
import sos.scheduler.editor.conf.container.JobIncludeFile;
import sos.scheduler.editor.conf.container.JobJavaAPI;
import sos.scheduler.editor.conf.container.JobOptions;
import sos.scheduler.editor.conf.container.JobProcessFile;
import sos.scheduler.editor.conf.container.JobScript;
import sos.scheduler.editor.conf.container.JobSetback;
import sos.scheduler.editor.conf.container.JobSourceViewer;
import sos.scheduler.editor.conf.container.JobStartWhenDirectoryChanged;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.JobOptionsListener;

public class JobMainForm extends Composite implements IUpdateLanguage {

    @SuppressWarnings("unused")
    private final String conSVNVersion = "$Id$";

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(JobMainForm.class);
    @SuppressWarnings("unused")
    private final String conClassName = "JobMainForm";

    private JobListener objDataProvider = null;
    private JobOptionsListener objDataOptionsProvider;
    private Group objMainOptionsGroup = null;
    private Group gMain = null;
    private Text tbxJobName = null;
    private Label lblJobTitlelabel1 = null;
    private Label lblProcessClass = null;
    private Text tbxJobTitle = null;
    private Combo cProcessClass = null;
    private boolean updateTree = false;
    private Button butBrowse = null;
    private boolean init = true;
    private Button butShowProcessClass = null;
    @SuppressWarnings("unused")
    private int _style = 0;

    private int type = Editor.SCRIPT;

    private Label label = null;
    private Text txtName = null;
    private Spinner spinner = null;

    private LanguageSelector languageSelector = null;

    private int intComboBoxStyle = SWT.NONE;

    private Composite tabItemJavaAPIComposite = null;
    private Composite tabItemOptionsComposite = null;
    private Composite tabItemDirChangedComposite = null;
    private Composite tabItemIncludedFilesComposite = null;
    private Composite objTabControlComposite = null;
    private Composite tabItemEMailComposite = null;
    private Composite tabItemSourceViewerComposite = null;
    private Composite tabItemDocumentationComposite = null;
    private Composite tabItemOrderSetBackComposite = null;
    private Composite tabItemDelayAfterErrorComposite = null;
    private Composite tabItemProcessFileComposite = null;
    private Composite tabItemScriptComposite = null;

    private GridLayout gridLayout = null;

    private CTabItem tabItemEMail = null;
    private CTabItem tabItemSourceViewer = null;
    private CTabItem tabItemDocumentation = null;
    private CTabItem tabItemOrderSetBack = null;
    private CTabItem tabItemDelayAfterError = null;
    private CTabItem tabItemProcessFile = null;
    private CTabItem tabItemScript = null;
    private CTabItem tabItemJavaAPI = null;
    private CTabItem tabItemOptions = null;
    private CTabItem tabItemDirChanged = null;
    private CTabItem tabItemIncludedFiles = null;
    private CTabFolder tabFolder = null;

    private JobSourceViewer objJobSourceViewer = null;
    private JobScript objJobScript = null;
    private JobProcessFile objJobProcessFile = null;
    private JobEmailSettings objJobEmailSettings = null;
    private JobOptions objJobOptions = null;
    private JobSetback objJobSetback = null;
    private JobDelayAfterError objDelayAfterError = null;
    private JobJavaAPI objJobJAPI = null;
    private JobDocumentation objJobJD = null;
    private JobStartWhenDirectoryChanged objJobSWDC = null;
    private JobIncludeFile objJobIncludeFile = null;

    public JobMainForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
        super(parent, style);
        // final ToolTipHandler tooltip = new ToolTipHandler(parent.getShell());

        init = true;
        _style = style;
        dom.setInit(true);
        this.setEnabled(Utils.isElementEnabled("job", dom, job));
        objDataProvider = new JobListener(dom, job, main);
        objDataProvider.setType(Editor.SCRIPT);

        objDataOptionsProvider = new JobOptionsListener(dom, job);

        initialize();
        updateTree = false;
        initForm();
        dom.setInit(false);
        tbxJobName.setFocus();
        init = false;
    }

    public void apply() {
        // if (isUnsaved())
        // addParam();
    }

    public boolean isUnsaved() {
        // return bApply.isEnabled();
        return false;
    }

    private void initialize() {
        this.setLayout(new GridLayout());
        createGroup();
        fillForm();
        setSize(new org.eclipse.swt.graphics.Point(723, 566));
    }

    private int intNoOfLabelColumns = 2;

    private void createGroup() {
        GridLayout gridLayoutMainOptionsGroup = new GridLayout();
        gridLayoutMainOptionsGroup.numColumns = 1;
        objMainOptionsGroup = new Group(this, SWT.NONE);
        objMainOptionsGroup.setText(objDataProvider.getJobNameAndTitle());

        objMainOptionsGroup.setLayout(gridLayoutMainOptionsGroup);
        objMainOptionsGroup.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_FILL, true, true));

        setResizableV(objMainOptionsGroup);

        gridLayout = new GridLayout();
        gridLayout.marginHeight = 1;
        gridLayout.numColumns = 6;

        gMain = new Group(objMainOptionsGroup, SWT.NONE);
        gMain.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_FILL, true, false));
        gMain.setText(Messages.getLabel("MainOptions"));
        gMain.setToolTipText(Messages.getTooltip("MainOptions"));
        gMain.setLayout(gridLayout);

        label = new Label(gMain, SWT.NONE);
        label.setLayoutData(new GridData(GridData.BEGINNING, GridData.END, false, false, intNoOfLabelColumns, 1));
        label.setText(Messages.getLabel("JobName"));
        tbxJobName = new Text(gMain, SWT.BORDER);
        tbxJobName.setToolTipText(Messages.getTooltip("jobname"));

        tbxJobName.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                // tbxJobName.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });

        tbxJobName.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.F1) {
                    MainWindow.message("F1 gedrückt", SWT.ICON_INFORMATION);
                    objDataProvider.openHelp(Messages.getF1("jobname")); // "http:www.sos-berlin.com/doc/en/scheduler.doc/xml/job.xml");
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }
        });

        tbxJobName.addVerifyListener(new VerifyListener() {
            public void verifyText(final VerifyEvent e) {
                if (!init) {
                    e.doit = Utils.checkElement(objDataProvider.getJobName(), objDataProvider.get_dom(), Editor.JOB, null);
                }
            }
        });
        tbxJobName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1));
        tbxJobName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (init) {
                    return;
                }
                checkName();
                objDataProvider.setJobName(tbxJobName.getText(), updateTree);
                objMainOptionsGroup.setText(objDataProvider.getJobNameAndTitle());
            }
        });

        lblJobTitlelabel1 = new Label(gMain, SWT.NONE);
        lblJobTitlelabel1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, intNoOfLabelColumns, 1));
        lblJobTitlelabel1.setText(Messages.getLabel("jobtitle"));
        lblJobTitlelabel1.setToolTipText(Messages.getTooltip("jobtitle"));

        // tbxJobTitle = new Combo(gMain, SWT.BORDER);
        tbxJobTitle = new Text(gMain, SWT.BORDER);
        tbxJobTitle.setToolTipText(Messages.getTooltip("jobtitle"));
        tbxJobTitle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1));
        tbxJobTitle.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if (init)
                    return;
                objDataProvider.setTitle(tbxJobTitle.getText());
            }
        });

        // tbxJobTitle.setItems(Options.getJobTitleList());

        lblProcessClass = new Label(gMain, SWT.NONE);
        lblProcessClass.setText(Messages.getLabel("ProcessClass"));
        lblProcessClass.setToolTipText(Messages.getTooltip("ProcessClass"));

        butShowProcessClass = new Button(gMain, SWT.ARROW | SWT.DOWN);
        butShowProcessClass.setVisible(objDataProvider.get_dom() != null && !objDataProvider.get_dom().isLifeElement());
        butShowProcessClass.setToolTipText(Messages.getTooltip("goto"));
        butShowProcessClass.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                String strT = cProcessClass.getText();
                if (strT.length() > 0) {
                    ContextMenu.goTo(strT, objDataProvider.get_dom(), Editor.PROCESS_CLASSES);
                }
            }
        });
        butShowProcessClass.setAlignment(SWT.RIGHT);
        butShowProcessClass.setVisible(true);

        cProcessClass = new Combo(gMain, intComboBoxStyle);
        cProcessClass.setToolTipText(Messages.getTooltip("ProcessClass"));
        cProcessClass.setMenu(new ContextMenu(cProcessClass, objDataProvider.get_dom(), Editor.PROCESS_CLASSES).getMenu());

        cProcessClass.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {

                if (init) {
                    return;
                }
                objDataProvider.setProcessClass(cProcessClass.getText());
                butShowProcessClass.setVisible(true);
            }
        });
        cProcessClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
        cProcessClass.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {

                if (init) {
                    return;
                }
                objDataProvider.setProcessClass(cProcessClass.getText());
            }
        });

        cProcessClass.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.F1) {
                    objDataProvider.openXMLAttributeDoc("job", "process_class");
                }
                if (event.keyCode == SWT.F10) {
                    objDataProvider.openXMLDoc("job");
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }
        });

        cProcessClass.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                String strT = cProcessClass.getText();
                if (strT.length() > 0) {
                    ContextMenu.goTo(strT, objDataProvider.get_dom(), Editor.PROCESS_CLASSES);
                }
            }
        });

        // -----

        butBrowse = new Button(gMain, SWT.NONE);
        butBrowse.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                String name = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_PROCESS_CLASS);
                if (name != null && name.length() > 0)
                    cProcessClass.setText(name);
            }
        });
        butBrowse.setText(Messages.getLabel("Browse.ProcessClass"));
        butBrowse.setToolTipText(Messages.getTooltip("Browse.ProcessClass"));

        createLanguageSelector(gMain);
        setResizable(objMainOptionsGroup);
        createJobDetailForm(objMainOptionsGroup);
    }

    private void setResizable(Control objControl) {
        objControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
    }

    private void setResizableV(Control objControl) {
        boolean flgGrapVerticalspace = true;
        objControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, flgGrapVerticalspace));
    }

    private void disposeTabPages() {
        if (tabItemEMail != null) {
            tabItemEMail.dispose();
        }
        if (tabItemSourceViewer != null) {
            tabItemSourceViewer.dispose();
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
        if (tabItemProcessFile != null) {
            tabItemProcessFile.dispose();
        }
        if (tabItemScript != null) {
            tabItemScript.dispose();
        }
        if (tabItemJavaAPI != null) {
            tabItemJavaAPI.dispose();
        }
        if (tabItemOptions != null) {
            tabItemOptions.dispose();
        }
        if (tabItemDirChanged != null) {
            tabItemDirChanged.dispose();
        }
        if (tabItemIncludedFiles != null) {
            tabItemIncludedFiles.dispose();
        }
        if (tabItemDirChanged != null) {
            tabItemDirChanged.dispose();
        }

    }

    @SuppressWarnings("deprecation")
    private void createJobDetailForm(Group pobjMainOptionsGroup) {

        tabFolder = new CTabFolder(pobjMainOptionsGroup, SWT.None); // SWT.Bottom
        tabFolder.setLayout(new GridLayout());
        setResizableV(tabFolder);
        tabFolder.setSimple(false);
        tabFolder.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                int intIndex = tabFolder.getSelectionIndex();
                Options.setLastTabItemIndex(intIndex);
                logger.debug("Selected item index = " + tabFolder.getSelectionIndex());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        createTabPages();
        tabFolder.setSelection(0);

    }

    private void createTabPages() {
       tabItemJavaAPIComposite = null;
       tabItemOptionsComposite = null;
       tabItemDirChangedComposite = null;
       tabItemIncludedFilesComposite = null;
       tabItemEMailComposite = null;
       tabItemSourceViewerComposite = null;
       tabItemDocumentationComposite = null;
       tabItemOrderSetBackComposite = null;
       tabItemDelayAfterErrorComposite = null;
       tabItemProcessFileComposite = null;
       tabItemScriptComposite = null;

        objTabControlComposite = new Composite(tabFolder, SWT.NONE);
        objTabControlComposite.setLayout(new GridLayout());
        setResizableV(objTabControlComposite);

        tabItemScript = new CTabItem(tabFolder, SWT.NONE);
        tabItemScript.setToolTipText("Shell scripts (Unix or MS-Windows");
        tabItemScript.setText(Messages.getLabel("script"));

        tabItemScriptComposite = new Composite(tabFolder, SWT.NONE);
        tabItemScriptComposite.setLayout(new GridLayout());
        setResizableV(tabItemScriptComposite);
        tabItemScript.setControl(tabItemScriptComposite);

        tabItemProcessFile = new CTabItem(tabFolder, SWT.NONE);
        tabItemProcessFile.setToolTipText("Shell ProcessFiles (Unix or MS-Windows");
        tabItemProcessFile.setText(Messages.getLabel("job.ProcessFile"));

        tabItemProcessFileComposite = new Composite(tabFolder, SWT.NONE);
        tabItemProcessFileComposite.setLayout(new GridLayout());
        setResizableV(tabItemProcessFileComposite);
        tabItemProcessFile.setControl(tabItemProcessFileComposite);
        if (objDataProvider.isJava()) {
            tabItemJavaAPI = new CTabItem(tabFolder, SWT.NONE);
            tabItemJavaAPI.setToolTipText("internal Java-API definition");
            tabItemJavaAPI.setText(Messages.getLabel("job.javaapi"));

            tabItemJavaAPIComposite = new Composite(tabFolder, SWT.NONE);
            tabItemJavaAPIComposite.setLayout(new GridLayout());
            setResizableV(tabItemJavaAPIComposite);
            tabItemJavaAPI.setControl(tabItemJavaAPIComposite);
        }

        tabItemIncludedFiles = new CTabItem(tabFolder, SWT.NONE);
        tabItemIncludedFiles.setText(Messages.getLabel("job.includedfiles"));
        tabItemIncludedFiles.setToolTipText("Additional Files to include for Shell-Scripts");
        tabItemIncludedFilesComposite = new Composite(tabFolder, SWT.NONE);
        tabItemIncludedFilesComposite.setLayout(new GridLayout());
        setResizableV(tabItemIncludedFilesComposite);
        tabItemIncludedFiles.setControl(tabItemIncludedFilesComposite);

        tabItemOptions = new CTabItem(tabFolder, SWT.NONE);
        tabItemOptions.setToolTipText("Job Options");
        tabItemOptions.setText(Messages.getLabel("options"));
        tabItemOptionsComposite = new Composite(tabFolder, SWT.NONE);
        tabItemOptionsComposite.setLayout(new GridLayout());
        setResizableV(tabItemOptionsComposite);
        tabItemOptions.setControl(tabItemOptionsComposite);

        tabItemEMail = new CTabItem(tabFolder, SWT.NONE);
        tabItemEMail.setToolTipText("eMail");
        tabItemEMail.setText(Messages.getLabel("eMail"));
        tabItemEMailComposite = new Composite(tabFolder, SWT.NONE);
        tabItemEMailComposite.setLayout(new GridLayout());
        setResizableV(tabItemEMailComposite);
        tabItemEMail.setControl(tabItemEMailComposite);

        if (objDataProvider.isOrderJob()) {
            tabItemOrderSetBack = new CTabItem(tabFolder, SWT.NONE);
            tabItemOrderSetBack.setToolTipText("Order SetBack");
            tabItemOrderSetBack.setText(Messages.getLabel("SetBack"));
            tabItemOrderSetBackComposite = new Composite(tabFolder, SWT.NONE);
            tabItemOrderSetBackComposite.setLayout(new GridLayout());
            setResizableV(tabItemOrderSetBackComposite);
            tabItemOrderSetBack.setControl(tabItemOrderSetBackComposite);
        } else {
            tabItemDelayAfterError = new CTabItem(tabFolder, SWT.NONE);
            tabItemDelayAfterError.setToolTipText("on Error");
            tabItemDelayAfterError.setText(Messages.getLabel("on Error"));
            tabItemDelayAfterErrorComposite = new Composite(tabFolder, SWT.NONE);
            tabItemDelayAfterErrorComposite.setLayout(new GridLayout());
            setResizableV(tabItemDelayAfterErrorComposite);
            tabItemDelayAfterError.setControl(tabItemDelayAfterErrorComposite);

            tabItemDirChanged = new CTabItem(tabFolder, SWT.NONE);
            tabItemDirChanged.setToolTipText("File Watcher");
            tabItemDirChanged.setText(Messages.getLabel("FileWatcher"));
            tabItemDirChangedComposite = new Composite(tabFolder, SWT.NONE);
            tabItemDirChangedComposite.setLayout(new GridLayout());
            setResizableV(tabItemDirChangedComposite);
            tabItemDirChanged.setControl(tabItemDirChangedComposite);
        }

        tabItemSourceViewer = new CTabItem(tabFolder, SWT.NONE);
        tabItemSourceViewer.setToolTipText("SourceViewer");
        tabItemSourceViewer.setText(Messages.getLabel("Source"));
        tabItemSourceViewerComposite = new Composite(tabFolder, SWT.NONE);
        tabItemSourceViewerComposite.setLayout(new GridLayout());
        setResizableV(tabItemSourceViewerComposite);
        tabItemSourceViewer.setControl(tabItemSourceViewerComposite);

        tabItemDocumentation = new CTabItem(tabFolder, SWT.NONE);
        tabItemDocumentation.setToolTipText("Documentation");
        tabItemDocumentation.setText(Messages.getLabel("Docu"));
        tabItemDocumentationComposite = new Composite(tabFolder, SWT.NONE);
        tabItemDocumentationComposite.setLayout(new GridLayout());
        setResizableV(tabItemDocumentationComposite);
        tabItemDocumentation.setControl(tabItemDocumentationComposite);

        createSetbackTab(tabItemOrderSetBackComposite);
        createDelayAfterErrorTab(tabItemDelayAfterErrorComposite);
        createDirChangedTab(tabItemDirChangedComposite);
        createJavaAPITab(tabItemJavaAPIComposite);

        createEmailSettingsTab(tabItemEMailComposite);
        createProcessFileTab(tabItemProcessFileComposite);
        createSourceViewerTab(tabItemSourceViewerComposite);
        createDocumentationTab(tabItemDocumentationComposite);
        createOptionsTab(tabItemOptionsComposite);
        createTabItemIncludeFile(tabItemIncludedFilesComposite);
        createScriptTab(tabItemScriptComposite);
    }

    private void createLanguageSelector(Composite pobjComposite) {
        Label labelLanguageSelector = new Label(pobjComposite, SWT.NONE);
        labelLanguageSelector.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, intNoOfLabelColumns, 1));
        labelLanguageSelector.setText(Messages.getLabel("Language.Monitor"));

        languageSelector = new LanguageSelector(pobjComposite, SWT.NONE);
        languageSelector.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                if (objDataProvider != null && init == false) {
                    String strT = languageSelector.getText();
                    objDataProvider.setLanguage(strT);
                    disposeTabPages();
                    createTabPages();
                    
                    
                    objJobScript.getCboPrefunction().setEnabled(false);
                    if (languageSelector.isJava() && objJobJAPI.getTbxClassName()  != null) {
                        objJobJAPI.getTbxClassName().setFocus();
                        objJobJAPI.getTClasspath().setText(objDataProvider.getClasspath());

                        if (!objJobJAPI.getTbxClassName().getText().equals("") && objDataProvider.getJavaClass().equals(""))
                            objDataProvider.setJavaClass(objJobJAPI.getTbxClassName().getText());
                        objJobJAPI.getTbxClassName().setText(objDataProvider.getJavaClass());
                        tabFolder.setSelection(tabItemJavaAPI);
                    } else {
                        String lan = "";
                        if (languageSelector.isScriptLanguage()) {
                            if (type == Editor.MONITOR) {
                                lan = "spooler_task_before;spooler_task_after;spooler_process_before;spooler_process_after";
                            } else {
                                lan = "spooler_init;spooler_open;spooler_process;spooler_close;spooler_exit;spooler_on_error;spooler_on_success";
                            }
                            objJobScript.getCboPrefunction().setItems(lan.split(";"));
                            objJobScript.getCboPrefunction().setEnabled(true);
                        }

                        tabFolder.setSelection(tabItemScript);
                        objJobScript.gettSource().setFocus();
                    }

                
                }
            }
        });

        GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_combo.minimumWidth = 100;
        languageSelector.setLayoutData(gd_combo);
        languageSelector.select(0);
        languageSelector.setItems(objDataProvider._languagesJob);
    }

    private void createSourceViewerTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobSourceViewer = new JobSourceViewer(pParentComposite, objDataProvider, objJobSourceViewer);
        pParentComposite.layout();
    }

    private void createScriptTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobScript = new JobScript(pParentComposite, objDataProvider, objJobScript);
        pParentComposite.layout();
    }

    private void createDocumentationTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }

        objJobJD = new JobDocumentation(pParentComposite, objDataProvider, objJobJD);
        pParentComposite.layout();
    }

    private void createJavaAPITab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobJAPI = new JobJavaAPI(pParentComposite, objDataProvider, objJobJAPI);
        pParentComposite.layout();
    }

    private void createProcessFileTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobProcessFile = new JobProcessFile(pParentComposite, objDataProvider, objJobProcessFile);
        pParentComposite.layout();
    }

    private void createOptionsTab(final Composite pParentComposite) {
        objJobOptions = new JobOptions(pParentComposite, objDataProvider, objJobOptions);
        objJobOptions.getbOrderYes().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (objJobOptions.getbOrderYes().getSelection() == false){
                    return;
                }
                disposeTabPages();
                createTabPages();
                tabFolder.setSelection(tabItemOptions);
            }

        });
        objJobOptions.getbOrderNo().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (objJobOptions.getbOrderNo().getSelection() == false){
                    return;
                }
                disposeTabPages();
                createTabPages();
                tabFolder.setSelection(tabItemOptions);
            }

        });        

        pParentComposite.layout();
    }

    private void createSetbackTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobSetback = new JobSetback(pParentComposite, objDataOptionsProvider, objJobSetback);
        pParentComposite.layout();
    }

    private void createDelayAfterErrorTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objDelayAfterError = new JobDelayAfterError(pParentComposite, objDataOptionsProvider, objDelayAfterError);
        pParentComposite.layout();
    }

    private void createEmailSettingsTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobEmailSettings = new JobEmailSettings(pParentComposite, objDataProvider, objJobEmailSettings);
        pParentComposite.layout();
    }

    private void createDirChangedTab(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobSWDC = new JobStartWhenDirectoryChanged(pParentComposite, objDataProvider, objJobSWDC);
        pParentComposite.layout();
    }

    private void createTabItemIncludeFile(final Composite pParentComposite) {
        if (pParentComposite == null) {
            return;
        }
        objJobIncludeFile = new JobIncludeFile(pParentComposite, objDataProvider, objJobIncludeFile);
        pParentComposite.layout();

    }

    public void initForm() {
        init = true;
        tbxJobName.setText(objDataProvider.getJobName());
        updateTree = true;
        tbxJobTitle.setText(objDataProvider.getTitle());
        objMainOptionsGroup.setText(objDataProvider.getJobNameAndTitle());

        String process_class = objDataProvider.getProcessClass();
        cProcessClass.setItems(objDataProvider.getProcessClasses());
        cProcessClass.setText(process_class);

        init = false;
    }

    private void checkName() {
        if (Utils.existName(tbxJobName.getText(), objDataProvider.getJob(), "job")) {
            tbxJobName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
        } else {
            tbxJobName.setBackground(null);
        }
    }

    private void fillForm() {
        init = true;
        int language = objDataProvider.getLanguage();
        if (objJobScript.getCboPrefunction() != null) {
            objJobScript.getCboPrefunction().removeAll();
        }

        tbxJobName.setText(objDataProvider.getJobName());
        if (type == Editor.MONITOR) {
            txtName.setText(objDataProvider.getJobName());
            spinner.setSelection((objDataProvider.getOrdering().length() == 0 ? 0 : Integer.parseInt(objDataProvider.getOrdering())));
            // bShell.setVisible(false);
        }

       
        languageSelector.selectLanguageItem(language);

        if (language != JobListener.NONE) {
            objDataProvider.fillTable(objJobIncludeFile.getTableIncludes());
        }

        String lan = "";
        if (!languageSelector.isShell() && !languageSelector.isJava()) {
            if (type == Editor.MONITOR) {
                lan = "spooler_task_before;spooler_task_after;spooler_process_before;spooler_process_after";
            } else {
                lan = "spooler_init;spooler_open;spooler_process;spooler_close;spooler_exit;spooler_on_error;spooler_on_success";
            }
            objJobScript.getCboPrefunction().setItems(lan.split(";"));
        }
       
        init = false;
    }
 

    @Override
    public void setToolTipText() {
        // TODO Auto-generated method stub

    }

} // @jve:decl-index=0:visual-constraint="10,10"
