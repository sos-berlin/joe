package sos.ftp.profiles;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import sos.util.SOSLogger;
import sos.util.SOSString;

import java.util.*;
import java.io.File;

/**
 * Formular zur Erstellen, Beabeiten oder Löschen von FTP Zugängen.
 * 
 * 
 * @author Mueruevet Oeksuez
 *
 */
public class FTPProfileDialog {

 
	private              Group               schedulerGroup                = null;
	private              SOSString           sosString                     = null;
	private   static     Shell               shell   = null;
	private              FTPProfile          currProfile                   = null;
	private              Combo               cboConnectname                = null;
	private              Text                txtPort                       = null;
	private              Text                txtUsername                   = null;
	private              Text                txtPassword                   = null;
	private              Text                txtRoot                       = null;
	private              Text                txtLocalDirectory             = null;
	private              Button              butAscii                      = null;
	private              Button              butbinary                     = null;
	private              Text                txtHost                       = null;
	private              boolean             newProfile                    = false;
	private              Button              butSavePassword               = null; 
	private              FTPDialogListener   listener                      = null;
	private              Button              useProxyButton                = null; 
    private              Text                txtProxyServer                = null;  
    private              Text                txtProxyUser                  = null;  
    private              Combo               cboProxyProtocol              = null; 
    private              Text                txtProxyPassword              = null;  
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
	private              boolean             saved                         = false; //hilsvariable            
	private              boolean             init                          = false;   
	private              Combo               combo                         = null;
	private   static     boolean             emptyItem                     = false;
	
	public FTPProfileDialog(File configFile) throws Exception{
		try {
			FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
			sosString = new SOSString();
			listener = new FTPDialogListener(configFile);
			sosString = new SOSString();
		} catch (Exception e) {
			throw new Exception ("error in " +  sos.util.SOSClassUtil.getMethodName() + " cause: " + e.toString());
		}
	}        

	public void fillCombo(Combo combo_) throws Exception{
		try {
			FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);

			combo = combo_;
			listener.setConnectionsname(combo);
			String[] profileNames = listener.getProfileNames();
			HashMap <String, FTPProfile> p = listener.getProfiles();
			for(int i = 0; i < profileNames.length; i++) {
				combo.setData(profileNames[i], p.get(profileNames[i]));
			}
			combo.setItems(profileNames);
			combo.setText(listener.getCurrProfileName() != null ? listener.getCurrProfileName() : "");
			if(profileNames.length > 0)
				combo.select(0);
			combo.setData(listener);
			sosString = new SOSString();

			if(emptyItem) {
				combo_.add("", 0);
				combo_.select(0);
			}
		} catch (Exception e) {
			throw new Exception ("error in " +  sos.util.SOSClassUtil.getMethodName() + " cause: " + e.toString());
		}
	}

	public void fillCombo(Combo combo_, boolean emptyItem_) throws Exception{
		FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
		try {
			emptyItem = emptyItem_;			
			fillCombo(combo_);			
		} catch (Exception e) {
			throw new Exception ("error in " +  sos.util.SOSClassUtil.getMethodName() + " cause: " + e.toString());
		}
	}
	
	public void showModal(Combo combo_) {

		try {
			FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
			combo = combo_;
			fillCombo(combo);
			shell = new Shell(SWT.CLOSE | SWT.TITLE
					| SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);

			shell.addTraverseListener(new TraverseListener() {
				public void keyTraversed(final TraverseEvent e) {				
					if(e.detail == SWT.TRAVERSE_ESCAPE) {
						close();
						saved = true;
						shell.dispose();
					}
				}
			});

			shell.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(final DisposeEvent e) {
					close();

				}
			});
			//shell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
			final GridLayout gridLayout = new GridLayout();
			gridLayout.marginTop = 5;
			gridLayout.marginRight = 5;
			gridLayout.marginLeft = 5;
			gridLayout.marginBottom = 5;
			shell.setLayout(gridLayout);
			shell.setSize(558, 488);


			shell.setText("Profiles");

			{
				schedulerGroup = new Group(shell, SWT.NONE);
				 
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
						//if( !cboConnectname.getText().equals(currProfile.get("name")))
						if( !cboConnectname.getText().equals(currProfile.getProfilename()))
							initForm();
					}
				});

				//if(!shell.isDisposed())
				//cboConnectname.setText(listener.getCurrProfileName());


				final Label protocolLabel = new Label(group, SWT.NONE);
				protocolLabel.setText("Protocol");

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
						if(init) {
							try {
								init = false;
								//TODO 
								if(txtPassword.getText().length() > 0) {
								}
							} catch(Exception ex) {
								System.out.println(ex.getMessage());
							}
						}
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
				final GridData gridData_5 = new GridData(SWT.DEFAULT, 24);
				gridData_5.verticalIndent = 5;
				savePasswordLabel.setLayoutData(gridData_5);
				savePasswordLabel.setText("Save Password");

				butSavePassword = new Button(group, SWT.CHECK);
				butSavePassword.setSelection(true);
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

				final Group groupProxy = new Group(tabFolder, SWT.NONE);
				final GridLayout gridLayout_3 = new GridLayout();
				gridLayout_3.numColumns = 2;
				groupProxy.setLayout(gridLayout_3);
				proxyTabItem.setControl(groupProxy);

				useProxyButton = new Button(groupProxy, SWT.CHECK);
				useProxyButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						txtProxyServer.setEnabled(useProxyButton.getSelection());
						txtProxyPort.setEnabled(useProxyButton.getSelection());
                        txtProxyUser.setEnabled(useProxyButton.getSelection());
                        txtProxyPassword.setEnabled(useProxyButton.getSelection());
                        cboProxyProtocol.setEnabled(useProxyButton.getSelection());
						setEnabled();
					}
				});
				useProxyButton.setLayoutData(new GridData(SWT.DEFAULT, 52));
				useProxyButton.setText("Use Proxy");
				new Label(groupProxy, SWT.NONE);

				final Label proxyServerLabel = new Label(groupProxy, SWT.NONE);
				proxyServerLabel.setText("Proxy Server");

				txtProxyServer = new Text(groupProxy, SWT.BORDER);
				txtProxyServer.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {
						setEnabled();
					}
				});
				txtProxyServer.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

				final Label proxyPortLabel = new Label(groupProxy, SWT.NONE);
				proxyPortLabel.setText("Proxy Port");

				txtProxyPort = new Text(groupProxy, SWT.BORDER);
				txtProxyPort.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent e) {
						setEnabled();
					}
				});
				txtProxyPort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

				
                final Label proxyUserLabel = new Label(groupProxy, SWT.NONE);
                proxyUserLabel.setText("Proxy User");

                txtProxyUser = new Text(groupProxy, SWT.BORDER);
                txtProxyUser.addModifyListener(new ModifyListener() {
                    public void modifyText(final ModifyEvent e) {
                        setEnabled();
                    }
                });
                txtProxyUser.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

                final Label proxyPasswordLabel = new Label(groupProxy, SWT.NONE);
                proxyPasswordLabel.setText("Proxy Password");

                txtProxyPassword = new Text(groupProxy, SWT.BORDER);
                txtProxyPassword.addModifyListener(new ModifyListener() {
                    public void modifyText(final ModifyEvent e) {
                        setEnabled();
                    }
                });
                txtProxyPassword.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

                final Label proxyProtocolLabel = new Label(groupProxy, SWT.NONE);
                proxyProtocolLabel.setText("Proxy Protocol");

                cboProxyProtocol = new Combo(groupProxy, SWT.NONE);
                cboProxyProtocol.setItems(new String[] {"http", "socks4", "socks5"});
 
                cboProxyProtocol.addModifyListener(new ModifyListener() {
                    public void modifyText(final ModifyEvent e) {
                        setEnabled();
                    }
                });                
                 
				sshTabItem = new TabItem(tabFolder, SWT.NONE);
				sshTabItem.setText("SSH");

				groupAuthenticationMethods = new Group(tabFolder, SWT.NONE);
				groupAuthenticationMethods.setText("Authentication Methods");
				final GridLayout gridLayout_4 = new GridLayout();
				gridLayout_4.numColumns = 2;
				groupAuthenticationMethods.setLayout(gridLayout_4);
				sshTabItem.setControl(groupAuthenticationMethods);

				new Label(groupAuthenticationMethods, SWT.NONE);
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
						try {
							apply();
						} catch (Exception ex) {
							message("could not apply, cause: " + ex.toString(), SWT.ICON_WARNING);
						}

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
						butSavePassword.setSelection(true);
						txtHost.setText("");
						
						//Proxy
						txtProxyPort.setText("");
						txtProxyServer.setText("");
						txtProxyPort.setEnabled(false);
                        txtProxyServer.setEnabled(false);
                        txtProxyUser.setEnabled(false);
                        txtProxyPassword.setEnabled(false);
                        cboProxyProtocol.setEnabled(false);
						useProxyButton.setSelection(false);
						cboProtokol.select(0);
						cboConnectname.setFocus();
						//auth
						txtDirPublicKey.setText("");
						butPublicKey.setSelection(false);
						butPasswordAndPublic.setSelection(false);
						butAuthPassword.setSelection(false);
					}
				});
				butNewProfile.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
				butNewProfile.setText("New Profile");
				//txtPort.setText(currProfile.get("port") != null ? currProfile.get("port").toString() : "");

				final Button butRemove = new Button(schedulerGroup, SWT.NONE);
				butRemove.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						if(cboConnectname.getText().length() == 0)
							return;

						listener.removeProfile(cboConnectname.getText());					
						if(cboConnectname.getItemCount() > 0)
							cboConnectname.select(0);
						initForm();
						saveSettings = true;
					}
				});
				butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
				butRemove.setText("Remove");			
			}

			final Button butClose = new Button(shell, SWT.NONE);
			butClose.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					close();
					saved = true;
					shell.dispose();
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
			if(listener.getCurrProfileName() != null){
                cboConnectname.setText(listener.getCurrProfileName());
			}
			initForm();

			shell.layout();
			shell.open();
			shell.setFocus();
		} catch (Exception e) {
			message("could not open Profile Dialag cause: " + e.toString(), SWT.ICON_WARNING);
		}
	}


	private void initForm()  {
		
		try {
			FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
			init = true;
 			String s = cboConnectname.getText();
			cboConnectname.setItems(listener.getProfileNames());//löscht den Eintrag, daher mit s merken und wieder zurückschreiben
			cboConnectname.setText(s);

			//currProfile = listener.getProfiles().get(cboConnectname.getText()) != null ? (Properties)listener.getProfiles().get(cboConnectname.getText()) : new Properties();
			currProfile = listener.getProfiles().get(cboConnectname.getText()) != null ? (FTPProfile)listener.getProfiles().get(cboConnectname.getText()) : new FTPProfile(new Properties());

			listener.setCurrProfile(currProfile);
			listener.setCurrProfileName(cboConnectname.getText());

			txtHost.setText(currProfile.getHost());
			txtPort.setText(currProfile.getPort());
			txtUsername.setText(currProfile.getUser());
			txtPassword.setText(currProfile.getPassword() );
			txtRoot.setText(currProfile.getRoot());
			txtLocalDirectory.setText(currProfile.getLocaldirectory());

			butSavePassword.setSelection(currProfile.isSavePassword());

			useProxyButton.setSelection(currProfile.getUseProxy());
			if(useProxyButton.getSelection()) {
				txtProxyServer.setEnabled(true);
				txtProxyPort.setEnabled(true);
                txtProxyUser.setEnabled(true);
                txtProxyPassword.setEnabled(true);
                cboProxyProtocol.setEnabled(true);
                txtProxyServer.setText(sosString.parseToString(currProfile.getProxyServer()));
                txtProxyUser.setText(sosString.parseToString(currProfile.getProxyUser()));
                txtProxyPassword.setText(sosString.parseToString(currProfile.getProxyPassword()));
                cboProxyProtocol.setText(sosString.parseToString(currProfile.getProxyProtocol()));
				txtProxyPort.setText(sosString.parseToString(currProfile.getProxyPort()));
			} else {
				txtProxyServer.setEnabled(false);
				txtProxyPort.setEnabled(false);
                txtProxyUser.setEnabled(false);
                txtProxyPassword.setEnabled(false);
                cboProxyProtocol.setEnabled(false);
                txtProxyServer.setText("");             
                txtProxyUser.setText("");             
                txtProxyPassword.setText("");
                cboProxyProtocol.setText("");                
				txtProxyPort.setText("");						
			}


			if(currProfile.getTransfermode().equalsIgnoreCase("binary")) {						
				butbinary.setSelection(true);
				butAscii.setSelection(false);
			} else {
				butbinary.setSelection(false);
				butAscii.setSelection(true);
			}
			String protocol = sosString.parseToString(currProfile.getProtocol());
			if(protocol.length() == 0)
				protocol = "FTP";


			cboProtokol.setText(protocol);

			if(protocol.equalsIgnoreCase("SFTP")) {
				groupAuthenticationMethods.setEnabled(true);
				txtDirPublicKey.setText(sosString.parseToString(currProfile.getAuthFile()));
				String method = sosString.parseToString(currProfile.getAuthMethod());
				if(method.equalsIgnoreCase("publickey")) {
					butPublicKey.setSelection(true);	
					butAuthPassword.setSelection(false);
					butPasswordAndPublic.setSelection(false);
				} else if(method.equalsIgnoreCase("password")) {
					butAuthPassword.setSelection(true);
					butPublicKey.setSelection(false);	
					butPasswordAndPublic.setSelection(false);
				} else if(method.equalsIgnoreCase("both")) {
					butPasswordAndPublic.setSelection(true);
					butAuthPassword.setSelection(false);
					butPublicKey.setSelection(false);	
				}


			} else {
				groupAuthenticationMethods.setEnabled(false);
				txtDirPublicKey.setText("");
				butPublicKey.setSelection(false);
				butAuthPassword.setSelection(false);
				butPasswordAndPublic.setSelection(false);
			}


			butApply.setEnabled(false);

			combo.setText(listener.getCurrProfileName());

			newProfile = false;
			init = false;
		} catch (Exception e) {
			message("could not reaad FTP Profiles:" + e.getMessage()  , SWT.ICON_WARNING);

		}
	}


	private void setEnabled() {
		if(butApply != null)
			butApply.setEnabled(cboConnectname.getText().length() > 0);

	}

	private void apply() throws Exception {
		try {
			saved = false;
			Properties prop = new Properties();
			String pName = cboConnectname.getText();
			prop.put("profilename", pName);
			prop.put("host", txtHost.getText());
			prop.put("port", txtPort.getText());
			prop.put("user", txtUsername.getText());
			
			prop.put("password", txtPassword.getText());
			prop.put("root", txtRoot.getText());
			if(txtLocalDirectory.getText().length() > 0 &&
					!new java.io.File(txtLocalDirectory.getText()).exists())
				new java.io.File(txtLocalDirectory.getText()).mkdirs();
			prop.put("localdirectory", txtLocalDirectory.getText());
			prop.put("transfermode", butbinary.getSelection() ? "binary" : "ASCII");
			prop.put("save_password", (butSavePassword.getSelection() ? "yes" : "no"));
			prop.put("protocol", cboProtokol.getText());

			if(useProxyButton.getSelection()) {
				prop.put("use_proxy", "yes");
				prop.put("proxy_server", txtProxyServer.getText());
                prop.put("proxy_port", txtProxyPort.getText());
                prop.put("proxy_user", txtProxyUser.getText());
                prop.put("proxy_password", txtProxyPassword.getText());
                prop.put("proxy_protocol", cboProxyProtocol.getText());
			}	    				

			if(cboProtokol.getText().equalsIgnoreCase("SFTP")){
				String method = getAuthMethod();			
				prop.put("auth_method", method);
				prop.put("auth_file", txtDirPublicKey.getText());
			}

			if(newProfile && !listener.getProfiles().containsKey(cboConnectname.getText()) ||
					listener.getProfiles().isEmpty()) {
				//neuer Eintrag
				listener.getProfiles().put(pName, new FTPProfile(prop));	
			} else {
				listener.removeProfile(pName);
			}

			listener.setProfiles(pName, new FTPProfile(prop));

			listener.setCurrProfileName(pName);

			Properties p = new Properties();
			p.putAll(prop);
			try {
				FTPProfile profile = new FTPProfile(p);
				listener.setCurrProfile(profile);
			} catch (Exception e) {
				message("could not create an FTPProfile Object, cause: " + e.toString(), SWT.ICON_ERROR);
			}


			cboConnectname.setItems(listener.getProfileNames());
			cboConnectname.setText(pName);
			newProfile = false;
			saveSettings = true;//Änderungen haben stattgefunden, d.h. in die ini Datei zurückschreiben
			butApply.setEnabled(false);
			
		} catch (Exception e) {
			throw new Exception ("error in " + sos.util.SOSClassUtil.getMethodName() + ", could not apply profile, cause: " + e.toString());
		}
	}

	private String getAuthMethod() {
		String authMethod = "";
		if(butPublicKey.getSelection()){
            authMethod = "publickey";
		}else if(butAuthPassword.getSelection()){
            authMethod = "password";
		}else if(butPasswordAndPublic.getSelection()){
                authMethod = "both";
	  	}

		return authMethod;
	}

	private void close()  {

		startCursor(shell);
		try {
			listener.refresh();

			if(saved) {				
				return;
			}
			

			if (butApply.getEnabled()) {
				int cont = message( "Do you want to apply the changes you made?", SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
				if(cont == SWT.OK) {				
					apply();
				} 
			}
			if(saveSettings) {
				listener.saveProfile(butSavePassword.getSelection());			
			}

			if(combo != null) {
				String[] profileNames = listener.getProfileNames();
				HashMap<String, FTPProfile> p = listener.getProfiles();
				for(int i = 0; i < profileNames.length; i++) {
					combo.setData(profileNames[i], p.get(profileNames[i]));
				}
				combo.setItems(profileNames);
				combo.setText(listener.getCurrProfileName());
				if(profileNames.length > 0)
					combo.select(0);
				combo.setData(listener);
			}


		} catch (Exception e) {
			FTPProfile.log ("error in FTPProfileDialog.close(), cause: " + e.toString(), -1);
		} finally {
			try {
			fillCombo(combo);
			} catch (Exception e) {
				FTPProfile.log ("error in FTPProfileDialog.close(), cause: " + e.toString(), -1);
			}	
		}
		stopCursor(shell);




	}

  
	public static int message(String message, int style) {
		Shell s_ = shell;
		if(s_ == null)
			s_ = new Shell();
			
		MessageBox mb = new MessageBox(s_, style);
		mb.setMessage(message);

		String title = "Message";
		if ((style & SWT.ICON_ERROR) != 0){
            title = "Error";
		}else if ((style & SWT.ICON_INFORMATION) != 0){
            title = "Information";
		}else if ((style & SWT.ICON_QUESTION) != 0){
            title = "Question";
		}else if ((style & SWT.ICON_WARNING) != 0){
            title = "Warning";
		}
		mb.setText(title);

		return mb.open();
	}

	//setzt den Maus auf SandUhr
	public static void startCursor(Shell shell){
		if(!shell.isDisposed())
			shell.setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT));
	}

	//setzt den Maus auf Pfeil
	public static void stopCursor(Shell shell){
		if(!shell.isDisposed())
			shell.setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_ARROW));
	}


}
