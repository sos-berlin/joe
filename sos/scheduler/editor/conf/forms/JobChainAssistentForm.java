/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainsListener;

public class JobChainAssistentForm {
 
	//private Element           job          = null;
	
	private Text              txtJobChain     = null;
 
	private Text              txtJobChainname      = null;
	
	private JobChainsListener listener     = null;
	
	private SchedulerDom      dom          = null;
	
	private ISchedulerUpdate  update       = null;
	
	private Button            butFinish    = null;
	
	private Button            butCancel    = null;
	
	private Button            butNext      = null;
	
	private Button            butShow      = null;		
	

	public JobChainAssistentForm(JobChainsListener listener_) {
		
		listener = listener_;	
			
	}
	
	public void startJobChainAssistant(ISchedulerUpdate update_) {

		update = update_;
		final Shell shellJobChainl = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		shellJobChainl.setLayout(gridLayout);
		shellJobChainl.setSize(400, 185);
		shellJobChainl.setText("Job Chain");
		shellJobChainl.open();

		{
			txtJobChain = new Text(shellJobChainl, SWT.MULTI);
			txtJobChain.setEditable(false);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
			gridData.heightHint = 87;
			gridData.widthHint = 383;
			txtJobChain.setLayoutData(gridData);
			txtJobChain.setText(Messages.getString("assistent.job_chain.no_job_chain_name"));
			//txtJobChain.setToolTipText(Messages.getTooltip("tooltip.assistent.job_chains"));
		}

		{
			final Label tasksLabel = new Label(shellJobChainl, SWT.NONE);
			tasksLabel.setLayoutData(new GridData());
			tasksLabel.setText("Name");
		}

		{
			txtJobChainname = new Text(shellJobChainl, SWT.BORDER);
			txtJobChainname.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));
			
		}

		{
			butFinish = new Button(shellJobChainl, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(txtJobChainname.getText() != null && txtJobChainname.getText().trim().length() > 0) {
						listener.newChain();
						listener.applyChain(txtJobChainname.getText(), true, true);
						listener.fillChains();
						shellJobChainl.dispose();	
					} else {
						int cont = MainWindow.message(shellJobChainl, sos.scheduler.editor.app.Messages.getString("no_job_chain_name"), SWT.ICON_WARNING | SWT.OK );
						txtJobChainname.setFocus();
					}
					
					
				}
			});
			butFinish.setText("Finish");
		}
		{
			butCancel = new Button(shellJobChainl, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					shellJobChainl.dispose();
				}
			});
			butCancel.setLayoutData(new GridData());
			butCancel.setText("Cancel");
		}
		{
			butNext = new Button(shellJobChainl, SWT.NONE);
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(txtJobChainname.getText() != null && txtJobChainname.getText().trim().length() > 0) {
						Element jobChain = new Element("job_chain");						
						Utils.setAttribute("name", txtJobChainname.getText(), jobChain);
						JobChainAssistentJobNodeForm node = new JobChainAssistentJobNodeForm(listener);
						node.showJobChainNode(jobChain, update);
						shellJobChainl.dispose();
					} else {
						int cont = MainWindow.message(shellJobChainl, sos.scheduler.editor.app.Messages.getString("no_job_chain_name"), SWT.ICON_WARNING | SWT.OK );
						txtJobChainname.setFocus();
					}
					
					
				}
			});
			butNext.setLayoutData(new GridData());
			butNext.setText("Next");
		}

		{
			butShow = new Button(shellJobChainl, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					Element jobChain = new Element("job_chain");
					if(txtJobChainname.getText() != null && txtJobChainname.getText().trim().length() > 0) {												
						Utils.setAttribute("name", txtJobChainname.getText(), jobChain);						
					}
					MainWindow.message(shellJobChainl, Utils.getElementAsString(jobChain), SWT.OK );
				}
			});
			butShow.setLayoutData(new GridData());
			butShow.setText("Show");
		}
		setToolTipText();
		shellJobChainl.layout();
		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		txtJobChainname.setToolTipText(Messages.getTooltip("tooltip.assistent.job_chains.name"));
		//txtJobChain.setToolTipText(Messages.getTooltip("tooltip.assistent.job_chains"));
		
	}
}

