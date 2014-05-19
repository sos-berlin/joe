/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;

import com.swtdesigner.SWTResourceManager;

/**
 * StartDialog vom Wizzard
 * 
 * @author mueruevet.oeksuez@sos-berlin.com
 *
 */

public class JobAssistentInfoForms {
	
	private SchedulerDom      dom          = null;
	
	private ISchedulerUpdate  update       = null;			
			
	private Button            butNext      = null;
	
	private Button            cancelButton = null;
				
	public JobAssistentInfoForms(SchedulerDom dom_, ISchedulerUpdate update_) {
		
		dom = dom_;
		update = update_;					
	}

	public void showInfoForm() {
				 
		final Shell shell = new Shell(sos.scheduler.editor.app.MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginBottom = 5;
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		shell.setSize(619, 431);		
		
		shell.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_JobWizard.label());

		java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();		
		shell.setBounds((screen.width - shell.getBounds().width) /2, 
				        (screen.height - shell.getBounds().height) /2, 
				        shell.getBounds().width, 
				        shell.getBounds().height);
		
		shell.open();

		{
			final Group jobGroup = SOSJOEMessageCodes.JOE_G_JobAssistent_JobGroup.Control(new Group(shell, SWT.NONE));
			final GridData gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 3, 1);
			gridData.widthHint = 581;
			gridData.heightHint = 329;
			jobGroup.setLayoutData(gridData);
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.verticalSpacing = 10;
			gridLayout_1.horizontalSpacing = 10;
			gridLayout_1.marginWidth = 10;
			gridLayout_1.marginTop = 10;
			gridLayout_1.marginRight = 10;
			gridLayout_1.marginLeft = 10;
			gridLayout_1.marginHeight = 10;
			gridLayout_1.marginBottom = 10;
			jobGroup.setLayout(gridLayout_1);
		

			final Text txtInfoGlobal = SOSJOEMessageCodes.JOE_T_JobAssistent_InfoGlobal.Control(new Text(jobGroup, SWT.READ_ONLY | SWT.WRAP));
			txtInfoGlobal.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					butNext.setFocus();
				}
			});
			txtInfoGlobal.setEditable(false);
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
			gridData_1.widthHint = 491;
			gridData_1.heightHint = 271;
			txtInfoGlobal.setLayoutData(gridData_1);

			final Button butShow = SOSJOEMessageCodes.JOE_B_JobAssistent_ShowInFuture.Control(new Button(jobGroup, SWT.CHECK));
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					Options.setShowWizardInfo(!butShow.getSelection());					
				}
			});		
		}
		
		new Label(shell, SWT.NONE);

		cancelButton = SOSJOEMessageCodes.JOE_B_JobAssistent_Cancel.Control(new Button(shell, SWT.NONE));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		
		{
			butNext = SOSJOEMessageCodes.JOE_B_JobAssistent_Next.Control(new Button(shell, SWT.NONE));
			butNext.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					Utils.startCursor(shell);
					JobAssistentTypeForms typeForms = new JobAssistentTypeForms(dom, update);
					
					typeForms.showTypeForms();	
					shell.dispose();
					Utils.stopCursor(shell);
				}
			});
		}
		setToolTipText();
		shell.layout();		
	}

	public void setToolTipText() {
//		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
//		cancelButton.setToolTipText(Messages.getTooltip("assistent.cancel"));
	}
	
	
}
