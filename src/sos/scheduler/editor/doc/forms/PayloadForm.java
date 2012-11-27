package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jdom.Element;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.PayloadListener;

public class PayloadForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
    private PayloadListener  listener      = null;

    private DocumentationDom dom           = null;

    private Element          parentElement = null;

    private Group            group         = null;

    private ParamsForm       fParams       = null;

    private Button           bNotes        = null;

    private Button           bDocNotes     = null;


    public PayloadForm(Composite parent, int style, DocumentationDom dom, Element parentElement) {
        super(parent, style);

        this.dom = dom;
        this.parentElement = parentElement;
        initialize();
    }


    private void initialize() {
        createGroup();
        setSize(new Point(651, 431));
        setLayout(new FillLayout());

        listener = new PayloadListener(dom, parentElement, fParams);
        fParams.setParams(dom, listener.getPayloadElement());
        setToolTipText();
    }


    /**
     * This method initializes group
     */
    private void createGroup() { //TODO i18n
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2; // Generated
        group = new Group(this, SWT.NONE);
        bNotes = new Button(group, SWT.NONE);
        bNotes.setText("Payload Note..."); // Generated
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.payload");
                DocumentationForm.openNoteDialog(dom, listener.getPayloadElement(), "note", tip, true,"Payload Note");
            }
        });
        bDocNotes = new Button(group, SWT.NONE);
        bDocNotes.setText("Payload Document Note..."); // Generated
        bDocNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.payload.document");
                DocumentationForm.openNoteDialog(dom, listener.getDocumentationElement(), "note", tip, true,"Payload Document Note");
            }
        });
        createFParams();
        group.setLayout(gridLayout); // Generated
        group.setText("Payload"); // Generated
    }


    /**
     * This method initializes fParams
     */
    private void createFParams() {
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2; // Generated
        gridData.verticalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.grabExcessVerticalSpace = true; // Generated
        gridData.horizontalAlignment = GridData.FILL; // Generated
        fParams = new ParamsForm(group, SWT.NONE);
        fParams.setLayoutData(gridData); // Generated
    }


    public void setToolTipText() {
        bNotes.setToolTipText(Messages.getTooltip("doc.payload.notes"));
        bDocNotes.setToolTipText(Messages.getTooltip("doc.payload.document"));
    }


    public void apply() {
        fParams.apply();
    }


    public boolean isUnsaved() {
        listener.checkPayload();
        return fParams.isUnsaved();
    }

} // @jve:decl-index=0:visual-constraint="10,10"
