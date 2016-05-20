package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_IncludeFilesForm_Add;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_IncludeFilesForm_Remove;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_IncludeFilesForm_IncludeFiles;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_IncludeFilesForm_File;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_IncludeFilesForm_Parameter;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Lst_IncludeFilesForm_Files;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_IncludeFilesForm_File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.jobdoc.editor.listeners.IncludeFilesListener;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class IncludeFilesForm extends JobDocBaseForm<IncludeFilesListener> {

    private Group group = null;
    private Label label = null;
    private Text tFile = null;
    private Button bAdd = null;
    private Label label1 = null;
    private Label label51 = null;
    private List fileList = null;
    private Button bRemove = null;

    public IncludeFilesForm(Composite parent, int style) {
        super(parent, style);
        initialize();
        bAdd.setEnabled(false);
        bRemove.setEnabled(false);
    }

    public void setParams(DocumentationDom dom, Element parent) {
        listener = new IncludeFilesListener(dom, parent);
        fileList.setItems(listener.getIncludes());
    }

    private void initialize() {
        createGroup();
    }

    public void setSeparator(String separator) {
        label51.setText(separator);
    }

    private void createGroup() {
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL;
        gridData4.verticalAlignment = GridData.CENTER;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.verticalAlignment = GridData.BEGINNING;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.verticalAlignment = GridData.FILL;
        GridData gridData1 = new GridData();
        gridData1.horizontalSpan = 3;
        gridData1.verticalAlignment = GridData.CENTER;
        gridData1.horizontalAlignment = GridData.FILL;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        group = JOE_G_IncludeFilesForm_IncludeFiles.Control(new SOSGroup(this, SWT.NONE));
        group.setLayout(gridLayout);
        label = JOE_L_IncludeFilesForm_File.control(new SOSLabel(group, SWT.NONE));
        tFile = JOE_T_IncludeFilesForm_File.control(new Text(group, SWT.BORDER));
        tFile.setLayoutData(gridData);
        tFile.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                bAdd.setEnabled(!tFile.getText().isEmpty());
                getShell().setDefaultButton(bAdd);
            }
        });
        bAdd = JOE_B_IncludeFilesForm_Add.control(new Button(group, SWT.NONE));
        bAdd.setLayoutData(gridData4);
        bAdd.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addFile();
            }
        });
        label1 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label1.setText("Label");
        label1.setLayoutData(gridData1);
        label51 = JOE_L_IncludeFilesForm_Parameter.control(new SOSLabel(group, SWT.NONE));
        label51.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        label51.setVisible(false);
        fileList = JOE_Lst_IncludeFilesForm_Files.control(new List(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL));
        fileList.setLayoutData(gridData2);
        fileList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                boolean selection = fileList.getSelectionIndex() >= 0;
                if (selection) {
                    tFile.setText(fileList.getItem(fileList.getSelectionIndex()));
                }
                bAdd.setEnabled(false);
                bRemove.setEnabled(selection);
            }
        });
        bRemove = JOE_B_IncludeFilesForm_Remove.control(new Button(group, SWT.NONE));
        bRemove.setLayoutData(gridData3);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                removeFile();
            }
        });
    }

    @Override
    public void apply() {
        addFile();
        if (listener != null) {
            listener.saveIncludes(fileList.getItems());
        }
    }

    @Override
    public boolean isUnsaved() {
        if (listener != null) {
            listener.saveIncludes(fileList.getItems());
        }
        return bAdd.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        tFile.setEnabled(enabled);
        bAdd.setEnabled(false);
        fileList.setEnabled(enabled);
        bRemove.setEnabled(false);
    }

    private void addFile() {
        if (!tFile.getText().isEmpty() && !listener.exists(tFile.getText(), fileList.getItems())) {
            fileList.add(tFile.getText());
            tFile.setText("");
            bAdd.setEnabled(false);
            fileList.deselectAll();
            bRemove.setEnabled(false);
            listener.setChanges(true);
        }
    }

    private void removeFile() {
        if (fileList.getSelectionIndex() >= 0) {
            fileList.remove(fileList.getSelectionIndex());
            fileList.deselectAll();
            tFile.setText("");
            bRemove.setEnabled(false);
            listener.setChanges(true);
        }
    }

    @Override
    public void openBlank() {
        //
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

}