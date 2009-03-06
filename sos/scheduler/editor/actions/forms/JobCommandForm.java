package sos.scheduler.editor.actions.forms;

import javax.xml.transform.TransformerException;
import sos.scheduler.editor.app.Editor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;
import sos.scheduler.editor.app.Options;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.actions.forms.ActionsForm;
import sos.scheduler.editor.actions.ActionsDom;
import sos.scheduler.editor.actions.listeners.JobCommandListener;
import sos.util.SOSDate;

public class JobCommandForm extends Composite implements IUnsaved, IUpdateLanguage {

	private JobCommandListener listener             = null;

	private Group              jobsAndOrdersGroup   = null;

	private Label              lblJob               = null;

	private Text               tTitle               = null;

	private Combo              tState               = null;

	private Combo              cboTimes          = null;

	private Text               tPriority            = null;

	private Combo              cJobchain            = null;

	private Button             bReplace             = null;

	private Text               tJob                 = null;

	private int                type                 = -1;

	private Label              jobchainLabel        = null;

	private Label              priorityLabel        = null;

	private Label              titleLabel           = null;

	private Label              stateLabel           = null;

	private Label              replaceLabel         = null;

	private Combo              cboEndstate          = null; 

	private Label              endStateLabel        = null;

	private boolean            event                = false;

	private Text               txtYear              = null; 

	private Text               txtMonth             = null;

	private Text               txtDay               = null;

	private Text               txtHour              = null;

	private Text               txtMin               = null;

	private Text               txtSec               = null;






	public JobCommandForm(Composite parent, int style, ActionsDom dom, Element command, ActionsForm main)
	throws JDOMException, TransformerException {
		super(parent, style);

		listener = new JobCommandListener(dom, command, main);
		if(command.getName().equalsIgnoreCase("start_job")) {
			type = Editor.JOB;
		} else {
			type = Editor.COMMANDS;
		}

		initialize();

		setToolTipText();			
		event = true;
	}


	public void apply() {
		//if (isUnsaved())
		//addParam();
		//addCommand();
	}


	public boolean isUnsaved() {
		return false;
	}


	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();

	}


	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 3;
		jobsAndOrdersGroup = new Group(this, SWT.NONE);
		jobsAndOrdersGroup.setText("Commands for Job: " + listener.getName() ); //+ (listener.isDisabled() ? " (Disabled)" : ""));
		jobsAndOrdersGroup.setText("Command: " + listener.getCommandName() ); 
		jobsAndOrdersGroup.setLayout(gridLayout2);

		jobchainLabel = new Label(jobsAndOrdersGroup, SWT.NONE);
		final GridData gridData_10 = new GridData();
		jobchainLabel.setLayoutData(gridData_10);
		jobchainLabel.setText("Job chain");

		cJobchain = new Combo(jobsAndOrdersGroup, SWT.NONE);
		cJobchain.setEnabled(false);
		cJobchain.setItems(listener.getJobChains());		
		cJobchain.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(!event)
					return;

				listener.setJobChain(cJobchain.getText());	

				String curstate = Utils.getAttributeValue("state", listener.getCommand());

				tState.setItems(listener.getStates());                		
				tState.setText(curstate);


				String curEndstate =  Utils.getAttributeValue("end_state", listener.getCommand());

				cboEndstate.setItems(listener.getStates());
				cboEndstate.setText(curEndstate);


			}
		});

		final GridData gridData_8 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		gridData_8.widthHint = 114;
		cJobchain.setLayoutData(gridData_8);
		lblJob = new Label(jobsAndOrdersGroup, SWT.NONE);
		lblJob.setLayoutData(new GridData(73, SWT.DEFAULT));
		lblJob.setText("Job / Order ID");

		tJob = new Text(jobsAndOrdersGroup, SWT.BORDER);
		tJob.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(type == Editor.JOB){
					listener.setJob(tJob.getText());
				} else {
					listener.setOrderId(tJob.getText());
				}

			}
		});
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		tJob.setLayoutData(gridData_3);
		final Label startAtLabel = new Label(jobsAndOrdersGroup, SWT.NONE);
		startAtLabel.setLayoutData(new GridData());
		startAtLabel.setText("Start at");

		final Composite composite = new Composite(jobsAndOrdersGroup, SWT.NONE);
		final GridData gridData = new GridData(GridData.BEGINNING, GridData.FILL, false, false);
		composite.setLayoutData(gridData);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 11;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		composite.setLayout(gridLayout);

		txtYear = new Text(composite, SWT.BORDER);
		txtYear.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(!event)
					return;
				setTime();
			}
		});
		txtYear.setEnabled(false);
		txtYear.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text); 
			}
		});
		txtYear.setTextLimit(4);
		final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_7.widthHint = 40;
		txtYear.setLayoutData(gridData_7);

		final Label label = new Label(composite, SWT.NONE);
		label.setText("-");

		txtMonth = new Text(composite, SWT.BORDER);
		txtMonth.addFocusListener(new FocusAdapter() {
			public void focusLost(final FocusEvent e) {
				//txtMonth.setText(Utils.fill(2, txtMonth.getText()));		
			}
		});
		txtMonth.setEnabled(false);
		txtMonth.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {	
				if(!event)
					return;
				Utils.setBackground(1, 12, txtMonth);
				if(!txtMonth.getBackground().equals(Options.getRequiredColor()))
					setTime();
			}
		});
		txtMonth.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text); 
			}
		});
		txtMonth.setTextLimit(2);
		final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_9.widthHint = 20;
		txtMonth.setLayoutData(gridData_9);

		final Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("-");

		txtDay = new Text(composite, SWT.BORDER);
		txtDay.addFocusListener(new FocusAdapter() {
			public void focusLost(final FocusEvent e) {
				//txtDay.setText(Utils.fill(2, txtDay.getText()));
			}
		});
		txtDay.setEnabled(false);
		txtDay.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(!event)
					return;
				Utils.setBackground(1, 31, txtDay);
				if(!txtDay.getBackground().equals(Options.getRequiredColor()))
					setTime();
			}
		});
		txtDay.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text); 
			}
		});
		txtDay.setTextLimit(2);
		final GridData gridData_13 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_13.widthHint = 20;
		txtDay.setLayoutData(gridData_13);

		final Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("      ");

		txtHour = new Text(composite, SWT.BORDER);
		txtHour.addFocusListener(new FocusAdapter() {
			public void focusLost(final FocusEvent e) {
				//txtHour.setText(Utils.fill(2, txtHour.getText()));
			}
		});
		txtHour.setEnabled(false);
		txtHour.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(!event)
					return;
				Utils.setBackground(0, 24, txtHour);
				if(!txtHour.getBackground().equals(Options.getRequiredColor()))
					setTime();
			}
		});
		txtHour.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text); 
			}
		});
		txtHour.setTextLimit(2);
		final GridData gridData_14 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_14.widthHint = 20;
		txtHour.setLayoutData(gridData_14);

		final Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText(":");

		txtMin = new Text(composite, SWT.BORDER);
		txtMin.addFocusListener(new FocusAdapter() {
			public void focusLost(final FocusEvent e) {
				//txtMin.setText(Utils.fill(2, txtMin.getText()));
			}
		});
		txtMin.setEnabled(false);
		txtMin.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(!event)
					return;
				Utils.setBackground(0, 60, txtMin);
				if(!txtMin.getBackground().equals(Options.getRequiredColor()))
					setTime();
			}
		});
		txtMin.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text); 
			}
		});
		txtMin.setTextLimit(2);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_1.widthHint = 20;
		txtMin.setLayoutData(gridData_1);

		final Label label_4 = new Label(composite, SWT.NONE);
		label_4.setText(":");

		txtSec = new Text(composite, SWT.BORDER);
		txtSec.addFocusListener(new FocusAdapter() {
			public void focusLost(final FocusEvent e) {
				//txtSec.setText(Utils.fill(2, txtSec.getText()));
			}
		});
		txtSec.setEnabled(false);
		txtSec.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(!event)
					return;
				Utils.setBackground(0, 60, txtSec);
				if(!txtSec.getBackground().equals(Options.getRequiredColor()))
					setTime();
			}
		});
		txtSec.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text); 
			}
		});
		txtSec.setTextLimit(2);
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_6.widthHint = 20;
		txtSec.setLayoutData(gridData_6);

		cboTimes = new Combo(jobsAndOrdersGroup, SWT.READ_ONLY);
		cboTimes.setVisibleItemCount(7);

		cboTimes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				normalized(cboTimes.getText());
				setTime();
				initTimes(listener.getAt());
			}
		});
		cboTimes.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		priorityLabel = new Label(jobsAndOrdersGroup, SWT.NONE);
		final GridData gridData_11 = new GridData();
		priorityLabel.setLayoutData(gridData_11);
		priorityLabel.setText("Priority");

		tPriority = new Text(jobsAndOrdersGroup, SWT.BORDER);
		tPriority.setEnabled(false);
		tPriority.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setPriority(tPriority.getText());

			}
		});
		tPriority.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
		titleLabel = new Label(jobsAndOrdersGroup, SWT.NONE);
		titleLabel.setLayoutData(new GridData());
		titleLabel.setText("Title");

		tTitle = new Text(jobsAndOrdersGroup, SWT.BORDER);
		tTitle.setEnabled(false);
		tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				listener.setTitle(tTitle.getText());

			}
		});

		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		tTitle.setLayoutData(gridData_5);

		stateLabel = new Label(jobsAndOrdersGroup, SWT.NONE);
		stateLabel.setLayoutData(new GridData());
		stateLabel.setText("State");

		tState = new Combo(jobsAndOrdersGroup, SWT.BORDER);
		tState.setEnabled(false);
		tState.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(event)
					listener.setState(tState.getText());

			}
		});
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
		tState.setLayoutData(gridData_2);

		endStateLabel = new Label(jobsAndOrdersGroup, SWT.NONE);
		endStateLabel.setLayoutData(new GridData());
		endStateLabel.setText("End State");

		cboEndstate = new Combo(jobsAndOrdersGroup, SWT.NONE);
		cboEndstate.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(event)
					listener.setEndState(cboEndstate.getText());
			}
		});
		cboEndstate.setEnabled(false);
		cboEndstate.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

		replaceLabel = new Label(jobsAndOrdersGroup, SWT.NONE);
		final GridData gridData_12 = new GridData();
		replaceLabel.setLayoutData(gridData_12);
		replaceLabel.setText("Replace");

		bReplace = new Button(jobsAndOrdersGroup, SWT.CHECK);
		bReplace.setSelection(true);
		bReplace.setEnabled(true);
		bReplace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.setReplace(bReplace.getSelection() ? "yes" : "no");

			}
		});
		bReplace.setLayoutData(new GridData());
		new Label(jobsAndOrdersGroup, SWT.NONE);

		createSashForm();
	}




	/**
	 * This method initializes sashForm
	 */
	private void createSashForm() {
		createGroup2();
	}


	/**
	 * This method initializes group2
	 */
	private void createGroup2() {
		if(type == Editor.JOB){
			setCommandsEnabled(false);			
		} else {			
			setCommandsEnabled(true);
		}
		clearFields();
		fillCommand();


	}



	private void clearFields() {
		if(type == Editor.JOB){

			tState.setVisible(false);
			cboEndstate.setVisible(false);
			tPriority.setVisible(false);
			cJobchain.setVisible(false);
			tTitle.setVisible(false);
			bReplace.setVisible(false);
			jobchainLabel.setVisible(false);
			priorityLabel.setVisible(false);
			titleLabel.setVisible(false);			
			stateLabel.setVisible(false);
			endStateLabel.setVisible(false);
			replaceLabel.setVisible(false);
			lblJob.setText("Job");
		} else {
			lblJob.setText("Order Id");			
		}
		tJob.setVisible(true);

	}


	private void setCommandsEnabled(boolean b) {
		tState.setEnabled(b);
		cboEndstate.setEnabled(b);
		tPriority.setEnabled(b);
		cJobchain.setEnabled(b);
		tTitle.setEnabled(b);
		bReplace.setEnabled(b);
	}


	public Element getCommand() {
		return listener.getCommand();
	}


	public void fillCommand() {

		if (listener.getCommand() !=  null) {
			String startAt = Utils.getAttributeValue("at", listener.getCommand());
			if (startAt == null || startAt.length() == 0)
				startAt = "now";

			cboTimes.setItems(JobCommandListener.START_TIMES);

			initTimes(startAt);


			if (type == Editor.COMMANDS) {
				cJobchain.setText(Utils.getAttributeValue("job_chain", listener.getCommand()));
				tJob.setText(Utils.getAttributeValue("id", listener.getCommand()));
				tTitle.setText(Utils.getAttributeValue("title", listener.getCommand()));
				tState.setItems(listener.getStates());
				tState.setText(Utils.getAttributeValue("state", listener.getCommand()));
				cboEndstate.setItems(listener.getStates());
				cboEndstate.setText(Utils.getAttributeValue("end_state", listener.getCommand()));

				tPriority.setText(Utils.getAttributeValue("priority", listener.getCommand()));
				bReplace.setSelection(Utils.getAttributeValue("replace", listener.getCommand()).equals("yes"));
			} else {
				tJob.setText(Utils.getAttributeValue("job", listener.getCommand()));
			}

		}
	}

	private void initTimes(String startAt) {
		String year   = "";
		String month  = "";
		String day    = "";
		String hour   = txtHour.getText();
		String min    = txtMin.getText();
		String sec    = txtSec.getText();

		boolean havesec     = false;
		boolean haveTime    = false;		
		boolean havenow     = false;
		boolean haveperiod  = false;
		int whichtime = 0; 




		if (startAt.equals("now")) {
			havenow = true;
			whichtime = 0;
		} else if (startAt.startsWith("period")) {
			haveperiod = true;
			whichtime = 1;
		} else if(startAt.startsWith("now")){
			startAt = startAt.trim();
			havenow = true;
			haveTime = true;
			String[] split = startAt.split("\\+");
			if(split.length == 2) {
				String[] time = split[1].split(":");
				if(time.length == 3) {     
					whichtime = 3;
					hour = time[0] != null && time[0].length() > 0 ? Utils.fill(2, time[0]) : "00";
					min = time[1] != null && time[1].length() > 0 ? Utils.fill(2, time[1]) : "00";
					sec = time[2] != null && time[2].length() > 0 ? Utils.fill(2, time[2]) : "00";
					havesec = true;	
				} else if(time.length == 2) {
					/*hour = "00";
					min = time[0] != null && time[0].length() > 0 ? Utils.fill(2, time[0]) : "00";
					sec = time[1] != null && time[1].length() > 0 ? Utils.fill(2, time[1]) : "00";
					*/
					whichtime = 4;
					hour = time[0] != null && time[0].length() > 0 ? Utils.fill(2, time[0]) : "00";
					min = time[1] != null && time[1].length() > 0 ? Utils.fill(2, time[1]) : "00";
					havesec = false;	
				} else if(time.length == 1) {
					havesec = true;		
					whichtime = 5;
					sec = time[0] != null && time[0].length() > 0 ? Utils.fill(2, time[0]) : "00";
				}
			}


		} else {
			whichtime = 6;
			haveTime = true;
			//yyyy-MM-dd HH:mm:ss
			if (startAt.indexOf("-") > -1 && startAt.indexOf(":") > -1) {
				//hat date und time
				String[] dt = startAt.split(" ");			
				String[] date = dt[0].split("-");
				String[] time = dt[1].split(":");

				year = date[0] != null && date[0].length() > 0 ? Utils.fill(4, date[0]) : "";
				month= date[1] != null && date[1].length() > 0 ? Utils.fill(2, date[1]) : "01";
				day  = date[2] != null && date[2].length() > 0 ? Utils.fill(2, date[2]) : "01";

				hour = time.length > 0 &&  time[0] != null && time[0].length() > 0 ? Utils.fill(2, time[0]) : "00";
				min = time.length > 1 && time[1] != null && time[1].length() > 0 ? Utils.fill(2, time[1]) : "00";
				sec = time.length > 2 && time[2] != null && time[2].length() > 0 ? Utils.fill(2, time[2]) : "00";


				/*try {
					hour = time.length > 0 &&  time[0] != null && time[0].length() > 0 ? Utils.fill(2, time[0]) : String.valueOf(SOSDate.getCurrentDateAsString("HH"));
					min = time.length > 1 && time[1] != null && time[1].length() > 0 ? Utils.fill(2, time[1]) : String.valueOf(SOSDate.getCurrentDateAsString("mm"));
					sec = time.length > 2 && time[2] != null && time[2].length() > 0 ? Utils.fill(2, time[2]) : String.valueOf(SOSDate.getCurrentDateAsString("ss"));
				} catch(Exception e) {}
				 */
			} else if((startAt.indexOf("-") > -1)) {
				//hat nur date
				String[] date = startAt.split("-");

				year = date[0] != null && date[0].length() > 0 ? Utils.fill(4, date[0]) : "";
				month= date[1] != null && date[1].length() > 0 ? Utils.fill(2, date[1]) : "01";
				day  = date[2] != null && date[2].length() > 0 ? Utils.fill(2, date[2]) : "01";

			} else if((startAt.indexOf(":") > -1)) {
				//hat nur time
				String[] time = startAt.split(";");
				hour = time[0] != null && time[0].length() > 0 ? Utils.fill(2, time[0]) : "00";
				min = time[1] != null && time[1].length() > 0 ? Utils.fill(2, time[1]) : "00";
				sec = time[2] != null && time[2].length() > 0 ? Utils.fill(2, time[2]) : "00";

			}


		}

		if(haveTime) {
			txtYear.setText(year);
			txtMonth.setText(month);
			txtDay.setText(day);
			txtHour.setText(hour.trim());
			txtMin.setText(min.trim());
			txtSec.setText(sec.trim());
		}
		
		if(!event) {
			switch (whichtime) {
			case 1: cboTimes.setText("now");
			        break;
			        
			case 2: cboTimes.setText("period");
			        break;
			        
			case 3: cboTimes.setText("now + HH:mm:ss");
			        txtHour.setFocus();
			        break;
			
			case 4: cboTimes.setText("now + HH:mm");
			        txtHour.setFocus();
			        break;
			        
			case 5: cboTimes.setText("now + SECOUNDS");
			        txtSec.setFocus();

			case 6: cboTimes.setText("yyyy-MM-dd HH:mm:ss");
 			        txtYear.setFocus();
			        break;
			default: cboTimes.setText("now");
			
			}
		}
		
		//if(!event) {// initialisierung
		/*if(havenow && haveTime && havesec) {
			cboTimes.setText("now + HH:mm:ss");
			txtHour.setFocus();
		} else if(havenow && haveTime) {
			cboTimes.setText("now + HH:mm");
			txtHour.setFocus();
		} else if(havesec) {
			cboTimes.setText("now + SECOUNDS");
			txtSec.setFocus();
		} else if(havenow){
			cboTimes.setText("now");
		} else if(haveperiod){
			cboTimes.setText("period");
		} else {
			cboTimes.setText("yyyy-MM-dd HH:mm:ss");
			txtYear.setFocus();
		}
		*/
		//}
		normalized(cboTimes.getText());
	}

	public void setToolTipText() {

		//tStartAt.setToolTipText(Messages.getTooltip("jobcommand.startat"));
		txtYear.setToolTipText(Messages.getTooltip("jobcommand.startat"));
		txtMonth.setToolTipText(Messages.getTooltip("jobcommand.startat"));
		txtDay.setToolTipText(Messages.getTooltip("jobcommand.startat"));
		txtHour.setToolTipText(Messages.getTooltip("jobcommand.startat"));
		txtMin.setToolTipText(Messages.getTooltip("jobcommand.startat"));
		txtSec.setToolTipText(Messages.getTooltip("jobcommand.startat"));

		tTitle.setToolTipText(Messages.getTooltip("jobcommand.title"));
		tPriority.setToolTipText(Messages.getTooltip("jobcommand.priority"));
		tState.setToolTipText(Messages.getTooltip("jobcommand.state"));		
		cboEndstate.setToolTipText(Messages.getTooltip("jobcommand.end_state"));
		bReplace.setToolTipText(Messages.getTooltip("jobcommand.replaceorder"));
		cJobchain.setToolTipText(Messages.getTooltip("jobcommand.jobchain"));
		tJob.setToolTipText(Messages.getTooltip("jobcommand.job_order_id"));
		cboTimes.setToolTipText(Messages.getTooltip("jobcommand.starttimes"));

	}


	private String setTime() {

		event = false;
		
		String retVal = "";
		if(cboTimes.getText().equals("period")) {		
			retVal =  "period";
		}else if(cboTimes.getText().equals("now")) {			
			retVal =  "now";
		} else if(cboTimes.getText().startsWith("now ")) {
			retVal = "now + ";
			if(txtHour.getEnabled()) {
				retVal = retVal + Utils.fill(2, txtHour.getText().length() == 0 ? "00" : txtHour.getText()) + ":";
				//retVal = retVal + "00" + ":";
			}
			if(txtMin.getEnabled()) {
				retVal = retVal + Utils.fill(2, txtMin.getText().length() == 0 ? "00" : txtMin.getText());
				//retVal = retVal + "00";
				if(txtSec.getEnabled()) {
					retVal = retVal + ":";
				}
			}

			if(txtSec.getEnabled()) {
				retVal = retVal + Utils.fill(2, txtSec.getText().length() == 0? "00" : txtSec.getText());				
			    //retVal = retVal + "00";
			}

		} else {

			retVal  = Utils.fill(4, txtYear.getText()) + "-" + 
			Utils.fill(2, txtMonth.getText())+ "-" +	
			Utils.fill(2, txtDay.getText()) + " " +
			Utils.fill(2, txtHour.getText()) + ":" + 
			Utils.fill(2, txtMin.getText()) + ":" + 
			Utils.fill(2, txtSec.getText());

		}
		//System.out.println("test: " + retVal);

		listener.setAt(retVal);
		event = false;
		return retVal;
	}


	private String normalized(String format) {
		//now + HH:MM
		//now + HH:MM:SS
		//now + SECOUNDS
		//yyyy-MM-dd HH:mm:ss
		String retVal = format;

		try {


			txtYear.setBackground(null);
			txtMonth.setBackground(null);
			txtDay.setBackground(null);
			txtHour.setBackground(null);
			txtMin.setBackground(null);
			txtSec.setBackground(null);

			txtYear.setEnabled(false);
			txtMonth.setEnabled(false);
			txtDay.setEnabled(false);
			txtHour.setEnabled(false);
			txtMin.setEnabled(false);
			txtSec.setEnabled(false);



			//if(format.equals("now + HH:MM")) {

			if(format.indexOf("yyyy") > -1) {
				txtYear.setEnabled(true);
				txtYear.setFocus();
				if(txtYear.getText().length() == 0)
					txtYear.setText(String.valueOf(SOSDate.getCurrentDateAsString("yyyy")));
			}

			if(format.indexOf("MM") > -1) {
				txtMonth.setEnabled(true);
				if(txtMonth.getText().length() == 0)
					txtMonth.setText(String.valueOf(SOSDate.getCurrentDateAsString("MM")));
			}

			if(format.indexOf("dd") > -1) {
				txtDay.setEnabled(true);
				if(txtDay.getText().length() == 0)
					txtDay.setText(String.valueOf(SOSDate.getCurrentDateAsString("dd")));
			}

			if(format.indexOf("HH") > -1) {
				txtHour.setEnabled(true);
				if(!txtYear.isEnabled()) txtHour.setFocus();					
			}

			if(format.indexOf("mm") > -1) {
				txtMin.setEnabled(true);					
			}

			if(format.indexOf("ss") > -1 || format.indexOf("SECOUNDS") > -1) {
				if(!txtHour.isEnabled()) txtSec.setFocus();
				txtSec.setEnabled(true);

			}
			/*if(format.indexOf("HH") > -1) {
					txtHour.setEnabled(true);
					if(!txtYear.isEnabled()) txtHour.setFocus();
					if(txtHour.getText().length() == 0) {
						if(txtYear.getEnabled())
							txtHour.setText(String.valueOf(SOSDate.getCurrentDateAsString("HH")));						
						else {
							txtHour.setText("00");
						}
					}

				if(format.indexOf("mm") > -1) {
					txtMin.setEnabled(true);
					if(txtMin.getText().length() == 0)
						if(txtYear.getEnabled())
							txtMin.setText(String.valueOf(SOSDate.getCurrentDateAsString("mm")));
						else
							txtMin.setText("00");
				}

				if(format.indexOf("ss") > -1 || format.indexOf("SECOUNDS") > -1) {
					if(!txtHour.isEnabled()) txtSec.setFocus();
					txtSec.setEnabled(true);
					if(txtSec.getText().length() == 0)
						if(txtYear.getEnabled())
							txtSec.setText(String.valueOf(SOSDate.getCurrentDateAsString("ss")));
						else
							txtSec.setText("00");
				}
			 */


		} catch(Exception e){
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch (Exception ee){
				//tu nichts
			}
		}

		return retVal;

	}



} // @jve:decl-index=0:visual-constraint="10,10"
