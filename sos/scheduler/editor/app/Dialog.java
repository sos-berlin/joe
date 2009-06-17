package sos.scheduler.editor.app;
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


class Dialog extends org.eclipse.swt.widgets.Dialog {
	
	
	Object result;
	
	//private FTPDialogListener listener = null;
	//FTPDialog ftpDialog = null;	
	private Object obj = null;
	
	private Text text = null; 

	public Dialog(Shell parent, int style) {
		super(parent, style);
	}
	
	
	public Dialog(Shell parent) {
		this(parent, 0);
	}
	
	/*public Object open(FTPDialog ftpDialog_) {
		//listener = listener_;
		ftpDialog = ftpDialog_;
		return open();
	}*/
	
	public Object open(Object obj_) {
		//listener = listener_;
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
		
		newFolderShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
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

		/*if (obj instanceof FTPDialogListener)
			text = new Text(newFolderShell, SWT.PASSWORD | SWT.BORDER);
		else
		*/ 
			text = new Text(newFolderShell, SWT.BORDER);
		
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
		
		//org.eclipse.swt.graphics.Rectangle rect = image.getBounds();
		newFolderShell.setSize(241, 107);

		
		org.eclipse.swt.widgets.Display display = parent.getDisplay();
		while (!newFolderShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		//image.dispose();		
		return result;
		
	}
	

	public static void main(String[] args) {
		final Shell shell =
			new Shell();
		shell.pack();
		

		Dialog dialog = new Dialog(shell);
		
		dialog.open();
	}
	
	private void close() {
		getParent().close();
	}
	
	public void doSomethings() {
		try {
		if(obj instanceof FTPDialog) {
			
			FTPDialog ftpDialog = (FTPDialog)obj;
			
			ftpDialog.getListener().getCurrProfile().mkDirs(text.getText());
			ftpDialog.refresh();
			
		/*} else if (obj instanceof FTPDialogListener) {
			
			FTPDialogListener listener = (FTPDialogListener)obj;
			listener.setPassword(text.getText());
			*/
		} else if(obj instanceof WebDavDialog) {
				
				WebDavDialog webdavDialog = (WebDavDialog)obj;
				String parentPath = webdavDialog.getTxtUrl().getText();
				if(!parentPath.endsWith("/"))
					parentPath = parentPath + "/";
				
				webdavDialog.getListener().mkDirs(parentPath + text.getText());
				webdavDialog.refresh();
				
		} else if (obj instanceof WebDavDialogListener) {
			
			WebDavDialogListener listener = (WebDavDialogListener)obj;
			listener.setPassword(text.getText());
			
		}
		} catch (Exception e) {
			try {
  			new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
  		} catch(Exception ee) {
  			//tu nichts
  		}
		}
		close();
	}
}