package sos.scheduler.editor.conf.forms;

import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.classes.WindowsSaver;


public class DetailDialogForm {
	
	private String           jobChainname      = "";
		
	private String           state             = null;	
	
	private Shell            shell             = null; 
	
	//private String[]         listOfOrderIds    = null;
	
	private DetailForm       dialogForm        = null;	
	
	private boolean          isLifeElement     = false;
	
	private String           path              = null;
	
	private String           orderId           = null;
 
	
	/**
	 * Wenn es eine JobDokumentation gibt, dann wird beim STarten der Wizzard die Parameter Fenster geöffnet
	 */
	//private String           descriptionname   = null;
	
	
	//public DetailDialogForm(String jobChainname_, String[] listOfOrderIds_, boolean isLifeElement_, String path_) {
	public DetailDialogForm(String jobChainname_, boolean isLifeElement_, String path_) {
		jobChainname = jobChainname_;
		//listOfOrderIds = listOfOrderIds_;	
		isLifeElement = isLifeElement_;
		path = path_;
	}
	
	//public DetailDialogForm(String jobChainname_, String state_, String[] listOfOrderIds_, boolean isLifeElement_, String path_) {
	public DetailDialogForm(String jobChainname_, String state_, String orderId_, boolean isLifeElement_, String path_) {
		jobChainname = jobChainname_;
		state = state_;
		//listOfOrderIds = listOfOrderIds_;
		this.orderId = orderId_;
		isLifeElement = isLifeElement_;
		path = path_;
	}
	
	/**
	 * Aufruf dieser Metho, wenn es eine Jobdokumentation gibt
	 * @param descriptionname_
	 */
	/*public void showDetails(String descriptionname_) {

		descriptionname = descriptionname_;
		showDetails();
	}*/
	
	public void showDetails() {
		
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shell= new Shell(sos.scheduler.editor.app.MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
		shell.setLayout(new FillLayout());
		
 		
	 
		shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		shell.setText("Details for JobChain: " + jobChainname + 
				     (state != null && state.length()> 0 ? "  State: " + state: "") + 
				     (orderId != null && orderId.length() > 0 ? "  Order Id: " + orderId : ""));
		final Composite composite = new Composite( shell, SWT.NONE);
		composite.setLayout(new FillLayout());
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
		gridData_6.widthHint = 500;
		gridData_6.heightHint = 500;
			
				
		//dialogForm =new DetailForm(composite, SWT.NONE, jobChainname, state, listOfOrderIds, Editor.JOB_CHAINS, null, null, isLifeElement, path);
		dialogForm =new DetailForm(composite, SWT.NONE, jobChainname, state, orderId, Editor.JOB_CHAINS, null, null, isLifeElement, path);


		if(!dialogForm.hasErrors())//im fehlerfall
			dialogForm.setLayout(new FillLayout());
	}

	public DetailForm getDialogForm() {
		return dialogForm;
	}
	
	
}
