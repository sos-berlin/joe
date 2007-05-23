package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.conf.listeners.DetailsListener;

public class DetailDialogForm {
	
	private String           jobChainname      = "";
		
	private String           state             = null;	
	
	private Shell            shell             = null; 
	
	private String[]         listOfOrderIds    = null;
	
	private DetailForm       dialogForm        = null;
		
	
	public DetailDialogForm(String jobChainname_, String[] listOfOrderIds_) {
		jobChainname = jobChainname_;
		listOfOrderIds = listOfOrderIds_;		
	}
	
	public DetailDialogForm(String jobChainname_, String state_, String[] listOfOrderIds_) {
		jobChainname = jobChainname_;
		state = state_;
		listOfOrderIds = listOfOrderIds_;
	}
	
	public void showDetails() {
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shell= new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER );
		shell.setLayout(new GridLayout());
		
		shell.setSize(620, 632);	
		shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		shell.setText("Details for JobChain: " + jobChainname + (state != null && state.length()> 0 ? " State: " + state: ""));
		final Composite composite = new Composite( shell, SWT.NONE);
		composite.setLayout(new FillLayout());
		final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData.heightHint = 588;
		gridData.widthHint = 590;
		composite.setLayoutData(gridData);
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
		gridData_6.widthHint = 686;
		gridData_6.heightHint = 572;
			
				
		dialogForm =new DetailForm(composite, SWT.NONE, jobChainname, state, listOfOrderIds, Editor.JOB_CHAINS, null, null);
		dialogForm.setLayout(new FillLayout());
		
		
	}

	public DetailForm getDialogForm() {
		return dialogForm;
	}
	
	
}
