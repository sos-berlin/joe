package sos.scheduler.editor.doc.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jdom.Element;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.doc.DocumentationDom;
import sos.scheduler.editor.doc.listeners.ReleasesListener;

public class ReleasesForm extends Composite implements IUnsaved, IUpdateLanguage {
	
	
    private ReleasesListener listener     = null;

    private DocumentationDom dom          = null;

    private Group            group        = null;

    private Table            tReleases    = null;

    private Button           bNew         = null;

    private Button           bRemove      = null;

    private DocumentationForm _gui        = null;


    public ReleasesForm(Composite parent, 
    		int style, 
    		DocumentationDom dom, 
    		Element parentElement,
    		DocumentationForm gui) {
        super(parent, style);
        listener = new ReleasesListener(dom, parentElement);
        _gui = gui;
        initialize();
        setToolTipText();

        this.dom = dom;
        
        listener.fillReleases(tReleases);
       // fNote.setTitle("HHallo");        
       // fNote.setParams(dom, listener.getRelease(), "note", true, !listener.isNewRelease());
    }


    private void initialize() {
        createGroup();
        setSize(new Point(801, 462));
        setLayout(new FillLayout());      
        bRemove.setEnabled(false);
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData2 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        GridData gridData1 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2; // Generated
        group = new Group(this, SWT.NONE);
        group.setText("Releases"); // Generated
        group.setLayout(gridLayout); // Generated
       
        //Label filler = new Label(group, SWT.NONE);
        createComposite();
        createGroup1();
        tReleases = new Table(group, SWT.FULL_SELECTION | SWT.BORDER);
        tReleases.setHeaderVisible(true); // Generated
        tReleases.setLayoutData(gridData); // Generated
        tReleases.setLinesVisible(true); // Generated
        tReleases.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tReleases.getSelectionCount() > 0) {
                    if (listener.selectRelease(tReleases.getSelectionIndex())) {                       
                        bRemove.setEnabled(true);
                    } else
                        tReleases.deselectAll();
                }
            }
        });
        TableColumn idTableColumn = new TableColumn(tReleases, SWT.NONE);
        idTableColumn.setWidth(250); // Generated
        idTableColumn.setText("ID"); // Generated
        TableColumn tableColumn5 = new TableColumn(tReleases, SWT.NONE);
        tableColumn5.setWidth(90); // Generated
        tableColumn5.setText("Title"); // Generated
        TableColumn tableColumn6 = new TableColumn(tReleases, SWT.NONE);
        tableColumn6.setWidth(90); // Generated
        tableColumn6.setText("Created"); // Generated
        TableColumn tableColumn1 = new TableColumn(tReleases, SWT.NONE);
        tableColumn1.setWidth(60); // Generated
        tableColumn1.setText("Modified"); // Generated
        bNew = new Button(group, SWT.NONE);
        bNew.setText("New Release"); // Generated
        bNew.setLayoutData(gridData1); // Generated
        bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newRelease();                                
                tReleases.deselectAll();
                try{
                listener.applyRelease("New Realease", 
                		String.valueOf((listener.getRelease().getParentElement().getChildren("release", dom.getNamespace()).size()))
                		, sos.util.SOSDate.getCurrentDateAsString("yyyy-mm-dd")
                		, sos.util.SOSDate.getCurrentDateAsString("yyyy-mm-dd"), 
                		null);
                listener.fillReleases(tReleases);
                _gui.updateReleases();
                } catch (Exception ex){
                	System.out.println(ex.toString());
                }
            }
        });
        bRemove = new Button(group, SWT.NONE);
        bRemove.setText("Remove Release"); // Generated
        bRemove.setLayoutData(gridData2); // Generated
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tReleases.getSelectionCount() > 0) {
                	listener.removeRelease(tReleases.getSelectionIndex());
                    //listener.removeRelease();
                    bRemove.setEnabled(false);
                    listener.fillReleases(tReleases);
                    //if(tReleases.getSelectionCount() > 0)
                    //	tReleases.remove(tReleases.getSelectionCount());
                    //tReleases.deselectAll();
                    Utils.setBackground(tReleases, true);
                    _gui.updateReleases();
                }
            }
        });
    }


    /**
     * This method initializes group1
     */
    private void createGroup1() {
        GridData gridData5 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
        gridData5.widthHint = 486;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 5; // Generated        
    }


    /**
     * This method initializes composite
     */
    private void createComposite() {
    }



    public void apply() {
        applyRelease();
    }

    public boolean isUnsaved() {
    	return false;
    }

    public void setToolTipText() {
        tReleases.setToolTipText(Messages.getTooltip("doc.releases.list"));
        bNew.setToolTipText(Messages.getTooltip("doc.releases.new"));
        bRemove.setToolTipText(Messages.getTooltip("doc.releases.remove"));

    }


    private void applyRelease() {
        listener.fillReleases(tReleases);
        bRemove.setEnabled(tReleases.getSelectionCount() > 0);
        Utils.setBackground(tReleases, true);

        
    }

} // @jve:decl-index=0:visual-constraint="10,10"
