package sos.scheduler.editor.doc.forms;

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
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.DocumentationListener;
import sos.scheduler.editor.doc.listeners.ScriptListener;

public class ScriptForm extends Composite implements IUnsaved, IUpdateLanguage {
    private ScriptListener   listener         = null;

    private Group            group            = null;

    private Label            label            = null;

    private Composite        composite        = null;

    private Button           rbJava           = null;

    private Button           rbJavascript     = null;

    private Button           rbPerlscript     = null;

    private Button           rbVBScript       = null;

    private Button           rbCom            = null;

    private Label            label1           = null;

    private Label            label2           = null;

    private Text             tJavaClass       = null;

    private Text             tComClass        = null;

    private Label            label3           = null;

    private Combo            cResource        = null;

    private IncludeFilesForm includeFilesForm = null;

    private Button           rbNone           = null;


    public ScriptForm(Composite parent, int style) {
        super(parent, style);
        initialize();
        setToolTipText();
    }


    public void setParams(DocumentationDom dom, Element parent, int type) {
        listener = new ScriptListener(dom, parent, type);
        includeFilesForm.setParams(dom, listener.getScript());
        //cResource.setItems(listener.getResources(null));
    }


    public void setTitle(String title) {
        group.setText(title);
    }


    public void setScriptNone(boolean enable) {
        rbNone.setVisible(enable);
    }


    private void initialize() {
        createGroup();
        setSize(new Point(743, 447));
        setLayout(new FillLayout());
        rbNone.setVisible(false);
        includeFilesForm.setSeparator(label3.getText());
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL; // Generated
        gridData1.grabExcessVerticalSpace = false; // Generated
        gridData1.grabExcessHorizontalSpace = true; // Generated
        gridData1.horizontalIndent = 7; // Generated
        gridData1.verticalAlignment = GridData.CENTER; // Generated
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.horizontalIndent = 7; // Generated
        gridData.verticalAlignment = GridData.CENTER; // Generated
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2; // Generated
        group = new Group(this, SWT.NONE);
        group.setText("Script"); // Generated
        group.setLayout(gridLayout); // Generated
        label = new Label(group, SWT.NONE);
        label.setText("Language:"); // Generated
        createComposite();
        label1 = new Label(group, SWT.NONE);
        label1.setText("Java Class:"); // Generated
        tJavaClass = new Text(group, SWT.BORDER);
        tJavaClass.setLayoutData(gridData); // Generated
        tJavaClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setJavaClass(tJavaClass.getText());
            }
        });
        label2 = new Label(group, SWT.NONE);
        label2.setText("Com Class:"); // Generated
        tComClass = new Text(group, SWT.BORDER);
        tComClass.setLayoutData(gridData1); // Generated
        tComClass.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setComClass(tComClass.getText());
            }
        });
        label3 = new Label(group, SWT.NONE);
        label3.setText("Resource ID:"); // Generated
        createCResource();
        createIncludeFilesForm();
    }


    /**
     * This method initializes composite
     */
    private void createComposite() {
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 6; // Generated
        composite = new Composite(group, SWT.NONE);
        composite.setLayout(gridLayout1); // Generated
        rbJava = new Button(composite, SWT.RADIO);
        rbJava.setText("Java"); // Generated
        rbJava.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbJava.getSelection()) {
                    listener.setLanguage(ScriptListener.JAVA);
                    fillForm();
                }
            }
        });
        rbCom = new Button(composite, SWT.RADIO);
        rbCom.setText("Com"); // Generated
        rbCom.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbCom.getSelection()) {
                    listener.setLanguage(ScriptListener.COM);
                    fillForm();
                }
            }
        });
        rbJavascript = new Button(composite, SWT.RADIO);
        rbJavascript.setText("Javascript"); // Generated
        rbJavascript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbJavascript.getSelection()) {
                    listener.setLanguage(ScriptListener.JAVA_SCRIPT);
                    fillForm();
                }
            }
        });
        rbPerlscript = new Button(composite, SWT.RADIO);
        rbPerlscript.setText("PerlScript"); // Generated
        rbPerlscript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbPerlscript.getSelection()) {
                    listener.setLanguage(ScriptListener.PERL);
                    fillForm();
                }
            }
        });
        rbVBScript = new Button(composite, SWT.RADIO);
        rbVBScript.setText("VBScript"); // Generated
        rbVBScript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbVBScript.getSelection()) {
                    listener.setLanguage(ScriptListener.VB_SCRIPT);
                    fillForm();
                }
            }
        });
        rbNone = new Button(composite, SWT.RADIO);
        rbNone.setText("None"); // Generated
        rbNone.setVisible(true); // Generated
        rbNone.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (rbNone.getSelection()) {
                    listener.setLanguage(ScriptListener.NONE);
                    fillForm();
                }
            }
        });
    }


    /**
     * This method initializes cResource
     */
    private void createCResource() {
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL; // Generated
        gridData2.horizontalIndent = 7; // Generated
        gridData2.verticalAlignment = GridData.CENTER; // Generated
        cResource = new Combo(group, SWT.NONE);
        cResource.setLayoutData(gridData2); // Generated
        cResource.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                listener.setResource(cResource.getText());
            }
        });
    }


    /**
     * This method initializes includeFilesForm
     */
    private void createIncludeFilesForm() {
        GridData gridData3 = new GridData();
        gridData3.horizontalSpan = 2; // Generated
        gridData3.verticalAlignment = GridData.FILL; // Generated
        gridData3.grabExcessHorizontalSpace = true; // Generated
        gridData3.grabExcessVerticalSpace = true; // Generated
        gridData3.horizontalAlignment = GridData.FILL; // Generated
        includeFilesForm = new IncludeFilesForm(group, SWT.NONE);
        includeFilesForm.setLayoutData(gridData3); // Generated
    }


    public void setToolTipText() {
        rbJava.setToolTipText(Messages.getTooltip("doc.script.java"));
        rbCom.setToolTipText(Messages.getTooltip("doc.script.com"));
        rbJavascript.setToolTipText(Messages.getTooltip("doc.script.javascript"));
        rbPerlscript.setToolTipText(Messages.getTooltip("doc.script.perlscript"));
        rbVBScript.setToolTipText(Messages.getTooltip("doc.script.vbscript"));
        rbNone.setToolTipText(Messages.getTooltip("doc.script.none"));
        tJavaClass.setToolTipText(Messages.getTooltip("doc.script.javaclass"));
        tComClass.setToolTipText(Messages.getTooltip("doc.script.comclass"));
        cResource.setToolTipText(Messages.getTooltip("doc.script.resource"));
    }


    public void apply() {
        includeFilesForm.apply();
        if (listener != null)
            listener.checkScript();
    }


    public boolean isUnsaved() {
        boolean status = includeFilesForm.isUnsaved();
        if (listener != null)
            listener.checkScript();
        return status;
    }


    public void init(boolean enabled, boolean btnNoneVisible) {
        rbNone.setVisible(btnNoneVisible);

        rbJava.setEnabled(enabled);
        rbCom.setEnabled(enabled);
        rbJavascript.setEnabled(enabled);
        rbPerlscript.setEnabled(enabled);
        rbVBScript.setEnabled(enabled);
        rbNone.setEnabled(enabled);
        tJavaClass.setEnabled(enabled);
        tComClass.setEnabled(enabled);
        cResource.setEnabled(enabled);
        includeFilesForm.setEnabled(enabled);

        if (enabled) {
            fillForm();
        }
    }


    private void fillForm() {
        DocumentationListener.setCheckbox(cResource, listener.getResources(null), listener.getResource());

        int language = listener.getLanguage();

        tComClass.setEnabled(false);
        tJavaClass.setEnabled(false);

        switch (language) {
            case ScriptListener.NONE:
                rbNone.setSelection(true);
                break;
            case ScriptListener.COM:
                rbCom.setSelection(true);
                tComClass.setEnabled(true);
                tComClass.setFocus();
                if (!tComClass.getText().equals("") && listener.getComClass().equals(""))
                    listener.setComClass(tComClass.getText());
                tComClass.setText(listener.getComClass());
                break;
            case ScriptListener.JAVA:
                rbJava.setSelection(true);
                tJavaClass.setEnabled(true);
                tJavaClass.setFocus();
                if (!tJavaClass.getText().equals("") && listener.getJavaClass().equals(""))
                    listener.setJavaClass(tJavaClass.getText());
                tJavaClass.setText(listener.getJavaClass());
                break;
            case ScriptListener.JAVA_SCRIPT:
                rbJavascript.setSelection(true);
                // tComClass.setFocus();
                break;
            case ScriptListener.PERL:
                rbPerlscript.setSelection(true);
                // tComClass.setFocus();
                break;
            case ScriptListener.VB_SCRIPT:
                rbVBScript.setSelection(true);
                // tComClass.setFocus();
                break;
        }

    }

} // @jve:decl-index=0:visual-constraint="10,10"
