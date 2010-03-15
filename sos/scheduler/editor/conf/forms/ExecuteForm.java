package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
//import org.eclipse.swt.widgets.Table;
//import org.eclipse.swt.widgets.TableColumn;
//import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.ExecuteListener;
import sos.scheduler.editor.conf.listeners.ScriptListener;

public class ExecuteForm extends Composite implements IUnsaved, IUpdateLanguage {


	private ExecuteListener listener      = null;

	private Group           group         = null;

	private Group           gExecutable   = null;

	private Label           label1        = null;

	private Text            tExecuteFile  = null;

	private Button          bIgnoreError  = null;

	private Label           label3        = null;

	private Text            tParameter    = null;

	private Label           label4        = null;

	private Text            tLogFile      = null;

	private Button          bIgnoreSignal = null;

	// private Button          bExecutable   = null;

	//private Button          bScript       = null;

	private ScriptForm      scriptForm    = null;

	private Label           label5        = null;

	//private Button          bNoExecute    = null;


	private ISchedulerUpdate update       = null;

	private boolean          init         = false;

	private TabFolder        tabFolder    = null;

	private static String RUN_EXECUTABLE  = "Run Executable";

	private static String SCRIPT  = "Script";

	private Composite composite_1 = null;

	public ExecuteForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate update_) {
		super(parent, style);
		init = true;
		update = update_;


		listener = new ExecuteListener(dom, job);

		initialize();
		setToolTipText();




		scriptForm.setAttributes(dom, job, Editor.EXECUTE);
		//if(job.getChild("script") == null && job.getChild("process")==null)
			//	scriptForm.setLanguage(ScriptListener.JAVA);
		fillForm();

		this.group.setEnabled(Utils.isElementEnabled("job", dom, job));

		init = false;

	}


	public void apply() {
		//if (bApply.isEnabled())
		//    applyVariable();
		if (scriptForm.isUnsaved())
			scriptForm.apply();
	}


	public boolean isUnsaved() {
		return scriptForm.isUnsaved(); //|| bApply.isEnabled();
	}


	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(729, 532));

	}


	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout4 = new GridLayout();
		group = new Group(this, SWT.NONE);
		group.setText("Execute");
		createGroup1();
		group.setLayout(gridLayout4);
		createGroup12();

		tabFolder = new TabFolder(group, SWT.NONE);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				reInit();

			}
		});
		tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		final TabItem tabItemScript = new TabItem(tabFolder, SWT.NONE);
		tabItemScript.setText(SCRIPT);

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		tabItemScript.setControl(composite);
		scriptForm = new ScriptForm(composite, SWT.NONE, update);
		scriptForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		if(listener.isExecutable()) {
			final TabItem tabItemRunExecutable  = new TabItem(tabFolder, SWT.NONE);

			tabItemRunExecutable.setText(RUN_EXECUTABLE);

			composite_1 = new Composite(tabFolder, SWT.NONE);
			composite_1.setLayout(new GridLayout());
			tabItemRunExecutable.setControl(composite_1);
			GridData gridData61 = new org.eclipse.swt.layout.GridData();
			gridData61.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
			gridData61.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
			GridData gridData41 = new org.eclipse.swt.layout.GridData();
			gridData41.grabExcessHorizontalSpace = false;
			gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
			gridData41.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
			gridData41.horizontalSpan = 2;
			GridData gridData21 = new org.eclipse.swt.layout.GridData();
			gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
			gridData21.grabExcessHorizontalSpace = false;
			gridData21.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
			GridData gridData12 = new org.eclipse.swt.layout.GridData();
			gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
			gridData12.grabExcessHorizontalSpace = true;
			gridData12.horizontalSpan = 3;
			gridData12.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
			GridData gridData3 = new org.eclipse.swt.layout.GridData();
			gridData3.horizontalSpan = 3;
			gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
			gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
			GridData gridData2 = new org.eclipse.swt.layout.GridData();
			gridData2.horizontalSpan = 3;
			gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
			gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 4;
			gExecutable = new Group(composite_1, SWT.NONE);
			gExecutable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
			gExecutable.setText("Run Executable");
			gExecutable.setLayout(gridLayout);
			label1 = new Label(gExecutable, SWT.NONE);
			label1.setText("File");
			tExecuteFile = new Text(gExecutable, SWT.BORDER);
			tExecuteFile.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					tExecuteFile.selectAll();
				}
			});
			tExecuteFile.setLayoutData(gridData12);
			tExecuteFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
				public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
					if(!init) {            		            			
						listener.setFile(tExecuteFile.getText());
					}
				}
			});
			label3 = new Label(gExecutable, SWT.NONE);
			label3.setText("Parameter:   ");
			tParameter = new Text(gExecutable, SWT.BORDER);
			tParameter.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					tParameter.selectAll();
				}
			});
			tParameter.setLayoutData(gridData2);
			tParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
				public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
					if(!init) 
						listener.setParam(tParameter.getText());
				}
			});
			label4 = new Label(gExecutable, SWT.NONE);
			label4.setText("Log file:");
			tLogFile = new Text(gExecutable, SWT.BORDER);
			tLogFile.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					tLogFile.selectAll();		
				}
			});
			tLogFile.setLayoutData(gridData3);
			tLogFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
				public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
					if(!init) 
						listener.setLogFile(tLogFile.getText());
				}
			});
			label5 = new Label(gExecutable, SWT.NONE);
			label5.setText("Ignore:");
			label5.setLayoutData(gridData61);
			bIgnoreSignal = new Button(gExecutable, SWT.CHECK);
			bIgnoreSignal.setText("Signal");
			bIgnoreSignal.setLayoutData(gridData21);
			bIgnoreSignal.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if(!init) 
						listener.setIgnoreSignal(bIgnoreSignal.getSelection());
				}
			});
			bIgnoreError = new Button(gExecutable, SWT.CHECK);
			bIgnoreError.setText("Error");
			bIgnoreError.setLayoutData(gridData41);
			bIgnoreError.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if(!init) 
						listener.setIgnoreError(bIgnoreError.getSelection());
				}
			});
		}
		/*bExecutable = new Button(composite_1, SWT.RADIO);
        bExecutable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	if(!init) {
            		if (bExecutable.getSelection()) {
            			scriptForm.setLanguage(ScriptListener.NONE);
            			listener.setExecutable(true);
            			fillForm();
            		}
            	}
            }
        });
		 */
		/*bNoExecute = new Button(composite_2, SWT.RADIO);
        bNoExecute.setBounds(140, 57,13, 16);
        bNoExecute.setSelection(true);
        bNoExecute.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	if(!init) {
            		if (bNoExecute.getSelection()) {
            			scriptForm.setLanguage(ScriptListener.NONE);
            			listener.setNothing();
            			fillForm();
            		}
            	}
            }
        });
		 */
		createScriptForm();
	}


	/**
	 * This method initializes group1
	 */
	private void createGroup1() {
	}


	/**
	 * This method initializes scriptForm
	 */
	private void createScriptForm() {
	}


	/**
	 * This method initializes group1
	 */
	private void createGroup12() {
		createTable();
	}


	/**
	 * This method initializes table
	 */
	private void createTable() {
	}

	private void fillForm() {
		setEnabled(false);
		//scriptForm.setFullEnabled(false);

		if (listener.isScript()) {
			//bNoExecute.setSelection(false);
			//bExecutable.setSelection(false);
			//bScript.setSelection(true);

			scriptForm.setFullEnabled(true);
			tabFolder.setSelection(0);
		} else if (listener.isExecutable()) {
			//bNoExecute.setSelection(false);
			//bExecutable.setSelection(true);
			//bScript.setSelection(false);
			tabFolder.setSelection(1);
			setEnabled(true);

			if (!tExecuteFile.getText().equals("") && listener.getFile().equals(""))
				listener.setFile(tExecuteFile.getText());
			tExecuteFile.setText(listener.getFile());

			if (!tLogFile.getText().equals("") && listener.getLogFile().equals(""))
				listener.setLogFile(tLogFile.getText());
			tLogFile.setText(listener.getLogFile());

			if (!tParameter.getText().equals("") && listener.getParam().equals(""))
				listener.setParam(tParameter.getText());
			tParameter.setText(listener.getParam());

			if (bIgnoreError.getSelection() && !listener.isIgnoreError())
				listener.setIgnoreError(true);
			bIgnoreError.setSelection(listener.isIgnoreError());

			if (bIgnoreSignal.getSelection() && !listener.isIgnoreSignal())
				listener.setIgnoreSignal(true);
			bIgnoreSignal.setSelection(listener.isIgnoreSignal());

			tExecuteFile.setFocus();
		} else {
			tabFolder.setSelection(0);
			//bNoExecute.setSelection(true);
			//bExecutable.setSelection(false);
			//bScript.setSelection(false);
		}

	}
	

	public void setEnabled(boolean enabled) {
		enabled = true;
		if(listener.isExecutable()) {
			tExecuteFile.setEnabled(enabled);
			tLogFile.setEnabled(enabled);
			tParameter.setEnabled(enabled);
			bIgnoreError.setEnabled(enabled);
			bIgnoreSignal.setEnabled(enabled);
		}
	}




	public void setToolTipText() {
	
		if(listener.isExecutable()) {
			tExecuteFile.setToolTipText(Messages.getTooltip("process.file"));
			tParameter.setToolTipText(Messages.getTooltip("process.param"));
			tLogFile.setToolTipText(Messages.getTooltip("process.log_file"));
			bIgnoreSignal.setToolTipText(Messages.getTooltip("process.ignore_signal"));
			bIgnoreError.setToolTipText(Messages.getTooltip("process.ignore_error"));
		}

	}

	private void  reInit() {
		if(!init) {    		    		

			if(tabFolder.getSelection()[0].getText().equals(SCRIPT)){

				/*if(listener.getJob() != null && listener.getJob().getChild("process") != null) {
    	    		int c = sos.scheduler.editor.app.MainWindow.message("Do you want really remove Run Executable File an put a new Script?", SWT.YES | SWT.NO | SWT.ICON_WARNING);
    	    		if(c != SWT.YES) {    	    			
    	    			return;
    	    		}

    	    	}*/

				listener.setExecutable(false);    			
				scriptForm.setLanguage(scriptForm.getSelectionLanguageButton());
				if(!init)
					fillForm();
			} else if(tabFolder.getSelection()[0].getText().equals(RUN_EXECUTABLE)){
				if(listener.getJob() != null && listener.getJob().getChild("script") != null) {
					sos.scheduler.editor.app.MainWindow.message("Please select None to define a new Run Executable?", SWT.ICON_WARNING);
					composite_1.setEnabled(false);
					tabFolder.setSelection(0);
					return;
				} else {
					composite_1.setEnabled(true);
				}
				int p = tExecuteFile.getText().length() + tLogFile.getText().length() + tParameter.getText().length();
				if(p > 0) {    				    			
					scriptForm.setLanguage(ScriptListener.NONE);
					listener.setExecutable(true);    			
					fillForm();
				}
			}
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
