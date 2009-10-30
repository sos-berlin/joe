package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.DatePicker;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.ReleaseListener;

public class ReleaseForm extends Composite implements IUnsaved, IUpdateLanguage {
    
	
	private ReleaseListener listener     = null;

    private DocumentationDom dom          = null;

    private Group            group        = null;

    private Label            label        = null;

    private Label            label1       = null;

    private Text             tTitle       = null;

    private Text             tID          = null;

    private Label            label2       = null;

    private Label            label3       = null;

    //private Button           bNotes       = null;

    //private Button           bChanges     = null;

    private DatePicker       created      = null;

    private DatePicker       modified     = null;

    private NoteForm         fNote        = null;
    
    private NoteForm         fChanges     = null;
    
   // private  Text txtNotes = null;    
    
   // private Text txtChanges = null;

    public ReleaseForm(Composite parent, int style, DocumentationDom dom, Element release) {
        super(parent, style);
        
        this.dom = dom;
        listener = new ReleaseListener(dom, release);
        
        initialize();
        setToolTipText();

        
        //listener.fillReleases(tReleases);
                
    }


    private void initialize() {
        createGroup();
        setSize(new Point(801, 462));
        setLayout(new FillLayout());

        setReleaseStatus(false);
        //bApply.setEnabled(false);
        //bRemove.setEnabled(false);
        
        fNote.setParams(dom, listener.getRelease(), "note", true, true);
        fNote.setTitle("Note");
                
        fChanges.setParams(dom, listener.getRelease(), "changes", true, true);
        fChanges.setTitle("Changes");
        
        tID.setText(listener.getID());
        tTitle.setText(listener.getTitle());
        
        created.setISODate(listener.getCreated());
        modified.setISODate(listener.getModified());
        
        //txtNotes.setText(listener.getNote("note"));
        //txtChanges.setText(listener.getNote("changes"));
        
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4; // Generated
        group = new Group(this, SWT.NONE);
        group.setText("Releases"); // Generated
        group.setLayout(gridLayout); // Generated
        label1 = new Label(group, SWT.NONE);
        label1.setLayoutData(new GridData());
        label1.setText("ID:"); // Generated
        GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tID = new Text(group, SWT.BORDER);
        tID.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tID.selectAll();
        	}
        });
        tID.setLayoutData(gridData3); // Generated
        tID.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(tID, true);
               listener.setId(tID.getText());
            }
        });
        label2 = new Label(group, SWT.NONE);
        label2.setText("Created:"); // Generated
        createCreated();
        label = new Label(group, SWT.NONE);
        label.setLayoutData(new GridData());
        label.setText("Title:"); // Generated
        GridData gridData21 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tTitle = new Text(group, SWT.BORDER);
        tTitle.addFocusListener(new FocusAdapter() {
        	public void focusGained(final FocusEvent e) {
        		tTitle.selectAll();
        	}
        });
        tTitle.setLayoutData(gridData21); // Generated
        tTitle.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                Utils.setBackground(tTitle, false);
               listener.setTitle(tTitle.getText());
            }
        });
        label3 = new Label(group, SWT.NONE);
        label3.setLayoutData(new GridData());
        label3.setText("Modified:"); // Generated
        createModified();
        //Label filler = new Label(group, SWT.NONE);
        createComposite();
        createGroup1();
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData5 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
        gridData5.widthHint = 486;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5; // Generated

        final Composite composite = new Composite(group, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1));
        final GridLayout gridLayout = new GridLayout();
        composite.setLayout(gridLayout);

        fNote = new NoteForm(composite, SWT.NONE);       
        fNote.setTitle("Note");
        fNote.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        

        /*txtNotes = new Text(composite, SWT.BORDER);
        txtNotes.setEditable(false);
        txtNotes.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        bNotes = new Button(composite, SWT.NONE);
        bNotes.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        bNotes.setText("Note..."); // Generated
        bNotes.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.releases");
                //DocumentationForm.openNoteDialog(dom, listener.getRelease(), "note", tip, true, false,"Release Note", txtNotes);
                DocumentationForm.openNoteDialog(dom, listener.getRelease(), "note", tip, true, false,"Release Note");
            }
        });
*/
        /*txtChanges = new Text(composite, SWT.BORDER);
        txtChanges.setEditable(false);
        txtChanges.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        */
        fChanges = new NoteForm(composite, SWT.NONE);
        fChanges.setTitle("Changes");
        fChanges.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        
        
        /*bChanges = new Button(composite, SWT.NONE);
        bChanges.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        bChanges.setText("Changes..."); // Generated
        bChanges.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                String tip = Messages.getTooltip("doc.note.text.changes");
                DocumentationForm.openNoteDialog(dom, listener.getRelease(), "changes", tip, true, false ,"Changes");
            }
        });
        */
        //group1 = new Group(group, SWT.NONE);
        //group1.setText("Authors"); // Generated
        //group1.setLayoutData(gridData5); // Generated
        //group1.setLayout(gridLayout1); // Generated
    }


    /**
     * This method initializes composite
     */
    private void createComposite() {
    }


    /**
     * This method initializes created
     */
    private void createCreated() {
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL; // Generated
        gridData6.grabExcessHorizontalSpace = false; // Generated
        gridData6.verticalAlignment = GridData.CENTER; // Generated
        created = new DatePicker(group, SWT.NONE);
        created.setEditable(true);
        created.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
            	listener.setCreated(created.getISODate());
               
            }
        });
        created.setLayoutData(gridData6); // Generated
    }


    /**
     * This method initializes modified
     */
    private void createModified() {
        GridData gridData13 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        modified = new DatePicker(group, SWT.NONE);
        modified.setEditable(true);
        modified.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setModified(modified.getISODate());
            }
        });
        modified.setLayoutData(gridData13); // Generated
    }


    public void apply() {
        applyRelease();
    }


    public boolean isUnsaved() {
    	return false;
    }


    public void setToolTipText() {
        tTitle.setToolTipText(Messages.getTooltip("doc.releases.title"));
        created.setToolTipText(Messages.getTooltip("doc.releases.created"));
        tID.setToolTipText(Messages.getTooltip("doc.releases.id"));
        modified.setToolTipText(Messages.getTooltip("doc.releases.modified"));
       // bNotes.setToolTipText(Messages.getTooltip("doc.releases.notes"));
       // bChanges.setToolTipText(Messages.getTooltip("doc.releases.changes"));

    }


    private void setReleaseStatus(boolean enabled) {
        //bApply.setEnabled(false);
        //bNotes.setEnabled(enabled);
        //bChanges.setEnabled(enabled);

        if (enabled) {
            tTitle.setText(listener.getTitle());

            if (listener.getCreated().equals(""))
                created.setNow();
            else
                created.setISODate(listener.getCreated());
            if (listener.getModified().equals(""))
                modified.setNow();
            else
                modified.setISODate(listener.getModified());

            tID.setText(listener.getID());
            tID.setFocus();
        }
    }


    

   

    private void applyRelease() {
        //bApply.setEnabled(false);        
        //tID.setText("");
        //tTitle.setText("");
        listener.applyRelease(
        		tTitle.getText(), 
        		tID.getText(), 
        		created.getISODate(), 
        		modified.getISODate() 
        		);
        

        
    }

} // @jve:decl-index=0:visual-constraint="10,10"
