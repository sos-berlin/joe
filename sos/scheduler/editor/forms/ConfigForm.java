/**
 * 
 */
package sos.scheduler.editor.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.listeners.ConfigListener;

/**
 * @author sky2000
 * 
 */
public class ConfigForm extends Composite {

	private ConfigListener listener;

	private Group gConfig = null;

	private Label label = null;

	private Text tSpoolerID = null;

	private Label label7 = null;

	private Text tParameter = null;

	private Label label10 = null;

	private Text tIncludePath = null;

	private Label label11 = null;

	private Text tLogDir = null;

	private Label label12 = null;

	private Text tMailXSLTStylesheet = null;

	private Label label13 = null;

	private Spinner sMaxPriority = null;

	private Group gPorts = null;

	private Button cSamePorts = null;

	private Label label14 = null;

	private Spinner sPort = null;

	private Label label3 = null;

	private Spinner sTcpPort = null;

	private Label label4 = null;

	private Spinner sUdpPort = null;

	private Group gMainScheduler = null;

	private Button cUseMainScheduler = null;

	private Label label1 = null;

	private Text tMainSchedulerHost = null;

	private Label label2 = null;

	private Spinner sMainSchedulerPort = null;

	private Group gJavaOptions = null;

	private Label label8 = null;

	private Text tJavaClassPath = null;

	private Label label9 = null;

	private Text tJavaOptions = null;

	private Group group = null;

	private Text tComment = null;

	/**
	 * @param parent
	 * @param style
	 */
	public ConfigForm(Composite parent, int style, DomParser dom) {
		super(parent, style);
		listener = new ConfigListener(dom);
		initialize();
		
		tSpoolerID.setFocus();
	}
	
	private void initialize() {
		this.setLayout(new FillLayout());
		createGConfig();
		setSize(new org.eclipse.swt.graphics.Point(714,501));

		// set all values
		listener.getDom().setInit(true);
		tSpoolerID.setText(listener.getSpoolerID());
		tParameter.setText(listener.getParam());
		tIncludePath.setText(listener.getIncludePath());
		tLogDir.setText(listener.getLogDir());
		tMailXSLTStylesheet.setText(listener.getMailXSLTStylesheet());
		sMaxPriority.setSelection(listener.getPriorityMax());
		cSamePorts.setSelection(listener.isPort());
		if (listener.isPort())
			sPort.setSelection(listener.getPort());
		sTcpPort.setSelection(listener.getTcpPort());
		sUdpPort.setSelection(listener.getUdpPort());
		cUseMainScheduler.setSelection(listener.isMainScheduler());

		int port = listener.getMainSchedulerPort();
		tMainSchedulerHost.setText(listener.getMainSchedulerHost());
		sMainSchedulerPort.setSelection(port);

		tJavaClassPath.setText(listener.getJavaClasspath());
		tJavaOptions.setText(listener.getJavaOptions());

		tComment.setText(listener.getComment());

		setEditable();
		listener.getDom().setInit(false);
	}

	private void setEditable() {
		tMainSchedulerHost.setEnabled(cUseMainScheduler.getSelection());
		sMainSchedulerPort.setEnabled(cUseMainScheduler.getSelection());

		sPort.setEnabled(cSamePorts.getSelection());
		sTcpPort.setEnabled(!cSamePorts.getSelection());
		sUdpPort.setEnabled(!cSamePorts.getSelection());
	}

	/**
	 * This method initializes gConfig
	 * 
	 */
	private void createGConfig() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData71 = new GridData();
		gridData71.widthHint = 60;
		GridData gridData51 = new GridData();
		gridData51.horizontalAlignment = GridData.FILL;
		gridData51.verticalAlignment = GridData.CENTER;
		GridData gridData41 = new GridData();
		gridData41.horizontalAlignment = GridData.FILL;
		gridData41.verticalAlignment = GridData.CENTER;
		GridData gridData31 = new GridData();
		gridData31.horizontalAlignment = GridData.FILL;
		gridData31.verticalAlignment = GridData.CENTER;
		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.FILL;
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.verticalAlignment = GridData.CENTER;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		gConfig = new Group(this, SWT.NONE);
		gConfig.setText("Config");
		gConfig.setLayout(gridLayout1);
		gConfig.setSize(new Point(798, 516));
		label = new Label(gConfig, SWT.NONE);
		label.setText("Scheduler ID:");
		tSpoolerID = new Text(gConfig, SWT.BORDER);
		tSpoolerID.setToolTipText(Messages.getTooltip("config.spooler_id"));
		tSpoolerID.setLayoutData(gridData1);
		tSpoolerID
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setSpoolerID(tSpoolerID.getText());
					}
				});
		label7 = new Label(gConfig, SWT.NONE);
		label7.setText("Parameter:");
		tParameter = new Text(gConfig, SWT.BORDER);
		tParameter.setToolTipText(Messages.getTooltip("config.param"));
		tParameter.setLayoutData(gridData21);
		tParameter
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setParam(tParameter.getText());
					}
				});
		label10 = new Label(gConfig, SWT.NONE);
		label10.setText("Include Path:");
		tIncludePath = new Text(gConfig, SWT.BORDER);
		tIncludePath.setToolTipText(Messages.getTooltip("config.include_path"));
		tIncludePath.setLayoutData(gridData31);
		tIncludePath
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setIncludePath(tIncludePath.getText());
					}
				});
		label11 = new Label(gConfig, SWT.NONE);
		label11.setText("Log Dir:");
		tLogDir = new Text(gConfig, SWT.BORDER);
		tLogDir.setToolTipText(Messages.getTooltip("config.log_dir"));
		tLogDir.setLayoutData(gridData41);
		tLogDir.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setLogDir(tLogDir.getText());
			}
		});
		label12 = new Label(gConfig, SWT.NONE);
		label12.setText("Mail XSLT Stylesheet:");
		tMailXSLTStylesheet = new Text(gConfig, SWT.BORDER);
		tMailXSLTStylesheet.setToolTipText(Messages
				.getTooltip("config.mail_xslt_stylesheet"));
		tMailXSLTStylesheet.setLayoutData(gridData51);
		tMailXSLTStylesheet
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setMailXSLTStylesheet(tMailXSLTStylesheet
								.getText());
					}
				});
		label13 = new Label(gConfig, SWT.NONE);
		label13.setText("Max Priority:");
		sMaxPriority = new Spinner(gConfig, SWT.NONE);
		sMaxPriority.setMaximum(99999999);
		sMaxPriority.setToolTipText(Messages.getTooltip("config.priority_max"));
		sMaxPriority.setLayoutData(gridData71);
		sMaxPriority.addModifyListener(new ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setPriorityMax(sMaxPriority.getSelection());
			}
		});
		createGPorts();
		createGMainScheduler();
		createGJavaOptions();
		createGroup();
	}

	/**
	 * This method initializes gPorts
	 * 
	 */
	private void createGPorts() {
		GridData gridData42 = new GridData();
		gridData42.horizontalSpan = 2;
		gridData42.verticalAlignment = GridData.CENTER;
		gridData42.grabExcessHorizontalSpace = true;
		gridData42.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 6;
		GridData gridData8 = new GridData();
		gridData8.grabExcessHorizontalSpace = false;
		gridData8.widthHint = 60;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.BEGINNING;
		gridData5.grabExcessHorizontalSpace = false;
		gridData5.widthHint = 60;
		gridData5.verticalAlignment = GridData.CENTER;
		GridData gridData7 = new GridData();
		gridData7.grabExcessHorizontalSpace = false;
		gridData7.widthHint = 60;
		GridData gridData3 = new GridData();
		gridData3.horizontalSpan = 6;
		GridLayout gridLayout11 = new GridLayout();
		gridLayout11.numColumns = 6;
		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = GridData.FILL;
		gridData12.horizontalSpan = 2;
		gridData12.verticalAlignment = GridData.CENTER;
		gPorts = new Group(gConfig, SWT.NONE);
		gPorts.setText("Job Scheduler Port");
		gPorts.setLayoutData(gridData12);
		gPorts.setLayout(gridLayout11);
		cSamePorts = new Button(gPorts, SWT.CHECK);
		cSamePorts.setText("Use the same port for udp and tcp");
		cSamePorts.setToolTipText(Messages.getTooltip("config.use_same_port"));
		cSamePorts.setLayoutData(gridData3);
		cSamePorts
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						setEditable();
					}
				});
		label14 = new Label(gPorts, SWT.NONE);
		label14.setText("Port:");
		sPort = new Spinner(gPorts, SWT.NONE);
		sPort.setDigits(0);
		sPort.setMaximum(99000);
		sPort.setToolTipText(Messages.getTooltip("config.port"));
		sPort.setLayoutData(gridData7);
		sPort.addModifyListener(new ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setPort(sPort.getSelection());
			}
		});
		label3 = new Label(gPorts, SWT.NONE);
		label3.setText("TCP:");
		sTcpPort = new Spinner(gPorts, SWT.NONE);
		sTcpPort.setMaximum(99000);
		sTcpPort.setToolTipText(Messages.getTooltip("config.tcp_port"));
		sTcpPort.setLayoutData(gridData5);
		sTcpPort.addModifyListener(new ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setTcpPort(sTcpPort.getSelection());
			}
		});
		label4 = new Label(gPorts, SWT.NONE);
		label4.setText("UDP:");
		sUdpPort = new Spinner(gPorts, SWT.NONE);
		sUdpPort.setMaximum(99000);
		sUdpPort.setToolTipText(Messages.getTooltip("config.udp_port"));
		sUdpPort.setLayoutData(gridData8);
		sUdpPort.addModifyListener(new ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setUdpPort(sUdpPort.getSelection());
			}
		});
	}

	/**
	 * This method initializes gMainScheduler
	 * 
	 */
	private void createGMainScheduler() {
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.widthHint = 200;
		GridLayout gridLayout7 = new GridLayout();
		gridLayout7.numColumns = 4;
		GridData gridData18 = new GridData();
		gridData18.horizontalSpan = 2;
		gridData18.verticalAlignment = GridData.CENTER;
		gridData18.grabExcessHorizontalSpace = true;
		gridData18.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 4;
		GridData gridData2 = new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1);
		GridData gridData11 = new GridData();
		gridData11.widthHint = 60;
		GridData gridData9 = new GridData();
		gridData9.horizontalSpan = 4;
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 4;
		GridData gridData13 = new GridData();
		gridData13.horizontalAlignment = GridData.FILL;
		gridData13.horizontalSpan = 2;
		gridData13.verticalAlignment = GridData.CENTER;
		gMainScheduler = new Group(gConfig, SWT.NONE);
		gMainScheduler.setText("Main Job Scheduler");
		gMainScheduler.setLayout(gridLayout7);
		gMainScheduler.setLayoutData(gridData13);
		gMainScheduler.setLayout(gridLayout4);
		cUseMainScheduler = new Button(gMainScheduler, SWT.CHECK);
		cUseMainScheduler.setText("use a main scheduler");
		cUseMainScheduler.setToolTipText(Messages
				.getTooltip("config.use_main_scheduler"));
		cUseMainScheduler.setLayoutData(gridData2);
		cUseMainScheduler
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						setEditable();
					}
				});
		cUseMainScheduler.setLayoutData(gridData9);
		label1 = new Label(gMainScheduler, SWT.NONE);
		label1.setText("Hostname:");
		tMainSchedulerHost = new Text(gMainScheduler, SWT.BORDER);
		tMainSchedulerHost.setToolTipText(Messages
				.getTooltip("config.main_scheduler_host"));
		tMainSchedulerHost.setLayoutData(gridData6);
		tMainSchedulerHost
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setMainScheduler(tMainSchedulerHost.getText()
								+ ":" + sMainSchedulerPort.getSelection());
					}
				});
		label2 = new Label(gMainScheduler, SWT.NONE);
		label2.setText("Port:");
		sMainSchedulerPort = new Spinner(gMainScheduler, SWT.NONE);
		sMainSchedulerPort.setMaximum(99000);
		sMainSchedulerPort.setToolTipText(Messages
				.getTooltip("config.main_scheduler_port"));
		sMainSchedulerPort.setLayoutData(gridData11);
		sMainSchedulerPort.addModifyListener(new ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setMainScheduler(tMainSchedulerHost.getText() + ":"
						+ sMainSchedulerPort.getSelection());
			}
		});
	}

	/**
	 * This method initializes gJavaOptions
	 * 
	 */
	private void createGJavaOptions() {
		GridData gridData24 = new GridData();
		gridData24.horizontalAlignment = GridData.FILL;
		gridData24.grabExcessHorizontalSpace = true;
		gridData24.verticalAlignment = GridData.CENTER;
		GridData gridData22 = new GridData();
		gridData22.horizontalAlignment = GridData.FILL;
		gridData22.grabExcessHorizontalSpace = true;
		gridData22.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout8 = new GridLayout();
		gridLayout8.numColumns = 2;
		GridData gridData20 = new GridData();
		gridData20.horizontalSpan = 2;
		gridData20.verticalAlignment = GridData.CENTER;
		gridData20.grabExcessHorizontalSpace = true;
		gridData20.horizontalAlignment = GridData.FILL;
		GridData gridData19 = new GridData();
		gridData19.horizontalAlignment = GridData.FILL;
		gridData19.verticalAlignment = GridData.CENTER;
		gridData19.horizontalSpan = 2;
		gridData19.grabExcessHorizontalSpace = true;
		GridLayout gridLayout6 = new GridLayout();
		gridLayout6.numColumns = 2;
		gJavaOptions = new Group(gConfig, SWT.NONE);
		gJavaOptions.setText("Main Java Options");
		gJavaOptions.setLayout(gridLayout8);
		gJavaOptions.setLayoutData(gridData20);
		label8 = new Label(gJavaOptions, SWT.NONE);
		label8.setText("Class Path:");
		tJavaClassPath = new Text(gJavaOptions, SWT.BORDER);
		tJavaClassPath.setToolTipText(Messages
				.getTooltip("config.java_class_path"));
		tJavaClassPath.setLayoutData(gridData22);
		tJavaClassPath
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setJavaClasspath(tJavaClassPath.getText());
					}
				});
		label9 = new Label(gJavaOptions, SWT.NONE);
		label9.setText("Options:");
		tJavaOptions = new Text(gJavaOptions, SWT.BORDER);
		tJavaOptions.setToolTipText(Messages.getTooltip("config.java_options"));
		tJavaOptions.setLayoutData(gridData24);
		tJavaOptions
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setJavaOptions(tJavaOptions.getText());
					}
				});
	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		group = new Group(gConfig, SWT.NONE);
		group.setText("Comment");
		group.setLayoutData(gridData);
		group.setLayout(new GridLayout());
		tComment = new Text(group, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
		tComment.setToolTipText(Messages.getTooltip("config.comment"));
		tComment.setLayoutData(gridData4);
		tComment.setFont(new Font(Display.getDefault(), "Courier New", 8,
				SWT.NORMAL));
		tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setComment(tComment.getText());
			}
		});
	}


} // @jve:decl-index=0:visual-constraint="10,10"
