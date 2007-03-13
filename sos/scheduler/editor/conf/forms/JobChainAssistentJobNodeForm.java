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
		gridLayout.numColumns = 5;
		shellJobChainl.setLayout(gridLayout);
		shellJobChainl.setSize(538, 248);
		shellJobChainl.setText("Job Chain Node");
		shellJobChainl.open();

		{
			txtJobChainNode = new Text(shellJobChainl, SWT.MULTI);
			txtJobChainNode.setEditable(false);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 4, 1);
			gridData.heightHint = 41;
			gridData.widthHint = 383;
			txtJobChainNode.setLayoutData(gridData);
			txtJobChainNode.setText(Messages.getString("assistent.job_chain.node"));
		}
		new Label(shellJobChainl, SWT.NONE);

		{
			final Label tasksLabel = new Label(shellJobChainl, SWT.NONE);
			tasksLabel.setLayoutData(new GridData());
			tasksLabel.setText("Jobs");
		}

		{
			comboJobs = new Combo(shellJobChainl, SWT.NONE);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
			gridData.widthHint = 133;
			comboJobs.setLayoutData(gridData);
			if(listener.getJobs() != null) {
				comboJobs.setItems(listener.getJobs());
			}			
			comboJobs.addMouseListener(new MouseAdapter() {
				public void mouseDown(final MouseEvent e) {
					System.out.println("mouseDown");
					if(refreshComboBox) {
						//init oder neue Jobs wurden definiert
						refreshComboBox = false;
						if(listener.getJobs() != null) {
							comboJobs.setItems(listener.getJobs());
						}
					}
				}
			});
			comboJobs.addMouseTrackListener(new MouseTrackAdapter() {
				public void mouseEnter(final MouseEvent e) {
					//System.out.println("mouseEnter");
					
				}
			});
			
			comboJobs.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(comboJobs.getItemCount() > 0  && comboJobs.getItem(comboJobs.getSelectionIndex()).trim().length() > 0) {
						
						System.out.println(comboJobs.getItem(comboJobs.getSelectionIndex()));
						txtJobChainNodename.setText(comboJobs.getItem(comboJobs.getSelectionIndex()));
						
					}
				}
			});
		}
		new Label(shellJobChainl, SWT.NONE);
		new Label(shellJobChainl, SWT.NONE);
		new Label(shellJobChainl, SWT.NONE);

		{
			final Label jobNodeLabel = new Label(shellJobChainl, SWT.NONE);
			jobNodeLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			jobNodeLabel.setText("Job Node");
		}

		{
			this.txtJobChainNodename = new Text(shellJobChainl, SWT.BORDER);
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
			gridData.widthHint = 158;
			this.txtJobChainNodename.setLayoutData(gridData);
		}

		{
			final Label lblstate = new Label(shellJobChainl, SWT.NONE);
			lblstate.setLayoutData(new GridData());
			lblstate.setText("State");
		}

		{
			txtState = new Text(shellJobChainl, SWT.BORDER);
			txtState.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					
				}
			});
			final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
			gridData.widthHint = 171;
			txtState.setLayoutData(gridData);
		}

		final Button butApply = new Button(shellJobChainl, SWT.NONE);
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
		butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butApply.setText("Apply");

		{
			final Label lblnextState = new Label(shellJobChainl, SWT.NONE);
			lblnextState.setText("Next State");
		}

		{
			txtNextState = new Text(shellJobChainl, SWT.BORDER);
			txtNextState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		}

		{
			final Label errorStateLabel = new Label(shellJobChainl, SWT.NONE);
			errorStateLabel.setLayoutData(new GridData());
			errorStateLabel.setText("Error State");
		}

		{
			txtErrorState = new Text(shellJobChainl, SWT.BORDER);
			txtErrorState.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		}
		new Label(shellJobChainl, SWT.NONE);

		table = new Table(shellJobChainl, SWT.FULL_SELECTION | SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
		gridData.heightHint = 70;
		gridData.widthHint = 400;
		table.setLayoutData(gridData);

		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(100);
		newColumnTableColumn.setText("Job");

		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setWidth(100);
		newColumnTableColumn_1.setText("State");

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setWidth(100);
		newColumnTableColumn_2.setText("Next State");

		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setWidth(100);
		newColumnTableColumn_3.setText("Error State");

		final Button newChainNodeButton = new Button(shellJobChainl, SWT.NONE);
		newChainNodeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				txtJobChainNodename.setText("");
				txtState.setText("");
				txtNextState.setText("");
				txtErrorState.setText("");
				table.deselectAll();
			}
		});
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		gridData_1.widthHint = 86;
		newChainNodeButton.setLayoutData(gridData_1);
		newChainNodeButton.setText("New Chain Node");

		final Button butRemove = new Button(shellJobChainl, SWT.NONE);
		butRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(table.getSelectionCount()> 0) {
				  table.remove(table.getSelectionIndex());	
				}				
			}
		});
		butRemove.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butRemove.setText("Remove");

		{
			butFinish = new Button(shellJobChainl, SWT.NONE);
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					
				}
			});
			butFinish.setText("Finish");
		}
		{
			butCancel = new Button(shellJobChainl, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					shellJobChainl.dispose();
				}
			});
			butCancel.setLayoutData(new GridData());
			butCancel.setText("Cancel");
		}
		{
			butNext = new Button(shellJobChainl, SWT.NONE);
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
			butNext.setLayoutData(new GridData());
			butNext.setText("Next");
		}

		{
			butShow = new Button(shellJobChainl, SWT.NONE);
			butShow.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					/*Element jobChain = new Element("job_chain");
					if(txtJobChainNodename.getText() != null && txtJobChainNodename.getText().trim().length() > 0) {												
						Utils.setAttribute("name", txtJobChainNodename.getText(), jobChain);						
					}
					*/
					
					MainWindow.message(shellJobChainl, Utils.getElementAsString(jobChain), SWT.OK );
				}
			});
			butShow.setLayoutData(new GridData());
			butShow.setText("Show");
		}

		final Button startJobAssistentButton = new Button(shellJobChainl, SWT.NONE);
		startJobAssistentButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				ShowAllImportJobsForm importJobs = new ShowAllImportJobsForm(listener.get_dom(), update);
				importJobs.showAllImportJobs("order", true, Editor.JOB_CHAINS);
				refreshComboBox = true;
			}
		});
		startJobAssistentButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		startJobAssistentButton.setText("Import Job");
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
}

