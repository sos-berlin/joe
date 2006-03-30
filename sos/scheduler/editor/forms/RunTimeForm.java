package sos.scheduler.editor.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.listeners.RunTimeListener;

public class RunTimeForm extends Composite {
	private RunTimeListener listener;
	
	private Group gRunTime = null;

	private DateForm holidayForm = null;

	private PeriodForm periodForm = null;

	private Group gComment = null;

	private Text tComment = null;

	public RunTimeForm(Composite parent, int style, DomParser dom, Element job, IUpdate gui) {
		super(parent, style);
		listener = new RunTimeListener(dom, job);
		initialize();

		dom.setInit(true);
		
		holidayForm.setObjects(dom, listener.getRunTime(), gui);
		
		periodForm.setParams(dom, listener.isOnOrder());
		periodForm.setRunOnce(true);
		periodForm.setEnabled(true);
		periodForm.setPeriod(listener.getRunTime());
		tComment.setText(listener.getComment());
		
		String title = gComment.getText();
		if(dom.isJobDisabled(Utils.getAttributeValue("name", job))) {
			title += " (Cannot be set for disabled Jobs)";
			tComment.setEnabled(false);
		}
		gComment.setText(title);
		
		dom.setInit(false);
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(576,518));
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 1;
		gRunTime = new Group(this, SWT.NONE);
		gRunTime.setText("Run Time");
		createPeriodForm();
		gRunTime.setLayout(gridLayout3);
		createGroup2();
		createHollidayForm();
	}

	/**
	 * This method initializes hollidayForm
	 * 
	 */
	private void createHollidayForm() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = false;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalSpan = 1;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		holidayForm = new DateForm(gRunTime, SWT.NONE, 0);
		holidayForm.setLayoutData(gridData4);
	}

	/**
	 * This method initializes periodForm	
	 *
	 */
	private void createPeriodForm() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.grabExcessHorizontalSpace = false;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessVerticalSpace = false;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		periodForm = new PeriodForm(gRunTime, SWT.NONE);
		periodForm.setLayoutData(gridData2);
	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup2() {
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalSpan = 1;
		gridData.horizontalSpan = 1;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gComment = new Group(gRunTime, SWT.NONE);
		gComment.setText("Comment");
		gComment.setLayoutData(gridData);
		gComment.setLayout(new GridLayout());
		tComment = new Text(gComment, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
		tComment.setToolTipText(Messages.getTooltip("run_time.comment"));
		tComment.setLayoutData(gridData1);
		tComment.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NONE));
		tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setComment(tComment.getText());
			}
		});
	}

} // @jve:decl-index=0:visual-constraint="10,10"
