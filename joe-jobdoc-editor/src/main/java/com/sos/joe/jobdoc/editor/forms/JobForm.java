package com.sos.joe.jobdoc.editor.forms;
import static com.sos.dialog.Globals.MsgHandler;

import java.io.File;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.JSHelper.Options.SOSOptionFileName;
import com.sos.JSHelper.io.Files.JSFolder;
import com.sos.dialog.components.SOSFileNameSelector;
import com.sos.dialog.components.SOSPreferenceStoreText;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.SourceGenerator;
import com.sos.joe.jobdoc.editor.listeners.JobDocJobListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class JobForm extends JobDocBaseForm<JobDocJobListener>/* SOSJOEMessageCodes  implements IUpdateLanguage */ {
	private final String									conClassName				= "JobForm";
	@SuppressWarnings("unused") private final static String	conSVNVersion				= "$Id: JobForm.java 25898 2014-06-20 14:36:54Z kb $";
	private JobDocJobListener								objJobDocJobDataProvider	= null;
	private Group											group						= null;
	@SuppressWarnings("unused") private Label				label						= null;
	private SOSPreferenceStoreText							tbxJavaClassName			= null;
	@SuppressWarnings("unused") private Label				label1						= null;
	private Text											tTitle						= null;
	@SuppressWarnings("unused") private Label				label2						= null;
	@SuppressWarnings("unused") private Label				label3						= null;
	private Combo											cOrder						= null;
	private Combo											cTasks						= null;
	private Combo											cbJobType;
	private SOSFileNameSelector								tbxSourceOutputPathName;
	private SOSPreferenceStoreText							tbxJavaPackageName;

	public JobForm(final Composite parent, final int style, final DocumentationDom objDom, final Element job) {
		super(parent, style);
		dom = objDom;
		objJobDocJobDataProvider = new JobDocJobListener(dom, job);
		listener = objJobDocJobDataProvider;
		initialize();
		setToolTipText();
		initValues();
	}

	private void initialize() {
		createGroup();
		setSize(new Point(696, 462));
		setLayout(new FillLayout());
	}

	private void createGroup() {
		SOSOptionFileName objJobDocFileName = new SOSOptionFileName(objJobDocJobDataProvider.getDom().getFilename());
		String strJobDocFileName = "nn";
		if (objJobDocFileName.IsNotEmpty()) {
			strJobDocFileName = objJobDocFileName.JSFile().getName();
		}
		String strPrefKey = conClassName + "/" + strJobDocFileName + "/";
//		GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		GridLayout gridLayout = new GridLayout(2, false);
		group = MsgHandler.newMsg("JOE_G_JobForm_Job").Control(new Group(this, SWT.NONE));
		group.setLayout(gridLayout);
		label = MsgHandler.newMsg("JOE_L_Name").Control(new Label(group, SWT.NONE));
		tbxJavaClassName = (SOSPreferenceStoreText) MsgHandler.newMsg("JOE_T_JobForm_Name").Control(new SOSPreferenceStoreText(group, SWT.BORDER));
		tbxJavaClassName.setLayoutData(gridData);
		tbxJavaClassName.setPreferenceStoreKey(strPrefKey + "tbxJavaClassName");
		tbxJavaClassName.addModifyListener(new ModifyListener() {
			@Override public void modifyText(final ModifyEvent e) {
				objJobDocJobDataProvider.setName(tbxJavaClassName.getText());
				Utils.setBackground(tbxJavaClassName, true);
			}
		});
		label1 = MsgHandler.newMsg("JOE_L_JobForm_Title").Control(new Label(group, SWT.NONE));
		tTitle = MsgHandler.newMsg("JOE_T_JobForm_Title").Control(new Text(group, SWT.BORDER));
		tTitle.setLayoutData(gridData); // Generated
		tTitle.addModifyListener(new ModifyListener() {
			@Override public void modifyText(final ModifyEvent e) {
				objJobDocJobDataProvider.setTitle(tTitle.getText());
				Utils.setBackground(tTitle, true);
			}
		});
		label2 = MsgHandler.newMsg("JOE_L_JobForm_Order").Control(new Label(group, SWT.NONE));
		createCOrder();
		label3 = MsgHandler.newMsg("JOE_L_JobForm_Tasks").Control(new Label(group, SWT.NONE));
		cTasks = MsgHandler.newMsg("JOE_Cbo_JobForm_Tasks").Control(new Combo(group, SWT.BORDER | SWT.READ_ONLY));
		cTasks.setLayoutData(new GridData(200, SWT.DEFAULT));
		cTasks.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(final SelectionEvent e) {
				objJobDocJobDataProvider.setTasks(cTasks.getText());
			}

			@Override public void widgetDefaultSelected(final SelectionEvent e) {
				//
			}
		});
		new Label(group, SWT.NONE);
		final Button vorschauButton = MsgHandler.newMsg("JOE_B_JobForm_Preview").Control(new Button(group, SWT.NONE));
		vorschauButton.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent e) {
				objJobDocJobDataProvider.preview();
			}
		});
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		Label lblO = MsgHandler.newMsg("JOE_L_JobForm_OutputPath").Control(new Label(group, SWT.NONE));
		lblO.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		tbxSourceOutputPathName = (SOSFileNameSelector) MsgHandler.newMsg("JOE_T_JobForm_OutputPath").Control(new SOSFileNameSelector(group, SWT.BORDER));
		tbxSourceOutputPathName.setPreferenceStoreKey(strPrefKey + "tbxSourceOutputPathName");
		tbxSourceOutputPathName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		@SuppressWarnings("unused") Label lblPackageName = MsgHandler.newMsg("JOE_L_JobForm_PackageName").Control(new Label(group, SWT.NONE));
		tbxJavaPackageName = (SOSPreferenceStoreText) MsgHandler.newMsg("JOE_T_JobForm_PackageName").Control(new SOSPreferenceStoreText(group, SWT.BORDER));
		tbxJavaPackageName.setPreferenceStoreKey(strPrefKey + "tbxJavaPackageName");
		tbxJavaPackageName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblJobType = MsgHandler.newMsg("JOE_L_JobForm_JobType").Control(new Label(group, SWT.NONE));
		lblJobType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		cbJobType = MsgHandler.newMsg("JOE_Cbo_JobForm_JobType").Control(new Combo(group, SWT.NONE));
		cbJobType.setItems(new String[] { "Job in a Job Chain", "Standalone Job" });
		GridData gd_cbJobType = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_cbJobType.widthHint = 190;
		cbJobType.setLayoutData(gd_cbJobType);
		// cbJobType.setText("Standalone Job");
		cbJobType.setText(cbJobType.getItem(1));
		new Label(group, SWT.NONE);
		Button btnNewButton = MsgHandler.newMsg("JOE_B_JobForm_GenerateSource").Control(new Button(group, SWT.NONE));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent arg0) {
				generateJavaSource();
			}
		});
	}

	protected void generateJavaSource() {
		SourceGenerator objSourceGenerator = new SourceGenerator();
		File objJobDocFileName = null;
		try {
			objJobDocFileName = objJobDocJobDataProvider.writeToFile();
			// TODO Option language
			objSourceGenerator.setDefaultLang("en");
			objSourceGenerator.setJavaClassName(tbxJavaClassName.getText());
			objSourceGenerator.setJobdocFile(objJobDocFileName);
			String strOutPath = tbxSourceOutputPathName.getText().trim();
			if (strOutPath.length() <= 0) {
				File tmp = File.createTempFile(Options.getXSLTFilePrefix(), Options.getXSLTFileSuffix());
				tmp.deleteOnExit();
				strOutPath = tmp.getParent();
				tbxSourceOutputPathName.setText(strOutPath);
			}
			JSFolder objOutputFolder = new JSFolder(strOutPath);
			objOutputFolder.CheckFolder(true);
			objSourceGenerator.setOutputDir(objOutputFolder);
			objSourceGenerator.setPackageName(tbxJavaPackageName.getText());
			// TODO Migrate Template path to JOE_HOME Folder
			//			File f = new File(Options.getSchedulerData(), "config/JOETemplates/java/xsl");
			//			objSourceGenerator.setTemplatePath(f);
			//			if (!f.exists()) {
			//				MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			//				mb.setMessage(JOE_M_FileNotFound.params(f.getAbsolutePath()));
			//				mb.open();
			//			}
			//			else {
			if (cOrder.getText().equalsIgnoreCase("no") && cbJobType.getText().equalsIgnoreCase("Standalone Job")) {
				objSourceGenerator.setStandAlone(true);
			}
			else {
				objSourceGenerator.setStandAlone(false);
			}
			objSourceGenerator.execute();
			MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			mb.setMessage(String.format("sources generated. Path is %1$s", strOutPath));
			mb.open();
			//			}
		}
		catch (Exception e) {
			new ErrorLog("Problems during source generation", e);
		}
	}

	private void createCOrder() {
		GridData gridData2 = new GridData(GridData.BEGINNING, GridData.CENTER);
		gridData2.widthHint = 200;
		cOrder = MsgHandler.newMsg("JOE_Cbo_JobForm_Order").Control(new Combo(group, SWT.BORDER | SWT.READ_ONLY));
		cOrder.setLayoutData(gridData2); // Generated
		cOrder.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(final SelectionEvent e) {
				objJobDocJobDataProvider.setOrder(cOrder.getText());
			}

			@Override public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
	}

	private void initValues() {
		tbxJavaClassName.setText(objJobDocJobDataProvider.getName());
		tTitle.setText(objJobDocJobDataProvider.getTitle());
		cOrder.setItems(objJobDocJobDataProvider.getOrderValues());
		cOrder.select(cOrder.indexOf(objJobDocJobDataProvider.getOrder()));
		cTasks.setItems(objJobDocJobDataProvider.getTasksValues());
		cTasks.select(cTasks.indexOf(objJobDocJobDataProvider.getTasks()));
		tbxJavaPackageName.initialize();
		tbxJavaClassName.initialize();
		tbxSourceOutputPathName.initialize();
		tbxJavaClassName.setFocus();
	}

	@Override public void openBlank() {
		
	}

	@Override public void updateLanguage() {
		
	}

	@Override protected void applySetting() {
		
	}

	@Override public boolean applyChanges() {
		return false;
	}

	@Override public boolean open(Collection files) {
		return false;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
