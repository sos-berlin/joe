package com.sos.joe.jobdoc.editor.forms;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_JobScriptForm_UseScript;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.jobdoc.editor.listeners.JobScriptListener;
import com.sos.joe.xml.jobdoc.DocumentationDom;

public class JobScriptForm extends JobDocBaseForm<JobScriptListener> {
	private JobScriptListener		listener	= null;
	private Button					cUseScript	= null;
	private ScriptForm				scriptForm	= null;
	private final DocumentationDom	dom;
	private final Element			job;

	public JobScriptForm(Composite parent, int style, DocumentationDom dom, Element job) {
		super(parent, style);
		initialize();
		this.dom = dom;
		this.job = job;
		listener = new JobScriptListener(dom, job);
		cUseScript.setSelection(listener.isScript());
		if (listener.isScript())
			scriptForm.setParams(dom, job, JOEConstants.DOC_SCRIPT);
		scriptForm.init(listener.isScript(), true);
	}

	private void initialize() {
		cUseScript = JOE_B_JobScriptForm_UseScript.Control(new Button(this, SWT.RADIO));
		cUseScript.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (cUseScript.getSelection() != listener.isScript()) {
					if (cUseScript.getSelection())
						listener.setScript();
					scriptForm.setParams(dom, job, JOEConstants.DOC_SCRIPT);
					scriptForm.init(listener.isScript(), true);
				}
			}
		});
		createScriptForm();
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



	@Override
	public void apply() {
		scriptForm.apply();
	}

	@Override
	public boolean isUnsaved() {
		if (scriptForm != null) {
			return scriptForm.isUnsaved();
		}
		return false;
	}

	@Override
	public void openBlank() {
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
} // @jve:decl-index=0:visual-constraint="10,10"
