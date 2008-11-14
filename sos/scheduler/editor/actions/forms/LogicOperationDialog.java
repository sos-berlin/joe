package sos.scheduler.editor.actions.forms;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import java.util.ArrayList;

import sos.scheduler.editor.app.ErrorLog;
import sos.util.SOSString;

class LogicOperationDialog extends org.eclipse.swt.widgets.Dialog {
	
	
	Object result;
	
	private Text            txt                   = null;
	
	private Text            txtExpression         = null; 

	private Button          butCancel             = null; 
	
	private Button          butApply              = null;
	
	private List            list                  = null;
	
	private ArrayList       operator              = null;
	
	private SOSString       sosString             = new SOSString();
	
	public LogicOperationDialog(int style) {
		super(new Shell(), style);
	}
	
	
	public Object open(Text txt_, ArrayList operator_) {
		//listener = listener_;
		txt = txt_;
		operator = operator_;
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
		
		newFolderShell.setImage(sos.scheduler.editor.app.ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 10;
		gridLayout.horizontalSpacing = 10;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		gridLayout.marginTop = 10;
		gridLayout.numColumns = 2;
		newFolderShell.setLayout(gridLayout);
		newFolderShell.setText("Logical Operation");
		newFolderShell.setText(getText());
		newFolderShell.pack();

		
			txtExpression = new Text(newFolderShell, SWT.MULTI | SWT.BORDER | SWT.WRAP);
		
		txtExpression.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR)
					doSomethings();
			}
		});
		final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
		gridData.heightHint = 104;
		txtExpression.setLayoutData(gridData);

		list = new List(newFolderShell, SWT.BORDER);
		list.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				if(list.getSelectionCount() > 0) {
					String sel = list.getSelection()[0];
					
					
					txtExpression.insert(sel);
					
						
					//String removeSelectedStr = txtExpression.getText().substring(0, txtExpression.getSelection().x) +					
                    //txtExpression.getText().substring(txtExpression.getSelection().y);
					
					//txtExpression.setText(removeSelectedStr);
				}
			}
		});
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1);
		gridData_1.heightHint = 167;
		list.setLayoutData(gridData_1);

		butCancel = new Button(newFolderShell, SWT.NONE);
		butCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				close();
			}
		});
		butCancel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		butCancel.setText("Cancel");

		butApply = new Button(newFolderShell, SWT.NONE);
		butApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				doSomethings();
			}
		});
		butApply.setText("Apply");
		newFolderShell.open();		
		
		//org.eclipse.swt.graphics.Rectangle rect = image.getBounds();
		newFolderShell.setSize(442, 381);

		init();
		
		
		org.eclipse.swt.widgets.Display display = parent.getDisplay();
		while (!newFolderShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		//image.dispose();		
		return result;
		
	}
	
	private void init() {
		try {
			
			txtExpression.setText(txt.getText());
			
			for(int i =0; i < operator.size(); i++) {
				list.add(sosString.parseToString(operator.get(i)));
			}
		} catch(Exception e){
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			} catch(Exception ee) {
				//tu nichts
			}
		}
	}

	public static void main(String[] args) {
		final Shell shell =
			new Shell();
		shell.pack();
		

		LogicOperationDialog logicOperationDialog = new LogicOperationDialog(SWT.NONE);
		Text text = new Text(shell, SWT.NONE);
		text.setText("1 or 2");
		ArrayList l = new ArrayList();
		l.add("or");
		l.add("and");
		l.add("(<key1> and <key2>)");
		l.add("group1");
		l.add("group2");
		logicOperationDialog.open(text, l);
	}
	
	private void close() {
		getParent().close();
	}
	
	public void doSomethings() {
		txt.setText(txtExpression.getText());
		close();
	}
}