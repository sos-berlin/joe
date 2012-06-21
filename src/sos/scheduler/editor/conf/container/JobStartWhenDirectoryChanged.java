package sos.scheduler.editor.conf.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.classes.FolderNameSelector;
import sos.scheduler.editor.classes.FormBaseClass;
import sos.scheduler.editor.conf.listeners.JobListener;

public class JobStartWhenDirectoryChanged extends FormBaseClass {

	@SuppressWarnings("unused")
	private final String		conSVNVersion		= "$Id$";

	private Group				group1				= null;
	private FolderNameSelector	tDirectory			= null;
	private Text				tRegex				= null;
	private Label				label11				= null;
	private Button				bApplyDirectory		= null;
	private Label				label1				= null;
	private Table				tDirectories		= null;
	private Button				bNewDirectory		= null;
	private Label				label21				= null;
	private Button				bRemoveDirectory	= null;

	private JobListener			objJobDataProvider	= null;

	public JobStartWhenDirectoryChanged(Composite pParentComposite, JobListener pobjDataProvider,JobStartWhenDirectoryChanged that) {
		super(pParentComposite, pobjDataProvider);
		objJobDataProvider = pobjDataProvider;
		createGroup();
        getValues(that);
	}

	 
	private void createGroup() {
		showWaitCursor();

		GridData gridData51 = new org.eclipse.swt.layout.GridData();
		gridData51.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData51.widthHint = 90;
		gridData51.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData41 = new org.eclipse.swt.layout.GridData();
		gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData41.widthHint = -1;
		gridData41.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData31 = new org.eclipse.swt.layout.GridData();
		gridData31.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData31.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData210 = new org.eclipse.swt.layout.GridData();
		gridData210.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData210.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData110 = new org.eclipse.swt.layout.GridData();
		gridData110.horizontalSpan = 5;
		gridData110.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData110.heightHint = 10;
		gridData110.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new org.eclipse.swt.layout.GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		 
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 5;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;

		group1 = new Group(objParent, SWT.NONE);
		group1.setText("Start When Directory Changed");
		group1.setLayout(gridLayout1);
		group1.setLayoutData(gridData);

		Label label = new Label(group1, SWT.NONE);
		label.setText("Watch Directory:");

		tDirectory = new FolderNameSelector(group1, SWT.BORDER);
		tDirectory.setParentForm(this);
		tDirectory.setI18NKey("StartWhenDirectoryChanged.WatchedDirectory");
		tDirectory.setDataProvider(objJobDataProvider);

		label11 = new Label(group1, SWT.NONE);
		label11.setText("File Regex:");
		tRegex = new Text(group1, SWT.BORDER);
		tRegex.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tRegex.selectAll();
			}
		});
		tRegex.setLayoutData(gridData4);
		bApplyDirectory = new Button(group1, SWT.NONE);
		bApplyDirectory.setText("Apply Dir");
		bApplyDirectory.setEnabled(false);
		bApplyDirectory.setLayoutData(gridData51);
		bApplyDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				applyDirectory();
			}
		});
		label1 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label1.setText("Label");
		label1.setLayoutData(gridData110);
		createTable3();
		bNewDirectory = new Button(group1, SWT.NONE);
		bNewDirectory.setText("New Dir");
		bNewDirectory.setLayoutData(gridData41);
		bNewDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tDirectories.deselectAll();
				objJobDataProvider.newDirectory();
				initDirectory(true);
				tDirectory.setFocus();
			}
		});
		label21 = new Label(group1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label21.setText("Label");
		label21.setLayoutData(gridData210);
		bRemoveDirectory = new Button(group1, SWT.NONE);
		bRemoveDirectory.setText("Remove Dir");
		bRemoveDirectory.setEnabled(false);
		bRemoveDirectory.setLayoutData(gridData31);
		bRemoveDirectory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tDirectories.getSelectionCount() > 0) {
					int index = tDirectories.getSelectionIndex();
					objJobDataProvider.deleteDirectory(index);
					tDirectories.remove(index);
					if (index >= tDirectories.getItemCount())
						index--;
					if (tDirectories.getItemCount() > 0) {
						tDirectories.setSelection(index);
						objJobDataProvider.selectDirectory(index);
						initDirectory(true);
					}
					else {
						initDirectory(false);
						bRemoveDirectory.setEnabled(false);
					}
				}
			}
		});

		tDirectory.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!tDirectory.getText().equals("")) {
					getShell().setDefaultButton(bApplyDirectory);
				}
				bApplyDirectory.setEnabled(!tDirectory.getText().equals(""));
			}
		});

		tRegex.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!tDirectory.getText().equals(""))
					getShell().setDefaultButton(bApplyDirectory);
				bApplyDirectory.setEnabled(!tDirectory.getText().equals(""));
			}
		});

		RestoreCursor();
	}

	private void getValues(JobStartWhenDirectoryChanged that){
	    if (that == null){
	        return;
	    }
	    
	   this.tRegex.setText(that.tRegex.getText());
	   this.tDirectory.setText(that.tDirectory.getText());
	   
	   for (int i= 0;i<that.tDirectories.getItemCount();i++){
	       TableItem t = new TableItem(this.tDirectories, SWT.None);
	       t.setText(that.tDirectories.getItems()[i].getText());
  	   }
	}
	
	private void initDirectory(boolean enabled) {
		tDirectory.setEnabled(enabled);
		tRegex.setEnabled(enabled);
		if (enabled) {
			tDirectory.setText(objJobDataProvider.getDirectory());
			tRegex.setText(objJobDataProvider.getRegex());
		}
		else {
			tDirectory.setText("");
			tRegex.setText("");
		}
		bApplyDirectory.setEnabled(false);
	}

	private void applyDirectory() {
		String strRegExp = tRegex.getText();
		if (Utils.isRegExpressions(strRegExp)) {
			objJobDataProvider.applyDirectory(tDirectory.getText(), strRegExp);
			objJobDataProvider.fillDirectories(tDirectories);
			initDirectory(false);
			getShell().setDefaultButton(null);
		}
		else {
			MainWindow.ErrMsg(Messages.getMsg("dirwatcher.regex.wrong", strRegExp)); //
		}
	}

	private void createTable3() {
		GridData gridData30 = new org.eclipse.swt.layout.GridData();
		gridData30.horizontalSpan = 4;
		gridData30.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData30.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData30.grabExcessHorizontalSpace = true;
		gridData30.grabExcessVerticalSpace = true;
		gridData30.verticalSpan = 3;

		tDirectories = new Table(group1, SWT.BORDER | SWT.FULL_SELECTION);
		tDirectories.setHeaderVisible(true);
		tDirectories.setLayoutData(gridData30);
		tDirectories.setLinesVisible(true);
		tDirectories.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tDirectories.getSelectionCount() > 0) {
					objJobDataProvider.selectDirectory(tDirectories.getSelectionIndex());
					initDirectory(true);
					tDirectory.setFocus();
				}
				else
					initDirectory(false);
				bRemoveDirectory.setEnabled(tDirectories.getSelectionCount() > 0);
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				tDirectory.selectAll();
			}
		});
		TableColumn tableColumn5 = new TableColumn(tDirectories, SWT.NONE);
		tableColumn5.setWidth(300);
		tableColumn5.setText(Messages.getLabel("Directory"));
		TableColumn tableColumn6 = new TableColumn(tDirectories, SWT.NONE);
		tableColumn6.setWidth(250);
		tableColumn6.setText(Messages.getLabel("dirwatcher.Regex"));
	}
}
