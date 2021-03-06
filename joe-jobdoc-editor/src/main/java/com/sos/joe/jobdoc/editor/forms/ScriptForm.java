package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ScriptForm_JavaRB;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ScriptForm_JavaScriptRB;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ScriptForm_NoneRB;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ScriptForm_PerlScriptRB;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ScriptForm_ShellRB;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_ScriptForm_VBScriptRB;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_Cbo_ScriptForm_Resource;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_ScriptForm_Script;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ScriptForm_JavaClass;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ScriptForm_Language;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_L_ScriptForm_ResourceID;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_T_ScriptForm_JavaClass;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import com.sos.dialog.classes.SOSGroup;
import com.sos.dialog.classes.SOSLabel;
import com.sos.joe.jobdoc.editor.listeners.DocumentationListener;
import com.sos.joe.jobdoc.editor.listeners.ScriptListener;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class ScriptForm extends JobDocBaseForm<ScriptListener> {

    private ScriptListener listener = null;
    private Group group;
    private Label label = null;
    private Composite composite;
    private Button rbJava = null;
    private Button rbJavascript = null;
    private Button rbPerlscript = null;
    private Button rbVBScript = null;
    private Label label1 = null;
    private Text tJavaClass = null;
    private Label label3 = null;
    private Combo cResource = null;
    private IncludeFilesForm includeFilesForm = null;
    private Button rbShell = null;
    private Button rbNone = null;

    public ScriptForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    public void setParams(DocumentationDom dom, Element parent, int type) {
        listener = new ScriptListener(dom, parent, type);
        includeFilesForm.setParams(dom, listener.getScript());
    }

    public void setTitle(String title) {
        group.setText(title);
    }

    public void setScriptNone(boolean enable) {
        rbNone.setVisible(enable);
    }

    private void initialize() {
        createGroup();
        includeFilesForm.setSeparator(label3.getText());
    }

    private void createGroup() {
        GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData1.horizontalIndent = 7;
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalIndent = 7;
        GridLayout gl_group = new GridLayout(2, false);
        group = JOE_G_ScriptForm_Script.Control(new SOSGroup(this, SWT.NONE));
        group.setLayout(gl_group);
        label = JOE_L_ScriptForm_Language.control(new SOSLabel(group, SWT.NONE));
        createComposite();
        label1 = JOE_L_ScriptForm_JavaClass.control(new SOSLabel(group, SWT.NONE));
        tJavaClass = JOE_T_ScriptForm_JavaClass.control(new Text(group, SWT.BORDER));
        tJavaClass.setLayoutData(gridData);
        tJavaClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            @Override
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setJavaClass(tJavaClass.getText());
            }
        });
        label3 = JOE_L_ScriptForm_ResourceID.control(new SOSLabel(group, SWT.NONE));
        createCResource();
        createIncludeFilesForm();
    }

    private void createComposite() {
        GridLayout gl_composite = new GridLayout(7, false);
        composite = new Composite(group, SWT.NONE);
        composite.setLayout(gl_composite);
        rbJava = JOE_B_ScriptForm_JavaRB.control(new Button(composite, SWT.RADIO));
        rbJava.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbJava.getSelection()) {
                    listener.setLanguage(ScriptListener.JAVA);
                    fillForm();
                }
            }
        });
        rbJavascript = JOE_B_ScriptForm_JavaScriptRB.control(new Button(composite, SWT.RADIO));
        rbJavascript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbJavascript.getSelection()) {
                    listener.setLanguage(ScriptListener.JAVA_SCRIPT);
                    fillForm();
                }
            }
        });
        rbPerlscript = JOE_B_ScriptForm_PerlScriptRB.control(new Button(composite, SWT.RADIO));
        rbPerlscript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbPerlscript.getSelection()) {
                    listener.setLanguage(ScriptListener.PERL);
                    fillForm();
                }
            }
        });
        rbVBScript = JOE_B_ScriptForm_VBScriptRB.control(new Button(composite, SWT.RADIO));
        rbVBScript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbVBScript.getSelection()) {
                    listener.setLanguage(ScriptListener.VB_SCRIPT);
                    fillForm();
                }
            }
        });
        rbShell = JOE_B_ScriptForm_ShellRB.control(new Button(composite, SWT.RADIO));
        rbShell.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbShell.getSelection()) {
                    listener.setLanguage(ScriptListener.SHELL);
                    fillForm();
                }
            }
        });
        rbNone = JOE_B_ScriptForm_NoneRB.control(new Button(composite, SWT.RADIO));
        rbNone.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbNone.getSelection()) {
                    listener.setLanguage(ScriptListener.NONE);
                    fillForm();
                }
            }
        });
    }

    private void createCResource() {
        GridData gridData2 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData2.horizontalIndent = 7;
        cResource = JOE_Cbo_ScriptForm_Resource.control(new Combo(group, SWT.NONE));
        cResource.setLayoutData(gridData2);
        cResource.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                listener.setResource(cResource.getText());
            }
        });
    }

    private void createIncludeFilesForm() {
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        GridData gridData3 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
        includeFilesForm = new IncludeFilesForm(group, SWT.NONE);
        includeFilesForm.setLayoutData(gridData3);
    }

    @Override
    public void apply() {
        includeFilesForm.apply();
        if (listener != null) {
            listener.checkScript();
        }
    }

    @Override
    public boolean isUnsaved() {
        boolean status = includeFilesForm.isUnsaved();
        if (listener != null) {
            listener.checkScript();
        }
        return status;
    }

    public void init(boolean enabled, boolean btnNoneVisible) {
        rbNone.setVisible(btnNoneVisible);
        rbJava.setEnabled(enabled);
        rbJavascript.setEnabled(enabled);
        rbPerlscript.setEnabled(enabled);
        rbVBScript.setEnabled(enabled);
        rbShell.setEnabled(enabled);
        rbNone.setEnabled(enabled);
        tJavaClass.setEnabled(enabled);
        cResource.setEnabled(enabled);
        includeFilesForm.setEnabled(enabled);
        if (enabled) {
            fillForm();
        }
    }

    private void fillForm() {
        DocumentationListener.setCheckbox(cResource, listener.getResources(null), listener.getResource());
        int language = listener.getLanguage();
        tJavaClass.setEnabled(false);
        switch (language) {
        case ScriptListener.NONE:
            rbNone.setSelection(true);
            break;
        case ScriptListener.JAVA:
            rbJava.setSelection(true);
            tJavaClass.setEnabled(true);
            tJavaClass.setFocus();
            if (!"".equals(tJavaClass.getText()) && "".equals(listener.getJavaClass())) {
                listener.setJavaClass(tJavaClass.getText());
            }
            tJavaClass.setText(listener.getJavaClass());
            break;
        case ScriptListener.JAVA_SCRIPT:
            rbJavascript.setSelection(true);
            break;
        case ScriptListener.PERL:
            rbPerlscript.setSelection(true);
            break;
        case ScriptListener.VB_SCRIPT:
            rbVBScript.setSelection(true);
            break;
        case ScriptListener.SHELL:
            rbShell.setSelection(true);
            break;
        }
    }

    @Override
    public void openBlank() {
        //
    }

    @Override
    protected void applySetting() {
        //
    }

    @Override
    public boolean applyChanges() {
        //
        return false;
    }

}