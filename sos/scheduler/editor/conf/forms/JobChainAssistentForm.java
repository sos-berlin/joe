/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
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
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainsListener;

public class JobChainAssistentForm {
 	
	
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
		shellJobChainl.setLayout(gridLayout);
		shellJobChainl.setSize(436, 239);
		shellJobChainl.setText("Job Chain");
		shellJobChainl.open();

		{
			final Group jobChainGroup = new Group(shellJobChainl, SWT.NONE);
			jobChainGroup.setText("Job Chain");
			final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
			gridData.heightHint = 186;
			jobChainGroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.numColumns = 4;
			jobChainGroup.setLayout(gridLayout_1);

			{
				txtJobChain = new Text(jobChainGroup, SWT.MULTI | SWT.WRAP);
				final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, false, false, 4, 1);
				gridData_1.heightHint = 84;
				gridData_1.widthHint = 366;
				txtJobChain.setLayoutData(gridData_1);
				txtJobChain.setEditable(false);
				txtJobChain.setText(Messages.getString("assistent.job_chain.no_job_chain_name"));
				//txtJobChain.setToolTipText(Messages.getTooltip("tooltip.assistent.job_chains"));
			}

			{
				final Label tasksLabel = new Label(jobChainGroup, SWT.NONE);
				tasksLabel.setText("Name");
			}

			{
				txtJobChainname = new Text(jobChainGroup, SWT.BORDER);
				txtJobChainname.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
				
			}

			{
				final Composite composite = new Composite(jobChainGroup, SWT.NONE);
				final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
				gridData_1.heightHint = 30;
				gridData_1.widthHint = 44;
				composite.setLayoutData(gridData_1);
				final GridLayout gridLayout_2 = new GridLayout();
				gridLayout_2.marginWidth = 0;
				composite.setLayout(gridLayout_2);

				{
					butFinish = new Button(composite, SWT.NONE);
					butFinish.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
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
			}
			{
				butCancel = new Button(jobChainGroup, SWT.NONE);
				butCancel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						shellJobChainl.dispose();
					}
				});
				butCancel.setText("Cancel");
			}
			new Label(jobChainGroup, SWT.NONE);

			{
				final Composite composite = new Composite(jobChainGroup, SWT.NONE);
				composite.setLayoutData(new GridData(GridData.END, GridData.FILL, false, false));
				final GridLayout gridLayout_2 = new GridLayout();
				gridLayout_2.marginWidth = 0;
				gridLayout_2.numColumns = 2;
				composite.setLayout(gridLayout_2);

				{
					butShow = new Button(composite, SWT.NONE);
					butShow.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							Element jobChain = new Element("job_chain");
							if(txtJobChainname.getText() != null && txtJobChainname.getText().trim().length() > 0) {												
								Utils.setAttribute("name", txtJobChainname.getText(), jobChain);						
							}
							MainWindow.message(shellJobChainl, Utils.getElementAsString(jobChain), SWT.OK );
						}
					});
					butShow.setText("Show");
				}
				{
					butNext = new Button(composite, SWT.NONE);
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
					butNext.setText("Next");
				}
			}
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

