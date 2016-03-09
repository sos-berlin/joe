package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ReleasesForm_NewRelease;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ReleasesForm_RemoveRelease;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ReleaseForm_Releases;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ReleasesForm_Created;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ReleasesForm_ID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ReleasesForm_Modified;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_TCl_ReleasesForm_Title;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Tbl_ReleasesForm_Releases;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.joe.jobdoc.editor.listeners.ReleasesListener;
import com.sos.joe.xml.Utils;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ReleasesForm extends JobDocBaseForm<ReleasesListener> {

    private Group group = null;
    private Table tReleases = null;
    private Button bNew = null;
    private Button bRemove = null;
    private DocumentationForm _gui = null;

    public ReleasesForm(Composite parent, int style, DocumentationDom dom, Element parentElement, DocumentationForm gui) {
        super(parent, style);
        listener = new ReleasesListener(dom, parentElement);
        _gui = gui;
        initialize();
        this.dom = dom;
        listener.fillReleases(tReleases);

    }

    private void initialize() {
        createGroup();
        // setSize(new Point(801, 462));
        // setLayout(new FillLayout());
        bRemove.setEnabled(false);
    }

    /** This method initializes group */
    private void createGroup() {
        GridData gridData2 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
        GridLayout gridLayout = new GridLayout(2, false);
        group = JOE_G_ReleaseForm_Releases.Control(new SOSGroup(this, SWT.NONE));
        group.setLayout(gridLayout); // Generated
        createComposite();
        createGroup1();
        tReleases = JOE_Tbl_ReleasesForm_Releases.Control(new Table(group, SWT.FULL_SELECTION | SWT.BORDER));
        tReleases.setHeaderVisible(true); // Generated
        tReleases.setLayoutData(gridData); // Generated
        tReleases.setLinesVisible(true); // Generated
        tReleases.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tReleases.getSelectionCount() > 0) {
                    if (listener.selectRelease(tReleases.getSelectionIndex())) {
                        bRemove.setEnabled(true);
                    } else
                        tReleases.deselectAll();
                }
            }
        });
        TableColumn idTableColumn = JOE_TCl_ReleasesForm_ID.Control(new TableColumn(tReleases, SWT.NONE));
        idTableColumn.setWidth(250); // Generated
        TableColumn tableColumn5 = JOE_TCl_ReleasesForm_Title.Control(new TableColumn(tReleases, SWT.NONE));
        tableColumn5.setWidth(90); // Generated
        TableColumn tableColumn6 = JOE_TCl_ReleasesForm_Created.Control(new TableColumn(tReleases, SWT.NONE));
        tableColumn6.setWidth(90); // Generated
        TableColumn tableColumn1 = JOE_TCl_ReleasesForm_Modified.Control(new TableColumn(tReleases, SWT.NONE));
        tableColumn1.setWidth(60);
        bNew = JOE_B_ReleasesForm_NewRelease.Control(new Button(group, SWT.NONE));
        bNew.setLayoutData(gridData1); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newRelease();
                tReleases.deselectAll();
                try {
                    // listener.applyRelease("New Realease",
                    // String.valueOf((listener.getRelease().getParentElement().getChildren("release",
                    // dom.getNamespace()).size()))
                    // , sos.util.SOSDate.getCurrentDateAsString("yyyy-mm-dd")
                    // , sos.util.SOSDate.getCurrentDateAsString("yyyy-mm-dd"),
                    // null);
                    listener.applyRelease(JOE_B_ReleasesForm_NewRelease.label(), String.valueOf((listener.getRelease().getParentElement().getChildren("release", dom.getNamespace()).size())), sos.util.SOSDate.getCurrentDateAsString("yyyy-mm-dd"), sos.util.SOSDate.getCurrentDateAsString("yyyy-mm-dd"), null);
                    listener.fillReleases(tReleases);
                    _gui.updateReleases();
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        });
        bRemove = JOE_B_ReleasesForm_RemoveRelease.Control(new Button(group, SWT.NONE));
        bRemove.setLayoutData(gridData2); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tReleases.getSelectionCount() > 0) {
                    listener.removeRelease(tReleases.getSelectionIndex());
                    // listener.removeRelease();
                    bRemove.setEnabled(false);
                    listener.fillReleases(tReleases);
                    // if(tReleases.getSelectionCount() > 0)
                    // tReleases.remove(tReleases.getSelectionCount());
                    // tReleases.deselectAll();
                    Utils.setBackground(tReleases, true);
                    _gui.updateReleases();
                }
            }
        });
    }

    /** This method initializes group1 */
    private void createGroup1() {
        GridData gridData5 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
        gridData5.widthHint = 486;
        // GridLayout gridLayout1 = new GridLayout(5, false);
    }

    /** This method initializes composite */
    private void createComposite() {
    }

    @Override
    public void apply() {
        applyRelease();
    }

    @Override
    public boolean isUnsaved() {
        return false;
    }

    private void applyRelease() {
        listener.fillReleases(tReleases);
        bRemove.setEnabled(tReleases.getSelectionCount() > 0);
        Utils.setBackground(tReleases, true);
    }

    @Override
    public void openBlank() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void applySetting() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean applyChanges() {
        // TODO Auto-generated method stub
        return false;
    }
} // @jve:decl-index=0:visual-constraint="10,10"
