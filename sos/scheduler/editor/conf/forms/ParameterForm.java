/**
 * 
 */
package sos.scheduler.editor.conf.forms;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.swtdesigner.SWTResourceManager;

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.ParameterListener;
import sos.util.SOSString;

/**
 * @author sky2000
 */
public class ParameterForm extends Composite implements IUnsaved, IUpdateLanguage {
	
	private Label       label4_3          = null;
	
	private Label       label4_1          = null;
	
	private ParameterListener listener    = null;
	
	private Group       gJobParameter     = null;
	
	private Table       tParameter        = null;
	
	private Button      bRemove           = null;
	
	private Label       label2            = null;
	
	private Text        tParaName         = null;
	
	private Label       label6            = null;
	
	private Text        tParaValue        = null;
	
	private Button      bApply            = null;
	
	private Label       label4            = null;
	
	private Text        txtParameterDescription  = null;
	
	private Table       tableEnvironment  = null; 
	
	private Text        txtEnvName        = null;
	
	private Text        txtEnvValue       = null;
	
	private Button      butEnvApply       = null; 
	
	private Button      butEnvRemove      = null; 
	
	private Text        txtIncludeFilename = null;
	
	private Text        txtIncludeNode     = null; 
	
	private Table       tableIncludeParams = null; 
	
	private Button      butIncludesApply   = null; 
	
	private Button      butImport          = null;
	
	private Button      butOpenInclude     = null;
	
	private Button      butRemoveInclude   = null;
	
	private CTabFolder  tabFolder          = null;
	
	private SOSString   sosString          = null;
	
	private JobListener jobListener        = null;
	
	private JobForm     jobForm            = null;
		
	private SchedulerDom dom              = null; 
	
	private CTabItem includeParameterTabItem = null; 
	
	private CTabItem parameterTabItem     = null;
	
	private CTabItem environmentTabItem   = null;
	
	private int      type                 = -1;
	

    /**
     * @param parent
     * @param style
     * @throws JDOMException
     */
    public ParameterForm(Composite parent, int style, 
    		SchedulerDom _dom, 
			Element parentElem, 
			ISchedulerUpdate main, 			
			int type_) throws JDOMException {
        
    	super(parent, style);       
        dom = _dom;
        type = type_;
		listener = new ParameterListener(dom, parentElem, main, Editor.CONFIG);		
		initialize();       
        setToolTipText();
        
    }

    private void initialize() {
		sosString = new SOSString(); 
		this.setLayout(new FillLayout());
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		gJobParameter = new Group(this, SWT.NONE);
		gJobParameter.setText("Parameter");
		gJobParameter.setLayout(gridLayout2);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gJobParameter.setLayoutData(gridData_1);
		
		
		createParameterGroup();		
		
		//dom.setInit(true);			
		
		initForm();
		
		//dom.setInit(false);
	}
	
	public void apply() {
		if (isUnsaved())
			addParam();
	}
	
	
	public boolean isUnsaved() {
		//return bApply.isEnabled();
		return false;
	}
	
	/**
	 * This method initializes group2
	 */
	public void createParameterGroup() {
		
		createParameter();
		
		//Environment
		if(type == Editor.JOB)
			createEnvironment();
		
		//Includes
		createIncludes();
		      
		//test
		//createParameterTabItem(); 
		
		setToolTipText();
	}
	
	
	
	
	private void addParam() {
		if(!tParaName.getText().equals(""))
			listener.saveParameter( tParameter, tParaName.getText().trim(), tParaValue.getText());
		tParaName.setText("");
		tParaValue.setText("");
		bRemove.setEnabled(false);
		tParameter.deselectAll();
		tParaName.setFocus();
		
	}
	
	private void addEnvironment() {
		listener.saveEnvironment( tableEnvironment, txtEnvName.getText().trim(), txtEnvValue.getText());
		txtEnvName.setText("");
		txtEnvValue.setText("");
		butEnvRemove.setEnabled(false);
		butEnvApply.setEnabled(false);
		tableEnvironment.deselectAll();
		txtEnvName.setFocus();
		
	}
	
	private void addInclude() {
		listener.saveIncludeParams( tableIncludeParams, txtIncludeFilename.getText().trim(), txtIncludeNode.getText());
		txtIncludeFilename.setText("");
		txtIncludeNode.setText("");
		butIncludesApply.setEnabled(false);
		butRemoveInclude.setEnabled(false);
		butOpenInclude.setEnabled(false);
		tableIncludeParams.deselectAll();
		txtIncludeFilename.setFocus();    	
	}
	
	public void initForm(){
		
		tParameter.removeAll();
		if(jobListener != null && jobListener.getInclude() != null && jobListener.getInclude().trim().length() > 0) {
			if(new File(Options.getSchedulerHome().endsWith("/") || Options.getSchedulerHome().endsWith("\\") ? Options.getSchedulerHome(): Options.getSchedulerHome() + "/" + jobListener.getInclude()).exists())
				listener.getAllParameterDescription();
		}
		listener.fillParams(tParameter);
		listener.fillEnvironment(tableEnvironment);
		listener.fillIncludeParams(tableIncludeParams);
		
		
		
	}
	
	
	public void setToolTipText() {
		tParaName.setToolTipText(Messages.getTooltip("job.param.name"));
		tParaValue.setToolTipText(Messages.getTooltip("job.param.value"));
		bRemove.setToolTipText(Messages.getTooltip("job.param.btn_remove"));
		bApply.setToolTipText(Messages.getTooltip("job.param.btn_add"));
		tParameter.setToolTipText(Messages.getTooltip("job.param.table"));
		if(type == Editor.JOB) {
			txtParameterDescription.setToolTipText(Messages.getTooltip("job.param.description"));		
			tableEnvironment.setToolTipText(Messages.getTooltip("job.environment.table"));        
			txtEnvName.setToolTipText(Messages.getTooltip("job.environment.name"));        
			txtEnvValue.setToolTipText(Messages.getTooltip("job.environment.value"));        
			butEnvApply.setToolTipText(Messages.getTooltip("job.environment.btn_apply"));         
			butEnvRemove.setToolTipText(Messages.getTooltip("job.environment.btn_remove"));
		}
	}
	
	
	private void startWizzard(boolean onlyParams) {
		if(jobListener.getInclude()!= null && jobListener.getInclude().trim().length() > 0) {
			//JobDokumentation ist bekannt -> d.h Parameter aus dieser Jobdoku extrahieren        			
			JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(listener.get_dom(), listener.get_main(), jobListener, tParameter, onlyParams ? Editor.JOB : Editor.JOB_WIZZARD);
			if(!onlyParams)
				paramsForm.setJobForm(jobForm);
			paramsForm.showAllImportJobParams(jobListener.getInclude());        			
		} else { 
			//Liste aller Jobdokumentation 
			JobAssistentImportJobsForm importParameterForms = new JobAssistentImportJobsForm(jobListener, tParameter, onlyParams ? Editor.JOB : Editor.JOB_WIZZARD);
			if(!onlyParams)
				importParameterForms.setJobForm(jobForm);
			importParameterForms.showAllImportJobs();
		}
	}
	
	
	public Table getTParameter() {
		return tParameter;
	}
	
	private void createParameterTabItem() {
		
		
		
		Element    params            = null;				
		
		final String node     = txtIncludeNode.getText();
		
		try {
			String f = txtIncludeFilename.getText();
			boolean fNotExist = false;
			if(!new File(f).exists()) {
				if(f.startsWith("/")) {
					String home = Options.getSchedulerHome();					
					f =  (home+ f);
					if(!new File(f).exists()) {
						fNotExist = true;
					}
				} else {
					fNotExist = true;
				}
				if(fNotExist) {
					MainWindow.message("file not exist: " + f, SWT.ICON_WARNING);
					return;
				}
			}
			
			final String filename = f;	
			
			
			for(int i = 0; i < tabFolder.getItemCount(); i++) {
				if(sosString.parseToString(tabFolder.getItem(i).getData("filename")).equals(filename) &&
						sosString.parseToString(tabFolder.getItem(i).getData("node")).equals(node)) {
					tabFolder.setSelection(tabFolder.getItem(i));
					return;
				}				
			}
			
			SAXBuilder builder = new SAXBuilder();
			
			
			final Document   doc= builder.build(filename);						
			java.util.List listOfElement = null;
			if(txtIncludeNode.getText() != null && txtIncludeNode.getText().length() > 0) {
				XPath x = XPath.newInstance(txtIncludeNode.getText());
				//Element e = (Element)x.selectSingleNode(doc);
				listOfElement = x.selectNodes(doc);												
				
			}else {
				listOfElement = new java.util.ArrayList();				
				params = doc.getRootElement();
				listOfElement.add(params);
			}
			
			java.util.HashMap hash = new java.util.HashMap(); //hilfsvariable
			for(int j = 0; j < listOfElement.size(); j++) {
				//Parametername in unterschiedlichen XPaths darf nur einmal vorkommen
				params = (Element)listOfElement.get(j);
				java.util.List paramList = params.getChildren("param");
				for(int i = 0; i < paramList.size(); i++) {
					Element param = (Element)paramList.get(i);					
					if(hash.containsKey(Utils.getAttributeValue("name", param))) {
						MainWindow.message("There is not a clearly Parameter: " + Utils.getAttributeValue("name", param), SWT.ICON_WARNING);
						return;
					}
					hash.put(Utils.getAttributeValue("name", param), "");
							
				}
			}
			
			includeParameterTabItem = new CTabItem(tabFolder, SWT.CLOSE);
			includeParameterTabItem.setText(new File(filename).getName());
			includeParameterTabItem.setData("filename", filename);
			includeParameterTabItem.setData("node", node);
			includeParameterTabItem.setData("doc", doc);
			//includeParameterTabItem.setData("params", params);
			includeParameterTabItem.setData("params", listOfElement);

			// --> bis hier alles in listener übernehmen
			
			final Group group_1 = new Group(tabFolder, SWT.NONE);
			
			group_1.setText(txtIncludeFilename.getText());
			
			final GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 5;
			group_1.setLayout(gridLayout);
			includeParameterTabItem.setControl(group_1);
			label2 = new Label(group_1, SWT.NONE);
			label2.setText("Name: ");
			final Text txtIncludeParameter = new Text(group_1, SWT.BORDER);
			final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
			txtIncludeParameter.setLayoutData(gridData_4);
			
			
			label6 = new Label(group_1, SWT.NONE);
			label6.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			label6.setText("Value: ");
			final Text txtIncludeParameterValue = new Text(group_1, SWT.BORDER);
			final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, true, false);
			txtIncludeParameterValue.setLayoutData(gridData_9);
			
			final Button butoIncludeSave = new Button(group_1, SWT.NONE);
			butoIncludeSave.setEnabled(false);
			final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
			gridData_7.widthHint = 36;
			butoIncludeSave.setLayoutData(gridData_7);
			butoIncludeSave.setText("Save");			
			
			txtIncludeParameterValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
				public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
					butoIncludeSave.setEnabled(!txtIncludeParameter.getText().equals(""));
				}
			});
			txtIncludeParameter.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
				public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
					butoIncludeSave.setEnabled(!txtIncludeParameter.getText().equals(""));
				}
			});
			
			label4 = new Label(group_1, SWT.SEPARATOR | SWT.HORIZONTAL);
			label4.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1));
			label4.setText("Label");
			final Table tableIncludeParameter = new Table(group_1, SWT.BORDER | SWT.FULL_SELECTION);
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
			gridData_1.heightHint = 85;
			
			tableIncludeParameter.setLayoutData(gridData_1);
			tableIncludeParameter.setHeaderVisible(true);
			tableIncludeParameter.setLinesVisible(true);
			
			TableColumn tcName = new TableColumn(tableIncludeParameter, SWT.NONE);
			tcName.setWidth(132);
			tcName.setText("Name");
			TableColumn tcValue = new TableColumn(tableIncludeParameter, SWT.NONE);
			tcValue.setWidth(450);
			tcValue.setText("Value");
			
			
			//fill Include Params From External File
			for(int j = 0; j < listOfElement.size(); j++) {
				params = (Element)listOfElement.get(j);
				java.util.List paramList = params.getChildren("param");
				for(int i = 0; i < paramList.size(); i++) {
					Element param = (Element)paramList.get(i);
					TableItem item = new TableItem( tableIncludeParameter, SWT.NONE);
					item.setText(0, Utils.getAttributeValue("name", param));
					item.setText(1, Utils.getAttributeValue("value", param));
					item.setData("param", param);
				}
			}
			
			
			final Button butIncludeRemove = new Button(group_1, SWT.NONE);
			final GridData gridData_8 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
			butIncludeRemove.setLayoutData(gridData_8);
			butIncludeRemove.setText("Remove");
			butIncludeRemove.setEnabled(false);
			butIncludeRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					updateIncludeParam(includeParameterTabItem, 
							false,
							tableIncludeParameter, 
							txtIncludeParameter, 
							txtIncludeParameterValue,
							butIncludeRemove);
				}
			});
			if(type == Editor.JOB) {
				//TODO: muss angepasst werden
				butImport = new Button(group_1, SWT.NONE);
				butImport.setVisible(false);
				butImport.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
				butImport.setText("import");
				butImport.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						//startWizzard(false);
						JobAssistentImportJobsForm importParameterForms = new JobAssistentImportJobsForm(jobListener, tableIncludeParameter, Editor.JOB);					
						//importParameterForms.setParameterForm(this);
						importParameterForms.showAllImportJobs();
					}
				});
				butImport.setText("Import");
			}
			
			
			txtIncludeParameterValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
				public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
					if (e.keyCode == SWT.CR && !txtIncludeParameter.getText().trim().equals("")){						
						updateIncludeParam(includeParameterTabItem, 
								true,
								tableIncludeParameter, 
								txtIncludeParameter, 
								txtIncludeParameterValue,
								butIncludeRemove);
						
					}
				}
			});
			txtIncludeParameter.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
				public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
					if (e.keyCode == SWT.CR && !txtIncludeParameter.equals("")) {						
						updateIncludeParam(includeParameterTabItem, 
								true,
								tableIncludeParameter, 
								txtIncludeParameter, 
								txtIncludeParameterValue,
								butIncludeRemove);
					}
				}
			});
			
			butoIncludeSave.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
										
					updateIncludeParam(includeParameterTabItem, 
							true,
							tableIncludeParameter, 
							txtIncludeParameter, 
							txtIncludeParameterValue,
							butIncludeRemove);
					
				}
			});
			
			tableIncludeParameter.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					TableItem item = (TableItem) e.item;
					if (item == null)
						return;
					txtIncludeParameter.setText(item.getText(0));
					txtIncludeParameterValue.setText(item.getText(1));
					butIncludeRemove.setEnabled(tableIncludeParameter.getSelectionCount() > 0);
					
					// TODO butOpen.setEnabled(tableIncludeParams.getSelectionCount() > 0);
					butoIncludeSave.setEnabled(false);
				}
			});
			
			tabFolder.setSelection(includeParameterTabItem);
		} catch(Exception e) {
			//System.err.println("..error : " + e.getMessage());
			MainWindow.message("could not create Tabitem cause: " , SWT.ICON_WARNING);
		}
	}
	
	private void updateIncludeParam(CTabItem includeParameterTabItem, 
			boolean add, 
			Table tableIncludeParameter, 
			Text txtIncludeParameter, 
			Text txtIncludeParameterValue,
			Button butIncludeRemove) {
		
		Document doc = (Document)includeParameterTabItem.getData("doc");
		String filename = (String)includeParameterTabItem.getData("filename");
		java.util.List listOfElement = (java.util.List)includeParameterTabItem.getData("params");
		//Element params =  (Element)includeParameterTabItem.getData("params");
		
		if(add) {
			//neue Parameter bzw. editieren vorhandene Parameter 		
			boolean found = false;
			for (int i =0; i < tableIncludeParameter.getItemCount(); i++) {
				TableItem item = tableIncludeParameter.getItem(i);
				if(item.getText(0).equals(txtIncludeParameter.getText())) {
					found = true;
					item.setText(0, txtIncludeParameter.getText());
					item.setText(1, txtIncludeParameterValue.getText());
					Element param = (Element)item.getData("param");
					param.setAttribute("name", item.getText(0) );
					param.setAttribute("value", item.getText(1) );
				}
			}
			if(!found) {
				if(listOfElement.size() == 1) {
					TableItem item = new TableItem(tableIncludeParameter, SWT.NONE);
					item.setText(0, txtIncludeParameter.getText());
					item.setText(1, txtIncludeParameterValue.getText());
					
					Element params = (Element)listOfElement.get(0);
					Element param = new Element("param");
					param.setAttribute("name", item.getText(0) );
					param.setAttribute("value", item.getText(1) );
					params.addContent(param);
				} else {
					MainWindow.message("could not save cause note path ist not clearly", SWT.ICON_WARNING);
				}

			}
		} else {
//			parameter löschen
			if(tableIncludeParameter.getSelectionCount()>0) {				
				Element param = (Element)tableIncludeParameter.getSelection()[0].getData("param");
				Element params = (Element)listOfElement.get(0);
				params.removeContent(param);
				tableIncludeParameter.remove(tableIncludeParameter.getSelectionIndex());
				
			}
		}
		
		/*for(int j =0; j < listOfElement.size(); j++) {
			Element params = (Element)listOfElement.get(j);
			params.removeChildren("param");
			for (int i =0; i < tableIncludeParameter.getItemCount(); i++) {
				TableItem item = tableIncludeParameter.getItem(i);
				Element param = new Element("param");
				param.setAttribute("name", item.getText(0) );
				param.setAttribute("value", item.getText(1) );
				params.addContent(param);
			}
		}*/
		
		IOUtils.saveXML(doc, filename);
		
		txtIncludeParameter.setText("");
		txtIncludeParameterValue.setText("");
		butIncludeRemove.setEnabled(false);
		tableIncludeParameter.deselectAll();
		txtIncludeParameter.setFocus();
	}
	
	
	public void setJobForm(JobForm jobForm) {
		this.jobForm = jobForm;
	}
	
	private void createParameter() {
		
		
		
		//gJobParameter = new Group(mainGroup, SWT.NONE);
		/*if(sashForm != null)
			gJobParameter = new Group(sashForm, SWT.NONE);
		else {
			gJobParameter = new Group(mainGroup, SWT.NONE);
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
			gJobParameter.setLayoutData(gridData_1);
		}*/
		
		//gJobParameter.setText("Parameter");
		//gJobParameter.setLayout(gridLayout1);
		//gridLayout1.marginHeight =400;
		
		//new CTabFolder(parent, SWT.TOP | SWT.CLOSE);
				
		tabFolder = new CTabFolder(gJobParameter, SWT.CLOSE | SWT.BORDER);
		
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData_2.heightHint = 203;
		gridData_2.widthHint = 760;
		tabFolder.setLayoutData(gridData_2);
		
		//Parameter
		
		parameterTabItem = new CTabItem(tabFolder, SWT.BORDER | SWT.CLOSE);
		parameterTabItem.setText("Parameter");
		
		final Group group_1 = new Group(tabFolder, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		group_1.setLayout(gridLayout);
		parameterTabItem.setControl(group_1);
		label2 = new Label(group_1, SWT.NONE);
		label2.setText("Name: ");
		tParaName = new Text(group_1, SWT.BORDER);
		final GridData gridData_4 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		tParaName.setLayoutData(gridData_4);
		tParaName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR && !tParaName.getText().equals(""))
					addParam();
			}
		});
		tParaName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(!tParaName.getText().trim().equals(""));
			}
		});
		label6 = new Label(group_1, SWT.NONE);
		label6.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		label6.setText("Value: ");
		tParaValue = new Text(group_1, SWT.BORDER);
		final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		tParaValue.setLayoutData(gridData_9);
		tParaValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR && !tParaName.getText().trim().equals(""))
					addParam();
			}
		});
		tParaValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(!tParaName.getText().equals(""));
			}
		});
		bApply = new Button(group_1, SWT.NONE);
		final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_7.widthHint = 36;
		bApply.setLayoutData(gridData_7);
		bApply.setText("&Apply");
		bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				addParam();
			}
		});
		label4 = new Label(group_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label4.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1));
		label4.setText("Label");
		tParameter = new Table(group_1, SWT.BORDER | SWT.FULL_SELECTION);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2);
		gridData_1.heightHint = 85;
		tParameter.setLayoutData(gridData_1);
		tParameter.setHeaderVisible(true);
		tParameter.setLinesVisible(true);
		tParameter.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				tParaName.setText(item.getText(0));
				tParaValue.setText(item.getText(1));
				bRemove.setEnabled(tParameter.getSelectionCount() > 0);
				if(type == Editor.JOB) {
					txtParameterDescription.setText(listener.getParameterDescription(item.getText(0)));
				}
				///TODO butOpen.setEnabled(tableIncludeParams.getSelectionCount() > 0);
				bApply.setEnabled(false);
			}
		});
		TableColumn tcName = new TableColumn(tParameter, SWT.NONE);
		tcName.setWidth(132);
		tcName.setText("Name");
		TableColumn tcValue = new TableColumn(tParameter, SWT.NONE);
		tcValue.setWidth(450);
		tcValue.setText("Value");
		
		if(type == Editor.JOB) {
			butImport = new Button(group_1, SWT.NONE);
			butImport.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			butImport.setText("import");
			butImport.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					startWizzard(true);
				}
			});
			butImport.setText("Import");
		}
		
		bRemove = new Button(group_1, SWT.NONE);
		final GridData gridData_8 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
		bRemove.setLayoutData(gridData_8);
		bRemove.setText("Remove");
		bRemove.setEnabled(false);
		bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				listener.deleteParameter(tParameter, tParameter.getSelectionIndex());
				tParaName.setText("");
				tParaValue.setText("");
				tParameter.deselectAll();
				bRemove.setEnabled(false);
				bApply.setEnabled(false);
				//TODO butOpen.setEnabled(tableIncludeParams.getSelectionCount() > 0);
			}
		});
		if(type == Editor.JOB) {
			txtParameterDescription = new Text(group_1, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.WRAP);        
			final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, true, 4, 1);
			gridData.heightHint = 51;
			txtParameterDescription.setLayoutData(gridData);
			txtParameterDescription.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					tParaName.setFocus();
				}
			});
			
			txtParameterDescription.setEditable(false);
			txtParameterDescription.setBackground(SWTResourceManager.getColor(247, 247, 247));        
			new Label(group_1, SWT.NONE);
		}
	}
	
	private void createEnvironment() {
		environmentTabItem = new CTabItem(tabFolder, SWT.CLOSE);
		environmentTabItem.setText("Environment");
		
		final Group group_2 = new Group(tabFolder, SWT.NONE);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 5;
		group_2.setLayout(gridLayout_1);
		environmentTabItem.setControl(group_2);
		
		final Label nameLabel = new Label(group_2, SWT.NONE);
		nameLabel.setText("Name: ");
		
		txtEnvName = new Text(group_2, SWT.BORDER);
		txtEnvName.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butEnvApply.setEnabled(!txtEnvName.getText().trim().equals(""));
			}
		});
		txtEnvName.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR && !txtEnvName.equals(""))
					addEnvironment();
			}
		});
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		txtEnvName.setLayoutData(gridData_5);
		
		final Label valueLabel = new Label(group_2, SWT.NONE);
		valueLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		valueLabel.setText("Value: ");
		
		txtEnvValue = new Text(group_2, SWT.BORDER);
		txtEnvValue.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butEnvApply.setEnabled(!txtEnvName.getText().trim().equals(""));
			}
		});
		txtEnvValue.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR && !txtEnvName.equals(""))
					addEnvironment();
			}
		});
		txtEnvValue.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		butEnvApply = new Button(group_2, SWT.NONE);
		butEnvApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addEnvironment();
			}
		});
		final GridData gridData_6 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		butEnvApply.setLayoutData(gridData_6);
		butEnvApply.setText("Apply");
		
		label4_1 = new Label(group_2, SWT.HORIZONTAL | SWT.SEPARATOR);
		label4_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1));
		label4_1.setText("Label");
		
		tableEnvironment = new Table(group_2, SWT.FULL_SELECTION | SWT.BORDER);
		tableEnvironment.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				txtEnvName.setText(item.getText(0));
				txtEnvValue.setText(item.getText(1));
				butEnvRemove.setEnabled(tableEnvironment.getSelectionCount() > 0);                                
				butEnvApply.setEnabled(false);
			}
		});
		tableEnvironment.setLinesVisible(true);
		tableEnvironment.setHeaderVisible(true);
		tableEnvironment.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1));
		
		final TableColumn colEnvName = new TableColumn(tableEnvironment, SWT.NONE);
		colEnvName.setWidth(205);
		colEnvName.setText("Name");
		
		final TableColumn colEnvValue = new TableColumn(tableEnvironment, SWT.NONE);
		colEnvValue.setWidth(522);
		colEnvValue.setText("Value");
		
		butEnvRemove = new Button(group_2, SWT.NONE);
		butEnvRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.deleteEnvironment(tableEnvironment, tableEnvironment.getSelectionIndex());
				txtEnvName.setText("");
				txtEnvValue.setText("");
				tableEnvironment.deselectAll();
				butEnvApply.setEnabled(false);
				butEnvRemove.setEnabled(false);
			}
		});
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		butEnvRemove.setLayoutData(gridData_3);
		butEnvRemove.setText("Remove");
		
	}
	
	private void createIncludes() {
		final CTabItem includesTabItem = new CTabItem(tabFolder, SWT.CLOSE);
		includesTabItem.setText("Includes");
		
		final Group group_3 = new Group(tabFolder, SWT.NONE);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 5;
		group_3.setLayout(gridLayout_2);
		includesTabItem.setControl(group_3);				
		
		
		final Label lblFilename = new Label(group_3, SWT.NONE);
		final GridData gridData = new GridData(31, SWT.DEFAULT);
		lblFilename.setLayoutData(gridData);
		lblFilename.setText("File:");
		
		txtIncludeFilename = new Text(group_3, SWT.BORDER);
		txtIncludeFilename.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butIncludesApply.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
			}
		});
		txtIncludeFilename.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR && !txtIncludeFilename.equals(""))
					addInclude();
			}
		});
		txtIncludeFilename.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		final Label lblNode = new Label(group_3, SWT.NONE);
		lblNode.setText("Node:");
		
		txtIncludeNode = new Text(group_3, SWT.BORDER);
		txtIncludeNode.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butIncludesApply.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
			}
		});
		txtIncludeNode.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR && !txtIncludeFilename.equals(""))
					addInclude();
			}
		});
		txtIncludeNode.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		butIncludesApply = new Button(group_3, SWT.NONE);
		butIncludesApply.setEnabled(false);
		butIncludesApply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				addInclude();
			}
		});
		butIncludesApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butIncludesApply.setText("Apply");

		label4_3 = new Label(group_3, SWT.HORIZONTAL | SWT.SEPARATOR);
		label4_3.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1));
		label4_3.setText("Label");
		
		tableIncludeParams = new Table(group_3, SWT.FULL_SELECTION | SWT.BORDER);
		tableIncludeParams.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(final MouseEvent e) {
				createParameterTabItem();
			}
		});
		tableIncludeParams.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				txtIncludeFilename.setText(item.getText(0));
				txtIncludeNode.setText(item.getText(1));
				butRemoveInclude.setEnabled(tableIncludeParams.getSelectionCount() > 0);                               
				butIncludesApply.setEnabled(false);
				butOpenInclude.setEnabled(true);				
			}
		});
		tableIncludeParams.setLinesVisible(true);
		tableIncludeParams.setHeaderVisible(true);
		tableIncludeParams.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 3));
		
		final TableColumn colParamColums = new TableColumn(tableIncludeParams, SWT.NONE);
		colParamColums.setWidth(318);
		colParamColums.setText("File");
		
		final TableColumn newColumnTableColumn_1 = new TableColumn(tableIncludeParams, SWT.NONE);
		newColumnTableColumn_1.setWidth(417);
		newColumnTableColumn_1.setText("Node");

		final Button butNewIncludes = new Button(group_3, SWT.NONE);
		butNewIncludes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tableIncludeParams.deselectAll();
				txtIncludeFilename.setText("");
				txtIncludeNode.setText("");
				butIncludesApply.setEnabled(false);
				butOpenInclude.setEnabled(false);
				butRemoveInclude.setEnabled(false);
			}
		});
		butNewIncludes.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butNewIncludes.setText("New");
		
		butOpenInclude = new Button(group_3, SWT.NONE);
		butOpenInclude.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				createParameterTabItem();        		
			}
		});
		butOpenInclude.setEnabled(false);
		butOpenInclude.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butOpenInclude.setText("Open");
		
		butRemoveInclude = new Button(group_3, SWT.NONE);
		butRemoveInclude.setEnabled(false);
		butRemoveInclude.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.deleteIncludeParams(tableIncludeParams, tableIncludeParams.getSelectionIndex());
				txtIncludeFilename.setText("");
				txtIncludeNode.setText("");
				tableIncludeParams.deselectAll();
				butIncludesApply.setEnabled(false);
				butRemoveInclude.setEnabled(false);
			}
		});
		butRemoveInclude.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butRemoveInclude.setText("Remove");
		
	
		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void close(final CTabFolderEvent e) {
				
				if (e.item.equals(parameterTabItem) || 
						e.item.equals(environmentTabItem) || 
						e.item.equals(includesTabItem)) {
					e.doit = false;
				}
				
			}
		});
		tabFolder.setSelection(0);
	}
    
} // @jve:decl-index=0:visual-constraint="10,10"
