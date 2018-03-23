package sos.ftp.profiles;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.jcraft.jsch.*;

public class JOEUserInfo implements UserInfo, UIKeyboardInteractive {

	private String passwd;

	public String getPassword() {
		return passwd;
	}

	public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
			boolean[] echo) {
		if (passwd == null) {
			final Shell shell = new Shell();
			shell.pack();
			FTPPopUpDialog fTPPopUpDialog = new FTPPopUpDialog(shell, prompt[0]);

			fTPPopUpDialog.open(this);
		}
		String[] response = new String[prompt.length];
    	response[0] = passwd;
		return response;
	}

	@Override
	public String getPassphrase() {
		return null;
	}

	@Override
	public boolean promptPassword(String message) {
		final Shell shell = new Shell();
		shell.pack();
		FTPPopUpDialog fTPPopUpDialog = new FTPPopUpDialog(shell);
		fTPPopUpDialog.open(this);
		return true;
	}

	@Override
	public boolean promptPassphrase(String message) {
		return false;
	}

	@Override
	public boolean promptYesNo(String message) {
		Display display = new Display();
		Shell shell = new Shell(display);
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
		messageBox.setText("Please confirm");
		messageBox.setMessage(message);
		int buttonID = messageBox.open();
		switch (buttonID) {
		case SWT.YES:
			return true;
		case SWT.NO:
			return false;
		case SWT.CANCEL:
			return false;
		}
		return false;
	};

	@Override
	public void showMessage(String message) {
		Display display = new Display();
		Shell shell = new Shell(display);
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.ABORT | SWT.RETRY | SWT.IGNORE);

		messageBox.setText("");
		messageBox.setMessage(message);
		messageBox.open();
	}

	public void setPassword(String passwd) {
		this.passwd = passwd;
	}

}