package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.DateListener;

// TODO implement the app.DateInput class for dates

public class DateForm extends Composite implements IUpdateLanguage {
    private Button bRemove;
    private List lInclude;
    private Label label1_1;
    private Label label_1;
    private Button bAdd;
    private Text tInclude;
    private Label label4;
    private Group gInclude;
    private Button bRemoveDate;
    private List lDates;
    private Label label3;
    private Button bAddDay;
    private Spinner sDay;
    private Label label2;
    private Spinner sMonth;
    private Label label1;
    private Spinner sYear;
    private Label yearLabel;
    private DateListener     listener;

    private int              type;

    private SchedulerDom     dom;

    private ISchedulerUpdate main;

    private static String[]  groupLabel    = { "Holidays", "Specific dates" };

    private static String[]  btnAddTooltip = { "btn_add_holiday", "btn_add_specific_day" };

    private Group            gDates        = null;
    private Group            gIncludeFiles = null;
    


 
    public DateForm(Composite parent, int style, int type) {
        super(parent, style);
        this.type = type;
        initialize();
        gIncludeFiles.setVisible((type == 0));
        setToolTipText();
    }


    public DateForm(Composite parent, int style, int type, SchedulerDom dom, Element element, ISchedulerUpdate main) {
        this(parent, style, type);
        setObjects(dom, element, main);

        setNow();
        this.gDates.setEnabled(Utils.isElementEnabled("job", dom, element));
    }


    public void setObjects(SchedulerDom dom, Element element, ISchedulerUpdate main) {
        listener = new DateListener(dom, element, type);
        listener.fillList(lDates);
        lInclude.setItems(listener.getIncludes());        
        this.main = main;
        this.dom = dom;
        setNow();
    }
 
    private void setNow() {
        int[] now = listener.getNow();
        sYear.setSelection(now[0]);
        sMonth.setSelection(now[1]);
        sDay.setSelection(now[2]);
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(380, 232));
    }


    /**
     * This method initializes group
     */
    private void createGroup() {
        GridLayout gridLayout = new GridLayout();
        gDates = new Group(this, SWT.NONE);
        gDates.setText(groupLabel[type]);
        gDates.setLayout(gridLayout);


        final Group group = new Group(gDates, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 7;
        group.setLayout(gridLayout_1);

        yearLabel = new Label(group, SWT.NONE);
        yearLabel.setLayoutData(new GridData());
        yearLabel.setText("Year:");

        sYear = new Spinner(group, SWT.BORDER);
        final GridData gridData2 = new GridData(40, SWT.DEFAULT);
        sYear.setLayoutData(gridData2);
        sYear.setMinimum(1900);
        sYear.setMaximum(10000);

        label1 = new Label(group, SWT.NONE);
        label1.setText("Month:");

        sMonth = new Spinner(group, SWT.BORDER);
        final GridData gridData21 = new GridData(20, SWT.DEFAULT);
        sMonth.setLayoutData(gridData21);
        sMonth.setMinimum(1);
        sMonth.setMaximum(12);

        label2 = new Label(group, SWT.NONE);
        label2.setText("Day:");

        sDay = new Spinner(group, SWT.BORDER);
        final GridData gridData31 = new GridData(20, SWT.DEFAULT);
        sDay.setLayoutData(gridData31);
        sDay.setMinimum(1);
        sDay.setMaximum(31);

        bAddDay = new Button(group, SWT.NONE);
        bAddDay.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		
        		 int year = sYear.getSelection();
             int month = sMonth.getSelection();
             int day = sDay.getSelection();
             if (listener.exists(year, month, day)) {
                 MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION);
                 mb.setMessage(Messages.getString("date.date_exists"));
                 mb.open();
                 if (main != null && dom.isChanged())
                     main.dataChanged();
             } else {
                 listener.addDate(year, month, day);
                 listener.fillList(lDates);
                 bRemoveDate.setEnabled(false);

                 // update the tree if not holidays
                 if (main != null && type == 1)
                     main.updateDays(3);
             }
        	}
        });
        final GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        bAddDay.setLayoutData(gridData3);
        bAddDay.setText("&Add Date");

        label3 = new Label(group, SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData gridData32 = new GridData(GridData.FILL, GridData.CENTER, false, false, 7, 1);
        gridData32.heightHint = 10;
        label3.setLayoutData(gridData32);
        label3.setText("Label");

        lDates = new List(group, SWT.BORDER);
        lDates.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        	  bRemoveDate.setEnabled(lDates.getSelectionCount() > 0);
        	}
        });
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 6, 2);
        gridData.heightHint = 280;
        lDates.setLayoutData(gridData);

        bRemoveDate = new Button(group, SWT.NONE);
        bRemoveDate.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        	  if (lDates.getSelectionCount() > 0) {
              int index = lDates.getSelectionIndex();
              listener.removeDate(index);
              listener.fillList(lDates);
              if (index >= lDates.getItemCount())
                  index--;
              if (lDates.getItemCount() > 0)
                  lDates.select(index);
              bRemoveDate.setEnabled(lDates.getSelectionCount() > 0);
              if ((main != null) && (type == 1))
                  main.updateDays(3);
        	  }
        	}
        });
        final GridData gridData1 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bRemoveDate.setLayoutData(gridData1);
        bRemoveDate.setEnabled(false);
        bRemoveDate.setText("Remove Date");

        gIncludeFiles = new Group(gDates, SWT.NONE);
        gIncludeFiles.setVisible(false);
        final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData_1.widthHint = 241;
        gIncludeFiles.setLayoutData(gridData_1);
        gIncludeFiles.setLayout(new GridLayout());

        gInclude = new Group(gIncludeFiles, SWT.NONE);
        gInclude.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 3;
        gInclude.setLayout(gridLayout_2);
        gInclude.setText("Include Files");

        label4 = new Label(gInclude, SWT.NONE);
        label4.setText("File:");

        tInclude = new Text(gInclude, SWT.BORDER);
        tInclude.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		 bAdd.setEnabled(!tInclude.getText().equals(""));
        	}
        });
        tInclude.addKeyListener(new KeyAdapter() {
        	public void keyPressed(final KeyEvent e) {
        		 if (e.keyCode == SWT.CR && !tInclude.getText().equals("")) {
               listener.addInclude(tInclude.getText());
               lInclude.setItems(listener.getIncludes());
               tInclude.setText("");
           }
        	}
        });
        final GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData6.widthHint = 50;
        tInclude.setLayoutData(gridData6);

        bAdd = new Button(gInclude, SWT.NONE);
        bAdd.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
        		applyFile();
        	}
        });
        final GridData gridData7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
        bAdd.setLayoutData(gridData7);
        bAdd.setEnabled(false);
        bAdd.setText("&Add File");

        label_1 = new Label(gInclude, SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData gridData1_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
        label_1.setLayoutData(gridData1_1);
        label_1.setText("Label");

        label1_1 = new Label(gInclude, SWT.NONE);
        label1_1.setLayoutData(new GridData());
        label1_1.setVisible(false);
        label1_1.setText("Classname:");

        lInclude = new List(gInclude, SWT.BORDER | SWT.H_SCROLL);
        lInclude.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
            bRemove.setEnabled(lInclude.getSelectionCount() > 0);
        	}
        });
        final GridData gridData4 = new GridData(GridData.FILL, GridData.FILL, true, true);
        lInclude.setLayoutData(gridData4);

        bRemove = new Button(gInclude, SWT.NONE);
        bRemove.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(final SelectionEvent e) {
            if (lInclude.getSelectionCount() > 0) {
              int index = lInclude.getSelectionIndex();
              listener.removeInclude(index);
              lInclude.setItems(listener.getIncludes());
              if (index >= lInclude.getItemCount())
                  index--;
              if (lInclude.getItemCount() > 0)
                  lInclude.setSelection(index);
          }        		
        	}
        });
        final GridData gridData5 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
        bRemove.setLayoutData(gridData5);
        bRemove.setEnabled(false);
        bRemove.setText("Remove File");
 
    }

    private void applyFile() {
       listener.addInclude(tInclude.getText());
       lInclude.setItems(listener.getIncludes());
       tInclude.setText("");
       tInclude.setFocus();
    }

    public void setToolTipText() {
    }

} // @jve:decl-index=0:visual-constraint="10,10"
