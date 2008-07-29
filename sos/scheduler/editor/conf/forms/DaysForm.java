package sos.scheduler.editor.conf.forms;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jdom.Element;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.DaysListener;

public class DaysForm extends Composite implements IUpdateLanguage {
    private DaysListener     listener;

    private ISchedulerUpdate _main;

    private int              _type        = 0;

    private static String[]  _groupLabel  = { "Weekdays", "Monthdays", "Ultimos", "Month" };

    private static String[]  _dayLabel    = { "Weekday:", "Monthday:", "Ultimo:", "Month" };

    private static String[]  _addLabel    = { "&Add Weekday", "&Add Monthday", "&Add Ultimo", "&Add Month" };

    //private static String[]  _removeLabel = { "Remove Weekday", "Remove Monthday", "Remove Ultimo", "Remove Month" };
    
    private static String[]  _removeLabel = { "Remove", "Remove", "Remove", "Remove" };

    // private static String[] _listLabel = {"Used Weekdays:", "Used
    // Monthdays:", "Used Ultimos:"};

    private Group            group        = null;

    private Label            label        = null;

    private Combo            cUnusedDays  = null;

    private Button           bAdd         = null;

    private List             lUsedDays    = null;

    private Button           bRemove      = null;

    private Label            label2       = null;
    
    private List             listOfDays   = null; 
    
    private List             listOfGroup  = null;
    
    private Button           butAdd       = null; 
    
    private Button           butRemove    = null; 
    
    private Button           butApplyGroup = null; 
    
    private boolean          newGroup      = false;
    
    private HashMap          listOfSameMonths = null;
    
    private Button           butNewGroup   = null; 
    
    private Element          parent_        = null;
    
    //private SchedulerDom     _dom                = null;


    public DaysForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main, int type) {
        super(parent, style);        
        if (type > 3 || type < 0)
            throw new IllegalArgumentException("the type must be from 0 to 2 or 6");

        listener = new DaysListener(dom, job, type);
        _main = main;
        parent_ = job;
        _type = type;
        //_dom= dom;
        initialize();
        setToolTipText();
        read();
        
        this.group.setEnabled(Utils.isElementEnabled("job", dom, job) && !Utils.hasSchedulesElement(dom, job));
        //this.group.setEnabled(Utils.isElementEnabled("job", dom, job));
        
        
    }


    private void initialize() {
    	initMonth();
        this.setLayout(new FillLayout());
        createGroup();
        setEnabledGroupElement(false);
        setSize(new org.eclipse.swt.graphics.Point(443, 312));
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData5 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
        gridData5.heightHint = 10;
        GridData gridData3 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, true);
        GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData2.widthHint = 90;
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 4, 1);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        group = new Group(this, SWT.NONE);
        group.setText(_groupLabel[_type]);
        group.setLayout(gridLayout);
        label = new Label(group, SWT.NONE);
        label.setText(_dayLabel[_type]);
        createCombo();
        bAdd = new Button(group, SWT.NONE);
        bAdd.setText(_addLabel[_type]);
        bAdd.setLayoutData(gridData2);
        getShell().setDefaultButton(bAdd);

        label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        label2.setText("Label");
        label2.setLayoutData(gridData5);

        bAdd.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	newGroup = false;
            	listOfGroup.removeAll();
            	butApplyGroup.setEnabled(false);
                listener.addDay(cUnusedDays.getText()); 
                _main.updateDays(_type, cUnusedDays.getText());                
                _main.updateFont();    
                //if(parent_.getName().equals("config"))
                //	_main.expandItem("Weekdays");
                read();
            }
        });
        lUsedDays = new List(group, SWT.BORDER);
        lUsedDays.setLayoutData(gridData);
        lUsedDays.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	listOfGroup.removeAll();
                bRemove.setEnabled(lUsedDays.getSelectionCount() > 0);
                boolean isGroup = false;
                if(lUsedDays.getSelectionCount() > 0) {
                	//String[] split = lUsedDays.getSelection()[0].split(" ");
                	
                	String[] split = null;
                	if(_type == DaysListener.SPECIFIC_MONTHS)
                		split = lUsedDays.getSelection()[0].split(" ");
                    else if(_type == DaysListener.ULTIMOS)
                    	split = listener.getNormalizedUltimos(lUsedDays.getSelection()[0]);
                    else
                    	split = lUsedDays.getSelection()[0].split(" ");
                	
                	if(split.length > 0) {
                		isGroup = true;                		
                		listOfGroup.setItems(split);                		
                	} 
                }
                
                setEnabledGroupElement(isGroup);
                newGroup= false;
            }
        });
        bRemove = new Button(group, SWT.NONE);
        bRemove.setText(_removeLabel[_type]);
        bRemove.setEnabled(false);
        bRemove.setLayoutData(gridData3);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            	newGroup = false;            	
            	delete();
            	listOfGroup.removeAll();
            }
        });

        listOfDays = new List(group, SWT.BORDER);
        listOfDays.setItems(listener.getAllDays());
        listOfDays.addMouseListener(new MouseAdapter() {
        	public void mouseDoubleClick(final MouseEvent e) {
        		addGroupDay();
        	}
        });
        listOfDays.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 3));
        new Label(group, SWT.NONE);

        listOfGroup = new List(group, SWT.BORDER);
        listOfGroup.addMouseListener(new MouseAdapter() {
        	public void mouseDoubleClick(final MouseEvent e) {
        		removeGroupDay();
        		if(listOfGroup.getItemCount() > 0)
                 	butApplyGroup.setEnabled(true); 
        	}
        });
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, false, 1, 3);
        gridData_1.heightHint = 95;
        listOfGroup.setLayoutData(gridData_1);

        butNewGroup = new Button(group, SWT.NONE);
        butNewGroup.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		newGroup = true;
        		setEnabledGroupElement(true);
        		listOfGroup.removeAll();
        		listOfDays.deselectAll();
        	}
        });
        butNewGroup.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butNewGroup.setText("New Group");

        butAdd = new Button(group, SWT.NONE);
        butAdd.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {        		
        		addGroupDay();        		
        	}
        });
        butAdd.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
        butAdd.setText("Add");

        butApplyGroup = new Button(group, SWT.NONE);
        butApplyGroup.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		        		
        		String group = "";
        		for (int i =0; i < listOfGroup.getItemCount(); i++) {
        			group = (group.length()==0 ? "" : group + " ") + listOfGroup.getItem(i);
        		}        		        		        	      		        	
        		
        		if(!newGroup && lUsedDays.getSelectionCount() > 0) {  
        			if(_type == DaysListener.WEEKDAYS)
        				listener.updateDay(group, lUsedDays.getSelection()[0]);
        			else 
        				listener.updateGroup(group, lUsedDays.getSelection()[0]);
        			
        		} else {
        			listener.addGroup(group);
        		}
        		_main.updateDays(_type, group);
        		_main.updateFont();
                read();
        		setEnabledGroupElement(false);        		        		         		
        		listOfGroup.removeAll();
        		newGroup = false;
        	}
        });
        butApplyGroup.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        butApplyGroup.setText("Apply Group");

        butRemove = new Button(group, SWT.NONE);
        butRemove.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		removeGroupDay();      
        		
        	}
        });
        butRemove.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, true));
        butRemove.setText("Remove");
        new Label(group, SWT.NONE);
    }


    /**
     * This method initializes combo
     */
    private void createCombo() {
        GridData gridData4 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1);
        gridData4.widthHint = 300;
        cUnusedDays = new Combo(group, SWT.READ_ONLY);
        cUnusedDays.setVisibleItemCount(10);
        cUnusedDays.setLayoutData(gridData4);
    }


    private void read() {
    	
    	if(_type == DaysListener.SPECIFIC_MONTHS)
    		cUnusedDays.setItems(listener.getUnUsedMonth());
    	else
    		cUnusedDays.setItems(listener.getUnusedDays());
    	
        cUnusedDays.select(0);

        int index = lUsedDays.getSelectionIndex();
        
        if(_type == DaysListener.SPECIFIC_MONTHS)
        	lUsedDays.setItems(listener.getUsedMonth());
        else if(_type == DaysListener.ULTIMOS)
        	lUsedDays.setItems(listener.getUsedUltimosDaysInString());
        else
        	lUsedDays.setItems(listener.getUsedDaysInString());
        
        
        if (index >= lUsedDays.getItemCount())
            index--;
        if (lUsedDays.getItemCount() > 0)
            lUsedDays.setSelection(index);

        bAdd.setEnabled(cUnusedDays.getItemCount() > 0);
        bRemove.setEnabled(lUsedDays.getSelectionCount() > 0);
    }


    

    public void setEnabledGroupElement(boolean enable) {
    	listOfDays.setEnabled(enable); 
        listOfGroup.setEnabled(enable);   
        
        butAdd.setEnabled(enable);          
        butRemove.setEnabled(enable); 
        
        butApplyGroup.setEnabled(false); 
    }
    
    private void addGroupDay() {    	 
    	 
    	if(listOfDays.getSelectionCount() > 0) {
    		boolean exist = false;
    		for (int i = 0; i < listOfGroup.getItemCount(); i++) {
    			if(listOfGroup.getItem(i).equals(listOfDays.getSelection()[0])) {
    				exist = true;
    			}  
    			if(_type == DaysListener.SPECIFIC_MONTHS) {
    				if(listOfSameMonths.get(listOfGroup.getItem(i)).equals(listOfDays.getSelection()[0])) {
    					exist = true;
    				}
    			}
    		}
    		if(!exist) {
    			listOfGroup.add(listOfDays.getSelection()[0]);
    		}
    	}
    	if(listOfGroup.getItemCount() > 0)
         	butApplyGroup.setEnabled(true); 
    	else 
    		butApplyGroup.setEnabled(false);
    }
    
    private void removeGroupDay() {
    	
    	if(listOfGroup.getSelectionCount() > 0) {
    		listOfGroup.remove(listOfGroup.getSelectionIndex());
    	}
    	
    	if(listOfGroup.getItemCount() > 0)
         	butApplyGroup.setEnabled(true); 
    	else 
    		butApplyGroup.setEnabled(false);
    }
    
    private void delete () {
    	
        listener.deleteDay(lUsedDays.getItem(lUsedDays.getSelectionIndex()));
        _main.updateDays(_type);
        _main.updateFont();
        read();
        setEnabledGroupElement(false);
    }
    

    
    private void initMonth() {    	
    	listOfSameMonths = new HashMap();
    	listOfSameMonths.put("january", "1");
    	listOfSameMonths.put("february", "2");
    	listOfSameMonths.put("march", "3");
    	listOfSameMonths.put("april", "4");
    	listOfSameMonths.put("may", "5"); 
    	listOfSameMonths.put("june", "6"); 
    	listOfSameMonths.put("july", "7"); 
    	listOfSameMonths.put("august", "8"); 
    	listOfSameMonths.put("september", "9"); 
    	listOfSameMonths.put("october", "10"); 
    	listOfSameMonths.put("november", "11"); 
    	listOfSameMonths.put("december", "12"); 
    	listOfSameMonths.put("1", "january"); 
    	listOfSameMonths.put("2", "february"); 
    	listOfSameMonths.put("3", "march"); 
    	listOfSameMonths.put("4", "april"); 
    	listOfSameMonths.put("5", "may"); 
    	listOfSameMonths.put("6", "june"); 
    	listOfSameMonths.put("7", "july"); 
    	listOfSameMonths.put("8", "august"); 
    	listOfSameMonths.put("9", "september"); 
    	listOfSameMonths.put("10", "october"); 
    	listOfSameMonths.put("11", "november"); 
    	listOfSameMonths.put("12", "december");
    }
    
    public void setToolTipText() {
        bAdd.setToolTipText(Messages.getTooltip("days.btn_add"));
        lUsedDays.setToolTipText(Messages.getTooltip("days.used_days"));
        bRemove.setToolTipText(Messages.getTooltip("days.btn_remove"));
        cUnusedDays.setToolTipText(Messages.getTooltip("days.unused_days"));

        listOfDays.setToolTipText(Messages.getTooltip("days.list_of_days"));
        listOfGroup.setToolTipText(Messages.getTooltip("days.list_of_groups"));
        butAdd.setToolTipText(Messages.getTooltip("days.list_of_groups.but_add"));
        butRemove.setToolTipText(Messages.getTooltip("days.list_of_groups.but_remove"));
        butNewGroup.setToolTipText(Messages.getTooltip("days.list_of_groups.but_new_group"));
        butApplyGroup.setToolTipText(Messages.getTooltip("days.btn_group"));
    }
    
} // @jve:decl-index=0:visual-constraint="10,10"
