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

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.listeners.DaysListener;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import org.eclipse.swt.graphics.Point;

public class DaysForm extends SOSJOEMessageCodes implements IUpdateLanguage {
	private DaysListener			listener;
	private ISchedulerUpdate		_main;
	private int						_type				= 0;
	private Group group;
	private Combo					cbUnusedDays			= null;
    private Button                  btAddFromCombo       = null;
    private Button                  btEveryDay           = null;
	private List					lUsedDays			= null;
	private Button					btRemoveFromList	= null;
	private List					listOfDays			= null;
	private List					listOfGroup			= null;
	private Button					btAdd	            = null;
	private Button					btRemove			= null;
	private Button					btApplyGroup		= null;
	private HashMap<String, String>	listOfSameMonths	= new HashMap<String, String>();
	private boolean					newGroup			= false;
	private Button					btNewGroup			= null;


	public DaysForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main, int type, boolean isWeekdaysHolidays) {
		super(parent, SWT.EMBEDDED);
		if (type > 3 || type < 0)
			throw new IllegalArgumentException(JOE_E_0008.label());
		listener = new DaysListener(dom, job, type, isWeekdaysHolidays);

		_main = main;
		_type = type;
		initialize();
		setToolTipText();
		read();

		this.group.setEnabled(Utils.isElementEnabled("job", dom, job) && !Utils.hasSchedulesElement(dom, job));
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
	}

	private void initialize() {
		initMonth();
        this.setLayout(new FillLayout(SWT.HORIZONTAL));
		createGroup();
		//setEnabledGroupElement(false);
		setSize(new Point(400, 230));
	}

	/**
	 * This method initializes group
	 */
	
 
	private void createGroup() {
		
		group = new Group(this, SWT.NONE);
        group.setLayout(new GridLayout(5, false));
		String strT = "";
		switch (_type) {
			case 0:
				strT = JOE_L_DaysForm_Weekday.label(); // Weekday

				break;
			case 1:
				strT = JOE_L_DaysForm_Monthday.label(); // Monthday

				break;
			case 2:
				strT = JOE_L_DaysForm_Ultimo.label(); // Ultimo

				break;
			case 3:
				strT = JOE_L_DaysForm_Month.label(); // Month

				break;

			default:
				strT = JOE_L_DaysForm_Weekday.label(); // Weekday
				break;
		}
 		group.setText(strT);

		strT = "";
		switch (_type) {
			case 0:
				strT = JOE_G_DaysForm_WeekdaysGroup.label(); // Weekdays

				break;
			case 1:
				strT = JOE_G_DaysForm_MonthdaysGroup.label(); // Monthdays

				break;
			case 2:
				strT = JOE_G_DaysForm_UltimosGroup.label(); // Ultimos

				break;
			case 3:
				strT = JOE_G_DaysForm_MonthGroup.label(); // Month

				break;

			default:
				strT = JOE_G_DaysForm_WeekdaysGroup.label(); // Weekdays
				break;
		}
        
		Label lblWeekdays = new Label(group, SWT.NONE);
        lblWeekdays.setText(strT);
        
        GridData gd_cUnusedDays = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        gd_cUnusedDays.widthHint = 150;
        cbUnusedDays = JOE_Cbo_DaysForm_UnusedDays.Control(new Combo(group, SWT.READ_ONLY));
        cbUnusedDays.setVisibleItemCount(10);
        cbUnusedDays.setLayoutData(gd_cUnusedDays);

		btAddFromCombo = new Button(group, SWT.NONE);
			
		strT = "";
		switch (_type) {
			case 0:
				strT = JOE_B_DaysForm_AddWeekday.label(); // &Add Weekday

				break;
			case 1:
				strT = JOE_B_DaysForm_AddMonthday.label(); // &Add Monthday

				break;
			case 2:
				strT = JOE_B_DaysForm_AddUltimo.label(); // &Add Ultimo

				break;
			case 3:
				strT = JOE_B_DaysForm_AddMonth.label(); // &Add Month

				break;

			default:
				strT = JOE_B_DaysForm_AddWeekday.label(); // &Add Weekday
				break;
		}
		
		
		btAddFromCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
 		btAddFromCombo.setText(strT);
		getShell().setDefaultButton(btAddFromCombo);


        btAddFromCombo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                newGroup = false;
                listOfGroup.removeAll();
                btApplyGroup.setEnabled(false);
                listener.addDay(cbUnusedDays.getText());
                _main.updateDays(_type, cbUnusedDays.getText());
                _main.updateFont();
                read();
            }
        });
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);

		
        Label horizontalLine = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
        gd_label.widthHint = 150;
        horizontalLine.setLayoutData(gd_label);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
		
	 
        lUsedDays = JOE_Lst_DaysForm_UsedDays.Control(new List(group, SWT.BORDER));
        GridData gd_lUsedDaysOben = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
        gd_lUsedDaysOben.widthHint = 450;
        gd_lUsedDaysOben.heightHint = 120;
        lUsedDays.setLayoutData(gd_lUsedDaysOben);        
        
		lUsedDays.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listOfGroup.removeAll();
				btRemoveFromList.setEnabled(lUsedDays.getSelectionCount() > 0);
				boolean isGroup = false;
				if (lUsedDays.getSelectionCount() > 0) {

					String[] split = null;
					if (_type == DaysListener.SPECIFIC_MONTHS) {
                        split = lUsedDays.getSelection()[0].split(" ");
					}else{
						if (_type == DaysListener.ULTIMOS) {
							split = listener.getNormalizedUltimos(lUsedDays.getSelection()[0]);
						}else{
                            split = lUsedDays.getSelection()[0].split(" ");
						}
					}

					if (split.length > 0) {
						isGroup = true;
						listOfGroup.setItems(split);
					}
				}

				setEnabledGroupElement(isGroup);
				newGroup = false;
			}
		});
		
        btRemoveFromList = JOE_B_DaysForm_Remove.Control(new Button(group, SWT.NONE));
        btRemoveFromList.setEnabled(false);
        btRemoveFromList.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
 
		btRemoveFromList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				newGroup = false;
				delete();
				listOfGroup.removeAll();
			}
		});

		
        listOfDays = JOE_Lst_DaysForm_DaysList.Control(new List(group, SWT.BORDER));
        listOfDays.setItems(listener.getAllDays());
	    GridData gd_listOfDays = new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 3);
	    gd_listOfDays.minimumWidth = 150;
	    gd_listOfDays.heightHint = 116;
	    gd_listOfDays.widthHint = 150;
	    listOfDays.setLayoutData(gd_listOfDays);		
		
		listOfDays.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				addGroupDay();
			}
		});
		
		
		
	     if (_type == 0) {

             btEveryDay = new Button(group, SWT.NONE);
	         btEveryDay.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
	         btEveryDay.setText("Every Day");
	          
             btEveryDay.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
                   public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                       addEveryDay();
                       applyGroup();
                   }
               });
         }else {
             new Label(group, SWT.NONE);

         }
	     
	     
 
	     
	    listOfGroup = JOE_Lst_DaysForm_GroupsList.Control(new List(group, SWT.BORDER));
	    GridData gd_listOfGroup = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3);
	    gd_listOfGroup.widthHint = 150;
	    gd_listOfGroup.minimumWidth = 150;
	    listOfGroup.setLayoutData(gd_listOfGroup);	     
	 
		listOfGroup.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				removeGroupDay();
				if (listOfGroup.getItemCount() > 0)
					btApplyGroup.setEnabled(true);
			}
		});
 
        btNewGroup = JOE_B_DaysForm_NewGroup.Control(new Button(group, SWT.NONE));
        btNewGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
 
 		btNewGroup.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				newGroup = true;
				setEnabledGroupElement(true);
				listOfGroup.removeAll();
				listOfDays.deselectAll();
			}
		});
		 
        btAdd = JOE_B_DaysForm_Add.Control(new Button(group, SWT.NONE));
        btAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
       
        btAdd.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {
                addGroupDay();
            }
        });
         
        btApplyGroup = JOE_B_DaysForm_ApplyGroup.Control(new Button(group, SWT.NONE));
        btApplyGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
   		
        btApplyGroup.addSelectionListener(new SelectionAdapter() {
              public void widgetSelected(final SelectionEvent e) {
                  applyGroup();
              }
          });
        
        btRemove = JOE_B_DaysForm_RemoveDay.Control(new Button(group, SWT.NONE));
        btRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1));
        new Label(group, SWT.NONE);

		btRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				removeGroupDay();
			}
		});
	
	}

	
  
   private void applyGroup() {

       String group = "";
       for (int i = 0; i < listOfGroup.getItemCount(); i++) {
           group = (group.length() == 0 ? "" : group + " ") + listOfGroup.getItem(i);
       }

       if (!newGroup && lUsedDays.getSelectionCount() > 0) {
           if (_type == DaysListener.WEEKDAYS)
               listener.updateDay(group, lUsedDays.getSelection()[0]);
           else
               listener.updateGroup(group, lUsedDays.getSelection()[0]);

       }
       else {
           listener.addGroup(group);
       }
       _main.updateDays(_type, group);
       _main.updateFont();
       read();
       setEnabledGroupElement(false);
       listOfGroup.removeAll();
       newGroup = false;
   }

	private void read() {

		if (_type == DaysListener.SPECIFIC_MONTHS)
			cbUnusedDays.setItems(listener.getUnUsedMonth());
		else
			cbUnusedDays.setItems(listener.getUnusedDays());

		cbUnusedDays.select(0);

		int index = lUsedDays.getSelectionIndex();

		if (_type == DaysListener.SPECIFIC_MONTHS)
			lUsedDays.setItems(listener.getUsedMonth());
		else
			if (_type == DaysListener.ULTIMOS)
				lUsedDays.setItems(listener.getUsedUltimosDaysInString());
			else
				lUsedDays.setItems(listener.getUsedDaysInString());

		if (index >= lUsedDays.getItemCount())
			index--;
		if (lUsedDays.getItemCount() > 0)
			lUsedDays.setSelection(index);

		btAddFromCombo.setEnabled(cbUnusedDays.getItemCount() > 0);
		btRemoveFromList.setEnabled(lUsedDays.getSelectionCount() > 0);
	}

	public void setEnabledGroupElement(boolean enable) {
		listOfDays.setEnabled(enable);
		listOfGroup.setEnabled(enable);

		btAdd.setEnabled(enable);
		btRemove.setEnabled(enable);

		btApplyGroup.setEnabled(false);
	}

	private void addGroupDay() {

		if (listOfDays.getSelectionCount() > 0) {
			boolean exist = false;
			for (int i = 0; i < listOfGroup.getItemCount(); i++) {
				if (listOfGroup.getItem(i).equals(listOfDays.getSelection()[0])) {
					exist = true;
				}
				if (_type == DaysListener.SPECIFIC_MONTHS) {
					if (listOfSameMonths.get(listOfGroup.getItem(i)).equals(listOfDays.getSelection()[0])) {
						exist = true;
					}
				}
			}
			if (!exist) {
				listOfGroup.add(listOfDays.getSelection()[0]);
			}
		}
		if (listOfGroup.getItemCount() > 0)
			btApplyGroup.setEnabled(true);
		else
			btApplyGroup.setEnabled(false);
	}

	
	private void addEveryDay() {

	    listOfGroup.removeAll();
	    for (int i = 0; i < listOfDays.getItemCount(); i++) {
            listOfGroup.add(listOfDays.getItem(i));
        }
        btApplyGroup.setEnabled(true);
    }
	
	private void removeGroupDay() {

		if (listOfGroup.getSelectionCount() > 0) {
			listOfGroup.remove(listOfGroup.getSelectionIndex());
		}

		if (listOfGroup.getItemCount() > 0)
			btApplyGroup.setEnabled(true);
		else
			btApplyGroup.setEnabled(false);
	}

	private void delete() {

		listener.deleteDay(lUsedDays.getItem(lUsedDays.getSelectionIndex()));
		_main.updateDays(_type);
		_main.updateFont();
		read();
		setEnabledGroupElement(false);
	}
	
	   

	private void initMonth() {

        String strMN[] = DaysListener.getMonth();

		for (int i = 0; i < strMN.length; i++) {
			listOfSameMonths.put(strMN[i], String.valueOf(i + 1));
			listOfSameMonths.put(String.valueOf(i + 1), strMN[i]);
		}
	}

	public void setToolTipText() {
//
	}

} // @jve:decl-index=0:visual-constraint="10,10"
