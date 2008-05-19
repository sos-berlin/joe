package sos.scheduler.editor.app;


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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.ResourceManager;
import sos.util.SOSString;
import java.util.*;


public class FTPDialogProfiles {


	private              Group            schedulerGroup                = null;

	private              SOSString           sosString                     = null;

	private              Shell               schedulerConfigurationShell   = null;

	private              Properties          currProfile                   = null;

	private              Combo               cboConnectname                = null;

	private              Text                txtPort                       = null;

	private              Text                txtUsername                   = null;

	private              Text                txtPassword                   = null;

	private              Text                txtRoot                       = null;

	private              Text                txtLocalDirectory             = null;

	private              Button              butAscii                      = null;

	private              Button              butbinary                     = null ;

	private              Text                txtHost                       = null;

	private              boolean             newProfile                    = false;

	private              Button              butSavePassword               = null; 

	private              FTPDialogListener   listener                      = null;

	private              Button              useProxyButton                = null; 

	private              Text                txtProxyServer                = null;  

	private              Text                txtProxyPort                  = null; 

	private              Button              butApply                      = null;
	
	private              boolean             saveSettings                  = false;

    private              Combo               cboProtokol                   = null; 
    
    private              TabItem             sshTabItem                    = null; 
	
    private              Group               groupAuthenticationMethods    = null;
    
    private              Button              butPublicKey                  = null;

    private              Button              butAuthPassword               = null;
    
    private              Button              butPasswordAndPublic          = null;
    
    private              Text                txtDirPublicKey               = null;
    
    
	public FTPDialogProfiles(FTPDialogListener listener_) {
		listener = listener_;
		sosString = new SOSString();

	}

	public void showForm() {

		schedulerConfigurationShell = new Shell(SWT.CLOSE | SWT.TITLE
				| SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
		schedulerConfigurationShell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(final DisposeEvent e) {
				close();

			}
		});
		schedulerConfigurationShell.setImage(ResourceManager
				.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 5;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginBottom = 5;
		schedulerConfigurationShell.setLayout(gridLayout);
		schedulerConfigurationShell.setSize(558, 488);


		schedulerConfigurationShell.setText("Profiles");

		{
			schedulerGroup = new Group(schedulerConfigurationShell, SWT.NONE);
			schedulerGroup.setText("Profiles");
			final GridData gridData = new GridData(GridData.FILL,
					GridData.FILL, true, true);
			gridData.widthHint = 581;
			gridData.heightHint = 233;
			schedulerGroup.setLayoutData(gridData);

			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 2;
			gridLayout_1.marginTop = 5;
			gridLayout_1.marginRight = 5;
			gridLayout_1.marginLeft = 5;
			gridLayout_1.marginBottom = 5;
			schedulerGroup.setLayout(gridLayout_1);

			final TabFolder tabFolder = new TabFolder(schedulerGroup, SWT.NONE);
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 4);
			gridData_1.heightHint = 178;
			tabFolder.setLayoutData(gridData_1);

			final TabItem propertiesTabItem = new TabItem(tabFolder, SWT.NONE);
			propertiesTabItem.setText("Properties");

			final Group group = new Group(tabFolder, SWT.NONE);
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.numColumns = 3;
			group.setLayout(gridLayout_2);
			propertiesTabItem.setControl(group);


			final Label lblName = new Label(group, SWT.NONE);
			lblName.setText("Name");

			cboConnectname = new Combo(group, SWT.NONE);
			
			cboConnectname.setItems(listener.getProfileNames());
			cboConnectname.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();

				}
			});
			final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1);
			cboConnectname.setLayoutData(gridData_2);
			cboConnectname.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					initForm();
				}
			});
			cboConnectname.select(0);

			final Label protocolLabel = new Label(group, SWT.NONE);
			protocolLabel.setText("protocol");

			cboProtokol = new Combo(group, SWT.NONE);
			cboProtokol.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			cboProtokol.setItems(new String[] {"FTP", "SFTP"});
			
			cboProtokol.select(0);

			final Label hostnameOrIpLabel = new Label(group, SWT.NONE);
			hostnameOrIpLabel.setText("Host Name or IP Address");

			txtHost = new Text(group, SWT.BORDER);
			txtHost.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();
				}
			});
			txtHost.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

			final Label portLabel = new Label(group, SWT.NONE);
			portLabel.setText("Port");

			txtPort = new Text(group, SWT.BORDER);
			txtPort.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();
				}
			});
			txtPort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

			final Label userNameLabel = new Label(group, SWT.NONE);
			userNameLabel.setText("User Name");

			txtUsername = new Text(group, SWT.BORDER);
			txtUsername.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();
				}
			});
			txtUsername.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
			//txtUsername.setText(currProfile.get("user") != null ? currProfile.get("user").toString() : "");

			final Label passwordLabel = new Label(group, SWT.NONE);
			passwordLabel.setText("Password");			

			txtPassword = new Text(group, SWT.PASSWORD | SWT.BORDER);
			txtPassword.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();
				}
			});
			txtPassword.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
			//txtPassword.setText(currProfile.get("password") != null ? currProfile.get("password").toString() : "");

			final Label rootLabel = new Label(group, SWT.NONE);
			rootLabel.setText("Root Directory");

			txtRoot = new Text(group, SWT.BORDER);
			txtRoot.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();
				}
			});
			txtRoot.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
			//txtRoot.setText(currProfile.get("root") != null ? currProfile.get("root").toString() : "");

			final Label directoryFroLocalLabel = new Label(group, SWT.NONE);
			directoryFroLocalLabel.setText("Directory For Local Copy");

			txtLocalDirectory = new Text(group, SWT.BORDER);
			txtLocalDirectory.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();
				}
			});
			txtLocalDirectory.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

			final Label savePasswordLabel = new Label(group, SWT.NONE);
			savePasswordLabel.setLayoutData(new GridData(SWT.DEFAULT, 24));
			savePasswordLabel.setText("Save Password");

			butSavePassword = new Button(group, SWT.CHECK);
			butSavePassword.addSelectionListener(new SelectionAdapter() {
				public void widgetDefaultSelected(final SelectionEvent e) {
					
				}
				public void widgetSelected(final SelectionEvent e) {
					setEnabled();
				}
			});
			butSavePassword.setLayoutData(new GridData());
			new Label(group, SWT.NONE);
			//txtLocalDirectory.setText(currProfile.get("localdirectory") != null ? currProfile.get("localdirectory").toString() : "");

			final Label transferModeLabel = new Label(group, SWT.NONE);
			final GridData gridData_3 = new GridData(GridData.BEGINNING, GridData.END, false, false);
			gridData_3.heightHint = 21;
			transferModeLabel.setLayoutData(gridData_3);
			transferModeLabel.setText("Transfer Mode");

			butAscii = new Button(group, SWT.RADIO);
			butAscii.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					setEnabled();
				}
			});
			butAscii.setLayoutData(new GridData());
			butAscii.setText("ASCII");


			butbinary = new Button(group, SWT.RADIO);
			butbinary.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					setEnabled();
				}
			});
			butbinary.setLayoutData(new GridData());
			butbinary.setText("Binary");

			final TabItem proxyTabItem = new TabItem(tabFolder, SWT.NONE);
			proxyTabItem.setText("Proxy");

			final Group group_1 = new Group(tabFolder, SWT.NONE);
			final GridLayout gridLayout_3 = new GridLayout();
			gridLayout_3.numColumns = 2;
			group_1.setLayout(gridLayout_3);
			proxyTabItem.setControl(group_1);

			useProxyButton = new Button(group_1, SWT.CHECK);
			useProxyButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					txtProxyServer.setEnabled(useProxyButton.getSelection());
					txtProxyPort.setEnabled(useProxyButton.getSelection());
					setEnabled();
				}
			});
			useProxyButton.setLayoutData(new GridData(SWT.DEFAULT, 52));
			useProxyButton.setText("Use Proxy");
			new Label(group_1, SWT.NONE);

			final Label proxyServerLabel = new Label(group_1, SWT.NONE);
			proxyServerLabel.setText("Proxy Server");

			txtProxyServer = new Text(group_1, SWT.BORDER);
			txtProxyServer.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();
				}
			});
			txtProxyServer.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

			final Label proxyPortLabel = new Label(group_1, SWT.NONE);
			proxyPortLabel.setText("Proxy Port");

			txtProxyPort = new Text(group_1, SWT.BORDER);
			txtProxyPort.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();
				}
			});
			txtProxyPort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

			sshTabItem = new TabItem(tabFolder, SWT.NONE);
			sshTabItem.setText("SSH");

			groupAuthenticationMethods = new Group(tabFolder, SWT.NONE);
			groupAuthenticationMethods.setText("Authentication Methods");
			final GridLayout gridLayout_4 = new GridLayout();
			gridLayout_4.numColumns = 2;
			groupAuthenticationMethods.setLayout(gridLayout_4);
			sshTabItem.setControl(groupAuthenticationMethods);

			final Label label_1 = new Label(groupAuthenticationMethods, SWT.NONE);
			new Label(groupAuthenticationMethods, SWT.NONE);

			butPublicKey = new Button(groupAuthenticationMethods, SWT.RADIO);
			butPublicKey.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					setEnabled();
				}
			});
			final GridData gridData_4 = new GridData(GridData.BEGINNING, GridData.END, false, false, 2, 1);
			butPublicKey.setLayoutData(gridData_4);
			butPublicKey.setText("Public Key");

			butAuthPassword = new Button(groupAuthenticationMethods, SWT.RADIO);
			butAuthPassword.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					setEnabled();
				}
			});
			butAuthPassword.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
			butAuthPassword.setText("Password");

			butPasswordAndPublic = new Button(groupAuthenticationMethods, SWT.RADIO);
			butPasswordAndPublic.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					setEnabled();
				}
			});
			butPasswordAndPublic.setText("Public Key and Password");
			new Label(groupAuthenticationMethods, SWT.NONE);

			final Label pathToPublicLabel = new Label(groupAuthenticationMethods, SWT.NONE);
			pathToPublicLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
			pathToPublicLabel.setText("Private Key");

			txtDirPublicKey = new Text(groupAuthenticationMethods, SWT.BORDER);
			txtDirPublicKey.addModifyListener(new ModifyListener() {
				public void modifyText(final ModifyEvent e) {
					setEnabled();
				}
			});
			txtDirPublicKey.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

			final Button button_4 = new Button(groupAuthenticationMethods, SWT.NONE);
			button_4.setVisible(false);
			button_4.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
				}
			});
			button_4.setText("...");
			new Label(schedulerGroup, SWT.NONE);

			butApply = new Button(schedulerGroup, SWT.NONE);
			butApply.setEnabled(false);
			butApply.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					apply();

				}
			});
			butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butApply.setText("Apply");
			//txtHost.setText(currProfile.get("host") != null ? currProfile.get("host").toString() : "");

			final Button butNewProfile = new Button(schedulerGroup, SWT.NONE);
			butNewProfile.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					newProfile = true;
					cboConnectname.setText("");
					txtPort.setText("");
					txtUsername.setText("");
					txtPassword.setText("");
					txtRoot.setText("");
					txtLocalDirectory.setText("");
					butAscii.setSelection(true);
					butbinary.setSelection(false);
					butSavePassword.setSelection(false);
					txtHost.setText("");
					txtProxyPort.setText("");
					txtProxyServer.setText("");
					txtProxyPort.setEnabled(false);
					txtProxyServer.setEnabled(false);
					useProxyButton.setSelection(false);
					cboProtokol.select(0);
				}
			});
			butNewProfile.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butNewProfile.setText("New Profile");
			//txtPort.setText(currProfile.get("port") != null ? currProfile.get("port").toString() : "");

			final Button butRemove = new Button(schedulerGroup, SWT.NONE);
			butRemove.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					listener.removeProfile(cboConnectname.getText());					
					if(cboConnectname.getItemCount() > 0)
						cboConnectname.select(0);
					initForm();
					saveSettings = true;
				}
			});
			butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
			butRemove.setText("Remove");


			/*if(currProfile.get("transfermode") != null && currProfile.get("transfermode").toString().equalsIgnoreCase("binary")) {
				butbinary.setSelection(true);
				butAscii.setSelection(false);
			} else {
				butbinary.setSelection(false);
				butAscii.setSelection(true);
			}*/

			// final Tree tree = new Tree(schedulerGroup, SWT.BORDER);



		}

		final Button butClose = new Button(schedulerConfigurationShell, SWT.NONE);
		butClose.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				close();		
				schedulerConfigurationShell.dispose();
			}
		});
		butClose.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		butClose.setText("Close");
		cboProtokol.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				
				if(cboProtokol.getText().equalsIgnoreCase("FTP"))
					groupAuthenticationMethods.setEnabled(false);
				else
					groupAuthenticationMethods.setEnabled(true);
				
				setEnabled();
			}
		});

		//setToolTipText();
		initForm();
		schedulerConfigurationShell.layout();
		schedulerConfigurationShell.open();
	}

	private String[] convert(Object[] obj) {
		String[] str = null;
		try {
			str = new String[obj.length];
			for(int i = 0; i < obj.length; i++) {
				if(sosString.parseToString(obj[i]).startsWith("profile ")) {
					str[i] = sosString.parseToString(obj[i]).substring("profile ".length());
				}
			}

		} catch (Exception e) {
			System.out.println("..error in FTPDilagProfiles: " + e.getMessage());
		}
		return str;
	}

	private void initForm() {
		try {
			 setToolTip();
			String s = cboConnectname.getText();
			cboConnectname.setItems(listener.getProfileNames());//löscht den Eintrag, daher mit s merken und wieder zurückschreiben
			cboConnectname.setText(s);
			
			currProfile = listener.getProfiles().get(cboConnectname.getText()) != null ? (Properties)listener.getProfiles().get(cboConnectname.getText()) : new Properties();

			listener.setCurrProfile(currProfile);
			listener.setCurrProfileName(cboConnectname.getText());

			txtHost.setText(currProfile.get("host") != null ? currProfile.get("host").toString() : "");
			txtPort.setText(currProfile.get("port") != null ? currProfile.get("port").toString() : "");
			txtUsername.setText(currProfile.get("user") != null ? currProfile.get("user").toString() : "");
			txtPassword.setText(currProfile.get("password") != null ? currProfile.get("password").toString() : "");
			txtRoot.setText(currProfile.get("root") != null ? currProfile.get("root").toString() : "");
			txtLocalDirectory.setText(currProfile.get("localdirectory") != null ? currProfile.get("localdirectory").toString() : "");

			butSavePassword.setSelection(sosString.parseToBoolean(currProfile.get("save_password")));

			useProxyButton.setSelection(sosString.parseToBoolean(currProfile.get("use_proxy")));
			if(useProxyButton.getSelection()) {
				txtProxyServer.setEnabled(true);
				txtProxyPort.setEnabled(true);
				txtProxyServer.setText(sosString.parseToString(currProfile.get("proxy_server")));
				txtProxyPort.setText(sosString.parseToString(currProfile.get("proxy_port")));
			} else {
				txtProxyServer.setEnabled(false);
				txtProxyPort.setEnabled(false);
				txtProxyServer.setText("");
				txtProxyPort.setText("");						
			}

			
			if(currProfile.get("transfermode") != null && currProfile.get("transfermode").toString().equalsIgnoreCase("binary")) {						
				butbinary.setSelection(true);
				butAscii.setSelection(false);
			} else {
				butbinary.setSelection(false);
				butAscii.setSelection(true);
			}
			String protocol = sosString.parseToString(currProfile.get("protocol"));
			if(protocol.length() == 0)
				protocol = "FTP";
			
			
			cboProtokol.setText(protocol);
			
			
			if(protocol.equalsIgnoreCase("SFTP")) {
				groupAuthenticationMethods.setEnabled(true);
				txtDirPublicKey.setText(sosString.parseToString(currProfile.get("auth_file")));
				String method = sosString.parseToString(currProfile.get("auth_method"));
				if(method.equalsIgnoreCase("publickey")) {
					butPublicKey.setSelection(true);	
				} else if(method.equalsIgnoreCase("password")) {
					butAuthPassword.setSelection(true);
				} else if(method.equalsIgnoreCase("both")) {
					butPasswordAndPublic.setSelection(true);
				}
					
				
			} else {
				groupAuthenticationMethods.setEnabled(false);
				txtDirPublicKey.setText("");
				butPublicKey.setSelection(false);
				butAuthPassword.setSelection(false);
				butPasswordAndPublic.setSelection(false);
			}
			
			
			butApply.setEnabled(false);
			
			
			
			newProfile = false;
		} catch (Exception e) {
			MainWindow.message("could not reaad FTP Profiles:" + e.getMessage()  , SWT.ICON_WARNING);
		}
	}


	private void setEnabled() {
		if(butApply != null)
			butApply.setEnabled(cboConnectname.getText().length() > 0);
				
	}

	private void apply() {
		Properties prop = new Properties();
		String pName = cboConnectname.getText();
		prop.put("name", pName);
		prop.put("host", txtHost.getText());
		prop.put("port", txtPort.getText());
		prop.put("user", txtUsername.getText());
		prop.put("password", txtPassword.getText());
		prop.put("root", txtRoot.getText());
		prop.put("localdirectory", txtLocalDirectory.getText());
		prop.put("transfermode", butbinary.getSelection() ? "binary" : "ASCII");
		prop.put("save_password", (butSavePassword.getSelection() ? "yes" : "no"));
		prop.put("protocol", cboProtokol.getText());

		if(useProxyButton.getSelection()) {
			prop.put("use_proxy", "yes");
			prop.put("proxy_server", txtProxyServer.getText());
			prop.put("proxy_port", txtProxyPort.getText());
		}	    				
		
		if(cboProtokol.getText().equalsIgnoreCase("SFTP")){
			String method = getAuthMethod();			
			prop.put("auth_method", method);
			prop.put("auth_file", txtDirPublicKey.getText());
		}

		if(newProfile && !listener.getProfiles().containsKey(cboConnectname.getText())) {
			//neuer Eintrag
			listener.getProfiles().put(pName, prop);	

		} else {
			listener.removeProfile(pName);

		}

		listener.setProfiles(pName, prop);

		listener.setCurrProfileName(pName);

		Properties p = new Properties();
		p.putAll(prop);
		listener.setCurrProfile(p);

		cboConnectname.setItems(listener.getProfileNames());
		cboConnectname.setText(pName);
		//initForm();
		newProfile = false;
		saveSettings = true;//Änderungen haben stattgefunden, d.h. in die ini Datei zurückschreiben
		butApply.setEnabled(false);
		
	}

	private String getAuthMethod() {
		String authMethod = "";
		if(butPublicKey.getSelection())
			authMethod = "publickey";
		else if(butAuthPassword.getSelection())
			authMethod = "password";
		else if(butPasswordAndPublic.getSelection())
			authMethod = "both";
		
		return authMethod;
	}
	
	private void close() {
		if (butApply.getEnabled()) {
			int cont = MainWindow.message(schedulerConfigurationShell, sos.scheduler.editor.app.Messages.getString("MainListener.apply_changes"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
			if(cont == SWT.OK) {				
				apply();
			} 
		}
		if(saveSettings) {
			//listener.saveSettings();
			listener.saveProfile(butSavePassword.getSelection());
		}
	}
	
	
	private void setToolTip() {
		cboConnectname.setToolTipText(Messages.getTooltip("ftp_profile_dialog.profilename"));
		cboProtokol.setToolTipText(Messages.getTooltip("ftp_profile_dialog.protocol"));
		txtHost.setToolTipText(Messages.getTooltip("ftp_profile_dialog.host"));
		txtPort.setToolTipText(Messages.getTooltip("ftp_profile_dialog.port"));
		txtUsername.setToolTipText(Messages.getTooltip("ftp_profile_dialog.user_name"));
		txtPassword.setToolTipText(Messages.getTooltip("ftp_profile_dialog.password"));
		txtRoot.setToolTipText(Messages.getTooltip("ftp_profile_dialog.root"));
		txtLocalDirectory.setToolTipText(Messages.getTooltip("ftp_profile_dialog.local_directory"));
		butSavePassword.setToolTipText(Messages.getTooltip("ftp_profile_dialog.save_password"));		
		butAscii.setToolTipText(Messages.getTooltip("ftp_profile_dialog.ascii"));
		butbinary.setToolTipText(Messages.getTooltip("ftp_profile_dialog.binary"));
		
		useProxyButton.setToolTipText(Messages.getTooltip("ftp_profile_dialog.use_proxy")); 
		txtProxyServer.setToolTipText(Messages.getTooltip("ftp_profile_dialog.proxy_server")); 
		txtProxyPort.setToolTipText(Messages.getTooltip("ftp_profile_dialog.proxy_port"));
		
		butPublicKey.setToolTipText(Messages.getTooltip("ftp_profile_dialog.public_key"));
	    butAuthPassword.setToolTipText(Messages.getTooltip("ftp_profile_dialog.auth_password"));	    
	    butPasswordAndPublic.setToolTipText(Messages.getTooltip("ftp_profile_dialog.password_and_public"));	    
	    txtDirPublicKey.setToolTipText(Messages.getTooltip("ftp_profile_dialog.directory_of_public_key"));

	}

}
