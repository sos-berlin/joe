package sos.ftp.profiles;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Combo;
import sos.util.SOSLogger;
import org.eclipse.swt.widgets.Text;
import java.io.File;

public class FTPProfilePicker extends Composite {

    private Button button = null;
    private Combo cboProfile = null;
    private File configFile = null;
    private FTPDialogListener listener = null;
    private FTPProfileDialog profileDialog = null;

    public FTPProfilePicker(Composite parent, int style, String configFile_) {
        super(parent, style);
        try {
            if (configFile_ == null) {
                throw new Exception("Config File is null");
            }
            configFile = new File(configFile_);
            initialize();
            init();
        } catch (Exception e) {
            FTPProfile.log("error in FTPProfilePicker.init()" + ", cause: " + e.toString(), 1);
        }

    }

    public FTPProfilePicker(Composite parent, int style, File configFile_) {
        super(parent, style);
        try {
            configFile = configFile_;
            initialize();
            init();
        } catch (Exception e) {
            FTPProfile.log("error in FTPProfilePicker.init()" + ", cause: " + e.toString(), 1);
        }
    }

    private void init() throws Exception {
        try {
            FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
            FTPProfile.log("Configuration File: " + (configFile != null ? configFile.getCanonicalPath() : ""), SOSLogger.DEBUG9);
            if (!configFile.exists() && !configFile.createNewFile()) {
                FTPProfileDialog.message("Could not create config file: " + configFile, SWT.ICON_WARNING);
                throw new Exception("Could not create config file: " + configFile);
            }
            profileDialog = new FTPProfileDialog(configFile);
            profileDialog.fillCombo(cboProfile);
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getClassName() + ", cause: " + e.toString());
        }
    }

    private void initialize() throws Exception {
        try {
            FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
            GridData gridData2 = new GridData();
            gridData2.horizontalAlignment = GridData.BEGINNING;
            gridData2.verticalAlignment = GridData.FILL;
            GridData gridData = new GridData();
            gridData.horizontalAlignment = GridData.FILL;
            gridData.grabExcessHorizontalSpace = true;
            gridData.verticalAlignment = GridData.FILL;
            GridLayout gridLayout = new GridLayout();
            gridLayout.numColumns = 2;
            gridLayout.verticalSpacing = 0;
            gridLayout.marginWidth = 0;
            gridLayout.marginHeight = 0;
            gridLayout.horizontalSpacing = 0;
            cboProfile = new Combo(this, SWT.READ_ONLY | SWT.BORDER);
            cboProfile.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    if (!cboProfile.getText().isEmpty()) {
                        listener = (FTPDialogListener) cboProfile.getData();
                        listener.setCurrProfileName(cboProfile.getText());
                    }
                }
            });
            cboProfile.setLayoutData(gridData);
            cboProfile.setText("");
            button = new Button(this, SWT.NONE);
            button.setText("Profile...");
            button.setLayoutData(gridData2);
            this.setLayout(gridLayout);
            button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

                public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                    profileDialog.showModal(cboProfile);
                }
            });
            setSize(new Point(242, 45));
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getClassName() + ", cause: " + e.toString());
        }
    }

    public void dispose() {
        FTPProfile.log("calling dispose", SOSLogger.DEBUG9);
        super.dispose();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        cboProfile.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        cboProfile.setVisible(visible);
        button.setVisible(visible);
    }

    public void removeModifyListener(ModifyListener listener) {
        cboProfile.removeModifyListener(listener);
    }

    public void setToolTipText(String string) {
        super.setToolTipText(string);
        cboProfile.setToolTipText(string);

    }

    public void setButtonText(String txt) {
        if (txt != null) {
            button.setText(txt);
        } else {
            button.setText("");
        }
    }

    public FTPProfile getProfileByName(String name) throws Exception {
        try {
            FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
            if (listener == null) {
                listener = (FTPDialogListener) cboProfile.getData();
            }
            if (listener.getProfiles() != null && listener.getProfiles().get(name) != null) {
                listener.setCurrProfileName(name);
                cboProfile.setText(name);
                return (FTPProfile) listener.getProfiles().get(name);
            }
            return null;
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ", cause: " + e.toString());
        }
    }

    public FTPProfile getSelectedFTPProfile() throws Exception {
        try {
            FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
            if (listener == null) {
                listener = (FTPDialogListener) cboProfile.getData();
            }
            if (getSelectedProfilename() != null) {
                return (FTPProfile) listener.getProfiles().get(getSelectedProfilename());
            }
            return null;
        } catch (Exception e) {
            throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ", cause: " + e.toString());
        }
    }

    public void setLogger(SOSLogger logger_) {
        FTPProfile.logger = logger_;
    }

    public void setLogText(Text text_) {
        FTPProfile.logtext = text_;
    }

    public String getSelectedProfilename() {
        return cboProfile.getText();
    }

    public void setProfilename(String profilename) {
        cboProfile.setText(profilename);
    }

    public FTPDialogListener getListener() {
        if (listener == null) {
            listener = (FTPDialogListener) cboProfile.getData();
        }
        return this.listener;
    }

    public void addModifyListener(ModifyListener listener) {
        cboProfile.addModifyListener(listener);
    }

    public void addSelectionListener(SelectionAdapter listener) {
        cboProfile.addSelectionListener(listener);
    }

    public void addEmptyItem() throws Exception {
        FTPProfile.log("calling " + sos.util.SOSClassUtil.getMethodName(), SOSLogger.DEBUG9);
        profileDialog.fillCombo(cboProfile, true);
    }

}