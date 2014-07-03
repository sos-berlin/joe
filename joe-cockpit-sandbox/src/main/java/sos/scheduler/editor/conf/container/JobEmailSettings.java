package sos.scheduler.editor.conf.container;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.classes.FormBaseClass;

import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.objects.job.JobListener;

public class JobEmailSettings extends FormBaseClass<JobListener> {

	private final String conClassName = this.getClass().getSimpleName();
	private final Logger logger = Logger.getLogger(this.getClass());

	private Combo			mailOnDelayAfterError	= null;
	private Text			mailCC					= null;
	private Text			mailBCC					= null;
	private Combo			mailOnError				= null;
	private Combo			mailOnWarning			= null;
	private Combo			mailOnSuccess			= null;
	private Combo			mailOnProcess			= null;
	private Text			mailTo					= null;

	private final String[]		comboItems			= { "yes", "no", "" };

	private final String	conSVNVersion			= "$Id$";


	public JobEmailSettings(final Composite pParentComposite, final JobListener pobjJobDataProvider) {
		super(pParentComposite, pobjJobDataProvider);
		objJobDataProvider = pobjJobDataProvider;

		logger.debug(conClassName + "\n" + conSVNVersion);

		createGroup();
		initForm();

	}

	@Override
	public void createGroup() {

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

		Group group4EMail = SOSJOEMessageCodes.JOE_G_JobEmailSettings_Notifications.Control(new Group(objParent, SWT.NONE));
		group4EMail.setLayout(gridLayout4EMailGroup);
		group4EMail.setLayoutData(gridData);

		@SuppressWarnings("unused")
		Label labelMailOnError = SOSJOEMessageCodes.JOE_L_MailForm_MailOnError.Control(new Label(group4EMail, SWT.NONE));

//		mailOnError = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnError.Control(new Combo(group4EMail, intComboBoxStyle));
		mailOnError = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnError.Control(new Combo(group4EMail, SWT.READ_ONLY));
		// mailOnError.setItems(new String[]{"yes", "no", ""});
		mailOnError.setItems(comboItems);
		mailOnError.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
				objJobDataProvider.setMailOnError( mailOnError.getText(), "no");
			}
		});
		GridData gd_mailOnError = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnError.minimumWidth = 150;
		mailOnError.setLayoutData(gd_mailOnError);

		@SuppressWarnings("unused")
		Label labelMailOnWarning = SOSJOEMessageCodes.JOE_L_MailForm_MailOnWarning.Control(new Label(group4EMail, SWT.NONE));

//		mailOnWarning = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnWarning.Control(new Combo(group4EMail, intComboBoxStyle));
		mailOnWarning = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnWarning.Control(new Combo(group4EMail, SWT.READ_ONLY));
		// mailOnWarning.setItems(new String[]{"yes", "no", ""});
		mailOnWarning.setItems(comboItems);
		mailOnWarning.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				mailOnDelayAfterError.setEnabled(mailOnWarning.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
				objJobDataProvider.setMailOnWarning(mailOnWarning.getText(), "no");
			}
		});
		GridData gd_mailOnWarning = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnWarning.minimumWidth = 150;
		mailOnWarning.setLayoutData(gd_mailOnWarning);

		@SuppressWarnings("unused")
		Label label3 = SOSJOEMessageCodes.JOE_L_MailForm_MailOnSuccess.Control(new Label(group4EMail, SWT.NONE));

//		mailOnSuccess = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnSuccess.Control(new Combo(group4EMail, intComboBoxStyle));
		mailOnSuccess = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnSuccess.Control(new Combo(group4EMail, SWT.READ_ONLY));
		// mailOnSuccess.setItems(new String[]{"yes", "no", ""});
		mailOnSuccess.setItems(comboItems);
		mailOnSuccess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				objJobDataProvider.setMailOnSuccess(mailOnSuccess.getText(), "no");
			}
		});
		GridData gd_mailOnSuccess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnSuccess.minimumWidth = 150;
		mailOnSuccess.setLayoutData(gd_mailOnSuccess);

		@SuppressWarnings("unused")
		final Label mailOnProcessLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailOnProcess.Control(new Label(group4EMail, SWT.NONE));

//		mailOnProcess = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnProcess.Control(new Combo(group4EMail, intComboBoxStyle));
		mailOnProcess = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnProcess.Control(new Combo(group4EMail, SWT.READ_ONLY));
		// mailOnProcess.setItems(new String[]{"yes", "no", ""});
		mailOnProcess.setItems(comboItems);
		mailOnProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				objJobDataProvider.setMailOnProcess(mailOnProcess.getText(), "no");
			}
		});
		GridData gd_mailOnProcess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gd_mailOnProcess.minimumWidth = 150;
		mailOnProcess.setLayoutData(gd_mailOnProcess);

		@SuppressWarnings("unused")
		final Label mailOnDelayLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailOnDelayAfterError.Control(new Label(group4EMail, SWT.NONE));

//		mailOnDelayAfterError = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnDelayAfterError.Control(new Combo(group4EMail, intComboBoxStyle));
		mailOnDelayAfterError = SOSJOEMessageCodes.JOE_Cbo_MailForm_MailOnDelayAfterError.Control(new Combo(group4EMail, SWT.READ_ONLY));
		mailOnDelayAfterError.setItems(new String[] { "all", "first_only", "last_only", "first_and_last_only", "" });
		mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
		mailOnDelayAfterError.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				objJobDataProvider.setMailOnDelayAfterError(mailOnDelayAfterError.getText(), "no");
			}
		});
		mailOnDelayAfterError.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

		final Label ddddLabel = new Label(group4EMail, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_1.heightHint = 8;
		ddddLabel.setLayoutData(gridData_1);

		@SuppressWarnings("unused")
		final Label mailToLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailTo.Control(new Label(group4EMail, SWT.NONE));

		mailTo = SOSJOEMessageCodes.JOE_T_MailForm_MailTo.Control(new Text(group4EMail, SWT.BORDER));
		mailTo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				objJobDataProvider.setLogMailTo(mailTo.getText(), "");
			}
		});
		mailTo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		@SuppressWarnings("unused")
		final Label mailCcLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailCC.Control(new Label(group4EMail, SWT.NONE));

		mailCC = SOSJOEMessageCodes.JOE_T_MailForm_MailCC.Control(new Text(group4EMail, SWT.BORDER));
		mailCC.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				objJobDataProvider.setLogMailCC(mailCC.getText(), "");
			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_2.minimumWidth = 60;
		mailCC.setLayoutData(gridData_2);

		@SuppressWarnings("unused")
		final Label mailBccLabel = SOSJOEMessageCodes.JOE_L_MailForm_MailBCC.Control(new Label(group4EMail, SWT.NONE));

		gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.minimumWidth = 60;
		mailBCC = SOSJOEMessageCodes.JOE_T_MailForm_MailBCC.Control(new Text(group4EMail, SWT.BORDER));
		mailBCC.setLayoutData(gridData);
		mailBCC.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				objJobDataProvider.setLogMailBcc(mailBCC.getText(), "");
			}
		});

		final Label label_1 = new Label(group4EMail, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_3.heightHint = 8;
		label_1.setLayoutData(gridData_3);

		objParent.layout();
	}

	private void initForm() {

		mailOnError.setText(objJobDataProvider.getMailOnError());
		mailOnWarning.setText(objJobDataProvider.getMailOnWarning());
		mailOnSuccess.setText(objJobDataProvider.getMailOnSuccess());
		mailOnProcess.setText(objJobDataProvider.getMailOnProcess());
		mailOnDelayAfterError.setText(objJobDataProvider.getMailOnDelayAfterError());

		mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));

		mailTo.setText(objJobDataProvider.getLogMailTo());
		mailCC.setText(objJobDataProvider.getLogMailCC());
		mailBCC.setText(objJobDataProvider.getLogMailBCC());

	}
}
