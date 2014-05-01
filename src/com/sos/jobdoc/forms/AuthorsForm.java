package com.sos.jobdoc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;

import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.jobdoc.DocumentationDom;
import com.sos.jobdoc.listeners.ReleaseAuthorsListener;
import com.sos.joe.interfaces.IUnsaved;
import com.sos.joe.interfaces.IUpdateLanguage;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public class AuthorsForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {

	@SuppressWarnings("unused")
	private final static String	conSVNVersion			= "$Id$";
	private ReleaseAuthorsListener	listener		= null;

	private Group					authorsGroup	= null;
	private Label					label4			= null;
	private Text					tName			= null;
	private Label					label5			= null;
	private Text					tEmail			= null;
	private Button					bApplyAuthor	= null;
	private Table					tAuthors		= null;
	private Button					bRemoveAutho	= null;

	public AuthorsForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
		super(parent, style);
		listener = new ReleaseAuthorsListener(dom, parentElement);
		initialize();
		setToolTipText();
	}

	private void initialize() {
		createGroup();
		setSize(new Point(801, 462));
		setLayout(new FillLayout());
		setReleaseStatus(false);
		listener.fillAuthors(tAuthors);
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5; // Generated
		
		authorsGroup = JOE_G_AuthorsForm_Authors.Control(new Group(this, SWT.NONE));
		authorsGroup.setLayout(gridLayout); // Generated
		
		createCreated();
		createModified();
		createComposite();
		createGroup1();
	}

	/**
	 * This method initializes group1
	 */
	private void createGroup1() {
		GridData gridData5 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
		gridData5.widthHint = 486;
		GridData gridData12 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 5; // Generated
		
		// group1 = new Group(group, SWT.NONE);
		// group1.setText("Authors"); // Generated
		// group1.setLayoutData(gridData5); // Generated
		// group1.setLayout(gridLayout1); // Generated
		
		label4 = JOE_L_Name.Control(new Label(authorsGroup, SWT.NONE));
		label4.setLayoutData(new GridData());
		
		GridData gridData7 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData7.widthHint = 121;
		tName = JOE_T_AuthorsForm_Name.Control(new Text(authorsGroup, SWT.BORDER));
		tName.setLayoutData(gridData7); // Generated
		tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (tName.getText().length() > 0 && tEmail.getText().length() > 0) {
					bApplyAuthor.setEnabled(true);
					getShell().setDefaultButton(bApplyAuthor);
				}
				Utils.setBackground(tName, bApplyAuthor.isEnabled());
			}
		});
		
		label5 = JOE_L_AuthorsForm_EMail.Control(new Label(authorsGroup, SWT.NONE));
		label5.setLayoutData(new GridData());
		GridData gridData8 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData8.widthHint = 183;
		
		tEmail = JOE_T_AuthorsForm_EMail.Control(new Text(authorsGroup, SWT.BORDER));
		tEmail.setLayoutData(gridData8); // Generated
		tEmail.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if (tName.getText().length() > 0 && tEmail.getText().length() > 0) {
					bApplyAuthor.setEnabled(true);
					getShell().setDefaultButton(bApplyAuthor);
				}
				Utils.setBackground(tEmail, bApplyAuthor.isEnabled());
			}
		});
		
		GridData gridData9 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		bApplyAuthor = JOE_B_AuthorsForm_Apply.Control(new Button(authorsGroup, SWT.NONE));
		bApplyAuthor.setLayoutData(gridData9); // Generated
		bApplyAuthor.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				applyAuthor(tName.getText(), tEmail.getText());
				bApplyAuthor.setEnabled(false);
				tName.setText("");
				tEmail.setText("");
				tAuthors.deselectAll();
				tName.setFocus();

			}
		});
		
		GridData gridData11 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
		tAuthors = JOE_Tbl_AuthorsForm_Authors.Control(new Table(authorsGroup, SWT.FULL_SELECTION | SWT.BORDER));
		tAuthors.setHeaderVisible(true); // Generated
		tAuthors.setLinesVisible(true); // Generated
		tAuthors.setLayoutData(gridData11); // Generated
		tAuthors.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (tAuthors.getSelectionCount() > 0) {
					TableItem author = tAuthors.getItem(tAuthors.getSelectionIndex());
					tName.setText(author.getText(0));
					tEmail.setText(author.getText(1));
					bRemoveAutho.setEnabled(true);
					tName.setFocus();
				}
				bApplyAuthor.setEnabled(false);
			}
		});
		
		TableColumn tableColumn2 = JOE_TCl_AuthorsForm_Name.Control(new TableColumn(tAuthors, SWT.NONE));
		tableColumn2.setWidth(250); // Generated
		
		TableColumn tableColumn11 = JOE_TCl_AuthorsForm_EMail.Control(new TableColumn(tAuthors, SWT.NONE));
		tableColumn11.setWidth(60); // Generated
		
		bRemoveAutho = JOE_B_AuthorsForm_Remove.Control(new Button(authorsGroup, SWT.NONE));
		bRemoveAutho.setLayoutData(gridData12); // Generated
		bRemoveAutho.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (tAuthors.getSelectionCount() > 0) {
					// tAuthors.remove(tAuthors.getSelectionIndex());
					listener.removeAuthor((Element) tAuthors.getSelection()[0].getData());
					listener.fillAuthors(tAuthors);
					tName.setText("");
					tEmail.setText("");
					bApplyAuthor.setEnabled(false);
					bRemoveAutho.setEnabled(false);
					tAuthors.deselectAll();
					Utils.setBackground(tAuthors, true);
				}
			}
		});
	}

	/**
	 * This method initializes composite
	 */
	private void createComposite() {
	}

	/**
	 * This method initializes created
	 */
	private void createCreated() {
	}

	/**
	 * This method initializes modified
	 */
	private void createModified() {
	}

	public void apply() {
		applyRelease();
	}

	public boolean isUnsaved() {
		return false;
	}

	public void setToolTipText() {
//
	}

	private void setReleaseStatus(boolean enabled) {
		// tEmail.setEnabled(enabled);
		bApplyAuthor.setEnabled(false);
		// tAuthors.setEnabled(enabled);
		bRemoveAutho.setEnabled(false);

		if (enabled) {
			listener.fillAuthors(tAuthors);
		}
	}

	/*private void applyAuthor(String name, String email) {
	    name = name.trim();
	    email = email.trim();
	    for (int i = 0; i < tAuthors.getItemCount(); i++) {
	        TableItem author = tAuthors.getItem(i);
	        if (author.getText(0).equalsIgnoreCase(name)) {
	            author.setText(0, name);
	            author.setText(1, email);
	            Utils.setBackground(tAuthors, true);
	            return;
	        }
	    }

	    // else new item
	    TableItem author = new TableItem(tAuthors, SWT.NONE);
	    author.setText(0, name);
	    author.setText(1, email);
	    Utils.setBackground(tAuthors, true);
	}*/

	private void applyAuthor(String name, String email) {
		name = name.trim();
		email = email.trim();
		for (int i = 0; i < tAuthors.getItemCount(); i++) {
			TableItem author = tAuthors.getItem(i);
			if (author.getText(0).equalsIgnoreCase(name)) {
				author.setText(0, name);
				author.setText(1, email);
				Utils.setBackground(tAuthors, true);
				return;
			}
		}

		// else new item
		TableItem author = new TableItem(tAuthors, SWT.NONE);
		author.setText(0, name);
		author.setText(1, email);
		Utils.setBackground(tAuthors, true);
		listener.applyRelease(tAuthors);
	}

	private void applyRelease() {
		tName.setText("");
		tEmail.setText("");
	}

} // @jve:decl-index=0:visual-constraint="10,10"
