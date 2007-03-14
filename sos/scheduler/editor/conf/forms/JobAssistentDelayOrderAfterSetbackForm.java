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

public class JobAssistentDelayOrderAfterSetbackForm {
 
	private Element           job          = null;
	
	private Text              txtDelayOrder= null;
 
	private Text              txtSetBack   = null;
	
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
	
	public JobAssistentDelayOrderAfterSetbackForm(SchedulerDom dom_, ISchedulerUpdate update_) {
		dom = dom_;
		update = update_;
		listener = new JobsListener(dom, update);			
			
	}
	
	private void init() {
		if(job != null && Utils.getAttributeValue("tasks", job).equals("unbounded")) {
			job.removeAttribute("tasks");			
		}
	}
	
	public void showDelayOrderAfterSetbackForm(Element job_, int assistentType_) {
		assistentType = assistentType_;
		job = job_;
		init();
		final Shell shellSetBack = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		shellSetBack.setLayout(gridLayout);
		shellSetBack.setSize(482, 287);
		shellSetBack.setText("Delay Order After Setback");
		shellSetBack.open();

		final Group jobGroup = new Group(shellSetBack, SWT.NONE);
		jobGroup.setText("Job");
		final GridData gridData_2 = new GridData(454, 230);
		jobGroup.setLayoutData(gridData_2);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 5;
		jobGroup.setLayout(gridLayout_1);

		{
			txtDelayOrder = new Text(jobGroup, SWT.MULTI);
			final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false, 5, 1);
			gridData.heightHint = 100;
			gridData.widthHint = 360;
			txtDelayOrder.setLayoutData(gridData);
			txtDelayOrder.setEditable(false);
			txtDelayOrder.setText(Messages.getString("assistent.order_after_setback"));
		}

		{
			lblOftenSetBack = new Label(jobGroup, SWT.NONE);
			lblOftenSetBack.setLayoutData(new GridData());
			lblOftenSetBack.setText(Messages.getString("assistent.often_setback"));
		}
		txtSetBack = new Text(jobGroup, SWT.BORDER);
		final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1);
		gridData_1.widthHint = 128;
		txtSetBack.setLayoutData(gridData_1);
		//txtSetBack.setText(;
		new Label(jobGroup, SWT.NONE);

		{
			lblLongWait = new Label(jobGroup, SWT.NONE);
			lblLongWait.setLayoutData(new GridData());
			lblLongWait.setText(Messages.getString("assistent.next_setback"));
			
		}

		{
			txtHour = new Text(jobGroup, SWT.BORDER);
			final GridData gridData = new GridData(30, SWT.DEFAULT);
			gridData.minimumWidth = 30;
			txtHour.setLayoutData(gridData);
			txtHour.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					if (!Utils.isNumeric( txtHour.getText())) {
						MainWindow.message(shellSetBack, Messages.getString("assistent.no_numeric"), SWT.OK );
					}
				}
			});
		}

		{
			txtMin = new Text(jobGroup, SWT.BORDER);
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
			txtSecound = new Text(jobGroup, SWT.BORDER);
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
			final Label hhmmssLabel = new Label(jobGroup, SWT.NONE);
			hhmmssLabel.setText("(hh:mm:ss)");
		}

		{
			final Label lblNumberOfSetBack = new Label(jobGroup, SWT.NONE);
			lblNumberOfSetBack.setText(Messages.getString("assistent.num_of_setback"));
		}

		final Combo combo = new Combo(jobGroup, SWT.NONE);
		combo.setItems(new String[] {"yes", "no"});
		combo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		combo.select(1);
		new Label(jobGroup, SWT.NONE);
		new Label(jobGroup, SWT.NONE);
		new Label(jobGroup, SWT.NONE);

		final Composite composite_1 = new Composite(jobGroup, SWT.NONE);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData_4.heightHint = 34;
		composite_1.setLayoutData(gridData_4);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.numColumns = 2;
		composite_1.setLayout(gridLayout_3);

		{
			butFinish = new Button(composite_1, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					job.removeChildren("delay_order_after_setback");
					if(txtSetBack.getText() != null && txtSetBack.getText().trim().length() > 0 ) {
						Element delayOrderAfterSetback = new Element("delay_order_after_setback"); 
						Utils.setAttribute("setback_count", txtSetBack.getText(), delayOrderAfterSetback);
						
						String a = (txtHour.getText() != null ?  txtHour.getText() : "") + 
						(txtMin.getText() != null ?  txtMin.getText() : "") +
						(txtSecound.getText() != null ?  txtSecound.getText() : "");
						
						if(a.trim().length() > 0) {
							String t = (txtHour.getText() == null ? "00" : txtHour.getText()) + ":" +
							(txtMin.getText() == null ? "00" : txtMin.getText()) + ":" +
							(txtSecound.getText() == null ? "00" : txtSecound.getText());
							Utils.setAttribute("delay", t, delayOrderAfterSetback);
						} else {
							delayOrderAfterSetback.removeAttribute("delay");
						}
					    Utils.setAttribute("is_maximum", combo.getItem(combo.getSelectionIndex()), delayOrderAfterSetback);
					    
					    job.addContent(delayOrderAfterSetback);				
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
		new Label(jobGroup, SWT.NONE);
		new Label(jobGroup, SWT.NONE);

		final Composite composite_2 = new Composite(jobGroup, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_5.heightHint = 35;
		composite_2.setLayoutData(gridData_5);
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.numColumns = 2;
		composite_2.setLayout(gridLayout_4);

		{
			butShow = new Button(composite_2, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					job.removeChildren("delay_order_after_setback");
					if(txtSetBack.getText() != null && txtSetBack.getText().trim().length() > 0 ) {
						Element delayOrderAfterSetback = new Element("delay_order_after_setback"); 
						Utils.setAttribute("setback_count", txtSetBack.getText(), delayOrderAfterSetback);
						
						String a = (txtHour.getText() != null ?  txtHour.getText() : "") + 
						(txtMin.getText() != null ?  txtMin.getText() : "") +
						(txtSecound.getText() != null ?  txtSecound.getText() : "");
						
						if(a.trim().length() > 0) {
							String t = (txtHour.getText() == null ? "00" : txtHour.getText()) + ":" +
							(txtMin.getText() == null ? "00" : txtMin.getText()) + ":" +
							(txtSecound.getText() == null ? "00" : txtSecound.getText());
							Utils.setAttribute("delay", t, delayOrderAfterSetback);
						} else {
							delayOrderAfterSetback.removeAttribute("delay");
						}
					    Utils.setAttribute("is_maximum", combo.getItem(combo.getSelectionIndex()), delayOrderAfterSetback);
					    
					    job.addContent(delayOrderAfterSetback);				
					}
					MainWindow.message(shellSetBack, Utils.getElementAsString(job), SWT.OK );
				}
			});
			butShow.setText("Show");
		}
		{
			butNext = new Button(composite_2, SWT.NONE);
			final GridData gridData = new GridData(GridData.END, GridData.CENTER, false, false);
			gridData.widthHint = 50;
			butNext.setLayoutData(gridData);
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(txtSetBack.getText() != null && txtSetBack.getText().trim().length() > 0 ) {
						Element delayOrderAfterSetback = new Element("delay_order_after_setback"); 
						Utils.setAttribute("setback_count", txtSetBack.getText(), delayOrderAfterSetback);
						
						String a = (txtHour.getText() != null ?  txtHour.getText() : "") + 
						(txtMin.getText() != null ?  txtMin.getText() : "") +
						(txtSecound.getText() != null ?  txtSecound.getText() : "");
						
						if(a.trim().length() > 0) {
							String t = (txtHour.getText() == null ? "00" : txtHour.getText()) + ":" +
							(txtMin.getText() == null ? "00" : txtMin.getText()) + ":" +
							(txtSecound.getText() == null ? "00" : txtSecound.getText());
							Utils.setAttribute("delay", t, delayOrderAfterSetback);
						} else {
							delayOrderAfterSetback.removeAttribute("delay");
						}
					    Utils.setAttribute("is_maximum", combo.getItem(combo.getSelectionIndex()), delayOrderAfterSetback);
					    
					    job.addContent(delayOrderAfterSetback);				
					}
					shellSetBack.dispose();
					JobAssistentDelayAfterErrorForm delay = new JobAssistentDelayAfterErrorForm(dom, update);
					delay.showDelayAfterErrorForm(job, assistentType);
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
		txtSetBack.setToolTipText(Messages.getTooltip("assistent.task"));
		
		
	}
	
	
}
