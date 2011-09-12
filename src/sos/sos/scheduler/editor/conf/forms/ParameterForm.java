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
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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

import sos.scheduler.editor.app.Editor;
import sos.scheduler.editor.app.IOUtils;
import sos.scheduler.editor.app.IUnsaved;
import sos.scheduler.editor.app.IUpdateLanguage;
import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Messages;
import sos.scheduler.editor.app.Options;
import sos.scheduler.editor.app.ResourceManager;
import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.ISchedulerUpdate;
import sos.scheduler.editor.conf.SchedulerDom;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.ParameterListener;
import sos.util.SOSString;

import com.swtdesigner.SWTResourceManager;

public class ParameterForm extends Composite implements IUnsaved, IUpdateLanguage {
	private Button				butDown_1				= null;
	private Button				butUp_1					= null;
	private Button				butImport_1				= null;
	private Label				label4_3				= null;
	private Label				label4_1				= null;
	private ParameterListener	listener				= null;
	private Group				gJobParameter			= null;
	private Table				tParameter				= null;
	private Button				bRemove					= null;
	private Label				label2					= null;
	private Text				tParaName				= null;
	private Label				label6					= null;
	private Text				tParaValue				= null;
	private Button				bApply					= null;
	private Label				label4					= null;
	private Text				txtParameterDescription	= null;
	private Table				tableEnvironment		= null;
	private Text				txtEnvName				= null;
	private Text				txtEnvValue				= null;
	private Button				butEnvApply				= null;
	private Button				butEnvRemove			= null;
	private Text				txtIncludeFilename		= null;
	private Text				txtIncludeNode			= null;
	private Table				tableIncludeParams		= null;
	private Button				butIncludesApply		= null;
	private Button				butImport				= null;
	private Button				butOpenInclude			= null;
	private Button				butRemoveInclude		= null;
	private CTabFolder			tabFolder				= null;
	private SOSString			sosString				= null;
	private SchedulerDom		dom						= null;
	private CTabItem			includeParameterTabItem	= null;
	private CTabItem			parameterTabItem		= null;
	private CTabItem			environmentTabItem		= null;
	private int					type					= -1;
	private Combo				cSource					= null;
	private CTabItem			parameterJobCmdTabItem	= null;
	private Group				group					= null;
	private String				includeFile				= null;
	private Button				butNewIncludes			= null;
	private Button				butIsLifeFile			= null;
	private Button				butDown					= null;
	private Button				butUp					= null;
	private Button				butNewParam				= null;
	private Button				butNewEnvironment		= null;
	private Button				butoIncludeSave			= null;
	private boolean				isRemoteConnection		= false;
	public static String		WIZARD					= "Wizard";

	/**
	 * @param parent
	 * @param style
	 * @throws JDOMException
	 */
	public ParameterForm(Composite parent, int style, SchedulerDom _dom, Element parentElem, ISchedulerUpdate main, int type_) throws JDOMException {
		super(parent, style);
		dom = _dom;
		type = type_;
		listener = new ParameterListener(dom, parentElem, main, type_);
		initialize();
		setToolTipText();
	}

	public ParameterForm(Composite parent, int style, SchedulerDom _dom, Element parentElem, ISchedulerUpdate main, int type_, String jobname)
			throws JDOMException {
		super(parent, style);
		dom = _dom;
		type = type_;
		listener = new ParameterListener(dom, parentElem, main, type_);
		listener.setJobname(jobname);
		initialize();
		setToolTipText();
	}

	private void initialize() {
		sosString = new SOSString();
		try {
			isRemoteConnection = sosString.parseToString(MainWindow.getContainer().getCurrentTab().getData("ftp_title")).length() > 0;
		}
		catch (Exception e) {
		}
		// this.setLayout(new FillLayout());
		this.setLayout(new GridLayout());
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		gJobParameter = new Group(this, SWT.NONE);
		gJobParameter.setText("Parameter");
		gJobParameter.setLayout(gridLayout2);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gJobParameter.setLayoutData(gridData_1);
		createParameterGroup();
		// dom.setInit(true);
		getDescription();
		initForm();
		// dom.setInit(false);
	}

	public void apply() {
		if (isUnsaved())
			addParam();
	}

	public boolean isUnsaved() {
		return bApply.isEnabled();
		/*|| 
		       (butEnvApply != null && butEnvApply.isEnabled()) ||
		       (butIncludesApply != null && butIncludesApply.isEnabled()) ||
		       (butoIncludeSave != null && butoIncludeSave.isEnabled());
		       */
		// return false;
	}

	/**
	 * This method initializes group2
	 */
	public void createParameterGroup() {
		// initTabFolder();
		// tabFolder = new CTabFolder(gJobParameter, SWT.CLOSE | SWT.BORDER);
		// tabFolder = new CTabFolder(gJobParameter, SWT.CLOSE);
		// tabFolder = new CTabFolder(gJobParameter, SWT.CLOSE);
		tabFolder = new CTabFolder(gJobParameter, SWT.BORDER);
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData_2.heightHint = 203;
		gridData_2.widthHint = 760;
		tabFolder.setLayoutData(gridData_2);
		// Parameter
		if (type == Editor.JOB_COMMANDS)
			// if(type == Editor.JOB_COMMANDS)
			createJobCommandParameter();
		else
			createParameter();
		// Environment
		if (type == Editor.JOB || type == Editor.JOB_COMMANDS)
			createEnvironment();
		// Includes
		if (type != Editor.WEBSERVICE)
			createIncludes();
		// test
		// createParameterTabItem();
		tabFolder.setSelection(0);
		if (tParaName != null)
			tParaName.setFocus();
		// setToolTipText();
	}

	private void addParam() {
		if (!tParaName.getText().equals(""))
			listener.saveParameter(tParameter, tParaName.getText().trim(), tParaValue.getText());
		tParaName.setText("");
		tParaValue.setText("");
		bRemove.setEnabled(false);
		tParameter.deselectAll();
		tParaName.setFocus();
	}

	private void addEnvironment() {
		listener.saveEnvironment(tableEnvironment, txtEnvName.getText().trim(), txtEnvValue.getText());
		txtEnvName.setText("");
		txtEnvValue.setText("");
		butEnvRemove.setEnabled(false);
		butEnvApply.setEnabled(false);
		tableEnvironment.deselectAll();
		txtEnvName.setFocus();
	}

	private void addInclude() {
		listener.saveIncludeParams(tableIncludeParams, txtIncludeFilename.getText().trim(), txtIncludeNode.getText(), (type == Editor.JOB
				|| type == Editor.COMMANDS || type == Editor.JOB_COMMANDS && butIsLifeFile.getSelection() ? butIsLifeFile.getSelection() : false));
		txtIncludeFilename.setText("");
		txtIncludeNode.setText("");
		butIncludesApply.setEnabled(false);
		butRemoveInclude.setEnabled(false);
		butOpenInclude.setEnabled(false);
		tableIncludeParams.deselectAll();
		txtIncludeFilename.setFocus();
		if (type == Editor.JOB || type == Editor.COMMANDS || type == Editor.JOB_COMMANDS)
			butIsLifeFile.setSelection(false);
	}

	public void initForm() {
		tParameter.removeAll();
		if (includeFile != null && includeFile.trim().length() > 0) {
			if (new File(Options.getSchedulerData().endsWith("/") || Options.getSchedulerData().endsWith("\\") ? Options.getSchedulerData()
					: Options.getSchedulerData() + "/" + includeFile).exists())
				listener.getAllParameterDescription();
		}
		listener.fillParams(tParameter);
		listener.fillEnvironment(tableEnvironment);
		listener.fillIncludeParams(tableIncludeParams);
	}

	private void startWizzard() {
		Utils.startCursor(getShell());
		if (includeFile != null && includeFile.trim().length() > 0) {
			// JobDokumentation ist bekannt -> d.h Parameter aus dieser Jobdoku extrahieren
			// JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(listener.get_dom(), listener.get_main(), new
			// JobListener(dom, listener.getParent(), listener.get_main()), tParameter, onlyParams ? Editor.JOB : Editor.JOB_WIZARD);
			JobAssistentImportJobParamsForm paramsForm = new JobAssistentImportJobParamsForm(listener.get_dom(), listener.get_main(), new JobListener(dom,
					listener.getParent(), listener.get_main()), tParameter, Editor.PARAMETER);
			paramsForm.showAllImportJobParams(includeFile);
		}
		else {
			// Liste aller Jobdokumentation
			JobAssistentImportJobsForm importParameterForms = new JobAssistentImportJobsForm(new JobListener(dom, listener.getParent(), listener.get_main()),
					tParameter, Editor.PARAMETER);
			importParameterForms.showAllImportJobs();
		}
		Utils.stopCursor(getShell());
	}

	public Table getTParameter() {
		return tParameter;
	}

	private void createParameterTabItem() {
		Element params = null;
		final String node = txtIncludeNode.getText();
		try {
			String f = txtIncludeFilename.getText();
			boolean fNotExist = false;
			if (!new File(f).exists()) {
				String data = ".";
				if ((dom.isDirectory() || dom.isLifeElement()) && butIsLifeFile.getSelection()) {
					if (f.startsWith("/") || f.startsWith("\\")) {
						data = Options.getSchedulerHotFolder();
					}
					else
						if (dom.getFilename() != null) {
							if (dom.isLifeElement())
								data = new File(dom.getFilename()).getParent();
							else
								// iddirectory
								data = new File(dom.getFilename()).getPath();
						}
				}
				else {
					// normale Konfiguration
					if (butIsLifeFile != null && butIsLifeFile.getSelection())
						data = Options.getSchedulerHotFolder();
					else
						data = Options.getSchedulerData();
				}
				f = ((data.endsWith("/") || data.endsWith("\\") ? data : data + "/") + f);
				if (!new File(f).exists()) {
					fNotExist = true;
				}
			}
			if (fNotExist) {
				MainWindow.message("file not exist: " + f, SWT.ICON_WARNING);
				return;
			}
			final String filename = f;
			for (int i = 0; i < tabFolder.getItemCount(); i++) {
				if (sosString.parseToString(tabFolder.getItem(i).getData("filename")).equals(filename)
						&& sosString.parseToString(tabFolder.getItem(i).getData("node")).equals(node)) {
					tabFolder.setSelection(tabFolder.getItem(i));
					return;
				}
			}
			SAXBuilder builder = new SAXBuilder();
			final Document doc = builder.build(filename);
			java.util.List listOfElement = null;
			if (txtIncludeNode.getText() != null && txtIncludeNode.getText().length() > 0) {
				XPath x = XPath.newInstance(txtIncludeNode.getText());
				// Element e = (Element)x.selectSingleNode(doc);
				listOfElement = x.selectNodes(doc);
			}
			else {
				listOfElement = new java.util.ArrayList();
				params = doc.getRootElement();
				if (params != null)
					listOfElement = params.getChildren("param");
			}
			java.util.HashMap hash = new java.util.HashMap(); // hilfsvariable
			for (int i = 0; i < listOfElement.size(); i++) {
				// Parametername in unterschiedlichen XPaths darf nur einmal vorkommen
				// Element params_ = (Element)listOfElement.get(j);
				// java.util.List paramList = params_.getChildren("param");
				// for(int i = 0; i < paramList.size(); i++) {
				Element param = (Element) listOfElement.get(i);
				if (hash.containsKey(Utils.getAttributeValue("name", param))) {
					MainWindow.message("There is not a clearly Parameter: " + Utils.getAttributeValue("name", param), SWT.ICON_WARNING);
					return;
				}
				hash.put(Utils.getAttributeValue("name", param), "");
			}
			/*java.util.HashMap hash = new java.util.HashMap(); //hilfsvariable
			for(int j = 0; j < listOfElement.size(); j++) {
				//Parametername in unterschiedlichen XPaths darf nur einmal vorkommen
				Element params_ = (Element)listOfElement.get(j);
				java.util.List paramList = params_.getChildren("param");
				for(int i = 0; i < paramList.size(); i++) {
					Element param = (Element)paramList.get(i);					
					if(hash.containsKey(Utils.getAttributeValue("name", param))) {
						MainWindow.message("There is not a clearly Parameter: " + Utils.getAttributeValue("name", param), SWT.ICON_WARNING);
						return;
					}
					hash.put(Utils.getAttributeValue("name", param), "");

				}
			}*/
			includeParameterTabItem = new CTabItem(tabFolder, SWT.CLOSE);
			includeParameterTabItem.setText(new File(filename).getName());
			includeParameterTabItem.setData("filename", filename);
			includeParameterTabItem.setData("node", node);
			includeParameterTabItem.setData("doc", doc);
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
			butoIncludeSave = new Button(group_1, SWT.NONE);
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
			final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 4, 3);
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
			for (int i = 0; i < listOfElement.size(); i++) {
				Element param = (Element) listOfElement.get(i);
				TableItem item = new TableItem(tableIncludeParameter, SWT.NONE);
				item.setText(0, Utils.getAttributeValue("name", param));
				item.setText(1, Utils.getAttributeValue("value", param));
				item.setData("param", param);
			}
			final Button newButton = new Button(group_1, SWT.NONE);
			newButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
			newButton.setText("New");
			// fill Include Params From External File
			/*for(int j = 0; j < listOfElement.size(); j++) {
				Element params_ = (Element)listOfElement.get(j);
				java.util.List paramList = params_.getChildren("param");
				for(int i = 0; i < paramList.size(); i++) {
					Element param = (Element)paramList.get(i);
					TableItem item = new TableItem( tableIncludeParameter, SWT.NONE);
					item.setText(0, Utils.getAttributeValue("name", param));
					item.setText(1, Utils.getAttributeValue("value", param));
					item.setData("param", param);
				}
			}*/
			final Button butIncludeRemove = new Button(group_1, SWT.NONE);
			final GridData gridData_8 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
			butIncludeRemove.setLayoutData(gridData_8);
			butIncludeRemove.setText("Remove");
			butIncludeRemove.setEnabled(false);
			butIncludeRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					updateIncludeParam(includeParameterTabItem, false, tableIncludeParameter, txtIncludeParameter, txtIncludeParameterValue, butIncludeRemove);
				}
			});
			if (type == Editor.JOB) {
				butImport = new Button(group_1, SWT.NONE);
				butImport.setVisible(false);
				butImport.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
				// butImport.setText("import");
				butImport.setText(WIZARD);
				butImport.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						JobAssistentImportJobsForm importParameterForms = new JobAssistentImportJobsForm(new JobListener(dom, listener.getParent(),
								listener.get_main()), tableIncludeParameter, Editor.JOB);
						importParameterForms.showAllImportJobs();
					}
				});
				// butImport.setText("Import");
				butImport.setText(WIZARD);
			}
			txtIncludeParameterValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
				public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
					if (e.keyCode == SWT.CR && !txtIncludeParameter.getText().trim().equals("")) {
						updateIncludeParam(includeParameterTabItem, true, tableIncludeParameter, txtIncludeParameter, txtIncludeParameterValue,
								butIncludeRemove);
					}
				}
			});
			txtIncludeParameter.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
				public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
					if (e.keyCode == SWT.CR && !txtIncludeParameter.equals("")) {
						updateIncludeParam(includeParameterTabItem, true, tableIncludeParameter, txtIncludeParameter, txtIncludeParameterValue,
								butIncludeRemove);
					}
				}
			});
			butoIncludeSave.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
					updateIncludeParam(includeParameterTabItem, true, tableIncludeParameter, txtIncludeParameter, txtIncludeParameterValue, butIncludeRemove);
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
					butoIncludeSave.setEnabled(false);
				}
			});
			newButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					txtIncludeParameter.setText("");
					txtIncludeParameterValue.setText("");
					butIncludeRemove.setEnabled(false);
					tableIncludeParameter.deselectAll();
					txtIncludeParameter.setFocus();
				}
			});
			// Speichern und löschen ist nicht im Xpath Ausdruck erlaubt. Grund: Parameter könne aus verschiedenen Paths geholt werden.
			boolean hasXPathExpression = txtIncludeNode.getText().trim().length() == 0;
			butoIncludeSave.setVisible(hasXPathExpression);
			butIncludeRemove.setVisible(hasXPathExpression);
			txtIncludeParameter.setEnabled(hasXPathExpression);
			txtIncludeParameterValue.setEnabled(hasXPathExpression);
			newButton.setEnabled(hasXPathExpression);
			tabFolder.setSelection(includeParameterTabItem);
		}
		catch (Exception e) {
			try {
				new sos.scheduler.editor.app.ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
			}
			catch (Exception ee) {
				// tu nichts
			}
			MainWindow.message("could not create Tabitem cause: " + e.getMessage(), SWT.ICON_WARNING);
		}
	}

	private void updateIncludeParam(CTabItem includeParameterTabItem, boolean add, Table tableIncludeParameter, Text txtIncludeParameter,
			Text txtIncludeParameterValue, Button butIncludeRemove) {
		Document doc = (Document) includeParameterTabItem.getData("doc");
		String filename = (String) includeParameterTabItem.getData("filename");
		java.util.List listOfElement = (java.util.List) includeParameterTabItem.getData("params");
		if (add) {
			// neue Parameter bzw. editieren vorhandene Parameter
			boolean found = false;
			for (int i = 0; i < tableIncludeParameter.getItemCount(); i++) {
				TableItem item = tableIncludeParameter.getItem(i);
				if (item.getText(0).equals(txtIncludeParameter.getText())) {
					found = true;
					item.setText(0, txtIncludeParameter.getText());
					item.setText(1, txtIncludeParameterValue.getText());
					Element param = (Element) item.getData("param");
					param.setAttribute("name", item.getText(0));
					param.setAttribute("value", item.getText(1));
				}
			}
			if (!found) {
				// if(txtIncludeNode.getText().length() == 0) {
				// if(listOfElement.size() > 0 && txtIncludeNode.getText().length() == 0) {
				TableItem item = new TableItem(tableIncludeParameter, SWT.NONE);
				item.setText(0, txtIncludeParameter.getText());
				item.setText(1, txtIncludeParameterValue.getText());
				/*Element params = null;
				if(listOfElement.size() > 0)
					params = ((Element)listOfElement.get(0)).getParentElement();
				*/
				Element param = new Element("param");
				param.setAttribute("name", item.getText(0));
				param.setAttribute("value", item.getText(1));
				// params.addContent(param);
				item.setData("param", param);
				listOfElement.add(param);
				includeParameterTabItem.setData("params", listOfElement);
				// } else {
				// MainWindow.message("could not save cause note path ist not clearly", SWT.ICON_WARNING);
				// }
			}
		}
		else {
			// parameter löschen
			if (tableIncludeParameter.getSelectionCount() > 0) {
				Element param = (Element) tableIncludeParameter.getSelection()[0].getData("param");
				Element params = ((Element) listOfElement.get(0)).getParentElement();
				params.removeContent(param);
				listOfElement = params.getChildren("param");
				tableIncludeParameter.remove(tableIncludeParameter.getSelectionIndex());
				includeParameterTabItem.setData("params", listOfElement);
			}
		}
		IOUtils.saveXML(doc, filename);
		txtIncludeParameter.setText("");
		txtIncludeParameterValue.setText("");
		butIncludeRemove.setEnabled(false);
		tableIncludeParameter.deselectAll();
		txtIncludeParameter.setFocus();
	}

	private void createParameter() {
		// Parameter
		parameterTabItem = new CTabItem(tabFolder, SWT.BORDER);
		parameterTabItem.setText("Parameter");
		final Group Group = new Group(tabFolder, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		Group.setLayout(gridLayout);
		parameterTabItem.setControl(Group);
		label2 = new Label(Group, SWT.NONE);
		label2.setText("Name: ");
		tParaName = new Text(Group, SWT.BORDER);
		tParaName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tParaName.selectAll();
			}
		});
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
		label6 = new Label(Group, SWT.NONE);
		label6.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		label6.setText("Value: ");
		tParaValue = new Text(Group, SWT.BORDER);
		tParaValue.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tParaValue.selectAll();
			}
		});
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
		final Button button = new Button(Group, SWT.NONE);
		final GridData gridDatax = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		gridDatax.widthHint = 28;
		button.setLayoutData(gridDatax);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(tParaValue.getText(), getShell(), true, "");
				if (text != null)
					tParaValue.setText(text);
			}
		});
		button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
		bApply = new Button(Group, SWT.NONE);
		bApply.setEnabled(false);
		final GridData gridData_7 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridData_7.widthHint = 36;
		bApply.setLayoutData(gridData_7);
		bApply.setText("&Apply");
		bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				addParam();
			}
		});
		label4 = new Label(Group, SWT.SEPARATOR | SWT.HORIZONTAL);
		label4.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 6, 1));
		label4.setText("Label");
		tParameter = new Table(Group, SWT.FULL_SELECTION | SWT.BORDER);
		tParameter.setLinesVisible(true);
		final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 4);
		tParameter.setLayoutData(gridData_1);
		tParameter.setHeaderVisible(true);
		tParameter.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (bApply.isEnabled()) {
					int ok = MainWindow.message(Messages.getString("MainListener.apply_changes"), //$NON-NLS-1$
							SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
					if (ok == SWT.YES) {
						addParam();
						return;
					}
				}
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				tParaName.setText(item.getText(0));
				tParaValue.setText(item.getText(1));
				bRemove.setEnabled(tParameter.getSelectionCount() > 0);
				if (type == Editor.JOB) {
					// txtParameterDescription.setText(listener.getParameterDescription(item.getText(0)));
					try {
						txtParameterDescription.setText(sosString.parseToString(item.getData("parameter_description_" + Options.getLanguage())));
					}
					catch (Exception ew) {
					}
				}
				bApply.setEnabled(false);
			}
		});
		TableColumn tcName = new TableColumn(tParameter, SWT.NONE);
		tcName.setWidth(262);
		tcName.setText("Name");
		TableColumn tcValue = new TableColumn(tParameter, SWT.NONE);
		tcValue.setWidth(500);
		tcValue.setText("Value");
		butNewParam = new Button(Group, SWT.NONE);
		butNewParam.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tParaName.setText("");
				tParaValue.setText("");
				bRemove.setEnabled(false);
				tParameter.deselectAll();
				tParaName.setFocus();
			}
		});
		butNewParam.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butNewParam.setText("New");
		final Composite composite = new Composite(Group, SWT.NONE);
		final GridData gridData_2 = new GridData(GridData.CENTER, GridData.CENTER, false, false);
		gridData_2.heightHint = 67;
		composite.setLayoutData(gridData_2);
		composite.setLayout(new GridLayout());
		butUp = new Button(composite, SWT.NONE);
		butUp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				// selektierte Datensatz wird eine Zeile nach oben verschoben
				listener.changeUp(tParameter);
			}
		});
		butUp.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		// butUp.setText("Up");
		butUp.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_up.gif"));
		butDown = new Button(composite, SWT.NONE);
		butDown.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.changeDown(tParameter);
			}
		});
		butDown.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		// butDown.setText("Down");
		butDown.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_down.gif"));
		butImport = new Button(Group, SWT.NONE);
		butImport.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		// butImport.setText("import");
		butImport.setText(WIZARD);
		butImport.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				startWizzard();
				tParaName.setFocus();
			}
		});
		// butImport.setText("Import");
		butImport.setText(WIZARD);
		bRemove = new Button(Group, SWT.NONE);
		final GridData gridData_8 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
		bRemove.setLayoutData(gridData_8);
		bRemove.setText("Remove");
		bRemove.setEnabled(false);
		bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				int index = tParameter.getSelectionIndex();
				listener.deleteParameter(tParameter, index);
				tParaName.setText("");
				tParaValue.setText("");
				tParameter.deselectAll();
				bRemove.setEnabled(false);
				bApply.setEnabled(false);
				if (index >= tParameter.getItemCount())
					index--;
				if (index >= 0) {
					tParameter.select(index);
					tParameter.setSelection(index);
					setParams(tParameter.getItem(index));
				}
			}
		});
		if (type == Editor.JOB) {
			txtParameterDescription = new Text(Group, SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
			final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true, 5, 1);
			gridData.minimumHeight = 100;
			txtParameterDescription.setLayoutData(gridData);
			txtParameterDescription.addFocusListener(new FocusAdapter() {
				public void focusGained(final FocusEvent e) {
					tParaName.setFocus();
				}
			});
			txtParameterDescription.setEditable(false);
			txtParameterDescription.setBackground(SWTResourceManager.getColor(247, 247, 247));
			new Label(Group, SWT.NONE);
			tParaName.setFocus();
		}
	}

	private void createEnvironment() {
		environmentTabItem = new CTabItem(tabFolder, SWT.BORDER);
		environmentTabItem.setText("Environment");
		final Group group_2 = new Group(tabFolder, SWT.NONE);
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 5;
		group_2.setLayout(gridLayout_1);
		environmentTabItem.setControl(group_2);
		final Label nameLabel = new Label(group_2, SWT.NONE);
		nameLabel.setText("Name: ");
		txtEnvName = new Text(group_2, SWT.BORDER);
		txtEnvName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtEnvName.selectAll();
			}
		});
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
		txtEnvValue.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtEnvValue.selectAll();
			}
		});
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
		butEnvApply.setEnabled(false);
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
				setEnvironment(item);
				/*txtEnvName.setText(item.getText(0));
				txtEnvValue.setText(item.getText(1));
				butEnvRemove.setEnabled(tableEnvironment.getSelectionCount() > 0);                                
				butEnvApply.setEnabled(false);
				*/
			}
		});
		tableEnvironment.setLinesVisible(true);
		tableEnvironment.setHeaderVisible(true);
		tableEnvironment.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 2));
		final TableColumn colEnvName = new TableColumn(tableEnvironment, SWT.NONE);
		colEnvName.setWidth(250);
		colEnvName.setText("Name");
		final TableColumn colEnvValue = new TableColumn(tableEnvironment, SWT.NONE);
		colEnvValue.setWidth(522);
		colEnvValue.setText("Value");
		butNewEnvironment = new Button(group_2, SWT.NONE);
		butNewEnvironment.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				txtEnvName.setText("");
				txtEnvValue.setText("");
				butEnvRemove.setEnabled(false);
				tableEnvironment.deselectAll();
				txtEnvName.setFocus();
			}
		});
		butNewEnvironment.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butNewEnvironment.setText("New");
		butEnvRemove = new Button(group_2, SWT.NONE);
		butEnvRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				int index = tableEnvironment.getSelectionIndex();
				listener.deleteEnvironment(tableEnvironment, index);
				txtEnvName.setText("");
				txtEnvValue.setText("");
				tableEnvironment.deselectAll();
				butEnvApply.setEnabled(false);
				butEnvRemove.setEnabled(false);
				txtEnvName.setFocus();
				if (index >= tableEnvironment.getItemCount())
					index--;
				if (index >= 0) {
					tableEnvironment.setSelection(index);
					tableEnvironment.select(index);
					setEnvironment(tableEnvironment.getItem(index));
				}
			}
		});
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		butEnvRemove.setLayoutData(gridData_3);
		butEnvRemove.setText("Remove");
		txtEnvName.setFocus();
	}

	private void createIncludes() {
		final CTabItem includesTabItem = new CTabItem(tabFolder, SWT.BORDER);
		includesTabItem.setText("Includes");
		final Group group_3 = new Group(tabFolder, SWT.NONE);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 5;
		group_3.setLayout(gridLayout_2);
		includesTabItem.setControl(group_3);
		if (type == Editor.JOB || type == Editor.COMMANDS || type == Editor.JOB_COMMANDS) {
			butIsLifeFile = new Button(group_3, SWT.CHECK);
			butIsLifeFile.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					butIncludesApply.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
				}
			});
			butIsLifeFile.setText("from Hot Folder");
		}
		else {
			final Label lblNode_ = new Label(group_3, SWT.NONE);
			lblNode_.setText("File:");
		}
		txtIncludeFilename = new Text(group_3, SWT.BORDER);
		txtIncludeFilename.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtIncludeFilename.selectAll();
			}
		});
		txtIncludeFilename.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butIncludesApply.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
				if (type == Editor.JOB || type == Editor.COMMANDS || type == Editor.JOB_COMMANDS)
					butIsLifeFile.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
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
		txtIncludeNode.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				txtIncludeNode.selectAll();
			}
		});
		txtIncludeNode.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				butIncludesApply.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
				if (type == Editor.JOB || type == Editor.COMMANDS || type == Editor.JOB_COMMANDS)
					butIsLifeFile.setEnabled(!txtIncludeFilename.getText().trim().equals(""));
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
				if (!isRemoteConnection)
					createParameterTabItem();
			}
		});
		tableIncludeParams.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				setInclude(item);
				/*
				txtIncludeFilename.setText(item.getText(0));
				txtIncludeNode.setText(item.getText(1));
				if(type == Editor.JOB || type == Editor.COMMANDS || type == Editor.JOB_COMMANDS) 
					butIsLifeFile.setSelection(item.getText(2).equalsIgnoreCase("live_file"));								
				butRemoveInclude.setEnabled(tableIncludeParams.getSelectionCount() > 0);                               
				butIncludesApply.setEnabled(false);
				
				butOpenInclude.setEnabled(true && !isRemoteConnection);				
				*/
			}
		});
		tableIncludeParams.setLinesVisible(true);
		tableIncludeParams.setHeaderVisible(true);
		tableIncludeParams.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 3));
		final TableColumn colParamColums = new TableColumn(tableIncludeParams, SWT.NONE);
		colParamColums.setWidth(250);
		colParamColums.setText("File");
		final TableColumn newColumnTableColumn_1 = new TableColumn(tableIncludeParams, SWT.NONE);
		newColumnTableColumn_1.setWidth(400);
		newColumnTableColumn_1.setText("Node");
		final TableColumn newColumnTableColumn = new TableColumn(tableIncludeParams, SWT.NONE);
		newColumnTableColumn.setWidth(100);
		newColumnTableColumn.setText("File/Live_File");
		if (type != Editor.JOB && type != Editor.COMMANDS && type != Editor.JOB_COMMANDS) {
			newColumnTableColumn.setWidth(200);
			newColumnTableColumn.setResizable(false);
		}
		butNewIncludes = new Button(group_3, SWT.NONE);
		butNewIncludes.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tableIncludeParams.deselectAll();
				txtIncludeFilename.setText("");
				txtIncludeNode.setText("");
				if (type == Editor.JOB || type == Editor.COMMANDS || type == Editor.JOB_COMMANDS)
					butIsLifeFile.setSelection(false);
				butIncludesApply.setEnabled(false);
				butOpenInclude.setEnabled(false);
				butRemoveInclude.setEnabled(false);
				txtIncludeFilename.setFocus();
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
				int index = tableIncludeParams.getSelectionIndex();
				listener.deleteIncludeParams(tableIncludeParams, tableIncludeParams.getSelectionIndex());
				txtIncludeFilename.setText("");
				txtIncludeNode.setText("");
				tableIncludeParams.deselectAll();
				butIncludesApply.setEnabled(false);
				butRemoveInclude.setEnabled(false);
				txtIncludeFilename.setFocus();
				if (index >= tableIncludeParams.getItemCount())
					index--;
				if (index >= 0) {
					tableIncludeParams.setSelection(index);
					setInclude(tableIncludeParams.getItem(index));
				}
			}
		});
		butRemoveInclude.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		butRemoveInclude.setText("Remove");
		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void close(final CTabFolderEvent e) {
				if (e.item.equals(parameterTabItem) || e.item.equals(environmentTabItem) || e.item.equals(includesTabItem)) {
					e.doit = false;
				}
			}
		});
		tabFolder.setSelection(0);
		txtIncludeFilename.setFocus();
	}

	public void createJobCommandParameter() {
		// parameterJobCmdTabItem = new CTabItem(tabFolder, SWT.BORDER | SWT.CLOSE);
		parameterJobCmdTabItem = new CTabItem(tabFolder, SWT.BORDER);
		parameterJobCmdTabItem.setText("Parameter");
		group = new Group(tabFolder, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		group.setLayout(gridLayout);
		parameterJobCmdTabItem.setControl(group);
		label2 = new Label(group, SWT.NONE);
		label2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		label2.setText("Name: ");
		tParaName = new Text(group, SWT.BORDER);
		tParaName.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tParaName.selectAll();
			}
		});
		final GridData gridData_9 = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData_9.widthHint = 200;
		tParaName.setLayoutData(gridData_9);
		tParaName.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR && !tParaName.equals(""))
					addParam();
			}
		});
		tParaName.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(!tParaName.getText().equals(""));
				if (tParaName.getText().equals("<from>")) {
					cSource.setVisible(true);
					tParaValue.setVisible(false);
				}
				else {
					cSource.setVisible(false);
					tParaValue.setVisible(true);
				}
			}
		});
		label6 = new Label(group, SWT.NONE);
		label6.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		label6.setText("Value: ");
		final Composite composite = new Composite(group, SWT.NONE);
		composite.addControlListener(new ControlAdapter() {
			public void controlResized(final ControlEvent e) {
				cSource.setBounds(0, 2, composite.getBounds().width, tParaName.getBounds().height);
				tParaValue.setBounds(0, 2, composite.getBounds().width, tParaName.getBounds().height);
			}
		});
		composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		cSource = new Combo(composite, SWT.READ_ONLY);
		cSource.setItems(new String[] { "order", "task" });
		cSource.setBounds(0, 0, 250, 21);
		cSource.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				tParaValue.setText(cSource.getText());
			}
		});
		cSource.setVisible(false);
		tParaValue = new Text(composite, SWT.BORDER);
		tParaValue.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				tParaValue.selectAll();
			}
		});
		tParaValue.setBounds(0, 0, 250, 21);
		tParaValue.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR && !tParaName.equals(""))
					addParam();
			}
		});
		tParaValue.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				bApply.setEnabled(!tParaName.getText().equals(""));
			}
		});
		final Button button = new Button(group, SWT.NONE);
		final GridData gridDatax = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false);
		gridDatax.widthHint = 28;
		button.setLayoutData(gridDatax);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String text = sos.scheduler.editor.app.Utils.showClipboard(tParaValue.getText(), getShell(), true, "");
				if (text != null)
					tParaValue.setText(text);
			}
		});
		button.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_edit.gif"));
		bApply = new Button(group, SWT.NONE);
		final GridData gridData_5 = new GridData(GridData.FILL, GridData.CENTER, false, false);
		bApply.setLayoutData(gridData_5);
		bApply.setText("&Apply");
		bApply.setEnabled(false);
		bApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				addParam();
			}
		});
		tParameter = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, false, true, 5, 5);
		gridData_3.widthHint = 342;
		gridData_3.heightHint = 140;
		tParameter.setLayoutData(gridData_3);
		tParameter.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent e) {
			}
		});
		tParameter.setHeaderVisible(true);
		tParameter.setLinesVisible(true);
		tParameter.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				setParams(item);
/*				tParaName.setText(item.getText(0));
				if (tParaName.getText().equals("<from>"))
					cSource.setText(item.getText(1));
				tParaValue.setText(item.getText(1));								

				bRemove.setEnabled(tParameter.getSelectionCount() > 0);
				bApply.setEnabled(false);
				tParaName.setFocus();
				*/
			}
		});
		TableColumn tcName = new TableColumn(tParameter, SWT.NONE);
		tcName.setWidth(252);
		tcName.setText("Name");
		TableColumn tcValue = new TableColumn(tParameter, SWT.NONE);
		tcValue.setWidth(500);
		tcValue.setText("Value");
		butNewParam = new Button(group, SWT.NONE);
		butNewParam.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tParaName.setText("");
				tParaValue.setText("");
				bRemove.setEnabled(false);
				tParameter.deselectAll();
				tParaName.setFocus();
			}
		});
		butNewParam.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		butNewParam.setText("New");
		final Composite composite_2 = new Composite(group, SWT.NONE);
		final GridData gridData_2_1 = new GridData(GridData.CENTER, GridData.CENTER, false, false);
		gridData_2_1.heightHint = 67;
		composite_2.setLayoutData(gridData_2_1);
		composite_2.setLayout(new GridLayout());
		butUp_1 = new Button(composite_2, SWT.NONE);
		butUp_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.changeUp(tParameter);
			}
		});
		butUp_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		// butUp_1.setText("Up");
		butUp_1.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_up.gif"));
		butDown_1 = new Button(composite_2, SWT.NONE);
		butDown_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				listener.changeDown(tParameter);
			}
		});
		butDown_1.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, false, false));
		butDown_1.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/icon_down.gif"));
		butImport_1 = new Button(group, SWT.NONE);
		butImport_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				startWizzard();
			}
		});
		butImport_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		// butImport_1.setText("Import");
		butImport_1.setText(WIZARD);
		bRemove = new Button(group, SWT.NONE);
		bRemove.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		bRemove.setText("Remove");
		bRemove.setEnabled(false);
		bRemove.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				int index = tParameter.getSelectionIndex();
				listener.deleteParameter(tParameter, index);
				tParaName.setText("");
				tParaValue.setText("");
				tParameter.deselectAll();
				bRemove.setEnabled(false);
				bApply.setEnabled(false);
				tParaName.setFocus();
				if (index >= tParameter.getItemCount())
					index--;
				if (index >= 0) {
					tParameter.setSelection(index);
					tParameter.select(index);
					setParams(tParameter.getItem(index));
				}
			}
		});
		final Composite composite_1 = new Composite(group, SWT.NONE);
		final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, true);
		gridData.widthHint = 87;
		composite_1.setLayoutData(gridData);
		composite_1.setLayout(new GridLayout());
		final Button paramButton = new Button(composite_1, SWT.RADIO);
		paramButton.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
		paramButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tParaName.setText("");
				tParaValue.setText("");
				tParaName.setFocus();
			}
		});
		paramButton.setSelection(true);
		paramButton.setText("Parameter");
		final Button fromTaskButton = new Button(composite_1, SWT.RADIO);
		fromTaskButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
		fromTaskButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tParaName.setText("<from>");
				cSource.setText("task");
				bApply.setFocus();
			}
		});
		fromTaskButton.setText("from task");
		final Button fromOrderButton = new Button(composite_1, SWT.RADIO);
		final GridData gridData_2 = new GridData(GridData.FILL, GridData.BEGINNING, false, true);
		fromOrderButton.setLayoutData(gridData_2);
		fromOrderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tParaName.setText("<from>");
				cSource.setText("order");
				bApply.setFocus();
			}
		});
		fromOrderButton.setText("from order");
	}

	private void getDescription() {
		Element desc = listener.getParent().getChild("description");
		if (desc != null) {
			Element include = desc.getChild("include");
			includeFile = Utils.getAttributeValue("file", include);
		}
	}

	public void setToolTipText() {
		tParaName.setToolTipText(Messages.getTooltip("job.param.name"));
		tParaValue.setToolTipText(Messages.getTooltip("job.param.value"));
		bRemove.setToolTipText(Messages.getTooltip("job.param.btn_remove"));
		bApply.setToolTipText(Messages.getTooltip("job.param.btn_add"));
		tParameter.setToolTipText(Messages.getTooltip("job.param.table"));
		butNewParam.setToolTipText(Messages.getTooltip("job.param.new"));
		if (butDown != null)
			butDown.setToolTipText(Messages.getTooltip("button_down"));
		if (butUp != null)
			butUp.setToolTipText(Messages.getTooltip("button_up"));
		if (txtParameterDescription != null) {
			txtParameterDescription.setToolTipText(Messages.getTooltip("job.param.description"));
		}
		if (txtEnvName != null) {
			tableEnvironment.setToolTipText(Messages.getTooltip("job.environment.table"));
			txtEnvName.setToolTipText(Messages.getTooltip("job.environment.name"));
			txtEnvValue.setToolTipText(Messages.getTooltip("job.environment.value"));
			butEnvApply.setToolTipText(Messages.getTooltip("job.environment.btn_apply"));
			butEnvRemove.setToolTipText(Messages.getTooltip("job.environment.btn_remove"));
			butNewEnvironment.setToolTipText(Messages.getTooltip("job.environment.new"));
		}
		if (txtIncludeFilename != null) {
			txtIncludeFilename.setToolTipText(Messages.getTooltip("parameter.includefile.name"));
			txtIncludeNode.setToolTipText(Messages.getTooltip("parameter.includenode.name"));
			tableIncludeParams.setToolTipText(Messages.getTooltip("parameter.include.table.name"));
			butIncludesApply.setToolTipText(Messages.getTooltip("parameter.include.but_apply.name"));
			butRemoveInclude.setToolTipText(Messages.getTooltip("parameter.include.but_remove.name"));
			butOpenInclude.setToolTipText(Messages.getTooltip("parameter.includetable_open.name"));
			butNewIncludes.setToolTipText(Messages.getTooltip("parameter.includetable_new.name"));
			if (type == Editor.JOB || type == Editor.COMMANDS || type == Editor.JOB_COMMANDS)
				butIsLifeFile.setToolTipText(Messages.getTooltip("is_live_file"));
		}
	}

	private void setParams(TableItem item) {
		tParaName.setText(item.getText(0));
		if (tParaName.getText().equals("<from>"))
			cSource.setText(item.getText(1));
		tParaValue.setText(item.getText(1));
		bRemove.setEnabled(tParameter.getSelectionCount() > 0);
		bApply.setEnabled(false);
		tParaName.setFocus();
	}

	private void setEnvironment(TableItem item) {
		txtEnvName.setText(item.getText(0));
		txtEnvValue.setText(item.getText(1));
		butEnvRemove.setEnabled(tableEnvironment.getSelectionCount() > 0);
		butEnvApply.setEnabled(false);
	}

	private void setInclude(TableItem item) {
		txtIncludeFilename.setText(item.getText(0));
		txtIncludeNode.setText(item.getText(1));
		if (type == Editor.JOB || type == Editor.COMMANDS || type == Editor.JOB_COMMANDS)
			butIsLifeFile.setSelection(item.getText(2).equalsIgnoreCase("live_file"));
		butRemoveInclude.setEnabled(tableIncludeParams.getSelectionCount() > 0);
		butIncludesApply.setEnabled(false);
		butOpenInclude.setEnabled(true && !isRemoteConnection);
	}
} // @jve:decl-index=0:visual-constraint="10,10"
