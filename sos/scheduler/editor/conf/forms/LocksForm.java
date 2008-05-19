/**
 * 
 */
package sos.scheduler.editor.conf.forms;

import org.eclipse.swt.SWT;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.LocksListener;

/**
 * @author sky2000
 */
public class LocksForm extends Composite implements IUnsaved, IUpdateLanguage {



	private LocksListener          listener           = null;

	private Group                  locksGroup         = null;

	private Table                  tableLocks         = null;

	private Label                  label1             = null;

	private Button                 bRemove            = null;

	private Button                 bNew               = null;

	private Button                 bApply             = null;

	private Text                   tLock              = null;

	private Label                  label5             = null;

	private Spinner                sMaxNonExclusive   = null;

	private Label                  label              = null;

	private Label                  label2             = null;

	private SchedulerDom           dom                 = null;



	/**
	 * @param parent
	 * @param style
	 * @throws JDOMException
	 */
	public LocksForm(Composite parent, int style, SchedulerDom dom_, Element config) throws JDOMException {
		super(parent, style);
		dom = dom_;
		listener = new LocksListener(dom, config);

		initialize();
		setToolTipText();

		listener.fillTable(tableLocks);
		if(dom.isLifeElement()) {
			tableLocks.setVisible(false);
			label.setVisible(false);
			label2.setVisible(false);
			bNew.setVisible(false);
			bRemove.setVisible(false);

			listener.selectLock(0);
			setInput(true);
			tLock.setBackground(null);
		}

	}


	public void apply() {
		if (isUnsaved())
			applyLock();
	}


	public boolean isUnsaved() {
		return bApply.isEnabled();
	}


	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(694, 294));

	}


	/**
	 * This method initializes group
	 */
	 private void createGroup() {
		 GridData gridData8 = new org.eclipse.swt.layout.GridData();
		 gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		 gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		 GridData gridData7 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
		 gridData7.heightHint = 10;
		 GridData gridData5 = new org.eclipse.swt.layout.GridData();
		 gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		 gridData5.grabExcessHorizontalSpace = true;
		 gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		 GridData gridData4 = new GridData(20, SWT.DEFAULT);
		 GridData gridData3 = new org.eclipse.swt.layout.GridData();
		 gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		 gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		 GridData gridData2 = new org.eclipse.swt.layout.GridData();
		 gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		 gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		 GridData gridData1 = new org.eclipse.swt.layout.GridData();
		 gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		 gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		 GridLayout gridLayout = new GridLayout();
		 gridLayout.numColumns = 5;
		 locksGroup = new Group(this, SWT.NONE);
		 locksGroup.setText("Locks");
		 locksGroup.setLayout(gridLayout);
		 label1 = new Label(locksGroup, SWT.NONE);
		 label1.setText("Lock");
		 tLock = new Text(locksGroup, SWT.BORDER);
		 label5 = new Label(locksGroup, SWT.NONE);
		 final GridData gridData = new GridData();
		 gridData.horizontalIndent = 5;
		 label5.setLayoutData(gridData);
		 label5.setText("Max Non Exclusive:");
		 sMaxNonExclusive = new Spinner(locksGroup, SWT.NONE);
		 bApply = new Button(locksGroup, SWT.NONE);
		 label = new Label(locksGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		 label.setText("Label");
		 label.setLayoutData(gridData7);
		 createTable();
		 bNew = new Button(locksGroup, SWT.NONE);
		 bNew.setText("&New Lock");
		 bNew.setLayoutData(gridData1);
		 getShell().setDefaultButton(bNew);

		 label2 = new Label(locksGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		 label2.setText("Label");
		 label2.setLayoutData(gridData8);
		 bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			 public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 apply();
				 listener.newLock();
				 setInput(true);
				 bApply.setEnabled(listener.isValidLock(tLock.getText()));
			 }
		 });
		 bRemove = new Button(locksGroup, SWT.NONE);
		 bRemove.setText("Remove Lock");
		 bRemove.setEnabled(false);
		 bRemove.setLayoutData(gridData2);
		 bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			 public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 if (tableLocks.getSelectionCount() > 0) {
					 if(Utils.checkElement(tableLocks.getSelection()[0].getText(0), dom, sos.scheduler.editor.app.Editor.LOCKS, null)) {
						 int index = tableLocks.getSelectionIndex();
						 listener.removeLock(index);
						 tableLocks.remove(index);
						 if (index >= tableLocks.getItemCount())
							 index--;
						 if (tableLocks.getItemCount() > 0) {
							 tableLocks.select(index);
							 listener.selectLock(index);
							 setInput(true);
						 } else
							 setInput(false);
					 }
				 }
				 bRemove.setEnabled(tableLocks.getSelectionCount() > 0);
				 tLock.setBackground(null);
			 }
		 });
		 tLock.setLayoutData(gridData5);
		 tLock.setEnabled(false);
		 tLock.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			 public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				 if (e.keyCode == SWT.CR)
					 applyLock();
			 }
		 });
		 tLock.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			 public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				 boolean valid = listener.isValidLock(tLock.getText()) || dom.isLifeElement();;
				 if (valid)
					 tLock.setBackground(null);
				 else
					 tLock.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
				 bApply.setEnabled(valid);
			 }
		 });
		 sMaxNonExclusive.setMaximum(99999999);
		 sMaxNonExclusive.setLayoutData(gridData4);
		 sMaxNonExclusive.setEnabled(false);
		 sMaxNonExclusive.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			 public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				 if (e.keyCode == SWT.CR)
					 applyLock();
			 }
		 });
		 sMaxNonExclusive.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			 public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				 bApply.setEnabled(true);
			 }
		 });
		 bApply.setText("Apply Lock");
		 bApply.setLayoutData(gridData3);
		 bApply.setEnabled(false);
		 bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			 public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 applyLock();
			 }
		 });
	 }


	 /**
	  * This method initializes table
	  */
	 private void createTable() {
		 GridData gridData = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 4, 3);
		 tableLocks = new Table(locksGroup, SWT.FULL_SELECTION | SWT.BORDER);
		 tableLocks.setHeaderVisible(true);
		 tableLocks.setLayoutData(gridData);
		 tableLocks.setLinesVisible(true);
		 tableLocks.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			 public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

				 Element currLock = listener.getLock(tableLocks.getSelectionIndex());
				 listener.selectLock(tableLocks.getSelectionIndex());
				 if(currLock != null && !Utils.isElementEnabled("lock", dom, currLock)) {
					 setInput(false);
					 bRemove.setEnabled(false);
					 bApply.setEnabled(false);
				 } else {

					 boolean selection = tableLocks.getSelectionCount() > 0;
					 bRemove.setEnabled(selection);
					 if (selection) {
						 listener.selectLock(tableLocks.getSelectionIndex());
						 setInput(true);
						 tLock.setBackground(null);
					 }
				 }
			 }
		 });
		 TableColumn lockTableColumn = new TableColumn(tableLocks, SWT.NONE);
		 lockTableColumn.setWidth(200);
		 lockTableColumn.setText("Lock");
		 TableColumn tableColumn1 = new TableColumn(tableLocks, SWT.NONE);
		 tableColumn1.setWidth(150);
		 tableColumn1.setText("Max Non Exclusive");
	 }


	 private void applyLock() {
		 
		 boolean _continue = true;
		 if(listener.getLock().length() > 0 &&  !Utils.checkElement(listener.getLock(), dom, sos.scheduler.editor.app.Editor.LOCKS, null))
			 _continue = false;

		 if(tableLocks.getSelectionCount() > 0)
			 listener.selectLock(tableLocks.getSelectionIndex());
		 
		 if(_continue)		 
			 listener.applyLock(tLock.getText(), sMaxNonExclusive.getSelection());
		 
		 
		 listener.fillTable(tableLocks);        
		 setInput(false);
		 getShell().setDefaultButton(bNew);
		 tLock.setBackground(null);
		 if(dom.isLifeElement())
			 setInput(true);
		 
		 //if(dom.isDirectory() || dom.isLifeElement())dom.setChangedForDirectory("lock", listener.getLock(), SchedulerDom.MODIFY);

	 }


	 private void setInput(boolean enabled) {
		 tLock.setEnabled(enabled);
		 sMaxNonExclusive.setEnabled(enabled);
		 if (enabled) {
			 tLock.setText(listener.getLock());
			 sMaxNonExclusive.setSelection(listener.getMax_non_exclusive());
			 tLock.setFocus();
		 } else {
			 tLock.setText("");
			 sMaxNonExclusive.setSelection(0);
		 }
		 bApply.setEnabled(false);
		 bRemove.setEnabled(tableLocks.getSelectionCount() > 0);
		 // tProcessClass.setBackground(null);
	 }


	 public void setToolTipText() {
		 bNew.setToolTipText(Messages.getTooltip("locks.btn_new"));
		 bRemove.setToolTipText(Messages.getTooltip("locks.btn_remove"));
		 bApply.setToolTipText(Messages.getTooltip("locks.btn_apply"));
		 tLock.setToolTipText(Messages.getTooltip("locks.lock_entry"));
		 sMaxNonExclusive.setToolTipText(Messages.getTooltip("locks.max_non_exclusive"));
		 tableLocks.setToolTipText(Messages.getTooltip("locks.table"));

	 }
} // @jve:decl-index=0:visual-constraint="10,10"
