package sos.scheduler.editor.conf.forms;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.listeners.WebserviceListener;

import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class WebserviceForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
	private WebserviceListener	listener	= null;
	private Group				group		= null;
	private Group				group_1;
	private Composite			gWebService	= null;
	private Button				bApply		= null;
	private Label				label		= null;
	private Text				tName		= null;
	private Label				label1		= null;
	private Text				tURL		= null;
	private Label				label2		= null;
	private CCombo				cChain		= null;
	private Label				label3		= null;
	private Text				sTimeout	= null;
	private Label				label5		= null;
	private Button				bDebug		= null;
	private Label				label7		= null;
	private Label				label13		= null;
	private Label				label19		= null;
	private Text				tRequest	= null;
	private Text				tForward	= null;
	private Text				tResponse	= null;

	public WebserviceForm(Composite parent, int style, SchedulerDom dom, Element element, ISchedulerUpdate main) {
		super(parent, style);
		listener = new WebserviceListener(dom, element, main);
		initialize();
		setToolTipText();
		cChain.setItems(listener.getJobChains());
		setInput(true);
		setEnabledComponent();
		bApply.setEnabled(false);
		new Label(group_1, SWT.NONE);
	}

	public void apply() {
		if (isUnsaved())
			applyService();
	}

	public boolean isUnsaved() {
		return bApply.isEnabled();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(653, 468));
		tName.setFocus();
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		group_1 = JOE_G_WeberviceForm_WebServices.Control(new Group(this, SWT.NONE));
		group_1.setLayout(gridLayout);
		createGroup1();
		bApply = JOE_B_WebserviceForm_Apply.Control(new Button(group_1, SWT.NONE));
		bApply.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bApply.setEnabled(false);
		bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				applyService();
			}
		});
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		createTable();
		new Label(group_1, SWT.NONE);
	}

	/**
	 * This method initializes table
	 */
	private void createTable() {
	}

	/**
	 * This method initializes group1
	 */
	private void createGroup1() {
		GridData gridData12 = new org.eclipse.swt.layout.GridData();
		gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData12.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData11 = new org.eclipse.swt.layout.GridData();
		gridData11.widthHint = 50;
		GridData gridData10 = new org.eclipse.swt.layout.GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData10.grabExcessHorizontalSpace = true;
		gridData10.horizontalSpan = 3;
		gridData10.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData9 = new org.eclipse.swt.layout.GridData();
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData8 = new org.eclipse.swt.layout.GridData();
		gridData8.horizontalSpan = 5;
		gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.horizontalSpan = 5;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 6;
		gWebService = new Composite(group_1, SWT.NONE);
		gWebService.setLayout(gridLayout1);
		gWebService.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false, 1, 3));
		label = JOE_L_Name.Control(new Label(gWebService, SWT.NONE));
		tName = JOE_T_WebserviceForm_Name.Control(new Text(gWebService, SWT.BORDER));
		tName.setLayoutData(gridData9);
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				getShell().setDefaultButton(null);
				boolean valid = listener.isValid(tName.getText());
				if (valid)
					tName.setBackground(null);
				else
					tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				valid = (valid && !tName.getText().equals("") && !tURL.getText().equals(""));
				if (valid) {
					getShell().setDefaultButton(bApply);
				}
				bApply.setEnabled(valid);
				// gWebService.setText(GROUP_WEB_SERVICE + ": " + tName.getText());
			}
		});
		label1 = JOE_L_WebserviceForm_URL.Control(new Label(gWebService, SWT.NONE));
		tURL = JOE_T_WebserviceForm_URL.Control(new Text(gWebService, SWT.BORDER));
		tURL.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			public void widgetSelected(final SelectionEvent e) {
			}
		});
		tURL.setLayoutData(gridData10);
		tURL.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if ((tURL.getText().length() > 0) && (tURL.getText().charAt(0) != '/')) {
					tURL.setText("/" + tURL.getText());
					tURL.setSelection(2);
				}
				boolean valid = (!tName.getText().equals("") && !tURL.getText().equals(""));
				if (valid) {
					getShell().setDefaultButton(bApply);
				}
				bApply.setEnabled(valid);
			}
		});
		label2 = JOE_L_WebserviceForm_JobChain.Control(new Label(gWebService, SWT.NONE));
		cChain = JOE_Cbo_WebserviceForm_JobChain.Control(new CCombo(gWebService, SWT.BORDER));
		cChain.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		cChain.setEditable(true);
		cChain.setLayoutData(gridData12);
		cChain.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				setEnabledComponent();
				/*boolean valid = (!tName.getText().equals(""));
				bApply.setEnabled(valid);
				if (valid) {
				 getShell().setDefaultButton(bApply);
				}
				sTimeout.setEnabled(!cChain.getText().equals(""));
				tRequest.setEnabled(!sTimeout.getEnabled());
				tResponse.setEnabled(!sTimeout.getEnabled());
				tForward.setEnabled(!sTimeout.getEnabled());
				*/
			}
		});
		label3 = JOE_L_WebserviceForm_Timeout.Control(new Label(gWebService, SWT.NONE));
		sTimeout = JOE_T_WebserviceForm_Timeout.Control(new Text(gWebService, SWT.BORDER));
		sTimeout.addVerifyListener(new VerifyListener() {
			public void verifyText(final VerifyEvent e) {
				e.doit = Utils.isOnlyDigits(e.text);
			}
		});
		sTimeout.setLayoutData(gridData11);
		sTimeout.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				boolean valid = (!tName.getText().equals(""));
				if (valid) {
					getShell().setDefaultButton(bApply);
				}
				bApply.setEnabled(valid);
			}
		});
		label5 = JOE_L_WebserviceForm_Debug.Control(new Label(gWebService, SWT.NONE));
		bDebug = JOE_B_WebserviceForm_Debug.Control(new Button(gWebService, SWT.CHECK));
		bDebug.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				boolean valid = (!tName.getText().equals(""));
				if (valid) {
					getShell().setDefaultButton(bApply);
				}
				bApply.setEnabled(valid);
			}
		});
		label7 = JOE_L_WebserviceForm_RequestXSLT.Control(new Label(gWebService, SWT.NONE));
		tRequest = JOE_T_WebserviceForm_RequestXSLT.Control(new Text(gWebService, SWT.BORDER));
		tRequest.setLayoutData(gridData8);
		tRequest.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				setEnabledComponent();
				/*boolean valid = (!tName.getText().equals(""));
				if (valid) {
				 getShell().setDefaultButton(bApply);
				}
				bApply.setEnabled(valid);
				cChain.setEnabled(tRequest.getText().equals(""));
				*/
			}
		});
		label19 = JOE_L_WebserviceForm_ResponseXSLT.Control(new Label(gWebService, SWT.NONE));
		tResponse = JOE_T_WebserviceForm_ResponseXSLT.Control(new Text(gWebService, SWT.BORDER));
		tResponse.setLayoutData(gridData6);
		tResponse.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				setEnabledComponent();
				/*boolean valid = (!tName.getText().equals(""));
				if (valid) {
				 getShell().setDefaultButton(bApply);
				}
				bApply.setEnabled(valid);
				cChain.setEnabled(tResponse.getText().equals(""));
				*/
			}
		});
		label13 = JOE_L_WebserviceForm_ForwardXSLT.Control(new Label(gWebService, SWT.NONE));
		tForward = JOE_T_WebserviceForm_ForwardXSLT.Control(new Text(gWebService, SWT.BORDER));
		tForward.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false, 5, 1));
		tForward.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				boolean valid = (!tName.getText().equals(""));
				if (valid) {
					getShell().setDefaultButton(bApply);
				}
				bApply.setEnabled(valid);
			}
		});
	}

	private void applyService() {
		boolean found = false;
		boolean exist = false;
		//TODO
		/*for (int i = 0; i < services.length; i++) {
		 String url = services[i].getText(1);
		 String name = services[i].getText(0);

		 if (url.equals(tURL.getText()) && sel != i) {
			 found = true;
		 }
		 if (name.equals(tName.getText()) && sel != i) {
			 exist = true;
		 }
		}
		*/
		if (found) {
			MainWindow.message(JOE_M_0041.label(), SWT.ICON_INFORMATION);
			tURL.setFocus();
		}
		else {
			if (!tRequest.getText().equals("") && tResponse.getText().equals("")) {
				MainWindow.message(JOE_M_0042.label(), SWT.ICON_INFORMATION);
				tResponse.setFocus();
			}
			else {
				if (tRequest.getText().equals("") && !tResponse.getText().equals("")) {
					MainWindow.message(JOE_M_0043.label(), SWT.ICON_INFORMATION);
					tRequest.setFocus();
				}
				else {
					if (exist) {
						MainWindow.message(JOE_M_0044.params(tName.getText()), SWT.ICON_INFORMATION);
						tName.setFocus();
					}
					else {
						if (tURL.getText().equals("")) {
							MainWindow.message(JOE_M_0045.label(), SWT.ICON_INFORMATION);
							tURL.setFocus();
						}
						else {
							if (Utils.str2int(sTimeout.getText()) == 0 && sTimeout.getText().length() > 0) {
								MainWindow.message(JOE_M_0046.label(), SWT.ICON_INFORMATION);
							}
							else {
								listener.applyService(bDebug.getSelection(), cChain.getText(), tName.getText(), tForward.getText(), tRequest.getText(),
										tResponse.getText(),
										//sTimeout.getText(), tURL.getText(), tParams.getItems());
										sTimeout.getText(), tURL.getText());
								//listener.fillTable(tServices);
								//setInput(true);
								bApply.setEnabled(false);
								//getShell().setDefaultButton(bNew);
							}
						}
					}
				}
			}
		}
	}

	private void setInput(boolean enabled) {
		if (enabled) {
			bDebug.setSelection(listener.getDebug());
			int index = listener.getChainIndex(listener.getJobChain());
			if (index != -1) {
				cChain.select(index);
			}
			tName.setText(listener.getName());
			cChain.setText(listener.getJobChain());
			tForward.setText(listener.getForwardXSLT());
			tRequest.setText(listener.getRequestXSLT());
			tResponse.setText(listener.getResponseXSLT());
			sTimeout.setText(Utils.getIntegerAsString(Utils.str2int(listener.getTimeout())));
			tURL.setText(listener.getURL());
			//gWebService.setText(GROUP_WEB_SERVICE + ": " + listener.getName());
			tName.setFocus();
		}
		else {
			tName.setText("");
			cChain.select(-1);
			tForward.setText("");
			tRequest.setText("");
			tResponse.setText("");
			sTimeout.setText("");
			tURL.setText("");
			//gWebService.setText(GROUP_WEB_SERVICE);
		}
		bDebug.setEnabled(enabled);
		cChain.setEnabled(enabled);
		tName.setEnabled(enabled);
		tForward.setEnabled(enabled);
		tRequest.setEnabled(enabled);
		tResponse.setEnabled(enabled);
		sTimeout.setEnabled(enabled);
		tURL.setEnabled(enabled);
		bApply.setEnabled(false);
		tName.setBackground(null);
	}

	public void setToolTipText() {
		//
	}

	private void setEnabledComponent() {
		boolean valid = (!tName.getText().equals(""));
		//bApply.setEnabled(valid);
		if (valid) {
			getShell().setDefaultButton(bApply);
		}
		sTimeout.setEnabled(!cChain.getText().equals(""));
		tRequest.setEnabled(!sTimeout.getEnabled());
		tResponse.setEnabled(!sTimeout.getEnabled());
		tForward.setEnabled(!sTimeout.getEnabled());
		bApply.setEnabled(valid);
		cChain.setEnabled(tRequest.getText().equals(""));
		/*if(JOEConstants.JOB_CHAIN == type) {
		 
		 boolean valid = (!tName.getText().equals(""));
		 bApply.setEnabled(valid);
		 if (valid) {
			 getShell().setDefaultButton(bApply);
		 }
		 sTimeout.setEnabled(!cChain.getText().equals(""));
		 tRequest.setEnabled(!sTimeout.getEnabled());
		 tResponse.setEnabled(!sTimeout.getEnabled());
		 tForward.setEnabled(!sTimeout.getEnabled());
		 
		} else {
		 
		 boolean valid = (!tName.getText().equals(""));
		 if (valid) {
			 getShell().setDefaultButton(bApply);
		 }
		 bApply.setEnabled(valid);
		 cChain.setEnabled(tRequest.getText().equals(""));
		 
		}
		*/
	}
} // @jve:decl-index=0:visual-constraint="10,10"
