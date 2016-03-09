package sos.scheduler.editor.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
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
import com.sos.joe.globals.misc.ResourceManager;
import sos.scheduler.editor.app.MainWindow;
import sos.util.SOSString;
import java.util.*;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.options.Options;

public class WebDavDialogProfiles {

    private Text txtProxyPort = null;
    private Text txtProxyServer = null;
    private Button useProxyButton = null;
    private Group schedulerGroup = null;
    private SOSString sosString = null;
    private Shell schedulerConfigurationShell = null;
    private Properties currProfile = null;
    private Combo cboConnectname = null;
    private Text txtUsername = null;
    private Text txtPassword = null;
    private Text txtURL = null;
    private Text txtLocalDirectory = null;
    private boolean newProfile = false;
    private Button butSavePassword = null;
    private WebDavDialogListener listener = null;
    private boolean init = false;
    private Button butApply = null;
    private boolean saveSettings = false;
    private Combo cboProtokol = null;
    private boolean saved = false;	// hilsvariable

    public WebDavDialogProfiles(WebDavDialogListener listener_) {
        listener = listener_;
        sosString = new SOSString();
    }

    public void showForm() {
        schedulerConfigurationShell = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
        schedulerConfigurationShell.addTraverseListener(new TraverseListener() {

            public void keyTraversed(final TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                    close();
                    saved = true;
                    schedulerConfigurationShell.dispose();
                }
            }
        });
        schedulerConfigurationShell.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(final DisposeEvent e) {
                close();
            }
        });
        schedulerConfigurationShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginTop = 5;
        gridLayout.marginRight = 5;
        gridLayout.marginLeft = 5;
        gridLayout.marginBottom = 5;
        schedulerConfigurationShell.setLayout(gridLayout);
        schedulerConfigurationShell.setSize(558, 365);
        schedulerConfigurationShell.setText("Profiles");
        {
            schedulerGroup = new Group(schedulerConfigurationShell, SWT.NONE);
            /*
             * schedulerGroup.addTraverseListener(new TraverseListener() {
             * public void keyTraversed(final TraverseEvent e) { if(e.detail ==
             * SWT.TRAVERSE_ESCAPE) { close(); saved = true;
             * schedulerConfigurationShell.dispose(); } } });
             */
            schedulerGroup.setText("Profiles");
            final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
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
            gridLayout_2.numColumns = 2;
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
            final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, true, false);
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
            cboProtokol.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            cboProtokol.setItems(new String[] { "WebDav", "SWebDav" });
            cboProtokol.select(0);
            final Label userNameLabel = new Label(group, SWT.NONE);
            userNameLabel.setText("User Name");
            txtUsername = new Text(group, SWT.BORDER);
            txtUsername.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });
            txtUsername.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            // txtUsername.setText(currProfile.get("user") != null ?
            // currProfile.get("user").toString() : "");
            final Label passwordLabel = new Label(group, SWT.NONE);
            passwordLabel.setText("Password");
            txtPassword = new Text(group, SWT.PASSWORD | SWT.BORDER);
            txtPassword.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    if (init) {
                        try {
                            init = false;
                            if (txtPassword.getText().length() > 0) {
                                String key = Options.getProperty("profile.timestamp." + cboConnectname.getText());
                                if (key != null && key.length() > 8) {
                                    key = key.substring(key.length() - 8);
                                }
                                String password = txtPassword.getText();
                                if (password.length() > 0 && sosString.parseToString(key).length() > 0) {
                                    password = SOSCrypt.decrypt(key, password);
                                }
                                txtPassword.setText(password);
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    setEnabled();
                }
            });
            txtPassword.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            // txtPassword.setText(currProfile.get("password") != null ?
            // currProfile.get("password").toString() : "");
            final Label urlLabel = new Label(group, SWT.NONE);
            urlLabel.setText("URL");
            txtURL = new Text(group, SWT.BORDER);
            txtURL.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });
            txtURL.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            // txturl.setText(currProfile.get("url") != null ?
            // currProfile.get("url").toString() : "");
            final Label directoryFroLocalLabel = new Label(group, SWT.NONE);
            directoryFroLocalLabel.setText("Directory For Local Copy");
            txtLocalDirectory = new Text(group, SWT.BORDER);
            txtLocalDirectory.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });
            txtLocalDirectory.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            final Label savePasswordLabel = new Label(group, SWT.NONE);
            final GridData gridData_5 = new GridData(SWT.DEFAULT, 24);
            gridData_5.verticalIndent = 5;
            savePasswordLabel.setLayoutData(gridData_5);
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
            final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
            tabItem.setText("Proxy");
            final Group group_1 = new Group(tabFolder, SWT.NONE);
            final GridLayout gridLayout_3 = new GridLayout();
            gridLayout_3.numColumns = 2;
            group_1.setLayout(gridLayout_3);
            tabItem.setControl(group_1);
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
            // txtLocalDirectory.setText(currProfile.get("localdirectory") !=
            // null ? currProfile.get("localdirectory").toString() : "");
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
            // txtHost.setText(currProfile.get("host") != null ?
            // currProfile.get("host").toString() : "");
            final Button butNewProfile = new Button(schedulerGroup, SWT.NONE);
            butNewProfile.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    newProfile = true;
                    cboConnectname.setText("");
                    txtUsername.setText("");
                    txtPassword.setText("");
                    txtURL.setText("");
                    txtLocalDirectory.setText("");
                    butSavePassword.setSelection(false);
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
            // txtPort.setText(currProfile.get("port") != null ?
            // currProfile.get("port").toString() : "");
            final Button butRemove = new Button(schedulerGroup, SWT.NONE);
            butRemove.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    listener.removeProfile(cboConnectname.getText());
                    if (cboConnectname.getItemCount() > 0)
                        cboConnectname.select(0);
                    initForm();
                    saveSettings = true;
                }
            });
            butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
            butRemove.setText("Remove");
            /*
             * if(currProfile.get("transfermode") != null &&
             * currProfile.get("transfermode"
             * ).toString().equalsIgnoreCase("binary")) {
             * butbinary.setSelection(true); butAscii.setSelection(false); }
             * else { butbinary.setSelection(false);
             * butAscii.setSelection(true); }
             */
            // final Tree tree = new Tree(schedulerGroup, SWT.BORDER);
        }
        final Button butClose = new Button(schedulerConfigurationShell, SWT.NONE);
        butClose.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                close();
                saved = true;
                schedulerConfigurationShell.dispose();
            }
        });
        butClose.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
        butClose.setText("Close");
        cboProtokol.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                setEnabled();
            }
        });
        // setToolTipText();
        initForm();
        schedulerConfigurationShell.layout();
        schedulerConfigurationShell.open();
    }

    private void initForm() {
        try {
            init = true;
            setToolTip();
            String s = cboConnectname.getText();
            cboConnectname.setItems(listener.getProfileNames());// löscht den
                                                                // Eintrag,
                                                                // daher mit s
                                                                // merken und
                                                                // wieder
                                                                // zurückschreiben
            cboConnectname.setText(s);
            currProfile = listener.getProfiles().get(cboConnectname.getText()) != null ? (Properties) listener.getProfiles().get(cboConnectname.getText())
                    : new Properties();
            listener.setCurrProfile(currProfile);
            listener.setCurrProfileName(cboConnectname.getText());
            txtUsername.setText(currProfile.get("user") != null ? currProfile.get("user").toString() : "");
            txtPassword.setText(currProfile.get("password") != null ? currProfile.get("password").toString() : "");
            txtURL.setText(currProfile.get("url") != null ? currProfile.get("url").toString() : "");
            txtLocalDirectory.setText(currProfile.get("localdirectory") != null ? currProfile.get("localdirectory").toString() : "");
            butSavePassword.setSelection(sosString.parseToBoolean(currProfile.get("save_password")));
            useProxyButton.setSelection(sosString.parseToBoolean(currProfile.get("use_proxy")));
            if (useProxyButton.getSelection()) {
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
            String protocol = sosString.parseToString(currProfile.get("protocol"));
            if (protocol.length() == 0)
                protocol = "WebDav";
            cboProtokol.setText(protocol);
            butApply.setEnabled(false);
            newProfile = false;
            init = false;
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ;could not reaad WebDav Profiles", e);
            MainWindow.message("could not reaad WebDav Profiles:" + e.getMessage(), SWT.ICON_WARNING);
        }
    }

    private void setEnabled() {
        if (butApply != null)
            butApply.setEnabled(cboConnectname.getText().length() > 0);
    }

    private void apply() {
        try {
            Properties prop = new Properties();
            String pName = cboConnectname.getText();
            prop.put("name", pName);
            prop.put("user", txtUsername.getText());
            prop.put("password", txtPassword.getText());
            prop.put("url", txtURL.getText());
            if (txtLocalDirectory.getText().length() > 0 && !new java.io.File(txtLocalDirectory.getText()).exists())
                new java.io.File(txtLocalDirectory.getText()).mkdirs();
            prop.put("localdirectory", txtLocalDirectory.getText());
            prop.put("save_password", (butSavePassword.getSelection() ? "yes" : "no"));
            prop.put("protocol", cboProtokol.getText());
            if (useProxyButton.getSelection()) {
                prop.put("use_proxy", "yes");
                prop.put("proxy_server", txtProxyServer.getText());
                prop.put("proxy_port", txtProxyPort.getText());
            }
            if ((newProfile && !listener.getProfiles().containsKey(cboConnectname.getText())) || listener.getProfiles().isEmpty()) {
                // neuer Eintrag
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
            // initForm();
            newProfile = false;
            saveSettings = true;// Änderungen haben stattgefunden, d.h. in die
                                // ini Datei zurückschreiben
            butApply.setEnabled(false);
        } catch (Exception e) {
            new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ;could not save Profile " + cboConnectname.getText(), e);
            MainWindow.message("could not save WebDav Profile " + cboConnectname.getText() + ": " + e.getMessage(), SWT.ICON_WARNING);
        }
    }

    private void close() {
        if (saved)
            return;
        if (butApply.getEnabled()) {
            int cont = MainWindow.message(schedulerConfigurationShell, Messages.getString("MainListener.apply_changes"), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            if (cont == SWT.OK) {
                apply();
            }
        }
        if (saveSettings) {
            // listener.saveSettings();
            listener.saveProfile(butSavePassword.getSelection());
        }
    }

    private void setToolTip() {
        cboConnectname.setToolTipText(Messages.getTooltip("webdav_profile_dialog.profilename"));
        cboProtokol.setToolTipText(Messages.getTooltip("webdav_profile_dialog.protocol"));
        txtUsername.setToolTipText(Messages.getTooltip("webdav_profile_dialog.user_name"));
        txtPassword.setToolTipText(Messages.getTooltip("webdav_profile_dialog.password"));
        txtURL.setToolTipText(Messages.getTooltip("webdav_profile_dialog.url"));
        txtLocalDirectory.setToolTipText(Messages.getTooltip("webdav_profile_dialog.local_directory"));
        butSavePassword.setToolTipText(Messages.getTooltip("webdav_profile_dialog.save_password"));
    }
}
