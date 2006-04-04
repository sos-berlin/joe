package sos.scheduler.editor.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.widgets.Shell;

public class TextDialog extends Dialog {

	private Shell _shell;

	private StyledText _styledText;

	private Point _size = new Point(300, 200);

	private Image _image;
	private Button clipboardButton;	
	private int _shellStyle = SWT.CLOSE | SWT.TITLE
	| SWT.APPLICATION_MODAL;
	
	private int _textStyle =  SWT.WRAP | SWT.BORDER;
	private boolean clipBoardClick=false;

	
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
	}

	public void setContent(String content, int alignment) {
		_styledText.setText(content);
		 _styledText.setLineAlignment(0, _styledText.getLineCount(), alignment);
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
		_shell.setVisible(false);
		_shell.setText(getText());

		try {
			_image = new Image(_shell.getDisplay(), getClass()
					.getResourceAsStream("/sos/scheduler/editor/editor.png"));
			_shell.setImage(_image);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	
		setDialog();
	}

	public String open() {
		String s="";
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
		if (_image != null)
			_image.dispose();
		
		return s;
	}

	public void setClipBoard(boolean value ){
		clipboardButton.setVisible(value);
	}

	private void setDialog() {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		_shell.setLayout(gridLayout);

		GridData gridData1 = new GridData();
		GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);

		_styledText = new StyledText(_shell, _textStyle);
		_styledText.setEditable(false);
		_styledText.setLayoutData(gridData);

		Button closeButton = new Button(_shell, SWT.NONE);
		closeButton.setText("Close");
		closeButton.setLayoutData(gridData1);
		closeButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						_shell.close();
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

	}

	public boolean isClipBoardClick() {
		return clipBoardClick;
	}
	
}