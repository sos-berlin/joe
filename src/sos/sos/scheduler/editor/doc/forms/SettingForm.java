package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.DocumentationListener;
import sos.scheduler.editor.doc.listeners.SettingListener;

public class SettingForm extends Composite implements IUnsaved, IUpdateLanguage {
    private SettingListener  listener    = null;

    private DocumentationDom dom         = null;

    private Group            group1      = null;

    private Label            label6      = null;

    private Label            label7      = null;

    private Text             tName       = null;

    private Text             tDefault    = null;

    private Label            label8      = null;

    private Text             tID         = null;

    private Label            label9      = null;

    private Combo            cReference = null;

    private Label            label10     = null;

    private Button           cRequired   = null;

    private Label            label13     = null;

    private Button           bNotes      = null;

    private Combo            cType       = null;

    private Button           bApply      = null;

    private Label            label       = null;

    private Table            tSettings   = null;

    private Button           bNew        = null;

    private Label            label1      = null;

    private Button           bRemove     = null;


    public SettingForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
        super(parent, style);
        this.dom = dom;
        listener = new SettingListener(dom, parentElement);
        initialize();
    }


    private void initialize() {
        createGroup1();
        this.setLayout(new FillLayout()); // Generated
        setSize(new Point(825, 445));

        bApply.setEnabled(false);
        bRemove.setEnabled(false);
        cType.setItems(listener.getTypes());
        setSettingStatus(false);
        listener.fillSettings(tSettings);
        setToolTipText();
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        gridData6.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        gridData5.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL; // Generated
        gridData4.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData2 = new GridData();
        gridData2.horizontalSpan = 5; // Generated
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        gridData2.verticalAlignment = GridData.FILL; // Generated
        gridData2.grabExcessHorizontalSpace = true; // Generated
        gridData2.grabExcessVerticalSpace = true; // Generated
        gridData2.verticalSpan = 3; // Generated
        GridData gridData1 = new GridData();
        gridData1.horizontalSpan = 6; // Generated
        gridData1.verticalAlignment = GridData.CENTER; // Generated
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData = new GridData();
        gridData.verticalSpan = 4; // Generated
        gridData.verticalAlignment = GridData.BEGINNING; // Generated
        gridData.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData14 = new GridData();
        gridData14.verticalAlignment = GridData.CENTER; // Generated
        gridData14.horizontalAlignment = GridData.END; // Generated
        GridData gridData12 = new GridData();
        gridData12.horizontalAlignment = GridData.FILL; // Generated
        gridData12.grabExcessHorizontalSpace = true; // Generated
        gridData12.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.FILL; // Generated
        gridData11.grabExcessHorizontalSpace = true; // Generated
        gridData11.horizontalSpan = 4; // Generated
        gridData11.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        gridData3.grabExcessHorizontalSpace = true; // Generated
        gridData3.horizontalSpan = 4; // Generated
        gridData3.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 6; // Generated
        group1 = new Group(this, SWT.NONE);
        group1.setText("Settings"); // Generated
        group1.setLayout(gridLayout1); // Generated
        label6 = new Label(group1, SWT.NONE);
        label6.setText("Name:"); // Generated
        tName = new Text(group1, SWT.BORDER);
        tName.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tName.selectAll();
        	}
        });
        tName.setLayoutData(gridData3); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                applySettingStatus();
            }
        });
        bApply = new Button(group1, SWT.NONE);
        bApply.setText("Apply Setting"); // Generated
        bApply.setLayoutData(gridData); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applySetting();
            }
        });
        label7 = new Label(group1, SWT.NONE);
        label7.setText("Default Value:"); // Generated
        tDefault = new Text(group1, SWT.BORDER);
        tDefault.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tDefault.selectAll();		
        	}
        });
        tDefault.setLayoutData(gridData11); // Generated
        label13 = new Label(group1, SWT.NONE);
        label13.setText("Type:"); // Generated
        createCType();
        label9 = new Label(group1, SWT.NONE);
        label9.setText("Reference:"); // Generated
        createCReference();
        tDefault.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                applySettingStatus();
            }
        });
        label8 = new Label(group1, SWT.NONE);
        label8.setText("ID:"); // Generated
        tID = new Text(group1, SWT.BORDER);
        tID.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tID.selectAll();
        	}
        });
        tID.setLayoutData(gridData12); // Generated
        label10 = new Label(group1, SWT.NONE);
        label10.setText("Required:"); // Generated
        cRequired = new Button(group1, SWT.CHECK);
        bNotes = new Button(group1, SWT.NONE);
        bNotes.setText("Note..."); // Generated
        bNotes.setLayoutData(gridData14); // Generated
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                applySettingStatus();
            }
        });
        cRequired.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applySettingStatus();
            }
        });
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.setting");
                DocumentationForm.openNoteDialog(dom, listener.getSettingElement(), "note", tip, true, !listener
                        .isNewSetting(),"Settings Note");
            }

        });
        label = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label"); // Generated
        label.setLayoutData(gridData1); // Generated
        tSettings = new Table(group1, SWT.BORDER);
        tSettings.setHeaderVisible(true); // Generated
        tSettings.setLayoutData(gridData2); // Generated
        tSettings.setLinesVisible(true); // Generated
        tSettings.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tSettings.getSelectionCount() > 0) {
                    if (listener.selectSetting(tSettings.getSelectionIndex())) {
                        setSettingStatus(true);
                        bRemove.setEnabled(true);
                    }
                }
            }
        });
        TableColumn tableColumn = new TableColumn(tSettings, SWT.NONE);
        tableColumn.setWidth(150); // Generated
        tableColumn.setText("Name"); // Generated
        TableColumn tableColumn31 = new TableColumn(tSettings, SWT.NONE);
        tableColumn31.setWidth(120); // Generated
        tableColumn31.setText("Default"); // Generated
        TableColumn tableColumn3 = new TableColumn(tSettings, SWT.NONE);
        TableColumn tableColumn4 = new TableColumn(tSettings, SWT.NONE);
        tableColumn4.setWidth(70); // Generated
        tableColumn4.setText("Required"); // Generated
        TableColumn tableColumn2 = new TableColumn(tSettings, SWT.NONE);
        TableColumn tableColumn1 = new TableColumn(tSettings, SWT.NONE);
        tableColumn1.setWidth(120); // Generated
        tableColumn1.setText("ID"); // Generated
        tableColumn2.setWidth(120); // Generated
        tableColumn2.setText("Reference"); // Generated
        tableColumn3.setWidth(80); // Generated
        tableColumn3.setText("Type"); // Generated
        bNew = new Button(group1, SWT.NONE);
        bNew.setText("New Setting"); // Generated
        bNew.setLayoutData(gridData5); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewSetting();
                setSettingStatus(true);
                bRemove.setEnabled(false);
                tSettings.deselectAll();
            }
        });
        label1 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label1.setText("Label"); // Generated
        label1.setLayoutData(gridData6); // Generated
        bRemove = new Button(group1, SWT.NONE);
        bRemove.setText("Remove Setting"); // Generated
        bRemove.setLayoutData(gridData4); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tSettings.getSelectionCount() > 0) {
                    if (listener.removeSetting(tSettings.getSelectionIndex())) {
                        setSettingStatus(false);
                        bRemove.setEnabled(false);
                        tSettings.deselectAll();
                        listener.fillSettings(tSettings);
                    }
                }
            }
        });
    }


    /**
     * This method initializes cReference
     */
    private void createCReference() {
        GridData gridData13 = new GridData();
        gridData13.horizontalAlignment = GridData.FILL; // Generated
        gridData13.grabExcessHorizontalSpace = true; // Generated
        gridData13.horizontalSpan = 2;  // Generated
        gridData13.verticalAlignment = GridData.CENTER; // Generated
        cReference = new Combo(group1, SWT.NONE);
        cReference.setLayoutData(gridData13); // Generated
        cReference.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                applySettingStatus();
            }
        });
    }


    /**
     * This method initializes cType
     */
    private void createCType() {
        GridData gridData15 = new GridData();
        gridData15.horizontalAlignment = GridData.FILL; // Generated
        gridData15.grabExcessHorizontalSpace = true; // Generated
        gridData15.verticalAlignment = GridData.CENTER; // Generated
        cType = new Combo(group1, SWT.READ_ONLY);
        cType.setLayoutData(gridData15); // Generated
        cType.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applySettingStatus();
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }


    public void apply() {
        if (isUnsaved())
            applySetting();
    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    public void setToolTipText() {
        tName.setToolTipText(Messages.getTooltip("doc.setting.name"));
        bApply.setToolTipText(Messages.getTooltip("doc.setting.apply"));
        tDefault.setToolTipText(Messages.getTooltip("doc.setting.default"));
        tID.setToolTipText(Messages.getTooltip("doc.setting.id"));
        cRequired.setToolTipText(Messages.getTooltip("doc.setting.required"));
        cReference.setToolTipText(Messages.getTooltip("doc.setting.reference"));
        cType.setToolTipText(Messages.getTooltip("doc.setting.type"));
        bNotes.setToolTipText(Messages.getTooltip("doc.setting.notes"));
        tSettings.setToolTipText(Messages.getTooltip("doc.setting.table"));
        bNew.setToolTipText(Messages.getTooltip("doc.setting.new"));
        bRemove.setToolTipText(Messages.getTooltip("doc.setting.remove"));
    }


    private void setSettingStatus(boolean enabled) {
        tName.setEnabled(enabled);
        tDefault.setEnabled(enabled);
        tID.setEnabled(enabled);
        cRequired.setEnabled(enabled);
        cReference.setEnabled(enabled);
        cType.setEnabled(enabled);
        bNotes.setEnabled(enabled);
        if (enabled) {
            tName.setText(listener.getName());
            tDefault.setText(listener.getDefaultValue());
            tID.setText(listener.getID());
            cRequired.setSelection(listener.isRequired());
            DocumentationListener.setCheckbox(cReference, listener.getReferences(listener.getID()), listener
                    .getReference());
            cType.select(cType.indexOf(listener.getType()));
            tName.setFocus();
            getShell().setDefaultButton(bApply);
        }
        bApply.setEnabled(false);
    }


    private void applySettingStatus() {
        bApply.setEnabled(tName.getText().length() > 0);
        Utils.setBackground(tName, true);
    }


    private void applySetting() {
        listener.applySetting(tName.getText(), tDefault.getText(), tID.getText(), cRequired.getSelection(), cReference
                .getText(), cType.getText());
        listener.fillSettings(tSettings);
        setSettingStatus(tSettings.getSelectionCount() > 0);
        bRemove.setEnabled(tSettings.getSelectionCount() > 0);
    }

} // @jve:decl-index=0:visual-constraint="10,10"
