package sos.scheduler.editor.actions.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.util.SOSString;
import sos.scheduler.editor.actions.forms.ActionsForm;
import sos.scheduler.editor.actions.ActionsDom;


import java.io.File;


public class SaveEventsDialogForm {


	private              Shell           _shell                         = null;
	
	private              SOSString        sosString                     = null;
	
	private              Group            eventgroup                    = null; 
	
	private              Text             txtName                       = null;
	
	private              Text             txtJobChain                   = null;
	
	private              Text             txtJob                        = null;
	
	private              Text             txtEventClass                 = null;
	
	private              Button           butDirectory                  = null; 

	private              Label            lblDirectory                  = null;
	
	private              ActionsDom       dom                           = null;
	
	private              String           filename                      = null;
	
	private              Button           butApply                      = null;

	public SaveEventsDialogForm() {
		try {
		ActionsForm f = (ActionsForm)MainWindow.getContainer().getCurrentEditor();
		dom = f.getDom();		
		sosString = new SOSString();
		showForm();
		init();
		
		while (!_shell.isDisposed()) {
			if (!_shell.getDisplay().readAndDispatch())
				_shell.getDisplay().sleep();
		}
		_shell.getDisplay().dispose();
		
		} catch(Exception e) {
			
				try {
	    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
	    		} catch(Exception ee) {
	    			//tu nichts
	    		}
		}

	}

	private void init() throws Exception {
		String filename = "";
		try {
			filename = dom.getFilename();
			
			if(sosString.parseToString(filename).length() == 0)
				return;
			
			
			File f = new File(filename);
			
			if(f.getParent() != null)
				lblDirectory.setText(normalized(f.getParent()));
			
			
			String[] split = f.getName().split("\\.");
			
			if (f.getName().endsWith("job_chain.actions.xml")) {
				txtJobChain.setText(split[0]);
				txtName.setText(f.getName().substring(split[0].length()+1, f.getName().indexOf("job_chain.actions.xml")-1));
				txtJobChain.setEnabled(true);
				txtJob.setEnabled(false);
				txtEventClass.setEnabled(false);
			} else if (f.getName().endsWith("job.actions.xml")) {
				txtJob.setText(split[0]);
				txtName.setText(f.getName().substring(split[0].length()+1, f.getName().indexOf("job.actions.xml")-1));
				txtJobChain.setEnabled(false);
				txtJob.setEnabled(true);
				txtEventClass.setEnabled(false);
			} else if (f.getName().endsWith("event_class.actions.xml")) {
				txtEventClass.setText(split[0]);
				txtName.setText(f.getName().substring(split[0].length()+1, f.getName().indexOf("event_class.actions.xml")-1));
				txtJobChain.setEnabled(false);
				txtJob.setEnabled(false);
				txtEventClass.setEnabled(true);
			} else  if (f.getName().endsWith(".actions.xml")) {				
				txtName.setText(f.getName().substring(0, f.getName().indexOf("actions.xml")-1));
			}
			
			
		} catch (Exception e) {
			try {
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
    		} catch(Exception ee) {
    			//tu nichts
    		}
    		throw e;
		}
		
	}
	public void showForm() {

		_shell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE
				| SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);

		_shell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(final TraverseEvent e) {				
				if(e.detail == SWT.TRAVERSE_ESCAPE) {					
					_shell.dispose();
				}
			}
		});

		_shell.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginBottom = 5;
		_shell.setLayout(gridLayout);
		_shell.setSize(425, 240);



		_shell.setText("Save Eventhandler");

		{
			eventgroup = new Group(_shell, SWT.NONE);
			eventgroup.setText("Eventhandler Name Specification");
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, true, true);
			gridData.widthHint = 581;
			gridData.heightHint = 77;
			eventgroup.setLayoutData(gridData);

			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 4;
			gridLayout_1.verticalSpacing = 10;
			gridLayout_1.horizontalSpacing = 10;
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.marginBottom = 10;
			eventgroup.setLayout(gridLayout_1);


			final Label nameLabel = new Label(eventgroup, SWT.NONE);
			nameLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
			nameLabel.setText("Name");



			txtName = new Text(eventgroup, SWT.BORDER);
			txtName.setBackground(SWTResourceManager.getColor(255, 255, 217));
			txtName.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					refresh();
				}
			});
			txtName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

			final Label jobketteLabel = new Label(eventgroup, SWT.NONE);
			jobketteLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
			jobketteLabel.setText("Jobchain");

			txtJobChain = new Text(eventgroup, SWT.BORDER);
			txtJobChain.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					refresh();
				}
			});
			txtJobChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

			final Label jobLabel = new Label(eventgroup, SWT.NONE);
			jobLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
			jobLabel.setText("Job");



			txtJob = new Text(eventgroup, SWT.BORDER);
			txtJob.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					refresh();
				}
			});
			txtJob.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));



			final Label eventClassLabel = new Label(eventgroup, SWT.NONE);
			eventClassLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
			eventClassLabel.setText("Event Class");


			txtEventClass = new Text(eventgroup, SWT.BORDER);
			txtEventClass.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					refresh();
				}
			});
			txtEventClass.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

			butApply = new Button(eventgroup, SWT.NONE);
			butApply.setEnabled(false);
			butApply.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(txtJob.getEnabled() && txtJobChain.getEnabled() && txtEventClass.getEnabled() &&
							(txtJob.getText().length() + txtJobChain.getText().length() + txtEventClass.getText().length()) == 0) {
						filename = txtName.getText() + ".actions.xml"; 
					} else if(txtJob.getEnabled()) {
						filename = txtJob.getText() + "." + txtName.getText() + ".job.actions.xml"; 
					} else if(txtJobChain.getEnabled()) {
						filename = txtJobChain.getText() + "." + txtName.getText() + ".job_chain.actions.xml";
					} else if(txtEventClass.getEnabled()) {
						filename = txtEventClass.getText() + "." + txtName.getText() + ".event_class.actions.xml";
					}
					
					filename = normalized(lblDirectory.getText())  + filename;
					File _file = new File(filename);
					boolean ok_ = true;
					if(_file.exists()) {
						int ok = MainWindow.message(Messages.getString("MainListener.doFileOverwrite"), //$NON-NLS-1$
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						if (ok == SWT.NO) {
							ok_ = false;									
						}
							
					}
					
					if(ok_) {
						dom.setFilename(filename);					
						_shell.dispose();
					}
					
					
				}
			});
			butApply.setText("Save");



			lblDirectory = new Label(eventgroup, SWT.NONE);
			lblDirectory.setText(normalized(Options.getSchedulerHome()));
			lblDirectory.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));



			butDirectory = new Button(eventgroup, SWT.NONE);
			butDirectory.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					DirectoryDialog fdialog = new DirectoryDialog(MainWindow.getSShell(), SWT.MULTI);
					fdialog.setFilterPath(Options.getLastDirectory());
					fdialog.setText("Save Event Handler ...");

					String path = fdialog.open();    
					if(path != null) 						
						lblDirectory.setText(normalized(path));
					
				}
			});
			butDirectory.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			butDirectory.setText("Directory");



		}

		setToolTipText();

		_shell.layout();
		_shell.open();
	}


	private void refresh() {

		butApply.setEnabled(txtName.getText().length() > 0);
		txtJobChain.setEnabled((txtJob.getText().length() + txtEventClass.getText().length()) == 0);
		txtJob.setEnabled((txtJobChain.getText().length() + txtEventClass.getText().length()) == 0);
		txtEventClass.setEnabled((txtJob.getText().length() + txtJobChain.getText().length()) == 0);

	}
	

	public void setToolTipText() {

		butApply.setToolTipText(Messages.getTooltip("events.save_dialog.but_apply"));
		txtJobChain.setToolTipText(Messages.getTooltip("events.save_dialog.job_chain"));
		txtJob.setToolTipText(Messages.getTooltip("events.save_dialog.job"));
		txtEventClass.setToolTipText(Messages.getTooltip("events.save_dialog.event_class"));		
		butDirectory.setToolTipText(Messages.getTooltip("events.save_dialog.but_directory"));
		txtName.setToolTipText(Messages.getTooltip("events.save_dialog.name"));
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	private String normalized(String path)  { 
		if(path == null)
			return "";
		
		path = path.replaceAll("\\\\", "/" );
		if(path.endsWith("/") || path.endsWith("\\") )
			return path;
		else
			return path + "/";
	}
}
