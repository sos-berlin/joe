package sos.ftp.profiles;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

 
class FTPPopUpDialog extends org.eclipse.swt.widgets.Dialog {

    private Object result;
    private Object obj = null;
    private Text password = null;
    private String passwordPrompt = "Password";

    public FTPPopUpDialog(Shell parent, int style) {
        super(parent, style);
    }

    public FTPPopUpDialog(Shell parent) {
        this(parent, 0);
    }
    public FTPPopUpDialog(Shell parent, String passwordPrompt) {
        this(parent, 0);
    	this.passwordPrompt = passwordPrompt;
    }

    public Object open(Object obj_) {
        obj = obj_;
        return open();
    }

    public Object open() {
        Shell parent = getParent();
        final Shell newFolderShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        newFolderShell.addTraverseListener(new TraverseListener() {

            public void keyTraversed(final TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                    close();
                }
            }
        });
        final GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 10;
        gridLayout.horizontalSpacing = 10;
        gridLayout.marginHeight = 10;
        gridLayout.marginWidth = 10;
        gridLayout.marginTop = 10;
        gridLayout.numColumns = 3;
        newFolderShell.setLayout(gridLayout);
        newFolderShell.setText("Please enter the Password");
        newFolderShell.pack();
        
        final Label passwordLabel = new Label(newFolderShell, SWT.NONE);
        passwordLabel.setText(passwordPrompt);

        password = new Text(newFolderShell, SWT.PASSWORD | SWT.BORDER);
        password.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                	assignPasswort();
                }
            }
        });
        password.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
        new Label(newFolderShell, SWT.NONE);

        final Button butOK = new Button(newFolderShell, SWT.NONE);
        butOK.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
            	assignPasswort();
            }
        });
        butOK.setText("OK");
        GridData gd_btOk = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
        gd_btOk.widthHint = 60;
        butOK.setLayoutData(gd_btOk);
        newFolderShell.setDefaultButton(butOK);

        final Button butCancel = new Button(newFolderShell, SWT.NONE);
        butCancel.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                close();
            }
        });
        butCancel.setText("Cancel");
        GridData gd_btCancel = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
        gd_btCancel.widthHint = 60;
        butOK.setLayoutData(gd_btCancel);
        newFolderShell.open();
        newFolderShell.setSize(241, 107);
        org.eclipse.swt.widgets.Display display = parent.getDisplay();
        while (!newFolderShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        final Shell shell = new Shell();
        shell.pack();
        FTPPopUpDialog fTPPopUpDialog = new FTPPopUpDialog(shell);
        fTPPopUpDialog.open();
    }

    private void close() {
        getParent().close();
    }

    public String getPassword() {
    	return password.getText();
    }
    
    public void assignPasswort() {
        if (obj instanceof FTPProfile) {
            FTPProfile prof = (FTPProfile) obj;
            prof.setPassword(password.getText());
        }
        if (obj instanceof JOEUserInfo) {
        	JOEUserInfo j = (JOEUserInfo) obj;
            j.setPassword(password.getText());
        }
        close();
    }

}