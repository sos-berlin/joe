/*
 * Created on 06.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.listeners.JobChainsListener;

public class JobChainAssistentJobNodeForm {
 
	private Element           jobChain          = null;
	
	private Text              txtJobChainNode     = null;
	
	private Text              txtJobChainNodename     = null;
	
	private JobChainsListener listener     = null;
		
	private Button            butFinish    = null;
	
	private Button            butCancel    = null;
	
	private Button            butNext      = null;
	
	private Button            butShow      = null;		
	
	private Combo             comboJobs    = null; 		
	
	private Text              txtState     = null;
	
	private Text              txtNextState = null;
	
	private Text              txtErrorState= null;
	
	private Table             table        = null; 
	
	private ISchedulerUpdate  update       = null;
	
	private boolean           refreshComboBox = true;
	
	public JobChainAssistentJobNodeForm(JobChainsListener listener_) {
		
		listener = listener_;	
		
			
	}
	
	public void showJobChainNode(Element jobChain_, ISchedulerUpdate update_) {
		
		jobChain = jobChain_;
		update = update_;
		final Shell shellJobChainl = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		final GridLayout gridLayout = new GridLayout();
		shellJobChainl.setLayout(gridLayout);
		shellJobChainl.setSize(463, 400);
		shellJobChainl.setText("Job Chain Node" + (Utils.getAttributeValue("name", jobChain) != null ? " for " + Utils.getAttributeValue("name", jobChain): ""));
		shellJobChainl.open();

		final Group jobChainGroup = new Group(shellJobChainl, SWT.NONE);
		jobChainGroup.setText("Job Chain");
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridData_2.heightHint = 347;
		gridData_2.widthHint = 441;
		jobChainGroup.setLayoutData(gridData_2);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginWidth = 10;
		gridLayout_1.marginTop = 10;
		gridLayout_1.marginRight = 10;
		gridLayout_1.marginLeft = 10;
		gridLayout_1.marginHeight = 10;
		gridLayout_1.marginBottom = 10;
		gridLayout_1.numColumns = 6;
		jobChainGroup.setLayout(gridLayout_1);

		{
			txtJobChainNode = new Text(jobChainGroup, SWT.MULTI);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false, 6, 1);
			gridData.heightHint = 99;
			txtJobChainNode.setLayoutData(gridData);
			txtJobChainNode.setEditable(false);
			txtJobChainNode.setText(Messages.getString("assistent.job_chain.node"));
		}

		{
			final Label tasksLabel = new Label(jobChainGroup, SWT.NONE);
			tasksLabel.setText("Jobs");
		}
		comboJobs = new Combo(jobChainGroup, SWT.NONE);
		comboJobs.setItems(listener.getJobs());
		final GridData gridData_1 = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
		gridData_1.widthHint = 75;
		comboJobs.setLayoutData(gridData_1);
		comboJobs.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {				
				if(refreshComboBox) {
					//init oder neue Jobs wurden definiert
					refreshComboBox = false;
					if(listener.getJobs() != null) {
						comboJobs.setItems(listener.getJobs());
					}
				}
			}
		});		
			
		comboJobs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(comboJobs.getItemCount() > 0  && comboJobs.getItem(comboJobs.getSelectionIndex()).trim().length() > 0) {
					
					System.out.println(comboJobs.getItem(comboJobs.getSelectionIndex()));
					txtJobChainNodename.setText(comboJobs.getItem(comboJobs.getSelectionIndex()));					
					txtState.setText("");
					txtNextState.setText("");
					txtErrorState.setText("");
					table.deselectAll();
					
				}
			}
		});
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);

		{
			final Label jobNodeLabel = new Label(jobChainGroup, SWT.NONE);
			jobNodeLabel.setText("Job Node");
		}

		{
			this.txtJobChainNodename = new Text(jobChainGroup, SWT.BORDER);
			txtJobChainNodename.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
		}

		{
			final Label lblstate = new Label(jobChainGroup, SWT.NONE);
			lblstate.setText("State");
		}

		{
			txtState = new Text(jobChainGroup, SWT.BORDER);
			txtState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			txtState.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					
				}
			});
		}

		final Button butApply = new Button(jobChainGroup, SWT.NONE);
		final GridData gridData_3 = new GridData(54, SWT.DEFAULT);
		butApply.setLayoutData(gridData_3);
		butApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(txtJobChainNodename.getText() != null && txtJobChainNodename.getText().trim().length() > 0) {
					if(table.getSelectionCount() > 0) {
						TableItem item = table.getItem(table.getSelectionIndex());
						item.setText(0, txtJobChainNodename.getText());	
						item.setText(1, txtState.getText());
						item.setText(2, txtNextState.getText());
						item.setText(3, txtErrorState.getText());						
					} else {
						TableItem item = new TableItem(table, SWT.NONE);	
						item.setText(0, txtJobChainNodename.getText());	
						item.setText(1, txtState.getText());
						item.setText(2, txtNextState.getText());
						item.setText(3, txtErrorState.getText());
						table.select(table.getItemCount()-1);
					}
	
				}
			 }
		});
		butApply.setText("Apply");

		{
			final Label lblnextState = new Label(jobChainGroup, SWT.NONE);
			lblnextState.setText("Next State");
		}

		{
			txtNextState = new Text(jobChainGroup, SWT.BORDER);
			txtNextState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1));
		}

		{
			final Label errorStateLabel = new Label(jobChainGroup, SWT.NONE);
			errorStateLabel.setText("Error State");
		}

		{
			txtErrorState = new Text(jobChainGroup, SWT.BORDER);
			txtErrorState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		}

		final Button newChainNodeButton = new Button(jobChainGroup, SWT.NONE);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_4.heightHint = 21;
		newChainNodeButton.setLayoutData(gridData_4);
		newChainNodeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				txtJobChainNodename.setText("");
				txtState.setText("");
				txtNextState.setText("");
				txtErrorState.setText("");
				table.deselectAll();
			}
		});
		newChainNodeButton.setText("New");

		table = new Table(jobChainGroup, SWT.FULL_SELECTION | SWT.BORDER);
		final GridData gridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 5, 2);
		gridData.minimumHeight = 40;
		gridData.heightHint = 82;
		gridData.widthHint = 324;
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(75);
		newColumnTableColumn.setText("Job");

		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(77);
		newColumnTableColumn_1.setText("State");

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(100);
		newColumnTableColumn_2.setText("Next State");

		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(100);
		newColumnTableColumn_3.setText("Error State");

		final Button butRemove = new Button(jobChainGroup, SWT.NONE);
		butRemove.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(table.getSelectionCount()> 0) {
				  table.remove(table.getSelectionIndex());	
				}				
			}
		});
		butRemove.setText("Remove");
		new Label(jobChainGroup, SWT.NONE);

		final Composite composite = new Composite(jobChainGroup, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginWidth = 0;
		gridLayout_2.numColumns = 2;
		composite.setLayout(gridLayout_2);

		{
			butFinish = new Button(composite, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					
					listener.newChain();
					listener.applyChain(Utils.getAttributeValue("name", jobChain), true, true);
					listener.fillChains();
					refreshElement(true);
					shellJobChainl.dispose();	
				}
			});
			butFinish.setText("Finish");
		}
		{
			butCancel = new Button(composite, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					shellJobChainl.dispose();
				}
			});
			butCancel.setText("Cancel");
		}

		final Button startJobAssistentButton = new Button(jobChainGroup, SWT.NONE);
		startJobAssistentButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(listener.get_dom(), update);
				importJobs.showAllImportJobs("order", true, Editor.JOB_CHAINS);
				txtJobChainNodename.setText("");
				txtState.setText("");
				txtNextState.setText("");
				txtErrorState.setText("");
				table.deselectAll();
				refreshComboBox = true;
			}
		});
		startJobAssistentButton.setText("Import Job");
		new Label(jobChainGroup, SWT.NONE);
		new Label(jobChainGroup, SWT.NONE);

		final Composite composite_1 = new Composite(jobChainGroup, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.END, GridData.FILL, false, false, 2, 1);
		gridData_5.widthHint = 79;
		composite_1.setLayoutData(gridData_5);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.marginWidth = 0;
		gridLayout_3.numColumns = 2;
		composite_1.setLayout(gridLayout_3);

		{
			butShow = new Button(composite_1, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					/*Element jobChain = new Element("job_chain");
					if(txtJobChainNodename.getText() != null && txtJobChainNodename.getText().trim().length() > 0) {												
						Utils.setAttribute("name", txtJobChainNodename.getText(), jobChain);						
					}
					*/
					refreshElement(false);
					/*Utils.setAttribute("orders_recoverable", "yes", jobChain);
					Utils.setAttribute("visible", "yes", jobChain);
					for (int i = 0; i < table.getItemCount(); i++) {
						TableItem ti = table.getItem(i);
						Element jobChainNode = new Element("job_chain_node");						
						Utils.setAttribute("job", ti.getText(0), jobChainNode);
						Utils.setAttribute("state",  ti.getText(1) , jobChainNode);
						Utils.setAttribute("next_state", ti.getText(2) , jobChainNode);
						Utils.setAttribute("error_state",  ti.getText(3), jobChainNode);
						jobChain.addContent(jobChainNode);
					}*/
					
					MainWindow.message(shellJobChainl, Utils.getElementAsString(jobChain), SWT.OK );
				}
			});
			butShow.setText("Show");
		}
		{
			butNext = new Button(composite_1, SWT.NONE);
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					/*if(txtJobChainNodename.getText() != null && txtJobChainNodename.getText().trim().length() > 0) {
						Element jobChain = new Element("job_chain");						
						Utils.setAttribute("name", txtJobChainNodename.getText(), jobChain);
						JobChainAssistentJobNodeForm assnode = new JobChainAssistentJobNodeForm(listener);
						assnode.showJobChainNode(jobChain);
						
					}
					*/
					shellJobChainl.dispose();
					
				}
			});
			butNext.setText("Next");
		}

		{
			if(listener.getJobs() != null) {
			}			
		}
		setToolTipText();
		shellJobChainl.layout();
		shellJobChainl.pack();
		
	}

	public void setToolTipText() {
		butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));
		butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		butShow.setToolTipText(Messages.getTooltip("assistent.show"));
		butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));		
	}
	
	private void refreshElement(boolean apply) {
		
		
		Utils.setAttribute("orders_recoverable", "yes", jobChain);
		Utils.setAttribute("visible", "yes", jobChain);
		jobChain.removeContent();
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem ti = table.getItem(i);
			Element jobChainNode = new Element("job_chain_node");						
			Utils.setAttribute("job", ti.getText(0), jobChainNode);
			Utils.setAttribute("state",  ti.getText(1) , jobChainNode);
			Utils.setAttribute("next_state", ti.getText(2) , jobChainNode);
			Utils.setAttribute("error_state",  ti.getText(3), jobChainNode);
			jobChain.addContent(jobChainNode);
			if(apply) {
//				Es soll den internen Variable in listener._node auf null setzten. Sonst funktioniert applyNode nicht
				listener.selectNode(null); 
				listener.applyNode(true, ti.getText(1), ti.getText(0), "", ti.getText(2), ti.getText(3), false, "");
			}
		}
	}
}

