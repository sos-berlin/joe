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

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.MailListener;


public class MailForm extends Composite implements IUnsaved, IUpdateLanguage {


	private Combo          cboHistoryWithLog            = null;
	
	private Combo          cboHistoryOnProcess          = null;
	
	private Combo          cboHistory                   = null;
	
	private Combo          mailOnDelayAfterError        = null;
	
	private MailListener   listener                     = null;

	private int            type                         = -1;

	private Group          group                        = null;

	private Text           mailCC                       = null;

	private Text           mailBCC                      = null;
	
	private Combo          mailOnError                  = null; 

	private Combo          mailOnWarning                = null;

	private Combo          mailOnSuccess                = null;
	
	private Combo          mailOnProcess                = null;
	
	private Text           mailTo                       = null;
	
	private Combo          LogLevel                     = null; 
	
	private boolean        init                         = true;
	

	public MailForm(Composite parent, int style, SchedulerDom dom, Element element) {

		super(parent, style);

		initialize();
		setToolTipText();
		
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

		
		mailOnError.setItems(new String[]{"yes", "no", ""}); 
		mailOnWarning.setItems(new String[]{"yes", "no", ""}); 
		mailOnSuccess.setItems(new String[]{"yes", "no", ""}); 		
		mailOnProcess.setItems(new String[]{"yes", "no", ""});
		mailOnDelayAfterError.setItems(new String[]{"all", "first_only", "last_only", "first_and_last_only", ""});
		cboHistory.setItems(new String[]{"yes", "no", ""});
		//cboHistoryOnProcess.setItems(new String[]{"yes", "no", "0", "1", "", ""});
		cboHistoryOnProcess.setItems(new String[]{"0", "1", "2", "3", "4", ""});
		cboHistoryWithLog.setItems(new String[]{"yes", "no", "gzip", ""});
		
		LogLevel.setItems(new String[]{"info", "debug1", "debug2", "debug3", "debug4", "debug5", "debug6", "debug7", "debug8", "debug9", ""});
		
		
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
		group = new Group(this, SWT.NONE);
		group.setText("Mail");
		group.setLayout(gridLayout);
		Label label14 = new Label(group, SWT.NONE);
		label14.setText("Mail On Error");
		
		mailOnError = new Combo(group, SWT.READ_ONLY);
		mailOnError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
				listener.setValue("mail_on_error", mailOnError.getText(), "no");
			}
		});
		mailOnError.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		Label label1 = new Label(group, SWT.NONE);
		label1.setText("Mail On Warning");

		mailOnWarning = new Combo(group, SWT.READ_ONLY);		
		mailOnWarning.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				mailOnDelayAfterError.setEnabled(mailOnWarning.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
				listener.setValue("mail_on_warning", mailOnWarning.getText(), "no");
			}
		});
		mailOnWarning.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		Label label3 = new Label(group, SWT.NONE);
		label3.setText("Mail On Success");

		mailOnSuccess = new Combo(group, SWT.READ_ONLY);
		mailOnSuccess.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("mail_on_success", mailOnSuccess.getText(), "no");
			}
		});
		mailOnSuccess.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

		
		final Label mailOnProcessLabel = new Label(group, SWT.NONE);
		mailOnProcessLabel.setLayoutData(new GridData());
		mailOnProcessLabel.setText("Mail On Process");

		mailOnProcess = new Combo(group, SWT.READ_ONLY);
		mailOnProcess.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("mail_on_process", mailOnProcess.getText(), "no");
			}
		});
		mailOnProcess.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

		final Label mailOnDelayLabel = new Label(group, SWT.NONE);
		mailOnDelayLabel.setText("Mail On Delay After Error");

		mailOnDelayAfterError = new Combo(group, SWT.READ_ONLY);
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
		

		final Label mailToLabel = new Label(group, SWT.NONE);
		mailToLabel.setText("Mail To");

		
		
		mailTo = new Text(group, SWT.BORDER);
		mailTo.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				listener.setValue("log_mail_to", mailTo.getText());
			}
		});
		mailTo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		
		final Label mailCcLabel = new Label(group, SWT.NONE);
		mailCcLabel.setText("Mail CC");

		mailCC = new Text(group, SWT.BORDER);
		mailCC.addModifyListener(new ModifyListener() {
			 public void modifyText(final ModifyEvent e) {
				 listener.setValue("log_mail_cc", mailCC.getText());
			 }
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_2.minimumWidth = 60;
		mailCC.setLayoutData(gridData_2);

		final Label mailBccLabel = new Label(group, SWT.NONE);
		mailBccLabel.setText("Mail BCC");

		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.minimumWidth = 60;
		mailBCC = new Text(group, SWT.BORDER);
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
		

		final Label logLevelLabel = new Label(group, SWT.NONE);
		logLevelLabel.setLayoutData(new GridData());
		logLevelLabel.setText("Log Level");

		LogLevel = new Combo(group, SWT.READ_ONLY);
		LogLevel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("log_level", LogLevel.getText());
			}
		});
		LogLevel.setLayoutData(new GridData());

		final Label label_2 = new Label(group, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_4.heightHint = 8;
		label_2.setLayoutData(gridData_4);

		final Label historyLabel = new Label(group, SWT.NONE);
		historyLabel.setText("History");

		cboHistory = new Combo(group, SWT.READ_ONLY);
		cboHistory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("history", cboHistory.getText());
			}
		});
		cboHistory.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

		final Label historyOnProcessLabel = new Label(group, SWT.NONE);
		historyOnProcessLabel.setText("History On Process");

		cboHistoryOnProcess = new Combo(group, SWT.NONE);
		cboHistoryOnProcess.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				setOnHistory();
			}
		});
		cboHistoryOnProcess.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit=Utils.isOnlyDigits(e.text);
			}
		});
		cboHistoryOnProcess.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
					cboHistoryOnProcess.setText(listener.getValue("history_on_process"));
				

				
				
			}
		});
		cboHistoryOnProcess.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

		final Label historyWithLogLabel = new Label(group, SWT.NONE);
		historyWithLogLabel.setText("History With Log");

		cboHistoryWithLog = new Combo(group, SWT.READ_ONLY);
		cboHistoryWithLog.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setValue("history_with_log", cboHistoryWithLog.getText());
			}
		});
		cboHistoryWithLog.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		

	 }


	 public void setToolTipText() {

		 mailTo.setToolTipText(Messages.getTooltip("job.settings.mail_to"));
		 mailCC.setToolTipText(Messages.getTooltip("job.settings.mail_cc"));
		 mailBCC.setToolTipText(Messages.getTooltip("job.settings.mail_bcc"));	
		 
		 mailOnError.setToolTipText(Messages.getTooltip("job.settings.mail_on_error")); 
		 mailOnWarning.setToolTipText(Messages.getTooltip("job.settings.mail_on_warning"));
		 mailOnSuccess.setToolTipText(Messages.getTooltip("job.settings.mail_on_success"));			
		 mailOnProcess.setToolTipText(Messages.getTooltip("job.settings.mail_on_process"));			
		 			
		 LogLevel.setToolTipText(Messages.getTooltip("job.settings.log_level"));

	 }

	 public boolean isUnsaved() {
		 return false;
	 }

	 public void apply() {

	 }
	 
	 private void setOnHistory() {
		 if(init)
			 return;
		boolean isDigit = true;
		char[] c = cboHistoryOnProcess.getText().toCharArray();
		for(int i = 0; i < c.length; i++) {
			isDigit = Character.isDigit(c[i]);
			if(!isDigit)
				break;
		}
		if(cboHistoryOnProcess.getText().equals("yes") || cboHistoryOnProcess.getText().equals("no") || isDigit)				
			listener.setValue("history_on_process", cboHistoryOnProcess.getText());
}

} // @jve:decl-index=0:visual-constraint="10,10"
