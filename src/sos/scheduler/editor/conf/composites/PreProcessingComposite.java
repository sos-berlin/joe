package sos.scheduler.editor.conf.composites;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
 import org.eclipse.swt.widgets.Text;

 
import sos.scheduler.editor.app.ContextMenu;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.util.SOSString;

public class PreProcessingComposite extends Composite {
    @SuppressWarnings("unused")
    private final String conSVNVersion = "$Id$";

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(PreProcessingComposite.class);
    @SuppressWarnings("unused")
    private final String conClassName = "JobMainForm";

    private JobListener objDataProvider = null;

    private Button              butFavorite         = null;
    private String              groupTitle          = "Script";
    private Group               gMain;
    private Text                txtName             = null;
    private Spinner             spinner             = null;
    private boolean             init                = false;
    private Combo               cboFavorite         = null;
 

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public PreProcessingComposite(Group parent, int style,JobListener objDataProvider_) {
        super(parent, style);
        objDataProvider = objDataProvider_;
        createGroup(parent);

    }
    
   
    private void createGroup(final Group parent) {
 
        final Display display = this.getDisplay();
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 13;
        gMain = new Group(this, SWT.NONE);
        gMain.setText(groupTitle);
        gMain.setLayout(gridLayout);

       
            final Composite scriptcom = new Composite(gMain, SWT.NONE);
            scriptcom.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 13, 1));
            final GridLayout gridLayout_1 = new GridLayout();
            gridLayout_1.verticalSpacing = 0;
            gridLayout_1.marginWidth = 0;
            gridLayout_1.marginHeight = 0;
            gridLayout_1.horizontalSpacing = 0;
            gridLayout_1.numColumns = 13;
            scriptcom.setLayout(gridLayout_1);

            final Label nameLabel = new Label(scriptcom, SWT.NONE);
            GridData gd_nameLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
            gd_nameLabel.widthHint = 60;
            nameLabel.setLayoutData(gd_nameLabel);
            nameLabel.setText("Name: ");

            txtName = new Text(scriptcom, SWT.BORDER);
            txtName.addFocusListener(new FocusAdapter() {
                public void focusGained(final FocusEvent e) {
                    txtName.selectAll();
                }
            });
            GridData gd_txtName = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gd_txtName.widthHint = 135;
            txtName.setLayoutData(gd_txtName);
            txtName.addModifyListener(new ModifyListener() {
                public void modifyText(final ModifyEvent e) {
                    if (!init)
                        objDataProvider.setMonitorName(txtName.getText());
                }
            });

            final Label orderingLabel = new Label(scriptcom, SWT.NONE);
            orderingLabel.setText("  Ordering: ");

            spinner = new Spinner(scriptcom, SWT.BORDER);
            GridData gd_spinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
            gd_spinner.widthHint = 106;
            spinner.setLayoutData(gd_spinner);
            spinner.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(final SelectionEvent e) {
                    if (!init)
                        objDataProvider.setOrdering(String.valueOf(spinner.getSelection()));
                }
            });
            spinner.setSelection(-1);
            spinner.setMaximum(999);

     
       
        new Label(gMain, SWT.NONE);

        butFavorite = new Button(gMain, SWT.NONE);

        
        butFavorite.setEnabled(true);
        butFavorite.setText("Favorites");

        cboFavorite = new Combo(gMain, SWT.NONE);
        GridData gd_cboFavorite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
        gd_cboFavorite.widthHint = 153;
        cboFavorite.setLayoutData(gd_cboFavorite);
   
    }

    public void init(){
        init = true;
        int language = objDataProvider.getLanguage();
        if (language < 0){
            language = 0;
        }
             
        txtName.setText(objDataProvider.getMonitorName());
        spinner.setSelection((objDataProvider.getOrdering().length() == 0 ? 0 : Integer.parseInt(objDataProvider.getOrdering())));

   
      
      
        init = false;
    }
     

    @Override protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    public Group getgMain() {
        return gMain;
    }

    public Combo getCboFavorite() {
        return cboFavorite;
    }


    public Button getButFavorite() {
        return butFavorite;
    }


    public Text getTxtName() {
        return txtName;
    }
 

}
