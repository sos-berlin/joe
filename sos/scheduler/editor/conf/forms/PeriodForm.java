package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.PeriodListener;


public class PeriodForm extends Composite implements IUpdateLanguage {


	private PeriodListener listener                  = null;

	private boolean        onOrder;

	private Composite      gPeriod                    = null;

	private Label          label1                     = null;

	private Button         bLetRun                    = null;

	private Label          label2                     = null;

	private Text           sBeginHours                = null;

	private Label          label3                     = null;

	private Text           sBeginMinutes              = null;

	private Label          label4                     = null;

	private Text           sBeginSeconds              = null;

	private Label          label5                     = null;

	private Label          label6                     = null;

	private Text           sEndHours                  = null;

	private Label          label7                     = null;

	private Text           sEndMinutes                = null;

	private Label          label8                     = null;

	private Text           sEndSeconds                = null;

	private Label          label10                    = null;

	private Text           sRepeatHours               = null;

	private Label          label11                    = null;

	private Text           sRepeatMinutes             = null;

	private Label          label12                    = null;

	private Text           sRepeatSeconds             = null;

	private Label          label13                    = null;

	private Text           sSingleHours               = null;

	private Label          label14                    = null;

	private Text           sSingleMinutes             = null;

	private Label          label15                    = null;

	private Text           sSingleSeconds             = null;

	private Label          label16                    = null;

	private Label          label9                     = null;

	private Label          label18                    = null;

	private Label          lRunOnce                   = null;

	private Button         cRunOnce                   = null;

	private boolean        event                      = true;

	private Button         bApply                     = null;

	private String         savBeginHours              = "";

	private String         savBeginMinutes            = "";

	private String         savBeginSeconds            = "";

	private String         savEndHours                = "";

	private String         savEndMinutes              = "";

	private String         savEndSeconds              = "";

	private String         savRepeatHours             = "";

	private String         savRepeatMinutes           = "";

	private String         savRepeatSeconds           = "";
	
	private String         savAbsoluteHours          = "";

	private String         savAbsoluteMinutes        = "";

	private String         savAbsoluteSecounds       = "";
	
	private boolean        assistent                  = false;

	private Text           sAbsoluteRepeatHours       = null;

	private Text           sAbsoluteRepeatMinutes     = null;

	private Text           sAbsoluteRepeatSeconds     = null;

	private Label          lblAbsolutRepeat           = null;

	private Label          label_1                    = null; 

	private Label          label14_1                 = null;

	private Label          label18_1                 = null;

	private int            _type                     = Editor.PERIODS;

	public PeriodForm(Composite parent, int style, int type) {
		super(parent, style);
		_type = type;
		initialize();
		setToolTipText();

		setRunOnce(false);

	}

	public PeriodForm(Composite parent, int style, boolean assistent_) {		
		this(parent, style, Editor.JOB_WIZZARD);
		assistent = assistent_;
		

	}

	public void setParams(SchedulerDom dom, boolean onOrder) {
		this.onOrder = onOrder;
		listener = new PeriodListener(dom);
	}


	private void initialize() {
		
		this.setLayout(new FillLayout());				
		
		createTimeSlotGroup();
		
		if(_type != Editor.RUNTIME)
			createStartTimeGroup();

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
			if(_type != Editor.RUNTIME) {
				event = false;
				sAbsoluteRepeatHours.setText(Utils.fill(2, String.valueOf(listener.getAbsoluteRepeatHours())));
				sAbsoluteRepeatMinutes.setText(Utils.fill(2, String.valueOf(listener.getAbsoluteRepeatMinutes())));

				sAbsoluteRepeatSeconds.setText(Utils.fill(2, String.valueOf(listener.getAbsoluteRepeatSeconds())));
				event = true;
			}

			if (!onOrder) {
				if(_type != Editor.RUNTIME) {
					event = false;
					sRepeatHours.setText(Utils.fill(2, String.valueOf(listener.getRepeatHours())));
					sRepeatMinutes.setText(Utils.fill(2, String.valueOf(listener.getRepeatMinutes())));
					event = true;
					sRepeatSeconds.setText(Utils.fill(2, String.valueOf(listener.getRepeatSeconds())));

					event = false;
					sSingleHours.setText(Utils.fill(2, String.valueOf(listener.getSingleHours())));
					sSingleMinutes.setText(Utils.fill(2, String.valueOf(listener.getSingleMinutes())));
					event = true;
					sSingleSeconds.setText(Utils.fill(2, String.valueOf(listener.getSingleSeconds())));
				}
				if (cRunOnce.isVisible())
					cRunOnce.setSelection(listener.getRunOnce());
			}

			bLetRun.setSelection(listener.getLetRun());
			sBeginHours.setFocus();
		}else if(listener.getAtElement() != null) {
			if(_type != Editor.RUNTIME) {
				event = false;
				listener.setPeriod(listener.getAtElement());
				sSingleHours.setText(Utils.fill(2, String.valueOf(listener.getSingleHours())));
				sSingleMinutes.setText(Utils.fill(2, String.valueOf(listener.getSingleMinutes())));
				event = true;
				sSingleSeconds.setText(Utils.fill(2, String.valueOf(listener.getSingleSeconds())));
			}
		}
	}


	public void setPeriod(Element period) {
		listener.setPeriod(period);
		fillPeriod();
	}

	public void setAtElement(Element at) {
		listener.setAtElement(at);
		fillPeriod();
	}

	public Element getPeriod() {
		return listener.getPeriod();
	}


	public void setEnabled(boolean enabled) {
		
		boolean singleStart = false;
		
		if(_type != Editor.RUNTIME) {
			singleStart = (!(sSingleHours.getText() + sSingleMinutes.getText() + sSingleSeconds.getText()).trim().equals(""));
		}
		
		if(!enabled){
			savBeginHours              = "";
			savBeginMinutes            = "";
			savBeginSeconds            = "";
			savEndHours                = "";
			savEndMinutes              = "";
			savEndSeconds              = "";
			savRepeatHours             = "";
			savRepeatMinutes           = "";
			savRepeatSeconds           = "";
			savAbsoluteHours           = "";
			savAbsoluteMinutes         = "";
			savAbsoluteSecounds        = "";
		}
		
		gPeriod.setEnabled(enabled);
		

		bLetRun.setEnabled(enabled && !singleStart && !onOrder);

		if(singleStart && bLetRun.getSelection()) {
			bLetRun.setSelection(false);
			listener.setLetRun(false);
		}
		cRunOnce.setEnabled(enabled && !onOrder);
		sBeginHours.setEnabled(enabled && !singleStart);
		sBeginMinutes.setEnabled(enabled && !singleStart);
		sBeginSeconds.setEnabled(enabled && !singleStart);
		sEndHours.setEnabled(enabled && !singleStart);
		sEndMinutes.setEnabled(enabled && !singleStart);
		sEndSeconds.setEnabled(enabled && !singleStart);
		if(_type != Editor.RUNTIME) {
			sRepeatHours.setEnabled(enabled && !onOrder && !singleStart);
			sRepeatMinutes.setEnabled(enabled && !onOrder && !singleStart);
			sRepeatSeconds.setEnabled(enabled && !onOrder && !singleStart);

			sSingleHours.setEnabled(enabled && !onOrder);
			sSingleMinutes.setEnabled(enabled && !onOrder);
			sSingleSeconds.setEnabled(enabled && !onOrder);

			sAbsoluteRepeatHours.setEnabled(enabled && !singleStart);
			sAbsoluteRepeatMinutes.setEnabled(enabled && !singleStart);
			sAbsoluteRepeatSeconds.setEnabled(enabled && !singleStart);                
		}
		if (singleStart) {
			if (!sBeginHours.getText().equals(""))
				savBeginHours = sBeginHours.getText();
			if (!sBeginMinutes.getText().equals(""))
				savBeginMinutes = sBeginMinutes.getText();
			if (!sBeginSeconds.getText().equals(""))
				savBeginSeconds = sBeginSeconds.getText();
			if (!sEndHours.getText().equals(""))
				savEndHours = sEndHours.getText();
			if (!sEndMinutes.getText().equals(""))
				savEndMinutes = sEndMinutes.getText();
			if (!sEndSeconds.getText().equals(""))
				savEndSeconds = sEndSeconds.getText();
			if(_type != Editor.RUNTIME) {
				if (!sRepeatHours.getText().equals(""))
					savRepeatHours = sRepeatHours.getText();
				if (!sRepeatMinutes.getText().equals(""))
					savRepeatMinutes = sRepeatMinutes.getText();
				if (!sRepeatSeconds.getText().equals(""))
					savRepeatSeconds = sRepeatSeconds.getText();                       
				if (!sAbsoluteRepeatHours.getText().equals(""))
					savAbsoluteHours = sAbsoluteRepeatHours.getText();                        
				if (!sAbsoluteRepeatMinutes.getText().equals(""))
					savAbsoluteMinutes = sAbsoluteRepeatMinutes.getText();            
				if (!sAbsoluteRepeatSeconds.getText().equals(""))
					savAbsoluteSecounds = sAbsoluteRepeatSeconds.getText();
			}

			sBeginHours.setText("");
			sBeginMinutes.setText("");
			sBeginSeconds.setText("");
			sEndHours.setText("");
			sEndMinutes.setText("");
			sEndSeconds.setText("");
			if(_type != Editor.RUNTIME) {
				sRepeatHours.setText("");
				sRepeatMinutes.setText("");
				sRepeatSeconds.setText("");
				sAbsoluteRepeatHours.setText("");
				sAbsoluteRepeatMinutes.setText("");
				sAbsoluteRepeatSeconds.setText("");  
			}
		} else {
			event = false;
			if(_type != Editor.RUNTIME) {
				sSingleHours.setText("");
				sSingleMinutes.setText("");
				sSingleSeconds.setText("");
				listener.setPeriodTime(23, bApply, "single_start", "", "", "");
			}
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
			if(_type != Editor.RUNTIME) {
				if (!savRepeatHours.equals(""))
					sRepeatHours.setText(savRepeatHours);
				if (!savRepeatMinutes.equals(""))
					sRepeatMinutes.setText(savRepeatMinutes);
				if (!savRepeatSeconds.equals(""))
					sRepeatSeconds.setText(savRepeatSeconds);            
				if (!savAbsoluteHours.equals(""))
					sAbsoluteRepeatHours.setText(savAbsoluteHours);
				if (!savAbsoluteMinutes.equals(""))
					sAbsoluteRepeatMinutes.setText(savAbsoluteMinutes);
				if (!savAbsoluteSecounds.equals(""))
					sAbsoluteRepeatSeconds.setText(savAbsoluteSecounds); 
			}
		}

	}


	public void setRunOnce(boolean visible) {
		lRunOnce.setVisible(visible);
		cRunOnce.setVisible(visible);
	}


	private boolean beginBeforeAfter() {
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
		} else {

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
		bLetRun.setToolTipText(Messages.getTooltip("period.let_run"));
		cRunOnce.setToolTipText(Messages.getTooltip("run_time.once"));
		sBeginHours.setToolTipText(Messages.getTooltip("period.begin.hours"));
		sBeginMinutes.setToolTipText(Messages.getTooltip("period.begin.minutes"));
		sBeginSeconds.setToolTipText(Messages.getTooltip("period.begin.seconds"));
		sEndHours.setToolTipText(Messages.getTooltip("period.end.hours"));
		sEndMinutes.setToolTipText(Messages.getTooltip("period.end.minutes"));
		sEndSeconds.setToolTipText(Messages.getTooltip("period.end.seconds"));
		if(_type != Editor.RUNTIME) {
			sRepeatHours.setToolTipText(Messages.getTooltip("period.repeat.hours"));
			sRepeatMinutes.setToolTipText(Messages.getTooltip("period.repeat.minutes"));
			sRepeatSeconds.setToolTipText(Messages.getTooltip("period.repeat.seconds"));
			sSingleHours.setToolTipText(Messages.getTooltip("period.single_start.hours"));
			sSingleMinutes.setToolTipText(Messages.getTooltip("period.single_start.minutes"));
			sSingleSeconds.setToolTipText(Messages.getTooltip("period.single_start.seconds"));
		}

	}


	public void setApplyButton(Button b) {
		bApply = b;
	}

	public PeriodListener getListener() {
		return listener;
	}


	/**
	 * This method initializes group
	 */
	private void createTimeSlotGroup() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		//gPeriod = new Group(this, SWT.NONE);
		gPeriod = new Composite(this, SWT.NONE);
		gPeriod.setEnabled(false);
		gPeriod.setLayout(gridLayout);

		final Group groupSlottime = new Group(gPeriod, SWT.NONE);
		groupSlottime.setText("Time Slot Period");
		final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		groupSlottime.setLayoutData(gridData);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 8;
		groupSlottime.setLayout(gridLayout_1);
		label1 = new Label(groupSlottime, SWT.NONE);
		label1.setLayoutData(new GridData());
		label1.setText("Let Run:");
		bLetRun = new Button(groupSlottime, SWT.CHECK);
		bLetRun.setLayoutData(new GridData());
		bLetRun.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setLetRun(bLetRun.getSelection());
				if (bApply != null) {
					bApply.setEnabled(true);
				}
			}
		});
		new Label(groupSlottime, SWT.NONE);
		lRunOnce = new Label(groupSlottime, SWT.NONE);
		lRunOnce.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1));
		lRunOnce.setText("Run Once:");
		cRunOnce = new Button(groupSlottime, SWT.CHECK);
		cRunOnce.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
		cRunOnce.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setRunOnce(cRunOnce.getSelection());
				if (bApply != null) {
					bApply.setEnabled(true);
				}

			}
		});
		label2 = new Label(groupSlottime, SWT.NONE);
		final GridData gridData_1 = new GridData(86, SWT.DEFAULT);
		label2.setLayoutData(gridData_1);
		label2.setText("Begin Time:");
		sBeginHours = new Text(groupSlottime, SWT.BORDER);
		sBeginHours.setLayoutData(new GridData(24, SWT.DEFAULT));

		sBeginHours.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});

		sBeginHours.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 23, sBeginHours);
				}
				if (event)  {                	
					listener.setPeriodTime(23, bApply, "begin", sBeginHours.getText(), sBeginMinutes.getText(),
							sBeginSeconds.getText());
				}

			}
		});
		label3 = new Label(groupSlottime, SWT.NONE);
		label3.setLayoutData(new GridData());
		label3.setText(":");
		sBeginMinutes = new Text(groupSlottime, SWT.BORDER);
		sBeginMinutes.setLayoutData(new GridData(24, SWT.DEFAULT));
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
		sBeginMinutes.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 59, sBeginMinutes);
				}

				if (event)
					listener.setPeriodTime(23, bApply, "begin", sBeginHours.getText(), sBeginMinutes.getText(),
							sBeginSeconds.getText());
			}
		});
		label4 = new Label(groupSlottime, SWT.NONE);
		label4.setLayoutData(new GridData());
		label4.setText(":");
		sBeginSeconds = new Text(groupSlottime, SWT.BORDER);
		final GridData gridData_2 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
		gridData_2.widthHint = 24;
		sBeginSeconds.setLayoutData(gridData_2);
		sBeginSeconds.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);

			}
		});


		sBeginSeconds.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 59, sBeginSeconds);
				}
				if (event)
					listener.setPeriodTime(23, bApply, "begin", sBeginHours.getText(), sBeginMinutes.getText(),
							sBeginSeconds.getText()); 
			}
		});
		label5 = new Label(groupSlottime, SWT.NONE);
		label5.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		label5.setText("hh:mm:ss");
		label6 = new Label(groupSlottime, SWT.NONE);
		label6.setLayoutData(new GridData(86, SWT.DEFAULT));
		label6.setText("End Time:");
		sEndHours = new Text(groupSlottime, SWT.BORDER);
		sEndHours.setLayoutData(new GridData(24, SWT.DEFAULT));
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

		sEndHours.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 24, sEndHours);
				}
				if (event)
					listener.setPeriodTime(24, bApply, "end", sEndHours.getText(), sEndMinutes.getText(), sEndSeconds
							.getText());
			}
		});
		label7 = new Label(groupSlottime, SWT.NONE);
		label7.setLayoutData(new GridData());
		label7.setText(":");
		sEndMinutes = new Text(groupSlottime, SWT.BORDER);
		sEndMinutes.setLayoutData(new GridData(24, SWT.DEFAULT));
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

		sEndMinutes.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 59, sEndMinutes);
				}

				if (event)
					listener.setPeriodTime(24, bApply, "end", sEndHours.getText(), sEndMinutes.getText(), sEndSeconds
							.getText());
			}
		});
		label8 = new Label(groupSlottime, SWT.NONE);
		label8.setLayoutData(new GridData());
		label8.setText(":");
		sEndSeconds = new Text(groupSlottime, SWT.BORDER);
		final GridData gridData_3 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
		gridData_3.widthHint = 24;
		sEndSeconds.setLayoutData(gridData_3);
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
		sEndSeconds.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!beginBeforeAfter()) {
					Utils.setBackground(0, 59, sEndSeconds);
				}

				if (event)
					listener.setPeriodTime(24, bApply, "end", sEndHours.getText(), sEndMinutes.getText(), sEndSeconds
							.getText());
			}
		});

		label9 = new Label(groupSlottime, SWT.NONE);
		label9.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		label9.setText("hh:mm:ss");


	}

	private void createStartTimeGroup() {

		final Group startTimePeriodGroup = new Group(gPeriod, SWT.NONE);
		startTimePeriodGroup.setText("Start Time Period");
		startTimePeriodGroup.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 7;
		startTimePeriodGroup.setLayout(gridLayout);
		label10 = new Label(startTimePeriodGroup, SWT.NONE);
		final GridData gridData = new GridData(86, SWT.DEFAULT);
		gridData.horizontalIndent = -1;
		label10.setLayoutData(gridData);
		label10.setText("Repeat Time:");

		sRepeatHours = new Text(startTimePeriodGroup, SWT.BORDER);
		sRepeatHours.setLayoutData(new GridData(24, SWT.DEFAULT));


		sRepeatHours.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});

		sRepeatHours.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				Utils.setBackground(0, 23, sRepeatHours);
				if (!(sRepeatMinutes.getText() + sRepeatHours.getText()).equals("")) {
					Utils.setBackground(0, 59, sRepeatSeconds);
				} else {
					sRepeatSeconds.setBackground(null);
				}
				if (event)
					listener.setPeriodTime(23, bApply, "repeat", sRepeatHours.getText(), sRepeatMinutes.getText(),
							sRepeatSeconds.getText());
			}
		});
		label11 = new Label(startTimePeriodGroup, SWT.NONE);
		label11.setLayoutData(new GridData());
		label11.setText(":");

		sRepeatMinutes = new Text(startTimePeriodGroup, SWT.BORDER);
		sRepeatMinutes.setLayoutData(new GridData(24, SWT.DEFAULT));

		sRepeatMinutes.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);

			}
		});

		sRepeatMinutes.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				Utils.setBackground(0, 59, sRepeatMinutes);
				if (!(sRepeatMinutes.getText() + sRepeatHours.getText()).equals("")) {
					Utils.setBackground(0, 59, sRepeatSeconds);
				} else {
					sRepeatSeconds.setBackground(null);
				}
				if (event)
					listener.setPeriodTime(23, bApply, "repeat", sRepeatHours.getText(), sRepeatMinutes.getText(),
							sRepeatSeconds.getText());
			}
		});
		label12 = new Label(startTimePeriodGroup, SWT.NONE);
		label12.setLayoutData(new GridData());
		label12.setText(":");
		
		sRepeatSeconds = new Text(startTimePeriodGroup, SWT.BORDER);
		sRepeatSeconds.setLayoutData(new GridData(24, SWT.DEFAULT));
		
		sRepeatSeconds.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		
		sRepeatSeconds.addModifyListener(new org.eclipse.swt.events.ModifyListener() {        	
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (!(sRepeatMinutes.getText() + sRepeatHours.getText()).equals("")) {
					Utils.setBackground(0, 59, sRepeatSeconds);
				} else {
					sRepeatSeconds.setBackground(null);
				}
				if (event) {
					if (Utils.str2int(sRepeatSeconds.getText()) > 59) {
						listener.setRepeatSeconds(bApply, sRepeatSeconds.getText());
					} else {
						listener.setPeriodTime(23, bApply, "repeat", sRepeatHours.getText(), sRepeatMinutes.getText(),
								sRepeatSeconds.getText());
					}
				}
			}
		});
		label18 = new Label(startTimePeriodGroup, SWT.NONE);
		label18.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		label18.setText("hh:mm:ss or ss");

		lblAbsolutRepeat = new Label(startTimePeriodGroup, SWT.NONE);
		lblAbsolutRepeat.setText("Absolute Repeat:");

		sAbsoluteRepeatHours = new Text(startTimePeriodGroup, SWT.BORDER);
		sAbsoluteRepeatHours.setLayoutData(new GridData(24, SWT.DEFAULT));
		sAbsoluteRepeatHours.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(0, 23, sSingleHours);
				if (event) {
					listener.setPeriodTime(23, bApply, "absolute_repeat", sAbsoluteRepeatHours.getText(),
							sAbsoluteRepeatMinutes.getText(), sAbsoluteRepeatSeconds.getText());
					
				}
			}
		});
		sAbsoluteRepeatHours.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});

		label14_1 = new Label(startTimePeriodGroup, SWT.NONE);
		label14_1.setLayoutData(new GridData());
		label14_1.setText(":");

		sAbsoluteRepeatMinutes = new Text(startTimePeriodGroup, SWT.BORDER);
		sAbsoluteRepeatMinutes.setLayoutData(new GridData(24, SWT.DEFAULT));
		sAbsoluteRepeatMinutes.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(0, 23, sSingleHours);
				if (event) {
					listener.setPeriodTime(23, bApply, "absolute_repeat", sAbsoluteRepeatHours.getText(),
							sAbsoluteRepeatMinutes.getText(), sAbsoluteRepeatSeconds.getText());
					
				}
			}
		});
		sAbsoluteRepeatMinutes.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});

		label_1 = new Label(startTimePeriodGroup, SWT.NONE);
		label_1.setLayoutData(new GridData());
		label_1.setText(":");

		sAbsoluteRepeatSeconds = new Text(startTimePeriodGroup, SWT.BORDER);
		sAbsoluteRepeatSeconds.setLayoutData(new GridData(24, SWT.DEFAULT));
		sAbsoluteRepeatSeconds.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				Utils.setBackground(0, 23, sSingleHours);
				if (event) {
					listener.setPeriodTime(23, bApply, "absolute_repeat", sAbsoluteRepeatHours.getText(),
							sAbsoluteRepeatMinutes.getText(), sAbsoluteRepeatSeconds.getText());
					
				}
			}
		});
		sAbsoluteRepeatSeconds.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});

		label18_1 = new Label(startTimePeriodGroup, SWT.NONE);
		label18_1.setText("hh:mm:ss or ss");

		label13 = new Label(startTimePeriodGroup, SWT.NONE);
		label13.setText("Single Start:");
		label13.setVisible(!assistent);
		sSingleHours = new Text(startTimePeriodGroup, SWT.BORDER);
		sSingleHours.setLayoutData(new GridData(24, SWT.DEFAULT));
		sSingleHours.setVisible(!assistent);
		sSingleHours.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);

			}
		});

		sSingleHours.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {

				Utils.setBackground(0, 23, sSingleHours);
				if (event) {
					listener.setPeriodTime(23, bApply, "single_start", sSingleHours.getText(),
							sSingleMinutes.getText(), sSingleSeconds.getText());
					setEnabled(true);
				}
			}
		});
		label14 = new Label(startTimePeriodGroup, SWT.NONE);
		label14.setLayoutData(new GridData());
		label14.setText(":");
		label14.setVisible(!assistent);
		sSingleMinutes = new Text(startTimePeriodGroup, SWT.BORDER);
		sSingleMinutes.setLayoutData(new GridData(24, SWT.DEFAULT));
		sSingleMinutes.setVisible(!assistent);
		sSingleMinutes.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});

		sSingleMinutes.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				Utils.setBackground(0, 59, sSingleMinutes);
				if (event) {
					listener.setPeriodTime(23, bApply, "single_start", sSingleHours.getText(),
							sSingleMinutes.getText(), sSingleSeconds.getText());
					setEnabled(true);
				}
			}
		});
		label15 = new Label(startTimePeriodGroup, SWT.NONE);
		label15.setLayoutData(new GridData());
		label15.setText(":");
		label15.setVisible(!assistent);
		sSingleSeconds = new Text(startTimePeriodGroup, SWT.BORDER);
		final GridData gridData_1 = new GridData(24, SWT.DEFAULT);
		sSingleSeconds.setLayoutData(gridData_1);
		sSingleSeconds.setVisible(!assistent);
		sSingleSeconds.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});

		sSingleSeconds.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				Utils.setBackground(0, 59, sSingleSeconds);
				if (event) {
					listener.setPeriodTime(23, bApply, "single_start", sSingleHours.getText(),
							sSingleMinutes.getText(), sSingleSeconds.getText());
					setEnabled(true);
				}
			}
		});
		label16 = new Label(startTimePeriodGroup, SWT.NONE);
		label16.setLayoutData(new GridData());
		label16.setText("hh:mm:ss");        
		label16.setVisible(!assistent);

	}
	
} // @jve:decl-index=0:visual-constraint="10,10"
