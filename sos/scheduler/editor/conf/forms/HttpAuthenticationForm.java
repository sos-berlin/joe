package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.HttpAuthenticationListener;
import sos.util.SOSCrypt;

public class HttpAuthenticationForm extends Composite implements IUnsaved, IUpdateLanguage {
	
	private HttpAuthenticationListener listener;
	
	private Group                      httpAuthenticationGroup             = null;
	
	private Text                       txtUsername                      = null;
	
	private Text                       txtPassword                      = null;
	
	private Table                      tableHttpUsers                   = null;
	
	private Button                     butApplyHttpUser                 = null;
	
	private Button                     butRemoveHttpUser                = null;
	
	private Button                     butEncrypt                       = null; 
	
	private Text                       txtMD5Password                   = null; 
	
	
	public HttpAuthenticationForm(Composite parent, int style, SchedulerDom dom, Element config) {
		
		super(parent, style);
		listener = new HttpAuthenticationListener(dom, config);
		initialize();
		setToolTipText();		
		listener.fillHttpAuthenticationTable(tableHttpUsers);		
		
	}
	
	
	private void initialize() {
		
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(653, 468));
		txtUsername.setFocus();
		
	}
	
	
	/**
	 * This method initializes group
	 */
	private void createGroup() {
		
		GridLayout gridLayout = new GridLayout();
		httpAuthenticationGroup = new Group(this, SWT.NONE);
		httpAuthenticationGroup.setText("HTTP Authentication");
		createGroup1();
		
		httpAuthenticationGroup.setLayout(gridLayout);
		
		new Label(httpAuthenticationGroup, SWT.NONE);
		
	}
	
	
	
	/**
	 * This method initializes group1
	 */
	private void createGroup1() {
		
		final Group group_1 = new Group(httpAuthenticationGroup, SWT.NONE);
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
		gridData_2.heightHint = 427;
		gridData_2.widthHint = 525;
		group_1.setLayoutData(gridData_2);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		group_1.setLayout(gridLayout);
		
		final Label lblUsername = new Label(group_1, SWT.NONE);
		lblUsername.setText("User Name");
		
		txtUsername = new Text(group_1, SWT.BORDER);
		txtUsername.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtUsername.setFocus();
			}
		});
		txtUsername.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {     
				/*if ((txtUsername.getText().length() > 0) ) {
					txtUsername.setText(txtUsername.getText());
					txtUsername.setSelection(2);
				}*/
				if (e.keyCode == SWT.CR && !txtUsername.getText().equals(""))
					applyHttpUser();
			}
		});
		txtUsername.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				//butApplyHttpUser.setEnabled(!txtUsername.getText().equals(""));
				//butEncrypt.setEnabled(!txtUsername.getText().equals(""));
				
			}
		});
		txtUsername.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		final Label lblPassword = new Label(group_1, SWT.NONE);
		lblPassword.setText("Password");
		
		txtPassword = new Text(group_1, SWT.BORDER);
		txtPassword.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtPassword.selectAll();
			}
		});
		txtPassword.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR && !txtPassword.getText().equals("")){
					encrypt();
					
				}
				
			}
		});
		txtPassword.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				//butApplyHttpUser.setEnabled(!txtPassword.getText().equals(""));
				butEncrypt.setEnabled(!txtPassword.getText().equals(""));
			}
		});
		txtPassword.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		butEncrypt = new Button(group_1, SWT.NONE);
		butEncrypt.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(txtPassword.getText() != null && txtPassword.getText().length() > 0) {
					encrypt();
				}
				
			}
		});
		butEncrypt.setEnabled(false);
		butEncrypt.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butEncrypt.setText("Encrypt");
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		
		final Label md5PasswordLabel = new Label(group_1, SWT.NONE);
		md5PasswordLabel.setText("MD5 Password");
		
		txtMD5Password = new Text(group_1, SWT.BORDER);
		txtMD5Password.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtMD5Password.selectAll();
			}
		});
		txtMD5Password.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR && !txtUsername.getText().equals("")){					
					applyHttpUser();
				}
				
			}
		});
		txtMD5Password.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butApplyHttpUser.setEnabled(!txtMD5Password.getText().equals(""));
			}
		});
		txtMD5Password.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		butApplyHttpUser = new Button(group_1, SWT.NONE);
		butApplyHttpUser.setEnabled(false);
		butApplyHttpUser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				applyHttpUser();
			}
		});
		butApplyHttpUser.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butApplyHttpUser.setText("Apply");
		
		tableHttpUsers = new Table(group_1, SWT.FULL_SELECTION | SWT.BORDER);
		tableHttpUsers.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tableHttpUsers.getSelectionCount() > 0) {
					TableItem item = tableHttpUsers.getItem(tableHttpUsers.getSelectionIndex());
					txtUsername.setText(item.getText(0));
					txtPassword.setText("");
					txtMD5Password.setText(item.getText(1));
					butApplyHttpUser.setEnabled(false);
					butEncrypt.setEnabled(false);
					txtUsername.setFocus();
				}
				butRemoveHttpUser.setEnabled(tableHttpUsers.getSelectionCount() > 0);
			}
		});
		tableHttpUsers.setLinesVisible(true);
		tableHttpUsers.setHeaderVisible(true);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
		gridData_1.minimumHeight = 100;
		gridData_1.horizontalIndent = 4;
		tableHttpUsers.setLayoutData(gridData_1);
		
		final TableColumn urlPathTableColumn = new TableColumn(tableHttpUsers, SWT.NONE);
		urlPathTableColumn.setWidth(150);
		urlPathTableColumn.setText("Name");
		
		final TableColumn pathTableColumn = new TableColumn(tableHttpUsers, SWT.NONE);
		pathTableColumn.setWidth(250);
		pathTableColumn.setText("Password");
		
		butRemoveHttpUser = new Button(group_1, SWT.NONE);
		butRemoveHttpUser.setEnabled(false);
		butRemoveHttpUser.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butRemoveHttpUser.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tableHttpUsers.getSelectionCount() > 0) {
					tableHttpUsers.remove(tableHttpUsers.getSelectionIndex());
					tableHttpUsers.deselectAll();
					txtUsername.setText("");
					txtPassword.setText("");
					txtMD5Password.setText("");
					butApplyHttpUser.setEnabled(false);
					butEncrypt.setEnabled(false);
					listener.applyHttpUser(tableHttpUsers.getItems());
					
				}
				butRemoveHttpUser.setEnabled(false);
				;
			}
		});
		butRemoveHttpUser.setText("Remove");
	}
	
	private void applyHttpUser() {
		String passw = txtMD5Password.getText(); //txtPassword.getText();
		String name = txtUsername.getText();
		TableItem[] items = tableHttpUsers.getItems();
		boolean found = false;
		for (int i = 0; i < items.length; i++) {
			if (items[i].getText(0).equals(name)) {
				items[i].setText(1, passw);
				found = true;
			}
		}
		if (!found) {
			TableItem item = new TableItem(tableHttpUsers, SWT.NONE);
			item.setText(new String[] { name,passw });
		}
		
		tableHttpUsers.deselectAll();
		txtPassword.setText("");
		txtMD5Password.setText("");
		txtUsername.setText("");
		butRemoveHttpUser.setEnabled(false);
		butApplyHttpUser.setEnabled(false);
		butEncrypt.setEnabled(false);
		txtPassword.setFocus();
		listener.applyHttpUser(tableHttpUsers.getItems());
		
	}
	
	
	
	public void setToolTipText() {
		
		txtUsername.setToolTipText(Messages.getTooltip("http_authentication.name"));
		txtMD5Password.setToolTipText(Messages.getTooltip("http_authentication.md5_password"));
		txtPassword.setToolTipText(Messages.getTooltip("http_authentication.password"));
		tableHttpUsers.setToolTipText(Messages.getTooltip("http_authentication.http_authentication_table"));
		butApplyHttpUser.setToolTipText(Messages.getTooltip("http_authentication.apply_button"));
		butEncrypt.setToolTipText(Messages.getTooltip("http_authentication.encryt_button"));
		butRemoveHttpUser.setToolTipText(Messages.getTooltip("http_authentication.remove_button"));
				
	}
	
	public boolean isUnsaved() {	
		return false;
	}
	
	
	public void apply() {
		applyHttpUser();
		
	}
	
	private void encrypt() {
		try {
			String _encrypt = SOSCrypt.MD5encrypt(txtPassword.getText());
			txtMD5Password.setText(_encrypt.toUpperCase());
		} catch (Exception ex) {
			try {
    			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() +  txtPassword.getText() + " could not encrypt.", ex);
    		} catch(Exception ee) {
    			//tu nichts
    		}
			MainWindow.message(getShell(), txtPassword.getText() + " could not encrypt." , SWT.ICON_WARNING | SWT.OK );
		}
	}
} // @jve:decl-index=0:visual-constraint="10,10"
