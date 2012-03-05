package sos.scheduler.editor.conf.forms;

//import org.eclipse.draw2d.*;
import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyAdapter;
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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.FileNameSelector;
import sos.scheduler.editor.classes.LanguageSelector;
import sos.scheduler.editor.classes.TextArea;
import sos.scheduler.editor.classes.TextArea.enuSourceTypes;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.container.JobDocumentation;
import sos.scheduler.editor.conf.container.JobJavaAPI;
import sos.scheduler.editor.conf.container.JobSourceViewer;
import sos.scheduler.editor.conf.container.JobStartWhenDirectoryChanged;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.util.SOSString;

public class JobMainForm extends Composite implements IUpdateLanguage {

	@SuppressWarnings("unused")
	private final String			conSVNVersion		= "$Id$";

	@SuppressWarnings("unused")
	private static Logger			logger				= Logger.getLogger(JobMainForm.class);
	@SuppressWarnings("unused")
	private final String			conClassName		= "JobMainForm";

	private JobListener				objDataProvider		= null;
	private Group					objMainOptionsGroup	= null;
	private Group					gMain				= null;
	private Text					tbxJobName			= null;
	private Label					lblJobTitlelabel1	= null;
	private Label					lblJobChainJob		= null;
	private Label					lblProcessClass		= null;
	// private Combo tbxJobTitle = null;
	private Text					tbxJobTitle			= null;
	private Combo					cProcessClass		= null;
	private Composite				cOrder				= null;
	private Button					bOrderYes			= null;
	private Button					bOrderNo			= null;
	private boolean					updateTree			= false;
	private Button					bStopOnError		= null;
	private Button					butBrowse			= null;
	private boolean					init				= true;
	private Button					butShowProcessClass	= null;
	@SuppressWarnings("unused")
	private int						_style				= 0;

	private Button					butFavorite			= null;
	private int						type				= Editor.SCRIPT;
	// private Group gScript_1;
	private Group					gScript_2;
	// private Group gScript_3;
	private Text					tbxClassName		= null;
	private Group					gInclude			= null;
	private Button					bRemove				= null;
	private Text					tbxFile2Include		= null;
	private Button					bAdd				= null;
	private Group					gSource				= null;
	// private Text tSource = null;
	private StyledText				tSource				= null;
	private Label					label14				= null;
	private Label					label				= null;
	private Text					txtName				= null;
	private Spinner					spinner				= null;
	private ISchedulerUpdate		update				= null;
	private Table					tableIncludes		= null;
	private Button					butIsLiveFile		= null;
	private Combo					cboFavorite			= null;
	private HashMap<String, String>	favorites			= null;
	private SOSString				sosString			= null;
	private Label					lblPrefunction		= null;
	private Combo					cboPrefunction		= null;
	private Button					btnEditButton		= null;
	private Text					tClasspath			= null;
	private LanguageSelector		languageSelector	= null;

	private int						intComboBoxStyle	= SWT.NONE;

	// private int intComboBoxStyle = SWT.READ_ONLY;

	public JobMainForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main) {
		super(parent, style);
		// final ToolTipHandler tooltip = new ToolTipHandler(parent.getShell());

		init = true;
		_style = style;
		dom.setInit(true);
		this.setEnabled(Utils.isElementEnabled("job", dom, job));
		objDataProvider = new JobListener(dom, job, main);
		objDataProvider.setType(Editor.SCRIPT);
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

	private int	intNoOfLabelColumns	= 2;

	private void createGroup() {
		GridLayout gridLayoutMainOptionsGroup = new GridLayout();
		gridLayoutMainOptionsGroup.numColumns = 1;
		objMainOptionsGroup = new Group(this, SWT.NONE);
		objMainOptionsGroup.setText(objDataProvider.getJobNameAndTitle());
		objMainOptionsGroup.setLayout(gridLayoutMainOptionsGroup);
		setResizableV(objMainOptionsGroup);

		gridLayout = new GridLayout();
		gridLayout.marginHeight = 1;
		gridLayout.numColumns = 6;

		gMain = new Group(objMainOptionsGroup, SWT.NONE);
		gMain.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_FILL, true, true));
		gMain.setText(Messages.getLabel("MainOptions"));
		gMain.setToolTipText(Messages.getTooltip("MainOptions"));
		gMain.setLayout(gridLayout);
		setResizableV(gMain);

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
		// lblJobTitlelabel1.setText("Job Title:");
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
		// label9.setText("Process Class:");
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
		// butBrowse.setText("Browse");

		// final Label stop_on_errorLabel = new Label(gMain, SWT.NONE);
		// stop_on_errorLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		// // stop_on_errorLabel.setText("Stop On Error");
		// stop_on_errorLabel.setText(Messages.getLabel("StopOnError"));
		// stop_on_errorLabel.setToolTipText(Messages.getTooltip("StopOnError"));
		//
		// bStopOnError = new Button(gMain, SWT.CHECK);
		// bStopOnError.setToolTipText(Messages.getTooltip("StopOnError"));
		//
		// // http://www.sos-berlin.com/doc/en/scheduler.doc/xml/job.xml#attribute_stop_on_error
		//
		// bStopOnError.addKeyListener(new KeyListener() {
		// @Override
		// public void keyPressed(KeyEvent event) {
		// if (event.keyCode == SWT.F1) {
		// objDataProvider.openXMLAttributeDoc("job", "stop_on_error");
		// }
		// if (event.keyCode == SWT.F10) {
		// objDataProvider.openXMLDoc("job");
		// }
		// }
		//
		// @Override
		// public void keyReleased(KeyEvent arg0) {
		// }
		// });
		//
		// // gridData_16.widthHint = 17;
		// bStopOnError.setSelection(true);
		// bStopOnError.addSelectionListener(new SelectionAdapter() {
		// public void widgetSelected(final SelectionEvent e) {
		// if (init) {
		// return;
		// }
		// objDataProvider.setStopOnError(bStopOnError.getSelection());
		// }
		// });
		//
		createLanguageSelector(gMain);
		createScriptForm(objMainOptionsGroup);
		setResizable(objMainOptionsGroup);

		// if (executeForm == null) {
		// executeForm = new ExecuteForm(objMainOptionsGroup, _style, objDataProvider.get_dom(), objDataProvider.getJob(),
		// objDataProvider.get_main());
		// executeForm.setLayout(new GridLayout());
		// executeForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		// }
	}

	private void setResizable(Control objControl) {
		objControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
	}

	private void setResizableV(Control objControl) {
		boolean flgGrapVerticalspace = true;
		objControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, flgGrapVerticalspace));
	}

	private Composite	tabItemJavaAPIComposite			= null;
	private Composite	tabItemOptionsComposite			= null;
	private Composite	tabItemDirChangedComposite		= null;

	private Composite	tabItemIncludedFilesComposite	= null;
	private GridLayout	gridLayout						= null;
	private Composite	objTabControlComposite			= null;

	private CTabItem	tabItemEMail					= null;
	private Composite	tabItemEMailComposite			= null;

	private CTabItem	tabItemSourceViewer				= null;
	private Composite	tabItemSourceViewerComposite	= null;

	private CTabItem	tabItemMonitor					= null;
	private Composite	tabItemMonitorComposite			= null;

	private CTabItem	tabItemDocumentation			= null;
	private Composite	tabItemDocumentationComposite	= null;

	private CTabItem	tabItemOrderSetBack				= null;
	private Composite	tabItemOrderSetBackComposite	= null;

	private CTabItem	tabItemErrorSetBack				= null;
	private Composite	tabItemErrorSetBackComposite	= null;

	private CTabItem	tabItemProcessFile				= null;
	private Composite	tabItemProcessFileComposite		= null;

	private CTabItem	tabItemScript					= null;
	private Composite	tabItemScriptComposite			= null;
	private CTabItem	tabItemJavaAPI					= null;
	private CTabItem	tabItemOptions					= null;
	private CTabItem	tabItemDirChanged				= null;
	private CTabFolder	tabFolder						= null;

	@SuppressWarnings("deprecation")
	private void createScriptForm(Group pobjMainOptionsGroup) {

		tabFolder = new CTabFolder(pobjMainOptionsGroup, SWT.NONE); // SWT.Bottom
		tabFolder.setLayout(new GridLayout());
		tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 8));

		setResizableV(tabFolder);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent event) {
				if (event.item.equals(tabItemDirChanged)) {
					event.doit = true;
					createTabItem4DirWatcher(tabItemDirChangedComposite);
				}

				if (event.item.equals(tabItemEMail)) {
					event.doit = true;
					createEMailGroup(tabItemEMailComposite);
				}

				if (event.item.equals(tabItemProcessFile)) {
					event.doit = true;
					createProcessFileGroup(tabItemProcessFileComposite);
				}

				if (event.item.equals(tabItemJavaAPI)) {
					event.doit = true;
					createJavaAPITab(tabItemJavaAPIComposite);
				}

				if (event.item.equals(tabItemSourceViewer)) {
					event.doit = true;
					createSourceViewer(tabItemSourceViewerComposite);
				}
				
				if (event.item.equals(tabItemMonitor)) {
					event.doit = true;
					createMonitorGroup(tabItemMonitorComposite);
				}
				
				if (event.item.equals(tabItemDocumentation)) {
					event.doit = true;
					createDocumentationGroup(tabItemDocumentationComposite);
				}
				
				if (event.item.equals(tabItemOptions)) {
					event.doit = true;
					createOptionsGroup(tabItemOptionsComposite);
				}
			}
		});
		
		tabFolder.setSimple(false);
//		tabFolder.setUnselectedImageVisible(false);
		tabFolder.setUnselectedCloseVisible(false);
		//
		// Set up a gradient background for the selected tab
//		tabFolder.setSelectionBackground(
//				new Color[] { Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW),
//						Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW) },
//				new int[] { 50, 100 });

		// Add a listener to get the close button on each tab
		tabFolder.addCTabFolderListener(new CTabFolderAdapter() {
			public void itemClosed(CTabFolderEvent event) {
				event.doit = false;
			}

			public void minimize(CTabFolderEvent event) {
				tabFolder.setMinimized(true);
				layout(true);
			}

			public void maximize(CTabFolderEvent event) {
				tabFolder.setMaximized(true);
				tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				layout(true);
			}

			public void restore(CTabFolderEvent event) {
				tabFolder.setMinimized(false);
				tabFolder.setMaximized(false);
				tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				layout(true);
			}
		});

//		tabFolder.setMinimizeVisible(true);
//		tabFolder.setMaximizeVisible(true);

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

		final CTabItem tabItemIncludedFiles = new CTabItem(tabFolder, SWT.NONE);
		tabItemIncludedFiles.setText(Messages.getLabel("job.includedfiles"));
		tabItemIncludedFiles.setToolTipText("Additional Files to include for Shell-Scripts");
		tabItemIncludedFilesComposite = new Composite(tabFolder, SWT.NONE);
		tabItemIncludedFilesComposite.setLayout(new GridLayout());
		setResizableV(tabItemIncludedFilesComposite);
		tabItemIncludedFiles.setControl(tabItemIncludedFilesComposite);

		tabItemOptions = new CTabItem(tabFolder, SWT.NONE);
		tabItemOptions.setToolTipText("Job Options");
		tabItemOptions.setText(Messages.getLabel("Options"));
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

		tabItemMonitor = new CTabItem(tabFolder, SWT.NONE);
		tabItemMonitor.setToolTipText("Monitor");
		tabItemMonitor.setText(Messages.getLabel("PrePost"));
		tabItemMonitorComposite = new Composite(tabFolder, SWT.NONE);
		tabItemMonitorComposite.setLayout(new GridLayout());
		setResizableV(tabItemMonitorComposite);
		tabItemMonitor.setControl(tabItemMonitorComposite);

		if (objDataProvider.isOrderJob()) {
			tabItemOrderSetBack = new CTabItem(tabFolder, SWT.NONE);
			tabItemOrderSetBack.setToolTipText("Order SetBack");
			tabItemOrderSetBack.setText(Messages.getLabel("SetBack"));
			tabItemOrderSetBackComposite = new Composite(tabFolder, SWT.NONE);
			tabItemOrderSetBackComposite.setLayout(new GridLayout());
			setResizableV(tabItemOrderSetBackComposite);
			tabItemOrderSetBack.setControl(tabItemOrderSetBackComposite);
		}
		else {
			tabItemErrorSetBack = new CTabItem(tabFolder, SWT.NONE);
			tabItemErrorSetBack.setToolTipText("on Error");
			tabItemErrorSetBack.setText(Messages.getLabel("on Error"));
			tabItemErrorSetBackComposite = new Composite(tabFolder, SWT.NONE);
			tabItemErrorSetBackComposite.setLayout(new GridLayout());
			setResizableV(tabItemErrorSetBackComposite);
			tabItemErrorSetBack.setControl(tabItemErrorSetBackComposite);

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

		createScriptTab(tabItemScriptComposite);
		createTabs();

	}

	private String[] normalized(String[] str) {
		String[] retVal = new String[] { "" };
		try {
			favorites = new HashMap<String, String>();
			if (str == null)
				return new String[0];

			String newstr = "";
			retVal = new String[str.length];
			for (int i = 0; i < str.length; i++) {
				String s = sosString.parseToString(str[i]);
				int idx = s.indexOf("_");
				if (idx > -1) {
					String lan = s.substring(0, idx);
					String name = s.substring(idx + 1);
					if (name == null || lan == null) {

					}
					else
						favorites.put(name, lan);
					newstr = name + ";" + newstr;
				}
			}
			retVal = newstr.split(";");
			return retVal;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			return retVal;
		}
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
					TabSwitcher();
				}
			}
		});

		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.minimumWidth = 100;
		languageSelector.setLayoutData(gd_combo);
		languageSelector.select(0);
		languageSelector.setItems(objDataProvider._languagesJob);
	}

	private void createScriptTab(Composite pobjTabControlComposite) {

		gSource = new Group(pobjTabControlComposite, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 20);
//		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData_5.heightHint = 500;
		gridData_5.minimumHeight = 100;
		gSource.setLayoutData(gridData_5);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginHeight = 0;
		gridLayout_2.numColumns = 4;
		gSource.setLayout(gridLayout_2);
		gSource.setText(Messages.getLabel("job.executable.label"));

		createScriptTab2(gSource, enuSourceTypes.ScriptSource);
		pobjTabControlComposite.layout();
	}

	private void createScriptTab2(Composite pParentComposite, final enuSourceTypes penuSourceType) {
		init = true;
		Label lblPrefunction = new Label(pParentComposite, SWT.NONE);
		lblPrefunction.setText(Messages.getLabel("job.selectpredefinedfunctions"));
		cboPrefunction = new Combo(pParentComposite, intComboBoxStyle);
		cboPrefunction.setVisibleItemCount(7);

		cboPrefunction.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (cboPrefunction.getText().length() > 0) {
					String lan = "function_" + ((type == Editor.MONITOR) ? "monitor" : "job") + "_"
							+ objDataProvider.getLanguage(objDataProvider.getLanguage()) + "_";
					String sourceTemplate = Options.getProperty(lan.toLowerCase() + cboPrefunction.getText());
					if (sourceTemplate != null) {
						tSource.append(Options.getProperty(lan.toLowerCase() + cboPrefunction.getText()));
						cboPrefunction.setText("");
						tSource.setFocus();
					}
				}
			}
		});
		cboPrefunction.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		Button btnFont = new Button(pParentComposite, SWT.NONE);
		btnFont.setText(Messages.getLabel("job.font"));

		btnEditButton = new Button(pParentComposite, SWT.NONE);
		final GridData gridData_3 = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		gridData_3.widthHint = 30;
		btnEditButton.setLayoutData(gridData_3);
		btnEditButton.setImage(objDataProvider.getImage("icon_edit.gif"));

		final TextArea txtArea = new TextArea(pParentComposite, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
		txtArea.setDataProvider(objDataProvider, penuSourceType);
		tSource = txtArea.getControl();

		btnFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtArea.changeFont();
			}
		});

		btnEditButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				txtArea.StartExternalEditor();
			}
		});

		int language = objDataProvider.getLanguage();
		if (language != JobListener.NONE) {
			String strSource = objDataProvider.getSource();
			if (strSource.length() > 0)
				tSource.setText(strSource);
//			else {
//				if (tSource.getText().length() > 0)
//					objDataProvider.setSource(tSource.getText());
//			}
		}

		init = false;
		pParentComposite.layout();
	}

	private boolean			flgSourceViewerisCreated	= false;
	private JobSourceViewer	objSourceViewer				= null;

	private void createSourceViewer(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		if (flgSourceViewerisCreated == true) {
			objSourceViewer.refreshContent();
			return;
		}

		objSourceViewer = new JobSourceViewer(pParentComposite, objDataProvider);
		pParentComposite.layout();
		flgSourceViewerisCreated = true;
	}

	private boolean	flgOptionsGroupisCreated	= false;

	private void createOptionsGroup(final Composite pParentComposite) {
		if (flgOptionsGroupisCreated == true || pParentComposite == null) {
			return;
		}
		flgOptionsGroupisCreated = true;

		Group gOptionsGroup = new Group(pParentComposite, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 1);
		gridData_5.heightHint = 100;
		gridData_5.minimumHeight = 30;
		gOptionsGroup.setLayoutData(gridData_5);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginHeight = 0;
		gridLayout_2.numColumns = 4;
		gOptionsGroup.setLayout(gridLayout_2);
		gOptionsGroup.setText(Messages.getLabel("Options"));

		lblJobChainJob = new Label(gOptionsGroup, SWT.NONE);
		lblJobChainJob.setText(Messages.getLabel("OrderJob"));
		lblJobChainJob.setToolTipText(Messages.getTooltip("OrderJob"));
		lblJobChainJob.addHelpListener(new HelpListener() {
			@Override
			public void helpRequested(HelpEvent objHelpEvent) {
				MainWindow.message(Messages.getString("OrderJob.Help"), SWT.ICON_INFORMATION);
			}
		});

		lblJobChainJob.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.F1) {
					MainWindow.message("F1 gedrückt", SWT.ICON_INFORMATION);
				}

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});

		cOrder = new Composite(gOptionsGroup, SWT.NONE);
		cOrder.setLayout(new RowLayout());
		bOrderYes = new Button(cOrder, SWT.RADIO);

		bOrderYes.setText(Messages.getLabel("yes.order"));
		bOrderYes.setToolTipText(Messages.getTooltip("yes.order"));
		bOrderYes.setSelection(objDataProvider.getOrder());

		bOrderYes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (init)
					return;

				if (!bOrderYes.getSelection())
					return;

				objDataProvider.setOrder(bOrderYes.getSelection());

				boolean _deleteRuntimeAttribute = false;

				Element job = objDataProvider.getJob();
				if (objDataProvider.getOrder() && job != null && job.getChild("run_time") != null) {
					if (sos.scheduler.editor.app.Utils.getAttributeValue("single_start", job.getChild("run_time")).length() > 0
							|| sos.scheduler.editor.app.Utils.getAttributeValue("let_run", job.getChild("run_time")).length() > 0
							|| sos.scheduler.editor.app.Utils.getAttributeValue("once", job.getChild("run_time")).length() > 0) {

						if (isVisible()) {
							_deleteRuntimeAttribute = Utils.checkElement(objDataProvider.getJobName(), objDataProvider.get_dom(), Editor.JOB, "change_order");
						}

						if (_deleteRuntimeAttribute) {
							objDataProvider.getJob().getChild("run_time").removeAttribute("single_start");
							objDataProvider.getJob().getChild("run_time").removeAttribute("let_run");
							objDataProvider.getJob().getChild("run_time").removeAttribute("once");
						}
					}
				}
			}
		});
		bOrderNo = new Button(cOrder, SWT.RADIO);

		bOrderNo.setText(Messages.getLabel("no.order"));
		bOrderNo.setToolTipText(Messages.getTooltip("no.order"));
		bOrderNo.setEnabled(true);
		bOrderNo.setSelection(!objDataProvider.getOrder());

		bOrderNo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (init)
					return;
				// objDataProvider.setPriority(sPriority.getText());

				if (objDataProvider.getOrder() && !Utils.checkElement(objDataProvider.getJobName(), objDataProvider.get_dom(), Editor.JOB, "change_order")) {
					// e.doit = false;
					init = true;
					bOrderNo.setSelection(false);
					bOrderYes.setSelection(true);
					init = false;
					return;
				}

				objDataProvider.setOrder(!bOrderNo.getSelection());
			}
		});

		new Label(gOptionsGroup, SWT.NONE);
		new Label(gOptionsGroup, SWT.NONE);

		final Label stop_on_errorLabel = new Label(gOptionsGroup, SWT.NONE);
		stop_on_errorLabel.setText(Messages.getLabel("StopOnError"));
		stop_on_errorLabel.setToolTipText(Messages.getTooltip("StopOnError"));

		bStopOnError = new Button(gOptionsGroup, SWT.CHECK);
		bStopOnError.setToolTipText(Messages.getTooltip("StopOnError"));
		bStopOnError.setSelection(objDataProvider.getStopOnError());

		// http://www.sos-berlin.com/doc/en/scheduler.doc/xml/job.xml#attribute_stop_on_error

		bStopOnError.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (objDataProvider.Check4HelpKey(event.keyCode, "job", "stop_on_error")) {
					event.doit = true;
					return;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});

		// gridData_16.widthHint = 17;
		bStopOnError.setSelection(true);
		bStopOnError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (init) {
					return;
				}
				objDataProvider.setStopOnError(bStopOnError.getSelection());
			}
		});

		new Label(gOptionsGroup, SWT.NONE);
		new Label(gOptionsGroup, SWT.NONE);

		final Label logLevelLabel = new Label(gOptionsGroup, SWT.NONE);
		logLevelLabel.setText(Messages.getLabel("LogLevel"));

		LogLevel = new Combo(gOptionsGroup, intComboBoxStyle);
		GridData gd_LogLevel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_LogLevel.minimumWidth = 150;
		LogLevel.setLayoutData(gd_LogLevel);
		LogLevel.setText(objDataProvider.getValue("log_level"));

		LogLevel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				objDataProvider.setValue("log_level", LogLevel.getText());
			}
		});
		LogLevel.setItems(new String[] { "info", "debug1", "debug2", "debug3", "debug4", "debug5", "debug6", "debug7", "debug8", "debug9", "" });

		// ------

		new Label(gOptionsGroup, SWT.NONE);

		final Label label_2 = new Label(gOptionsGroup, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1);
		gridData_4.heightHint = 8;
		label_2.setLayoutData(gridData_4);

		// -----
		final Label historyLabel = new Label(gOptionsGroup, SWT.NONE);
		historyLabel.setText(Messages.getLabel("history"));

		cboHistory = new Combo(gOptionsGroup, intComboBoxStyle);
		cboHistory.setText(objDataProvider.getHistory());
		cboHistory.setItems(new String[] { "yes", "no", "" });

		cboHistory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				objDataProvider.setHistory(cboHistory.getText());
			}
		});
		GridData gd_cboHistory = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_cboHistory.minimumWidth = 150;
		cboHistory.setLayoutData(gd_cboHistory);

		new Label(gOptionsGroup, SWT.NONE);
		new Label(gOptionsGroup, SWT.NONE);
		// ----
		final Label historyOnProcessLabel = new Label(gOptionsGroup, SWT.NONE);
		historyOnProcessLabel.setText(Messages.getLabel("HistoryOnProcess"));

		cboHistoryOnProcess = new Combo(gOptionsGroup, intComboBoxStyle);
		cboHistoryOnProcess.setText(objDataProvider.getHistoryOnProcess());

		cboHistoryOnProcess.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (init) {
					return;
				}
				boolean isDigit = true;
				char[] c = cboHistoryOnProcess.getText().toCharArray();
				for (int i = 0; i < c.length; i++) {
					isDigit = Character.isDigit(c[i]);
					if (!isDigit)
						break;
				}
				if (cboHistoryOnProcess.getText().equals("yes") || cboHistoryOnProcess.getText().equals("no") || isDigit)
					objDataProvider.setHistoryOnProcess(cboHistoryOnProcess.getText());
			}
		});

		cboHistoryOnProcess.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});

		cboHistoryOnProcess.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				cboHistoryOnProcess.setText(objDataProvider.getValue("history_on_process"));
			}
		});

		GridData gd_cboHistoryOnProcess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_cboHistoryOnProcess.minimumWidth = 150;
		cboHistoryOnProcess.setLayoutData(gd_cboHistoryOnProcess);

		new Label(gOptionsGroup, SWT.NONE);
		new Label(gOptionsGroup, SWT.NONE);

		// -----
		final Label historyWithLogLabel = new Label(gOptionsGroup, SWT.NONE);
		historyWithLogLabel.setText(Messages.getLabel("HistoryWithLog"));

		cboHistoryWithLog = new Combo(gOptionsGroup, intComboBoxStyle);
		cboHistoryWithLog.setText(objDataProvider.getHistoryWithLog());
		cboHistoryWithLog.setItems(new String[] { "yes", "no", "gzip", "" });

		cboHistoryWithLog.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				objDataProvider.setHistoryWithLog(cboHistoryWithLog.getText());
			}
		});
		GridData gd_cboHistoryWithLog = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_cboHistoryWithLog.minimumWidth = 150;
		cboHistoryWithLog.setLayoutData(gd_cboHistoryWithLog);

		// ---
		cboHistoryOnProcess.setItems(new String[] { "0", "1", "2", "3", "4", "" });

		pParentComposite.layout();
	}

	private boolean	flgMonitorisCreated	= false;

	private void createMonitorGroup(final Composite pParentComposite) {
		if (flgMonitorisCreated == true || pParentComposite == null) {
			return;
		}
		flgMonitorisCreated = true;

		Group gMonitorGroup = new Group(pParentComposite, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, true, true, 13, 1);
		gridData_5.heightHint = 100;
		gridData_5.minimumHeight = 30;
		gMonitorGroup.setLayoutData(gridData_5);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginHeight = 0;
		gridLayout_2.numColumns = 4;
		gMonitorGroup.setLayout(gridLayout_2);
		gMonitorGroup.setText(Messages.getLabel("job.executable.label"));

		final Label nameLabel = new Label(gMonitorGroup, SWT.NONE);
		GridData gd_nameLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_nameLabel.widthHint = 60;
		nameLabel.setLayoutData(gd_nameLabel);
		nameLabel.setText("Name");

		final Text txtMonitorName = new Text(gMonitorGroup, SWT.BORDER);
		txtMonitorName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtMonitorName.selectAll();
			}
		});
		GridData gd_txtName = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gd_txtName.widthHint = 135;
		txtMonitorName.setLayoutData(gd_txtName);
		txtMonitorName.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (!init) {
					objDataProvider.setMonitorName(txtMonitorName.getText());
				}
			}
		});

		final Label orderingLabel = new Label(gMonitorGroup, SWT.NONE);
		orderingLabel.setText("Ordering");

		spinner = new Spinner(gMonitorGroup, SWT.BORDER);
		GridData gd_spinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_spinner.widthHint = 106;
		spinner.setLayoutData(gd_spinner);
		spinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (!init)
					objDataProvider.setOrdering(String.valueOf(spinner.getSelection()));
			}
		});
		spinner.setSelection(-1);
		spinner.setMaximum(999);

		// createLanguageSelector(pParentComposite);

		Label labelLanguageSelector = new Label(gMonitorGroup, SWT.NONE);
		labelLanguageSelector.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, intNoOfLabelColumns, 1));
		labelLanguageSelector.setText(Messages.getLabel("Language.Monitor"));

		final LanguageSelector languageSelector4Monitor = new LanguageSelector(gMonitorGroup, SWT.NONE);
		languageSelector4Monitor.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (objDataProvider != null && init == false) {
					String strT = languageSelector4Monitor.getText();
					objDataProvider.setLanguage(strT);
				}
			}
		});

		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.minimumWidth = 100;
		languageSelector4Monitor.setLayoutData(gd_combo);
		languageSelector4Monitor.select(0);
		languageSelector4Monitor.setItems(objDataProvider._languagesMonitor);

		butFavorite = new Button(gMonitorGroup, SWT.NONE);

		butFavorite.setEnabled(false);
		butFavorite.setVisible(true);
		butFavorite.setText("Favorites"); //TODO lang "Favorites"

		cboFavorite = new Combo(gMonitorGroup, intComboBoxStyle);
		GridData gd_cboFavorite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_cboFavorite.widthHint = 153;
		cboFavorite.setLayoutData(gd_cboFavorite);
		cboFavorite.setVisible(true);

		butFavorite.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				Options.setProperty("monitor_favorite_" + objDataProvider.getLanguage(objDataProvider.getLanguage()) + "_" + txtMonitorName.getText(),
						getFavoriteValue());
				Options.saveProperties();
				cboFavorite.setItems(normalized(Options.getPropertiesWithPrefix("monitor_favorite_")));
			}
		});

		cboFavorite.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (cboFavorite.getText().length() > 0) {
					if (Options.getProperty(getPrefix() + cboFavorite.getText()) != null) {

						if ((tbxClassName.isEnabled() && tbxClassName.getText().length() > 0)
								|| (tableIncludes.isEnabled() && tableIncludes.getItemCount() > 0)) {
							int c = MainWindow.message(getShell(), "Overwrite this Monitor?", SWT.ICON_QUESTION | SWT.YES | SWT.NO);
							if (c != SWT.YES)
								return;
							else {
								tbxClassName.setText("");
								tableIncludes.clearAll();
								objDataProvider.removeIncludes();
							}
						}

						if (favorites != null && favorites.get(cboFavorite.getText()) != null && favorites.get(cboFavorite.getText()).toString().length() > 0) {
							objDataProvider.setLanguage(objDataProvider.languageAsInt(favorites.get(cboFavorite.getText()).toString()));
							txtMonitorName.setText(cboFavorite.getText());
							if (objDataProvider.isJava()) {
								tbxClassName.setText(Options.getProperty(getPrefix() + cboFavorite.getText()));
							}
							else {
								String[] split = Options.getProperty(getPrefix() + cboFavorite.getText()).split(";");
								for (int i = 0; i < split.length; i++) {
									objDataProvider.addInclude(split[i]);
								}
							}
						}
					}
				}
			}
		});

		createScriptTab2(gMonitorGroup, enuSourceTypes.MonitorSource);
		pParentComposite.layout();
	}

	private boolean	flgProcessFileIsCreated	= false;

	private void createProcessFileGroup(final Composite pParentComposite) {
		if (flgProcessFileIsCreated == true || pParentComposite == null) {
			return;
		}
		flgProcessFileIsCreated = true;
		pParentComposite.setLayout(new GridLayout());

		GridData gridData61 = new org.eclipse.swt.layout.GridData();
		gridData61.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData61.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData41 = new org.eclipse.swt.layout.GridData();
		gridData41.grabExcessHorizontalSpace = false;
		gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData41.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData41.horizontalSpan = 2;
		GridData gridData21 = new org.eclipse.swt.layout.GridData();
		gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData21.grabExcessHorizontalSpace = false;
		gridData21.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData12 = new org.eclipse.swt.layout.GridData();
		gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData12.grabExcessHorizontalSpace = true;
		gridData12.horizontalSpan = 3;
		gridData12.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalSpan = 3;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalSpan = 3;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		Group gExecutable = new Group(pParentComposite, SWT.NONE);
		gExecutable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		gExecutable.setText("Run Executable");
		gExecutable.setLayout(gridLayout);
		Label label1 = new Label(gExecutable, SWT.NONE);
		label1.setText("File");
		final Text tExecuteFile = new Text(gExecutable, SWT.BORDER);
		tExecuteFile.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tExecuteFile.selectAll();
			}
		});
		tExecuteFile.setLayoutData(gridData12);
		tExecuteFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!init) {
					objDataProvider.setFile(tExecuteFile.getText());
				}
			}
		});
		Label label3 = new Label(gExecutable, SWT.NONE);
		label3.setText("Parameter:   ");
		final Text tParameter = new Text(gExecutable, SWT.BORDER);
		tParameter.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tParameter.selectAll();
			}
		});
		tParameter.setLayoutData(gridData2);
		tParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!init)
					objDataProvider.setParam(tParameter.getText());
			}
		});
		Label label4 = new Label(gExecutable, SWT.NONE);
		label4.setText("Log file:");
		final Text tLogFile = new Text(gExecutable, SWT.BORDER);
		tLogFile.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tLogFile.selectAll();
			}
		});
		tLogFile.setLayoutData(gridData3);
		tLogFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!init)
					objDataProvider.setLogFile(tLogFile.getText());
			}
		});
		Label label5 = new Label(gExecutable, SWT.NONE);
		label5.setText("Ignore:");
		label5.setLayoutData(gridData61);
		final Button bIgnoreSignal = new Button(gExecutable, SWT.CHECK);
		bIgnoreSignal.setText("Signal");
		bIgnoreSignal.setLayoutData(gridData21);
		bIgnoreSignal.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (!init)
					objDataProvider.setIgnoreSignal(bIgnoreSignal.getSelection());
			}
		});
		final Button bIgnoreError = new Button(gExecutable, SWT.CHECK);
		bIgnoreError.setText("Error");
		bIgnoreError.setLayoutData(gridData41);
		bIgnoreError.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (!init)
					objDataProvider.setIgnoreError(bIgnoreError.getSelection());
			}
		});

		boolean enabled = true;
		tExecuteFile.setEnabled(enabled);
		tLogFile.setEnabled(enabled);
		tParameter.setEnabled(enabled);
		bIgnoreError.setEnabled(enabled);
		bIgnoreSignal.setEnabled(enabled);

		tExecuteFile.setToolTipText(Messages.getTooltip("process.file"));
		tParameter.setToolTipText(Messages.getTooltip("process.param"));
		tLogFile.setToolTipText(Messages.getTooltip("process.log_file"));
		bIgnoreSignal.setToolTipText(Messages.getTooltip("process.ignore_signal"));
		bIgnoreError.setToolTipText(Messages.getTooltip("process.ignore_error"));

		pParentComposite.layout();
	}

	private boolean				flgDocumentationisCreated	= false;
	private JobDocumentation	objJD						= null;

	private void createDocumentationGroup(final Composite pParentComposite) {
		if (flgDocumentationisCreated == true || pParentComposite == null) {
			Editor.objMainWindow.setStatusLine("Tab already created");
			return;
		}
		flgDocumentationisCreated = true;

		if (objJD == null) {
			objJD = new JobDocumentation(pParentComposite, objDataProvider);
		}
		pParentComposite.layout();
	}

	private boolean		flgJavaAPITabisCreated	= false;
	private JobJavaAPI	objJAPI					= null;

	private void createJavaAPITab(final Composite pParentComposite) {
		if (flgJavaAPITabisCreated == true || pParentComposite == null) {
			Editor.objMainWindow.setStatusLine("Tab already created");
			return;
		}
		objJAPI = new JobJavaAPI(pParentComposite, objDataProvider);
		flgJavaAPITabisCreated = true;
		pParentComposite.layout();
	}

	private void createTabs() {
		int intNumColumns = 3;

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.numColumns = intNumColumns;
		gInclude = new Group(tabItemIncludedFilesComposite, SWT.NONE);
		gInclude.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, true, intNumColumns, 1));
		gInclude.setText("Include Files");
		gInclude.setLayout(gridLayout1);
		setResizableV(gInclude);

		butIsLiveFile = new Button(gInclude, SWT.CHECK);
		butIsLiveFile.setText("in live Folder");

		final FileNameSelector fleFile2Include = new FileNameSelector(gInclude, SWT.BORDER);
		fleFile2Include.setDataProvider(objDataProvider);
		tbxFile2Include = fleFile2Include;

		tbxFile2Include.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				String strT = fleFile2Include.getFileName();
				if (strT.trim().length() > 0) {
					applyFile2Include();
				}
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		butIsLiveFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (init) {
					return;
				}
				fleFile2Include.flgIsFileFromLiveFolder = butIsLiveFile.getSelection();
			}
		});

		bAdd = new Button(gInclude, SWT.NONE);
		label = new Label(gInclude, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText("Label");
		label.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false, 3, 1));

		tableIncludes = new Table(gInclude, SWT.FULL_SELECTION | SWT.BORDER);
		tableIncludes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tableIncludes.getSelectionCount() > 0) {

					tbxFile2Include.setText(tableIncludes.getSelection()[0].getText(0));
					tbxFile2Include.setEnabled(true);
					butIsLiveFile.setSelection(tableIncludes.getSelection()[0].getText(1) != null
							&& tableIncludes.getSelection()[0].getText(1).equals("live_file"));
					bRemove.setEnabled(tableIncludes.getSelectionCount() > 0);
					bAdd.setEnabled(false);
				}
			}
		});
		tableIncludes.setLinesVisible(true);
		tableIncludes.setHeaderVisible(true);
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 2);
		gridData_2.heightHint = 4;
		gridData_2.minimumHeight = 20;
		tableIncludes.setLayoutData(gridData_2);

		final TableColumn newColumnTableColumn = new TableColumn(tableIncludes, SWT.NONE);
		newColumnTableColumn.setWidth(272);
		newColumnTableColumn.setText("Name");

		final TableColumn newColumnTableColumn_1 = new TableColumn(tableIncludes, SWT.NONE);
		newColumnTableColumn_1.setWidth(81);
		newColumnTableColumn_1.setText("File/Live File");

		final Button butNew = new Button(gInclude, SWT.NONE);
		butNew.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tableIncludes.deselectAll();
				tbxFile2Include.setText("");
				tbxFile2Include.setEnabled(true);
				butIsLiveFile.setSelection(false);
				butIsLiveFile.setEnabled(true);
				tbxFile2Include.setFocus();
			}
		});
		butNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butNew.setText("New");
		tbxFile2Include.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				bAdd.setEnabled(objDataProvider.isNotEmpty(tbxFile2Include.getText()));
			}
		});
		tbxFile2Include.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR && objDataProvider.isNotEmpty(tbxFile2Include.getText())) {
					objDataProvider.addInclude(tableIncludes, tbxFile2Include.getText(), butIsLiveFile.getSelection());
					objDataProvider.fillTable(tableIncludes);
					tbxFile2Include.setText("");
				}
			}
		});
		bAdd.setText("&Add File");
		bAdd.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		bAdd.setEnabled(false);
		bAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				applyFile2Include();
			}
		});
		bRemove = new Button(gInclude, SWT.NONE);
		bRemove.setText("Remove File");
		bRemove.setEnabled(false);
		bRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableIncludes.getSelectionCount() > 0) {
					int index = tableIncludes.getSelectionIndex();
					objDataProvider.removeInclude(index);
					objDataProvider.fillTable(tableIncludes);
					if (index >= tableIncludes.getItemCount())
						index--;
					// if (tableIncludes.getItemCount() > 0)
					// tableIncludes.setSelection(index);
					tableIncludes.deselectAll();
					tbxFile2Include.setText("");
					tbxFile2Include.setEnabled(false);
				}
			}
		});

	}

	private void applyFile2Include() {
		String strFileName = tbxFile2Include.getText();
		boolean flgIsLiveFile = butIsLiveFile.getSelection();
		File objF = null;
		if (flgIsLiveFile == true) {
			objF = new File(Options.getSchedulerHotFolder(), strFileName);
		}
		else {
			objF = new File(strFileName);
		}
		if (objF.exists() == false || objF.canRead() == false) {
			MainWindow.ErrMsg(String.format("File '%1$s' not found or is not readable", strFileName));
			tbxFile2Include.setText("");
		}
		else {
			objDataProvider.addInclude(tableIncludes, strFileName, butIsLiveFile.getSelection());
			tbxFile2Include.setText("");
			tbxFile2Include.setEnabled(false);
			butIsLiveFile.setEnabled(false);
			tableIncludes.deselectAll();
		}
		tbxFile2Include.setFocus();
	}

	private String getPrefix() {
		if (favorites != null && cboFavorite.getText().length() > 0 && favorites.get(cboFavorite.getText()) != null)
			return "monitor_favorite_" + favorites.get(cboFavorite.getText()) + "_";
		if (objDataProvider.getLanguage() == 0) {
			return "";
		}
		return "monitor_favorite_" + objDataProvider.getLanguage(objDataProvider.getLanguage()) + "_";
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

		// bOrderYes.setSelection(objDataProvider.getOrder());
		// bOrderNo.setSelection(!objDataProvider.getOrder());
		// bStopOnError.setSelection(objDataProvider.getStopOnError());
		//
		// LogLevel.setText(objDataProvider.getValue("log_level"));
		//
		// cboHistory.setText(objDataProvider.getValue("history"));
		// cboHistoryOnProcess.setText(objDataProvider.getValue("history_on_process"));
		// cboHistoryWithLog.setText(objDataProvider.getValue("history_with_log"));

		init = false;
	}

	private void checkName() {
		if (Utils.existName(tbxJobName.getText(), objDataProvider.getJob(), "job")) {
			tbxJobName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		}
		else {
			tbxJobName.setBackground(null);
		}
	}

	public void setToolTipText() {

	}

	private String getFavoriteValue() {
		if (objDataProvider.isJava()) {
			return tbxClassName.getText();
		}
		else {
			return objDataProvider.getIncludesAsString();
		}
	}

	private void fillForm() {
		init = true;
		int language = objDataProvider.getLanguage();
		if (cboPrefunction != null) {
			cboPrefunction.removeAll();
		}

		tbxJobName.setText(objDataProvider.getJobName());
		if (type == Editor.MONITOR) {
			txtName.setText(objDataProvider.getJobName());
			spinner.setSelection((objDataProvider.getOrdering().length() == 0 ? 0 : Integer.parseInt(objDataProvider.getOrdering())));
			// bShell.setVisible(false);
		}

		setEnabled(true);

		Enable(cboFavorite, true);
		Enable(butFavorite, true);
		languageSelector.selectLanguageItem(language);
		Enable(tClasspath, false);

		TabSwitcher();

		if (language != JobListener.NONE) {
			objDataProvider.fillTable(tableIncludes);
		}

		String lan = "";
		if (!languageSelector.isShell() && !languageSelector.isJava()) {
			if (type == Editor.MONITOR) {
				lan = "spooler_task_before;spooler_task_after;spooler_process_before;spooler_process_after";
			}
			else {
				lan = "spooler_init;spooler_open;spooler_process;spooler_close;spooler_exit;spooler_on_error;spooler_on_success";
			}
			cboPrefunction.setItems(lan.split(";"));
		}
		TabSwitcher();
		init = false;
	}

	private void TabSwitcher() {

		if (cboPrefunction == null) {
			return;
		}
		
		cboPrefunction.setEnabled(false);
		if (languageSelector.isJava() && tbxClassName != null) {
			tbxClassName.setEnabled(true);
			tbxClassName.setFocus();
			tClasspath.setEnabled(true);
			tClasspath.setText(objDataProvider.getClasspath());

			if (!tbxClassName.getText().equals("") && objDataProvider.getJavaClass().equals(""))
				objDataProvider.setJavaClass(tbxClassName.getText());
			tbxClassName.setText(objDataProvider.getJavaClass());

			objTabControlComposite.setEnabled(false);
			tabItemIncludedFilesComposite.setEnabled(false);
			tabItemJavaAPIComposite.setEnabled(true);
			tabItemJavaAPIComposite.forceFocus();
			tabFolder.setSelection(tabItemJavaAPI);
		}
		else {
			if (tbxClassName != null) {
				tbxClassName.setEnabled(false);
				tClasspath.setEnabled(false);
				tabItemJavaAPIComposite.setEnabled(false);
			}

			String lan = "";
			if (languageSelector.isScriptLanguage()) {
				if (type == Editor.MONITOR) {
					lan = "spooler_task_before;spooler_task_after;spooler_process_before;spooler_process_after";
				}
				else {
					lan = "spooler_init;spooler_open;spooler_process;spooler_close;spooler_exit;spooler_on_error;spooler_on_success";
				}
				cboPrefunction.setItems(lan.split(";"));
				cboPrefunction.setEnabled(true);
			}

			objTabControlComposite.setEnabled(true);
			tabItemIncludedFilesComposite.setEnabled(true);
			objTabControlComposite.forceFocus();
			/** 
			 * Der Wechsel auf den Tab findet zwar statt, aber der Inhalt des Tabs wird nicht angezeigt.
			 * Keine Ahnung warum das so ist.
			 */
//			int intLastTabItemIndex = Options.getLastTabItemIndex();
//			if (intLastTabItemIndex == -1) {
				tabFolder.setSelection(tabItemScript);
				tSource.setFocus();
//			}
//			else {
//				CTabItem objSelectedItem = tabFolder.getItem(intLastTabItemIndex);
//				tabFolder.setSelection(objSelectedItem);
//				tabFolder.showItem(objSelectedItem);
////				tabFolder.layout();
//			}
		}
	}

	private void Enable(Control objC, boolean flgStatus) {
		if (objC != null) {
			objC.setEnabled(flgStatus);
		}
	}

	// private Group group = null;

	boolean				flgTabItem4DirWatcherIsCreated	= false;
	private JobStartWhenDirectoryChanged objSWDC = null;
	
	private void createTabItem4DirWatcher(final Composite pParentComposite) {
		if (flgTabItem4DirWatcherIsCreated == true || pParentComposite == null) {
			return;
		}
		flgTabItem4DirWatcherIsCreated = true;

		objSWDC = new JobStartWhenDirectoryChanged(pParentComposite, objDataProvider);
		flgTabItem4DirWatcherIsCreated = true;
		pParentComposite.layout();
	}

	private Combo	cboHistoryWithLog		= null;
	private Combo	cboHistoryOnProcess		= null;
	private Combo	cboHistory				= null;
	private Combo	mailOnDelayAfterError	= null;
	// private MailListener listener = null;
	// private int type = -1;
	// private Group group = null;
	private Text	mailCC					= null;
	private Text	mailBCC					= null;
	private Combo	mailOnError				= null;
	private Combo	mailOnWarning			= null;
	private Combo	mailOnSuccess			= null;
	private Combo	mailOnProcess			= null;
	private Text	mailTo					= null;
	private Combo	LogLevel				= null;

	private boolean	flgEMailGroupIsCreated	= false;

	private void createEMailGroup(final Composite pParentComposite) {

		if (flgEMailGroupIsCreated == true || pParentComposite == null) {
			return;
		}
		flgEMailGroupIsCreated = true;

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;

		GridLayout gridLayout4EMailGroup = new GridLayout();
		gridLayout4EMailGroup.marginRight = 5;
		gridLayout4EMailGroup.marginLeft = 5;
		gridLayout4EMailGroup.marginTop = 5;
		gridLayout4EMailGroup.numColumns = 2;

		Group group4EMail = new Group(pParentComposite, SWT.NONE);
		group4EMail.setText(Messages.getLabel("Notifications"));
		group4EMail.setLayout(gridLayout4EMailGroup);
		group4EMail.setLayoutData(gridData);

		Label labelMailOnError = new Label(group4EMail, SWT.NONE);
		labelMailOnError.setText(Messages.getLabel("MailOnError"));

		mailOnError = new Combo(group4EMail, intComboBoxStyle);
		mailOnError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
				objDataProvider.setValue("mail_on_error", mailOnError.getText(), "no");
			}
		});
		GridData gd_mailOnError = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnError.minimumWidth = 150;
		mailOnError.setLayoutData(gd_mailOnError);
		Label labelMailOnWarning = new Label(group4EMail, SWT.NONE);
		labelMailOnWarning.setText(Messages.getLabel("MailOnWarning"));

		mailOnWarning = new Combo(group4EMail, intComboBoxStyle);
		mailOnWarning.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				mailOnDelayAfterError.setEnabled(mailOnWarning.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
				objDataProvider.setValue("mail_on_warning", mailOnWarning.getText(), "no");
			}
		});
		GridData gd_mailOnWarning = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnWarning.minimumWidth = 150;
		mailOnWarning.setLayoutData(gd_mailOnWarning);
		Label label3 = new Label(group4EMail, SWT.NONE);
		label3.setText(Messages.getLabel("MailOnSuccess"));

		mailOnSuccess = new Combo(group4EMail, intComboBoxStyle);
		mailOnSuccess.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				objDataProvider.setValue("mail_on_success", mailOnSuccess.getText(), "no");
			}
		});
		GridData gd_mailOnSuccess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnSuccess.minimumWidth = 150;
		mailOnSuccess.setLayoutData(gd_mailOnSuccess);

		final Label mailOnProcessLabel = new Label(group4EMail, SWT.NONE);
		mailOnProcessLabel.setText(Messages.getLabel("Mail On Process"));

		mailOnProcess = new Combo(group4EMail, intComboBoxStyle);
		mailOnProcess.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				objDataProvider.setValue("mail_on_process", mailOnProcess.getText(), "no");
			}
		});
		GridData gd_mailOnProcess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnProcess.minimumWidth = 150;
		mailOnProcess.setLayoutData(gd_mailOnProcess);

		final Label mailOnDelayLabel = new Label(group4EMail, SWT.NONE);
		mailOnDelayLabel.setText(Messages.getLabel("MailOnDelayAfterError"));

		mailOnDelayAfterError = new Combo(group4EMail, intComboBoxStyle);
		mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
		mailOnDelayAfterError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				objDataProvider.setValue("mail_on_delay_after_error", mailOnDelayAfterError.getText(), "no");
			}
		});
		mailOnDelayAfterError.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

		final Label ddddLabel = new Label(group4EMail, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_1.heightHint = 8;
		ddddLabel.setLayoutData(gridData_1);

		final Label mailToLabel = new Label(group4EMail, SWT.NONE);
		mailToLabel.setText(Messages.getLabel("MailTo"));

		mailTo = new Text(group4EMail, SWT.BORDER);
		mailTo.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				mailTo.selectAll();
			}
		});
		mailTo.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				objDataProvider.setValue("log_mail_to", mailTo.getText());
			}
		});
		mailTo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label mailCcLabel = new Label(group4EMail, SWT.NONE);
		mailCcLabel.setText(Messages.getLabel("MailCC"));

		mailCC = new Text(group4EMail, SWT.BORDER);
		mailCC.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				mailCC.selectAll();
			}
		});
		mailCC.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				objDataProvider.setValue("log_mail_cc", mailCC.getText());
			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_2.minimumWidth = 60;
		mailCC.setLayoutData(gridData_2);

		final Label mailBccLabel = new Label(group4EMail, SWT.NONE);
		mailBccLabel.setText(Messages.getLabel("MailBCC"));

		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.minimumWidth = 60;
		mailBCC = new Text(group4EMail, SWT.BORDER);
		mailBCC.addFocusListener(new FocusAdapter() {
			public void focusGained(final org.eclipse.swt.events.FocusEvent e) {
				mailBCC.selectAll();
			}
		});
		mailBCC.setLayoutData(gridData);
		mailBCC.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				objDataProvider.setValue("log_mail_bcc", mailBCC.getText());
			}
		});

		final Label label_1 = new Label(group4EMail, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_3.heightHint = 8;
		label_1.setLayoutData(gridData_3);

		pParentComposite.layout();
	}

} // @jve:decl-index=0:visual-constraint="10,10"
