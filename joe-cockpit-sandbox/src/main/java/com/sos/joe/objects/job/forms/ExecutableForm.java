package com.sos.joe.objects.job.forms;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;

import sos.scheduler.editor.classes.LanguageSelector;
import sos.scheduler.editor.conf.container.JobIncludeFile;
import sos.scheduler.editor.conf.container.JobJavaAPI;
import sos.scheduler.editor.conf.container.JobProcessFile;
import sos.scheduler.editor.conf.container.JobScript;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.TreeData;
import com.sos.joe.globals.options.Options;
import com.sos.joe.objects.job.JobListener;
import com.sos.scheduler.model.LanguageDescriptor;
import com.sos.scheduler.model.LanguageDescriptorList;
import com.sos.scheduler.model.objects.JSObjJob;

public class ExecutableForm extends SOSJOEMessageCodes {
	@SuppressWarnings("unused")
	private final String		conClassName					= this.getClass().getSimpleName();
	private final Logger		logger							= Logger.getLogger(this.getClass());
	@SuppressWarnings("unused")
	private final String		conSVNVersion					= "$Id: ScriptForm.java 21297 2013-11-07 12:02:28Z ur $";
	private final int			intNoOfLabelColumns				= 2;
	protected JobListener		objDataProvider					= null;
	private final Cursor		objLastCursor					= null;
	private boolean				init							= true;
	protected Element			job;
	protected LanguageSelector	languageSelector				= null;
	private Composite			tabItemJavaAPIComposite			= null;
	private Composite			tabItemIncludedFilesComposite	= null;
	private Composite			objTabControlComposite			= null;
	private Composite			tabItemScriptComposite			= null;
	private CTabItem			tabItemScript					= null;
	private CTabItem			tabItemJavaAPI					= null;
	private CTabItem			tabItemIncludedFiles			= null;
	protected CTabFolder		tabFolder						= null;
	protected JobScript			objJobScript					= null;
	private JobJavaAPI			objJobJAPI						= null;
	private JobIncludeFile		objJobIncludeFile				= null;
	protected Group				objExecutableGroup				= null;
	private TreeData			objTreeData						= null;
	private Composite			objParent						= null;

	public ExecutableForm(final Composite parent, final TreeData pobjTreeData) {
		super(parent, SWT.None);
		objParent = parent;
		objDataProvider = new JobListener(pobjTreeData);
		objDataProvider._languages = JSObjJob.ValidLanguages4Job;
		objTreeData = pobjTreeData;
		initialize();
	}

	public void apply() {
	}

	public boolean isUnsaved() {
		return false;
	}

	protected void initialize() {
		try {
			init = true;
			this.setLayout(new GridLayout());
			createGroup();
			fillForm();
			init = false;
		}
		catch (Exception e) {
			new ErrorLog(e.getLocalizedMessage(), e);
		}
		finally {
		}
	}

	protected void createGroup() {
		GridLayout gridLayoutMainOptionsGroup = new GridLayout();
		gridLayoutMainOptionsGroup.numColumns = 1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 1;
		gridLayout.numColumns = 1;
		if (objDataProvider.getLanguageDescriptor() == null) {
			LanguageDescriptor objLD = LanguageDescriptorList.getDefaultLanguage4Monitor();
			objDataProvider.setLanguage(objLD);
			languageSelector.selectLanguageItem(objLD);
		}
		createScriptTabForm(objParent);
	}

	protected void disposeTabPages() {
		if (tabItemScript != null) {
			tabItemScript.dispose();
		}
		if (tabItemJavaAPI != null) {
			tabItemJavaAPI.dispose();
		}
		if (tabItemIncludedFiles != null) {
			tabItemIncludedFiles.dispose();
		}
		if (tabItemProcessFile != null) {
			tabItemProcessFile.dispose();
		}
	}

	protected void createScriptTabForm(final Composite pobjMainOptionsGroup) {
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
		createLanguageSelector(pobjMainOptionsGroup);
		createTabPages();
		tabFolder.setSelection(0);
	}
	private Composite	tabItemProcessFileComposite	= null;
	private CTabItem	tabItemProcessFile			= null;

	private void createTabPages() {
		tabItemJavaAPIComposite = null;
		tabItemIncludedFilesComposite = null;
		tabItemScriptComposite = null;
		tabItemProcessFileComposite = null;
		//
		objTabControlComposite = new Composite(tabFolder, SWT.NONE);
		objTabControlComposite.setLayout(new GridLayout());
		setResizableV(objTabControlComposite);
		//
		tabItemScript = JOE_ScriptForm_TabItemScript.Control(new CTabItem(tabFolder, SWT.NONE));
		tabItemScriptComposite = new Composite(tabFolder, SWT.NONE);
		tabItemScriptComposite.setLayout(new GridLayout());
		setResizableV(tabItemScriptComposite);
		tabItemScript.setControl(tabItemScriptComposite);
		//
		if (objDataProvider.isJava()) {
			tabItemJavaAPI = JOE_ScriptForm_TabItemJavaAPI.Control(new CTabItem(tabFolder, SWT.NONE));
			tabItemJavaAPIComposite = new Composite(tabFolder, SWT.NONE);
			tabItemJavaAPIComposite.setLayout(new GridLayout());
			setResizableV(tabItemJavaAPIComposite);
			tabItemJavaAPI.setControl(tabItemJavaAPIComposite);
		}
		tabItemIncludedFiles = JOE_ScriptForm_TabItemIncludes.Control(new CTabItem(tabFolder, SWT.NONE));
		tabItemIncludedFilesComposite = new Composite(tabFolder, SWT.NONE);
		tabItemIncludedFilesComposite.setLayout(new GridLayout());
		setResizableV(tabItemIncludedFilesComposite);
		tabItemIncludedFiles.setControl(tabItemIncludedFilesComposite);
		tabItemProcessFile = JOE_TI_ScriptJobMainForm_ProcessFile.Control(new CTabItem(tabFolder, SWT.NONE));
		tabItemProcessFileComposite = new Composite(tabFolder, SWT.NONE);
		tabItemProcessFileComposite.setLayout(new GridLayout());
		setResizableV(tabItemProcessFileComposite);
		tabItemProcessFile.setControl(tabItemProcessFileComposite);
		//
		createJavaAPITab(tabItemJavaAPIComposite);
		createTabItemIncludeFile(tabItemIncludedFilesComposite);
		createScriptTab(tabItemScriptComposite);
		createProcessFileTab(tabItemProcessFileComposite);
	}

	private void createProcessFileTab(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		new JobProcessFile(pParentComposite, objDataProvider);
		pParentComposite.layout();
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
		objJobJAPI = new JobJavaAPI(pParentComposite, objDataProvider, objJobJAPI);
		pParentComposite.layout();
	}

	private void createTabItemIncludeFile(final Composite pParentComposite) {
		if (pParentComposite == null) {
			return;
		}
		objJobIncludeFile = new JobIncludeFile(pParentComposite, objDataProvider, objJobIncludeFile);
		pParentComposite.layout();
	}

	protected void createLanguageSelector(final Composite pobjComposite) {
		Label labelLanguageSelector = JOE_L_ScriptForm_Language.Control(new Label(pobjComposite, SWT.NONE));
		labelLanguageSelector.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, intNoOfLabelColumns, 1));
		languageSelector = new LanguageSelector(pobjComposite, SWT.BORDER);
		languageSelector.setEditable(false);
		languageSelector.setVisibleItemCount(10);
		languageSelector.addModifyListener(new ModifyListener() {
			@Override public void modifyText(final ModifyEvent arg0) {
				if (objDataProvider != null && init == false) {
					LanguageDescriptor objLD = languageSelector.getLanguageDescriptor();
					objDataProvider.setLanguage(objLD);
					disposeTabPages();
					createTabPages();
					objJobScript.getCboPrefunction().setEnabled(false);
					if (languageSelector.isJava() && objJobJAPI.getTbxClassName() != null) {
						objJobJAPI.getTbxClassName().setFocus();
						objJobJAPI.getTClasspath().setText(objDataProvider.getClasspath());
						if (!objJobJAPI.getTbxClassName().getText().equals("") && objDataProvider.getJavaClass().equals(""))
							objDataProvider.setJavaClass(objJobJAPI.getTbxClassName().getText());
						objJobJAPI.getTbxClassName().setText(objDataProvider.getJavaClass());
						tabFolder.setSelection(tabItemJavaAPI);
					}
					else {
						//						String lan = "";
						if (languageSelector.isScriptLanguage()) {
							String lan = getPredefinedFunctionNames();
							objJobScript.getCboPrefunction().setItems(lan.split(";"));
							objJobScript.getCboPrefunction().setEnabled(true);
						}
						if (languageSelector.isJava()) {
							tabFolder.setSelection(tabItemJavaAPI);
						}
						else {
							tabFolder.setSelection(tabItemScript);
							objJobScript.gettSource().setFocus();
						}
					}
				}
			}
		});
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.minimumWidth = 100;
		languageSelector.setLayoutData(gd_combo);
		languageSelector.setItemTexts(LanguageDescriptorList.getComboItems4APIJobs());
		languageSelector.select(LanguageDescriptorList.conLanguageSHELL);
		//		languageSelector.setItems(getScriptLanguages());
	}
	LanguageDescriptor	objLanguageDescriptor	= null;

	protected void fillForm() {
		init = true;
		objLanguageDescriptor = objDataProvider.getLanguageDescriptor();
		if (objJobScript.getCboPrefunction() != null) {
			objJobScript.getCboPrefunction().removeAll();
		}
		languageSelector.selectLanguageItem(objLanguageDescriptor);
		if (objLanguageDescriptor.getLanguageNumber() != JobListener.NONE) {
			objDataProvider.fillIncludesTable(objJobIncludeFile.getTableIncludes());
		}
		else {
			LanguageDescriptor objDefaultL = LanguageDescriptorList.getDefaultLanguage();
			languageSelector.selectLanguageItem(objDefaultL);
			objDataProvider.setLanguage(objDefaultL);
		}
		String lan = "";
		if (!languageSelector.isShell() && !languageSelector.isJava()) {
			lan = this.getPredefinedFunctionNames();
			objJobScript.getCboPrefunction().setItems(lan.split(";"));
		}
		if (languageSelector.isJava() && languageSelector.isHiddenJavaAPIJob() == false) {
			tabFolder.setSelection(tabItemJavaAPI);
		}
		else {
			if (languageSelector.isHiddenJavaAPIJob() == true) {
				//				tabItemJavaAPI.s
			}
			tabFolder.setSelection(tabItemScript);
			objJobScript.gettSource().setFocus();
		}
		init = false;
	}

	public JobJavaAPI getObjJobJAPI() {
		return objJobJAPI;
	}

	public JobIncludeFile getObjJobIncludeFile() {
		return objJobIncludeFile;
	}

	public CTabItem getTabItemIncludedFiles() {
		return tabItemIncludedFiles;
	}

	protected String getPredefinedFunctionNames() {
		// TODO attribute of languageDescriptor
		return "spooler_task_before;spooler_task_after;spooler_process_before;spooler_process_after";
	}
}
