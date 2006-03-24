package sos.scheduler.editor.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jdom.Element;

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.listeners.PeriodsListener;
import org.eclipse.swt.widgets.Label;

public class PeriodsForm extends Composite implements IUnsaved {
	private PeriodsListener listener;

	private DomParser dom;

	private Group group = null;

	private Table tPeriods = null;

	private Button bNew = null;

	private Button bRemove = null;

	private PeriodForm periodForm = null;

	private Button bApply = null;

	private Label label = null;

	private Label label1 = null;

	public PeriodsForm(Composite parent, int style, DomParser dom,
			Element element) {
		super(parent, style);
		listener = new PeriodsListener(dom, element);
		this.dom = dom;
		initialize();

		listener.fillTable(tPeriods);
		periodForm.setEnabled(false);
	}

	public void apply() {
		if(isUnsaved())
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
	 * 
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
		group = new Group(this, SWT.NONE);
		group.setText("Periods");
		group.setLayout(gridLayout);
		createPeriodForm();
		bApply = new Button(group, SWT.NONE);
		label = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setText("Label");
		label.setLayoutData(gridData3);
		createTable();
		bNew = new Button(group, SWT.NONE);
		bNew.setText("&New Period");
		bNew.setToolTipText(Messages.getTooltip("periods.btn_new"));
		bNew.setLayoutData(gridData5);
		label1 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		label1.setText("Label");
		label1.setLayoutData(gridData7);
		bRemove = new Button(group, SWT.NONE);
		bNew
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						tPeriods.deselectAll();
						bApply.setEnabled(true);
						fillPeriod(true);
					}
				});
		bRemove.setText("Remove Period");
		bRemove.setEnabled(false);
		bRemove.setLayoutData(gridData6);
		bRemove.setToolTipText(Messages.getTooltip("periods.btn_remove"));
		bApply.setText("&Apply Period");
		bApply.setEnabled(false);
		bApply.setLayoutData(gridData1);
		bApply.setToolTipText(Messages.getTooltip("periods.btn_apply"));
		bApply
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						applyPeriod();
					}
				});
		bRemove
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						if (tPeriods.getSelectionCount() > 0) {
							int index = tPeriods.getSelectionIndex();
							tPeriods.remove(index);
							listener.removePeriod(index);

							if (index >= tPeriods.getItemCount())
								index--;
							if (tPeriods.getItemCount() > 0)
								tPeriods.select(index);
						}

						fillPeriod(false);
						bRemove.setEnabled(tPeriods.getSelectionCount() > 0);
						periodForm.setEnabled(tPeriods.getSelectionCount() > 0);
					}
				});
	}

	/**
	 * This method initializes table
	 * 
	 */
	private void createTable() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.verticalSpan = 3;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalSpan = 2;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tPeriods = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		tPeriods.setHeaderVisible(true);
		tPeriods.setToolTipText(Messages.getTooltip("periods.table"));
		tPeriods.setLayoutData(gridData2);
		tPeriods.setLinesVisible(true);
		tPeriods
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						bRemove.setEnabled(tPeriods.getSelectionCount() > 0);
						periodForm.setEnabled(tPeriods.getSelectionCount() > 0);
						if (tPeriods.getSelectionCount() > 0) {
							periodForm.setPeriod(listener.getPeriod(tPeriods
									.getSelectionIndex()));
							bApply.setEnabled(true);
						}
					}
				});
		TableColumn tableColumn = new TableColumn(tPeriods, SWT.NONE);
		tableColumn.setWidth(60);
		tableColumn.setText("Let Run");
		TableColumn tableColumn1 = new TableColumn(tPeriods, SWT.NONE);
		tableColumn1.setWidth(80);
		tableColumn1.setText("Begin");
		TableColumn tableColumn2 = new TableColumn(tPeriods, SWT.NONE);
		tableColumn2.setWidth(80);
		tableColumn2.setText("End");
		TableColumn tableColumn3 = new TableColumn(tPeriods, SWT.NONE);
		tableColumn3.setWidth(80);
		tableColumn3.setText("Repeat");
		TableColumn tableColumn4 = new TableColumn(tPeriods, SWT.NONE);
		tableColumn4.setWidth(80);
		tableColumn4.setText("Single Start");
	}

	/**
	 * This method initializes periodForm
	 * 
	 */
	private void createPeriodForm() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		periodForm = new PeriodForm(group, SWT.NONE);
		periodForm.setDom(dom);
		periodForm.setLayoutData(gridData);
	}
	
	private void fillPeriod(boolean newPeriod) {
		int index = tPeriods.getSelectionIndex();
		periodForm.setEnabled(index != -1 || newPeriod);
		if (index != -1) {
			periodForm.setPeriod(listener.getPeriod(index));
			periodForm.fillPeriod();
		} else if (newPeriod) {
			periodForm.setPeriod(listener.getNewPeriod());
			periodForm.fillPeriod();
		}
	}

	private void applyPeriod() {
		listener.applyPeriod(periodForm.getPeriod());
		listener.fillTable(tPeriods);
		bApply.setEnabled(false);
		bRemove.setEnabled(false);
		fillPeriod(false);
	}

} // @jve:decl-index=0:visual-constraint="10,10"
