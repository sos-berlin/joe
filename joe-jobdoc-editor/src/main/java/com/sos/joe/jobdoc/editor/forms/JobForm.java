package com.sos.joe.jobdoc.editor.forms;
import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.jdom.JDOMException;

import com.sos.joe.xml.Utils;

import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.SourceGenerator;
import com.sos.joe.jobdoc.editor.listeners.JobDocJobListener;
import com.sos.joe.xml.jobdoc.DocumentationDom;
 
public class JobForm extends SOSJOEMessageCodes implements IUpdateLanguage {
	@SuppressWarnings("unused") private final static String	conSVNVersion	= "$Id: JobForm.java 25898 2014-06-20 14:36:54Z kb $";
	private JobDocJobListener										listener		= null;
	private Group											group			= null;
	@SuppressWarnings("unused") private Label				label			= null;
	private Text											tName			= null;
	@SuppressWarnings("unused") private Label				label1			= null;
	private Text											tTitle			= null;
	@SuppressWarnings("unused") private Label				label2			= null;
	@SuppressWarnings("unused") private Label				label3			= null;
	private Combo											cOrder			= null;
	private Combo											cTasks			= null;
	private Combo											cbJobType;
	private Text											sourceOutputPath;
	private Text											packageName;

	public JobForm(final Composite parent, final int style, final DocumentationDom dom, final Element job) {
		super(parent, style);
		initialize();
		setToolTipText();
		listener = new JobDocJobListener(dom, job);
		initValues();
	}

	private void initialize() {
		createGroup();
		setSize(new Point(696, 462));
		setLayout(new FillLayout());
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		GridLayout gridLayout = new GridLayout(2, false);
		group = JOE_G_JobForm_Job.Control(new Group(this, SWT.NONE));
		group.setLayout(gridLayout);
		label = JOE_L_Name.Control(new Label(group, SWT.NONE));
		tName = JOE_T_JobForm_Name.Control(new Text(group, SWT.BORDER));
		tName.setLayoutData(gridData); // Generated
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			@Override public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
				listener.setName(tName.getText());
				Utils.setBackground(tName, true);
			}
		});
		label1 = JOE_L_JobForm_Title.Control(new Label(group, SWT.NONE));
		tTitle = JOE_T_JobForm_Title.Control(new Text(group, SWT.BORDER));
		tTitle.setLayoutData(gridData1); // Generated
		tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			@Override public void modifyText(final org.eclipse.swt.events.ModifyEvent e) {
				listener.setTitle(tTitle.getText());
				Utils.setBackground(tTitle, true);
			}
		});
		label2 = JOE_L_JobForm_Order.Control(new Label(group, SWT.NONE));
		createCOrder();
		label3 = JOE_L_JobForm_Tasks.Control(new Label(group, SWT.NONE));
		cTasks = JOE_Cbo_JobForm_Tasks.Control(new Combo(group, SWT.BORDER | SWT.READ_ONLY));
		cTasks.setLayoutData(new GridData(200, SWT.DEFAULT)); // Generated
		cTasks.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				listener.setTasks(cTasks.getText());
			}

			@Override public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
				//
			}
		});
		new Label(group, SWT.NONE);
		final Button vorschauButton = JOE_B_JobForm_Preview.Control(new Button(group, SWT.NONE));
		vorschauButton.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent e) {
				listener.preview();
			}
		});
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		Label lblO = JOE_L_JobForm_OutputPath.Control(new Label(group, SWT.NONE));
		lblO.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		sourceOutputPath = JOE_T_JobForm_OutputPath.Control(new Text(group, SWT.BORDER));
		sourceOutputPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		@SuppressWarnings("unused") Label lblPackageName = JOE_L_JobForm_PackageName.Control(new Label(group, SWT.NONE));
		packageName = JOE_T_JobForm_PackageName.Control(new Text(group, SWT.BORDER));
		packageName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblJobType = JOE_L_JobForm_JobType.Control(new Label(group, SWT.NONE));
		lblJobType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		cbJobType = JOE_Cbo_JobForm_JobType.Control(new Combo(group, SWT.NONE));
		cbJobType.setItems(new String[] { "Job in a Job Chain", "Standalone Job" });
		GridData gd_cbJobType = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_cbJobType.widthHint = 190;
		cbJobType.setLayoutData(gd_cbJobType);
		// cbJobType.setText("Standalone Job");
		cbJobType.setText(cbJobType.getItem(1));
		new Label(group, SWT.NONE);
		Button btnNewButton = JOE_B_JobForm_GenerateSource.Control(new Button(group, SWT.NONE));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(final SelectionEvent arg0) {
				generateJavaSource();
			}
		});
		createCTasks();
	}

	protected void generateJavaSource() {
		SourceGenerator s = new SourceGenerator();
		File documentation;
		try {
			documentation = listener.writeToFile();
			s.setDefaultLang("en");
			s.setJavaClassName(tName.getText());
			s.setJobdocFile(documentation);
			if (sourceOutputPath.getText().trim().equals("")) {
				File tmp = File.createTempFile(Options.getXSLTFilePrefix(), Options.getXSLTFileSuffix());
				tmp.deleteOnExit();
				sourceOutputPath.setText(tmp.getParent());
			}
			s.setOutputDir(new File(sourceOutputPath.getText()));
			s.setPackageName(packageName.getText());
			// TODO Migrate Template path to JOE_HOME Folder
			File f = new File(Options.getSchedulerData(), "config/JOETemplates/java/xsl");
			s.setTemplatePath(f);
			if (!f.exists()) {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION);
				mb.setMessage(JOE_M_FileNotFound.params(f.getAbsolutePath()));
				mb.open();
			}
			else {
				if (cOrder.getText().equalsIgnoreCase("no") && cbJobType.getText().equalsIgnoreCase("Standalone Job")) {
					s.setStandAlone(true);
				}
				else {
					s.setStandAlone(false);
				}
				s.execute();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes cOrder
	 */
	private void createCOrder() {
		GridData gridData2 = new GridData(GridData.BEGINNING, GridData.CENTER);
		gridData2.widthHint = 200;
		cOrder = JOE_Cbo_JobForm_Order.Control(new Combo(group, SWT.BORDER | SWT.READ_ONLY));
		cOrder.setLayoutData(gridData2); // Generated
		cOrder.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				listener.setOrder(cOrder.getText());
			}

			@Override public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	/**
	 * This method initializes cTasks
	 */
	private void createCTasks() {
		//
	}

	private void initValues() {
		tName.setText(listener.getName());
		tTitle.setText(listener.getTitle());
		cOrder.setItems(listener.getOrderValues());
		cOrder.select(cOrder.indexOf(listener.getOrder()));
		cTasks.setItems(listener.getTasksValues());
		cTasks.select(cTasks.indexOf(listener.getTasks()));
		tName.setFocus();
	}

	@Override public void setToolTipText() {
		//
	}
} // @jve:decl-index=0:visual-constraint="10,10"
