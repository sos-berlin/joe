package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobOptionsListener;


public class JobAssistentMonitoringDirectoryForms {
	
	private Element            job                      = null;
	
	private SchedulerDom       dom                      = null;
	
	//private ISchedulerUpdate   update                   = null;
	
	private JobOptionsListener optionlistener           = null;
	
	private Button             butFinish                = null;
	
	private Button             butCancel                = null;
	
	private Button             butNext                  = null;
	
	private Button             butShow                  = null;				
	
	private Text               txtMonitoringDirectory   = null;
	
	private Shell              shellRunOptions          = null; 
	
	private Text               txtDirectory             = null; 
	
	private Text               txtRegExp                = null;  
	
	private Table              tableWatchDirectory      = null; 
	
	private Button             butApply                 = null; 
	
	private Button             butNewDirectory          = null; 
	
	private Button             butRemoveDirectory       = null; 		
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	//private int                assistentType            = -1; 
	
	private Element            jobBackUp                = null;    
	
	/** Hilsvariable für das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog über den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	private boolean            closeDialog              = false;
	
	
	public JobAssistentMonitoringDirectoryForms(SchedulerDom dom_, ISchedulerUpdate update_, Element job_, int assistentType_) {
		dom = dom_;
		//update = update_;
		optionlistener = new JobOptionsListener(dom, job_);
		job = job_;
		jobBackUp = (Element)job_.clone();
		//assistentType = assistentType_;
		
	}
	
	public void showMonitoringDirectoryForm() {
		
		shellRunOptions = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		shellRunOptions.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {
				if(!closeDialog)
					close();
				e.doit = shellRunOptions.isDisposed();
			}
		});
		shellRunOptions.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shellRunOptions.setLayout(gridLayout);
		shellRunOptions.setSize(542, 377);
		shellRunOptions.setText("Monitoring Directory");
		
		final Group jobGroup = new Group(shellRunOptions, SWT.NONE);
		jobGroup.setText("Job");
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
		gridData_3.widthHint = 514;
		gridData_3.heightHint = 283;
		jobGroup.setLayoutData(gridData_3);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginWidth = 10;
		gridLayout_1.marginTop = 10;
		gridLayout_1.marginRight = 10;
		gridLayout_1.marginLeft = 10;
		gridLayout_1.marginHeight = 10;
		gridLayout_1.marginBottom = 10;
		gridLayout_1.numColumns = 3;
		jobGroup.setLayout(gridLayout_1);
		
		{
			txtMonitoringDirectory = new Text(jobGroup, SWT.MULTI | SWT.WRAP);
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, false, false, 3, 1);
			gridData.horizontalIndent = -3;
			gridData.heightHint = 117;
			gridData.widthHint = 466;
			txtMonitoringDirectory.setLayoutData(gridData);
			txtMonitoringDirectory.setEditable(false);
			txtMonitoringDirectory.setText(Messages.getString("assistant.directory_monitoring"));
		}
		
		final Label lblDirectory = new Label(jobGroup, SWT.NONE);
		lblDirectory.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		lblDirectory.setText("Directory");
		
		txtDirectory = new Text(jobGroup, SWT.BORDER);		
		txtDirectory.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR)
					apply();
			}
		});
		txtDirectory.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		txtDirectory.setFocus();
		
		final Composite composite = new Composite(jobGroup, SWT.NONE);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.FILL, false, false, 1, 2);
		gridData_4.heightHint = 59;
		gridData_4.widthHint = 52;
		composite.setLayoutData(gridData_4);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.horizontalSpacing = 0;
		gridLayout_4.marginWidth = 0;
		gridLayout_4.marginHeight = 0;
		composite.setLayout(gridLayout_4);
		
		butApply = new Button(composite, SWT.NONE);
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_2.widthHint = 53;
		butApply.setLayoutData(gridData_2);
		butApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				apply();				
			}
		});
		butApply.setText("Apply Dir");
		
		butNewDirectory = new Button(composite, SWT.NONE);
		final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.widthHint = 47;
		butNewDirectory.setLayoutData(gridData);
		butNewDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				txtDirectory.setText("");
				txtRegExp.setText("");
				txtDirectory.setFocus();
				tableWatchDirectory.deselectAll();
				
			}
		});
		butNewDirectory.setText("New");
		
		final Label lblRegExp = new Label(jobGroup, SWT.NONE);
		lblRegExp.setText("Regular expression ");
		
		txtRegExp = new Text(jobGroup, SWT.BORDER);
		txtRegExp.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR)
					apply();
			}
		});
		txtRegExp.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		
		tableWatchDirectory = new Table(jobGroup, SWT.FULL_SELECTION | SWT.BORDER);
		final GridData gridData_7 = new GridData(GridData.BEGINNING, GridData.FILL, false, false, 2, 1);
		gridData_7.heightHint = 55;
		gridData_7.widthHint = 391;
		gridData_7.minimumHeight = 200;
		tableWatchDirectory.setLayoutData(gridData_7);
		optionlistener.fillDirectories(tableWatchDirectory);
		tableWatchDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				if(tableWatchDirectory.getSelectionCount() > 0){
					butRemoveDirectory.setEnabled(true);
				} else {
					butRemoveDirectory.setEnabled(false);
				}
				
			}
		});
		tableWatchDirectory.setEnabled(true);
		tableWatchDirectory.setLinesVisible(true);
		tableWatchDirectory.setHeaderVisible(true);
		
		final TableColumn newColumnTableColumn = new TableColumn(tableWatchDirectory, SWT.NONE);
		newColumnTableColumn.setWidth(156);
		newColumnTableColumn.setText("Directory");
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(tableWatchDirectory, SWT.NONE);
		newColumnTableColumn_1.setWidth(186);
		newColumnTableColumn_1.setText("Regular Expression");
		
		butRemoveDirectory = new Button(jobGroup, SWT.NONE);
		butRemoveDirectory.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butRemoveDirectory.setEnabled(false);
		butRemoveDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				if(tableWatchDirectory.getSelectionCount()>0) {
					optionlistener.deleteDirectory(tableWatchDirectory.getSelectionIndex());				
				}
				
			}
		});
		butRemoveDirectory.setText("Remove");
		
		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();		
		shellRunOptions.setBounds((screen.width - shellRunOptions.getBounds().width) /2, 
				(screen.height - shellRunOptions.getBounds().height) /2, 
				shellRunOptions.getBounds().width, 
				shellRunOptions.getBounds().height);
		
		shellRunOptions.open();
		
		final Composite composite_1 = new Composite(shellRunOptions, SWT.NONE);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginWidth = 0;
		composite_1.setLayout(gridLayout_2);
		{
			butCancel = new Button(composite_1, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					
					close();
				}
			});
			butCancel.setText("Close");
		}
		
		final Composite composite_2 = new Composite(shellRunOptions, SWT.NONE);
		composite_2.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.marginWidth = 0;
		gridLayout_3.numColumns = 3;
		composite_2.setLayout(gridLayout_3);
		
		{
			butShow = new Button(composite_2, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					MainWindow.message(shellRunOptions, Utils.getElementAsString(optionlistener.getJob()), SWT.OK );
					txtDirectory.setFocus();
				}
			});
			butShow.setText("Show");
		}
		
		{
			butFinish = new Button(composite_2, SWT.NONE);
			butFinish.setLayoutData(new GridData(11, SWT.DEFAULT));
			butFinish.setVisible(false);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					closeDialog = true;
					shellRunOptions.dispose();
				}
			});
			butFinish.setText("Finish");
		}
		butNext = new Button(composite_2, SWT.NONE);
		butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_6.widthHint = 54;
		butNext.setLayoutData(gridData_6);
		butNext.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				closeDialog = true;
				shellRunOptions.dispose();								
			}
		});
		butNext.setText("Apply");
		txtDirectory.setFocus();
		setToolTipText();
		shellRunOptions.layout();
		
	}
	
	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.close"));
		butNext.setToolTipText(Messages.getTooltip("assistent.apply"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));			
		txtDirectory.setToolTipText(Messages.getTooltip("assistent.directory"));
		txtRegExp.setToolTipText(Messages.getTooltip("assistent.reg_exp"));
		tableWatchDirectory.setToolTipText(Messages.getTooltip("assistent.table_watch_directory"));
		butApply.setToolTipText(Messages.getTooltip("assistent.apply_directory"));
		butNewDirectory.setToolTipText(Messages.getTooltip("assistent.new_directory"));
		butRemoveDirectory.setToolTipText(Messages.getTooltip("assistent.remove_directory"));
	}
	
	private void close() {
		int cont = MainWindow.message(shellRunOptions, sos.scheduler.editor.app.Messages.getString("assistent.close"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK) {Utils.getElementAsString((Element)jobBackUp);
		job.setContent(jobBackUp.cloneContent());
		shellRunOptions.dispose();	
		}
	}
	
	private void apply() {		
		if(txtDirectory.getText()!=null && txtDirectory.getText().trim().length()>0) {
			optionlistener.newDirectory();
			optionlistener.applyDirectory(txtDirectory.getText(), txtRegExp.getText());
			optionlistener.fillDirectories(tableWatchDirectory);
		}
		txtDirectory.setFocus();
	}
}
