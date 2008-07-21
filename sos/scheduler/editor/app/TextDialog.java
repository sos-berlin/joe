package sos.scheduler.editor.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TextDialog extends Dialog {

	private Shell      _shell;

	private StyledText _styledText;

	private Point      _size           = new Point(300, 200);
	
	private Image      _image          = null;

	private Button     clipboardButton = null;

	private int        _shellStyle     = SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL;

	private int        _textStyle      = SWT.WRAP | SWT.BORDER;


	private boolean    clipBoardClick  = false;

	private Button     butApply        = null; 

	private boolean    applyBoardClick = false;

	private boolean    bSaveWindow     = false;

	private boolean    bEdit           = false;

	public TextDialog(Shell parent, int shellStyle, int textStyle) {
		super(parent, SWT.NONE);
		_shellStyle = shellStyle;
		_textStyle = textStyle;
		init();

	}


	public TextDialog(Shell parent) {
		super(parent, SWT.NONE);        
		init();

	}


	public void setContent(String content) {
		setContent(content, SWT.LEFT);        
		bEdit = true;
	}

	/**
	 * Schreibt den String in das Dialog und schrolt zu der Stelle, 
	 * in der selectString steht 
	 * @param content
	 * @param selectString
	 */
	public void setContent(String content, String selectString) {

		String _selectString = "=\"" + selectString + "\"";
		int pos = content.indexOf(_selectString);

		if (pos == -1) {
			pos = content.indexOf(selectString);
		}

		setContent(content, SWT.LEFT);    	                       
		_styledText.setSelection(pos, pos);        
		_styledText.showSelection();
		bEdit = true;
	}

	public void setContent(String content, int alignment) {
		_styledText.setText(content);        
		_styledText.setLineAlignment(0, _styledText.getLineCount(), alignment);
		bEdit = true;

	}


	public void setSize(Point size) {
		_size = size;
	}


	public StyledText getStyledText() {
		return _styledText;
	}


	private void init() {
		Shell parent = getParent(); 
		_shell = new Shell(parent, _shellStyle);
		_shell.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {   
				if(bSaveWindow)
					Options.saveWindow(_shell, "xml_dialog"); 
				else if (!bSaveWindow && butApply.isEnabled()) {
					close();
				}
			}
		});

		_shell.setVisible(false);



		_shell.setText(getText());

		try {
			_image = ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png");
			_shell.setImage(_image);
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			e.printStackTrace();
			return;
		}

		setDialog();
	}


	public String open(boolean bLoadWindow) {
		try {
			String s = "";
			if(bLoadWindow)
				Options.loadWindow(_shell, "xml_dialog");
			else
				_shell.setSize(_size);

			_shell.open();

			Display display = _shell.getDisplay();
			while (!_shell.isDisposed()) {
				s = _styledText.getText();
				if (!display.readAndDispatch())
					display.sleep();
			}

			if (_styledText != null)
				_styledText.dispose();

			return s;
		} catch (Exception e) {
			try {
				new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() , e);
			} catch(Exception ee) {
				//tu nichts
			}
			return "";
		}
	}



	public void setClipBoard(boolean value) {
		clipboardButton.setVisible(value);
	}


	private void setDialog() {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		_shell.setLayout(gridLayout);

		GridData gridData1 = new GridData();
		GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 3, 1);

		_styledText = new StyledText(_shell, SWT.V_SCROLL | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
		_styledText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {        		
				if (bEdit)
					butApply.setEnabled(true);  

			}
		});
		_styledText.setEditable(false);
		_styledText.setLayoutData(gridData);


		Button closeButton = new Button(_shell, SWT.NONE);
		closeButton.setText("Close");
		closeButton.setLayoutData(gridData1);
		closeButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				close();            	
			}
		});

		clipboardButton = new Button(_shell, SWT.NONE);
		clipboardButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				clipBoardClick = true;
				_shell.close();

			}
		});
		clipboardButton.setVisible(false);
		clipboardButton.setLayoutData(new GridData());
		clipboardButton.setText("Clipboard");


		butApply = new Button(_shell, SWT.NONE);
		butApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				applyBoardClick =true;
				_shell.close();
			}
		});
		butApply.setEnabled(false);
		butApply.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		butApply.setText("Apply");


	}


	public boolean isClipBoardClick() {
		return clipBoardClick;
	}


	public boolean isApplyBoardClick() {
		return applyBoardClick;
	}


	public void setVisibleApplyButton(boolean bApply) {
		butApply.setVisible(bApply);
	}


	public Shell getShell() {
		return _shell;
	}

	//location soll gespeichert werden 
	public void setBSaveWindow(boolean saveWindow) {
		bSaveWindow = saveWindow;
	}

	private void close() {
		if(butApply.getEnabled()) {
			int count = MainWindow.message(_shell, sos.scheduler.editor.app.Messages.getString("detailform.close"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
			if(count != SWT.OK) {
				return;
			}
		}				
		_shell.close();
	}
}