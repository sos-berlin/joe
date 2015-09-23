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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

class FTPPopUpDialog extends org.eclipse.swt.widgets.Dialog {


	private Object result;

	private Object obj = null;

	private Text text = null; 

	public FTPPopUpDialog(Shell parent, int style) {
		super(parent, style);
	}


	public FTPPopUpDialog(Shell parent) {
		this(parent, 0);
	}

	public Object open(Object obj_) {	
		obj = obj_;
		return open();
	}

	public Object open() {
		Shell parent = getParent();
		final Shell newFolderShell =
			new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

		newFolderShell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(final TraverseEvent e) {				
				if(e.detail == SWT.TRAVERSE_ESCAPE) {
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
		gridLayout.numColumns = 2;
		newFolderShell.setLayout(gridLayout);
		newFolderShell.setText("Create New Folder");
		newFolderShell.setText(getText());
		newFolderShell.pack();


		text = new Text(newFolderShell, SWT.PASSWORD | SWT.BORDER);

		text.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR)
					doSomethings();
			}
		});
		text.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));

		final Button butOK = new Button(newFolderShell, SWT.NONE);
		butOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {


				doSomethings();

			}
		});
		butOK.setText("OK");

		final Button butCancel = new Button(newFolderShell, SWT.NONE);
		butCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				close();
			}
		});
		butCancel.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false));
		butCancel.setText("Cancel");
		newFolderShell.open();		

		newFolderShell.setSize(241, 107);


		org.eclipse.swt.widgets.Display display = parent.getDisplay();
		while (!newFolderShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;

	}


	public static void main(String[] args) {
		final Shell shell =
			new Shell();
		shell.pack();


		FTPPopUpDialog fTPPopUpDialog = new FTPPopUpDialog(shell);

		fTPPopUpDialog.open();
	}

	private void close() {
		getParent().close();
	}

	public void doSomethings() {

		if (obj instanceof FTPProfile) {

			FTPProfile prof = (FTPProfile)obj;
			prof.setPassword(text.getText());

		}
		close();
	}
}