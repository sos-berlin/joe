package sos.scheduler.editor.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.listeners.PeriodListener;

public class PeriodForm extends Composite {
	private PeriodListener listener;

	private Group gPeriod = null;

	private Label label1 = null;

	private Button bLetRun = null;

	private Label label2 = null;

	private Spinner sBeginHours = null;

	private Label label3 = null;

	private Spinner sBeginMinutes = null;

	private Label label4 = null;

	private Spinner sBeginSeconds = null;

	private Label label5 = null;

	private Label label6 = null;

	private Spinner sEndHours = null;

	private Label label7 = null;

	private Spinner sEndMinutes = null;

	private Label label8 = null;

	private Spinner sEndSeconds = null;

	private Label label10 = null;

	private Spinner sRepeatHours = null;

	private Label label11 = null;

	private Spinner sRepeatMinutes = null;

	private Label label12 = null;

	private Spinner sRepeatSeconds = null;

	private Label label13 = null;

	private Spinner sSingleHours = null;

	private Label label14 = null;

	private Spinner sSingleMinutes = null;

	private Label label15 = null;

	private Spinner sSingleSeconds = null;

	private Label label16 = null;

	private Label label9 = null;

	private Label label18 = null;

	private Label lRunOnce = null;

	private Button cRunOnce = null;

	public PeriodForm(Composite parent, int style) {
		super(parent, style);
		initialize();
		
		setRunOnce(false);
	}

	public PeriodForm(Composite parent, int style, DomParser dom) {
		this(parent, style);
		listener = new PeriodListener(dom);
	}

	public void setDom(DomParser dom) {
		listener = new PeriodListener(dom);
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(452, 219));
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
		GridData gridData112 = new org.eclipse.swt.layout.GridData();
		gridData112.horizontalSpan = 2;
		GridData gridData101 = new org.eclipse.swt.layout.GridData();
		gridData101.horizontalSpan = 2;
		GridData gridData12 = new org.eclipse.swt.layout.GridData();
		gridData12.widthHint = 24;
		GridData gridData111 = new org.eclipse.swt.layout.GridData();
		gridData111.widthHint = 24;
		GridData gridData10 = new org.eclipse.swt.layout.GridData();
		gridData10.widthHint = 24;
		GridData gridData9 = new org.eclipse.swt.layout.GridData();
		gridData9.widthHint = 50;
		GridData gridData8 = new org.eclipse.swt.layout.GridData();
		gridData8.widthHint = 24;
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.widthHint = 24;
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.widthHint = 24;
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.widthHint = 24;
		GridData gridData41 = new org.eclipse.swt.layout.GridData();
		gridData41.widthHint = 24;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.widthHint = 24;
		GridData gridData21 = new org.eclipse.swt.layout.GridData();
		gridData21.widthHint = 24;
		GridData gridData11 = new org.eclipse.swt.layout.GridData();
		gridData11.widthHint = 24;
		GridData gridData4 = new GridData();
		gridData4.horizontalSpan = 1;
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 1;
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 7;
		gPeriod = new Group(this, SWT.NONE);
		gPeriod.setEnabled(false);
		gPeriod.setText("Period");
		gPeriod.setLayout(gridLayout);
		label1 = new Label(gPeriod, SWT.NONE);
		label1.setText("Let Run:");
		bLetRun = new Button(gPeriod, SWT.CHECK);
		bLetRun.setToolTipText(Messages.getTooltip("period.let_run"));
		bLetRun.setLayoutData(gridData);
		lRunOnce = new Label(gPeriod, SWT.NONE);
		lRunOnce.setText("Run Once:");
		lRunOnce.setLayoutData(gridData101);
		cRunOnce = new Button(gPeriod, SWT.CHECK);
		cRunOnce.setToolTipText(Messages.getTooltip("run_time.once"));
		cRunOnce.setLayoutData(gridData112);
		cRunOnce.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setRunOnce(cRunOnce.getSelection());
			}
		});
		bLetRun
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						listener.setLetRun(bLetRun.getSelection());
					}
				});
		label2 = new Label(gPeriod, SWT.NONE);
		label2.setText("Begin Time:");
		sBeginHours = new Spinner(gPeriod, SWT.NONE);
		sBeginHours.setToolTipText(Messages.getTooltip("period.begin.hours"));
		sBeginHours.setLayoutData(gridData11);
		sBeginHours.setMaximum(24);
		sBeginHours
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setBeginHours(sBeginHours.getSelection());
					}
				});
		label3 = new Label(gPeriod, SWT.NONE);
		label3.setText(":");
		sBeginMinutes = new Spinner(gPeriod, SWT.NONE);
		sBeginMinutes.setToolTipText(Messages
				.getTooltip("period.begin.minutes"));
		sBeginMinutes.setLayoutData(gridData21);
		sBeginMinutes.setMaximum(60);
		sBeginMinutes
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setBeginMinutes(sBeginMinutes.getSelection());
					}
				});
		label4 = new Label(gPeriod, SWT.NONE);
		label4.setText(":");
		sBeginSeconds = new Spinner(gPeriod, SWT.NONE);
		sBeginSeconds.setToolTipText(Messages
				.getTooltip("period.begin.seconds"));
		sBeginSeconds.setLayoutData(gridData3);
		sBeginSeconds.setMaximum(60);
		sBeginSeconds
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setBeginSeconds(sBeginSeconds.getSelection());
					}
				});
		label5 = new Label(gPeriod, SWT.NONE);
		label5.setText("hh:mm:ss");
		label5.setLayoutData(gridData2);
		label6 = new Label(gPeriod, SWT.NONE);
		label6.setText("End Time:");
		sEndHours = new Spinner(gPeriod, SWT.NONE);
		sEndHours.setToolTipText(Messages.getTooltip("period.end.hours"));
		sEndHours.setLayoutData(gridData41);
		sEndHours.setMaximum(24);
		sEndHours
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setEndHours(sEndHours.getSelection());
					}
				});
		label7 = new Label(gPeriod, SWT.NONE);
		label7.setText(":");
		sEndMinutes = new Spinner(gPeriod, SWT.NONE);
		sEndMinutes.setToolTipText(Messages.getTooltip("period.end.minutes"));
		sEndMinutes.setLayoutData(gridData5);
		sEndMinutes.setMaximum(60);
		sEndMinutes
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setEndMinutes(sEndMinutes.getSelection());
					}
				});
		label8 = new Label(gPeriod, SWT.NONE);
		label8.setText(":");
		sEndSeconds = new Spinner(gPeriod, SWT.NONE);
		sEndSeconds.setToolTipText(Messages.getTooltip("period.end.seconds"));
		sEndSeconds.setLayoutData(gridData6);
		sEndSeconds.setMaximum(60);
		label9 = new Label(gPeriod, SWT.NONE);
		label9.setText("hh:mm:ss");
		sEndSeconds
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setEndSeconds(sEndSeconds.getSelection());
					}
				});
		label10 = new Label(gPeriod, SWT.NONE);
		label10.setText("Repeat Time:");
		sRepeatHours = new Spinner(gPeriod, SWT.NONE);
		sRepeatHours.setToolTipText(Messages.getTooltip("period.repeat.hours"));
		sRepeatHours.setLayoutData(gridData7);
		sRepeatHours.setMaximum(24);
		sRepeatHours
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setRepeatHours(sRepeatHours.getSelection());
					}
				});
		label11 = new Label(gPeriod, SWT.NONE);
		label11.setText(":");
		sRepeatMinutes = new Spinner(gPeriod, SWT.NONE);
		sRepeatMinutes.setToolTipText(Messages
				.getTooltip("period.repeat.minutes"));
		sRepeatMinutes.setLayoutData(gridData8);
		sRepeatMinutes.setMaximum(60);
		sRepeatMinutes
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener
								.setRepeatMinutes(sRepeatMinutes.getSelection());
					}
				});
		label12 = new Label(gPeriod, SWT.NONE);
		label12.setText(":");
		sRepeatSeconds = new Spinner(gPeriod, SWT.NONE);
		sRepeatSeconds.setToolTipText(Messages
				.getTooltip("period.repeat.seconds"));
		sRepeatSeconds.setLayoutData(gridData9);
		sRepeatSeconds.setMaximum(99999999);
		label18 = new Label(gPeriod, SWT.NONE);
		label18.setText("hh:mm:ss or ss");
		sRepeatSeconds
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener
								.setRepeatSeconds(sRepeatSeconds.getSelection());
					}
				});
		label13 = new Label(gPeriod, SWT.NONE);
		label13.setText("Single Start:");
		sSingleHours = new Spinner(gPeriod, SWT.NONE);
		sSingleHours.setToolTipText(Messages
				.getTooltip("period.single_start.hours"));
		sSingleHours.setLayoutData(gridData10);
		sSingleHours.setMaximum(24);
		sSingleHours
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener.setSingleHours(sSingleHours.getSelection());
					}
				});
		label14 = new Label(gPeriod, SWT.NONE);
		label14.setText(":");
		sSingleMinutes = new Spinner(gPeriod, SWT.NONE);
		sSingleMinutes.setToolTipText(Messages
				.getTooltip("period.single_start.minutes"));
		sSingleMinutes.setLayoutData(gridData111);
		sSingleMinutes.setMaximum(60);
		sSingleMinutes
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener
								.setSingleMinutes(sSingleMinutes.getSelection());
					}
				});
		label15 = new Label(gPeriod, SWT.NONE);
		label15.setText(":");
		sSingleSeconds = new Spinner(gPeriod, SWT.NONE);
		sSingleSeconds.setToolTipText(Messages
				.getTooltip("period.single_start.seconds"));
		sSingleSeconds.setLayoutData(gridData12);
		sSingleSeconds.setMaximum(60);
		sSingleSeconds
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						listener
								.setSingleSeconds(sSingleSeconds.getSelection());
					}
				});
		label16 = new Label(gPeriod, SWT.NONE);
		label16.setText("hh:mm:ss");
		label16.setLayoutData(gridData4);
	}

	public void fillPeriod() {
		if (listener.getPeriod() != null) {

			sBeginHours.setSelection(listener.getBeginHours());
			sBeginMinutes.setSelection(listener.getBeginMinutes());
			sBeginSeconds.setSelection(listener.getBeginSeconds());

			sEndHours.setSelection(listener.getEndHours());
			sEndMinutes.setSelection(listener.getEndMinutes());
			sEndSeconds.setSelection(listener.getEndSeconds());

			int hours = listener.getRepeatHours();
			int minutes = listener.getRepeatMinutes();
			sRepeatHours.setSelection(hours);
			sRepeatMinutes.setSelection(minutes);
			sRepeatSeconds.setSelection(listener.getRepeatSeconds());

			sSingleHours.setSelection(listener.getSingleHours());
			sSingleMinutes.setSelection(listener.getSingleMinutes());
			sSingleSeconds.setSelection(listener.getSingleSeconds());

			bLetRun.setSelection(listener.getLetRun());
			if(cRunOnce.isVisible())
				cRunOnce.setSelection(listener.getRunOnce());
		}
	}

	public void setPeriod(Element period) {
		listener.setPeriod(period);
		fillPeriod();
	}

	public Element getPeriod() {
		return listener.getPeriod();
	}

	public void setEnabled(boolean enabled) {
		gPeriod.setEnabled(enabled);

		bLetRun.setEnabled(enabled);
		cRunOnce.setEnabled(enabled);
		sBeginHours.setEnabled(enabled);
		sBeginMinutes.setEnabled(enabled);
		sBeginSeconds.setEnabled(enabled);
		sEndHours.setEnabled(enabled);
		sEndMinutes.setEnabled(enabled);
		sEndSeconds.setEnabled(enabled);
		sRepeatHours.setEnabled(enabled);
		sRepeatMinutes.setEnabled(enabled);
		sRepeatSeconds.setEnabled(enabled);
		sSingleHours.setEnabled(enabled);
		sSingleMinutes.setEnabled(enabled);
		sSingleSeconds.setEnabled(enabled);

	}
	
	public void setRunOnce(boolean visible) {
		lRunOnce.setVisible(visible);
		cRunOnce.setVisible(visible);
	}

} // @jve:decl-index=0:visual-constraint="10,10"
