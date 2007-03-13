/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
import sos.scheduler.editor.conf.listeners.JobsListener;

public class JobAssistentDelayAfterErrorForm {
 
	private Element           job          = null;
	
	private Text              txtDelayAfterError= null;
 
	private Text              txtErrorCount   = null;
	
	private JobsListener      listener     = null;
	
	private SchedulerDom      dom          = null;
	
	private ISchedulerUpdate  update       = null;
	
	private Button            butFinish    = null;
	
	private Button            butCancel    = null;
	
	private Button            butNext      = null;
	
	private Button            butShow      = null;	
		
	private Label             lblOftenSetBack = null; 
	
	private Label             lblLongWait  = null;
	
	private Text              txtHour      = null;
	
	private Text              txtMin       = null;
	
	private Text              txtSecound   = null;
	
	 
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int assistentType = -1; 
	
	public JobAssistentDelayAfterErrorForm(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
			
	}
	
	private void init() {
		if(job != null && Utils.getAttributeValue("tasks", job).equals("unbounded")) {
			job.removeAttribute("tasks");			
		}
	}
	
	public void showDelayAfterErrorForm(Element job_, int assistentType_) {
		assistentType = assistentType_;
		job = job_;
		init();
		final Shell shellSetBack = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		shellSetBack.setLayout(gridLayout);
		shellSetBack.setSize(481, 317);
		shellSetBack.setText("Delay order after setback");
		shellSetBack.open();

		final Group jobGroup = new Group(shellSetBack, SWT.NONE);
		jobGroup.setText("Job");
		final GridData gridData_2 = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData_2.heightHint = 263;
		gridData_2.widthHint = 454;
		jobGroup.setLayoutData(gridData_2);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		jobGroup.setLayout(gridLayout_1);

		{
			txtDelayAfterError = new Text(jobGroup, SWT.MULTI);
			final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
			gridData.heightHint = 100;
			gridData.widthHint = 360;
			txtDelayAfterError.setLayoutData(gridData);
			txtDelayAfterError.setEditable(false);
			txtDelayAfterError.setText(Messages.getString("assistent.delay_after_error"));
		}

		{
			lblOftenSetBack = new Label(jobGroup, SWT.NONE);
			lblOftenSetBack.setLayoutData(new GridData());
			lblOftenSetBack.setText(Messages.getString("assistent.often_setback"));
		}
		txtErrorCount = new Text(jobGroup, SWT.BORDER);
		final GridData gridData_1 = new GridData(128, SWT.DEFAULT);
		txtErrorCount.setLayoutData(gridData_1);
		

		{
			lblLongWait = new Label(jobGroup, SWT.NONE);
			lblLongWait.setLayoutData(new GridData());
			lblLongWait.setText(Messages.getString("assistent.next_setback"));
			
		}

		final Composite composite = new Composite(jobGroup, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
		final GridLayout gridLayout_5 = new GridLayout();
		gridLayout_5.numColumns = 4;
		composite.setLayout(gridLayout_5);

		{
			txtHour = new Text(composite, SWT.BORDER);
			txtHour.setLayoutData(new GridData(30, SWT.DEFAULT));
			txtHour.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if (!Utils.isNumeric( txtHour.getText())) {
						MainWindow.message(shellSetBack, Messages.getString("assistent.no_numeric"), SWT.OK );
					}
				}
			});
		}

		{
			txtMin = new Text(composite, SWT.BORDER);
			txtMin.setLayoutData(new GridData(30, SWT.DEFAULT));
			txtMin.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if (!Utils.isNumeric( txtMin.getText())) {
						MainWindow.message(shellSetBack, Messages.getString("assistent.no_numeric"), SWT.OK );
					}
				}
			});
		}

		{
			txtSecound = new Text(composite, SWT.BORDER);
			txtSecound.setLayoutData(new GridData(30, SWT.DEFAULT));
			txtSecound.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if (!Utils.isNumeric( txtSecound.getText())) {
						MainWindow.message(shellSetBack, Messages.getString("assistent.no_numeric"), SWT.OK );
					}
				}
			});
		}

		{
			final Label hhmmssLabel = new Label(composite, SWT.NONE);
			hhmmssLabel.setText("(hh:mm:ss)");
		}

		{
			final Label lnlNumberOfMaxErros = new Label(jobGroup, SWT.NONE);
			lnlNumberOfMaxErros.setLayoutData(new GridData());
			lnlNumberOfMaxErros.setText(Messages.getString("assistent.delay_after_error_count"));
		}

		final Text txtStop = new Text(jobGroup, SWT.BORDER);
		final GridData gridData_3 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		gridData_3.widthHint = 28;
		txtStop.setLayoutData(gridData_3);

		final Composite composite_1 = new Composite(jobGroup, SWT.NONE);
		final GridData gridData_4 = new GridData(GridData.BEGINNING, GridData.FILL, false, false);
		gridData_4.heightHint = 35;
		composite_1.setLayoutData(gridData_4);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.numColumns = 2;
		composite_1.setLayout(gridLayout_3);

		{
			butFinish = new Button(composite_1, SWT.NONE);
			butFinish.setLayoutData(new GridData(71, SWT.DEFAULT));
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					job.removeChildren("delay_after_error");
					if(txtErrorCount.getText() != null && txtErrorCount.getText().trim().length() > 0 ) {
						Element delayAfterError = new Element("delay_after_error"); 
						Utils.setAttribute("error_count", txtErrorCount.getText(), delayAfterError);
						
						String a = (txtHour.getText() != null ?  txtHour.getText() : "") + 
						(txtMin.getText() != null ?  txtMin.getText() : "") +
						(txtSecound.getText() != null ?  txtSecound.getText() : "");
						
						if(a.trim().length() > 0) {
							String t = (txtHour.getText() == null ? "00" : txtHour.getText()) + ":" +
							(txtMin.getText() == null ? "00" : txtMin.getText()) + ":" +
							(txtSecound.getText() == null ? "00" : txtSecound.getText());
							Utils.setAttribute("delay", t, delayAfterError);
						} else {
							delayAfterError.removeAttribute("delay");
						}
						job.addContent(delayAfterError);
					}
					if(txtStop.getText() != null && txtStop.getText().trim().length() > 0) {
						Element delayAfterError2 = new Element("delay_after_error"); 
						Utils.setAttribute("error_count", txtStop.getText(), delayAfterError2);
						Utils.setAttribute("delay", "stop", delayAfterError2);
						job.addContent(delayAfterError2);
					}
					
					shellSetBack.dispose();
					listener.newImportJob(job, assistentType);
				}
			});
			butFinish.setText("Finish");
		}
		{
			butCancel = new Button(composite_1, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					shellSetBack.dispose();
				}
			});
			butCancel.setText("Cancel");
		}

		final Composite composite_2 = new Composite(jobGroup, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData_5.widthHint = 123;
		gridData_5.heightHint = 46;
		composite_2.setLayoutData(gridData_5);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.numColumns = 2;
		composite_2.setLayout(gridLayout_4);

		{
			butShow = new Button(composite_2, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					job.removeChildren("delay_after_error");
					if(txtErrorCount.getText() != null && txtErrorCount.getText().trim().length() > 0 ) {
						Element delayAfterError = new Element("delay_after_error"); 
						Utils.setAttribute("error_count", txtErrorCount.getText(), delayAfterError);
						
						String a = (txtHour.getText() != null ?  txtHour.getText() : "") + 
						(txtMin.getText() != null ?  txtMin.getText() : "") +
						(txtSecound.getText() != null ?  txtSecound.getText() : "");
						
						if(a.trim().length() > 0) {
							String t = (txtHour.getText() == null ? "00" : txtHour.getText()) + ":" +
							(txtMin.getText() == null ? "00" : txtMin.getText()) + ":" +
							(txtSecound.getText() == null ? "00" : txtSecound.getText());
							Utils.setAttribute("delay", t, delayAfterError);
						} else {
							delayAfterError.removeAttribute("delay");
						}
						job.addContent(delayAfterError);
					}
					if(txtStop.getText() != null && txtStop.getText().trim().length() > 0) {
						Element delayAfterError2 = new Element("delay_after_error"); 
						Utils.setAttribute("error_count", txtStop.getText(), delayAfterError2);
						Utils.setAttribute("delay", "stop", delayAfterError2);
						job.addContent(delayAfterError2);
					}
					MainWindow.message(shellSetBack, Utils.getElementAsString(job), SWT.OK );
				}
			});
			butShow.setText("Show");
		}
		{
			butNext = new Button(composite_2, SWT.NONE);
			butNext.setVisible(false);
			final GridData gridData = new GridData(GridData.END, GridData.CENTER, false, false);
			gridData.widthHint = 61;
			butNext.setLayoutData(gridData);
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(txtErrorCount.getText() != null && txtErrorCount.getText().trim().length() > 0) {
						Utils.setAttribute("tasks", txtErrorCount.getText(), job);						
					}
					if(job.getChild("process") != null) {
						JobAssistentProcessForms process = new JobAssistentProcessForms(dom, update);
						process.showProcessForm(job, assistentType);						
					} else {
						JobAssistentScriptForms script = new JobAssistentScriptForms(dom, update);
						script.showScriptForm(job, assistentType);						
					}
					shellSetBack.dispose();
				}
			});
			butNext.setText("Next");
		}

		{
			if(Utils.getAttributeValue("tasks", job_) != null && 
					!Utils.getAttributeValue("tasks", job_).equals("unbounded")) {
			}
		}
		setToolTipText();
		shellSetBack.layout();
		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		
		
		
	}
	
	
}
