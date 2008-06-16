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
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.WebservicesListener;

public class WebservicesForm extends Composite implements IUnsaved, IUpdateLanguage {


	private WebservicesListener listener          = null;

	private Group               group             = null;

	private Table               tServices         = null;

	private Button              bRemove           = null;

	private Button              bNew              = null;

	private Label               label8            = null;

	
	public WebservicesForm(Composite parent, int style, SchedulerDom dom, Element config, ISchedulerUpdate main) {
		super(parent, style);
		listener = new WebservicesListener(dom, config, main);
		initialize();
		setToolTipText();

		listener.fillTable(tServices);   
		
	}


	public void apply() {
		//if (isUnsaved())

			//applyService();

	}


	public boolean isUnsaved() {
		return false;
	}


	private void initialize() {
		this.setLayout(new FillLayout());
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(653, 468));
	}


	/**
	 * This method initializes group
	 */
	 private void createGroup() {
		GridData gridData19 = new org.eclipse.swt.layout.GridData();
		gridData19.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData19.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalSpan = 1;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		group = new Group(this, SWT.NONE);
		group.setText("Web Services");
		
		GridData gridData1 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
		tServices = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		tServices.setHeaderVisible(true);
		tServices.setLayoutData(gridData1);
		tServices.setLinesVisible(true);
		tServices.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			 public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				 boolean selection = tServices.getSelectionCount() > 0;
				 bRemove.setEnabled(selection);
				 if (selection) {
					 listener.selectService(tServices.getSelectionIndex());
					 /*setInput(true);
					 sTimeout.setEnabled(!cChain.getText().equals(""));
					 tRequest.setEnabled(!sTimeout.getEnabled());
					 tResponse.setEnabled(!sTimeout.getEnabled());
					 tForward.setEnabled(!sTimeout.getEnabled());
					 */
				 }
			 }
		});
		TableColumn tableColumn = new TableColumn(tServices, SWT.NONE);
		tableColumn.setWidth(150);
		tableColumn.setText("Name");
		TableColumn tableColumn1 = new TableColumn(tServices, SWT.NONE);
		tableColumn1.setWidth(150);
		tableColumn1.setText("URL");
		TableColumn tableColumn2 = new TableColumn(tServices, SWT.NONE);
		tableColumn2.setWidth(100);
		tableColumn2.setText("Job Chain");
		GridData gridData5 = new org.eclipse.swt.layout.GridData(GridData.FILL, GridData.BEGINNING, false, false);
		bNew = new Button(group, SWT.NONE);
		bNew.setText("&New Web Service");
		bNew.setLayoutData(gridData5);
		getShell().setDefaultButton(bNew);

		bNew.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.newService(tServices);
				//setInput(true);
				//tName.setFocus();

			}
		});
		
		group.setLayout(gridLayout);
		label8 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		label8.setText("Label");
		label8.setLayoutData(gridData19);
		bRemove = new Button(group, SWT.NONE);
		bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (tServices.getSelectionCount() > 0) {
					int index = tServices.getSelectionIndex();
					listener.removeService(index);
					tServices.remove(index);
					if (index >= tServices.getItemCount())
						index--;
					if (tServices.getItemCount() > 0) {
						tServices.select(index);
						listener.selectService(index);
						//setInput(true);
					}// else
						//setInput(false);
				}
				bRemove.setEnabled(tServices.getSelectionCount() > 0);
			}
		});
		bRemove.setEnabled(false);
		bRemove.setLayoutData(gridData3);
		bRemove.setText("Remove Web Service");
	 }

	
	 public void setToolTipText() {
		 bRemove.setToolTipText(Messages.getTooltip("web_services.btn_remove"));
		 bNew.setToolTipText(Messages.getTooltip("web_services.btn_new"));
		 tServices.setToolTipText(Messages.getTooltip("web_services.table"));

	 }
} // @jve:decl-index=0:visual-constraint="10,10"
