package sos.scheduler.editor.forms;


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

import sos.scheduler.editor.app.DomParser;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.IUpdate;
import sos.scheduler.editor.listeners.DaysListener;


public class DaysForm extends Composite  implements IUpdateLanguage{
	private DaysListener listener;
	private IUpdate _main;
	private int _type = 0;
	private static String[] _groupLabel = {"Weekdays", "Monthdays", "Ultimos"}; 
	private static String[] _dayLabel = {"Weekday:", "Monthday:", "Ultimo:"};
	private static String[] _addLabel = {"&Add Weekday", "&Add Monthday", "&Add Ultimo"};
	private static String[] _removeLabel = {"Remove Weekday", "Remove Monthday", "Remove Ultimo"};
//	private static String[] _listLabel = {"Used Weekdays:", "Used Monthdays:", "Used Ultimos:"};
	
	private Group group = null;
	private Label label = null;
	private Combo cUnusedDays = null;
	private Button bAdd = null;
	private List lUsedDays = null;
	private Button bRemove = null;
	private Label label2 = null;
	public DaysForm(Composite parent, int style, DomParser dom, Element job, IUpdate main, int type) {
		super(parent, style);
		if(type > 2 || type < 0)
			throw new IllegalArgumentException("the type must be from 0 to 2");
		
		listener = new DaysListener(dom, job, type);
		_main = main;
		_type = type;
		
		initialize();
		setToolTipText();
		read();
	}

	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(443,312));
	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup() {
		GridData gridData5 = new org.eclipse.swt.layout.GridData();
		gridData5.horizontalSpan = 3;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData5.heightHint = 10;
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData2 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData2.widthHint = 90;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
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
				listener.addDay(cUnusedDays.getText());
				_main.updateDays(_type);
				read();
			}
		});
		lUsedDays = new List(group, SWT.BORDER);
		lUsedDays.setLayoutData(gridData);
		lUsedDays.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				bRemove.setEnabled(lUsedDays.getSelectionCount() > 0);
			}
		});
		bRemove = new Button(group, SWT.NONE);
		bRemove.setText(_removeLabel[_type]);
		bRemove.setEnabled(false);
		bRemove.setLayoutData(gridData3);
		bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.deleteDay(lUsedDays.getItem(lUsedDays.getSelectionIndex()));
				_main.updateDays(_type);
				read();
			}
		});
	}

	/**
	 * This method initializes combo	
	 *
	 */
	private void createCombo() {
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		cUnusedDays = new Combo(group, SWT.READ_ONLY);
		cUnusedDays.setVisibleItemCount(10);
		cUnusedDays.setLayoutData(gridData4);
	}
	
	private void read() {
		cUnusedDays.setItems(listener.getUnusedDays());
		cUnusedDays.select(0);

		int index = lUsedDays.getSelectionIndex();
		lUsedDays.setItems(listener.getUsedDays());
		if(index >= lUsedDays.getItemCount())
			index--;
		if(lUsedDays.getItemCount() > 0)
			lUsedDays.setSelection(index);
		
		bAdd.setEnabled(cUnusedDays.getItemCount() > 0);
		bRemove.setEnabled(lUsedDays.getSelectionCount() > 0);
	}

	public void setToolTipText(){
		bAdd.setToolTipText(Messages.getTooltip("days.btn_add"));
		lUsedDays.setToolTipText(Messages.getTooltip("days.used_days"));
		bRemove.setToolTipText(Messages.getTooltip("days.btn_remove"));
		cUnusedDays.setToolTipText(Messages.getTooltip("days.unused_days"));

  }


}  //  @jve:decl-index=0:visual-constraint="10,10"
