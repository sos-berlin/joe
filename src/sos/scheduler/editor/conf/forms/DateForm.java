package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TableColumn;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.sos.joe.interfaces.ISchedulerUpdate;

import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.SOSJOEMessageCodes;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.DateListener;
import sos.scheduler.editor.conf.listeners.DaysListener;
import sos.scheduler.editor.app.Options;

public class DateForm extends SOSJOEMessageCodes implements IUpdateLanguage {

	private Table				tableIncludes	= null;

	private Button				butIsLifeFile	= null;

	private Button				bRemove			= null;

	private Label				label_1			= null;

	private Button				bAdd			= null;

	private Combo				tInclude		= null;

	private Group				gInclude		= null;

	private Button				bRemoveDate		= null;

	private List				lDates			= null;

	private Label				label3			= null;

	private Button				bAddDay			= null;

	private Spinner				sDay			= null;

	private Label				label2			= null;

	private Spinner				sMonth			= null;

	private Label				label1			= null;

	private Spinner				sYear			= null;

	private Label				yearLabel		= null;

	private DateListener		listener		= null;

	private int					type			= -1;

	private SchedulerDom		dom				= null;

	private ISchedulerUpdate	main			= null;

	private static String[]		groupLabel		= { "Holidays", "Specific dates" };

	private Group				gDates			= null;

	private Button				butOpenInclude	= null;

	public DateForm(Composite parent, int style, int type) {
		super(parent, style);
		this.type = type;
		initialize();
		setToolTipText();
	}

	public DateForm(Composite parent, int style, int type, SchedulerDom dom, Element element, ISchedulerUpdate main) {
		this(parent, style, type);
		setObjects(dom, element, main);

		setNow();
		// this.gDates.setEnabled(Utils.isElementEnabled("job", dom, element));
		this.gDates.setEnabled(Utils.isElementEnabled("job", dom, element) && !Utils.hasSchedulesElement(dom, element));
		bAddDay.setFocus();
	}

	public void setObjects(SchedulerDom dom, Element element, ISchedulerUpdate main) {
		listener = new DateListener(dom, element, type);
		listener.fillList(lDates);
		if (type == 0)
			// lInclude.setItems(listener.getIncludes());
			listener.fillTable(tableIncludes);
		this.main = main;
		this.dom = dom;
		if (type == 0)
			tInclude.setItems(listener.getHolidayDescription());
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
		String strT = "";
		switch (type) {
			case 0:
				strT = JOE_G_DateForm_Holiday.label();

				break;
			case 1:
				strT = JOE_G_DateForm_Specific.label();

				break;

			default:
				strT = JOE_G_DateForm_Holiday.label();
				break;
		}

		// gDates.setText(groupLabel[type]);
		gDates.setText(strT);
		gDates.setLayout(gridLayout);

		final Group group = JOE_G_DateForm_Dates.Control(new Group(gDates, SWT.NONE));
		group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 7;
		group.setLayout(gridLayout_1);

		yearLabel = JOE_L_DateForm_Year.Control(new Label(group, SWT.NONE));
		yearLabel.setLayoutData(new GridData());
//		yearLabel.setText("Year:");

		sYear = JOE_Sp_DateForm_Year.Control(new Spinner(group, SWT.BORDER));
		final GridData gridData2 = new GridData(40, SWT.DEFAULT);
		sYear.setLayoutData(gridData2);
		sYear.setMinimum(1900);
		sYear.setMaximum(10000);

		label1 = JOE_L_DateForm_Month.Control(new Label(group, SWT.NONE));
//		label1.setText("Month:");

		sMonth = JOE_Sp_DateForm_Month.Control(new Spinner(group, SWT.BORDER));
		final GridData gridData21 = new GridData(20, SWT.DEFAULT);
		sMonth.setLayoutData(gridData21);
		sMonth.setMinimum(1);
		sMonth.setMaximum(12);

		label2 = JOE_L_DateForm_Day.Control(new Label(group, SWT.NONE));
//		label2.setText("Day:");

		sDay = JOE_Sp_DateForm_Day.Control(new Spinner(group, SWT.BORDER));
		final GridData gridData31 = new GridData(20, SWT.DEFAULT);
		sDay.setLayoutData(gridData31);
		sDay.setMinimum(1);
		sDay.setMaximum(31);

		bAddDay = JOE_B_DateForm_AddDate.Control(new Button(group, SWT.NONE));
		bAddDay.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

				int year = sYear.getSelection();
				int month = sMonth.getSelection();
				int day = sDay.getSelection();

				String sDate = listener.asStr(year) + "-" + listener.asStr(month) + "-" + listener.asStr(day);
				try {
					sos.util.SOSDate.getDate(sDate);
				}
				catch (Exception ex) {
//					MainWindow.message(sDate + " is not a valid Date: " + ex.getMessage(), SWT.ICON_ERROR);
					MainWindow.message(JOE_M_0015.params(sDate, ex.getMessage()), SWT.ICON_ERROR);
					return;
				}
				if (listener.exists(year, month, day)) {
					MessageBox mb = JOE_M_0014.Control(new MessageBox(getShell(), SWT.ICON_INFORMATION));
//					mb.setMessage(Messages.getString("date.date_exists"));
					mb.open();
					if (main != null && dom.isChanged())
						main.dataChanged();
				}
				else {
					listener.addDate(year, month, day);
					listener.fillList(lDates);
					bRemoveDate.setEnabled(false);

					// update the tree if not holidays
					if (main != null && type == DateListener.DATE)
						main.updateDays(DaysListener.SPECIFIC_DAY);
					// main.updateDays(DateListener.DATE);

					if (type == DateListener.DATE && main != null) {
						main.updateFont();
					}

				}
			}
		});
		final GridData gridData3 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		bAddDay.setLayoutData(gridData3);
//		bAddDay.setText("&Add Date");

		label3 = new Label(group, SWT.HORIZONTAL | SWT.SEPARATOR);
		final GridData gridData32 = new GridData(GridData.FILL, GridData.CENTER, false, false, 7, 1);
		gridData32.heightHint = 10;
		label3.setLayoutData(gridData32);
//		label3.setText("Label");

		lDates = JOE_Lst_DateForm_DatesList.Control(new List(group, SWT.BORDER));
		lDates.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				bRemoveDate.setEnabled(lDates.getSelectionCount() > 0);
			}
		});
		final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 6, 2);
		lDates.setLayoutData(gridData);

		bRemoveDate = JOE_B_DateForm_RemoveDate.Control(new Button(group, SWT.NONE));
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
						main.updateDays(DaysListener.SPECIFIC_DAY);
					if (type == DateListener.DATE && main != null) {
						main.updateFont();
					}
				}
			}
		});
		final GridData gridData1 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		bRemoveDate.setLayoutData(gridData1);
		bRemoveDate.setEnabled(false);
//		bRemoveDate.setText("Remove Date");
		if (type == 0)
			createGroupForIncludes();

	}

	private void applyFile() {
		listener.addInclude(tableIncludes, tInclude.getText(), butIsLifeFile.getSelection());
		listener.fillTable(tableIncludes);

		tInclude.setText("");
		tInclude.setFocus();
		tableIncludes.deselectAll();
		butOpenInclude.setEnabled(false);
		bRemove.setEnabled(false);
		butIsLifeFile.setSelection(false);
	}

	private void createGroupForIncludes() {
		gInclude = JOE_G_DateForm_IncludeFiles.Control(new Group(gDates, SWT.NONE));
		gInclude.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 3;
		gInclude.setLayout(gridLayout_2);
//		gInclude.setText("Include Files");

		butIsLifeFile = JOE_B_DateForm_IsLifeFile.Control(new Button(gInclude, SWT.CHECK));
		butIsLifeFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		butIsLifeFile.setLayoutData(new GridData());
//		butIsLifeFile.setText("from Hot Folder");

		tInclude = JOE_Cbo_DateForm_Include.Control(new Combo(gInclude, SWT.BORDER));
		tInclude.setCapture(true);
		tInclude.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				butIsLifeFile.setSelection(false);
			}
		});
		tInclude.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				bAdd.setEnabled(!tInclude.getText().equals(""));
			}
		});
		tInclude.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR && !tInclude.getText().equals("")) {
					listener.addInclude(tableIncludes, tInclude.getText(), butIsLifeFile.getSelection());
					listener.fillTable(tableIncludes);
					tInclude.setText("");
				}
			}
		});
		final GridData gridData6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		tInclude.setLayoutData(gridData6);

		bAdd = JOE_B_DateForm_AddFile.Control(new Button(gInclude, SWT.NONE));
		bAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				applyFile();
			}
		});
		final GridData gridData7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		bAdd.setLayoutData(gridData7);
		bAdd.setEnabled(false);
//		bAdd.setText("&Add File");

		label_1 = JOE_Sep_DateForm_S2.Control(new Label(gInclude, SWT.HORIZONTAL | SWT.SEPARATOR));
		final GridData gridData1_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
		label_1.setLayoutData(gridData1_1);
//		label_1.setText("Label");

		tableIncludes = JOE_Tbl_DateForm_Includes.Control(new Table(gInclude, SWT.FULL_SELECTION | SWT.BORDER));
		tableIncludes.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				openInclude();
			}
		});
		tableIncludes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tableIncludes.getSelectionCount() > 0) {
					bRemove.setEnabled(true);
					butOpenInclude.setEnabled(true);
				}
				else {
					bRemove.setEnabled(false);
					butOpenInclude.setEnabled(false);
					return;
				}

				if (tableIncludes.getSelection()[0].getText(2) != null && tableIncludes.getSelection()[0].getText(2).length() > 0)
					tInclude.setText(tableIncludes.getSelection()[0].getText(2));
				else
					tInclude.setText(tableIncludes.getSelection()[0].getText(0));
				butIsLifeFile.setSelection(tableIncludes.getSelection()[0].getText(1).equalsIgnoreCase("live_file"));
				/*Object fname = Options.getHolidaysDescription().get(tableIncludes.getSelection()[0].getText());
				if(fname != null && fname.toString().length() > 0)
					tInclude.setText(fname.toString());
					*/

			}
		});
		tableIncludes.setLinesVisible(true);
		tableIncludes.setHeaderVisible(true);
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 3);
		tableIncludes.setLayoutData(gridData_2);

		final TableColumn newColumnTableColumn = JOE_TCl_DateForm_NameColumn.Control(new TableColumn(tableIncludes, SWT.NONE));
		newColumnTableColumn.setWidth(200);
//		newColumnTableColumn.setText("Name");

		/*	Wir nehmen diese Spalte erstmal raus, weil nicht klar ist, wie die description zu stande kommt.ur20120723
		 * final TableColumn newColumnTableColumn_1 = new TableColumn(tableIncludes, SWT.NONE);
			newColumnTableColumn_1.setWidth(67);
			newColumnTableColumn_1.setText("File/Life File");
			
//			final TableColumn newColumnTableColumn_1 = JOE_TCl_DateForm_FileColumn.Control(new TableColumn(tableIncludes, SWT.NONE));
//			newColumnTableColumn_1.setWidth(67);
		*/

		final Button butIncludeNew = JOE_B_DateForm_NewButton.Control(new Button(gInclude, SWT.NONE));
		butIncludeNew.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				butOpenInclude.setEnabled(false);
				butIsLifeFile.setSelection(false);
				tInclude.setText("");
				tableIncludes.deselectAll();
				bRemove.setEnabled(false);

			}
		});
		butIncludeNew.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
//		butIncludeNew.setText("New");

		butOpenInclude = JOE_B_DateForm_OpenButton.Control(new Button(gInclude, SWT.NONE));
		butOpenInclude.setEnabled(false);
		butOpenInclude.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				openInclude();

			}
		});
		butOpenInclude.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
//		butOpenInclude.setText("Open");

		bRemove = JOE_B_DateForm_RemoveFile.Control(new Button(gInclude, SWT.NONE));
		bRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				removeInclude();
				/*
				if (tableIncludes.getSelectionCount() > 0) {
					int index = tableIncludes.getSelectionIndex();
					listener.removeInclude(index);					
					listener.fillTable(tableIncludes);
					if (index >= tableIncludes.getItemCount())
						index--;
					if (tableIncludes.getItemCount() > 0)
						tableIncludes.setSelection(index);
				} 
				*/
			}
		});
		final GridData gridData5 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		bRemove.setLayoutData(gridData5);
		bRemove.setEnabled(false);
		butOpenInclude.setEnabled(false);
//		bRemove.setText("Remove File");
	}

	public void setToolTipText() {
//
//		sYear.setToolTipText(Messages.getTooltip("date.year"));
//		sMonth.setToolTipText(Messages.getTooltip("date.month"));
//		sDay.setToolTipText(Messages.getTooltip("date.day"));
//		bAddDay.setToolTipText(Messages.getTooltip("date.btn_add_holiday"));
//		lDates.setToolTipText(Messages.getTooltip("date.list"));
//
//		bRemoveDate.setToolTipText(Messages.getTooltip("date.btn_remove"));
//		if (butIsLifeFile != null)
//			butIsLifeFile.setToolTipText(Messages.getTooltip("is_live_file"));
//		if (tInclude != null)
//			tInclude.setToolTipText(Messages.getTooltip("include.file"));
//		if (bAdd != null)
//			bAdd.setToolTipText(Messages.getTooltip("include.add"));
//		if (tableIncludes != null)
//			tableIncludes.setToolTipText(Messages.getTooltip("include.list"));
//		if (bRemove != null)
//			bRemove.setToolTipText(Messages.getTooltip("include.remove"));

	}

	private void removeInclude() {
		if (tableIncludes.getSelectionCount() > 0) {
			int index = tableIncludes.getSelectionIndex();
			listener.removeInclude(index);
			listener.fillTable(tableIncludes);
			if (index >= tableIncludes.getItemCount())
				index--;
			if (tableIncludes.getItemCount() > 0)
				tableIncludes.setSelection(index);
			tInclude.setText("");
		}
	}

	private void openInclude() {

		try {

			if (tableIncludes.getSelectionCount() == 0)
				return;

			String filename = tableIncludes.getSelection()[0].getText(0);
			if (butIsLifeFile.getSelection()) {

				filename = Options.getSchedulerNormalizedHotFolder() + filename;
			}

			if (!(new java.io.File(filename).exists())) {

				filename = Options.getSchedulerNormalizedHome() + "config/" + filename;

			}

			if (!(new java.io.File(filename).exists())) {
				return;
			}

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(filename);
			String xml = Utils.getElementAsString(doc.getRootElement());
			Utils.showClipboard(xml, getShell(), false, null, false, null, false);

		}
		catch (Exception ex) {
//			MainWindow.message("could not open Holiday File" + ex.getMessage(), SWT.ICON_ERROR);
			MainWindow.message(JOE_E_0001.params(ex.getMessage()), SWT.ICON_ERROR);
			return;
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
