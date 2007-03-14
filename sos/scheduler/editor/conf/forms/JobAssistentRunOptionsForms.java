package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobsListener;


public class JobAssistentRunOptionsForms {
	
	private Element           job          = null;
	
	private SchedulerDom      dom          = null;
	
	private ISchedulerUpdate  update       = null;
	
	private JobsListener      listener     = null;
	
	private Button            butFinish    = null;
	
	private Button            butCancel    = null;
	
	private Button            butNext      = null;
	
	private Button            butShow      = null;				
			
	private Text              txtRunOptions     = null;
	
	private Button            butRuntime   = null; 
	
	private Button            butDirectoryMonitoring = null;
	
	private Text              txtDirectory = null; 
	
	private Text              txtRegExp    = null;  
	
	private Table             tableWatchDirectory = null; 
	
	private Button            butApply     = null; 
	
	private Button            butNewDirectory = null; 
	
	private Button            butRemoveDirectory = null; 		
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int assistentType = -1; 
	
	
	public JobAssistentRunOptionsForms(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
	}



	public void showRunOptionsForm(Element job_, int assistentType_) {
		assistentType = assistentType_;
		job = job_;
		final Shell shellRunOptions = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		shellRunOptions.setLayout(gridLayout);
		shellRunOptions.setSize(542, 423);
		shellRunOptions.setText("Run Options");

		final Group jobGroup = new Group(shellRunOptions, SWT.NONE);
		jobGroup.setText("Job");
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData_3.widthHint = 514;
		gridData_3.heightHint = 370;
		jobGroup.setLayoutData(gridData_3);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 3;
		jobGroup.setLayout(gridLayout_1);

		{
			txtRunOptions = new Text(jobGroup, SWT.MULTI | SWT.WRAP);
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, false, false, 3, 1);
			gridData.heightHint = 117;
			gridData.widthHint = 488;
			txtRunOptions.setLayoutData(gridData);
			txtRunOptions.setEditable(false);
			txtRunOptions.setText(Messages.getString("assistent.run_options"));
		}

		butDirectoryMonitoring = new Button(jobGroup, SWT.CHECK);
		final GridData gridData_2 = new GridData(GridData.CENTER, GridData.CENTER, false, false);
		gridData_2.heightHint = 32;
		butDirectoryMonitoring.setLayoutData(gridData_2);
		butDirectoryMonitoring.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(butDirectoryMonitoring.getSelection()) {
				  	txtDirectory.setEnabled(true);
				  	txtRegExp.setEnabled(true);
				  	tableWatchDirectory.setEnabled(true);
				  	butApply.setEnabled(true);
				  	butNewDirectory.setEnabled(true);  	
				} else {
					txtDirectory.setEnabled(false);
					txtRegExp.setEnabled(false);
					tableWatchDirectory.setEnabled(false);
					tableWatchDirectory.setEnabled(false);
				  	butApply.setEnabled(false);
				  	butNewDirectory.setEnabled(false);
				  	butRemoveDirectory.setEnabled(false);
				}
			}
		});
		butDirectoryMonitoring.setText("Directory Monitoring");

		butRuntime = new Button(jobGroup, SWT.CHECK);
		final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.FILL, false, false);
		gridData_1.horizontalIndent = 10;
		gridData_1.heightHint = 36;
		butRuntime.setLayoutData(gridData_1);
		butRuntime.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				MainWindow.message(shellRunOptions, Messages.getString("assistent.run_time"), SWT.OK );
			}
		});
		butRuntime.setText("The Job Run Time");
		new Label(jobGroup, SWT.NONE);

		final Label lblDirectory = new Label(jobGroup, SWT.NONE);
		lblDirectory.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		lblDirectory.setText("Directory");

		txtDirectory = new Text(jobGroup, SWT.BORDER);
		txtDirectory.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		txtDirectory.setEnabled(false);

		final Composite composite = new Composite(jobGroup, SWT.NONE);
		final GridData gridData_4 = new GridData(GridData.BEGINNING, GridData.FILL, false, false, 1, 3);
		gridData_4.widthHint = 60;
		composite.setLayoutData(gridData_4);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.marginHeight = 0;
		composite.setLayout(gridLayout_4);

		butApply = new Button(composite, SWT.NONE);
		butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butApply.setEnabled(false);
		butApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(butDirectoryMonitoring.getSelection()) {
									
					if(txtDirectory.getText() != null && txtDirectory.getText().length() > 0) {
						if(tableWatchDirectory.getSelectionCount()>0) {							
							TableItem item = tableWatchDirectory.getItem(tableWatchDirectory.getSelectionIndex());
							item.setText(0, txtDirectory.getText());	
							item.setText(1, txtRegExp.getText());			
							
							tableWatchDirectory.select(tableWatchDirectory.getItemCount()-1);
						} else {
							TableItem item = new TableItem(tableWatchDirectory, SWT.NONE);	
							item.setText(0, txtDirectory.getText());	
							item.setText(1, txtRegExp.getText());
							tableWatchDirectory.select(tableWatchDirectory.getItemCount()-1);
						}
					}
				}
			}
		});
		butApply.setText("Apply");

		butNewDirectory = new Button(composite, SWT.NONE);
		butNewDirectory.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butNewDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(butDirectoryMonitoring.getSelection()) {
					txtDirectory.setText("");
					txtRegExp.setText("");
					tableWatchDirectory.deselectAll();
				}
			}
		});
		butNewDirectory.setEnabled(false);
		butNewDirectory.setText("New");

		butRemoveDirectory = new Button(composite, SWT.NONE);
		butRemoveDirectory.setEnabled(false);
		butRemoveDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(butDirectoryMonitoring.getSelection()) {
					if(tableWatchDirectory.getSelectionCount()>0) {
						tableWatchDirectory.remove(tableWatchDirectory.getSelectionIndex());
						txtDirectory.setText("");
						txtRegExp.setText("");
					}
				}
			}
		});
		butRemoveDirectory.setText("Remove");

		final Label lblRegExp = new Label(jobGroup, SWT.NONE);
		lblRegExp.setText("Regular expression ");
 
		txtRegExp = new Text(jobGroup, SWT.BORDER);
		txtRegExp.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		txtRegExp.setEnabled(false);

		final Label watchDirectoryLabel = new Label(jobGroup, SWT.NONE);
		watchDirectoryLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		watchDirectoryLabel.setText("Watch Directory");

		tableWatchDirectory = new Table(jobGroup, SWT.FULL_SELECTION | SWT.BORDER);
		final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, false, false);
		gridData.heightHint = 55;
		gridData.widthHint = 284;
		gridData.minimumHeight = 200;
		tableWatchDirectory.setLayoutData(gridData);
		tableWatchDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(butDirectoryMonitoring.getSelection()) {
					if(tableWatchDirectory.getSelectionCount() > 0){
						butRemoveDirectory.setEnabled(true);
					} else {
						butRemoveDirectory.setEnabled(false);
					}
				}
			}
		});
		tableWatchDirectory.setEnabled(false);
		tableWatchDirectory.setLinesVisible(true);
		tableWatchDirectory.setHeaderVisible(true);

		final TableColumn newColumnTableColumn = new TableColumn(tableWatchDirectory, SWT.NONE);
		newColumnTableColumn.setWidth(156);
		newColumnTableColumn.setText("Directory");

		final TableColumn newColumnTableColumn_1 = new TableColumn(tableWatchDirectory, SWT.NONE);
		newColumnTableColumn_1.setWidth(186);
		newColumnTableColumn_1.setText("Regular Expression");

		final Composite composite_1 = new Composite(jobGroup, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData_5.widthHint = 99;
		composite_1.setLayoutData(gridData_5);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 2;
		composite_1.setLayout(gridLayout_2);

		{
			butFinish = new Button(composite_1, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					if(butDirectoryMonitoring.getSelection()) {						
						for(int i= 0; i < tableWatchDirectory.getItemCount(); i++) {
							Element dirMon = new Element("start_when_directory_changed");
							dirMon.setAttribute("directory", tableWatchDirectory.getItem(i).getText(0));
							if(tableWatchDirectory.getItem(i).getText(1) != null && tableWatchDirectory.getItem(i).getText(1).length() > 0) {
								dirMon.setAttribute("regex", tableWatchDirectory.getItem(i).getText(1));
							}
							job.addContent(dirMon);
						}
					  	
					}
					
					listener.newImportJob(job, assistentType);
					
					shellRunOptions.dispose();
				}
			});
			butFinish.setText("Finish");
		}
		{
			butCancel = new Button(composite_1, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					shellRunOptions.dispose();
				}
			});
			butCancel.setText("Cancel");
		}

		final Composite composite_2 = new Composite(jobGroup, SWT.NONE);
		composite_2.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false, 2, 1));
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.numColumns = 2;
		composite_2.setLayout(gridLayout_3);

		{
			butShow = new Button(composite_2, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(butDirectoryMonitoring.getSelection()) {						
						for(int i= 0; i < tableWatchDirectory.getItemCount(); i++) {
							Element dirMon = new Element("start_when_directory_changed");
							dirMon.setAttribute("directory", tableWatchDirectory.getItem(i).getText(0));
							if(tableWatchDirectory.getItem(i).getText(1) != null && tableWatchDirectory.getItem(i).getText(1).length() > 0) {
								dirMon.setAttribute("regex", tableWatchDirectory.getItem(i).getText(1));
							}
							job.addContent(dirMon);
						}
					  	
					}
					MainWindow.message(shellRunOptions, Utils.getElementAsString(job), SWT.OK );
				}
			});
			butShow.setText("Show");
		}
		butNext = new Button(composite_2, SWT.NONE);
		butNext.setLayoutData(new GridData());
		butNext.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(butDirectoryMonitoring.getSelection()) {						
					for(int i= 0; i < tableWatchDirectory.getItemCount(); i++) {
						Element dirMon = new Element("start_when_directory_changed");
						dirMon.setAttribute("directory", tableWatchDirectory.getItem(i).getText(0));
						if(tableWatchDirectory.getItem(i).getText(1) != null && tableWatchDirectory.getItem(i).getText(1).length() > 0) {
							dirMon.setAttribute("regex", tableWatchDirectory.getItem(i).getText(1));
						}
						job.addContent(dirMon);
					}
				  	
				}
				JobAssistentDelayOrderAfterSetbackForm setBack = new JobAssistentDelayOrderAfterSetbackForm(dom, update);
				setBack.showDelayOrderAfterSetbackForm(job, assistentType);
				shellRunOptions.dispose();
				//MainWindow.message(shellRunOptions, "run options??????", SWT.OK );					
			}
		});
		butNext.setText("Next");
		butNext.setEnabled(false);
		{
			if(Utils.getAttributeValue("order", job).equalsIgnoreCase("no")) {
			} else {
				butNext.setEnabled(true);
			}
		}

		{
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("language", script) != null &&
					Utils.getAttributeValue("language", script).trim().length() > 0	){
			}
		}

		
		{
			Element script = job.getChild("script");
			if(script != null && Utils.getAttributeValue("filename", script) != null &&
					Utils.getAttributeValue("filename", script).trim().length() > 0	){
			}
		}

		{
			if(job != null){
				Element script = job.getChild("script");				 
				if(script!= null) {
					java.util.List includes = script.getChildren("include");
					for (int i = 0; i < includes.size(); i++) {
						Element include = (Element)includes.get(i);
						if( Utils.getAttributeValue("file", include) != null &&
								Utils.getAttributeValue("file", include).length() > 0) {
						}
					}
					
				}
				
			}
		}
		
		shellRunOptions.open();
		setToolTipText();
		shellRunOptions.layout();		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));			
		butDirectoryMonitoring.setToolTipText(Messages.getTooltip("assistent.directory_monitoring"));
		butRuntime.setToolTipText(Messages.getTooltip("assistent.run_time"));
		txtDirectory.setToolTipText(Messages.getTooltip("assistent.directory"));
		txtRegExp.setToolTipText(Messages.getTooltip("assistent.reg_exp"));
		tableWatchDirectory.setToolTipText(Messages.getTooltip("assistent.table_watch_directory"));
		butApply.setToolTipText(Messages.getTooltip("assistent.apply_directory"));
		butNewDirectory.setToolTipText(Messages.getTooltip("assistent.new_directory"));
		butRemoveDirectory.setToolTipText(Messages.getTooltip("assistent.remove_directory"));
	}
}
