package sos.ftp.profiles;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.util.SOSString;

public class FTPProfileDialog {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPProfileDialog.class);
    private Group profilesGroup = null;
    private SOSString sosString = null;
    private static Shell shell = null;
    private FTPProfile currProfile = null;
    private Combo cboConnectname = null;
    private Text txtPort = null;
    private Text txtFtpUsername = null;
    private Text txtFtpPassword = null;
    private Text txtSFtpUsername = null;
    private Text txtSFtpPassphrase = null;
    private Text txtRoot = null;
    private Text txtLocalDirectory = null;
    private Button butAscii = null;
    private Button butbinary = null;
    private Button butPassive = null;
    private Text txtHost = null;
    private boolean newProfile = false;
    private FTPDialogListener listener = null;
    private Button useProxyButton = null;
    private Text txtProxyServer = null;
    private Button butUseKeyAgent = null;
    private Button butPrivateKeyFile = null;

    private Button butTwoFactor = null;
    private Button butPublickeyPassphraseInteractive = null;
    private Button butPublickeyPassphraseInput = null;
    private Text txtSftpPassword = null;

    private Text txtProxyUser = null;
    private Combo cboProxyProtocol = null;
    private Text txtProxyPassword = null;
    private Text txtProxyPort = null;
    private Button butApply = null;
    private boolean saveSettings = false;
    private Combo cboProtokol = null;
    private TabItem sFtpTabItem = null;
    private TabItem ftpTabItem = null;
    private Group groupSftp = null;
    private Button butAuthPublicKey = null;
    private Button butAuthPassword = null;
    private Button butAuthKeyboardInteractive = null;
    private Button butAuthPasswordInteractive = null;
    private Button butAuthFtpPasswordInteractive = null;
    private Button butAuthPasswordInput = null;
    private Button butAuthFtpPasswordInput = null;
    private Text txtPathToPrivateKey = null;
    private boolean saved = false;
    private Combo combo = null;
    private static boolean emptyItem = false;

    public FTPProfileDialog(File configFile) throws Exception {
        try {
            LOGGER.trace("calling " + sos.util.SOSClassUtil.getMethodName());
            sosString = new SOSString();
            listener = new FTPDialogListener(configFile);
            sosString = new SOSString();
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + " cause: " + e.toString());
        }
    }

    public void fillCombo(Combo combo_) throws Exception {
        try {
            LOGGER.trace("calling " + sos.util.SOSClassUtil.getMethodName());
            combo = combo_;
            listener.setConnectionsname(combo);
            String[] profileNames = listener.getProfileNames();
            HashMap<String, FTPProfile> p = listener.getProfiles();
            for (int i = 0; i < profileNames.length; i++) {
                combo.setData(profileNames[i], p.get(profileNames[i]));
            }
            combo.setItems(profileNames);
            combo.setText(listener.getCurrProfileName() != null ? listener.getCurrProfileName() : "");

            combo.setData(listener);
            sosString = new SOSString();
            if (emptyItem) {
                combo_.add("", 0);
                combo_.select(0);
            }
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + " cause: " + e.toString());
        }
    }

    public void fillCombo(Combo combo_, boolean emptyItem_) throws Exception {
        LOGGER.trace("calling " + sos.util.SOSClassUtil.getMethodName());
        try {
            emptyItem = emptyItem_;
            fillCombo(combo_);
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + " cause: " + e.toString());
        }
    }

    public void showModal(Combo combo_) {
        try {
            LOGGER.trace("calling " + sos.util.SOSClassUtil.getMethodName());
            combo = combo_;
            fillCombo(combo);
            shell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
            shell.addTraverseListener(new TraverseListener() {

                public void keyTraversed(final TraverseEvent e) {
                    if (e.detail == SWT.TRAVERSE_ESCAPE) {
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
            final GridLayout gridLayout = new GridLayout();
            gridLayout.marginTop = 5;
            gridLayout.marginRight = 5;
            gridLayout.marginLeft = 5;
            gridLayout.marginBottom = 5;
            shell.setLayout(gridLayout);
            shell.setSize(870, 520);
            shell.setText("Profiles");
            profilesGroup = new Group(shell, SWT.NONE);
            profilesGroup.setText("Profiles");
            final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
            gridData.widthHint = 650;
            gridData.heightHint = 233;
            profilesGroup.setLayoutData(gridData);
            final GridLayout gridLayout_profiles = new GridLayout();
            gridLayout_profiles.numColumns = 2;
            gridLayout_profiles.marginTop = 5;
            gridLayout_profiles.marginRight = 5;
            gridLayout_profiles.marginLeft = 5;
            gridLayout_profiles.marginBottom = 5;
            profilesGroup.setLayout(gridLayout_profiles);
            final TabFolder tabFolderProperties = new TabFolder(profilesGroup, SWT.NONE);
            final GridData gridData_properties = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 4);
            gridData_properties.heightHint = 178;
            tabFolderProperties.setLayoutData(gridData_properties);
            final TabItem propertiesTabItem = new TabItem(tabFolderProperties, SWT.NONE);
            propertiesTabItem.setText("Properties");
            final Group groupProperties = new Group(tabFolderProperties, SWT.NONE);
            final GridLayout gridLayout_properties = new GridLayout();
            gridLayout_properties.numColumns = 3;
            groupProperties.setLayout(gridLayout_properties);
            propertiesTabItem.setControl(groupProperties);
            final Label lblName = new Label(groupProperties, SWT.NONE);
            lblName.setText("Name");
            cboConnectname = new Combo(groupProperties, SWT.NONE);
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
                    if (!cboConnectname.getText().equals(currProfile.getProfilename())) {
                        initForm();
                    }
                }
            });
            final Label protocolLabel = new Label(groupProperties, SWT.NONE);
            protocolLabel.setText("Protocol");
            cboProtokol = new Combo(groupProperties, SWT.NONE);
            cboProtokol.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
            cboProtokol.setItems(new String[] { "FTP", "SFTP" });
            cboProtokol.select(0);

            cboProtokol.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    if ("FTP".equalsIgnoreCase(cboProtokol.getText())) {
                        if ("".equals(txtFtpUsername.getText())) {
                            txtFtpUsername.setText(txtSFtpUsername.getText());
                            txtSFtpUsername.setText("");
                        }
                        if ("".equals(txtFtpPassword.getText())) {
                            txtFtpPassword.setText(txtSFtpPassphrase.getText());
                            txtSFtpPassphrase.setText("");
                        }
                    } else {
                        if ("".equals(txtSFtpUsername.getText())) {
                            txtSFtpUsername.setText(txtFtpUsername.getText());
                            txtFtpUsername.setText("");
                        }
                        if ("".equals(txtSFtpPassphrase.getText())) {
                            txtSFtpPassphrase.setText(txtFtpPassword.getText());
                            txtFtpPassword.setText("");
                        }
                    }
                    setEnabled();
                }
            });

            final Label hostnameOrIpLabel = new Label(groupProperties, SWT.NONE);
            hostnameOrIpLabel.setText("Host Name or IP Address");
            txtHost = new Text(groupProperties, SWT.BORDER);
            txtHost.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });
            txtHost.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
            final Label portLabel = new Label(groupProperties, SWT.NONE);
            portLabel.setText("Port");
            txtPort = new Text(groupProperties, SWT.BORDER);
            txtPort.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });
            txtPort.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
            final Label rootLabel = new Label(groupProperties, SWT.NONE);
            rootLabel.setText("Root Directory");
            txtRoot = new Text(groupProperties, SWT.BORDER);
            txtRoot.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });
            txtRoot.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
            final Label directoryFroLocalLabel = new Label(groupProperties, SWT.NONE);
            directoryFroLocalLabel.setText("Directory For Local Copy");
            txtLocalDirectory = new Text(groupProperties, SWT.BORDER);
            txtLocalDirectory.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });
            txtLocalDirectory.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

            new Label(groupProperties, SWT.NONE);
            final Label transferModeLabel = new Label(groupProperties, SWT.NONE);
            final GridData gridData_3 = new GridData(GridData.BEGINNING, GridData.END, false, false);
            gridData_3.heightHint = 21;
            transferModeLabel.setLayoutData(gridData_3);
            transferModeLabel.setText("Transfer Mode");
            butAscii = new Button(groupProperties, SWT.RADIO);
            butAscii.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                }
            });
            butAscii.setLayoutData(new GridData());
            butAscii.setText("ASCII");
            new Label(groupProperties, SWT.NONE);
            new Label(groupProperties, SWT.NONE);
            butbinary = new Button(groupProperties, SWT.RADIO);
            butbinary.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                }
            });
            butbinary.setLayoutData(new GridData());
            butbinary.setText("Binary");

            new Label(groupProperties, SWT.NONE);
            final Label passiveMode = new Label(groupProperties, SWT.NONE);
            final GridData gridData_4 = new GridData(GridData.BEGINNING, GridData.END, false, false);
            gridData_3.heightHint = 21;
            passiveMode.setLayoutData(gridData_4);
            passiveMode.setText("Passive Mode");
            butPassive = new Button(groupProperties, SWT.CHECK);
            butPassive.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                }
            });
            butPassive.setLayoutData(new GridData());

            ftpTabItem = new TabItem(tabFolderProperties, SWT.NONE);
            ftpTabItem.setText("FTP");
            Group groupFtp = new Group(tabFolderProperties, SWT.NONE);
            final GridLayout gridLayout_ftp = new GridLayout();
            gridLayout_ftp.numColumns = 2;
            groupFtp.setLayout(gridLayout_ftp);
            ftpTabItem.setControl(groupFtp);
            new Label(groupFtp, SWT.NONE);
            new Label(groupFtp, SWT.NONE);
            final Label ftpUserNameLabel = new Label(groupFtp, SWT.NONE);
            ftpUserNameLabel.setText("User Name");
            txtFtpUsername = new Text(groupFtp, SWT.BORDER);
            txtFtpUsername.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });
            txtFtpUsername.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));

            Composite ftpPasswordRadio = new Composite(groupFtp, SWT.NULL);
            final GridLayout gridLayoutFtpPasswordRadio = new GridLayout();
            gridLayoutFtpPasswordRadio.numColumns = 3;
            ftpPasswordRadio.setLayout(gridLayoutFtpPasswordRadio);
            ftpPasswordRadio.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

            butAuthFtpPasswordInteractive = new Button(ftpPasswordRadio, SWT.RADIO);
            butAuthFtpPasswordInteractive.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    txtFtpPassword.setEnabled(!butAuthFtpPasswordInteractive.getSelection());
                }
            });
            new Label(ftpPasswordRadio, SWT.NONE);
            new Label(ftpPasswordRadio, SWT.NONE);

            butAuthFtpPasswordInteractive.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1));
            butAuthFtpPasswordInteractive.setText("Password Interactive");

            butAuthFtpPasswordInput = new Button(ftpPasswordRadio, SWT.RADIO);
            butAuthFtpPasswordInput.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    txtFtpPassword.setEnabled(butAuthFtpPasswordInput.getSelection());
                }
            });

            butAuthFtpPasswordInput.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1));
            butAuthFtpPasswordInput.setText("Use Password stored with JOE");

            txtFtpPassword = new Text(ftpPasswordRadio, SWT.PASSWORD | SWT.BORDER);
            txtFtpPassword.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });

            txtFtpPassword.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

            sFtpTabItem = new TabItem(tabFolderProperties, SWT.NONE);
            sFtpTabItem.setText("SFTP");
            groupSftp = new Group(tabFolderProperties, SWT.NONE);
            groupSftp.setText("Authentication Methods");
            final GridLayout gridLayout_sftp = new GridLayout();
            gridLayout_sftp.numColumns = 3;
            groupSftp.setLayout(gridLayout_sftp);
            sFtpTabItem.setControl(groupSftp);

            new Label(groupSftp, SWT.NONE);
            new Label(groupSftp, SWT.NONE);
            new Label(groupSftp, SWT.NONE);

            final Label sFtpUserNameLabel = new Label(groupSftp, SWT.NONE);
            sFtpUserNameLabel.setText("User Name");

            txtSFtpUsername = new Text(groupSftp, SWT.BORDER);
            txtSFtpUsername.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });
            txtSFtpUsername.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
            Label separator = new Label(groupSftp, SWT.HORIZONTAL | SWT.SEPARATOR);
            separator.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

            butTwoFactor = new Button(groupSftp, SWT.CHECK);
            butTwoFactor.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                }
            });
            final GridData gridData_pk = new GridData(GridData.BEGINNING, GridData.END, false, false, 3, 1);
            butTwoFactor.setLayoutData(gridData_pk);
            butTwoFactor.setText("Two-factor Authentication (Public/Private Key and Password are required)");

            new Label(groupSftp, SWT.NONE);
            new Label(groupSftp, SWT.NONE);
            new Label(groupSftp, SWT.NONE);

            final GridData gridData_pk2 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1);

            butAuthPublicKey = new Button(groupSftp, SWT.CHECK);
            butAuthPublicKey.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    txtSFtpPassphrase.setEnabled(butAuthPublicKey.getSelection());
                    txtPathToPrivateKey.setEnabled(butAuthPublicKey.getSelection());
                    butUseKeyAgent.setEnabled(butAuthPublicKey.getSelection());
                    butPrivateKeyFile.setEnabled(butAuthPublicKey.getSelection());
                    butPublickeyPassphraseInteractive.setEnabled(butAuthPublicKey.getSelection());
                    butPublickeyPassphraseInput.setEnabled(butAuthPublicKey.getSelection());
                }
            });

            butAuthPublicKey.setLayoutData(gridData_pk2);
            butAuthPublicKey.setText("Public / Private Key");

            Composite publickeyRadio = new Composite(groupSftp, SWT.NULL);
            final GridLayout gridLayoutPublickeyRadio = new GridLayout();
            gridLayoutPublickeyRadio.numColumns = 3;
            publickeyRadio.setLayout(gridLayoutPublickeyRadio);
            publickeyRadio.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

            butUseKeyAgent = new Button(publickeyRadio, SWT.RADIO);
            butUseKeyAgent.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    butPublickeyPassphraseInteractive.setEnabled(!butUseKeyAgent.getSelection());
                    txtPathToPrivateKey.setEnabled(!butUseKeyAgent.getSelection());
                    txtSFtpPassphrase.setEnabled(!butUseKeyAgent.getSelection());

                }
            });
            butUseKeyAgent.setLayoutData(gridData_pk2);
            butUseKeyAgent.setText("Use Key Agent");
            new Label(publickeyRadio, SWT.NONE);
            new Label(publickeyRadio, SWT.NONE);

            butPrivateKeyFile = new Button(publickeyRadio, SWT.RADIO);
            butPrivateKeyFile.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    butPublickeyPassphraseInteractive.setEnabled(butPrivateKeyFile.getSelection());
                    butPublickeyPassphraseInput.setEnabled(butPrivateKeyFile.getSelection());
                    txtPathToPrivateKey.setEnabled(butPrivateKeyFile.getSelection());
                    txtSFtpPassphrase.setEnabled(butPrivateKeyFile.getSelection());

                }
            });
            butPrivateKeyFile.setLayoutData(gridData_pk2);
            butPrivateKeyFile.setText("Use Private Key File");

            txtPathToPrivateKey = new Text(publickeyRadio, SWT.BORDER);
            txtPathToPrivateKey.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });

            txtPathToPrivateKey.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

            Composite publickeyPassphrase = new Composite(groupSftp, SWT.NULL);
            final GridLayout gridLayoutPublickeyPassPhrase = new GridLayout();
            gridLayoutPublickeyPassPhrase.numColumns = 3;
            publickeyPassphrase.setLayout(gridLayoutPublickeyPassPhrase);
            publickeyPassphrase.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));

            Label col = new Label(publickeyPassphrase, SWT.NONE);
            col.setText("                                         ");
            butPublickeyPassphraseInteractive = new Button(publickeyPassphrase, SWT.RADIO);
            butPublickeyPassphraseInteractive.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    txtSFtpPassphrase.setEnabled(!butPublickeyPassphraseInteractive.getSelection());
                }
            });

            butPublickeyPassphraseInteractive.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
            butPublickeyPassphraseInteractive.setText("Passphrase Interactive");

            col = new Label(publickeyPassphrase, SWT.NONE);
            col.setText("                                         ");
            butPublickeyPassphraseInput = new Button(publickeyPassphrase, SWT.RADIO);
            butPublickeyPassphraseInput.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    txtSFtpPassphrase.setEnabled(!butPublickeyPassphraseInteractive.getSelection());
                }
            });

            butPublickeyPassphraseInput.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1));
            butPublickeyPassphraseInput.setText("Use Passphrase stored with JOE");
            txtSFtpPassphrase = new Text(publickeyPassphrase, SWT.PASSWORD | SWT.BORDER);
            txtSFtpPassphrase.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });

            txtSFtpPassphrase.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));

            separator = new Label(groupSftp, SWT.HORIZONTAL | SWT.SEPARATOR);
            separator.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

            butAuthPassword = new Button(groupSftp, SWT.CHECK);
            butAuthPassword.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    txtSftpPassword.setEnabled(butAuthPassword.getSelection());
                    butAuthKeyboardInteractive.setEnabled(butAuthPassword.getSelection());
                    butAuthPasswordInteractive.setEnabled(butAuthPassword.getSelection());
                    butAuthPasswordInput.setEnabled(butAuthPassword.getSelection());

                }
            });

            butAuthPassword.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
            butAuthPassword.setText("Password");
            new Label(groupSftp, SWT.NONE);
            Composite passwordRadio = new Composite(groupSftp, SWT.NULL);
            final GridLayout gridLayoutPasswordRadio = new GridLayout();
            gridLayoutPasswordRadio.numColumns = 3;
            passwordRadio.setLayout(gridLayoutPasswordRadio);
            passwordRadio.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

            butAuthKeyboardInteractive = new Button(passwordRadio, SWT.RADIO);
            butAuthKeyboardInteractive.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    txtSftpPassword.setEnabled(!butAuthKeyboardInteractive.getSelection());
                }
            });

            final GridData gridData_ki = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1);
            butAuthKeyboardInteractive.setLayoutData(gridData_ki);
            butAuthKeyboardInteractive.setText("Keyboard Interactive");
            new Label(passwordRadio, SWT.NONE);
            new Label(passwordRadio, SWT.NONE);

            butAuthPasswordInteractive = new Button(passwordRadio, SWT.RADIO);
            butAuthPasswordInteractive.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    txtSftpPassword.setEnabled(!butAuthPasswordInteractive.getSelection());
                }
            });
            new Label(passwordRadio, SWT.NONE);
            new Label(passwordRadio, SWT.NONE);

            butAuthPasswordInteractive.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1));
            butAuthPasswordInteractive.setText("Password Interactive");

            butAuthPasswordInput = new Button(passwordRadio, SWT.RADIO);
            butAuthPasswordInput.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    setEnabled();
                    txtSftpPassword.setEnabled(butAuthPasswordInput.getSelection());
                }
            });

            butAuthPasswordInput.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1));
            butAuthPasswordInput.setText("Use Password stored with JOE");

            txtSftpPassword = new Text(passwordRadio, SWT.PASSWORD | SWT.BORDER);
            txtSftpPassword.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });

            txtSftpPassword.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

            final TabItem proxyTabItem = new TabItem(tabFolderProperties, SWT.NONE);
            proxyTabItem.setText("Proxy");
            final Group groupProxy = new Group(tabFolderProperties, SWT.NONE);
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
            cboProxyProtocol.setItems(new String[] { "http", "socks4", "socks5" });
            cboProxyProtocol.addModifyListener(new ModifyListener() {

                public void modifyText(final ModifyEvent e) {
                    setEnabled();
                }
            });

            new Label(profilesGroup, SWT.NONE);
            butApply = new Button(profilesGroup, SWT.NONE);
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
            final Button butNewProfile = new Button(profilesGroup, SWT.NONE);
            butNewProfile.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    newProfile = true;
                    cboConnectname.setText("");
                    txtPort.setText("");
                    txtFtpUsername.setText("");
                    txtFtpPassword.setText("");
                    txtRoot.setText("");
                    txtLocalDirectory.setText(getInitValueLocalDirectory());
                    butAscii.setSelection(true);
                    butbinary.setSelection(false);
                    butPassive.setSelection(true);
                    txtHost.setText("");
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
                    txtPathToPrivateKey.setText("");
                    butTwoFactor.setSelection(false);
                    butAuthPublicKey.setSelection(false);
                    butUseKeyAgent.setSelection(false);
                    butPrivateKeyFile.setSelection(false);
                    butPublickeyPassphraseInteractive.setSelection(false);
                    butPublickeyPassphraseInput.setSelection(false);
                    butAuthPassword.setSelection(false);
                    butAuthKeyboardInteractive.setSelection(false);
                    butAuthPasswordInteractive.setSelection(false);
                    butAuthFtpPasswordInteractive.setSelection(false);
                    butAuthPasswordInput.setSelection(false);
                    butAuthFtpPasswordInput.setSelection(false);
                    txtSftpPassword.setText("");

                }
            });
            butNewProfile.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butNewProfile.setText("New Profile");
            final Button butRemove = new Button(profilesGroup, SWT.NONE);
            butRemove.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    if (cboConnectname.getText().isEmpty()) {
                        return;
                    }
                    listener.removeProfile(cboConnectname.getText());
                    if (cboConnectname.getItemCount() > 0) {
                        cboConnectname.select(0);
                    }
                    initForm();
                    saveSettings = true;
                }
            });
            butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
            butRemove.setText("Remove");
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
                    if ("FTP".equalsIgnoreCase(cboProtokol.getText())) {
                        groupSftp.setEnabled(false);
                        groupFtp.setEnabled(true);
                    } else {
                        groupSftp.setEnabled(true);
                        groupFtp.setEnabled(false);
                    }
                    setEnabled();
                }
            });
            if (listener.getCurrProfileName() != null) {
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

    private String getInitValueLocalDirectory() {
        String s = System.getProperty("SCHEDULER_DATA");
        if (s == null) {
            s = System.getProperty("java.io.tmpdir");
        }
        s = s + "/joe/cache/";
        return s;
    }

    private void initForm() {
        try {
            LOGGER.trace("calling " + sos.util.SOSClassUtil.getMethodName());
            String s = cboConnectname.getText();
            cboConnectname.setItems(listener.getProfileNames());
            cboConnectname.setText(s);

            if (listener.getProfiles().get(cboConnectname.getText()) != null) {
                currProfile = (FTPProfile) listener.getProfiles().get(cboConnectname.getText());
            } else {
                currProfile = new FTPProfile(new Properties());
            }

            String protocol = sosString.parseToString(currProfile.getProtocol());
            if(!SOSString.isEmpty(protocol)) {
                protocol = protocol.toUpperCase();
            }
            listener.setCurrProfile(currProfile);
            listener.setCurrProfileName(cboConnectname.getText());
            txtHost.setText(currProfile.getHost());
            txtPort.setText(currProfile.getPort());
            if ("FTP".equalsIgnoreCase(protocol)) {
                txtFtpUsername.setText(currProfile.getUser());
                txtFtpPassword.setText(currProfile.getPassword());
            } else {
                txtSFtpUsername.setText(currProfile.getUser());
                txtSFtpPassphrase.setText(currProfile.getPassword());
            }
            txtRoot.setText(currProfile.getRoot());
            txtLocalDirectory.setText(currProfile.getLocaldirectory(getInitValueLocalDirectory()));
            butUseKeyAgent.setSelection(currProfile.isUseKeyAgent());
            useProxyButton.setSelection(currProfile.getUseProxy());
            if (useProxyButton.getSelection()) {
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
            if ("binary".equalsIgnoreCase(currProfile.getTransfermode())) {
                butbinary.setSelection(true);
                butAscii.setSelection(false);
            } else {
                butbinary.setSelection(false);
                butAscii.setSelection(true);
            }
            butPassive.setSelection(currProfile.isPassiveMode());
            if (protocol.isEmpty()) {
                protocol = "FTP";
            }
            cboProtokol.setText(protocol);
            if ("SFTP".equalsIgnoreCase(protocol)) {
                groupSftp.setEnabled(true);
                txtPathToPrivateKey.setText(sosString.parseToString(currProfile.getAuthFile()));
                txtSFtpPassphrase.setText(currProfile.getSftpPassphrase());
                txtSftpPassword.setText(currProfile.getPassword());
                txtSFtpUsername.setText(currProfile.getUser());
                butTwoFactor.setSelection(currProfile.isTwoFactorAuthentication());
                butAuthPublicKey.setSelection(currProfile.isPublicKeyAuthentication());
                butUseKeyAgent.setSelection(currProfile.isUseKeyAgent());
                butPrivateKeyFile.setSelection(!currProfile.isUseKeyAgent());
                butPublickeyPassphraseInteractive.setSelection(currProfile.isPromptForPassphrase());
                butPublickeyPassphraseInput.setSelection(!currProfile.isPromptForPassphrase());
                butAuthPassword.setSelection(currProfile.isPasswordAuthentication());
                butAuthKeyboardInteractive.setSelection(currProfile.isKeyboardInteractive());
                butAuthPasswordInteractive.setSelection(currProfile.isPromptForPassword());
                butAuthPasswordInput.setSelection(!currProfile.isPromptForPassword() && !currProfile.isKeyboardInteractive());
            } else {
                groupSftp.setEnabled(false);
                txtPathToPrivateKey.setText("");
                txtSFtpPassphrase.setText("");
                txtSftpPassword.setText("");
                txtSFtpUsername.setText("");
                butUseKeyAgent.setSelection(false);
                butTwoFactor.setSelection(false);
                butAuthPublicKey.setSelection(false);
                butAuthPassword.setSelection(false);
                butPublickeyPassphraseInteractive.setSelection(false);
                butAuthKeyboardInteractive.setSelection(false);
                butAuthPasswordInteractive.setSelection(false);
                butAuthFtpPasswordInteractive.setSelection(currProfile.isPromptForPassword());
                butAuthFtpPasswordInput.setSelection(!currProfile.isPromptForPassword());
            }
            butApply.setEnabled(false);
            combo.setText(listener.getCurrProfileName());
            newProfile = false;

            txtSFtpPassphrase.setEnabled(butAuthPublicKey.getSelection() && butPublickeyPassphraseInteractive.getSelection());
            txtPathToPrivateKey.setEnabled(butAuthPublicKey.getSelection() && butPrivateKeyFile.getSelection());
            txtSftpPassword.setEnabled(butAuthPassword.getSelection() && butAuthPasswordInput.getSelection());
            txtFtpPassword.setEnabled(butAuthFtpPasswordInput.getSelection());

            butUseKeyAgent.setEnabled(butAuthPublicKey.getSelection());
            butPrivateKeyFile.setEnabled(butAuthPublicKey.getSelection());
            butPublickeyPassphraseInteractive.setEnabled(butAuthPublicKey.getSelection());
            butPublickeyPassphraseInput.setEnabled(butAuthPublicKey.getSelection());

            butAuthKeyboardInteractive.setEnabled(butAuthPassword.getSelection());
            butAuthPasswordInteractive.setEnabled(butAuthPassword.getSelection());
            butAuthPasswordInput.setEnabled(butAuthPassword.getSelection());
            butAuthFtpPasswordInput.setEnabled("FTP".equalsIgnoreCase(protocol));
            butAuthFtpPasswordInteractive.setEnabled("FTP".equalsIgnoreCase(protocol));

            txtProxyServer.setEnabled(useProxyButton.getSelection());
            txtProxyPort.setEnabled(useProxyButton.getSelection());
            txtProxyUser.setEnabled(useProxyButton.getSelection());
            txtProxyPassword.setEnabled(useProxyButton.getSelection());
            cboProxyProtocol.setEnabled(useProxyButton.getSelection());

        } catch (

        Exception e) {
            message("could not reaad FTP Profiles:" + e.getMessage(), SWT.ICON_WARNING);
        }
    }

    private void setEnabled() {
        if (butApply != null) {
            butApply.setEnabled(!cboConnectname.getText().isEmpty());
        }
    }

    private void apply() throws Exception {
        try {
            saved = false;
            Properties prop = new Properties();
            String pName = cboConnectname.getText();
            prop.put("profilename", pName);
            prop.put("host", txtHost.getText());
            prop.put("port", txtPort.getText());
            if ("FTP".equalsIgnoreCase(cboProtokol.getText())) {
                prop.put("user", txtFtpUsername.getText());
                prop.put("password", txtFtpPassword.getText());
                prop.put("prompt_for_password", bool2String(butAuthFtpPasswordInteractive.getSelection()));

            }
            prop.put("root", txtRoot.getText());
            if (!txtLocalDirectory.getText().isEmpty() && !new java.io.File(txtLocalDirectory.getText()).exists()) {
                new java.io.File(txtLocalDirectory.getText()).mkdirs();
            }
            prop.put("localdirectory", txtLocalDirectory.getText());
            prop.put("transfermode", butbinary.getSelection() ? "binary" : "ASCII");
            prop.put("passivemode", butPassive.getSelection() ? "true" : "false");
            prop.put("protocol", cboProtokol.getText());
            if (useProxyButton.getSelection()) {
                prop.put("use_proxy", "yes");
                prop.put("proxy_server", txtProxyServer.getText());
                prop.put("proxy_port", txtProxyPort.getText());
                prop.put("proxy_user", txtProxyUser.getText());
                prop.put("proxy_password", txtProxyPassword.getText());
                prop.put("proxy_protocol", cboProxyProtocol.getText());
            }

            if ("SFTP".equalsIgnoreCase(cboProtokol.getText())) {
                prop.put("user", txtSFtpUsername.getText());
                prop.put("use_key_agent", bool2String(butUseKeyAgent.getSelection()));
                prop.put("twofactor_authentication", bool2String(butTwoFactor.getSelection()));
                prop.put("publickey_authentication", bool2String(butAuthPublicKey.getSelection()));
                prop.put("password_authentication", bool2String(butAuthPassword.getSelection()));
                prop.put("keyboard_interactive", bool2String(butAuthKeyboardInteractive.getSelection()));
                prop.put("prompt_for_passphrase", bool2String(butPublickeyPassphraseInteractive.getSelection()));
                prop.put("prompt_for_password", bool2String(butAuthPasswordInteractive.getSelection()));
                prop.put("sftp_passphrase", txtSFtpPassphrase.getText());
                prop.put("password", txtSftpPassword.getText());
                prop.put("auth_file", txtPathToPrivateKey.getText());

            }
            if (newProfile && !listener.getProfiles().containsKey(cboConnectname.getText()) || listener.getProfiles().isEmpty()) {
                listener.getProfiles().put(pName, new FTPProfile(prop));
            } else {
                listener.removeProfile(pName);
            }
            listener.setProfiles(pName, new FTPProfile(prop));
            listener.setCurrProfileName(pName);

            try {
                FTPProfile profile = new FTPProfile(prop);
                listener.setCurrProfile(profile);
            } catch (Exception e) {
                message("could not create an FTPProfile Object, cause: " + e.toString(), SWT.ICON_ERROR);
            }
            cboConnectname.setItems(listener.getProfileNames());
            cboConnectname.setText(pName);
            newProfile = false;
            saveSettings = true;
            butApply.setEnabled(false);
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ", could not apply profile, cause: " + e.toString());
        }
    }

    private String bool2String(boolean b) {
        if (b) {
            return "yes";
        } else {
            return "no";
        }
    }

    private void close() {
        startCursor(shell);
        try {
            listener.refresh();
            if (saved) {
                return;
            }
            if (butApply.getEnabled()) {
                int cont = message("Do you want to apply the changes you made?", SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                if (cont == SWT.OK) {
                    apply();
                }
            }
            if (saveSettings) {
                listener.saveProfile();
            }
            if (combo != null) {
                String[] profileNames = listener.getProfileNames();
                HashMap<String, FTPProfile> p = listener.getProfiles();
                for (int i = 0; i < profileNames.length; i++) {
                    combo.setData(profileNames[i], p.get(profileNames[i]));
                }
                combo.setItems(profileNames);
                combo.setText(listener.getCurrProfileName());
                if (profileNames.length > 0) {
                    combo.select(0);
                }
                combo.setData(listener);
            }
        } catch (Exception e) {
            LOGGER.error("error in FTPProfileDialog.close(), cause: " + e.toString(), e);
        } finally {
            try {
                fillCombo(combo);
            } catch (Exception e) {
                LOGGER.error("error in FTPProfileDialog.close(), cause: " + e.toString(), e);
            }
        }
        stopCursor(shell);
    }

    public static int message(String message, int style) {
        Shell s_ = shell;
        if (s_ == null) {
            s_ = new Shell();
        }
        MessageBox mb = new MessageBox(s_, style);
        mb.setMessage(message);
        String title = "Message";
        if ((style & SWT.ICON_ERROR) != 0) {
            title = "Error";
        } else if ((style & SWT.ICON_INFORMATION) != 0) {
            title = "Information";
        } else if ((style & SWT.ICON_QUESTION) != 0) {
            title = "Question";
        } else if ((style & SWT.ICON_WARNING) != 0) {
            title = "Warning";
        }
        mb.setText(title);
        return mb.open();
    }

    public static void startCursor(Shell shell) {
        if (!shell.isDisposed()) {
            shell.setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT));
        }
    }

    public static void stopCursor(Shell shell) {
        if (!shell.isDisposed()) {
            shell.setCursor(new Cursor(shell.getDisplay(), SWT.CURSOR_ARROW));
        }
    }

}