package sos.scheduler.editor.conf.forms;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.MergeAllXMLinDirectory;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobLockUseListener;
import sos.scheduler.editor.conf.listeners.JobOptionsListener;

public class JobLockUseForm extends Composite implements IUnsaved, IUpdateLanguage {
    private Combo tLockUse;
    private JobLockUseListener listener;

    private Group              group1               = null;
    private Label              lockLabel            = null;
    private Label              label11              = null;
    private Button             bApplyLockUse        = null;
    private Table              tLockUseTable        = null;
    private Button             bNewLockUse          = null;
    private Button             bRemoveLockUse       = null;
    private Button             bExclusive           = null;
    private Button             butBrowse            = null;

    public JobLockUseForm(Composite parent, int style, SchedulerDom dom, Element job) {
        super(parent, style);
        listener = new JobLockUseListener(dom, job);
        initialize();
        initLockUseTable(true);

        setToolTipText();
        
        java.util.ArrayList listOfReadOnly = dom.getListOfReadOnlyFiles();
        if (listOfReadOnly != null && listOfReadOnly.contains(Utils.getAttributeValue("name", job))) {        	
        	this.group1.setEnabled(false);        	
        }
      
    }


    public void apply() {
        if (isUnsaved())
            applyLockUse();
    }


    public boolean isUnsaved() {
        return bApplyLockUse.isEnabled();
    }


    private void initialize() {
        this.setLayout(new FillLayout());
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(678, 425));
    }
 
    /**
     * This method initializes group1
     */
    private void createGroup() {
      GridData gridData51 = new org.eclipse.swt.layout.GridData();
      gridData51.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
      gridData51.widthHint = 90;
      gridData51.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
      GridLayout gridLayout1 = new GridLayout();
      gridLayout1.numColumns = 5;
      group1 = new Group(this, SWT.NONE);
      group1.setText("Use");
      group1.setLayout(gridLayout1);
      lockLabel = new Label(group1, SWT.NONE);
      lockLabel.setText("Lock");

      tLockUse = new Combo(group1, SWT.NONE);
      tLockUse.setEnabled(false);
      tLockUse.addModifyListener(new ModifyListener() {
      	public void modifyText(final ModifyEvent e) {
      		 if (!tLockUse.getText().equals(""))
             getShell().setDefaultButton(bApplyLockUse);
         bApplyLockUse.setEnabled(listener.isValidLock(tLockUse.getText()));
      	}
      });
      tLockUse.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
      label11 = new Label(group1, SWT.NONE);
      label11.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
      label11.setText("Exclusive");

      bExclusive = new Button(group1, SWT.CHECK);
      bExclusive.setSelection(true);
      bExclusive.setEnabled(true);
      bExclusive.addSelectionListener(new SelectionAdapter() {
      	public void widgetSelected(final SelectionEvent e) {
      	   bApplyLockUse.setEnabled(true);
      	}
      });
      bApplyLockUse = new Button(group1, SWT.NONE);
      bApplyLockUse.setText("Apply Lock Use");
      bApplyLockUse.setEnabled(false);
      bApplyLockUse.setLayoutData(gridData51);
      bApplyLockUse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
          public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
              applyLockUse();
          }
      });
      new Label(group1, SWT.NONE);
      GridData gridData30 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 3, 3);
      this.tLockUseTable = new Table(group1, SWT.BORDER | SWT.FULL_SELECTION);
      this.tLockUseTable.setHeaderVisible(true);
      this.tLockUseTable.setLayoutData(gridData30);
      this.tLockUseTable.setLinesVisible(true);
      this.tLockUseTable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
          public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
              if (tLockUseTable.getSelectionCount() > 0) {
                  listener.selectLockUse(tLockUseTable.getSelectionIndex());
                  initLockUse(true);
              } else
                  initLockUse(false);
              bRemoveLockUse.setEnabled(tLockUseTable.getSelectionCount() > 0);
          }
      });
      TableColumn tableColumn5 = new TableColumn(this.tLockUseTable, SWT.NONE);
      tableColumn5.setWidth(300);
      tableColumn5.setText("Lock");
      TableColumn tableColumn6 = new TableColumn(this.tLockUseTable, SWT.NONE);
      tableColumn6.setWidth(70);
      tableColumn6.setText("Exclusive");
      GridData gridData41 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.CENTER, false, false);
      bNewLockUse = new Button(group1, SWT.NONE);
      bNewLockUse.setText("New Lock Use");
      bNewLockUse.setLayoutData(gridData41);
      bNewLockUse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
          public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
              tLockUseTable.deselectAll();
              listener.newLockUse();
              initLockUse(true);
              tLockUse.setFocus();
          }
      });
      new Label(group1, SWT.NONE);

      butBrowse = new Button(group1, SWT.NONE);
      butBrowse.addSelectionListener(new SelectionAdapter() {
      	public void widgetSelected(final SelectionEvent e) {
      		String name = IOUtils.openDirectoryFile(MergeAllXMLinDirectory.MASK_LOCK);
      		tLockUse.setEnabled(true);
    		if(name != null && name.length() > 0) {
    			tLockUseTable.deselectAll();
                listener.newLockUse();
                initLockUse(true);
                tLockUse.setFocus();
    			tLockUse.setText(name);
    		}
      		
      	}
      });
      butBrowse.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
      butBrowse.setText("Browse");
      new Label(group1, SWT.NONE);
      GridData gridData31 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);
      bRemoveLockUse = new Button(group1, SWT.NONE);
      bRemoveLockUse.setText("Remove Lock Use");
      bRemoveLockUse.setEnabled(false);
      bRemoveLockUse.setLayoutData(gridData31);
      bRemoveLockUse.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
          public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
              if (tLockUseTable.getSelectionCount() > 0) {
                  int index = tLockUseTable.getSelectionIndex();
                  listener.deleteLockUse(index);
                  tLockUseTable.remove(index);
                  if (index >= tLockUseTable.getItemCount())
                      index--;
                  if (tLockUseTable.getItemCount() > 0) {
                      tLockUseTable.setSelection(index);
                      listener.selectLockUse(index);
                      initLockUse(true);
                  } else {
                  	  initLockUse(false);
                      bRemoveLockUse.setEnabled(false);
                  }
              }
          }
      });
    }

 
    
    // lock.use

    private void initLockUseTable(boolean enabled) {
        tLockUseTable.setEnabled(enabled);
        bNewLockUse.setEnabled(true);
        initLockUse(false);
        listener.fillLockUse(tLockUseTable);
        String[] locks = listener.getLocks();
        if (locks != null)   tLockUse.setItems(locks);

    }


    private void initLockUse(boolean enabled) {
      tLockUse.setEnabled(enabled);
      bExclusive.setEnabled(enabled);
        if (enabled) {
            bExclusive.setSelection(listener.getExclusive());
            tLockUse.setText(listener.getLockUse());
        } else {
            tLockUse.setText("");
            bExclusive.setSelection(true);
        }
        bApplyLockUse.setEnabled(false);
    }


    private void applyLockUse() {
    	  listener.applyLockUse(tLockUse.getText(), bExclusive.getSelection());
        listener.fillLockUse(this.tLockUseTable);
        initLockUse(false);
        getShell().setDefaultButton(null);
    }




    public void setToolTipText() {
        bApplyLockUse.setToolTipText(Messages.getTooltip("lockuse.btn_apply"));
        bNewLockUse.setToolTipText(Messages.getTooltip("lockuse.btn_new"));
        bRemoveLockUse.setToolTipText(Messages.getTooltip("lockuse.btn_remove"));
        tLockUseTable.setToolTipText(Messages.getTooltip("lockuse.table"));
        tLockUse.setToolTipText(Messages.getTooltip("lockuse.lookuse"));
        bExclusive.setToolTipText(Messages.getTooltip("lockuse.exclusive"));
        butBrowse.setToolTipText(Messages.getTooltip("job_chains.node.Browse"));
    }

} // @jve:decl-index=0:visual-constraint="10,10"
