package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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
import sos.scheduler.editor.app.Messages;

 
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobEmailSettings extends FormBaseClass {

    
    private Combo   mailOnDelayAfterError   = null;
    private Text    mailCC                  = null;
    private Text    mailBCC                 = null;
    private Combo   mailOnError             = null;
    private Combo   mailOnWarning           = null;
    private Combo   mailOnSuccess           = null;
    private Combo   mailOnProcess           = null;
    private Text    mailTo                  = null;

    @SuppressWarnings("unused")
    private final String    conClassName            = "JobEmailSettings";
    @SuppressWarnings("unused")
    private final String    conSVNVersion           = "$Id$";

    private boolean init = true;
    private JobListener objJobDataProvider = null;

    public JobEmailSettings(Composite pParentComposite, JobListener pobjJobDataProvider) {
        super(pParentComposite, pobjJobDataProvider);
        objJobDataProvider = pobjJobDataProvider;
        
        init = true;
        createGroup();
        initForm();
        init = false;
    }

    public void apply() {
        // if (isUnsaved())
        // addParam();
    }

    public boolean isUnsaved() {
        return false;
    }

    public void refreshContent () {
    }
    
    private void createGroup() {
 
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

        Group group4EMail = new Group(objParent, SWT.NONE);
        group4EMail.setText(Messages.getLabel("Notifications"));
        group4EMail.setLayout(gridLayout4EMailGroup);
        group4EMail.setLayoutData(gridData);

        Label labelMailOnError = new Label(group4EMail, SWT.NONE);
        labelMailOnError.setText(Messages.getLabel("MailOnError"));

        mailOnError = new Combo(group4EMail, intComboBoxStyle);
        mailOnError.setItems(new String[]{"yes", "no", ""}); 
        mailOnError.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
                objJobDataProvider.setValue("mail_on_error", mailOnError.getText(), "no");
            }
        });
        GridData gd_mailOnError = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_mailOnError.minimumWidth = 150;
        mailOnError.setLayoutData(gd_mailOnError);
        Label labelMailOnWarning = new Label(group4EMail, SWT.NONE);
        labelMailOnWarning.setText(Messages.getLabel("MailOnWarning"));

        mailOnWarning = new Combo(group4EMail, intComboBoxStyle);
        mailOnWarning.setItems(new String[]{"yes", "no", ""}); 
        mailOnWarning.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                mailOnDelayAfterError.setEnabled(mailOnWarning.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
                objJobDataProvider.setValue("mail_on_warning", mailOnWarning.getText(), "no");
            }
        });
        GridData gd_mailOnWarning = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_mailOnWarning.minimumWidth = 150;
        mailOnWarning.setLayoutData(gd_mailOnWarning);
        Label label3 = new Label(group4EMail, SWT.NONE);
        label3.setText(Messages.getLabel("MailOnSuccess"));

        mailOnSuccess = new Combo(group4EMail, intComboBoxStyle);
        mailOnSuccess.setItems(new String[]{"yes", "no", ""});      
        mailOnSuccess.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                objJobDataProvider.setValue("mail_on_success", mailOnSuccess.getText(), "no");
            }
        });
        GridData gd_mailOnSuccess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_mailOnSuccess.minimumWidth = 150;
        mailOnSuccess.setLayoutData(gd_mailOnSuccess);

        final Label mailOnProcessLabel = new Label(group4EMail, SWT.NONE);
        mailOnProcessLabel.setText(Messages.getLabel("Mail On Process"));

        mailOnProcess = new Combo(group4EMail, intComboBoxStyle);
        mailOnProcess.setItems(new String[]{"yes", "no", ""});
        mailOnProcess.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                objJobDataProvider.setValue("mail_on_process", mailOnProcess.getText(), "no");
            }
        });
        GridData gd_mailOnProcess = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gd_mailOnProcess.minimumWidth = 150;
        mailOnProcess.setLayoutData(gd_mailOnProcess);

        final Label mailOnDelayLabel = new Label(group4EMail, SWT.NONE);
        mailOnDelayLabel.setText(Messages.getLabel("MailOnDelayAfterError"));

        mailOnDelayAfterError = new Combo(group4EMail, intComboBoxStyle);
        mailOnDelayAfterError.setItems(new String[]{"all", "first_only", "last_only", "first_and_last_only", ""});        
        mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
        mailOnDelayAfterError.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                objJobDataProvider.setValue("mail_on_delay_after_error", mailOnDelayAfterError.getText(), "no");
            }
        });
        mailOnDelayAfterError.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

        final Label ddddLabel = new Label(group4EMail, SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
        gridData_1.heightHint = 8;
        ddddLabel.setLayoutData(gridData_1);

        final Label mailToLabel = new Label(group4EMail, SWT.NONE);
        mailToLabel.setText(Messages.getLabel("MailTo"));

        mailTo = new Text(group4EMail, SWT.BORDER);
        mailTo.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                mailTo.selectAll();
            }
        });
        mailTo.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                objJobDataProvider.setValue("log_mail_to", mailTo.getText());
            }
        });
        mailTo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Label mailCcLabel = new Label(group4EMail, SWT.NONE);
        mailCcLabel.setText(Messages.getLabel("MailCC"));

        mailCC = new Text(group4EMail, SWT.BORDER);
        mailCC.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                mailCC.selectAll();
            }
        });
        mailCC.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                objJobDataProvider.setValue("log_mail_cc", mailCC.getText());
            }
        });
        final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_2.minimumWidth = 60;
        mailCC.setLayoutData(gridData_2);

        final Label mailBccLabel = new Label(group4EMail, SWT.NONE);
        mailBccLabel.setText(Messages.getLabel("MailBCC"));

        gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData.minimumWidth = 60;
        mailBCC = new Text(group4EMail, SWT.BORDER);
        mailBCC.addFocusListener(new FocusAdapter() {
            public void focusGained(final org.eclipse.swt.events.FocusEvent e) {
                mailBCC.selectAll();
            }
        });
        mailBCC.setLayoutData(gridData);
        mailBCC.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                objJobDataProvider.setValue("log_mail_bcc", mailBCC.getText());
            }
        });

        final Label label_1 = new Label(group4EMail, SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
        gridData_3.heightHint = 8;
        label_1.setLayoutData(gridData_3);

 
        objParent.layout();
    }
     
   
    private void initForm(){
         
        mailOnError.setText(objJobDataProvider.getValue("mail_on_error")); 
        mailOnWarning.setText(objJobDataProvider.getValue("mail_on_warning"));
        mailOnSuccess.setText(objJobDataProvider.getValue("mail_on_success"));        
        mailOnProcess.setText(objJobDataProvider.getValue("mail_on_process"));                
        mailOnDelayAfterError.setText(objJobDataProvider.getValue("mail_on_delay_after_error"));
        
        mailOnDelayAfterError.setEnabled(mailOnError.getText().equals("yes") || mailOnWarning.getText().equals("yes"));
        
        mailTo.setText(objJobDataProvider.getValue("log_mail_to"));
        mailCC.setText(objJobDataProvider.getValue("log_mail_cc"));
        mailBCC.setText(objJobDataProvider.getValue("log_mail_bcc"));     
         
                
    }
}
