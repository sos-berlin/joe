package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
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

import com.sos.joe.interfaces.ISchedulerUpdate;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.DaysListener;
import sos.scheduler.editor.conf.listeners.SpecificWeekdaysListener;

public class SpecificWeekdaysForm extends SOSJOEMessageCodes implements IUpdateLanguage {
	
	
    private SpecificWeekdaysListener     listener        = null;

    private ISchedulerUpdate             _main           = null;      

    private Group                        group           = null;
 
    private Combo                        cWeekdays       = null;

    private Button                       bAdd            = null;

    private List                         lUsedDays       = null;

    private Button                       bRemove         = null;

    private Label                        label2          = null;
    
    private Combo                        cWeekdayNumber  = null;


    public SpecificWeekdaysForm(Composite parent, int style, SchedulerDom dom, Element job, ISchedulerUpdate main, int type) {
        super(parent, style);
     
        listener = new SpecificWeekdaysListener(dom, job);
        _main = main;

        initialize();
        setToolTipText();
        lUsedDays.setItems(listener.getDays());
        
        this.group.setEnabled(Utils.isElementEnabled("job", dom, job));
     
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(443, 312));
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridData gridData5 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
        gridData5.heightHint = 10;
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        
        group = JOE_G_SpecificWeekdaysForm_Monthdays.Control(new Group(this, SWT.NONE));
        group.setLayout(gridLayout);
 

        createCombo();
        
        GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData2.widthHint = 90;
        bAdd = JOE_B_SpecificWeekdaysForm_AddWeekday.Control(new Button(group, SWT.NONE));
        bAdd.setLayoutData(gridData2);
        getShell().setDefaultButton(bAdd);
        bAdd.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.addDay(cWeekdays.getText() ,cWeekdayNumber.getText());
                _main.updateSpecificWeekdays();
                _main.updateFont();
                String s = cWeekdayNumber.getText() + "." + cWeekdays.getText();
                if (lUsedDays.indexOf(s) == -1) lUsedDays.add(s);
                bRemove.setEnabled(lUsedDays.getSelectionCount() > 0);
            }
        });

        label2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
//      label2.setText("Label");
        label2.setLayoutData(gridData5);
        
        lUsedDays = JOE_Lst_SpecificWeekdaysForm_UsedDays.Control(new List(group, SWT.BORDER));
        lUsedDays.setLayoutData(gridData);
        lUsedDays.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                bRemove.setEnabled(lUsedDays.getSelectionCount() > 0);
            }
        });
        
        bRemove = JOE_B_SpecificWeekdaysForm_RemoveWeekday.Control(new Button(group, SWT.NONE));
        bRemove.setEnabled(false);
        bRemove.setLayoutData(gridData3);
        bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                listener.deleteDay(lUsedDays.getItem(lUsedDays.getSelectionIndex()));
                _main.updateFont();
                lUsedDays.remove(lUsedDays.getSelectionIndex());
                _main.updateSpecificWeekdays();
                bRemove.setEnabled(lUsedDays.getSelectionCount() > 0);
            }
        });
    }


    /**
     * This method initializes combo
     */
    private void createCombo() {
        GridData gridData4 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData4.widthHint = 300;

        cWeekdayNumber = JOE_Cbo_SpecificWeekdaysForm_Daynames.Control(new Combo(group, SWT.NONE));
        cWeekdayNumber.setItems(SpecificWeekdaysListener._daynames);
        cWeekdayNumber.setVisibleItemCount(8);
        cWeekdayNumber.select(0);
        cWeekdayNumber.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        
//        String[] strWeekdays = JOE_M_SpecificWeekdaysForm_Weekdays.label().split(";");
        String[] strWeekdays = DaysListener.getWeekdays();
        cWeekdays = JOE_Cbo_SpecificWeekdaysForm_Weekdays.Control(new Combo(group, SWT.READ_ONLY));
        cWeekdays.setItems(strWeekdays);
        cWeekdays.setText(strWeekdays[0]);
        cWeekdays.setVisibleItemCount(7);
        cWeekdays.setLayoutData(gridData4);
    }




    public void setToolTipText() {
//
    }

} // @jve:decl-index=0:visual-constraint="10,10"
