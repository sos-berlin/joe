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

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.ResourcesListener;

public class ResourcesForm extends Composite implements IUnsaved, IUpdateLanguage {
    private ResourcesListener listener     = null;

    private DocumentationDom  dom          = null;

    private Group             group        = null;

    private Button            cMemory      = null;

    private Button            cSpace       = null;

    private Group             group1       = null;

    private Group             group2       = null;

    private Label             label        = null;

    private Label             label1       = null;

    private Text              tMemory      = null;

    private Combo             cbMemory     = null;

    private Combo             cbSpace      = null;

    private Text              tSpace       = null;

    private Label             label2       = null;

    private Label             label3       = null;

    private Button            bMemoryNotes = null;

    private Button            bSpaceNotes  = null;


    public ResourcesForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
        super(parent, style);
        this.dom = dom;
        listener = new ResourcesListener(dom, parentElement);
        initialize();
        setToolTipText();
    }


    private void initialize() {
        createGroup();
        setSize(new Point(726, 459));
        setLayout(new FillLayout());

        cbMemory.setItems(listener.getUnits());
        cbSpace.setItems(listener.getUnits());
        setSpaceStatus(listener.isSpace());
        setMemoryStatus(listener.isMemory());

    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.CENTER; // Generated
        gridData3.verticalAlignment = GridData.BEGINNING; // Generated
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.CENTER; // Generated
        gridData2.verticalAlignment = GridData.BEGINNING; // Generated
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2; // Generated
        group = new Group(this, SWT.NONE);
        group.setText("Resources"); // Generated
        group.setLayout(gridLayout); // Generated
        cMemory = new Button(group, SWT.CHECK);
        cMemory.setLayoutData(gridData2); // Generated
        cMemory.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setMemory(cMemory.getSelection());
                setMemoryStatus(cMemory.getSelection());
            }
        });
        createGroup1();
        cSpace = new Button(group, SWT.CHECK);
        cSpace.setLayoutData(gridData3); // Generated
        cSpace.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setSpace(cSpace.getSelection());
                setSpaceStatus(cSpace.getSelection());
            }
        });
        createGroup2();
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.FILL; // Generated
        gridData11.grabExcessHorizontalSpace = true; // Generated
        gridData11.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData10 = new GridData();
        gridData10.widthHint = 150; // Generated
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5; // Generated
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.verticalAlignment = GridData.CENTER; // Generated
        gridData.horizontalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.grabExcessVerticalSpace = false; // Generated
        gridData.verticalAlignment = GridData.FILL; // Generated
        group1 = new Group(group, SWT.NONE);
        group1.setLayoutData(gridData); // Generated
        group1.setLayout(gridLayout1); // Generated
        group1.setText("Memory"); // Generated
        label = new Label(group1, SWT.NONE);
        label.setText("Minimum:"); // Generated
        tMemory = new Text(group1, SWT.BORDER);
        tMemory.setLayoutData(gridData11); // Generated
        tMemory.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setMemory(tMemory.getText());
                Utils.setBackground(tMemory, cMemory.getSelection());
            }
        });
        tMemory.addVerifyListener(new org.eclipse.swt.events.VerifyListener() {
            public void verifyText(org.eclipse.swt.events.VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        label2 = new Label(group1, SWT.NONE);
        label2.setText("Unit:"); // Generated
        createCbMemory();
        bMemoryNotes = new Button(group1, SWT.NONE);
        bMemoryNotes.setText("Memory Notes..."); // Generated
        bMemoryNotes.setLayoutData(gridData10); // Generated
        bMemoryNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.memory");
                DocumentationForm.openNoteDialog(dom, listener.getMemoryElement(), "note", tip, true);
            }
        });
    }


    /**
     * This method initializes group2
     */
    private void createGroup2() {
        GridData gridData6 = new GridData();
        gridData6.widthHint = 150; // Generated
        GridData gridData9 = new GridData();
        gridData9.horizontalAlignment = GridData.FILL; // Generated
        gridData9.grabExcessHorizontalSpace = true; // Generated
        gridData9.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 5; // Generated
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.verticalAlignment = GridData.CENTER; // Generated
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.grabExcessVerticalSpace = false; // Generated
        gridData1.verticalAlignment = GridData.FILL; // Generated
        group2 = new Group(group, SWT.NONE);
        group2.setLayoutData(gridData1); // Generated
        group2.setLayout(gridLayout2); // Generated
        group2.setText("Space"); // Generated
        label1 = new Label(group2, SWT.NONE);
        label1.setText("Minimum:"); // Generated
        tSpace = new Text(group2, SWT.BORDER);
        tSpace.setLayoutData(gridData9); // Generated
        tSpace.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setSpace(tSpace.getText());
                Utils.setBackground(tSpace, cSpace.getSelection());
            }
        });
        tSpace.addVerifyListener(new org.eclipse.swt.events.VerifyListener() {
            public void verifyText(org.eclipse.swt.events.VerifyEvent e) {
                e.doit = Utils.isOnlyDigits(e.text);
            }
        });
        label3 = new Label(group2, SWT.NONE);
        label3.setText("Unit:"); // Generated
        createCbSpace();
        bSpaceNotes = new Button(group2, SWT.NONE);
        bSpaceNotes.setText("Space Notes..."); // Generated
        bSpaceNotes.setLayoutData(gridData6); // Generated
        bSpaceNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.space");
                DocumentationForm.openNoteDialog(dom, listener.getSpaceElement(), "note", tip, true);
            }
        });
    }


    /**
     * This method initializes cbMemory
     */
    private void createCbMemory() {
        GridData gridData4 = new GridData();
        gridData4.widthHint = 90; // Generated
        gridData4.widthHint = 90; // Generated
        cbMemory = new Combo(group1, SWT.NONE);
        cbMemory.setLayoutData(gridData4); // Generated
        cbMemory.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setMemoryUnit(cbMemory.getText());
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }


    /**
     * This method initializes cbSpace
     */
    private void createCbSpace() {
        GridData gridData5 = new GridData();
        gridData5.widthHint = 90; // Generated
        gridData5.widthHint = 90; // Generated
        cbSpace = new Combo(group2, SWT.NONE);
        cbSpace.setLayoutData(gridData5); // Generated
        cbSpace.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.setSpaceUnit(cbSpace.getText());
            }


            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }


    public void apply() {
    }


    public boolean isUnsaved() {
        return false;
    }


    public void setToolTipText() {
        cMemory.setToolTipText(Messages.getTooltip("doc.resources.memory.useit"));
        tMemory.setToolTipText(Messages.getTooltip("doc.resources.memory.minimum"));
        cbMemory.setToolTipText(Messages.getTooltip("doc.resources.unit"));
        bMemoryNotes.setToolTipText(Messages.getTooltip("doc.resources.memory.notes"));
        cSpace.setToolTipText(Messages.getTooltip("doc.resources.space.useit"));
        tSpace.setToolTipText(Messages.getTooltip("doc.resources.space.minimum"));
        cbSpace.setToolTipText(Messages.getTooltip("doc.resources.unit"));
        bSpaceNotes.setToolTipText(Messages.getTooltip("doc.resources.space.notes"));
    }


    private void setMemoryStatus(boolean enabled) {
        tMemory.setEnabled(enabled);
        cbMemory.setEnabled(enabled);
        bMemoryNotes.setEnabled(enabled);
        if (enabled) {
            if (tMemory.getText().length() > 0)
                listener.setMemory(tMemory.getText());
            tMemory.setText(listener.getMemory());
            cbMemory.select(cbMemory.indexOf(listener.getMemoryUnit()));
            cMemory.setSelection(true);
        }
        Utils.setBackground(tMemory, enabled);
        tMemory.setFocus();
    }


    private void setSpaceStatus(boolean enabled) {
        tSpace.setEnabled(enabled);
        cbSpace.setEnabled(enabled);
        bSpaceNotes.setEnabled(enabled);
        if (enabled) {
            if (tSpace.getText().length() > 0)
                listener.setSpace(tSpace.getText());
            tSpace.setText(listener.getSpace());
            cbSpace.select(cbSpace.indexOf(listener.getSpaceUnit()));
            cSpace.setSelection(true);
        }
        Utils.setBackground(tSpace, enabled);
        tSpace.setFocus();
    }

} // @jve:decl-index=0:visual-constraint="10,10"
