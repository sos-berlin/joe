package sos.scheduler.editor.conf.composites;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.conf.listeners.JobListener;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.options.Options;

public class PreProcessingComposite extends SOSJOEMessageCodes {

    @SuppressWarnings("unused")
    private final String conSVNVersion = "$Id$";
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(PreProcessingComposite.class);
    @SuppressWarnings("unused")
    private final String conClassName = "PreProcessingComposite";
    private JobListener objDataProvider = null;
    private Button butFavorite = null;
    // private String groupTitle = "Script";
    private Group gMain;
    private Text txtName = null;
    private Spinner spinner = null;
    private boolean init = false;
    private Combo cboFavorite = null;

    /** Create the composite.
     * 
     * @param parent
     * @param style */
    public PreProcessingComposite(Group parent, int style, JobListener objDataProvider_) {
        super(parent, style);
        objDataProvider = objDataProvider_;
        createGroup(parent);
    }

    private void createGroup(final Group parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        gMain = JOE_G_PreProcessingComposite_Script.Control(new Group(this, SWT.NONE));
        gMain.setLayout(gridLayout);
        final Composite scriptcom = JOE_Cmp_PreProcessingComposite_NameOrdering.Control(new Composite(gMain, SWT.NONE));
        scriptcom.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.verticalSpacing = 0;
        gridLayout_1.marginWidth = 10;
        gridLayout_1.marginHeight = 0;
        gridLayout_1.horizontalSpacing = 10;
        gridLayout_1.numColumns = 4;
        scriptcom.setLayout(gridLayout_1);
        final Label nameLabel = JOE_L_Name.Control(new Label(scriptcom, SWT.NONE));
        GridData gd_nameLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gd_nameLabel.widthHint = 85;
        nameLabel.setLayoutData(gd_nameLabel);
        txtName = JOE_T_PreProcessingComposite_PreProcessingName.Control(new Text(scriptcom, SWT.BORDER));
        GridData gd_txtName = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gd_txtName.widthHint = 160;
        txtName.setText(objDataProvider.getMonitorName());
        txtName.setLayoutData(gd_txtName);
        txtName.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!init)
                    objDataProvider.setMonitorName(txtName.getText());
            }
        });
        @SuppressWarnings("unused")
        final Label orderingLabel = JOE_L_PreProcessingComposite_Ordering.Control(new Label(scriptcom, SWT.NONE));
        GridData gd_orderingLabel = new GridData(SWT.CENTER, SWT.CENTER, true, false);
        gd_orderingLabel.widthHint = 60;
        orderingLabel.setLayoutData(gd_orderingLabel);
        spinner = JOE_Sp_PreProcessingComposite_Ordering.Control(new Spinner(scriptcom, SWT.BORDER));
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
        butFavorite = JOE_B_PreProcessingComposite_Favourites.Control(new Button(gMain, SWT.NONE));
        butFavorite.setEnabled(true);
        GridData gd_butFavorite = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        gd_butFavorite.widthHint = 100;
        butFavorite.setLayoutData(gd_butFavorite);
        cboFavorite = JOE_Cbo_PreProcessingComposite_Favourites.Control(new Combo(gMain, SWT.NONE));
        GridData gd_cboFavorite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
        gd_cboFavorite.widthHint = 145;
        cboFavorite.setLayoutData(gd_cboFavorite);
    }

    public void init() {
        init = true;
        int language = objDataProvider.getLanguage();
        if (language < 0) {
            language = 0;
        }
        spinner.setSelection((objDataProvider.getOrdering().length() == 0 ? 0 : Integer.parseInt(objDataProvider.getOrdering())));
        init = false;
    }

    @Override
    protected void checkSubclass() {
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
