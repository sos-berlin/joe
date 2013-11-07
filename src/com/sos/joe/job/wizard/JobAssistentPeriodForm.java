package com.sos.joe.job.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.*;

public class JobAssistentPeriodForm extends SOSJOEMessageCodes implements IUpdateLanguage {

	private PeriodListener	listener;

	private Group			gPeriod				= null;

	private Label			label2				= null;

	private Text			sBeginHours			= null;

	private Label			label3				= null;

	private Text			sBeginMinutes		= null;

	private Label			label4				= null;

	private Text			sBeginSeconds		= null;

	private Label			label5				= null;

	private Label			label6				= null;

	private Text			sEndHours			= null;

	private Label			label7				= null;

	private Text			sEndMinutes			= null;

	private Label			label8				= null;

	private Text			sEndSeconds			= null;

	private Label			label9				= null;

	private boolean			event				= true;

	private Button			bApply				= null;

	private String			savBeginHours		= "";

	private String			savBeginMinutes		= "";

	private String			savBeginSeconds		= "";

	private String			savEndHours			= "";

	private String			savEndMinutes		= "";

	private String			savEndSeconds		= "";

	private PeriodsListener	periodslistener		= null;

	public static String	EVERY_DAY			= "Every Day ";

	public static String	SPECIFIC_DAY		= "Specific Day ";

	public static String	WEEK_DAY			= "Week Day ";

	public static String	MONTH_DAY			= "Month Day";

	public static String	SPECIFIC_WEEK_DAY	= "Specific Weekday";

	public JobAssistentPeriodForm(Composite parent, int style) {
		super(parent, style);
		initialize();

		setRunOnce(false);
		GridData gridData6 = new GridData(42, SWT.DEFAULT);
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.widthHint = 24;
		GridData gridData41 = new org.eclipse.swt.layout.GridData();
		gridData41.widthHint = 24;
		GridData gridData3 = new GridData(42, SWT.DEFAULT);
		GridData gridData21 = new GridData(24, SWT.DEFAULT);
		GridData gridData11 = new org.eclipse.swt.layout.GridData();
		gridData11.widthHint = 24;
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 7;

		gPeriod = JOE_G_JobAssistent_Period.Control(new Group(this, SWT.NONE));
		gPeriod.setEnabled(true);
		// gPeriod.setText("Period");
		gPeriod.setLayout(gridLayout);

		label2 = JOE_L_JobAssistent_BeginTime.Control(new Label(gPeriod, SWT.NONE));
		// label2.setText("Begin Time:");

		sBeginHours = JOE_T_JobAssistent_BeginHours.Control(new Text(gPeriod, SWT.BORDER));
		sBeginHours.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		sBeginHours.setLayoutData(gridData11);
		sBeginHours.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 23, sBeginHours);
				}
				listener.setPeriodTime(23, bApply, "begin", sBeginHours.getText(), sBeginMinutes.getText(), sBeginSeconds.getText());
			}
		});

		label3 = JOE_L_Colon.Control(new Label(gPeriod, SWT.NONE));
		// label3.setText(":");

		sBeginMinutes = JOE_T_JobAssistent_BeginMinutes.Control(new Text(gPeriod, SWT.BORDER));
		sBeginMinutes.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		sBeginMinutes.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {

			}
		});
		sBeginMinutes.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {

			}
		});
		sBeginMinutes.setLayoutData(gridData21);
		sBeginMinutes.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 59, sBeginMinutes);
				}

				listener.setPeriodTime(23, bApply, "begin", sBeginHours.getText(), sBeginMinutes.getText(), sBeginSeconds.getText());
			}
		});

		label4 = JOE_L_Colon.Control(new Label(gPeriod, SWT.NONE));
		// label4.setText(":");

		sBeginSeconds = JOE_T_JobAssistent_BeginSeconds.Control(new Text(gPeriod, SWT.BORDER));
		sBeginSeconds.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);

			}
		});
		sBeginSeconds.setLayoutData(gridData3);
		sBeginSeconds.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 59, sBeginSeconds);
				}

				listener.setPeriodTime(23, bApply, "begin", sBeginHours.getText(), sBeginMinutes.getText(), sBeginSeconds.getText());
			}
		});

		label5 = JOE_L_JobAssistent_TimeFormat.Control(new Label(gPeriod, SWT.NONE));
		// label5.setText("hh:mm:ss");
		label5.setLayoutData(gridData2);

		label6 = JOE_L_JobAssistent_EndTime.Control(new Label(gPeriod, SWT.NONE));
		// label6.setText("End Time:");

		sEndHours = JOE_T_JobAssistent_EndHours.Control(new Text(gPeriod, SWT.BORDER));
		sEndHours.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		sEndHours.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
			}
		});
		sEndHours.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
			}
		});
		sEndHours.setLayoutData(gridData41);
		sEndHours.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 24, sEndHours);
				}

				listener.setPeriodTime(24, bApply, "end", sEndHours.getText(), sEndMinutes.getText(), sEndSeconds.getText());
			}
		});

		label7 = JOE_L_Colon.Control(new Label(gPeriod, SWT.NONE));
		// label7.setText(":");

		sEndMinutes = JOE_T_JobAssistent_EndMinutes.Control(new Text(gPeriod, SWT.BORDER));
		sEndMinutes.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		sEndMinutes.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
			}
		});
		sEndMinutes.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {

			}
		});
		sEndMinutes.setLayoutData(gridData5);

		sEndMinutes.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 59, sEndMinutes);
				}

				listener.setPeriodTime(24, bApply, "end", sEndHours.getText(), sEndMinutes.getText(), sEndSeconds.getText());
			}
		});

		label8 = JOE_L_Colon.Control(new Label(gPeriod, SWT.NONE));
		// label8.setText(":");

		sEndSeconds = JOE_T_JobAssistent_EndSeconds.Control(new Text(gPeriod, SWT.BORDER));
		sEndSeconds.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		sEndSeconds.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
			}
		});
		sEndSeconds.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
			}
		});
		sEndSeconds.setLayoutData(gridData6);

		label9 = JOE_L_JobAssistent_TimeFormat.Control(new Label(gPeriod, SWT.NONE));
		// label9.setText("hh:mm:ss");

		sEndSeconds.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 59, sEndSeconds);
				}

				listener.setPeriodTime(24, bApply, "end", sEndHours.getText(), sEndMinutes.getText(), sEndSeconds.getText());
			}
		});
		// Format
		new Label(gPeriod, SWT.NONE);

		setToolTipText();
	}

	public JobAssistentPeriodForm(Composite parent, int style, SchedulerDom dom, boolean onOrder) {
		this(parent, style);
		listener = new PeriodListener(dom);
	}

	public void setParams(SchedulerDom dom, boolean onOrder, PeriodsListener periodslistener_) {
		listener = new PeriodListener(dom);
		periodslistener = periodslistener_;
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(452, 219));
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
	}

	public void fillPeriod() {
		event = false;
		if (listener.getPeriod() != null) {

			event = false;
			sBeginHours.setText(Utils.fill(2, String.valueOf(listener.getBeginHours())));
			sBeginMinutes.setText(Utils.fill(2, String.valueOf(listener.getBeginMinutes())));
			event = true;
			sBeginSeconds.setText(Utils.fill(2, String.valueOf(listener.getBeginSeconds())));

			event = false;
			sEndHours.setText(Utils.fill(2, String.valueOf(listener.getEndHours())));
			sEndMinutes.setText(Utils.fill(2, String.valueOf(listener.getEndMinutes())));
			event = true;
			sEndSeconds.setText(Utils.fill(2, String.valueOf(listener.getEndSeconds())));

			sBeginHours.setFocus();
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
		sBeginHours.setEnabled(enabled);
		sBeginMinutes.setEnabled(enabled);
		sBeginSeconds.setEnabled(enabled);
		sEndHours.setEnabled(enabled);
		sEndMinutes.setEnabled(enabled);
		sEndSeconds.setEnabled(enabled);

		event = false;
		listener.setPeriodTime(23, bApply, "single_start", "", "", "");
		event = true;

		if (!savBeginHours.equals(""))
			sBeginHours.setText(savBeginHours);
		if (!savBeginMinutes.equals(""))
			sBeginMinutes.setText(savBeginMinutes);
		if (!savBeginSeconds.equals(""))
			sBeginSeconds.setText(savBeginSeconds);
		if (!savEndHours.equals(""))
			sEndHours.setText(savEndHours);
		if (!savEndMinutes.equals(""))
			sEndMinutes.setText(savEndMinutes);
		if (!savEndSeconds.equals(""))
			sEndSeconds.setText(savEndSeconds);

	}

	public void setRunOnce(boolean visible) {
	}

	private boolean beginBeforeAfter() {
		if (listener.getPeriod() == null) {
			listener.setPeriod(periodslistener.getNewPeriod());
		}
		int bh = Utils.str2int(0, sBeginHours.getText());
		int bm = Utils.str2int(0, sBeginMinutes.getText());
		int bs = Utils.str2int(0, sBeginSeconds.getText());
		int eh = Utils.str2int(0, sEndHours.getText());
		int em = Utils.str2int(0, sEndMinutes.getText());
		int es = Utils.str2int(0, sEndSeconds.getText());
		int gbs = bs + (bm * 60) + (bh * 60 * 60);
		int ges = es + (em * 60) + (eh * 60 * 60);
		if (gbs > ges && gbs != 0 && ges != 0 && bh < 24 && bm < 60 && bs < 60 && eh < 24 && em < 60 && es < 60) {
			Utils.setBackground(99, 0, sBeginHours);
			Utils.setBackground(99, 0, sBeginMinutes);
			Utils.setBackground(99, 0, sBeginSeconds);
			Utils.setBackground(99, 0, sEndHours);
			Utils.setBackground(99, 0, sEndMinutes);
			Utils.setBackground(99, 0, sEndSeconds);
			return true;
		}
		else {

			Utils.setBackground(0, 23, sBeginHours);
			Utils.setBackground(0, 59, sBeginMinutes);
			Utils.setBackground(0, 59, sBeginSeconds);
			Utils.setBackground(0, 24, sEndHours);
			Utils.setBackground(0, 59, sEndMinutes);
			Utils.setBackground(0, 59, sEndSeconds);

			return false;
		}
	}

	public void setToolTipText() {
		// sBeginHours.setToolTipText(Messages.getTooltip("period.begin.hours"));
		// sBeginMinutes.setToolTipText(Messages.getTooltip("period.begin.minutes"));
		// sBeginSeconds.setToolTipText(Messages.getTooltip("period.begin.seconds"));
		// sEndHours.setToolTipText(Messages.getTooltip("period.end.hours"));
		// sEndMinutes.setToolTipText(Messages.getTooltip("period.end.minutes"));
		// sEndSeconds.setToolTipText(Messages.getTooltip("period.end.seconds"));
	}

	public void setApplyButton(Button b) {
		bApply = b;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
