package sos.scheduler.editor.conf.forms;
 
import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.composites.SOSTabFileOrderSource;
import sos.scheduler.editor.conf.composites.SOSTabJobChainDiagram;
import sos.scheduler.editor.conf.composites.SOSTabJobChainNodes;
import sos.scheduler.editor.conf.listeners.JobChainListener;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.*;

import com.sos.joe.xml.jobscheduler.SchedulerDom;

<<<<<<< HEAD
public class JobChainNodesForm extends Composite implements IUnsaved, IUpdateLanguage {
=======
<<<<<<< HEAD
public class JobChainNodesForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
>>>>>>> origin/release/1.9
    private final String                                    conClassName                = "JobChainNodesForm";
    final String                                            conMethodName               = conClassName + "::enclosing_method";
    @SuppressWarnings("unused") private final String        conSVNVersion               = "$Id$";
    @SuppressWarnings("unused") private static final Logger logger                      = Logger.getLogger(JobChainNodesForm.class);
    private JobChainListener                                listener                    = null;
    private SchedulerDom                                    dom                         = null;
    private CTabFolder              mainTabFolder                           = null;
    
    SOSTabJobChainNodes         tbtmJobchainNode;
    SOSTabFileOrderSource       tbtmFileOrderSource;
    SOSTabJobChainDiagram       tbtmJobchainDiagram;
     
    public JobChainNodesForm(Composite parent_, int style, SchedulerDom dom_, Element jobChain) {
        super(parent_, style);
        dom = dom_;
        listener = new JobChainListener(dom, jobChain);
        initialize();
        setToolTipText();
   
    }
=======
public class JobChainNodesForm extends SOSJOEMessageCodes implements IUnsaved {
	private final String									conClassName				= "JobChainNodesForm";
	final String											conMethodName				= conClassName + "::enclosing_method";
	@SuppressWarnings("unused") private final String		conSVNVersion				= "$Id$";
	@SuppressWarnings("unused") private static final Logger	logger						= Logger.getLogger(JobChainNodesForm.class);
	private Button											dumm2						= null;
	private Button											bNewNode					= null;
	private Table											tNodes						= null;
	private Button											bApplyNode					= null;
	private Text											tMoveTo						= null;
	private Button											bRemoveFile					= null;
	private Combo											cErrorState					= null;
	private Label											label9						= null;
	private Combo											cNextState					= null;
	@SuppressWarnings("unused") private Label				label8						= null;
	private Button											bFileSink					= null;
	private Button											bEndNode					= null;
	private Button											bFullNode					= null;
	private Composite										cType						= null;
	private Combo											cJob						= null;
	@SuppressWarnings("unused") private Label				label7						= null;
	private Text											tState						= null;
	private Label											label6						= null;
	private static final String								GROUP_FILEORDERSOURCE_TITLE	= "File Order Sources";
	private Group											gFileOrderSource			= null;
	private JobChainListener								listener					= null;
	private Group											jobChainGroup				= null;
	private Button											bNewFileOrderSource			= null;
	private Button											bRemoveFileOrderSource		= null;
	private Button											bApplyFileOrderSource		= null;
	private Text											tDirectory					= null;
	private Text											tDelayAfterError			= null;
	private Text											tMax						= null;
	private Text											tNextState					= null;
	private Text											tRegex						= null;
	private Text											tRepeat						= null;
	private Table											tFileOrderSource			= null;
	private Button											bRemoveNode					= null;
	private Group											gNodes;
	private Text											tDelay						= null;
	private Button											butImportJob				= null;
	private boolean											refresh						= false;
	private Button											butDetailsJob				= null;
	private Button											butBrowse					= null;
	private ISchedulerUpdate								update						= null;
	private Combo											cOnError					= null;
	private Button											butUp						= null;
	private Button											butDown						= null;
	private SchedulerDom									dom							= null;
	private Button											butGoto						= null;
	private Button											butInsert					= null;
	private boolean											isInsert					= false;
	private Button											reorderButton				= null;
	private Button											butAddMissingNodes			= null;
	/**
	 * Hilfsvariable: Wenn Parameter Formular geöffnet wurde muss überprüft
	 * werden, ob der Checkbox in der Tabelle - State gesetzt werden soll.
	 */
	private boolean											checkParameter				= false;

	// private Text txtStateText = null;
	// private Composite composite_2 = null;
	public JobChainNodesForm(Composite parent, int style, SchedulerDom dom_, Element jobChain) {
		super(parent, style);
		dom = dom_;
		listener = new JobChainListener(dom, jobChain);
		initialize();
		boolean existChainNodes = check();
		jobChainGroup.setEnabled(existChainNodes);
		bNewNode.setEnabled(existChainNodes);
		if (existChainNodes)
			fillChain(false, false);
		this.setEnabled(Utils.isElementEnabled("job_chain", dom, jobChain));
	}
>>>>>>> origin/release/1.8

    @Override public void apply() {
        //tbtmReturncodes.applyReturnCodes();
        tbtmFileOrderSource.applyFileOrderSource();
        tbtmJobchainNode.applyNode();
    }

    @Override public boolean isUnsaved() {
        return false;
    }

    private void initialize() {
        this.setLayout(new GridLayout());
        createGroup();
        setSize(new Point(776, 664));
    }
 
    
    private void createGroup() {
        try {
            
            mainTabFolder = new CTabFolder(this, SWT.NONE);
            mainTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            mainTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
             
            mainTabFolder.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(final SelectionEvent e) {
                 
                    int intIndex = mainTabFolder.getSelectionIndex();
                
                    switch (intIndex) {
                        case 0: //
                            tbtmJobchainNode.enableFileOrderSourceControls();
                              break;
                        case 2:
                            try {
                                String xml = Utils.getElementAsString(listener.getChain());
                                File inputFile = new File(listener.getDom().getFilename(),listener.getChainName() + ".job_chain.xml~");
                                tbtmJobchainDiagram.jobChainDiagram(xml,inputFile);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            break;
                        
                        default:
                            break;
                    }                    
                    
                     
 

                }
            });
            
           tbtmJobchainNode = new SOSTabJobChainNodes("Nodes", mainTabFolder, listener);
           tbtmFileOrderSource = new SOSTabFileOrderSource("File Order Source", mainTabFolder,  listener);
           if (Options.isShowDiagram()){
              tbtmJobchainDiagram = new SOSTabJobChainDiagram("Diagram", mainTabFolder);
           }
         
           mainTabFolder.setSelection(0);
           tbtmJobchainNode.enableFileOrderSourceControls();
 
                  
        }
        catch (Exception e) {
            try {
                new ErrorLog(JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            }
            catch (Exception ee) {}
        }
    }

 
     
    public void setISchedulerUpdate(ISchedulerUpdate update_) {
          listener.setISchedulerUpdate(update_);
    }

<<<<<<< HEAD
    @Override public void setToolTipText() {
        //      
    }
=======
>>>>>>> origin/release/1.8

   
} // @jve:decl-index=0:visual-constraint="10,10"
