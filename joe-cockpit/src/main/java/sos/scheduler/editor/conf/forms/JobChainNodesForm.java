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
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.options.Options;

import static com.sos.joe.globals.messages.SOSJOEMessageCodes.*;

import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobChainNodesForm extends Composite implements IUnsaved, IUpdateLanguage {
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
    //SOSTabJobChainReturnCodes   tbtmReturncodes;
    //SOSTabJobChainDiagram       tbtmJobchainNodeParameters;    
 
    public JobChainNodesForm(Composite parent_, int style, SchedulerDom dom_, Element jobChain) {
        super(parent_, style);
        dom = dom_;
        listener = new JobChainListener(dom, jobChain);
        initialize();
        setToolTipText();
   
    }

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
                        case 3:
                            // tbtmReturncodes.setJobchainListOfReturnCodeElements(listener.getJobchainListOfReturnCodeElements());
                            // tbtmReturncodes.setEnabled(tbtmJobchainNode.hasSelectedNode());
                            // tbtmReturncodes.setTitle(tbtmJobchainNode.getState());
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
         //  tbtmReturncodes = new SOSTabJobChainReturnCodes("Return Codes", mainTabFolder, listener);
         //  tbtmJobchainNodeParameters = new SOSTabJobChainDiagram("Node Parameters", mainTabFolder);
            
           mainTabFolder.setSelection(0);
           tbtmJobchainNode.enableFileOrderSourceControls();
          // tbtmReturncodes.setEnabled(tbtmJobchainNode.hasSelectedNode());

                  
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

    @Override public void setToolTipText() {
        //      
    }

   
} // @jve:decl-index=0:visual-constraint="10,10"
