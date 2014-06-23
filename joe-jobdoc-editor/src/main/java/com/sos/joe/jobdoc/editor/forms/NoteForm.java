package com.sos.joe.jobdoc.editor.forms;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.listeners.NoteListener;
import com.sos.joe.jobdoc.editor.listeners.SettingsListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class NoteForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
	private SettingsListener					settingsListener	= null;
	private Group								group				= null;
	@SuppressWarnings("unused") private Label	label				= null;
	private Combo								cLang				= null;
	private Text								text				= null;
	private NoteListener						listener			= null; // @jve:decl-index=0:
	private Button								bApply				= null;
	private int									type				= -1;
	private Button								bClear				= null;

	public NoteForm(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	public NoteForm(Composite parent, int style, int type) {
		super(parent, style);
		this.type = type;
		initialize();
	}

	public void setParams(DocumentationDom dom, Element parent, String name, boolean optional) {
		setParams(dom, parent, name, optional, true);
	}

	public void setParams(DocumentationDom dom, Element parent, String name, boolean optional, boolean changeStatus) {
		listener = new NoteListener(dom, parent, name, optional, changeStatus);
		cLang.setItems(listener.getLanguages());
		String strTemplateLang = listener.getLang();
		if (strTemplateLang == null) {
			strTemplateLang = Options.getTemplateLanguage();
		}
		if (strTemplateLang != null) {
			cLang.select(cLang.indexOf(strTemplateLang));
		}
		text.setText(listener.getNote());
		bApply.setEnabled(false);
	}

	public void setTitle(String title) {
		group.setText(title);
	}

	private void initialize() {
		createGroup();
		setSize(new Point(650, 446));
		setLayout(new FillLayout());
		bApply.setEnabled(false);
		setToolTipText();
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridData gridData8 = new GridData(GridData.END, GridData.CENTER, true, false);
		GridData gridData11 = new GridData(GridData.END, GridData.CENTER, false, false);
		gridData11.widthHint = 150; // Generated
		gridData11.horizontalIndent = 10; // Generated
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
		GridLayout gridLayout = new GridLayout(4, false);
		group = JOE_G_NoteForm_Documentation.Control(new Group(this, SWT.NONE));
		group.setLayout(gridLayout); // Generated
		label = JOE_L_NoteForm_Language.Control(new Label(group, SWT.NONE));
		createCLang();
		bClear = JOE_B_NoteForm_Clear.Control(new Button(group, SWT.NONE));
		bClear.setLayoutData(gridData8); // Generated
		bClear.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				text.setText("");
			}
		});
		bApply = JOE_B_NoteForm_Apply.Control(new Button(group, SWT.NONE));
		bApply.setLayoutData(gridData11); // Generated
		bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (isUnsaved())
					apply();
				bApply.setEnabled(false);
				if (!getShell().equals(ErrorLog.getSShell()))
					getShell().dispose();
			}
		});
		text = new Text(group, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
		text.setLayoutData(gridData); // Generated
		text.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			@Override public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bClear.setEnabled(text.getText().length() > 0);
				if (listener != null) {
					bApply.setEnabled(true);
				}
			}
		});
	}

	/**
	 * This method initializes cLang
	 */
	private void createCLang() {
		GridData gridData1 = new GridData();
		gridData1.widthHint = 100; // Generated
		cLang = JOE_Cbo_NoteForm_Language.Control(new Combo(group, SWT.BORDER | SWT.READ_ONLY));
		cLang.setLayoutData(gridData1); // Generated
		cLang.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				changeLang();
			}

			@Override public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	@Override public void setToolTipText() {
		switch (type) {
			case JOEConstants.DOC_CONFIGURATION:
				setToolTipText(JOE_M_NoteForm_Config.label());
				break;
			case JOEConstants.DOC_SETTINGS:
				setToolTipText(JOE_M_NoteForm_Settings.label());
				break;
			case JOEConstants.DOC_DOCUMENTATION:
				setToolTipText(JOE_M_NoteForm_Doc.label());
				break;
		}
	}

	@Override public void setToolTipText(String string) {
		super.setToolTipText(string);
		text.setToolTipText(string);
	}

	@Override public void apply() {
		if (listener != null) {
			listener.setNote(text.getText());
			listener.createDefault();
		}
	}

	@Override public boolean isUnsaved() {
		if (listener != null)
			listener.createDefault();
		if (settingsListener != null)
			settingsListener.checkSettings();
		return listener != null ? bApply.getEnabled() : false;
	}

	private void changeLang() {
		if (listener != null) {
			if (Utils.applyFormChanges(this)) {
				listener.setLang(cLang.getText());
				text.setText(listener.getNote());
				bApply.setEnabled(false);
			}
		}
		text.setFocus();
	}

	@Override public boolean setFocus() {
		text.setFocus();
		return super.setFocus();
	}

	public void setSettingsListener(SettingsListener settingsListener1) {
		this.settingsListener = settingsListener1;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
