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
import org.eclipse.swt.widgets.TreeItem;
import org.jdom.Element;

import com.sos.jobdoc.DocumentationDom;
import com.sos.jobdoc.IUpdateTree;
import com.sos.jobdoc.listeners.ApplicationsListener;
import com.sos.jobdoc.listeners.DocumentationListener;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;

public class ApplicationsForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
    private ApplicationsListener listener      = null;

    private IUpdateTree          treeHandler   = null;

    private TreeItem             tItem         = null;

    private Element              parentElement = null;

    private Group                group         = null;

    @SuppressWarnings("unused")
	private Label                label         = null;

    private Text                 tName         = null;

    @SuppressWarnings("unused")
	private Label                label1        = null;

    private Text                 tID           = null;

    @SuppressWarnings("unused")
	private Label                label2        = null;

    private Combo                cReference    = null;

    private Button               bApply        = null;

    private Label                label3        = null;

    private Table                table         = null;

    private Button               bNew          = null;

    private Label                label4        = null;

    private Button               bRemove       = null;


    public ApplicationsForm(Composite parent, int style, DocumentationDom dom, Element parentElement,
            IUpdateTree treeHandler, TreeItem tItem) {
        super(parent, style);
        this.treeHandler = treeHandler;
        this.tItem = tItem;
        this.parentElement = parentElement;
        listener = new ApplicationsListener(dom, parentElement);
        initialize();
    }


    private void initialize() {
        createGroup();
        setSize(new Point(670, 422));
        setLayout(new FillLayout());

        bApply.setEnabled(false);
        bRemove.setEnabled(false);
        setAppStatus(false);
        fillTable();
        setToolTipText();
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData8 = new GridData();
        gridData8.horizontalAlignment = GridData.FILL; // Generated
        gridData8.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData7 = new GridData();
        gridData7.horizontalAlignment = GridData.FILL; // Generated
        gridData7.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        gridData6.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL; // Generated
        gridData4.grabExcessHorizontalSpace = true; // Generated
        gridData4.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData3 = new GridData();
        gridData3.horizontalSpan = 5; // Generated
        gridData3.verticalAlignment = GridData.CENTER; // Generated
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData2 = new GridData();
        gridData2.verticalSpan = 2; // Generated
        gridData2.verticalAlignment = GridData.BEGINNING; // Generated
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        GridData gridData1 = new GridData();
        gridData1.horizontalSpan = 4; // Generated
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.verticalAlignment = GridData.FILL; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.grabExcessVerticalSpace = true; // Generated
        gridData1.verticalSpan = 3; // Generated
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3; // Generated
        gridData.verticalAlignment = GridData.CENTER; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.horizontalAlignment = GridData.FILL; // Generated
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5; // Generated
        
        group = JOE_G_ApplicationsForm_Applications.Control(new Group(this, SWT.NONE));
        group.setLayout(gridLayout); // Generated
        
        label = JOE_L_Name.Control(new Label(group, SWT.NONE));
        
        tName = JOE_T_ApplicationsForm_Name.Control(new Text(group, SWT.BORDER));
        tName.setLayoutData(gridData); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        
        bApply = JOE_B_ApplicationsForm_ApplyApplication.Control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData2); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyApp();
            }
        });
        
        label1 = JOE_L_ApplicationsForm_ID.Control(new Label(group, SWT.NONE));
        
        tID = JOE_T_ApplicationsForm_ID.Control(new Text(group, SWT.BORDER));
        tID.setLayoutData(gridData4); // Generated
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                setApplyStatus();
            }
        });
        
        label2 = JOE_L_ApplicationsForm_Reference.Control(new Label(group, SWT.NONE));
        
        createCReference();
        
        label3 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
//        label3.setText("Label"); // Generated
        label3.setLayoutData(gridData3); // Generated
        
        table = JOE_Tbl_ApplicationsForm_Applications.Control(new Table(group, SWT.BORDER));
        table.setHeaderVisible(true); // Generated
        table.setLayoutData(gridData1); // Generated
        table.setLinesVisible(true); // Generated
        table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    if (listener.selectApp(table.getSelectionIndex())) {
                        setAppStatus(true);
                        bRemove.setEnabled(true);
                    }
                }
            }
        });
        
        TableColumn tableColumn = JOE_TCl_ApplicationsForm_Name.Control(new TableColumn(table, SWT.NONE));
        tableColumn.setWidth(200); // Generated
        
        TableColumn tableColumn1 = JOE_TCl_ApplicationsForm_ID.Control(new TableColumn(table, SWT.NONE));
        tableColumn1.setWidth(180); // Generated
        
        TableColumn tableColumn2 = JOE_TCl_ApplicationsForm_Reference.Control(new TableColumn(table, SWT.NONE));
        tableColumn2.setWidth(180); // Generated
        
        bNew = JOE_B_ApplicationsForm_NewApplication.Control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData6); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewApp();
                setAppStatus(true);
                bRemove.setEnabled(false);
                table.deselectAll();
            }
        });
        
        label4 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
//        label4.setText("Label"); // Generated
        label4.setLayoutData(gridData7); // Generated
        
        bRemove = JOE_B_ApplicationsForm_RemoveApplication.Control(new Button(group, SWT.NONE));
        bRemove.setLayoutData(gridData8); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (table.getSelectionCount() > 0) {
                    if (listener.removeApp(table.getSelectionIndex())) {
                        setAppStatus(false);
                        fillTable();
                    }
                    bRemove.setEnabled(false);
                }
            }
        });
    }

    /**
     * This method initializes cReference
     */
    private void createCReference() {
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        gridData5.grabExcessHorizontalSpace = true; // Generated
        gridData5.verticalAlignment = GridData.CENTER; // Generated
        
        cReference = JOE_Cbo_ApplicationsForm_Reference.Control(new Combo(group, SWT.NONE));
        cReference.setLayoutData(gridData5); // Generated
        cReference.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setApplyStatus();
            }
        });
    }


    public void apply() {
        if (isUnsaved())
            applyApp();
    }


    public boolean isUnsaved() {
        return bApply.isEnabled();
    }


    public void setToolTipText() {
//
    }


    private void setAppStatus(boolean enabled) {
        tName.setEnabled(enabled);
        tID.setEnabled(enabled);
        cReference.setEnabled(enabled);
        if (enabled) {
            tName.setText(listener.getName());
            tID.setText(listener.getID());
            DocumentationListener.setCheckbox(cReference, listener.getReferences(listener.getID()), listener
                    .getReference());
            tName.setFocus();
            getShell().setDefaultButton(bApply);
        }
        bApply.setEnabled(false);
    }


    private void setApplyStatus() {
        bApply.setEnabled(tName.getText().length() > 0);
        Utils.setBackground(tName, true);
    }


    private void applyApp() {
        listener.applyApp(tName.getText(), tID.getText(), cReference.getText());
        bApply.setEnabled(false);
        fillTable();
        bRemove.setEnabled(table.getSelectionCount() > 0);
    }


    private void fillTable() {
        listener.fillApps(table);
        treeHandler.fillApplications(tItem, parentElement, true);
    }

} // @jve:decl-index=0:visual-constraint="10,10"
