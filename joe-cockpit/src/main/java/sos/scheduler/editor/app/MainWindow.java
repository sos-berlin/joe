package sos.scheduler.editor.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import sos.ftp.profiles.FTPProfileJadeClient;
import sos.scheduler.editor.classes.WindowsSaver;
import sos.scheduler.editor.conf.forms.HotFolderDialog;
import sos.scheduler.editor.conf.forms.JobChainConfigurationForm;
import sos.scheduler.editor.conf.forms.SchedulerForm;

import com.sos.JSHelper.Basics.VersionInfo;
import com.sos.VirtualFileSystem.common.SOSFileEntry;
import com.sos.event.service.forms.ActionsForm;
import com.sos.i18n.annotation.I18NMessage;
import com.sos.i18n.annotation.I18NMessages;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.forms.DocumentationForm;
import com.sos.joe.wizard.forms.JobAssistentForm;
import com.sos.joe.xml.DomParser;
import com.sos.joe.xml.jobdoc.DocumentationDom;
import com.sos.joe.xml.jobscheduler.MergeAllXMLinDirectory;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public class MainWindow {

    private static final Logger LOGGER = Logger.getLogger(MainWindow.class);
    private static final String DOT = System.getProperty("os.name").startsWith("Windows") ? "dot.exe" : "dot";
    private static final String NEWLINE_TAB = "n\t";
    private static final String PROPERTY_NAME_EDITOR_JOB_SHOW_WIZARD = "editor.job.show.wizard";
    private static final String STRING_EDITOR = "editor";
    private static final String ICON_OPEN_HOT_FOLDER_GIF = "/sos/scheduler/editor/icon_open_hot_folder.gif";
    private static final String EMPTY = "";
    private static Shell sShell = null;
    private static Menu mFile = null;
    private static ToolItem butSave = null;
    private static ToolItem butShowAsXML = null;
    private static boolean flag = true;
    private static Label StatusLine = null;
    private final String conSVNVersion = "$Id$";
    private MainListener listener = null;
    private Menu menuBar = null;
    private Menu submenu = null;
    private Menu submenu1 = null;
    private Composite groupmain = null;
    private WindowsSaver objPersistenceStore;
    public static final String ICON_OPEN_GIF = "/sos/scheduler/editor/icon_open.gif";
    public static final String ICON_EDITOR_PNG = "/sos/scheduler/editor/editor.png";
    public static final String name = System.getProperty("os.name");
    public static final boolean isWindows = name.startsWith("Windows");
    public static final String JOE_I_0010 = "JOE_I_0010";
    @I18NMessages(value = { @I18NMessage("Open"), @I18NMessage(value = "Open", locale = "en_UK", explanation = "start the open dialog box ..."),
            @I18NMessage(value = "Öffnen", locale = "de", explanation = "Den Dialog für das Öffnen einer Datei starten"),
            @I18NMessage(value = "Open", locale = "es", explanation = "Open"), @I18NMessage(value = "Open", locale = "fr", explanation = "Open"),
            @I18NMessage(value = "Open", locale = "it", explanation = "Open") }, msgnum = "MENU_OPEN", msgurl = "Menu-Open")
    public static final String MENU_OPEN = "MENU_OPEN";
    @I18NMessages(value = { @I18NMessage("Configuration"), @I18NMessage(value = "Configuration", locale = "en_UK", explanation = "Configuration"),
            @I18NMessage(value = "Konfiguration", locale = "de", explanation = "Configuration"),
            @I18NMessage(value = "Configuration", locale = "es", explanation = "Configuration"),
            @I18NMessage(value = "Configuration", locale = "fr", explanation = "Configuration"),
            @I18NMessage(value = "Configuration", locale = "it", explanation = "Configuration") }, msgnum = "MENU_Configuration",
            msgurl = "Menu-Configuration")
    public static final String MENU_Configuration = "MENU_Configuration";
    @I18NMessages(value = { @I18NMessage("New"), @I18NMessage(value = "New Object", locale = "en_UK", explanation = "New"),
            @I18NMessage(value = "Neues Objekt erstellen", locale = "de", explanation = "New"),
            @I18NMessage(value = "New", locale = "es", explanation = "New"), @I18NMessage(value = "New", locale = "fr", explanation = "New"),
            @I18NMessage(value = "New", locale = "it", explanation = "New") }, msgnum = "MENU_New", msgurl = "Menu-New")
    public static final String MENU_New = "MENU_New";
    @I18NMessages(value = { @I18NMessage("Documentation"),
            @I18NMessage(value = "Modify Documentation", locale = "en_UK", explanation = "Modify Documentation of a job template"),
            @I18NMessage(value = "Dokumentation bearbeiten", locale = "de", explanation = "Dokumentation eines Job Templates bearbeiten"),
            @I18NMessage(value = "Modify Documentation", locale = "es", explanation = "Modify Documentation of a job template"),
            @I18NMessage(value = "Modify Documentation", locale = "fr", explanation = "Modify Documentation of a job template"),
            @I18NMessage(value = "Modify Documentation", locale = "it", explanation = "Modify Documentation of a job template") },
            msgnum = "MENU_Documentation", msgurl = "Menu-Documentation")
    public static final String MENU_Documentation = "MENU_Documentation";
    @I18NMessages(value = { @I18NMessage("EventHandler"), @I18NMessage(value = "xEventHandler", locale = "en_UK", explanation = "EventHandler"),
            @I18NMessage(value = "Ereignis-Behandlung", locale = "de", explanation = "EventHandler"),
            @I18NMessage(value = "EventHandler", locale = "es", explanation = "EventHandler"),
            @I18NMessage(value = "EventHandler", locale = "fr", explanation = "EventHandler"),
            @I18NMessage(value = "EventHandler", locale = "it", explanation = "EventHandler") }, msgnum = "MENU_EventHandler",
            msgurl = "Menu-EventHandler")
    public static final String MENU_EventHandler = "MENU_EventHandler";
    @I18NMessages(value = { @I18NMessage("HotFolderObject"),
            @I18NMessage(value = "HotFolderObject", locale = "en_UK", explanation = "HotFolderObject"),
            @I18NMessage(value = "HotFolder-Objekt", locale = "de", explanation = "HotFolderObject"),
            @I18NMessage(value = "HotFolderObject", locale = "es", explanation = "HotFolderObject"),
            @I18NMessage(value = "HotFolderObject", locale = "fr", explanation = "HotFolderObject"),
            @I18NMessage(value = "HotFolderObject", locale = "it", explanation = "HotFolderObject") }, msgnum = "MENU_HotFolderObject",
            msgurl = "Menu-HotFolderObject")
    public static final String MENU_HotFolderObject = "MENU_HotFolderObject";
    @I18NMessages(value = { @I18NMessage("Job"), @I18NMessage(value = "Job", locale = "en_UK", explanation = "Job"),
            @I18NMessage(value = "Job", locale = "de", explanation = "Job"), @I18NMessage(value = "Job", locale = "es", explanation = "Job"),
            @I18NMessage(value = "Job", locale = "fr", explanation = "Job"), @I18NMessage(value = "Job", locale = "it", explanation = "Job") },
            msgnum = "MENU_Job", msgurl = "Menu-Job")
    public static final String MENU_Job = "MENU_Job";
    @I18NMessages(value = { @I18NMessage("JobChain"), @I18NMessage(value = "JobChain", locale = "en_UK", explanation = "JobChain"),
            @I18NMessage(value = "Job-Kette", locale = "de", explanation = "Job-Kette"),
            @I18NMessage(value = "JobChain", locale = "es", explanation = "JobChain"),
            @I18NMessage(value = "JobChain", locale = "fr", explanation = "JobChain"),
            @I18NMessage(value = "JobChain", locale = "it", explanation = "JobChain") }, msgnum = "MENU_JobChain", msgurl = "Menu-JobChain")
    public static final String MENU_JobChain = "MENU_JobChain";
    @I18NMessages(value = { @I18NMessage("ProcessClass"), @I18NMessage(value = "ProcessClass", locale = "en_UK", explanation = "ProcessClass"),
            @I18NMessage(value = "Prozess Klasse", locale = "de", explanation = "ProcessClass"),
            @I18NMessage(value = "ProcessClass", locale = "es", explanation = "ProcessClass"),
            @I18NMessage(value = "ProcessClass", locale = "fr", explanation = "ProcessClass"),
            @I18NMessage(value = "ProcessClass", locale = "it", explanation = "ProcessClass") }, msgnum = "MENU_ProcessClass",
            msgurl = "Menu-ProcessClass")
    public static final String MENU_ProcessClass = "MENU_ProcessClass";
    @I18NMessages(value = { @I18NMessage("Lock"), @I18NMessage(value = "Lock", locale = "en_UK", explanation = "Lock"),
            @I18NMessage(value = "Sperre", locale = "de", explanation = "Sperren verwalten bzw anlegen"),
            @I18NMessage(value = "Lock", locale = "es", explanation = "Lock"), @I18NMessage(value = "Lock", locale = "fr", explanation = "Lock"),
            @I18NMessage(value = "Lock", locale = "it", explanation = "Lock") }, msgnum = "MENU_Lock", msgurl = "Menu-Lock")
    public static final String MENU_Lock = "MENU_Lock";
    @I18NMessages(value = { @I18NMessage("File"), @I18NMessage(value = "File", locale = "en_UK", explanation = "File"),
            @I18NMessage(value = "Datei", locale = "de", explanation = "Menü der Dateioperationen"),
            @I18NMessage(value = "File", locale = "es", explanation = "File"), @I18NMessage(value = "File", locale = "fr", explanation = "File"),
            @I18NMessage(value = "File", locale = "it", explanation = "File") }, msgnum = "MENU_File", msgurl = "Menu-File")
    public static final String MENU_File = "MENU_File";
    @I18NMessages(value = { @I18NMessage("Options"), @I18NMessage(value = "Options", locale = "en_UK", explanation = "Options"),
            @I18NMessage(value = "Einstellungen", locale = "de", explanation = "Einstellungen für JOE"),
            @I18NMessage(value = "Options", locale = "es", explanation = "Options"),
            @I18NMessage(value = "Options", locale = "fr", explanation = "Options"),
            @I18NMessage(value = "Options", locale = "it", explanation = "Options") }, msgnum = "MENU_Options", msgurl = "Menu-Options")
    public static final String MENU_Options = "MENU_Options";
    @I18NMessages(value = { @I18NMessage("Help"), @I18NMessage(value = "Help", locale = "en_UK", explanation = "Help"),
            @I18NMessage(value = "Hilfe", locale = "de", explanation = "Hilfe zu JOE"),
            @I18NMessage(value = "Help", locale = "es", explanation = "Help"), @I18NMessage(value = "Help", locale = "fr", explanation = "Help"),
            @I18NMessage(value = "Help", locale = "it", explanation = "Help") }, msgnum = "MENU_Help", msgurl = "Menu-Help")
    public static final String MENU_Help = "MENU_Help";
    @I18NMessages(value = { @I18NMessage("About"), @I18NMessage(value = "About", locale = "en_UK", explanation = "About"),
            @I18NMessage(value = "Über", locale = "de", explanation = "Liefert Informationen über ..."),
            @I18NMessage(value = "About", locale = "es", explanation = "About"), @I18NMessage(value = "About", locale = "fr", explanation = "About"),
            @I18NMessage(value = "About", locale = "it", explanation = "About") }, msgnum = "MENU_About", msgurl = "Menu-About")
    public static final String MENU_About = "MENU_About";
    public static final String MENU_Reset_Dialog = "MENU_ResetDialog";
    public static final String MENU_Order = "MENU_Order";
    public static IContainer container = null;

    public MainWindow() {
        LOGGER.debug(conSVNVersion);
    }

    private void createContainer(Composite objParent) {
        Runtime rt = Runtime.getRuntime();
        String[] args = { DOT, "-T" + "", "", "-o", "" };
        try {
            rt.exec(args);
            Options.setShowDiagram(true);
        } catch (IOException e) {
            Options.setShowDiagram(false);
        }
        container = new TabbedContainer(objParent);
        sShell.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        sShell.addShellListener(new ShellListener() {

            @Override
            public void shellActivated(ShellEvent event) {
                shellActivated_();
            }

            @Override
            public void shellClosed(ShellEvent arg0) {
                saveWindowPosAndSize();
                LOGGER.debug("shellClosed");
            }

            @Override
            public void shellDeactivated(ShellEvent arg0) {
                LOGGER.debug("shellDeactivated");
            }

            @Override
            public void shellDeiconified(ShellEvent arg0) {
                LOGGER.debug("shellDeiconified");
            }

            @Override
            public void shellIconified(ShellEvent arg0) {
                //
            }
        });
    }

    private String getMenuText(final String pstrText, final String pstrAccelerator) {
        String strRet = pstrText;
        int intLen = pstrAccelerator.trim().length();
        if (intLen > 0) {
            if (intLen == 1) {
                strRet += "\tCtrl+" + pstrAccelerator;
            } else {
                strRet += "\t" + pstrAccelerator;
            }
        }
        return strRet;
    }

    public void OpenLastFolder() {
        if (Options.openLastFolder()) {
            String strF = Options.getLastFolderName();
            if (strF != null && !strF.trim().isEmpty()) {
                setStatusLine(String.format("Open last Folder '%1$s'", strF));
                container.openDirectory(strF);
            }
        }
    }

    public void setStatusLine(final String pstrText, final int pintMsgType) {
        StatusLine.setText(pstrText);
    }

    public void setStatusLine(final String pstrText) {
        StatusLine.setText(pstrText);
    }

    public void createSShell() {
        Shell shell = new Shell();
        final GridLayout gridLayout_1 = new GridLayout();
        shell.setLayout(gridLayout_1);
        shell.setMinimumSize(940, 600);
        shell.setImage(ResourceManager.getImageFromResource(ICON_EDITOR_PNG));
        Composite groupmain = new Composite(shell, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        groupmain.setLayout(gridLayout);
        groupmain.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        createSShell(shell, groupmain);
        Options.loadWindow(sShell, STRING_EDITOR);
        String strT = Messages.getLabel(JOE_I_0010) + " " + VersionInfo.JAR_VERSION;
        container.setTitleText(strT);
        sShell.setText(strT);
        LOGGER.debug(strT);
        Options.conJOEGreeting = strT;
        sShell.setData(sShell.getText());
    }

    public void createSShell(Composite containerParent, Composite objParent) {
        sShell = objParent.getShell();
        groupmain = objParent;
        groupmain.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        createToolBar();
        createContainer(containerParent);
        StatusLine = new Label(groupmain, SWT.BOTTOM);
        GridData gridStatuslineLayout = new GridData();
        gridStatuslineLayout.horizontalAlignment = GridData.FILL;
        gridStatuslineLayout.grabExcessHorizontalSpace = true;
        StatusLine.setLayoutData(gridStatuslineLayout);
        listener = new MainListener(this, container);
        objPersistenceStore = new WindowsSaver(this.getClass(), sShell, 940, 600);
        objPersistenceStore.restoreWindowLocation();
        listener.loadOptions();
        listener.loadJobTitels();
        listener.loadHolidaysTitel();
        menuBar = new Menu(sShell, SWT.BAR);
        MenuItem submenuItem2 = new MenuItem(menuBar, SWT.CASCADE);
        submenuItem2.setText("&" + getMenuText(Messages.getLabel(MENU_File), EMPTY));
        mFile = new Menu(submenuItem2);
        MenuItem open = new MenuItem(mFile, SWT.PUSH);
        open.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (container.openQuick() != null) {
                    setSaveStatus();
                }
            }
        });
        open.setText(getMenuText(Messages.getLabel(MENU_OPEN), "O"));
        open.setAccelerator(SWT.CTRL | 'O');
        MenuItem mNew = new MenuItem(mFile, SWT.CASCADE);
        mNew.setText(getMenuText(Messages.getLabel(MENU_New), EMPTY));
        Menu pmNew = new Menu(mNew);
        MenuItem pNew = new MenuItem(pmNew, SWT.PUSH);
        pNew.setText(getMenuText(Messages.getLabel(MENU_Configuration), "I"));
        pNew.setAccelerator(SWT.CTRL | 'I');
        pNew.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.newScheduler() != null) {
                    setSaveStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        mNew.setMenu(pmNew);
        MenuItem push1 = new MenuItem(pmNew, SWT.PUSH);
        push1.setText(getMenuText(Messages.getLabel(MENU_Documentation), "P"));
        push1.setAccelerator(SWT.CTRL | 'P');
        push1.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.newDocumentation() != null) {
                    setSaveStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem pNewActions = new MenuItem(pmNew, SWT.PUSH);
        pNewActions.setText(getMenuText(Messages.getLabel(MENU_EventHandler), "H"));
        pNewActions.setAccelerator(SWT.CTRL | 'H');
        pNewActions.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.newActions() != null) {
                    setSaveStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        new MenuItem(mFile, SWT.SEPARATOR);
        MenuItem openDir = new MenuItem(mFile, SWT.PUSH);
        openDir.setText(getMenuText(Messages.getLabel("MENU_OpenHotFolder"), "D"));
        openDir.setAccelerator(SWT.CTRL | 'D');
        openDir.setEnabled(true);
        openDir.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.openDirectory(null) != null) {
                    setSaveStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem mORC = new MenuItem(mFile, SWT.CASCADE);
        mORC.setText(getMenuText(Messages.getLabel("MENU_OpenRemoteConfiguration"), EMPTY));
        Menu pMOpenGlobalScheduler = new Menu(mORC);
        MenuItem pOpenGlobalScheduler = new MenuItem(pMOpenGlobalScheduler, SWT.PUSH);
        pOpenGlobalScheduler.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Utils.startCursor(getSShell());
                String globalSchedulerPath =
                        Options.getSchedulerData().endsWith("/") || Options.getSchedulerData().endsWith("\\") ? Options.getSchedulerData()
                                : Options.getSchedulerData() + "/";
                globalSchedulerPath = globalSchedulerPath + "config/remote/_all";
                File f = new java.io.File(globalSchedulerPath);
                if (!f.exists() && !f.mkdirs()) {
                    MainWindow.message("could not create Global Scheduler Configurations: " + globalSchedulerPath, SWT.ICON_WARNING);
                    Utils.stopCursor(getSShell());
                    return;
                }
                if (container.openDirectory(globalSchedulerPath) != null) {
                    setSaveStatus();
                }
                Utils.stopCursor(getSShell());
            }
        });
        pOpenGlobalScheduler.setText(Messages.getLabel("MENU_OpenGlobalScheduler"));
        MenuItem pOpenSchedulerCluster = new MenuItem(pMOpenGlobalScheduler, SWT.PUSH);
        pOpenSchedulerCluster.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                HotFolderDialog dialog = new HotFolderDialog();
                dialog.showForm(HotFolderDialog.SCHEDULER_CLUSTER);
            }
        });
        pOpenSchedulerCluster.setText(Messages.getLabel("MENU_OpenClusterConfiguration"));
        MenuItem pOpenSchedulerHost = new MenuItem(pMOpenGlobalScheduler, SWT.PUSH);
        pOpenSchedulerHost.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                HotFolderDialog dialog = new HotFolderDialog();
                dialog.showForm(HotFolderDialog.SCHEDULER_HOST);
            }
        });
        pOpenSchedulerHost.setText(Messages.getLabel("MENU_OpenRemoteSchedulerConfiguration"));
        mORC.setMenu(pMOpenGlobalScheduler);
        new MenuItem(mFile, SWT.SEPARATOR);
        MenuItem pSaveFile = new MenuItem(mFile, SWT.PUSH);
        pSaveFile.setText("Save                                    \tCtrl+S");
        pSaveFile.setAccelerator(SWT.CTRL | 'S');
        pSaveFile.setEnabled(false);
        pSaveFile.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                save();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
                //
            }
        });
        MenuItem pSaveAs = new MenuItem(mFile, SWT.PUSH);
        pSaveAs.setText("Save As                            ");
        pSaveAs.setEnabled(false);
        pSaveAs.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.getCurrentEditor() != null && container.getCurrentEditor().applyChanges()) {
                    if (container.getCurrentTab().getData("ftp_title") != null) {
                        container.getCurrentTab().setData("ftp_title", null);
                        container.getCurrentTab().setData("ftp_profile_name", null);
                        container.getCurrentTab().setData("ftp_remote_directory", null);
                        container.getCurrentTab().setData("ftp_hot_folder_elements", null);
                        container.getCurrentTab().setData("ftp_profile", null);
                    }
                    container.getCurrentEditor().saveAs();
                    setSaveStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem pSaveAsHotFolderElement = new MenuItem(mFile, SWT.PUSH);
        pSaveAsHotFolderElement.setText("Save As Hot Folder Elements   \tCtrl+B");
        pSaveAsHotFolderElement.setAccelerator(SWT.CTRL | 'B');
        pSaveAsHotFolderElement.setEnabled(false);
        pSaveAsHotFolderElement.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.getCurrentEditor() != null && container.getCurrentEditor().applyChanges()) {
                    SchedulerForm form = (SchedulerForm) container.getCurrentEditor();
                    SchedulerDom currdom = form.getDom();
                    saveDirectory(currdom, true, SchedulerDom.DIRECTORY, null, container);
                    setSaveStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        new MenuItem(mFile, SWT.SEPARATOR);
        MenuItem mFTP = new MenuItem(mFile, SWT.CASCADE);
        mFTP.setText("FTP");
        Menu pmFTP = new Menu(mNew);
        MenuItem pOpenHotFolderFTP = new MenuItem(pmFTP, SWT.PUSH);
        pOpenHotFolderFTP.setText("Open Hot Folder by FTP");
        pOpenHotFolderFTP.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                FTPDialog ftp = new FTPDialogHotFolder();
                ftp.showForm();
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem pOpenFTP = new MenuItem(pmFTP, SWT.PUSH);
        pOpenFTP.setText("Open By FTP");
        pOpenFTP.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                FTPDialog ftp = new FTPDialogOpenFile();
                ftp.showForm();
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        new MenuItem(pmFTP, SWT.SEPARATOR);
        MenuItem pSaveFTP = new MenuItem(pmFTP, SWT.PUSH);
        pSaveFTP.setText("Save By FTP");
        pSaveFTP.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                saveByFTP();
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        mFTP.setMenu(pmFTP);
        new MenuItem(mFile, SWT.SEPARATOR);
        boolean existwebDavLib = existLibraries();
        MenuItem mWebDav = new MenuItem(mFile, SWT.CASCADE);
        mWebDav.setText("WebDav");
        mWebDav.setAccelerator(SWT.CTRL | 'N');
        mWebDav.setEnabled(existwebDavLib);
        Menu pmWebDav = new Menu(mNew);
        MenuItem pOpenWebDav = new MenuItem(pmWebDav, SWT.PUSH);
        pOpenWebDav.setText("Open by WebDav");
        pOpenWebDav.setEnabled(existwebDavLib);
        pOpenWebDav.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                try {
                    if (existLibraries()) {
                        WebDavDialog webdav = new WebDavDialog();
                        webdav.showForm(WebDavDialog.OPEN);
                    }
                } catch (Exception ex) {
                    new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not open file on Webdav Server", ex);
                    MainWindow.message("could not open file on Webdav Server, cause: " + ex.getMessage(), SWT.ICON_WARNING);
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem pOpenHotFolderWebDav = new MenuItem(pmWebDav, SWT.PUSH);
        pOpenHotFolderWebDav.setText("Open HotFolder by WebDav");
        pOpenHotFolderWebDav.setEnabled(existwebDavLib);
        pOpenHotFolderWebDav.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (existLibraries()) {
                    WebDavDialog webdav = new WebDavDialog();
                    webdav.showForm(WebDavDialog.OPEN_HOT_FOLDER);
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        new MenuItem(pmWebDav, SWT.SEPARATOR);
        MenuItem pSaveWebDav = new MenuItem(pmWebDav, SWT.PUSH);
        pSaveWebDav.setText("Save by WebDav");
        pSaveWebDav.setEnabled(existwebDavLib);
        pSaveWebDav.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (existLibraries()) {
                    WebDavDialog webdav = new WebDavDialog();
                    DomParser currdom = getSpecifiedDom();
                    if (currdom == null) {
                        return;
                    }
                    if (currdom instanceof SchedulerDom && ((SchedulerDom) currdom).isDirectory()) {
                        webdav.showForm(WebDavDialog.SAVE_AS_HOT_FOLDER);
                    } else {
                        webdav.showForm(WebDavDialog.SAVE_AS);
                    }
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        mWebDav.setMenu(pmWebDav);
        new MenuItem(mFile, SWT.SEPARATOR);
        submenuItem2.setMenu(mFile);
        MenuItem pExit = new MenuItem(mFile, SWT.PUSH);
        pExit.setText(getMenuText(Messages.getLabel("MENU_Exit"), "E"));
        pExit.setAccelerator(SWT.CTRL | 'E');
        pExit.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                try {
                    saveWindowPosAndSize();
                    sShell.close();
                } catch (Exception es) {
                    new ErrorLog("error: " + sos.util.SOSClassUtil.getMethodName(), es);
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem submenuItem = new MenuItem(menuBar, SWT.CASCADE);
        submenuItem.setText(getMenuText(Messages.getLabel(MENU_Options), EMPTY));
        MenuItem submenuItem3 = new MenuItem(menuBar, SWT.CASCADE);
        submenuItem3.setText("&" + getMenuText(Messages.getLabel(MENU_Help), EMPTY));
        submenu1 = new Menu(submenuItem3);
        MenuItem pHelS = new MenuItem(submenu1, SWT.PUSH);
        pHelS.setText("JOE " + getMenuText(Messages.getLabel(MENU_Help), EMPTY));
        pHelS.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                listener.openHelp(Options.getHelpURL("index"));
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem pHelp = new MenuItem(submenu1, SWT.PUSH);
        pHelp.setText(getMenuText(Messages.getLabel(MENU_Help), "F1"));
        pHelp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.getCurrentEditor() != null) {
                    listener.openHelp(container.getCurrentEditor().getHelpKey());
                } else {
                    String msg = Messages.getString("help.info");
                    MainWindow.message(msg, SWT.ICON_INFORMATION);
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem pAbout = new MenuItem(submenu1, SWT.PUSH);
        pAbout.setText(getMenuText(Messages.getLabel(MENU_About), EMPTY) + " JOE");
        pAbout.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.showAbout();
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        submenuItem3.setMenu(submenu1);
        submenu = new Menu(submenuItem);
        submenuItem.setMenu(submenu);
        MenuItem submenuItemInfo = new MenuItem(submenu, SWT.PUSH);
        submenuItemInfo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                listener.resetInfoDialog();
                Options.setPropertyBoolean(PROPERTY_NAME_EDITOR_JOB_SHOW_WIZARD, true);
                Options.saveProperties();
            }
        });
        submenuItemInfo.setText(getMenuText(Messages.getLabel(MENU_Reset_Dialog), EMPTY));
        sShell.setMenuBar(menuBar);
        sShell.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(ShellEvent e) {
                e.doit = container.closeAll();
                setSaveStatus();
                Options.saveWindow(sShell, STRING_EDITOR);
                saveWindowPosAndSize();
                listener.saveOptions();
                ResourceManager.dispose();
            }

            @Override
            public void shellActivated(org.eclipse.swt.events.ShellEvent e) {
                setSaveStatus();
            }
        });
        objPersistenceStore.restoreWindowSize();
    }

    private void saveWindowPosAndSize() {
        objPersistenceStore.saveWindow();
    }

    public static Shell getSShell() {
        return sShell;
    }

    public static void setSaveStatus() {
        setMenuStatus();
        container.setStatusInTitle();
    }

    public static boolean setMenuStatus() {
        boolean saved = true;
        if (container.getCurrentEditor() != null) {
            saved = !container.getCurrentEditor().hasChanges();
            butShowAsXML.setEnabled(true);
            butSave.setEnabled(container.getCurrentEditor().hasChanges());
        } else {
            butShowAsXML.setEnabled(false);
            butSave.setEnabled(false);
        }
        MenuItem[] items = mFile.getItems();
        int index = 0;
        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];
            if (item.getText().startsWith("Save")) {
                index = i;
                break;
            }
        }
        items[index].setEnabled(container.getCurrentEditor() != null);
        items[index + 1].setEnabled(container.getCurrentEditor() != null);
        if (container.getCurrentEditor() instanceof sos.scheduler.editor.conf.forms.SchedulerForm) {
            sos.scheduler.editor.conf.forms.SchedulerForm form = (sos.scheduler.editor.conf.forms.SchedulerForm) container.getCurrentEditor();
            SchedulerDom dom = form.getDom();
            if (dom.isDirectory()) {
                items[index + 1].setEnabled(false);
            }
            if (!dom.isLifeElement() && !dom.isDirectory()) {
                items[index + 2].setEnabled(true);
                butSave.setEnabled(true);
            } else {
                items[index + 2].setEnabled(false);
            }
        } else {
            items[index + 2].setEnabled(false);
        }
        return saved;
    }

    public static int ErrMsg(final String pstrMessage) {
        return message(pstrMessage, SWT.ICON_ERROR);
    }

    public static int message(String message, int style) {
        return message(getSShell(), message, style);
    }

    public static int message(String application, String message, int style) {
        return message(getSShell(), application, message, style);
    }

    public static int message(Shell shell, String pstrMessage, int style) {
        MessageBox mb = new MessageBox(shell, style);
        if (pstrMessage == null) {
            pstrMessage = "??????";
        }
        mb.setMessage(pstrMessage);
        String title = Messages.getLabel("message");
        if ((style & SWT.ICON_ERROR) != 0) {
            title = Messages.getLabel("error");
        } else {
            if ((style & SWT.ICON_INFORMATION) != 0) {
                title = Messages.getLabel("information");
            } else if ((style & SWT.ICON_QUESTION) != 0) {
                title = Messages.getLabel("question");
            } else if ((style & SWT.ICON_WARNING) != 0) {
                title = Messages.getLabel("warning");
            }
        }
        mb.setText("JOE: " + title);
        return mb.open();
    }

    public static int message(Shell shell, String application, String pstrMessage, int style) {
        MessageBox mb = new MessageBox(shell, style);
        if (pstrMessage == null) {
            pstrMessage = "??????";
        }
        mb.setMessage(pstrMessage);
        String title = Messages.getLabel("message");
        if ((style & SWT.ICON_ERROR) != 0) {
            title = Messages.getLabel("error");
        } else {
            if ((style & SWT.ICON_INFORMATION) != 0) {
                title = Messages.getLabel("information");
            } else if ((style & SWT.ICON_QUESTION) != 0) {
                title = Messages.getLabel("question");
            } else if ((style & SWT.ICON_WARNING) != 0) {
                title = Messages.getLabel("warning");
            }
        }
        mb.setText(application + ": " + title);
        return mb.open();
    }

    public static IContainer getContainer() {
        return container;
    }

    private void save() {
        Utils.startCursor(getSShell());
        HashMap changes = new HashMap();
        if (container.getCurrentEditor() instanceof sos.scheduler.editor.conf.forms.SchedulerForm) {
            sos.scheduler.editor.conf.forms.SchedulerForm form = (sos.scheduler.editor.conf.forms.SchedulerForm) container.getCurrentEditor();
            SchedulerDom currdom = form.getDom();
            long l = currdom.getLastModifiedFile();
            currdom.setLastModifiedFile(0);
            changes = (java.util.HashMap) currdom.getChangedJob().clone();
        }
        if (container.getCurrentEditor().applyChanges()) {
            saveJobChainNodeParameter();
            container.getCurrentEditor().save();
            SOSFileEntry sosFileEntry = (SOSFileEntry) MainWindow.getContainer().getCurrentTab().getData("sosFileEntry");
            if (sosFileEntry != null) {
                String oldFilename = sosFileEntry.getFilename();
                if (!sosFileEntry.isDirectory()) {
                    sosFileEntry.setFilename(new File(container.getCurrentEditor().getFilename()).getName());
                }
                saveFTP(oldFilename, sosFileEntry);
            }
            saveWebDav(changes);
            setSaveStatus();
        }
        Utils.stopCursor(getSShell());
    }

    private void createToolBar() {
        final ToolBar toolBar = new ToolBar(groupmain, SWT.NONE);
        toolBar.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, false));
        final ToolItem butNew = new ToolItem(toolBar, SWT.NONE);
        butNew.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_new.gif"));
        final Menu menu = new Menu(toolBar);
        butNew.setToolTipText("New Configuration");
        MenuItem itemConfig = new MenuItem(menu, SWT.PUSH);
        itemConfig.setText("Configuration");
        itemConfig.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.newScheduler() != null) {
                    setSaveStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem itemDoc = new MenuItem(menu, SWT.PUSH);
        itemDoc.setText("JobDoc - Documentation");
        itemDoc.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.newDocumentation() != null) {
                    setSaveStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem itemActions = new MenuItem(menu, SWT.PUSH);
        itemActions.setText("Event Handler");
        itemActions.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (container.newActions() != null) {
                    setSaveStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        addDropDown(butNew, menu);
        final ToolItem butOpen = new ToolItem(toolBar, SWT.PUSH);
        butOpen.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (container.openQuick() != null) {
                    setSaveStatus();
                }
            }
        });
        butOpen.setImage(ResourceManager.getImageFromResource(ICON_OPEN_GIF));
        butOpen.setToolTipText("Open JobScheduler Configuration");
        final ToolItem butOpenHotFolder = new ToolItem(toolBar, SWT.PUSH);
        butOpenHotFolder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (container.openDirectory(null) != null) {
                    setSaveStatus();
                }
            }
        });
        butOpenHotFolder.setImage(ResourceManager.getImageFromResource(ICON_OPEN_HOT_FOLDER_GIF));
        butOpenHotFolder.setToolTipText("Open HotFolder");
        butSave = new ToolItem(toolBar, SWT.PUSH);
        butSave.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                save();
            }
        });
        butSave.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/save.gif"));
        butSave.setToolTipText("Save Configuration");
        butShowAsXML = new ToolItem(toolBar, SWT.PUSH);
        butShowAsXML.setEnabled(container != null && container.getCurrentEditor() instanceof SchedulerForm);
        butShowAsXML.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                try {
                    if (container.getCurrentEditor() == null) {
                        return;
                    }
                    DomParser currDomParser = getSpecifiedDom();
                    Utils.showClipboard(Utils.getElementAsString(currDomParser.getRoot()), getSShell(), false, null, false, null, false);
                } catch (Exception ex) {
                    new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " cause: " + ex.toString(), ex);
                }
            }
        });
        butShowAsXML.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_view_as_xml.gif"));
        butShowAsXML.setToolTipText("Show Configuration as XML");
        final ToolItem butFTP = new ToolItem(toolBar, SWT.NONE);
        final Menu menuFTP = new Menu(toolBar);
        addDropDown(butFTP, menuFTP);
        butFTP.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_open_ftp.gif"));
        butFTP.setToolTipText("FTP");
        MenuItem itemFTPOpenHotFolder = new MenuItem(menuFTP, SWT.PUSH);
        itemFTPOpenHotFolder.setText("Open Hot Folder by FTP");
        itemFTPOpenHotFolder.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                FTPDialog ftp = new FTPDialogHotFolder();
                ftp.showForm();
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem itemFTPOpen = new MenuItem(menuFTP, SWT.PUSH);
        itemFTPOpen.setText("Open by FTP");
        itemFTPOpen.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                FTPDialog ftp = new FTPDialogOpenFile();
                ftp.showForm();
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        MenuItem itemFTPSave = new MenuItem(menuFTP, SWT.PUSH);
        itemFTPSave.setText("Save As By FTP");
        itemFTPSave.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                saveByFTP();
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        final ToolItem itemReset = new ToolItem(toolBar, SWT.PUSH);
        itemReset.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_reset.gif"));
        itemReset.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                int c =
                        MainWindow.message("Do you want to reload the configuration and discard the changes?", SWT.ICON_INFORMATION | SWT.YES
                                | SWT.NO);
                if (c != SWT.YES) {
                    return;
                }
                if (container.getCurrentEditor() instanceof SchedulerForm) {
                    SchedulerForm form = (SchedulerForm) container.getCurrentEditor();
                    SchedulerDom currdom = form.getDom();
                    if (currdom.isLifeElement()) {
                        Utils.reset(currdom.getRoot(), form, currdom);
                    } else {
                        Utils.reset(currdom.getRoot().getChild("config"), form, currdom);
                    }
                } else if (container.getCurrentEditor() instanceof ActionsForm) {
                    ActionsForm form = (ActionsForm) container.getCurrentEditor();
                    com.sos.joe.xml.Events.ActionsDom currdom = form.getDom();
                    Utils.reset(currdom.getRoot(), form, currdom);
                } else if (container.getCurrentEditor() instanceof DocumentationForm) {
                    DocumentationForm form = (DocumentationForm) container.getCurrentEditor();
                    DocumentationDom currdom = form.getDom();
                    Utils.reset(currdom.getRoot(), form, currdom);
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        final ToolItem butWizzard = new ToolItem(toolBar, SWT.PUSH);
        butWizzard.setToolTipText("Wizard");
        butWizzard.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_wizzard.gif"));
        butWizzard.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                startWizard();
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
        final ToolItem butHelp = new ToolItem(toolBar, SWT.PUSH);
        butHelp.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_help.gif"));
        butHelp.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (container.getCurrentEditor() != null) {
                    listener.openHelp(container.getCurrentEditor().getHelpKey());
                } else {
                    String msg = Messages.getString("help.info");
                    MainWindow.message(msg, SWT.ICON_INFORMATION);
                }
            }

            @Override
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                //
            }
        });
    }

    private static void addDropDown(final ToolItem item, final Menu menu) {
        item.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                Rectangle rect = item.getBounds();
                Point pt = new Point(rect.x, rect.y + rect.height);
                pt = item.getParent().toDisplay(pt);
                menu.setLocation(pt.x, pt.y);
                menu.setVisible(true);
            }
        });
    }

    private void saveJobChainNodeParameter() {
        try {
            if (container.getCurrentTab().getData("details_parameter") != null) {
                HashMap h = new HashMap();
                h = (HashMap) container.getCurrentTab().getData("details_parameter");
                Iterator it = h.keySet().iterator();
                while (it.hasNext()) {
                    Element jobChain = (Element) it.next();
                    String configFilename = h.get(jobChain).toString();
                    File configFile = new File(configFilename);
                    if (configFile.exists()) {
                        String newConfigFilename = configFile.getParent();
                        newConfigFilename = newConfigFilename != null ? newConfigFilename : EMPTY;
                        newConfigFilename = new File(newConfigFilename, Utils.getAttributeValue("name", jobChain) + ".config.xml").getCanonicalPath();
                        File newConfigFile = new File(newConfigFilename);
                        DomParser currdom = getSpecifiedDom();
                        String oldname = configFile.getName().replaceAll(".config.xml", EMPTY);
                        String newName = newConfigFile.getName().replaceAll(".config.xml", EMPTY);
                        sos.scheduler.editor.conf.listeners.DetailsListener.changeDetailsJobChainname(newName, oldname, (SchedulerDom) currdom);
                        if (!newConfigFile.exists() && !configFile.renameTo(newConfigFile)) {
                            MainWindow.message("could not rename job chain node configuration file [" + configFilename + "] in [" + newConfigFilename
                                    + "].\n" + "Please try later by Hand.", SWT.ICON_WARNING);
                        }
                    }
                }
                container.getCurrentTab().setData("details_parameter", new HashMap());
            }
        } catch (Exception e) {
            //
        }
    }

    public static void saveFTP(String oldFilename, SOSFileEntry sosFileEntry) {
        try {
            DomParser currdom = getSpecifiedDom();
            if (container.getCurrentTab().getData("ftp_title") != null && !container.getCurrentTab().getData("ftp_title").toString().isEmpty()) {
                if (currdom == null) {
                    return;
                }
                String remoteDir = sosFileEntry.getParentPath();
                String aktFilename = sosFileEntry.getFilename();
                sos.ftp.profiles.FTPProfile profile = (sos.ftp.profiles.FTPProfile) container.getCurrentTab().getData("ftp_profile");
                Text txtLog = new Text(getSShell(), SWT.NONE);
                profile.setLogText(txtLog);
                FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(profile);
                if (((SchedulerDom) currdom).isLifeElement()) {
                    ftpProfileJadeClient.copyLocalFileToRemote(profile.getLocaldirectory(), remoteDir, aktFilename);
                    if (aktFilename.length() > 0 && !aktFilename.equalsIgnoreCase(oldFilename)) {
                        ftpProfileJadeClient.removeFile(remoteDir, oldFilename);
                    }
                } else {
                    ftpProfileJadeClient.copyLocalFilesToRemote(profile.getLocaldirectory() + "/" + aktFilename, remoteDir, aktFilename);
                }
            }
        } catch (Exception e) {
            MainWindow.message("could not save per ftp, cause: " + e.toString(), SWT.ICON_WARNING);
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        }
    }

    private void saveWebDav(java.util.HashMap changes) {
        WebDavDialogListener webdavListener = null;
        Text txtLog = null;
        if (container.getCurrentTab().getData("webdav_title") != null && !container.getCurrentTab().getData("webdav_title").toString().isEmpty()) {
            DomParser currdom = getSpecifiedDom();
            if (currdom == null) {
                return;
            }
            String profilename = container.getCurrentTab().getData("webdav_profile_name").toString();
            String remoteDir = container.getCurrentTab().getData("webdav_remote_directory").toString();
            ArrayList webdavHotFolderElements = new ArrayList();
            if (container.getCurrentTab().getData("webdav_hot_folder_elements") != null) {
                webdavHotFolderElements = (ArrayList) container.getCurrentTab().getData("webdav_hot_folder_elements");
            }
            java.util.Properties profile = (java.util.Properties) container.getCurrentTab().getData("webdav_profile");
            txtLog = new Text(getSShell(), SWT.NONE);
            txtLog.setVisible(false);
            final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
            gridData.widthHint = 0;
            gridData.heightHint = 0;
            txtLog.setLayoutData(gridData);
            txtLog.setSize(0, 0);
            webdavListener = new WebDavDialogListener(profile, profilename);
            webdavListener.setLogText(txtLog);
            if (currdom instanceof SchedulerDom && ((SchedulerDom) currdom).isLifeElement()) {
                String filename = container.getCurrentEditor().getFilename();
                if (!new File(remoteDir).getName().equalsIgnoreCase(new File(filename).getName())) {
                    webdavListener.removeFile(remoteDir);
                }
                remoteDir = remoteDir.substring(0, remoteDir.lastIndexOf("/")) + "/" + new File(filename).getName();
                webdavListener.saveAs(filename, remoteDir);
            } else if (currdom instanceof SchedulerDom && ((SchedulerDom) currdom).isDirectory()) {
                webdavListener.saveHotFolderAs(container.getCurrentEditor().getFilename(), remoteDir, webdavHotFolderElements, changes);
            } else {
                webdavListener.saveAs(container.getCurrentEditor().getFilename(), remoteDir);
            }
            if (webdavListener.hasError()) {
                String text = Utils.showClipboard(txtLog.getText(), getSShell(), false, EMPTY);
                if (text != null) {
                    txtLog.setText(text);
                }
            }
        }
    }

    private void saveByFTP() {
        FTPDialog ftpSaveAsDialog = new FTPDialogSaveAs();
        DomParser currdom = getSpecifiedDom();
        if (currdom == null) {
            return;
        }
        if (currdom instanceof SchedulerDom && ((SchedulerDom) currdom).isDirectory()) {
            ftpSaveAsDialog.showForm();
        } else {
            ftpSaveAsDialog.showForm();
        }
    }

    private boolean existLibraries() {
        boolean libExist = false;
        try {
            try {
                Class.forName("org.apache.commons.logging.LogFactory");
            } catch (Exception e) {
                throw e;
            }
            try {
                Class.forName("org.apache.commons.httpclient.HttpState");
            } catch (Exception e) {
                throw e;
            }
            try {
                Class.forName("org.apache.commons.codec.DecoderException");
            } catch (Exception e) {
                throw e;
            }
            try {
                Class.forName("org.apache.webdav.lib.WebdavResource");
            } catch (Exception e) {
                throw e;
            }
            libExist = true;
        } catch (Exception e) {

        }
        return libExist;
    }

    public static DomParser getSpecifiedDom() {
        DomParser currdom = null;
        if (MainWindow.getContainer().getCurrentEditor() instanceof SchedulerForm) {
            SchedulerForm form = (SchedulerForm) MainWindow.getContainer().getCurrentEditor();
            currdom = form.getDom();
        } else if (MainWindow.getContainer().getCurrentEditor() instanceof DocumentationForm) {
            DocumentationForm form = (DocumentationForm) MainWindow.getContainer().getCurrentEditor();
            currdom = form.getDom();
        } else if (MainWindow.getContainer().getCurrentEditor() instanceof JobChainConfigurationForm) {
            JobChainConfigurationForm form = (JobChainConfigurationForm) MainWindow.getContainer().getCurrentEditor();
            currdom = form.getDom();
        } else if (MainWindow.getContainer().getCurrentEditor() instanceof ActionsForm) {
            ActionsForm form = (ActionsForm) MainWindow.getContainer().getCurrentEditor();
            currdom = form.getDom();
        } else {
            MainWindow.message("Could not save FTP File. <unspecified type>  ", SWT.ICON_WARNING);
        }
        return currdom;
    }

    private void startWizard() {
        try {
            Utils.startCursor(sShell);
            SchedulerForm _scheduler = container.newScheduler(SchedulerDom.LIVE_JOB);
            if (_scheduler != null) {
                setSaveStatus();
            }
            JobAssistentForm assitant = new JobAssistentForm(_scheduler.getDom(), _scheduler);
            assitant.startJobAssistant();
            setSaveStatus();
        } catch (Exception ex) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; could not start wizard.", ex);
        } finally {
            Utils.stopCursor(sShell);
        }
    }

    public static void shellActivated_() {
        try {
            if (!(MainWindow.container.getCurrentEditor() instanceof SchedulerForm) || MainWindow.getContainer().getCurrentEditor() == null || !flag) {
                return;
            }
            DomParser dom = getSpecifiedDom();
            if (dom.getFilename() != null) {
                File f = new File(dom.getFilename());
                ArrayList<File> changeFiles = new ArrayList<File>();
                ArrayList<File> newFFiles = new ArrayList<File>();
                ArrayList<File> delFFiles = new ArrayList<File>();
                HashMap<String, Long> hFFiles = new HashMap<String, Long>();
                long lastmod = 0;
                if (dom.getFilename() != null) {
                    hFFiles = ((SchedulerDom) dom).getHotFolderFiles();
                    if (f.isDirectory()) {
                        ArrayList<File> listOfhotFolderFiles = ((SchedulerDom) dom).getHoltFolderFiles(new File(dom.getFilename()));
                        for (int i = 0; i < listOfhotFolderFiles.size(); i++) {
                            File fFile = listOfhotFolderFiles.get(i);
                            try {
                                long current = fFile.lastModified();
                                if (hFFiles.containsKey(fFile.getName())) {
                                    long domc = Long.parseLong(hFFiles.get(fFile.getName()).toString());
                                    if (current != domc) {
                                        changeFiles.add(fFile);
                                    }
                                } else {
                                    String fName = fFile.getName();
                                    int pos1 = fName.indexOf(".");
                                    int pos2 = fName.lastIndexOf(".");
                                    String n = fName.substring(pos1, pos2) + "_" + fName.substring(0, pos1);
                                    if (!(((SchedulerDom) dom).getChangedJob().get(n) != null && ((SchedulerDom) dom).getChangedJob().get(n).equals(
                                            SchedulerDom.DELETE))) {
                                        newFFiles.add(fFile);
                                    }
                                }
                            } catch (Exception e) {
                                LOGGER.error(e.getMessage(), e);
                            }
                            lastmod = lastmod + fFile.lastModified();
                        }
                        Iterator<String> it = hFFiles.keySet().iterator();
                        while (it.hasNext()) {
                            String fName = it.next();
                            if (!new File(dom.getFilename(), fName).exists()) {
                                delFFiles.add(new File(dom.getFilename(), fName));
                            }
                        }
                    } else {
                        lastmod = f.lastModified();
                    }
                }
                if (dom.getFilename() != null && dom.getLastModifiedFile() != 0 && lastmod != dom.getLastModifiedFile()) {
                    flag = false;
                    String msg = EMPTY;
                    if (f.isDirectory()) {
                        msg = Messages.getString("directory.modified", dom.getFilename());
                        if (!newFFiles.isEmpty()) {
                            msg = msg + "\n" + Messages.getString("files.new");
                            for (int i = 0; i < newFFiles.size(); i++) {
                                if (i == 0) {
                                    msg = msg + NEWLINE_TAB + newFFiles.get(i).getName();
                                } else {
                                    msg = msg + NEWLINE_TAB + newFFiles.get(i).getName();
                                }
                            }
                        }
                        if (!changeFiles.isEmpty()) {
                            msg = msg + "\n" + Messages.getString("files.changed");
                            for (int i = 0; i < changeFiles.size(); i++) {
                                if (i == 0) {
                                    msg = msg + NEWLINE_TAB + changeFiles.get(i);
                                } else {
                                    msg = msg + NEWLINE_TAB + changeFiles.get(i);
                                }
                            }
                        }
                        if (!delFFiles.isEmpty()) {
                            msg = msg + "\n" + Messages.getString("files.removed");
                            for (int i = 0; i < delFFiles.size(); i++) {
                                if (i == 0) {
                                    msg = msg + NEWLINE_TAB + delFFiles.get(i);
                                } else {
                                    msg = msg + NEWLINE_TAB + delFFiles.get(i);
                                }
                            }
                        }
                        msg = msg + "\n" + Messages.getString("reload.wanted");
                    } else {
                        if (!new File(dom.getFilename()).exists()) {
                            msg = Messages.getString("file.deleted.on.filesystem", dom.getFilename());
                            delFFiles.add(new File(dom.getFilename()));
                        } else {
                            msg = Messages.getString("file.changed.on.filesystem", dom.getFilename());
                        }
                    }
                    int c = MainWindow.message(sShell, msg, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    if (c == SWT.YES) {
                        try {
                            if (f.isDirectory()) {
                                for (int i = 0; i < changeFiles.size(); i++) {
                                    File hFfile = changeFiles.get(i);
                                    String sXPATH = getXPathString(hFfile, false);
                                    XPath x1 = XPath.newInstance(sXPATH);
                                    List<Element> listOfElement = x1.selectNodes(dom.getDoc());
                                    if (!listOfElement.isEmpty()) {
                                        Element e = listOfElement.get(0);
                                        Element pe = e.getParentElement();
                                        e.detach();
                                        Element n = MergeAllXMLinDirectory.readElementFromHotHolderFile(hFfile);
                                        pe.addContent((Element) n.clone());
                                    }
                                }
                                for (int i = 0; i < newFFiles.size(); i++) {
                                    File newHFFile = newFFiles.get(i);
                                    String sXPATH = getXPathString(newHFFile, true);
                                    XPath x1 = XPath.newInstance(sXPATH);
                                    List<Element> listOfElement = x1.selectNodes(dom.getDoc());
                                    if (!listOfElement.isEmpty()) {
                                        Element pe = listOfElement.get(0);
                                        Element n = MergeAllXMLinDirectory.readElementFromHotHolderFile(newHFFile);
                                        pe.addContent((Element) n.clone());
                                    } else {
                                        Element pe = new Element(sXPATH);
                                        dom.getRoot().addContent(pe);
                                        Element n = MergeAllXMLinDirectory.readElementFromHotHolderFile(newHFFile);
                                        pe.addContent((Element) n.clone());
                                    }
                                }
                                for (int i = 0; i < delFFiles.size(); i++) {
                                    File delFile = delFFiles.get(i);
                                    String sXPATH = getXPathString(delFile, false);
                                    XPath x1 = XPath.newInstance(sXPATH);
                                    List<Element> listOfElement = x1.selectNodes(dom.getDoc());
                                    if (!listOfElement.isEmpty()) {
                                        Element pe = listOfElement.get(0);
                                        pe.detach();
                                        ((SchedulerDom) dom).getHotFolderFiles().remove(delFile.getName());
                                    }
                                }
                                if (!changeFiles.isEmpty() || !newFFiles.isEmpty() || !delFFiles.isEmpty()) {
                                    SchedulerForm form = (SchedulerForm) container.getCurrentEditor();
                                    form.updateTree("main");
                                    form.update();
                                    dom.readFileLastModified();
                                }
                            } else {
                                if (!delFFiles.isEmpty()) {
                                    MainWindow.getContainer().getCurrentTab().dispose();
                                    return;
                                }
                                dom.read(dom.getFilename());
                                if (container.getCurrentEditor() instanceof SchedulerForm) {
                                    SchedulerForm form = (SchedulerForm) container.getCurrentEditor();
                                    form.updateTree("main");
                                    form.update();
                                } else if (container.getCurrentEditor() instanceof DocumentationForm) {
                                    DocumentationForm form = (DocumentationForm) container.getCurrentEditor();
                                    form.updateTree("main");
                                    form.update();
                                } else if (container.getCurrentEditor() instanceof ActionsForm) {
                                    ActionsForm form = (ActionsForm) container.getCurrentEditor();
                                    form.updateTree("main");
                                    form.update();
                                }
                            }
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    } else {
                        if (!f.isDirectory() && !delFFiles.isEmpty()) {
                            dom.setFilename(null);
                            dom.setChanged(true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            new ErrorLog(Messages.getString("exception.raised", sos.util.SOSClassUtil.getMethodName()), e);
        }
        flag = true;
    }

    private static String getXPathString(File hFfile, boolean onlyParentPath) {
        String aName = EMPTY;
        String eName = EMPTY;
        String parentElementname = EMPTY;
        String attributName = "name";
        int pos1 = hFfile.getName().indexOf(".");
        int pos2 = hFfile.getName().lastIndexOf(".");
        aName = hFfile.getName().substring(0, pos1);
        eName = hFfile.getName().substring(pos1 + 1, pos2);
        if ("order".equalsIgnoreCase(eName) || "add_order".equalsIgnoreCase(eName)) {
            parentElementname = "commands";
            aName = aName.substring(aName.indexOf(",") + 1);
            attributName = "id";
        } else if ("process_class".equalsIgnoreCase(eName)) {
            parentElementname = eName.concat("es");
        } else {
            parentElementname = eName.concat("s");
        }
        if (onlyParentPath) {
            return "//" + parentElementname;
        } else {
            return "//" + parentElementname + "/" + eName + "[@" + attributName + "='" + aName + "']";
        }
    }

    public static boolean saveDirectory(final DomParser dom, final boolean saveas, final int type, final String nameOfElement,
            final IContainer container) {
        Document currDoc = dom.getDoc();
        File configFile = null;
        try {
            if (dom.getFilename() == null || saveas) {
                DirectoryDialog fdialog = new DirectoryDialog(ErrorLog.getSShell(), SWT.MULTI);
                fdialog.setFilterPath(Options.getLastDirectory());
                fdialog.setText("Save object to hot folder ...");
                String path = fdialog.open();
                if (path == null) {
                    return false;
                }
                File _file = null;
                if ("order".equals(dom.getRoot().getName()) || "add_order".equals(dom.getRoot().getName())) {
                    _file =
                            new File(path + "//" + Utils.getAttributeValue("job_chain", dom.getRoot()) + ","
                                    + Utils.getAttributeValue("id", dom.getRoot()) + ".order.xml");
                } else {
                    _file = new File(path + "//" + Utils.getAttributeValue("name", dom.getRoot()) + "." + dom.getRoot().getName() + ".xml");
                }
                if (_file.exists()) {
                    int ok = ErrorLog.message(Messages.getString("MainListener.doFileOverwrite"), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    if (ok == SWT.NO) {
                        return false;
                    }
                }
                configFile = new File(path);
            } else {
                configFile = new File(dom.getFilename());
            }
            MergeAllXMLinDirectory save = null;
            if (dom instanceof SchedulerDom && ((SchedulerDom) dom).isLifeElement() && configFile.isFile()) {
                save = new MergeAllXMLinDirectory(configFile.getParent());
            } else {
                save = new MergeAllXMLinDirectory(configFile.getPath());
            }
            if (type == SchedulerDom.DIRECTORY) {
                save.saveXMLDirectory(currDoc, ((SchedulerDom) dom).getChangedJob());
            } else {
                org.jdom.Element elem = null;
                if (type == SchedulerDom.LIFE_LOCK) {
                    SchedulerForm form = (SchedulerForm) MainWindow.getContainer().getCurrentEditor();
                    org.eclipse.swt.widgets.Tree tree = form.getTree();
                    TreeData data = (TreeData) tree.getSelection()[0].getData();
                    elem = data.getElement().getChild("locks").getChild("lock");
                } else if (type == SchedulerDom.LIFE_PROCESS_CLASS) {
                    SchedulerForm form = (SchedulerForm) MainWindow.getContainer().getCurrentEditor();
                    org.eclipse.swt.widgets.Tree tree = form.getTree();
                    TreeData data = (TreeData) tree.getSelection()[0].getData();
                    elem = data.getElement().getChild("process_classes").getChild("process_class");
                } else {
                    elem = currDoc.getRootElement();
                }
                String name =
                        save.saveLifeElement(nameOfElement, elem, ((SchedulerDom) dom).getChangedJob(),
                                ((SchedulerDom) dom).getListOfChangeElementNames());
                Options.setLastDirectory(new File(name), dom);
                try {
                    dom.setFilename(new java.io.File(name).getCanonicalPath());
                } catch (Exception e) {
                }
                dom.setChanged(true);
            }
            dom.readFileLastModified();
            dom.setChanged(false);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " could not save directory.", e);
        }
        return true;
    }

}