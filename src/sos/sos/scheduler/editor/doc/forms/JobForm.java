package sos.scheduler.editor.doc.forms;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.SourceGenerator;
import sos.scheduler.editor.doc.listeners.JobListener;

public class JobForm extends Composite implements IUpdateLanguage {
	private final static String	conSVNVersion	= "$Id$";
	private JobListener			listener		= null;
	private Group				group			= null;
	private Label				label			= null;
	private Text				tName			= null;
	private Label				label1			= null;
	private Text				tTitle			= null;
	private Label				label2			= null;
	private Label				label3			= null;
	private Combo				cOrder			= null;
	private Combo				cTasks			= null;
	private Combo cbJobType;
	private Text sourceOutputPath;
	private Text packageName;

	public JobForm(Composite parent, int style, DocumentationDom dom, Element job) {
		super(parent, style);
		initialize();
		setToolTipText();

		listener = new JobListener(dom, job);
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
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL; // Generated
		gridData1.grabExcessHorizontalSpace = true; // Generated
		gridData1.verticalAlignment = GridData.CENTER; // Generated
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL; // Generated
		gridData.grabExcessHorizontalSpace = true; // Generated
		gridData.verticalAlignment = GridData.CENTER; // Generated
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2; // Generated
		group = new Group(this, SWT.NONE);
		group.setText("Job"); // Generated
		group.setLayout(gridLayout); // Generated
		
		label = new Label(group, SWT.NONE);
		label.setText("Name:"); // Generated
		tName = new Text(group, SWT.BORDER);
		tName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tName.selectAll();
			}
		});
		tName.setLayoutData(gridData); // Generated
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setName(tName.getText());
				Utils.setBackground(tName, true);
			}
		});
		label1 = new Label(group, SWT.NONE);
		label1.setText("Title:"); // Generated
		tTitle = new Text(group, SWT.BORDER);
		tTitle.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tTitle.selectAll();
			}
		});
		tTitle.setLayoutData(gridData1); // Generated
		tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setTitle(tTitle.getText());
				Utils.setBackground(tTitle, true);
			}
		});
		label2 = new Label(group, SWT.NONE);
		label2.setText("Order:"); // Generated
		createCOrder();
		label3 = new Label(group, SWT.NONE);
		label3.setText("Tasks:");
		cTasks = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		cTasks.setLayoutData(new GridData(200, SWT.DEFAULT)); // Generated
		cTasks.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setTasks(cTasks.getText());
			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		new Label(group, SWT.NONE);

		final Button vorschauButton = new Button(group, SWT.NONE);
		vorschauButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.preview();
			}
		});
		vorschauButton.setText("Preview");
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		
		Label lblO = new Label(group, SWT.NONE);
		lblO.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblO.setText("Source Output Path");
		
		sourceOutputPath = new Text(group, SWT.BORDER);
		sourceOutputPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPackageName = new Label(group, SWT.NONE);
		lblPackageName.setText("Package Name");
		
		packageName = new Text(group, SWT.BORDER);
		packageName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblJobType = new Label(group, SWT.NONE);
		lblJobType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJobType.setText("Job Type");
		
		cbJobType = new Combo(group, SWT.NONE);
		cbJobType.setItems(new String[] {"Job in a Job Chain", "Standalone Job"});
		GridData gd_cbJobType = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_cbJobType.widthHint = 190;
		cbJobType.setLayoutData(gd_cbJobType);
		cbJobType.setText("Standalone Job");
		new Label(group, SWT.NONE);
		
		Button btnNewButton = new Button(group, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
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
				    if ((cOrder.getText().equalsIgnoreCase("no")) && cbJobType.getText().equalsIgnoreCase("Standalone Job")) {
				      s.setStandAlone(true);
				    }else {
					  s.setStandAlone(false);
				    }

				    s.execute();
				}catch (IOException e) {
					e.printStackTrace();
				}
				catch (JDOMException e) {
					e.printStackTrace();
				}
			 
			}
		});
		btnNewButton.setText("Generate Java Source");
		createCTasks();
	}

	/**
	 * This method initializes cOrder
	 */
	private void createCOrder() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.BEGINNING; // Generated
		gridData2.widthHint = 200; // Generated
		gridData2.verticalAlignment = GridData.CENTER; // Generated
		cOrder = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		cOrder.setLayoutData(gridData2); // Generated
		cOrder.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setOrder(cOrder.getText());
			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	/**
	 * This method initializes cTasks
	 */
	private void createCTasks() {
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

	public void setToolTipText() {
		tName.setToolTipText(Messages.getTooltip("doc.job.name"));
		tTitle.setToolTipText(Messages.getTooltip("doc.job.title"));
		cOrder.setToolTipText(Messages.getTooltip("doc.job.order"));
		cTasks.setToolTipText(Messages.getTooltip("doc.job.tasks"));
	}

} // @jve:decl-index=0:visual-constraint="10,10"
