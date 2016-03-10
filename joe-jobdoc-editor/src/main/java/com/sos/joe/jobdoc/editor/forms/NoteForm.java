package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_NoteForm_Apply;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_NoteForm_Clear;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Cbo_NoteForm_Language;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_NoteForm_Documentation;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_NoteForm_Language;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_NoteForm_Config;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_NoteForm_Doc;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_M_NoteForm_Settings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.dialog.components.TextArea;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;
import com.sos.joe.jobdoc.editor.listeners.NoteListener;
import com.sos.joe.jobdoc.editor.listeners.SettingsListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class NoteForm extends JobDocBaseForm<NoteListener> {

    private SettingsListener settingsListener = null;
    private Group group = null;
    @SuppressWarnings("unused")
    private Label label = null;
    private Combo cLang = null;
    private TextArea text = null;
    private int type = -1;
    private Button bClear = null;

    public NoteForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    public NoteForm(Composite parent, int style, int type) {
        this(parent, style);
        // super(parent, style);
        this.type = type;
        // initialize();
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
        text.setXMLText(listener.getNote());
        bApply.setEnabled(false);
    }

    public void setTitle(String title) {
        group.setText(title);
    }

    private void initialize() {
        createGroup();
        bApply.setEnabled(false);
        setToolTipText();
    }

    /** This method initializes group */
    private void createGroup() {
        GridData gridData8 = new GridData(GridData.END, GridData.CENTER, true, false);
        GridData gridData11 = new GridData(GridData.END, GridData.CENTER, false, false);
        gridData11.widthHint = 150;
        gridData11.horizontalIndent = 10;
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
        GridLayout gridLayout = new GridLayout(4, false);
        group = JOE_G_NoteForm_Documentation.Control(new SOSGroup(this, SWT.NONE));
        group.setLayout(gridLayout);
        label = JOE_L_NoteForm_Language.Control(new SOSLabel(group, SWT.NONE));
        createCLang();
        bClear = JOE_B_NoteForm_Clear.Control(new Button(group, SWT.NONE));
        bClear.setLayoutData(gridData8);
        bClear.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                text.setXMLText("");
            }
        });

        bApply = JOE_B_NoteForm_Apply.Control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData11);
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (isUnsaved()) {
                    apply();
                }
                bApply.setEnabled(false);
                if (!getShell().equals(ErrorLog.getSShell())) {
                    getShell().dispose();
                }
            }
        });

        text = new TextArea(group, "JobDoc.Documentation");
        text.setLayoutData(gridData);
        text.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                bClear.setEnabled(text.getText().length() > 0);
                if (listener != null) {
                    bApply.setEnabled(true);
                }
            }
        });
    }

    /** This method initializes cLang */
    private void createCLang() {
        GridData gridData1 = new GridData();
        gridData1.widthHint = 100;
        cLang = JOE_Cbo_NoteForm_Language.Control(new Combo(group, SWT.BORDER | SWT.READ_ONLY));
        cLang.setLayoutData(gridData1);
        cLang.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                changeLang();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    public void setToolTipText() {
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

    @Override
    public void setToolTipText(String string) {
        super.setToolTipText(string);
        text.setToolTipText(string);
    }

    @Override
    public void apply() {
        if (listener != null) {
            listener.setNote(text.getText());
            listener.createDefault();
            text.setXMLText(text.getText());
        }
    }

    @Override
    public boolean isUnsaved() {
        if (listener != null) {
            listener.createDefault();
        }
        if (settingsListener != null) {
            settingsListener.checkSettings();
        }
        return listener != null ? bApply.getEnabled() : false;
    }

    private void changeLang() {
        if (listener != null) {
            if (Utils.applyFormChanges(this)) {
                listener.setLang(cLang.getText());
                text.setXMLText(listener.getNote());
                bApply.setEnabled(false);
            }
        }
        setFocus();
    }

    @Override
    public boolean setFocus() {
        text.setFocus();
        return super.setFocus();
    }

    public void setSettingsListener(SettingsListener settingsListener1) {
        this.settingsListener = settingsListener1;
    }

    @Override
    public void openBlank() {
    }

    @Override
    protected void applySetting() {
        apply();
    }

    @Override
    public boolean applyChanges() {
        apply();
        return false;
    }
} // @jve:decl-index=0:visual-constraint="10,10"
