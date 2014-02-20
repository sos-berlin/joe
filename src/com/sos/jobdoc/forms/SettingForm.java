package com.sos.jobdoc.forms;

import org.eclipse.swt.SWT;
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

import com.sos.jobdoc.DocumentationDom;
import com.sos.jobdoc.listeners.DocumentationListener;
import com.sos.jobdoc.listeners.SettingListener;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;

public class SettingForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
    private SettingListener  listener    = null;

    private DocumentationDom dom         = null;

    private Group            group1      = null;

    @SuppressWarnings("unused")
	private Label            label6      = null;

    @SuppressWarnings("unused")
	private Label            label7      = null;

    private Text             tName       = null;

    private Text             tDefault    = null;

    @SuppressWarnings("unused")
	private Label            label8      = null;

    private Text             tID         = null;

    @SuppressWarnings("unused")
	private Label            label9      = null;

    private Combo            cReference = null;

    @SuppressWarnings("unused")
	private Label            label10     = null;

    private Button           cRequired   = null;

    @SuppressWarnings("unused")
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
        GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        
        GridData gridData5 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        
        GridData gridData4 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        
        GridData gridData2 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 3);
        
        GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 6, 1);
        
        GridData gridData = new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 4);
        
        GridData gridData14 = new GridData(GridData.END,GridData.CENTER, false, false);
        
        GridData gridData12 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        
        GridData gridData11 = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
        
        GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
        
        GridLayout gridLayout1 = new GridLayout(6, false);
        
        group1 = JOE_G_SettingForm_Settings.Control(new Group(this, SWT.NONE));
        group1.setLayout(gridLayout1); // Generated
        
        label6 = JOE_L_Name.Control(new Label(group1, SWT.NONE));
        
        tName = JOE_T_SettingForm_Name.Control(new Text(group1, SWT.BORDER));
        tName.setLayoutData(gridData3); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                applySettingStatus();
            }
        });
        
        bApply = JOE_B_SettingForm_Apply.Control(new Button(group1, SWT.NONE));
        bApply.setLayoutData(gridData); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applySetting();
            }
        });
        
        label7 = JOE_L_SettingForm_DefaultValue.Control(new Label(group1, SWT.NONE));
        
        tDefault = JOE_T_SettingForm_DefaultValue.Control(new Text(group1, SWT.BORDER));
        tDefault.setLayoutData(gridData11); // Generated
        tDefault.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                applySettingStatus();
            }
        });
        
        label13 = JOE_L_SettingForm_Type.Control(new Label(group1, SWT.NONE));
        
        createCType();
        
        label9 = JOE_L_SettingForm_Reference.Control(new Label(group1, SWT.NONE));

        createCReference();
        
        label8 = JOE_L_SettingForm_ID.Control(new Label(group1, SWT.NONE));

        tID = JOE_T_SettingForm_ID.Control(new Text(group1, SWT.BORDER));
        tID.setLayoutData(gridData12); // Generated
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                applySettingStatus();
            }
        });
        
        label10 = JOE_L_SettingForm_Required.Control(new Label(group1, SWT.NONE));
        
        cRequired = JOE_B_SettingForm_Required.Control(new Button(group1, SWT.CHECK));
        cRequired.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applySettingStatus();
            }
        });
        cRequired.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applySettingStatus();
            }
        });
        
        bNotes = JOE_B_SettingForm_Notes.Control(new Button(group1, SWT.NONE));
        bNotes.setLayoutData(gridData14); // Generated
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//                String tip = Messages.getTooltip("doc.note.text.setting");
            	String tip = "";
//                DocumentationForm.openNoteDialog(dom, listener.getSettingElement(), "note", tip, true, !listener
//                        .isNewSetting(),"Settings Note");
                DocumentationForm.openNoteDialog(dom, listener.getSettingElement(), "note", tip, true, !listener
                        .isNewSetting(), JOE_B_SettingForm_Notes.label());
            }

        });
        
        label = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setText("Label"); // Generated
        label.setLayoutData(gridData1); // Generated
        
        tSettings = JOE_Tbl_SettingForm_Settings.Control(new Table(group1, SWT.BORDER));
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
        
        TableColumn tableColumn = JOE_TCl_SettingForm_Name.Control(new TableColumn(tSettings, SWT.NONE));
        tableColumn.setWidth(150); // Generated
        
        TableColumn tableColumn31 = JOE_TCl_SettingForm_Default.Control(new TableColumn(tSettings, SWT.NONE));
        tableColumn31.setWidth(120); // Generated
        
        TableColumn tableColumn3 = JOE_TCl_SettingForm_Type.Control(new TableColumn(tSettings, SWT.NONE));
        tableColumn3.setWidth(80); // Generated
        
        TableColumn tableColumn4 = JOE_TCl_SettingForm_Required.Control(new TableColumn(tSettings, SWT.NONE));
        tableColumn4.setWidth(70); // Generated
        
        TableColumn tableColumn2 = JOE_TCl_SettingForm_Reference.Control(new TableColumn(tSettings, SWT.NONE));
        tableColumn2.setWidth(110); // Generated
        
        TableColumn tableColumn1 = JOE_TCl_SettingForm_ID.Control(new TableColumn(tSettings, SWT.NONE));
        tableColumn1.setWidth(120); // Generated
        
        bNew = JOE_B_SettingForm_New.Control(new Button(group1, SWT.NONE));
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
        
        bRemove = JOE_B_SettingForm_Remove.Control(new Button(group1, SWT.NONE));
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
        GridData gridData13 = new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1);

        cReference = JOE_Cbo_SettingForm_Reference.Control(new Combo(group1, SWT.NONE));
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
        GridData gridData15 = new GridData(GridData.FILL, GridData.CENTER, true, false);

        cType = JOE_Cbo_SettingForm_Type.Control(new Combo(group1, SWT.READ_ONLY));
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
//
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
