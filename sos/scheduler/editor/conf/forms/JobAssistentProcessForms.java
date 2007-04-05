package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;
import sos.scheduler.editor.conf.listeners.ExecuteListener;
import org.eclipse.swt.widgets.Combo;

import com.swtdesigner.SWTResourceManager;

public class JobAssistentProcessForms {
	
	private SchedulerDom      dom              = null;
	
	private ISchedulerUpdate  update           = null;
	
	private ExecuteListener    executeListener = null;
	
	private Button            butFinish        = null;
	
	private Button            butCancel        = null;
	
	private Button            butNext          = null;
	
	private Button            butShow          = null;		
	
	private Text              txtFile          = null;
	
	private Text              txtParameter     = null;
	
	private Text              txtLog           = null; 
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int               assistentType    = -1; 		
	
	private Shell processShell                 = null;
	
	private Combo             jobname          = null;
	
	public JobAssistentProcessForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
		dom = dom_;
		update = update_;
		assistentType = assistentType_;
		executeListener = new ExecuteListener(dom, job_);		
	}
	
	public void showProcessForm() {
		
		
		processShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		processShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		processShell.setLayout(gridLayout);
		processShell.setSize(480, 385);
		String step = "  ";
		if (Utils.getAttributeValue("order", executeListener.getJob()).equalsIgnoreCase("yes"))
			step = step + " [Step 5 of 9]";
		else 
			step = step + " [Step 5 of 8]";
		processShell.setText("Execute" + step);
		
		{
			final Group jobGroup = new Group(processShell, SWT.NONE);
			jobGroup.setText(" Job: " + Utils.getAttributeValue("name", executeListener.getJob()));
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, false, false, 2, 1);
			gridData.heightHint = 293;
			jobGroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.marginBottom = 10;
			gridLayout_1.numColumns = 2;
			jobGroup.setLayout(gridLayout_1);
			
			{
				final Text txtProcess = new Text(jobGroup, SWT.MULTI);
				final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
				gridData_1.horizontalIndent = -3;
				gridData_1.heightHint = 175;
				gridData_1.widthHint = 394;
				txtProcess.setLayoutData(gridData_1);
				txtProcess.setEnabled(false);
				txtProcess.setText(Messages.getString("assistent.process"));
			}
			
			{
				final Label lblFile = new Label(jobGroup, SWT.NONE);
				lblFile.setText("File");
			}
			txtFile = new Text(jobGroup, SWT.BORDER);
			txtFile.setFocus();
			txtFile.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(txtFile.getText() != null || txtFile.getText().trim().length() > 0) {
						executeListener.setFile(txtFile.getText());
					}
				}
			});
			txtFile.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
			txtFile.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			txtFile.setEnabled(false);
						
			txtFile.setText(executeListener.getFile() );
			
			{
				final Label lblParameter = new Label(jobGroup, SWT.NONE);
				lblParameter.setText("Parameter");
			}
			txtParameter = new Text(jobGroup, SWT.BORDER);
			txtParameter.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(txtParameter.getText() != null || txtParameter.getText().trim().length() > 0) {
						executeListener.setParam(txtParameter.getText());
					}
				}
			});
			txtParameter.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			txtParameter.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			txtParameter.setEnabled(false);
			
			txtParameter.setText(executeListener.getParam());
			
			{
				final Label lblRessources = new Label(jobGroup, SWT.NONE);
				lblRessources.setLayoutData(new GridData(SWT.DEFAULT, 17));
				lblRessources.setText("Log File");
				
			}
			txtLog = new Text(jobGroup, SWT.BORDER);
			txtLog.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if(txtLog.getText() != null || txtLog.getText().trim().length() > 0) {
						executeListener.setLogFile(txtLog.getText());
					}
				}
			});
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, false, false);
			gridData_1.heightHint = 0;
			txtLog.setLayoutData(gridData_1);
			txtLog.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			txtLog.setEnabled(false);
			
			txtLog.setText(executeListener.getLogFile());
		}
		
		
		if(executeListener.getFile() == null || executeListener.getFile().trim().length()==0){
			txtFile.setEnabled(true);
		}
		
		if(executeListener.getParam() == null || executeListener.getParam().trim().length() ==0) {
			txtParameter.setEnabled(true);
		}
		
		if(executeListener.getLogFile() == null || executeListener.getLogFile().trim().length() ==0) {
			txtLog.setEnabled(true);
		}
		
		
		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();		
		processShell.setBounds((screen.width - processShell.getBounds().width) /2, 
				(screen.height - processShell.getBounds().height) /2, 
				processShell.getBounds().width, 
				processShell.getBounds().height);
		processShell.open();
		
		final Composite composite = new Composite(processShell, SWT.NONE);
		composite.setLayoutData(new GridData());
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginWidth = 0;
		composite.setLayout(gridLayout_2);
		{
			butCancel = new Button(composite, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					close();
				}
			});
			butCancel.setText("Cancel");
		}
		
		final Composite composite_1 = new Composite(processShell, SWT.NONE);
		final GridData gridData = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData.widthHint = 123;
		composite_1.setLayoutData(gridData);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.marginWidth = 0;
		gridLayout_3.numColumns = 3;
		composite_1.setLayout(gridLayout_3);
		
		{
			butShow = new Button(composite_1, SWT.NONE);
			butShow.setLayoutData(new GridData());
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					MainWindow.message(processShell, Utils.getElementAsString(executeListener.getJob()), SWT.OK );
					txtFile.setFocus();
				}
			});
			butShow.setText("Show");
		}
		
		{
			butFinish = new Button(composite_1, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(jobname != null)
						jobname.setText(Utils.getAttributeValue("name",executeListener.getJob()));	
					JobsListener listener = new JobsListener(dom, update);
					listener.newImportJob(executeListener.getJob(), assistentType);
					MainWindow.message(processShell,  Messages.getString("assistent.finish") + "\n\n" + Utils.getElementAsString(executeListener.getJob()), SWT.OK );
					processShell.dispose();
					
				}
			});
			butFinish.setText("Finish");
		}
		{
			butNext = new Button(composite_1, SWT.NONE);
			butNext.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(Utils.getAttributeValue("order", executeListener.getJob()).equals("yes")) {
						JobAssistentTimeoutOrderForms timeout = new JobAssistentTimeoutOrderForms(dom, update, executeListener.getJob(), assistentType);
						timeout.showTimeOutForm();
						if(jobname != null) 													
							timeout.setJobname(jobname);
					} else {
						JobAssistentTimeoutForms timeout = new JobAssistentTimeoutForms(dom, update, executeListener.getJob(), assistentType);
						timeout.showTimeOutForm();												
					}
					processShell.dispose();					
				}
			});
			butNext.setText("Next");
		}
		setToolTipText();
		processShell.layout();		
	}
	
	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		txtFile.setToolTipText(Messages.getTooltip("assistent.process_file"));
		txtParameter.setToolTipText(Messages.getTooltip("assistent.process_parameter"));
		txtLog.setToolTipText(Messages.getTooltip("assistent.process_log"));
	}
	
	private void close() {
		int cont = MainWindow.message(processShell, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK)
			processShell.dispose();
	}
	
	public void setJobname(Combo jobname) {
		this.jobname = jobname;
}
}
