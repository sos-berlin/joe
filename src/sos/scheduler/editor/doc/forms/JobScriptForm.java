package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.JobScriptListener;

public class JobScriptForm extends Composite implements IUnsaved, IUpdateLanguage {
    private JobScriptListener listener   = null;

    private Button            cUseScript = null;

    private ScriptForm        scriptForm = null;

    private DocumentationDom  dom;

    private Element           job;


    public JobScriptForm(Composite parent, int style, DocumentationDom dom, Element job) {
        super(parent, style);
        initialize();
        setToolTipText();
        this.dom = dom;
        this.job = job;
        listener = new JobScriptListener(dom, job);
        cUseScript.setSelection(listener.isScript());
        if (listener.isScript())
            scriptForm.setParams(dom, job, Editor.DOC_SCRIPT);
        scriptForm.init(listener.isScript(), true);
    }


    private void initialize() {
        cUseScript = new Button(this, SWT.RADIO);
        cUseScript.setText("Use script (this will disable the process element and delete its content!)"); // Generated
        cUseScript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (cUseScript.getSelection() != listener.isScript()) {
                    if (cUseScript.getSelection())
                        listener.setScript();
                    scriptForm.setParams(dom, job, Editor.DOC_SCRIPT);
                    scriptForm.init(listener.isScript(), true);
                }
            }
        });
        createScriptForm();
        setSize(new Point(694, 407));
        setLayout(new GridLayout());
    }


    /**
     * This method initializes scriptForm
     */
    private void createScriptForm() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.grabExcessVerticalSpace = true; // Generated
        gridData.verticalAlignment = GridData.FILL; // Generated
        scriptForm = new ScriptForm(this, SWT.NONE);
        scriptForm.setLayoutData(gridData); // Generated
    }


    public void setToolTipText() {
        cUseScript.setToolTipText(Messages.getTooltip("doc.script.useScript"));
    }


    public void apply() {
        scriptForm.apply();
    }


    public boolean isUnsaved() {
        return scriptForm.isUnsaved();
    }

} // @jve:decl-index=0:visual-constraint="10,10"
