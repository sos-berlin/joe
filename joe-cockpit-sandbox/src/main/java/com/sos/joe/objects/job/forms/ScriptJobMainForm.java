package com.sos.joe.objects.job.forms;

//import org.eclipse.draw2d.*;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import sos.scheduler.editor.conf.composites.JobMainComposite;
import sos.scheduler.editor.conf.container.JobDelayAfterError;
import sos.scheduler.editor.conf.container.JobDocumentation;
import sos.scheduler.editor.conf.container.JobEmailSettings;
import sos.scheduler.editor.conf.container.JobOptions;
import sos.scheduler.editor.conf.container.JobSetback;
import sos.scheduler.editor.conf.container.JobStartWhenDirectoryChanged;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.objects.job.JobListener;
import com.sos.scheduler.model.LanguageDescriptor;
import com.sos.scheduler.model.objects.JSObjJob;
 
public class ScriptJobMainForm extends SOSJOEMessageCodes {

	@SuppressWarnings("unused")
	private final String				conClassName					= this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String			conSVNVersion					= "$Id$";
	@SuppressWarnings("unused")
	private final Logger				logger							= Logger.getLogger(this.getClass());

	private JobListener	objDataOptionsProvider = null;

	private Composite					tabItemOptionsComposite			= null;
	private Composite					tabItemDirChangedComposite		= null;
	private Composite					objTabControlComposite			= null;
	private Composite					tabItemEMailComposite			= null;
	private Composite					tabItemDocumentationComposite	= null;
	private Composite					tabItemOrderSetBackComposite	= null;
	private Composite					tabItemDelayAfterErrorComposite	= null;
	private Composite					tabItemSourceViewerComposite	= null;

	private JobMainComposite			jobMainComposite				= null;

	private Composite tabItemExecutableComposite = null;
	private CTabItem tabItemExecutable = null;
	
	private CTabItem					tabItemSourceViewer				= null;
	private CTabItem					tabItemEMail					= null;
	private CTabItem					tabItemDocumentation			= null;
	private CTabItem					tabItemOrderSetBack				= null;
	private CTabItem					tabItemDelayAfterError			= null;
	private CTabItem					tabItemOptions					= null;
	private CTabItem					tabItemDirChanged				= null;

	private JobOptions					objJobOptions					= null;

	private Group						objMainOptionsGroup1			= null;
	private  TreeData objTreeData = null;
	private boolean				init							= true;
	protected CTabFolder		tabFolder						= null;

//	public ScriptJobMainForm(final Composite parent, final int style, final SchedulerDom dom, final Element job, final ISchedulerUpdate main) {
//		super(parent, style, dom, job, main);
//		showWaitCursor();
//		objDataOptionsProvider = new JobOptionsListener(dom, job);
//		initialize();
//		restoreCursor();
//	}

	protected JobListener		objDataProvider					= null;

    public ScriptJobMainForm(final Composite parent,  final TreeData pobjTreeData) {
		super(parent, SWT.None);

        try {
			showWaitCursor();
			objTreeData = pobjTreeData;
			objDataOptionsProvider = new JobListener(pobjTreeData);
			objDataProvider = new JobListener (pobjTreeData);
//		objDataProvider._languages = JSObjJob.ValidLanguages4Job;

			initialize();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
        finally {
			restoreCursor();
        }
    }

	public void initForm() {
		jobMainComposite.init();
	}

	@Deprecated // use JSObjJob instead
	protected String[] getScriptLanguages() {
//		return LanguageDescriptorList.getLanguages4APIJobs();
	        return JSObjJob.ValidLanguages4Job;
}

	@Deprecated // use JSObjJob instead
	protected String getPredefinedFunctionNames() {
        return JSObjJob.InternalAPIMethodNames;
	}

	protected void initialize() {
		try {
			init = true;
			this.setLayout(new GridLayout());
			createGroup();
//			fillForm();
			setSize(new Point(723, 566));
			initForm();
			init = false;
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
		}
		finally {
		}
	}

//	protected LanguageSelector	languageSelector				= null;
	LanguageDescriptor	objLanguageDescriptor	= null;

	protected void fillForm() {
		init = true;
//		objLanguageDescriptor = objDataProvider.getLanguageDescriptor();
//		if (objJobScript.getCboPrefunction() != null) {
//			objJobScript.getCboPrefunction().removeAll();
//		}
//		languageSelector.selectLanguageItem(objLanguageDescriptor);
//		if (objLanguageDescriptor.getLanguageNumber() != JobListener.NONE) {
//			objDataProvider.fillIncludesTable(objJobIncludeFile.getTableIncludes());
//		}
//		else {
//			LanguageDescriptor objDefaultL = LanguageDescriptorList.getDefaultLanguage();
//			languageSelector.selectLanguageItem(objDefaultL);
//			objDataProvider.setLanguage(objDefaultL);
//		}
//		String lan = "";
//		if (!languageSelector.isShell() && !languageSelector.isJava()) {
//			lan = this.getPredefinedFunctionNames();
//			objJobScript.getCboPrefunction().setItems(lan.split(";"));
//		}
//		if (languageSelector.isJava() && languageSelector.isHiddenJavaAPIJob() == false) {
//			tabFolder.setSelection(tabItemJavaAPI);
//		}
//		else {
//			if (languageSelector.isHiddenJavaAPIJob() == true) {
//				//				tabItemJavaAPI.s
//			}
//			tabFolder.setSelection(tabItemScript);
//			objJobScript.gettSource().setFocus();
//		}
		init = false;
	}

	protected void createGroup() {
		try {
			this.setRedraw(false);
			GridLayout gridLayoutMainOptionsGroup = new GridLayout();
			gridLayoutMainOptionsGroup.numColumns = 1;
			objMainOptionsGroup1 = new Group(this, SWT.NONE);
			objMainOptionsGroup1.setRedraw(false);
			objMainOptionsGroup1.setText(objDataProvider.getJobNameAndTitle());

			objMainOptionsGroup1.setLayout(gridLayoutMainOptionsGroup);
			objMainOptionsGroup1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

			GridLayout gridLayout = new GridLayout();
			gridLayout.marginHeight = 1;
			gridLayout.numColumns = 1;

			jobMainComposite = new JobMainComposite(objMainOptionsGroup1, SWT.NONE, objDataProvider);
			//			jobMainComposite.setLayout(gridLayout);
			//			jobMainComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
//			createLanguageSelector(jobMainComposite.getgMain());
			createScriptTabForm(objMainOptionsGroup1);
//			createJobMainTabPages();
//			languageSelector.addModifyListener(new ModifyListener() {
//				@Override
//				public void modifyText(final ModifyEvent arg0) {
//
//					disposeJobMainTabPages();
//					createJobMainTabPages();
//				}
//			});
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
		}
		finally {
			objMainOptionsGroup1.setRedraw(true);
			this.setRedraw(true);
		}
	}

	protected void createScriptTabForm(final Group pobjMainOptionsGroup) {
		tabFolder = new CTabFolder(pobjMainOptionsGroup, SWT.None); // SWT.Bottom
		tabFolder.setLayout(new GridLayout());
		setResizableV(tabFolder);
		tabFolder.setSimple(true);
		tabFolder.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(final SelectionEvent e) {
				int intIndex = tabFolder.getSelectionIndex();
				Options.setLastTabItemIndex(intIndex);
				//                logger.debug("Selected item index = " + tabFolder.getSelectionIndex());
				logger.debug(JOE_M_ScriptForm_ItemIndex.params(tabFolder.getSelectionIndex()));
			}

			@Override public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}
		});
		createJobMainTabPages();
		tabFolder.setSelection(0);
	}

	private void doDispose(final Control pobjC) {
		if (pobjC != null) {
			pobjC.dispose();
		}
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
		if (tabItemExecutable != null) {
			tabItemExecutable.dispose();
		}
		if (tabItemDirChanged != null) {
			tabItemDirChanged.dispose();
		}

		if (tabItemDirChanged != null) {
			tabItemDirChanged.dispose();
		}
	}

	private void createJobMainTabPages() {
		tabItemExecutableComposite = null;
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

		tabItemExecutable = JOE_TI_ScriptJobMainForm_Executable.Control(new CTabItem(tabFolder, SWT.NONE));
		tabItemExecutableComposite = new Composite(tabFolder, SWT.NONE);
		tabItemExecutableComposite.setLayout(new GridLayout());
		setResizableV(tabItemExecutableComposite);
		tabItemExecutable.setControl(tabItemExecutableComposite);

		tabItemOptions = JOE_TI_ScriptJobMainForm_Options.Control(new CTabItem(tabFolder, SWT.NONE));
		tabItemOptionsComposite = new Composite(tabFolder, SWT.NONE);
		tabItemOptionsComposite.setLayout(new GridLayout());
		setResizableV(tabItemOptionsComposite);
		tabItemOptions.setControl(tabItemOptionsComposite);

		tabItemEMail = JOE_TI_ScriptJobMainForm_EMail.Control(new CTabItem(tabFolder, SWT.NONE));
		tabItemEMailComposite = new Composite(tabFolder, SWT.NONE);
		tabItemEMailComposite.setLayout(new GridLayout());
		setResizableV(tabItemEMailComposite);
		tabItemEMail.setControl(tabItemEMailComposite);

		if (objDataProvider.isOrderJob()) {
			tabItemOrderSetBack = JOE_TI_ScriptJobMainForm_SetBack.Control(new CTabItem(tabFolder, SWT.NONE));
			tabItemOrderSetBackComposite = new Composite(tabFolder, SWT.NONE);
			tabItemOrderSetBackComposite.setLayout(new GridLayout());
			setResizableV(tabItemOrderSetBackComposite);
			tabItemOrderSetBack.setControl(tabItemOrderSetBackComposite);
		}
		else {
			tabItemDelayAfterError = JOE_TI_ScriptJobMainForm_OnError.Control(new CTabItem(tabFolder, SWT.NONE));
			tabItemDelayAfterErrorComposite = new Composite(tabFolder, SWT.NONE);
			tabItemDelayAfterErrorComposite.setLayout(new GridLayout());
			setResizableV(tabItemDelayAfterErrorComposite);
			tabItemDelayAfterError.setControl(tabItemDelayAfterErrorComposite);

			tabItemDirChanged = JOE_TI_ScriptJobMainForm_FileWatcher.Control(new CTabItem(tabFolder, SWT.NONE));
			tabItemDirChangedComposite = new Composite(tabFolder, SWT.NONE);
			tabItemDirChangedComposite.setLayout(new GridLayout());
			setResizableV(tabItemDirChangedComposite);
			tabItemDirChanged.setControl(tabItemDirChangedComposite);
		}

		tabItemDocumentation = JOE_TI_ScriptJobMainForm_Doc.Control(new CTabItem(tabFolder, SWT.NONE));
		tabItemDocumentationComposite = new Composite(tabFolder, SWT.NONE);
		tabItemDocumentationComposite.setLayout(new GridLayout());
		setResizableV(tabItemDocumentationComposite);
		tabItemDocumentation.setControl(tabItemDocumentationComposite);

		tabItemSourceViewer = JOE_TI_ScriptJobMainForm_XML.Control(new CTabItem(tabFolder, SWT.NONE));
		tabItemSourceViewer.setData("type", "xml");

		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				CTabItem objSelectedItem = tabFolder.getSelection();
//				System.out.println(objSelectedItem.getText() + " selected");
				String strData = (String) objSelectedItem.getData("type");
				if (strData != null) {
					if (strData.equalsIgnoreCase("xml")) {
						tabItemSourceViewerComposite = createSourceViewerTab(tabItemSourceViewer, tabItemSourceViewerComposite);
					}
				}
			}
		});
		
		createExecutableTab(tabItemExecutableComposite);
		createSetbackTab(tabItemOrderSetBackComposite);
		createDelayAfterErrorTab(tabItemDelayAfterErrorComposite);
		createDirChangedTab(tabItemDirChangedComposite);

		createEmailSettingsTab(tabItemEMailComposite);
		createDocumentationTab(tabItemDocumentationComposite);
		createOptionsTab(tabItemOptionsComposite);
	}

	private Composite createSourceViewerTab(final CTabItem pobjTabItem, Composite pParentComposite) {
		if (pParentComposite != null) {
			pParentComposite.dispose();
		}

		pParentComposite = new Composite(tabFolder, SWT.NONE);
		pParentComposite.setLayout(new GridLayout());
		setResizableV(pParentComposite);
		pobjTabItem.setControl(pParentComposite);

		new JobSourceViewer(pParentComposite, objDataProvider);
		pParentComposite.layout();
		return pParentComposite;
	}

	private void createDocumentationTab(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}

		new JobDocumentation(pParentComposite, objDataProvider);
		pParentComposite.layout();
	}

	private void createExecutableTab(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}

		new ExecutableForm(pParentComposite, objTreeData);
		pParentComposite.layout();
	}

	private void createOptionsTab(final Composite pParentComposite) {
		objJobOptions = new JobOptions(pParentComposite, objDataProvider);
		objJobOptions.getbOrderYes().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (objJobOptions.getbOrderYes().getSelection() == false) {
					return;
				}
				disposeJobMainTabPages();
				createJobMainTabPages();
				tabFolder.setSelection(tabItemOptions);
			}

		});
		objJobOptions.getbOrderNo().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (objJobOptions.getbOrderNo().getSelection() == false) {
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

} // @jve:decl-index=0:visual-constraint="10,10"
