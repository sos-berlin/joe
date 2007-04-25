package sos.scheduler.editor.conf.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.JobsListener;


public class JobAssistentImportJobParamsForm {
	private Text         txtDescription         = null;
	
	private Table        table                  = null;
	
	private Table        tableDescParameters        = null;    
	
	private Shell        jobParameterShell      = null;
	
	private Text         txtValue               = null;
	
	private String       xmlPaths               = null;
	
	private Text         txtName                = null;
	
	private JobListener  joblistener            = null;

	//Tabelle aus der JobFrom: Falls die Klasse über den Import Button vom JobFrom erfolgte
	private Table        tParameter             = null; 		
	
	private Button       butFinish              = null;
		
	private Button       butApply               = null;
	
	private Button       butNext                = null;	
	
	private Button       butBack                = null; 
	
	private ArrayList    listOfParams           = new ArrayList();
		
	private SchedulerDom dom                    = null;
	
	private ISchedulerUpdate update             = null;
	
	private Button       butCancel              = null; 
	
	private Button       showButton             = null;
	
	/** Wer hat ihn aufgerufen, der Job assistent oder job_chain assistent*/
	private int          assistentType          = -1; 
	
	private Button       butPut                 = null;
	
	private Button       butPutAll              = null;
	
	private Button       butRemove              = null;
	
	private Button       butRemoveAll           = null;
	
	private Combo        jobname                = null;
	
	private boolean      startWizzard           = true;
	
	private Element      jobBackUp              = null;    
	
	private JobForm      jobForm                = null;
	
	/** Hilsvariable für das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog über den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	private boolean      closeDialog   = false;         
	
	
	public JobAssistentImportJobParamsForm() {}
	
	public JobAssistentImportJobParamsForm(SchedulerDom dom_, 
			ISchedulerUpdate update_, 
			Element job_, 
			int assistentType_) {
		dom = dom_;
		update = update_;
		assistentType = assistentType_;
		joblistener = new JobListener(dom,  job_, update);				
	}
	
	public JobAssistentImportJobParamsForm(SchedulerDom dom_, 
			ISchedulerUpdate update_, 
			JobListener joblistener_, 
			Table tParameter_,			
			int assistentType_) {
		
		dom = dom_;
		update = update_;		
		joblistener = joblistener_;	
		jobBackUp = (Element)joblistener.getJob().clone();
		tParameter = tParameter_;
		this.assistentType = assistentType_;		
	}
	
	
	public ArrayList parseDocuments(String xmlFilename) {
		
		//Wizzard ohne Jobbeschreibung starten
		if(xmlFilename == null || xmlFilename.trim().length() == 0) 
			return new ArrayList();
		
		xmlPaths = sos.scheduler.editor.app.Options.getSchedulerHome() ;
		if(!xmlFilename.replaceAll("\\\\", "/").startsWith(xmlPaths.replaceAll("\\\\", "/")))
			xmlFilename  = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? xmlPaths.concat(xmlFilename) : xmlPaths.concat("/").concat(xmlFilename));
		ArrayList listOfParams = null;
		try {
			
			listOfParams = new ArrayList();			
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build( new File( xmlFilename ) );
			Element root = doc.getRootElement();				
			Element config = root.getChild("configuration", root.getNamespace());
			if(config == null) {
				return listOfParams;
			}
			Element params = config.getChild("params", config.getNamespace());
			if(params == null)
				return listOfParams;
			List listMainElements = params.getChildren("param", params.getNamespace());
			HashMap h = null; 
			for( int i=0; i<listMainElements.size(); i++ ){					
				Element elMain  = (Element)(listMainElements.get( i ));					
				if(elMain.getName().equalsIgnoreCase("param")) { 
					//System.out.println("Debug: Name= " + elMain.getAttributeValue("name") + ", default_value = " + elMain.getAttributeValue("default_value") + " required = " + elMain.getAttributeValue("required"));
					h = new HashMap();
					h.put("name", elMain.getAttributeValue("name"));
					h.put("default_value", (elMain.getAttributeValue("default_value") != null ? elMain.getAttributeValue("default_value").toString() : ""));
					h.put("required", elMain.getAttributeValue("required"));
					Element note = elMain.getChild("note", elMain.getNamespace());
					if(note != null) {
						List notelist = note.getChildren();
						for (int j = 0; j < notelist.size(); j++) {
							Element elNote  = (Element)(notelist.get( j ));							
							h.put("description", elNote.getText());	
							
						}
					}
					
					listOfParams.add(h);						
				}
				
			}
			
		} catch( Exception ex ) {			
			ex.printStackTrace();
		}
		return listOfParams; 
	}
		
	
	/**
	 * 
	 * @param xmlFilename -> Job Dokumentation
	 */
	public void showAllImportJobParams(String xmlFilename)  {
		
		try {
			jobParameterShell = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);					
			jobParameterShell.addShellListener(new ShellAdapter() {
				public void shellClosed(final ShellEvent e) {
					if(!closeDialog)
						close();
					e.doit = jobParameterShell.isDisposed();
				}  
			});
			
			jobParameterShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
			final GridLayout gridLayout = new GridLayout();
			jobParameterShell.setLayout(gridLayout);			
			String step = "  ";
			if (Utils.getAttributeValue("order", joblistener.getJob()).equalsIgnoreCase("yes"))
				step = step + " [Step 3 of 9]";
			else 
				step = step + " [Step 3 of 8]";
			jobParameterShell.setText("Job Parameter" + step);
			Label nameLabel;
			
			final Group textParameterGroup = new Group(jobParameterShell, SWT.NONE);
			textParameterGroup.setText(" Job " + Utils.getAttributeValue("name", joblistener.getJob()));
			final GridData gridData_3 = new GridData(620, 519);
			gridData_3.minimumWidth = -1;
			textParameterGroup.setLayoutData(gridData_3);
			final GridLayout gridLayout_3 = new GridLayout();
			gridLayout_3.marginWidth = 10;
			gridLayout_3.marginTop = 10;
			gridLayout_3.marginRight = 10;
			gridLayout_3.marginLeft = 10;
			gridLayout_3.marginHeight = 10;
			gridLayout_3.marginBottom = 10;
			gridLayout_3.numColumns = 7;
			textParameterGroup.setLayout(gridLayout_3);
			new Label(textParameterGroup, SWT.NONE);

			final Text txtParameter = new Text(textParameterGroup, SWT.WRAP);
			txtParameter.setEditable(false);
			final GridData gridData_7 = new GridData(GridData.FILL, GridData.FILL, true, false, 6, 1);
			gridData_7.heightHint = 122;
			gridData_7.widthHint = 414;
			txtParameter.setLayoutData(gridData_7);
			txtParameter.setText(Messages.getString("assistent.parameters"));
			new Label(textParameterGroup, SWT.NONE);

			final Composite composite = new Composite(textParameterGroup, SWT.NONE);
			final GridData gridData_5 = new GridData(GridData.BEGINNING, GridData.FILL, false, false, 2, 1);
			gridData_5.widthHint = 50;
			composite.setLayoutData(gridData_5);
			final GridLayout gridLayout_4 = new GridLayout();
			gridLayout_4.marginWidth = 0;
			composite.setLayout(gridLayout_4);
			
			butCancel = new Button(composite, SWT.NONE);
			butCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					close();
				}
			});
			butCancel.setText("Cancel");
			new Label(textParameterGroup, SWT.NONE);

			final Composite composite_1 = new Composite(textParameterGroup, SWT.NONE);
			final GridData gridData_4 = new GridData(GridData.END, GridData.CENTER, false, false);
			gridData_4.widthHint = 86;
			composite_1.setLayoutData(gridData_4);
			final GridLayout gridLayout_2 = new GridLayout();
			gridLayout_2.numColumns = 2;
			gridLayout_2.marginWidth = 0;
			composite_1.setLayout(gridLayout_2);
			showButton = new Button(composite_1, SWT.NONE);
			showButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			showButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {										
					//MainWindow.message(jobParameterShell, Utils.getElementAsString(joblistener.getJob()), SWT.OK );
					Utils.showClipboard(Utils.getElementAsString(joblistener.getJob()), jobParameterShell);
				}
			});
			showButton.setText("Show");
			if(assistentType == Editor.JOB)
				showButton.setVisible(false);
			
			butFinish = new Button(composite_1, SWT.NONE);
			butFinish.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			butFinish.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					
					if(assistentType == Editor.JOB || assistentType == Editor.JOB_WIZZARD) {
						
						tParameter.removeAll();
						joblistener.fillParams(tParameter);						
						
					} else {
						
						if(jobname != null)
							jobname.setText(Utils.getAttributeValue("name",joblistener.getJob()));
						JobsListener listener = new JobsListener(dom, update);
						listener.newImportJob(joblistener.getJob(), assistentType);		
						
					}
					MainWindow.message(jobParameterShell,  Messages.getString("assistent.finish") + "\n\n" + Utils.getElementAsString(joblistener.getJob()), SWT.OK );
					
					closeDialog = true;
					jobParameterShell.dispose();
				}
			});
			butFinish.setText("Finish");

			butBack = new Button(textParameterGroup, SWT.NONE);
			butBack.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(dom, update, assistentType);
					importJobs.showAllImportJobs(joblistener);
					if(jobname != null) 													
						importJobs.setJobname(jobname);
					if(jobBackUp != null) 
						importJobs.setBackUpJob(jobBackUp, jobForm);
					closeDialog = true;
					jobParameterShell.dispose();
				}
			});
			butBack.setText("Back");
			butNext = new Button(textParameterGroup, SWT.NONE);
			butNext.setFocus();
			final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, true, false);
			gridData_9.widthHint = 54;
			butNext.setLayoutData(gridData_9);
			butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
			butNext.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(assistentType != Editor.JOB) {
						JobAssistentTasksForm tasks = new JobAssistentTasksForm(dom, update, joblistener.getJob(), assistentType);											
						tasks.showTasksForm();	
						if(jobname != null) 													
							tasks.setJobname(jobname);
						if(jobBackUp != null)
							tasks.setBackUpJob(jobBackUp, jobForm);
					} 
					closeDialog = true;					
					
					jobParameterShell.dispose();
				}
			});
				
			butNext.setText(" Next ");
			
			if(assistentType == Editor.JOB) {
				butNext.setEnabled(false);
				butBack.setEnabled(false);
			} else {
				butNext.setEnabled(true);
				butBack.setEnabled(true);
			}
			new Label(textParameterGroup, SWT.NONE);

			final Label label_1 = new Label(textParameterGroup, SWT.BORDER);
			final GridData gridData_6_1 = new GridData(GridData.FILL, GridData.BEGINNING, false, false, 6, 1);
			gridData_6_1.heightHint = 0;
			label_1.setLayoutData(gridData_6_1);
			label_1.setText("label");
			new Label(textParameterGroup, SWT.NONE);
			
			{
				nameLabel = new Label(textParameterGroup, SWT.NONE);
				nameLabel.setLayoutData(new GridData());
				nameLabel.setText("Name");
			}
			{
				txtName = new Text(textParameterGroup, SWT.BORDER);
				txtName.addKeyListener(new KeyAdapter() {
					public void keyPressed(final KeyEvent e) {
						if (e.keyCode == SWT.CR && !txtName.getText().equals("")){
		                    addParam();
						}
					}
				});
				final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
				gridData.widthHint = 168;
				txtName.setLayoutData(gridData);
				txtName.setText("");
				txtName.setFocus();
			}
						
			
			final Label lblTitle = new Label(textParameterGroup, SWT.NONE);
			final GridData gridData_6 = new GridData(GridData.END, GridData.CENTER, false, false);
			gridData_6.widthHint = 41;
			lblTitle.setLayoutData(gridData_6);
			lblTitle.setAlignment(SWT.RIGHT);
			lblTitle.setText("Value");
			
			txtValue = new Text(textParameterGroup, SWT.BORDER);
			txtValue.addKeyListener(new KeyAdapter() {
				public void keyPressed(final KeyEvent e) {
					if (e.keyCode == SWT.CR && !txtName.getText().trim().equals(""))
	                    addParam();
				}
			});
			final GridData gridData_10 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_10.widthHint = 175;
			txtValue.setLayoutData(gridData_10);
			new Label(textParameterGroup, SWT.NONE);
			{
				butApply = new Button(textParameterGroup, SWT.NONE);
				butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
				butApply.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						addParam();
												
					}
				});
				
				butApply.setText("Apply");
			}

			tableDescParameters = new Table(textParameterGroup, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
			tableDescParameters.addMouseListener(new MouseAdapter() {
				public void mouseDoubleClick(final MouseEvent e) {
					addParams();
				}
			});
			tableDescParameters.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					if(tableDescParameters.getSelectionCount() > -1) {	
						txtDescription.setText((tableDescParameters.getSelection()[0].getData() != null? tableDescParameters.getSelection()[0].getData().toString(): "") );
					}
				}
			});
			tableDescParameters.setLinesVisible(true);
			tableDescParameters.setHeaderVisible(true);
			final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1);
			gridData.widthHint = 245;
			tableDescParameters.setLayoutData(gridData);

			final TableColumn newColumnTableColumn = new TableColumn(tableDescParameters, SWT.NONE);
			newColumnTableColumn.setWidth(122);
			newColumnTableColumn.setText("Name");

			final TableColumn newColumnTableColumn_1 = new TableColumn(tableDescParameters, SWT.NONE);
			newColumnTableColumn_1.setWidth(145);
			newColumnTableColumn_1.setText("Value");

			final Composite composite_2 = new Composite(textParameterGroup, SWT.NONE);
			final GridData gridData_11 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_11.widthHint = 36;
			composite_2.setLayoutData(gridData_11);
			composite_2.setLayout(new GridLayout());

			butPut = new Button(composite_2, SWT.NONE);
			butPut.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					addParams();
					
				}
			});
			final GridData gridData_13 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_13.widthHint = 33;
			butPut.setLayoutData(gridData_13);
			butPut.setText(">");

			butPutAll = new Button(composite_2, SWT.NONE);
			butPutAll.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					for(int i = 0; i < tableDescParameters.getItemCount(); i++) {												
						joblistener.saveParameter(table, tableDescParameters.getItem(i).getText(0), tableDescParameters.getItem(i).getText(1), (tableDescParameters.getItem(i).getData() != null?tableDescParameters.getItem(i).getData().toString(): ""), tableDescParameters.getItem(i).getBackground().equals(Options.getRequiredColor() ));
					}
					tableDescParameters.removeAll();
					
				}
			});
			butPutAll.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butPutAll.setText(">>");

			butRemove = new Button(composite_2, SWT.NONE);
			butRemove.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					removeParams();
				}		
			});
			final GridData gridData_12 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_12.widthHint = 29;
			butRemove.setLayoutData(gridData_12);
			butRemove.setText("<");

			butRemoveAll = new Button(composite_2, SWT.NONE);
			butRemoveAll.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {					
					String remItem= null;
					ArrayList listOfParams = new ArrayList();
					
					for(int i = 0; i < table.getItemCount();i++){
						TableItem item = table.getItem(i);
						if(item.getBackground().equals(Options.getRequiredColor())) {
							remItem = (remItem!=null?remItem:"") +"\n\t" + item.getText(0);
							//merke die Parameter, die nicht gelöscht werden sollen, weil sie required sind
							HashMap h = new HashMap();
							h.put("name", item.getText(0));
							h.put("default_value", (item.getText(1)!=null?item.getText(1):""));
							h.put("required", "true");
							h.put("description", (item.getData()!=null?item.getData():""));
							listOfParams.add(h);
							
						} else {
							TableItem itemDP =  new TableItem(tableDescParameters, SWT.NONE);
							itemDP.setText(0, item.getText(0));
							itemDP.setText(1, item.getText(1));
							itemDP.setData(item.getData());
						}
						
					}
					txtName.setFocus();
										
					joblistener.fillParams(listOfParams, table, true);
										
					if(remItem != null) {
						MainWindow.message(jobParameterShell, sos.scheduler.editor.app.Messages.getString("assistent.jobparameter.required") + remItem, SWT.ICON_WARNING | SWT.OK );
					}
					
					table.redraw();
					table.deselectAll();
					tableDescParameters.deselectAll();
					
				}
			});
			butRemoveAll.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butRemoveAll.setText("<<");
			
			table = new Table(textParameterGroup, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
			table.addMouseListener(new MouseAdapter() {
				public void mouseDoubleClick(final MouseEvent e) {
					removeParams();
				}
			});
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1);
			gridData_1.heightHint = 135;
			gridData_1.widthHint = 185;
			table.setLayoutData(gridData_1);
			table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					if(table.getSelectionCount() > -1) {						
						txtName.setText(table.getSelection()[0].getText(0));						
						txtValue.setText(table.getSelection()[0].getText(1));						
						txtDescription.setText((table.getSelection()[0].getData() != null? table.getSelection()[0].getData().toString(): "") );
						txtName.setFocus();
					}
				}
			});
			table.setLayoutDeferred(true);
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			
			final TableColumn colName = new TableColumn(table, SWT.NONE);
			
			colName.setWidth(119);
			colName.setText("Name");
			
			final TableColumn colValue = new TableColumn(table, SWT.NONE);
			colValue.setWidth(212);
			colValue.setText("Value");
						
			txtDescription = new Text(textParameterGroup, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
			final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false, 7, 1);
			gridData_2.heightHint = 108;
			gridData_2.widthHint = 403;
			txtDescription.setLayoutData(gridData_2);
			txtDescription.setBackground(SWTResourceManager.getColor(247, 247, 247));
			txtDescription.setEditable(false);
			
			
            butFinish.setText("Finish");
					
			//der Wizzard soll ohne Jobbeschreibung laufen
			if(!xmlFilename.equals(".."))
				listOfParams = this.parseDocuments(xmlFilename);
			
			fillTable(listOfParams);
			
			setToolTipText();

			java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			
			jobParameterShell.setBounds((screen.width - jobParameterShell.getBounds().width) /2, 
					        (screen.height - jobParameterShell.getBounds().height) /2, 
					        jobParameterShell.getBounds().width, 
					        jobParameterShell.getBounds().height);
			
			jobParameterShell.layout();
			jobParameterShell.pack();
			jobParameterShell.open();
		} catch (Exception e) {
			System.out.println("..error in JobAssistentImportJobParamsForm.showAllImportJobParams " + ": " + e.getMessage());
		}
	}
	
	
	public void fillTable(ArrayList list) throws Exception {
		ArrayList listOfRequired = new ArrayList();
		try {
			HashMap h = new HashMap();
			tableDescParameters.removeAll();
			
			ArrayList jobP = getParameters();
//			eventuell vorhandene Parameters aus der Job Editor hinzufügen							
			joblistener.fillParams(jobP, table , true);	
			
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					h = (HashMap)list.get(i);	                	                										
					
					if(h.get("required") != null && (h.get("required").equals("true"))) {
						listOfRequired.add(h);												
					} else {
						if(h.get("name") != null && 
								joblistener.existsParams(h.get("name").toString(), table, null) == null) {
							TableItem item = new TableItem(tableDescParameters, SWT.NONE);
							item.setBackground(null);
							
							item.setChecked(true);
							item.setText(0,(h.get("name") != null ? h.get("name").toString() : ""));
							item.setText(1, (h.get("default_value") != null ? h.get("default_value").toString() : ""));					
							String desc = (h.get("description") != null ? h.get("description").toString(): "");							
							item.setData(desc);
						}
					}
				}
				
//				eventuell vorhandene Parameters aus der Job Editor hinzufügen				
				joblistener.fillParams(listOfRequired, table, false);
					
			}
		} catch (Exception e) {
			throw new Exception("error in JobAssistentImportJobParamsForm.fillTable() "  + e.toString());
		}
	}
	
	public void setToolTipText() {
		txtName.setToolTipText(Messages.getTooltip("job.param.name"));
		txtValue.setToolTipText(Messages.getTooltip("job.param.value"));
		table.setToolTipText(Messages.getTooltip("tableparams"));
		tableDescParameters.setToolTipText(Messages.getTooltip("table_description_params"));
		
		butApply.setToolTipText(Messages.getTooltip("jobparameter.apply"));
		if(butNext != null)
			butNext.setToolTipText(Messages.getTooltip("assistent.next"));
		
		if(butFinish != null) butFinish.setToolTipText(Messages.getTooltip("assistent.finish"));
		if(butCancel != null ) butCancel.setToolTipText(Messages.getTooltip("assistent.cancel"));		
		if(showButton != null) showButton.setToolTipText(Messages.getTooltip("assistent.show"));
		if(butPut != null) butPut.setToolTipText(Messages.getTooltip("jobparameter.put"));
		if(butPutAll != null) butPutAll.setToolTipText(Messages.getTooltip("jobparameter.put_all"));
		if(butRemove != null) butRemove.setToolTipText(Messages.getTooltip("jobparameter.remove"));
		if(butRemoveAll != null) butRemoveAll.setToolTipText(Messages.getTooltip("jobparameter.remove_all"));
		butBack.setToolTipText(Messages.getTooltip("butBack"));		
	}
	
	public ArrayList getParameters() {
		
		Element params = joblistener.getJob().getChild("params");
		ArrayList listOfParams = new ArrayList();
		List param = null;
		if(params != null) {
			param = params.getChildren("param");
		}
		if(param != null) {
			
			for (int i =0; i < param.size(); i++) {
				Element el = (Element)param.get(i);
				HashMap h = new HashMap();	
				h.put("name", Utils.getAttributeValue("name", el));
				h.put("default_value", Utils.getAttributeValue("value", el));
				listOfParams.add(h);
			}
		}
				
		return listOfParams;
	}
	
	private boolean existItem(String name, Table tab) {
		for(int i =0; tab != null &&  i < tab.getItemCount(); i++ ) {
			TableItem item = tab.getItem(i);
			if(item.getText(0) != null && item.getText(0).equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	
	private void refreshTable(Table _table, Table _fillTable) {
		String exists = "";
		ArrayList listOfParam = new ArrayList();
		for(int i = 0; i < _table.getItemCount(); i++) {
			TableItem item = _table.getItem(i);
			String name = item.getText(0);
			if(!existItem(name, _fillTable)) {				
				joblistener.saveParameter(table, item.getText(0), item.getText(1));				
			} else {
				exists = exists + "\n\t" + name;
			}
		}
						
		if(exists != null && exists.trim().length() > 0) {
			MainWindow.message(jobParameterShell, sos.scheduler.editor.app.Messages.getString("assistent.jobparameter.exist") + exists, SWT.ICON_WARNING | SWT.OK );
		}
	}
	
	private void close() {
		int cont = MainWindow.message(jobParameterShell, sos.scheduler.editor.app.Messages.getString("assistent.cancel"), SWT.ICON_WARNING | SWT.OK |SWT.CANCEL );
		if(cont == SWT.OK) {
			if(jobBackUp != null) 
				joblistener.getJob().setContent(jobBackUp.cloneContent());
			jobParameterShell.dispose();
		} 
	}
	
	private void addParam() {
		if(txtName.getText()!= null && txtName.getText().trim().length() == 0) {							
			MainWindow.message(jobParameterShell, sos.scheduler.editor.app.Messages.getString("no_param_name"), SWT.ICON_WARNING | SWT.OK );
			txtName.setFocus();
			return;
		}
		
		joblistener.saveParameter(table, txtName.getText(),txtValue.getText());
		txtName.setFocus();
	}

	public void setJobname(Combo jobname) {
		this.jobname = jobname;
	}	
	
	public void setJobForm(JobForm jobForm_){
		jobForm = jobForm_;
	}
	
	/**
	 * Der Wizzard wurde für ein bestehende Job gestartet. 
	 * Beim verlassen der Wizzard ohne Speichern, muss der bestehende Job ohne Änderungen wieder zurückgesetz werden.
	 * @param backUpJob
	 */
	public void setBackUpJob(Element backUpJob, JobForm jobForm_) {
		jobBackUp = (Element)backUpJob.clone();	
		jobForm = jobForm_;
	}
	
	private void addParams() {
		if(tableDescParameters.getSelectionIndex() > -1) {
			String existParams = "";
			for(int i = 0; i < tableDescParameters.getSelectionIndices().length; i++) {
				
				TableItem item = tableDescParameters.getItem(tableDescParameters.getSelectionIndices()[i]);
				String name = item.getText(0);
				if(!existItem(name, table)) {							
					joblistener.saveParameter(table, item.getText(0), item.getText(1), (item.getData() != null? item.getData().toString():""), item.getBackground().equals(Options.getRequiredColor() ));
				} else {
					existParams = existParams + name+ "\n";															
				}
				
				if(existParams.length() > 0)
					MainWindow.message(jobParameterShell, sos.scheduler.editor.app.Messages.getString("assistent.jobparameter.exist") + existParams, SWT.ICON_WARNING | SWT.OK );
			}
			tableDescParameters.remove(tableDescParameters.getSelectionIndices());
		}else {
			MainWindow.message(jobParameterShell, sos.scheduler.editor.app.Messages.getString("assistent.jobparameter.no_selected"), SWT.ICON_WARNING | SWT.OK );
		}
		tableDescParameters.deselectAll();
		table.deselectAll();
		txtName.setFocus();
	}
	
	private void removeParams() {
		if(table.getSelectionIndex()>-1){
			
			String remItem = "";
			
			int i = 0;
			while( table.getSelection().length > 0) {
				
				TableItem item = table.getItem(table.getSelectionIndices()[i]);
				if(item.getBackground().equals(Options.getRequiredColor())) {
					
					remItem = remItem + "\n\t" + item.getText(0);
					table.deselect(table.getSelectionIndices()[i]);
					
				} else{							
					
					TableItem itemDesc = new TableItem(tableDescParameters, SWT.NONE);
					itemDesc.setText(0, item.getText(0));
					itemDesc.setText(1, item.getText(1));
					joblistener.deleteParameter(table,  table.getSelectionIndices()[i]);
				}
				
			}
			if(remItem.length() > 0)
				MainWindow.message(jobParameterShell, sos.scheduler.editor.app.Messages.getString("assistent.jobparameter.required") + remItem, SWT.ICON_WARNING | SWT.OK );
			
			table.remove(table.getSelectionIndices());
		}	else {
			MainWindow.message(jobParameterShell, sos.scheduler.editor.app.Messages.getString("assistent.jobparameter.no_selected_table") , SWT.ICON_WARNING | SWT.OK );
		}
		table.deselectAll();
		tableDescParameters.deselectAll();
		txtName.setFocus();
		
	}
}
