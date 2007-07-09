package sos.scheduler.editor.doc.forms;

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

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.NoteListener;
import sos.scheduler.editor.doc.listeners.SettingsListener;

public class NoteForm extends Composite implements IUnsaved, IUpdateLanguage {
    private SettingsListener settingsListener = null;

    private Group            group            = null;

    private Label            label            = null;

    private Combo            cLang            = null;

    private Text             text             = null;

    private NoteListener     listener         = null; // @jve:decl-index=0:

    private Button           bApply           = null;

    private int              type             = -1;

    private Button           bClear           = null;


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
        cLang.select(cLang.indexOf(listener.getLang()));
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
        GridData gridData8 = new GridData();
        gridData8.horizontalAlignment = GridData.END; // Generated
        gridData8.grabExcessHorizontalSpace = true; // Generated
        gridData8.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.END; // Generated
        gridData11.widthHint = 150; // Generated
        gridData11.horizontalIndent = 10; // Generated
        gridData11.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData = new GridData();
        gridData.horizontalSpan = 4; // Generated
        gridData.verticalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.grabExcessVerticalSpace = true; // Generated
        gridData.horizontalAlignment = GridData.FILL; // Generated
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4; // Generated
        group = new Group(this, SWT.NONE);
        group.setText("Documentation"); // Generated
        group.setLayout(gridLayout); // Generated
        label = new Label(group, SWT.NONE);
        label.setText("Language:"); // Generated
        createCLang();
        bClear = new Button(group, SWT.NONE);
        bClear.setText("Clear"); // Generated
        bClear.setLayoutData(gridData8); // Generated
        bClear.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                text.setText("");
            }
        });
        bApply = new Button(group, SWT.NONE);
        bApply.setText("Apply"); // Generated
        bApply.setLayoutData(gridData11); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
              if (isUnsaved())
                    apply();
                bApply.setEnabled(false);
                getShell().dispose();
            }
        });
        text = new Text(group, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        text.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        text.setLayoutData(gridData); // Generated
        text.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
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
        cLang = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
        cLang.setLayoutData(gridData1); // Generated
        cLang.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                changeLang();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }


    public void setToolTipText() {
        cLang.setToolTipText(Messages.getTooltip("doc.note.language"));
        bClear.setToolTipText(Messages.getTooltip("doc.note.clear"));
        bApply.setToolTipText(Messages.getTooltip("doc.note.apply"));

        switch (type) {
            case Editor.DOC_CONFIGURATION:
                setToolTipText(Messages.getTooltip("doc.note.text.configuration"));
                break;
            case Editor.DOC_SETTINGS:
                setToolTipText(Messages.getTooltip("doc.note.text.settings"));
                break;
            case Editor.DOC_DOCUMENTATION:
                setToolTipText(Messages.getTooltip("doc.note.text.documentation"));
                break;
        }
    }


    public void setToolTipText(String string) {
        super.setToolTipText(string);
        text.setToolTipText(string);
    }


    public void apply() {
        if (listener != null) {
            listener.setNote(text.getText());
            listener.createDefault();
        }
    }


    public boolean isUnsaved() {
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


    public boolean setFocus() {
        text.setFocus();
        return super.setFocus();
    }


    public void setSettingsListener(SettingsListener settingsListener) {
        this.settingsListener = settingsListener;
    }

} // @jve:decl-index=0:visual-constraint="10,10"
