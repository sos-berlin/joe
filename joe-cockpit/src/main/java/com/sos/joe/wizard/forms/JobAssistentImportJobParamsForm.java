package com.sos.joe.wizard.forms;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.jdom.Element;
import org.w3c.dom.Document;

import sos.scheduler.editor.app.Utils;
import sos.scheduler.editor.conf.forms.DetailForm;
import sos.scheduler.editor.conf.forms.JobDocumentationForm;
import sos.scheduler.editor.conf.forms.ScriptJobMainForm;
import sos.scheduler.editor.conf.listeners.JobListener;
import sos.scheduler.editor.conf.listeners.JobsListener;
import sos.scheduler.editor.conf.listeners.ParameterListener;

import com.sos.JSHelper.io.Files.JSXMLFile;
import com.sos.dialog.swtdesigner.SWTResourceManager;
import com.sos.i18n.annotation.I18NResourceBundle;
import com.sos.joe.globals.JOEConstants;
import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.SOSJOEMessageCodes;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.joe.globals.options.Options;
import com.sos.joe.xml.jobscheduler.SchedulerDom;
import com.sos.scheduler.model.xmldoc.Description;
import com.sos.scheduler.model.xmldoc.Description.Configuration.Params;
import com.sos.scheduler.model.xmldoc.Note;
import com.sos.scheduler.model.xmldoc.Param;

@I18NResourceBundle(baseName = "JOEMessages", defaultLocale = "en")
public class JobAssistentImportJobParamsForm {

    private static final Logger LOGGER = Logger.getLogger(JobAssistentImportJobParamsForm.class);
    private static final String KEY_PARAMETER_DESCRIPTION_ = "parameter_description_";
    private static final String KEY_PARAMETER_DESCRIPTION_EN = "parameter_description_en";
    private static final String KEY_PARAMETER_DESCRIPTION_DE = "parameter_description_de";
    private static final String FALSE = "false";
    private static final String PARAM_ATTRIBUTE_DEFAULT_VALUE = "default_value";
    private static final String PARAM_ATTRIBUTE_REQUIRED = "required";
    private static final String PARAM_NAME = "name";
    private static final String TRUE = "true";
    private static JAXBContext context = null;
    private static Unmarshaller unmarshaller = null;
    private Text txtDescription = null;
    private Table tblSelectedParams = null;
    private Table tableDescParameters = null;
    private Shell jobParameterShell = null;
    private Text txtValue = null;
    private String xmlPaths = null;
    private Text txtName = null;
    private JobListener joblistener = null;
    private Table tParameter = null;
    private Button butFinish = null;
    private Button butApply = null;
    private Button butNext = null;
    private Button butBack = null;
    private ArrayList<HashMap<String, Object>> listOfParams = new ArrayList<HashMap<String, Object>>();
    private SchedulerDom dom = null;
    private ISchedulerUpdate update = null;
    private Button butCancel = null;
    private Button showButton = null;
    private int assistentType = -1;
    private Button butPut = null;
    private Button butPutAll = null;
    private Button butRemove = null;
    private Button butRemoveAll = null;
    private Combo jobname = null;
    private Element jobBackUp = null;
    private ScriptJobMainForm jobForm = null;
    private JobDocumentationForm jobDocForm = null;
    private boolean closeDialog = false;
    private sos.scheduler.editor.conf.listeners.ParameterListener paramListener = null;
    private Text refreshDetailsText = null;
    private DetailForm detailForm;
    public static final String conSystemPropertyUSER_DIR = "user.dir";

    public JobAssistentImportJobParamsForm() {
    }

    public JobAssistentImportJobParamsForm(final SchedulerDom dom_, final ISchedulerUpdate update_, final Element job_, final int assistentType_) {
        dom = dom_;
        update = update_;
        assistentType = assistentType_;
        joblistener = new JobListener(dom, job_, update);
        paramListener = new ParameterListener(dom, job_, update_, assistentType);
    }

    public JobAssistentImportJobParamsForm(final SchedulerDom dom_, final ISchedulerUpdate update_, final JobListener joblistener_, final int assistentType_) {
        dom = dom_;
        update = update_;
        joblistener = joblistener_;
        jobBackUp = (Element) joblistener.getJob().clone();
        assistentType = assistentType_;
        paramListener = new ParameterListener(dom, joblistener_.getJob(), update_, assistentType);
    }

    public JobAssistentImportJobParamsForm(final SchedulerDom dom_, final ISchedulerUpdate update_, final JobListener joblistener_, final Table tParameter_,
            final int assistentType_) {
        dom = dom_;
        update = update_;
        joblistener = joblistener_;
        jobBackUp = (Element) joblistener.getJob().clone();
        tParameter = tParameter_;
        assistentType = assistentType_;
        paramListener = new ParameterListener(dom, joblistener_.getJob(), update_, assistentType);
    }

    public ArrayList<HashMap<String, Object>> parseDocuments(String xmlFilename, final String type) {
        if (xmlFilename == null || xmlFilename.trim().isEmpty()) {
            return new ArrayList<HashMap<String, Object>>();
        }
        xmlPaths = Options.getSchedulerData() + "/jobs";
        xmlPaths = xmlPaths.replaceAll("\\\\", "/");
        xmlFilename = xmlFilename.replaceAll("\\\\", "/");
        if (!xmlFilename.startsWith(xmlPaths)) {
            String s[] = xmlFilename.split("/");
            xmlFilename = s[s.length - 1];
            xmlFilename = xmlPaths.endsWith("/") ? xmlPaths.concat(xmlFilename) : xmlPaths.concat("/").concat(xmlFilename);
        }
        ArrayList<HashMap<String, Object>> listOfParams1 = null;
        try {
            listOfParams1 = new ArrayList<HashMap<String, Object>>();
            if (!new File(xmlFilename).exists()) {
                ErrorLog.message(jobParameterShell, SOSJOEMessageCodes.JOE_M_0025.params(xmlFilename), SWT.OK);
                return listOfParams1;
            }
            Params objParams = getParamList(xmlFilename);
            if (objParams == null) {
                LOGGER.debug(SOSJOEMessageCodes.JOE_M_0026.label());
                return listOfParams1;
            }
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            HashMap<String, Object> h = null;
            for (int i = 0; i < objParams.getParam().size(); i++) {
                Param objParam = objParams.getParam().get(i);
                h = new HashMap<String, Object>();
                h.put(PARAM_NAME, objParam.getName());
                h.put(PARAM_ATTRIBUTE_DEFAULT_VALUE, objParam.getDefaultValue());
                String strIsRequired = FALSE;
                if (objParam.isRequired()) {
                    strIsRequired = TRUE;
                }
                h.put(PARAM_ATTRIBUTE_REQUIRED, strIsRequired);
                String strLanguage = Options.getLanguage();
                DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl", null);
                DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                doc.setXmlStandalone(true);
                doc.setXmlVersion("1.0");
                org.w3c.dom.Element root = doc.createElement("param");
                doc.appendChild(root);
                for (int j = 0; j < objParam.getNote().size(); j++) {
                    Note objNote = objParam.getNote().get(j);
                    int intSize = objNote.getContent().size();
                    for (int k = 0; k < intSize; k++) {
                        if (objNote.getLanguage().equalsIgnoreCase(strLanguage)) {
                            Object objO = objNote.getContent().get(k);
                            if (objO instanceof org.apache.xerces.dom.ElementNSImpl) {
                                org.apache.xerces.dom.ElementNSImpl objElem = (org.apache.xerces.dom.ElementNSImpl) objO;
                                boolean flgRecursivelyImportTheSubtree = true;
                                root.appendChild(doc.importNode(objElem, flgRecursivelyImportTheSubtree));
                            }
                        }
                    }
                    DOMSource source = new DOMSource(root);
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    StreamResult result = new StreamResult(bout);
                    transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
                    transformer.transform(source, result);
                    String strTempXML = bout.toString();
                    LOGGER.trace(strTempXML);
                    h.put(KEY_PARAMETER_DESCRIPTION_ + strLanguage, strTempXML);
                }
                listOfParams1.add(h);
            }
        } catch (Exception ex) {
            ErrorLog.message(jobParameterShell, SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()) + " " + ex.getLocalizedMessage(), SWT.OK);
            new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), ex);
        }
        return listOfParams1;
    }

    private Params getParamList(final String pstrDocuFileName) throws Exception {
        String strUserDir = System.getProperty(conSystemPropertyUSER_DIR);
        Params objParams = null;
        try {
            JSXMLFile fleFile = new JSXMLFile(pstrDocuFileName);
            System.setProperty(conSystemPropertyUSER_DIR, fleFile.getParent());
            context = JAXBContext.newInstance(Description.class);
            unmarshaller = context.createUnmarshaller();
            File objTempFile = File.createTempFile("sosxmldoc", ".xml");
            objTempFile.deleteOnExit();
            String strTempFileName = objTempFile.getAbsolutePath();
            fleFile.writeDocument(strTempFileName);
            Description objDescr = (Description) unmarshaller.unmarshal(new File(pstrDocuFileName));
            objParams = objDescr.getConfiguration().getParams();
        } catch (TransformerFactoryConfigurationError e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            System.setProperty(conSystemPropertyUSER_DIR, strUserDir);
        }
        return objParams;
    }

    public void showAllImportJobParams(final String xmlFilename) {
        try {
            jobParameterShell = new Shell(ErrorLog.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER | SWT.RESIZE);
            jobParameterShell.addShellListener(new ShellAdapter() {

                @Override
                public void shellClosed(final ShellEvent e) {
                    if (!closeDialog) {
                        close();
                    }
                    e.doit = jobParameterShell.isDisposed();
                }
            });
            jobParameterShell.setImage(ResourceManager.getImageFromResource("/sos/scheduler/editor/editor.png"));
            java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            jobParameterShell.setBounds((screen.width - jobParameterShell.getBounds().width) / 2, 
                    (screen.height - jobParameterShell.getBounds().height) / 2, 1, jobParameterShell.getBounds().height);
            final GridLayout gridLayout = new GridLayout();
            jobParameterShell.setLayout(gridLayout);
            String step = " ";
            if ("yes".equalsIgnoreCase(Utils.getAttributeValue("order", joblistener.getJob()))) {
                step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step3of9.label();
            } else {
                step += SOSJOEMessageCodes.JOE_M_JobAssistent_Step3of8.label();
            }
            jobParameterShell.setText(SOSJOEMessageCodes.JOE_M_JobAssistent_JobParameter.label() + step);
            Label nameLabel;
            final Group textParameterGroup = new Group(jobParameterShell, SWT.NONE);
            textParameterGroup.setText(SOSJOEMessageCodes.JOE_G_JobAssistent_ParamGroup.params(Utils.getAttributeValue(PARAM_NAME, joblistener.getJob())));
            final GridData gridData_3 = new GridData(GridData.FILL, GridData.FILL, true, true);
            gridData_3.minimumWidth = -1;
            textParameterGroup.setLayoutData(gridData_3);
            final GridLayout gridLayout_3 = new GridLayout();
            gridLayout_3.marginTop = 5;
            gridLayout_3.marginWidth = 10;
            gridLayout_3.marginRight = 10;
            gridLayout_3.marginLeft = 10;
            gridLayout_3.marginHeight = 10;
            gridLayout_3.marginBottom = 10;
            gridLayout_3.numColumns = 5;
            textParameterGroup.setLayout(gridLayout_3);
            final Composite composite_3 = SOSJOEMessageCodes.JOE_Composite1.Control(new Composite(textParameterGroup, SWT.NONE));
            final GridData gridData_4 = new GridData(GridData.FILL, GridData.END, true, false, 5, 1);
            gridData_4.minimumHeight = 30;
            composite_3.setLayoutData(gridData_4);
            final GridLayout gridLayout_1 = new GridLayout();
            gridLayout_1.numColumns = 5;
            composite_3.setLayout(gridLayout_1);
            final Composite composite = SOSJOEMessageCodes.JOE_Composite2.Control(new Composite(composite_3, SWT.NONE));
            composite.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, true));
            final GridLayout gridLayout_4 = new GridLayout();
            gridLayout_4.marginWidth = 0;
            composite.setLayout(gridLayout_4);
            butCancel = SOSJOEMessageCodes.JOE_B_JobAssistent_Cancel.Control(new Button(composite, SWT.NONE));
            butCancel.setLayoutData(new GridData());
            butCancel.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    close();
                }
            });
            final Composite composite_1 = SOSJOEMessageCodes.JOE_Composite3.Control(new Composite(composite_3, SWT.NONE));
            composite_1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
            final GridLayout gridLayout_2 = new GridLayout();
            gridLayout_2.numColumns = 2;
            gridLayout_2.marginWidth = 0;
            composite_1.setLayout(gridLayout_2);
            showButton = SOSJOEMessageCodes.JOE_B_JobAssistent_Show.Control(new Button(composite_1, SWT.NONE));
            showButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
            showButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    Utils.showClipboard(Utils.getElementAsString(joblistener.getJob()), jobParameterShell, false, null, false, null, false);
                }
            });
            if (assistentType == JOEConstants.JOB) {
                showButton.setVisible(false);
            }
            butFinish = SOSJOEMessageCodes.JOE_B_JobAssistent_Finish.Control(new Button(composite_1, SWT.NONE));
            butFinish.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
            butFinish.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (assistentType == JOEConstants.PARAMETER) {
                        HashMap<String, String> h = new HashMap<String, String>();
                        for (int i = 0; i < tParameter.getItemCount(); i++) {
                            TableItem item = tParameter.getItem(i);
                            h.put(item.getText(0), item.getText(2));
                        }
                        tParameter.removeAll();
                        paramListener.fillParams(tParameter);
                        for (int i = 0; i < tParameter.getItemCount(); i++) {
                            TableItem item = tParameter.getItem(i);
                            if (h.get(item.getText(0)) != null) {
                                item.setText(2, h.get(item.getText(0)));
                            }
                        }
                        if (detailForm != null) {
                            detailForm.onJobAssistentImportJobParamsFormClose();
                        }
                    } else if (assistentType == JOEConstants.JOB || assistentType == JOEConstants.JOB_WIZARD) {
                        if (jobForm != null) {
                            jobForm.initForm();
                        }
                        if (jobDocForm != null) {
                            jobDocForm.initForm();
                        }
                    } else if (assistentType == JOEConstants.JOB_CHAINS || assistentType == JOEConstants.JOBS) {
                        if (jobname != null) {
                            jobname.setText(Utils.getAttributeValue(PARAM_NAME, joblistener.getJob()));
                        }
                        JobsListener listener = new JobsListener(dom, update);
                        listener.newImportJob(joblistener.getJob(), assistentType);
                    }
                    if (Options.getPropertyBoolean("editor.job.show.wizard")) {
                        Utils.showClipboard(Utils.getElementAsString(joblistener.getJob()), jobParameterShell, false, null, false, null, true);
                    }
                    jobParameterShell.dispose();
                }
            });
            butBack = SOSJOEMessageCodes.JOE_B_JobAssistent_Back.Control(new Button(composite_3, SWT.NONE));
            butBack.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
            butBack.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    JobAssistentImportJobsForm importJobs = new JobAssistentImportJobsForm(dom, update, assistentType);
                    if (jobname != null) {
                        importJobs.setJobname(jobname);
                    }
                    importJobs.setBackUpJob(jobBackUp, jobForm);
                    importJobs.showAllImportJobs(joblistener);
                    closeDialog = true;
                    jobParameterShell.dispose();
                }
            });
            butBack.setEnabled(false);
            butNext = SOSJOEMessageCodes.JOE_B_JobAssistent_Next.Control(new Button(composite_3, SWT.NONE));
            butNext.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
            butNext.setFocus();
            butNext.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
            butNext.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    Utils.startCursor(jobParameterShell);
                    if (assistentType != JOEConstants.JOB) {
                        JobAssistentTasksForm tasks = new JobAssistentTasksForm(dom, update, joblistener.getJob(), assistentType);
                        tasks.showTasksForm();
                        if (jobname != null) {
                            tasks.setJobname(jobname);
                        }
                        tasks.setBackUpJob(jobBackUp, jobForm);
                    }
                    closeDialog = true;
                    Utils.startCursor(jobParameterShell);
                    jobParameterShell.dispose();
                }
            });
            butNext.setEnabled(false);
            if (assistentType == JOEConstants.JOB || assistentType == JOEConstants.PARAMETER) {
                butNext.setEnabled(false);
                butBack.setEnabled(false);
            } else {
                butNext.setEnabled(true);
                butBack.setEnabled(true);
            }
            Utils.createHelpButton(composite_3, "JOE_M_JobAssistentImportJobParamsForm_Help.label", jobParameterShell);
            final Label label_1 = new Label(textParameterGroup, SWT.BORDER);
            final GridData gridData_6_1 = new GridData(GridData.FILL, GridData.BEGINNING, false, false, 5, 1);
            gridData_6_1.heightHint = 0;
            label_1.setLayoutData(gridData_6_1);
            nameLabel = SOSJOEMessageCodes.JOE_L_Name.Control(new Label(textParameterGroup, SWT.NONE));
            nameLabel.setLayoutData(new GridData());
            txtName = SOSJOEMessageCodes.JOE_T_JobAssistent_Name.Control(new Text(textParameterGroup, SWT.BORDER));
            txtName.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    if (butApply != null) {
                        butApply.setEnabled(!txtName.getText().isEmpty());
                    }
                }
            });
            txtName.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR && !"".equals(txtName.getText())) {
                        addParam();
                    }
                }
            });
            final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData.widthHint = 168;
            txtName.setLayoutData(gridData);
            txtName.setText("");
            txtName.setFocus();
            final Label lblTitle = SOSJOEMessageCodes.JOE_L_Value.Control(new Label(textParameterGroup, SWT.NONE));
            final GridData gridData_6 = new GridData(GridData.END, GridData.CENTER, false, false);
            gridData_6.widthHint = 41;
            lblTitle.setLayoutData(gridData_6);
            lblTitle.setAlignment(SWT.RIGHT);
            txtValue = SOSJOEMessageCodes.JOE_T_JobAssistent_Value.Control(new Text(textParameterGroup, SWT.BORDER));
            txtValue.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    butApply.setEnabled(!txtName.getText().isEmpty());
                }
            });
            txtValue.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.keyCode == SWT.CR && !"".equals(txtName.getText().trim())) {
                        addParam();
                    }
                }
            });
            final GridData gridData_10 = new GridData(GridData.FILL, GridData.CENTER, true, false);
            gridData_10.widthHint = 175;
            txtValue.setLayoutData(gridData_10);
            butApply = SOSJOEMessageCodes.JOE_B_JobAssistent_Apply.Control(new Button(textParameterGroup, SWT.NONE));
            butApply.setEnabled(false);
            butApply.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butApply.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    addParam();
                }
            });
            tableDescParameters = SOSJOEMessageCodes.JOE_Tbl_JobAssistent_DescParams.Control(new Table(textParameterGroup, SWT.MULTI | SWT.FULL_SELECTION
                    | SWT.BORDER));
            tableDescParameters.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDoubleClick(final MouseEvent e) {
                    addParams();
                }
            });
            tableDescParameters.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    if (tableDescParameters.getSelectionCount() > -1) {
                        String s = tableDescParameters.getSelection()[0].getData(KEY_PARAMETER_DESCRIPTION_ + Options.getLanguage()) != null 
                                ? tableDescParameters.getSelection()[0].getData(KEY_PARAMETER_DESCRIPTION_+ Options.getLanguage()).toString() : "";
                        s = s.replaceAll("\\<.*?>", "");
                        txtDescription.setText(s);
                    }
                }
            });
            tableDescParameters.setLinesVisible(true);
            tableDescParameters.setHeaderVisible(true);
            final GridData gridData2 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
            gridData2.widthHint = 245;
            tableDescParameters.setLayoutData(gridData2);
            final TableColumn newColumnTableColumn = SOSJOEMessageCodes.JOE_TCl_JobAssistent_NameColumn.Control(new TableColumn(tableDescParameters, SWT.NONE));
            newColumnTableColumn.setWidth(122);
            final TableColumn newColumnTableColumn_1 = SOSJOEMessageCodes.JOE_TCl_JobAssistent_ValueColumn.Control(new TableColumn(tableDescParameters, SWT.NONE));
            newColumnTableColumn_1.setWidth(145);
            final Composite composite_2 = SOSJOEMessageCodes.JOE_Composite4.Control(new Composite(textParameterGroup, SWT.NONE));
            final GridData gridData_11 = new GridData(GridData.CENTER, GridData.CENTER, false, false);
            gridData_11.widthHint = 49;
            composite_2.setLayoutData(gridData_11);
            composite_2.setLayout(new GridLayout());
            butPut = SOSJOEMessageCodes.JOE_B_JobAssistent_Put.Control(new Button(composite_2, SWT.NONE));
            butPut.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    addParams();
                }
            });
            final GridData gridData_13 = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData_13.widthHint = 33;
            butPut.setLayoutData(gridData_13);
            butPutAll = SOSJOEMessageCodes.JOE_B_JobAssistent_PutAll.Control(new Button(composite_2, SWT.NONE));
            butPutAll.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    for (int i = 0; i < tableDescParameters.getItemCount(); i++) {
                        paramListener.saveParameter(tblSelectedParams, tableDescParameters.getItem(i).getText(0), tableDescParameters.getItem(i).getText(1), 
                                tableDescParameters.getItem(i).getData(KEY_PARAMETER_DESCRIPTION_DE) != null 
                                ? tableDescParameters.getItem(i).getData(KEY_PARAMETER_DESCRIPTION_DE).toString() : "", 
                                tableDescParameters.getItem(i).getData(KEY_PARAMETER_DESCRIPTION_EN) != null 
                                ? tableDescParameters.getItem(i).getData(KEY_PARAMETER_DESCRIPTION_EN).toString() : "", 
                                tableDescParameters.getItem(i).getBackground().equals(Options.getRequiredColor()));
                    }
                    tableDescParameters.removeAll();
                    butApply.setEnabled(false);
                }
            });
            butPutAll.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            butRemove = SOSJOEMessageCodes.JOE_B_JobAssistent_Remove.Control(new Button(composite_2, SWT.NONE));
            butRemove.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    removeParams();
                }
            });
            final GridData gridData_12 = new GridData(GridData.FILL, GridData.CENTER, false, false);
            gridData_12.widthHint = 29;
            butRemove.setLayoutData(gridData_12);
            butRemoveAll = SOSJOEMessageCodes.JOE_B_JobAssistent_RemoveAll.Control(new Button(composite_2, SWT.NONE));
            butRemoveAll.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    String remItem = null;
                    ArrayList<HashMap<String, Object>> listOfParams = new ArrayList<HashMap<String, Object>>();
                    for (int i = 0; i < tblSelectedParams.getItemCount(); i++) {
                        TableItem item = tblSelectedParams.getItem(i);
                        if (item.getBackground().equals(Options.getRequiredColor())) {
                            remItem = (remItem != null ? remItem : "") + "\n\t" + item.getText(0);
                            HashMap<String, Object> h = new HashMap<String, Object>();
                            h.put(PARAM_NAME, item.getText(0));
                            h.put(PARAM_ATTRIBUTE_DEFAULT_VALUE, item.getText(1) != null ? item.getText(1) : "");
                            h.put(PARAM_ATTRIBUTE_REQUIRED, TRUE);
                            h.put(KEY_PARAMETER_DESCRIPTION_DE, item.getData(KEY_PARAMETER_DESCRIPTION_DE) != null 
                                    ? item.getData(KEY_PARAMETER_DESCRIPTION_DE) : "");
                            h.put(KEY_PARAMETER_DESCRIPTION_EN, item.getData(KEY_PARAMETER_DESCRIPTION_EN) != null 
                                    ? item.getData(KEY_PARAMETER_DESCRIPTION_EN) : "");
                            listOfParams.add(h);
                        } else {
                            TableItem itemDP = new TableItem(tableDescParameters, SWT.NONE);
                            itemDP.setText(0, item.getText(0));
                            itemDP.setText(1, item.getText(1));
                            itemDP.setData(KEY_PARAMETER_DESCRIPTION_DE, item.getData(KEY_PARAMETER_DESCRIPTION_DE));
                            itemDP.setData(KEY_PARAMETER_DESCRIPTION_EN, item.getData(KEY_PARAMETER_DESCRIPTION_EN));
                        }
                    }
                    txtName.setFocus();
                    paramListener.fillParams(listOfParams, tblSelectedParams, true);
                    if (remItem != null) {
                        ErrorLog.message(jobParameterShell, SOSJOEMessageCodes.JOE_M_JobAssistent_ParamsRequired.params(remItem), SWT.ICON_WARNING | SWT.OK);
                    }
                    tblSelectedParams.redraw();
                    tblSelectedParams.deselectAll();
                    tableDescParameters.deselectAll();
                }
            });
            butRemoveAll.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));
            tblSelectedParams = SOSJOEMessageCodes.JOE_Tbl_JobAssistent_SelectedParams.Control(new Table(textParameterGroup, SWT.MULTI | SWT.FULL_SELECTION
                    | SWT.BORDER));
            tblSelectedParams.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDoubleClick(final MouseEvent e) {
                    removeParams();
                }
            });
            final GridData gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
            gridData_1.heightHint = 135;
            gridData_1.widthHint = 185;
            tblSelectedParams.setLayoutData(gridData_1);
            tblSelectedParams.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

                @Override
                public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                    if (butApply.isEnabled()) {
                        int ok = ErrorLog.message(SOSJOEMessageCodes.JOE_M_ApplyChanges.label(), SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
                        if (ok == SWT.YES) {
                            addParam();
                            return;
                        }
                    }
                    if (tblSelectedParams.getSelectionCount() > -1) {
                        txtName.setText(tblSelectedParams.getSelection()[0].getText(0));
                        txtValue.setText(tblSelectedParams.getSelection()[0].getText(1));
                        txtDescription.setText(tblSelectedParams.getSelection()[0].getData(KEY_PARAMETER_DESCRIPTION_ + Options.getLanguage()) != null 
                                ? tblSelectedParams.getSelection()[0].getData(KEY_PARAMETER_DESCRIPTION_ + Options.getLanguage()).toString() : "");
                        txtValue.setFocus();
                        butApply.setEnabled(false);
                    }
                }
            });
            tblSelectedParams.setLayoutDeferred(true);
            tblSelectedParams.setLinesVisible(true);
            tblSelectedParams.setHeaderVisible(true);
            final TableColumn colName = SOSJOEMessageCodes.JOE_TCl_JobAssistent_NameColumn.Control(new TableColumn(tblSelectedParams, SWT.NONE));
            colName.setWidth(119);
            final TableColumn colValue = SOSJOEMessageCodes.JOE_TCl_JobAssistent_ValueColumn.Control(new TableColumn(tblSelectedParams, SWT.NONE));
            colValue.setWidth(212);
            txtDescription = new Text(textParameterGroup, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.H_SCROLL);
            final GridData gridData_2 = new GridData(GridData.FILL, GridData.CENTER, false, false, 5, 1);
            gridData_2.heightHint = 108;
            txtDescription.setLayoutData(gridData_2);
            txtDescription.setBackground(SWTResourceManager.getColor(247, 247, 247));
            if (!"..".equals(xmlFilename)) {
                listOfParams = this.parseDocuments(xmlFilename, "");
            }
            fillTable(listOfParams);
            jobParameterShell.layout();
            jobParameterShell.pack();
            jobParameterShell.open();
        } catch (Exception e) {
            new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
        }
    }

    private boolean isInList(final HashMap item, final ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            HashMap h = (HashMap) list.get(i);
            if (h.get("name").equals(item.get("name"))) {
                return true;
            }
        }
        return false;
    }

    public void fillTable(final ArrayList list) throws Exception {
        ArrayList<HashMap<String, Object>> listOfRequired = new ArrayList<HashMap<String, Object>>();
        try {
            HashMap h = new HashMap();
            tableDescParameters.removeAll();
            ArrayList<HashMap<String, Object>> jobP = getParameters();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    h = (HashMap) list.get(i);
                    if (!isInList(h, jobP) && h.get(PARAM_ATTRIBUTE_REQUIRED) != null && TRUE.equals(h.get(PARAM_ATTRIBUTE_REQUIRED))) {
                        listOfRequired.add(h);
                    }
                }
            }
            paramListener.fillParams(jobP, tblSelectedParams, true);
            for (int i = 0; i < listOfRequired.size(); i++) {
                h = listOfRequired.get(i);
                if (h.get(PARAM_NAME) != null && paramListener.existsParams(h.get(PARAM_NAME).toString(), tblSelectedParams, null) == null) {
                    TableItem item = new TableItem(tableDescParameters, SWT.NONE);
                    item.setBackground(Options.getRequiredColor());
                    item.setChecked(true);
                    item.setText(0, h.get(PARAM_NAME) != null ? h.get(PARAM_NAME).toString() : "");
                    item.setText(1, h.get(PARAM_ATTRIBUTE_DEFAULT_VALUE) != null ? h.get(PARAM_ATTRIBUTE_DEFAULT_VALUE).toString() : "");
                    String desc_de = h.get(KEY_PARAMETER_DESCRIPTION_DE) != null ? h.get(KEY_PARAMETER_DESCRIPTION_DE).toString() : "";
                    item.setData(KEY_PARAMETER_DESCRIPTION_DE, desc_de);
                    String desc_en = h.get(KEY_PARAMETER_DESCRIPTION_EN) != null ? h.get(KEY_PARAMETER_DESCRIPTION_EN).toString() : "";
                    item.setData(KEY_PARAMETER_DESCRIPTION_EN, desc_en);
                }
            }
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    h = (HashMap) list.get(i);
                    if (h.get(PARAM_ATTRIBUTE_REQUIRED) == null || FALSE.equals(h.get("required")) && h.get(PARAM_NAME) != null 
                            && paramListener.existsParams(h.get("name").toString(), tblSelectedParams, null) == null) {
                        TableItem item = new TableItem(tableDescParameters, SWT.NONE);
                        item.setBackground(null);
                        item.setChecked(true);
                        item.setText(0, h.get(PARAM_NAME) != null ? h.get(PARAM_NAME).toString() : "");
                        item.setText(1, h.get(PARAM_ATTRIBUTE_DEFAULT_VALUE) != null ? h.get(PARAM_ATTRIBUTE_DEFAULT_VALUE).toString() : "");
                        String desc_de = h.get(KEY_PARAMETER_DESCRIPTION_DE) != null ? h.get(KEY_PARAMETER_DESCRIPTION_DE).toString() : "";
                        item.setData(KEY_PARAMETER_DESCRIPTION_DE, desc_de);
                        String desc_en = h.get(KEY_PARAMETER_DESCRIPTION_EN) != null ? h.get(KEY_PARAMETER_DESCRIPTION_EN).toString() : "";
                        item.setData(KEY_PARAMETER_DESCRIPTION_EN, desc_en);
                    }
                }
            }
        } catch (Exception e) {
            new ErrorLog(SOSJOEMessageCodes.JOE_E_0002.params(sos.util.SOSClassUtil.getMethodName()), e);
            throw new Exception(SOSJOEMessageCodes.JOE_M_0010.params(e.toString()));
        }
    }

    public void setToolTipText() {
        //
    }

    public ArrayList<HashMap<String, Object>> getParameters() {
        Element params = joblistener.getJob().getChild("params");
        ArrayList<HashMap<String, Object>> listOfParams = new ArrayList<HashMap<String, Object>>();
        List param = null;
        if (params != null) {
            param = params.getChildren("param");
        }
        if (param != null) {
            for (int i = 0; i < param.size(); i++) {
                Element el = (Element) param.get(i);
                HashMap<String, Object> h = new HashMap<String, Object>();
                h.put(PARAM_NAME, Utils.getAttributeValue(PARAM_NAME, el));
                h.put(PARAM_ATTRIBUTE_DEFAULT_VALUE, Utils.getAttributeValue("value", el));
                h.put("description_de", paramListener.getParameterDescription(Utils.getAttributeValue(PARAM_NAME, el), "de"));
                h.put("description_en", paramListener.getParameterDescription(Utils.getAttributeValue(PARAM_NAME, el), "en"));
                listOfParams.add(h);
            }
        }
        return listOfParams;
    }

    private boolean existItem(final String name, final Table tab) {
        for (int i = 0; tab != null && i < tab.getItemCount(); i++) {
            TableItem item = tab.getItem(i);
            if (item.getText(0) != null && item.getText(0).equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void close() {
        int cont = ErrorLog.message(jobParameterShell, SOSJOEMessageCodes.JOE_M_JobAssistent_CancelWizard.label(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        if (cont == SWT.OK) {
            if (jobBackUp != null) {
                joblistener.getJob().setContent(jobBackUp.cloneContent());
                List attr = ((Element) jobBackUp.clone()).getAttributes();
                joblistener.getJob().getAttributes().clear();
                for (int i = 0; i < attr.size(); i++) {
                    org.jdom.Attribute at = (org.jdom.Attribute) attr.get(i);
                    joblistener.getJob().setAttribute(at.getName(), at.getValue());
                }
            }
            jobParameterShell.dispose();
        }
    }

    private void addParam() {
        if (txtName.getText() != null && txtName.getText().trim().isEmpty()) {
            ErrorLog.message(jobParameterShell, SOSJOEMessageCodes.JOE_M_JobAssistent_NoParamName.label(), SWT.ICON_WARNING | SWT.OK);
            txtName.setFocus();
            return;
        }
        paramListener.saveParameter(tblSelectedParams, txtName.getText(), txtValue.getText());
        txtName.setText("");
        txtValue.setText("");
        butApply.setEnabled(false);
        tblSelectedParams.deselectAll();
        txtName.setFocus();
    }

    public void setJobname(final Combo jobname) {
        this.jobname = jobname;
    }

    public void setJobForm(final ScriptJobMainForm jobForm_) {
        if (jobForm_ != null) {
            jobForm = jobForm_;
        }
    }

    public void setJobForm(final JobDocumentationForm jobDocForm_) {
        if (jobDocForm_ != null) {
            jobDocForm = jobDocForm_;
        }
    }

    public void setBackUpJob(final Element backUpJob, final ScriptJobMainForm jobForm_) {
        if (backUpJob != null) {
            jobBackUp = (Element) backUpJob.clone();
        }
        if (jobForm_ != null) {
            jobForm = jobForm_;
        }
    }

    private void addParams() {
        String strParamName = "";
        String strParamDefaultValue = "";
        if (tableDescParameters.getSelectionIndex() > -1) {
            String existParams = "";
            for (int i = 0; i < tableDescParameters.getSelectionIndices().length; i++) {
                TableItem item = tableDescParameters.getItem(tableDescParameters.getSelectionIndices()[i]);
                strParamName = item.getText(0);
                if (!existItem(strParamName, tblSelectedParams)) {
                    strParamName = item.getText(0);
                    strParamDefaultValue = item.getText(1);
                    paramListener.saveParameter(tblSelectedParams, strParamName, strParamDefaultValue, item.getData(KEY_PARAMETER_DESCRIPTION_DE) != null 
                            ? item.getData(KEY_PARAMETER_DESCRIPTION_DE).toString() : "", 
                            item.getData(KEY_PARAMETER_DESCRIPTION_EN) != null ? item.getData(KEY_PARAMETER_DESCRIPTION_EN).toString() : "", 
                            item.getBackground().equals(Options.getRequiredColor()));
                } else {
                    existParams = existParams + strParamName + "\n";
                }
                if (!existParams.isEmpty()) {
                    ErrorLog.message(jobParameterShell, SOSJOEMessageCodes.JOE_M_JobAssistent_ParamsExist.params(existParams), SWT.ICON_WARNING | SWT.OK);
                }
            }
            tableDescParameters.remove(tableDescParameters.getSelectionIndices());
        } else {
            ErrorLog.message(jobParameterShell, SOSJOEMessageCodes.JOE_M_JobAssistent_NoParamsSelected.label(), SWT.ICON_WARNING | SWT.OK);
        }
        tableDescParameters.deselectAll();
        tblSelectedParams.deselectAll();
        txtName.setText(strParamName);
        txtValue.setText(strParamDefaultValue);
        txtValue.setFocus();
        butApply.setEnabled(false);
    }

    private void removeParams() {
        if (tblSelectedParams.getSelectionIndex() > -1) {
            String remItem = "";
            int i = 0;
            while (tblSelectedParams.getSelection().length > 0) {
                TableItem item = tblSelectedParams.getItem(tblSelectedParams.getSelectionIndices()[i]);
                if (item.getBackground().equals(Options.getRequiredColor())) {
                    remItem = remItem + "\n\t" + item.getText(0);
                    tblSelectedParams.deselect(tblSelectedParams.getSelectionIndices()[i]);
                } else {
                    TableItem itemDesc = new TableItem(tableDescParameters, SWT.NONE);
                    itemDesc.setText(0, item.getText(0));
                    itemDesc.setText(1, item.getText(1));
                    itemDesc.setData(KEY_PARAMETER_DESCRIPTION_DE, item.getData(KEY_PARAMETER_DESCRIPTION_DE));
                    itemDesc.setData(KEY_PARAMETER_DESCRIPTION_EN, item.getData(KEY_PARAMETER_DESCRIPTION_EN));
                    paramListener.deleteParameter(tblSelectedParams, tblSelectedParams.getSelectionIndices()[i]);
                }
            }
            if (!remItem.isEmpty()) {
                ErrorLog.message(jobParameterShell, SOSJOEMessageCodes.JOE_M_JobAssistent_ParamsRequired.params(remItem), SWT.ICON_WARNING | SWT.OK);
            }
            tblSelectedParams.remove(tblSelectedParams.getSelectionIndices());
        } else {
            ErrorLog.message(jobParameterShell, SOSJOEMessageCodes.JOE_M_JobAssistent_NoRemParamsSelected.label(), SWT.ICON_WARNING | SWT.OK);
        }
        tblSelectedParams.deselectAll();
        tableDescParameters.deselectAll();
        butApply.setEnabled(false);
        txtName.setText("");
        txtValue.setText("");
        txtName.setFocus();
    }

    public void setDetailsRefresh(final Text refreshDetailsText_) {
        refreshDetailsText = refreshDetailsText_;
    }

    public void setDetailForm(DetailForm detailForm) {
        this.detailForm = detailForm;
    }

}