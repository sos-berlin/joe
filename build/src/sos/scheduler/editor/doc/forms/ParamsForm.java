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
import sos.scheduler.editor.doc.listeners.ParamsListener;

public class ParamsForm extends Composite implements IUpdateLanguage, IUnsaved {
    private ParamsListener   listener         = null; // @jve:decl-index=0:

    private DocumentationDom dom              = null; // @jve:decl-index=0:

    private Group            group            = null;

    private Label            label            = null;

    private Text             tParamsID        = null;

    private Label            label1           = null;

    private Combo            cParamsReference = null;

    private Button           bParamsNotes     = null;

    private Group            group1           = null;

    private Label            label2           = null;

    private Label            label3           = null;

    private Label            label4           = null;

    private Text             tName            = null;

    private Text             tDefault         = null;

    private Button           cRequired        = null;

    private Label            label5           = null;

    private Text             tID              = null;

    private Label            label6           = null;

    private Combo            cReference       = null;

    private Button           bApply           = null;

    private Label            label7           = null;

    private Table            tParams          = null;

    private Button           bNew             = null;

    private Label            label8           = null;

    private Button           bRemove          = null;

    private Button           bNotes           = null;


    public ParamsForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }


    public ParamsForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
        super(parent, style);
        initialize();

        setParams(dom, parentElement);
    }


    public void setParams(DocumentationDom dom, Element parentElement) {
        this.dom = dom;

        listener = new ParamsListener(dom, parentElement);

        listener.fillParams(tParams);
        bRemove.setEnabled(false);
        tParamsID.setText(listener.getID());
        DocumentationListener.setCheckbox(cParamsReference, listener.getReferences(listener.getID()), listener
                .getReference());
    }


    private void initialize() {
        createGroup();
        setSize(new Point(743, 429));
        setLayout(new FillLayout());

        setParamStatus(false);
        tParamsID.setFocus();
        setToolTipText();
    }


    public void setParamNote(boolean enabled) {
        bParamsNotes.setVisible(enabled);
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5; // Generated
        group = new Group(this, SWT.NONE);
        group.setText("Parameters"); // Generated
        group.setLayout(gridLayout); // Generated
        label = new Label(group, SWT.NONE);
        label.setText("ID:"); // Generated
        tParamsID = new Text(group, SWT.BORDER);
        tParamsID.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tParamsID.selectAll();
        	}
        });
        tParamsID.setLayoutData(gridData); // Generated
        tParamsID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setID(tParamsID.getText());
            }
        });
        label1 = new Label(group, SWT.NONE);
        label1.setText("Reference:"); // Generated
        createCParamsReference();
        bParamsNotes = new Button(group, SWT.NONE);
        bParamsNotes.setText("Parameter Note..."); // Generated
        bParamsNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.params");
                DocumentationForm.openNoteDialog(dom, listener.getParamsElement(), "note", tip, true,"Parameter Note");
            }
        });
        createGroup1();
    }


    /**
     * This method initializes cParamsReference
     */
    private void createCParamsReference() {
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.verticalAlignment = GridData.CENTER; // Generated
        cParamsReference = new Combo(group, SWT.NONE);
        cParamsReference.setLayoutData(gridData1); // Generated
        cParamsReference.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                listener.setReference(cParamsReference.getText());
            }
        });
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData14 = new GridData();
        gridData14.horizontalAlignment = GridData.FILL; // Generated
        gridData14.horizontalSpan = 2; // Generated
        gridData14.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData13 = new GridData();
        GridData gridData12 = new GridData();
        gridData12.horizontalSpan = 4; // Generated
        gridData12.horizontalAlignment = GridData.FILL; // Generated
        gridData12.verticalAlignment = GridData.FILL; // Generated
        gridData12.grabExcessHorizontalSpace = true; // Generated
        gridData12.grabExcessVerticalSpace = true; // Generated
        gridData12.verticalSpan = 3; // Generated
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.FILL; // Generated
        gridData11.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData10 = new GridData();
        gridData10.horizontalAlignment = GridData.FILL; // Generated
        gridData10.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData9 = new GridData();
        gridData9.horizontalAlignment = GridData.FILL; // Generated
        gridData9.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData8 = new GridData();
        gridData8.verticalSpan = 4; // Generated
        gridData8.verticalAlignment = GridData.BEGINNING; // Generated
        gridData8.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData7 = new GridData();
        gridData7.horizontalSpan = 5; // Generated
        gridData7.verticalAlignment = GridData.CENTER; // Generated
        gridData7.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        gridData5.grabExcessHorizontalSpace = true; // Generated
        gridData5.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData4 = new GridData();
        gridData4.verticalAlignment = GridData.CENTER; // Generated
        gridData4.grabExcessHorizontalSpace = true; // Generated
        gridData4.horizontalSpan = 3; // Generated
        gridData4.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData3 = new GridData();
        gridData3.verticalAlignment = GridData.CENTER; // Generated
        gridData3.grabExcessHorizontalSpace = true; // Generated
        gridData3.horizontalSpan = 3; // Generated
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5; // Generated
        GridData gridData2 = new GridData();
        gridData2.horizontalSpan = 5; // Generated
        gridData2.verticalAlignment = GridData.FILL; // Generated
        gridData2.grabExcessHorizontalSpace = true; // Generated
        gridData2.grabExcessVerticalSpace = true; // Generated
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        group1 = new Group(group, SWT.NONE);
        group1.setText("Parameter Values"); // Generated
        group1.setLayout(gridLayout1); // Generated
        group1.setLayoutData(gridData2); // Generated
        label2 = new Label(group1, SWT.NONE);
        label2.setText("Name:"); // Generated
        tName = new Text(group1, SWT.BORDER);
        tName.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tName.selectAll();
        	}
        });
        tName.setLayoutData(gridData3); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        bApply = new Button(group1, SWT.NONE);
        bApply.setText("Apply Param"); // Generated
        bApply.setLayoutData(gridData8); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyParam();
            }
        });
        label3 = new Label(group1, SWT.NONE);
        label3.setText("Default Value:"); // Generated
        tDefault = new Text(group1, SWT.BORDER);
        tDefault.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tDefault.selectAll();		
        	}
        });
        tDefault.setLayoutData(gridData4); // Generated
        label6 = new Label(group1, SWT.NONE);
        label6.setText("Reference:"); // Generated
        createCReference();
        tDefault.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        label4 = new Label(group1, SWT.NONE);
        label4.setText("Required:"); // Generated
        cRequired = new Button(group1, SWT.CHECK);
        cRequired.setLayoutData(gridData13); // Generated
        cRequired.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                setApplyStatus();
            }
        });
        label5 = new Label(group1, SWT.NONE);
        label5.setText("ID:"); // Generated
        tID = new Text(group1, SWT.BORDER);
        tID.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tID.selectAll();
        	}
        });
        tID.setLayoutData(gridData5); // Generated
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        bNotes = new Button(group1, SWT.NONE);
        bNotes.setEnabled(false);
        bNotes.setText("Note..."); // Generated
        bNotes.setLayoutData(gridData14); // Generated
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.param");
                if((listener.getParamElement() == null)|| ((listener.getParamElement() != null) && listener.getParamElement().getParentElement() == null)) {
                	 applyParam();
                }
                DocumentationForm.openNoteDialog(dom, listener.getParamElement(), "note", tip, true, !listener
                        .isNewParam(),"Parameter Note");
            }
        });
        label7 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label7.setText("Label"); // Generated
        label7.setLayoutData(gridData7); // Generated
        tParams = new Table(group1, SWT.BORDER);
        tParams.setHeaderVisible(true); // Generated
        tParams.setLayoutData(gridData12); // Generated
        tParams.setLinesVisible(true); // Generated
        TableColumn tableColumn = new TableColumn(tParams, SWT.NONE);
        tableColumn.setWidth(200); // Generated
        tableColumn.setText("Name"); // Generated
        TableColumn tableColumn21 = new TableColumn(tParams, SWT.NONE);
        tableColumn21.setWidth(150); // Generated
        tableColumn21.setText("Default"); // Generated
        TableColumn tableColumn3 = new TableColumn(tParams, SWT.NONE);
        TableColumn tableColumn2 = new TableColumn(tParams, SWT.NONE);
        TableColumn tableColumn1 = new TableColumn(tParams, SWT.NONE);
        tableColumn1.setWidth(120); // Generated
        tableColumn1.setText("ID"); // Generated
        tableColumn2.setWidth(120); // Generated
        tableColumn2.setText("Reference"); // Generated
        tableColumn3.setWidth(70); // Generated
        tableColumn3.setText("Required"); // Generated
        tParams.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tParams.getSelectionCount() > 0) {
                    if (listener.selectParam(tParams.getSelectionIndex())) {
                        bRemove.setEnabled(true);
                        setParamStatus(true);
                    }
                }
                bApply.setEnabled(false);
            }
        });
        bNew = new Button(group1, SWT.NONE);
        bNew.setText("New Param"); // Generated
        bNew.setLayoutData(gridData11); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewParam();
                setParamStatus(true);
                tParams.deselectAll();
            }
        });
        label8 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
        label8.setText("Label"); // Generated
        label8.setLayoutData(gridData10); // Generated
        bRemove = new Button(group1, SWT.NONE);
        bRemove.setText("Remove Param"); // Generated
        bRemove.setLayoutData(gridData9); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tParams.getSelectionCount() > 0) {
                    listener.removeParam(tParams.getSelectionIndex());
                    setParamStatus(false);
                    bApply.setEnabled(false);
                    bRemove.setEnabled(false);
                    tParams.deselectAll();
                    listener.fillParams(tParams);

                    DocumentationListener.setCheckbox(cParamsReference, listener.getReferences(listener.getID()),
                            listener.getReference());
                }
            }
        });
    }


    /**
     * This method initializes cReference
     */
    private void createCReference() {
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        gridData6.grabExcessHorizontalSpace = true; // Generated
        gridData6.verticalAlignment = GridData.CENTER; // Generated
        cReference = new Combo(group1, SWT.NONE);
        cReference.setLayoutData(gridData6); // Generated
        cReference.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setApplyStatus();
            }
        });
    }


    private void setParamStatus(boolean enabled) {
        tName.setEnabled(enabled);
        tDefault.setEnabled(enabled);
        tID.setEnabled(enabled);
        cRequired.setEnabled(enabled);
        cReference.setEnabled(enabled);
        bNotes.setEnabled(enabled);

        if (enabled) {
            tName.setText(listener.getParamName());
            tDefault.setText(listener.getParamDefaultValue());
            tID.setText(listener.getParamID());
            cRequired.setSelection(listener.getParamRequired());
            DocumentationListener.setCheckbox(cReference, listener.getReferences(listener.getParamID()), listener
                    .getParamReference());
            tName.setFocus();
        }
        bApply.setEnabled(false);
    }


    private void setApplyStatus() {
        bApply.setEnabled(tName.getText().length() > 0);
        bNotes.setEnabled(tName.getText().length() > 0);
        Utils.setBackground(tName, true);
        getShell().setDefaultButton(bApply);
    }


    private void applyParam() {
        listener.applyParam(tName.getText(), tDefault.getText(), tID.getText(), cReference.getText(), cRequired
                .getSelection());

        DocumentationListener.setCheckbox(cParamsReference, listener.getReferences(listener.getID()), listener
                .getReference());
        bApply.setEnabled(false);
        listener.fillParams(tParams);
        bRemove.setEnabled(tParams.getSelectionCount() > 0);
    }


    public void setToolTipText() {
        tParamsID.setToolTipText(Messages.getTooltip("doc.params.id"));
        cParamsReference.setToolTipText(Messages.getTooltip("doc.params.reference"));
        bParamsNotes.setToolTipText(Messages.getTooltip("doc.params.notes"));
        tName.setToolTipText(Messages.getTooltip("doc.params.param.name"));
        bApply.setToolTipText(Messages.getTooltip("doc.params.param.apply"));
        tDefault.setToolTipText(Messages.getTooltip("doc.params.param.default"));
        tID.setToolTipText(Messages.getTooltip("doc.params.param.id"));
        cRequired.setToolTipText(Messages.getTooltip("doc.params.param.required"));
        cReference.setToolTipText(Messages.getTooltip("doc.params.param.reference"));
        bNotes.setToolTipText(Messages.getTooltip("doc.params.param.notes"));
        tParams.setToolTipText(Messages.getTooltip("doc.params.param.table"));
        bNew.setToolTipText(Messages.getTooltip("doc.params.param.new"));
        bRemove.setToolTipText(Messages.getTooltip("doc.params.param.remove"));
    }


    public void apply() {
        if (isUnsaved())
            applyParam();
    }


    public boolean isUnsaved() {
        listener.checkParams();
        return bApply.isEnabled();
    }


    public void checkParams() {
        listener.checkParams();
    }

} // @jve:decl-index=0:visual-constraint="10,10"
