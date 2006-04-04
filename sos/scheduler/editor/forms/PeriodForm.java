package sos.scheduler.editor.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.listeners.PeriodListener;



public class PeriodForm extends Composite implements  IUpdateLanguage {
	private PeriodListener listener;
	
	private boolean onOrder;

	private Group gPeriod = null;

	private Label label1 = null;

	private Button bLetRun = null;

	private Label label2 = null;

	private Text sBeginHours = null;

	private Label label3 = null;

	private Text sBeginMinutes = null;

	private Label label4 = null;

	private Text sBeginSeconds = null;

	private Label label5 = null;

	private Label label6 = null;

	private Text sEndHours = null;

	private Label label7 = null;

	private Text sEndMinutes = null;

	private Label label8 = null;

	private Text sEndSeconds = null;

	private Label label10 = null;

	private Text sRepeatHours = null;

	private Label label11 = null;

	private Text sRepeatMinutes = null;

	private Label label12 = null;

	private Text sRepeatSeconds = null;

	private Label label13 = null;

	private Text sSingleHours = null;

	private Label label14 = null;

	private Text sSingleMinutes = null;

	private Label label15 = null;

	private Text sSingleSeconds = null;

	private Label label16 = null;

	private Label label9 = null;

	private Label label18 = null;

	private Label lRunOnce = null;

	private Button cRunOnce = null;
	private boolean event  =  true;
	private Button bApply = null;


	public PeriodForm(Composite parent, int style) {
		super(parent, style);
		initialize();
		setToolTipText();
		
		setRunOnce(false);



	}

	public PeriodForm(Composite parent, int style, DomParser dom, boolean onOrder) {
		this(parent, style);
		this.onOrder = onOrder;
		listener = new PeriodListener(dom);
	}

	public void setParams(DomParser dom, boolean onOrder) {
		this.onOrder = onOrder;
		listener = new PeriodListener(dom);
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(452, 219));
		GridData gridData12 = new GridData(42, SWT.DEFAULT);
		GridData gridData111 = new org.eclipse.swt.layout.GridData();
		gridData111.widthHint = 24;
		GridData gridData10 = new org.eclipse.swt.layout.GridData();
		gridData10.widthHint = 24;
		GridData gridData9 = new GridData(42, SWT.DEFAULT);
		GridData gridData8 = new org.eclipse.swt.layout.GridData();
		gridData8.widthHint = 24;
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.widthHint = 24;
		GridData gridData6 = new GridData(42, SWT.DEFAULT);
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.widthHint = 24;
		GridData gridData41 = new org.eclipse.swt.layout.GridData();
		gridData41.widthHint = 24;
		GridData gridData3 = new GridData(42, SWT.DEFAULT);
		GridData gridData21 = new GridData(24, SWT.DEFAULT);
		GridData gridData11 = new org.eclipse.swt.layout.GridData();
		gridData11.widthHint = 24;
		GridData gridData4 = new GridData();
		gridData4.horizontalSpan = 1;
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 7;
		gPeriod = new Group(this, SWT.NONE);
		gPeriod.setEnabled(false);
		gPeriod.setText("Period");
		gPeriod.setLayout(gridLayout);
		label1 = new Label(gPeriod, SWT.NONE);
		label1.setLayoutData(new GridData());
		label1.setText("Let Run:");
		GridData gridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
		bLetRun = new Button(gPeriod, SWT.CHECK);
		bLetRun.setLayoutData(gridData);
		bLetRun
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						listener.setLetRun(bLetRun.getSelection());
						if (bApply != null) {
							bApply.setEnabled(true);
						}
					}
				});
		GridData gridData101 = new GridData();
		lRunOnce = new Label(gPeriod, SWT.NONE);
		lRunOnce.setText("Run Once:");
		lRunOnce.setLayoutData(gridData101);
		new Label(gPeriod, SWT.NONE);
		GridData gridData112 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
		cRunOnce = new Button(gPeriod, SWT.CHECK);
		cRunOnce.setLayoutData(gridData112);
		cRunOnce.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.setRunOnce(cRunOnce.getSelection());
				if (bApply != null) {
					bApply.setEnabled(true);
				}

			}
		});
		label2 = new Label(gPeriod, SWT.NONE);
		label2.setText("Begin Time:");
		sBeginHours = new Text(gPeriod, SWT.BORDER);
		
	
		sBeginHours.setLayoutData(gridData11);

		sBeginHours
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) 
						listener.setPeriodTime(bApply,"begin",sBeginHours.getText(),sBeginMinutes.getText(),sBeginSeconds.getText());
					}
				});
		label3 = new Label(gPeriod, SWT.NONE);
		label3.setText(":");
		sBeginMinutes = new Text(gPeriod, SWT.BORDER);
		sBeginMinutes.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
			

			}
		});
		sBeginMinutes.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
			
			}
		});
		sBeginMinutes.setLayoutData(gridData21);
		sBeginMinutes
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"begin",sBeginHours.getText(),sBeginMinutes.getText(),sBeginSeconds.getText());
   				}
				});
		label4 = new Label(gPeriod, SWT.NONE);
		label4.setText(":");
		sBeginSeconds = new Text(gPeriod, SWT.BORDER);
		sBeginSeconds.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {

			}
		});
		sBeginSeconds.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {

			}
		});
		sBeginSeconds.setLayoutData(gridData3);

		sBeginSeconds
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"begin",sBeginHours.getText(),sBeginMinutes.getText(),sBeginSeconds.getText());
					}
				});
		label5 = new Label(gPeriod, SWT.NONE);
		label5.setText("hh:mm:ss");
		label5.setLayoutData(gridData2);
		label6 = new Label(gPeriod, SWT.NONE);
		label6.setText("End Time:");
		sEndHours = new Text(gPeriod, SWT.BORDER);
		sEndHours.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {

			}
		});
		sEndHours.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
			}
		});
		sEndHours.setLayoutData(gridData41);

		sEndHours
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"end",sEndHours.getText(),sEndMinutes.getText(),sEndSeconds.getText());
					}
				});
		label7 = new Label(gPeriod, SWT.NONE);
		label7.setText(":");
		sEndMinutes = new Text(gPeriod, SWT.BORDER);
		sEndMinutes.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
						}
		});
		sEndMinutes.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				
			}
		});
		sEndMinutes.setLayoutData(gridData5);

		sEndMinutes
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"end",sEndHours.getText(),sEndMinutes.getText(),sEndSeconds.getText());
					}
				});
		label8 = new Label(gPeriod, SWT.NONE);
		label8.setText(":");
		sEndSeconds = new Text(gPeriod, SWT.BORDER);
		sEndSeconds.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				
			}
		});
		sEndSeconds.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) { 
				
			}
		});
		sEndSeconds.setLayoutData(gridData6);

		label9 = new Label(gPeriod, SWT.NONE);
		label9.setText("hh:mm:ss");
		sEndSeconds
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"end",sEndHours.getText(),sEndMinutes.getText(),sEndSeconds.getText());
					}
				});
		label10 = new Label(gPeriod, SWT.NONE);
		label10.setText("Repeat Time:");
		sRepeatHours = new Text(gPeriod, SWT.BORDER);
		sRepeatHours.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				
			}
		});
		sRepeatHours.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				

			}
		});
		sRepeatHours.setLayoutData(gridData7);

		sRepeatHours
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"repeat",sRepeatHours.getText(),sRepeatMinutes.getText(),sRepeatSeconds.getText());
					}
				});
		label11 = new Label(gPeriod, SWT.NONE);
		label11.setText(":");
		sRepeatMinutes = new Text(gPeriod, SWT.BORDER);
		sRepeatMinutes.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				
			}
		});
		sRepeatMinutes.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				
			}
		});
		sRepeatMinutes.setLayoutData(gridData8);

		sRepeatMinutes
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"repeat",sRepeatHours.getText(),sRepeatMinutes.getText(),sRepeatSeconds.getText());
  				}
				});
		label12 = new Label(gPeriod, SWT.NONE);
		label12.setText(":");
		sRepeatSeconds = new Text(gPeriod, SWT.BORDER);
		sRepeatSeconds.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
							}
		});
		sRepeatSeconds.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				
			}
		});
		sRepeatSeconds.setLayoutData(gridData9);
		label18 = new Label(gPeriod, SWT.NONE);
		label18.setText("hh:mm:ss or ss");
		sRepeatSeconds
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"repeat",sRepeatHours.getText(),sRepeatMinutes.getText(),sRepeatSeconds.getText());
					}
				});
		label13 = new Label(gPeriod, SWT.NONE);
		label13.setText("Single Start:");
		sSingleHours = new Text(gPeriod, SWT.BORDER);
		sSingleHours.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				 
			}
		});
		sSingleHours.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				 
			}
		});
		sSingleHours.setLayoutData(gridData10);

		sSingleHours
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"single_start",sSingleHours.getText(),sSingleMinutes.getText(),sSingleSeconds.getText());
					}
				});
		label14 = new Label(gPeriod, SWT.NONE);
		label14.setText(":");
		sSingleMinutes = new Text(gPeriod, SWT.BORDER);
		sSingleMinutes.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				 
			}
		});
		sSingleMinutes.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				 
			}
		});
		sSingleMinutes.setLayoutData(gridData111);

		sSingleMinutes
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"single_start",sSingleHours.getText(),sSingleMinutes.getText(),sSingleSeconds.getText());
					}
				});
		label15 = new Label(gPeriod, SWT.NONE);
		label15.setText(":");
		sSingleSeconds = new Text(gPeriod, SWT.BORDER);
		sSingleSeconds.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				 
			}
		});
		sSingleSeconds.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				 
			}
		});
		sSingleSeconds.setLayoutData(gridData12);

		sSingleSeconds
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						if (event) listener.setPeriodTime(bApply,"single_start",sSingleHours.getText(),sSingleMinutes.getText(),sSingleSeconds.getText());
					}
				});
		label16 = new Label(gPeriod, SWT.NONE);
		label16.setText("hh:mm:ss");
		label16.setLayoutData(gridData4);				
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createGroup() {
	}

	public void fillPeriod() {
		event = false;
		if (listener.getPeriod() != null) {
			
			event = false;
			sBeginHours.setText(Utils.fill(2,String.valueOf(listener.getBeginHours())));
			sBeginMinutes.setText(Utils.fill(2,String.valueOf(listener.getBeginMinutes())));
			event = true;
			sBeginSeconds.setText(Utils.fill(2,String.valueOf(listener.getBeginSeconds())));

			event = false;
			sEndHours.setText(Utils.fill(2,String.valueOf(listener.getEndHours())));
			sEndMinutes.setText(Utils.fill(2,String.valueOf(listener.getEndMinutes())));
			event = true;
			sEndSeconds.setText(Utils.fill(2,String.valueOf(listener.getEndSeconds())));

			if(!onOrder) {

				event = false;
				sRepeatHours.setText(Utils.fill(2,String.valueOf(listener.getRepeatHours())));
				sRepeatMinutes.setText(Utils.fill(2,String.valueOf(listener.getRepeatMinutes())));
				event = true;
				sRepeatSeconds.setText(Utils.fill(2,String.valueOf(listener.getRepeatSeconds())));

				event = false;
				sSingleHours.setText(Utils.fill(2,String.valueOf(listener.getSingleHours())));
				sSingleMinutes.setText(Utils.fill(2,String.valueOf(listener.getSingleMinutes())));
				event = true;
				sSingleSeconds.setText(Utils.fill(2,String.valueOf(listener.getSingleSeconds())));

			if(cRunOnce.isVisible())
				cRunOnce.setSelection(listener.getRunOnce());
			}
			
			bLetRun.setSelection(listener.getLetRun());
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
		cRunOnce.setEnabled(enabled && !onOrder);
		sBeginHours.setEnabled(enabled);
		sBeginMinutes.setEnabled(enabled);
		sBeginSeconds.setEnabled(enabled);
		sEndHours.setEnabled(enabled);
		sEndMinutes.setEnabled(enabled);
		sEndSeconds.setEnabled(enabled);
		sRepeatHours.setEnabled(enabled && !onOrder);
		sRepeatMinutes.setEnabled(enabled && !onOrder);
		sRepeatSeconds.setEnabled(enabled && !onOrder);
		sSingleHours.setEnabled(enabled && !onOrder);
		sSingleMinutes.setEnabled(enabled && !onOrder);
		sSingleSeconds.setEnabled(enabled && !onOrder);


	}
	
	public void setRunOnce(boolean visible) {
		lRunOnce.setVisible(visible);
		cRunOnce.setVisible(visible);
	}
	public void setToolTipText(){
	 	bLetRun.setToolTipText(Messages.getTooltip("period.let_run"));
		cRunOnce.setToolTipText(Messages.getTooltip("run_time.once"));
		sBeginHours.setToolTipText(Messages.getTooltip("period.begin.hours"));
		sBeginMinutes.setToolTipText(Messages
				.getTooltip("period.begin.minutes"));
		sBeginSeconds.setToolTipText(Messages
				.getTooltip("period.begin.seconds"));
		sEndHours.setToolTipText(Messages.getTooltip("period.end.hours"));
		sEndMinutes.setToolTipText(Messages.getTooltip("period.end.minutes"));
		sEndSeconds.setToolTipText(Messages.getTooltip("period.end.seconds"));
		sRepeatHours.setToolTipText(Messages.getTooltip("period.repeat.hours"));
		sRepeatMinutes.setToolTipText(Messages
				.getTooltip("period.repeat.minutes"));
		sRepeatSeconds.setToolTipText(Messages
				.getTooltip("period.repeat.seconds"));
		sSingleHours.setToolTipText(Messages
				.getTooltip("period.single_start.hours"));
		sSingleMinutes.setToolTipText(Messages
				.getTooltip("period.single_start.minutes"));
		sSingleSeconds.setToolTipText(Messages
				.getTooltip("period.single_start.seconds"));
 
	 
  }	
	
	public void setApplyButton(Button b) {
		bApply = b;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
