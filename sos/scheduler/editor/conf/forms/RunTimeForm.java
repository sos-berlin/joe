package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.RunTimeListener;


public class RunTimeForm extends Composite implements IUpdateLanguage {
    
	
	private Text            tFunction                = null;
	
    private RunTimeListener listener                 = null;

    private Group           gRunTime                 = null;

    //private DateForm        holidayForm              = null;

    private PeriodForm      periodForm               = null;

    private Group           gComment                 = null;

    private Text            tComment                 = null;        

    private Combo           comSchedule              = null; 

    private Button          butBrowse                = null;
    
    private ISchedulerUpdate _gui                    = null;
    
    private Group           groupStartTimeFuction    = null;
    
    private Group           groupSchedule            = null;
    
    private Element         runTimeBackUpElem        = null;
        
    
    
    public RunTimeForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate gui) {
    	
        super(parent, style);
        _gui = gui;
        listener = new RunTimeListener(dom, job);
        initialize();
        setToolTipText();
        dom.setInit(true);
        this.gRunTime.setEnabled(Utils.isElementEnabled("job", dom, job));        
        //holidayForm.setObjects(dom, listener.getRunTime(), gui);
        periodForm.setParams(dom, listener.isOnOrder());
        periodForm.setRunOnce(true);
        periodForm.setEnabled(true);
        periodForm.setPeriod(listener.getRunTime());
        tComment.setText(listener.getComment());
        tFunction.setText(listener.getFunction());
        String title = gComment.getText();
        if (dom.isJobDisabled(Utils.getAttributeValue("name", job))) {
            title += " (Cannot be set for disabled Jobs)";
            tComment.setEnabled(false);
        }
        gComment.setText(title);
        dom.setInit(false);
        
    }


    private void initialize() {

        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(576, 518));
        
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
    	
        GridLayout gridLayout3 = new GridLayout();
        gRunTime = new Group(this, SWT.NONE);
        gRunTime.setText("Run Time");
                
        createPeriodForm();
        gRunTime.setLayout(gridLayout3);
        GridData gridData4 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, true);
        gridData4.heightHint = 348;

        groupStartTimeFuction = new Group(gRunTime, SWT.NONE);
        groupStartTimeFuction.setText("Start Time Function:");
        groupStartTimeFuction.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        final GridLayout gridLayout = new GridLayout();
        groupStartTimeFuction.setLayout(gridLayout);

        tFunction = new Text(groupStartTimeFuction, SWT.BORDER);
        tFunction.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        			setEnabled();
        			listener.setFunction(tFunction.getText());
        			_gui.updateFont();
        		
        	}
        });
        final GridData gridData10_1_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData10_1_1.widthHint = 243;
        tFunction.setLayoutData(gridData10_1_1);

        groupSchedule = new Group(gRunTime, SWT.NONE);
        groupSchedule.setText("Schedule");
        groupSchedule.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 2;
        groupSchedule.setLayout(gridLayout_2);

        comSchedule = new Combo(groupSchedule, SWT.NONE);
        comSchedule.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {        		
        		listener.setSchedule(comSchedule.getText());
        	}
        });
        comSchedule.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        comSchedule.setItems(listener.getSchedules());
        comSchedule.setText(listener.getSchedule());
        comSchedule.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        			setEnabled();
            		listener.setSchedule(comSchedule.getText());
            		_gui.updateFont();
        	}
        });

        butBrowse = new Button(groupSchedule, SWT.NONE);
        butBrowse.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		String name = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_SCHEDULE);
				if(name != null && name.length() > 0)
					comSchedule.setText(name);
        	}
        });
        butBrowse.setText("Browse");
        
       // holidayForm = new DateForm(gRunTime, SWT.NONE, 0);
      //  holidayForm.setLayoutData(gridData4);
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
        gComment = new Group(gRunTime, SWT.NONE);
        gComment.setText("Comment");
        gComment.setLayoutData(gridData);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        gComment.setLayout(gridLayout_1);
		
        GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true);
        tComment = new Text(gComment, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
        tComment.setLayoutData(gridData1);
        tComment.setFont(ResourceManager.getFont("Courier New", 8, SWT.NONE));
        tComment.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                listener.setComment(tComment.getText());
            }
        });

        final Button button = new Button(gComment, SWT.NONE);
        button.setAlignment(SWT.UP);
        final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true);
        gridData_1.widthHint = 29;
        button.setLayoutData(gridData_1);
        button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(tComment.getText(), getShell(), true, "");
				if(text != null)
					tComment.setText(text);
			}
		});
        button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
                
    }


 


    /**
     * This method initializes periodForm
     */
    private void createPeriodForm() {
    	
    	GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, false, false);
    	gridData2.widthHint = 151;

    	periodForm = new PeriodForm(gRunTime, SWT.NONE, Editor.RUNTIME);
    	periodForm.setLayoutData(gridData2);
    	periodForm.setSchedulerUpdate( _gui);
    	
    }



    public void setToolTipText() {

        tComment.setToolTipText(Messages.getTooltip("run_time.comment"));
        butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));        
        tFunction.setToolTipText(Messages.getTooltip("run_time.start_time_function"));
        comSchedule.setToolTipText(Messages.getTooltip("run_time.combo_schedule"));
        
    }
    
    private void setEnabled() {

    	boolean enable = true;

    	if(comSchedule.getText().trim().length() > 0) {
    		
    		groupSchedule.setEnabled(true);    		
    		groupStartTimeFuction.setEnabled(false);
    		enable = false;
    		
    	} else if(tFunction.getText().trim().length() > 0) {
    		
    		groupSchedule.setEnabled(false);
    		groupStartTimeFuction.setEnabled(true);
    		enable = false;
    		    		
    	} else {
    		
    		if(runTimeBackUpElem != null) {
    			
    			Element e = listener.getRunTime(); 
    			e.setContent(runTimeBackUpElem.cloneContent());    		    
    			for(int i = 0; i < runTimeBackUpElem.getAttributes().size(); i++) {
    				org.jdom.Attribute attr = (org.jdom.Attribute)runTimeBackUpElem.getAttributes().get(i);
    				e.setAttribute(attr.getName(), attr.getValue(), e.getNamespace());
    			}
    			runTimeBackUpElem = null;
    			
    		}
    		groupSchedule.setEnabled(true);
    		groupStartTimeFuction.setEnabled(true);
    	}

    	    	
    	if(!enable) {
    		if(runTimeBackUpElem == null) {

    			runTimeBackUpElem = (Element)listener.getRunTime().clone();    		
    			listener.getRunTime().removeContent();
    			listener.getRunTime().getAttributes().clear();
    		}    		
    		
    	} 

    	
    	periodForm.setEnabled(enable);    	    	
    	//holidayForm.setEnabled(enable);
    	

    	//setEnableOfChildren(holidayForm, enable);
    	
    	/*for(int i = 0; i < holidayForm.getChildren().length; i++) {
    		holidayForm.getChildren()[i].setEnabled(enable);
    	}*/

    }
    
   /* private void setEnableOfChildren(Composite form, boolean enable) {
    	for(int i = 0; i < form.getChildren().length; i++) {
    		if(form.getChildren()[i] instanceof Composite) {
    			org.eclipse.swt.widgets.Composite c = (Composite)form.getChildren()[i]; 
    			c.setEnabled(enable);    		
    			//if(c instanceof Composite)
    			if(c.getChildren() != null &&  c.getChildren().length > 0)
    				setEnableOfChildren(c, enable) ;
    		}
    	}
    }
*/
} // @jve:decl-index=0:visual-constraint="10,10"
