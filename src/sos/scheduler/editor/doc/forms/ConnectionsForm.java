package sos.scheduler.editor.doc.forms;

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
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.IUpdateTree;
import sos.scheduler.editor.doc.listeners.ConnectionsListener;

public class ConnectionsForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
    private ConnectionsListener listener     = null;

    private IUpdateTree         treeHandler  = null;

    private DocumentationDom    dom          = null;

    private Group               group        = null;

    @SuppressWarnings("unused")
	private Label               label        = null;

    private Text                tName        = null;

    private Button              bNotes       = null;

    private Button              bApply       = null;

    private Label               label1       = null;

    private Button              bNew         = null;

    private Label               label2       = null;

    private Button              bRemove      = null;

    private Table               tConnections = null;


    public ConnectionsForm(Composite parent, int style, DocumentationDom dom, Element parentElement,
            IUpdateTree treeHandler) {
        super(parent, style);
        this.treeHandler = treeHandler;
        this.dom = dom;
        listener = new ConnectionsListener(dom, parentElement);
        initialize();
    }


    private void initialize() {
        createGroup();
        setSize(new Point(619, 458));
        setLayout(new FillLayout());

        setConnectionStatus(false);
        bRemove.setEnabled(false);
        fillTable();
        setToolTipText();
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData1 = new GridData();
        gridData1.horizontalSpan = 3; // Generated
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.verticalAlignment = GridData.FILL; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.grabExcessVerticalSpace = true; // Generated
        gridData1.verticalSpan = 3; // Generated
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        gridData6.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL; // Generated
        gridData5.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL; // Generated
        gridData4.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        gridData3.horizontalSpan = 4; // Generated
        gridData3.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        gridData2.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4; // Generated
        
        group = JOE_G_ConnectionsForm_Connections.Control(new Group(this, SWT.NONE));
        group.setLayout(gridLayout); // Generated
        
        label = JOE_L_Name.Control(new Label(group, SWT.NONE));
        
        tName = JOE_T_ConnectionsForm_Name.Control(new Text(group, SWT.BORDER));
        tName.setLayoutData(gridData); // Generated
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bApply.setEnabled(true);
                getShell().setDefaultButton(bApply);
            }
        });
        
        bNotes = JOE_B_ConnectionsForm_Notes.Control(new Button(group, SWT.NONE));
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                DocumentationForm.openNoteDialog(dom, listener.getConnectionElement(), "note", null, true, !listener
                        .isNewConnection(), JOE_B_ConnectionsForm_Notes.label());
            }
        });
        
        bApply = JOE_B_ConnectionsForm_Apply.Control(new Button(group, SWT.NONE));
        bApply.setLayoutData(gridData2); // Generated
        bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyConnection();
            }
        });
        
        label1 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
//        label1.setText("Label"); // Generated
        label1.setLayoutData(gridData3); // Generated
        
        tConnections = JOE_Tbl_ConnectionsForm_Connections.Control(new Table(group, SWT.BORDER));
        tConnections.setHeaderVisible(true); // Generated
        tConnections.setLayoutData(gridData1); // Generated
        tConnections.setLinesVisible(true); // Generated
        tConnections.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tConnections.getSelectionCount() > 0) {
                    listener.selectConnection(tConnections.getSelectionIndex());
                    setConnectionStatus(true);
                    bRemove.setEnabled(true);
                    bApply.setEnabled(false);
                }
            }
        });
        
        TableColumn tableColumn = JOE_TCl_ConnectionsForm_Name.Control(new TableColumn(tConnections, SWT.NONE));
        tableColumn.setWidth(400); // Generated
        
        bNew = JOE_B_ConnectionsForm_New.Control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData4); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setNewConnection();
                setConnectionStatus(true);
                bApply.setEnabled(true);
                bRemove.setEnabled(false);
                getShell().setDefaultButton(bApply);
            }
        });
        
        label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
//        label2.setText("Label"); // Generated
        label2.setLayoutData(gridData5); // Generated
        
        bRemove = JOE_B_ConnectionsForm_Remove.Control(new Button(group, SWT.NONE));
        bRemove.setLayoutData(gridData6); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tConnections.getSelectionCount() > 0) {
                    if (listener.removeConnection(tConnections.getSelectionIndex())) {
                        setConnectionStatus(false);
                        bRemove.setEnabled(false);
                        bApply.setEnabled(false);
                        fillTable();
                    }

                }
            }
        });
    }


    public void apply() {
        if (isUnsaved())
            applyConnection();
    }


    public boolean isUnsaved() {
        listener.checkSettings();
        return bApply.isEnabled();
    }


    public void setToolTipText() {
//
    }


    private void applyConnection() {
        listener.applyConnection(tName.getText());
        fillTable();
        bRemove.setEnabled(tConnections.getSelectionCount() > 0);
        bApply.setEnabled(false);
    }


    private void setConnectionStatus(boolean enabled) {
        tName.setEnabled(enabled);
        bNotes.setEnabled(enabled);
        if (enabled) {
            tName.setText(listener.getName());
            tName.setFocus();
        }
        bApply.setEnabled(false);
    }


    private void fillTable() {
        listener.fillConnections(tConnections);
        if (treeHandler != null)
            treeHandler.fillConnections();
    }

} // @jve:decl-index=0:visual-constraint="10,10"
