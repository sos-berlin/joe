package sos.scheduler.editor.conf.forms;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jdom.Element;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.listeners.PeriodsListener;

import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.IUnsaved;
import com.sos.joe.globals.interfaces.IUpdateLanguage;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class PeriodsForm extends SOSJOEMessageCodes implements IUnsaved, IUpdateLanguage {
	private PeriodsListener	listener	= null;
	private SchedulerDom	dom			= null;
	private Group			group		= null;
	private Table			tPeriods	= null;
	private Button			bNew		= null;
	private Button			bRemove		= null;
	private PeriodForm		periodForm	= null;
	private Button			bApply		= null;
	private Label			label		= null;
	private Label			label1		= null;

	public PeriodsForm(Composite parent, int style, SchedulerDom dom, Element element, ISchedulerUpdate _main) {
		super(parent, style);
		listener = new PeriodsListener(dom, element, _main);
		this.dom = dom;
		initialize();
		setToolTipText();
		listener.fillTable(tPeriods);
		periodForm.setEnabled(false);
		periodForm.hasRepeatTimes(listener.hasRepeatTimes());
		//periodForm.setEnabled(!Utils.hasSchedulesElement(dom, element));
		this.group.setEnabled(Utils.isElementEnabled("job", dom, element) && !Utils.hasSchedulesElement(dom, element));
		//this.group.setEnabled(Utils.isElementEnabled("job", dom, element));
	}

	public void apply() {
		if (isUnsaved())
			applyPeriod();
	}

	public boolean isUnsaved() {
		return bApply.isEnabled();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(689, 476));
	}

	/**
	 * This method initializes group
	 */
	private void createGroup() {
		GridData gridData7 = new org.eclipse.swt.layout.GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalSpan = 2;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.heightHint = 10;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData6 = new org.eclipse.swt.layout.GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		group = JOE_G_PeriodsForm_Periods.Control(new Group(this, SWT.NONE));
		group.setLayout(gridLayout);
		createPeriodForm();
		bApply = JOE_B_PeriodsForm_ApplyPeriod.Control(new Button(group, SWT.NONE));
		bApply.setEnabled(false);
		bApply.setLayoutData(gridData1);
		bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				applyPeriod();
			}
		});
		label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		//        label.setText("Label");
		label.setLayoutData(gridData3);
		createTable();
		bNew = JOE_B_PeriodsForm_NewPeriod.Control(new Button(group, SWT.NONE));
		getShell().setDefaultButton(bNew);
		bNew.setLayoutData(gridData5);
		bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//repeat bzw. repeat_absolute darf nur einmal  def. werden
				periodForm.hasRepeatTimes(listener.hasRepeatTimes());
				tPeriods.deselectAll();
				getShell().setDefaultButton(bApply);
				bApply.setEnabled(false);
				fillPeriod(true);
				periodForm.setApplyButton(bApply);
			}
		});
		label1 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		//        label1.setText("Label");
		label1.setLayoutData(gridData7);
		bRemove = JOE_B_PeriodsForm_RemovePeriod.Control(new Button(group, SWT.NONE));
		bRemove.setEnabled(false);
		bRemove.setLayoutData(gridData6);
		bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				removePeriod();
				/* if (tPeriods.getSelectionCount() > 0) {
				     int index = tPeriods.getSelectionIndex();
				     tPeriods.remove(index);
				     
				     //listener.removePeriod(index);
				     listener.removePeriod(listener.getPeriod(index));

				     if (index >= tPeriods.getItemCount())
				         index--;
				     if (tPeriods.getItemCount() > 0) {
				         tPeriods.select(index);
				         //tPeriodSelect();   
				     }
				 }

				 fillPeriod(true);
				 bRemove.setEnabled(tPeriods.getSelectionCount() > 0);
				 periodForm.setEnabled(tPeriods.getSelectionCount() > 0);
				 bApply.setEnabled(false);
				 */
			}
		});
	}

	/**
	 * This method initializes table
	 */
	private void createTable() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalSpan = 2;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tPeriods = JOE_Tbl_PeriodsForm_Periods.Control(new Table(group, SWT.BORDER | SWT.FULL_SELECTION));
		tPeriods.setHeaderVisible(true);
		tPeriods.setLayoutData(new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3));
		tPeriods.setLinesVisible(true);
		tPeriods.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				tPeriodSelect();
			}
		});
		TableColumn tableColumn = JOE_TCl_PeriodsForm_LetRun.Control(new TableColumn(tPeriods, SWT.NONE));
		tableColumn.setWidth(59);
		TableColumn tableColumn1 = JOE_TCl_PeriodsForm_Begin.Control(new TableColumn(tPeriods, SWT.NONE));
		tableColumn1.setWidth(56);
		TableColumn tableColumn2 = JOE_TCl_PeriodsForm_End.Control(new TableColumn(tPeriods, SWT.NONE));
		tableColumn2.setWidth(55);
		TableColumn tableColumn3 = new TableColumn(tPeriods, SWT.NONE);
		tableColumn3.setWidth(59);
		tableColumn3.setText(PeriodForm.REPEAT_TIME);
		TableColumn tableColumn4 = new TableColumn(tPeriods, SWT.NONE);
		tableColumn4.setWidth(80);
		tableColumn4.setText(PeriodForm.SINGLE_START);
		final TableColumn newColumnTableColumn = new TableColumn(tPeriods, SWT.NONE);
		newColumnTableColumn.setWidth(92);
		newColumnTableColumn.setText(PeriodForm.ABSOLUTE_TIME);
		final TableColumn newColumnTableColumn_1 = JOE_TCl_PeriodsForm_WhenHoliday.Control(new TableColumn(tPeriods, SWT.NONE));
		newColumnTableColumn_1.setWidth(100);
	}

	/**
	 * This method initializes periodForm
	 */
	private void createPeriodForm() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		periodForm = new PeriodForm(group, SWT.NONE, JOEConstants.PERIODS);
		periodForm.setParams(dom, listener.isOnOrder());
		periodForm.setLayoutData(gridData);
		//periodForm.setPeriodsForm(this);//um die Tabelle zu aktualisieren
	}

	private void fillPeriod(boolean newPeriod) {
		int index = tPeriods.getSelectionIndex();
		periodForm.setEnabled(index != -1 || newPeriod);
		if (index != -1) {
			periodForm.setPeriod(listener.getPeriod(index));
			periodForm.fillPeriod();
		}
		else
			if (newPeriod) {
				periodForm.setPeriod(listener.getNewPeriod());
				periodForm.fillPeriod();
				periodForm.setEnabled(true);
			}
	}

	public void applyPeriod() {
		periodForm.savePeriod();
		listener.applyPeriod(periodForm.getPeriod());
		listener.fillTable(tPeriods);
		bRemove.setEnabled(false);
		fillPeriod(false);
		periodForm.hasRepeatTimes(listener.hasRepeatTimes());
		getShell().setDefaultButton(bNew);
		bApply.setEnabled(false);
	}

	public void refreshPeriodsTable() {
		listener.fillTable(tPeriods);
		bRemove.setEnabled(false);
		fillPeriod(false);
		periodForm.hasRepeatTimes(listener.hasRepeatTimes());
		periodForm.setEnabled(false);
		bApply.setEnabled(false);
	}

	public void setToolTipText() {
		//
	}

	private void tPeriodSelect() {
		if (bApply.isEnabled()) {
			int c = sos.scheduler.editor.app.MainWindow.message(getShell(), JOE_M_ApplyChanges.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			if (c == SWT.YES) {
				applyPeriod();
			}
		}
		periodForm.setEvent(false);
		bRemove.setEnabled(tPeriods.getSelectionCount() > 0);
		periodForm.setApplyButton(bApply);
		if (tPeriods.getSelectionCount() > 0) {
			if (tPeriods.getSelection()[0].getData() != null) {
				Element currPeriod = (Element) tPeriods.getSelection()[0].getData();
				if (currPeriod.getName().equals("at"))
					periodForm.setAtElement(currPeriod);
				else
					periodForm.setPeriod(currPeriod);
				if (listener.isRepeatElement(currPeriod)) {
					//es muss die Möglichkeit gegeben werden, einen repeat/absolute_repeat element zu verändern
					periodForm.hasRepeatTimes(false);
				}
				else {
					periodForm.hasRepeatTimes(listener.hasRepeatTimes());
				}
			}
			bApply.setEnabled(false);
			periodForm.setEnabled(tPeriods.getSelectionCount() > 0);
			periodForm.fillPeriod();
			periodForm.setEvent(true);
			periodForm.setFocus();
		}
		bApply.setEnabled(false);
	}

	private void removePeriod() {
		if (tPeriods.getSelectionCount() > 0) {
			int index = tPeriods.getSelectionIndex();
			tPeriods.remove(index);
			//listener.removePeriod(index);
			listener.removePeriod(listener.getPeriod(index));
			if (index >= tPeriods.getItemCount())
				index--;
			if (tPeriods.getItemCount() > 0) {
				tPeriods.select(index);
				//tPeriodSelect();   
			}
		}
		fillPeriod(true);
		bRemove.setEnabled(tPeriods.getSelectionCount() > 0);
		periodForm.setEnabled(tPeriods.getSelectionCount() > 0);
		bApply.setEnabled(false);
	}
} // @jve:decl-index=0:visual-constraint="10,10"
