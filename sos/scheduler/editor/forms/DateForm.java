package sos.scheduler.editor.forms;

import org.eclipse.swt.SWT;
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
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.listeners.DateListener;

public class DateForm extends Composite implements IUpdateLanguage {
	private DateListener listener;

	private int type;
	
	private DomParser dom;

	private IUpdate main;

	private static String[] groupLabel = { "Holidays", "Specific dates" };
	
	private static String[] btnAddTooltip = {"btn_add_holiday", "btn_add_specific_day"};

	private Group gDates = null;

	private List lDates = null;

	private Button bRemoveDate = null;

	private Label label = null;

	private Spinner sYear = null;

	private Label label1 = null;

	private Spinner sMonth = null;

	private Label label2 = null;

	private Spinner sDay = null;

	private Button bAddDay = null;

	private Label label3 = null;

	public DateForm(Composite parent, int style, int type) {
		super(parent, style);
		this.type = type;
		initialize();
    setToolTipText();		
	}

	public DateForm(Composite parent, int style, int type, DomParser dom,
			Element element, IUpdate main) {
		this(parent, style, type);
		setObjects(dom, element, main);

		setNow();
	}

	public void setObjects(DomParser dom, Element element, IUpdate main) {
		listener = new DateListener(dom, element, type);
		listener.fillList(lDates);
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
	 * 
	 */
	private void createGroup() {
		GridData gridData32 = new org.eclipse.swt.layout.GridData();
		gridData32.horizontalSpan = 7;
		gridData32.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData32.heightHint = 10;
		gridData32.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData31 = new org.eclipse.swt.layout.GridData();
		gridData31.widthHint = 20;
		GridData gridData21 = new org.eclipse.swt.layout.GridData();
		gridData21.widthHint = 20;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData2 = new GridData();
		gridData2.widthHint = 40;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 6;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 7;
		gDates = new Group(this, SWT.NONE);
		gDates.setText(groupLabel[type]);
		gDates.setLayout(gridLayout);
		label = new Label(gDates, SWT.NONE);
		label.setText("Year:");
		sYear = new Spinner(gDates, SWT.NONE);
		label1 = new Label(gDates, SWT.NONE);
		label1.setText("Month:");
		sMonth = new Spinner(gDates, SWT.NONE);
		label2 = new Label(gDates, SWT.NONE);
		label2.setText("Day:");
		sDay = new Spinner(gDates, SWT.NONE);
		bAddDay = new Button(gDates, SWT.NONE);
		label3 = new Label(gDates, SWT.SEPARATOR | SWT.HORIZONTAL);
		label3.setText("Label");
		label3.setLayoutData(gridData32);
		lDates = new List(gDates, SWT.BORDER);
		lDates.setLayoutData(gridData);
		lDates
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						bRemoveDate.setEnabled(lDates.getSelectionCount() > 0);
					}
				});
		bRemoveDate = new Button(gDates, SWT.NONE);
		bRemoveDate.setText("Remove Date");
		bRemoveDate.setEnabled(false);
		bRemoveDate.setLayoutData(gridData1);
		bRemoveDate
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (lDates.getSelectionCount() > 0) {
							int index = lDates.getSelectionIndex();
							listener.removeDate(index);
							listener.fillList(lDates);
							if (index >= lDates.getItemCount())
								index--;
							if (lDates.getItemCount() > 0)
								lDates.select(index);
							bRemoveDate
									.setEnabled(lDates.getSelectionCount() > 0);
							if ((main != null) && (type == 1))
								main.updateDays (3);
						}
					}
				});
		sYear.setMaximum(10000);
		sYear.setLayoutData(gridData2);
		sYear.setMinimum(1900);
		sMonth.setMaximum(12);
		sMonth.setMinimum(1);
		sMonth.setLayoutData(gridData21);
		sDay.setMaximum(31);
		sDay.setMinimum(1);
		sDay.setLayoutData(gridData31);
		bAddDay.setText("&Add Date");
		bAddDay.setLayoutData(gridData3);
		getShell().setDefaultButton(bAddDay);

		bAddDay
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						int year = sYear.getSelection();
						int month = sMonth.getSelection();
						int day = sDay.getSelection();
						if (listener.exists(year, month, day)) {
							MessageBox mb = new MessageBox(getShell(),
									SWT.ICON_INFORMATION);
							mb.setMessage(Messages
									.getString("date.date_exists"));
							mb.open();
							if(main != null && dom.isChanged())
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
	}
	 public void setToolTipText(){
			lDates.setToolTipText(Messages.getTooltip("date.list"));
			bRemoveDate.setToolTipText(Messages.getTooltip("date.btn_remove"));
			sYear.setToolTipText(Messages.getTooltip("date.year"));
			sMonth.setToolTipText(Messages.getTooltip("date.month"));
			sDay.setToolTipText(Messages.getTooltip("date.day"));
			bAddDay.setToolTipText(Messages.getTooltip("date." + btnAddTooltip[type]));
	  }

} // @jve:decl-index=0:visual-constraint="10,10"
