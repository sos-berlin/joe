package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ConfigListener;

/**
 * @author sky2000
 */
public class ConfigForm extends SOSJOEMessageCodes implements IUpdateLanguage {

	private Button			butBrowse_2					= null;

	private Combo			cConfigurationDeleteEvent	= null;

	private Label			label12_3					= null;

	private Button			butBrowse_1					= null;

	private Combo			cConfigurationModifyEvent	= null;

	private Label			label12_2					= null;

	private Button			butBrowse					= null;

	private Combo			cConfigurationAddEvent		= null;

	private Label			label12_1					= null;

	private ConfigListener	listener					= null;

	private Group			gConfig						= null;

	private Label			label						= null;

	private Text			tSpoolerID					= null;

	private Label			label7						= null;

	private Text			tParameter					= null;

	private Label			label10						= null;

	private Text			tIncludePath				= null;

	private Label			label11						= null;

	private Text			tLogDir						= null;

	private Label			label12						= null;

	private Text			tMailXSLTStylesheet			= null;

	private Group			gPorts						= null;

	private Button			cSamePorts					= null;

	private Label			label14						= null;

	private Text			sPort						= null;

	private Text			sTcpPort					= null;

	private Label			label4						= null;

	private Text			sUdpPort					= null;

	private Group			gMainScheduler				= null;

	private Label			label1						= null;

	private Text			tMainSchedulerHost			= null;

	private Label			label2						= null;

	private Text			sMainSchedulerPort			= null;

	private Group			gJavaOptions				= null;

	private Label			label8						= null;

	private Text			tJavaClassPath				= null;

	private Label			label9						= null;

	private Text			tJavaOptions				= null;

	private Group			group						= null;

	private Text			tComment					= null;

	private Text			tIpAddress					= null;

	private Button			button						= null;

	private Text			txtCentralConfigDir			= null;

	/**
	 * @param parent
	 * @param style
	 */
	public ConfigForm(Composite parent, int style, SchedulerDom dom, ISchedulerUpdate _update) {
		super(parent, style);
		listener = new ConfigListener(dom);
		initialize();
		setToolTipText();
		tSpoolerID.setFocus();

	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGConfig();
		setSize(new org.eclipse.swt.graphics.Point(714, 501));

		// set all values
		listener.getDom().setInit(true);
		tSpoolerID.setText(listener.getSpoolerID());
		tParameter.setText(listener.getParam());
		tIncludePath.setText(listener.getIncludePath());
		tIpAddress.setText(listener.getIpAddress());
		tLogDir.setText(listener.getLogDir());
		tMailXSLTStylesheet.setText(listener.getMailXSLTStylesheet());
		txtCentralConfigDir.setText(listener.getCentralConfigDir());

		cSamePorts.setSelection(listener.isPort());
		if (listener.isPort()) {
			sPort.setText(listener.getPort());
		}
		else {
			sTcpPort.setText(listener.getTcpPort());
			sUdpPort.setText(listener.getUdpPort());
		}

		tMainSchedulerHost.setText(listener.getMainSchedulerHost());
		sMainSchedulerPort.setText(listener.getMainSchedulerPort());

		tJavaClassPath.setText(listener.getJavaClasspath());
		tJavaOptions.setText(listener.getJavaOptions());

		tComment.setText(listener.getComment());

		setEditable();
		listener.getDom().setInit(false);
		tSpoolerID.setFocus();
	}

	private void setEditable() {
		sPort.setEnabled(cSamePorts.getSelection());
		sTcpPort.setEnabled(!cSamePorts.getSelection());
		sUdpPort.setEnabled(!cSamePorts.getSelection());
		if (!sPort.getEnabled() && sTcpPort.getText().equals("") && sUdpPort.getText().equals("")) {
			sTcpPort.setText(sPort.getText());
			sUdpPort.setText(sPort.getText());
		}
		if (sPort.getEnabled() && sTcpPort.getText().equals(sUdpPort.getText()) && !sTcpPort.getText().equals("")) {
			String s = sTcpPort.getText();
			sTcpPort.setText("");
			sUdpPort.setText("");
			sPort.setText(s);
		}
	}

	/**
	 * This method initializes gConfig
	 */
	private void createGConfig() {
		GridLayout gridLayout1 = new GridLayout();
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		gConfig = JOE_G_ConfigForm_Config.Control(new Group(this, SWT.NONE));
		// gConfig.setText("Config");
		gConfig.setLayout(gridLayout1);
		gConfig.setSize(new Point(798, 516));

		final Group group_1 = JOE_G_ConfigForm_Group1.Control(new Group(gConfig, SWT.NONE));
		group_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		group_1.setLayout(gridLayout_1);

		label = JOE_L_ConfigForm_SchedulerID.Control(new Label(group_1, SWT.NONE));
		// label.setText("Scheduler ID:");

		tSpoolerID = JOE_T_ConfigForm_SchedulerID.Control(new Text(group_1, SWT.BORDER));
		// tSpoolerID.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// tSpoolerID.selectAll();
		// }
		// });
		tSpoolerID.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		tSpoolerID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setSpoolerID(tSpoolerID.getText());
			}
		});

		label7 = JOE_L_ConfigForm_Params.Control(new Label(group_1, SWT.NONE));
		// label7.setText("Parameter:");

		tParameter = JOE_T_ConfigForm_Params.Control(new Text(group_1, SWT.BORDER));
		// tParameter.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// tParameter.selectAll();
		// }
		// });
		tParameter.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		tParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setParam(tParameter.getText());
			}
		});

		label10 = JOE_L_ConfigForm_IncludePath.Control(new Label(group_1, SWT.NONE));
		// label10.setText("Include Path:");

		tIncludePath = JOE_T_ConfigForm_IncludePath.Control(new Text(group_1, SWT.BORDER));
		// tIncludePath.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// tIncludePath.selectAll();
		// }
		// });
		tIncludePath.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		tIncludePath.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setIncludePath(tIncludePath.getText());
			}
		});

		@SuppressWarnings("unused")
		final Label ipaddressLabel = JOE_L_ConfigForm_IPAddress.Control(new Label(group_1, SWT.NONE));
		// ipaddressLabel.setText("IP-Address");

		tIpAddress = JOE_T_ConfigForm_IPAddress.Control(new Text(group_1, SWT.BORDER));
		// tIpAddress.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// tIpAddress.selectAll();
		// }
		// });
		tIpAddress.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setIpAddress(tIpAddress.getText());
			}
		});
		tIpAddress.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		label11 = JOE_L_ConfigForm_LogDir.Control(new Label(group_1, SWT.NONE));
		// label11.setText("Log Dir:");

		tLogDir = JOE_T_ConfigForm_LogDir.Control(new Text(group_1, SWT.BORDER));
		// tLogDir.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// tLogDir.selectAll();
		// }
		// });
		tLogDir.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		tLogDir.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setLogDir(tLogDir.getText());
			}
		});

		label12 = JOE_L_ConfigForm_MailXSLT.Control(new Label(group_1, SWT.NONE));
		// label12.setText("Mail XSLT:");

		tMailXSLTStylesheet = JOE_T_ConfigForm_MailXSLT.Control(new Text(group_1, SWT.BORDER));
		// tMailXSLTStylesheet.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// tMailXSLTStylesheet.selectAll();
		// }
		// });
		tMailXSLTStylesheet.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		tMailXSLTStylesheet.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setMailXSLTStylesheet(tMailXSLTStylesheet.getText());
			}
		});

		final Label centralConfigurationDirectoryLabel = JOE_L_ConfigForm_CentralConfigDir.Control(new Label(group_1, SWT.NONE));
		centralConfigurationDirectoryLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		// centralConfigurationDirectoryLabel.setText("Central Configuration Dir:");

		txtCentralConfigDir = JOE_T_ConfigForm_CentralConfigDir.Control(new Text(group_1, SWT.BORDER));
		// txtCentralConfigDir.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// txtCentralConfigDir.selectAll();
		// }
		// });
		txtCentralConfigDir.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setCentralConfigDir(txtCentralConfigDir.getText());
			}
		});
		txtCentralConfigDir.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		createGPorts();
		createGMainScheduler();
		createGJavaOptions();
		createGroup();
	}

	/**
	 * This method initializes gPorts
	 */
	private void createGPorts() {
		GridData gridData42 = new GridData();
		gridData42.horizontalSpan = 2;
		gridData42.verticalAlignment = GridData.CENTER;
		gridData42.grabExcessHorizontalSpace = true;
		gridData42.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 6;

		final Group eventGroup = JOE_G_ConfigForm_Event.Control(new Group(gConfig, SWT.NONE));
		// eventGroup.setText("Event");
		eventGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 2));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		eventGroup.setLayout(gridLayout);

		label12_1 = JOE_L_ConfigForm_ConfigAddEvent.Control(new Label(eventGroup, SWT.NONE));
		final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.widthHint = 153;
		label12_1.setLayoutData(gridData);
		// label12_1.setText("Configuration Add Event:");

		cConfigurationAddEvent = JOE_Cbo_ConfigForm_ConfigAddEvent.Control(new Combo(eventGroup, SWT.NONE));
		cConfigurationAddEvent.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		cConfigurationAddEvent.setItems(listener.getJobs());
		cConfigurationAddEvent.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setConfigurationAddEvent(cConfigurationAddEvent.getText());
			}
		});
		cConfigurationAddEvent.setText(listener.getConfigurationAddEvent());

		butBrowse = JOE_B_ConfigForm_Browse1.Control(new Button(eventGroup, SWT.NONE));
		butBrowse.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String jobname = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_JOB);
				if (jobname != null && jobname.length() > 0) {
					cConfigurationAddEvent.setText(jobname);
				}
			}
		});
		// butBrowse.setText("Browse");

		label12_2 = JOE_L_ConfigForm_ConfigModifyEvent.Control(new Label(eventGroup, SWT.NONE));
		// label12_2.setText("Configuration Modify Event:");

		cConfigurationModifyEvent = JOE_Cbo_ConfigForm_ConfigModifyEvent.Control(new Combo(eventGroup, SWT.NONE));
		cConfigurationModifyEvent.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		cConfigurationModifyEvent.setItems(listener.getJobs());
		cConfigurationModifyEvent.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setConfigurationModifyEvent(cConfigurationModifyEvent.getText());
			}
		});
		cConfigurationModifyEvent.setText(listener.getConfigurationModifyEvent());

		butBrowse_1 = JOE_B_ConfigForm_Browse2.Control(new Button(eventGroup, SWT.NONE));
		butBrowse_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butBrowse_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String jobname = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_JOB);
				if (jobname != null && jobname.length() > 0)
					cConfigurationModifyEvent.setText(jobname);
			}
		});
		// butBrowse_1.setText("Browse");

		label12_3 = JOE_L_ConfigForm_ConfigDeleteEvent.Control(new Label(eventGroup, SWT.NONE));
		// label12_3.setText("Configuration Delete Event:");

		cConfigurationDeleteEvent = JOE_Cbo_ConfigForm_ConfigDeleteEvent.Control(new Combo(eventGroup, SWT.NONE));
		cConfigurationDeleteEvent.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		cConfigurationDeleteEvent.setItems(listener.getJobs());
		cConfigurationDeleteEvent.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setConfigurationDeleteEvent(cConfigurationDeleteEvent.getText());
			}
		});
		cConfigurationDeleteEvent.setText(listener.getConfigurationDeleteEvent());

		butBrowse_2 = JOE_B_ConfigForm_Browse3.Control(new Button(eventGroup, SWT.NONE));
		butBrowse_2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butBrowse_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String jobname = IOUtils.getJobschedulerObjectPathName(MergeAllXMLinDirectory.MASK_JOB);
				if (jobname != null && jobname.length() > 0)
					cConfigurationDeleteEvent.setText(jobname);
			}
		});
		// butBrowse_2.setText("Browse");
	}

	/**
	 * This method initializes gMainScheduler
	 */
	private void createGMainScheduler() {
		GridData gridData18 = new GridData();
		gridData18.horizontalSpan = 2;
		gridData18.verticalAlignment = GridData.CENTER;
		gridData18.grabExcessHorizontalSpace = true;
		gridData18.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 4;
	}

	/**
	 * This method initializes gJavaOptions
	 */
	private void createGJavaOptions() {
		GridData gridData24 = new GridData();
		gridData24.horizontalIndent = 9;
		gridData24.horizontalAlignment = GridData.FILL;
		gridData24.grabExcessHorizontalSpace = true;
		gridData24.verticalAlignment = GridData.CENTER;
		GridData gridData22 = new GridData();
		gridData22.horizontalIndent = 9;
		gridData22.horizontalAlignment = GridData.FILL;
		gridData22.grabExcessHorizontalSpace = true;
		gridData22.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout8 = new GridLayout();
		gridLayout8.numColumns = 2;
		GridData gridData19 = new GridData();
		gridData19.horizontalAlignment = GridData.FILL;
		gridData19.verticalAlignment = GridData.CENTER;
		gridData19.horizontalSpan = 2;
		gridData19.grabExcessHorizontalSpace = true;
		GridLayout gridLayout6 = new GridLayout();
		gridLayout6.numColumns = 2;

		final Composite composite = JOE_Cmp_ConfigForm_CmpPort.Control(new Composite(gConfig, SWT.NONE));
		final GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		gridData.heightHint = 67;
		composite.setLayoutData(gridData);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);
		GridLayout gridLayout11 = new GridLayout();
		gridLayout11.marginWidth = 0;
		gridLayout11.numColumns = 6;

		gPorts = JOE_G_ConfigForm_Ports.Control(new Group(composite, SWT.NONE));
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, false, true);
		gridData_2.heightHint = 60;
		gPorts.setLayoutData(gridData_2);
		// gPorts.setText("Job Scheduler Port");
		gPorts.setLayout(gridLayout11);
		GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, false, false, 6, 1);
		gridData3.horizontalIndent = 5;

		cSamePorts = JOE_B_ConfigForm_SamePortsCheckBtn.Control(new Button(gPorts, SWT.CHECK));
		// cSamePorts.setText("Use the same port for udp and tcp");
		cSamePorts.setLayoutData(gridData3);
		cSamePorts.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				setEditable();
			}
		});

		label14 = JOE_L_ConfigForm_SchedulerPort.Control(new Label(gPorts, SWT.NONE));
		final GridData gridData_5 = new GridData(30, SWT.DEFAULT);
		gridData_5.horizontalIndent = 5;
		label14.setLayoutData(gridData_5);
		// label14.setText("Port:");

		sPort = JOE_T_ConfigForm_SamePort.Control(new Text(gPorts, SWT.BORDER));
		// sPort.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// sPort.selectAll();
		// }
		// });
		sPort.setLayoutData(new GridData(60, SWT.DEFAULT));
		sPort.addModifyListener(new ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setPort(sPort.getText());
			}
		});
		GridData gridData5 = new GridData(60, SWT.DEFAULT);
		gridData5.horizontalIndent = 10;

		final Label tcpLabel = JOE_L_ConfigForm_TCP.Control(new Label(gPorts, SWT.NONE));
		final GridData gridData_6 = new GridData();
		gridData_6.horizontalIndent = 10;
		tcpLabel.setLayoutData(gridData_6);
		// tcpLabel.setText("TCP:");

		sTcpPort = JOE_T_ConfigForm_TCP.Control(new Text(gPorts, SWT.BORDER));
		// sTcpPort.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// sTcpPort.selectAll();
		// }
		// });
		sTcpPort.setLayoutData(gridData5);
		sTcpPort.addModifyListener(new ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setTcpPort(sTcpPort.getText());
			}
		});

		label4 = JOE_L_ConfigForm_UDP.Control(new Label(gPorts, SWT.NONE));
		// label4.setText("UDP:");

		sUdpPort = JOE_T_ConfigForm_UDP.Control(new Text(gPorts, SWT.BORDER));
		// sUdpPort.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// sUdpPort.selectAll();
		// }
		// });
		sUdpPort.setLayoutData(new GridData(60, SWT.DEFAULT));
		sUdpPort.addModifyListener(new ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setUdpPort(sUdpPort.getText());
			}
		});

		GridLayout gridLayout7 = new GridLayout();
		gridLayout7.numColumns = 4;
		GridData gridData9 = new GridData();
		gridData9.horizontalSpan = 4;
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 4;

		gMainScheduler = JOE_G_ConfigForm_Supervisor.Control(new Group(composite, SWT.NONE));
		gMainScheduler.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		// gMainScheduler.setText("Supervisor");
		gMainScheduler.setLayout(gridLayout7);

		label1 = JOE_L_ConfigForm_Host.Control(new Label(gMainScheduler, SWT.NONE));
		label1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
		// label1.setText("Host:");
		GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData6.widthHint = 58;

		tMainSchedulerHost = JOE_T_ConfigForm_Host.Control(new Text(gMainScheduler, SWT.BORDER));
		// tMainSchedulerHost.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// tMainSchedulerHost.selectAll();
		// }
		// });
		tMainSchedulerHost.setLayoutData(gridData6);
		tMainSchedulerHost.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setMainScheduler(tMainSchedulerHost.getText() + ":" + listener.getMainSchedulerPort());
			}
		});

		label2 = JOE_L_ConfigForm_SupervisorPort.Control(new Label(gMainScheduler, SWT.NONE));
		label2.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
		// label2.setText("Port:");
		GridData gridData11 = new GridData(GridData.FILL, GridData.CENTER, false, true);
		gridData11.widthHint = 47;

		sMainSchedulerPort = JOE_L_ConfigForm_SupervisorPort.Control(new Text(gMainScheduler, SWT.BORDER));
		// sMainSchedulerPort.addFocusListener(new FocusAdapter() {
		// @Override
		// public void focusGained(FocusEvent arg0) {
		// sMainSchedulerPort.selectAll(); }
		// });
		sMainSchedulerPort.setLayoutData(gridData11);
		sMainSchedulerPort.addModifyListener(new ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (sMainSchedulerPort.getText().equals(""))
					sMainSchedulerPort.setBackground(Options.getRequiredColor());
				else
					sMainSchedulerPort.setBackground(null);
				listener.setMainScheduler(listener.getMainSchedulerHost() + ":" + sMainSchedulerPort.getText());
			}
		});

		gJavaOptions = JOE_G_ConfigForm_JavaOptions.Control(new Group(gConfig, SWT.NONE));
		// gJavaOptions.setText("Main Java Options");
		gJavaOptions.setLayout(gridLayout8);
		gJavaOptions.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		label8 = JOE_L_ConfigForm_ClassPath.Control(new Label(gJavaOptions, SWT.NONE));
		// label8.setText("Class Path:");

		tJavaClassPath = JOE_T_ConfigForm_ClassPath.Control(new Text(gJavaOptions, SWT.BORDER));
		// tJavaClassPath.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// tJavaClassPath.selectAll();
		// }
		// });
		tJavaClassPath.setLayoutData(gridData22);
		tJavaClassPath.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setJavaClasspath(tJavaClassPath.getText());
			}
		});

		label9 = JOE_L_ConfigForm_Options.Control(new Label(gJavaOptions, SWT.NONE));
		// label9.setText("Options:");

		tJavaOptions = JOE_T_ConfigForm_Options.Control(new Text(gJavaOptions, SWT.BORDER));
		// tJavaOptions.addFocusListener(new FocusAdapter() {
		// public void focusGained(final FocusEvent e) {
		// tJavaOptions.selectAll();
		// }
		// });
		tJavaOptions.setLayoutData(gridData24);
		tJavaOptions.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setJavaOptions(tJavaOptions.getText());
			}
		});
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		group = JOE_G_ConfigForm_Comment.Control(new Group(gConfig, SWT.NONE));
		// group.setText("Comment");
		group.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		group.setLayout(gridLayout);

		button = JOE_B_ConfigForm_Comment.Control(new Button(group, SWT.NONE));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
				if (text != null)
					tComment.setText(text);
			}
		});
		button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));

		tComment = JOE_T_ConfigForm_Comment.Control(new Text(group, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL));
		tComment.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == 97 && e.stateMask == SWT.CTRL) {
					tComment.setSelection(0, tComment.getText().length());
				}
			}
		});
		tComment.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 2));
		tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
		tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setComment(tComment.getText());
			}
		});
		new Label(group, SWT.NONE);
	}

	public void setToolTipText() {
		// tSpoolerID.setToolTipText(Messages.getTooltip("config.spooler_id"));
		// tParameter.setToolTipText(Messages.getTooltip("config.param"));
		// tIncludePath.setToolTipText(Messages.getTooltip("config.include_path"));
		// tLogDir.setToolTipText(Messages.getTooltip("config.log_dir"));
		// tMailXSLTStylesheet.setToolTipText(Messages.getTooltip("config.mail_xslt_stylesheet"));
		// txtCentralConfigDir.setToolTipText(Messages.getTooltip("config.central_configuration_directory"));
		//
		// cSamePorts.setToolTipText(Messages.getTooltip("config.use_same_port"));
		// sPort.setToolTipText(Messages.getTooltip("config.port"));
		// sTcpPort.setToolTipText(Messages.getTooltip("config.tcp_port"));
		// sUdpPort.setToolTipText(Messages.getTooltip("config.udp_port"));
		//
		// tMainSchedulerHost.setToolTipText(Messages.getTooltip("config.main_scheduler_host"));
		// tIpAddress.setToolTipText(Messages.getTooltip("config.main_scheduler_ip_address"));
		//
		// sMainSchedulerPort.setToolTipText(Messages.getTooltip("config.main_scheduler_port"));
		// tJavaClassPath.setToolTipText(Messages.getTooltip("config.java_class_path"));
		// tComment.setToolTipText(Messages.getTooltip("config.comment"));
		// tJavaOptions.setToolTipText(Messages.getTooltip("config.java_options"));
		//
		// cConfigurationAddEvent.setToolTipText(Messages.getTooltip("config.configuration_add_event"));
		// cConfigurationModifyEvent.setToolTipText(Messages.getTooltip("config.configuration_modify_event"));
		// cConfigurationDeleteEvent.setToolTipText(Messages.getTooltip("config.configuration_delete_event"));
		//
		// butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
		// butBrowse_1.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
		// butBrowse_2.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
		//
		// button.setToolTipText(Messages.getTooltip("button.comment"));
	}
} // @jve:decl-index=0:visual-constraint="10,10"
