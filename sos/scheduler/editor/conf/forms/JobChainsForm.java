package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
 
import org.eclipse.swt.widgets.Composite;
 
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobChainsListener;

public class JobChainsForm extends Composite implements IUnsaved, IUpdateLanguage {
  private Button dumm2;
  private Button bRemoveNode;
  private Button bNewNode;
  private Table tNodes;
  private Button dummy1;
  private static final String GROUP_NODES_TITLE = "Chain Nodes";
  private static final String GROUP_FILEORDERSOURCE_TITLE = "File Order Sources";
    private Button bApplyNode;
    private Label label8;
    private Button bEndNode;
    private Button bFullNode;
    private Composite cType;
    private Label label7;
    private Text tState;
    private Label label6;
    private Label label1;
    private CCombo cJob;
    private CCombo cErrorState;
    private Label label9;
    private CCombo cNextState;
    private Group gFileOrderSource;
 
    private JobChainsListener   listener;

    private Group               group             = null;
  
    private Table               tChains           = null;

    private Button              bRemoveChain      = null;

    private Label               label             = null;

    private Text                tName             = null;

    private Button              bRecoverable      = null;

    private Button              bVisible          = null;

 

 

    private Button              bNewChain         = null;
  

    private Button              bApplyChain       = null;
 
    
    private SashForm            sashForm               = null;
    private Group               gNodes                 = null;
    private Button              bNewFileOrderSource    = null;
    private Button              bRemoveFileOrderSource = null;
    private Button              bApplyFileOrderSource  = null;
    private Text                tDirectory             = null;
    private Text                tDelayAfterError       = null;
    private Text                tMax                   = null;
    private Text                tNextState             = null;
    private Text                tRegex                 = null;
    private Text                tRepeat                = null;
    private Table               tFileOrderSource       = null;
    private Text                tMoveTo                = null;
    private Button              bRemoveFile            = null;    
    private Button              bFileSink              = null;
    
    
    
    
    public JobChainsForm(Composite parent, int style, SchedulerDom dom, Element config) {
        super(parent, style);
        listener = new JobChainsListener(dom, config);
        initialize();
        setToolTipText();
        fillChain(false, false);
        listener.fillChains(tChains);
        sashForm.setWeights(new int[] { 20, 50, 30 });
    }


    public void apply() {
        if (bApplyChain.isEnabled())
            applyChain();
        if (bApplyNode.isEnabled())
            applyNode();
    }


    public boolean isUnsaved() {
        return bApplyChain.isEnabled() || bApplyNode.isEnabled();
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(676, 464));
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        group = new Group(this, SWT.NONE);
         
        final GridLayout gridLayout = new GridLayout();
        group.setLayout(gridLayout);

        sashForm = new SashForm(group, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        final Group jobchainsGroup = new Group(sashForm, SWT.VERTICAL);
        jobchainsGroup.setText("Job Chains");
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 5;
        jobchainsGroup.setLayout(gridLayout_2);
        label = new Label(jobchainsGroup, SWT.NONE);
        label.setText("Chain Name:");
        tName = new Text(jobchainsGroup, SWT.BORDER);
        tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        tName.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        tName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                boolean valid = listener.isValidChain(tName.getText());
                if (!valid)
                    tName.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
                else {
                    getShell().setDefaultButton(bApplyChain);
                    tName.setBackground(null);
                }
                bApplyChain.setEnabled(valid && !tName.equals(""));
            }
        });
        bRecoverable = new Button(jobchainsGroup, SWT.CHECK);
        bRecoverable.setLayoutData(new GridData());
        bRecoverable.setSelection(true);
        bRecoverable.setText("Orders Recoverable");
        bRecoverable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                getShell().setDefaultButton(bApplyChain);
                bApplyChain.setEnabled(true);
            }
        });
        bVisible = new Button(jobchainsGroup, SWT.CHECK);
        bVisible.setLayoutData(new GridData());
        bVisible.setSelection(true);
        bVisible.setText("Visible");
        bVisible.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                getShell().setDefaultButton(bApplyChain);
                bApplyChain.setEnabled(true);
            }
        });
        bApplyChain = new Button(jobchainsGroup, SWT.NONE);
        bApplyChain.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        bApplyChain.setText("A&pply Job Chain");
        bApplyChain.setEnabled(false);
        bApplyChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                applyChain();
            }
        });
        tChains = new Table(jobchainsGroup, SWT.FULL_SELECTION | SWT.BORDER);
        tChains.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2));
        tChains.setHeaderVisible(true);
        tChains.setLinesVisible(true);
        tChains.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (tChains.getSelectionCount() > 0) {
                    listener.selectChain(tChains.getSelectionIndex());
                    fillChain(true, false);
                    bRemoveNode.setEnabled(false);
                    bRemoveFileOrderSource.setEnabled(false);
                    bApplyChain.setEnabled(false);
                }
                bRemoveChain.setEnabled(tChains.getSelectionCount() > 0);
            }
        });
        TableColumn tableColumn_2 = new TableColumn(tChains, SWT.NONE);
        tableColumn_2.setWidth(200);
        tableColumn_2.setText("Name");
        TableColumn tableColumn1 = new TableColumn(tChains, SWT.NONE);
        tableColumn1.setWidth(150);
        tableColumn1.setText("Orders Recoverable");
        TableColumn tableColumn2 = new TableColumn(tChains, SWT.NONE);
        tableColumn2.setWidth(150);
        tableColumn2.setText("Visible");
        bNewChain = new Button(jobchainsGroup, SWT.NONE);
        bNewChain.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        bNewChain.setText("New Job &Chain");
        getShell().setDefaultButton(bNewChain);
        bNewChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.newChain();
                tChains.deselectAll();
                fillChain(true, true);
                bApplyChain.setEnabled(false);
                enableFileOrderSource(true);              
                tName.setFocus();
            }
        });
        bRemoveChain = new Button(jobchainsGroup, SWT.NONE);
        bRemoveChain.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        bRemoveChain.setText("Remove Job Chain");
        bRemoveChain.setEnabled(false);
        bRemoveChain.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                deleteChain();
            }
        });
        new Label(jobchainsGroup, SWT.NONE);
        new Label(jobchainsGroup, SWT.NONE);
        new Label(jobchainsGroup, SWT.NONE);
        new Label(jobchainsGroup, SWT.NONE);

        dummy1 = new Button(jobchainsGroup, SWT.NONE);
        dummy1.setVisible(false);
        dummy1.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        dummy1.setEnabled(false);
        dummy1.setText("Remove Order File Source");

        gNodes = new Group(sashForm, SWT.NONE);
        gNodes.setText("Chain Node");
        final GridLayout gridLayout_4 = new GridLayout();
        gridLayout_4.numColumns = 7;
        gNodes.setLayout(gridLayout_4);

        label6 = new Label(gNodes, SWT.NONE);
        label6.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
        label6.setText("State:");

        tState = new Text(gNodes, SWT.BORDER);
        tState.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {

            boolean valid = listener.isValidState(tState.getText());
            if (!valid)
                tState.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
            else
                tState.setBackground(null);
            bApplyNode.setEnabled(isValidNode()&& valid);
            if (bApplyNode.getEnabled())
                getShell().setDefaultButton(bApplyNode);
        	}
        });
        final GridData gridData18 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        tState.setLayoutData(gridData18);

        label7 = new Label(gNodes, SWT.NONE);
        label7.setText("Job:");

        cJob = new CCombo(gNodes, SWT.BORDER);
        cJob.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {

            bApplyNode.setEnabled(isValidNode());
            if (bApplyNode.getEnabled())
                getShell().setDefaultButton(bApplyNode);
        	}
        });
        cJob.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		 if (e.keyCode == SWT.CR) {
               applyNode();
           }
        	}
        });
        final GridData gridData13 = new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
        cJob.setLayoutData(gridData13);

        bApplyNode = new Button(gNodes, SWT.NONE);
        bApplyNode.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {  
        		  applyNode();
        	}
        });
        final GridData gridData7 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bApplyNode.setLayoutData(gridData7);
        bApplyNode.setEnabled(false);
        bApplyNode.setText("&Apply Chain Node");

        label1 = new Label(gNodes, SWT.NONE);
        label1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
        label1.setText("Type:");

        cType = new Composite(gNodes, SWT.NONE);
        final GridData gridData5 = new GridData(GridData.BEGINNING, GridData.CENTER, true, false, 1, 2);
        cType.setLayoutData(gridData5);
        cType.setLayout(new RowLayout(SWT.VERTICAL));

        bFullNode = new Button(cType, SWT.RADIO);
        bFullNode.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if (bFullNode.getSelection()) {
        			tMoveTo.setEnabled(false);
        			bRemoveFile.setEnabled(false);
              cNextState.setEnabled(true);
              cErrorState.setEnabled(true);
              cJob.setEnabled(true);
              bApplyNode.setEnabled(isValidNode());
              if (bApplyNode.getEnabled())
                  getShell().setDefaultButton(bApplyNode);
          }
        	}
        });
        bFullNode.setText("Full Node");

        bEndNode = new Button(cType, SWT.RADIO);
        bEndNode.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if (bEndNode.getSelection()) {
        			tMoveTo.setEnabled(false);
        			bRemoveFile.setEnabled(false);
              cNextState.setEnabled(false);
              cErrorState.setEnabled(false);
              cJob.setEnabled(false);
              cJob.setText("");
              cNextState.setText("");
              cErrorState.setText("");
              if (tState.getText().equals(""))
                  bApplyNode.setEnabled(false);
          }
        	}
        });
        bEndNode.setText("End Node");

        bFileSink = new Button(cType, SWT.RADIO);
        bFileSink.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if (bFileSink.getSelection()) {
              cNextState.setEnabled(false);
              cErrorState.setEnabled(false);
              cJob.setEnabled(false);
              cJob.setText("");
              cNextState.setText("");
              cErrorState.setText("");
              tMoveTo.setEnabled(true);
              bRemoveFile.setEnabled(true);
              
              if (tState.getText().equals(""))
                  bApplyNode.setEnabled(false);
          }
        	}
        });
        bFileSink.setEnabled(false);
        bFileSink.setText("File Sink");

        label8 = new Label(gNodes, SWT.NONE);
        label8.setLayoutData(new GridData());
        label8.setText("Next State:");

        cNextState = new CCombo(gNodes, SWT.BORDER);
        cNextState.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
            bApplyNode.setEnabled(isValidNode());
            if (bApplyNode.getEnabled())
                getShell().setDefaultButton(bApplyNode);
        	}
        });
        cNextState.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
            if (e.keyCode == SWT.CR) {
              applyNode();
          }
        	}
        });
        final GridData gridData14 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData14.widthHint = 80;
        cNextState.setLayoutData(gridData14);

        label9 = new Label(gNodes, SWT.NONE);
        label9.setText("Error State:");

        cErrorState = new CCombo(gNodes, SWT.BORDER);
        cErrorState.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) { getShell().setDefaultButton(bApplyNode);
          bApplyNode.setEnabled(isValidNode());
          if (bApplyNode.getEnabled())
              getShell().setDefaultButton(bApplyNode);
        	}
        });
        cErrorState.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
            if (e.keyCode == SWT.CR) {
              applyNode();
          }
        	}
        });
        final GridData gridData15 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData15.widthHint = 80;
        cErrorState.setLayoutData(gridData15);
        new Label(gNodes, SWT.NONE);
        new Label(gNodes, SWT.NONE);

        final Label removeFileLabel = new Label(gNodes, SWT.NONE);
        removeFileLabel.setText("Remove File");

        bRemoveFile = new Button(gNodes, SWT.CHECK);
        bRemoveFile.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		 if (e.keyCode == SWT.CR) {
               applyNode();
           }
        	}
        });
        bRemoveFile.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        	  if (bRemoveFile.getSelection())tMoveTo.setText(""); 
        		bApplyNode.setEnabled(isValidNode());
             if (bApplyNode.getEnabled())
                 getShell().setDefaultButton(bApplyNode);
         	
        	}
        });
        bRemoveFile.setEnabled(false);
        bRemoveFile.setLayoutData(new GridData());

        final Label movweToLabel = new Label(gNodes, SWT.NONE);
        movweToLabel.setText("Move to");

        tMoveTo = new Text(gNodes, SWT.BORDER);
        tMoveTo.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		 if (e.keyCode == SWT.CR) {
               applyNode();
           }
        	}
        });
        tMoveTo.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
       		   if (!tMoveTo.getText().equals(""))bRemoveFile.setSelection(false);
        	   bApplyNode.setEnabled(isValidNode());
             if (bApplyNode.getEnabled())
                 getShell().setDefaultButton(bApplyNode);
         
        	}
        });
        tMoveTo.setEnabled(false);
        tMoveTo.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        new Label(gNodes, SWT.NONE);

        tNodes = new Table(gNodes, SWT.FULL_SELECTION | SWT.BORDER);
        tNodes.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		if (tNodes.getSelectionCount() > 0) {
              listener.selectNode(tNodes);
              enableNode(true);
              fillNode(false);
           }
           bRemoveNode.setEnabled(tNodes.getSelectionCount() > 0);
        	}
        });
        tNodes.setLinesVisible(true);
        tNodes.setHeaderVisible(true);
        final GridData gridData4 = new GridData(GridData.FILL, GridData.FILL, true, true, 6, 2);
        tNodes.setLayoutData(gridData4);

        final TableColumn tableColumn3 = new TableColumn(tNodes, SWT.NONE);
        tableColumn3.setWidth(90);
        tableColumn3.setText("State");

        final TableColumn newColumnTableColumn_3 = new TableColumn(tNodes, SWT.NONE);
        newColumnTableColumn_3.setWidth(100);
        newColumnTableColumn_3.setText("Node");

        final TableColumn tableColumn4 = new TableColumn(tNodes, SWT.NONE);
        tableColumn4.setWidth(200);
        tableColumn4.setText("Job/Dir");

        final TableColumn tableColumn5 = new TableColumn(tNodes, SWT.NONE);
        tableColumn5.setWidth(90);
        tableColumn5.setText("Next State");

        final TableColumn tableColumn6 = new TableColumn(tNodes, SWT.NONE);
        tableColumn6.setWidth(90);
        tableColumn6.setText("Error State");

        bNewNode = new Button(gNodes, SWT.NONE);
        bNewNode.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		getShell().setDefaultButton(null);
            tNodes.deselectAll();
            listener.selectNode(null);
            bRemoveNode.setEnabled(false);
            enableNode(true);
            fillNode(true);
            tState.setFocus();
        	}
        });
        bNewNode.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        bNewNode.setText("New Chain &Node");

        bRemoveNode = new Button(gNodes, SWT.NONE);
        bRemoveNode.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
            if (tNodes.getSelectionCount() > 0) {
              int index = tNodes.getSelectionIndex();
              listener.deleteNode(tNodes);
              tNodes.remove(index);
              if (index >= tNodes.getItemCount())
                  index--;
              boolean empty = tNodes.getItemCount() == 0;

              fillNode(empty);
              enableNode(!empty);
              bRemoveNode.setEnabled(!empty);
              if (!empty) {
                  tNodes.select(index);
                  listener.selectNode(tNodes);
              } else {
                  listener.selectNode(null);
              }
          }
        	}
        });
        final GridData gridData11 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bRemoveNode.setLayoutData(gridData11);
        bRemoveNode.setEnabled(false);
        bRemoveNode.setText("Remove Chain Node");
        new Label(gNodes, SWT.NONE);
        new Label(gNodes, SWT.NONE);
        new Label(gNodes, SWT.NONE);
        new Label(gNodes, SWT.NONE);
        new Label(gNodes, SWT.NONE);
        new Label(gNodes, SWT.NONE);

        dumm2 = new Button(gNodes, SWT.NONE);
        dumm2.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        dumm2.setVisible(false);
        dumm2.setEnabled(false);
        dumm2.setText("Remove Order File Source");

        
        gFileOrderSource = new Group(sashForm, SWT.VERTICAL);
        gFileOrderSource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        gFileOrderSource.setText("File Order Source");
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 5;
        gFileOrderSource.setLayout(gridLayout_1);

        final Label directoryLabel = new Label(gFileOrderSource, SWT.NONE);
        directoryLabel.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
        directoryLabel.setText("Directory");

        tDirectory = new Text(gFileOrderSource, SWT.BORDER);
        tDirectory.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        	  bApplyFileOrderSource.setEnabled(isValidSource());
            if (bApplyFileOrderSource.getEnabled())
                getShell().setDefaultButton(bApplyFileOrderSource);
        	}
        });
        tDirectory.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        	  
            
        	}
        });
        tDirectory.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Label delay_after_errorLabel = new Label(gFileOrderSource, SWT.NONE);
        delay_after_errorLabel.setText("Delay after error");


        tDelayAfterError = new Text(gFileOrderSource, SWT.BORDER);
        tDelayAfterError.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        	  bApplyFileOrderSource.setEnabled(isValidSource());
            if (bApplyFileOrderSource.getEnabled())
                getShell().setDefaultButton(bApplyFileOrderSource);
        		
        	}
        });
        tDelayAfterError.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        bApplyFileOrderSource = new Button(gFileOrderSource, SWT.NONE);
        bApplyFileOrderSource.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		applyFileOrderSource();
        	}
        });
        bApplyFileOrderSource.setEnabled(false);
        bApplyFileOrderSource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        bApplyFileOrderSource.setText("Apply File Order Source");

        final Label regexLabel = new Label(gFileOrderSource, SWT.NONE);
        regexLabel.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
        regexLabel.setText("Regex");

        
        tRegex = new Text(gFileOrderSource, SWT.BORDER);
        tRegex.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
         	  bApplyFileOrderSource.setEnabled(isValidSource());
            if (bApplyFileOrderSource.getEnabled())
                getShell().setDefaultButton(bApplyFileOrderSource);
        	}
        });
        tRegex.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Label repeatLabel = new Label(gFileOrderSource, SWT.NONE);
        repeatLabel.setText("Repeat");

        tRepeat = new Text(gFileOrderSource, SWT.BORDER);
        tRepeat.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        	  bApplyFileOrderSource.setEnabled(isValidSource());
            if (bApplyFileOrderSource.getEnabled())
                getShell().setDefaultButton(bApplyFileOrderSource);

        	}
        });
        tRepeat.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        new Label(gFileOrderSource, SWT.NONE);

        final Label maxLabel = new Label(gFileOrderSource, SWT.NONE);
        maxLabel.setText("Max");

        tMax = new Text(gFileOrderSource, SWT.BORDER);
        tMax.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        	  bApplyFileOrderSource.setEnabled(isValidSource());
            if (bApplyFileOrderSource.getEnabled())
                getShell().setDefaultButton(bApplyFileOrderSource);
        		
        	}
        });
        tMax.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Label stateLabel = new Label(gFileOrderSource, SWT.NONE);
        stateLabel.setText("Next state");

        tNextState = new Text(gFileOrderSource, SWT.BORDER);
        tNextState.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        	  bApplyFileOrderSource.setEnabled(isValidSource());
            if (bApplyFileOrderSource.getEnabled())
                getShell().setDefaultButton(bApplyFileOrderSource);

        	}
        });
        tNextState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        tFileOrderSource = new Table(gFileOrderSource, SWT.BORDER);
        tFileOrderSource.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
         		if (tFileOrderSource.getSelectionCount() > 0) {
              listener.selectFileOrderSource(tFileOrderSource);
              bApplyFileOrderSource.setEnabled(false);
              fillFileOrderSource(false); 
              enableFileOrderSource(true);              
           }
           bRemoveFileOrderSource.setEnabled(tFileOrderSource.getSelectionCount() > 0);
          	}
        	
        });
        tFileOrderSource.setLinesVisible(true);
        tFileOrderSource.setHeaderVisible(true);
        tFileOrderSource.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2));

        final TableColumn newColumnTableColumn = new TableColumn(tFileOrderSource, SWT.NONE);
        newColumnTableColumn.setWidth(300);
        newColumnTableColumn.setText("Directory");

        final TableColumn newColumnTableColumn_1 = new TableColumn(tFileOrderSource, SWT.NONE);
        newColumnTableColumn_1.setWidth(200);
        newColumnTableColumn_1.setText("Regex");

        
        final TableColumn newColumnTableColumn_2 = new TableColumn(tFileOrderSource, SWT.NONE);
        newColumnTableColumn_2.setWidth(100);
        newColumnTableColumn_2.setText("Next State");

        
        bNewFileOrderSource = new Button(gFileOrderSource, SWT.NONE);
        bNewFileOrderSource.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {	
        		getShell().setDefaultButton(null);
        		tFileOrderSource.deselectAll();
            listener.selectFileOrderSource(null);
            bRemoveFileOrderSource.setEnabled(false);
            fillFileOrderSource(true);
            enableFileOrderSource(true);
            tDirectory.setFocus();
        	}
        });
        bNewFileOrderSource.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        bNewFileOrderSource.setText("New File Order Source");

        bRemoveFileOrderSource = new Button(gFileOrderSource, SWT.NONE);
        bRemoveFileOrderSource.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        	  if (tFileOrderSource.getSelectionCount() > 0) {
              bFileSink.setEnabled(tFileOrderSource.getItemCount() > 0);
              tMoveTo.setEnabled(tFileOrderSource.getItemCount() > 0);
              bRemoveFile.setEnabled(tFileOrderSource.getItemCount() > 0);
              
              int index = tFileOrderSource.getSelectionIndex();
              listener.deleteFileOrderSource(tFileOrderSource);
              tFileOrderSource.remove(index);
              if (index >= tFileOrderSource.getItemCount())
                  index--;
              boolean empty = tFileOrderSource.getItemCount() == 0;

              fillFileOrderSource(empty);
              enableFileOrderSource(!empty);           
              bRemoveFileOrderSource.setEnabled(!empty);
              if (!empty) {
              	tFileOrderSource.select(index);
                  listener.selectFileOrderSource(tFileOrderSource);
              } else {
                  listener.selectFileOrderSource(null);
              }
          }
        	}
        });
        bRemoveFileOrderSource.setEnabled(false);
        bRemoveFileOrderSource.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        bRemoveFileOrderSource.setText("Remove Order File Source");
        //group.setTabList(new Control[] {cChains, fileOrderSourceGroup, gNodes, fileOrderSinkGroup, label_2});
 
    }


     


    private void fillChain(boolean enable, boolean isNew) {
      tName.setEnabled(enable);
      bRecoverable.setEnabled(enable);
      bVisible.setEnabled(enable);

      tName.setText(enable ? listener.getChainName() : "");
      bRecoverable.setSelection(enable ? listener.getRecoverable() || isNew : true);
      bVisible.setSelection(enable ? listener.getVisible() || isNew : true);

      tName.setBackground(null);
      bApplyChain.setEnabled(enable);

      if (enable && !isNew) {
        listener.fillFileOrderSource(tFileOrderSource);
        listener.fillChain(tNodes);
        gNodes.setText(GROUP_NODES_TITLE + " for: " + listener.getChainName());
        gFileOrderSource.setText(GROUP_FILEORDERSOURCE_TITLE + " for: " + listener.getChainName());
        bNewFileOrderSource.setEnabled(true);
        bNewNode.setEnabled(true);
     } else {
        bNewNode.setEnabled(false);
        bNewFileOrderSource.setEnabled(false);
    }

      enableNode(false);
      enableFileOrderSource(false);           
           
      
  }
    private void deleteChain() {
        if (tChains.getSelectionCount() > 0) {
            int index = tChains.getSelectionIndex();
            listener.deleteChain(index);
            tChains.remove(index);
            if (index >= tChains.getItemCount())
                index--;
            if (tChains.getItemCount() > 0) {
                tChains.select(index);
                listener.selectChain(index);
            }
        }
        boolean selection = tChains.getSelectionCount() > 0;
        bRemoveChain.setEnabled(selection);
        fillChain(selection, false);
    }


    private void enableNode(boolean enable) {
      bFullNode.setEnabled(enable);
      bEndNode.setEnabled(enable);
      bFileSink.setEnabled(enable && tFileOrderSource.getItemCount() > 0);
     
      tState.setEnabled(enable);
      cJob.setEnabled(enable);
      cNextState.setEnabled(enable);
      cErrorState.setEnabled(enable);

      tMoveTo.setEnabled(enable && tFileOrderSource.getItemCount() > 0);
      bRemoveFile.setEnabled(enable && tFileOrderSource.getItemCount() > 0);
     
      
      
      bApplyNode.setEnabled(false);
  }

    private void enableFileOrderSource(boolean enable) {
      tDirectory.setEnabled(enable);
      tMax.setEnabled(enable);
      tRepeat.setEnabled(enable);
      tDelayAfterError.setEnabled(enable);
      tNextState.setEnabled(enable);
      tRegex.setEnabled(enable);
      bApplyFileOrderSource.setEnabled(false);
  }

 
    
    private void fillNode(boolean clear) {
      boolean fullNode = listener.isFullNode();
      boolean fileSinkNode = listener.isFileSinkNode();
      boolean endNode = !fullNode && !fileSinkNode;
     
      bFullNode.setSelection(clear || fullNode);
      bEndNode.setSelection(!clear && endNode);
      bFileSink.setSelection(!clear && fileSinkNode  && tFileOrderSource.getItemCount() > 0);
     
      cNextState.setEnabled(fullNode);
      cErrorState.setEnabled(fullNode);
      cJob.setEnabled(fullNode);

      tMoveTo.setEnabled(fileSinkNode  && tFileOrderSource.getItemCount() > 0);
      bRemoveFile.setEnabled(fileSinkNode  && tFileOrderSource.getItemCount() > 0);

      tState.setText(clear ? "" : listener.getState());

      cJob.setItems(listener.getJobs());
      cNextState.setItems(listener.getStates());
      cErrorState.setItems(listener.getStates());

      tMoveTo.setText(listener.getMoveTo());
      bRemoveFile.setSelection(listener.getRemoveFile());
      
      int job = cJob.indexOf(listener.getJob());
      if (clear || job == -1)
          cJob.setText(listener.getJob());
      else
          cJob.select(job);

      int next = cNextState.indexOf(listener.getNextState());
      if (clear || !fullNode || next == -1)
          cNextState.setText(listener.getNextState());
      else
          cNextState.select(next);

      int error = cErrorState.indexOf(listener.getErrorState());
      if (clear || !fullNode || error == -1)
          cErrorState.setText(listener.getErrorState());
      else
          cErrorState.select(error);

      bApplyNode.setEnabled(false);
  }

    private void fillFileOrderSource(boolean clear) {
    	
    	tDirectory.setText(clear ? "" : listener.getFileOrderSource("directory"));
    	tRegex.setText(clear ? "" : listener.getFileOrderSource("regex"));
    	tMax.setText(clear ? "" : listener.getFileOrderSource("max"));
    	tDelayAfterError.setText(clear ? "" : listener.getFileOrderSource("delay_after_error"));
    	tRepeat.setText(listener.getFileOrderSource(clear ? "" : "repeat"));
    	tNextState.setText(listener.getFileOrderSource(clear ? "" : "next_state"));

      bApplyFileOrderSource.setEnabled(false);
  }

    
    
    private void applyChain() {
        listener.applyChain(tName.getText(), bRecoverable.getSelection(), bVisible.getSelection());
        int index = tChains.getSelectionIndex();
        if (index == -1)
            index = tChains.getItemCount();
        listener.fillChains(tChains);
        tChains.select(index);
        fillChain(true, false);
        bRemoveChain.setEnabled(true);
        bApplyChain.setEnabled(false);
        getShell().setDefaultButton(bNewChain);
    }
    
 

    private void applyNode() {
        String msg = "";
        if (!msg.equals("")) {
            MainWindow.message(msg, SWT.ICON_INFORMATION);
        } else {
            listener.applyNode(bFullNode.getSelection() || bEndNode.getSelection(), tState.getText(), cJob.getText(), cNextState.getText(), cErrorState.getText(),bRemoveFile.getSelection(),tMoveTo.getText());
            listener.fillChain(tNodes);
            bApplyNode.setEnabled(false);
            bRemoveNode.setEnabled(false);
            listener.selectNode(null);
            fillNode(true);
            enableNode(false);
        }
    }

    private void applyFileOrderSource() {
      String msg = "";

      if (!msg.equals("")) {
          MainWindow.message(msg, SWT.ICON_INFORMATION);
      } else {
          listener.applyFileOrderSource(tDirectory.getText(),tRegex.getText(), tNextState.getText(), tMax.getText(), tRepeat.getText(),tDelayAfterError.getText());
          listener.fillFileOrderSource(tFileOrderSource);
          bApplyFileOrderSource.setEnabled(false);
          bRemoveFileOrderSource.setEnabled(false);
     
          bFileSink.setEnabled(bFullNode.getEnabled());
          tMoveTo.setEnabled(bFullNode.getEnabled());
          bRemoveFile.setEnabled(bFullNode.getEnabled());
          
          listener.selectFileOrderSource(null);
          fillFileOrderSource(true);
          enableFileOrderSource(false);
      }
  }

    private boolean isValidNode() {
      if (tState.getText().equals("") || bFullNode.getSelection() && cJob.getText().equals("")) {
              return false;
          } else {
              return true;
          }
    }
    
    private boolean isValidSource() {
      if (tDirectory.getText().equals("") ) {
              return false;
          } else {
              return true;
          }
    }
    
    
    

    public void setToolTipText() {
      tName.setToolTipText(Messages.getTooltip("job_chains.chain.name"));
      bNewChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_new"));
      bRemoveChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_remove"));
      bRecoverable.setToolTipText(Messages.getTooltip("job_chains.chain.orders_recoverable"));
      bVisible.setToolTipText(Messages.getTooltip("job_chains.chain.visible"));
      bApplyChain.setToolTipText(Messages.getTooltip("job_chains.chain.btn_apply"));
      tChains.setToolTipText(Messages.getTooltip("job_chains.table"));
      tState.setToolTipText(Messages.getTooltip("job_chains.node.state"));
      cErrorState.setToolTipText(Messages.getTooltip("job_chains.node.error_state"));
      cJob.setToolTipText(Messages.getTooltip("job_chains.node.job"));
      cNextState.setToolTipText(Messages.getTooltip("job_chains.node.next_state"));
      bApplyNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_apply"));
      bFullNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_full_node"));
      bEndNode.setToolTipText(Messages.getTooltip("job_chains.node.btn_end_node"));
      bFileSink.setToolTipText(Messages.getTooltip("job_chains.node.btn_filesink_node"));

  }
} // @jve:decl-index=0:visual-constraint="10,10"
