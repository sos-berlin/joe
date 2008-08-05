/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.conf.listeners.DetailXMLEditorListener;
import sos.scheduler.editor.conf.listeners.JobChainConfigurationListener;
import sos.scheduler.editor.conf.DetailDom;

public class DetailXMLEditorDialogForm {
	
	private String                  xmlFilename     = null;
	
	private Text                    txtXML          = null; 
	
	private DetailXMLEditorListener listener        = null;
	
	private Button                  butApply        = null;
	
	private Shell                   shell           = null; 
	
	private String                  state           = null;
	 
	private String                  jobChainname    = null;
	
	private String[]                listOfOrderIds  = null;
	
	private String                  orderId         = null;    
	
	private DetailDom               dom             = null;
		
	/** wer hat ihn aufgerufen*/
	private int                     type            = -1;
	
	/** falls type = Editor.Details*/
	private Composite        parent                 = null;
	
	/** falls type = Editor.Details*/
	private JobChainConfigurationListener confListener = null;
	
	/** falls type = Editor.Details*/
	private Tree             tree                   = null;
	
	private boolean                  isLifeElement   = false;
	
	private String                   path            = null;
	
	public DetailXMLEditorDialogForm(String xmlFilename_, 
			                         String jobChainname_, 
			                         String state_, 
			                         String[] listOfOrderIds_, 
			                         String orderId_, 
			                         int type_, 
			                         boolean isLifeElement_,
			                         String path_) {
		jobChainname = jobChainname_;
		state = state_;
		xmlFilename = xmlFilename_;
		listOfOrderIds = listOfOrderIds_;
		orderId = orderId_;
		type = type_;
		isLifeElement = isLifeElement_;
		path = path_;
	}
	
	public DetailXMLEditorDialogForm(DetailDom dom_, int type_, boolean isLifeElement_, String path_) {
		dom = dom_;
		xmlFilename = dom.getFilename();
		type = type_;
		isLifeElement = isLifeElement_;
		path = path_;
	}
	
	public void showXMLEditor() {
		
		shell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {
				close();
				e.doit = shell.isDisposed();
			}
		});
		
		shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginBottom = 5;
		shell.setLayout(gridLayout);
		shell.setSize(693, 743);		
		
		shell.setText("XML Configurations: " + xmlFilename);
		
		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();		
		shell.setBounds((screen.width - shell.getBounds().width) /2, 
				(screen.height - shell.getBounds().height) /2, 
				shell.getBounds().width, 
				shell.getBounds().height);
		
		
		
		{
			final Group jobGroup = new Group(shell, SWT.NONE);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 2;
			jobGroup.setLayout(gridLayout_1);
			jobGroup.setText( "XML" );
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
			gridData.minimumWidth = 10;
			gridData.minimumHeight = 10;
			gridData.widthHint = 663;
			gridData.heightHint = 685;
			jobGroup.setLayoutData(gridData);
			
			
			txtXML = new Text(jobGroup, SWT.V_SCROLL | SWT.MULTI | SWT.WRAP | SWT.H_SCROLL);
			txtXML.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					butApply.setEnabled(true);
				}
			});
			final GridData gridData_2 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 2);
			gridData_2.heightHint = 672;
			gridData_2.widthHint = 553;
			txtXML.setLayoutData(gridData_2);
			txtXML.setEnabled(true);
			txtXML.setEditable(true);

			butApply = new Button(jobGroup, SWT.NONE);
			butApply.setEnabled(false);
			butApply.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					
						listener.saveXML(txtXML.getText());
						if(type == Editor.DETAILS) {
							confListener.treeFillMain(tree, parent);
							shell.setFocus();
						}
						butApply.setEnabled(false);
						shell.close();
				}
			});
			final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
			gridData_1.widthHint = 62;
			butApply.setLayoutData(gridData_1);
			butApply.setText("Apply");

			final Button butClose = new Button(jobGroup, SWT.NONE);
			butClose.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					close();					
					
				}
			});
			butClose.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			butClose.setText("Cancel");
			
		}
		if(type == Editor.JOB_CHAINS) {
			listener = new DetailXMLEditorListener(xmlFilename);
		} else {
			listener = new DetailXMLEditorListener(dom);
		}
		try {
        txtXML.setText(listener.readCommands());
		} catch (Exception e) {
			try {
				System.err.println("error in DetailXMLEditorDialogForm.showXMLEditor: " + e.toString());						
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
    		} catch(Exception ee) {
    			//tu nichts
    		}
			
		}
        
		setToolTipText();
		shell.open();
		shell.layout();		
		butApply.setEnabled(false);
	}
	
	public void setToolTipText() {
		butApply.setToolTipText(Messages.getTooltip("detail.xml_Editor.apply"));
		txtXML.setToolTipText(Messages.getTooltip("detail.xml_Editor.xml"));
	}

	private boolean closeDialog() {
		int cont = -1;
		boolean retVal = false;
		if(butApply.isEnabled()) {
			cont = MainWindow.message(shell, sos.scheduler.editor.app.Messages.getString("detailform.close"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
			
			if(cont == SWT.OK) {						
				shell.dispose();
				retVal = true;
			}
			
		} else{
			retVal = true;
		}
		return retVal;
	}
	
	private void openDetailForm() {
		  		
			DetailDialogForm detail = new DetailDialogForm(jobChainname, state, listOfOrderIds, isLifeElement, path);
			detail.showDetails();
			detail.getDialogForm().open(orderId);
		 
	}
	
	private void close() {
		if (closeDialog() ) {
			if(type == Editor.JOB_CHAINS)
				openDetailForm();
			else if(type == Editor.DETAILS) {
				confListener.treeSelection(tree, parent);
				dom.setChanged(true);
			}
			shell.dispose();
			
		}
		
	}
	
	public void setConfigurationData(JobChainConfigurationListener confListener_, Tree tree_, Composite parent_) {
		confListener = confListener_;
		tree = tree_;
		parent = parent_;
		
	}
	
}


