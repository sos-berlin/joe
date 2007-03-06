package sos.scheduler.editor.conf.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.layout.FillLayout;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.JobsListener;
import sos.util.SOSClassUtil;
import sos.util.SOSString;

public class ShowAllImportJobParamsForm {
	private Text txtDescription         = null;
	private Table table                 = null;
	private Shell jobParameterShell     = null;
	private Text txtValue               = null;
	
	private String xmlPaths             = null;
	private Text txtName                = null; 
	private JobsListener listener;
	private JobListener joblistener;
	//TAbelle aus der JobFrom
	private Table tParameter = null; 
	private SOSString sosString         = new SOSString();
	
	/** Beiinhaltet Informationen aus der Dialog import Jobs: name, titel und Pfad des Job, */
	private HashMap jobInfo             = null;
	
	private Button butApply         = null;
	private Button butImport            = null;
	private Button butRemove         = null;
	
	public ShowAllImportJobParamsForm() {		
	}
	
	public ShowAllImportJobParamsForm(SchedulerDom dom, ISchedulerUpdate update, HashMap jobInfo_) {
		listener = new JobsListener(dom, update);
		HashMap h = new HashMap();		
		jobInfo = jobInfo_;		
	}
	
	public ShowAllImportJobParamsForm(SchedulerDom dom, ISchedulerUpdate update, JobListener joblistener_, Table tParameter_,HashMap jobInfo_) {
		joblistener = joblistener_;
		HashMap h = new HashMap();
		tParameter = tParameter_;
		jobInfo = jobInfo_;		
	}
	
	
	public ArrayList parseDocuments(String xmlFilename) {
		
		xmlPaths = sos.scheduler.editor.app.Options.getSchedulerHome() ;
		xmlPaths = (xmlPaths.endsWith("/") || xmlPaths.endsWith("\\") ? xmlPaths.concat("jobs") : xmlPaths.concat("/jobs"));
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
				HashMap h = null; //doc.getRootElement().getChild("description")
				for( int i=0; i<listMainElements.size(); i++ ){					
					Element elMain  = (Element)(listMainElements.get( i ));					
					if(elMain.getName().equalsIgnoreCase("param")) { 
						//System.out.println("Name= " + elMain.getAttributeValue("name") + ", default_value = " + elMain.getAttributeValue("default_value") + " required = " + elMain.getAttributeValue("required"));
						h = new HashMap();
						h.put("name", elMain.getAttributeValue("name"));
						h.put("default_value", sosString.parseToString(elMain.getAttributeValue("default_value")));
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
		jobParameterShell = new Shell();
		final GridLayout gridLayout = new GridLayout();
		jobParameterShell.setLayout(gridLayout);
		jobParameterShell.setText("Job Parameter");
		Label nameLabel;

		final Group group_1 = new Group(jobParameterShell, SWT.BORDER);
		final GridData gridData_3 = new GridData(531, 136);
		gridData_3.minimumWidth = -1;
		group_1.setLayoutData(gridData_3);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.numColumns = 5;
		group_1.setLayout(gridLayout_3);

		{
			nameLabel = new Label(group_1, SWT.NONE);
			nameLabel.setText("Name");
		}
		{
			txtName = new Text(group_1, SWT.BORDER);
			txtName.setLayoutData(new GridData(185, SWT.DEFAULT));
			txtName.setText("");
		}

		final Label lblTitle = new Label(group_1, SWT.NONE);
		lblTitle.setLayoutData(new GridData());
		lblTitle.setText("Value");

		txtValue = new Text(group_1, SWT.BORDER);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_5.widthHint = 160;
		txtValue.setLayoutData(gridData_5);
		{
			butApply = new Button(group_1, SWT.NONE);
			final GridData gridData = new GridData(57, SWT.DEFAULT);
			butApply.setLayoutData(gridData);
			butApply.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					System.out.println("apply");
					//if(table.getItems().
					boolean update = false;
					for (int i =0; i < table.getItemCount(); i++) {
						if(txtName.getText()!= null && txtName.getText().trim().length() == 0) {							
								int cont = MainWindow.message(jobParameterShell, sos.scheduler.editor.app.Messages.getString("no_param_name"), SWT.ICON_WARNING | SWT.OK );
								txtName.setFocus();
								return;
							}							
						
						if(table.getItem(i).getText(0).equalsIgnoreCase(txtName.getText())) {
							table.getItem(i).setText(0, txtName.getText());
							table.getItem(i).setText(1, txtValue.getText());
							table.getItem(i).setData(txtDescription.getText());
							update = true;
							break;
						}						
					}
					if(!update) {						
						/*TableItem item = new TableItem(table, SWT.NONE);
						item.addDisposeListener(new DisposeListener() {
							public void widgetDisposed(final DisposeEvent e) {
								if(table.getSelectionCount() > -1) {
									table.remove(table.getSelectionIndex());
								}									
							}
						});	
						*/		
						if(txtName.getText() != null && txtName.getText().trim().length() > 0) {
							TableItem item = new TableItem(table, SWT.NONE);
							item.setText(0,txtName.getText());
							item.setText(1, txtValue.getText() );							
							item.setData(txtDescription.getText());
						}
					}
				}
			});
			butApply.setText("Apply");
		}
		new Label(group_1, SWT.NONE);

		txtDescription = new Text(group_1, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
		final GridData gridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 3, 1);
		gridData.heightHint = 65;
		gridData.widthHint = 398;
		txtDescription.setLayoutData(gridData);

		butImport = new Button(group_1, SWT.NONE);
		butImport.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				ArrayList listOfParams = new ArrayList();
				
				for (int i = 0; i < table.getItemCount(); i++) {
					HashMap h = new HashMap();
					if(table.getItem(i).getText(0) != null && table.getItem(i).getText(0).length() > 0) {
						h.put("name", table.getItem(i).getText(0));
						h.put("default_value", table.getItem(i).getText(1));
						listOfParams.add(h);
					}
				}
				jobInfo.put("params", listOfParams);
				if(listener != null)
					listener.newImportJob(jobInfo);
				else
					joblistener.fillParams(listOfParams, tParameter);
				jobParameterShell.close();
			}
		});
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		gridData_4.widthHint = 54;
		butImport.setLayoutData(gridData_4);
		butImport.setText("Import");
		
		ArrayList listOfParams = this.parseDocuments(xmlFilename);
		final Group jobnamenGroup = new Group(jobParameterShell, SWT.BORDER);
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_2.heightHint = 417;
		gridData_2.widthHint = 522;
		jobnamenGroup.setLayoutData(gridData_2);
		jobnamenGroup.setBounds(5, 79,483, 264);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		jobnamenGroup.setLayout(gridLayout_1);

		table = new Table(jobnamenGroup, SWT.FULL_SELECTION | SWT.BORDER);
        table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(table.getSelectionCount() > -1) {
					//System.out.println("1: " + table.getSelection()[0].getText(0));
					txtName.setText(table.getSelection()[0].getText(0));
					//System.out.println("1: " + table.getSelection()[0].getText(1));
					txtValue.setText(table.getSelection()[0].getText(1));
					//System.out.println("1: " + table.getSelection()[0].getData());
					txtDescription.setText((table.getSelection()[0].getData() != null? table.getSelection()[0].getData().toString(): "") );
				}
			}
		});
		table.setLayoutDeferred(true);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 1, 3);
		gridData_1.widthHint = 392;
		table.setLayoutData(gridData_1);

		final TableColumn colName = new TableColumn(table, SWT.NONE);
		
		colName.setWidth(200);
		colName.setText("Name");

		final TableColumn colValue = new TableColumn(table, SWT.NONE);
		colValue.setWidth(230);
		colValue.setText("Value");
			
		final TableColumn colDescription = new TableColumn(table, SWT.NONE);
		colDescription.setWidth(200);
		colDescription.setWidth(0);
		colDescription.setResizable(false);
		colDescription.setText("Description");

		butRemove = new Button(jobnamenGroup, SWT.NONE);
		butRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if(table.getSelectionCount() > -1) {
					table.remove(table.getSelectionIndex());
				}
			}
		});
		butRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butRemove.setText("Remove");
		fillTable(listOfParams);
		setToolTipText();
		jobParameterShell.layout();
		jobParameterShell.pack();
		jobParameterShell.open();
		} catch (Exception e) {
			System.out.println("..error in ShowAllImportJobParamsForm.showAllImportJobParams " + ": " + e.getMessage());
		}
	}
				
	
	public void fillTable(ArrayList list) throws Exception {
		try {
			HashMap h = new HashMap();
			table.removeAll();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					h = (HashMap)list.get(i);	                	                										
					TableItem item = new TableItem(table, SWT.NONE);
					item.addDisposeListener(new DisposeListener() {
						public void widgetDisposed(final DisposeEvent e) {
							/*if(table.getSelectionCount() > -1) {
								table.remove(table.getSelectionIndex());
							}*/
							//System.out.println("ddddddddd");
						}
					});
					if(sosString.parseToBoolean(h.get("required"))) {
						item.setBackground(Options.getRequiredColor());
					} else {
						item.setBackground(null);
					}
					item.setChecked(true);
					item.setText(0,sosString.parseToString(h.get("name")));
					item.setText(1, sosString.parseToString(h.get("default_value")));
					//item.setText(3, sosString.parseToString(h.get("required")));
					String desc = sosString.parseToString(h.get("description"));		
					//desc = desc.replaceAll("  ", " ");
					//desc = desc.replaceAll("\n", " ");
					//desc = desc.replaceAll("\t", " ");
					//item.setText(2, desc);
					item.setData(sosString.parseToString(h.get("description")));
					
					
				}
			}
		} catch (Exception e) {
			throw new Exception("error in " + SOSClassUtil.getMethodName() + " " + e.toString());
		}
	}
	
	public void setToolTipText() {
		txtName.setToolTipText(Messages.getTooltip("job.param.name"));
		txtValue.setToolTipText(Messages.getTooltip("job.param.value"));
		table.setToolTipText(Messages.getTooltip("tableparams"));
		butApply.setToolTipText(Messages.getTooltip("jobparameter.apply"));
		butRemove.setToolTipText(Messages.getTooltip("jobparameter.remove"));
		butImport.setToolTipText(Messages.getTooltip("jobparameter.import"));
		
		//txtDescription
	
    }
}
