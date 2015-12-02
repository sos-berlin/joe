package sos.scheduler.editor.conf.forms;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.MailListener;

import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class MailForm extends SOSJOEMessageCodes implements IUnsaved {
	private Combo			cboHistoryWithLog		= null;
	private Combo			cboHistoryOnProcess		= null;
	private Combo			cboHistory				= null;
	private Combo			mailOnDelayAfterError	= null;
	private MailListener	listener				= null;
	private int				type					= -1;
	private Group			group					= null;
	private Text			mailCC					= null;
	private Text			mailBCC					= null;
	private Combo			mailOnError				= null;
	private Combo			mailOnWarning			= null;
	private Combo			mailOnSuccess			= null;
	private Combo			mailOnProcess			= null;
	private Text			mailTo					= null;
	private Combo			LogLevel				= null;
	private boolean			init					= true;
	private String[]		comboItems				= { "yes", "no", "" };

	public MailForm(Composite parent, int style, SchedulerDom dom, Element element) {
		super(parent, style);
		initialize();
		setAttributes(dom, element, type);
		init = false;
	}

	public void setAttributes(SchedulerDom dom, Element element, int type) {
		listener = new MailListener(dom, element);
		mailOnError.setText(listener.getValue("mail_on_error"));
		mailOnWarning.setText(listener.getValue("mail_on_warning"));
		mailOnSuccess.setText(listener.getValue("mail_on_success"));
		mailOnProcess.setText(listener.getValue("mail_on_process"));
		mailOnDelayAfterError.setText(listener.getValue("mail_on_delay_after_error"));
		mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
		mailTo.setText(listener.getValue("log_mail_to"));
		mailCC.setText(listener.getValue("log_mail_cc"));
		mailBCC.setText(listener.getValue("log_mail_bcc"));
		LogLevel.setText(listener.getValue("log_level"));
		cboHistory.setText(listener.getValue("history"));
		cboHistoryOnProcess.setText(listener.getValue("history_on_process"));
		cboHistoryWithLog.setText(listener.getValue("history_with_log"));
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(604, 427));
		mailOnError.setItems(comboItems);
		mailOnWarning.setItems(comboItems);
		mailOnSuccess.setItems(comboItems);
		mailOnProcess.setItems(comboItems);
		mailOnDelayAfterError.setItems(new String[] { "all", "first_only", "last_only", "first_and_last_only", "" });
		cboHistory.setItems(comboItems);
		cboHistoryOnProcess.setItems(new String[] { "0", "1", "2", "3", "4", "" });
		cboHistoryWithLog.setItems(new String[] { "yes", "no", "gzip", "" });
		LogLevel.setItems(new String[] { "info", "debug1", "debug2", "debug3", "debug4", "debug5", "debug6", "debug7", "debug8", "debug9", "" });
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginTop = 5;
		gridLayout.numColumns = 2;
		group = JOE_G_MailForm_Mail.Control(new Group(this, SWT.NONE));
		group.setLayout(gridLayout);
		@SuppressWarnings("unused") Label label14 = JOE_L_MailForm_MailOnError.Control(new Label(group, SWT.NONE));
		mailOnError = JOE_Cbo_MailForm_MailOnError.Control(new Combo(group, SWT.READ_ONLY));
		mailOnError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
				listener.setValue("mail_on_error", mailOnError.getText(), "no");
			}
		});
		GridData gd_mailOnError = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnError.minimumWidth = 150;
		mailOnError.setLayoutData(gd_mailOnError);
		@SuppressWarnings("unused") Label label1 = JOE_L_MailForm_MailOnWarning.Control(new Label(group, SWT.NONE));
		mailOnWarning = JOE_Cbo_MailForm_MailOnWarning.Control(new Combo(group, SWT.READ_ONLY));
		mailOnWarning.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				mailOnDelayAfterError.setEnabled(mailOnWarning.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
				listener.setValue("mail_on_warning", mailOnWarning.getText(), "no");
			}
		});
		GridData gd_mailOnWarning = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnWarning.minimumWidth = 150;
		mailOnWarning.setLayoutData(gd_mailOnWarning);
		@SuppressWarnings("unused") Label label3 = JOE_L_MailForm_MailOnSuccess.Control(new Label(group, SWT.NONE));
		mailOnSuccess = JOE_Cbo_MailForm_MailOnSuccess.Control(new Combo(group, SWT.READ_ONLY));
		mailOnSuccess.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("mail_on_success", mailOnSuccess.getText(), "no");
			}
		});
		GridData gd_mailOnSuccess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnSuccess.minimumWidth = 150;
		mailOnSuccess.setLayoutData(gd_mailOnSuccess);
		@SuppressWarnings("unused") final Label mailOnProcessLabel = JOE_L_MailForm_MailOnProcess.Control(new Label(group, SWT.NONE));
		mailOnProcess = JOE_Cbo_MailForm_MailOnProcess.Control(new Combo(group, SWT.READ_ONLY));
		mailOnProcess.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("mail_on_process", mailOnProcess.getText(), "no");
			}
		});
		GridData gd_mailOnProcess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnProcess.minimumWidth = 150;
		mailOnProcess.setLayoutData(gd_mailOnProcess);
		@SuppressWarnings("unused") final Label mailOnDelayLabel = JOE_L_MailForm_MailOnDelayAfterError.Control(new Label(group, SWT.NONE));
		mailOnDelayAfterError = JOE_Cbo_MailForm_MailOnDelayAfterError.Control(new Combo(group, SWT.READ_ONLY));
		mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
		mailOnDelayAfterError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("mail_on_delay_after_error", mailOnDelayAfterError.getText(), "no");
			}
		});
		mailOnDelayAfterError.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		final Label ddddLabel = new Label(group, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_1.heightHint = 8;
		ddddLabel.setLayoutData(gridData_1);
		@SuppressWarnings("unused") final Label mailToLabel = JOE_L_MailForm_MailTo.Control(new Label(group, SWT.NONE));
		mailTo = JOE_T_MailForm_MailTo.Control(new Text(group, SWT.BORDER));
		mailTo.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setValue("log_mail_to", mailTo.getText());
			}
		});
		mailTo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		@SuppressWarnings("unused") final Label mailCcLabel = JOE_L_MailForm_MailCC.Control(new Label(group, SWT.NONE));
		mailCC = JOE_T_MailForm_MailCC.Control(new Text(group, SWT.BORDER));
		mailCC.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setValue("log_mail_cc", mailCC.getText());
			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_2.minimumWidth = 60;
		mailCC.setLayoutData(gridData_2);
		@SuppressWarnings("unused") final Label mailBccLabel = JOE_L_MailForm_MailBCC.Control(new Label(group, SWT.NONE));
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.minimumWidth = 60;
		mailBCC = JOE_T_MailForm_MailBCC.Control(new Text(group, SWT.BORDER));
		mailBCC.setLayoutData(gridData);
		mailBCC.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setValue("log_mail_bcc", mailBCC.getText());
			}
		});
		final Label label_1 = new Label(group, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_3.heightHint = 8;
		label_1.setLayoutData(gridData_3);
		@SuppressWarnings("unused") final Label logLevelLabel = JOE_L_MailForm_LogLevel.Control(new Label(group, SWT.NONE));
		LogLevel = JOE_Cbo_MailForm_LogLevel.Control(new Combo(group, SWT.READ_ONLY));
		GridData gd_LogLevel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_LogLevel.minimumWidth = 150;
		LogLevel.setLayoutData(gd_LogLevel);
		LogLevel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("log_level", LogLevel.getText());
			}
		});
		final Label label_2 = new Label(group, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_4.heightHint = 8;
		label_2.setLayoutData(gridData_4);
		@SuppressWarnings("unused") final Label historyLabel = JOE_L_MailForm_History.Control(new Label(group, SWT.NONE));
		cboHistory = JOE_Cbo_MailForm_History.Control(new Combo(group, SWT.READ_ONLY));
		cboHistory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("history", cboHistory.getText());
			}
		});
		GridData gd_cboHistory = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_cboHistory.minimumWidth = 150;
		cboHistory.setLayoutData(gd_cboHistory);
		@SuppressWarnings("unused") final Label historyOnProcessLabel = JOE_L_MailForm_HistoryOnProcess.Control(new Label(group, SWT.NONE));
		cboHistoryOnProcess = JOE_Cbo_MailForm_HistoryOnProcess.Control(new Combo(group, SWT.NONE));
		cboHistoryOnProcess.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				setOnHistory();
			}
		});
		cboHistoryOnProcess.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		cboHistoryOnProcess.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				cboHistoryOnProcess.setText(listener.getValue("history_on_process"));
			}
		});
		GridData gd_cboHistoryOnProcess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_cboHistoryOnProcess.minimumWidth = 150;
		cboHistoryOnProcess.setLayoutData(gd_cboHistoryOnProcess);
		@SuppressWarnings("unused") final Label historyWithLogLabel = JOE_L_MailForm_HistoryWithLog.Control(new Label(group, SWT.NONE));
		cboHistoryWithLog = JOE_Cbo_MailForm_HistoryWithLog.Control(new Combo(group, SWT.READ_ONLY));
		cboHistoryWithLog.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("history_with_log", cboHistoryWithLog.getText());
			}
		});
		GridData gd_cboHistoryWithLog = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_cboHistoryWithLog.minimumWidth = 150;
		cboHistoryWithLog.setLayoutData(gd_cboHistoryWithLog);
	}


	public boolean isUnsaved() {
		return false;
	}

	public void apply() {
	}

	private void setOnHistory() {
		if (init)
			return;
		boolean isDigit = true;
		char[] c = cboHistoryOnProcess.getText().toCharArray();
		for (int i = 0; i < c.length; i++) {
			isDigit = Character.isDigit(c[i]);
			if (!isDigit)
				break;
		}
		if (cboHistoryOnProcess.getText().equals("yes") || cboHistoryOnProcess.getText().equals("no") || isDigit)
			listener.setValue("history_on_process", cboHistoryOnProcess.getText());
	}
} // @jve:decl-index=0:visual-constraint="10,10"
