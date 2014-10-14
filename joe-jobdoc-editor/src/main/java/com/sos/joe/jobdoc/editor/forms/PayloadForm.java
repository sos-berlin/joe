package com.sos.joe.jobdoc.editor.forms;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_PayloadForm_DocNote;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_B_PayloadForm_PayloadNote;
import static com.sos.joe.globals.messages.SOSJOEMessageCodes.JOE_G_PayloadForm_Payload;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jdom.Element;

import com.sos.dialog.classes.SOSButton;
import com.sos.dialog.classes.SOSGroup;
import com.sos.joe.jobdoc.editor.listeners.PayloadListener;
import com.sos.joe.xml.jobdoc.DocumentationDom;
 
public class PayloadForm extends JobDocBaseForm<PayloadListener>  {
	private Element				parentElement	= null;
	private Group				group			= null;
	private ParamsForm			fParams			= null;
	private Button				bNotes			= null;
	private Button				bDocNotes		= null;

	public PayloadForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
		super(parent, style);
		this.dom = dom;
		this.parentElement = parentElement;
		initialize();
	}

	private void initialize() {
		createGroup();
		listener = new PayloadListener(dom, parentElement, fParams);
		fParams.setParams(dom, listener.getPayloadElement());
		setToolTipText();
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridLayout gridLayout = new GridLayout(2, false);
		group = JOE_G_PayloadForm_Payload.Control(new SOSGroup(this, SWT.NONE));
		group.setLayout(gridLayout);
		bNotes = new SOSButton(group, "JOE_B_PayloadForm_PayloadNote");
		bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//                String tip = Messages.getTooltip("doc.note.text.payload");
				String tip = "";
				//                DocumentationForm.openNoteDialog(dom, listener.getPayloadElement(), "note", tip, true,"Payload Note");
				DocumentationForm.openNoteDialog(dom, listener.getPayloadElement(), "note", tip, true, JOE_B_PayloadForm_PayloadNote.label());
			}
		});
		bDocNotes = JOE_B_PayloadForm_DocNote.Control(new Button(group, SWT.NONE));
		bDocNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//                String tip = Messages.getTooltip("doc.note.text.payload.document");
				String tip = "";
				//                DocumentationForm.openNoteDialog(dom, listener.getDocumentationElement(), "note", tip, true,"Payload Document Note");
				DocumentationForm.openNoteDialog(dom, listener.getDocumentationElement(), "note", tip, true, JOE_B_PayloadForm_DocNote.label());
			}
		});
		createFParams();
	}

	/**
	 * This method initializes fParams
	 */
	private void createFParams() {
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
		fParams = new ParamsForm(group, SWT.NONE);
		fParams.setLayoutData(gridData); // Generated
	}

	@Override
	public void setToolTipText() {
		//    	
	}

	@Override
	public void apply() {
		fParams.apply();
	}

	@Override
	public boolean isUnsaved() {
		listener.checkPayload();
		return fParams.isUnsaved();
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
